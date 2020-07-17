'use strict';
angular.module('webApp').controller('PrincipalController', function ($scope, $rootScope, $translate, $http, SicliService, SicpfService, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Device) {
        Log.debug("PrincipalController()");
        Analytics.trackEvent('controller','PrincipalController');
        $scope.container = {};     
        $rootScope.currentview.id = 'principal';
        $rootScope.currentview.group = 'Principal';
        $rootScope.currentview.title = 'Principal';
        $rootScope.currentview.icon = 'fa-home';
        $rootScope.currentview.locked = true;
        $rootScope.currentview.menu = false;
        $rootScope.currentview.description = 'Tela Principal';
            
        $rootScope.documentos.declararEndereco = false;
        $rootScope.documentos.ativarCheckDeclararResidencia = false;
        
        
        
        $scope.addClassHide = function (param) {
        	if(param === 'cpf-help') {
        		$('#cpf-help').addClass('hide');
        	} else {
        		$('#cpf-notFound').addClass('hide');
        	}
        }
        
         $scope.search = function () {
        	$scope.operacaoSelected = 1;
        	if($scope.container.numcpf){
        		
        	
	        	var obj = {
	        			cpf_cliente : Number($scope.container.numcpf),
	        			integracao : INTEGRACAO,
	        			operacao : $scope.operacaoSelected,
	        			modalidade : 0
	            };
	
	        	$rootScope.$broadcast(START_LOADING);
	        	
	        	
	        	$http.post(URL_SIMTR + SERVICO_AUTORIZACAO , obj).then(function (res) {
	        		
	        		var urlSicpf = URL_SIMTR + SERVICO_SICPF + '/' + $scope.container.numcpf + '/receita';
	             	$http.get(urlSicpf).then(function (respon) {
	             		
	             		var data = new Date(respon.data['data-nascimento']).toLocaleDateString();
	             		var contribuinte = respon.data['nome-contribuinte'];
	             		var cpf = respon.data.cpf;
	             		var mae = respon.data['nome-mae'];
	             		var objects = getDevices(Device.devices());
	             		var value = objects[0].codigo;
	             		var show = objects.length > 2;
	                    $('.theme-main-input-number-cpf').addClass('has-success').removeClass('has-error');
	                     
	                    $rootScope.project.customersummary = {nome: contribuinte, 
	                    									  cpf: $scope.cpfMask(cpf), 
	                    									  datanascimento: data, 
	                    									  nomemae: mae,
	                    									  showdevices: show,
	                    									  devices: objects,
	                    									  device: value};
	         			SicpfService.setSicpf(respon.data);
	         			SicpfService.setCpf($scope.container.numcpf);
	         			SicpfService.setNewSearch(true);
	
	             	}, function (errorCpf) {
	             		$(".page-loader").fadeOut(1000, "linear");
	             		$('#cpf-notFound').removeClass('hide');
	             		$('#error-div').addClass('hide');
	         			$('.theme-main-input-number-cpf').addClass('has-error').removeClass('has-success');
	             	});
	
	        		if(res.status == 200){
	        			DossieService.putBackUrl('principal');
	            		DossieService.setAutorizacao(res.data);
	            		$rootScope.go('pesquisasCadastrais');	
	        		}
	        		
	             	        		
	        		
	        	} , function (error) {
	        		$(".page-loader").fadeOut(1000, "linear");
	         		$('#error-div').removeClass('hide');
	         		if(error.data){
	         			$('#error-autorizacao').text(error.data.observacao ? error.data.observacao : error.data.mensagem);
	         		}else if(error.detalhe){
	         			$('#error-autorizacao').text(error.detalhe)
	         		}
	     			$('.theme-main-input-number-cpf').addClass('has-error').removeClass('has-success');
	        	});

        	}else{
        		$(".page-loader").fadeOut(1000, "linear");
         		$('#error-div').removeClass('hide');
         		$('#error-autorizacao').text("CPF: Campo Obrigatório.")
     			$('.theme-main-input-number-cpf').addClass('has-error').removeClass('has-success');
        	}
        }
         
        
        $rootScope.decideStyleIcon = function (name) {
        	if (name == 'circle') {
        		return 'icongray';
        	}
        	if (name == 'times-circle') {
        		return 'iconred';
        	}
        	if (name == 'check-circle') {
        		return 'icongreen';
        	}
        	if (name == 'exclamation-circle') {
        		return 'iconorange';
        	}
        	
        	return '';
        }
        
        $scope.getCustomerSummary = function (sicpfData, cpf) {
            // Converte data no formato yyyy/mm/dd para dd/mm/yyyy.
//            var dataNascimento = new Date(sicpfData.contribuinte.DATA_NASCIMENTO + ' 12:00:00').toLocaleString().substring(0, 10);

            return 'Nome: ' + sicpfData['nome-contribuinte'] +
             '<br />CPF: ' + $scope.cpfMask(cpf) + 
             '<br />Data de Nascimento: ' + sicpfData['data-nascimento'] +
             '<br />Nome da Mãe: ' + sicpfData['nome-mae'];
        }
        
        $scope.carregarRelatorios = function(){
        	$rootScope.$broadcast(START_LOADING);
    		var urlOrganograma = URL_SIMTR + SERVICO_OBTER_ORGANOGRAMA;
         	$http.get(urlOrganograma).then(function (respon) {
         		$rootScope.organograma = respon.data;
         		$rootScope.$broadcast(FINISH_LOADING);
         		$rootScope.go('relatorios');
         	}, function (error) {
         		$rootScope.putError(error);
         	});
        }
        
        $scope.cpfMask =  function (input ) {
			var str = input + '';
			if (str.length <= 11) {
				str = str.replace(/\D/g, '');
				str = str.replace(/(\d{3})(\d)/, '$1.$2');
				str = str.replace(/(\d{3})(\d)/, '$1.$2');
				str = str.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
			}
			return str;
        }
        
    });