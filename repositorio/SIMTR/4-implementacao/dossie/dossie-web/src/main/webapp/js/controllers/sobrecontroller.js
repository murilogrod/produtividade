'use strict';
angular.module('webApp').controller('SobreController', function ($scope, $rootScope, $translate, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
        Log.debug("SobreController()");
        Analytics.trackEvent('controller','SobreController');
        $scope.container = {};     
        $rootScope.currentview.id = 'sobre';
        $rootScope.currentview.group = 'Sobre';
        $rootScope.currentview.title = 'Sobre';
        $rootScope.currentview.icon = 'fa-question';
        $rootScope.currentview.locked = false;
				$rootScope.currentview.menu = true;
        $rootScope.currentview.description = 'Sobre';
        	/* Central Caixa de Notificações - inicio */
            $scope.sobreIsSubscriptionSupported = null;
            $scope.sobreIsSubscriptionInititalized = null;
            $scope.sobreIsSubscribed = null;
            $scope.sobreIsDev = null;
            function onSuccess() {
                $scope.sobreIsSubscriptionSupported = $rootScope.isSubscriptionSupported();
                $scope.sobreIsSubscriptionInititalized = $rootScope.isSubscriptionInititalized();
                $scope.sobreIsSubscribed = $rootScope.isSubscribed();
                $scope.sobreIsDev = $rootScope.isSubscriptionDev();
            }
            $rootScope.startMessageService(onSuccess);
            $scope.doSubscribe = function($event) {
                $rootScope.subscribe($event,false,onSuccess);
            }
            $scope.doUnsubscribe = function($event) {
                $rootScope.unsubscribe($event,onSuccess);
            }
            /* Central Caixa de Notificações - fim */
            /* Central Caixa - Email - inicio */
            function onSuccessEmail() {
                $scope.checkSubscriptionEmail();
                $scope.isSubscribedEmail = $rootScope.isSubscribedEmail;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            }
            $scope.doSubscribeEmail = function() {
                $rootScope.subscribeEmail(onSuccessEmail);
            }
            $scope.doUnsubscribeEmail = function() {
                $rootScope.unsubscribeEmail(onSuccessEmail, $scope);
            }
            $scope.checkSubscriptionEmail = function() {
                Analytics.trackEvent('Sobre','checksubscription email');
                var request = {
                    app: CENTRAL_ID,
                    email: $rootScope.user
                };
                WebServiceRest.create(URL_CAIXA_SICPU + "ws/email/checksubscription/" + API_VERSAO, JSON.stringify(request), null)
                .then(function(res) {
                    if(!res.temErro) {
                        $scope.isSubscribedEmail = res.subscribed;
                        $scope.$apply();
                    } else if(res.temErro) {
                        Alert.showMessage("Atenção", res.msgsErro[0]);
                    }
                }, function(xhr, status, err) {
                        var message = $translate.instant('label.falha.verificar.inscricao');
                        if (xhr && xhr.responseText) {
                            try {
                                var response = JSON.parse(xhr.responseText);
                                if (response && response.msgsErro && response.msgsErro.length > 0) {
                                    message = response.msgsErro[0];
                                }
                            } catch(ignore) {
                            }
                        }
                        Error.handler(message, err);
                        $("#pnlConnecting").hide();
                });
            }
            if ($rootScope.user != null) {
                $scope.checkSubscriptionEmail();
                $scope.isSubscribedEmailSuported = true;
            }
            /* Central Caixa - Email - fim */
    });