'use strict';
angular.module('webApp').controller('DeclaracaodedadosController', function ($scope, $rootScope, $translate,  $http, DossieService, SicpfService,Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage, KeycloakService) {
    Log.debug("DeclaracaodedadosController()");
    Analytics.trackEvent('controller','DeclaracaodedadosController');
    $scope.container = {};     
    $rootScope.currentview.id = 'declaracaodeDados';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Declaração de Dados';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.activeurl = Url.active();
    
    $scope.listaGeralMun = [];
    
//    if(DossieService.getAutorizacao().documentos_pendentes == null || DossieService.getAutorizacao().documentos_pendentes.indexOf(DADOS_DECLARADOS) == -1) {
//        // Se o documento de DADOS DECLARADOS não está na lista de documentos pendentes, pula a tela de declaração de dados.
//    	$rootScope.$broadcast(START_LOADING);
//        $rootScope.go('assinaturadoGerente');
//    }
    
    $scope.fields = Object.assign([], FIELDS_DADOS_DECLARADOS);
    
    $scope.fields['declaracao_proposito'] = [];
    $scope.fields['email'] = $rootScope.emailClienteSSO;    
    $scope.fields['valor_total_patrimonio'] = '0.00';
    $scope.isInformarPatrimonio = false;
    $scope.fields['declaracao_fatca_crs'] = false;
    $scope.fields['naturalidade'] = "";
    
    
    $scope.meioComunicacaoList = [];
    
    $scope.meioComunicacaoCelular = [{type: 0, pos: 0, value: 'Selecione...'}];
    $scope.meioComunicaoCelularSelected = 0;
    
	$scope.meioComunicacao = {};
    
    $scope.meioComunicacaoCombo = [ {id : 1 , value : 'Telefone Fixo'},
    	{id: 4 , value: 'Celular'},
    	{id: 3 , value: 'Email'}];
    
    $scope.showDDD = true;
    $scope.showEmail = false;
    $scope.password  = null;
    $scope.profissaoCadastrada = '';
    $scope.naturalidadeCadastrada = '';
    
    
    
    $scope.carregarListProfissoes = function(){
    	var path = './json/profissoes.json';
    	$.getJSON(path, function(profissoes) {
    		$scope.listaProfissoes = profissoes;
		}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista Padrão de Profissões!");
		}); 	
    	
    };
    
    $scope.getCurrentDate = function () {
    	var currentDate = new Date();
    	return currentDate.getDateFormatted() + ' de ' + MONTHS[currentDate.getMonth()] + ' de ' + currentDate.getFullYear();
    }
    
    $scope.validarCPF = function (field) {
    	if(!$scope.fields['conjuge_cpf']){
    		$scope.fields['conjuge_nome'] = "";
    		$scope.fields['conjuge_nascimento'] = "";
    		$scope.checkField($scope.fields['conjuge_cpf'], 'numCpfdoconjuge');
    	} else {
    		$scope.consultaCpf(field);
    	}
    	$scope.checkField($scope.fields['conjuge_cpf'], 'numCpfdoconjuge');
    	
    }
    
    $scope.changeMeioComunicacao = function () {
    	if ($scope.meioComunicacao.type == 1 || $scope.meioComunicacao.type == 4) {
    		$scope.showDDD = true;
            $scope.showEmail =false;
    	} else {
    		$scope.showDDD = false;
            $scope.showEmail =true;
    	}
    	$scope.meioComunicacao.value =  '';
    }
    
    $scope.checkField = function (valor, name){
    	var valid = false;
    	if(valor){
    		valid = true;
    		$('.'+name).removeClass('has-error');
    		$('.label-'+name).css({"color": "black"});
    	}
    	validaCampo('theme-main-input-'+name, valid);
    	
    }
    
    $scope.carregarEstados = function(){
    	var path = './json/uf.json';
    	$.getJSON(path, function(lista) {
    		$scope.listaNaturalidadeUf = lista;
    		  $scope.carregarListaGeralMun();
    	}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista de Estados!");
		}); 	
    };
    
    $scope.carregarMunicipios = function(estado){
    	if(estado){
    		$scope.carregarMun(estado);	
    	}else{
    		$scope.fields['naturalidade_uf'] = '';
    	}
    };
    
    $scope.carregarMun = function(estado){    
    	$scope.listaNaturalidadeMunicipio = [];
    	if(estado){    		
    		$scope.listaGeralMun.forEach(function(item){
        			if(item.estado == estado){
        				$scope.listaNaturalidadeMunicipio.push(item);
        			}
        		});    		
    	}
    }
    
    $scope.carregarListaGeralMun = function(){
    	var path = './json/municipios.json';
    		$.getJSON(path, function(lista) {
        		$scope.listaGeralMun = lista;       		
        	}).error( function (){
    			$rootScope.putErrorString("Erro ao Obter Lista de Municipios!");
    		}); 	
    		
    }
    
    $scope.carregarSexo = function(){
    	var path = './json/sexo.json';
    	$.getJSON(path, function(lista) {
    		$scope.listaSexo = lista;
    	}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista de Sexos!");
		}); 	
    };
    
    $scope.carregarGrauInstrucao = function(){
    	var path = './json/grau-instrucao.json';
    	$.getJSON(path, function(lista) {
    		$scope.listaGrauInstrucao = lista;
    	}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista de Sexos!");
		}); 	
    };
    
    $scope.carregarEstadoCivil = function(){
    	var path = './json/estado-civil.json';
    	$.getJSON(path, function(lista) {
    		$scope.listaEstadoCivil = lista;
    	}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista de Estado Civil!");
		}); 	
    };
    
    $scope.updateData = function () {
    	var errors = $scope.formDeclaracaoDados.$error;
    	var valid = true;
    	if(errors.required != undefined) {
    		for(var i = 0; i < errors.required.length; i++) {
    			if(errors.required[i].$valid == false) {
    				$scope.sinalizaCampoComErro(errors.required[i].$name);
    				valid = false;    	
    			}
    		}
    	}
 
    	if(!$scope.fields['naturalidade']){
    	    $scope.sinalizaCampoComErro('naturalidade');
    	    valid = false;
    	}
    	
    	//Validação adesão ao serviço de SMS
    	/*if(!$scope.isContemTelefone()){
    		$scope.sinalizaCampoComErro('numTelefone');
    		valid = false;
    	}*/
    	
    	var vlPatrimonio = $scope.fields['valor_total_patrimonio'];
    	if(new String(vlPatrimonio) == "R$ 0,00" && $scope.isInformarPatrimonio == true){
    		$scope.sinalizaCampoComErro('valorTotalPatrimonio');
    		valid = false;
    	}
    	
    	var declaracaoProposito = $scope.fields['declaracao_proposito']; 
    	if( !(declaracaoProposito) || declaracaoProposito.length == 0){
    		$scope.sinalizaCampoComErro('declaracaoProposito');
    		valid = false;
    	}
    	
    	if(!$scope.validaEnderecoResidencial()){
    		valid = false;
    	}
    	
    	if(!$scope.validaEnderecoComercial()){
    		valid = false;
    	}
    	
    	//Validação adesão ao serviço de SMS
      	/*if($scope.fields['bloco_sms'] && $scope.meioComunicaoCelularSelected == 0){
    		$rootScope.putErrorString('Selecione o celular para receber o sms.');
    		valid = false;
    	}*/
   	
    	if(!$scope.password){
    		$rootScope.putErrorString('Senha Obrigatória!');
    		valid = false;
    	}
    	
    	return valid;
    }
    
    
    $scope.validaEnderecoResidencial = function(){
    	
    	var isValid = true;
    	$scope.isAddEnderecoResidencial = false;
    	
     if($scope.fields['logradouro_residencial'] ||
    	$scope.fields['tipo_logradouro_residencial'] ||
    	$scope.fields['numero_residencial'] ||
		$scope.fields['complemento_residencial'] ||
		$scope.fields['bairro_residencial'] ||
		$scope.fields['uf_residencial'] ||
		$scope.fields['cidade_residencial'] ||
		$scope.fields['cep_residencial']){

    	 
         if(!$scope.fields['logradouro_residencial']){
        	 $scope.sinalizaCampoComErro('logradouro_residencial');
        	 isValid = false
         }
         
         if(!$scope.fields['tipo_logradouro_residencial']){
        	 $scope.sinalizaCampoComErro('tipo_logradouro_residencial');
        	 isValid = false
         }
         
         if(!$scope.fields['numero_residencial']){
        	 $scope.sinalizaCampoComErro('numero_residencial');
        	 isValid = false
         }
         
         if(!$scope.fields['complemento_residencial']){
        	 $scope.sinalizaCampoComErro('complemento_residencial');
        	 isValid = false
         }
         
         if(!$scope.fields['bairro_residencial']){
        	 $scope.sinalizaCampoComErro('bairro_residencial');
        	 isValid = false
         } 
         
         if(!$scope.fields['uf_residencial']){
        	 $scope.sinalizaCampoComErro('uf_residencial');
        	 isValid = false
         } 
         
         if(!$scope.fields['cidade_residencial']){
        	 $scope.sinalizaCampoComErro('cidade_residencial');
        	 isValid = false
         }
         
         if(!$scope.fields['cep_residencial']){
        	 $scope.sinalizaCampoComErro('cidade_residencial');
        	 isValid = false
         }
         
         if(isValid){
        	 $scope.isAddEnderecoResidencial = true;
         }
         
      }
       return isValid;
    }
    
    
    $scope.validaEnderecoComercial = function(){
    	
    	var isValid = true;
    	$scope.isAddEnderecoComercial = false;
    	
       if($scope.fields['logradouro_comercial'] ||
    	  $scope.fields['tipo_logradouro_comercial'] ||
 	   	  $scope.fields['numero_comercial'] ||
 	   	  $scope.fields['complemento_comercial'] ||
 	   	  $scope.fields['bairro_comercial'] ||
 	   	  $scope.fields['uf_comercial'] ||
 	   	  $scope.fields['cidade_comercial'] || 
 	   	  $scope.fields['cep_comercial']){
    	   
    	   if(!$scope.fields['logradouro_comercial']){
    		   $scope.sinalizaCampoComErro('logradouro_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['tipo_logradouro_comercial']){
    		   $scope.sinalizaCampoComErro('tipo_logradouro_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['numero_comercial']){
    		   $scope.sinalizaCampoComErro('numero_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['complemento_comercial']){
    		   $scope.sinalizaCampoComErro('complemento_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['bairro_comercial']){
    		   $scope.sinalizaCampoComErro('bairro_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['uf_comercial']){
    		   $scope.sinalizaCampoComErro('uf_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['cidade_comercial']){
    		   $scope.sinalizaCampoComErro('uf_comercial');
          	 	isValid = false
    	   }
    	   if(!$scope.fields['cep_comercial']){
    		   $scope.sinalizaCampoComErro('cep_comercial');
          	 	isValid = false
    	   }
    	   
    	   if(isValid){
    		   $scope.isAddEnderecoComercial = true;
    	   }
    	   
       } 
       
       return isValid;
    }
    
    
    
	
    $scope.sinalizaCampoComErro = function (nameCampo){
		GrowlMessage.addErrorMessage($translate.instant(nameCampo));
		validaCampo(nameCampo, false);
		$('.theme-main-input-'+nameCampo).addClass('has-error');
		$('.label-'+nameCampo).css({"color": "#843534"});
    }
    
    $scope.isContemTelefone = function (){
    	if($scope.meioComunicacaoList.length != 0){      		
    		for(var i in $scope.meioComunicacaoList){
    			if($scope.meioComunicacaoList[i].type == '1' || 
    			   $scope.meioComunicacaoList[i].type == '2'){
    				return true;
    			};
    		}
    	}
    	
    	return false;
    }
    
    
    
    $scope.removeMeioComunicacao = function (item) {
    	for (var i = 0 ; i < $scope.meioComunicacaoList.length; i++) {
    		if (item.value == $scope.meioComunicacaoList[i].value) {
    			if ($scope.meioComunicacaoList[i].type == 2 ) {
    				$scope.meioComunicacaoCelular.splice(i+1, 1);
    			}
    			$scope.meioComunicacaoList.splice(i, 1);
    			break;
    		}
    	}
    }
    
    $scope.decideActive = function (value , name) {
    	if (value == name) {
    		return 'btn-primary active';
    	}
    	return 'btn-default';
    }
    
    $scope.validarCelularSelecionado = function (field) {
    	$scope.fields.push('sms_telefone');
    	if($scope.fields['bloco_sms'] && field.meioComunicaoCelularSelected == 0){
    		$rootScope.putErrorString('Selecione o celular para receber o sms.');
    		$scope.fields['sms_telefone'] = '';
    		
    	}else{
    		var tel = $scope.meioComunicacaoCelular[field.meioComunicaoCelularSelected].value;
    		$scope.fields['sms_telefone'] = tel;
    		
    	}

    	
    	$scope.meioComunicaoCelularSelected = field.meioComunicaoCelularSelected;
    }
    
    $scope.includeMeioComunicacao = function () {
    	if ($scope.meioComunicacao.type == 3) {
    		if ($scope.countMeioComunicacao($scope.meioComunicacao.type) < 2) {
    			$scope.meioComunicacaoList.push({type : $scope.meioComunicacao.type , value : $scope.meioComunicacao.value});
    		} else {
    			$rootScope.putErrorString('Não é permitido adicionar mais de 2 e-mails');
    		}
    	} else if ($scope.meioComunicacao.type == 1 || $scope.meioComunicacao.type == 4 ) {
    		var count = $scope.countMeioComunicacao(1) + $scope.countMeioComunicacao(4);
    		if (count > 3) {
    			$rootScope.putErrorString('Não é permitido adicionar mais de 4 telefones');
    		} else {
    			$scope.meioComunicacaoList.push({type : $scope.meioComunicacao.type , value : $scope.meioComunicacao.value});
    			if($scope.meioComunicacao.type == 4){
    				$scope.meioComunicacaoCelular.push({type : $scope.meioComunicacao.type, pos:  $scope.meioComunicacaoCelular.length, value : $scope.meioComunicacao.value});	
    			}
    			
    		}
    	}
    	$scope.meioComunicacao.value = "";
	}
    
    $scope.countMeioComunicacao = function (type) {
    	var count = 0;
    	for (var i = 0 ; i < $scope.meioComunicacaoList.length; i++) {
    		if ($scope.meioComunicacaoList[i].type == type) {
    			count++;
    		}
    	}
    	return count;
    }
    
    
    $scope.continuar = function() {
    
    	if(!$scope.updateData()){
    		return; 
    	}
  
    	
    	$rootScope.$broadcast(START_LOADING);
//    	$rootScope.validarSenahaSSO($http, DossieService.getAutorizacao().cpf_cliente, $scope.password, $scope.advance);
    	$scope.advance();
    	
    }
    
    $scope.advance = function () {    	
    		
    		var dados = [];  		
    		/*GED espera as informções contidas em uma unica string
	    	var endereco = $scope.fields['cep_residencial'] + " " + $scope.fields['tipo_logradouro_residencial'] + " " + $scope.fields['logradouro_residencial'] + 
	    	" " + $scope.fields['numero_residencial'] + " " + $scope.fields['complemento_residencial'] + " " + $scope.fields['bairro_residencial'] + " " + 
	    	$scope.fields['uf_residencial'] + " " + $scope.fields['cidade_residencial'];
	    	
	    	var enderecoComercial = $scope.fields['cep_comercial'] + " " + $scope.fields['tipo_logradouro_comercial'] + " " + $scope.fields['logradouro_comercial'] + 
	    	" " + $scope.fields['numero_comercial'] + " " + $scope.fields['complemento_comercial'] + " " + $scope.fields['bairro_comercial'] + " " + 
	    	$scope.fields['uf_comercial'] + " " + $scope.fields['cidade_comercial'];


    		dados.push({chave : 'endereco_residencial' , valor: endereco});
    		dados.push({chave : 'endereco_comercial' , valor: enderecoComercial}); */
    		
    		//Carrega as informações do endereço em campos diferentes para serem apresentados na tela de alteração
    		dados.push({chave : 'nome_social' , valor: $scope.fields['nome_social']});
    		
//    		dados.push({chave : 'logradouro_residencial' , valor: $scope.fields['logradouro_residencial'] ? $scope.fields['logradouro_residencial'] : ""});
    		
//    		if($scope.meioComunicaoCelularSelected > 0){
//    			dados.push({chave : 'celular_selecionado' , valor: $scope.meioComunicacaoCelular[$scope.meioComunicaoCelularSelected-1].value ? $scope.meioComunicacaoCelular[$scope.meioComunicaoCelularSelected].value : ""});	
//    		}
    		
    		
//    		dados.push({chave : 'logradouro_comercial' , valor: $scope.fields['logradouro_comercial'] ? $scope.fields['logradouro_comercial'] : ""});
//    		dados.push({chave : 'declaracao' , valor: $scope.fields['declaracao'] ? $scope.fields['declaracao'] : ""});
    		
			dados.push({chave : 'declaracao_proposito' , valor: $scope.fields['declaracao_proposito'] ? $scope.fields['declaracao_proposito'].toString() : ""});
			
			if($scope.fields['declaracao_fatca_crs']){
				
				if($scope.fields['declaracao'] == 'FATCA' && $scope.fields['n_tin']){
					dados.push({chave : 'n_tin' , valor: $scope.fields['n_tin']});
					dados.push({chave : 'marca_documento_principal_tin' , valor: 'S'});
				}
				
				if($scope.fields['declaracao'] == 'CRS' && $scope.fields['pais_crs']){
					dados.push({chave : 'pais_crs' , valor: $scope.fields['pais_crs']});
				} 
				
			}
			
			
			dados.push({chave : 'declaracao_fatca_crs' , valor: $scope.fields['declaracao_fatca_crs'] ? 'Sim' : 'Não'});
			
			dados.push({chave : 'tipo_patrimonio' , valor: $scope.isInformarPatrimonio ? 'C' : 'N'});
			if($scope.isInformarPatrimonio){
				var patrimonio = $scope.fields['valor_total_patrimonio'].replace('R$', '').replace(/\./g,'').replace(/\,/g,'').trim();
				dados.push({chave : 'valor_total_patrimonio' , valor: $scope.fields['valor_total_patrimonio'] ? patrimonio.substring(0, patrimonio.length-2) : ""});
			}
			
			if($scope.fields['estado_civil'] && $scope.fields['estado_civil'] != 'Não Informado' && $scope.fields['estado_civil'] != 'Solteiro (a)' && $scope.fields['estado_civil'] != 'Divorciado' && $scope.fields['estado_civil'] != 'Separado (a) Judicialmente' && $scope.fields['estado_civil'] != 'Viúvo (a)'){
				if($scope.fields['conjuge_nascimento']) dados.push({chave : 'conjuge_nascimento' , valor: $scope.fields['conjuge_nascimento'] ? moment( $scope.fields['conjuge_nascimento'] ).format('DD/MM/YYYY') : ''});
				if($scope.fields['conjuge_cpf']) dados.push({chave : 'conjuge_cpf' , valor: $scope.fields['conjuge_cpf'] ? $scope.fields['conjuge_cpf'] : ""});
				if($scope.fields['conjuge_nome']) dados.push({chave : 'conjuge_nome' , valor: $scope.fields['conjuge_nome'] ? $scope.fields['conjuge_nome'] : ""});
			}
			dados.push({chave : 'estado_civil' , valor: $scope.fields['estado_civil'] ? $scope.fields['estado_civil'] : ""});
//			dados.push({chave : 'endereco' , valor: $scope.fields['endereco'] ? $scope.fields['endereco'] : ""});
			
			if($rootScope.documentos.declararSemRenda){
				dados.push({chave : 'codigo_ocupacao', valor: 791});
				dados.push({chave : 'descricao_ocupacao', valor: "SEM OCUPACAO"});
				dados.push({chave : 'tipo_renda', valor: "F"});
				dados.push({chave : 'caracteristica_renda', valor: 2});
			} else {
				dados.push({chave : 'descricao_ocupacao' , valor: $scope.fields['profissao'].description.nome ? $scope.fields['profissao'].description.nome : ""});
				dados.push({chave : 'codigo_ocupacao', valor: $scope.fields['profissao'].description.codigo ? $scope.fields['profissao'].description.codigo : ""});
			}
			
			dados.push({chave : 'grau_instrucao' , valor: $scope.fields['grau_instrucao'] ? $scope.fields['grau_instrucao'] : ""});
			dados.push({chave : 'sexo' , valor: $scope.fields['sexo'] ? $scope.fields['sexo'] : ""});
			dados.push({chave : 'cpf' , valor: SicpfService.getCpf()});
			dados.push({chave : 'nome' , valor: SicpfService.getSicpf()['nome-contribuinte']});
			dados.push({chave : 'token', valor: '{"datahora" : "' + moment().format('YYYY-MM-DD HH:mm:ss') +  '", "token": "' + KeycloakService.hasToken() + '"}'});
			
//			dados.push({chave : 'sms_valor_debito' , valor: $scope.fields['sms_valor_debito'] ? $scope.fields['sms_valor_debito'] : ""});
			
			/*if($scope.fields['bloco_sms']){
			dados.push({chave : 'sms_telefone' , valor: $scope.fields['sms_telefone'] ? $scope.fields['sms_telefone'] : ''});
			dados.push({chave : 'sms_valor_saque_trans' , valor: $scope.fields['sms_valor_saque_trans'] ? $scope.fields['sms_valor_saque_trans'].replace('R$', '').replace(',','.') : ""});
			dados.push({chave : 'sms_valor_cartao' , valor: $scope.fields['sms_valor_cartao'] ? $scope.fields['sms_valor_cartao'].replace('R$', '').replace(',','.') : "" });
			dados.push({chave : 'sms_fgts' , valor: $scope.fields['sms_fgts'] ? 'Sim' : 'Não'});
			dados.push({chave : 'sms_produtos_servicos' , valor: $scope.fields['sms_produtos_servicos'] ? 'Sim' : 'Não'});
			}*/
//			dados.push({chave : 'sms_cartao_credito' , valor: $scope.fields['sms_cartao_credito'] ? 'Sim' : 'Não'});
			dados.push({chave : 'cidade_assinatura' , valor: $scope.fields['cidade_assinatura'] ? $scope.fields['cidade_assinatura'] : ""});
			
			dados.push({chave : 'naturalidade_uf' , valor: $scope.fields['naturalidade_uf'] ? $scope.fields['naturalidade_uf'] : ""});
			dados.push({chave : 'naturalidade' , valor: $scope.fields['naturalidade'].title ? $scope.fields['naturalidade'].title : ""});
			
			//Dados Obrigatórios para cadastro junto ao SICLI que não são obtidos via Tela
			
			dados.push({chave : 'tipo_ocupacao', valor: 'I'});
			dados.push({chave : 'codigo_pais_origem' , valor: "10"}); // País de Origem Brasil
			dados.push({chave : 'tipo_pessoa' , valor: "1"});   //Tipo Pessoa Física

			
			if($scope.isAddEnderecoComercial){
				$scope.addCamposEnderecoComercial(dados);
			}
			
			if($scope.isAddEnderecoResidencial){
				$scope.addCamposEnderecoResidencial(dados);
			}
			
			var sms = {
        			bloco_sms : false,
        			sms_celular: null,
        			sms_valor_saque_trans: null,
        			sms_valor_cartao: null,
        			sms_fgts: null,
        			sms_produtos_servicos: null,
        			sms_cartao_credito: null,
        			email: null
        		};
			$scope.loadMeioComunicacaoToServer(dados, sms);
			$scope.saveSmsGroup(sms);
    		var obj = { cpf_cliente : SicpfService.getCpf(), dados : dados};
    		$rootScope.$broadcast(START_LOADING);
    		$http.post(URL_SIMTR + SERVICO_POST_DADOS_DECLARADOS, obj).then(function (res) {
    			if (res.status == 200) {
    				DossieService.setExtracao({});
    				DossieService.getExtracao().link = res.data.link;
    				DossieService.getExtracao().identificador = res.data.identificador;
    				atualizaAutorizacaoComExtracao(DossieService, DADOS_DECLARADOS);
    				DossieService.putBackUrl('declaracaodeDados');
    				$rootScope.go('digitalizacaodeDocumentos');
    			}
    		}, function (error) {
    			$rootScope.$broadcast(FINISH_LOADING);
    			$rootScope.putError(error);
    		});
    
    	
    }
    
    
    
    $scope.addCamposEnderecoResidencial = function(dados){
    	
    	$scope.fields['tipo_logradouro_residencial'] = ($scope.fields['tipo_logradouro_residencial'] ? $scope.fields['tipo_logradouro_residencial'].trim() : '');
    	$scope.fields['logradouro_residencial'] = ($scope.fields['logradouro_residencial'] ? $scope.fields['logradouro_residencial'].trim() : '');
  	  	$scope.fields['numero_residencial'] = ($scope.fields['numero_residencial'] ? $scope.fields['numero_residencial'].trim() : '');
  	  	$scope.fields['complemento_residencial'] = ($scope.fields['complemento_residencial'] ? $scope.fields['complemento_residencial'].trim() : '');
  	   
  	   var enderecoResidencial = $scope.fields['tipo_logradouro_residencial'] + ' ' +
  	   							 $scope.fields['logradouro_residencial'] + ' ' + 
  	   							 $scope.fields['numero_residencial'] + ' ' +
  	   							 $scope.fields['complemento_residencial'];
  	   
  	   	dados.push({chave : 'endereco_residencial' , valor: enderecoResidencial});
    	dados.push({chave : 'tipo_logradouro_residencial' , valor: $scope.fields['tipo_logradouro_residencial'] ? $scope.fields['tipo_logradouro_residencial'] : ""});
    	dados.push({chave : 'logradouro_residencial' , valor: $scope.fields['logradouro_residencial'] ? $scope.fields['logradouro_residencial'] : ""});
    	dados.push({chave : 'numero_residencial' , valor: $scope.fields['numero_residencial'] ? $scope.fields['numero_residencial'] : ""});
		dados.push({chave : 'complemento_residencial' , valor: $scope.fields['complemento_residencial'] ? $scope.fields['complemento_residencial'] : ""});
		dados.push({chave : 'bairro_residencial' , valor: $scope.fields['bairro_residencial'] ? $scope.fields['bairro_residencial'] : ""});
		dados.push({chave : 'uf_residencial' , valor: $scope.fields['uf_residencial'] ? $scope.fields['uf_residencial'] : ""});
		dados.push({chave : 'cidade_residencial' , valor: $scope.fields['cidade_residencial'] ? $scope.fields['cidade_residencial'] : ""});
		dados.push({chave : 'ano_referencia_residencial' , valor: new Date().getFullYear()});
		dados.push({chave : 'mes_referencia_residencial' , valor: new Date().getMonth() + 1});
		dados.push({chave : 'finalidade_residencial' , valor: "1"});
		dados.push({chave : 'indicador_comprovacao_residencial' , valor: "N"});
		dados.push({chave : 'indicador_correspondencia_residencial' , valor: "S"});
		dados.push({chave : 'tipo_endereco_residencial' , valor: "G"});
		
		var cepResidencial = $scope.fields['cep_residencial'];
		
		cepResidencial = cepResidencial.replace(/\D+/g, '');  
		if(cepResidencial && cepResidencial.length == 8){
			dados.push({chave : 'cep_residencial' , valor: cepResidencial.substring(0, 5)});
			dados.push({chave : 'cep_residencial_complemento' , valor: cepResidencial.substring(cepResidencial.length - 3, cepResidencial.length)});
		}
		
    }
    
    
   $scope.addCamposEnderecoComercial = function(dados){
	   
		$scope.fields['tipo_logradouro_comercial'] = ($scope.fields['tipo_logradouro_comercial'] ? $scope.fields['tipo_logradouro_comercial'].trim() : '');
    	$scope.fields['logradouro_comercial'] = ($scope.fields['logradouro_comercial'] ? $scope.fields['logradouro_comercial'].trim() : '');
  	  	$scope.fields['numero_comercial'] = ($scope.fields['numero_comercial'] ? $scope.fields['numero_comercial'].trim() : '');
  	  	$scope.fields['complemento_comercial'] = ($scope.fields['complemento_comercial'] ? $scope.fields['complemento_comercial'].trim() : '');
  	   
  	   var enderecocomercial = $scope.fields['tipo_logradouro_comercial'] + ' ' +
  	   							 $scope.fields['logradouro_comercial'] + ' ' + 
  	   							 $scope.fields['numero_comercial'] + ' ' +
  	   							 $scope.fields['complemento_comercial'];	   
	 
  	   dados.push({chave : 'endereco_comercial' , valor: enderecocomercial});
	   dados.push({chave : 'tipo_logradouro_comercial' , valor: $scope.fields['tipo_logradouro_comercial'] ? $scope.fields['tipo_logradouro_comercial'].trim() : ""});
	   dados.push({chave : 'logradouro_comercial' , valor: $scope.fields['logradouro_comercial'] ? $scope.fields['logradouro_comercial'] : ""});
	   dados.push({chave : 'numero_comercial' , valor: $scope.fields['numero_comercial'] ? $scope.fields['numero_comercial'] : ""});
	   dados.push({chave : 'complemento_comercial' , valor: $scope.fields['complemento_comercial'] ? $scope.fields['complemento_comercial'] : ""});
	   dados.push({chave : 'bairro_comercial' , valor: $scope.fields['bairro_comercial'] ? $scope.fields['bairro_comercial'] : ""});
	   dados.push({chave : 'uf_comercial' , valor: $scope.fields['uf_comercial'] ? $scope.fields['uf_comercial'] : ""});
	   dados.push({chave : 'cidade_comercial' , valor: $scope.fields['cidade_comercial'] ? $scope.fields['cidade_comercial'] : ""});
	   dados.push({chave : 'ano_referencia_comercial' , valor: new Date().getFullYear()});
	   dados.push({chave : 'mes_referencia_comercial' , valor: new Date().getMonth() + 1});
	   dados.push({chave : 'finalidade_comercial' , valor: "3"});
	   dados.push({chave : 'indicador_comprovacao_comercial' , valor: "N"});
	   dados.push({chave : 'indicador_correspondencia_comercial' , valor: "S"});
	   dados.push({chave : 'tipo_endereco_comercial' , valor: "N"});
	
	   var cepComercial = $scope.fields['cep_comercial'];
	   cepComercial = cepComercial.replace(/\D+/g, '');  
		if(cepComercial && cepComercial.length == 8){
			dados.push({chave : 'cep_comercial' , valor: cepComercial.substring(0, 5)});
			dados.push({chave : 'cep_comercial_complemento' , valor: cepComercial.substring(cepComercial.length - 3, cepComercial.length)});
		}
   }
      
    
    $scope.consultaCpf = function(field){
    	$scope.operacaoSelected = 1;
    	var obj = {
    			cpf_cliente : Number($scope.fields['conjuge_cpf']),
    			integracao : INTEGRACAO,
    			operacao : $scope.operacaoSelected,
    			modalidade : 0
        };

		var urlSicpf = URL_SIMTR + SERVICO_SICPF + '/' + obj.cpf_cliente;
     	$http.get(urlSicpf).then(function (respon) {
     		$scope.fields['conjuge_nascimento'] = moment(respon.data.data_nascimento, 'DD.MM.YYYY');
     		$scope.fields['conjuge_nome'] = respon.data.nome;
     	}, function (errorCpf) {
     		
     	});
    }
    
    
    $scope.saveSmsGroup = function (sms) {
    	if ($scope.fields['bloco_sms']) {
    		sms.bloco_sms = true;
    		sms.sms_valor_saque_trans = $scope.fields['sms_valor_saque_trans'];
    		sms.sms_valor_cartao = $scope.fields['sms_valor_cartao'];
    		sms.sms_fgts = $scope.fields['sms_fgts'];
    		sms.sms_produtos_servicos = $scope.fields['sms_produtos_servicos'];
    		sms.sms_cartao_credito = $scope.fields['sms_cartao_credito'];
    	} else {
    		sms = {};
    	}
    	
    	DossieService.setSms(sms);
    }
    
    $scope.loadMeioComunicacaoToServer = function (dados , sms) {
    	var countEmail = 0;
    	var countTelefone = 1;
    	for (var i = 0 ; i < $scope.meioComunicacaoList.length; i++) {
    		if ($scope.meioComunicacaoList[i].type == 3) {
				dados.push({chave: countEmail == 0 ? 'email_principal' :  'email_secundario', valor: $scope.meioComunicacaoList[i].value});
				dados.push({chave: countEmail == 0 ? 'tipo_comunicacao_email_principal' :  'tipo_comunicacao_email_secundario', valor:  '2'});
				dados.push({chave: countEmail == 0 ? 'meio_comunicacao_5' :  'meio_comunicacao_6', valor:  'e-mail'});
				dados.push({chave: countEmail == 0 ? 'tipo_email_principal' :  'tipo_email_secundario',valor:  '1'});
				if (countEmail == 0 && sms.email == null) {
					sms.email = $scope.meioComunicacaoList[i].value;
				}
    			countEmail++;
    		} else {
    			dados.push({chave: 'telefone_' + countTelefone , valor : $scope.meioComunicacaoList[i].value.substring(2, $scope.meioComunicacaoList[i].value.length)});
    			dados.push({chave: 'ddd_' + countTelefone , valor : $scope.meioComunicacaoList[i].value.substring(0,2)});
    			dados.push({chave:  'tipo_comunicacao_' + countTelefone, valor:  '1'});
    			dados.push({chave:  'tipo_telefone_' + countTelefone, valor:  $scope.meioComunicacaoList[i].type ==  1 ? '1' : '4'}); // 1 - Comum / 2- fax / 3 - PABX / 4 - Celular
    			dados.push({chave:  'meio_comunicacao_' + countTelefone, valor:  'Telefone'});
    			dados.push({chave: 'contato_' + countTelefone , valor : SicpfService.getSicpf()['nome-contribuinte']});
    			
    			if ($scope.meioComunicacaoList[i].type == 2 && sms.sms_celular == null) {
    				sms.sms_celular = $scope.meioComunicacaoList[i].value.substring(0,2) + $scope.meioComunicacaoList[i].value.substring(2, $scope.meioComunicacaoList[i].value.length);
    			}
    			countTelefone++;
    		}
    	}
    }
    
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.hideAlert();
    
    $scope.carregarMeiosComunicacao = function(i) {
    	var ddd = $scope.fields['ddd_'+i];
    	var telefone = $scope.fields['telefone_'+i];
    	if(ddd){
    		$scope.meioComunicacaoList.push({type : $scope.fields['tipo_telefone_'+i], value : ddd + telefone});
    		if($scope.fields['tipo_telefone_'+i]== 4){
    			$scope.meioComunicacaoCelular.push({type : $scope.fields['tipo_telefone_'+i], pos: $scope.meioComunicacaoCelular.length, value : ddd + telefone});	
    		}
    		$scope.carregarMeiosComunicacao(++i);	 
    	}
    	
    }
    
    $scope.carregarEmail = function() {
    	if($scope.fields['email_principal']){
    		$scope.meioComunicacaoList.push({type : 3, value : $scope.fields['email_principal']});	
    	}
    	if($scope.fields['email_secundario']){
    		$scope.meioComunicacaoList.push({type : 3, value : $scope.fields['email_secundario']});	
    	}
    	
    }
    
    $scope.listar = function() {
    	$rootScope.$broadcast(START_LOADING);
    	Analytics.trackEvent('DeclaracaodedadosController','listar()');
        WebServiceX.read("ws/aberturadeconta/declaracaodedados", $rootScope.headers)
      	.then(function(res) {
      		Analytics.trackEvent('DeclaracaodedadosController','listar():success');
      		if(!res.temErro) {
      			if (res.data) 
		            $scope.container = res.data[0];
      			if (res.tipo) {
      				$scope.container = res.tipo[0];
      				 
      				$http.get(URL_SIMTR  + SERVICO_POST_CONSULTAR_DADOS_DECLARADOS + SicpfService.getCpf()).then(function (respon) {
      					if($rootScope.declaracaoDadosParametro == 'alterar'){

      						for(var i = 0; i < respon.data.length; i++){
	      						$scope.fields[respon.data[i].chave] = respon.data[i].valor;
	      					}
	      					$scope.carregarMeiosComunicacao(1);
	      					$scope.fields['valor_total_patrimonio'] = $scope.fields['valor_total_patrimonio'].replace(',', '.');
	      					$scope.fields['declaracao_proposito'] = $scope.fields['declaracao_proposito'].toString().split(',');
	      					$scope.fields['conjuge_nascimento'] = moment( $scope.fields['conjuge_nascimento'],'DD-MM-YYYY').toDate();
	      					$scope.fields['valor_total_patrimonio'] = new Number($scope.fields['valor_total_patrimonio']).toLocaleString("pt-BR", { style: "currency" , currency:"BRL"});
	      					for (var i = 0 ; i < $scope.meioComunicacaoCelular.length; i++) {
	      						if($scope.meioComunicacaoCelular[i].value == $scope.fields['sms_telefone']){
	      							$scope.meioComunicaoCelularSelected = i;
	      						}
	      					}
	      					
	      					if($scope.fields['sms_telefone']){
	      						$scope.fields['bloco_sms'] = true;
	      					}
	      					
	      					$scope.carregarEmail();
	      					
	      					if($scope.fields['descricao_ocupacao']){
	      						$scope.profissaoCadastrada = $scope.fields['descricao_ocupacao'];
	      						$scope.fields['profissao'] = {};
	      						$scope.fields['profissao'].description = {};
	      						$scope.fields['profissao'].description.nome = $scope.fields['descricao_ocupacao'];
	      						$scope.fields['profissao'].description.codigo = $scope.fields['codigo_ocupacao'];
	      					}
	      					
	      					if($scope.fields['naturalidade']){
	      						$scope.naturalidadeCadastrada = $scope.fields['naturalidade'];
	      						var title = $scope.fields['naturalidade'];	      						
	      						$scope.fields['naturalidade'] = {} ;
	      						$scope.fields['naturalidade'].title = title;
	      					}
	      					
	      					
	      					if($scope.fields['cep_residencial']){
	      						$scope.fields['cep_residencial'] = $scope.fields['cep_residencial'] + '-' + $scope.fields['cep_residencial_complemento']; 
	      					}
	      					
	      					if($scope.fields['cep_comercial']){
	      						$scope.fields['cep_comercial'] = $scope.fields['cep_comercial'] + '-' + $scope.fields['cep_comercial_complemento']; 
	      					}
	      					
	      					$scope.isInformarPatrimonio =  $scope.fields['tipo_patrimonio'] == 'C' ? true : false;
	      					

	      					if($scope.fields['pais_crs'] || $scope.fields['n_tin']){

	      						$scope.fields['declaracao_fatca_crs'] = true;
	      						
	      						if($scope.fields['pais_crs']){
	      							$scope.fields['declaracao'] = 'CRS';
	      						}else if($scope.fields['n_tin']){
	      							$scope.fields['declaracao'] = 'FATCA';
	      						}
	      					}else{
	      						$scope.fields['declaracao_fatca_crs'] = false;
	      					}	
	      					
	      					
      				
      						$scope.fields['sms_valor_saque_trans'] = $scope.fields['sms_valor_saque_trans'] ? 'R$' + $scope.fields['sms_valor_saque_trans'].replace('.',',') : '';
      						$scope.fields['sms_valor_cartao'] = $scope.fields['sms_valor_cartao'] ? 'R$' + $scope.fields['sms_valor_cartao'].replace('.',',') : ''; 
	      					
	      					
	      					$scope.fields['sms_fgts'] = $scope.fields['sms_fgts'] == 'Sim' ? true : false;
	      					$scope.fields['sms_produtos_servicos'] = $scope.fields['sms_produtos_servicos'] == 'Sim' ? true : false;
	      					$scope.fields['sms_cartao_credito'] = $scope.fields['sms_cartao_credito'] == 'Sim' ? true : false;
	      					$scope.carregarMunicipios($scope.fields['naturalidade_uf']);
      					}
      				}, function (error) {
      		    		$rootScope.putError(error);
      		    	});
      			}
	     			
      		} else if(res.temErro) {
      			console.info(res.msgsErro[0]);
				$scope.$apply();
      		}
      		
      		
      		
      		
      	}, function(xhr, status, err) {
				var message = $translate.instant('label.falha.listar.controller') + "declaracaodedadosController";
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
        
        $rootScope.$broadcast(FINISH_LOADING);
    };
    
    
//    $scope.validaCEP = function (parameter) {
//    	var cep;
//    	if (parameter === 'RESIDENCIAL') {
//    		cep = $scope.fields['cep_residencial'];
//    	} else {
//    		cep = $scope.fields['cep_comercial'];
//    	}
//    	if(cep === undefined || cep === '') {
//    		if (parameter === 'RESIDENCIAL') {
//    			$('#btnSearchCepResidencial').prop('disabled', true);
//    		} else {
//    			$('#btnSearchCepComercial').prop('disabled', true);
//    		}
//    	} else {
//    		if (parameter === 'RESIDENCIAL') {
//    			$('#btnSearchCepResidencial').prop('disabled', false);
//    		} else {
//    			$('#btnSearchCepComercial').prop('disabled', false);
//    		}
//
//    	}
//    }    
    
    $scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Declaração de Dados', $http, 'Interrompido', 'principal');
    }
    
    $scope.buscarCep = function (parameter) {
    	var cep;
    	if (parameter === 'RESIDENCIAL') {
    		cep = $scope.fields['cep_residencial'];
    	}  else {
    		cep = $scope.fields['cep_comercial'];
    	}
		$.ajax({
	        url: URL_SIMTR + SERVICO_LOCALIDADE + cep,
	        type: 'GET',
	        dataType:"json",
	        beforeSend: function(request){
	            request.withCredentials = true;
	            request.setRequestHeader('Authorization', 'Bearer ' + localStorage['token']);
	        },
	        success: function(result){
	        	if(parameter === 'RESIDENCIAL'){
	        		if(result !== undefined){
	        			$scope.fields['tipo_logradouro_residencial'] = (result.enderecos[0].nomeTipoLogradouro ? result.enderecos[0].nomeTipoLogradouro.trim() : result.enderecos[0].nomeTipoLogradouro);
	        			$scope.fields['logradouro_residencial'] = (result.enderecos[0].nomeLogradouro ? result.enderecos[0].nomeLogradouro.trim() : result.enderecos[0].nomeLogradouro);
	        			//$scope.fields['numero_residencial'] = result.enderecos[0].numeroLocalidade;
	        			$scope.fields['complemento_residencial'] = (result.enderecos[0].nomeLogradouroComplemento ? result.enderecos[0].nomeLogradouroComplemento.trim() : result.enderecos[0].nomeLogradouroComplemento);
	        			$scope.fields['bairro_residencial'] = (result.enderecos[0].nomeBairro ? result.enderecos[0].nomeBairro.trim() : result.enderecos[0].nomeBairro);
	        			$scope.fields['uf_residencial'] = (result.enderecos[0].siglaUf ? result.enderecos[0].siglaUf.trim() : result.enderecos[0].siglaUf);
	        			$scope.fields['cidade_residencial'] = (result.enderecos[0].nomeLocalidade ? result.enderecos[0].nomeLocalidade.trim() : result.enderecos[0].nomeLocalidade);
	        			$('#txtCidadeResidencial').prop('disabled', true);
	        			$('#txtUfResidencial').prop('disabled', true);
	        		} else {
	        			$('#txtCidadeResidencial').prop('disabled', false);
	        			$('#txtUfResidencial').prop('disabled', false);
	        			GrowlMessage.addWarningMessage('CEP não encontrado');
	        		}
	        	} else {
	         		if(result !== undefined){
	         			$scope.fields['tipo_logradouro_comercial'] = (result.enderecos[0].nomeTipoLogradouro ? result.enderecos[0].nomeTipoLogradouro.trim() : result.enderecos[0].nomeTipoLogradouro);
	         			$scope.fields['logradouro_comercial'] = (result.enderecos[0].nomeLogradouro ? result.enderecos[0].nomeLogradouro.trim() : result.enderecos[0].nomeLogradouro);
	         			$scope.fields['complemento_comercial'] = (result.enderecos[0].nomeLogradouroComplemento ? result.enderecos[0].nomeLogradouroComplemento.trim() : result.enderecos[0].nomeLogradouroComplemento);
	         			$scope.fields['bairro_comercial'] = (result.enderecos[0].nomeBairro ? result.enderecos[0].nomeBairro.trim() : result.enderecos[0].nomeBairro);
	         			$scope.fields['uf_comercial'] = (result.enderecos[0].siglaUf ? result.enderecos[0].siglaUf.trim() : result.enderecos[0].siglaUf);
	         			$scope.fields['cidade_comercial'] = (result.enderecos[0].nomeLocalidade ? result.enderecos[0].nomeLocalidade.trim() : result.enderecos[0].nomeLocalidade);      		
	        			$('#txtCidadeComercial').prop('disabled', true);
	        			$('#txtUfComercial').prop('disabled', true);
	        		} else {
	        			$('#txtCidadeComercial').prop('disabled', false);
	        			$('#txtUfComercial').prop('disabled', false);
	         			GrowlMessage.addWarningMessage('CEP não encontrado');
	         		}
	        	}
				$scope.$apply();
	        },
	        error: function(error){
	        	GrowlMessage.addErrorMessage('Serviço indisponível');
	        }
	    });    	
    }
    
    $scope.listar();
    $scope.carregarEstados();
  
    $scope.carregarListProfissoes();
    $scope.carregarSexo();
    $scope.carregarGrauInstrucao();
    $scope.carregarEstadoCivil();
    $scope.carregarEmail();
    $rootScope.initScroll();
    mascaraMoeda($('#txtValorTotalPatrimonio'));

});