	
//	var keycloakURL = location.href;
//	 if (keycloakURL.indexOf('#') != -1) {
//		 keycloakURL = keycloakURL.split('#')[0];
//     }
//	 keycloakURL += 'keycloak.json';
	
	$.ajax({
			type: 'GET',
			url: 'ws/servidor/propriedades/urlapi',
			contentType: 'application/json',
			dataType: 'json',
			success: function (result) {
				URL_SIMTR_API = result.urlSimtrApi;
				URL_SSO_AUTH = result.urlSsoAuth;
				URL_SIMTR = URL_SIMTR_API + '/simtr-api/rest';
			},
			async: false
		 });

	console.log(URL_SIMTR_API);
	console.log(URL_SSO_AUTH);

	 var keycloak = new Keycloak({
		  "realm": "intranet",
		  "url": URL_SSO_AUTH,
		  "ssl-required": "external",
		  "clientId": "cli-web-mtr",
		  "public-client": true
		});

		
	//var keycloak = new Keycloak(keycloakURL);
	
	keycloak.onTokenExpired = function () {
		keycloak.updateToken(-1).success(function (token) {
			if (token) {
				localStorage['token'] = keycloak.token;
				updateKeycloak();
			} else {
				reset();
			}
		}).error(function (error){
			reset();
		});
	}
	
	function updateKeycloak() {
		var keycloakService =  angular.element(document.body).injector().get('KeycloakService');
		if (keycloakService) {
			keycloakService.loadKeycloak(keycloak);
		}
		updateUserSSO();
	}
	
	function reset () {
		keycloak.loginRequired = false;
		keycloak.logout();
		keycloak.clearToken();
		delete localStorage['SSO-CALLED'];
	}
	
	keycloak.onAuthSuccess = function (res){
		localStorage['token'] = keycloak.token;
	};
	
	var url = window.location.href;
	if (localStorage['SSO-CALLED'] !== 'undefined' && localStorage['SSO-CALLED'] === 'true') {
		url = window.location.href;
	}
	
	angular.element(document).ready(function() {
		if (localStorage['SSO-CALLED'] !== 'undefined' && localStorage['SSO-CALLED'] === 'true') {
			try {
				if (localStorage['token'] !== 'undefined' && localStorage['token'] != null && url.indexOf('#state') == -1 && keycloak && keycloak.isTokenExpired(5)){
					callSSO();
				} else {
					keycloak.init({	onLoad: 'check-sso', url : url}).success(function (res) {
						if (res) {
							localStorage['token'] = keycloak.token;
							updateUserSSO();
							delete localStorage['SSO-CALLED'];
							angular.bootstrap(document, ['webApp']);
						}
					});
				}
			} catch (error) {
				if (error == 'Not authenticated') {
					callSSO();
				}
			}
		} else {
			callSSO();
		}
	});
	
	function updateUserSSO() {
		var user = decodeToken(keycloak.idToken);
		if (user['preferred_username'] == null || user['preferred_username'] === 'undefined') {
			localStorage['usuarioSSO'] = JSON.stringify(userTest);
		} else {
			localStorage['usuarioSSO'] = JSON.stringify(user);
		}
	}
	
	function callSSO() {
		localStorage['SSO-CALLED']  = true;
		keycloak.init().success(function (res) {
    		keycloak.loginRequired = false;
    		keycloak.logout();
    		keycloak.clearToken();
    		var keys = Object.keys(localStorage);
        	for (var i = 0 ; i < keys.length ; i++) {
        		if (keys[i].indexOf('kc-callback') != -1) {
        			delete localStorage[keys[i]];
        		}
        	}
    		keycloak.login({redirectUri : location.href });
		});
	}
	
	
