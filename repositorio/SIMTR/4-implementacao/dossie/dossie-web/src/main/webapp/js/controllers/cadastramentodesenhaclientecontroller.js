'use strict';
angular.module('webApp').controller('CadastramentodesenhaclienteController', function ($scope, $rootScope, $translate, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("CadastramentodesenhaclienteController()");
    Analytics.trackEvent('controller','CadastramentodesenhaclienteController');
    $scope.container = {};     
    $rootScope.currentview.id = 'cadastramentodeSenhaCliente';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Cadastramento de Senha Cliente';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.advance = function () {
    	if (DossieService.verifyPassword($scope.container.password,$scope.container.retypePassword)) {
    		DossieService.setPassword($scope.container.password);
    		$rootScope.$broadcast(START_LOADING);
    		DossieService.putBackUrl('cadastramentodeSenhaCliente');
    		$rootScope.go('declaracaodeDados');
    	} else {
    		$rootScope.putErrorString('Senhas não conferem');
    	}
    }
    
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.hideAlert();
});