'use strict';

angular.module('myApp.Interceptor', [])

    .config(
        ['$httpProvider',
            function ($httpProvider) {
                /*
                 * Esse interceptor foi definido de forma anonima
                 */
                $httpProvider.interceptors.push(function ($q, $log) {

                    return {

                        'request': function (config) {
                            $log.info(config);
                            var url = location.href;
                            if (url.indexOf('#/') != -1) {
                            	url = url.split('#/')[0];
                            }
                            config.headers['Access-Control-Allow-Origin'] = url;
                            if (keycloak && config.url && config.url.indexOf('/rest') != -1) {
                            	config.headers['Authorization'] = 'Bearer ' + keycloak.token;
                            }
                            
                            if (keycloak) {
                            	keycloak.updateToken(5).success(function (token) {
                            		if (token) {
                            			localStorage['token'] = keycloak.token;
                            			var keycloakService =  angular.element(document.body).injector().get('KeycloakService');
                            			if (keycloakService) {
                            				keycloakService.loadKeycloak(keycloak);
                            			}
                            			var user =keycloak.idTokenParsed;
                            			if (user['preferred_username'] == null || user['preferred_username'] === 'undefined') {
                            				localStorage['usuarioSSO'] = JSON.stringify(userTest);
                            			} else {
                            				localStorage['usuarioSSO'] = JSON.stringify(user);
                            			}
                            		}
                            	});
                            }
                            
                            if (config.beforeSend)
                            	config.beforeSend();
                            
                            return config;
                        },

                        'requestError': function (rejection) {

                            $log.info(rejection);

                            if (error.status === 401) {
                                $rootScope.$broadcast('unauthorized', error);
                                $location.path('/login');
                                return $q.reject(rejection);
                            }

                            //
                            // return responseOrNewPromise;
                            // }
                            //
                            // return $q.reject(rejection);
                        },

                        'response': function (response) {
                            $log.info(response);

                            if (response.config.complete)
                                response.config.complete(response);
                            
                            return response || $q.when(response);
                        },

                        'responseError': function (rejection) {
                            // do something on error
                            // if (canRecover(rejection)) {
                            // return responseOrNewPromise
                            // }
                            // return $q.reject(rejection);

                            $log.info(rejection);

                            return $q.reject(rejection);
                        }

                    };

                })

            }]);
