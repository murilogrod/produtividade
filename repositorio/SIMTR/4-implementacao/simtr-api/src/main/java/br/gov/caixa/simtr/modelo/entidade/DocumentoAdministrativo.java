package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb049_documento_adm")
public class DocumentoAdministrativo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Documento documento;
    private DocumentoAdministrativo documentoSubstituto;
    private ProcessoAdministrativo processoAdministrativo;
    private ContratoAdministrativo contratoAdministrativo;
    private ApensoAdministrativo apensoAdministrativo;
    private Boolean valido;
    private Boolean confidencial;
    private String descricao;
    private String justificativaSubstituicao;
    private Calendar dataHoraExclusao;
    private String matriculaExclusao;
    private String justificativaExclusao;
    // ************************************

    public DocumentoAdministrativo() {
        super();
        this.valido = Boolean.TRUE;
    }

    @Id
    @Column(name = "nu_documento_adm")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Documento.class, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "nu_documento", nullable = false)
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @OneToOne(targetEntity = DocumentoAdministrativo.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nu_documento_substituto", nullable = true)
    public DocumentoAdministrativo getDocumentoSubstituto() {
        return documentoSubstituto;
    }

    public void setDocumentoSubstituto(DocumentoAdministrativo documentoSubstituto) {
        this.documentoSubstituto = documentoSubstituto;
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

    @ManyToOne(targetEntity = ApensoAdministrativo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_apenso_adm", nullable = true)
    public ApensoAdministrativo getApensoAdministrativo() {
        return apensoAdministrativo;
    }

    public void setApensoAdministrativo(ApensoAdministrativo apensoAdministrativo) {
        this.apensoAdministrativo = apensoAdministrativo;
    }

    @Column(name = "ic_valido", nullable = false)
    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    @Column(name = "ic_confidencial", nullable = false)
    public Boolean getConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    @Column(name = "de_documento_adm", nullable = true, columnDefinition = "text")
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "de_justificativa_substituicao", nullable = true, columnDefinition = "text")
    public String getJustificativaSubstituicao() {
        return justificativaSubstituicao;
    }

    public void setJustificativaSubstituicao(String justificativaSubstituicao) {
        this.justificativaSubstituicao = justificativaSubstituicao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_exclusao", nullable = true)
    public Calendar getDataHoraExclusao() {
        return dataHoraExclusao;
    }

    public void setDataHoraExclusao(Calendar dataHoraExclusao) {
        this.dataHoraExclusao = dataHoraExclusao;
    }

    @Column(name = "co_matricula_exclusao", nullable = true, length = 7)
    public String getMatriculaExclusao() {
        return matriculaExclusao;
    }

    public void setMatriculaExclusao(String matriculaExclusao) {
        this.matriculaExclusao = matriculaExclusao;
    }

    @Column(name = "de_justificativa_exclusao", nullable = true, columnDefinition = "text")
    public String getJustificativaExclusao() {
        return justificativaExclusao;
    }

    public void setJustificativaExclusao(String justificativaExclusao) {
        this.justificativaExclusao = justificativaExclusao;
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
        final DocumentoAdministrativo other = (DocumentoAdministrativo) obj;
        return Objects.equals(this.id, other.id);
    }

}
