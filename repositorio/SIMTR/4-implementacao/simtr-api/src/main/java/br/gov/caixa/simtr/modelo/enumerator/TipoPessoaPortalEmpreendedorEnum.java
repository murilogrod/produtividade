package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum TipoPessoaPortalEmpreendedorEnum {

    F("PF"), J("PJ");

    private final String descricao;

    private TipoPessoaPortalEmpreendedorEnum(final String description) {
        this.descricao = description;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TipoPessoaPortalEmpreendedorEnum get(final String id) {
        for (final TipoPessoaPortalEmpreendedorEnum item : TipoPessoaPortalEmpreendedorEnum.values()) {
            if (item.name().equals(id)) {
                return item;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Tipo de pessoa não encontrada: {0}", id));
    }
}