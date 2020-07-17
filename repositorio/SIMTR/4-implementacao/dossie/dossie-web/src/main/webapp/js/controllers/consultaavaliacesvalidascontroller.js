'use strict';
angular.module('webApp').controller('ConsultaavaliacesvalidasController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("ConsultaavaliacesvalidasController()");
    Analytics.trackEvent('controller','ConsultaavaliacesvalidasController');
    $scope.container = {};     
    $rootScope.currentview.id = 'consultaAvaliacesValidas';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Consulta Avaliações Válidas';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});