'use strict';
angular.module('webApp').controller('AssinaturadogerenteController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AssinaturadogerenteController()");
    Analytics.trackEvent('controller','AssinaturadogerenteController');
    $scope.container = {};     
    $rootScope.currentview.id = 'assinaturadoGerente';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Assinatura do Gerente';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.reset = function () {
    	$scope.tokenWasMade = false;
        $scope.firstTokenApproved = false;
        $scope.secondTokenApproved = false;
        $scope.token = null;
        
        $scope.firstPassword = null;
        $scope.secondPassword = null;
    }
    $scope.reset();
    
    $scope.makeToken = function () {
    	$scope.reset();
    	$scope.token = Math.floor(Math.random() * ( 999999 - 100000   + 1)) + 100000 ;
    	$scope.tokenWasMade = true;
    }
    
    $scope.approveFirstPassword = function () {
    	$scope.firstTokenApproved = $scope.token == $scope.firstPassword;
    	if (!$scope.firstTokenApproved) {
    		$rootScope.putErrorString('Senha não confere');
    	}
    }
    
    $scope.approveSecondPassword = function () {
    	$scope.secondTokenApproved = $scope.token == $scope.secondPassword;
    	if (!$scope.secondTokenApproved) {
    		$rootScope.putErrorString('Senha não confere');
    	}
    }
    
    $scope.advance = function () {
    	$scope.autorizacao = DossieService.getAutorizacao();
    	if (typeof $scope.autorizacao !== 'undefined' && $scope.autorizacao !== null && $scope.autorizacao.autorizado) {
    		DossieService.putBackUrl('assinaturadoGerente');
    		$rootScope.go('autorizacaoDocumental');
    	} else {
    		var obj = {
        			cpf_cliente : Number(SicpfService.getCpf()),
        			integracao : INTEGRACAO,
        			operacao : OPERACAO_CONTA_PF,
        			modalidade : MODALIDADE_CONTA_PF
        	};
    		$rootScope.$broadcast(START_LOADING);
        	$http.post(URL_SIMTR + SERVICO_AUTORIZACAO , obj).then(function (res) {
        		DossieService.putBackUrl('assinaturadoGerente');
        		DossieService.setAutorizacao(res.data);
        		$rootScope.go('autorizacaoDocumental');
        	} , function (error) {
        		DossieService.putBackUrl('assinaturadoGerente');
        		$rootScope.go('autorizacaoDocumental');
        	});
    	}
    }
    
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.hideAlert();
});