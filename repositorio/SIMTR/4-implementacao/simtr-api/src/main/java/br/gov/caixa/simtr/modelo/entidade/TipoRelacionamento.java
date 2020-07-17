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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb041_tipo_relacionamento", indexes = {
    @Index(name = "ix_mtrtb041_01", unique = true, columnList = "no_tipo_relacionamento")
})
public class TipoRelacionamento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Boolean indicadorPrincipal;
    private Boolean indicadorRelacionado;
    private Boolean indicadorSequencia;
    private Boolean indicadorReceitaPessoaFisica;
    private Boolean indicadorReceitaPessoaJuridica;
    private Calendar dataHoraUltimaAlteracao;
    private TipoPessoaEnum tipoPessoaEnum;
    // ************************************************
    private Set<ProcessoDocumento> processoDocumentos;
    private Set<DossieClienteProduto> dossiesClienteProduto;
    private Set<CampoFormulario> camposFormulario;

    public TipoRelacionamento() {
        super();
        this.processoDocumentos = new HashSet<>();
        this.dossiesClienteProduto = new HashSet<>();
        this.camposFormulario = new HashSet<>();
    }

    @Id
    @Column(name = "nu_tipo_relacionamento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_tipo_relacionamento", nullable = false, length = 50)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_principal", nullable = false)
    public Boolean getIndicadorPrincipal() {
        return indicadorPrincipal;
    }

    public void setIndicadorPrincipal(Boolean indicadorPrincipal) {
        this.indicadorPrincipal = indicadorPrincipal;
    }

    @Column(name = "ic_relacionado", nullable = false)
    public Boolean getIndicadorRelacionado() {
        return indicadorRelacionado;
    }

    public void setIndicadorRelacionado(Boolean indicadorRelacionado) {
        this.indicadorRelacionado = indicadorRelacionado;
    }

    @Column(name = "ic_sequencia", nullable = false)
    public Boolean getIndicadorSequencia() {
        return indicadorSequencia;
    }

    public void setIndicadorSequencia(Boolean indicadorSequencia) {
        this.indicadorSequencia = indicadorSequencia;
    }
    
    @Column(name = "ic_receita_pf", nullable = false)
    public Boolean getIndicadorReceitaPessoaFisica() {
        return indicadorReceitaPessoaFisica;
    }

    public void setIndicadorReceitaPessoaFisica(Boolean indicadorReceitaPessoaFisica) {
        this.indicadorReceitaPessoaFisica = indicadorReceitaPessoaFisica;
    }

    @Column(name = "ic_receita_pj", nullable = false)
    public Boolean getIndicadorReceitaPessoaJuridica() {
        return indicadorReceitaPessoaJuridica;
    }

    public void setIndicadorReceitaPessoaJuridica(Boolean indicadorReceitaPessoaJuridica) {
        this.indicadorReceitaPessoaJuridica = indicadorReceitaPessoaJuridica;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = false, length = 1)
    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao", nullable = false)
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    // ************************************************   
    @OneToMany(targetEntity = ProcessoDocumento.class, mappedBy = "tipoRelacionamento", fetch = FetchType.LAZY)
    public Set<ProcessoDocumento> getProcessoDocumentos() {
        return processoDocumentos;
    }

    public void setProcessoDocumentos(Set<ProcessoDocumento> processoDocumentos) {
        this.processoDocumentos = processoDocumentos;
    }

    @OneToMany(targetEntity = DossieClienteProduto.class, mappedBy = "tipoRelacionamento", fetch = FetchType.LAZY)
    public Set<DossieClienteProduto> getDossiesClienteProduto() {
        return dossiesClienteProduto;
    }

    public void setDossiesClienteProduto(Set<DossieClienteProduto> dossiesClienteProduto) {
        this.dossiesClienteProduto = dossiesClienteProduto;
    }

    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "tipoRelacionamento", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormulario() {
        return camposFormulario;
    }

    public void setCamposFormulario(Set<CampoFormulario> camposFormulario) {
        this.camposFormulario = camposFormulario;
    }

    // ************************************************
    public boolean addProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.addAll(Arrays.asList(processoDocumentos));
    }

    public boolean removeProcessoDocumentos(ProcessoDocumento... processoDocumentos) {
        return this.processoDocumentos.removeAll(Arrays.asList(processoDocumentos));
    }

    public boolean addDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.addAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean removeDossiesClienteProduto(DossieClienteProduto... dossiesClienteProduto) {
        return this.dossiesClienteProduto.removeAll(Arrays.asList(dossiesClienteProduto));
    }

    public boolean addCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.addAll(Arrays.asList(camposFormulario));
    }

    public boolean removeCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.removeAll(Arrays.asList(camposFormulario));
    }

    // ************************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final TipoRelacionamento other = (TipoRelacionamento) obj;
        return Objects.equals(this.id, other.id);
    }
}
