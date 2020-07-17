package br.gov.caixa.simtr.util;

public final class ConstantesUtil {

    public static final String API_KEY_SIMTR = "simtr_apikey";

    public static final String CHARSET_UTF_8 = "UTF-8";

    public static final String PROCESSO_ADMINISTRATIVO_ELETRONICO = "Processo Administrativo Eletrônico";

    public static final Integer TAMANHO_CPF = 11;
    public static final Integer TAMANHO_CNPJ = 14;

    public static final String TIPOLOGIA_CARTAO_ASSINATURA = "0003000100010001";
    public static final String TIPOLOGIA_CONSULTA_CADASTRAL = "0001000100060011";
    public static final String TIPOLOGIA_DADOS_DECLARADOS_DD = "0001000600029001";
    public static final String TIPOLOGIA_DADOS_DECLARADOS_PF = "0001000600029002";
    public static final String TIPOLOGIA_DADOS_DECLARADOS_PF_CONJUGE = "0001000600029003";
    public static final String TIPOLOGIA_DADOS_DECLARADOS_PJ = "0001000200019002";
    public static final String TIPOLOGIA_SELFIE = "0001000100020009";
    public static final String TIPOLOGIA_DADOS_SIISO = "0001000200019003";
    public static final String TIPOLOGIA_DADOS_AVALIADOR_TOMADOR = "0001000200030011";
    
    public static final Integer TRATAMENTO_UNIDADE_PRIORIZACAO_RETORNO_FILA = 0;

    // ====== CONFIGURACOES ======
    public static final String SCHEMA_MTRSM001 = "mtr";
    public static final String SCHEMA_MTRSM002 = "mtr";
    public static final String PERSISTENCE_UNIT = "simtrPU";

    public static final String RELATORIO_CAMINHO_JASPER = "/relatorios/";
    public static final String RELATORIO_CAMINHO_IMAGENS = "/relatorios/imagens/";
    // ====== CONFIGURACOES ======

    // ====== MENSAGENS ======
    public static final String MSG_CANAL_NAO_AUTORIZADO = "Canal de comunicação não autorizado operação solicitada.";
    public static final String MSG_CANAL_NAO_LOCALIZADO_SSO = "Canal de comunicação não localizado para cliente SSO do usuario.";
    public static final String MSG_CANAL_JANELA_EXTRACAO_NAO_AUTORIZADO = "Canal de comunicação não autorizado para consumo da janela de extração de dados solicitada.";
    public static final String MSG_CANAL_SERVICO_AUTENTICIDADE_NAO_AUTORIZADO = "Canal de comunicação não autorizado para consumo do serviço de avaliação de autenticidade.";
    public static final String MSG_CNPJ_INVALIDO = "CNPJ informado inválido.";
    public static final String MSG_CPF_INVALIDO = "CPF informado inválido.";
    public static final String MSG_CONTEUDO_ENCAMINHADO_INVALIDO = "Conteudo do documento encaminhado para armazenamento é invalido.";
    public static final String MSG_CONTROLE_DOCUMENTO_NAO_LOCALIZADO = "Registro de controle de solicitação de serviço não localizado para o codigo fornecido.";
    public static final String MSG_DOCUMENTO_ATUALIZADO_PARCIALMENTE = "O documento não pode ser rejeitado por já ter recebido informação de extração de dados e/ou Avaliação de Autenticidade";
    public static final String MSG_DOCUMENTO_CANAL_NAO_AUTORIZADO = "Canal não autorizado para manipulação do documento solicitado.";
    public static final String MSG_DOCUMENTO_ENCAMINHADO_NULO = "Documento encaminhado para armazenamento é nulo.";
    public static final String MSG_DOCUMENTO_NAO_CONTEM_ATRIBUTOS = "Documento encaminhado para armazenamento não contem a lista de atributos do documento carregada.";
    public static final String MSG_DOCUMENTO_NAO_CONTEM_TIPO = "Documento encaminhado para armazenamento não contem o tipo de documento definido.";
    public static final String MSG_DOCUMENTO_NAO_LOCALIZADO = "Documento localizado para o identificador informado.";
    public static final String MSG_DOCUMENTO_ORIGEM_INVALIDA = "Origem do documento inválida.";
    public static final String MSG_DOCUMENTO_REJEITADO = "O documento já teve indicação de rejeição e por isso não pode ser atualizado.";
    public static final String MSG_DOCUMENTO_SEM_CANAL = "Canal de comunicação não fornecido no documento a ser armazenado.";
    public static final String MSG_DOSSIE_CLIENTE_CNPJ_NAO_LOCALIZADO = "Dossiê do cliente não localizado para o CNPJ informado.";
    public static final String MSG_DOSSIE_CLIENTE_CPF_NAO_LOCALIZADO = "Dossiê do cliente não localizado para o CPF informado.";
    public static final String MSG_FALHA_CRIACAO_DOCUMENTO_SIECM = "Falha ao criar documento para envio ao SIECM: ";
    public static final String MSG_FALHA_ATUALIZACAO_ATRIBUTOS_DOCUMENTO = "Falha ao preparar os atributos encaminhados para armazenamento.";
    public static final String MSG_FALHA_ID_NAO_LOCALIZADO_SIECM = "Documento não localizado para o identificador informado. ID = ";
    public static final String MSG_FORMATO_CONTEUDO_INVALIDO = "Formato do Conteudo do documento encaminhado para armazenamento é invalido.";
    public static final String MSG_JANELA_EXTRACAO_NAO_DEFINIDA = "Janela temporal de extração de dados não definida.";
    public static final String MSG_TIPO_DOCUMENTO_NAO_LOCALIZADO = "Tipo de Documento não localizado para o código de tipologia fornecido.";
    // ====== MENSAGENS ======

    // ====== PALAVRAS-CHAVE ======
    public static final String KEY_CAIXA = "CAIXA";
    public static final String KEY_CODIGO_NSU = "codigoNSU";
    public static final String KEY_DATA_HORA = "dataHora";
    public static final String KEY_DOSSIE_PRODUTO = "dossieProduto";
    public static final String KEY_ID_DOSSIE_PRODUTO = "idDossieProduto";
    public static final String KEY_ID_PRODUTO = "idProduto";
    public static final String KEY_NADA_CONSTA = "NADA CONSTA";
    public static final String KEY_OCORRENCIA = "ocorrencia";
    public static final String KEY_REGULARIDADE = "regularidade";
    public static final String KEY_SEQUENCIAL = "sequencial";
    public static final String KEY_SISTEMA = "sistema";
    public static final String KEY_SITUACAO = "situacao";
    // ====== PALAVRAS-CHAVE ======

    // ====== SEGURANCA ======
    public static final String KEYCLOAK_SECURITY_DOMAIN = "keycloak";
    
    public static final String CLIENT_ID_SIMTR_WEB = "cli-web-mtr";
    public static final String CLIENT_ID_SIMTR_SER = "cli-ser-mtr";
    public static final String CLIENT_ID_SIMTR_BPM = "cli-web-mtr-bpm";

    public final static String HEADER_AUTHORIZATION = "Authorization";    
    
    public static final String PERFIL_SSOUMA = "uma_authorization";
    public static final String PERFIL_MTRADM = "MTRADM";
    public static final String PERFIL_MTRTEC = "MTRTEC";
    public static final String PERFIL_MTRAUD = "MTRAUD";
    public static final String PERFIL_MTRSDNOPE = "MTRSDNOPE";
    public static final String PERFIL_MTRSDNTTO = "MTRSDNTTO";
    public static final String PERFIL_MTRSDNTTG = "MTRSDNTTG";
    public static final String PERFIL_MTRSDNMTZ = "MTRSDNMTZ";
    public static final String PERFIL_MTRSDNSEG = "MTRSDNSEG";
    public static final String PERFIL_MTRSDNEXT = "MTRSDNEXT";
    public static final String PERFIL_MTRSDNINT = "MTRSDNINT";
    public static final String PERFIL_MTRBPMANP = "MTRBPMANP";
    public static final String PERFIL_MTRBPMDEV = "MTRBPMDEV";
    public static final String PERFIL_MTRBPMUSU = "MTRBPMUSU";
    public static final String PERFIL_MTRBPMAPV = "MTRBPMAPV";
    public static final String PERFIL_MTRBPMADM = "MTRBPMADM";
    public static final String PERFIL_MTRDOSOPE = "MTRDOSOPE";
    public static final String PERFIL_MTRDOSMTZ = "MTRDOSMTZ";
    public static final String PERFIL_MTRDOSINT = "MTRDOSINT";
    public static final String PERFIL_MTRPAEOPE = "MTRPAEOPE";
    public static final String PERFIL_MTRPAESIG = "MTRPAESIG";
    public static final String PERFIL_MTRPAEMTZ = "MTRPAEMTZ";
    // ====== SEGURANCA ======

    // ====== REGEX ======
    public static final String REGEX_BASE64 = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    // ====== REGEX ======
    
    // ====== SIECM ======
    public static final String SIECM_OS_CAIXA = "OS_CAIXA";
    public static final String SIECM_OS_DOSSIE_DIGITAL = "OS_DOSSIEDIGITAL";
    public static final String SIECM_OS_PROCESSO_ADMINISTRATIVO = "OS_PADM";
    public static final String SIECM_OS_REUSO = "OS_REUSO";
    // ====== SIECM ======

    private ConstantesUtil() {
    }
}
