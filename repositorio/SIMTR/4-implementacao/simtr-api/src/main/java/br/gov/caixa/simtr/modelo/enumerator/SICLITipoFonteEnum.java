package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum SICLITipoFonteEnum {

    D("Documento"),
    F("Formulário");

    private final String descricao;

    private SICLITipoFonteEnum(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static SICLITipoFonteEnum get(final String id) {
        for (final SICLITipoFonteEnum item : SICLITipoFonteEnum.values()) {
            if (item.name().equals(id)) {
                return item;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Tipo Fonte não encontrada: {0}", id));
    }
}
