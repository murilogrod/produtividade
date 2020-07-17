package br.gov.caixa.simtr.controle.vo.checklist;

import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChecklistPendenteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Checklist checklist;
    private Long identificadorInstanciaDocumento;
    // *********************************************
    private List<Apontamento> apontamentosAusentes;

    public ChecklistPendenteVO() {
        super();
        this.apontamentosAusentes = new ArrayList<>();
    }

    public Long getIdentificadorInstanciaDocumento() {
        return identificadorInstanciaDocumento;
    }

    public void setIdentificadorInstanciaDocumento(Long identificadorInstanciaDocumento) {
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public List<Apontamento> getApontamentosAusentes() {
        return apontamentosAusentes;
    }

    public void setApontamentosAusentes(List<Apontamento> apontamentosAusentes) {
        this.apontamentosAusentes = apontamentosAusentes;
    }
}
