package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SiricIndicadorConvenioEnum {
    
    NAO("0"), SIM("1");

    private String value;

    SiricIndicadorConvenioEnum(String value) {
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

    public static SiricIndicadorConvenioEnum fromValue(String text) {
        for (SiricIndicadorConvenioEnum b : SiricIndicadorConvenioEnum.values()) {
            if (b.value.equals(text)) {
                return b;
            }
        }
        return null;
    }
}
