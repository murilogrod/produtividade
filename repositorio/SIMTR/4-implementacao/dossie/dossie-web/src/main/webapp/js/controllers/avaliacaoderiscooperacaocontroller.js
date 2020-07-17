'use strict';
angular.module('webApp').controller('AvaliacaoderiscooperacaoController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AvaliacaoderiscooperacaoController()");
    Analytics.trackEvent('controller','AvaliacaoderiscooperacaoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'avaliacaodeRiscoOperacao';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Avaliação de Risco Operação';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});