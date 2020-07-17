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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb057_opcao_atributo", indexes = {
    @Index(name = "ix_mtrtb057_01", unique = true, columnList = "nu_atributo_extracao, no_value"),
})
public class OpcaoAtributo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private AtributoExtracao atributoExtracao;
    private String valorOpcao;
    private String descricaoOpcao;
    
    //*************************************

    public OpcaoAtributo() {
        super();
    }

    @Id
    @Column(name = "nu_opcao_atributo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = AtributoExtracao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_atributo_extracao", nullable = false)
    public AtributoExtracao getAtributoExtracao() {
        return atributoExtracao;
    }

    public void setAtributoExtracao(AtributoExtracao atributoExtracao) {
        this.atributoExtracao = atributoExtracao;
    }

    

    @Column(name = "no_value", nullable = false, length = 50)
    public String getValorOpcao() {
        return valorOpcao;
    }

    public void setValorOpcao(String valorOpcao) {
        this.valorOpcao = valorOpcao;
    }

    @Column(name = "no_opcao", nullable = false, length = 255)
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
        final OpcaoAtributo other = (OpcaoAtributo) obj;
        return Objects.equals(this.id, other.id);
    }
}
