package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb043_documento_garantia", indexes = {
    @Index(name = "ix_mtrtb043_01", unique = true, columnList = "nu_garantia, nu_processo")
})
public class DocumentoGarantia extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Garantia garantia;
    private Processo processo;
    private TipoDocumento tipoDocumento;
    private FuncaoDocumental funcaoDocumental;

    //******************************************
    public DocumentoGarantia() {
        super();
    }

    @Id
    @Column(name = "nu_documento_garantia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Garantia.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_garantia", nullable = false)
    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo", nullable = false)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = false)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(targetEntity = FuncaoDocumental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_funcao_documental", nullable = false)
    public FuncaoDocumental getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumental funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    //******************************************
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
        final DocumentoGarantia other = (DocumentoGarantia) obj;
        return Objects.equals(this.id, other.id);
    }

}
