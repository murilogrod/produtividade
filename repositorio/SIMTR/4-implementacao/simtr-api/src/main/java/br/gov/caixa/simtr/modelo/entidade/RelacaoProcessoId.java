package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

public class RelacaoProcessoId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer processoPai;
    private Integer processoFilho;

    public RelacaoProcessoId() {
        super();
    }

    public RelacaoProcessoId(Integer processoPai, Integer processoFilho) {
        super();
        this.processoPai = processoPai;
        this.processoFilho = processoFilho;
    }

    public Integer getProcessoPai() {
        return processoPai;
    }

    public void setProcessoPai(Integer processoPai) {
        this.processoPai = processoPai;
    }

    public Integer getProcessoFilho() {
        return processoFilho;
    }

    public void setProcessoFilho(Integer processoFilho) {
        this.processoFilho = processoFilho;
    }

    // *************************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.processoPai);
        hash = 89 * hash + Objects.hashCode(this.processoFilho);
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
        final RelacaoProcessoId other = (RelacaoProcessoId) obj;
        if (!Objects.equals(this.processoPai, other.processoPai)) {
            return false;
        }

        return Objects.equals(this.processoFilho, other.processoFilho);
    }

}
