'use strict';
angular.module('webApp').controller('DigitalizacaocompresidnciafechadoController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
    Log.debug("DigitalizacaocompresidnciafechadoController()");
    Analytics.trackEvent('controller','DigitalizacaocompresidnciafechadoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaoCompResidnciaFechado';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = '';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.extracao = DossieService.getExtracao();
    $scope.sicpf = SicpfService.getSicpf();
    $scope.cpf = SicpfService.getCpf();
    
    $scope.imgResidencia = DossieService.getExtracao().link + CONVERT_TIFF;
    
	$scope.container = mountFieldsFrontEndExtracao(FIELDS_COMPROVANTE_RESIDENCIA , $scope.extracao.dados);
	
	$scope.errors = [];
	$scope.alteracaolimite = false;
	
	$scope.activeurl = Url.active();
    
	$scope.openPDF = function () {
    	$scope.currentDocument = $rootScope.currentDocument;
    	openPDFForAll($scope);
    }

	$scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Comprovante de Residência', $http, 'Interrompido', 'principal');
    }
     
    $scope.updateData = function () {
    	var errors = $scope.formComprovanteResidencia.$error;
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

			var dataReferencia = $scope.getCampoByKey(FIELD_DATA_REFERENCIA.key).value;			
			if(dataReferencia && dataReferencia.length >= 10){
				$scope.getCampoByKey(FIELD_MES_REFERENCIA.key).value = dataReferencia.substring(3, 5);
				$scope.getCampoByKey(FIELD_ANO_REFERENCIA.key).value = dataReferencia.substring(dataReferencia.length - 4, dataReferencia.length);
			}
		
			var cep_completo = $scope.getCampoByKey(FIELD_CEP_COMPLETO.key).value.replace(/\D+/g, '');	    	 
	    	 if(cep_completo && cep_completo.length >= 7){
	    		 $scope.getCampoByKey(FIELD_CEP.key).value = cep_completo.substring(0, 5);
				 $scope.getCampoByKey(FIELD_CEP_COMPLEMENTO.key).value = cep_completo.substring(cep_completo.length - 3, cep_completo.length);
				 $scope.getCampoByKey(FIELD_CEP_COMPLETO.key).value = cep_completo;
	    	 }
	    	 
	    	 var logradouro = $scope.getCampoByKey(FIELD_LOGRADOURO.key).value;
	    	 var tipoLogradouro = $scope.getCampoByKey(FIELD_TIPO_LOGRADOURO.key).value;
	    	 var numero = $scope.getCampoByKey(FIELD_NUMERO.key).value;
	    	 var complemento = $scope.getCampoByKey(FIELD_COMPLEMENTO.key).value;
	    	 
	    	 $scope.getCampoByKey(FIELD_ENDERECO.key).value = tipoLogradouro + ' ' + logradouro + ' ' + numero + ' ' + complemento ;  
	    	
			updateDataForExtracao(DossieService, $scope ,$http , $rootScope, FIELDS_COMPROVANTE_RESIDENCIA, ENDERECO, 'Digitalização de Comprovante de Renda');
		}
    }
    
    
    
    $scope.getCampoByKey = function(key){
    	for(var i = 0; i < $scope.container.length; i++){
    		if($scope.container[i].key == key){
    			return $scope.container[i];
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
    
    $scope.checkField = function (field, data) {
    	var errors = $scope.formComprovanteResidencia.$error;
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
    	
    	if(field.item.key == FIELD_CEP_COMPLETO.key){
    		$scope.carregarCep();
    	}
    	
    	if(field.item.key == 'endereco'){
    		if(field.item.value && (field.item.value.length > 50)){
    			GrowlMessage.addErrorMessage("Quantidade máxima do Endereço 50 caracteres!");
    			validaCampo(field.item.key, false);
    		}
    	}
    	
    	if(field.item.key == 'tipo_logradouro'){
    		if(field.item.value && (field.item.value.length > 3)){
    			GrowlMessage.addErrorMessage("Quantidade máxima do Tipo Logradouro 3 caracteres!");
    			validaCampo(field.item.key, false);
    		}
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
    
    $scope.carregarCep = function(){
    	if($scope.getCampoByKey(FIELD_CEP_COMPLETO.key).value){
    		$rootScope.$broadcast(START_LOADING);
    		var cep = $scope.getCampoByKey(FIELD_CEP_COMPLETO.key).value.replace('-','').replace(/\./g,'');
	    	$.ajax({
		        url: URL_SIMTR + SERVICO_LOCALIDADE + cep,
		        type: 'GET',
		        dataType:"json",
		        beforeSend: function(request){
		            request.withCredentials = true;
		            request.setRequestHeader('Authorization', 'Bearer ' + localStorage['token']);
		        },
		        success: function(result){
		        	if(result){
		        		$scope.getCampoByKey(FIELD_TIPO_LOGRADOURO.key).value = result.enderecos[0].nomeTipoLogradouro;
		        		$scope.getCampoByKey(FIELD_LOGRADOURO.key).value = result.enderecos[0].nomeLogradouro;
		        		$scope.getCampoByKey(FIELD_BAIRRO.key).value = result.enderecos[0].nomeBairro;
		        			if(result.enderecos[0].nomeLocalidade){
		        				$scope.getCampoByKey(FIELD_MUNICIPIO.key).value = result.enderecos[0].nomeLocalidade;
		        				$scope.getCampoByKey(FIELD_MUNICIPIO.key).disabled = true;		
							}
							if(result.enderecos[0].siglaUf){
								$scope.getCampoByKey(FIELD_UF.key).value = result.enderecos[0].siglaUf;
								$scope.getCampoByKey(FIELD_UF.key).disabled = true;		
							}	
		        	}else{
		        		limparCamposCep();
		        	}
		        	$scope.$apply();
		        	$rootScope.$broadcast(FINISH_LOADING);
		        },
		        error: function(error){	
		        	limparCamposCep();
		        	$rootScope.$broadcast(FINISH_LOADING);
		        }
		    });   
    	}else{
    		limparCamposCep();
    		$rootScope.$broadcast(FINISH_LOADING);
    	}
    }
    
    function limparCamposCep(){
    		$scope.getCampoByKey(FIELD_TIPO_LOGRADOURO.key).value = '';
    		$scope.getCampoByKey(FIELD_LOGRADOURO.key).value = '';
    		$scope.getCampoByKey(FIELD_BAIRRO.key).value = '';
    		$scope.getCampoByKey(FIELD_MUNICIPIO.key).disabled = false;
    		$scope.getCampoByKey(FIELD_UF.key).value = '';
    		$scope.getCampoByKey(FIELD_UF.key).disabled = false;
    }
    
    
    $scope.carregarCep();
     
    $rootScope.hideAlert();
    $rootScope.initScroll();  
    	
    
});