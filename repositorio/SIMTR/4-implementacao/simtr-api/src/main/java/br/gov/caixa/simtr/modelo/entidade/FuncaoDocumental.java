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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb010_funcao_documental", indexes = {
    @Index(name = "ix_mtrtb010_01", unique = true, columnList = "no_funcao")
})
public class FuncaoDocumental extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Boolean usoProcessoAdministrativo;
    private Boolean usoDossieDigital;
    private Boolean usoApoioNegocio;
    private Calendar dataHoraUltimaAlteracao;
    // *************************************************
    private Set<TipoDocumento> tiposDocumento;
    private Set<RegraDocumental> regrasDocumentais;
    private Set<ProcessoDocumento> processoDocumentos;
    private Set<VinculacaoChecklist> vinculacoesChecklists;

    public FuncaoDocumental(Integer id) {
        super();
        this.id = id;
    }

    public FuncaoDocumental() {
        super();
        this.usoApoioNegocio = Boolean.FALSE;
        this.usoDossieDigital = Boolean.FALSE;
        this.usoProcessoAdministrativo = Boolean.FALSE;
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.tiposDocumento = new HashSet<>();
        this.regrasDocumentais = new HashSet<>();
        this.processoDocumentos = new HashSet<>();
        this.vinculacoesChecklists = new HashSet<>();
    }

    @Id
    @Column(name = "nu_funcao_documental")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_funcao", nullable = false, length = 100, unique = true)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

	@ManyToMany(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb011_funcao_documento",
		joinColumns = {@JoinColumn(name = "nu_funcao_documental", referencedColumnName = "nu_funcao_documental") },
		inverseJoinColumns = {@JoinColumn(name = "nu_tipo_documento", referencedColumnName = "nu_tipo_documento") })
    public Set<TipoDocumento> getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(Set<TipoDocumento> tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    @OneToMany(targetEntity = RegraDocumental.class, mappedBy = "funcaoDocumental", fetch = FetchType.LAZY)
    public Set<RegraDocumental> getRegrasDocumentais() {
        return regrasDocumentais;
    }

    public void setRegrasDocumentais(Set<RegraDocumental> regrasDocumentais) {
        this.regrasDocumentais = regrasDocumentais;
    }

    @OneToMany(targetEntity = ProcessoDocumento.class, mappedBy = "funcaoDocumental", fetch = FetchType.LAZY)
    public Set<ProcessoDocumento> getProcessoDocumentos() {
        return processoDocumentos;
    }

    public void setProcessoDocumentos(Set<ProcessoDocumento> processoDocumentos) {
        this.processoDocumentos = processoDocumentos;
    }

    @OneToMany(targetEntity = VinculacaoChecklist.class, mappedBy = "funcaoDocumental", fetch = FetchType.LAZY)
    public Set<VinculacaoChecklist> getVinculacoesChecklists() {
        return vinculacoesChecklists;
    }

    public void setVinculacoesChecklists(Set<VinculacaoChecklist> vinculacoesChecklists) {
        this.vinculacoesChecklists = vinculacoesChecklists;
    }

    //***************************************************
    public boolean addTiposDocumento(TipoDocumento... tiposDocumento) {
        return this.tiposDocumento.addAll(Arrays.asList(tiposDocumento));
    }

    public boolean removeTiposDocumento(TipoDocumento... tiposDocumento) {
        return this.tiposDocumento.removeAll(Arrays.asList(tiposDocumento));
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

    public boolean addVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.addAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean removeVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.removeAll(Arrays.asList(vinculacoesChecklists));
    }

    //***************************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final FuncaoDocumental other = (FuncaoDocumental) obj;
        return Objects.equals(this.id, other.id);
    }

}
