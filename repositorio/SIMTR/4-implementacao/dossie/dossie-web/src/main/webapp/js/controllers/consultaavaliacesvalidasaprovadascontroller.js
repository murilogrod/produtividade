'use strict';
angular.module('webApp').controller('ConsultaavaliacesvalidasaprovadasController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("ConsultaavaliacesvalidasaprovadasController()");
    Analytics.trackEvent('controller','ConsultaavaliacesvalidasaprovadasController');
    $scope.container = {};     
    $rootScope.currentview.id = 'consultaAvaliacesValidasAprovadas';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Consulta Avaliações Válidas Aprovadas';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});