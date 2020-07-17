package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDocumento {

    private ConstantesNegocioDocumento() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO = "v1.negocio.documento.AtributoDocumentoDTO";
    public static final String API_MODEL_V1_CANAL = "v1.negocio.documento.CanalDTO";
    public static final String API_MODEL_V1_DOCUMENTO = "v1.negocio.documento.DocumentoDTO";
    public static final String API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.documento.FuncaoDocumentalDTO";
    public static final String API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.documento.TipoDocumentoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_CANAL = "canal";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";


    //************* NOMES DE ATRIBUTOS *************
    //Atributos Comuns
    public static final String ID = "id";
    public static final String NOME = "nome";

    //Atributos Atributo Documento
    public static final String CHAVE = "chave";
    public static final String VALOR = "valor";
    
    //Atributos Canal
    public static final String CANAL_CAIXA = "canal_caixa";
    public static final String DESCRICAO_CANAL = "descricao_canal";
    public static final String SIGLA_CANAL = "sigla_canal";

    //Atributos Documento
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String ANALISE_OUTSOURCING = "analise_outsourcing";
    public static final String CANAL_CAPTURA = "canal_captura";
    public static final String CODIGO_GED = "codigo_ged";
    public static final String CODIGO_TIPOLOGIA = "codigo_tipologia";
    public static final String BINARIO = "binario";
    public static final String CONTEUDOS = "conteudos";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DATA_HORA_VALIDADE = "data_hora_validade";
    public static final String DOSSIE_DIGITAL = "dossie_digital";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MIMETYPE = "mimetype";
    public static final String MIME_TYPE = "mime_type";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    public static final String PERMITE_REUSO = "permite_reuso";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    

}
