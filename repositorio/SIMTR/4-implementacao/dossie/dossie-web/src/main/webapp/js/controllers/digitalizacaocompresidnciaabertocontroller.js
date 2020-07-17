'use strict';
angular.module('webApp').controller('DigitalizacaocompresidnciaabertoController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("DigitalizacaocompresidnciaabertoController()");
    Analytics.trackEvent('controller','DigitalizacaocompresidnciaabertoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaoCompResidnciaAberto';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Digitalização Comp Residência Aberto';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});