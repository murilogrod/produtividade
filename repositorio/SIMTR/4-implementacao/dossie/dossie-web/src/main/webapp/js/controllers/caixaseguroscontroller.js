'use strict';
angular.module('webApp').controller('CaixasegurosController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("CaixasegurosController()");
    Analytics.trackEvent('controller','CaixasegurosController');
    $scope.container = {};     
    $rootScope.currentview.id = 'cAIXASeguros';
    $rootScope.currentview.group = 'CaixaSeguros';
    $rootScope.currentview.title = 'CAIXA Seguros';
    $rootScope.currentview.icon = 'fa-heartbeat';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});