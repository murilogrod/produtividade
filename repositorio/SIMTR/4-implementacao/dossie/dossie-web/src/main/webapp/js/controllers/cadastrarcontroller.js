'use strict';
angular.module('webApp').controller('CadastrarController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("CadastrarController()");
    Analytics.trackEvent('controller','CadastrarController');
    $scope.container = {};     
    $rootScope.currentview.id = 'cadastrar';
    $rootScope.currentview.group = 'Login';
    $rootScope.currentview.title = 'Quero me cadastrar';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
    $rootScope.currentview.menu = false;
    $rootScope.currentview.description = 'Tela de Cadastramento';
});