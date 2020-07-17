/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.vo.autorizacao;

import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;

/**
 *
 * @author c090347
 */
public class MensagemOrientacaoVO {

    private SistemaPesquisaEnum sistemaPesquisa;
    private String grupoOcorrencia;
    private String mensagemOrientacao;

    public MensagemOrientacaoVO() {
        super();
    }

    public MensagemOrientacaoVO(SistemaPesquisaEnum sistemaPesquisa, String grupoOcorrencia, String mensagemOrientacao) {
        this();
        this.sistemaPesquisa = sistemaPesquisa;
        this.grupoOcorrencia = grupoOcorrencia;
        this.mensagemOrientacao = mensagemOrientacao;
    }

    public SistemaPesquisaEnum getSistemaPesquisa() {
        return sistemaPesquisa;
    }

    public void setSistemaPesquisa(SistemaPesquisaEnum sistemaPesquisa) {
        this.sistemaPesquisa = sistemaPesquisa;
    }

    public String getGrupoOcorrencia() {
        return grupoOcorrencia;
    }

    public void setGrupoOcorrencia(String grupoOcorrencia) {
        this.grupoOcorrencia = grupoOcorrencia;
    }

    public String getMensagemOrientacao() {
        return mensagemOrientacao;
    }

    public void setMensagemOrientacao(String mensagemOrientacao) {
        this.mensagemOrientacao = mensagemOrientacao;
    }

}
