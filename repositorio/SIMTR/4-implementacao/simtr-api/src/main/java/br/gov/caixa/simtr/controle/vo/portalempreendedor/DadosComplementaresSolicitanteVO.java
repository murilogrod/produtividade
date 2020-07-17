package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class DadosComplementaresSolicitanteVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private ConjugeVO conjuge;
    private String cpf;
    private String nome;
    private String dataNascimento;
    private IdentificacaoVO identificacao;
    private String estadoCivil;
    private String sexo;
    private String escolaridade;
    private String nacionalidade;
    
    public ConjugeVO getConjuge() {
        return conjuge;
    }

    public void setConjuge(ConjugeVO conjuge) {
        this.conjuge = conjuge;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public IdentificacaoVO getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(IdentificacaoVO identificacao) {
        this.identificacao = identificacao;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }
    
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
}
