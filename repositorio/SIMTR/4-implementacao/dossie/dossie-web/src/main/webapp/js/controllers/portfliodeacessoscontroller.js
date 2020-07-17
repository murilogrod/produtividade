'use strict';
angular.module('webApp').controller('PortfliodeacessosController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("PortfliodeacessosController()");
    Analytics.trackEvent('controller','PortfliodeacessosController');
    $scope.container = {};     
    $rootScope.currentview.id = 'portfliodeAcessos';
    $rootScope.currentview.group = 'PortfolioAcessos';
    $rootScope.currentview.title = 'Portfólio de Acessos';
    $rootScope.currentview.icon = 'fa-calendar-o';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});