package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author f525904
 *
 */

public enum TipoEventoEnum {

    RECEBIDA("proposta.recebida"),
    PESQUISA_CONTIGENCIADA("proposta.pesquisa.contigenciada"),
    AVALIACAO_ERRO("proposta.avaliacao.erro"),
    CONCLUIDA("proposta.concluida");

    private String value;

    TipoEventoEnum(String value) {
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

    public static TipoEventoEnum fromValue(String text) {
        for (TipoEventoEnum b : TipoEventoEnum.values()) {
            if (b.value.equals(text)) {
                return b;
            }
        }
        return null;
    }

}
