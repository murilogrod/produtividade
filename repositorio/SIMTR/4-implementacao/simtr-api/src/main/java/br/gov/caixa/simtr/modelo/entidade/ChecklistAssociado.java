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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb054_checklist_associado")
public class ChecklistAssociado extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Checklist checklist;
    private DossieProduto dossieProduto;
    private Processo processoFase;
    private InstanciaDocumento instanciaDocumento;
    //********************************
    private Set<Verificacao> verificacoes;

    public ChecklistAssociado() {
        super();
        this.verificacoes = new HashSet<>();
    }

    @Id
    @Column(name = "nu_checklist_associado")
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

    @ManyToOne(targetEntity = InstanciaDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = " nu_instancia_documento", nullable = true)
    public InstanciaDocumento getInstanciaDocumento() {
        return instanciaDocumento;
    }

    public void setInstanciaDocumento(InstanciaDocumento instanciaDocumento) {
        this.instanciaDocumento = instanciaDocumento;
    }
    //******************************************************

    @OneToMany(targetEntity = Verificacao.class, mappedBy = "checklistAssociado", fetch = FetchType.LAZY)
    public Set<Verificacao> getVerificacoes() {
        return verificacoes;
    }

    public void setVerificacoes(Set<Verificacao> verificacoes) {
        this.verificacoes = verificacoes;
    }

    //********************************************************
    public boolean addVerificacoes(Verificacao... verificacoes) {
        return this.verificacoes.addAll(Arrays.asList(verificacoes));
    }

    public boolean removeVerificacoea(Verificacao... verificacoes) {
        return this.verificacoes.removeAll(Arrays.asList(verificacoes));
    }
    //********************************************************

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.checklist);
        hash = 17 * hash + Objects.hashCode(this.dossieProduto);
        hash = 17 * hash + Objects.hashCode(this.processoFase);
        hash = 17 * hash + Objects.hashCode(this.instanciaDocumento);
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
        final ChecklistAssociado other = (ChecklistAssociado) obj;
        if (!Objects.equals(this.checklist, other.checklist)) {
            return false;
        }
        if (!Objects.equals(this.dossieProduto, other.dossieProduto)) {
            return false;
        }
        if (!Objects.equals(this.processoFase, other.processoFase)) {
            return false;
        }
        
        return Objects.equals(this.instanciaDocumento, other.instanciaDocumento);
    }

}
