package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Calendar;
import javax.persistence.Index;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb053_vinculacao_checklist", indexes = {
    @Index(name = "ix_mtrtb053_01", unique = true, columnList = "nu_processo_dossie,nu_processo_fase,nu_tipo_documento,dt_revogacao"),
    @Index(name = "ix_mtrtb053_02", unique = true, columnList = "nu_processo_dossie,nu_processo_fase,nu_funcao_documental,dt_revogacao"),
    @Index(name = "ix_mtrtb053_03", unique = true, columnList = "nu_processo_dossie,nu_processo_fase,nu_checklist,dt_revogacao")
})
public class VinculacaoChecklist extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Checklist checklist;
    private Processo processoDossie;
    private Processo processoFase;
    private TipoDocumento tipoDocumento;
    private FuncaoDocumental funcaoDocumental;
    private Calendar dataRevogacao;

    // **********************************
    public VinculacaoChecklist() {
        super();
    }

    @Id
    @Column(name = "nu_vinculacao_checklist")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Checklist.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_checklist", nullable = false)
    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_dossie", nullable = false)
    public Processo getProcessoDossie() {
        return processoDossie;
    }

    public void setProcessoDossie(Processo processoDossie) {
        this.processoDossie = processoDossie;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_fase", nullable = false)
    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = true)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(targetEntity = FuncaoDocumental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_funcao_documental", nullable = true)
    public FuncaoDocumental getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumental funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_revogacao", nullable = false)
    public Calendar getDataRevogacao() {
        return dataRevogacao;
    }

    public void setDataRevogacao(Calendar dataRevogacao) {
        this.dataRevogacao = dataRevogacao;
    }

    // **********************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.checklist);
        hash = 41 * hash + Objects.hashCode(this.processoDossie);
        hash = 41 * hash + Objects.hashCode(this.processoFase);
        hash = 41 * hash + Objects.hashCode(this.tipoDocumento);
        hash = 41 * hash + Objects.hashCode(this.funcaoDocumental);
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
        final VinculacaoChecklist other = (VinculacaoChecklist) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.checklist, other.checklist)) {
            return false;
        }
        if (!Objects.equals(this.processoDossie, other.processoDossie)) {
            return false;
        }
        if (!Objects.equals(this.processoFase, other.processoFase)) {
            return false;
        }
        if (!Objects.equals(this.tipoDocumento, other.tipoDocumento)) {
            return false;
        }
        return Objects.equals(this.funcaoDocumental, other.funcaoDocumental);
    }
}
