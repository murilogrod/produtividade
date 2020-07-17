'use strict';
angular.module('webApp').controller('CadastroController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("CadastroController()");
    Analytics.trackEvent('controller','CadastroController');
    $scope.container = {};     
    $rootScope.currentview.id = 'cadastro';
    $rootScope.currentview.group = 'Cadastro';
    $rootScope.currentview.title = 'Cadastro';
    $rootScope.currentview.icon = 'fa-pencil-square-o';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});
