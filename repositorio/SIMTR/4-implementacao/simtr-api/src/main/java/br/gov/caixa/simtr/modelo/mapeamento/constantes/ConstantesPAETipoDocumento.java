/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesPAETipoDocumento {

    private ConstantesPAETipoDocumento() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_ATRIBUTO_EXTRACAO = "v1.pae.tipologia.AtributoExtracaoDTO";
    public static final String API_MODEL_TIPO_DOCUMENTO = "v1.pae.tipologia.TipoDocumentoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO = "atributo_extracao";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";

    //************* NOMES DE ATRIBUTOS *************
    public static final String ATRIBUTO_EXTRACAO = "atributo_extracao";
    public static final String ATRIBUTOS_EXTRACAO = "atributos_extracao";
    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String NOME_INTEGRACAO = "nome_integracao";
    public static final String NOME_NEGOCIAL = "nome_negocial";
    public static final String OBRIGATORIO = "obrigatorio";
    public static final String TIPO_CAMPO = "tipo_campo";
    public static final String TAGS = "tags";
}
