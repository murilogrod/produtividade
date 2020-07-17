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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb047_contrato_adm", indexes = {
    @Index(name = "ix_mtrtb047_01", unique = true, columnList = "nu_contrato, nu_ano_contrato")
})
public class ContratoAdministrativo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ProcessoAdministrativo processoAdministrativo;
    private Integer numeroContrato;
    private Integer anoContrato;
    private String descricaoContrato;
    private Calendar dataHoraInclusao;
    private String matriculaInclusao;
    private String cpfCnpjFornecedor;
    private Integer unidadeOperacional;
    // ************************************
    private Set<ApensoAdministrativo> apensosAdministrativos;
    private Set<DocumentoAdministrativo> documentosAdministrativos;

    public ContratoAdministrativo() {
        super();
        this.apensosAdministrativos = new HashSet<>();
        this.documentosAdministrativos = new HashSet<>();
    }

    @Id
    @Column(name = "nu_contrato_adm")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ProcessoAdministrativo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_adm", nullable = false)
    public ProcessoAdministrativo getProcessoAdministrativo() {
        return processoAdministrativo;
    }

    public void setProcessoAdministrativo(ProcessoAdministrativo processoAdministrativo) {
        this.processoAdministrativo = processoAdministrativo;
    }

    @Column(name = "nu_contrato", nullable = true)
    public Integer getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(Integer numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    @Column(name = "nu_ano_contrato", nullable = true)
    public Integer getAnoContrato() {
        return anoContrato;
    }

    public void setAnoContrato(Integer anoContrato) {
        this.anoContrato = anoContrato;
    }

    @Column(name = "de_contrato", nullable = false, columnDefinition = "text")
    public String getDescricaoContrato() {
        return descricaoContrato;
    }

    public void setDescricaoContrato(String descricaoContrato) {
        this.descricaoContrato = descricaoContrato;
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

    @Column(name = "nu_cpf_cnpj_fornecedor", nullable = false, length = 14)
    public String getCpfCnpjFornecedor() {
        return cpfCnpjFornecedor;
    }

    public void setCpfCnpjFornecedor(String cpfCnpjFornecedor) {
        this.cpfCnpjFornecedor = cpfCnpjFornecedor;
    }

    @Column(name = "nu_unidade_operacional", nullable = false)
    public Integer getUnidadeOperacional() {
        return unidadeOperacional;
    }

    public void setUnidadeOperacional(Integer unidadeOperacional) {
        this.unidadeOperacional = unidadeOperacional;
    }

    // ************************************
    @OneToMany(targetEntity = ApensoAdministrativo.class, mappedBy = "contratoAdministrativo", fetch = FetchType.LAZY)
    public Set<ApensoAdministrativo> getApensosAdministrativos() {
        return apensosAdministrativos;
    }

    public void setApensosAdministrativos(Set<ApensoAdministrativo> apensosAdministrativos) {
        this.apensosAdministrativos = apensosAdministrativos;
    }

    @OneToMany(targetEntity = DocumentoAdministrativo.class, mappedBy = "contratoAdministrativo", fetch = FetchType.LAZY)
    public Set<DocumentoAdministrativo> getDocumentosAdministrativos() {
        return documentosAdministrativos;
    }

    public void setDocumentosAdministrativos(Set<DocumentoAdministrativo> documentosAdministrativos) {
        this.documentosAdministrativos = documentosAdministrativos;
    }

    // ************************************
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
        final ContratoAdministrativo other = (ContratoAdministrativo) obj;
        return Objects.equals(this.id, other.id);
    }

}
