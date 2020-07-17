package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb022_produto", indexes = {
    @Index(name = "ix_mtrtb022_01", unique = true, columnList = "nu_operacao, nu_modalidade"),
    @Index(name = "ix_mtrtb022_02", unique = true, columnList = "nu_portal_empreendedor")})
public class Produto extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer operacao;
    private Integer modalidade;
    private String nome;
    private Boolean contratacaoConjunta;
    private TipoPessoaEnum tipoPessoa;
    private Boolean dossieDigital;
    private Boolean pesquisaCadin;
    private Boolean pesquisaScpc;
    private Boolean pesquisaSerasa;
    private Boolean pesquisaCcf;
    private Boolean pesquisaSicow;
    private Boolean pesquisaReceita;
    private Boolean pesquisaSinad;
    private Integer codigoProdutoPortalEmpreendedor;

    // ************************************
    private Set<ProdutoDossie> produtosDossie;
    private Set<Processo> processos;
    private Set<ElementoConteudo> elementosConteudo;
    private Set<ComportamentoPesquisa> comportamentosPesquisas;
    private Set<ComposicaoDocumental> composicoesDocumentais;
    private Set<Garantia> garantias;
    private Set<GarantiaInformada> garantiasInformadas;
    private Set<CampoFormulario> camposFormulario;

    public Produto() {
        super();
        this.produtosDossie = new HashSet<>();
        this.processos = new HashSet<>();
        this.composicoesDocumentais = new HashSet<>();
        this.elementosConteudo = new HashSet<>();
        this.garantias = new HashSet<>();
        this.garantiasInformadas = new HashSet<>();
        this.camposFormulario = new HashSet<>();
    }

    public Produto(Integer operacao, Integer modalidade) {
        super();
        this.produtosDossie = new HashSet<>();
        this.processos = new HashSet<>();
        this.composicoesDocumentais = new HashSet<>();
        this.elementosConteudo = new HashSet<>();
        this.garantias = new HashSet<>();
        this.garantiasInformadas = new HashSet<>();
        this.operacao = operacao;
        this.modalidade = modalidade;
    }

    @Id
    @Column(name = "nu_produto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "nu_operacao", nullable = false)
    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    @Column(name = "nu_modalidade", nullable = false)
    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    @Column(name = "no_produto", nullable = false, length = 255)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_contratacao_conjunta", nullable = false)
    public Boolean getContratacaoConjunta() {
        return contratacaoConjunta;
    }

    public void setContratacaoConjunta(Boolean contratacaoConjunta) {
        this.contratacaoConjunta = contratacaoConjunta;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = false, length = 1)
    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Column(name = "ic_dossie_digital", nullable = false)
    public Boolean getDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(Boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

    @Column(name = "ic_pesquisa_cadin", nullable = false)
    public Boolean getPesquisaCadin() {
        return pesquisaCadin;
    }

    public void setPesquisaCadin(Boolean pesquisaCadin) {
        this.pesquisaCadin = pesquisaCadin;
    }

    @Column(name = "ic_pesquisa_scpc", nullable = false)
    public Boolean getPesquisaScpc() {
        return pesquisaScpc;
    }

    public void setPesquisaScpc(Boolean pesquisaScpc) {
        this.pesquisaScpc = pesquisaScpc;
    }

    @Column(name = "ic_pesquisa_serasa", nullable = false)
    public Boolean getPesquisaSerasa() {
        return pesquisaSerasa;
    }

    public void setPesquisaSerasa(Boolean pesquisaSerasa) {
        this.pesquisaSerasa = pesquisaSerasa;
    }

    @Column(name = "ic_pesquisa_ccf", nullable = false)
    public Boolean getPesquisaCcf() {
        return pesquisaCcf;
    }

    public void setPesquisaCcf(Boolean pesquisaCcf) {
        this.pesquisaCcf = pesquisaCcf;
    }

    @Column(name = "ic_pesquisa_sicow", nullable = false)
    public Boolean getPesquisaSicow() {
        return pesquisaSicow;
    }

    public void setPesquisaSicow(Boolean pesquisaSicow) {
        this.pesquisaSicow = pesquisaSicow;
    }

    @Column(name = "ic_pesquisa_receita", nullable = false)
    public Boolean getPesquisaReceita() {
        return pesquisaReceita;
    }

    public void setPesquisaReceita(Boolean pesquisaReceita) {
        this.pesquisaReceita = pesquisaReceita;
    }

    @Column(name = "ic_pesquisa_sinad", nullable = false)
    public Boolean getPesquisaSinad() {
        return pesquisaSinad;
    }

    public void setPesquisaSinad(Boolean pesquisaSinad) {
        this.pesquisaSinad = pesquisaSinad;
    }
    
    @Column(name = "nu_portal_empreendedor", nullable = true)
    public Integer getCodigoProdutoPortalEmpreendedor() {
        return codigoProdutoPortalEmpreendedor;
    }

    public void setCodigoProdutoPortalEmpreendedor(Integer codigoProdutoPortalEmpreendedor) {
        this.codigoProdutoPortalEmpreendedor = codigoProdutoPortalEmpreendedor;
    }

    // ************************************
    @OneToMany(targetEntity = ProdutoDossie.class, mappedBy = "produto", fetch = FetchType.LAZY)
    public Set<ProdutoDossie> getProdutosDossie() {
        return produtosDossie;
    }

    public void setProdutosDossie(Set<ProdutoDossie> produtosDossie) {
        this.produtosDossie = produtosDossie;
    }

    @ManyToMany(targetEntity = Processo.class, mappedBy = "produtos", fetch = FetchType.LAZY)
    public Set<Processo> getProcessos() {
        return processos;
    }

    public void setProcessos(Set<Processo> processos) {
        this.processos = processos;
    }

    @OneToMany(targetEntity = ElementoConteudo.class, mappedBy = "produto", fetch = FetchType.LAZY)
    public Set<ElementoConteudo> getElementosConteudo() {
        return elementosConteudo;
    }

    public void setElementosConteudo(Set<ElementoConteudo> elementosConteudo) {
        this.elementosConteudo = elementosConteudo;
    }

    @OneToMany(targetEntity = ComportamentoPesquisa.class, mappedBy = "produto", fetch = FetchType.LAZY)
    public Set<ComportamentoPesquisa> getComportamentosPesquisas() {
        return comportamentosPesquisas;
    }

    public void setComportamentosPesquisas(Set<ComportamentoPesquisa> comportamentosPesquisas) {
        this.comportamentosPesquisas = comportamentosPesquisas;
    }

    @ManyToMany(targetEntity = ComposicaoDocumental.class, mappedBy = "produtos", fetch = FetchType.LAZY)
    public Set<ComposicaoDocumental> getComposicoesDocumentais() {
        return composicoesDocumentais;
    }

    public void setComposicoesDocumentais(Set<ComposicaoDocumental> composicoesDocumentais) {
        this.composicoesDocumentais = composicoesDocumentais;
    }

    @ManyToMany(targetEntity = Garantia.class, mappedBy = "produtos", fetch = FetchType.LAZY)
    public Set<Garantia> getGarantias() {
        return garantias;
    }

    public void setGarantias(Set<Garantia> garantias) {
        this.garantias = garantias;
    }

    @OneToMany(targetEntity = GarantiaInformada.class, mappedBy = "produto", fetch = FetchType.LAZY)
    public Set<GarantiaInformada> getGarantiasInformadas() {
        return garantiasInformadas;
    }

    public void setGarantiasInformadas(Set<GarantiaInformada> garantiasInformadas) {
        this.garantiasInformadas = garantiasInformadas;
    }

    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "produto", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormulario() {
        return camposFormulario;
    }

    public void setCamposFormulario(Set<CampoFormulario> camposFormulario) {
        this.camposFormulario = camposFormulario;
    }

    // ************************************
    public boolean addProdutosDossie(ProdutoDossie... produtosDossie) {
        return this.produtosDossie.addAll(Arrays.asList(produtosDossie));
    }

    public boolean removeProdutosDossie(ProdutoDossie... produtosDossie) {
        return this.produtosDossie.removeAll(Arrays.asList(produtosDossie));
    }

    public boolean addProcessos(Processo... processos) {
        return this.processos.addAll(Arrays.asList(processos));
    }

    public boolean removeProcessos(Processo... processos) {
        return this.processos.removeAll(Arrays.asList(processos));
    }

    public boolean addElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.addAll(Arrays.asList(elementosConteudo));
    }

    public boolean removeElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.removeAll(Arrays.asList(elementosConteudo));
    }

    public boolean addComportamentosPesquisas(ComportamentoPesquisa... comportamentosPesquisas) {
        return this.comportamentosPesquisas.addAll(Arrays.asList(comportamentosPesquisas));
    }

    public boolean removeComportamentosPesquisas(ComportamentoPesquisa... comportamentosPesquisas) {
        return this.comportamentosPesquisas.removeAll(Arrays.asList(comportamentosPesquisas));
    }

    public boolean addComposicoesDocumentais(ComposicaoDocumental... composicoesDocumentais) {
        return this.composicoesDocumentais.addAll(Arrays.asList(composicoesDocumentais));
    }

    public boolean removeComposicoesDocumentais(ComposicaoDocumental... composicoesDocumentais) {
        return this.composicoesDocumentais.removeAll(Arrays.asList(composicoesDocumentais));
    }

    public boolean addGarantias(Garantia... garantias) {
        return this.garantias.addAll(Arrays.asList(garantias));
    }

    public boolean removeGarantias(Garantia... garantias) {
        return this.garantias.removeAll(Arrays.asList(garantias));
    }

    public boolean addGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.addAll(Arrays.asList(garantiasInformadas));
    }

    public boolean removeGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.removeAll(Arrays.asList(garantiasInformadas));
    }
    
    public boolean addCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.addAll(Arrays.asList(camposFormulario));
    }

    public boolean removeCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.removeAll(Arrays.asList(camposFormulario));
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
        final Produto other = (Produto) obj;
        return Objects.equals(this.id, other.id);
    }
}
