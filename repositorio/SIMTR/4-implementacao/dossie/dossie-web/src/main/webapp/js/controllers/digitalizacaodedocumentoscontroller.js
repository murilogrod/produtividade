'use strict';
angular.module('webApp').controller('DigitalizacaodedocumentosController', function ($scope, $rootScope, $translate, $location,  $http, DossieService,SicpfService,Log, WebServiceX, Alert, Analytics, Error, Utils, WebServiceRest, Url) {
    Log.debug("DigitalizacaodedocumentosController()");
    Analytics.trackEvent('controller','DigitalizacaodedocumentosController');
    $scope.container = {};     
    $rootScope.currentview.id = 'digitalizacaodeDocumentos';
    $rootScope.currentview.group = 'AberturadeConta';
    $rootScope.currentview.title = 'Digitalização de Documentos';
    $rootScope.currentview.icon = 'none';
    $rootScope.currentview.locked = false;
	$rootScope.currentview.menu = true;
    $rootScope.currentview.description = '';
    
    $scope.autorizacao = DossieService.getAutorizacao();
    $scope.listaCombo = [];
    $scope.container.documento = '';
    $scope.file = null;
    $scope.isContinuar = false;   
    $scope.container.isDeclararResidencia = $rootScope.documentos.declararEndereco;
    
    $rootScope.listaProfissoes = [];
    
    $scope.activeurl = Url.active();
    
    
    $scope.loadData = function () {
	    	$scope.container.rg_status = false;
	    	$scope.container.cnh_status = false;
	    	$scope.container.identificacao_status = false;
	    	$scope.container.residencia_status = false;
	    	$scope.container.renda_status = false;
	    	$scope.container.assinatura_status = false;
	    	$scope.container.dadosDeclarados_status = false;
	    	$scope.container.identificacaoSelected = 'CNH';
    	if ($scope.autorizacao.documentos_utilizados) {
    		
        	for (var i = 0 ; i < $scope.autorizacao.documentos_utilizados.length; i++) {
        		if ($scope.autorizacao.documentos_utilizados[i].tipo === 'CARTEIRA_IDENTIDADE'){
        			$scope.container.identificacao_img = $scope.autorizacao.documentos_utilizados[i].link + CONVERT_TIFF;
        			$scope.container.identificacaoSelected = 'RG';
        			$scope.container.rg_status = true;
        		}
        		
        		if($scope.autorizacao.documentos_utilizados[i].tipo === 'CNH'){
        			$scope.container.identificacao_img = $scope.autorizacao.documentos_utilizados[i].link + CONVERT_TIFF;
        			$scope.container.identificacaoSelected = 'CNH';
        			$scope.container.cnh_status = true;
        		}
        		
        		if ($scope.autorizacao.documentos_utilizados[i].tipo === 'DOCUMENTO_CONCESSIONARIA'){
        			$scope.container.residencia_img = $scope.autorizacao.documentos_utilizados[i].link + CONVERT_TIFF;
        			$scope.container.residencia_status = true;
        		}
        		if ($scope.autorizacao.documentos_utilizados[i].tipo === 'DEMONSTRATIVO_PAGAMENTO'){
        			$scope.container.renda_img = $scope.autorizacao.documentos_utilizados[i].link + CONVERT_TIFF;
        			$scope.container.renda_status = true;
        		}
        		if ($scope.autorizacao.documentos_utilizados[i].tipo === 'CARTAO_ASSINATURA'){        			
       				$scope.container.assinatura_img = $scope.autorizacao.documentos_utilizados[i].link;
        			$scope.container.assinatura_status = true;
        		}
        		if ($scope.autorizacao.documentos_utilizados[i].tipo === 'DADOS_DECLARADOS'){
        			$scope.container.dadosDeclarados_img = $scope.autorizacao.documentos_utilizados[i].link;
        			$scope.container.dadosDeclarados_status = true;
        		}
        		
        		if($scope.container.rg_status || $scope.container.cnh_status){
        			$scope.container.identificacao_status = true;
        		}
        		
        	}
        }
    }
    
    $scope.loadData();
  
    
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
    	window.open($scope.container.dadosDeclarados_img);
    }
    
    $scope.cancelarAtend = function(){
    	$rootScope.auditarAtendimento('Digitalização de Documentos', $http, 'Interrompido', 'principal');
    }
    
	$scope.digitalizarDocPessoais = function (tipoDocumento){
		$rootScope.$broadcast(START_LOADING);
		
		if(tipoDocumento  == 'IDENTIFICACAO'){
			if($scope.container.identificacaoSelected == 'CNH'){
				tipoDocumento = CLASS_CNH;
			}else if($scope.container.identificacaoSelected == 'RG'){
				tipoDocumento = CLASS_DOC_IDENT;
			}
		}	
		DossieService.setClasse(tipoDocumento);
		$rootScope.digitalizarDoc($scope.autorizacao.cpf_cliente , tipoDocumento, $http, $rootScope.extrairDados, $scope.direcionarAposExtracao);
	};	
		
		
		
	$scope.direcionarAposExtracao = function(){
			DossieService.putBackUrl('digitalizacaodeDocumentos');
			var tipoDocumento = DossieService.getClasse();
			if (tipoDocumento == CLASS_DOC_IDENT || tipoDocumento == CLASS_CNH) {
				$rootScope.go('digitalizacaoDocIdentificacaoFechado');
			}
			if (tipoDocumento == CLASS_CONTA_ENERGIA) {
				$rootScope.go('digitalizacaoCompResidnciaFechado');
			}
			if (tipoDocumento == CLASS_CONTRACHEQUE) {				
				$scope.carregarListProfissoes();
				        			
			}			
		}
	
    $scope.carregarListProfissoes = function(){
    	var path = './json/profissoes.json';
    	$.getJSON(path, function(profissoes) {
    		$rootScope.listaProfissoes = profissoes;
    		$rootScope.go('digitalizacaoCompRendaFechado');    		
		}).error( function (){
			$rootScope.putErrorString("Erro ao Obter Lista Padrão de Profissões!");
		}); 	
    };
	
	
	
    $scope.direcionarTelaAssinatura = function(){	   
    	$rootScope.go('cartaoAssinatura');
	};
	

	$scope.digitalizarDadosDeclarados = function(parametro){
		$rootScope.declaracaoDadosParametro = parametro;
		$rootScope.$broadcast(START_LOADING);
		DossieService.putBackUrl('digitalizacaodeDocumentos');
		$rootScope.go('declaracaodeDados');	
	};
	
	$scope.isAtivarAlteracaoAssinatura = function(){
		if( $scope.container.assinatura_status && 
			$scope.container.identificacao_status && 
		    $scope.container.renda_status &&
		    ($scope.container.isDeclararResidencia || $scope.container.residencia_status)){
			
			return true;
		}else{
			return false;
		}
	}
	
	$scope.isAtivarAlteracaoDadosDeclarados = function(){
		if( $scope.container.dadosDeclarados_status &&
			$scope.container.assinatura_status && 
			$scope.container.identificacao_status && 
		    $scope.container.renda_status &&
		    ($scope.container.isDeclararResidencia || $scope.container.residencia_status)){
			
			return true;
		}else{
			return false;
		}
	}
	
	$scope.isAtivarDigitalizacaoIdentificacao = function(){
		if(!$scope.container.identificacao_status && 
		    ($scope.container.identificacaoSelected  != undefined)	){
			return true;
		}else{
			return false;
		}
		
	};
	
	$scope.isAtivarAtualizacaoIdentificacao = function(){
		if($scope.container.identificacao_status && 
		   ($scope.container.identificacaoSelected  != undefined)){
			return true;
		}else{
			return false;
		}
	};
	
	$scope.isAtivarDigitalizacaoRenda = function(){
		if(!$scope.container.renda_status && !$scope.container.isSemRenda){
		    return true;	
		}else{
			return false;
		} 
	};
	
	
	$scope.isAtivarAtualizarRenda = function(){
		if($scope.container.renda_status){
			 return true;	
		}else{
			return false;
		}
	};
	
	
	$scope.isAtivarDigitalizacaoResidencia = function(){
		if(!$scope.container.residencia_status && 
		   !$scope.container.isDeclararResidencia){
			return true;
		}else{
			return false;
		}
	};
	
	
	$scope.isAtivarAtualizarResidencia = function(){
		if($scope.container.residencia_status && 
		   !$scope.container.isDeclararResidencia){
			return true;
		}else{
			return false;
		}
	};
		
	$scope.isResidenciaCadastrada = function (){
		if(($scope.container.isDeclararResidencia && $scope.container.dadosDeclarados_status) || 
			$scope.container.residencia_status){
			return true;
		}else {
			return false;
		}
	}
	
	$scope.setDeclararResidencia = function(){
		$rootScope.documentos.declararEndereco = $scope.container.isDeclararResidencia;
	};
	
	$scope.setDeclararSemRenda = function(){
		$rootScope.documentos.declararSemRenda = $scope.container.isSemRenda;
	};
	
	$scope.validarContinuar = function (){
	if($scope.container.identificacao_status && $scope.isResidenciaCadastrada() 
			&& ($scope.container.renda_status || $scope.container.dadosDeclarados_status) 
			&& $scope.container.assinatura_status && $scope.container.dadosDeclarados_status){
		 $scope.isContinuar = true;
	}else{
		$scope.isContinuar = false;
	}
	
	};	
	
	$scope.validarContinuar();
	
	
	$scope.continuar = function(){
		$rootScope.$broadcast(START_LOADING);
        var params = {
            "cpf_cliente": Number(SicpfService.getCpf()),
            "integracao": INTEGRACAO,
            "documentos": $scope.getListaIdParaAtualizacao()
        };
        // Atualiza os documentos que foram conferidos nesta sessão.
        $http.post(URL_SIMTR + SERVICO_CADASTRO_ATUALIZACAO , params).then(function (response) {
            if(response.status == 204) { // Dados e status no GED dos documentos atualizados com sucesso.                 
                
            	$rootScope.auditarAtendimento('Digitalização de Documentos', $http, 'Criado', 'dossiDigital');
                
            } else { // Houve erro na atualização dos documentos.
            	$rootScope.putErrorString("Cadastramento/atualização não realizado. Falha de comunicação com o SICLI.");
            }
        }, function (error) {
        	var msg = "Cadastramento/atualização não realizado. Falha de Comunicação.  ";
				if(error.data != null) {
					msg += error.data.observacao ? error.data.observacao : error.data.mensagem  ;
					msg += error.data.detalhe ? error.data.detalhe : '';
				}
					$rootScope.putErrorString(msg);
					
        });
	}
	
	
	$scope.getListaIdParaAtualizacao = function(){
		var listaIds = [];
		for (var i = 0 ; i < $scope.autorizacao.documentos_utilizados.length; i++) {
			var doc = $scope.autorizacao.documentos_utilizados[i];
			
			if (doc.tipo == 'DADOS_DECLARADOS' || 
				doc.tipo == 'CARTEIRA_IDENTIDADE' || 
				doc.tipo == 'CNH' || 
				doc.tipo == 'DEMONSTRATIVO_PAGAMENTO' ||
				( doc.tipo == 'DOCUMENTO_CONCESSIONARIA' &&  !$scope.container.isDeclararResidencia)
				){
			
				listaIds.push(doc.identificador);
			}
		}
		
		return listaIds;
	}
	
	
	$scope.exibirDocumento = function (tipoDocumento){
		
		$scope.container.tipoDocumento = tipoDocumento;
		
		if(tipoDocumento == 'IDENTIFICACAO'){
			$scope.container.linkExibirIdentificacao = $scope.container.identificacao_img;
			$scope.container.tituloExibirDoc = 'Documento de Identificação';
			$("#modalDoc").modal();
		}
		if(tipoDocumento == 'DOCUMENTO DE CONCESSIONARIA'){
			$scope.container.linkExibirResidencia = $scope.container.residencia_img;
			$scope.container.tituloExibirDoc = 'Comprovante de Residência';
			$("#modalDoc").modal();
		}
		if(tipoDocumento == 'DEMONSTRATIVO PAGAMENTO'){
			$scope.container.linkExibirRenda = $scope.container.renda_img;
			$scope.container.tituloExibirDoc = 'Comprovante de Renda';
			$("#modalDoc").modal();
			
		}
		if(tipoDocumento == 'CARTAO DE ASSINATURA'){
			$scope.container.linkExibirAssinatura = $scope.container.assinatura_img;
			$scope.container.tituloExibirDoc = 'Cartão de Assinatura';
			$("#modalDoc").modal();
		}
		if(tipoDocumento == 'DADOS DECLARADOS'){
			$scope.container.linkExibirDeclarados = $scope.container.dadosDeclarados_img;
			$scope.container.tituloExibirDoc = 'Dados Declarados';
			$("#modalDoc").modal();
		}
	};
  
    $scope.container.isSemRenda = (!$scope.container.renda_status && !$scope.container.dadosDeclarados_status && !$scope.isAtivarDigitalizacaoRenda()) ? true : $rootScope.documentos.declararSemRenda;
    $rootScope.hideAlert();
    $rootScope.initScroll();
});