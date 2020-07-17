package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb037_regra_documental")
public class RegraDocumental extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ComposicaoDocumental composicaoDocumental;
    private TipoDocumento tipoDocumento;
    private FuncaoDocumental funcaoDocumental;

    // **********************************
    public RegraDocumental() {
        super();
    }

	@Id
    @Column(name = "nu_regra_documental")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ComposicaoDocumental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_composicao_documental", nullable = false)
    public ComposicaoDocumental getComposicaoDocumental() {
        return composicaoDocumental;
    }

    public void setComposicaoDocumental(ComposicaoDocumental composicaoDocumental) {
        this.composicaoDocumental = composicaoDocumental;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = true)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(targetEntity = FuncaoDocumental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_funcao_documental", nullable = true)
    public FuncaoDocumental getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumental funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    // **********************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final RegraDocumental other = (RegraDocumental) obj;
        return Objects.equals(this.id, other.id);
    }

}
