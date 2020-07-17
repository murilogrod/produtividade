'use strict';
angular.module('webApp').controller('InformacesdoclienteeparametrosdacontaController', function ($scope, $rootScope, $translate, $http, SicliService, SicpfService, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("InformacesdoclienteeparametrosdacontaController()");
    Analytics.trackEvent('controller','InformacesdoclienteeparametrosdacontaController');
    $scope.container = {};     
    $rootScope.currentview.id = 'informacesdoClienteeParametrosdaConta';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Informações do Cliente e Parâmetros da Conta';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    $scope.sicli = SicliService.getSicli();
    $scope.cpf = SicpfService.getCpf();
    $scope.sicpf = SicpfService.getSicpf();
    $scope.operacaoSelected = 1;
    $scope.operacoes = [ { label : "001 - Conta Corrente PF" , value : 1}];
    $scope.insert  = function () {
    	var obj = {
    			cpf_cliente : Number($scope.cpf),
    			integracao : INTEGRACAO,
    			operacao : $scope.operacaoSelected,
    			modalidade : 0
        };
        
    	$rootScope.$broadcast(START_LOADING);
    	$http.post(URL_SIMTR + SERVICO_AUTORIZACAO , obj).then(function (res) {
    		DossieService.putBackUrl('informacesdoClienteeParametrosdaConta');
    		DossieService.setAutorizacao(res.data);
    		$rootScope.go('pesquisasCadastrais');
    	} , function (error) {
    		$rootScope.putError(error);
    	});
    };
    
});