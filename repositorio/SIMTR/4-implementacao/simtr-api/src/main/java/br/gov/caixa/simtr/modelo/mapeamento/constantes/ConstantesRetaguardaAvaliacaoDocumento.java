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
public final class ConstantesRetaguardaAvaliacaoDocumento {

    private ConstantesRetaguardaAvaliacaoDocumento() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO = "v1.retaguarda.outsourcing.AtributoDocumentoDTO";
    public static final String API_MODEL_V1_DADO_BASE = "v1.retaguarda.outsourcing.DadoBaseDTO";
    public static final String API_MODEL_V1_RESULTADO_AVALIACAO_EXTRACAO = "v1.retaguarda.outsourcing.ResultadoAvaliacaoExtracaoDTO";
    public static final String API_MODEL_V1_RETORNO_AVALIACAO_EXTRACAO = "v1.retaguarda.outsourcing.RetornoAvaliacaoExtracaoDTO";
    public static final String API_MODEL_V1_SOLICITACAO_AVALIACAO_EXTRACAO = "v1.retaguarda.outsourcing.SolicitacaoAvaliacaoExtracaoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo-documento";
    public static final String XML_ROOT_ELEMENT_DADO_BASE = "dado-base";
    public static final String XML_ROOT_ELEMENT_RESULTADO_AVALIACAO_EXTRACAO = "resultado-avaliacao-extracao";
    public static final String XML_ROOT_ELEMENT_RETORNO_AVALIACAO_EXTRACAO = "retorno-avaliacao-extracao";
    public static final String XML_ROOT_ELEMENT_SOLICITACAO_AVALIACAO_EXTRACAO = "solicitacao-avaliacao-extracao";

    //************* NOMES DE ATRIBUTOS *************
    
    //Atributos do Serviço de Avaliação/Extração
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String BINARIO = "binario";
    public static final String BINARIOS = "binarios";
    public static final String BINARIOS_COMPLEMENTARES = "binarios_complementares";
    public static final String CHAVE = "chave";
    public static final String CODIGO_REJEICAO = "codigo_rejeicao";
    public static final String DADOS = "dados";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DATA_HORA_ENVIO_AUTENTICIDADE = "data_hora_envio_autenticidade";
    public static final String DATA_HORA_ENVIO_EXTRACAO = "data_hora_envio_extracao";
    public static final String DATA_HORA_RETORNO_AUTENTICIDADE = "data_hora_retorno_autenticidade";
    public static final String DATA_HORA_RETORNO_EXTRACAO = "data_hora_retorno_extracao";
    public static final String DESCRICAO_REJEICAO = "descricao_rejeicao";
    public static final String EXECUTA_AUTENTICIDADE = "executa_autenticidade";
    public static final String EXECUTA_EXTRACAO = "executa_extracao";
    public static final String IDENTIFICADOR_DOCUMENTO = "identificador_documento";
    public static final String INDICE_ASSERTIVIDADE = "indice_assertividade";
    public static final String INDICE_AVALIACAO_AUTENTICIDADE = "indice_avaliacao_autenticidade";
    public static final String JANELA_TEMPORAL = "janela_temporal";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MIMETYPE = "mimetype";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String PROCESSO = "processo";
    public static final String SELFIES = "selfies";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALOR = "valor";

}
