package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb046_processo_adm", indexes = {
    @Index(name = "ix_mtrtb046_01", unique = true, columnList = "nu_processo, nu_ano_processo")
    ,
    @Index(name = "ix_mtrtb046_02", unique = true, columnList = "nu_pregao, nu_unidade_contratacao, nu_ano_pregao"),})
public class ProcessoAdministrativo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer numeroProcesso;
    private Integer anoProcesso;
    private Integer numeroPregao;
    private Integer anoPregao;
    private String objetoContratacao;
    private Integer unidadeContratacao;
    private Integer unidadeDemandante;
    private Calendar dataHoraInclusao;
    private String matriculaInclusao;
    private Calendar dataHoraFinalizacao;
    private String matriculaFinalizacao;
    private String protocoloSICLG;
    // ************************************
    private Set<ContratoAdministrativo> contratosAdministrativos;
    private Set<ApensoAdministrativo> apensosAdministrativos;
    private Set<DocumentoAdministrativo> documentosAdministrativos;

    public ProcessoAdministrativo() {
        super();
        this.contratosAdministrativos = new HashSet<>();
        this.apensosAdministrativos = new HashSet<>();
        this.documentosAdministrativos = new HashSet<>();
    }

    @Id
    @Column(name = "nu_processo_adm")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nu_processo", nullable = false)
    public Integer getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(Integer numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    @Column(name = "nu_ano_processo", nullable = false)
    public Integer getAnoProcesso() {
        return anoProcesso;
    }

    public void setAnoProcesso(Integer anoProcesso) {
        this.anoProcesso = anoProcesso;
    }

    @Column(name = "nu_pregao", nullable = true)
    public Integer getNumeroPregao() {
        return numeroPregao;
    }

    public void setNumeroPregao(Integer numeroPregao) {
        this.numeroPregao = numeroPregao;
    }

    @Column(name = "nu_ano_pregao", nullable = true)
    public Integer getAnoPregao() {
        return anoPregao;
    }

    public void setAnoPregao(Integer anoPregao) {
        this.anoPregao = anoPregao;
    }

    @Column(name = "nu_unidade_contratacao", nullable = true)
    public Integer getUnidadeContratacao() {
        return unidadeContratacao;
    }

    public void setUnidadeContratacao(Integer unidadeContratacao) {
        this.unidadeContratacao = unidadeContratacao;
    }

    @Column(name = "nu_unidade_demandante", nullable = false)
    public Integer getUnidadeDemandante() {
        return unidadeDemandante;
    }

    public void setUnidadeDemandante(Integer unidadeDemandante) {
        this.unidadeDemandante = unidadeDemandante;
    }

    @Column(name = "de_objeto_contratacao", nullable = false, columnDefinition = "text")
    public String getObjetoContratacao() {
        return objetoContratacao;
    }

    public void setObjetoContratacao(String objetoContratacao) {
        this.objetoContratacao = objetoContratacao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_inclusao", nullable = false)
    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    @Column(name = "co_matricula", nullable = false, length = 7)
    public String getMatriculaInclusao() {
        return matriculaInclusao;
    }

    public void setMatriculaInclusao(String matriculaInclusao) {
        this.matriculaInclusao = matriculaInclusao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_finalizacao", nullable = true)
    public Calendar getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(Calendar dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    @Column(name = "co_matricula_finalizacao", nullable = true, length = 7)
    public String getMatriculaFinalizacao() {
        return matriculaFinalizacao;
    }

    public void setMatriculaFinalizacao(String matriculaFinalizacao) {
        this.matriculaFinalizacao = matriculaFinalizacao;
    }

    @Column(name = "co_protocolo_siclg", nullable = true, length = 255)
    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    // ************************************
    @OneToMany(targetEntity = ContratoAdministrativo.class, mappedBy = "processoAdministrativo", fetch = FetchType.LAZY)
    public Set<ContratoAdministrativo> getContratosAdministrativos() {
        return contratosAdministrativos;
    }

    public void setContratosAdministrativos(Set<ContratoAdministrativo> contratosAdministrativos) {
        this.contratosAdministrativos = contratosAdministrativos;
    }

    @OneToMany(targetEntity = ApensoAdministrativo.class, mappedBy = "processoAdministrativo", fetch = FetchType.LAZY)
    public Set<ApensoAdministrativo> getApensosAdministrativos() {
        return apensosAdministrativos;
    }

    public void setApensosAdministrativos(Set<ApensoAdministrativo> apensosAdministrativos) {
        this.apensosAdministrativos = apensosAdministrativos;
    }

    @OneToMany(targetEntity = DocumentoAdministrativo.class, mappedBy = "processoAdministrativo", fetch = FetchType.LAZY)
    public Set<DocumentoAdministrativo> getDocumentosAdministrativos() {
        return documentosAdministrativos;
    }

    public void setDocumentosAdministrativos(Set<DocumentoAdministrativo> documentosAdministrativos) {
        this.documentosAdministrativos = documentosAdministrativos;
    }

    // ************************************
    public boolean addContratosAdministrativos(ContratoAdministrativo... contratosAdministrativos) {
        return this.contratosAdministrativos.addAll(Arrays.asList(contratosAdministrativos));
    }

    public boolean removeContratosAdministrativos(ContratoAdministrativo... contratosAdministrativos) {
        return this.contratosAdministrativos.removeAll(Arrays.asList(contratosAdministrativos));
    }

    public boolean addApensosAdministrativos(ApensoAdministrativo... apensosAdministrativos) {
        return this.apensosAdministrativos.addAll(Arrays.asList(apensosAdministrativos));
    }

    public boolean removeApensosAdministrativos(ApensoAdministrativo... apensosAdministrativos) {
        return this.apensosAdministrativos.removeAll(Arrays.asList(apensosAdministrativos));
    }

    public boolean addDocumentosAdministrativos(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.addAll(Arrays.asList(documentosAdministrativos));
    }

    public boolean removeDocumentosAdministrativos(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.removeAll(Arrays.asList(documentosAdministrativos));
    }

    // ************************************
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
        final ProcessoAdministrativo other = (ProcessoAdministrativo) obj;

        return Objects.equals(this.id, other.id);
    }
}
