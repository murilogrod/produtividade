package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class ConjugeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cpf;
    private String nome;
    private String dataNascimento;
    private boolean possuiCpf;

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

    public boolean isPossuiCpf() {
        return possuiCpf;
    }

    public void setPossuiCpf(boolean possuiCpf) {
        this.possuiCpf = possuiCpf;
    }
}
