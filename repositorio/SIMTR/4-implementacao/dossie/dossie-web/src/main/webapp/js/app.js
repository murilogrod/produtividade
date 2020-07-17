angular.module("webApp", ["ui.router", "ui.mask", "starter.controllers", "starter.services", "starter.filters", "starter.directives", 
		"hs.WebService", "ncy-angular-breadcrumb", "fxpicklist", "pascalprecht.translate",
		"myApp.Interceptor", "ngSanitize"])
		

/*
	Aqui é montada a navegação por todas as telas definidas no projeto.
	Cada tela é montada como um estado.
	Cada estado é composto de uma página (html) e um controller (js)
	
	de acordo com as necessidades, um estado pode redefinir tambem o cabecalho, o rodape e o menu
*/

    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProviderRef = $stateProvider;

        $.ajax({
			type: 'GET',
			url: 'ws/analyticres/url',
			contentType: 'application/json',
			dataType: 'json',
			success: function (result) {
				URL_CAIXA_ANALYTICS = result.url;
				URL_CAIXA_SICPU = result.urlsicpu;
			},
			async: false
		 });
        if (URL_CAIXA_ANALYTICS == null || URL_CAIXA_ANALYTICS == "") {
    		console.error("Erro ao recuperar a URL ANALYTICS: " + URL_CAIXA_ANALYTICS );
    	} else {
    		console.info("URL ANALYTICS : " + URL_CAIXA_ANALYTICS);
    		console.info("URL SICPU : " + URL_CAIXA_SICPU);
    	}
        
        $.ajax({
			type: 'GET',
			url: 'ws/servidor/instancia',
			contentType: 'application/json',
			dataType: 'json',
			success: function (result) {
				INSTANCIA_JBOSS = result.nomeInstancia;
			},
			async: false
		 });

          $stateProvider.state(STATE_INIT,{
                url: '/',
                abstract: false,
                views: {
                    'header-container': {
                        templateUrl: 'pages/header.html',
                        controller : 'AppController'
                    },
                    'menu-container': {
                        templateUrl: 'pages/menu.html',
                        controller : 'AppController'
                    },
                    'page-container': {
                        templateUrl: 'pages/main.html',
                        controller : 'AppController'
                    },
                    'footer-container': {
                        templateUrl: 'pages/footer.html',
                        controller : 'AppController'
                    }
                }
            });	
          
          $stateProvider.state("setup",{
              url: '/setup?code',
              abstract: false,
              views: {
                  'header-container': {
                      controller: "SetupController"
                  }
              }
          })            
          
          $stateProvider.state("auth",{
              url: '/auth?code',
              abstract: false,
              views: {
                  'page-container': {
                      templateUrl: 'pages/auth.html'
                  }
              }
          })
          .state('mensagens',{
              url: '/mensagens',
              views: {
                  'header-container': {
                  	templateUrl: 'pages/header.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'page-container@': {
                      templateUrl: 'pages/sicpuMensage/moduloMensagem.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }
          })
          
          .state('emails',{
              url: '/emails',
              views: {
                  'header-container': {
                  	templateUrl: 'pages/header.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'page-container@': {
                      templateUrl: 'pages/sicpuMensage/moduloEmail.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }
          });

			
			var state = {
              url: '/principal',
              
              views: { 'page-container': {
                      templateUrl: 'pages/principal/Principal.html'
                  }
              }                
          };
					  
					  			
          $stateProvider.state('principal',state);
          
			var state = {
              url: '/sobre',
              ncyBreadcrumb: {
	            	label: 'Sobre'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/sobre/Sobre.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('sobre',state);
          
			var state = {
              url: '/cadastrar',
              ncyBreadcrumb: {
	            	label: 'Quero me cadastrar'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/login/Cadastrar.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('cadastrar',state);
          
			var state = {
              url: '/esquecisenha',
              ncyBreadcrumb: {
	            	label: 'Esqueci minha senha'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/login/Esquecisenha.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('esquecisenha',state);
			
			var state = {
              url: '/perfil',
              ncyBreadcrumb: {
	            	label: 'Perfil'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/login/Perfil.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('perfil',state);
          
			var state = {
              url: '/resumodoCliente',
              ncyBreadcrumb: {
	            	label: 'Resumo do Cliente'
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/resumo/Resumodocliente.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('resumodoCliente',state);
          
			var state = {
              url: '/cadastro',
              ncyBreadcrumb: {
	            	label: 'Cadastro'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/cadastro/Cadastro.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('cadastro',state);
          
			var state = {
              url: '/avaliacaodeRisco',
              ncyBreadcrumb: {
	            	label: 'Avaliação de Risco'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/avaliacaorisco/Avaliacaoderisco.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('avaliacaodeRisco',state);
          
			var state = {
              url: '/informacesdoClienteeParametrosdaConta',
              ncyBreadcrumb: {
	            	label: 'Informações do Cliente e Parâmetros da Conta'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Informacesdoclienteeparametrosdaconta.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('informacesdoClienteeParametrosdaConta',state);
          
			var state = {
              url: '/informacesdoClienteContaConjunta',
              ncyBreadcrumb: {
	            	label: 'Informações do Cliente Conta Conjunta'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Informacesdoclientecontaconjunta.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('informacesdoClienteContaConjunta',state);
          
			var state = {
              url: '/informacesdoClienteProcuradorTutorCurador',
              ncyBreadcrumb: {
	            	label: 'Informações do Cliente Procurador Tutor Curador'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Informacesdoclienteprocuradortutorcurador.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('informacesdoClienteProcuradorTutorCurador',state);
			
			var state = {
              url: '/pesquisasCadastrais',
              ncyBreadcrumb: {
	            	label: 'Pesquisas Cadastrais'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Pesquisascadastrais.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('pesquisasCadastrais',state);
			
			var state = {
              url: '/digitalizacaodeDocumentos',
              ncyBreadcrumb: {
	            	label: 'Digitalização de Documentos'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaodedocumentos.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('digitalizacaodeDocumentos',state);
          
			var state = {
              url: '/digitalizacaoDocIdentificacaoFechado',
              ncyBreadcrumb: {
	            	label: 'Digitalização Doc Identificação Fechado'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaodocidentificacaofechado.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('digitalizacaoDocIdentificacaoFechado',state);
          
			var state = {
              url: '/digitalizacaoDocIdentificacaoAberto',
              ncyBreadcrumb: {
	            	label: 'Digitalização Doc Identificação Aberto'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaodocidentificacaoaberto.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('digitalizacaoDocIdentificacaoAberto',state);
          
			
			var state = {
              url: '/digitalizacaoCompResidnciaFechado',
              ncyBreadcrumb: {
	            	label: 'Digitalização Comp Residência Fechado'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaocompresidnciafechado.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('digitalizacaoCompResidnciaFechado',state);
          
			var state = {
              url: '/digitalizacaoCompResidnciaAberto',
              ncyBreadcrumb: {
	            	label: 'Digitalização Comp Residência Aberto'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaocompresidnciaaberto.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('digitalizacaoCompResidnciaAberto',state);
          
			var state = {
              url: '/digitalizacaoCompRendaFechado',
              ncyBreadcrumb: {
	            	label: 'Digitalização Comp Renda Fechado'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaocomprendafechado.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('digitalizacaoCompRendaFechado',state);
			
			var state = {
              url: '/digitalizacaoCompRendaAberto',
              ncyBreadcrumb: {
	            	label: 'Digitalização Comp Renda Aberto'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Digitalizacaocomprendaaberto.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('digitalizacaoCompRendaAberto',state);
          
			var state = {
              url: '/cartaoAssinatura',
              ncyBreadcrumb: {
	            	label: 'Cartão Assinatura'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Cartaoassinatura.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('cartaoAssinatura',state);
          
			var state = {
              url: '/cadastramentodeSenhaCliente',
              ncyBreadcrumb: {
	            	label: 'Cadastramento de Senha Cliente'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Cadastramentodesenhacliente.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('cadastramentodeSenhaCliente',state);
			
			var state = {
              url: '/produtosdoCliente',
              ncyBreadcrumb: {
	            	label: 'Produtos do Cliente'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/produtos/Produtosdocliente.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('produtosdoCliente',state);
          
			var state = {
              url: '/dossiDigital',
              ncyBreadcrumb: {
	            	label: 'Dossiê Digital'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/dossiedigital/Dossidigital.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('dossiDigital',state);
          
			var state = {
              url: '/roteiroNegocial',
              ncyBreadcrumb: {
	            	label: 'Roteiro Negocial'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/roteironegocial/Roteironegocial.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('roteiroNegocial',state);
          
			var state = {
              url: '/portfliodeAcessos',
              ncyBreadcrumb: {
	            	label: 'Portfólio de Acessos'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/portfolioacessos/Portfliodeacessos.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('portfliodeAcessos',state);
          
			var state = {
              url: '/cAIXASeguros',
              ncyBreadcrumb: {
	            	label: 'CAIXA Seguros'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/caixaseguros/Caixaseguros.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('cAIXASeguros',state);
          
			var state = {
              url: '/declaracaodeDados',
              ncyBreadcrumb: {
	            	label: 'Declaração de Dados'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Declaracaodedados.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('declaracaodeDados',state);
          
			var state = {
              url: '/assinaturadoGerente',
              ncyBreadcrumb: {
	            	label: 'Assinatura do Gerente'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Assinaturadogerente.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('assinaturadoGerente',state);
          
			var state = {
              url: '/autorizacaoDocumental',
              ncyBreadcrumb: {
	            	label: 'Autorização Documental'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Autorizacaodocumental.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('autorizacaoDocumental',state);
			
			var state = {
              url: '/consultaAvaliacesValidas',
              ncyBreadcrumb: {
	            	label: 'Consulta Avaliações Válidas'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Consultaavaliacesvalidas.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('consultaAvaliacesValidas',state);
          
			var state = {
              url: '/avaliacaodeRiscoClienteComercial',
              ncyBreadcrumb: {
	            	label: 'Avaliação de Risco Cliente Comercial'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Avaliacaoderiscoclientecomercial.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('avaliacaodeRiscoClienteComercial',state);
          
			var state = {
              url: '/consultaAvaliacesValidasAprovadas',
              ncyBreadcrumb: {
	            	label: 'Consulta Avaliações Válidas Aprovadas'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Consultaavaliacesvalidasaprovadas.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('consultaAvaliacesValidasAprovadas',state);
			
			var state = {
              url: '/avaliacaodeRiscoOperacao',
              ncyBreadcrumb: {
	            	label: 'Avaliação de Risco Operação'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Avaliacaoderiscooperacao.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('avaliacaodeRiscoOperacao',state);
			
			var state = {
              url: '/aberturadeConta',
              ncyBreadcrumb: {
	            	label: 'Abertura de Conta'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Aberturadeconta.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
					  			
          $stateProvider.state('aberturadeConta',state);
          
			var state = {
              url: '/assinaturadoGerenteFinal',
              ncyBreadcrumb: {
	            	label: 'Assinatura do Gerente Final'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Assinaturadogerentefinal.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
          $stateProvider.state('assinaturadoGerenteFinal',state);
          
			var state = {
              url: '/aberturadeContaResultado',
              ncyBreadcrumb: {
	            	label: 'Abertura de Conta Resultado'
						
              
              },
              
              views: {
                  'header-container': {
                      templateUrl: 'pages/header.html'
                  },
                  'page-container': {
                      templateUrl: 'pages/aberturadeconta/Aberturadecontaresultado.html'
                  },
                  'menu-container': {
                      templateUrl: 'pages/menu.html'
                  },
                  'footer-container': {
                      templateUrl: 'pages/footer.html'
                  }
              }                
          };
          
					  			
          $stateProvider.state('aberturadeContaResultado',state);
          
          
          
          var state = {
                  url: '/relatorios',
                  ncyBreadcrumb: {
    	            	label: 'Relatórios de Documentos'
    						
                  
                  },
                  
                  views: {
                      'header-container': {
                          templateUrl: 'pages/header.html'
                      },
                      'page-container': {
                          templateUrl: 'pages/relatorios/Relatorios.html'
                      },
                      'menu-container': {
                          templateUrl: 'pages/menu.html'
                      },
                      'footer-container': {
                          templateUrl: 'pages/footer.html'
                      }
                  }                
              };
          
          $stateProvider.state('relatorios',state);
          
          
          var state = {
                  url: '/parametrizacaofuncoes',
                  ncyBreadcrumb: {
    	            	label: 'Parametrizacao de Funcoes por Tipos Documentais'
                  },
                  
                  views: {
                      'header-container': {
                          templateUrl: 'pages/header.html'
                      },
                      'page-container': {
                          templateUrl: 'pages/parametrizacao/ParametrizacaoFuncoes.html'
                      },
                      'menu-container': {
                          templateUrl: 'pages/menu.html'
                      },
                      'footer-container': {
                          templateUrl: 'pages/footer.html'
                      }
                  }                
              };
          
          $stateProvider.state('parametrizacaofuncoes',state);
          
          var state = {
                  url: '/parametrizacaoniveldocumental',
                  ncyBreadcrumb: {
    	            	label: 'Parametrizacao de Níveis Documentais'
                  },
                  
                  views: {
                      'header-container': {
                          templateUrl: 'pages/header.html'
                      },
                      'page-container': {
                          templateUrl: 'pages/parametrizacao/ParametrizacaoNivelDocumental.html'
                      },
                      'menu-container': {
                          templateUrl: 'pages/menu.html'
                      },
                      'footer-container': {
                          templateUrl: 'pages/footer.html'
                      }
                  }                
              };
          
          $stateProvider.state('parametrizacaoniveldocumental',state);
          
          
          var state = {
                  url: '/parametrizacaoproduto',
                  ncyBreadcrumb: {
    	            	label: 'Parametrizacao de Produtos'
                  },
                  
                  views: {
                      'header-container': {
                          templateUrl: 'pages/header.html'
                      },
                      'page-container': {
                          templateUrl: 'pages/parametrizacao/ParametrizacaoProduto.html'
                      },
                      'menu-container': {
                          templateUrl: 'pages/menu.html'
                      },
                      'footer-container': {
                          templateUrl: 'pages/footer.html'
                      }
                  }                
              };
          
          $stateProvider.state('parametrizacaoproduto',state);
          


        // if none of the above states are matched, use this as the fallback
        $urlRouterProvider.otherwise('/principal');
    });