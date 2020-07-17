'use strict';
angular.module('webApp').controller('InformacesdoclientecontaconjuntaController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("InformacesdoclientecontaconjuntaController()");
    Analytics.trackEvent('controller','InformacesdoclientecontaconjuntaController');
    $scope.container = {};     
    $rootScope.currentview.id = 'informacesdoClienteContaConjunta';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Informações do Cliente Conta Conjunta';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});