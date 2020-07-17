'use strict';
angular.module('webApp').controller('DigitalizacaodocidentificacaoabertoController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("DigitalizacaodocidentificacaoabertoController()");
    Analytics.trackEvent('controller','DigitalizacaodocidentificacaoabertoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaoDocIdentificacaoAberto';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Digitalização Doc Identificação Aberto';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});