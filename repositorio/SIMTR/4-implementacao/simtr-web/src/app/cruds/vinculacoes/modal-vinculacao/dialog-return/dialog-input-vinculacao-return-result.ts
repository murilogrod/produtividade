import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { VinculacaoInput } from "../../model/vinculacao-input";
import { VinculacaoOutput } from "../../model/vinculacao-output";

export class DialogInputVinculacaoReturnResult extends DialogComponent<VinculacaoInput, VinculacaoOutput>  {

    public dialogReturn: VinculacaoOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}