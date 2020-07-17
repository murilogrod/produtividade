'use strict';
angular.module('webApp').controller('AutorizacaodocumentalController', function ($scope, $rootScope, $translate, $http, SicpfService, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AutorizacaodocumentalController()");
    Analytics.trackEvent('controller','AutorizacaodocumentalController');
    $scope.container = {};     
    $rootScope.currentview.id = 'autorizacaoDocumental';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Autorização Documental';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.autorizacao = DossieService.getAutorizacao();
    
    $scope.showMessageOk = $scope.autorizacao.autorizado;
    $scope.callingBackend = $scope.showMessageOk == false;
    
    if ($scope.callingBackend) {            
        var params = {
            "cpf_cliente": Number(SicpfService.getCpf()),
            "integracao": INTEGRACAO
        };
        // Atualiza os documentos que foram conferidos nesta sessão.
        $http.post(URL_SIMTR + SERVICO_CADASTRO_ATUALIZACAO , params).then(function (response) {
            if(response.status == 204) { // Dados e status no GED dos documentos atualizados com sucesso.                 
                
                var obj = {
                    cpf_cliente : Number(SicpfService.getCpf()),
                    integracao : INTEGRACAO,
                    operacao : $scope.autorizacao.operacao,
                    modalidade : $scope.autorizacao.modalidade
                };
                // Em seguida tenta obter a autorização novamente.
                $http.post(URL_SIMTR + SERVICO_AUTORIZACAO , obj).then(function (res) {
                    DossieService.setAutorizacao(res.data);
                    $scope.autorizacao = res.data;
                    $scope.showMessageOk = $scope.autorizacao.autorizado;
                    $scope.callingBackend = false;
                } , function (error) {
                    $scope.showMessageOk = false;
                    $scope.callingBackend = false;
                });
                
            } else { // Houve erro na atualização dos documentos.
                $rootScope.error = response.data.mensagem;
                $rootScope.showError = true;
            }
        }, function (error) {
            $rootScope.putError(error);
        });
    }
    
    $scope.advance = function () {
    	$rootScope.$broadcast(START_LOADING);
    	DossieService.putBackUrl('autorizacaoDocumental');
    	$rootScope.go('aberturadeConta');
    }
    
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.hideAlert();
});