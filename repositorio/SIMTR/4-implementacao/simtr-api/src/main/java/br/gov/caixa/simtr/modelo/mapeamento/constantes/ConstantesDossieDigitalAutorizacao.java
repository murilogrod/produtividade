package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesDossieDigitalAutorizacao {

    private ConstantesDossieDigitalAutorizacao() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ANALISE_REGRA = "v1.dossiedigital.dossiecliente.AnaliseRegraDTO";
    public static final String API_MODEL_V1_DOCUMENTO_CONCLUSAO = "v1.dossiedigital.autorizacao.DocumentoConclusaoDTO";
    public static final String API_MODEL_V1_DOCUMENTO_UTILIZADO = "v1.dossiedigital.autorizacao.DocumentoUtilizadoDTO";
    public static final String API_MODEL_V1_MENSAGEM_ORIENTACAO = "v1.dossiedigital.autorizacao.MensagemOrientacaoDTO";
    public static final String API_MODEL_V1_RETORNO_AUTORIZACAO = "v1.dossiedigital.autorizacao.RetornoAutorizacaoDTO";
    public static final String API_MODEL_V1_RETORNO_AUTORIZACAO_CONJUNTA = "v1.dossiedigital.autorizacao.RetornoAutorizacaoConjuntaDTO";
    public static final String API_MODEL_V1_SOLICITACAO_AUTORIZACAO = "v1.dossiedigital.autorizacao.SolicitacaoAutorizacaoDTO";
    public static final String API_MODEL_V1_SOLICITACAO_AUTORIZACAO_CONJUNTA = "v1.dossiedigital.autorizacao.SolicitacaoAutorizacaoConjuntaDTO";
    public static final String API_MODEL_V1_SOLICITACAO_CONCLUSAO_OPERACAO = "v1.dossiedigital.autorizacao.SolicitacaoConclusaoOperacaoDTO";
    
    public static final String API_MODEL_V2_ATRIBUTO_DOCUMENTO = "v2.dossiedigital.autorizacao.AtributoDocumentoDTO";
    public static final String API_MODEL_V2_DOCUMENTO_CONCLUSAO = "v2.dossiedigital.autorizacao.DocumentoConclusaoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ANALISE_REGRA = "analise_regra";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO_CONCLUSAO = "documento_conclusao";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO_UTILIZADO = "documento_utilizado";
    public static final String XML_ROOT_ELEMENT_MENSAGEM_ORIENTACAO = "mensagem_orientacao";
    public static final String XML_ROOT_ELEMENT_RETORNO_AUTORIZACAO = "retorno_autorizacao";
    public static final String XML_ROOT_ELEMENT_RETORNO_AUTORIZACAO_CONJUNTA = "retorno_autorizacao-conjunta";
    public static final String XML_ROOT_ELEMENT_SIMULACAO_AUTORIZACAO = "simulacao_autorizacao";
    public static final String XML_ROOT_ELEMENT_SOLICITACAO_AUTORIZACAO = "solicitacao_autorizacao";
    public static final String XML_ROOT_ELEMENT_SOLICITACAO_VERIFICACAO_AUTORIZACAO = "solicitacao_verificacao_autorizacao";
    public static final String XML_ROOT_ELEMENT_SOLICITACAO_AUTORIZACAO_CONJUNTA = "solicitacao_autorizacao_conjunta";
    public static final String XML_ROOT_ELEMENT_SOLICITACAO_CONCLUSAO_OPERACAO = "solicitacao_conclusao_operacao";

    //************* NOMES DE ATRIBUTOS *************
    public static final String INTEGRACAO = "integracao";

    //Atributos da Serviço de Autorizacao
    public static final String AUTORIZACAO = "autorizacao";
    public static final String AUTORIZACOES = "autorizacoes";
    public static final String AUTORIZADO = "autorizado";
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String CHAVE = "chave";
    public static final String CPF_CLIENTE = "cpf_cliente";
    public static final String CNPJ_CLIENTE = "cnpj_cliente";
    public static final String DATA_HORA = "data_hora";
    public static final String DOCUMENTO_UTILIZADO = "documento_utilizado";
    public static final String DOCUMENTO_PENDENTE = "documento_pendente";
    public static final String DOCUMENTOS_UTILIZADOS = "documentos_utilizados";
    public static final String DOCUMENTOS_PENDENTES = "documentos_pendentes";
    public static final String DOSSIE_DIGITAL = "dossie_digital";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String IDENTIFICADOR = "identificador";
    public static final String IDENTIFICADOR_COMPOSICAO = "identificador_composicao";
    public static final String LINK = "link";
    public static final String MENSAGEM = "mensagem";
    public static final String MENSAGEM_ORIENTACAO = "mensagem_orientacao";
    public static final String MENSAGENS_ORIENTACAO = "mensagens_orientacao";
    public static final String MIMETYPE = "mimetype";
    public static final String MODALIDADE = "modalidade";
    public static final String OBSERVACAO = "observacao";
    public static final String OCORRENCIA = "ocorrencia";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    public static final String OPERACAO = "operacao";
    public static final String PRODUTO = "produto";
    public static final String PRODUTO_LOCALIZADO = "produto_localizado";
    public static final String PROSSEGUIR = "prosseguir";
    public static final String RESULTADO_PESQUISA = "resultado_pesquisa";
    public static final String SISTEMA = "sistema";
    public static final String TIPO = "tipo";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALOR = "valor";
}
