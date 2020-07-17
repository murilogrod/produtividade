package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb020_processo", indexes = {
    @Index(name = "ix_mtrtb020_01", unique = true, columnList = "no_processo"),
    @Index(name = "ix_mtrtb020_02", unique = true, columnList = "nu_identificador_bpm")
})
public class Processo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Boolean ativo;
    private String avatar;
    private Boolean controlaValidade;
    private Boolean exigenciaValidacao;
    private TipoPessoaEnum tipoPessoa;
    private Boolean indicadorGeracaoDossie;
    private Boolean indicadorInterface;
    private String orientacao;
    private String nomeProcessoBPM;
    private String nomeContainerBPM;
    private Integer identificadorBPM;
    private Calendar dataHoraUltimaAlteracao;
    private Boolean tratamentoSeletivo;
    private Integer tempoTratamento; 

    // ************************************
    private Set<Produto> produtos;
    private Set<DossieProduto> dossiesProduto;
    private Set<DossieProduto> dossiesProdutoFase;
    private Set<RelacaoProcesso> relacoesProcessoVinculoPai;
    private Set<RelacaoProcesso> relacoesProcessoVinculoFilho;
    private Set<UnidadeAutorizada> unidadesAutorizadas;
    private Set<CampoFormulario> camposFormulario;
    private Set<CampoFormulario> camposFormularioFase;
    private Set<ElementoConteudo> elementosConteudo;
    private Set<ProcessoDocumento> processoDocumentos;
    private Set<DocumentoGarantia> documentosGarantia;
    private Set<VinculacaoChecklist> vinculacoesChecklists;
    private Set<VinculacaoChecklist> vinculacoesChecklistsFase;
    private Set<ChecklistAssociado> checklistsAssociados;
    private Set<RespostaDossie> respostasDossies;
    private Set<UnidadePadrao> unidadesPadrao;
    private Set<ProcessoFaseDossie> processosFaseDossie;

    public Processo() {
        super();
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.produtos = new HashSet<>();
        this.dossiesProduto = new HashSet<>();
        this.processosFaseDossie = new HashSet<>();
        this.relacoesProcessoVinculoPai = new HashSet<>();
        this.relacoesProcessoVinculoFilho = new HashSet<>();
        this.camposFormulario = new HashSet<>();
        this.camposFormularioFase = new HashSet<>();
        this.unidadesAutorizadas = new HashSet<>();
        this.camposFormulario = new HashSet<>();
        this.elementosConteudo = new HashSet<>();
        this.processoDocumentos = new HashSet<>();
        this.documentosGarantia = new HashSet<>();
        this.vinculacoesChecklists = new HashSet<>();
        this.vinculacoesChecklistsFase = new HashSet<>();
        this.checklistsAssociados = new HashSet<>();
        this.respostasDossies = new HashSet<>();
        this.unidadesPadrao = new HashSet<>();
        this.exigenciaValidacao = false;
        this.tratamentoSeletivo = false;
        this.ativo = true;
    }

    @Id
    @Column(name = "nu_processo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_processo", nullable = false, length = 255)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Column(name = "de_avatar", nullable = true, length = 255)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "ic_controla_validade_documento", nullable = false)
    public Boolean getControlaValidade() {
        return controlaValidade;
    }

    public void setControlaValidade(Boolean controlaValidade) {
        this.controlaValidade = controlaValidade;
    }

    @Column(name = "ic_validar_documento", nullable = false)
    public Boolean getExigenciaValidacao() {
        return exigenciaValidacao;
    }

    public void setExigenciaValidacao(Boolean exigenciaValidacao) {
        this.exigenciaValidacao = exigenciaValidacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = false, length = 1)
    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Column(name = "ic_dossie", nullable = false)
    public Boolean getIndicadorGeracaoDossie() {
        return indicadorGeracaoDossie;
    }

    public void setIndicadorGeracaoDossie(Boolean indicadorGeracaoDossie) {
        this.indicadorGeracaoDossie = indicadorGeracaoDossie;
    }
    
    @Column(name = "ic_interface", nullable = false)
    public Boolean getIndicadorInterface() {
        return indicadorInterface;
    }

    public void setIndicadorInterface(Boolean indicadorInterface) {
        this.indicadorInterface = indicadorInterface;
    }

    @Column(name = "de_orientacao", nullable = true, columnDefinition = "text")
    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
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
    
    @Column(name = "nu_identificador_bpm", nullable = true)
    public Integer getIdentificadorBPM() {
        return identificadorBPM;
    }

    public void setIdentificadorBPM(Integer identificadorBPM) {
        this.identificadorBPM = identificadorBPM;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    @Column(name = "ic_tratamento_seletivo", nullable = false)
    public Boolean getTratamentoSeletivo() {
        return tratamentoSeletivo;
    }

    public void setTratamentoSeletivo(Boolean tratamentoSeletivo) {
        this.tratamentoSeletivo = tratamentoSeletivo;
    }
    
    @Column(name = "qt_tempo_tratamento", nullable = false)
    public Integer getTempoTratamento() {
        return tempoTratamento;
    }

    public void setTempoTratamento(Integer tempoTratamento) {
        this.tempoTratamento = tempoTratamento;
    }

    // ************************************
    @ManyToMany(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb023_produto_processo", joinColumns = {
        @JoinColumn(name = "nu_processo", referencedColumnName = "nu_processo")
    }, inverseJoinColumns = {
        @JoinColumn(name = "nu_produto", referencedColumnName = "nu_produto")
    })
    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    @OneToMany(targetEntity = DossieProduto.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<DossieProduto> getDossiesProduto() {
        return dossiesProduto;
    }

    public void setDossiesProduto(Set<DossieProduto> dossiesProduto) {
        this.dossiesProduto = dossiesProduto;
    }

    @OneToMany(targetEntity = RelacaoProcesso.class, mappedBy = "processoPai", fetch = FetchType.LAZY)
    public Set<RelacaoProcesso> getRelacoesProcessoVinculoPai() {
        return relacoesProcessoVinculoPai;
    }

    public void setRelacoesProcessoVinculoPai(Set<RelacaoProcesso> relacoesProcessoVinculoPai) {
        this.relacoesProcessoVinculoPai = relacoesProcessoVinculoPai;
    }

    @OneToMany(targetEntity = RelacaoProcesso.class, mappedBy = "processoFilho", fetch = FetchType.LAZY)
    public Set<RelacaoProcesso> getRelacoesProcessoVinculoFilho() {
        return relacoesProcessoVinculoFilho;
    }

    public void setRelacoesProcessoVinculoFilho(Set<RelacaoProcesso> relacoesProcessoVinculoFilho) {
        this.relacoesProcessoVinculoFilho = relacoesProcessoVinculoFilho;
    }

    @OneToMany(targetEntity = UnidadeAutorizada.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<UnidadeAutorizada> getUnidadesAutorizadas() {
        return unidadesAutorizadas;
    }

    public void setUnidadesAutorizadas(Set<UnidadeAutorizada> unidadeAutorizadas) {
        this.unidadesAutorizadas = unidadeAutorizadas;
    }

    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormulario() {
        return camposFormulario;
    }

    public void setCamposFormulario(Set<CampoFormulario> camposFormulario) {
        this.camposFormulario = camposFormulario;
    }

    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "processoFase", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormularioFase() {
        return camposFormularioFase;
    }

    public void setCamposFormularioFase(Set<CampoFormulario> camposFormularioFase) {
        this.camposFormularioFase = camposFormularioFase;
    }

    @OneToMany(targetEntity = ElementoConteudo.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<ElementoConteudo> getElementosConteudo() {
        return elementosConteudo;
    }

    public void setElementosConteudo(Set<ElementoConteudo> elementosConteudo) {
        this.elementosConteudo = elementosConteudo;
    }

    @OneToMany(targetEntity = ProcessoDocumento.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<ProcessoDocumento> getProcessoDocumentos() {
        return processoDocumentos;
    }

    public void setProcessoDocumentos(Set<ProcessoDocumento> processoDocumentos) {
        this.processoDocumentos = processoDocumentos;
    }

    @OneToMany(targetEntity = DocumentoGarantia.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<DocumentoGarantia> getDocumentosGarantia() {
        return documentosGarantia;
    }

    public void setDocumentosGarantia(Set<DocumentoGarantia> documentosGarantia) {
        this.documentosGarantia = documentosGarantia;
    }

    @OneToMany(targetEntity = VinculacaoChecklist.class, mappedBy = "processoDossie", fetch = FetchType.LAZY)
    public Set<VinculacaoChecklist> getVinculacoesChecklists() {
        return vinculacoesChecklists;
    }

    public void setVinculacoesChecklists(Set<VinculacaoChecklist> vinculacoesChecklists) {
        this.vinculacoesChecklists = vinculacoesChecklists;
    }

    @OneToMany(targetEntity = VinculacaoChecklist.class, mappedBy = "processoFase", fetch = FetchType.LAZY)
    public Set<VinculacaoChecklist> getVinculacoesChecklistsFase() {
        return vinculacoesChecklistsFase;
    }

    public void setVinculacoesChecklistsFase(Set<VinculacaoChecklist> vinculacoesChecklistsFase) {
        this.vinculacoesChecklistsFase = vinculacoesChecklistsFase;
    }

    @OneToMany(targetEntity = ChecklistAssociado.class, mappedBy = "processoFase", fetch = FetchType.LAZY)
    public Set<ChecklistAssociado> getChecklistsAssociados() {
        return checklistsAssociados;
    }

    public void setChecklistsAssociados(Set<ChecklistAssociado> checklistsAssociados) {
        this.checklistsAssociados = checklistsAssociados;
    }

    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "processoFase", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossies() {
        return respostasDossies;
    }

    public void setRespostasDossies(Set<RespostaDossie> respostasDossies) {
        this.respostasDossies = respostasDossies;
    }

    @OneToMany(targetEntity = UnidadePadrao.class, mappedBy = "processo", fetch = FetchType.LAZY)
    public Set<UnidadePadrao> getUnidadesPadrao() {
        return unidadesPadrao;
    }

    public void setUnidadesPadrao(Set<UnidadePadrao> unidadesPadrao) {
        this.unidadesPadrao = unidadesPadrao;
    }
    
    @OneToMany(targetEntity = ProcessoFaseDossie.class, mappedBy = "processoFase", fetch = FetchType.LAZY)
    public Set<ProcessoFaseDossie> getProcessosFaseDossie() {
        return processosFaseDossie;
    }

    public void setProcessosFaseDossie(Set<ProcessoFaseDossie> processosFaseDossie) {
        this.processosFaseDossie = processosFaseDossie;
    }

    // ************************************
    public boolean addProdutos(Produto... produtos) {
        return this.produtos.addAll(Arrays.asList(produtos));
    }

    public boolean removeProdutos(Produto... produtos) {
        return this.produtos.removeAll(Arrays.asList(produtos));
    }

    public boolean addDossiesProduto(DossieProduto... dossiesProduto) {
        return this.dossiesProduto.addAll(Arrays.asList(dossiesProduto));
    }

    public boolean removeDossiesProduto(DossieProduto... dossiesProduto) {
        return this.dossiesProduto.removeAll(Arrays.asList(dossiesProduto));
    }

    public boolean addDossiesProdutoFase(DossieProduto... dossiesProduto) {
        return this.dossiesProdutoFase.addAll(Arrays.asList(dossiesProduto));
    }

    public boolean removeDossiesProdutoFase(DossieProduto... dossiesProduto) {
        return this.dossiesProdutoFase.removeAll(Arrays.asList(dossiesProduto));
    }

    public boolean addRelacoesProcessoVinculoPai(RelacaoProcesso... relacoesProcesso) {
        return this.relacoesProcessoVinculoPai.addAll(Arrays.asList(relacoesProcesso));
    }

    public boolean removeRelacoesProcessoVinculoPai(RelacaoProcesso... relacoesProcesso) {
        return this.relacoesProcessoVinculoPai.removeAll(Arrays.asList(relacoesProcesso));
    }

    public boolean addRelacoesProcessoVinculoFilho(RelacaoProcesso... relacoesProcesso) {
        return this.relacoesProcessoVinculoFilho.addAll(Arrays.asList(relacoesProcesso));
    }

    public boolean removeRelacoesProcessoVinculoFilho(RelacaoProcesso... relacoesProcesso) {
        return this.relacoesProcessoVinculoFilho.removeAll(Arrays.asList(relacoesProcesso));
    }

    public boolean addUnidadesAutorizadas(UnidadeAutorizada... unidadesAutorizadas) {
        return this.unidadesAutorizadas.addAll(Arrays.asList(unidadesAutorizadas));
    }

    public boolean removeUnidadesAutorizadas(UnidadeAutorizada... unidadesAutorizadas) {
        return this.unidadesAutorizadas.removeAll(Arrays.asList(unidadesAutorizadas));
    }

    public boolean addCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.addAll(Arrays.asList(camposFormulario));
    }

    public boolean removeCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.removeAll(Arrays.asList(camposFormulario));
    }

    public boolean addCamposFormularioFase(CampoFormulario... camposFormulario) {
        return this.camposFormularioFase.addAll(Arrays.asList(camposFormulario));
    }

    public boolean removeCamposFormularioFase(CampoFormulario... camposFormulario) {
        return this.camposFormularioFase.removeAll(Arrays.asList(camposFormulario));
    }

    public boolean addElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.addAll(Arrays.asList(elementosConteudo));
    }

    public boolean removeElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.removeAll(Arrays.asList(elementosConteudo));
    }

    public boolean addProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.addAll(Arrays.asList(processoDocumentos));
    }

    public boolean removeProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.removeAll(Arrays.asList(processoDocumentos));
    }

    public boolean addDocumentosGarantia(DocumentoGarantia... documentosGarantia) {
        return this.documentosGarantia.addAll(Arrays.asList(documentosGarantia));
    }

    public boolean removeDocumentosGarantia(DocumentoGarantia... documentosGarantia) {
        return this.documentosGarantia.removeAll(Arrays.asList(documentosGarantia));
    }

    public boolean addVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.addAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean removeVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.removeAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean addVinculacoesChecklistsFase(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklistsFase.addAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean removeVinculacoesChecklistsFase(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklistsFase.removeAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean addChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.addAll(Arrays.asList(checklistsAssociados));
    }

    public boolean removeChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.removeAll(Arrays.asList(checklistsAssociados));
    }

    public boolean addRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.addAll(Arrays.asList(respostasDossies));
    }

    public boolean removeRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.removeAll(Arrays.asList(respostasDossies));
    }

    public boolean addUnidadesPadrao(UnidadePadrao... unidadesPadrao) {
        return this.unidadesPadrao.addAll(Arrays.asList(unidadesPadrao));
    }

    public boolean removeUnidadesPadrao(UnidadePadrao... unidadesPadrao) {
        return this.unidadesPadrao.removeAll(Arrays.asList(unidadesPadrao));
    }
    
    public boolean addProcessosFaseDossie(ProcessoFaseDossie... processosFaseDossie) {
        return this.processosFaseDossie.addAll(Arrays.asList(processosFaseDossie));
    }
    
    public boolean removeProcessosFaseDossie(ProcessoFaseDossie... processosFaseDossie) {
        return this.processosFaseDossie.removeAll(Arrays.asList(processosFaseDossie));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Processo other = (Processo) obj;
        return Objects.equals(this.id, other.id);
    }
}
