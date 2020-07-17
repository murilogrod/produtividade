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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb016_processo_fase_dossie")
public class ProcessoFaseDossie extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DossieProduto dossieProduto;
    private Processo processoFase;
    private Calendar dataHoraInclusao;
    private Calendar dataHoraSaida;
    private String responsavel;
    private Integer unidade;

    public ProcessoFaseDossie() {
        super();
        this.dataHoraInclusao = Calendar.getInstance();
    }

    @Id
    @Column(name = "nu_processo_fase_dossie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = DossieProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_produto", nullable = false)
    public DossieProduto getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(DossieProduto dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_fase", nullable = false)
    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_inclusao", nullable = false)
    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_saida", nullable = true)
    public Calendar getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(Calendar dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
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
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.processoFase);
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
        final ProcessoFaseDossie other = (ProcessoFaseDossie) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.processoFase, other.processoFase);
    }

}
