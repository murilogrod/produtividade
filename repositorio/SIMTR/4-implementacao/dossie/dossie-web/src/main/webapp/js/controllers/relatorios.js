
'use strict';
angular.module('webApp').controller('RelatoriosController', function ($scope, $rootScope, $translate, $location, $http, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
	
	   Log.debug("RelatoriosController()");
       Analytics.trackEvent('controller','RelatoriosController');
       
       $rootScope.currentview.id = 'relatorios';
       $rootScope.currentview.group = 'relatorios';
       $rootScope.currentview.title = 'Relatórios de Documentos';
       $rootScope.currentview.icon = 'none';
       $rootScope.currentview.locked = false;
   	   $rootScope.currentview.menu = true;
       $rootScope.currentview.description = '';
       $scope.listaUnidades = []; 
       $scope.listaUnidadesSr = [];
       $scope.resultadoRelatorio = [];
       $scope.agrupadorSelecionado = 'Dire';
       $scope.situacaoSelecionado = "Selecione...";
       $scope.dataInicialConsulta = "";
       $scope.dataFinalConsulta = "";
       $scope.agrupadorConsulta = "";
       
       $scope.listaSituacao = [ {id : "" , value : 'Selecione...'},
    	{id : 1 , value : 'Criado'},
       	{id: 2 , value: 'Atualizado'},
       	{id: 3 , value: 'Interrompido'}];
       
       $scope.listaAgrupador = [ 
    	    {id : 1 , value : 'Dire'},
    	    
          	{id: 2 , value: 'SR'},
          	{id: 3 , value: 'Agência'}];
       
       $scope.carregarFiltros = function(primeiraChamada){
    	   $rootScope.$broadcast(START_LOADING);
    	   $scope.listaDires = [];
    	   $scope.listaDires.push({email: "----", nome: "Selecione...",  superintendencias_regionais: []});
    	   $scope.listaDires.push(...$rootScope.organograma);
    	   $scope.listaUnidadesSr = [];
    	   $scope.direSelecionado = $scope.listaDires[0];
    	   
    	   $rootScope.$broadcast(START_LOADING);

    	   $scope.agrupadorConsulta = $scope.agrupadorSelecionado;
    	   
    	   var params = $scope.carregarParametrosConsulta();
    	   
    	   $http.get(URL_SIMTR  + SERVICO_CONSULTAR_RELATORIO + params).then(function (respon) {
    		   $scope.resumo = respon.data;
    		   $scope.resumo.total_documentos_utilizados = 0;
    		   $scope.resumo.total_dossie_interrompidos = 0;
    		   $scope.resumo.total_documentos_utilizados = 0;
    		   $scope.resumo.total_dossie_criado = 0;
    			   respon.data.resultado.forEach(function(item){
        			   if(item.acao === 'Interrompido'){
        				   $scope.resumo.total_dossie_interrompidos += item.total_dossie;
        			   } else {
        				   $scope.resumo.total_documentos_utilizados += item.total_documentos;
        				   $scope.resumo.total_dossie_criado += item.total_dossie;
        			   }
        			   
        		   });   
    	   }, function (error) {
	    		$rootScope.putError(error);
	       });
    	   
    	   
    	   
    	   
    	   $rootScope.$broadcast(FINISH_LOADING);
       };
       
       
       $scope.carregarTodasSReUndiades = function(){
    	   $scope.listaSrs = [];
    	   $scope.listaUnidadesSr = [];
    	   $scope.listaSrs.push({nome : "Selecione...", agencias : []});
    	   $scope.listaUnidadesSr.push({nome : "Selecione..."});
    	   if($rootScope.organograma){
    		   $rootScope.organograma.forEach(function (dire){
        		   $scope.listaSrs.push(...dire.superintendencias_regionais);
        		   dire.superintendencias_regionais.forEach(function (sr){
        			   $scope.listaUnidadesSr.push(...sr.agencias);
        		   });
        	   });   
    		   $scope.agenciaSelecionado = $scope.listaUnidadesSr[0];
    		   $scope.srSelecionado = $scope.listaSrs[0];
    	   }
    	   
       };
       
       $scope.carregarSrs = function(){
    	   $rootScope.$broadcast(START_LOADING);
    	   $scope.listaSrs = [];
    	   $scope.listaUnidadesSr = [];
    	   $scope.listaUnidadesSr.push({nome : "Selecione..."});
    	   $scope.listaSrs.push({nome : "Selecione...", agencias : []});
    	   
    	   $scope.listaSrs.push(...$scope.direSelecionado.superintendencias_regionais);
    	   $scope.srSelecionado = $scope.listaSrs[0];
    	   $scope.agenciaSelecionado = $scope.listaUnidadesSr[0];
    	   $rootScope.$broadcast(FINISH_LOADING);
       };
       
       $scope.carregarUnidades = function(){
    	   $rootScope.$broadcast(START_LOADING);
    	   $scope.listaUnidadesSr = [];
    	   $scope.listaUnidadesSr.push({nome : "Selecione..."});
    	   if($scope.srSelecionado){
    		   $scope.listaUnidadesSr.push(...$scope.srSelecionado.agencias);   
    	   }
    	   $scope.agenciaSelecionado = $scope.listaUnidadesSr[0];
    	   $rootScope.$broadcast(FINISH_LOADING);
       };
       
       $scope.consultar = function(){
    	   $rootScope.$broadcast(START_LOADING);

    	   $scope.agrupadorConsulta = $scope.agrupadorSelecionado;
    	   
    	   var params = $scope.carregarParametrosConsulta();
    	   
    	   $http.get(URL_SIMTR  + SERVICO_CONSULTAR_RELATORIO + params).then(function (respon) {
    		   $scope.resumo = respon.data;
    		   $scope.resultadoRelatorio = respon.data.resultado;
   			   if(respon.data.resultado.length > 0){
	   				$scope.resultadoRelatorio.forEach(function(resumo){
	     			   
	     			   $rootScope.organograma.forEach(function (dire){
	     				   if(dire.numero_unidade === resumo.agrupador){
	     					   resumo.agrupador = resumo.agrupador + " - " + dire.nome;
	
	     				   } else {
	     					   dire.superintendencias_regionais.forEach(function (sr){
	     						   
	     						   if(sr.numero_unidade === resumo.agrupador){
	     	    					   resumo.agrupador = resumo.agrupador + " - " + sr.nome;
	     						   }else if($scope.agrupadorSelecionado === "Agência"){
	     							   sr.agencias.forEach(function(agencia){
	                     				   if(agencia.numero_unidade === resumo.agrupador){
	                     					   resumo.agrupador = resumo.agrupador + " - " + agencia.nome;
	                     				   }
	                     			   });
	     						   }
	                 			  
	                 		   });
	     				   }
	             		   
	             	   });   
	     			   
	     		   });
    		   }else{
	    		   GrowlMessage.addErrorMessage("Nenhum Registro Encontrado.");   
    		   }
    		   
    		   
    		   $rootScope.$broadcast(FINISH_LOADING);
    	   }, function (error) {
	    		$rootScope.putError(error);
	       });
    	   
       };
       
       $scope.carregarParametrosConsulta = function(){
    	   var params = "?agrupador=" + $scope.agrupadorSelecionado;
    	   
    	   if($scope.dataInicial){
    		   $scope.dataInicialConsulta = moment( $scope.dataInicial, 'DD/MM/YYYY').toDate();
    		   params = params + "&data_inicio=" + $scope.dataInicialConsulta;
    		   
    	   }
    	   if($scope.dataFinal){
    		   $scope.dataFinalConsulta = moment( $scope.dataFinal, 'DD/MM/YYYY').toDate();
    		   params = params + "&data_fim=" + $scope.dataFinalConsulta;
    	   }
    	   if($scope.situacaoSelecionado && $scope.situacaoSelecionado != "Selecione..."){
    		   params = params + "&situacao=" + $scope.situacaoSelecionado;
    	   }
    	   if($scope.agenciaSelecionado && $scope.agenciaSelecionado.nome  != "Selecione..."){
    		   params = params + "&unidade=" + $scope.agenciaSelecionado.numero_unidade;   
    	   }
    	   if($scope.direSelecionado && $scope.direSelecionado.nome != "Selecione..."){
    		   params = params + "&dire=" + $scope.direSelecionado.numero_unidade;
    	   }
    	   if($scope.srSelecionado && $scope.srSelecionado.nome  != "Selecione..."){
    		   params = params + "&sr=" + $scope.srSelecionado.numero_unidade;
    	   }
    	   
    	   params = params + "&unidadeUsuario=" + angular.element(document.body).injector().get('KeycloakService').getUserSSO()['nu-lotacaofisica'];
    	   return params;
       };
       
       $scope.gerarRelatorioExcel = function(filtros){
    	    	$rootScope.$broadcast(START_LOADING);
    	    	
    	    	var params = '?acao='+filtros.acao;
    	    	
    	    	params = params + '&codigoAgrupador='+filtros.agrupador.split("-")[0].trim();
    	    	
    	    	if($scope.agrupadorConsulta){
    	    		params = params + '&agrupador='+$scope.agrupadorConsulta;
    	    	}
    	    	
    	    	if($scope.dataInicialConsulta){
    	    		params = params + '&data_inicio='+$scope.dataInicialConsulta;
    	    	}
    	    	
    	    	if($scope.dataFinalConsulta){
    	    		params = params + '&data_fim='+$scope.dataFinalConsulta;
    	    	}
    	    	
    	    	$http.get(URL_SIMTR  + SERVICO_GET_RELATORIO_DOSSIE + params,
    	    			{responseType: 'arraybuffer'}).then(function (res) {
    	    		var buffer = new Uint8Array(res.data);
    				var blob = new Blob([buffer], {type : 'application/vnd.ms-excel;charset=utf-8'});
    				 var objectUrl = URL.createObjectURL(blob);
    		            window.open(objectUrl);
    		            $rootScope.$broadcast(FINISH_LOADING);
    				
    	    	}, function (error) {
    	    		$rootScope.putError(error);
    	    		 $rootScope.$broadcast(FINISH_LOADING);
    	    	});
    	   
       }
       
       $scope.limparDados = function(){
    	   $scope.agrupadorSelecionado = 'Dire';
           $scope.situacaoSelecionado = "Selecione...";
           delete $scope.dataInicialConsulta;
           delete $scope.dataFinalConsulta;
           delete $scope.dataInicial;
           delete $scope.dataFinal;
           delete $scope.agrupadorConsulta;
           $scope.agenciaSelecionado = $scope.listaUnidadesSr[0];
           $scope.srSelecionado = $scope.listaSrs[0];
           delete $scope.resultadoRelatorio;
           $scope.direSelecionado = $scope.listaDires[0];
       };
       
       
       $scope.carregarFiltros();
       $scope.carregarTodasSReUndiades();
	
	
});