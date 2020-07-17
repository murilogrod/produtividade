package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@IdClass(RelacaoProcessoId.class)
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb026_relacao_processo", indexes = {
    @Index(name = "ix_mtrtb026_01", unique = true, columnList = "nu_processo_pai, nu_ordem")
})
public class RelacaoProcesso extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Processo processoPai;
    private Processo processoFilho;
    private Integer prioridade;
    private Integer ordem;

    @Id
    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_pai", nullable = false)
    public Processo getProcessoPai() {
        return processoPai;
    }

    public void setProcessoPai(Processo processoPai) {
        this.processoPai = processoPai;
    }

    @Id
    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_filho", nullable = false)
    public Processo getProcessoFilho() {
        return processoFilho;
    }

    public void setProcessoFilho(Processo processoFilho) {
        this.processoFilho = processoFilho;
    }

    @Column(name = "nu_prioridade", nullable = false)
    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
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
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.processoPai);
        hash = 29 * hash + Objects.hashCode(this.processoFilho);
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
        final RelacaoProcesso other = (RelacaoProcesso) obj;
        if (!Objects.equals(this.processoPai, other.processoPai)) {
            return false;
        }
        return Objects.equals(this.processoFilho, other.processoFilho);
    }

}
