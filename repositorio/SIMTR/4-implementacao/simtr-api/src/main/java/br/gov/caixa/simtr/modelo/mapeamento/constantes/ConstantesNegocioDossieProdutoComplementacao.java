package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieProdutoComplementacao {

    private ConstantesNegocioDossieProdutoComplementacao() {
    }

    //************* API MODEL *************
    
    public static final String API_MODEL_V1_PROCESSO = "v1.negocio.dossieproduto.complementacao.ProcessoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_PROCESSO = "processo";
    

    //************* NOMES DE ATRIBUTOS *************
    //Processo
    public static final String AVATAR = "avatar";
    public static final String ID = "id"; //Produto_Contratado e Garantia_Informada
    public static final String QUANTIDADE_DOSSIES = "quantidade_dossies";
    public static final String NOME = "nome";
    public static final String PROCESSO_FILHO = "processo_filho";
    public static final String PROCESSOS_FILHO = "processos_filho";
    public static final String UNIDADE_AUTORIZADA = "unidade_autorizada";
    public static final String UNIDADES_AUTORIZADAS = "unidades_autorizadas";
    
    
}
