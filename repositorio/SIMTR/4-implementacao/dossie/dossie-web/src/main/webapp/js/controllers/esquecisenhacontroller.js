'use strict';
angular.module('webApp').controller('EsquecisenhaController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("EsquecisenhaController()");
    Analytics.trackEvent('controller','EsquecisenhaController');
    $scope.container = {};     
    $rootScope.currentview.id = 'esquecisenha';
    $rootScope.currentview.group = 'Login';
    $rootScope.currentview.title = 'Esqueci minha senha';
    $rootScope.currentview.icon = 'fa-question';
    $rootScope.currentview.locked = false;
    $rootScope.currentview.menu = false;
    $rootScope.currentview.description = 'Tela de recuperação de senha';
});