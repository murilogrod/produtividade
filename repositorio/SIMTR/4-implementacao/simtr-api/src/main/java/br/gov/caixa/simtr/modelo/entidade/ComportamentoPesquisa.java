package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

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
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb044_comportamento_pesquisa", indexes = {
    @Index(name = "ix_mtrtb044_01", unique = true, columnList = "nu_produto, ic_sistema_retorno, ic_codigo_retorno")
})
public class ComportamentoPesquisa extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Produto produto;
    private SistemaPesquisaEnum sistemaRetorno;
    private SistemaPesquisaTipoRetornoEnum valorCodigoRetorno;
    private Boolean bloqueio;
    private String orientacao;

    //****************************************
    @Id
    @Column(name = "nu_comportamento_pesquisa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_produto", nullable = false)
    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_sistema_retorno", nullable = false, length = 10)
    public SistemaPesquisaEnum getSistemaRetorno() {
        return sistemaRetorno;
    }

    public void setSistemaRetorno(SistemaPesquisaEnum sistemaRetorno) {
        this.sistemaRetorno = sistemaRetorno;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_codigo_retorno", nullable = false, length = 10)
    public SistemaPesquisaTipoRetornoEnum getValorCodigoRetorno() {
        return valorCodigoRetorno;
    }

    public void setValorCodigoRetorno(SistemaPesquisaTipoRetornoEnum valorCodigoRetorno) {
        this.valorCodigoRetorno = valorCodigoRetorno;
    }

    @Column(name = "ic_bloqueio", nullable = false)
    public Boolean getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(Boolean bloqueio) {
        this.bloqueio = bloqueio;
    }

    @Column(name = "de_orientacao", nullable = true, columnDefinition = "text")
    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    //****************************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final ComportamentoPesquisa other = (ComportamentoPesquisa) obj;
        return Objects.equals(this.id, other.id);
    }

}
