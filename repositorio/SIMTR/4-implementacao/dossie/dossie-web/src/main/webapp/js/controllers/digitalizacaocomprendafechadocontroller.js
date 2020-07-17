'use strict';
angular.module('webApp').controller('DigitalizacaocomprendafechadoController', function ($scope, $rootScope, $translate,$http,DossieService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
    Log.debug("DigitalizacaocomprendafechadoController()");
    Analytics.trackEvent('controller','DigitalizacaocomprendafechadoController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaoCompRendaFechado';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = '';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.extracao = DossieService.getExtracao();
    $scope.imgRenda = DossieService.getExtracao().link + CONVERT_TIFF;
    
    $scope.sicpf = SicpfService.getSicpf();
    $scope.cpf = SicpfService.getCpf();
    
	$scope.errors = [];
	$scope.alteracaolimite = false;
	
	$scope.activeurl = Url.active();
    
	$scope.container = mountFieldsFrontEndExtracao(FIELDS_COMPROVANTE_RENDA , $scope.extracao.dados);
	$scope.listaProfissoes = $rootScope.listaProfissoes;
	$scope.ocupacaoSelected;
	$scope.ocupacaoEncontrada;
	
	$scope.isContemOcupacaoEmListaProfissoes;
	
	$scope.isDisableOcupacao;
	
    
	$scope.openPDF = function () {
    	$scope.currentDocument = $rootScope.currentDocument;
    	openPDFForAll($scope);
    }
	
	$scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Digitalização Comprovante de Renda', $http, 'Interrompido', 'principal');
    }
     
    $scope.updateData = function () {
    	var errors = $scope.formComprovanteRenda.$error;
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
        		if(!datevalid && $scope.container[i].msgdata !== '') {
        			valid = false;
    				GrowlMessage.addErrorMessage($scope.container[i].msgdata);
    				validaCampo($scope.container[i].key, valid);
        		}
			}
		}
		
		if(valid == true){
			var campoDataReferencia = $scope.obterCampoPorNome('referencia');
			if(campoDataReferencia && campoDataReferencia.value &&  campoDataReferencia.value.length > 3 ){
			  var campoPeriodo =	 $scope.obterCampoPorNome('periodo_referencia');
			  campoPeriodo.value = campoDataReferencia.value.substring(3, campoDataReferencia.length);
			}
			updateDataForExtracao(DossieService, $scope ,$http , $rootScope, FIELDS_COMPROVANTE_RENDA, RENDA, 'Digitalização de Comprovante de Renda');
		}
    }
    
    
    
    $scope.obterCampoPorNome = function (nome){
    	
    	for(var i = 0; $scope.container.length > i; i++){
			if($scope.container[i].key == nome){
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
    };
    
    
    $scope.validarOcupacao = function(ocupacao){
    	
    	$scope.isContemOcupacaoEmListaProfissoes = false;
    	
    	if(!ocupacao){
    	  return;
    	}
    	
    	$.each($rootScope.listaProfissoes, function (key, value) {
    		  if(ocupacao.indexOf(value.nome) > -1){
    			$scope.isContemOcupacaoEmListaProfissoes = true;
    			$scope.ocupacaoEncontrada = ocupacao;
    			
    			$scope.obterCampoPorNome('descricao_ocupacao').value = value.nome;
    			$scope.obterCampoPorNome('codigo_ocupacao').value = value.codigo;
				
    			return;
    		 }
    	});
    	
    	
    	
    	
    };
    
    
	    $scope.getClasseValidacaoOnLoad = function(item) {	    	
	    	if(item.key == 'descricao_ocupacao'){
	    	    if($scope.isContemOcupacaoEmListaProfissoes){
	    	    	return 'has-success';
	    	    }else{
	    	    	return 'has-error-ocupacao';
	    	    }
	    	}
		};
		
		

	$scope.setOcupacao = function(value) {
		if (value) {
			$scope.validarOcupacao(value.originalObject.nome);
		}

	};
	    
	
	$scope.isDesabilitar = function (item){
		if(item.key == 'descricao_ocupacao'){
		   return $scope.isContemOcupacaoEmListaProfissoes;
	   }   
   }
	    
    $scope.isValidCampoObrigatorio = function (field){
    	
    		var campo = field.item;
    		
    		if(campo.value == undefined || campo.value == null || 
    				campo.value == ''){
    			
    			return false;
    		}else{
    			
    			validaCampo(field.item.key, true);
        		
    			return true;
    		}
    };
    

    
    
    
    $scope.isValidCampoData = function(field){
    	mascaraData(field.item);
    	var isValid = validaDATA(field.item.value);
    	return isValid;
    };
    
    
//    $scope.validaDados = function (field){
//       if(!$scope.isValidCampoObrigatorio(field)){
//    	   
//    		GrowlMessage.addErrorMessage('Campo Obrigatorio: ' + field.item.name);
//			validaCampo(field.item.key, false);
//			
//       }else if(field.item.data == true && !$scope.isValidCampoData(field)){
//    		   
//    		 GrowlMessage.addErrorMessage('Data Inválida! Campo: ' + field.item.name);
//    		 validaCampo(field.item.key, false);
//       }else{
//    	   
//    	     validaCampo(field.item.key, true);
//       }	
//    	
//    };
    
    
    $scope.checkField = function (field, data) {
    	var errors = $scope.formComprovanteRenda.$error;
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
    
//    $scope.verificaOcupacao = function(){
//    	var ocupacao = $scope.getOcupacaoExtraida();
//    	if(ocupacao != null){
//    		$scope.listaProfissoes = $scope.getListProfissoes();
//    		$scope.iscontemOcupacao($scope.listaProfissoes, ocupacao);
//    	}
//    };
//    
    $scope.getOcupacaoExtraida = function(){
    	var ocupacao = null;
    	$.each($scope.container, function (chave, obj) {
    		if(obj.key == 'descricao_ocupacao'){
    			ocupacao = obj.value;
    		} 
    	});
    	
    	return ocupacao;
    };
    
    
    
    $scope.desabilitarCampoOcupacao = function () {
    	$scope.validarOcupacao($scope.getOcupacaoExtraida());
    	$scope.isDisableOcupacao = $scope.isContemOcupacaoEmListaProfissoes;
    	
    }
    
    $scope.desabilitarCampoOcupacao();
    
//    $rootScope.$broadcast(FINISH_LOADING);
//    
//    angular.element(document).ready(function () {
//    	$scope.validaCamposObrigatorios();
//      });
    
   
    
    $scope.localSearch = function(str, people) {
        var matches = [];
        people.forEach(function(person) {
          var fullName = person.firstName + ' ' + person.surname;
          if ((person.firstName.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) ||
              (person.surname.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) ||
              (fullName.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0)) {
            matches.push(person);
          }
        });
        return matches;
      };
    
    
    
    
     
    $rootScope.hideAlert();
    
    $rootScope.initScroll();
    
});