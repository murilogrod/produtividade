package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author f525904
 *
 */
public enum CodigoSituacaoEnum {

    EM_PROCESSAMENTO("0"),
    CONCLUIDO("1");

    private String value;

    CodigoSituacaoEnum(String value) {
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

    public static CodigoSituacaoEnum fromValue(String text) {
        for (CodigoSituacaoEnum b : CodigoSituacaoEnum.values()) {
            if (b.value.equals(text)) {
                return b;
            }
        }
        return null;
    }
}
