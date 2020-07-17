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
public final class ConstantesRetaguardaExtracaoDados {

    private ConstantesRetaguardaExtracaoDados() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_ATRIBUTO_DOCUMENTO = "v1.retaguarda.extracaodados.AtributoDocumentoDTO";
    public static final String API_MODEL_ATRIBUTO_DOCUMENTO_RESULTADO = "v1.retaguarda.extracaodados.AtributoDocumentoResultadoDTO";
    public static final String API_MODEL_DOCUMENTO = "v1.retaguarda.extracaodados.DocumentoDTO";
    public static final String API_MODEL_PENDENCIA_EXTRACAO = "v1.retaguarda.extracaodados.PendenciaExtracaoDTO";
    public static final String API_MODEL_RESULTADO_AVALIACAO_EXTRACAO = "v1.retaguarda.extracaodados.ResultadoAvaliacaoExtracaoDTO";
    public static final String API_MODEL_TIPO_DOCUMENTO = "v1.retaguarda.extracaodados.TipoDocumentoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo-documento";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO_RESULTADO = "atributo-documento-resultado";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_PENDENCIA_EXTRACAO = "pendencia_extracao";
    public static final String XML_ROOT_ELEMENT_RESULTADO_EXTRACAO = "resultado-extracao";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";

    //************* NOMES DE ATRIBUTOS *************
    //Atributos do Serviço de Avaliação/Extração
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String BINARIO = "binario";
    public static final String CHAVE = "chave";
    public static final String CODIGO_CONTROLE = "codigo_controle";
    public static final String CODIGO_REJEICAO = "codigo_rejeicao";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DATA_HORA_ENVIO_EXTRACAO = "data_hora_envio_extracao";
    public static final String DATA_HORA_RETORNO_EXTRACAO = "data_hora_retorno_extracao";
    public static final String EXECUTA_CLASSIFICACAO = "executa_classificacao";
    public static final String IDENTIFICADOR_DOCUMENTO = "identificador_documento";
    public static final String ID = "id";
    public static final String ID_TIPO_DOCUMENTO = "id_tipo_documento";
    public static final String INDICE_ASSERTIVIDADE = "indice_assertividade";
    public static final String JANELA_TEMPORAL = "janela_temporal";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MIMETYPE = "mimetype";
    public static final String NOME = "nome";
    public static final String NOME_TIPO_DOCUMENTO = "nome_tipo_documento";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String QUANTIDADE = "quantidade";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALOR = "valor";
}
