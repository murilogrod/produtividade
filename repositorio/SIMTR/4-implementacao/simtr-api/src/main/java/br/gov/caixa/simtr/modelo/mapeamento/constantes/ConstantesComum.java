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
public final class ConstantesComum {

    private ConstantesComum() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_RETORNO_ERRO = "v1.comum.RetornoErroDTO";

    //************* XML Root *************
    public static final String XML_ROOT_RETORNO_ERRO = "retorno_erro";
        
    //************* NOMES DE ATRIBUTOS *************
    public static final String CODIGO_HTTP = "codigo_http";
    public static final String DETALHE = "detalhe";
    public static final String MENSAGEM = "mensagem";
    public static final String STACKTRACE = "stacktrace";


}
