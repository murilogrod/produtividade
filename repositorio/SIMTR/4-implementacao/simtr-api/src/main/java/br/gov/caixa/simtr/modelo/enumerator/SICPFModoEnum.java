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
public enum SICPFModoEnum {

    /**
     * Indica que a comparação deve ser exata por igualdade.
     */
    E("EXATO"),
    /**
     * Indica que a comparação deve por parte do texto localizado. Utilizado em
     * situações onde a fliação possui os nomes de pai e mão conjuntamente e
     * precisa capturar apenas o da mãe
     */
    P("PARCIAL");

    private final String descricao;

    private SICPFModoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static SICPFModoEnum getByCodigo(final String codigo) {
        for (final SICPFModoEnum enumTemplate : SICPFModoEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Modo de comparação não conhecido: {0}", codigo));
    }
}
