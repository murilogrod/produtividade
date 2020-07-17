package br.gov.caixa.simtr.controle.vo.vinculacaochecklist;

import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;

public class VinculacaoChecklistValidadaVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Processo processoDossie;
    private Processo processoFase;
    private TipoDocumento tipoDocumento;
    private FuncaoDocumental funcaoDocumental;
    private Checklist checklist;
    private VinculacaoChecklist vinculacaoChecklistAnterior;
    
    public VinculacaoChecklistValidadaVO(Processo processoDossie, Processo processoFase, TipoDocumento tipoDocumento, FuncaoDocumental funcaoDocumental, Checklist checklist, VinculacaoChecklist vinculacaoChecklistAnterior) {
        super();
        this.processoDossie = processoDossie;
        this.processoFase = processoFase;
        this.tipoDocumento = tipoDocumento;
        this.funcaoDocumental = funcaoDocumental;
        this.checklist = checklist;
        this.vinculacaoChecklistAnterior = vinculacaoChecklistAnterior;
    }

    public Processo getProcessoDossie() {
        return processoDossie;
    }

    public void setProcessoDossie(Processo processoDossie) {
        this.processoDossie = processoDossie;
    }

    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public FuncaoDocumental getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumental funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public VinculacaoChecklist getVinculacaoChecklistAnterior() {
        return vinculacaoChecklistAnterior;
    }

    public void setVinculacaoChecklistAnterior(VinculacaoChecklist vinculacaoChecklistAnterior) {
        this.vinculacaoChecklistAnterior = vinculacaoChecklistAnterior;
    }
}
