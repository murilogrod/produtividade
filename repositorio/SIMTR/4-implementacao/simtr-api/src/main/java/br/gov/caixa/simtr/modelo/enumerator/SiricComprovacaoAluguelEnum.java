package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author f525904
 *
 */

public enum SiricComprovacaoAluguelEnum {
    
    NAO("0"), SIM("1");

    private String value;

    SiricComprovacaoAluguelEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static SiricComprovacaoAluguelEnum fromValue(String text) {
        for (SiricComprovacaoAluguelEnum b : SiricComprovacaoAluguelEnum.values()) {
            if (b.value.equals(text)) {
                return b;
            }
        }
        return null;
    }
}
