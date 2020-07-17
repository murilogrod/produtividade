package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDashboard {

    private ConstantesNegocioDashboard() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_DOSSIE_PRODUTO = "v1.negocio.dashboard.DossieProdutoDTO";
    public static final String API_MODEL_V1_PRODUTO_CONTRATADO = "v1.negocio.dashboard.ProdutoContratadoDTO";
    public static final String API_MODEL_V1_SITUACAO_DOSSIE = "v1.negocio.dashboard.SituacaoDossieDTO";
    public static final String API_MODEL_V1_RETORNO_PESSOAL_VISAO_UNIDADE = "v1.negocio.dashboard.RetornoPessoalVisaoUnidadeDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO = "dossie_produto";
    public static final String XML_ROOT_ELEMENT_PRODUTO_CONTRATADO = "produto_contratado";
    public static final String XML_ROOT_ELEMENT_RETORNO_PESSOAL_VISAO_UNIDADE = "retorno_pessoal_visao_unidade";
    public static final String XML_ROOT_ELEMENT_SITUACAO_DOSSIE = "situacao_dosie";

    //************* NOMES DE ATRIBUTOS *************
    //Comuns
    public static final String NOME = "nome";//Produto_Contratado e Situacao_Dosie
    public static final String NOME_CLIENTE = "nome_cliente";//Nome do cliente

    //Retorno Pessoal do Usuário - Visão Unidade
    public static final String DOSSIE_PRODUTO = "dossie_produto";
    public static final String DOSSIES_PRODUTO = "dossies_produto";
    public static final String MENSAGEM = "mensagem";
    public static final String RESUMO_SITUACAO = "resumo_situacao";
    
    //Dossiê de Produto
    public static final String ID = "id";
    public static final String CPF = "cpf";
    public static final String CNPJ = "cnpj";
    public static final String MATRICULA = "matricula";
    public static final String UNIDADE_CRIACAO = "unidade_criacao";
    public static final String UNIDADE_PRIORIZADO = "unidade_priorizado";
    public static final String PESO_PRIORIZADO = "peso_priorizado";
    public static final String CANAL_CAIXA = "canal_caixa";
    public static final String DATA_HORA_FINALIZACAO = "data_hora_finalizacao";
    public static final String SITUACAO_ATUAL = "situacao_atual";
    public static final String DATA_HORA_SITUACAO = "data_hora_situacao";
    public static final String PROCESSO_DOSSIE = "processo_dossie";
    public static final String PROCESSO_FASE = "processo_fase";
    public static final String PROCESSO_FASE_ID = "processo_fase_id";
    ////////////////////////////////////
    public static final String UNIDADES_TRATAMENTO = "unidades_tratamento";
    public static final String UNIDADE_TRATAMENTO = "unidade_tratamento";
    public static final String HISTORICO_SITUACOES = "historico_situacoes";
    public static final String SITUACAO_DOSSIE = "situacao_dossie";
    public static final String PRODUTOS_CONTRATADOS = "produtos_contratados";
    public static final String PRODUTO_CONTRATADO = "produto_contratado";

    //Produto Contratado
    public static final String CODIGO_OPERACAO = "codigo_operacao";
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    
    //Situação do Dossiê
    public static final String DATA_HORA_INCLUSAO = "data_hora_inclusao";
    public static final String DATA_HORA_SAIDA = "data_hora_saida";
    public static final String UNIDADE = "unidade";
    public static final String OBSERVACAO = "observacao";

}
