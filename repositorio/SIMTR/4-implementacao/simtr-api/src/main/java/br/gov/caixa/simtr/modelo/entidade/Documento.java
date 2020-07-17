package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb003_documento")

public class Documento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoDocumento tipoDocumento;
    private Canal canalCaptura;
    private FormatoConteudoEnum formatoConteudoEnum;
    private FormatoConteudoEnum formatoConteudoTratadoEnum;
    private String codigoGED;
    private String codigoSiecmTratado;
    private String codigoSiecmCaixa;
    private String codigoSiecmReuso;
    private Calendar dataHoraCaptura;
    private String responsavel;
    private Calendar dataHoraValidade;
    private Calendar dataHoraCadastroCliente;
    private Boolean dossieDigital;
    private OrigemDocumentoEnum origemDocumento;
    private TemporalidadeDocumentoEnum situacaoTemporalidade;
    private BigDecimal indiceAntifraude;
    private BigDecimal percentualAssertividadeDocumento;
    private Integer quantidadeConteudos;
    private Long quantidadeBytes;
    // *********************************************
    private Set<DocumentoAdministrativo> documentosAdministrativos;
    private Set<Conteudo> conteudos;
    private Set<DossieCliente> dossiesCliente;
    private Set<InstanciaDocumento> instanciasDocumento;
    private Set<AtributoDocumento> atributosDocumento;
    private Set<ControleDocumento> controlesDocumento;

    public Documento() {
        super();
        this.documentosAdministrativos = new HashSet<>();
        this.conteudos = new HashSet<>();
        this.dossiesCliente = new HashSet<>();
        this.instanciasDocumento = new HashSet<>();
        this.atributosDocumento = new HashSet<>();
        this.controlesDocumento = new HashSet<>();
        this.quantidadeBytes = 0L;
        this.quantidadeConteudos = 0;
    }

    @Id
    @Column(name = "nu_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = true)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(targetEntity = Canal.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_canal_captura", nullable = false)
    public Canal getCanalCaptura() {
        return canalCaptura;
    }

    public void setCanalCaptura(Canal canalCaptura) {
        this.canalCaptura = canalCaptura;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "no_formato", length = 10, nullable = true)
    public FormatoConteudoEnum getFormatoConteudoEnum() {
        return formatoConteudoEnum;
    }

    public void setFormatoConteudoEnum(FormatoConteudoEnum formatoConteudoEnum) {
        this.formatoConteudoEnum = formatoConteudoEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "no_formato_tratado", length = 10, nullable = true)
    public FormatoConteudoEnum getFormatoConteudoTratadoEnum() {
        return formatoConteudoTratadoEnum;
    }

    public void setFormatoConteudoTratadoEnum(FormatoConteudoEnum formatoConteudoTratadoEnum) {
        this.formatoConteudoTratadoEnum = formatoConteudoTratadoEnum;
    }

    @Column(name = "co_siecm_proprio", nullable = true, length = 255)
    public String getCodigoGED() {
        return codigoGED;
    }

    public void setCodigoGED(String codigoGED) {
        this.codigoGED = codigoGED;
    }

    @Column(name = "co_siecm_tratado", nullable = true, length = 255)
    public String getCodigoSiecmTratado() {
        return codigoSiecmTratado;
    }

    public void setCodigoSiecmTratado(String codigoSiecmTratado) {
        this.codigoSiecmTratado = codigoSiecmTratado;
    }

    @Column(name = "co_siecm_caixa", nullable = true, length = 255)
    public String getCodigoSiecmCaixa() {
        return codigoSiecmCaixa;
    }

    public void setCodigoSiecmCaixa(String codigoSiecmCaixa) {
        this.codigoSiecmCaixa = codigoSiecmCaixa;
    }

    @Column(name = "co_siecm_reuso", nullable = true, length = 255)
    public String getCodigoSiecmReuso() {
        return codigoSiecmReuso;
    }

    public void setCodigoSiecmReuso(String codigoSiecmReuso) {
        this.codigoSiecmReuso = codigoSiecmReuso;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_captura", nullable = false)
    public Calendar getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(Calendar dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    @Column(name = "co_responsavel", nullable = false, length = 20)
    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_validade", nullable = true)
    public Calendar getDataHoraValidade() {
        return dataHoraValidade;
    }

    public void setDataHoraValidade(Calendar dataHoraValidade) {
        this.dataHoraValidade = dataHoraValidade;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_cadastro_cliente", nullable = true)
    public Calendar getDataHoraCadastroCliente() {
        return dataHoraCadastroCliente;
    }

    public void setDataHoraCadastroCliente(Calendar dataHoraCadastroCliente) {
        this.dataHoraCadastroCliente = dataHoraCadastroCliente;
    }

    @Column(name = "ic_dossie_digital", nullable = false)
    public Boolean getDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(Boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_origem_documento", nullable = false, length = 1)
    public OrigemDocumentoEnum getOrigemDocumento() {
        return origemDocumento;
    }

    public void setOrigemDocumento(OrigemDocumentoEnum origemDocumento) {
        this.origemDocumento = origemDocumento;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ic_temporario", nullable = false)
    public TemporalidadeDocumentoEnum getSituacaoTemporalidade() {
        return situacaoTemporalidade;
    }

    public void setSituacaoTemporalidade(TemporalidadeDocumentoEnum situacaoTemporalidade) {
        this.situacaoTemporalidade = situacaoTemporalidade;
    }

    @Column(name = "ix_antifraude", nullable = true, scale = 5, precision = 2)
    public BigDecimal getIndiceAntifraude() {
        return indiceAntifraude;
    }

    public void setIndiceAntifraude(BigDecimal indiceAntifraude) {
        this.indiceAntifraude = indiceAntifraude;
    }
    
    @Column(name = "ix_pad", nullable = true, scale = 5, precision = 2)
    public BigDecimal getPercentualAssertividadeDocumento() {
        return percentualAssertividadeDocumento;
    }

    public void setPercentualAssertividadeDocumento(BigDecimal percentualAssertividadeDocumento) {
        this.percentualAssertividadeDocumento = percentualAssertividadeDocumento;
    }

    @Column(name = "qt_conteudos", nullable = false)
    public Integer getQuantidadeConteudos() {
        return quantidadeConteudos;
    }

    public void setQuantidadeConteudos(Integer quantidadeConteudos) {
        this.quantidadeConteudos = quantidadeConteudos;
    }

    @Column(name = "qt_bytes", nullable = false)
    public Long getQuantidadeBytes() {
        return quantidadeBytes;
    }

    public void setQuantidadeBytes(Long quantidadeBytes) {
        this.quantidadeBytes = quantidadeBytes;
    }

    // *********************************************
    // Alterado relacionamento de OneToOne para OneToMany devido a erro conhecido conforme referência da JBoss.org abaixo:
    // https://developer.jboss.org/wiki/SomeExplanationsOnLazyLoadingone-to-one
    @OneToMany(targetEntity = DocumentoAdministrativo.class, mappedBy = "documento", fetch = FetchType.LAZY)
    public Set<DocumentoAdministrativo> getDocumentosAdministrativos() {
        return documentosAdministrativos;
    }

    public void setDocumentosAdministrativos(Set<DocumentoAdministrativo> documentosAdministrativos) {
        this.documentosAdministrativos = documentosAdministrativos;
    }

    @ManyToMany(targetEntity = DossieCliente.class, mappedBy = "documentos", fetch = FetchType.LAZY)
    public Set<DossieCliente> getDossiesCliente() {
        return dossiesCliente;
    }

    public void setDossiesCliente(Set<DossieCliente> dossiesCliente) {
        this.dossiesCliente = dossiesCliente;
    }

    @OneToMany(targetEntity = Conteudo.class, mappedBy = "documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(Set<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

    @OneToMany(targetEntity = InstanciaDocumento.class, mappedBy = "documento", fetch = FetchType.LAZY)
    public Set<InstanciaDocumento> getInstanciasDocumento() {
        return instanciasDocumento;
    }

    public void setInstanciasDocumento(Set<InstanciaDocumento> instanciasDocumento) {
        this.instanciasDocumento = instanciasDocumento;
    }

    @OneToMany(targetEntity = AtributoDocumento.class, mappedBy = "documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<AtributoDocumento> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(Set<AtributoDocumento> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }

    @OneToMany(targetEntity = ControleDocumento.class, mappedBy = "documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<ControleDocumento> getControlesDocumento() {
        return controlesDocumento;
    }

    public void setControlesDocumento(Set<ControleDocumento> controlesDocumento) {
        this.controlesDocumento = controlesDocumento;
    }

    // *********************************************
    public boolean addDocomentoAdministrativo(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.addAll(Arrays.asList(documentosAdministrativos));
    }

    public boolean removeDocomentoAdministrativo(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.removeAll(Arrays.asList(documentosAdministrativos));
    }

    public boolean addDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.addAll(Arrays.asList(dossiesCliente));
    }

    public boolean removeDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.removeAll(Arrays.asList(dossiesCliente));
    }

    public boolean addConteudos(Conteudo... conteudos) {
        return this.conteudos.addAll(Arrays.asList(conteudos));
    }

    public boolean removeConteudos(Conteudo... conteudos) {
        return this.conteudos.removeAll(Arrays.asList(conteudos));
    }

    public boolean addInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.addAll(Arrays.asList(instanciasDocumento));
    }

    public boolean removeInstanciasDocumento(InstanciaDocumento... instanciasDocumentos) {
        return this.instanciasDocumento.removeAll(Arrays.asList(instanciasDocumentos));
    }

    public boolean addAtributosDocumento(AtributoDocumento... atributosDocumento) {
        return this.atributosDocumento.addAll(Arrays.asList(atributosDocumento));
    }

    public boolean removeAtributosDocumento(AtributoDocumento... atributosDocumento) {
        return this.atributosDocumento.removeAll(Arrays.asList(atributosDocumento));
    }

    public boolean addControlesDocumento(ControleDocumento... controlesDocumento) {
        return this.controlesDocumento.addAll(Arrays.asList(controlesDocumento));
    }

    public boolean removeControlesDocumento(ControleDocumento... controlesDocumento) {
        return this.controlesDocumento.removeAll(Arrays.asList(controlesDocumento));
    }

    // *********************************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final Documento other = (Documento) obj;
        return Objects.equals(this.id, other.id);
    }

}
