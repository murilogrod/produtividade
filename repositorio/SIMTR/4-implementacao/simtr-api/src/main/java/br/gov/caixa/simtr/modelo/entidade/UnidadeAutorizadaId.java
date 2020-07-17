package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

public class UnidadeAutorizadaId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer processo;
    private Integer unidade;

    public UnidadeAutorizadaId() {
        super();
    }

    public UnidadeAutorizadaId(Integer processo, Integer unidade) {
        super();
        this.processo = processo;
        this.unidade = unidade;
    }

    public Integer getProcesso() {
        return processo;
    }

    public void setProcesso(Integer processo) {
        this.processo = processo;
    }

    public Integer getUnidade() {
        return this.unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    // *************************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.processo);
        hash = 37 * hash + Objects.hashCode(this.unidade);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnidadeAutorizadaId other = (UnidadeAutorizadaId) obj;
        if (!Objects.equals(this.processo, other.processo)) {
            return false;
        }
        return Objects.equals(this.unidade, other.unidade);
    }
}
