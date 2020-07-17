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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb040_opcao_selecionada")
public class OpcaoSelecionada extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private AtributoDocumento atributoDocumento;
    private String valorOpcao;
    private String descricaoOpcao;

    public OpcaoSelecionada() {
        super();
    }

    @Id
    @Column(name = "nu_opcao_selecionada")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = AtributoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_atributo_documento", nullable = false)
    public AtributoDocumento getAtributoDocumento() {
        return atributoDocumento;
    }

    public void setAtributoDocumento(AtributoDocumento atributoDocumento) {
        this.atributoDocumento = atributoDocumento;
    }

    @Column(name = "no_value", nullable = false, length = 50)
    public String getValorOpcao() {
        return valorOpcao;
    }

    public void setValorOpcao(String valorOpcao) {
        this.valorOpcao = valorOpcao;
    }

    @Column(name = "no_opcao", nullable = false, length = 50)
    public String getDescricaoOpcao() {
        return descricaoOpcao;
    }

    public void setDescricaoOpcao(String descricaoOpcao) {
        this.descricaoOpcao = descricaoOpcao;
    }

    //*************************************    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public String toString() {
        return this.valorOpcao;
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
        final OpcaoSelecionada other = (OpcaoSelecionada) obj;
        return Objects.nonNull(this.id) && Objects.nonNull(other.id) && Objects.equals(this.id, other.id);
    }
}
