package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

public class UnidadeTratamentoId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long dossieProduto;
    private Integer unidade;

    public UnidadeTratamentoId() {
        super();
    }

    public UnidadeTratamentoId(Long dossieProduto, Integer nuUnidade) {
        super();
        this.dossieProduto = dossieProduto;
        this.unidade = nuUnidade;
    }

    public Long getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(Long dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    public Integer getUnidade() {
        return this.unidade;
    }

    public void setUnidade(Integer nuUnidade) {
        this.unidade = nuUnidade;
    }

    // *************************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.dossieProduto);
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
        final UnidadeTratamentoId other = (UnidadeTratamentoId) obj;
        if (!Objects.equals(this.dossieProduto, other.dossieProduto)) {
            return false;
        }
        return Objects.equals(this.unidade, other.unidade);
    }
}
