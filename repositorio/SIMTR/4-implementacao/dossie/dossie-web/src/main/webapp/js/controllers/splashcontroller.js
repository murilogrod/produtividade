'use strict';
var DEFAULT_VIEW = "principal";
angular.module('webApp').controller('SplashController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
        Log.debug("SplashController()");
        Analytics.trackEvent('controller','SplashController');
        $scope.container = {};     
        $rootScope.currentview.id = 'splash';
        $rootScope.currentview.group = 'Splash';
        $rootScope.currentview.title = 'Splash';
        $rootScope.currentview.icon = 'fa-picture-o';
        $rootScope.currentview.locked = false;
        $rootScope.currentview.menu = false;
        $rootScope.currentview.description = 'Tela de Splash';
 });