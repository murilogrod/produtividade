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
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb009_tipo_documento", indexes = {
    @Index(name = "ix_mtrtb009_01", unique = true, columnList = "no_tipo_documento")
    ,
    @Index(name = "ix_mtrtb009_02", unique = true, columnList = "co_tipologia")
})
public class TipoDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private TipoPessoaEnum tipoPessoaEnum;
    private Integer prazoValidade;
    private Boolean validadeAutoContida;
    private String codigoTipologia;
    private String nomeClasseSIECM;
    private String nomeArquivoMinuta;
    private Boolean reuso;
    private Boolean usoProcessoAdministrativo;
    private Boolean usoDossieDigital;
    private Boolean usoApoioNegocio;
    private String tags;
    private Boolean enviaAvaliacaoCadastral;
    private Boolean enviaAvaliacaoDocumental;
    private Boolean enviaAvaliacaoSICOD;
    private Boolean enviaExtracaoExterna;
    private Boolean permiteExtracaoM0;
    private Boolean permiteMultiplosAtivos;
    private String avatar;
    private String corRGB;
    private Calendar dataHoraUltimaAlteracao;
    private Boolean guardaBinarioOutsourcing;
    private Boolean ativo;
    
    // ***********************************
    private Set<Documento> documentos;
    private Set<FuncaoDocumental> funcoesDocumentais;
    private Set<ElementoConteudo> elementosConteudo;
    private Set<RegraDocumental> regrasDocumentais;
    private Set<ProcessoDocumento> processoDocumentos;
    private Set<AtributoExtracao> atributosExtracao;
    private Set<VinculacaoChecklist> vinculacoesChecklists;

    public TipoDocumento() {
        super();
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.documentos = new HashSet<>();
        this.funcoesDocumentais = new HashSet<>();
        this.elementosConteudo = new HashSet<>();
        this.regrasDocumentais = new HashSet<>();
        this.processoDocumentos = new HashSet<>();
        this.atributosExtracao = new HashSet<>();
        this.vinculacoesChecklists = new HashSet<>();
        this.validadeAutoContida = false;
        this.reuso = true;
        this.usoProcessoAdministrativo = false;
        this.enviaAvaliacaoCadastral = false;
        this.enviaAvaliacaoDocumental = false;
        this.enviaExtracaoExterna = false;
        this.permiteExtracaoM0 = false;
        this.permiteMultiplosAtivos = false;
    }

    @Id
    @Column(name = "nu_tipo_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_tipo_documento", nullable = false, length = 100)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = true, length = 1, columnDefinition = "bpchar")
    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    @Column(name = "pz_validade_dias", nullable = true)
    public Integer getPrazoValidade() {
        return prazoValidade;
    }

    public void setPrazoValidade(Integer prazoValidade) {
        this.prazoValidade = prazoValidade;
    }

    @Column(name = "ic_validade_auto_contida", nullable = false)
    public Boolean getValidadeAutoContida() {
        return validadeAutoContida;
    }

    public void setValidadeAutoContida(Boolean validadeAutoContida) {
        this.validadeAutoContida = validadeAutoContida;
    }

    @Column(name = "co_tipologia", nullable = true, length = 100)
    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    @Column(name = "no_classe_siecm", nullable = true, length = 100)
    public String getNomeClasseSIECM() {
        return nomeClasseSIECM;
    }

    public void setNomeClasseSIECM(String nomeClasseSIECM) {
        this.nomeClasseSIECM = nomeClasseSIECM;
    }

    @Column(name = "no_arquivo_minuta", nullable = true, length = 100)
    public String getNomeArquivoMinuta() {
        return nomeArquivoMinuta;
    }

    public void setNomeArquivoMinuta(String nomeArquivoMinuta) {
        this.nomeArquivoMinuta = nomeArquivoMinuta;
    }

    @Column(name = "ic_reuso", nullable = false)
    public Boolean getReuso() {
        return reuso;
    }

    public void setReuso(Boolean reuso) {
        this.reuso = reuso;
    }

    @Column(name = "ic_processo_administrativo", nullable = false)
    public Boolean getUsoProcessoAdministrativo() {
        return usoProcessoAdministrativo;
    }

    public void setUsoProcessoAdministrativo(Boolean usoProcessoAdministrativo) {
        this.usoProcessoAdministrativo = usoProcessoAdministrativo;
    }

    @Column(name = "ic_dossie_digital", nullable = false)
    public Boolean getUsoDossieDigital() {
        return usoDossieDigital;
    }

    public void setUsoDossieDigital(Boolean usoDossieDigital) {
        this.usoDossieDigital = usoDossieDigital;
    }

    @Column(name = "ic_apoio_negocio", nullable = false)
    public Boolean getUsoApoioNegocio() {
        return usoApoioNegocio;
    }

    public void setUsoApoioNegocio(Boolean usoApoioNegocio) {
        this.usoApoioNegocio = usoApoioNegocio;
    }

    @Column(name = "de_tags", nullable = true, columnDefinition = "text")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Column(name = "ic_validacao_cadastral", nullable = false)
    public Boolean getEnviaAvaliacaoCadastral() {
        return enviaAvaliacaoCadastral;
    }

    public void setEnviaAvaliacaoCadastral(Boolean enviaAvaliacaoCadastral) {
        this.enviaAvaliacaoCadastral = enviaAvaliacaoCadastral;
    }

    @Column(name = "ic_validacao_documental", nullable = false)
    public Boolean getEnviaAvaliacaoDocumental() {
        return enviaAvaliacaoDocumental;
    }

    public void setEnviaAvaliacaoDocumental(Boolean enviaAvaliacaoDocumental) {
        this.enviaAvaliacaoDocumental = enviaAvaliacaoDocumental;
    }

    @Column(name = "ic_validacao_sicod", nullable = false)
    public Boolean getEnviaAvaliacaoSICOD() {
        return enviaAvaliacaoSICOD;
    }

    public void setEnviaAvaliacaoSICOD(Boolean enviaAvaliacaoSICOD) {
        this.enviaAvaliacaoSICOD = enviaAvaliacaoSICOD;
    }

    @Column(name = "ic_extracao_externa", nullable = false)
    public Boolean getEnviaExtracaoExterna() {
        return enviaExtracaoExterna;
    }

    public void setEnviaExtracaoExterna(Boolean enviaExtracaoExterna) {
        this.enviaExtracaoExterna = enviaExtracaoExterna;
    }

    @Column(name = "ic_extracao_m0", nullable = false)
    public Boolean getPermiteExtracaoM0() {
        return permiteExtracaoM0;
    }

    public void setPermiteExtracaoM0(Boolean permiteExtracaoM0) {
        this.permiteExtracaoM0 = permiteExtracaoM0;
    }

    @Column(name = "ic_multiplos", nullable = false)
    public Boolean getPermiteMultiplosAtivos() {
        return permiteMultiplosAtivos;
    }

    public void setPermiteMultiplosAtivos(Boolean permiteMultiplosAtivos) {
        this.permiteMultiplosAtivos = permiteMultiplosAtivos;
    }

    @Column(name = "no_avatar", nullable = true, length = 255)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "co_rgb_box", nullable = true, length = 7)
    public String getCorRGB() {
        return corRGB;
    }

    public void setCorRGB(String corRGB) {
        this.corRGB = corRGB;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    public void setGuardaBinarioOutsourcing(Boolean permiteExtracaoM0) {
        this.guardaBinarioOutsourcing = permiteExtracaoM0;
    }

    @Column(name = "ic_guarda_binario_outsourcing", nullable = false)
    public Boolean getGuardaBinarioOutsourcing() {
        return guardaBinarioOutsourcing;
    }
    
    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
    
    // ***********************************
    @OneToMany(targetEntity = Documento.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    @ManyToMany(targetEntity = FuncaoDocumental.class, mappedBy = "tiposDocumento", fetch = FetchType.LAZY)
    public Set<FuncaoDocumental> getFuncoesDocumentais() {
        return funcoesDocumentais;
    }

    public void setFuncoesDocumentais(Set<FuncaoDocumental> funcoesDocumentais) {
        this.funcoesDocumentais = funcoesDocumentais;
    }

    @OneToMany(targetEntity = ElementoConteudo.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<ElementoConteudo> getElementosConteudo() {
        return elementosConteudo;
    }

    public void setElementosConteudo(Set<ElementoConteudo> elementosConteudo) {
        this.elementosConteudo = elementosConteudo;
    }

    @OneToMany(targetEntity = RegraDocumental.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<RegraDocumental> getRegrasDocumentais() {
        return regrasDocumentais;
    }

    public void setRegrasDocumentais(Set<RegraDocumental> regrasDocumentais) {
        this.regrasDocumentais = regrasDocumentais;
    }

    @OneToMany(targetEntity = ProcessoDocumento.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<ProcessoDocumento> getProcessoDocumentos() {
        return processoDocumentos;
    }

    public void setProcessoDocumentos(Set<ProcessoDocumento> processoDocumentos) {
        this.processoDocumentos = processoDocumentos;
    }

    @OneToMany(targetEntity = AtributoExtracao.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<AtributoExtracao> getAtributosExtracao() {
        return atributosExtracao;
    }

    public void setAtributosExtracao(Set<AtributoExtracao> atributosExtracao) {
        this.atributosExtracao = atributosExtracao;
    }

    @OneToMany(targetEntity = VinculacaoChecklist.class, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
    public Set<VinculacaoChecklist> getVinculacoesChecklists() {
        return vinculacoesChecklists;
    }

    public void setVinculacoesChecklists(Set<VinculacaoChecklist> vinculacoesChecklists) {
        this.vinculacoesChecklists = vinculacoesChecklists;
    }

    // ***********************************
    public boolean addDocumento(Documento... documentos) {
        return this.documentos.addAll(Arrays.asList(documentos));
    }

    public boolean removeDocumento(Documento... documentos) {
        return this.documentos.removeAll(Arrays.asList(documentos));
    }

    public boolean addFuncoesDocumentais(FuncaoDocumental... funcoesDocumentais) {
        return this.funcoesDocumentais.addAll(Arrays.asList(funcoesDocumentais));
    }

    public boolean removeFuncoesDocumentais(FuncaoDocumental... funcoesDocumentais) {
        return this.funcoesDocumentais.removeAll(Arrays.asList(funcoesDocumentais));
    }

    public boolean addElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.addAll(Arrays.asList(elementosConteudo));
    }

    public boolean removeElementosConteudo(ElementoConteudo... elementosConteudo) {
        return this.elementosConteudo.removeAll(Arrays.asList(elementosConteudo));
    }

    public boolean addRegrasDocumentais(RegraDocumental... regrasDocumentais) {
        return this.regrasDocumentais.addAll(Arrays.asList(regrasDocumentais));
    }

    public boolean removeRegrasDocumentais(RegraDocumental... regrasDocumentais) {
        return this.regrasDocumentais.removeAll(Arrays.asList(regrasDocumentais));
    }

    public boolean addProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.addAll(Arrays.asList(processoDocumentos));
    }

    public boolean removeProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.removeAll(Arrays.asList(processoDocumentos));
    }

    public boolean addAtributosExtracao(AtributoExtracao... atributosExtracao) {
        return this.atributosExtracao.addAll(Arrays.asList(atributosExtracao));
    }

    public boolean removeAtributosExtracao(AtributoExtracao... atributosExtracao) {
        return this.atributosExtracao.removeAll(Arrays.asList(atributosExtracao));
    }

    public boolean addVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.addAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean removeVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.removeAll(Arrays.asList(vinculacoesChecklists));
    }

    // ***********************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final TipoDocumento other = (TipoDocumento) obj;
        return Objects.equals(this.id, other.id);
    }
}
