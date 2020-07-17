package br.gov.caixa.simtr.modelo.mapeamento.constantes;

import java.io.Serializable;


public class ConstantesConsultaFormasApresentacao implements Serializable {
    private static final long serialVersionUID = 1L;
    
 // ************* API MODEL *************
    // Identificadores de objetos na visão das operações de cadastro interno a plataforma
    public static final String API_MODEL_FORMAS_APRESENTACAO = "cadastro.campoformulario.FormasApresentacaoDTO";
    

    // ************* XML Root *************
    public static final String XML_ROOT_FORMAS_APRESENTACAO = "formas_apresentacao";

    // ************* NOMES DE ATRIBUTOS *************
    public static final String TIPO_DISPOSITIVO = "tipo_dispositivo";
    public static final String LARGURA = "largura";

}
