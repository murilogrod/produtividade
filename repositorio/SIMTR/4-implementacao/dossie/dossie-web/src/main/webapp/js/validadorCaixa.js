'use strict';

//Criação dos REGEX para validação na formatação dos campos
var REGEX_CPF = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
var REGEX_CNPJ = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";
var REGEX_NIS_PIS_PASEP = "^\\d{3}\\.\\d{5}\\.\\d{2}\\-\\d{1}$";
var REGEX_CEP = "^\\d{5}\\-\\d{3}$";
var REGEX_AGENCIA = "^\\d{4}$";
var REGEX_MATRICULA = "^\([A-z]{1})\\d{6}$";
var REGEX_EMAIL = "^([a-z0-9_\.-]+)@([\da-z\.-]+)\\.([a-z\.]{2,6})$";
var REGEX_TELEFONE = "^\\([1-9]{2}\\)[2-9][0-9]{3,4}\\-[0-9]{4}$";
var REGEX_MOEDA_BRASIL = "^R\$(\d{1,3}(\.\d{3})*|\d+)(\,\d{2})?$";
var REGEX_MOEDA_EUA = "/^((?:\d{1,3}\,?)+)(\.\d{1,2})/";
var REGEX_DATA_BRASIL = /(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d/;


//Dispara função para verificar a data
function disparaData(strDATA, showError, formValid) {
	if(!showError || !formValid) {
		$('.'+strDATA.key).addClass('has-error');
		$('.label-'+strDATA.key).css({"color": "#843534"});
	} else {
		$('.'+strDATA.key).removeClass('has-error');
		$('.label-'+strDATA.key).css({"color": "black"});
	}
}

//Função para marcar de vermelho campos inválidos
function validaCampo(strDATA, valid) {
	if(!valid) {
		$('.'+strDATA).addClass('has-error');
		$('.label-'+strDATA).css({"color": "#843534"});
	} else {
		$('.'+strDATA).removeClass('has-error');
		$('.label-'+strDATA).css({"color": "black"});
	}
}

//Desabilita botão continuar considerando campo required inválido
function validaBotaoContinuar(buttonId, valid) {
	if(!valid) {
		$('#' + buttonId).prop('disabled', true);
	} else {
		$('#' + buttonId).prop('disabled', false);
	}
}

//function mascara de data dd/mm/yyyy
function mascaraData(val) {
	  var pass = val.value;
	  var expr = /[0123456789]/;

	  if(pass !== undefined && pass !== null && pass.length !== undefined && pass.length !== null) {
		  for (var i = 0; i < pass.length; i++) {
			  // charAt -> retorna o caractere posicionado no índice especificado
			  var lchar = val.value.charAt(i);
			  var nchar = val.value.charAt(i + 1);
			  
			  if (i == 0) {
				  // search -> retorna um valor inteiro, indicando a posição do inicio da primeira
				  // ocorrência de expReg dentro de instStr. Se nenhuma ocorrencia for encontrada o método retornara -1
				  // instStr.search(expReg);
				  if ((lchar.search(expr) != 0) || (lchar > 3)) {
					  val.value = "";
				  }
				  
			  } else if (i == 1) {
				  
				  if (lchar.search(expr) != 0) {
					  // substring(indice1,indice2)
					  // indice1, indice2 -> será usado para delimitar a string
					  var tst1 = val.value.substring(0, (i));
					  val.value = tst1;
					  continue;
				  }
				  
				  if ((nchar != '/') && (nchar != '')) {
					  var tst1 = val.value.substring(0, (i) + 1);
					  
					  if (nchar.search(expr) != 0)
						  var tst2 = val.value.substring(i + 2, pass.length);
					  else
						  var tst2 = val.value.substring(i + 1, pass.length);
					  
					  val.value = tst1 + '/' + tst2;
				  }
				  
			  } else if (i == 4) {
				  
				  if (lchar.search(expr) != 0) {
					  var tst1 = val.value.substring(0, (i));
					  val.value = tst1;
					  continue;
				  }
				  
				  if ((nchar != '/') && (nchar != '')) {
					  var tst1 = val.value.substring(0, (i) + 1);
					  
					  if (nchar.search(expr) != 0)
						  var tst2 = val.value.substring(i + 2, pass.length);
					  else
						  var tst2 = val.value.substring(i + 1, pass.length);
					  
					  val.value = tst1 + '/' + tst2;
				  }
			  }
			  
			  if (i >= 6) {
				  if (lchar.search(expr) != 0) {
					  var tst1 = val.value.substring(0, (i));
					  val.value = tst1;
				  }
			  }
		  }
	  }
	  

	  if (pass !== undefined && pass !== null && pass.length !== undefined && pass.length !== null && pass.length > 10)
	    val.value = val.value.substring(0, 10);
	  return true;
	}

//Dispara função para verificar o CPF
function disparaCPF(strCPF , button){

	//Chama função de verificar CPF
	var showError = validaCPF(strCPF);
	if(!showError) {
		$('#cpf-help').removeClass('hide');
		//$('.theme-main-input-number-cpf').addClass('has-error').removeClass('has-success');
		$('#' + button).prop('disabled', true);
		$('#' + button).css({"opacity": "0.5"});
	}
	if(showError) {
		$('#cpf-help').addClass('hide');
		//$('.theme-main-input-number-cpf').addClass('has-success').removeClass('has-error');
		$('#' + button).prop('disabled', false);
		$('#' + button).css({"opacity": "1"});
	}
	
	if (button && document.getElementById(button)) {
		document.getElementById(button).disabled = !showError;
	} 

}

//Dispara função para verificar a agência
function disparaAgencia(strAgencia){

	//Chama função de verificar agência
	var showError = validaAgencia(strAgencia);
	if(!showError) {
		$('#agencia-help').removeClass('hide');
		$('#agencia-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#agencia-help').addClass('hide');
		$('#agencia-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar a matrícula
function disparaMatricula(strMatricula){

	//Chama função de verificar matrícula
	var showError = validaCPF(strMatricula);
	if(!showError) {
		$('#matricula-help').removeClass('hide');
		$('#matricula-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#matricula-help').addClass('hide');
		$('#matricula-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar a operação
function disparaOperacao(strOperacao){

	//Chama função de verificar operação
	var showError = validaCPF(strOperacao);
	if(!showError) {
		$('#operacao-help').removeClass('hide');
		$('#operacao-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#operacao-help').addClass('hide');
		$('#operacao-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar o CNPJ
function disparaCNPJ(strCNPJ){

	//Chama função de verificar CNPJ
	var showError = validaCNPJ(strCNPJ);
	if(!showError) {
		$('#cnpj-help').removeClass('hide');
		$('#cnpj-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#cnpj-help').addClass('hide');
		$('#cnpj-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar o PIS
function disparaPIS(strNIS){

	//Chama função de verificar NIS
	var showError = validaNIS_PIS_PASEP(strNIS);
	if(!showError) {
		$('#pis-help').removeClass('hide');
		$('#pis-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#pis-help').addClass('hide');
		$('#pis-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar o NIS
function disparaNIS(strNIS){

	//Chama função de verificar NIS
	var showError = validaNIS_PIS_PASEP(strNIS);
	if(!showError) {
		$('#nis-help').removeClass('hide');
		$('#nis-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#nis-help').addClass('hide');
		$('#nis-group').addClass('has-success').removeClass('has-error');
	}

}

//Dispara função para verificar o PASEP
function disparaPASEP(strNIS){

	//Chama função de verificar NIS
	var showError = validaNIS_PIS_PASEP(strNIS);
	if(!showError) {
		$('#pasep-help').removeClass('hide');
		$('#pasep-group').addClass('has-error').removeClass('has-success');
	}
	if(showError) {
		$('#pasep-help').addClass('hide');
		$('#pasep-group').addClass('has-success').removeClass('has-error');
	}

}

//Função resposavel por validar o campo cep padrão 99999-999
function verificarCEP(strCEP) {
	var retorno = false;
	if(strCEP != undefined && strCEP != null && strCEP.match(REGEX_CEP)) {
		retorno = true;
	}
	return retorno;
}


//Função resposavel por validar o campo data padrão dd/mm/yyyy
function validaDATA(strDATA) {
	var retorno = false;
	if(strDATA != undefined && strDATA != null && strDATA.match(REGEX_DATA_BRASIL)) {
		retorno = true;
	}
	return retorno;
}


//Metodo resposavel por validar o campo CPF
function validaCPF(strCPF){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_CPF);
	if(!padrao.test(strCPF.value)){
		strCPF.value='';
		strCPF.focus();
		return false;
	}

	//Retira do valor apenas os númericos para efetuar o calculo
	var cpf = strCPF.value.replace(/[^\d]+/g,'');

	//Verifica se o campo está vazio
	if(cpf === '') {
		strCPF.value='';
		strCPF.focus();
		return false;
	}

	// Verifica numeros repetidos no cpf, para evitar que processe cpf já invalido
	if (cpf.length !== 11 ||
		cpf === "00000000000" ||
		cpf === "11111111111" ||
		cpf === "22222222222" ||
		cpf === "33333333333" ||
		cpf === "44444444444" ||
		cpf === "55555555555" ||
		cpf === "66666666666" ||
		cpf === "77777777777" ||
		cpf === "88888888888" ||
		cpf === "99999999999"){
		strCPF.value='';
		strCPF.focus();
		return false;
	}

	// Valida 1o digito verificador
	var add = 0;
	for (var i=0; i < 9; i ++){
		add += parseInt(cpf.charAt(i)) * (10 - i);
	}
	var rev = 11 - (add % 11);
	if (rev === 10 || rev === 11)
		rev = 0;
	if (rev !== parseInt(cpf.charAt(9))){
		strCPF.value='';
		strCPF.focus();
		return false;
	}

	// Valida 2o digito verificador
	add = 0;
	for (i = 0; i < 10; i ++)
		add += parseInt(cpf.charAt(i)) * (11 - i);
	rev = 11 - (add % 11);
	if (rev === 10 || rev === 11)
		rev = 0;
	if (rev !== parseInt(cpf.charAt(10))) {
		strCPF.value='';
		strCPF.focus();
		return false;
	}

	return true;
}


//Funcao responsavel por validar o campo CNPJ
function validaCNPJ(strCNPJ){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_CNPJ);
	if(!padrao.test(strCNPJ.value)){
		strCNPJ.value='';
		strCNPJ.focus();
		return false;
	}

	//Retira do valor apenas os dados numericos
	var cnpj = strCNPJ.value.replace(/[^\d]+/g,'');
 
    if(cnpj == '') return false;
     
    if (cnpj.length != 14)
        return false;
 
    // Elimina CNPJs invalidos conhecidos
    if (cnpj == "00000000000000" || 
        cnpj == "11111111111111" || 
        cnpj == "22222222222222" || 
        cnpj == "33333333333333" || 
        cnpj == "44444444444444" || 
        cnpj == "55555555555555" || 
        cnpj == "66666666666666" || 
        cnpj == "77777777777777" || 
        cnpj == "88888888888888" || 
        cnpj == "99999999999999")
        return false;
         
    // Valida DVs
    var tamanho = cnpj.length - 2;
    var numeros = cnpj.substring(0,tamanho);
    var digitos = cnpj.substring(tamanho);
    var soma = 0;
    var pos = tamanho - 7;
    for (var i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
            pos = 9;
    }
    var resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0))
        return false;
         
    tamanho = tamanho + 1;
    numeros = cnpj.substring(0,tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
            pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(1))
          return false;
           
    return true;
}


//Função de validação para NIS, PIS, PASEP
function validaNIS_PIS_PASEP(strNisPisPasep){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_NIS_PIS_PASEP);
	if(!padrao.test(strNisPisPasep.value)){
		strNisPisPasep.value='';
		strNisPisPasep.focus();
		return false;
	}

	//Retira pontos e hífen do valor do campo
	var numero_pis = strNisPisPasep.value.replace(/[^\d]+/g,'');

	//Variaveis de controle de validação
	var soma=0;
	var resto=0;

	//Verifica o tamanho do campo e o conteúdo
	if(numero_pis != "" && numero_pis.length == 11){
		var crppis = parseInt(numero_pis.substr(10,1));
		if(isNaN(crppis)){
			strNisPisPasep.value = '';
			strNisPisPasep.focus();
			return false;
		}

		var soma = 0;

		//Executa o cálculo para a validação do campo
		for(var cont=0;cont<numero_pis.length;cont++){
			if(cont==0)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 3);
			if(cont==1)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 2);
			if(cont==2)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 9);
			if(cont==3)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 8);
			if(cont==4)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 7);
			if(cont==5)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 6);
			if(cont==6)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 5);
			if(cont==7)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 4);
			if(cont==8)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 3);
			if(cont==9)soma = soma + (parseInt(numero_pis.substr(cont,1)) * 2);
		}

		resto=11 - (soma % 11);

		if((resto == 10) || (resto == 11))

			resto = 0;

		if(resto == crppis){

			return true;

		}else{
			strNisPisPasep.value = '';
			strNisPisPasep.focus();
			return false;
		}

	}else{
		strNisPisPasep.value = '';
		strNisPisPasep.focus();
		return false;
	}
}


function validaCEP(strCEP){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_CEP);
	if(!padrao.test(strCEP.value)){
		$('#cep-help').removeClass('hide');
		$('#cep-group').addClass('has-error').removeClass('has-success');
		strCEP.value='';
		strCEP.focus();
		return false;
	} else {
		$('#cep-help').addClass('hide');
		$('#cep-group').addClass('has-success').removeClass('has-error');
	}

}

function validaAgencia(strAgencia){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_AGENCIA);
	if(!(strAgencia.value.length == 4 && padrao.test(strAgencia.value))){
		strAgencia.value='';
		strAgencia.focus();
		return false;
	} else {
		return true;
	}

}

function validaConta(strConta){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_CONTA);
	if(!strConta.length <= 9){
		strConta.value='';
		strConta.focus();
		return false;
	}

}

function validaOperacao(strOperacao){

	var isCorreto = false;
	var operacoesValidasCaixa = [
		"001",
		"002",
		"003",
		"006",
		"008",
		"013",
		"022",
		"023",
		"028"
	]


	for (i = 0; i < operacoesValidasCaixa.length; i++) {
		if(operacoesValidasCaixa[i] == strOperacao.value){
			isCorreto = true;
			break;
		}
	}

	if(!isCorreto){
		$('#operacao-help').removeClass('hide');
		$('#operacao-group').addClass('has-error').removeClass('has-success');
		strOperacao.value='';
		strOperacao.focus();
	} else {
		$('#operacao-help').addClass('hide');
		$('#operacao-group').addClass('has-success').removeClass('has-error');
	}

}

function validaMatricula(strMatricula){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_MATRICULA);
	if(!padrao.test(strMatricula.value)){
		$('#matricula-help').removeClass('hide');
		$('#matricula-group').addClass('has-error').removeClass('has-success');
		strMatricula.value='';
		strMatricula.focus();
		return false;
	} else {
		$('#matricula-help').addClass('hide');
		$('#matricula-group').addClass('has-success').removeClass('has-error');
	}

}

function validaEmail(strEmail){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_EMAIL);
	if(!padrao.test(strEmail.value)){
		$('#email-help').removeClass('hide');
		$('#email-group').addClass('has-error').removeClass('has-success');
		strEmail.value='';
		strEmail.focus();
		return false;
	} else {
		$('#email-help').addClass('hide');
		$('#email-group').addClass('has-success').removeClass('has-error');
	}

}

function validaTelefone(strTelefone){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_TELEFONE);
	if(!padrao.test(strTelefone.value)){
		$('#telefone-help').removeClass('hide');
		$('#telefone-group').addClass('has-error').removeClass('has-success');
		strTelefone.value='';
		strTelefone.focus();
		return false;
	} else {
		$('#telefone-help').addClass('hide');
		$('#telefone-group').addClass('has-success').removeClass('has-error');
	}

}

function validaMoedaBrasil(strMoeda){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_MOEDA_BRASIL);
	if(!padrao.test(strMoeda.value)){
		$('#moeda-brasil-help').removeClass('hide');
		$('#moeda-brasil-group').addClass('has-error').removeClass('has-success');
		strMoeda.value='';
		strMoeda.focus();
		return false;
	} else {
		$('#moeda-brasil-help').addClass('hide');
		$('#moeda-brasil-group').addClass('has-success').removeClass('has-error');
	}

}

function validaMoedaEua(strMoeda){

	//Verifica se o campo foi formatado corretamente
	var padrao = new RegExp(REGEX_MOEDA_EUA);
	if(!padrao.test(strMoeda.value)){
		$('#moeda-eua-help').removeClass('hide');
		$('#moeda-eua-group').addClass('has-error').removeClass('has-success');
		strMoeda.value='';
		strMoeda.focus();
		return false;
	} else {
		$('#moeda-eua-help').addClass('hide');
		$('#moeda-eua-group').addClass('has-success').removeClass('has-error');
	}

}

function mascaraMoeda(strMoeda){
	$('#'+strMoeda.id+'').maskMoney();
}