package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum CanalCaixaEnum {

    AGE("Agência Fisica"),
    AGD("Agência Digital"),
    AUT("Sistemas de Automação Internos CAIXA"),
    CCA("Correspondente CAIXA Aqui");

    private final String descricao;

    private CanalCaixaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CanalCaixaEnum getCanalCaixaByCodigo(final String codigo) {
        for (final CanalCaixaEnum enumTemplate : CanalCaixaEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Canal CAIXA não conhecido: {0}", codigo));
    }
}
