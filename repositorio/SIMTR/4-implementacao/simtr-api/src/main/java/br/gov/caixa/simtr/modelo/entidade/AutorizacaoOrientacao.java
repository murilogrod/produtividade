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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb103_autorizacao_orientacao")
public class AutorizacaoOrientacao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Autorizacao autorizacao;
    private SistemaPesquisaEnum sistemaPesquisa;
    private String descricaoOrientacao;

    public AutorizacaoOrientacao() {
        super();
    }

    @Id
    @Column(name = "nu_autorizacao_orientacao")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_sistema", nullable = false, length = 10)
    public SistemaPesquisaEnum getSistemaPesquisa() {
        return sistemaPesquisa;
    }

    public void setSistemaPesquisa(SistemaPesquisaEnum sistemaPesquisa) {
        this.sistemaPesquisa = sistemaPesquisa;
    }

    @Column(name = "de_orientacao", nullable = false, columnDefinition = "text")
    public String getDescricaoOrientacao() {
        return descricaoOrientacao;
    }

    public void setDescricaoOrientacao(String descricaoOrientacao) {
        this.descricaoOrientacao = descricaoOrientacao;
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.autorizacao);
        hash = 37 * hash + Objects.hashCode(this.sistemaPesquisa);
        hash = 37 * hash + Objects.hashCode(this.descricaoOrientacao);
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
        final AutorizacaoOrientacao other = (AutorizacaoOrientacao) obj;
        if (!Objects.equals(this.descricaoOrientacao, other.descricaoOrientacao)) {
            return false;
        }
        if (!Objects.equals(this.autorizacao, other.autorizacao)) {
            return false;
        }
        
        return this.sistemaPesquisa != other.sistemaPesquisa;
    }

}
