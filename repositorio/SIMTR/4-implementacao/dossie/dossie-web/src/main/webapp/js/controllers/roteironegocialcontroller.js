'use strict';
angular.module('webApp').controller('RoteironegocialController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("RoteironegocialController()");
    Analytics.trackEvent('controller','RoteironegocialController');
    $scope.container = {};     
    $rootScope.currentview.id = 'roteiroNegocial';
    $rootScope.currentview.group = 'RoteiroNegocial';
    $rootScope.currentview.title = 'Roteiro Negocial';
    $rootScope.currentview.icon = 'fa-file-text-o';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});