angular.module("caixa.analytics", [])
	
	.factory('WebServiceRest', function() {
		var ws = function(method, url, data, headers) {
	    	var urlContext = url;
	        return $.ajax({
	            type: method,
	            url: urlContext,
	            headers: headers,
	            contentType: 'application/json',
	            dataType: 'json',
	            data: data
	        });
	    }
	    return {
	        read: function(url, headers) {
	            return ws('GET', url, {}, headers);
	        },
	        readAll: function(url, data, headers) {
	            return ws('GET', url, data, headers);
	        },
	        create: function(url, data, headers) {
	            return ws('POST', url, data, headers);
	        },
	        update: function(url, data, headers) {
	            return ws('PUT', url, data, headers);
	        },
	        deleta: function(url, headers) {
	            return ws('DELETE', url, {}, headers);
	        }
	    }
	})

	.factory('ISPService', function (Log,Error) {
        var VERSION = '1.0.0';        
        var SOURCE = "https://extreme-ip-lookup.com/json/";
        return {
            version: function() {
                return VERSION;
            },
            get: function (success, error) {
                try {
                    var url = SOURCE;
                    $.ajax({
                        type: "GET",
                        crossDomain: true,
                        url: url,
                        cache: true,
                        timeout: TIMEOUT,
                        success: success,
                        error: error
                    });
                } catch (error) {
                    Error.handler("Falha ao processar requisição", error);
                }
            }
        }
    })//v1.0.0

    .factory('Analytics', function ($location, $window,WebServiceRest,ISPService, $rootScope) {
        var VERSION = '1.0.0';
        HOME = URL_CAIXA_ANALYTICS;
        var enabled = false;
        var key = null;
        var session = null;
        var category = "";
        var INIT_EVENT = "INIT";
        var INIT_AGENT = "AGENT";
        var NOTE = "NOTE";
        var ISP = "ISP";
        var PAGE = "page";
        var ERROR = "error";
        var WARN = "warn";
        var TIMEOUT = 120000;
        var userAgent = null;

        var soname = null;
        var sover = null;
        var navname = null;
        var navver = null;
        var devname = null;

        var evaluationRate = null;
        var userFeedback = null;
        var provedor = null;

        var EVENT = HOME + "event";
        
        var token = null;

        if ($window && $window.navigator && $window.navigator.userAgent) {
            userAgent = $window.navigator.userAgent
        }
               
        function getEvaluationRest() {
            /*
        	WebServiceRest.readAll(URL_CAIXA_ANALYTICS + "ws/json/listarEvaluationUserFeedback?json=evaluationRate", null, $rootScope.headers)
        	.then(function(res) {
        		if(!res.temErro) {
        			var response = res;
                    evaluationRate = JSON.parse(response.message);
        		}else if(res.temErro) {        			
        			console.error(res.msgsErro[0] + ": " + json);
        		}
        	}, function(xhr, status, err) {
        		console.error("Ocorreu um erro ao obter o json Evaluation: " + xhr.responseText);
        	});
            */
        }
        function getUserFeedbackRest() {
            /*
        	WebServiceRest.readAll(URL_CAIXA_ANALYTICS + "ws/json/listarEvaluationUserFeedback?json=userFeedback", null, $rootScope.headers)
        	.then(function(res) {
        		if(!res.temErro) {
        			var response = res;
                    userFeedback = JSON.parse(response.message);
        		}else if(res.temErro) {        			
        			console.error(res.msgsErro[0] + ": " + json);
        		}
        	}, function(xhr, status, err) {
        		console.error("Ocorreu um erro ao obter o json UserFeedback: " + xhr.responseText);
        	});
            */
        }
        
        getEvaluationRest();
        getUserFeedbackRest();
        
       
        function getStamp(name) {
            var result = null;
            try {
                result = window.localStorage.getItem(name);
                if (result == "" || result == "null") {
                    result = null;
                }
            } catch (ignore) {
            }
            return result;
        }
        function setStamp(name,value) {
            try {
                window.localStorage.setItem(name,value);
            } catch (ignore) {
            }
        }

        function replaceAll(str,find,replace) {
            function escapeRegExp(str) {
                return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
            }
            return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
        }
                
        function getRandom(min,max) {
            return Math.floor(Math.random()*(max-min+1)+min);
        }
        function makeid() {
            return getRandom(1,2147483647);
        }

        function getFakeEnvironment() {
            var environments = [
                {id:1, name: "Windows7/Chrome5", devname:"Desktop",soname:"Windows",sover:"7",navname:"Chrome",navver:"54"},
                {id:2, name: "Windows7/Firefox4", devname:"Desktop",soname:"Windows",sover:"7",navname:"Firefox",navver:"49"},
                {id:3, name: "Windows7/IE10", devname:"Desktop",soname:"Windows",sover:"7",navname:"Microsoft Internet Explorer",navver:"10"},
                {id:4, name: "Windows10/IE11", devname:"Desktop",soname:"Windows",sover:"10",navname:"Microsoft Internet Explorer",navver:"11"},
                {id:5, name: "Windows10/Edge", devname:"Desktop",soname:"Windows",sover:"10",navname:"Microsoft Edge",navver:"14"},
                {id:6, name: "Android4/Chrome5", devname:"Mobile",soname:"Android",sover:"43",navname:"Chrome",navver:"54"},
                {id:7, name: "Android5/Chrome4", devname:"Mobile",soname:"Android",sover:"50",navname:"Chrome",navver:"48"},
                {id:8, name: "Android6/Chrome5", devname:"Mobile",soname:"Android",sover:"601",navname:"Chrome",navver:"54"},
                {id:9, name: "iOS93/Safari9", devname:"Mobile",soname:"iOS",sover:"935",navname:"Safari",navver:"9"}
            ]
            return environments;
        }

        function fail(data) {
            if (data && data.statusText) {
                console.log(data.statusText);
            } else {
                console.log("Analytics: falha ao obter recurso externo");
            }

        }
                       
        function fakeEvent(device, key, session, category, action, label, value) {
        	
        	var json = montarJson(key, session, category, action, label, value, device);
        	
        	enviarEventoRest(json);
        	            
        }

        function montarJson(key, session, category, action, label, value, device) {
        	var json = '{';
        	json +=    '	"chave": "' + key +'",';
        	if (session){
        		json +=    '	"sessao": "' + session +'",';
        	} else {
        		json +=    '	"sessao": "",';
        	}
        	json +=    '	"categoria": "' + category +'",';
        	json +=    '	"acao": "' + action +'",';
        	json +=    '	"etiqueta": "' + label +'",';
        	if (value) {
        		json +=    '	"valor": "' + value +'",';
        	}else {
        		json +=    '	"valor": "",';
        	}
        	
        	if (device!=null && device["soname"]){
        		json +=    '	"nomeSistemaOperacional": "'+ device["soname"] +'",';
        	}else{
        		json +=    '	"nomeSistemaOperacional": "",';        		
        	}
        	
        	if (device!=null && device["sover"]) {
        		json +=    '	"versaoSistemaOperacional": "'+ device["sover"] +'",';
        	}else{
        		json +=    '	"versaoSistemaOperacional": "",';        		
        	}
        	
        	if (device!=null && device["navname"]) {
        		json +=    '	"nomeNavegador": "'+ device["navname"] +'",';
        	}else{
        		json +=    '	"nomeNavegador": "",';        		
        	}
        	
        	if (device!=null && device["navver"]) {
        		json +=    '	"versaoNavegador": "'+ device["navver"] +'",';
        	}else{
        		json +=    '	"versaoNavegador": "",';        		
        	}
        	
        	if (device!=null && device["devname"]) {
        		json +=    '	"dispositivo": "'+ device["devname"] +'",';
        	}else{
        		json +=    '	"dispositivo": "",';        		
        	}
        	
        	json +=    '	"userAgent": "' + userAgent +'",';
        	
        	if (token!=null) {
        		json +=    '	"token": "'+ token +'"';
        	} else {
        		json +=    '	"token": ""';
        	}
        	
        	json += '}';
        	
        	return json;
        }
        
        function enviarEventoRest(json) {
        	var url = null;
        	if (token==null) {
        		url = URL_CAIXA_ANALYTICS + "ws/evento/registrar";
        	} else {
        		url = URL_CAIXA_ANALYTICS + "ws/evento/registrarAutenticado";
        	}
 // Desativando Analytics
//        	WebServiceRest.create(url, json, null)
//        	.then(function(res) {
//        		if(!res.temErro) {
//        			//console.info(res.msgsErro[0]);
//        		}else if(res.temErro) {        			
//        			console.error(res.msgsErro[0] + ": " + json);
//        		}
//        	}, function(xhr, status, err) {
//        		console.error("Ocorreu um erro ao enviar o evento: " + xhr.statusText);
//        	});
        }
        
        function registerEvent(category, action, label, value) {
        	
        	var json = montarJson(key, session, category, action, label, value, null);
        	        	
        	enviarEventoRest(json);
        }
        
        function inicializar(id,newcategory,devmode) {
        	
        	if (!id) return;
            key = id;
            if (newcategory) category = newcategory;
            session = makeid();
            var lastsession = getStamp(category);

            enabled = true;

            this.trackEvent(INIT_EVENT,lastsession);

            if (userAgent != null) {
                this.trackEvent(INIT_AGENT,userAgent);
            }

            setStamp(category,session);
            
        }
        
        function registrarProvedor() {
        	        	
        	function checarProvedorIp() {        		
            	WebServiceRest.readAll(URL_CAIXA_ANALYTICS + "ws/json/checarProvedor", null, null)
            	.then(function(res) {
            		if(!res.temErro) {
            			var response = res;
                        provedor = JSON.parse(response.message);
                        Analytics.trackEvent("ISP", provedor);
            		}else if(res.temErro) {
            			function success(data) {                            
                            try {
                                if (data) {
                                    $rootScope.provider = data.org;
                                    if (!$rootScope.provider) {
                                        $rootScope.provider = data.isp;
                                    }
                                    Analytics.trackEvent("ISP", $rootScope.provider);
                                }
                            } catch (ignore) {
                            }
                        }
            			function error(data) {
                            console.error("Não foi possível obter as informações do provedor");                            
                        }
                        ISPService.get(success, error);                        
            		}
            	}, function(xhr, status, err) {
            		console.error("Ocorreu um erro ao verificar se o provedor já está cadastrado: " + xhr.responseText);
            	});
            }                        
        	checarProvedorIp();
        	
        }        
        
        return {
            init: function (id,newcategory,devmode) {
            	this.configs(id,newcategory,devmode);                
            },
            initAuth: function (user, pass,id,newcategory,devmode) {
            	this.auth(user, pass,id,newcategory,devmode);                
            },
            auth: function (user, pass,id,newcategory,devmode) {
            	
            	var json = { usuario : user, senha: pass};            	            	
            	            	
            	$.ajax({
            		type: "POST",
            		url: URL_CAIXA_ANALYTICS + "ws/autenticacao/autenticarInterface",
            		data: JSON.stringify(json),
            		contentType: 'application/json',
                    dataType: 'json',                    
            		success: function (res) {
            			if(!res.temErro) {        			
                            token = res.user.token;
                		}else if(res.temErro) {        			
                			console.error(res.message + ": " + json);
                		}
            		},
            		error: function (xhr) {
            			var cause = null;
                        if (xhr != null && xhr.responseText) cause = xhr.responseText
                        if (cause == null && xhr != null && xhr.statusText) cause = xhr.statusText;
                        console.error("Ocorreu um erro ao se autenticar " + xhr.responseText);  
            		},            		
            		async: false
            	 });
            	
            	this.configs(id,newcategory,devmode);
            },
            configs: function (id,newcategory,devmode) {
            	if (!id) return;
                key = id;
                if (newcategory) category = newcategory;
                session = makeid();
                var lastsession = getStamp(category);

                enabled = true;

                this.trackEvent(INIT_EVENT,lastsession);

                if (userAgent != null) {
                    this.trackEvent(INIT_AGENT,userAgent);
                }

                setStamp(category,session);
                
                registrarProvedor();
            },
            setEnable: function(value) {
                enabled = value;
            },
            setSession: function(id) {
                session = id;
            },
            setCategory: function(id) {
                category = id;
            },
            isEnabled: function() {
                return enabled;
            },
            trackWarn: function(label, value) {
                if (!enabled) return;
                if (!key) return;

                registerEvent(category,WARN,label,value);
            },
            trackException: function(label, value) {
                if (!enabled) return;
                if (!key) return;

                registerEvent(category,ERROR,label,value);
            },
            trackEvent: function(action, label, value) {
                if (!enabled) return;
                if (!key) return;

                registerEvent(category,action,label,value);
            },
            trackPage: function(page, value) {
                if (!enabled) return;
                if (!key) return;

                registerEvent(category,PAGE,page,value);
            },
            fakeInit: function(device, key, session) {
                fakeEvent(device, key, session, category, INIT_EVENT);
            },
            fakeSession: function(device, key, session) {
                fakeEvent(device, key, session, category, SESSION_EVENT);
            },
            fakeError: function(device, key, session, category, label, value) {
                fakeEvent(device, key, session, category, ERROR, label, value);
            },
            fakeWarn: function(device, key, session, category, label, value) {
                fakeEvent(device, key, session, category, WARN, label, value);
            },           
            fakeNote: function(device, key, session,category, label, value) {
                fakeEvent(device, key, session, category, NOTE, label, value);
            },
            fakeEvent: function(device, key, session, category, action, label, value) {
                return fakeEvent(device, key, session, category, action, label, value);
            },
            getFakeEnvironment: function() {
                return getFakeEnvironment();
            },
            getEvaluationRate: function() {
                return evaluationRate;
            },
            getUserFeedback: function() {
                return userFeedback;
            },
            isFeedbackReady: function() {
                return userFeedback != null && evaluationRate != null;
            }
        }
    }) //Analytics.1.0.9
    
    .directive('analyticsEvent', function(Analytics) {
        var help = {};
        return {
            restrict: 'A',
            link: function(scope, $elm, attrs) {
                $elm.click(function(){
                    var element = $($elm);
                    var event = attrs.analyticsEvent;
                    if (!event) return;

                    Analytics.trackEvent(scope.pagename,"click:" + event);
                });
            }
        }
    })//Analytics.1.0.7

    .directive('analyticsPage', function(Analytics) {
        var help = {};
        return {
            restrict: 'A',
            link: function(scope, $elm, attrs) {
                var element = $($elm);
                var page = attrs.analyticsPage;
                if (!page) return;

                scope.pagename = page;
                Analytics.trackPage(scope.pagename);
            }
        }
    });//Analytics.1.0.7