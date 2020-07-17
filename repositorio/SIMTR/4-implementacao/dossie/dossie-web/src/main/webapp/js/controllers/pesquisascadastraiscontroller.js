'use strict';
angular.module('webApp').controller('PesquisascadastraisController', function ($scope, $rootScope, $translate, $location, $http, DossieService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url, GrowlMessage) {
    Log.debug("PesquisascadastraisController()");
    Analytics.trackEvent('controller','PesquisascadastraisController');
    $scope.container = {};   
    $scope.sistemas = ['SINAD' , 'CADIN', 'SERASA','SICCF','SPC','SICOW'];
    $scope.autorizacao = DossieService.getAutorizacao();
    
    $scope.lista = [];
    DossieService.setNegarTalaoCheque(false);
    
    $scope.activeurl = Url.active();
    
    $scope.findSistema = function (name) {
    	return $scope.autorizacao && $scope.autorizacao.resultado_pesquisa && $scope.autorizacao.resultado_pesquisa[name];
    }
    $scope.insertInLista = function (situacao, sistema, ocorrencia, orientacao) {
    	var listaItem = {
				situacao : situacao,
				sistema : sistema,
				ocorrencia : ocorrencia,
				orientacao: orientacao
		}
    	if (situacao == 'exclamation-circle' && sistema == 'SPC' && orientacao == 'Não fornecer talão de cheque') {
    		DossieService.setNegarTalaoCheque(true);
    	}
    	$scope.lista.push(listaItem);
    }
    
    $scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Pesquisas Cadastrais', $http, 'Interrompido', 'principal');
    }
    
    $scope.findInMensagemOrientacao = function (sistema) {
    	if ($scope.autorizacao && $scope.autorizacao.mensagens_orientacao) {
    		var ok = true;
    		for (var i = 0 ; i < $scope.autorizacao.mensagens_orientacao.length; i++) {
    			if ($scope.autorizacao.mensagens_orientacao[i].sistema == sistema) {
    				$scope.insertInLista('exclamation-circle', sistema, $scope.autorizacao.mensagens_orientacao[i].ocorrencia, $scope.autorizacao.mensagens_orientacao[i].mensagem);
    				ok = false;
    			}
    		}
    		if (ok) {
    			$scope.insertInLista('check-circle', sistema, 'Nada Consta', 'Nada Consta');
    		}
    	} else {
    		$scope.insertInLista('check-circle', sistema, 'Nada Consta', 'Nada Consta');
    	}
    }
    
    $scope.carregarPerguntasAleatorias = function(){
    	var path = './json/perguntas_aleatorias.json';
    	$scope.listaA = [];
    	$scope.listaB = [];
    	$scope.listaC = [];
    	$.getJSON(path, function(lista) {
    		lista.forEach(function(item){
    			if(item.grupo === "A" && item.ocorrencia == $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo){
    				$scope.listaA.push(item);
    			}else if(item.grupo === "B" && item.ocorrencia == $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo){
    				$scope.listaB.push(item);
    			}else if(item.ocorrencia == $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo) {
    				$scope.listaC.push(item);
    			}
    			
    		});
    		
    		$scope.primeiraPergunta = $scope.listaA[Math.floor((Math.random() * $scope.listaA.length))];
    		$scope.segundaPergunta = $scope.listaB[Math.floor((Math.random() * $scope.listaB.length))];
    		$scope.terceiraPergunta = $scope.listaC[Math.floor((Math.random() * $scope.listaC.length))];
    		$("#modalPerguntasAleatorias").modal({backdrop: 'static', keyboard: false});

    	}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista de Estados!");
		}); 	
    	
    	
    	
    };
    
    $scope.sinalizaCampoComErro = function (nameCampo){
		validaCampo(nameCampo, false);
		$('.theme-main-input-'+nameCampo).addClass('has-error');
		$('.label-'+nameCampo).css({"color": "#843534"});
    }
    
    $scope.continuarPerguntas = function(){
    	var continuar = true;
    	if($scope.resposta1 === undefined){
    		$scope.sinalizaCampoComErro("optradio");
    		continuar = false;
    	}
    	
    	if($scope.resposta2 === undefined){
    		$scope.sinalizaCampoComErro("optradio2");
    		continuar = false;
    	}
    	
    	if($scope.resposta3 === undefined){
    		$scope.sinalizaCampoComErro("optradio3");
    		continuar = false;
    	}
    	
    	if(continuar){
    		var totalRespostas = new Number($scope.resposta1 === "true" ? $scope.primeiraPergunta.valor : 0);
        	totalRespostas += new Number($scope.resposta2 === "true" ? $scope.segundaPergunta.valor : 0); 
        	totalRespostas += new Number($scope.resposta3 === "true" ? $scope.terceiraPergunta.valor : 0);
        	
        	if(totalRespostas >= 75){
        		$scope.autorizacao.prosseguir = false;
        		$scope.autorizacao.autorizado = false;
        		GrowlMessage.addErrorMessage('O processo não pode ser continuado.');
        	}
        	$("#modalPerguntasAleatorias").modal('hide');
    	}
    	
    }
    
    $scope.loadData = function () {
    	if ($scope.autorizacao && $scope.autorizacao.resultado_pesquisa) {
    		for (var i = 0 ; i < $scope.sistemas.length; i++) {
    			var sistema = $scope.findSistema($scope.sistemas[i]);
    			if (sistema == null || sistema === 'undefined') {
    				$scope.insertInLista('circle', $scope.sistemas[i], 'Pesquisa não realizada', '-');
    			} else {
    				if (sistema.retorno.codigo == 'X5') {
    					$scope.insertInLista('times-circle', $scope.sistemas[i], sistema.retorno.mensagem, '');
    				} else {
    					$scope.findInMensagemOrientacao($scope.sistemas[i]);
    				}
    			}
    		}
    		if($scope.autorizacao.resultado_pesquisa.SICOW && $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo && 
    				($scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo == 500 ||
    				 $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo == 501 ||
    				 $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo == 502 || 
    				 $scope.autorizacao.resultado_pesquisa.SICOW.retorno.codigo == 507 )){
    			$scope.carregarPerguntasAleatorias();	
    		}
    		
    	}else{
    		$scope.mensagemErro = "";
    		for(var a = 0; a < $scope.autorizacao.mensagens_orientacao.length ; a++ ){
    			var erroStatus = 'Cancelado';
    			if($scope.autorizacao.mensagens_orientacao[a].mensagem.includes('Suspenso')){
    				erroStatus = 'Suspenso';
    			}else if($scope.autorizacao.mensagens_orientacao[a].mensagem.includes('Nulo')){
    				erroStatus = 'Nulo';
    			}
    			alert($scope.autorizacao.mensagens_orientacao[a].mensagem);
    			$scope.mensagemErro = "Status do CPF não permite continuidade do processo - " + erroStatus + "\n";
    		}
    		
    	}
    };
    
    $scope.loadData();
//    $scope.carregarPerguntasAleatorias();
           
  $scope.proceed = function () {
		DossieService.putBackUrl('pesquisasCadastrais');
		$rootScope.go('digitalizacaodeDocumentos');	
    }
    
    $rootScope.currentview.id = 'pesquisasCadastrais';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Pesquisas Cadastrais';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    $rootScope.$broadcast(FINISH_LOADING);
    $rootScope.initScroll();
});