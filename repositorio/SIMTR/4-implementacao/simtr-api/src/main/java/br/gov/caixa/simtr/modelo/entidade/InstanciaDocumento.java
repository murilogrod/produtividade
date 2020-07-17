package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb014_instancia_documento", indexes = {
    @Index(name = "ix_mtrtb014_01", unique = true, columnList = "nu_documento, nu_dossie_produto")})
public class InstanciaDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Documento documento;
    private DossieProduto dossieProduto;
    private ElementoConteudo elementoConteudo;
    private GarantiaInformada garantiaInformada;
    private DossieClienteProduto dossieClienteProduto;
    // ***************************************************
    private Set<SituacaoInstanciaDocumento> situacoesInstanciaDocumento;
    private Set<ChecklistAssociado> checklistsAssociados;

    public InstanciaDocumento() {
        super();
        this.situacoesInstanciaDocumento = new HashSet<>();
        this.checklistsAssociados = new HashSet<>();
    }

    @Id
    @Column(name = "nu_instancia_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Documento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_documento", nullable = false)
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @ManyToOne(targetEntity = DossieProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_produto", nullable = false)
    @Fetch(FetchMode.JOIN)
    public DossieProduto getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(DossieProduto dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    @ManyToOne(targetEntity = ElementoConteudo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_elemento_conteudo", nullable = true)
    public ElementoConteudo getElementoConteudo() {
        return elementoConteudo;
    }

    public void setElementoConteudo(ElementoConteudo elementoConteudo) {
        this.elementoConteudo = elementoConteudo;
    }

    @ManyToOne(targetEntity = GarantiaInformada.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_garantia_informada", nullable = true)
    public GarantiaInformada getGarantiaInformada() {
        return garantiaInformada;
    }

    public void setGarantiaInformada(GarantiaInformada garantiaInformada) {
        this.garantiaInformada = garantiaInformada;
    }

    @ManyToOne(targetEntity = DossieClienteProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_cliente_produto", nullable = true)
    public DossieClienteProduto getDossieClienteProduto() {
        return dossieClienteProduto;
    }

    public void setDossieClienteProduto(DossieClienteProduto dossieClienteProduto) {
        this.dossieClienteProduto = dossieClienteProduto;
    }

    // ***************************************************
    @OneToMany(targetEntity = SituacaoInstanciaDocumento.class, mappedBy = "instanciaDocumento", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<SituacaoInstanciaDocumento> getSituacoesInstanciaDocumento() {
        return situacoesInstanciaDocumento;
    }

    public void setSituacoesInstanciaDocumento(Set<SituacaoInstanciaDocumento> situacoesInstanciaDocumento) {
        this.situacoesInstanciaDocumento = situacoesInstanciaDocumento;
    }

    @OneToMany(targetEntity = ChecklistAssociado.class, mappedBy = "instanciaDocumento", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ChecklistAssociado> getChecklistsAssociados() {
        return checklistsAssociados;
    }

    public void setChecklistsAssociados(Set<ChecklistAssociado> checklistsAssociados) {
        this.checklistsAssociados = checklistsAssociados;
    }

    // ***************************************************
    public boolean addSituacoesInstanciaDocumento(SituacaoInstanciaDocumento... situacoesInstanciaDocumento) {
        return this.situacoesInstanciaDocumento.addAll(Arrays.asList(situacoesInstanciaDocumento));
    }

    public boolean removeSituacoesInstanciaDocumento(SituacaoInstanciaDocumento... situacoesInstanciaDocumento) {
        return this.situacoesInstanciaDocumento.removeAll(Arrays.asList(situacoesInstanciaDocumento));
    }

    public boolean addChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.addAll(Arrays.asList(checklistsAssociados));
    }

    public boolean removeChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.removeAll(Arrays.asList(checklistsAssociados));
    }

    // ***************************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final InstanciaDocumento other = (InstanciaDocumento) obj;
        return Objects.equals(this.id, other.id);
    }

}
