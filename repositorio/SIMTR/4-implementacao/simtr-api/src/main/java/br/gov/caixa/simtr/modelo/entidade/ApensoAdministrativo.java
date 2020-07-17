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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb048_apenso_adm", indexes = {
    @Index(name = "ix_mtrtb048_01", unique = true, columnList = "co_protocolo_siclg")
    ,
    @Index(name = "ix_mtrtb048_02", unique = true, columnList = "nu_processo_adm, no_titulo")
    ,
    @Index(name = "ix_mtrtb048_03", unique = true, columnList = "nu_contrato_adm, no_titulo")
})
public class ApensoAdministrativo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ProcessoAdministrativo processoAdministrativo;
    private ContratoAdministrativo contratoAdministrativo;
    private String cpfCnpjFornecedor;
    private TipoApensoEnum tipoApenso;
    private String descricaoApenso;
    private Calendar dataHoraInclusao;
    private String matriculaInclusao;
    private String protocoloSICLG;
    private String titulo;
    // ************************************
    private Set<DocumentoAdministrativo> documentosAdministrativos;

    public ApensoAdministrativo() {
        super();
        this.documentosAdministrativos = new HashSet<>();
    }

    @Id
    @Column(name = "nu_apenso_adm")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ProcessoAdministrativo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_adm", nullable = true)
    public ProcessoAdministrativo getProcessoAdministrativo() {
        return processoAdministrativo;
    }

    public void setProcessoAdministrativo(ProcessoAdministrativo processoAdministrativo) {
        this.processoAdministrativo = processoAdministrativo;
    }

    @ManyToOne(targetEntity = ContratoAdministrativo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_contrato_adm", nullable = true)
    public ContratoAdministrativo getContratoAdministrativo() {
        return contratoAdministrativo;
    }

    public void setContratoAdministrativo(ContratoAdministrativo contratoAdministrativo) {
        this.contratoAdministrativo = contratoAdministrativo;
    }

    @Column(name = "nu_cpf_cnpj_fornecedor", nullable = true, length = 14)
    public String getCpfCnpjFornecedor() {
        return cpfCnpjFornecedor;
    }

    public void setCpfCnpjFornecedor(String cpfCnpjFornecedor) {
        this.cpfCnpjFornecedor = cpfCnpjFornecedor;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_apenso", nullable = false, length = 2)
    public TipoApensoEnum getTipoApenso() {
        return tipoApenso;
    }

    public void setTipoApenso(TipoApensoEnum tipoApenso) {
        this.tipoApenso = tipoApenso;
    }

    @Column(name = "de_apenso", nullable = true, columnDefinition = "text")
    public String getDescricaoApenso() {
        return descricaoApenso;
    }

    public void setDescricaoApenso(String descricaoApenso) {
        this.descricaoApenso = descricaoApenso;
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

    @Column(name = "co_protocolo_siclg", nullable = true, length = 255)
    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    @Column(name = "no_titulo", nullable = true, length = 100)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // ************************************
    @OneToMany(targetEntity = DocumentoAdministrativo.class, mappedBy = "apensoAdministrativo", fetch = FetchType.LAZY)
    public Set<DocumentoAdministrativo> getDocumentosAdministrativos() {
        return documentosAdministrativos;
    }

    public void setDocumentosAdministrativos(Set<DocumentoAdministrativo> documentosAdministrativos) {
        this.documentosAdministrativos = documentosAdministrativos;
    }

    // ************************************
    public boolean addDocumentosAdministrativos(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.addAll(Arrays.asList(documentosAdministrativos));
    }

    public boolean removeDocumentosAdministrativos(DocumentoAdministrativo... documentosAdministrativos) {
        return this.documentosAdministrativos.removeAll(Arrays.asList(documentosAdministrativos));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 5;
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
        final ApensoAdministrativo other = (ApensoAdministrativo) obj;
        return Objects.equals(this.id, other.id);
    }
}
