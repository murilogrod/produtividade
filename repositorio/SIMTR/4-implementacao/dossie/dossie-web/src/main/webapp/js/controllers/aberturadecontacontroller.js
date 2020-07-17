'use strict';
angular.module('webApp').controller('AberturadecontaController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest) {
    Log.debug("AberturadecontaController()");
    Analytics.trackEvent('controller','AberturadecontaController');
    $scope.container = {};     
    $rootScope.currentview.id = 'aberturadeConta';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Abertura de Conta';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.maxCartoesAdicionais = 5;
    
    $scope.chequeEspecialLimit = 5000;
    $scope.cdcLimit = 5000;
    $scope.cdcValue = null;
    $scope.creditCardLimit = 8000;
    $scope.deniedTalaoCheque = DossieService.getNegarTalaoCheque();
    $scope.talaoCheque = $scope.deniedTalaoCheque;
    $scope.formOk = true;
    $scope.password = null;
    
    $scope.cpf = null;
    $scope.limiteCartao = null;
    
    $scope.cartoesAdicionais = [];
    
    $scope.fieldsTermoAdesao = Object.assign([], FIELDS_ABERTURA_CONTA_TERMO_ADESAO);
    $scope.fieldsContratoRelacionamento = Object.assign([], FIELDS_ABERTURA_CONTA_CONTRATO_RELACIONAMENTO);
    
    $scope.fillFixedFields = function () {
    	var currentDate = new Date();
    	$scope.fieldsTermoAdesao['data_abertura'] = $scope.fieldsContratoRelacionamento['data_abertura'] = moment().format('DD/MM/YYYY');
    	$scope.fieldsTermoAdesao['nome_titular_1'] =  $scope.fieldsContratoRelacionamento['nome_titular_1'] =  SicpfService.getSicpf()['nome-contribuinte'];
    	$scope.fieldsTermoAdesao['identificador_titular_1'] = $scope.fieldsContratoRelacionamento['identificador_titular_1'] = SicpfService.getCpf();
    	$scope.fieldsTermoAdesao['dia_impressao'] =  $scope.fieldsContratoRelacionamento['dia_impressao'] = currentDate.getDate();
    	$scope.fieldsTermoAdesao['mes_impressao'] = $scope.fieldsContratoRelacionamento['mes_impressao'] = MONTHS[currentDate.getMonth()];
    	$scope.fieldsTermoAdesao['ano_impressao'] = $scope.fieldsContratoRelacionamento['ano_impressao'] = currentDate.getFullYear();
    }
    $scope.fillFixedFields();
    
    $scope.removeCreditCard = function (index) {
    	$scope.cartoesAdicionais.splice(index, 1);
    }
    
    $scope.validaLimiteContratado = function (value){
    	if (value == null || value == '') {
    		return;
    	}
    	if (typeof value !== 'number') {
    		value = parseToFloat(value);
    	}
    	
    	var ok = value != null && value <= $scope.creditCardLimit;
    	if (!ok) {
    		$('#numLimitecontratadoCartao-help').removeClass('hide');
    		$scope.formOk = false;
    		return;
    	} else {
    		$('#numLimitecontratadoCartao-help').addClass('hide');
    		$scope.formOk = true;
    	}
    	
    	if ($scope.cartoesAdicionais && $scope.getTotalCreditCards() > value) {
    		$('#numLimitecontratadoCartaoAdicional-help').removeClass('hide');
    		$scope.formOk = false;
    		return;
    	} else {
    		$('#numLimitecontratadoCartaoAdicional-help').addClass('hide');
    		$scope.formOk = true;
    	}
    	
    } 
    
    
    $scope.validaLimites = function (value, id , max_value) {
    	if (value == null || value == '') {
    		return;
    	}
    	if (typeof value !== 'number') {
    		value = parseToFloat(value);
    	}
    	var ok = value != null && value <= max_value;
    	if (ok) {
    		$('#' + id).addClass('hide');
    		$scope.formOk = true;
    	} else {
    		$('#' + id).removeClass('hide');
    		$scope.formOk = false;
    	}
    }
    
    $scope.validaDataDebito = function  (value, id) {
    	value = Number(value);
    	var ok = value != null && value >= 1 && value <= 30;
    	if (ok) {
    		$('#' + id).addClass('hide');
    		$scope.formOk = true;
    	} else {
    		$('#' + id).removeClass('hide');
    		$scope.formOk = false;
    	}
    }
    
    $scope.getTotalCreditCards = function () {
    	var total = 0.0;
		for (var i = 0 ; i < $scope.cartoesAdicionais.length; i++) {
			total += parseToFloat($scope.cartoesAdicionais[i].limite);
		}
		return total;
    }
    
    $scope.canIncludeCreditCard = function () {
    	if ($scope.cartoesAdicionais && $scope.cartoesAdicionais.length >= 5) {
    		return false;
    	}
    	
    	if ($scope.cartoesAdicionais && $scope.getTotalCreditCards() >= $scope.creditCardLimit) {
    		return false;
    	}
    	
    	return true;
    }
    
    $scope.includeCreditCard = function () {
    	var limiteCartaoNumber = parseToFloat($scope.limiteCartao);
    	if (limiteCartaoNumber > $scope.creditCardLimit) {
    		$rootScope.putErrorString('O valor do limite é maior que o permitido de R$ 8.000,00');
    		return;
    	}
    	if ((limiteCartaoNumber + $scope.getTotalCreditCards()) > $scope.creditCardLimit) {
    		$rootScope.putErrorString('A soma dos valores irá ultrapassar o permitido de R$ 8.000,00');
    		return;
    	}
    	
    	if ($scope.cartoesAdicionais && $scope.cartoesAdicionais.length > 0) {
    		for (var i = 0 ; i < $scope.cartoesAdicionais.length; i++) {
    			 if ($scope.cpf == $scope.cartoesAdicionais[i].cpf) {
    				 $rootScope.putErrorString('CPF já cadastrado na lista');
    				 return;
    			 }
    		}
    	}
    	
    	$rootScope.$broadcast(START_LOADING);
    	var urlSicpf = URL_SIMTR + SERVICO_SICPF + '/' + $scope.cpf + '/receita';
    	$http.get(urlSicpf).then(function (res) {
    		$('#cpf-notFound').addClass('hide');
            $('.theme-main-input-number-cpf').addClass('has-success').removeClass('has-error');
            
            var obj = {
            	name : res.data['nome-contribuinte'],
            	cpf: $scope.cpf,
            	dataNasc : new Date(res.data['data-nascimento']).toLocaleDateString(),
            	limite: $scope.limiteCartao,
            	limiteNumber : limiteCartaoNumber
            };
            
            $scope.cartoesAdicionais.push(obj);
            
            $rootScope.$broadcast(FINISH_LOADING);
    	}, function (error) {
    		$rootScope.$broadcast(FINISH_LOADING);
    		$rootScope.putError(error);
    		$(".page-loader").fadeOut(1000, "linear");
     		$('#cpf-notFound').removeClass('hide');
 			$('.theme-main-input-number-cpf').addClass('has-error').removeClass('has-success');
    	});
    }
    
    $scope.getCurrentDate = function () {
    	var currentDate = new Date();
    	return currentDate.getDate() + ' de ' + MONTHS[currentDate.getMonth()] + ' de ' + currentDate.getFullYear();
    }
    
    function getField (list , field) {
    	for (var i = 0 ; i < list.length; i++) {
			if (list[i].chave == field) {
				return list[i];
			}
		}
    }
    
    function putEmailField(list) {
    	var sms = DossieService.getSms();
    	var cartaoEmail = getField(list , 'cartao_email');
		if (cartaoEmail) {
			if (sms) {
				cartaoEmail.valor = (sms.email == null || sms.email === 'undefined' ? "endereço" : sms.email);
			} else {
				cartaoEmail.valor = "endereço";
			}
		} else {
			if (sms) {
				putField(list, 'cartao_email' , (sms.email == null || sms.email === 'undefined' ? "endereço" : sms.email));
			} else {
				putField(list, 'cartao_email' , 'endereço');
			}
		}
    }
    
    function putField(list , key , value)  {
    	list.push({chave: key , valor : value});
    }
    
    $scope.openMinuta = function (tipoDocumento) {
    	var noTipoDocumento = tipoDocumento == 1 ? 'CONTRATO DE RELACIONAMENTO' : 'TERMO DE ADESÃO A CESTA';
    	
    	var atributos = $scope.mountAtributos(tipoDocumento == 1 ? $scope.fieldsContratoRelacionamento : $scope.fieldsTermoAdesao );
    	if (tipoDocumento == 1) {
            putEmailField(atributos);

            var sms = DossieService.getSms();
            if (sms) {
    			if (sms.bloco_sms) {
                    atributos[searchArray('bloco_sms', atributos)].valor = sms.bloco_sms;
    				if (sms.sms_celular) {
    					atributos.push({chave : 'sms_celular' , valor : sms.sms_celular});
                    }
    				atributos.push({chave : 'sms_valor_saque' , valor : sms.sms_valor_saque.replace('R$', '')});
    				atributos.push({chave : 'sms_valor_debito' , valor : sms.sms_valor_debito.replace('R$', '')});
    				atributos.push({chave : 'sms_fgts' , valor : sms.sms_fgts});
    				atributos.push({chave : 'sms_produtos_servicos' , valor : sms.sms_produtos_servicos});
    				atributos.push({chave : 'sms_cartao_credito' , valor : sms.sms_cartao_credito});
    			}
    		}
            
    		if ($scope.cartoesAdicionais && $scope.cartoesAdicionais.length > 0) {
    			var count = 1;
    			for (var i = 0 ; i < $scope.cartoesAdicionais.length; i++) {
    				atributos.push({chave : 'cartao_adicional_nome_' + count , valor : $scope.cartoesAdicionais[i].name});
    				atributos.push({chave : 'cartao_adicional_cpf_' + count , valor :  $scope.cartoesAdicionais[i].cpf});
    				atributos.push({chave : 'cartao_adicional_nascimento_' + count , valor : $scope.cartoesAdicionais[i].dataNasc});
    				atributos.push({chave : 'cartao_adicional_limite_' + count , valor : $scope.cartoesAdicionais[i].limiteNumber});
    				count++;
    			}
    		}
    	}
    	
    	var obj = {integracao : INTEGRACAO, tipo_documento : noTipoDocumento, formato_saida : 'PDF' , 
    				atributos: $scope.mountAtributos(tipoDocumento == 1 ? $scope.fieldsContratoRelacionamento : $scope.fieldsTermoAdesao )};
    	
    	$http.post(URL_SIMTR + SERVICO_MINUTA , obj , {responseType: 'arraybuffer'}).then(function (res) {
    		var buffer = new Uint8Array(res.data);
			var blob = new Blob([buffer], {type : 'application/pdf'});
			window.open((window.URL || window.webkitURL).createObjectURL(blob), "_blank");
    	}, function (error) {
    		var decoder = new TextDecoder("utf-8");
    		error.data = JSON.parse(decoder.decode(error.data));
    		$rootScope.putError(error);
    	});
    }
    
    $scope.mountAtributos = function (fields) {
    	var atributos = [];
    	var keys = Object.keys(fields);
    	for (var i = 0 ; i < keys.length; i++) {
    		var value = fields[keys[i]];
    		if (value != null && value !== 'undefined') {
    			atributos.push({chave : keys[i] , valor: fields[keys[i]]});
    		}
    	}
    	return atributos;
    }
    
    $scope.openPDF = function (tipo) {
    	var noDocumento = tipo == 1 ? 'resources/pdfs/DICAS_SEGURANCA.pdf' : 'resources/pdfs/REGULAMENTO_CESTA.pdf';
    	window.open(location.protocol + '//' + location.hostname + location.pathname + noDocumento, "_blank" );
    }
    
    $scope.advance = function () {
    	if (DossieService.verifyPassword($scope.password,DossieService.getPassword())) {
    		var obj = {
    				cpf_cliente : SicpfService.getCpf(),
    				integracao : INTEGRACAO,
    				autorizacao : DossieService.getAutorizacao().autorizacao,
    				documentos : [
    					{
    						tipo_documento : 'CONTRATO DE RELACIONAMENTO',
    						atributos : $scope.mountAtributos($scope.fieldsContratoRelacionamento)
    					}, 
    					{
    						tipo_documento : 'TERMO DE ADESÃO A CESTA',
    						atributos : $scope.mountAtributos($scope.fieldsTermoAdesao)
    					}
    				]
    		}
    		
    		var sms = DossieService.getSms();
    		var documentoContrato = obj.documentos[0];
    		var chaveToken = {chave : 'token', valor: '{"datahora" : "' + moment().format('YYYY-MM-DD HH:mm:ss') +  '", "token": "' + TOKEN + '"}'};
    		documentoContrato.atributos.push(chaveToken);
    		obj.documentos[1].atributos.push(chaveToken);
    		
    		
    		if (sms) {
    			if (sms.bloco_sms) {
    				documentoContrato.atributos.push({chave : 'bloco_sms' , valor : sms.bloco_sms});
    				if (sms.sms_celular) {
    					documentoContrato.atributos.push({chave : 'sms_celular' , valor : sms.sms_celular});
    				}
    				documentoContrato.atributos.push({chave : 'sms_valor_saque' , valor : sms.sms_valor_saque});
    				documentoContrato.atributos.push({chave : 'sms_valor_debito' , valor : sms.sms_valor_debito});
    				documentoContrato.atributos.push({chave : 'sms_fgts' , valor : sms.sms_fgts});
    				documentoContrato.atributos.push({chave : 'sms_produtos_servicos' , valor : sms.sms_produtos_servicos});
    				documentoContrato.atributos.push({chave : 'sms_cartao_credito' , valor : sms.sms_cartao_credito});
    			}
    			if (sms.email) {
    				documentoContrato.abritubos.push({chave : 'email' , valor: sms.email});
    			}
    		}
    		
    		if ($scope.cartoesAdicionais && $scope.cartoesAdicionais.length > 0) {
    			var count = 1;
    			for (var i = 0 ; i < $scope.cartoesAdicionais.length; i++) {
    				documentoContrato.atributos.push({chave : 'nome_adicional_' + count , valor : $scope.cartoesAdicionais[i].name});
    				documentoContrato.atributos.push({chave : 'cpf_adicional_' + count , valor :  $scope.cartoesAdicionais[i].cpf});
    				documentoContrato.atributos.push({chave : 'data_nascimento_adicional_' + count , valor : $scope.cartoesAdicionais[i].dataNasc});
    				documentoContrato.atributos.push({chave : 'limite_adicional_' + count , valor : $scope.cartoesAdicionais[i].limiteNumber});
    				count++;
    			}
    		}
    		
    		var chequeEspecial = {
    				bloco_cheque_especial : $scope.fieldsContratoRelacionamento['bloco_cheque_especial'],
    				limite_cheque_especial : $scope.fieldsContratoRelacionamento['limite_cheque_especial']
    		};
    		DossieService.setChequeEspecial(chequeEspecial);
    		DossieService.setGerarAssinaturaEletronica($scope.fieldsContratoRelacionamento['bloco_assinatura']);
    		
    		$rootScope.$broadcast(START_LOADING);
    		$http.post(URL_SIMTR + SERVICO_DOCUMENTOS_GUARDA , obj).then(function (res) {
    			$rootScope.$broadcast(FINISH_LOADING);
    			if (res.status == 204) {
    				DossieService.putBackUrl('aberturadeConta');
    				$rootScope.go('assinaturadoGerenteFinal');
    			}
    		} , function (error) {
    			$rootScope.$broadcast(FINISH_LOADING);
    			$rootScope.putError(error);
    		});
    	} else {
    		
    		$rootScope.putErrorString('Senha não confere');
    	}
    }
    
    $scope.listar = function() {
    	Analytics.trackEvent('AberturadecontaController','listar()');
        WebServiceX.read("ws/aberturadeconta/aberturadeconta", $rootScope.headers)
      	.then(function(res) {
      		Analytics.trackEvent('AberturadecontaController','listar():success');
      		if(!res.temErro) {
      			if (res.data) 
		            $scope.container = res.data[0];
      			if (res.tipo) 
		            $scope.container = res.tipo[0];
				$scope.$apply();	      			     			
      		} else if(res.temErro) {
      			console.info(res.msgsErro[0]);
      			$scope.$apply();
      		}
      	}, function(xhr, status, err) {
      		Analytics.trackEvent('aberturadecontaController','listar():error');
				var message = $translate.instant('label.falha.listar.controller') + "aberturadecontaController";
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
    		if (err == UNAUTH || xhr.status == 502) {
    				$rootScope.goLogin();
    		}
      	});        
    };
    $scope.listar();
    
    $rootScope.$broadcast(FINISH_LOADING);
});