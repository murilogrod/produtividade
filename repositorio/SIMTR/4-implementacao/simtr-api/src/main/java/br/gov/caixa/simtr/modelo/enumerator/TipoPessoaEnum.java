package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum TipoPessoaEnum {

    F("Física"), J("Jurídica"), A("Ambos");

    private final String descricao;

    private TipoPessoaEnum(final String description) {
        this.descricao = description;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TipoPessoaEnum get(final String id) {
        for (final TipoPessoaEnum item : TipoPessoaEnum.values()) {
            if (item.name().equals(id)) {
                return item;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Tipo de pessoa não encontrada: {0}", id));
    }
}
