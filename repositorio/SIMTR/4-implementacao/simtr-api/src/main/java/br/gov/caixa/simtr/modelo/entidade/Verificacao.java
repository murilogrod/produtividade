package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb055_verificacao")
public class Verificacao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ChecklistAssociado checklistAssociado;
    private Calendar dataHoraVerificacao;
    private String matriculaOperador;
    private Integer unidadeOperador;
    private Boolean indicacaoRealizacao;
    private Boolean indicacaoVerificacao;
    private Calendar dataHoraContestacao;
    private String matriculaContestacao;
    private Calendar dataHoraRevisao;
    private String matriculaRevisao;
    private Integer unidadeRevisao;
    private Boolean indicacaoRevisao;
    // *********************************************
    private Set<Parecer> pareceres;

    public Verificacao() {
        super();
        this.pareceres = new HashSet<>();
    }

    @Id
    @Column(name = "nu_verificacao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ChecklistAssociado.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_checklist_associado", nullable = true)
    public ChecklistAssociado getChecklistAssociado() {
        return checklistAssociado;
    }

    public void setChecklistAssociado(ChecklistAssociado checklistAssociado) {
        this.checklistAssociado = checklistAssociado;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_verificacao", nullable = false)
    public Calendar getDataHoraVerificacao() {
        return dataHoraVerificacao;
    }

    public void setDataHoraVerificacao(Calendar dataHoraVerificacao) {
        this.dataHoraVerificacao = dataHoraVerificacao;
    }

    @Column(name = "co_matricula", length = 7, nullable = false)
    public String getMatriculaOperador() {
        return matriculaOperador;
    }

    public void setMatriculaOperador(String matriculaOperador) {
        this.matriculaOperador = matriculaOperador;
    }

    @Column(name = "nu_unidade", nullable = false)
    public Integer getUnidadeOperador() {
        return unidadeOperador;
    }

    public void setUnidadeOperador(Integer unidadeOperador) {
        this.unidadeOperador = unidadeOperador;
    }

    @Column(name = "ic_verificacao_realizada", nullable = false)
    public Boolean getIndicacaoRealizacao() {
        return indicacaoRealizacao;
    }

    public void setIndicacaoRealizacao(Boolean indicacaoRealizacao) {
        this.indicacaoRealizacao = indicacaoRealizacao;
    }

    @Column(name = "ic_verificacao_aprovada", nullable = false)
    public Boolean getIndicacaoVerificacao() {
        return indicacaoVerificacao;
    }

    public void setIndicacaoVerificacao(Boolean indicacaoVerificacao) {
        this.indicacaoVerificacao = indicacaoVerificacao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_contestacao", nullable = true)
    public Calendar getDataHoraContestacao() {
        return dataHoraContestacao;
    }

    public void setDataHoraContestacao(Calendar dataHoraContestacao) {
        this.dataHoraContestacao = dataHoraContestacao;
    }

    @Column(name = "co_matricula_contestacao", length = 7, nullable = true)
    public String getMatriculaContestacao() {
        return matriculaContestacao;
    }

    public void setMatriculaContestacao(String matriculaContestacao) {
        this.matriculaContestacao = matriculaContestacao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_revisao", nullable = true)
    public Calendar getDataHoraRevisao() {
        return dataHoraRevisao;
    }

    public void setDataHoraRevisao(Calendar dataHoraRevisao) {
        this.dataHoraRevisao = dataHoraRevisao;
    }

    @Column(name = "co_matricula_revisao", length = 7, nullable = true)
    public String getMatriculaRevisao() {
        return matriculaRevisao;
    }

    public void setMatriculaRevisao(String matriculaRevisao) {
        this.matriculaRevisao = matriculaRevisao;
    }

    @Column(name = "nu_unidade_revisao", nullable = false)
    public Integer getUnidadeRevisao() {
        return unidadeRevisao;
    }

    public void setUnidadeRevisao(Integer unidadeRevisao) {
        this.unidadeRevisao = unidadeRevisao;
    }

    @Column(name = "ic_revisao_aprovada", nullable = false)
    public Boolean getIndicacaoRevisao() {
        return indicacaoRevisao;
    }

    public void setIndicacaoRevisao(Boolean indicacaoRevisao) {
        this.indicacaoRevisao = indicacaoRevisao;
    }

    // ***********************************
    @OneToMany(targetEntity = Parecer.class, mappedBy = "verificacao", fetch = FetchType.LAZY)
    public Set<Parecer> getPareceres() {
        return pareceres;
    }

    public void setPareceres(Set<Parecer> pareceres) {
        this.pareceres = pareceres;
    }

    // ************************************
    public boolean addPareceres(Parecer... pareceres) {
        return this.pareceres.addAll(Arrays.asList(pareceres));
    }

    public boolean removePareceres(Parecer... pareceres) {
        return this.pareceres.removeAll(Arrays.asList(pareceres));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.checklistAssociado);
        hash = 41 * hash + Objects.hashCode(this.dataHoraVerificacao);
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
        final Verificacao other = (Verificacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.checklistAssociado, other.checklistAssociado)) {
            return false;
        }
        return Objects.equals(this.dataHoraVerificacao, other.dataHoraVerificacao);
    }
}
