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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb036_composicao_documental")
public class ComposicaoDocumental extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Calendar dataHoraInclusao;
    private Calendar dataHoraRevogacao;
    private String matriculaInclusao;
    private String matriculaRevogacao;
    private Boolean indicadorConclusao;
    private Boolean indicadorCadastroCAIXA;
    private TipoPessoaEnum tipoPessoa;
    private Calendar dataHoraUltimaAlteracao;
    // **********************************
    private Set<DossieCliente> dossiesCliente;
    private Set<Produto> produtos;
    private Set<RegraDocumental> regrasDocumentais;

    public ComposicaoDocumental() {
        super();
        this.dossiesCliente = new HashSet<>();
        this.produtos = new HashSet<>();
        this.regrasDocumentais = new HashSet<>();
    }

    @Id
    @Column(name = "nu_composicao_documental")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "no_composicao_documental", length = 100, nullable = false)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
    @Column(name = "ts_revogacao", nullable = true)
    public Calendar getDataHoraRevogacao() {
        return dataHoraRevogacao;
    }

    public void setDataHoraRevogacao(Calendar dataHoraRevogacao) {
        this.dataHoraRevogacao = dataHoraRevogacao;
    }

    @Column(name = "co_matricula_inclusao", length = 7, nullable = false)
    public String getMatriculaInclusao() {
        return matriculaInclusao;
    }

    public void setMatriculaInclusao(String matriculaInclusao) {
        this.matriculaInclusao = matriculaInclusao;
    }

    @Column(name = "co_matricula_revogacao", length = 7, nullable = true)
    public String getMatriculaRevogacao() {
        return matriculaRevogacao;
    }

    public void setMatriculaRevogacao(String matriculaRevogacao) {
        this.matriculaRevogacao = matriculaRevogacao;
    }

    @Column(name = "ic_conclusao_operacao", nullable = false)
    public Boolean getIndicadorConclusao() {
        return indicadorConclusao;
    }

    public void setIndicadorConclusao(Boolean indicadorConclusao) {
        this.indicadorConclusao = indicadorConclusao;
    }

    @Column(name = "ic_cadastro_caixa", nullable = false)
    public Boolean getIndicadorCadastroCAIXA() {
        return indicadorCadastroCAIXA;
    }

    public void setIndicadorCadastroCAIXA(Boolean indicadorCadastroCAIXA) {
        this.indicadorCadastroCAIXA = indicadorCadastroCAIXA;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = false)
    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao", nullable = false)
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    // **********************************
    @ManyToMany(targetEntity = DossieCliente.class, mappedBy = "composicoesDocumentais", fetch = FetchType.LAZY)
    public Set<DossieCliente> getDossiesCliente() {
        return dossiesCliente;
    }

    public void setDossiesCliente(Set<DossieCliente> dossiesCliente) {
        this.dossiesCliente = dossiesCliente;
    }

    @ManyToMany(targetEntity = Produto.class, fetch = FetchType.EAGER)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb039_produto_composicao", joinColumns = {
        @JoinColumn(name = "nu_composicao_documental", referencedColumnName = "nu_composicao_documental")
    }, inverseJoinColumns = {
        @JoinColumn(name = "nu_produto", referencedColumnName = "nu_produto")
    })
    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    @OneToMany(targetEntity = RegraDocumental.class, mappedBy = "composicaoDocumental", fetch = FetchType.LAZY)
    public Set<RegraDocumental> getRegrasDocumentais() {
        return regrasDocumentais;
    }

    public void setRegrasDocumentais(Set<RegraDocumental> regrasDocumentais) {
        this.regrasDocumentais = regrasDocumentais;
    }

    // **********************************
    public boolean addDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.addAll(Arrays.asList(dossiesCliente));
    }

    public boolean removeDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.removeAll(Arrays.asList(dossiesCliente));
    }

    public boolean addProdutos(Produto... produtos) {
        return this.produtos.addAll(Arrays.asList(produtos));
    }

    public boolean removeProdutos(Produto... produtos) {
        return this.produtos.removeAll(Arrays.asList(produtos));
    }

    public boolean addRegrasDocumentais(RegraDocumental... regrasDocumentais) {
        return this.regrasDocumentais.addAll(Arrays.asList(regrasDocumentais));
    }

    public boolean removeRegrasDocumentais(RegraDocumental... regrasDocumentais) {
        return this.regrasDocumentais.removeAll(Arrays.asList(regrasDocumentais));
    }

    // **********************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final ComposicaoDocumental other = (ComposicaoDocumental) obj;
        return Objects.equals(this.id, other.id);
    }
}
