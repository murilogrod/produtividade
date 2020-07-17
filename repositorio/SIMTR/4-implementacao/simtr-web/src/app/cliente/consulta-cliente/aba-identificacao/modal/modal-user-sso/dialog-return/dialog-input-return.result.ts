import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { VinculoClienteModel } from "../model/vinculo-cliente.model";
import { UserSSOOutput } from "../model/user-sso.output";

export class DialogInputReturnResult extends DialogComponent<VinculoClienteModel, UserSSOOutput>  {

    public dialogReturn: UserSSOOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}