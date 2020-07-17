package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AcaoComparacaoEnum {
    /**
     * I - Inserir novo
       A - Alterar o existente
       E - Excluir o existente
       S - Substituir o existente por um novo
       N - Não fazer qualquer ação para este elemento
     */
    I("I"), A("A"), E("E"), S("S"), N("N");
    
    private String value;

    private AcaoComparacaoEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getIdentificador() {
        return this.value;
    }

    public static AcaoComparacaoEnum fromValue(String text) {
        for (AcaoComparacaoEnum b : AcaoComparacaoEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
