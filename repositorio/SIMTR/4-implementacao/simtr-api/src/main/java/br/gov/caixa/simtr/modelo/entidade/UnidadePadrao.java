package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb058_unidade_padrao", indexes = {
    @Index(name = "ix_mtrtb058_01", unique = true, columnList = "nu_processo, nu_etapa_bpm, nu_unidade")
})
public class UnidadePadrao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Processo processo;
    private Integer etapaBPM;
    private Integer unidade;
    //*************************************

    public UnidadePadrao() {
        super();
    }

    @Id
    @Column(name = "nu_unidade_padrao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo", nullable = false)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @Column(name = "nu_etapa_bpm", nullable = false)
    public Integer getEtapaBPM() {
        return etapaBPM;
    }

    public void setEtapaBPM(Integer etapaBPM) {
        this.etapaBPM = etapaBPM;
    }

    @Column(name = "nu_unidade", nullable = false)
    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final UnidadePadrao other = (UnidadePadrao) obj;
        return Objects.equals(this.id, other.id);
    }
}
