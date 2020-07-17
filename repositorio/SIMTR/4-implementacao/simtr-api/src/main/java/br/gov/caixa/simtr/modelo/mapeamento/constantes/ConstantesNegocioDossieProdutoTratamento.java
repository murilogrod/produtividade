package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieProdutoTratamento {

    private ConstantesNegocioDossieProdutoTratamento() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_DOSSIE_PRODUTO = "v1.negocio.dossieproduto.tratamento.DossieProdutoDTO";
    public static final String API_MODEL_V1_VERIFICACAO = "v1.negocio.dossieproduto.tratamento.VerificacaoDTO";
    
    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO = "dossie_produto";
    public static final String XML_ROOT_ELEMENT_VERIFICACAO = "verificacao";
    
    //************* NOMES DE ATRIBUTOS *************
    //Comuns
    public static final String INSTANCIA_DOCUMENTO = "instancia_documento";//Dossie_Produto, Verificacao
    public static final String PROCESSO_FASE = "processo_fase";//Dossie_Produto, Verificacao
    public static final String UNIDADE = "unidade";//Dossie_Produto, Verificacao

    //Dossie Produto e Situacao Dossie
    public static final String ALTERACAO = "alteracao";
    public static final String CANAL_CAIXA = "canal_caixa";//Canal, Dossie Produto
    public static final String ID = "id";
    public static final String INSTANCIAS_DOCUMENTO = "instancias_documento";
    public static final String UNIDADE_CRIACAO = "unidade_criacao";
    public static final String UNIDADE_PRIORIZADO = "unidade_priorizado";
    public static final String MATRICULA_PRIORIZADO = "matricula_priorizado";
    public static final String PESO_PRIORIDADE = "peso_prioridade";
    public static final String DATA_HORA_FINALIZACAO = "data_hora_finalizacao";
    public static final String UNIDADES_TRATAMENTO = "unidades_tratamento";
    public static final String SITUACAO_ATUAL = "situacao_atual";
    public static final String DATA_HORA_SITUACAO = "data_hora_situacao";
    public static final String HISTORICO_SITUACOES = "historico_situacoes";
    public static final String SITUACAO_DOSSIE = "situacao_dossie";
    public static final String PROCESSO_DOSSIE = "processo_dossie";
    public static final String RESPOSTA_FORMULARIO = "resposta_formulario";
    public static final String RESPOSTAS_FORMULARIO = "respostas_formulario";
    public static final String PRODUTOS_CONTRATADOS = "produtos_contratados";
    public static final String PRODUTO_CONTRATADO = "produto_contratado";
    public static final String VINCULOS_PESSOAS = "vinculos_pessoas";
    public static final String VINCULO_PESSOA = "vinculo_pessoa";
    public static final String GARANTIAS_INFORMADAS = "garantias_informadas";
    public static final String GARANTIA_INFORMADA = "garantia_informada";
    public static final String VERIFICACOES = "verificacoes";
    public static final String VERIFICACAO = "verificacao";
    
    //Checklist e Verificação
    public static final String APROVADA = "aprovada";
    public static final String CHECKLIST = "checklist";
    public static final String DATA_HORA_VERIFICACAO = "data_hora_verificacao";
    public static final String IDENTIFICADOR_VERIFICACAO = "identificador_verificacao";
    public static final String REALIZADA = "realizada";
    
}
