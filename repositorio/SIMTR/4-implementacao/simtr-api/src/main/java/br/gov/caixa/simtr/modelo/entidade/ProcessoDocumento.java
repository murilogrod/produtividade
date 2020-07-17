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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb025_processo_documento")
public class ProcessoDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Processo processo;
    private FuncaoDocumental funcaoDocumental;
    private TipoDocumento tipoDocumento;
    private TipoRelacionamento tipoRelacionamento;
    private Boolean obrigatorio;

    // **********************************
    public ProcessoDocumento() {
        super();
        this.obrigatorio = Boolean.FALSE;
    }

    @Id
    @Column(name = "nu_processo_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo", nullable = false)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @ManyToOne(targetEntity = FuncaoDocumental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_funcao_documental", nullable = true)
    public FuncaoDocumental getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumental funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = true)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(targetEntity = TipoRelacionamento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_relacionamento", nullable = false)
    public TipoRelacionamento getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    @Column(name = "ic_obrigatorio", nullable = false)
    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
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
        final ProcessoDocumento other = (ProcessoDocumento) obj;
        return Objects.equals(this.id, other.id);
    }

}
