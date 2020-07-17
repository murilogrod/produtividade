package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import javax.persistence.IdClass;

@Entity
@IdClass(UnidadeAutorizadaId.class)
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb021_unidade_autorizada")
public class UnidadeAutorizada implements Serializable {

    private static final long serialVersionUID = 1L;

    private Processo processo;
    private Integer unidade;
    // *********************************************

    public UnidadeAutorizada() {
        super();
    }

    @Id
    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo", nullable = true)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @Id
    @Column(name = "nu_unidade", nullable = false)
    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    // *********************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.processo);
        hash = 71 * hash + Objects.hashCode(this.unidade);
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
        final UnidadeAutorizada other = (UnidadeAutorizada) obj;
        if (!Objects.equals(this.processo, other.processo)) {
            return false;
        }
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        return true;
    }
}
