package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@IdClass(UnidadeTratamentoId.class)
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb018_unidade_tratamento")
public class UnidadeTratamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private DossieProduto dossieProduto;
    private Integer unidade;
    // *********************************************

    public UnidadeTratamento() {
        super();
    }

    @Id
    @ManyToOne(targetEntity = DossieProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_produto", nullable = false)
    public DossieProduto getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(DossieProduto dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    @Id
    @Column(name = "nu_unidade", nullable = false)
    public Integer getUnidade() {
        return this.unidade;
    }

    public void setUnidade(Integer nuUnidade) {
        this.unidade = nuUnidade;
    }

    // *********************************************
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
        result = prime * result + ((dossieProduto == null) ? 0 : dossieProduto.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UnidadeTratamento other = (UnidadeTratamento) obj;
        if (unidade == null) {
            if (other.unidade != null) {
                return false;
            }
        } else if (!unidade.equals(other.unidade)) {
            return false;
        }
        if (dossieProduto == null) {
            if (other.dossieProduto != null) {
                return false;
            }
        } else if (!dossieProduto.equals(other.dossieProduto)) {
            return false;
        }
        return true;
    }
}
