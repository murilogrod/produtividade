package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb001_dossie_cliente", indexes = {
    @Index(name = "ix_mtrtb001_01", unique = true, columnList = "nu_cpf_cnpj")})
public class DossieCliente extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long cpfCnpj;
    private TipoPessoaEnum tipoPessoa;
    private String nome;
    private String email;
    private Calendar dataHoraApuracaoNivel;

    // *****************************************
    private Set<Documento> documentos;
    private Set<ComposicaoDocumental> composicoesDocumentais;
    private Set<DossieClienteProduto> dossiesClienteProduto;
    private Set<DossieClienteProduto> dossiesClienteProdutoRelacionado;
    private Set<GarantiaInformada> garantiasInformadas;

    public DossieCliente() {
        super();
        this.documentos = new HashSet<>();
        this.composicoesDocumentais = new HashSet<>();
        this.dossiesClienteProduto = new HashSet<>();
        this.garantiasInformadas = new HashSet<>();
    }

    @Id
    @Column(name = "nu_dossie_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nu_cpf_cnpj", nullable = false)
    public Long getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(Long cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    @Column(name = "ic_tipo_pessoa", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Column(name = "no_cliente", nullable = false, length = 255)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "de_email", nullable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_apuracao_nivel", nullable = true)
    public Calendar getDataHoraApuracaoNivel() {
        return dataHoraApuracaoNivel;
    }

    public void setDataHoraApuracaoNivel(Calendar dataHoraApuracaoNivel) {
        this.dataHoraApuracaoNivel = dataHoraApuracaoNivel;
    }

    // *****************************************
    @ManyToMany(targetEntity = Documento.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb005_documento_cliente", joinColumns = {
        @JoinColumn(name = "nu_dossie_cliente", referencedColumnName = "nu_dossie_cliente")}, inverseJoinColumns = {
        @JoinColumn(name = "nu_documento", referencedColumnName = "nu_documento")})
    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    @ManyToMany(targetEntity = ComposicaoDocumental.class, fetch = FetchType.LAZY)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb038_nivel_documental", joinColumns = {
        @JoinColumn(name = "nu_dossie_cliente", referencedColumnName = "nu_dossie_cliente")}, inverseJoinColumns = {
        @JoinColumn(name = "nu_composicao_documental", referencedColumnName = "nu_composicao_documental")})
    public Set<ComposicaoDocumental> getComposicoesDocumentais() {
        return composicoesDocumentais;
    }

    public void setComposicoesDocumentais(Set<ComposicaoDocumental> composicoesDocumentais) {
        this.composicoesDocumentais = composicoesDocumentais;
    }

    @OneToMany(targetEntity = DossieClienteProduto.class, mappedBy = "dossieCliente", fetch = FetchType.LAZY)
    public Set<DossieClienteProduto> getDossiesClienteProduto() {
        return dossiesClienteProduto;
    }

    public void setDossiesClienteProduto(Set<DossieClienteProduto> dossiesClienteProduto) {
        this.dossiesClienteProduto = dossiesClienteProduto;
    }

    @OneToMany(targetEntity = DossieClienteProduto.class, mappedBy = "dossieClienteRelacionado", fetch = FetchType.LAZY)
    public Set<DossieClienteProduto> getDossiesClienteProdutoRelacionado() {
        return dossiesClienteProdutoRelacionado;
    }

    public void setDossiesClienteProdutoRelacionado(Set<DossieClienteProduto> dossiesClienteProdutoRelacionado) {
        this.dossiesClienteProdutoRelacionado = dossiesClienteProdutoRelacionado;
    }

    @ManyToMany(targetEntity = GarantiaInformada.class, mappedBy = "dossiesCliente", fetch = FetchType.LAZY)
    public Set<GarantiaInformada> getGarantiasInformadas() {
        return garantiasInformadas;
    }

    public void setGarantiasInformadas(Set<GarantiaInformada> garantiasInformadas) {
        this.garantiasInformadas = garantiasInformadas;
    }

    // *****************************************
    public boolean addDocumentos(Documento... documentos) {
        return this.documentos.addAll(Arrays.asList(documentos));
    }

    public boolean removeDocumentos(Documento... documentos) {
        return this.documentos.removeAll(Arrays.asList(documentos));
    }

    public boolean addComposicoesDocumentais(ComposicaoDocumental... composicoesDocumentais) {
        return this.composicoesDocumentais.addAll(Arrays.asList(composicoesDocumentais));
    }

    public boolean removeComposicoesDocumentais(ComposicaoDocumental... composicoesDocumentais) {
        return this.composicoesDocumentais.removeAll(Arrays.asList(composicoesDocumentais));
    }

    public boolean addDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.addAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean removeDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.removeAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean addGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.addAll(Arrays.asList(garantiasInformadas));
    }

    public boolean removeGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.removeAll(Arrays.asList(garantiasInformadas));
    }

    // *****************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final DossieCliente other = (DossieCliente) obj;
        return Objects.equals(this.id, other.id);
    }

}
