package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class ResponsavelVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private String codigoQualificacao;
    private String descricaoQualificacao;
    private String tipo;
    private String ni;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoQualificacao() {
        return codigoQualificacao;
    }

    public void setCodigoQualificacao(String codigoQualificacao) {
        this.codigoQualificacao = codigoQualificacao;
    }

    public String getDescricaoQualificacao() {
        return descricaoQualificacao;
    }

    public void setDescricaoQualificacao(String descricaoQualificacao) {
        this.descricaoQualificacao = descricaoQualificacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNi() {
        return ni;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }
}
