package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb002_dossie_produto")
public class DossieProduto extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Processo processo;
    private Integer unidadeCriacao;
    private Integer unidadePriorizado;
    private String matriculaPriorizado;
    private Integer pesoPrioridade;
    private Calendar dataHoraFinalizacao;
    private Canal canal;
    private String nomeProcessoBPM;
    private String nomeContainerBPM;
    private Long idInstanciaProcessoBPM;
    private Calendar dataHoraFalhaBPM;
    // ************************************************
    private Set<InstanciaDocumento> instanciasDocumento;
    private Set<DossieClienteProduto> dossiesClienteProduto;
    private Set<ProdutoDossie> produtosDossie;
    private Set<SituacaoDossie> situacoesDossie;
    private Set<RespostaDossie> respostasDossie;
    private Set<UnidadeTratamento> unidadesTratamento;
    private Set<GarantiaInformada> garantiasInformadas;
    private Set<ChecklistAssociado> checklistsAssociados;
    private Set<ProcessoFaseDossie> processosFaseDossie;

    public DossieProduto() {
        super();
        this.instanciasDocumento = new HashSet<>();
        this.dossiesClienteProduto = new HashSet<>();
        this.produtosDossie = new HashSet<>();
        this.situacoesDossie = new HashSet<>();
        this.respostasDossie = new HashSet<>();
        this.unidadesTratamento = new HashSet<>();
        this.garantiasInformadas = new HashSet<>();
        this.checklistsAssociados = new HashSet<>();
        this.processosFaseDossie = new HashSet<>();
    }

    @Id
    @Column(name = "nu_dossie_produto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_dossie", nullable = false)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @ManyToOne(targetEntity = Canal.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_canal", nullable = false)
    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    @Column(name = "nu_unidade_criacao", nullable = false)
    public Integer getUnidadeCriacao() {
        return unidadeCriacao;
    }

    public void setUnidadeCriacao(Integer unidadeCriacao) {
        this.unidadeCriacao = unidadeCriacao;
    }

    @Column(name = "nu_unidade_priorizado", nullable = true)
    public Integer getUnidadePriorizado() {
        return unidadePriorizado;
    }

    public void setUnidadePriorizado(Integer unidadePriorizado) {
        this.unidadePriorizado = unidadePriorizado;
    }

    @Column(name = "co_matricula_priorizado", nullable = true, length = 7)
    public String getMatriculaPriorizado() {
        return matriculaPriorizado;
    }

    public void setMatriculaPriorizado(String matriculaPriorizado) {
        this.matriculaPriorizado = matriculaPriorizado;
    }

    @Column(name = "nu_peso_prioridade", nullable = true)
    public Integer getPesoPrioridade() {
        return pesoPrioridade;
    }

    public void setPesoPrioridade(Integer pesoPrioridade) {
        this.pesoPrioridade = pesoPrioridade;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_finalizado", nullable = true)
    public Calendar getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(Calendar dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    @Column(name = "no_processo_bpm", nullable = true, length = 255)
    public String getNomeProcessoBPM() {
        return nomeProcessoBPM;
    }

    public void setNomeProcessoBPM(String nomeProcessoBPM) {
        this.nomeProcessoBPM = nomeProcessoBPM;
    }

    @Column(name = "no_container_bpm", nullable = true, length = 255)
    public String getNomeContainerBPM() {
        return nomeContainerBPM;
    }

    public void setNomeContainerBPM(String nomeContainerBPM) {
        this.nomeContainerBPM = nomeContainerBPM;
    }

    @Column(name = "nu_instancia_processo_bpm", nullable = true)
    public Long getIdInstanciaProcessoBPM() {
        return idInstanciaProcessoBPM;
    }

    public void setIdInstanciaProcessoBPM(Long idInstanciaProcessoBPM) {
        this.idInstanciaProcessoBPM = idInstanciaProcessoBPM;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_falha_bpm", nullable = true)
    public Calendar getDataHoraFalhaBPM() {
        return dataHoraFalhaBPM;
    }

    public void setDataHoraFalhaBPM(Calendar dataHoraFalhaBPM) {
        this.dataHoraFalhaBPM = dataHoraFalhaBPM;
    }

    // ************************************************
    @OneToMany(targetEntity = InstanciaDocumento.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY)
    public Set<InstanciaDocumento> getInstanciasDocumento() {
        return instanciasDocumento;
    }

    public void setInstanciasDocumento(Set<InstanciaDocumento> instanciasDocumentos) {
        this.instanciasDocumento = instanciasDocumentos;
    }

    @OneToMany(targetEntity = DossieClienteProduto.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<DossieClienteProduto> getDossiesClienteProduto() {
        return dossiesClienteProduto;
    }

    public void setDossiesClienteProduto(Set<DossieClienteProduto> dossiesClienteProduto) {
        this.dossiesClienteProduto = dossiesClienteProduto;
    }

    @OneToMany(targetEntity = ProdutoDossie.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ProdutoDossie> getProdutosDossie() {
        return produtosDossie;
    }

    public void setProdutosDossie(Set<ProdutoDossie> produtosDossie) {
        this.produtosDossie = produtosDossie;
    }

    @OneToMany(targetEntity = SituacaoDossie.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<SituacaoDossie> getSituacoesDossie() {
        return situacoesDossie;
    }

    public void setSituacoesDossie(Set<SituacaoDossie> situacoesDossie) {
        this.situacoesDossie = situacoesDossie;
    }

    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<RespostaDossie> getRespostasDossie() {
        return respostasDossie;
    }

    public void setRespostasDossie(Set<RespostaDossie> respostasDossie) {
        this.respostasDossie = respostasDossie;
    }

    @OneToMany(targetEntity = UnidadeTratamento.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<UnidadeTratamento> getUnidadesTratamento() {
        return unidadesTratamento;
    }

    public void setUnidadesTratamento(Set<UnidadeTratamento> unidadesTratamento) {
        this.unidadesTratamento = unidadesTratamento;
    }

    @OneToMany(targetEntity = GarantiaInformada.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<GarantiaInformada> getGarantiasInformadas() {
        return garantiasInformadas;
    }

    public void setGarantiasInformadas(Set<GarantiaInformada> garantiasInformadas) {
        this.garantiasInformadas = garantiasInformadas;
    }

    @OneToMany(targetEntity = ChecklistAssociado.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ChecklistAssociado> getChecklistsAssociados() {
        return checklistsAssociados;
    }

    public void setChecklistsAssociados(Set<ChecklistAssociado> checklistsAssociados) {
        this.checklistsAssociados = checklistsAssociados;
    }
    
    @OneToMany(targetEntity = ProcessoFaseDossie.class, mappedBy = "dossieProduto", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ProcessoFaseDossie> getProcessosFaseDossie() {
        return processosFaseDossie;
    }

    public void setProcessosFaseDossie(Set<ProcessoFaseDossie> processosFaseDossie) {
        this.processosFaseDossie = processosFaseDossie;
    }

    // ************************************************
    public boolean addInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.addAll(Arrays.asList(instanciasDocumento));
    }

    public boolean removeInstanciasDocumento(InstanciaDocumento... instanciasDocumentos) {
        return this.instanciasDocumento.removeAll(Arrays.asList(instanciasDocumentos));
    }

    public boolean addDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.addAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean removeDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.removeAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean addProdutosDossie(ProdutoDossie... produtosDossie) {
        return this.produtosDossie.addAll(Arrays.asList(produtosDossie));
    }

    public boolean removeProdutosDossie(ProdutoDossie... produtosDossie) {
        return this.produtosDossie.removeAll(Arrays.asList(produtosDossie));
    }

    public boolean addSituacoesDossie(SituacaoDossie... situacoesDossie) {
        return this.situacoesDossie.addAll(Arrays.asList(situacoesDossie));
    }

    public boolean removeSituacoesDossie(SituacaoDossie... situacoesDossie) {
        return this.situacoesDossie.removeAll(Arrays.asList(situacoesDossie));
    }

    public boolean addRespostasDossie(RespostaDossie... respostasDossie) {
        return this.respostasDossie.addAll(Arrays.asList(respostasDossie));
    }

    public boolean removeRespostasDossie(RespostaDossie... respostasDossie) {
        return this.respostasDossie.removeAll(Arrays.asList(respostasDossie));
    }

    public boolean addUnidadesTratamento(UnidadeTratamento... unidadesTratamento) {
        return this.unidadesTratamento.addAll(Arrays.asList(unidadesTratamento));
    }

    public boolean removeUnidadesTratamento(UnidadeTratamento... unidadesTratamento) {
        return this.unidadesTratamento.removeAll(Arrays.asList(unidadesTratamento));
    }

    public boolean addGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.addAll(Arrays.asList(garantiasInformadas));
    }

    public boolean removeGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.removeAll(Arrays.asList(garantiasInformadas));
    }

    public boolean addChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.addAll(Arrays.asList(checklistsAssociados));
    }

    public boolean removeChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.removeAll(Arrays.asList(checklistsAssociados));
    }
    
    public boolean addProcessosFaseDossie(ProcessoFaseDossie... processosFaseDossie) {
        return this.processosFaseDossie.addAll(Arrays.asList(processosFaseDossie));
    }
    
    public boolean removeProcessosFaseDossie(ProcessoFaseDossie... processosFaseDossie) {
        return this.processosFaseDossie.removeAll(Arrays.asList(processosFaseDossie));
    }

    // ************************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final DossieProduto other = (DossieProduto) obj;
        return Objects.equals(this.id, other.id);
    }

}