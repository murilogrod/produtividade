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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM002, name = "mtrtb008_conteudo")
public class Conteudo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Documento documento;
    private String base64;
    private Integer ordem;
    // *********************************************

    public Conteudo() {
        super();
    }

    public Conteudo(String base64, Integer ordem) {
        this.base64 = base64;
        this.ordem = ordem;
    }
    
    public Conteudo(Documento documento, String base64, Integer ordem) {
        this.documento = documento;
        this.base64 = base64;
        this.ordem = ordem;
    }
    
    @Id
    @Column(name = "nu_conteudo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Documento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_documento", nullable = false)
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Column(name = "de_conteudo", nullable = false, columnDefinition = "text")
    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Column(name = "nu_ordem", nullable = false)
    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    // *********************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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

        final Conteudo other = (Conteudo) obj;
        if (null == this.id || null == other.id) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

}
