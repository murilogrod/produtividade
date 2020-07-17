package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum TipoDispositivoEnum {

    W("Web"),
    L("Low DPI"),
    M("Medium DPI"),
    H("High DPI"),
    X("eXtra DPI");

    private final String descricao;

    private TipoDispositivoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoDispositivoEnum getByCodigo(final String codigo) {
        for (final TipoDispositivoEnum enumTemplate : TipoDispositivoEnum.values()) {
            if (enumTemplate.getDescricao().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Tipo de Dispotivo não identificado: {0}", codigo));
    }
}
