package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb056_parecer")
public class Parecer extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Apontamento apontamento;
    private Verificacao verificacao;
    private Boolean indicacaoAprovado;
    private String comentarioTratamento;
    private String comentarioContestacao;
    private Boolean indicacaoContestacao;
    // *********************************************

    public Parecer() {
        super();
    }

    @Id
    @Column(name = "nu_parecer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Apontamento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_apontamento", nullable = false)
    public Apontamento getApontamento() {
        return apontamento;
    }

    public void setApontamento(Apontamento apontamento) {
        this.apontamento = apontamento;
    }

    @ManyToOne(targetEntity = Verificacao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_verificacao", nullable = false)
    public Verificacao getVerificacao() {
        return verificacao;
    }

    public void setVerificacao(Verificacao verificacao) {
        this.verificacao = verificacao;
    }

    @Column(name = "ic_aprovado", nullable = false)
    public Boolean getIndicacaoAprovado() {
        return indicacaoAprovado;
    }

    public void setIndicacaoAprovado(Boolean indicacaoAprovado) {
        this.indicacaoAprovado = indicacaoAprovado;
    }

    @Column(name = "de_comentario_tratamento", nullable = true, columnDefinition = "text")
    public String getComentarioTratamento() {
        return comentarioTratamento;
    }

    public void setComentarioTratamento(String comentarioTratamento) {
        this.comentarioTratamento = comentarioTratamento;
    }

    @Column(name = "de_comentario_contestacao", nullable = true, columnDefinition = "text")
    public String getComentarioContestacao() {
        return comentarioContestacao;
    }

    public void setComentarioContestacao(String comentarioContestacao) {
        this.comentarioContestacao = comentarioContestacao;
    }

    @Column(name = "ic_contestacao_acatada", nullable = true)
    public Boolean getIndicacaoContestacao() {
        return indicacaoContestacao;
    }

    public void setIndicacaoContestacao(Boolean indicacaoContestacao) {
        this.indicacaoContestacao = indicacaoContestacao;
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.apontamento);
        hash = 53 * hash + Objects.hashCode(this.verificacao);
        hash = 53 * hash + Objects.hashCode(this.indicacaoAprovado);
        hash = 53 * hash + Objects.hashCode(this.comentarioTratamento);
        hash = 53 * hash + Objects.hashCode(this.comentarioContestacao);
        hash = 53 * hash + Objects.hashCode(this.indicacaoContestacao);
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
        final Parecer other = (Parecer) obj;
        if (!Objects.equals(this.comentarioTratamento, other.comentarioTratamento)) {
            return false;
        }
        if (!Objects.equals(this.comentarioContestacao, other.comentarioContestacao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.apontamento, other.apontamento)) {
            return false;
        }
        if (!Objects.equals(this.verificacao, other.verificacao)) {
            return false;
        }
        if (!Objects.equals(this.indicacaoAprovado, other.indicacaoAprovado)) {
            return false;
        }
        return Objects.equals(this.indicacaoContestacao, other.indicacaoContestacao);
    }

}
