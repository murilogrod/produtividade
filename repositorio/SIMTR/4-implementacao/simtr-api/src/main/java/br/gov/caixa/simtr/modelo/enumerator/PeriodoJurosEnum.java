package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum PeriodoJurosEnum {

    D("Diario"),
    M("Mensal"),
    A("Anual");

    private final String descricao;

    private PeriodoJurosEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static PeriodoJurosEnum getByCodigo(final String codigo) {
        for (final PeriodoJurosEnum enumTemplate : PeriodoJurosEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Periodo de Juros não conhecido: {0}", codigo));
    }
}
