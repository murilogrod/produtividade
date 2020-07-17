/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.vo;

import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;

/**
 *
 * @author c090347
 */
public class AtributoComplementarRelatorioVO {

    private String chave;
    private String valor;
    private TipoAtributoEnum tipoAtributoEnum;

    public AtributoComplementarRelatorioVO(String chave, String valor, TipoAtributoEnum tipoAtributoEnum) {
        this.chave = chave;
        this.valor = valor;
        this.tipoAtributoEnum = tipoAtributoEnum;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TipoAtributoEnum getTipoAtributoEnum() {
        return tipoAtributoEnum;
    }

    public void setTipoAtributoEnum(TipoAtributoEnum tipoAtributoEnum) {
        this.tipoAtributoEnum = tipoAtributoEnum;
    }

}
