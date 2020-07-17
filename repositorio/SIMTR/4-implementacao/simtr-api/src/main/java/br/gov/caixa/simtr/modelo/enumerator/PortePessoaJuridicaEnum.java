package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum PortePessoaJuridicaEnum {

    ND(-1, "NAO DEFINIDO"),
    PORTE0(0, "PORTE 0"),
    ME(1, "MICROEMPRESA"),
    MEI(2, "MICRO EMPREENDEDOR INDIVIDUAL"),
    EPP(3, "EMPRESA DE PEQUENO PORTE"),
    DEMAIS(5, "DEMAIS");

    private final int codigo;
    private final String descricao;

    private PortePessoaJuridicaEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static PortePessoaJuridicaEnum getByCodigo(int codigo) {
        for (final PortePessoaJuridicaEnum enumTemplate : PortePessoaJuridicaEnum.values()) {
            if (enumTemplate.codigo == codigo) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Porte não identificador pelo codigo: {0}", codigo));
    }

    public static PortePessoaJuridicaEnum getBySigla(String sigla) {
        for (final PortePessoaJuridicaEnum enumTemplate : PortePessoaJuridicaEnum.values()) {
            if (enumTemplate.name().equals(sigla)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Porte não identificador pela sigla: {0}", sigla));
    }

}
