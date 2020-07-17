package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Objects;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb017_stco_instnca_documento")
public class SituacaoInstanciaDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private InstanciaDocumento instanciaDocumento;
    private SituacaoDocumento situacaoDocumento;
    private Calendar dataHoraInclusao;
    private String responsavel;
    private Integer unidade;

    public SituacaoInstanciaDocumento() {
        super();
        this.dataHoraInclusao = Calendar.getInstance();
    }

    @Id
    @Column(name = "nu_stco_instnca_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = InstanciaDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_instancia_documento", nullable = false)
    public InstanciaDocumento getInstanciaDocumento() {
        return instanciaDocumento;
    }

    public void setInstanciaDocumento(InstanciaDocumento instanciaDocumento) {
        this.instanciaDocumento = instanciaDocumento;
    }

    @ManyToOne(targetEntity = SituacaoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_situacao_documento", nullable = false)
    public SituacaoDocumento getSituacaoDocumento() {
        return situacaoDocumento;
    }

    public void setSituacaoDocumento(SituacaoDocumento situacaoDocumento) {
        this.situacaoDocumento = situacaoDocumento;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_inclusao", nullable = false)
    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    @Column(name = "co_responsavel", nullable = false, length = 20)
    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Column(name = "nu_unidade", nullable = false)
    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    //*****************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.instanciaDocumento);
        hash = 83 * hash + Objects.hashCode(this.situacaoDocumento);
        hash = 83 * hash + Objects.hashCode(this.unidade);
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
        final SituacaoInstanciaDocumento other = (SituacaoInstanciaDocumento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.instanciaDocumento, other.instanciaDocumento)) {
            return false;
        }
        if (!Objects.equals(this.situacaoDocumento, other.situacaoDocumento)) {
            return false;
        }
        return Objects.equals(this.unidade, other.unidade);
    }
}
