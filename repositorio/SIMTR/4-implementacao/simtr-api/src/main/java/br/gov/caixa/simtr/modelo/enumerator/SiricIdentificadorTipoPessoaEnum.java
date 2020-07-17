package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author f525904
 *
 */
public enum SiricIdentificadorTipoPessoaEnum {
    
    CPF("1"), CNPJ("2");

    private String value;

    private SiricIdentificadorTipoPessoaEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getIdentificador() {
        return this.value;
    }

    public static SiricIdentificadorTipoPessoaEnum fromValue(String text) {
        for (SiricIdentificadorTipoPessoaEnum b : SiricIdentificadorTipoPessoaEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
