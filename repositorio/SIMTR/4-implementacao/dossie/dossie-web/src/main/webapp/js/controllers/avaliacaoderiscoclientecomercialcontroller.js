'use strict';
angular.module('webApp').controller('AvaliacaoderiscoclientecomercialController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AvaliacaoderiscoclientecomercialController()");
    Analytics.trackEvent('controller','AvaliacaoderiscoclientecomercialController');
    $scope.container = {};     
    $rootScope.currentview.id = 'avaliacaodeRiscoClienteComercial';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Avaliação de Risco Cliente Comercial';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});