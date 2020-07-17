/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

/**
 *
 * @author c090347
 */
public enum ModoPartilhaEnum {

    /**
     * Indica que a partilha deve ser feita encaminhando a "sobra" da informação
     * para o outro campo definido. Ou seja, a diferença do valor localizado na
     * comparação com a informação desejada.
     */
    S("SOBRA"),
    /**
     * Indica que a partilha deve ser feita encaminhando a o texto localizado na
     * informação para o outro campo definido. Ou seja, a o valor localizado na
     * comparação com a informação desejada.
     */
    V("VALOR");

    private final String descricao;

    private ModoPartilhaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ModoPartilhaEnum getByCodigo(final String codigo) {
        for (final ModoPartilhaEnum enumTemplate : ModoPartilhaEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Modo de partilha não conhecido: {0}", codigo));
    }
}
