'use strict';
angular.module('webApp').controller('ResumodoclienteController', function ($scope, $rootScope, $translate, $http, SicliService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("ResumodoclienteController()");
    Analytics.trackEvent('controller','ResumodoclienteController');
    $scope.container = {};  
    $scope.cpf = SicpfService.getCpf();
    $scope.sicpf = SicpfService.getSicpf();
    $scope.sicli = SicliService.getSicli();
    $rootScope.currentview.id = 'resumodoCliente';
    $rootScope.currentview.group = 'Resumo';
    $rootScope.currentview.title = 'Resumo do Cliente';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    $scope.telefones = [];
    $scope.emails = [];
    $scope.rendaTotal = 0;
    if (SicpfService.isNewSearch()) {
    	$scope.sicli = null;
    }
    $scope.loadRendaAndContatos = function () {
    	if ($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO) {
			for (var i  = 0 ; i < $scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO.length ; i++) {
				if ($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO[i].meioComunicacao == 11 || 
						$scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO[i].meioComunicacao == 14) {
					$scope.telefones.push($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO[i]);
				}
				if ($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO[i].meioComunicacao == 20) {
					$scope.emails.push($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.MEIO_COMUNICACAO[i]);
				}
			}
		}
		
		if ($scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.RENDA) {
			for (var i = 0 ; i < $scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.RENDA.length; i++) {
				$scope.rendaTotal += $scope.sicli.DADOS.cliente.CLIENTE.CAMPOS_RETORNADOS.RENDA[i].vrrendabruta;
			}
		}
    }
    
    $scope.loadData = function () {
    	if ($scope.sicli == null || $scope.sicli === 'undefined' || SicpfService.isNewSearch()) {
    		$http.get(URL_SIMTR + SERVICO_SICLI + '/cpf/' + Number($scope.cpf) + '/sicli').then(function (res) {
    			if (res.data != null && res.data != '' && res.data !== 'undefined' && res.data.COD_RETORNO == '00' && res.data.DADOS.CONTROLE_NEGOCIAL.COD_RETORNO != '03') {
    				SicliService.setSicli(res.data);
    				SicliService.setCpf($scope.cpf);
    				$scope.sicli = res.data;
    				$scope.loadRendaAndContatos();
    				
    				SicpfService.setNewSearch(false);
    			} 
    		});
    	} else {
    		$scope.loadRendaAndContatos();
    	}
    }
    
    $scope.loadData();
    
    $(".page-loader").fadeOut(1000, "linear");
});