import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { ApontamentoInput } from "../../model/apontamento-input";
import { ApontamentoOutput } from "../../model/apontamento-output";


export class DialogInputApontamentoReturnResult extends DialogComponent<ApontamentoInput, ApontamentoOutput>  {

    public dialogReturn: ApontamentoOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}