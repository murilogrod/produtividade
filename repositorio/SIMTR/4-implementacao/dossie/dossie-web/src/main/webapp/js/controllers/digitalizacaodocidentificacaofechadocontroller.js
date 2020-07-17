angular.module('webApp').controller('DigitalizacaodocidentificacaofechadoController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService, uiUploader, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
        Log.debug("DigitalizacaodocidentificacaofechadoController()");
        Analytics.trackEvent('controller','DigitalizacaodocidentificacaofechadoController');
        $rootScope.currentview.id = 'digitalizacaoDocIdentificacaoFechado';
        $rootScope.currentview.group = 'AberturadeConta';
        $rootScope.currentview.title = '';
        $rootScope.currentview.icon = 'none';
        $rootScope.currentview.locked = false;
		$rootScope.currentview.menu = true;
        $rootScope.currentview.description = '';
        
        
    	$scope.errors = [];
    	$scope.alteracaolimite = false;
    	$scope.imgIdentificacao = DossieService.getExtracao().link + CONVERT_TIFF;
    	$scope.activeurl = Url.active();
        
        $scope.extracao = DossieService.getExtracao();
        
        if (DossieService.getClasse() == CLASS_CNH) {
        	$scope.container = mountFieldsFrontEndExtracao(FIELDS_CNH , $scope.extracao.dados);
        } else {
        	$scope.container = mountFieldsFrontEndExtracao(FIELDS_RG , $scope.extracao.dados);
        }
        
        $scope.openPDF = function () {
        	$scope.currentDocument = $rootScope.currentDocument;
        	openPDFForAll($scope, $http);
        }
        
        $scope.cancelarAtend = function(){
        	$rootScope.auditarAtendimento('Digitalização de Documento de Identidade', $http, 'Interrompido', 'principal');
        }
        
        $scope.updateData = function () {
        	var errors = $scope.formComprovanteIdentificacao.$error;
        	var valid = true;
        	if(errors.required != undefined) {
        		for(var i = 0; i < errors.required.length; i++) {
        			if(errors.required[i].$valid == false) {
        				valid = false;
        				var key = JSON.parse(errors.required[i].$name).error;
        				GrowlMessage.addErrorMessage(key);
        				validaCampo(JSON.parse(errors.required[i].$name).name, valid);
        			}
        		}
        	} 
    		for(var i = 0; i < $scope.container.length; i++) {
    			if($scope.container[i].data) {
    				var datevalid = validaDATA($scope.container[i].value);
            		if(!datevalid) {
            			valid = false;
        				GrowlMessage.addErrorMessage($scope.container[i].msgdata);
        				validaCampo($scope.container[i].key, valid);
            		}
    			}
    		}
    		
        	if(valid == true){
        	      if (DossieService.getClasse() == CLASS_CNH) {
        	    	  updateDataForExtracao(DossieService, $scope ,$http , $rootScope, FIELDS_CNH, IDENTIFICACAO, 'Digitalização de Documento de Identidade');
        	      } else {
        	    	  updateDataForExtracao(DossieService, $scope ,$http , $rootScope, FIELDS_RG, IDENTIFICACAO, 'Digitalização de Documento de Identidade');
        	      }
        	} 
        }
        
        $scope.isContinuar = function() {
        	var x = document.getElementsByClassName("has-error");
        	if(x.length > 0) {
        		return false;
        	} else {
        		return true;
        	}
        }
        
    	$scope.isInternetExplorer = function(){
    		return navigator.appName == 'Microsoft Internet Explorer' || (navigator.appName == "Netscape" && navigator.appVersion.indexOf('Edge') > -1) || (navigator.appName == "Netscape" && navigator.appVersion.indexOf('Trident') > -1);
    	};        
        
        $scope.checkField = function (field, data) {
        	var errors = $scope.formComprovanteIdentificacao.$error;
        	var valid = true;
        	if(errors.required != undefined) {
        		for(var i = 0; i < errors.required.length; i++) {
        			if(JSON.parse(errors.required[i].$name).name === field.item.key) {
        				valid = errors.required[i].$valid;
        				var key = JSON.parse(errors.required[i].$name).error;
        				$scope.errors.push(field.item.error);
        				if(verifyOccurrenceItemList($scope.errors, field.item.error) <= 5) {
        					GrowlMessage.addErrorMessage(field.item.error);
        				}
        				break;
        			}
        		}
        		
        	}
        	if(!$scope.alteracaolimite) {
        		validaCampo(field.item.key, valid);
        		
        	} else {
        		valid = false;
        	}
        	var key = field.item.error;
        	if(data && field.item.value !== undefined) {
        		mascaraData(field.item);
        		var key = field.item.msgdata;
        		var datevalid = validaDATA(field.item.value);
        		if(!datevalid) {
        			$scope.errors.push(key);
    				if(verifyOccurrenceItemList($scope.errors, key) <= 5) {
    					GrowlMessage.addErrorMessage(key);
    				}                
        		} 
        		disparaData(field.item, datevalid, valid);
        	}
        }
        
        $scope.validarPercentualMudanca = function(field, index) {
        	
        		var valorAlterado = field.item.value ? field.item.value : "";
	        	
	        	var valorOriginal;
	        	var objetoOriginal;
	        	
	        	for( objetoOriginal in $scope.extracao.dados){
	        		if($scope.extracao.dados[objetoOriginal].chave === field.item.key){
	        			valorOriginal = $scope.extracao.dados[objetoOriginal].valor ? $scope.extracao.dados[objetoOriginal].valor : "";
	        			break;
	        		}
	        	}
	        	
	    		if(index === null){
	        		valorOriginal = $scope.extracao.cpf_cliente;
	        	}
	    		
	    		if(valorOriginal){
		        	var campoAlteracao = $scope.extracao.dados[objetoOriginal].chave;
		        	var percAlteracaoPermitido = $scope.extracao.dados[objetoOriginal].percentual_alteracao;
		        	if(percAlteracaoPermitido < 100){
		        		validarAlteracaoCampos(valorOriginal, valorAlterado, percAlteracaoPermitido, campoAlteracao, field.item.alteracaolimite, $scope, GrowlMessage);
		        	}
	    		}
        }
        
        $rootScope.$broadcast(FINISH_LOADING);
        $rootScope.hideAlert();
        
        $rootScope.initScroll();
        
});
