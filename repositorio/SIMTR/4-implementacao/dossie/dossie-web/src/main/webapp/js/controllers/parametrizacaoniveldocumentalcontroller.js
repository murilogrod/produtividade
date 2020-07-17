'use strict';
angular.module('webApp').controller('ParametrizacaoNivelDocumentalController', function ($scope, $rootScope, $translate, $location, $http, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
	
	   Log.debug("ParametrizacaoNivelDocumentalController()");
       Analytics.trackEvent('controller','ParametrizacaoNivelDocumentalController');
       
       $rootScope.currentview.id = 'parametrizacaoNivelDocumental';
       $rootScope.currentview.group = 'parametrizacaoNivelDocumental';
       $rootScope.currentview.title = 'Parametrização de Funções por Tipos Documentais';
       $rootScope.currentview.icon = 'none';
       $rootScope.currentview.locked = false;
   	   $rootScope.currentview.menu = true;
       $rootScope.currentview.description = '';
       $scope.valuesSelects = [];
       $scope.listaDocumentosDisponiveis = [];
       $scope.listaDocs = [];
       $scope.itemParaExclusao;
       
       $scope.listaNiveis = [];
       $scope.listaSafe = [];
       
       $scope.carregarParametrizacoes = function(){
    	   $scope.listaNiveis = [];
    	   $scope.listaNiveisParametrizados = [];
           $scope.listaSafe = []; 
    	   $http.get(URL_SIMTR + SERVICO_COMPOSICAO_DOCUMENTAL).then(function (respon) {
    		   respon.data.forEach(function(item){
    			  var sublista = [];
    			  item.regras_associadas.forEach(function(sub){
    				  sublista.push({id_regra_documental: sub.id_regra_documental, id_tipo_documento : sub.id_tipo_documento,  id_funcao_documental: sub.id_funcao_documental });
    			  });
    			  $scope.listaNiveis.push({id: item.id_composicao_documental, nome: item.nome_composicao_documental, checked: false, regras: sublista}); 
    			  
    			  if(sublista.length > 0){
    				  $scope.listaNiveisParametrizados.push({id: item.id_composicao_documental, nome: item.nome_composicao_documental, checked: false, regras: sublista});
    			  }
    			  
    		   });
    		   
        		$scope.listaSafe = $scope.listaNiveisParametrizados;
        		$rootScope.$broadcast(FINISH_LOADING);
        	}, function (errorCpf) {
        		$rootScope.$broadcast(FINISH_LOADING);
        		$rootScope.putErrorString("Erro ao Obter Lista de Composição Documental!");
        	});
    	   
       };
       
       
       $scope.carregarFuncoesDocumentais = function(){
    	   $http.get(URL_SIMTR + SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL).then(function (respon) {
    		   respon.data.forEach(function(item){
    			  if(item.tipos_documento.length > 0){
    				  $scope.listaDocumentosDisponiveis.push({id: item.id_funcao_documental, nome: item.nome_funcao_documental, checked: false, documentos: item.tipos_documento});
    			  }
    		   });
    		   
    		   $scope.carregarParametrizacoes();
    		   
        	}, function (errorCpf) {
        		$rootScope.putErrorString("Erro ao Obter Lista de Funções Documental!");
        		$rootScope.$broadcast(FINISH_LOADING);
        	});
    	   
       };
       
       $scope.carregarRegrasFuncoesDocumentos = function(idComposicao){
    	   if($scope.idComposicao != idComposicao){
    		   $scope.leftShiftAll();
    		   $scope.idComposicao = idComposicao;
    		   $scope.listaFuncoesVinculadas = [];
    		   var regras = $scope.getRegrasDaComposicao(idComposicao);   
    		   $scope.picklist_src = [];
    		   regras.forEach(function(doc){
    			   $scope.srcoptions.forEach(function(src){
    				   if(src.id == doc.id_funcao_documental){
    					   $scope.picklist_src.push(src);  
    				   }
    			   });
    		   });
    		   $scope.rightShift();
    	   }
       };
       
       
       $scope.getListaRegrasParaAtualizar = function(idComposicao){
    	 
    	   var regrasParaAtualizar = [];
    	   
    	   $scope.listaFuncoesVinculadas.forEach(function (f){
    		   var regra = $scope.getRegraCadastrada(f.id);
    		   if(regra == null){
    			   regra = {id_funcao_documental: f.id, id_regra_documental: null,  id_tipo_documento : null};
    		   }
    		   
    		   regrasParaAtualizar.push(regra);
    		
       	   });
    	   
    	   return regrasParaAtualizar;
       }
       
       $scope.getRegraCadastrada = function (idFuncao){
    	   var regra = null; 
    	   var regrasDaComposicao = $scope.getRegrasDaComposicao($scope.idComposicao);
    	   regrasDaComposicao.forEach(function(regraCadastrada){
    		   if(regraCadastrada.id_funcao_documental == idFuncao){
    			   regra = regraCadastrada;
    		   }
    	   } );
    	   
    	   return regra;    	   
       }
       
       
       
       $scope.salvarNivelDocumental = function (){
    	   var listaRegras = $scope.getListaRegrasParaAtualizar($scope.idComposicao);
    	   
    	   $http.put(URL_SIMTR + SERVICO_COMPOSICAO_DOCUMENTAL + '/' + $scope.idComposicao + '/' + 'regras-documentais', listaRegras).then(function (respon) {
				$scope.limparDados();
				GrowlMessage.addNoticeMessage("Nível Documental Alterado com sucesso!");
    	   }, function (errorCpf) {
    			$rootScope.putErrorString("Erro ao Alterar Nível Documental!");
        		$rootScope.$broadcast(FINISH_LOADING);
    	   });
       };
       
       $scope.incluirNivelDocumental = function (){
    	   
    	   if($scope.nomeParametrizacao){
    		   delete $rootScope.idFuncao;
    		   $scope.leftShiftAll();
    			   $scope.listaNiveis.forEach(function(item){
    				   if(item.nome.toUpperCase() == $scope.nomeParametrizacao.toUpperCase()){
    					   $rootScope.idFuncao = item.id;
    				   }
    			   });
    			   
    			   if(!$rootScope.idFuncao){
    				   var obj = {
    						   id_composicao_documental : null,
    						   nome_composicao_documental: $scope.nomeParametrizacao,
    						   data_hora_inclusao : $scope.dataAtualFormatada(),
    						   data_hora_revogacao : null,
    						   matricula_inclusao: "string",
    						   matricula_revogacao: null,
    						   indicador_conclusao: true,
    						   regras_associadas : $scope.getListaRegrasParaAtualizar(null) 
    				   }
    				   
    				   $http.post(URL_SIMTR + SERVICO_COMPOSICAO_DOCUMENTAL, obj).then(function (respon) {
    					   GrowlMessage.addNoticeMessage("Nível Documental Incluído com sucesso!");
    				   }, function (errorCpf) {
    					   $rootScope.putErrorString("Erro ao Alterar Nível Documental!");
    					   $rootScope.$broadcast(FINISH_LOADING);
    				   });
    			   }else{
    				   delete $scope.ultimoTipoCarregado;
    				   $scope.carregarTiposDocumentos($rootScope.idFuncao, $scope.nomeParametrizacao);
    			   }
    	
    	   
    	   }
    	   
    	   
       }
       
       
       $scope.incluirParametrizacaoNivelDocumental = function (){
	       	if($scope.nomeParametrizacao){
	       		var obj = {
	       				id_composicao_documental : null,
	       				nome_composicao_documental: $scope.nomeParametrizacao,
	       				data_hora_inclusao : $scope.dataAtualFormatada(),
	       				data_hora_revogacao : null,
	       				matricula_inclusao: "string",
	       				matricula_revogacao: null,
	       				indicador_conclusao: true,
	       				regras_associadas : $scope.getListaRegrasParaAtualizar(null) 
	       		}
	       		
	       		$http.post(URL_SIMTR + SERVICO_COMPOSICAO_DOCUMENTAL, obj).then(function (respon) {
	       			$scope.limparDados();
	       			GrowlMessage.addNoticeMessage("Nível Documental Incluído com sucesso!");
	       		}, function (errorCpf) {
	       			$rootScope.putErrorString("Erro ao Alterar Nível Documental!");
	       			$rootScope.$broadcast(FINISH_LOADING);
	       		});
	       	}
    	   
    	   
       }
       $scope.dataAtualFormatada =  function (){
    	    var data = new Date(),
    	        dia  = data.getDate().toString(),
    	        diaF = (dia.length == 1) ? '0'+dia : dia,
    	        mes  = (data.getMonth()+1).toString(), //+1 pois no getMonth Janeiro começa com zero.
    	        mesF = (mes.length == 1) ? '0'+mes : mes,
    	        anoF = data.getFullYear();
    	    return diaF+"/"+mesF+"/"+anoF + " "+ "00:00:00";
       
       }
       
       
       $scope.limparDados = function(){
    	   $("#cmbFuncao_value").val("");
    	   $scope.carregarParametrizacoes()
		   $scope.leftShiftAll();
    	   delete $scope.idComposicao;
		   delete $rootScope.idFuncao;
		   delete $rootScope.listaIds;
       };
       

       
      
       $scope.getRegrasDaComposicao = function(idComposicao){
    	   var regras = [];
    	   $scope.listaNiveis.forEach(function(item){
    		   if(item.id == idComposicao){
    			   $scope.nomeParametrizacao = item.nome;
    			   $("#cmbFuncao_value").val(item.nome)
    			   regras = item.regras;
    		   }
    	   });
    	   
    	   return regras;
       };
    	   
       
       $scope.apresentarModalSalvarParametrizacao = function(){
    	   $scope.nomeParametrizacao = $("#cmbFuncao_value").val(); 
    	   if($scope.checkField($scope.nomeParametrizacao, 'nome-parametrizacao')){
    		   $rootScope.listaIds = [];
    		   $scope.destoptions.forEach(function(item){
    			   $rootScope.listaIds.push(item.id) 
    		   });
    		   if($rootScope.listaIds.length > 0){
				   if($scope.idComposicao){
					   Alert.showConfirm("Confirma a alteração da parametrização?", "Confirma a alteração da parametrização?", $scope.salvarNivelDocumental, $scope.limparDados);
				   }else{
					   Alert.showConfirm("Confirma a inclusão da parametrização?", "Confirma a inclusão da parametrização??", $scope.incluirParametrizacaoNivelDocumental, $scope.limparDados);
				   }
    		   }else{
    			   GrowlMessage.addErrorMessage("Adicione pelo menos um nível documento.");
    		   }
    	   }
    	   
       };
       
       $scope.iniciarModalSalvar = function (nomeParametrizacao){
    	   delete $rootScope.idComposicao;
    	   $scope.nomeParametrizacao = $("#cmbFuncao_value").val();
    	   if($scope.nomeParametrizacao){
    		   $scope.listaNiveis.forEach(function(item){
    			   if(item.nome.toUpperCase() === $scope.nomeParametrizacao.toUpperCase()){
    				   $rootScope.idComposicao = item.id;
    			   }
    		   });
    		   if(!$rootScope.idComposicao){
    			   Alert.showConfirm("Confirma a inclusão do nível documental?", "Confirma a inclusão do nível documental?", $scope.incluirNivelDocumental, $scope.limparDados);
    		   }
    	   }
       };
       
       $scope.apresentarModalExcluirParametrizacao = function(id) {
    	   $rootScope.itemParaExclusao = id;
    	   $("#modalExcluirProdutos").modal();  
       };
       
       
       $scope.apresentarModalExcluirParametrizacao = function(id) {
    	   $rootScope.itemParaExclusao = id;
    	   Alert.showConfirm("Confirma a exclusão da parametrização?", "Confirma a exclusão da parametrização?", $scope.deletarRegistro, null);
       };
       
       $scope.deletarRegistro = function(id){
    	   $http.delete(URL_SIMTR + SERVICO_COMPOSICAO_DOCUMENTAL + '/' + $rootScope.itemParaExclusao).then(function (respon) {
    		   $("#cmbFuncao_value").val("");
    		   $scope.leftShiftAll()
    		   $scope.limparDados();
    		   GrowlMessage.addNoticeMessage("Parametrização Excluída com sucesso!");
    	   }, function (errorCpf) {
    		   
    	   });
       };
       
       
     $rootScope.$broadcast(START_LOADING);
     $scope.carregarFuncoesDocumentais();
       
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