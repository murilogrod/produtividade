package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieProdutoValidacao {

    private ConstantesNegocioDossieProdutoValidacao() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ELEMENTO_CONTEUDO_PENDENTE = "v1.negocio.dossieproduto.validacao.ElementoConteudoPendenteDTO";
    public static final String API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.dossieproduto.validacao.FuncaoDocumentalDTO";
    public static final String API_MODEL_V1_PENDENCIA_DOSSIE_PRODUTO = "v1.negocio.dossieproduto.validacao.PendenciaDossieProdutoDTO";
    public static final String API_MODEL_V1_PENDENCIA_RESPOSTA_FORMULARIO = "v1.negocio.dossieproduto.validacao.PendenciaRespostaFormularioDTO";
    public static final String API_MODEL_V1_PENDENCIA_VINCULO_PESSOA = "v1.negocio.dossieproduto.validacao.PendenciaVinculoPessoaDTO";
    public static final String API_MODEL_V1_PENDENCIA_VINCULO_PRODUTO = "v1.negocio.dossieproduto.validacao.PendenciaVinculoProdutoDTO";
    public static final String API_MODEL_V1_PENDENCIA_GARANTIA_INFORMADA = "v1.negocio.dossieproduto.validacao.PendenciaGarantiaInformadaDTO";
    public static final String API_MODEL_V1_PENDENCIA_VINCULO_PROCESSO = "v1.negocio.dossieproduto.validacao.PendenciaVinculoProcessoDTO";
    public static final String API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.dossieproduto.validacao.TipoDocumentoDTO";
    public static final String API_MODEL_V1_TIPO_RELACIONAMENTO = "v1.negocio.dossieproduto.validacao.TipoRelacionamentoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO_PENDENTE = "elemento_conteudo_pendente";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_DOSSIE_PRODUTO = "pendencia_dossie_produto";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_RESPOSTA_FORMULARIO = "pendencia_resposta_formulario";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PESSOA = "pendencia_vinculo_pessoa";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PRODUTO = "pendencia_vinculo_produto";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_GARANTIA_INFORMADA = "pendencia_garantia_informada";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PROCESSO = "pendencia_vinculo_processo";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";
    public static final String XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO = "tipo_relacionamento";

    //************* NOMES DE ATRIBUTOS *************
    //Atributos Comums
    public static final String ID = "id"; //elemento_conteudo, funcao_documental, resposta_formulario, tipo_documento, tipo_relacionamento
    public static final String NOME = "nome"; //elemento_conteudo, funcao_documental, tipo_documento, tipo_relacionamento
    public static final String DESCRICAO_PENDENCIA = "descricao_pendencia"; 
    
    //Formulário
    public static final String LABEL = "label";
    public static final String CAMPO_FORMULARIO_LOCALIZADO = "campo_formulario_localizado";
    public static final String RESPOSTA_FORMULARIO = "resposta_formulario";
    public static final String RESPOSTAS_FORMULARIO = "respostas_formulario";


    //Vinculo de Pessoas
    public static final String ASSOCIADO_PROCESSO = "associado_processo";
    public static final String DOSSIE_CLIENTE_LOCALIZADO = "dossie_cliente_localizado";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String IDENTIFICADOR_DOSSIE_CLIENTE = "identificador_dossie_cliente";
    public static final String PENDENCIA_VINCULO_PESSOA = "pendencia_vinculo_pessoa";
    public static final String PENDENCIAS_VINCULO_PESSOA = "pendencias_vinculo_pessoa";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String TIPOS_DOCUMENTO = "tipos_documento";
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";

    //Atributos do Processo e Produto Contratado
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    public static final String CODIGO_OPERACAO = "codigo_operacao";
    public static final String ELEMENTO_CONTEUDO = "elemento_conteudo";
    public static final String ELEMENTOS_CONTEUDO = "elementos_conteudo";
    public static final String ELEMENTO_CONTEUDO_LOCALIZADO = "elemento_conteudo_localizado";
    public static final String IDENTIFICADOR_PROCESSO = "identificador_processo";
    public static final String IDENTIFICADOR_PRODUTO = "identificador_produto";
    public static final String PENDENCIA_PROCESSO_DOSSIE = "pendencia_processo_dossie";
    public static final String PENDENCIAS_PROCESSO_DOSSIE = "pendencias_processo_dossie";
    public static final String PENDENCIA_PROCESSO_FASE = "pendencia_processo_fase";
    public static final String PENDENCIAS_PROCESSO_FASE = "pendencias_processo_fase";
    public static final String PENDENCIA_VINCULO_PRODUTO = "pendencia_vinculo_produto";
    public static final String PENDENCIAS_VINCULO_PRODUTO = "pendencias_vinculo_produto";
    public static final String PENDENCIAS_GARANTIA = "pendencias_garantia";
    public static final String PRODUTO_LOCALIZADO = "produto_localizado";
    public static final String QUANTIDADE_PENDENTES = "quantidade_pendentes";
}
