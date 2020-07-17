/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.vo.autorizacao;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import java.io.Serializable;

/**
 *
 * @author c090347
 */
public class AutorizacaoDocumentoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Documento documento;
    private String finalidade;
    private String link;

    public AutorizacaoDocumentoVO() {
        super();
    }

    public AutorizacaoDocumentoVO(Documento documento, String finalidade, String link) {
        this.documento = documento;
        this.finalidade = finalidade;
        this.link = link;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
