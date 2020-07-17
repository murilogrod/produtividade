'use strict';
angular.module('webApp').controller('InformacesdoclienteprocuradortutorcuradorController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("InformacesdoclienteprocuradortutorcuradorController()");
    Analytics.trackEvent('controller','InformacesdoclienteprocuradortutorcuradorController');
    $scope.container = {};     
    $rootScope.currentview.id = 'informacesdoClienteProcuradorTutorCurador';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Informações do Cliente Procurador Tutor Curador';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});