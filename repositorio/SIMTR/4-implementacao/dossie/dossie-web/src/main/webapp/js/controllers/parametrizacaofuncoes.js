'use strict';
angular.module('webApp').controller('ParametrizacaoFuncoesController', function ($scope, $rootScope, $translate, $http, $window, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
	
	   Log.debug("ParametrizacaoFuncoesController()");
       Analytics.trackEvent('controller','ParametrizacaoFuncoesController');
       
       $rootScope.currentview.id = 'parametrizacaofuncoes';
       $rootScope.currentview.group = 'parametrizacaofuncoes';
       $rootScope.currentview.title = 'Parametrização de Funções por Tipos Documentais';
       $rootScope.currentview.icon = 'none';
       $rootScope.currentview.locked = false;
   	   $rootScope.currentview.menu = true;
       $rootScope.currentview.description = '';
       $scope.valuesSelects = [];
       $scope.listaDocumentosDisponiveis = [];
       $scope.listaDocs = [];
       $scope.itemParaExclusao;
       $scope.nomeParametrizacao = "";
       
       $scope.listaFuncoes = [];
       $scope.listaFuncoesCompleta = [];
       $scope.listaSafe = [];
       $scope.ultimoTipoCarregado = 0;
       
       $scope.carregarParametrizacoes = function(){
    	   $scope.listaFuncoes = [];
           $scope.listaSafe = [];
           $scope.listaFuncoesCompleta = [];
    	   $http.get(URL_SIMTR + SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL).then(function (respon) {
    		   respon.data.forEach(function(item){
    			  var sublista = [];
    			  item.tipos_documento.forEach(function(sub){
    				  sublista.push({id: sub.id_tipo_documento, nome: sub.nome_tipo_documento});
    			  });
    			  
    			  if(item.tipos_documento && item.tipos_documento.length > 0){
    				  $scope.listaFuncoes.push({id: item.id_funcao_documental, nome: item.nome_funcao_documental, checked: false, documentos: sublista});
    				  $scope.listaFuncoesCompleta.push({id: item.id_funcao_documental, nome: item.nome_funcao_documental, checked: false, documentos: sublista});
    			  }else{
    				  $scope.listaFuncoesCompleta.push({id: item.id_funcao_documental, nome: item.nome_funcao_documental, checked: false, documentos: sublista});
    			  }
    			  
    		   });
    		   
        		$scope.listaSafe = $scope.listaFuncoes;
        		$rootScope.$broadcast(FINISH_LOADING);
        		
        	}, function (errorCpf) {
        		
        	});
    	   
       };
       
       
       $scope.setFuncao = function (value){	
			if(value != undefined){
				for(var i = 0; $scope.listaFuncoesCompleta.length > i; i++){
					if($scope.listaFuncoesCompleta[i] == $scope.nomeParametrizacao){
						$scope.nomeParametrizacao = value;
					}
				}		
			}
			
		};
       
       $scope.carregarDocumentosDisponiveis = function(){
    	   $http.get(URL_SIMTR + SERVICO_GET_PARAMETROS_TIPO_DOCUMENTO).then(function (respon) {
    		   respon.data.forEach(function(item){
    			   $scope.listaDocumentosDisponiveis.push({id: item.id_tipo_documento, nome : item.nome_tipo_documento});
    		   });
          		$scope.listaSafe = $scope.listaFuncoes;
        	}, function (errorCpf) {
        		
        	});
       };
       
       $scope.carregarTiposDocumentos = function(id, nome){
    	   if(id != $scope.ultimoTipoCarregado){
    		   $scope.ultimoTipoCarregado = id;
    		   $scope.nomeParametrizacao = nome;
    		   $("#cmbFuncao_value").val(nome);
    		   $rootScope.idFuncao = id;
    		   $scope.listaTiposDocumentos = [];
    		   $scope.listaFuncoes.forEach(function(item){
    			   if(item.id == id){
    				   $scope.picklist_src = [];
    				   item.documentos.forEach(function(doc){
    					   $scope.srcoptions.forEach(function(src){
    						   if(src.id == doc.id){
    							   $scope.picklist_src.push(src);  
    						   }
    						   
    					   });
    				   });
    			   }
    		   });
    		   $scope.rightShift();
    	   }
       };
       
       $scope.salvarFuncao = function(){
    	   $scope.nomeParametrizacao = $("#cmbFuncao_value").val();
    	   if($scope.nomeParametrizacao){
    		   delete $rootScope.idFuncao;
    		   $scope.leftShiftAll();
    			   $scope.listaFuncoesCompleta.forEach(function(item){
    				   if(item.nome.toUpperCase() == $scope.nomeParametrizacao.toUpperCase()){
    					   $rootScope.idFuncao = item.id;
    				   }
    			   });
    			   if(!$rootScope.idFuncao){
    				   Alert.showConfirm("Confirma a inclusão da função documental?", "Confirma a inclusão da função documental?", $scope.incluirFuncao, $scope.limparDados);
    			   }else{
    				   delete $scope.ultimoTipoCarregado;
    				   $scope.carregarTiposDocumentos($rootScope.idFuncao, $scope.nomeParametrizacao);
    			   }
    	   }
       };
       
       $scope.apresentarModalSalvarParametrizacao = function(){
    	   if($scope.nomeParametrizacao){

    		   $rootScope.listaIds = [];
    		   $scope.destoptions.forEach(function(item){
    			   $rootScope.listaIds.push(item.id) 
    		   });
    		   if($scope.checkField($scope.nomeParametrizacao, 'nome-parametrizacao')){
    			   if($rootScope.listaIds.length > 0){
    				   var inclusao = false;
    				   $scope.listaFuncoes.forEach(function(item){
    					   if($rootScope.idFuncao == item.id){
    						   inclusao = true;
    					   }
    				   });
    				   
    				   if(inclusao){
    					   Alert.showConfirm("Confirma a alteração da Parametrização?", "Confirma a alteração da Parametrização?", $scope.salvarParametrizacao, $scope.limparDados);
    				   }else{
    					   Alert.showConfirm("Confirma a inclusão da Parametrização?", "Confirma a inclusão da Parametrização?", $scope.incluirParametrizacao, $scope.limparDados);
    				   }   
    			   }else {
    				   GrowlMessage.addErrorMessage("Adicione pelo menos um tipo de documento.");
    			   }
    		   }else{
    			   GrowlMessage.addErrorMessage("Nome da Função é obrigatório.");
    		   }
    		   
    	   }
    	   
       };
       
       $scope.apresentarModalExcluirParametrizacao = function(id) {
    	   $rootScope.itemParaExclusao = id;
    	   Alert.showConfirm("Deseja excluir a parametrização?", "Deseja excluir a parametrização?", $scope.deletarRegistro, null);
       };
       
       $scope.deletarRegistro = function(id){
    	   $http.delete(URL_SIMTR + SERVICO_DELETE_PARAMETRIZACAO_FUNCAO + $rootScope.itemParaExclusao).then(function (respon) {
    		   $scope.carregarParametrizacoes();
    		   $scope.leftShiftAll();
    		   GrowlMessage.addNoticeMessage("Registro Excluído com sucesso!");
    		   $("#cmbFuncao_value").val("");
    	   }, function (errorCpf) {
    		   GrowlMessage.addErrorMessage("Este registro não pode ser excluído.");
    	   });
       };
       
       $scope.incluirFuncao = function(){
		   $rootScope.$broadcast(START_LOADING);
		   var lTiposDocumentos = [];

		   var obj = {nome_funcao_documental: $scope.nomeParametrizacao,
				   	tipos_documento: lTiposDocumentos};
		   
    	   $http.post(URL_SIMTR + SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL, obj).then(function (res) {
				$scope.leftShiftAll();
				GrowlMessage.addNoticeMessage("Função incluida com sucesso!");
				$scope.carregarParametrizacoes();
			}, function (error) {
				$rootScope.putError(error);
			});   
    	   
       };
       
       $scope.limparDados = function(){
    	    delete $scope.nomeParametrizacao;
    	    delete $scope.ultimoTipoCarregado;
			$scope.leftShiftAll();
			delete $rootScope.idFuncao;
			delete $rootScope.listaIds;
			$("#cmbFuncao_value").val("");
       };
       
       $scope.salvarParametrizacao = function(){
		   $rootScope.$broadcast(START_LOADING);   
    	   $http.put(URL_SIMTR + SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL + '/' + $rootScope.idFuncao + '/tipos-documento', $rootScope.listaIds).then(function (res) {
				delete $rootScope.idFuncao;
				delete $rootScope.listaIds;
				delete $scope.ultimoTipoCarregado;
				$scope.leftShiftAll();
				$scope.carregarParametrizacoes();
				GrowlMessage.addNoticeMessage("Parametrização Alterada com sucesso!");
				$("#cmbFuncao_value").val("");
				
			}, function (error) {
				$rootScope.putError(error);
			});   
       };
       
       $scope.incluirParametrizacao = function(){
    	   $rootScope.$broadcast(START_LOADING);
    	   $scope.nomeParametrizacao = $("#cmbFuncao_value").val(); 
    	   $scope.listaFuncoesCompleta.forEach(function(item){
    		   if($scope.nomeParametrizacao.toUpperCase() === item.nome.toUpperCase()){
				   $rootScope.idFuncao = item.id;
			   }
    	   })
    	   
    	   $http.put(URL_SIMTR + SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL +'/'+ $rootScope.idFuncao + '/tipos-documento', $rootScope.listaIds).then(function (res) {
				delete $rootScope.idFuncao;
				delete $rootScope.listaIds;
				$scope.nomeParametrizacao = "";
				$scope.leftShiftAll();
				$scope.carregarParametrizacoes();
				GrowlMessage.addNoticeMessage("Parametrização Incluída com sucesso!");
				$("#cmbFuncao_value").val("");
				
			}, function (error) {
				$rootScope.putError(error);
			});   
       };
       
       
       $rootScope.$broadcast(START_LOADING);
       $scope.carregarDocumentosDisponiveis();
       $scope.carregarParametrizacoes();
       
       $scope.checkField = function (valor, name){
	       	var valid = false;
	       	if(valor){
	       		valid = true;
	       		$('.label-'+name).css({"color": "black"});
	       		$('.'+name).removeClass('has-error');
	       		return true;
	       	}
	       	validaCampo(name, valid);
	       	return false;
       };
       
       
       
});