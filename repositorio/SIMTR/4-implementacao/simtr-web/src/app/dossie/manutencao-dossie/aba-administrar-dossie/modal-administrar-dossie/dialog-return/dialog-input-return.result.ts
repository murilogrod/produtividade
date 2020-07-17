import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { AdministrarDossieOutput } from "../../model/administrar-dossie-output";

export class DialogInputReturnResult extends DialogComponent<any, AdministrarDossieOutput>  {

    public dialogReturn: AdministrarDossieOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}