'use strict';
angular.module('webApp').controller('ProdutosdoclienteController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("ProdutosdoclienteController()");
    Analytics.trackEvent('controller','ProdutosdoclienteController');
    $scope.container = {};     
    $rootScope.currentview.id = 'produtosdoCliente';
    $rootScope.currentview.group = 'Produtos';
    $rootScope.currentview.title = 'Produtos do Cliente';
    $rootScope.currentview.icon = 'fa-chain';
    $rootScope.currentview.locked = false;
			$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
});