import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { CloneInativarRemoverChecklistInput } from "../../model/clone-inativar-remover-checklist-input";
import { CloneInativarRemoverChecklistOutput } from "../../model/clone-inativar-remover-checklist-output";

export class DialogInputCloneInativarRemoverChecklistReturnResult extends DialogComponent<CloneInativarRemoverChecklistInput, CloneInativarRemoverChecklistOutput>  {

    public dialogReturn: CloneInativarRemoverChecklistOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}