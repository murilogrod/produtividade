'use strict';
angular.module('webApp').controller('DossidigitalController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url) {
    Log.debug("DossidigitalController()");
    Analytics.trackEvent('controller','DossidigitalController');
    $scope.container = {};     
    $rootScope.currentview.id = 'dossiDigital';
    $rootScope.currentview.group = 'DossieDigital';
    $rootScope.currentview.title = 'Dossiê Digital';
    $rootScope.currentview.icon = 'fa-folder-open-o';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.activeurl = Url.active();
});