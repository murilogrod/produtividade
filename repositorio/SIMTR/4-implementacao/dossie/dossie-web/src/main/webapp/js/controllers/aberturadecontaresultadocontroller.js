'use strict';
angular.module('webApp').controller('AberturadecontaresultadoController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService,  Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AberturadecontaresultadoController()");
    Analytics.trackEvent('controller','AberturadecontaresultadoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'aberturadeContaResultado';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Abertura de Conta Resultado';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.nome = SicpfService.getSicpf()['nome-contribuinte'];
    $scope.chequeEspecial = DossieService.getChequeEspecial();
    $scope.gerarAssinaturaEletronica = DossieService.getGerarAssinaturaEletronica();
    $scope.sms = DossieService.getSms();
    
    $scope.advance = function () {
    	$rootScope.go('principal');
    }
});