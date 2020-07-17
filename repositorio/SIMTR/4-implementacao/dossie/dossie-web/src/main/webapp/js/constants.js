var URL_SIMTR = '';
const SERVICO_SICLI = '/negocio/v1/dossiecliente';
const SERVICO_AUTORIZACAO = '/dossie-digital/v1/autorizacao';
const SERVICO_CONSULTA_USUARIO = '/keycloak/v1/consultarusuario?user=';
const SERVICO_VALIDAR_SENHA = '/keycloak/v1/validarsenha';
const SERVICO_CADASTRAR_USER_SSO = '/keycloak/v1/incluirusuario';
const SERVICO_SICPF = '/negocio/v1/dossie-cliente/cpf';
const SERVICO_EXTRACAO = '/dossie-digital/v1/documento/extracao-dados';
const SERVICO_ATUALIZA_EXTRACAO = '/dossie-digital/v1/documento/atualizacao-dados';
const SERVICO_GET_CARTAO_ASSINATURA = '/dossie-digital/v1/documento/cartao-assinatura/cpf/';
const SERVICO_GET_RELATORIO_DOSSIE = '/dossie-digital/v1/documento/relatorio-dossie';
const SERVICO_POST_CARTAO_ASSINATURA = '/dossie-digital/v1/documento/cartao-assinatura';
const SERVICO_POST_DADOS_DECLARADOS = '/dossie-digital/v2/documento/dados-declarados';
const SERVICO_POST_ATENDIMENTO = '/dossie-digital/v1/atendimento';
const SERVICO_POST_CONSULTAR_DADOS_DECLARADOS = '/dossie-digital/v1/documento/dados-declarados/cpf/';
const SERVICO_OBTER_ORGANOGRAMA = '/dossie-digital/v1/organograma';
const SERVICO_CONSULTAR_RELATORIO = '/dossie-digital/v1/atendimento';
const SERVICO_CADASTRO_ATUALIZACAO = '/dossie-digital/v1/cadastro/atualizacao';
const SERVICO_MINUTA = '/dossie-digital/v1/documento/minuta';
const SERVICO_DOCUMENTOS_GUARDA= '/dossie-digital/v1/operacao/documentos-guarda';
const SERVICO_LOCALIDADE = '/negocio/v1/dossie-cliente/localidade/cep/';
const SERVICO_GET_TIPOLOGIA = '/negocio/v1/documento/tipologia';
const SERVICO_GET_PARAMETROS_TIPO_DOCUMENTO = '/dossie-digital/v1/cadastro/tipo-documento';
const SERVICO_GET_PARAMETROS_FUNCAO_DOCUMENTAL = '/dossie-digital/v1/cadastro/funcao-documental';
const SERVICO_DELETE_PARAMETRIZACAO_FUNCAO = '/dossie-digital/v1/cadastro/funcao-documental/'
const SERVICO_COMPOSICAO_DOCUMENTAL = '/dossie-digital/v1/cadastro/composicao-documental'
	
	

const SERVICO_SCANNER = 'http://localhost:5128/scan?{0}colorMode=RGB&format=JPG&dpi=300';
const DEVICES_LIST = 'http://localhost:5128/scan?list=devices';
const CONVERT_TIFF = "/CONVERT_TIFF";

const LOADING_ID = 'loadingID';
const START_LOADING = 'startLoading';
const FINISH_LOADING = 'finishLoading';

const CLASS_CNH = 'CNH';
const CLASS_DOC_IDENT = 'DOCUMENTO DE IDENTIDADE';
const CLASS_CONTRACHEQUE = 'DEMONSTRATIVO PAGAMENTO';
const CLASS_CONTA_ENERGIA = 'DOCUMENTO DE CONCESSIONARIA';

const IDENTIFICACAO = 'IDENTIFICAÇÃO';
const ENDERECO = 'ENDEREÇO';
const RENDA = 'RENDA';
const CARTAO_ASSINATURA = 'CARTÃO DE ASSINATURA';
const DADOS_DECLARADOS = 'DADOS DECLARADOS';

const INTEGRACAO = 2189545189;
const OPERACAO_CONTA_PF = 1;
const MODALIDADE_CONTA_PF = 0;
const TOKEN = 'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvbGczaGJWRU0zX2dSdDFEZHBYTXdPWHRTLUUyTVFRRGIwQmh3Nkl2WmRvIn0.eyJqdGkiOiI5MDBmN2QxZC1mNmU1LTQwZjMtOTdmOS0xNTRhODlkYTE4MDkiLCJleHAiOjE1MjgzNzgyNTcsIm5iZiI6MCwiaWF0IjoxNTI4Mzc4MTk3LCJpc3MiOiJodHRwczovL2xvZ2luZGVzLmNhaXhhLmdvdi5ici9hdXRoIiwiYXVkIjoiY2xpLXNlci1tdHIiLCJzdWIiOiI1MTRkZjNhOC05NTc4LTQyY2YtOWE4OS1jY2NmNmY1ZTAzNjUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjbGktc2VyLW10ciIsIm5vbmNlIjoiYzRmZDMyZWItZmVjMS00ZjY2LTg4MjktYjRhZTI5MjU0YTBlIiwiYXV0aF90aW1lIjowLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiIwMTI1NTE5MTE3MSIsInJlc291cmNlX2FjY2VzcyI6e30sInRpcG9jcmVkIjoic2VuaGEiLCJpcCI6IjEwLjIyNC41Ljc5IiwidmFsb3IiOiJhdXRvIn0.NEbBj6TppJsIKW1ycCfSOjUPj-U5sf1rzP2rtzrJDNQ8GzSSkV2sFhyPHF14IF_naJcNg52VL7XrnLOMNT15exBZmlGPgtaR-jcpqyxHEEM9gTQ6rLpCJ39BqI4Wd_ddIy3tjXoAXKcUhcMDzQ87utGk5V9QeOltObmgvVVSMopB0sViGZeYYDy9SV5a9ChD9nQDvV2E98VlyHMIaPXyHaxeboPJiLkEC9q3WoD8IiVoOrfUTVLTmqf-KGHvZKTWULKNbe0KmQ-LxxFcKTux1QTnha_jR2HoKnkYlTa6nZLMk1NBGdvc_MqX09j3AtkRd5jn7X8MpKByVsWZ8RkAHg';

const LABEL_SELECIONE = 'Selecione';
const DEVICE_CAMARA = 'Cam';

const MONTHS = { 0 : 'Janeiro',
	1: 'Fevereiro',
	2: 'Março',
	3: 'Abril',
	4: 'Maio',
	5: 'Junho',
	6: 'Julho',
	7: 'Agosto',
	8: 'Setembro',
	9: 'Outubro',
	10: 'Novembro',
	11: 'Dezembro'}

const FIELD_CPF = { key : 'cpf' , name : 'CPF', required: 'CPF obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do CPF é de', tooltip: false};
const FIELD_NOME = { key : 'nome', name : 'Nome', required: 'Nome é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Nome é de', tooltip: false};
const FIELD_NUMERO_DOCUMENTO = { key : 'numero_documento' , name : 'Número do Documento', required: 'Número do Documento é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número do Documento é de', tooltip: false};
const FIELD_DATA_EMISSAO = { key : 'data_emissao' , name : 'Data de Emissão', required: 'Data de Emissão é obrigatória', data: true, msgdata: 'Data de Emissão inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Emissão é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_DATA_VALIDADE = { key : 'data_validade' , name : 'Data de Validade', required: 'Data de Validade é obrigatória', data: true, msgdata:'Data de Validade inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Validade é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_DATA_NASCIMENTO = { key : 'data_nascimento' , name : 'Data de Nascimento', required: 'Data de Nascimento é obrigatória', data: true, msgdata: 'Data de Nascimento inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Nascimento é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_NUMERO_IDENTIDADE = { key : 'numero_identidade' , name : 'Número de Identidade', required: 'Número de Identidade é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número de Identidade é de', tooltip: true, dica: 'Esse número pode conter letras'};
const FIELD_FILIACAO = { key : 'filiacao' , name : 'Filiação', required: 'Filiação é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Filiação é de', tooltip: true, dica: 'Separe os nomes com um "Enter"'};
const FIELD_DATA_1A_HABILITACAO = { key : 'data_1a_habilitacao' , name : 'Data 1ª Habilitação', required: 'Data 1ª Habilitação é obrigatória', data: true, msgdata: 'Data 1ª Habilitação inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data 1ª Habilitação é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_UF_EMISSAO = { key : 'uf' , name : 'UF Emissão', required: 'UF Emissão é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da UF Emissão é de', tooltip: true, dica: 'Formato da SIGLA é XX'};
const FIELD_MARCA_DOCUMENTO_PRINCIPAL = {key : 'marca_documento_principal' , name : 'Marca Documento Principal', required: '', data: false, alteracaolimite: '', hidden : true, default_value : 'S', tooltip: false };

const FIELD_NUMERO_REGISTRO = { key : 'numero_registro' , name : 'Número do Registro', required: 'Número do Registro obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número do Registro é de', tooltip: true, dica: 'Esse número pode conter letras'};
const FIELD_UF = { key : 'uf' , name : 'UF', required: 'UF é obrigatório', required: 'UF é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da UF é de', tooltip: true, dica: 'Formato da SIGLA é XX'};
const FIELD_NATURALIDADE = { key : 'naturalidade' , name : 'Naturalidade', required: 'Naturalidade é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Naturalidade é de', tooltip: false};
const FIELD_DATA_EXPEDICAO = {key : 'data_emissao' , name : 'Data de Expedição', required: 'Data de Expedição é obrigatória', data: true, msgdata: 'Data de Expedição inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Expedição é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_REFERENCIA = {key : 'referencia' , name : 'Data de Referência', required: 'Data de Referência é obrigatória', data: true, msgdata: 'Data de Referência inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Referência é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_PERIODO_REFERENCIA = {key : 'periodo_referencia' , name : '', required: '', data: true, msgdata: '', alteracaolimite: '', hidden : true, tooltip: false};
const FIELD_DATA_EMISSAO_RENDA = {key : 'data_emissao' , name : 'Data de Emissão', required: 'Data de Emissão é obrigatório', data: true, msgdata: 'Data Emissão inválido', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Referência é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_CODIGO_SIRIC = {key : 'codigo_siric' , name : 'Código SIRIC', required: '', data: false, alteracaolimite: '', hidden : true, default_value : '64522009', tooltip: false };
const FIELD_TIPO_FONTE_PAGADORA = {key : 'tipo_pessoa_fonte_pagadora' , name : 'Tipo Fonte Pagadora', required: '', data: false, alteracaolimite: '', hidden : true, default_value : '2', tooltip: false };
const FIELD_TIPO_RENDA = {key : 'tipo_renda' , name : 'Tipo Renda', required: '', data: false, alteracaolimite: '', hidden : true, default_value : 'F', tooltip: false };


const FIELD_FONTE_PAGADORA = {key : 'fonte_pagadora' , name : 'CPF/CNPJ Fonte Pagadora', required: 'CPF/CNPJ Fonte Pagadora é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do CPF/CNPJ Fonte Pagadora é de', tooltip: false};
const FIELD_DATA_ADMISSAO = {key : 'data_admissao' , name : 'Data de Admissão', required: 'Data de Admissão é obrigatória', data: true, msgdata: 'Data de Admissão inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data de Admissão é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_RENDA_BRUTA = {key : 'renda_bruta' , name : 'Renda Bruta', required: 'Renda Bruta é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Renda Bruta é de', tooltip: false};
const FIELD_RENDA_LIQUIDA = {key : 'renda_liquida' , name : 'Renda Liquida', required: 'Renda Líquida é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Renda Líquida é de', tooltip: false};
const FIELD_CARACTERISTICA = {key : 'caracteristica_renda' , name : 'Caracterstica Renda', required: '', data: false, alteracaolimite: '', hidden : true, default_value : '6', tooltip: false };
const FIELD_CODIGO_OCUPACAO = {key : 'codigo_ocupacao' , name : 'Código Ocupação', required: 'Ocupação é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Ocupação é de', hidden : true, tooltip: false};
const FIELD_DESCRICAO_OCUPACAO = {key : 'descricao_ocupacao' , name : 'Ocupação', required: 'Ocupação é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Ocupação é de', tooltip: false};
const FIELD_EMISSOR = {key : 'emissor' , name : 'Emissor', required: 'Emissor é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Emissor é de', tooltip: true, dica: 'Nome da concessionária em SIGLA Exemplo: CEB, Light, COPASA etc'};
const FIELD_EMISSOR_RG = {key : 'emissor' , name : 'Emissor', required: 'Emissor é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Emissor é de', tooltip: false};
const FIELD_EMISSOR_RENDA = {key : 'emissor' , name : 'Emissor', required: 'Emissor é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Emissor é de', tooltip: false};
const FIELD_IRRF = {key : 'imposto_retido_fonte' , name : 'IRRF', required: 'IRRF é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do IRRF é de', tooltip: false};
const FIELD_ENDERECO = {key: 'endereco', name : 'Endereço', required: 'Endereço é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Endereço é de', hidden : true, tooltip: false};
const FIELD_LOGRADOURO = {key: 'logradouro', name : 'Logradouro', required: 'Logradouro é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Logradouro é de', tooltip: true, dica: 'Exemplo: Rio Branco nº 174, 7º andar, Centro'};

const FIELD_CEP_COMPLETO = {key : 'cep_completo', name : 'CEP', required: 'CEP é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do CEP é de', tooltip: false};
const FIELD_CEP = {key : 'cep', name : 'CEP', required: '', data: false, alteracaolimite: '', hidden : true, tooltip: false};
const FIELD_CEP_COMPLEMENTO = {key : 'cep_complemento', name : 'Cep Complemento', required: '', data: false, alteracaolimite: '', hidden : true, tooltip: false};
const FIELD_INDICADOR_COMPROVACAO = {key : 'indicador_comprovacao', name : 'Indicador Comprovação', required: '', data: false, alteracaolimite: '', hidden : true, default_value : 'S', tooltip: false};
const FIELD_INDICADOR_CORRESPONDENCIA = {key : 'indicador_correspondencia', name : 'Indicador Correspondência', required: '', data: false, alteracaolimite: '', hidden : true, default_value : 'S', tooltip: false};


const FIELD_DATA_REFERENCIA = {key : 'data_referencia' , name : 'Data Referência', required: 'Data Referência é obrigatória', data: true, msgdata: 'Data Referência inválida', alteracaolimite: 'Percentual máximo permitido de alteração da Data Referência é de', tooltip: true, dica: 'Formato da data é DD/MM/AAAA'};
const FIELD_ANO_REFERENCIA = {key : 'ano_referencia', name : 'Ano Referência', required: '', data: false, alteracaolimite: '', hidden : true, tooltip: false};
const FIELD_MES_REFERENCIA = {key : 'mes_referencia', name : 'Mês Referência', required: '', data: false, alteracaolimite: '', hidden : true, tooltip: false};
const FIELD_CODIGO_FINALIDADE = {key : 'codigo_finalidade', name : 'Código Finalidade', required: '', data: false, alteracaolimite: '', hidden : true, default_value : '1', tooltip: false};
const FIELD_TP_ENDERECO = {key : 'tipo_endereco', name : 'Tipo Endereço', required: '', data: false, alteracaolimite: '', hidden : true, default_value : 'G', tooltip: false};

const FIELD_TIPO_LOGRADOURO = {key : 'tipo_logradouro', name : 'Tipo Logradouro', required: 'Tipo Logradouro é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Tipo Logradouro é de', tooltip: true, dica: 'Exemplos: Rua, Av, QD etc. Máximo de 3 posições'};
const FIELD_BAIRRO = {key : 'bairro', name : 'Bairro', required: 'Bairro é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Bairro é de', tooltip: false};
const FIELD_MUNICIPIO = {key : 'municipio', name : 'Município', required: 'Município é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Município é de', tooltip: false};
const FIELD_COMPLEMENTO = {key : 'complemento', name : 'Complemento', required: 'Complemento é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Complemento é de', tooltip: false};
const FIELD_NUMERO = {key : 'numero', name : 'Número', required: 'Número é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número é de', tooltip: false};
const FIELD_ORGAO_EMISSOR = {key : 'orgao_emissor', name : 'Orgão Emissor', required: 'Orgão Emissor é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Orgão Emissor é de', tooltip: false};
const FIELD_CATEGORIA_HABILITACAO = {key : 'categoria', name : 'Categoria da Habilitação', required: 'A Categoria da Habilitação é obrigatória', data: false, alteracaolimite: 'Percentual máximo permitido de alteração da Categoria de Habilitação é de', tooltip: true, dica: 'Campo que indica o tipo de habilitação do condutor. Exemplo: A (Moto), A/B (Carro e Moto), etc'};
const FIELD_NUMERO_FORMULARIO_CNH = {key : 'numero_formulario', name : 'Número do Formulário', required: 'O Número do Formulário da CNH é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número de Formulário da CNH é de', tooltip: true, dica: 'Esse número fica na lateral esquerda da CNH, na vertical, ao lado da foto do condutor'};
const FIELD_NUMERO_RENACH = {key : 'numero_renach', name : 'Número RENACH', required: 'O Número RENACH é obrigatório', data: false, alteracaolimite: 'Percentual máximo permitido de alteração do Número RENACH é de', tooltip: true, dica: 'O RENACH fica ao lado da assinatura do emissor, um código com números e letras, e inicia com a sigla do Estado'};

const FIELDS_CNH = [FIELD_CPF, FIELD_NOME , FIELD_NUMERO_DOCUMENTO , FIELD_DATA_EMISSAO, FIELD_DATA_VALIDADE, FIELD_DATA_NASCIMENTO ,FIELD_NUMERO_IDENTIDADE,
	FIELD_FILIACAO,FIELD_DATA_1A_HABILITACAO, FIELD_UF_EMISSAO, FIELD_ORGAO_EMISSOR, FIELD_CATEGORIA_HABILITACAO, FIELD_NUMERO_FORMULARIO_CNH,
	FIELD_NUMERO_RENACH, FIELD_MARCA_DOCUMENTO_PRINCIPAL];

const FIELDS_RG = [FIELD_NOME,FIELD_NUMERO_REGISTRO,FIELD_DATA_NASCIMENTO, FIELD_FILIACAO, FIELD_NATURALIDADE, FIELD_UF, FIELD_EMISSOR_RG, 
	               FIELD_DATA_EXPEDICAO, FIELD_MARCA_DOCUMENTO_PRINCIPAL];

const FIELDS_COMPROVANTE_RESIDENCIA = [FIELD_NOME, FIELD_DATA_REFERENCIA,  FIELD_DATA_EMISSAO, FIELD_EMISSOR, FIELD_CEP_COMPLETO,  
									   FIELD_TIPO_LOGRADOURO, FIELD_LOGRADOURO, FIELD_NUMERO, FIELD_COMPLEMENTO, FIELD_BAIRRO, FIELD_MUNICIPIO, FIELD_UF, 
									   FIELD_ENDERECO, FIELD_CEP, FIELD_CEP_COMPLEMENTO, FIELD_INDICADOR_COMPROVACAO, FIELD_INDICADOR_CORRESPONDENCIA, 
									   FIELD_ANO_REFERENCIA, FIELD_MES_REFERENCIA, FIELD_CODIGO_FINALIDADE, FIELD_TP_ENDERECO];

const FIELDS_COMPROVANTE_RENDA = [FIELD_NOME, FIELD_REFERENCIA, FIELD_DATA_EMISSAO_RENDA,  FIELD_FONTE_PAGADORA, FIELD_DATA_ADMISSAO, FIELD_RENDA_BRUTA,
	FIELD_RENDA_LIQUIDA, FIELD_DESCRICAO_OCUPACAO, FIELD_EMISSOR_RENDA, FIELD_IRRF, FIELD_CARACTERISTICA, FIELD_PERIODO_REFERENCIA, FIELD_CODIGO_SIRIC, 
	FIELD_TIPO_FONTE_PAGADORA, FIELD_TIPO_RENDA, FIELD_CODIGO_OCUPACAO];

const FIELDS_ABERTURA_CONTA_CONTRATO_RELACIONAMENTO = {
		numero_operacao : '001',
		numero_agencia: '0002',
		numero_conta: '12345',
		numero_dv: '9',
		data_abertura: null,
		nome_titular_1: null,
		identificador_titular_1: null,
		bloco_cheque_especial: false,
		limite_cheque_especial: 0,
		dia_debito: 1,
		taxa_efetiva_mensal: '12,00',
		taxa_efetiva_anual: '289,59',
		cet_mensal: '13,20',
		cet_anual: '342,74',
		garantia_percentual: '0',
		garantia_valor: '0,00',
		bloco_cdc: false,
		bloco_cartao: false,
		cartao_bandeira: 'Elo',
		cartao_variante: 'Nacional',
		cartao_limite: 0,
		cartao_vencimento: 1,
		cartao_email: null,
		cartao_pontos: true,
		cartao_seguro: true,
		cartao_avaliacao: false,
		cartao_campanha: true,

		cartao_adicional_nome_1 : null,
		cartao_adicional_cpf_1: null,
		cartao_adicional_nascimento_1: null,
		cartao_adicional_limite_1: null,
		cartao_adicional_nome_2: null,
		cartao_adicional_cpf_2: null,
		cartao_adicional_nascimento_2: null,
		cartao_adicional_limite_2: null,
		cartao_adicional_nome_3: null,
		cartao_adicional_cpf_3: null,
		cartao_adicional_nascimento_3: null,
		cartao_adicional_limite_3: null,
		cartao_adicional_nome_4: null,
		cartao_adicional_cpf_4: null,
		cartao_adicional_nascimento_4: null,
		cartao_adicional_limite_4: null,
		cartao_adicional_nome_5: null,
		cartao_adicional_cpf_5: null,
		cartao_adicional_nascimento_5: null,
		cartao_adicional_limite_5: null,

		bloco_adep: false,
		bloco_dda: false,
		bloco_assinatura_eletronica: false,

		bloco_sms : false,
		sms_celular: null,
		sms_valor_saque: null,
		sms_valor_debito: null,
		sms_fgts: null,
		sms_produtos_servicos: null,
		sms_cartao_credito: null,
		local_impressao: 'Brasília',
		dia_impressao: null,
		mes_impressao: null,
		ano_impressao: null
}

const FIELDS_ABERTURA_CONTA_TERMO_ADESAO = {
		numero_agencia : '0002',
		numero_conta : '12345',
		numero_dv : '9',
		bloco_adesao : true,
		data_debito : 1,
		modalidade : 'CESTA FÁCIL CAIXA',
		composicao : '4 saques, 4 extratos e 8 TEDs',
		data_abertura : null,
		data_encerramento : '',
		nome_titular_1 : null,
		identificador_titular_1 : null,
		local_impressao : 'Brasília',
		dia_impressao : null,
		mes_impressao : null,
		ano_impressao : null
}

const FIELDS_DADOS_DECLARADOS = {
 declaracao_proposito : '',
 n_tin: '',
 declaracao_fatca_crs: 'Sim',
 tipo_patrimonio: '',
 email_secundario: '',
 email_principal: '',
 contato_4: '',
 telefone_4: '',
 ddd_4: '',
 finalidade_4 : '' ,
 contato_3: '',
 telefone_3: '',
 ddd_3: '',
 finalidade_3: '',
 contato_2: '',
 telefone_2: '',
 ddd_2: '',
 finalidade_2: '',
 contato_1: '',
 telefone_1: '',
 ddd_1: '',
 finalidade_1: '',
 nome: '',
 conjuge_nascimento: '',
 conjuge_cpf: '',
 conjuge_nome: '',
 estado_civil: '',
 endereco: '',
 codigo_ocupacao: '',
 tipo_ocupacao: '',
 grau_instrucao: '',
 sexo: '',
 cpf: '',
 bloco_sms: true,
 sms_celular: null,
 sms_valor_saque: 'R$50,00',
 sms_valor_debito: 'R$50,00',
 sms_fgts: 'Sim',
 sms_produtos_servicos: 'Sim',
 sms_cartao_credito: 'Sim'
};


const MENU_USER_SICLI = '{"notes":[],"enabled": true,"icon":"fa-arrow-right","description":null,"title":"XXX","locked":false,"target":"resumodoCliente","platforms":[{"name":"Desktop","icon":"fa fa-desktop","id":"desktop","check":true},{"name":"Browser Mobile","icon":"glyphicon glyphicon-globe","id":"mobile","check":true},{"name":"App Mobile","icon":"fa fa-mobile","id":"nativo","check":true},{"name":"Android","icon":"fa fa-android","id":"android","check":true},{"name":"IOS","icon":"fa fa-apple","id":"ios","check":true},{"name":"WinPhone","icon":"fa fa-windows","id":"winphone","check":true},{"name":"Impressão","icon":"fa fa-print","id":"printer","check":true}],"group":"Resumo"}';

const   userTest ={
		  jti: "bae26635-beb9-4640-b0d2-30da8c232353",
		  exp: 1525460046,
		  nbf: 0,
		  iat: 1525457046,
		  iss: "https://login.des.caixa/auth/realms/intranet",
		  aud: "cli-web-mtr",
		  sub: "d5c045e5-864e-4e66-94de-60618651c24a",
		  typ: "Bearer",
		  azp: "cli-web-mtr",
		  auth_time: 1525448160,
		  session_state: "8b23b5ba-e893-4143-972c-794b777c2fb0",
		  acr: "0",
		  client_session: "3d422af3-0a4e-475e-be7e-559aa32cba33",
		  'allowed-origins': [
		    "http://simtr-dev.nprd.caixa",
		    "http://simtr.geban.caixa",
		    "http://go7875et148",
		    "http://simtr-tqs.nprd.caixa",
		    "http://simtr-hmp.nprd.caixa",
		    "http://localhost",
		    "http://simtr.des.pedes.caixa",
		    "http://simtr-prd.nprd.caixa"
		  ],
		  realm_access: {
		    roles: [
		      "ecm_user",
		      "uma_authorization"
		    ]
		  },
		  resource_access: {
		    account: {
		      roles: [
		        "manage-account",
		        "view-profile"
		      ]
		    }
		  },
		  'uf-lotacaofisica': "DF",
		  'co-unidade': "5402",
		  'nu-funcao': "2030",
		  'no-usuario': "FABIO SEIXAS SALES",
		  'co-cargo': "TBN",
		  'nu-cpf': "81056699515",
		  'no-funcao': "COOR PROJETOS MATRIZ",
		  preferred_username: "c090347",
		  given_name: "FABIO SEIXAS SALES",
		  'nu-lotacaofisica': "5402",
		  'no-cargo': "TECNICO BANCARIO NOVO",
		  'sg-unidade': "GEBAN",
		  'no-unidade': "GN PADRAO PARA PROCESSOS BANCARIOS",
		  'no-lotacaofisica': "GN PADRAO PARA PROCESSOS BANCARIOS",
		  family_name: "SALES",
		  email: "fabio.sales@caixa.gov.br"
		}

function getMimeType(extension) {	
	switch (extension.toLowerCase()) {
	case 'bmp':
		return 'image/bmp';
	case 'jpg':
		return 'image/jpg';
	case 'jpeg':
		return 'image/jpeg';
	case 'png':
		return 'image/png';
	case 'tif':
	case 'tiff':
		return 'image/tiff';
	case 'pdf':
		return 'application/pdf';
		
	default:
		return '';
	}
}

function openPDFForAll($scope ,$http) {
	if ($scope.extracao.link != null && $scope.extracao.link !== 'undefined') {
		
		if($scope.currentDocument.format.toLowerCase() == 'pdf') {
			window.open($scope.extracao.link, '_blank');
		} else {
			$scope.showCanvas = true;			
		}
		
//		var pdfjsLib = window['pdfjs-dist/build/pdf'];
//		
//		PDFJS.workerSrc = location.protocol + '//' + location.hostname + location.pathname + 'js/pdf.worker.js';
//		pdfjsLib.PDFJS.workerSrc = location.protocol + '//' + location.hostname + location.pathname + 'js/pdf.worker.js';
//		
//		var loadPDF = pdfjsLib.PDFJS.getDocument($scope.extracao.link);
//		loadPDF.promise.then(function (pdf) {
//			var pageNumber = 1;
//			
//			pdf.getPage(pageNumber).then(function (page) {
//				var scale = 1.5;
//				var viewport = page.viewport(scale);
//				
//				var canvas = document.getElementById('pdfRenderer');
//				var context = canvas.getContext('2d');
//				canvas.height = viewport.height;
//				canvas.width = viewport.height;
//				
//				var renderContext = {
//						canvasContext : context,
//						viewport : viewport
//				};
//				
//				var renderTask = page.render(renderContext);
//				renderTask.then(function () {
//					console.log('Page Rendered');
//				});
//			});
//		}, function (error) {
//			console.error(reason);
//		});
	} else {
		$scope.showCanvas = false;
	}
}

function decideFormat(mimetype) {
	var result = null;
	/*if (mimetype) {
		if (mimetype == 'image/tiff') {
			result = 'TIF';
		}
		if (mimetype == 'image/png') {
			result = 'PNG';
		}
		if (mimetype == 'image/jpg' || mimetype == 'image/jpeg') {
			result = 'JPG';
		}
		if (mimetype == 'image/bmp') {
			result = 'BMP';
		}
		if (mimetype == 'application/pdf') {
			result = 'PDF';
		}
	}*/
	mimetype = mimetype.toUpperCase();
	
	var jpeg = mimetype.indexOf('.JPEG');
	var jpg = mimetype.indexOf('.JPG');
	var tiff = mimetype.indexOf('.TIFF');
	var tif = mimetype.indexOf('.TIF');
	var bmp = mimetype.indexOf('.BMP');
	var png = mimetype.indexOf('.PNG');
	var pdf = mimetype.indexOf('.PDF');
	
	
	if (mimetype) {
		if (jpeg > 0 || jpg > 0) {
			result = 'JPG';
		}
		
		if(tif > 0 || tiff > 0) {
			result = 'TIF';
		}
		
		if(bmp > 0) {
			result = 'BMP';
		}
		
		if(png > 0) {
			result = 'PNG';
		}

		if(pdf > 0) {
			result = 'PDF';
		}
		
	}
	
	
	return result;
}

function getDevices(object){
	var defaultOption = {codigo: -1, device: LABEL_SELECIONE};
	var devices = [defaultOption];
	if(object !== null) {
		Object.keys(object).forEach(function (key) {
			var name = object[key];
			if(name.indexOf(DEVICE_CAMARA) === -1){
				devices.push({codigo: parseInt(key), device: name});
			}
		});	
	}
	return devices;
}

function getURLScanner(selectDevice, showdevices){
	var servico = SERVICO_SCANNER;
	var param = servico.split('{0}');
	var url;
	if(showdevices){
		url = param[0] + 'device='+selectDevice + '&' + param[1];
	} else {
		url = param[0] + param[1];
	}
	return url;
}

function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}


function validarAlteracaoCampos(valorOriginal, valorAlterado, percentualPermitido, campoAlteracao, msgalteracaolimite, $scope, GrowlMessage){
	var valorOriginalLimpo = valorOriginal.replace(new RegExp(escapeRegExp(' '), 'g'), '');
	var valorAlteradoLimpo = valorAlterado.replace(new RegExp(escapeRegExp(' '), 'g'), '');
	
	var qtdPermitida = (valorOriginalLimpo.length * percentualPermitido) / 100;
	var diffTamanho = Math.abs(valorOriginalLimpo.length - valorAlteradoLimpo.length);
	
	
	
	
	var totalLetrasOriginal = new Map();
	var totalLetrasAlterada = new Map();
	
	var arrayOriginal = valorOriginalLimpo.split('');
	var arrayAlterada = valorAlteradoLimpo.split('');
	
	for(var a = 0; a < arrayOriginal.length; a++) {
		var totalLetra = totalLetrasOriginal.get(arrayOriginal[a]);
		if(totalLetra != null) {
			totalLetrasOriginal.set(arrayOriginal[a], ++totalLetra);
		} else {
			totalLetrasOriginal.set(arrayOriginal[a], 1);
		}
	}

	for(var a = 0; a < arrayAlterada.length; a++) {
		var totalLetra = totalLetrasAlterada.get(arrayAlterada[a]);
		if(totalLetra != null) {
			totalLetrasAlterada.set(arrayAlterada[a], ++totalLetra);
		} else {
			totalLetrasAlterada.set(arrayAlterada[a], 1);
		}
	}
	
	var difference = 0;
	
	totalLetrasOriginal.forEach(function(value, key){
		var qtdLetraOriginal = totalLetrasOriginal.get(key);
		var qtdLetraAlterada = totalLetrasAlterada.get(key);

		if(qtdLetraAlterada != null && qtdLetraOriginal != null) {
			if(qtdLetraOriginal > qtdLetraAlterada) {
				difference = difference + qtdLetraOriginal - qtdLetraAlterada;
			}

		} else if(qtdLetraOriginal != null){
			difference = difference + qtdLetraOriginal;
		}		
	});

	if(diffTamanho > qtdPermitida) {
		//$('#'+campoAlteracao).removeClass('hide');
		$('.theme-main-input-'+campoAlteracao).addClass('has-error').removeClass('has-success');
		$('.label-'+campoAlteracao).css({"color": "#843534"});
		//$('#btnContinuar').prop("disabled", true);
		$scope.errors.push(msgalteracaolimite);
		
		if(verifyOccurrenceItemList($scope.errors, msgalteracaolimite) === 1) {
			GrowlMessage.addErrorMessage(msgalteracaolimite + ' ' + percentualPermitido + '%.');
		}		
		
		$scope.alteracaolimite = true;
	}else if(difference == 0 && valorOriginalLimpo !== valorAlteradoLimpo && (valorAlteradoLimpo.lenght - valorOriginalLimpo) > qtdPermitida) {
		//$('#'+campoAlteracao).removeClass('hide');
		$('.theme-main-input-'+campoAlteracao).addClass('has-error').removeClass('has-success');
		$('.label-'+campoAlteracao).css({"color": "#843534"});
		//$('#btnContinuar').prop("disabled", true);
		removeFromList($scope.errors, msgalteracaolimite);
		$scope.errors.push(msgalteracaolimite);
	
		if(verifyOccurrenceItemList($scope.errors, msgalteracaolimite) === 1) {
			GrowlMessage.addErrorMessage(msgalteracaolimite + ' ' + percentualPermitido + '%.');
		}			
		
		$scope.alteracaolimite = true;
	}else if(difference > qtdPermitida){
		//$('#'+campoAlteracao).removeClass('hide');
		$('.theme-main-input-'+campoAlteracao).addClass('has-error').removeClass('has-success');
		$('.label-'+campoAlteracao).css({"color": "#843534"});
		//$('#btnContinuar').prop("disabled", true);
		removeFromList($scope.errors, msgalteracaolimite);
		$scope.errors.push(msgalteracaolimite);
		
		if(verifyOccurrenceItemList($scope.errors, msgalteracaolimite) === 1) {
			GrowlMessage.addErrorMessage(msgalteracaolimite + ' ' + percentualPermitido + '%.');
		}			
		
		$scope.alteracaolimite = true;
	}else{
		//$('#'+campoAlteracao).addClass('hide');
		$('.theme-main-input-'+campoAlteracao).removeClass('has-error').addClass('has-success');
		$('.label-'+campoAlteracao).css({"color": "black"});
		//$('#btnContinuar').prop("disabled", false);
		$scope.alteracaolimite = false;
		if($( ".theme-main-input-" + campoAlteracao).hasClass( "has-success" )){
			removeFromList($scope.errors, msgalteracaolimite);
		}
	}
	
}

function updateDataForExtracao(DossieService, $scope ,$http , $rootScope , fields , TIPO_DOCUMENTO, tela) {
	var obj = {cpf_cliente :  $scope.extracao.cpf_cliente, 
			   integracao : INTEGRACAO , 
			   identificador : $scope.extracao.identificador ,
			   dados : mounObjToUpdateDataExtracao( fields , $scope.container) 
			};
	$rootScope.$broadcast(START_LOADING);
	$http.post(URL_SIMTR + SERVICO_ATUALIZA_EXTRACAO , obj).then(function (res) {
		if(res.status == 204) {
			$scope.autorizacao = DossieService.getAutorizacao();
			removeFromList($scope.autorizacao.documentos_pendentes, TIPO_DOCUMENTO);
			atualizaAutorizacaoComExtracao(DossieService, TIPO_DOCUMENTO);			
			DossieService.setAutorizacao($scope.autorizacao);
			$rootScope.auditarAtendimento(tela, $http, 'Atualizado', 'digitalizacaodeDocumentos');
			$rootScope.$broadcast(FINISH_LOADING);
		} else {
    		$rootScope.error = res.data.mensagem;
    		$rootScope.showError = true;
    		$rootScope.$broadcast(FINISH_LOADING);
		}
	}, function (error) {
		$rootScope.putError(error);
		$rootScope.$broadcast(FINISH_LOADING);
	});
}

  function atualizaAutorizacaoComExtracao(DossieService, tipoDoc){
	
	var autorizacao = DossieService.getAutorizacao();
	if(autorizacao.documentos_utilizados == undefined){
		autorizacao.documentos_utilizados = [];
	}
	
	for (var i = 0;  i < autorizacao.documentos_utilizados.length; i++){
		if(autorizacao.documentos_utilizados[i].tipo == getTipoDocumentosUtilizados(DossieService, tipoDoc)){
			autorizacao.documentos_utilizados[i].link = DossieService.getExtracao().link;
			autorizacao.documentos_utilizados[i].identificador = DossieService.getExtracao().identificador;
			return;
		}
	}
	
	var obj = {
			tipo : getTipoDocumentosUtilizados(DossieService, tipoDoc),
			link : DossieService.getExtracao().link,
			identificador : DossieService.getExtracao().identificador
	};
	
	 autorizacao.documentos_utilizados.push(obj);
}

	function getTipoDocumentosUtilizados (DossieService, tipoDoc){
			if(tipoDoc == IDENTIFICACAO){
				if(DossieService.getClasse() == CLASS_CNH){
					return "CNH";
				}else{
					return "CARTEIRA_IDENTIDADE";
				}
			}	
				
			if(tipoDoc == RENDA){
				return "DEMONSTRATIVO_PAGAMENTO";
			}	
			if(tipoDoc == ENDERECO){
				return "DOCUMENTO_CONCESSIONARIA";
			}
			if(tipoDoc == CARTAO_ASSINATURA){
				return "CARTAO_ASSINATURA";
			}
			if(tipoDoc == DADOS_DECLARADOS){
				return "DADOS_DECLARADOS";
			}
			return '';
	
	};


function verifyOccurrenceItemList(list, key) {
	var qtd = 0;
	if (list && key) {
		for (var i = 0 ; i < list.length; i++) {
			if(list[i] === key) {
				qtd++;
			}
		}
	}
	return qtd;
}

function removeFromList(list, key) {
	if (list && key) {
		for (var i = 0 ; i < list.length; i++) {
			if (list[i].indexOf(key) != -1) {
				list.splice(i, 1);
			}
		}
	}
}

function mounObjToUpdateDataExtracao(fields, data) {
	var result = [];
	for (var i = 0 ; i < fields.length; i++) {
		for (var x = 0 ; x < data.length ; x++) {
			if (data[x].key == fields[i].key) {
				if(!data[x].value && data[x].default_value){
					result.push({chave: data[x].key , valor: data[x].default_value});
				}else{
					result.push({chave: data[x].key , valor: data[x].value});
				}
				break;
			}
		}
	}
	
	return result;
}

function updateFieldDate(key, value){
	var data = null;
	if(key === FIELD_DATA_REFERENCIA.key) {
		data = formatFieldDate(value);
	} else if(key === FIELD_DATA_EMISSAO.key) {
		data = formatFieldDate(value);
	} else if(key === FIELD_DATA_NASCIMENTO.key) {
		data = formatFieldDate(value);
	} else {
		data = value;	
	}
	return data;
	
}

function formatFieldDate(value) {
	var data = null;
	var dia = null;
	var mes = null;
	var ano = null;
	if(!value.includes('/') && validaDATA(value)) {
		dia = value.substring(0, 2);
		mes = value.substring(2, 4);
		ano = value.substring(4, 8);
	} else {
		if(validaDATA(value)){
			dia = value.substring(0, 2);
			mes = value.substring(3, 5);
			ano = value.substring(6, 10);
		} else {
			return '';
		}
	}
	data = dia + "/" + mes + "/" + ano;
	return data;
}

function mountFieldsFrontEndExtracao(fields, data) {
	var result = [];
	for (var i = 0 ; i < fields.length ; i++) {
		if(fields[i].data){
			if(fields[i].tooltip){
				result.push({name : fields[i].name , value :  findValueExtracao(data, fields[i].key) , key : fields[i].key, percentual : findPercentualExtracao(data, fields[i].key), error: fields[i].required, data: fields[i].data, msgdata: fields[i].msgdata, alteracaolimite: fields[i].alteracaolimite, hidden: fields[i].hidden, default_value: fields[i].default_value, tooltip: fields[i].tooltip, dica: fields[i].dica});  				
			} else {
				result.push({name : fields[i].name , value :  findValueExtracao(data, fields[i].key) , key : fields[i].key, percentual : findPercentualExtracao(data, fields[i].key), error: fields[i].required, data: fields[i].data, msgdata: fields[i].msgdata, alteracaolimite: fields[i].alteracaolimite, hidden: fields[i].hidden, default_value: fields[i].default_value, tooltip: fields[i].tooltip});  
			}
		} else {
			if(fields[i].tooltip){
				result.push({name : fields[i].name , value :  findValueExtracao(data, fields[i].key) , key : fields[i].key, percentual : findPercentualExtracao(data, fields[i].key), error: fields[i].required, data: fields[i].data, alteracaolimite: fields[i].alteracaolimite, hidden: fields[i].hidden, default_value: fields[i].default_value, tooltip: fields[i].tooltip, dica: fields[i].dica});				
			} else {
				result.push({name : fields[i].name , value :  findValueExtracao(data, fields[i].key) , key : fields[i].key, percentual : findPercentualExtracao(data, fields[i].key), error: fields[i].required, data: fields[i].data, alteracaolimite: fields[i].alteracaolimite, hidden: fields[i].hidden, default_value: fields[i].default_value, tooltip: fields[i].tooltip});
			}
		}
	}
	
	return result;
}

function findPercentualExtracao(data, key) {
	if (data && key) {
		for (var i = 0 ; i < data.length; i++) {
			if (data[i].chave == key) {
				return data[i].percentual_alteracao;
			}
		}
	}
	
	return null;
}

function findValueExtracao(data, key) {
	if (data && key) {
		for (var i = 0 ; i < data.length; i++) {
			if (data[i].chave == key) {
				return data[i].valor;
			}
		}
	}
	
	return null;
}

function parseToFloat(value) {
	value = value.substring(value.indexOf('R$') + 2).trim();
	value = value.replace('.', '');
	value = value.replace(',', '.');
	return parseFloat(value);
}

function urlBase64Decode (str) {
	var output = str.replace(/-/g, '+').replace(/_/g, '/');
	switch (output.length % 4) {
	  case 0: { break; }
	  case 2: { output += '=='; break; }
	  case 3: { output += '='; break; }
	  default: {
	    throw 'Illegal base64url string!';
	  }
	}
	return window.decodeURIComponent(escape(window.atob(output))); // polyfill
																		// https://github.com/davidchambers/Base64.js
}

function decodeToken(token) {
    var parts = token.split('.');

    if (parts.length !== 3) {
      throw new Error('JWT must have 3 parts');
    }

    var decoded = urlBase64Decode(parts[1]);
    if (!decoded) {
      throw new Error('Cannot decode the token');
    }

    return angular.fromJson(decoded);
  }
  
  function getTokenExpirationDate(token) {
    var decoded = decodeToken(token);

    if(typeof decoded.exp === "undefined") {
      return null;
    }

    var d = new Date(0); // The 0 here is the key, which sets the date to the
							// epoch
    d.setUTCSeconds(decoded.exp);

    return d;
  }
  
  function isTokenExpired (token, offsetSeconds) {
    var d = getTokenExpirationDate(token);
    offsetSeconds = offsetSeconds || 0;
    if (d === null) {
      return false;
    }

    // Token expired?
    return !(d.valueOf() > (new Date().valueOf() + (offsetSeconds * 1000)));
  }
  
  String.prototype.replaceAll = function(search, replacement) {
	    var target = this;
	    return target.replace(new RegExp(search, 'g'), replacement);
	};

  /**
   * Retorna a posição de um objeto em um array dada sua chave.
   * O array passado deve ter o campo chamado 'chave'.
   * @param {*} nomeChave 
   * @param {*} myArray 
   */
  function searchArray(nomeChave, myArray){
    for (var i=0; i < myArray.length; i++) {
        if (myArray[i].chave === nomeChave) {
            return i;
        }
    }
}
  
Date.prototype.getDateFormatted = function() {
	var date = this.getDate();
	return date < 10 ? '0' + date : date;
}
  