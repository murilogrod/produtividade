'use strict';
angular.module('webApp').controller('CartaoassinaturaController', function ($scope, $rootScope, $translate, $http, DossieService, SicpfService, KeycloakService, Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url) {
    Log.debug("CartaoassinaturaController()");
    Analytics.trackEvent('controller','CartaoassinaturaController');
    $scope.container = {};     
    $rootScope.currentview.id = 'cartaoAssinatura';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Cartão Assinatura';
    $rootScope.currentview.icon = 'fa-list-alt';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    $scope.container.isUsuarioCadastradoSSO = false;
    $scope.container.imgAssinaturaDigitalizada = '';
    $scope.container.senha = "";
    $scope.container.email = "";
    $scope.container.isValidEmail;
    $scope.container.isDisableCampoSenha = true;
    $scope.container.redigitacaoSenha = "";
	$scope.container.isSenhaConfere = false;
	$scope.activeurl = Url.active();
	$scope.container.isSucessoConsultaSSO = false;
    
    DossieService.putBackUrl('digitalizacaodeDocumentos');
    
    
    $scope.loadData = function (isCadastrado) {
    	$scope.container.isSucessoConsultaSSO = true;
    	$scope.container.isUsuarioCadastradoSSO = isCadastrado;
    	
    	if(isCadastrado == false){
    	     $scope.container.isDisableCampoSenha = false;
    	}
    	
    	$scope.atualizaImagemCartaoAssinatura(DossieService.getAutorizacao());
    	$scope.finalizaCarregamentoPagina(); 	
    	
    	$rootScope.$broadcast(FINISH_LOADING); 
    };
    
    $scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Cartão Assinatura', $http, 'Interrompido', 'principal');
    }
    
    $scope.atualizaImagemCartaoAssinatura = function (autorizacao){
			if (!autorizacao || !autorizacao.documentos_utilizados) return;
    		for (var i = 0 ; i < autorizacao.documentos_utilizados.length; i++) {
    		
	    		if (autorizacao.documentos_utilizados[i].tipo == 'CARTAO_ASSINATURA'){ 
	    			if(autorizacao.documentos_utilizados[i].link){
	    				$scope.container.imgAssinaturaDigitalizada = autorizacao.documentos_utilizados[i].link;
	    				
	    			}
	    			
	    		}
	    	}
    	
    };
	
	
	$scope.atualizaImagemCartaoAssinaturaBase64 = function (base64){		
		$scope.container.imgAssinaturaDigitalizada = `data:image/jpeg;base64,${base64}`;
	};

    
    $scope.obterCartaoAssinatura = function() {
    	$rootScope.$broadcast(START_LOADING);
    	$http.get(URL_SIMTR  + SERVICO_GET_CARTAO_ASSINATURA + SicpfService.getCpf(), {responseType: 'arraybuffer'}).then(function (res) {
    		var buffer = new Uint8Array(res.data);
			var blob = new Blob([buffer], {type : 'application/pdf'});
			$scope.url = (window.URL || window.webkitURL).createObjectURL(blob);
			
			$rootScope.verificaUsuarioCadastradoSSO($http, SicpfService.getCpf(), $scope.loadData);			
			
    	}, function (error) {
    		$rootScope.putError(error);
    	});
    };
    
    
    
    
    $('#fileIdentificacao').bind('change', function () {
    	$scope.mimetype =$(this)[0].files[0].type;
    	const file = $(this)[0].files[0];
    	file.onresult = function (result) {
			$scope.file = result;
    		$scope.$apply();
    	};
    	DossieService.toBase64(file);
    });
    
    $scope.print = function () {
    	window.open($scope.url);
    }
    
    $scope.digitalizar = function (){
    	$rootScope.$broadcast(START_LOADING);
    	$rootScope.error = '';
    	$rootScope.showError = false;   
    	DossieService.setClasse(CARTAO_ASSINATURA);
		$rootScope.digitalizarDoc(DossieService.getAutorizacao().cpf_cliente , CARTAO_ASSINATURA, $http, $scope.aposDigitalizacao);
    };
    
       
    
    $scope.aposDigitalizacao = function (cpf_cliente, imagensScanner){
    	$rootScope.$broadcast(START_LOADING);
    	if(imagensScanner != null && imagensScanner.length > 0){
    		
    		$scope.salvarCartaoAssinatura(imagensScanner[0].base64, imagensScanner[0].name);   		
    		
    	}
    	
    };
    
    
	  $scope.salvarCartaoAssinatura = function (imagemDigitalizada, nomeArquivo) {
	    	var obj = {cpf_cliente : DossieService.getAutorizacao().cpf_cliente,
	    			formato:  decideFormat(nomeArquivo),
	    			imagem : imagemDigitalizada
	    	}
	    	
	    	
	    	$http.post(URL_SIMTR + SERVICO_POST_CARTAO_ASSINATURA , obj).then(function (res) {				
				$rootScope.$broadcast(FINISH_LOADING);
				if (res.status == 204) {
					$scope.atualizaImagemCartaoAssinaturaBase64(obj.imagem);					
					DossieService.setExtracao({});
					DossieService.getExtracao().link = `data:image/jpeg;base64,${obj.imagem}`;
					atualizaAutorizacaoComExtracao(DossieService, CARTAO_ASSINATURA);
					Alert.showMessage('Sucesso', 'Cartão Assinatura submetido com sucesso.');
				} else {
					$rootScope.putErrorString("Erro ao salvar Cartão Assinatura! Problema na comunicação com o servidor!");
				}	
	    	}, function (error) {
	    		$rootScope.$broadcast(FINISH_LOADING);
	    		$rootScope.putError(error);
	    	});
	    };
    
    
    $scope.exibirDoc = function (){
    	if($scope.container.imgAssinaturaDigitalizada == '' ){
    		Alert.showMessage('Atenção', 'Cartão Assinatura não digitalizado!');
    	}
    };
    
    
    $scope.checkRedigitacaoSenha = function (){
    	if($scope.container.senha != '' && $scope.container.senha == $scope.container.redigitacaoSenha){
    		$scope.container.isSenhaConfere = true;
    	}else{
    		$scope.container.isSenhaConfere = false;
    	}
    };
    
    $scope.isAtivarRedigitacaoSenha = function(){
    	if($scope.container.senha != ""){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    $scope.limpaRedigitacao = function(){
    	$rootScope.error = '';
    	$rootScope.showError = false;    	
    	$scope.container.redigitacaoSenha = "";
    	$scope.checkRedigitacaoSenha();
    }
    
    $scope.isContinuar = function(){    	
    	if($scope.container.imgAssinaturaDigitalizada != '' && 
    	   ($scope.container.isUsuarioCadastradoSSO || $scope.container.isSenhaConfere) &&
    	   ($scope.container.isUsuarioCadastradoSSO || $scope.container.isValidEmail)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
   $scope.validarEmail = function (){
	   $scope.container.isValidEmail  = false;
	   
	   if($scope.container.email != undefined && 
		  $scope.container.email != null &&
		  $scope.container.email != ''){
		   
		   var regex =  /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		   
		   if(!regex.exec($scope.container.email)){
			   $scope.container.isValidEmail = false;
		   }else{
			   $scope.container.isValidEmail = true;
		   }
	   }
	   
	  if($scope.container.isValidEmail  == false){
		  $rootScope.putErrorString("E-mail Inválido!");		  
				$('#divEmail').addClass('has-error');
				$('#divEmail').css({"color": "#843534"});
	  } else {
				$('#divEmail').removeClass('has-error');
				$('#divEmail').css({"color": "black"});
			
	  } 	   
	   
	   
   }; 
    
    
   $scope.continuar = function(){
	   if(!$scope.container.isUsuarioCadastradoSSO){
		 var regex = /[0-9]{6}/;
		 if(!regex.exec($scope.container.senha)){
			 $rootScope.putErrorString("A senha deve ter no minímo 6 números");
			 return;
		 }
		 
		 if($scope.container.imgAssinaturaDigitalizada == ''){
			 $rootScope.putErrorString("Cartão Assinatura não digitalizado!");
			 return; 
		 }
		 
	   }
	   
	   if($scope.container.isUsuarioCadastradoSSO){
		   $scope.sucessoUserSSOCadastrado();
	   }else{	   
		   $rootScope.cadastraSenhaSSO ($http, SicpfService, $scope.container.senha, $scope.container.email,  $scope.sucessoUserSSOCadastrado);
	   }
		   
	 };
	   
    
	    
	$scope.sucessoUserSSOCadastrado = function(){
		$rootScope.$broadcast(FINISH_LOADING);
		$rootScope.go('digitalizacaodeDocumentos');	
	};    

	
	$scope.finalizaCarregamentoPagina = function (){
		$rootScope.hideAlert();		
		$rootScope.initScroll();		
	}
	
	$scope.obterCartaoAssinatura();
	
});