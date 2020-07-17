package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class IdentificacaoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numero;
    private String orgaoEmissor;
    private String ufOrgaoEmissor;
    
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }
    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }
    public String getUfOrgaoEmissor() {
        return ufOrgaoEmissor;
    }
    public void setUfOrgaoEmissor(String ufOrgaoEmissor) {
        this.ufOrgaoEmissor = ufOrgaoEmissor;
    }
}
