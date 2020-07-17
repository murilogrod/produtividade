'use strict';
angular.module('webApp').controller('DigitalizacaocomprendaabertoController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("DigitalizacaocomprendaabertoController()");
    Analytics.trackEvent('controller','DigitalizacaocomprendaabertoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaoCompRendaAberto';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Digitalização Comp Renda Aberto';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});