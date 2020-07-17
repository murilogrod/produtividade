/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.enumerator;

/**
 *
 * @author c090347
 */
public enum SICPFCampoEnum {

    CPF("CPF"),
    NASCIMENTO("Data de Nascimento"),
    NOME("Nome do Contribuinte"),
    NOME_MAE("Nome da Mãe do Contribuinte"),
    ELEITOR("Titulo de Eleitor");

    private final String descricao;

    private SICPFCampoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
