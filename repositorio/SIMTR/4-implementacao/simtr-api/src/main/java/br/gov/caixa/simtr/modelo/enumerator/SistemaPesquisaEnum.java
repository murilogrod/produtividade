package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum SistemaPesquisaEnum {

    SICPF,
    CADIN,
    SICCF,
    RECEITA,
    SERASA,
    SICOW,
    SINAD,
    SPC;

    public static SistemaPesquisaEnum getBySigla(final String sigla) {
        for (final SistemaPesquisaEnum enumTemplate : SistemaPesquisaEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(sigla)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Sistema de Pesquisa não conhecido: {0}", sigla));
    }
}
