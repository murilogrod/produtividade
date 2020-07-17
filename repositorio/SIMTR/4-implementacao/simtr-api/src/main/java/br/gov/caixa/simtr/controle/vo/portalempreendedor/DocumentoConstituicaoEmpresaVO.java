package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class DocumentoConstituicaoEmpresaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String conteudo;
    private String nome;

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
