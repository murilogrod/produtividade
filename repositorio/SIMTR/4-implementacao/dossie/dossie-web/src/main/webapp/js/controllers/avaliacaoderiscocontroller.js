'use strict';
angular.module('webApp').controller('AvaliacaoderiscoController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AvaliacaoderiscoController()");
    Analytics.trackEvent('controller','AvaliacaoderiscoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'avaliacaodeRisco';
    $rootScope.currentview.group = 'AvaliacaoRisco';
    $rootScope.currentview.title = 'Avaliação de Risco';
    $rootScope.currentview.icon = 'fa-flag-o';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});