package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioTipologia {

    private ConstantesNegocioTipologia() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_EXTRACAO = "v1.negocio.tipologia.AtributoExtracaoDTO";
    public static final String API_MODEL_V1_OPCAO_ATRIBUTO = "v1.negocio.tipologia.OpcaoAtributoDTO";
    public static final String API_MODEL_V1_TIPOLOGIA_DOCUMENTAL = "v1.negocio.tipologia.TipologiaDocumentalDTO";
    public static final String VISAO_FUNCAO_API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.tipologia.funcao.FuncaoDocumentalDTO";
    public static final String VISAO_FUNCAO_API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.tipologia.funcao.TipoDocumentoDTO";
    public static final String VISAO_TIPO_API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.tipologia.tipo.FuncaoDocumentalDTO";
    public static final String VISAO_TIPO_API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.tipologia.tipo.TipoDocumentoDTO";
    public static final String API_MODEL_V1_DOMINIO = "v1.negocio.tipologia.DominioDTO";
    public static final String API_MODEL_V1_OPCAO_DOMINIO = "v1.negocio.tipologia.OpcaoDominioDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO = "atributo_extracao";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String XML_ROOT_ELEMENT_OPCAO_ATRIBUTO = "opcao_atributo";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";
    public static final String XML_ROOT_ELEMENT_TIPOLOGIA_DOCUMENTAL = "tipologia_documental";
    public static final String XML_ROOT_ELEMENT_DOMINIO = "dominio";
    public static final String XML_ROOT_ELEMENT_OPCAO_DOMINIO = "opcao_dominio";

    //************* NOMES DE ATRIBUTOS *************
    //Atributos Comuns
    public static final String ID = "id";
    public static final String NOME = "nome";

    //Atributos Atributo Extração
    public static final String EXPRESSAO_INTERFACE = "expressao_interface";
    public static final String GRUPO_ATRIBUTO = "grupo_atributo";
    public static final String NOME_DOCUMENTO = "nome_documento";
    public static final String NOME_NEGOCIAL = "nome_negocial";
    public static final String OBRIGATORIO = "obrigatorio";
    public static final String OPCAO = "opcao";
    public static final String OPCOES = "opcoes";
    public static final String ORDEM_APRESENTACAO = "ordem_apresentacao";
    public static final String ORIENTACAO_PREENCHIMENTO = "orientacao_preenchimento";
    public static final String PRESENTE_DOCUMENTO = "presente_documento";
    public static final String TIPO_CAMPO = "tipo_campo";

    //Atributos Opcao Atributo
    public static final String CHAVE = "chave";
    public static final String VALOR = "valor";
    
    //Atributos Tipo de Documento
    public static final String ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String ATRIBUTOS_DOCUMENTO = "atributos_documento";
    public static final String AVATAR = "avatar";
    public static final String CODIGO_TIPOLOGIA = "codigo_tipologia";
    public static final String COR_FUNDO = "cor_fundo";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String PERMITE_REUSO = "permite_reuso";
    public static final String PERMITE_EXTRACAO_EXTERNA = "permite_extracao_externa";
    public static final String PERMITE_AVALIACAO_AUTENTICIDADE = "permite_avaliacao_autenciade";
    public static final String TIPO_PESSOA = "tipo_pessoa";

    //Atributos Função Documental / Tipologia
    public static final String TIPOS_DOCUMENTOS = "tipos_documentos";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    
    //Atributos Tipologia
    public static final String TIPOS_DOCUMENTO = "tipos_documento";
    public static final String DOMINIOS = "dominios";

    
    // Dominio
    public static final String DOMINIO = "dominio";
    public static final String MULTIPLOS = "multiplos";
}
