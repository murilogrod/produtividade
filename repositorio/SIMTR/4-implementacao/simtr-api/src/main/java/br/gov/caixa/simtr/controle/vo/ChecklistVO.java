package br.gov.caixa.simtr.controle.vo;

import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.Checklist;

public class ChecklistVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Checklist checklist;
    private Long quantidadeAssociacoes;
    
    
    public ChecklistVO(Checklist checklist, Long quantidadeAssociacoes) {
        super();
        this.checklist = checklist;
        this.quantidadeAssociacoes = quantidadeAssociacoes;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public Long getQuantidadeAssociacoes() {
        return quantidadeAssociacoes;
    }

    public void setQuantidadeAssociacoes(Long quantidadeAssociacoes) {
        this.quantidadeAssociacoes = quantidadeAssociacoes;
    }

}
