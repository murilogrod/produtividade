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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb101_documento")
public class DocumentoAutorizacao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Autorizacao autorizacao;
    private String codigoDocumentoGED;
    private String finalidade;

    // ************************************
    public DocumentoAutorizacao() {
        super();
    }

    @Id
    @Column(name = "nu_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Autorizacao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_autorizacao", nullable = false)
    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    @Column(name = "co_documento_ged", length = 100, nullable = false)
    public String getCodigoDocumentoGED() {
        return codigoDocumentoGED;
    }

    public void setCodigoDocumentoGED(String codigoDocumentoGED) {
        this.codigoDocumentoGED = codigoDocumentoGED;
    }

    @Column(name = "de_finalidade", length = 100, nullable = false)
    public String getFinalidade() {
        return this.finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.autorizacao);
        hash = 29 * hash + Objects.hashCode(this.codigoDocumentoGED);
        hash = 29 * hash + Objects.hashCode(this.finalidade);
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
        final DocumentoAutorizacao other = (DocumentoAutorizacao) obj;
        if (!Objects.equals(this.finalidade, other.finalidade)) {
            return false;
        }
        if (!Objects.equals(this.autorizacao, other.autorizacao)) {
            return false;
        }
        
        return Objects.equals(this.codigoDocumentoGED, other.codigoDocumentoGED);
    }
    
}
