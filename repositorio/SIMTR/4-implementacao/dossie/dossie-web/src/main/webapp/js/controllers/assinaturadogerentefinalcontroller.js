'use strict';
angular.module('webApp').controller('AssinaturadogerentefinalController', function ($scope, $rootScope, $translate , $http, DossieService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AssinaturadogerentefinalController()");
    Analytics.trackEvent('controller','AssinaturadogerentefinalController');
    $scope.container = {};     
    $rootScope.currentview.id = 'assinaturadoGerenteFinal';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Assinatura do Gerente Final';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
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
		$rootScope.$broadcast(START_LOADING);
		DossieService.putBackUrl('assinaturadoGerenteFinal');
		$rootScope.go('aberturadeContaResultado');
    }
    
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.hideAlert();
    $rootScope.currentview.description = '';
});