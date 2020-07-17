/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.vo.autorizacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;

/**
 *
 * @author c090347
 */
public class AutorizacaoVO {

    private Autorizacao autorizacao;
    private SipesResponseDTO resultadoPesquisaSIPES;
    private List<AutorizacaoDocumentoVO> documentosUtilizados;
    private List<MensagemOrientacaoVO> mensagensOrientacoes;

    public AutorizacaoVO() {
        super();
        this.documentosUtilizados = new ArrayList<>();
        this.mensagensOrientacoes = new ArrayList<>();
    }

    public AutorizacaoVO(Autorizacao autorizacao, List<AutorizacaoDocumentoVO> documentosUtilizados) {
        this();
        this.autorizacao = autorizacao;
        this.documentosUtilizados = documentosUtilizados;
    }

    public AutorizacaoVO(Autorizacao autorizacao, List<AutorizacaoDocumentoVO> documentosUtilizados, List<MensagemOrientacaoVO> mensagensOrientacoes) {
        this();
        this.autorizacao = autorizacao;
        this.documentosUtilizados = documentosUtilizados;
        this.mensagensOrientacoes = mensagensOrientacoes;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public SipesResponseDTO getResultadoPesquisaSIPES() {
        return resultadoPesquisaSIPES;
    }

    public void setResultadoPesquisaSIPES(SipesResponseDTO resultadoPesquisaSIPES) {
        this.resultadoPesquisaSIPES = resultadoPesquisaSIPES;
    }

    public List<AutorizacaoDocumentoVO> getDocumentosUtilizados() {
        return documentosUtilizados;
    }

    public void setDocumentosUtilizados(List<AutorizacaoDocumentoVO> documentosUtilizados) {
        this.documentosUtilizados = documentosUtilizados;
    }

    public List<MensagemOrientacaoVO> getMensagensOrientacoes() {
        return mensagensOrientacoes;
    }

    public void setMensagensOrientacoes(List<MensagemOrientacaoVO> mensagensOrientacoes) {
        this.mensagensOrientacoes = mensagensOrientacoes;
    }

    //*************************************
    public void addDocumentosUtilizados(AutorizacaoDocumentoVO... documentosUtilizados) {
        this.documentosUtilizados.addAll(Arrays.asList(documentosUtilizados));
    }

    public void addMensagensOrientacoes(MensagemOrientacaoVO... mensagensOrientacoes) {
        this.mensagensOrientacoes.addAll(Arrays.asList(mensagensOrientacoes));
    }

}
