angular.module("starter.controllers", ["ngAnimate", "highcharts-ng", "smart-table", "ui.bootstrap", "ui.uploader", "angular.qrcode", "caixa.analytics", "angucomplete-alt"])
    .run(function ($rootScope, $state, $location, $filter, $timeout, $translate, Utils, Error, Log, DossieService, Analytics, CentralService, WebResource, Alert, Feedback, NativeInterface, WebServiceX, SicpfService, Device) {
        Log.info("controller.run()");
        
        $rootScope.breadcrumb = {
        		"pesquisa" : 'Pesquisa',
        		"cadastral" : 'Cadastral',
        		"digitalizacao" : 'Digitalização',
        		"documentos" : 'de Documentos',
        		"senha" : 'Senha do',
        		"cliente" : 'Cliente',
        		"dados" : 'Dados',
        		"declarados" : 'Declarados',
        		"dossie" : 'Dossiê',
        		"criado" : 'Criado'
        }
        
        
        $rootScope.headers = {
            "authCode": 100
        }
        
        $rootScope.goBack = function () {
        	DossieService.goBack();
        }
        
        
        $rootScope.error = null;
        $rootScope.showError = false; 
        
        $rootScope.hideAlert = function () {
        	 $rootScope.error = null;
             $rootScope.showError = false;
        }
        
        $rootScope.initScroll = function () {
     		document.body.scrollTop = 0;
       }
        
        $rootScope.putError = function (error) {
        	$rootScope.$broadcast(FINISH_LOADING);
        	if(error.data != null) {
        		$rootScope.error = error.data.observacao ? error.data.observacao : (error.data.mensagem + ' - ' + error.data.detalhe) ;
        	} else {
        		$rootScope.error = 'Ocorreu uma falha na comunicação com o back-end.'
        	}
			$rootScope.showError = true;
        }
        
        $rootScope.putErrorString = function (msg) {
        	$rootScope.$broadcast(FINISH_LOADING);
    		$rootScope.error = msg;
    		$rootScope.showError = true;
        }

        $rootScope.location = $location;

        $rootScope.$watch('location.url()', getTitle);

        //Função para pegar o titulo da pagina
        function getTitle() {
            $rootScope.pageTitle = $location.url().substring(1);
        };
        
        $rootScope.$on('startLoading' ,function () {
        	var div = $('#'+LOADING_ID);
        	if (div) {
        		div.css('display', 'block');
        		div.css('opacity', '0.7');
        	}
        })
        
        $rootScope.$on('finishLoading' , function () {
        	var div = $('#'+LOADING_ID);
        	if (div) {
        		div.fadeOut(1000, "linear");
        	}
        })

        //Funções para saber se a pagina carregou ou não
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            $rootScope.stateIsLoading = true;
        })

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            $(".page-loader").fadeOut(1000, "linear");
        })
        
    	$rootScope.digitalizarDoc = function (cpf_cliente, tipoDocumento, $http, callAfterScanner, callAposExtracao) {		
			var selectDevice = $rootScope.project.customersummary.device;
			var showdevices = $rootScope.project.customersummary.showdevices;
			if(selectDevice == -1 && $rootScope.project.customersummary.showdevices) {
				$rootScope.$broadcast(FINISH_LOADING);
				$('#div-dispositivo').addClass('has-error').addClass('validation-error');
				$('#scan-dispositivo').addClass('validation-error');
				Alert.showMessage('Error', 'Selecione o driver de seu scanner!');
			} else {	
         		var obj = Device.resultscanner(selectDevice, showdevices, $rootScope);
        		if (obj != undefined && obj.ok != false) {
					var listaImagens = obj.scannedImageList;	
					callAfterScanner(cpf_cliente, listaImagens, tipoDocumento, $http, callAposExtracao);	
        		} else {
    				$rootScope.$broadcast(FINISH_LOADING);
        			Alert.showMessage('Error', 'Erro ao obter Dados do Scanner!');
        		}
			}
    	};
            
            /* Mock para Digitalização do Scanner*/
//    		$rootScope.digitalizarDoc = function (cpf_cliente, tipoDocumento, $http, callAfterScanner, callAposExtracao) {		
//    					
//    					var caminhoJsonMock;
//    					
//    					if(tipoDocumento == CLASS_DOC_IDENT)
//    						caminhoJsonMock = './json/imagem-rg-scanner-mock.json';
//    					
//    					if(tipoDocumento == CLASS_CNH)
//    						caminhoJsonMock = './json/imagem-cnh-scanner-mock.json';
//    					
//    					if(tipoDocumento == CLASS_CONTRACHEQUE)
//    						caminhoJsonMock = './json/imagem-renda-scanner-mock.json';
//    					
//    					if(tipoDocumento == CLASS_CONTA_ENERGIA)
//    						caminhoJsonMock = './json/imagem-res-scanner-mock.json';
//    					
//    					if(tipoDocumento == CARTAO_ASSINATURA)
//    						caminhoJsonMock = './json/imagem-assinatura-scanner-mock.json';
//    					
//    					$.getJSON(caminhoJsonMock, function(mockRgScanner) {
//    						var listaImagens = mockRgScanner.scannedImageList;						
//    						callAfterScanner(cpf_cliente, listaImagens, tipoDocumento, $http, callAposExtracao);						
//    						
//    					});  
//    				
//    			};
    			
    			
    			$rootScope.extrairDados = function (cpf, imagensScanner, tipoDocumento, $http, callAposExtracao) {			
    				
    				var obj = { integracao : INTEGRACAO, 
    							formato : decideFormat(imagensScanner[0].name), 
    							tipo_documento : tipoDocumento, 
    							imagens : [imagensScanner[0].base64],
    							cpf_cliente : cpf};
    			
    				$rootScope.currentDocument = {                                                          
    					image: obj.imagens[0],
    					format: obj.formato,
    					mimeType: getMimeType(obj.formato)
    				};
    				
    				$http({
    			        url: URL_SIMTR + SERVICO_EXTRACAO,
    			        method: "POST",
    			        data: obj
    			    }).then(function(res) {
    					DossieService.setExtracao(res.data);
    					callAposExtracao();
    		    		$rootScope.$broadcast(FINISH_LOADING);
    			    }, 
    			    function(error) { // optional
    					if(error.status && error.status === 401){
    						$rootScope.putErrorString("Erro de Autorização para requisição do serviço.");
    					}else if(error.status && error.status === 500){
    						$rootScope.putErrorString("Serviço temporariamente indisponível. Por favor, tente novamente mais tarde.");
    					}else{
    						$rootScope.putError(error);
    					}
    		     		$rootScope.$broadcast(FINISH_LOADING);
    			    });	    				
    				
    			};
    			
    			$rootScope.apresentarModalCancelarAtendimento = function () {			
    				$("#modalCancelar").modal();
    			};
    			
    			$rootScope.auditarAtendimento = function (tela, $http, acaoAtendimento, retorno) {
    				
	        		$rootScope.go(retorno);
	        		$rootScope.$broadcast(FINISH_LOADING);
	        
    			}
    			
    			
	/*	$rootScope.auditarAtendimento = function (tela, $http, acaoAtendimento, retorno) {
    				
    				//TODO Implementar atendimento dA TABELA DO SIICO
    				
    				var unidade = angular.element(document.body).injector().get('KeycloakService').getUserSSO()['nu-lotacaofisica'];
    				var matricula = angular.element(document.body).injector().get('KeycloakService').getUserSSO()['preferred_username'];
	    			var obj = { cpf_cliente : SicpfService.getCpf(),  acao : acaoAtendimento, etapa : tela, unidade_operador : unidade, matricula_operador : matricula};
	        		$rootScope.$broadcast(START_LOADING);
	        		
	        		$http.post(URL_SIMTR + SERVICO_POST_ATENDIMENTO, obj)
	                .then(function (res) {
	                	$rootScope.go(retorno);
	        			$rootScope.$broadcast(FINISH_LOADING);
	        		}, function (error) {
	        			$rootScope.$broadcast(FINISH_LOADING);
	        			$rootScope.putError(error);
	        		}); 
    			} */
    			
    			
    			$rootScope.verificaUsuarioCadastradoSSO = function ($http, cpf, callAposVerificarUsuario){
    				
    				$rootScope.$broadcast(START_LOADING);
    				
    			  	$http.get(URL_SIMTR  + SERVICO_CONSULTA_USUARIO + cpf).then(function (res) {
    					
    					if(res.status == 200 || res.status == 204){
    						callAposVerificarUsuario(true);
    					}else{
    						$rootScope.putErrorString("Erro ao consultar Usuário no SSO.");
    						$rootScope.$broadcast(FINISH_LOADING); 
    					}   
    		    	}, function (error) {
    		    		 if (error.status == 404){
    		    			 callAposVerificarUsuario(false); 
    		    		 }else{
    		    			 $rootScope.putErrorString("Erro ao consultar Usuário no SSO.");
    		    			 $rootScope.$broadcast(FINISH_LOADING); 
    		    		 }
    		    	});    			
    				
    			};
    			
    			
    		$rootScope.validarSenahaSSO = function ($http, user, password, callSenhaValidada){
    				
    				$rootScope.$broadcast(START_LOADING);
    				
    				var obj = {
    						usuario : user,
    						senha : password
    				};
    				
    				$http.post(URL_SIMTR  + SERVICO_VALIDAR_SENHA, obj).then(function (res) {
    					
    					if(res.status == 200 || res.status == 204){
    						
    						res.status;
    						$rootScope.$broadcast(FINISH_LOADING);
    						callSenhaValidada();
    						
    					}else{
    						$rootScope.putErrorString("Senha Inválida!");
    					}   
    		    	}, function (error) {
    		    		if(error.status == 401){
    		    			$rootScope.putErrorString("Senha Inválida!");
    		    		}else{
    		    			 $rootScope.putErrorString("Erro ao Comunicar com o Serviço SSO.");
    		    		}
    		    	});    				
    				
    			};
    			
    			

        		$rootScope.cadastraSenhaSSO = function ($http, SicpfService, senha_user, email, callAposCadastrarUsuario){
        			$rootScope.$broadcast(START_LOADING);
        				var obj = {
        						usuario : SicpfService.getCpf(),
        						nome : SicpfService.getSicpf()['nome-contribuinte'],
        						dtnasc : SicpfService.getSicpf()['data-nascimento'].toLocaleString().substring(0, 10),
        						senha :  senha_user,
        						e_mail : email
        				};
        				
        				$http.post(URL_SIMTR  + SERVICO_CADASTRAR_USER_SSO , obj, 
        						{headers : {'Content-Type' : 'application/json' }}
        				      ).then(function (res) {
        		    		
        					if(res.status == 200 || res.status == 204){
        						$rootScope.emailClienteSSO = email;
        						callAposCadastrarUsuario();
        					}else{
        						$rootScope.putErrorString("Erro ao cadastrar Usuário no SSO.");
        						$rootScope.$broadcast(FINISH_LOADING);
        					}
        					
        		    	}, function (error) {
        		    		var msg = "Erro ao cadastrar Usuário no SSO. Motivo: "
        		    		if(error.data != null) {
        		        		msg += error.data.observacao ? error.data.observacao : error.data.mensagem ;
        		        	}
        		    		$rootScope.putErrorString(msg);
        		    		$rootScope.$broadcast(FINISH_LOADING);
        		    	});
        		
        			}; 
    		
    

        try {
//            Analytics.init(ANALYTICS_ID, APP_NAME);
            $rootScope.home = DEFAULT_VIEW;
            $rootScope.splash = 'splash';
            $rootScope.contextEditable = false;


            /* Central Caixa de Notificações - inicio */
            $rootScope.startMessageService = function (onsuccess, onfail) {
                Analytics.trackEvent('root', 'initialize');

                if (!$rootScope.isSubscriptionSupported()) return;

                var key = CENTRAL_ID;
                var user = null;
                if ($rootScope.user) {
                    user = $rootScope.user;
                }

                if (key != null) {
                    function inititalizeSuccess(isSubscribed) {
                        Analytics.trackEvent('root', 'initializeSuccess');
                        Log.info("Inicialização do serviço de notificações efetuado com sucesso");
                        if (onsuccess) onsuccess();
                    }
                    function inititalizeFail() {
                        Analytics.trackEvent('root', 'initializeFail');
                        Log.error("Falha ao inicializar serviço de mensagens");
                        if (onfail) onfail();
                    }
                    CentralService.initialize(key, user, inititalizeSuccess, inititalizeFail);
                }
            }
            $rootScope.subscribe = function ($event, silent, success, fail) {
                Analytics.trackEvent('root', 'subscribe');
                if ($event) {
                    Feedback.loading($event);
                }
                function subscribeSuccess(subscription) {
                    Analytics.trackEvent('root', 'subscribeSuccess');
                    Feedback.reset();
                    Log.info("Assinatura do canal efetuada com sucesso");
                    if (!silent) {
                        Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.assinatura.efetuada.com.sucesso'));
                    }
                    if (success) success();
                }
                function subscribeFail(message, error) {
                    Analytics.trackEvent('root', 'subscribeFail');
                    Feedback.reset();
                    Log.info("Falha ao assinar canal");
                    if (!message) message = $translate.instant('label.falha.ao.efetuar.assinatura');
                    if (!silent) {
                        Alert.showMessage($translate.instant('label.atencao'), message);
                    }
                    if (fail) fail();
                }
                CentralService.subscribe(subscribeSuccess, subscribeFail);
            }
            $rootScope.unsubscribe = function ($event, success, fail) {
                Analytics.trackEvent('root', 'unsubscribe');
                if ($event) {
                    Feedback.loading($event);
                }
                function subscribeSuccess(subscription) {
                    Analytics.trackEvent('root', 'unsubscribeSuccess');
                    Feedback.reset();
                    Log.info("Assinatura do canal cancelada com sucesso");
                    Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.cancelamento.efetuado.com.sucesso'));
                    if (success) success();
                }
                function subscribeFail(message, error) {
                    Analytics.trackEvent('root', 'unsubscribeFail');
                    Feedback.reset();
                    Log.info("Falha ao cancelar assinatura do canal");
                    if (fail) fail();
                }
                CentralService.unsubscribe(subscribeSuccess, subscribeFail);
            }
            $rootScope.isSubscribed = function () {
                var result = CentralService.isSubscribed();
                Analytics.trackEvent('root', 'isSubscribed:' + result);
                return result;
            }
            $rootScope.isSubscriptionSupported = function () {
                var result = CentralService.isSupported();
                Analytics.trackEvent('root', 'isSubscriptionSupported:' + result);
                return result;
            }
            $rootScope.isSubscriptionInititalized = function () {
                return CentralService.isInitialized();
            }
            $rootScope.isSubscriptionDev = function () {
                return CentralService.isDevMode();
            },
                $rootScope.setSubscriptionDev = function (value) {
                    CentralService.setDevMode(value);
                },
                $rootScope.showPush = function (push) {
                    Analytics.trackEvent('root', 'showPush');
                    if (!push) return;
                    $rootScope.message = push;

                    var modalmetric = Alert.showModal("resources/modalpush.html", null, $rootScope, true, true, "md", null, null);
                    modalmetric.result.then(function () {
                        Analytics.trackEvent('root', 'showPushOpen');
                        var url = $rootScope.message.url;
                        Log.info("opening url: " + url);
                        window.open($rootScope.message.url, '_blank');
                        modalmetric.dismiss();
                    }, function () {
                        Analytics.trackEvent('root', 'showPushClose');
                        Log.info("closing message");
                        modalmetric.dismiss();
                    });
                };
            CentralService.setEvent($rootScope.showPush);
            $rootScope.showInvite = function () {
                Analytics.trackEvent('root', 'showInvite');
                if ($rootScope.isSubscriptionSupported() == false) return;
                if ($rootScope.isSubscriptionInititalized() == false) return;

                var invite = NativeInterface.getPreference(APP_ID + ":invitePush", "true") == "true";
                if (invite == false) return;
                NativeInterface.setPreference(APP_ID + ':invitePush', false);

                $timeout(function () {
                    if ($rootScope.isSubscribed()) return;

                    var modalmetric = Alert.showModal("resources/modalinvite.html", null, $rootScope, false, 'static', "md", null, null);
                    modalmetric.result.then(function () {
                        Analytics.trackEvent('root', 'showInviteAccept');
                        Log.info("User accept");
                        modalmetric.dismiss();
                        $rootScope.subscribe(null, true);
                    }, function () {
                        Analytics.trackEvent('root', 'showInviteDeny');
                        Log.info("User refused");
                        modalmetric.dismiss();
                    });

                }, 15000);

            };
            /* Central Caixa de Notificações - fim */


            /* Central Caixa Email - inicio */
            $rootScope.subscribeEmail = function (success) {
                Analytics.trackEvent('root', 'subscribe email');

                var user = [];
                var email = { email: $rootScope.user };
                user.push(email);

                var request = {
                    app: CENTRAL_ID,
                    users: user
                };

                WebServiceX.create(URL_CAIXA_SICPU + "ws/email/subscribe/" + API_VERSAO, JSON.stringify(request), null)
                    .then(function (res) {
                        Feedback.reset();
                        if (!res.temErro) {
                            Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.assinatura.recebimento.email.realizada.com.sucesso'));
                            success();
                        } else if (res.temErro) {
                            Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                        }
                    }, function (xhr, status, err) {
                        var message = $translate.instant('label.falha.efetuar.inscricao.recebimento.email');
                        if (xhr && xhr.responseText) {
                            try {
                                var response = JSON.parse(xhr.responseText);
                                if (response && response.msgsErro && response.msgsErro.length > 0) {
                                    message = response.msgsErro[0];
                                }
                            } catch (ignore) {
                            }
                        }
                        Error.handler(message, err);
                        Feedback.reset();
                        $("#pnlConnecting").hide();
                    });
            }

            $rootScope.unsubscribeEmail = function (success, scope) {
                Analytics.trackEvent('root', 'unsubscribe email');

                var user = [];
                var email = { email: $rootScope.user };
                user.push(email);

                var request = {
                    app: CENTRAL_ID,
                    users: user
                };

                WebServiceX.create(URL_CAIXA_SICPU + "ws/email/unsubscribe/" + API_VERSAO, JSON.stringify(request), null)
                    .then(function (res) {
                        if (!res.temErro) {
                            scope.isSubscribedEmail = false;
                            Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.cancelamento.recebimento.email.realizado.sucesso'));
                            success();
                        } else if (res.temErro) {
                            Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                        }
                    }, function (xhr, status, err) {
                        var message = $translate.instant('label.falha.efetuar.cancelamento.recebimento.email');
                        if (xhr && xhr.responseText) {
                            try {
                                var response = JSON.parse(xhr.responseText);
                                if (response && response.msgsErro && response.msgsErro.length > 0) {
                                    message = response.msgsErro[0];
                                }
                            } catch (ignore) {
                            }
                        }
                        Error.handler(message, err);
                        Feedback.reset();
                        $("#pnlConnecting").hide();
                    });
            }
            /* Central Caixa Email - fim */

            $rootScope.sobreIsDev = null;
            function onSuccess() {
                $rootScope.sobreIsDev = $rootScope.isSubscriptionDev();
            }
            $rootScope.setDevMode = function (value) {
                if (value) {
                    function response(data) {
                        $rootScope.setSubscriptionDev(data);
                        onSuccess();
                    }
                    var devcode = Utils.getRandom(0, 999999);
                    Alert.showInputRequired($translate.instant('label.modo.desenvolvimento'), $translate.instant('label.informe.codigo.desenvolvimento'), devcode, response);
                } else {
                    $rootScope.setSubscriptionDev(null);
                    onSuccess();
                }
            }

            /*Highcharts.setOptions({
                global: {
                    useUTC: false
                },
                lang: {
                    months: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
                    shortMonths: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
                    weekdays: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sabado']
                }
            });*/

            $rootScope.currentview = {};
            $rootScope.documentos = {};
            
            $rootScope.project = PROJECT;
            $rootScope.theme = $rootScope.project.theme;
            $rootScope.contraste = $rootScope.project.contraste;
            $rootScope.navlock = false;
            $rootScope.contextEditable = false;
            $rootScope.platforms = Utils.getPlatforms();
            $rootScope.instanciaJboss = INSTANCIA_JBOSS;

            $rootScope.feedback = Analytics.isEnabled();
            $rootScope.openFeedback = function (group) {
                var modalmetric = Alert.showModal("resources/modalfeedback.html", null, $rootScope, true, true, "md", null, null);
                modalmetric.result.then(function () {
                    modalmetric.dismiss();
                }, function () {
                    modalmetric.dismiss();
                });
            }

            $rootScope.setPlatforms = function (platforms) {
                $rootScope.platforms = platforms;
            }
            $rootScope.setUser = function (value) {
                $rootScope.user = value;
                $rootScope.$apply();
            }
            $rootScope.setContext = function (value) {
                $rootScope.contextEditable = value;
                $rootScope.$apply();
            }
            $rootScope.setNavlock = function (value) {
                $rootScope.navlock = value;
            }
            $rootScope.isNavlock = function () {
                return $rootScope.navlock;
            }

            $rootScope.dateNow = function (format) {
                return $filter('date')(new Date(), format);
            }
            $rootScope.getCurrentState = function (state) {
                if (!$rootScope.project || !$rootScope.project.views) return;

                for (var i in $rootScope.project.views) {
                    var view = $rootScope.project.views[i];
                    if (view.id == state) {
                        return view;
                    }
                }
                return {};
            }

            $rootScope.getViewContainer = function (view) {
                var found = null;
                for (var i in view.body) {
                    var element = view.body[i];
                    if (element.type == "tab") {
                        found = element;
                        break;
                    }
                }
                if (!found) return null;

                if (found.values.length == 0) return null;

                return found.values[0];

            }

            $rootScope.addState = function (view) {
                if ($state.get(view.id)) {
                    return;
                }

                var state = {
                    url: '/' + view.id,
                    views: {
                        'header-container': {
                            templateUrl: 'pages/header.html'
                        },
                        'menu-container': {
                            templateUrl: 'pages/menu.html'
                        },
                        'page-container': {
                            templateUrl: 'pages/main.html',
                            controller: 'MainController'

                        },
                        /*'tab-container@principal': {
                            templateUrl: 'pages/auth.html'
                        },*/
                        'footer-container': {
                            templateUrl: 'pages/footer.html'
                        }
                    }
                };
                //state.views["tab-container@" + view.id] = {templateUrl: "pages/auth.html"};
                var viewContainer = $rootScope.getViewContainer(view);
                if (viewContainer) {
                    state.views["tab-container@" + view.id] = { templateUrl: 'pages/main.html' };
                }

                $stateProviderRef.state(view.id, state);
                Log.debug("state: " + view.id);

            }

            $rootScope.checkState = function (state) {
                for (var i in $rootScope.project.views) {
                    var view = $rootScope.project.views[i];
                    if (view.id == state) {
                        if (!$state.get(view.id)) {
                            $rootScope.addState(view);
                            return;
                        }
                    }
                }
            }

            $rootScope.loadStates = function () {
                try {
                    if (!$rootScope.project || !$rootScope.project.views) return;
                    for (var i in $rootScope.project.views) {
                        if (i == 0) {
                            $rootScope.home = $rootScope.project.views[0].id;
                        }
                        var view = $rootScope.project.views[i];
                        $rootScope.addState(view);
                    }
                } catch (error) {
                    Log.error("Falhar ao carregar estado:", error);
                }
            }

            /*$rootScope.processState = function(view) {
                var state = {
                    url: '/' + view.id,
                    views: {
                        'header-container': {
                            templateUrl: 'pages/header.html'
                        },
                        'menu-container': {
                            templateUrl: 'pages/menu.html'
                        },
                        'page-container': {
                            templateUrl: 'pages/main.html'
                        },
                        'tab-container@principal': {
                            templateUrl: 'pages/auth.html'
                        },
                        'footer-container': {
                            templateUrl: 'pages/footer.html'
                        }
                    }
                };
                $stateProviderRef.state(view.id, state);

            }*/

            $rootScope.goHome = function () {
                Analytics.trackEvent('root', 'goHome');
                if ($rootScope.home) {
                    $rootScope.go($rootScope.home);
                }
            }

            $rootScope.goSplash = function () {
                Analytics.trackEvent('root', 'goSplash');
                if ($rootScope.splash) {
                    $rootScope.go($rootScope.splash);
                }
            }

            $rootScope.goLogin = function () {
                Analytics.trackEvent('root', 'goLogin');
                $location.path("/login");
            }

            $rootScope.goAuth = function () {
                Analytics.trackEvent('root', 'goAuth');
                $location.path("/login");
            }

            $rootScope.goSetup = function () {
                Analytics.trackEvent('root', 'goSetup');
                $location.path("/setup");
            }

            $rootScope.go = function (state) {
                Analytics.trackEvent('root', 'go(' + state + ')');

                if (!$rootScope.headers) {
                    //$rootScope.goAuth();
                    $rootScope.goSetup();
                    return;
                }

                $rootScope.checkState(state);
                
                $state.go(state);

//                $location.path("/" + state);
            }

            $rootScope.updateProject = function (project) {
                $rootScope.project = project;
                $rootScope.loadStates();
                $rootScope.goHome();
            }
            $rootScope.getProject = function () {
                return $rootScope.project;
            }

            $rootScope.compiler = function (value) {
                try {
                    var result = value;
                    if (result != null) {
                        var variables = $rootScope.project.variables;
                        for (var i in variables) {
                            var variable = variables[i];

                            var eval = null;
                            try {
                                eval = $rootScope.$eval(variable.value);
                            } catch (error) {
                            }
                            if (!eval) eval = variable.value;
                            var variable = "%" + variable.name + "%";
                            result = Utils.replaceAll(result, variable, eval);
                        }
                    }
                } catch (error) {
                    Error.handler("Falha ao processar template", error);
                }
                return result;
            }


        } catch (error) {
            Error.handler("Falha ao processar inicialização", error);
        }

        $rootScope.loadTheme = function (theme) {
            function loadThemeSuccess(data) {
                try {
                    if (!data) throw new Error.throw("Não foi possível obter o tema selecionado: " + theme);
                    theme.content = data;
                    Log.info("Tema carregado com sucesso");
                } catch (error) {
                    Error.handler("Falha ao processar tema " + theme, error);
                }
            }
            WebResource.get(CSS_HOME + theme.value, loadThemeSuccess);
        }
    
    $rootScope.atualizaAutorizacao = function (cpfCliente, $http) {
    	var obj = {
    			cpf_cliente : cpfCliente,
    			integracao : INTEGRACAO,
    			operacao : 1,
    			modalidade : 0
        };
    	
    	$http.post(URL_SIMTR + SERVICO_AUTORIZACAO , obj).then(function (res) {    		
    		DossieService.setAutorizacao(res.data);    
    		$rootScope.$broadcast(FINISH_LOADING);
    	} , function (error) {
    		$rootScope.putError(error);
    		$rootScope.$broadcast(FINISH_LOADING);
    	
    	});
    }
    
    
    })

    .controller('AppController', function ($scope, $rootScope, $state, $timeout, $parse, $window, $translate, Error, Log, Feedback, Alert, Analytics, Geolocation) {
        Log.info("AppController()");

        Analytics.trackPage('Página Aplicação');

        $window.scrollTo(0, 1); //force Fullscreen on Mobile

        $rootScope.initDate = function (name) {
            $timeout(function () {
                $('#' + name).daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                });
            });
        }

        $rootScope.toDate = function (value) {
            return new Date(value);
        }

        $rootScope.fromDate = function (date, format) {
            try {
                if (date != null) {
                    return moment(date).format(format);
                } else {
                    return date;
                }
            } catch (error) {
                return date;
            }
        }

        $rootScope.loadMap = function (mapname) {
            $timeout(function () {
                Geolocation.loadMap("#" + mapname);
            });
        }

        $rootScope.loadChart = function (container, type) {
            $timeout(function () {
                if (type == 'line-chart') {
                    $scope.linechart = linechart;
                } else if (type == 'column-chart') {
                    $scope.columnchart = columnchart;
                } else if (type == 'pie-chart') {
                    $scope.piechart = piechart;
                }
            });
        }

        $rootScope.getPlatforms = function (platforms) {
            var result = "";
            var separator = " ";
            var PREVIEW = 'preview';
            var DESKTOP = 'desktop';
            var MOBILE = 'mobile';
            var NATIVO = 'nativo';
            var ADNROID = 'android';
            var IOS = 'ios';
            var WPHONE = 'winphone';

            function getValue(name) {
                for (var i in platforms) {
                    var currentitem = platforms[i];
                    if (currentitem.id == name) {
                        return currentitem.check;
                    }
                }
            }

            if (!platforms || platforms.length == 0) return result;

            result = "hidden-platform ";

            var showPreview = getValue(PREVIEW);
            var showDesktop = getValue(DESKTOP);
            var showMobile = getValue(MOBILE);
            var showNative = getValue(NATIVO);
            var showBrowserMobile = showMobile && !showNative;
            var showAndroid = !showDesktop && getValue(ADNROID);
            var showIOS = !showDesktop && getValue(IOS);
            var showWPhone = !showDesktop && getValue(WPHONE);

            if (showPreview) {
                result += " show-preview ";
            }
            if (showDesktop) {
                result += " show-desktop ";
            }
            if (showMobile) {
                if (showAndroid) {
                    result += "show-mobileandroid ";
                }
                if (showIOS) {
                    result += "show-mobileios ";
                }
                if (showWPhone) {
                    result += "show-mobilewinphone ";
                }
                if (!showAndroid && !showIOS && !showWPhone) {
                    result += "show-mobile ";
                }
            }
            if (showNative) {
                if (showAndroid) {
                    result += "show-nativoandroid ";
                }
                if (showIOS) {
                    result += "show-nativoios ";
                }
                if (showWPhone) {
                    result += "show-nativowinphone ";
                }
                if (!showAndroid && !showIOS && !showWPhone) {
                    result += "show-nativo ";
                }
            }
            if (!showMobile && !showNative) {
                if (showAndroid) {
                    result += "show-android ";
                }
                if (showIOS) {
                    result += "show-ios ";
                }
                if (showWPhone) {
                    result += "show-winphone ";
                }
            }

            return result;
        }

        $rootScope.doPrint = function ($event) {
            if ($event) {
                Feedback.loading($event);
            }
            $window.print();

            Feedback.reset();
        }

        $rootScope.confirmDelete = function () {
            function confirm() {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.exclusao.confirmada.pelo.usuario'));
            }
            function deny() {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.exclusao.cancelada.pelo.usuario'));
            }
            Alert.showConfirm($translate.instant('label.atencao'), $translate.instant('label.confirma.exclusao.deste.item'), confirm, deny);
        }

        $rootScope.openViewDelayed = function (id, delay, $event, message) {
            Log.debug("openViewDelayed(" + delay + ")");
            if ($event) {
                Feedback.loading($event);
            }
            var delayms = delay * 1000;
            $scope.dismissOpenDelayed(true);
            $scope.delayedPromise = $timeout(function () {
                if ($event) {
                    Feedback.reset();
                }
                if (id != null && id != '') {
                    $scope.openView(id, true);
                } else {
                    if ($event) {
                        if (!message) message = DEFAULT_BUTTON_MESSAGE;
                        Alert.showMessage($translate.instant('label.atencao'), message);
                    }
                }

            }, delayms);
        }

        $rootScope.scannerBarcode = function () {
            if (typeof cordova != 'undefined') {
                cordova.plugins.barcodeScanner.scan(
                    function (result) {
                        if (!result.cancelled) {
                            alert("Seu codigo é um " + result.format + "\n" +
                                "O conteudo é: " + result.text);
                        }
                    },
                    function (error) {
                        alert("O escaneamento falhou: " + error);
                    }
                );
            } else {
                Alert.showMessage($translate.instant('label.atencao'), "Procedimento só pode ser executado via smartphone");
            }
        }

        $rootScope.dismissOpenDelayed = function (silent, destination) {
            Log.debug("dismissOpenDelayed()");
            if ($scope.delayedPromise) {
                if ($timeout.cancel($scope.delayedPromise)) {
                    if (!silent) Alert.showMessage($translate.instant('label.atencao'), "Timer cancelado, clique novamente para continuar.");
                }
                $scope.delayedPromise = null;
            } else {
                $scope.openView(destination);
            }
        }

        $rootScope.openView = function (id, silent) {
            Log.debug("openView(" + id + "," + silent + ")");
            if (!id) return;
            if (id == "") return;
            if ($rootScope.contextEditable) return;
            if ($rootScope.isNavlock()) {
                if (!silent) Alert.showMessage($translate.instant('label.atencao'), "Navegação suspensa durante a edição.||Clique em OK para prosseguir");
                return;
            }

            $rootScope.dismissOpenDelayed(true);

            $rootScope.go(id);
        }

    })

    .controller('SetupController', function ($scope, $rootScope, $state, $stateParams, $timeout, Log, Feedback, NativeInterface, Analytics, WebServiceX, Utils) {
        Log.info("SetupController()");

        var authCode = null;
        var id = $rootScope.project.id;

        if ($rootScope.project) {
            authCode = NativeInterface.getPreference(id + ":auth", null);
        }

        if ($stateParams.code) {
            $rootScope.headers = {
                authCode: $stateParams.code
            }
        } else if (authCode) {
            $rootScope.headers = {
                authCode: authCode
            }
        }

        if ($rootScope.headers) {
            $timeout(function () {
                Analytics.trackEvent('SetupController', 'autoLogin()');
                WebServiceX.readAll("ws/auth/", {}, $rootScope.headers)
                    .then(function (res) {
                        $rootScope.platforms = Utils.getPlatforms();
                        function onstartsuccess() {
                            $rootScope.showInvite();
                        }
                        $rootScope.startMessageService(onstartsuccess);


                        NativeInterface.setPreference(id + ":auth", $rootScope.headers.authCode);

                        $rootScope.$apply(function () {
                            $rootScope.goSplash();
                        });

                    }, function (xhr, status, err) {
                        Log.error("Falha durante auto-autenticação", err);

                        NativeInterface.setPreference(id + ":auth", null);
                        $rootScope.headers = null;
                        $rootScope.$apply(function () {
                            $rootScope.goLogin();
                        });
                    });
            }, 5000);

        } else {
            $rootScope.goLogin();

        }

    })

    .controller('PreviewController', function ($scope, $rootScope, $state, $stateParams, Log, Feedback) {
        Log.info("PreviewController()");


        if ($stateParams.code) {
        	/* $rootScope.headers = {
            authCode: $stateParams.code,
            navegador : Utils.getEnv().browser,
            mode: PREVIEW
        }*/
        }

        if ($rootScope.project && $rootScope.project.views) {
            $rootScope.loadStates();
        }

        if ($stateParams.mode == BYPASS) {
            $rootScope.goHome();
        }

    })

    .controller('LoginController', function ($scope, $rootScope, $translate, Log, WebServiceX, Analytics, Error, Utils, NativeInterface, $stateParams, Feedback, Alert) {
        Log.debug("LoginController()");
        Analytics.trackEvent('controller', 'LoginController');

        $scope.container = {};
        $rootScope.currentview.id = 'login';
        $rootScope.currentview.group = 'Login';
        $rootScope.currentview.title = 'Login';
        $rootScope.currentview.icon = 'fa-sign-in';
        $rootScope.currentview.locked = false;
        $rootScope.currentview.menu = false;
        $rootScope.currentview.description = 'Tela de Login';

        Log.info("AuthController()");

        var id = $rootScope.project.id;

        $scope.session = {
            authCode: null,
            result: null
        }

        if ($rootScope.headers && $rootScope.headers.authCode) {
            $scope.session.authCode = $rootScope.headers.authCode;
        }

        $scope.doLogin = function ($event) {
            Feedback.loading($event);

            if ($rootScope.headers && $rootScope.headers.mode == PREVIEW) {

                if ($scope.session.authCode == $rootScope.headers.authCode) {
                    $rootScope.goHome();
                } else {
                    $scope.session.result = $translate.instant('label.codigo.acesso.autorizado.contate.responsavel.projeto');
                    Alert.showMessage($translate.instant('label.atencao'), $scope.session.result);
                }

                Feedback.reset();
                return;
            }

            if (!$scope.session.authCode) {
                $scope.session.result = $translate.instant('label.aplicacao.restrita.informe.codigo.acesso');
                Alert.showMessage($translate.instant('label.atencao'), $scope.session.result);
                Feedback.reset();
                return;
            }

            if (!$scope.session.user) {
                $scope.session.result = $translate.instant('label.aplicacao.restrita.informe.email');
                Alert.showMessage($translate.instant('label.atencao'), $scope.session.result);
                Feedback.reset();
                return;
            }

            $rootScope.headers = {
                "authCode": $scope.session.authCode,
                "navegador": Utils.getEnv().browser
            }

            Analytics.trackEvent('AuthController', 'doLogin()');
            WebServiceX.readAll("ws/auth/", {}, $rootScope.headers)
                .then(function (res) {
                    $rootScope.platforms = Utils.getPlatforms();

                    function onstartsuccess() {
                        $rootScope.showInvite();
                    }
                    $rootScope.startMessageService(onstartsuccess);


                    Feedback.reset();

                    NativeInterface.setPreference(id + ":auth", $scope.session.authCode);

                    $scope.autenticar($scope.session.user, $scope.session.authCode);


                }, function (xhr, status, err) {
                    Log.error("Falha durante autenticação", err);
                    Feedback.reset();
                    $scope.session.result = $translate.instant('label.codigo.acesso.autorizado.contate.responsavel.projeto');
                    Alert.showMessage($translate.instant('label.atencao'), $scope.session.result);
                    $scope.$apply();

                });
        };

        if ($stateParams.code) {
            $scope.session.authCode = $stateParams.code;
            $scope.doLogin();
        }

        $scope.autenticar = function (usuario, psw) {

//            Analytics.trackEvent('LoginController', 'Autenticacao');

            var autenticarRequisicao = {};
            autenticarRequisicao.usuario = usuario;
            autenticarRequisicao.senha = psw;
            autenticarRequisicao.navegador = Utils.getEnv().browser;
            WebServiceX.create('ws/auth/autenticar/', JSON.stringify(autenticarRequisicao))
                .then(function (res) {
                    Analytics.trackEvent('LoginController', 'Autenticacao:success');
                    if (!res.temErro) {
                        $rootScope.user = $scope.session.user;
                        $rootScope.goHome();
                        $rootScope.$apply();
                    } else if (res.temErro) {
                        console.info(res.msgsErro[0]);
                        //Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                        $rootScope.goHome();
                        $rootScope.$apply();
                    }
                }, function (xhr, status, err) {
                    Analytics.trackEvent('loginController', 'listar():error');
                    var message = "Falha ao listar loginController";
                    if (xhr && xhr.responseText) {
                        try {
                            var response = JSON.parse(xhr.responseText);
                            if (response && response.msgsErro && response.msgsErro.length > 0) {
                                message = response.msgsErro[0];
                            }
                        } catch (ignore) { }
                    }
                    Error.handler(message, err);
                    if (err == UNAUTH) {
                        //$rootScope.goLogin();
                        $rootScope.goHome();
                        $rootScope.$apply();
                    }
                });

        };

    })

    .controller('MenuController', function ($scope, $rootScope, Log) {
        Log.info("MenuController()");
        
        $rootScope.checkDeviceValidation = function () {
			var selectDevice = $rootScope.project.customersummary.device;
			if(selectDevice > -1){
				$('#div-dispositivo').removeClass('has-error').removeClass('validation-error'); 
				$('#scan-dispositivo').removeClass('validation-error');
			}
        }        

        setTimeout(function () {
            adequacaoAcessibilidade();
            controlaMenu();
        }, 200);
    })

    .controller('HeaderController', function ($scope, $rootScope, $state, Log, KeycloakService) {
        Log.info("HeaderController()");

        $rootScope.selectedProject = $rootScope.project;

        if ($state.current.name == $rootScope.home) {
            $rootScope.project.telainicial = true;
        } else {
            $rootScope.project.telainicial = false;
        }
        
        $scope.usuario = KeycloakService.loadUserSSO();
        
        $scope.logout = function () {
        	if (keycloak) {
        		keycloak.loginRequired = true;
        		keycloak.logout();
        	}
        }
        
        $scope.checkboxModel = {
            alteraTemaContraste: { selected: false }, myClick: function ($event) {
                if ($event.selected) {
                    $rootScope.project.theme = $rootScope.contraste;
                    $rootScope.loadTheme($rootScope.contraste);
                } else {
                    $rootScope.project.theme = $rootScope.theme;
                    $rootScope.loadTheme($rootScope.theme);
                }
                $rootScope.alteraContraste = $event.selected;
            }
        };

        $rootScope.goElement = function (id) {
            var element = document.getElementById(id);
            element.focus();
            element.scrollIntoView();
        }

        $scope.pesquisarTelas = function (str, telas) {
            var matches = [];

            telas.forEach(function (tela) {
                if (tela.title.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) {
                    matches.push(tela);
                }
            });
            return matches;
        };

        $scope.openView = function (id, silent) {
            Log.debug("openView(" + id + "," + silent + ")");
            if (!id) return;
            if (id == "") return;
            if ($rootScope.contextEditable) return;

            $scope.dismissOpenDelayed(true);

            $rootScope.go(id);
        }

        $scope.selectedTela = function (selected) {
            if (selected !== undefined) {
                console.log(selected);
                $scope.openView(selected.description.target);
            }
        };

        $scope.telas = [];
        for (var i in $rootScope.project.menus) {
            var menu = $rootScope.project.menus[i];
            if (menu.submenus == null) {
                $scope.telas.push(menu);
            } else {
                for (var j in menu.submenus) {
                    var submenu = menu.submenus[j];
                    if (submenu.submenus == null) {
                        $scope.telas.push(submenu);
                    } else {
                        for (var k in submenu.submenus) {
                            var submenu2 = submenu.submenus[k];
                            $scope.telas.push(submenu2);
                        };
                    };
                };
            };
        };

    })

    .controller('MainController', function ($scope, $rootScope, $state, $window, Log, Analytics, Utils) {
        Log.info("MainController()");
        Analytics.trackPage('Página Principal');

        //$rootScope.currentview = $rootScope.getCurrentState($state.current.name);
        $rootScope.currentview = $rootScope.getCurrentState($state.current.name);

        $scope.pageOptions = [{ name: "3", value: 3 }, { name: "5", value: 5 }, { name: "10", value: 10 }, { name: "20", value: 20 }, { name: "30", value: 30 }, { name: "50", value: 50 }];
        $scope.itemsOnPage = $scope.pageOptions[0];

        $scope.doExportDataTxt = function (data) {
            var dados = JSON.stringify(data);
            dados = vkbeautify.json(dados);
            Utils.exportToFile("dados.txt", dados);
        }

        $scope.doExportDataCsv = function (data) {
            Utils.exportToCsv("dados.csv", data, ";");
        }
    })

    .controller('WebGLController', function ($scope, $rootScope, $timeout, Log, Analytics, Utils) {
        Log.info("WebGLController()");
        Analytics.trackPage('Página WebGL');

        $scope.initWebGL = function (id, title, subtitle, object, material) {
            $timeout(function () {
                var textStyle = $scope.getStyle(".text-primary");
                init(id, title, subtitle, object, material, textStyle);
            });
        }

        $scope.getStyle = function (className) {
            var result = null;

            for (var x = 0; document.styleSheets.length; x++) {
                var stylesheets = document.styleSheets[x];
                if (!stylesheets) break;

                var classes = stylesheets.rules || stylesheets.cssRules;
                if (classes) {
                    for (var y = 0; y < classes.length; y++) {
                        if (classes[y].selectorText == className) {
                            result = classes[y].style.color;
                        }
                    }
                }
            }
            return result;
        }

    })

    .controller('TabController', function ($scope, $rootScope, $state, $window, Log, Analytics, Utils) {
        Log.info("TabController()");
        Analytics.trackPage('Página Tab');

        //$rootScope.currentview = $rootScope.getCurrentState($state.current.name);
        $scope.currentview = null;

        $scope.pageOptions = [{ name: "3", value: 3 }, { name: "5", value: 5 }, { name: "10", value: 10 }, { name: "20", value: 20 }, { name: "30", value: 30 }, { name: "50", value: 50 }];
        $scope.itemsOnPage = $scope.pageOptions[0];

        $scope.doExportDataTxt = function (data) {
            var dados = JSON.stringify(data);
            dados = vkbeautify.json(dados);
            Utils.exportToFile("dados.txt", dados);
        }

        $scope.doExportDataCsv = function (data) {
            Utils.exportToCsv("dados.csv", data, ";");
        }

        $scope.init = function (element) {
            if (!element) return;
            if (!element.values || element.values.length == 0) return;

            var view = element.values[0];
            //$scope.currentview = $rootScope.getCurrentState(view.value);
            $scope.loadView(view.value);
        }

        $scope.loadView = function (viewid) {
            $scope.currentview = $rootScope.getCurrentState(viewid);
        }

    })

    .controller('uploaderCtrl', function ($scope, $rootScope, Log, uiUploader) {
        $scope.btn_remove = function (file) {
            Log.info('deleting=' + file);
            uiUploader.removeFile(file);
        };

        $scope.btn_clean = function () {
            uiUploader.removeAll();
        };

        $scope.btn_upload = function () {
            Log.info('uploading...');
            uiUploader.startUpload({
                concurrency: 2,
                onProgress: function (file) {
                    Log.info(file.name + '=' + file.humanSize);
                    $scope.$apply();
                },
                onCompleted: function (file, response) {
                    Log.info(file + 'response' + response);
                }
            });
        };

        $scope.files = [];
        $scope.changeUpload = function (e) {
            var files = e.target.files;
            uiUploader.removeAll();
            uiUploader.addFiles(files);
            $scope.files = uiUploader.getFiles();
            $scope.$apply();

            Log.info('uploading...');
            uiUploader.startUpload({
                concurrency: 2,
                onloadstart: function (file) {
                    file.loaded = 1;
                    $scope.$apply();
                },
                onProgress: function (file) {
                    posicao = (file.loaded * 100) / file.size;
                    file.loaded = Math.floor(posicao);
                    $scope.$apply();
                },
                onCompleted: function (file, response) {
                    file.loaded = 100;
                    $scope.$apply();
                }
            });
        };
    })

    .controller('FeedbackController', function ($scope, $rootScope, Log, Error, EndPoint, Alert, Analytics) {
        Log.debug("FeedbackController()");

        Analytics.trackEvent('controller', 'FeedbackController');

        $scope.evaluationRate = Analytics.getEvaluationRate();
        $scope.userFeedback = Analytics.getUserFeedback();

        $scope.user = {
            feedback: angular.copy($scope.userFeedback),
            message: null
        }

        $scope.getRate = function (rate) {
            for (var i in $scope.evaluationRate) {
                var evaluation = $scope.evaluationRate[i];
                if (evaluation.rate == rate) return evaluation;
            }
            return null;
        }

        $scope.sendFeedback = function () {

            for (var i in $scope.user.feedback) {
                var feedback = $scope.user.feedback[i];
                if (feedback.rate == null) {
                    Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.faltou.avaliar.quesito') + feedback.name + " " + feedback.description);
                    return;
                }
            }

            var generalrate = 0;
            for (var i in $scope.user.feedback) {
                var feedback = $scope.user.feedback[i];
                Analytics.trackEvent('feedback:' + feedback.id, feedback.name, feedback.rate);
                generalrate += feedback.rate;
            }
            generalrate = generalrate / $scope.user.feedback.length;
            Analytics.trackEvent('feedbackGeral', 'Avaliação Geral', generalrate);

            if ($scope.user.message != null && $scope.user.message.trim() != "") {
                var message = $scope.user.message.replace(/(?:\r\n|\r|\n)/g, '|');
                Analytics.trackEvent('feedbackMessage', message, generalrate);
            }

            $scope.closeDialog();
            Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.sua.opiniao.foi.enviada.com.sucesso'));
        }
    })

    .controller('TranslateCtrl', function ($scope, $translate) {
        $scope.changeLanguage = function (key) {
            $translate.use(key);
        };
    })

    .controller('downloadCtrl', function ($scope, $rootScope, Log) {

        $scope.saveToDisk = function (fileURL) {
            //fileURL = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFgAAAAgCAIAAAAZhijPAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3hpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDE0IDc5LjE1Njc5NywgMjAxNC8wOC8yMC0wOTo1MzowMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0iQkQyQTFCMDM3MEU2MDNBRjYzNjFFQzU1Q0NCMDFBQzciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6ODY2MDk0NEYwMzNGMTFFNjk4OTZDRjAxMzBGN0I0RkEiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6ODY2MDk0NEUwMzNGMTFFNjk4OTZDRjAxMzBGN0I0RkEiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6YTM1YTMxNDktY2QzMS05MzRiLWFlYjgtMTBlNzE5MTM1NDFmIiBzdFJlZjpkb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6ZDE4MjJkYjYtMDMzNi0xMWU2LTk5ZGItYWEwOTJjNDA5MGFk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'
            fileName = 'downloadArquivo';

            // for non-IE
            if (!window.ActiveXObject) {
                var save = document.createElement('a');
                save.href = fileURL;
                save.target = '_blank';
                save.download = fileName || 'unknown';

                var event = document.createEvent('Event');
                event.initEvent('click', true, true);
                save.dispatchEvent(event);
                (window.URL || window.webkitURL).revokeObjectURL(save.href);
            }

            // for IE
            else if (!!window.ActiveXObject && document.execCommand) {
                var _window = window.open(fileURL, '_blank');
                _window.document.close();
                _window.document.execCommand('SaveAs', true, fileName || fileURL);
                _window.close();
            }
        }
    })



    .controller('MensagemController', function ($scope, $rootScope, $translate, Log, Error, Alert, Analytics, WebServiceX) {
        Log.debug("MensagemController()");
        var json = {
            token: $scope.user.token
        };
        WebServiceX.create("ws/push/listarInscritosMensagem", JSON.stringify(json), $rootScope.headers)
            .then(function (res) {
                if (!res.temErro) {
                    $scope.usersOrigin = JSON.parse(res.data);
                    $scope.users = $scope.usersOrigin;
                    $scope.$apply();
                } else if (res.temErro) {
                    console.error(res.msgsErro[0] + ": " + json);
                    Alert.showMessage("Atenção", res.msgsErro[0]);
                    $scope.$apply();
                }
            }, function (xhr, status, err) {
                var response = JSON.parse(xhr.responseText);
                if (response == null || response.code != 0) {
                    Error.handler("Falha obter os inscritos ", err);
                }
                if (err == UNAUTH || xhr.status == 502) {
                    $rootScope.goAuth();
                }
            }
            );

        $scope.limpar = function (action) {
            $scope.categories = angular.copy(CATEGORIAS);
            $scope.message = { name: $translate.instant('label.nome.da.mensagem'), title: $translate.instant('label.titulo.da.mensagem'), content: $translate.instant('label.conteudo.da.mensagem'), url: "", user: "", category: "1" };

            $scope.users = $scope.usersOrigin;
            $scope.userselected = [];

        }
        $scope.limpar();

        $scope.$on('closeView', function (event, userLocation) {
            Log.debug("event:refreshView");
            $rootScope.gotoPrincipal();
        });

        $scope.doEditAction = function (action) {
            $scope.actioname = action;

            if ($scope.actioname == 'confirm' && $scope.message.confirm != null) {
                $scope.messageaction = $scope.message.confirm;
            } else if ($scope.actioname == 'cancel' && $scope.message.cancel != null) {
                $scope.messageaction = $scope.message.cancel;
            } else if ($scope.actioname == 'close' && $scope.message.close != null) {
                $scope.messageaction = $scope.message.close;
            } else {
                $scope.messageaction = { title: null, value: null };
            }

            $scope.modalaction = Alert.showModal("resources/modalaction.html", null, $scope, true, true, "md", null, null);
            $scope.modalaction.result.then(function () {
                if ($scope.actioname == 'confirm') {
                    $scope.message.confirm = $scope.messageaction;
                } else if ($scope.actioname == 'cancel') {
                    $scope.message.cancel = $scope.messageaction;
                } else if ($scope.actioname == 'close') {
                    $scope.message.close = $scope.messageaction;
                }
                $scope.modalaction.dismiss();
            }, function () {
                $scope.modalaction.dismiss();
            });


        }

        $scope.removeAction = function (actioname) {
            function confirm() {

                if ($scope.actioname == 'confirm') {
                    $scope.message.confirm = null;
                } else if ($scope.actioname == 'cancel') {
                    $scope.message.cancel = null;
                } else if ($scope.actioname == 'close') {
                    $scope.message.close = null;
                }

                $scope.modalaction.dismiss();
            }
            Alert.showConfirm($translate.instant('label.atencao'), $translate.instant('label.confirma.remover.esta.acao'), confirm);
        }

        $scope.enviarMensagem = function () {

            console.log($scope.message);

            if (!$scope.message.name) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.informe.o.nome'));
                return;
            }
            if (!$scope.message.title) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.informe.o.titulo'));
                return;
            }
            if (!$scope.message.content) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.informe.a.mensagem'));
                return;
            }
            var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if ($scope.message.user.length > 0 && !regex.test($scope.message.user)) {
                Alert.showMessage("Atenção", "Destinatário Inválido");
                return;
            } else {
                $scope.message.user = $scope.message.user.trim();
            }

            $scope.listusers = [];
            var user = {};
            for (var i = 0; i < $scope.userselected.length; i++) {
                user.email = $scope.userselected[i];
                $scope.listusers.push(user);
                user = {};
            }

            var json = {
                nome: $scope.message.name,
                titulo: $scope.message.title,
                mensagem: $scope.message.content,
                site: $scope.message.url,
                destinatario: JSON.stringify($scope.listusers),
                confirmar: JSON.stringify($scope.message.confirm),
                cancelar: JSON.stringify($scope.message.cancel),
                fechar: JSON.stringify($scope.message.close),
                categoria: $scope.message.category,
                token: $scope.user.token

            };

            WebServiceX.create("ws/push/enviarMensagem", JSON.stringify(json), $rootScope.headers)
                .then(function (res) {
                    if (!res.temErro) {

                        $scope.message.name = $translate.instant('label.nome.da.mensagem');
                        $scope.message.title = $translate.instant('label.titulo.a.mensagem');
                        $scope.message.content = $translate.instant('label.conteudo.a.mensagem');
                        $scope.message.url = "";
                        $scope.message.user = "";

                        delete $scope.confirm;
                        delete $scope.cancel;
                        delete $scope.close;

                        $scope.message.category = "1";
                        $scope.user.token = $scope.user.token;

                        $scope.users = $scope.usersOrigin;
                        $scope.userselected = [];


                        $scope.$apply();

                        Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.sua.mensagem.foi.enviada.com.sucesso'));

                    } else if (res.temErro) {
                        console.error(res.msgsErro[0] + ": " + json);
                        Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                        $scope.$apply();
                    }
                }, function (xhr, status, err) {
                    var response = JSON.parse(xhr.responseText);
                    if (response == null || response.code != 0) {
                        Error.handler("Falha ao enviar a mensagem", err);
                    }
                    if (err == UNAUTH || xhr.status == 502) {
                        $rootScope.goAuth();
                    }
                });
        }
    })

    .controller('EmailsController', function ($scope, $rootScope, $translate, Log, Error, Alert, Analytics, WebServiceX) {
        Log.debug("EmailsController()");

        WebServiceX.create("ws/push/listarInscritos", null, $rootScope.headers)
            .then(function (res) {
                if (!res.temErro) {
                    $scope.usersOrigin = JSON.parse(res.data);
                    $scope.users = $scope.usersOrigin;
                    $scope.$apply();
                } else if (res.temErro) {
                    console.error(res.msgsErro[0] + ": " + json);
                    Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                    $scope.$apply();
                }
            }, function (xhr, status, err) {
                var response = JSON.parse(xhr.responseText);
                if (response == null || response.code != 0) {
                    Error.handler("Falha obter os inscritos ", err);
                }
                if (err == UNAUTH || xhr.status == 502) {
                    $rootScope.goAuth();
                }
            }
            );

        $scope.$on('closeView', function (event, userLocation) {
            Log.debug("event:refreshView");
            $rootScope.gotoPrincipal();
        });


        // INICIO - ARQUIVO
        var dropZone = document.getElementById('uplArquivo');
        dropZone.addEventListener('dragover', handleDragOver, false);
        dropZone.addEventListener('drop', handleFileSelect, false);

        function handleDragOver(evt) {
            evt.stopPropagation();
            evt.preventDefault();
            evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
        }

        function handleFileSelect(evt) {
            evt.stopPropagation();
            evt.preventDefault();
            var files = evt.dataTransfer.files;

            for (var i = 0, f; f = files[i]; i++) {
                readFile(f);
            }
            $scope.$apply();
        }

        function readFile(file) {
            var reader = new FileReader();
            var name = file.name;
            var infoFile = '' + escape(file.name) + '(' + file.type + ') - ' + file.size + ' bytes, last modified: ' + (file.lastModifiedDate ? file.lastModifiedDate.toLocaleDateString() : 'n/a');

            reader.onload = function (evt) {
                var index = $scope.container.arquivo.length;
                var extension = name.substring(name.lastIndexOf('.') + 1);

                $scope.container.arquivo[index] = { "content": evt.target.result, "name": name, "type": extension };
                $scope.container.listas[index] = infoFile;
                $rootScope.listas[index] = infoFile;
                $scope.$apply();
            };

            reader.readAsDataURL(file);
        }

        function loadFile(file, filename) {
            var reader = new FileReader();
            var name = filename;
            var infoFile = '' + escape(filename) + '(' + file.type + ') - ' + file.size + ' bytes, last modified: ' + (file.lastModifiedDate ? file.lastModifiedDate.toLocaleDateString() : 'n/a');

            var index = $scope.container.arquivo.length;
            var extension = name.substring(name.lastIndexOf('.') + 1);

            $scope.container.arquivo[index] = { "content": file.name, "name": name, "type": extension };
            $scope.container.listas[index] = infoFile;
            $rootScope.listas[index] = infoFile;
            $scope.$apply();
        }

        $scope.removerArquivo = function (index) {
            $scope.container.arquivo.splice(index, 1);
            $scope.container.listas.splice(index, 1);
        }
        // FIM - ARQUIVO




        $scope.enviarMensagem = function () {


            if (!$scope.email.title) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.informe.o.titulo.do.email'));
                return;
            }
            if (!$scope.email.content) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.informe.a.mensagem.do.email'));
                return;
            }
            if (!$scope.userselected || $scope.userselected.length <= 0) {
                Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.adicione.um.destinatario'));
                return;
            }


            var attachment = {};
            attachment = JSON.parse(angular.toJson($scope.container.arquivo));

            $scope.listusers = [];
            var user = {};
            for (var i = 0; i < $scope.userselected.length; i++) {
                user.email = $scope.userselected[i];
                $scope.listusers.push(user);
                user = {};
            }

            var json = {
                titulo: $scope.email.title,
                mensagem: $scope.email.content,
                categoria: $scope.email.category,
                arquivos: JSON.stringify(attachment),
                emails: JSON.stringify($scope.listusers),
                emailAtivo: $scope.email.deleted,
                token: $scope.user.token
            };
            WebServiceX.create("ws/push/enviarMensagemEmail", JSON.stringify(json), $rootScope.headers)
                .then(function (res) {
                    if (!res.temErro) {

                        $scope.limpar();

                        $scope.$apply();

                        Alert.showMessage($translate.instant('label.atencao'), $translate.instant('label.sua.mensagem.foi.enviada.com.sucesso'));

                    } else if (res.temErro) {
                        console.error(res.msgsErro[0] + ": " + json);
                        Alert.showMessage($translate.instant('label.atencao'), res.msgsErro[0]);
                        $scope.$apply();
                    }
                }, function (xhr, status, err) {
                    try {
                        var response = JSON.parse(xhr.responseText);
                        if (response == null || response.code != 0) {
                            Error.handler("Falha ao enviar a mensagem", err);
                        }
                        if (err == UNAUTH || xhr.status == 502) {
                            $rootScope.goAuth();
                        }
                    }
                    catch (err) {
                        console.log(xhr.responseText);
                        Error.handler("Falha ao enviar a mensagem", "Ocorreu um erro no formato do json", null, null);
                    }
                });

        }
        
    

        $scope.limpar = function () {

            $scope.users = $scope.usersOrigin;
            $scope.categories = angular.copy(CATEGORIAS);

            $scope.email = {
                title: $translate.instant('label.titulo.do.email'),
                content: $translate.instant('label.conteudo.da.mensagem'),
                category: "1",
                deleted: false,
                recipients: [],
                attchament: null
            }

            $scope.container = [];
            $scope.container.arquivo = [];
            $scope.container.listas = [];
            $rootScope.listas = [];

            $scope.userselected = [];
        }
        $scope.limpar();
        
   
        
        
    });