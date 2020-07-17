'use strict';
angular.module('webApp').controller('PerfilController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("PerfilController()");
    Analytics.trackEvent('controller','PerfilController');
    $scope.container = {};     
    $rootScope.currentview.id = 'perfil';
    $rootScope.currentview.group = 'Login';
    $rootScope.currentview.title = 'Perfil';
    $rootScope.currentview.icon = 'fa-user';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = 'Tela de Perfil do Usuário';
});