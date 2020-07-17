import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { SocioReceitaFederalOutput } from "../model/socio-receita-federal-output";
import { ReceitaFederalInput } from "../../model/receita-federal-input";
import { LoaderService } from "src/app/services";

export class DialogInputReturnResult extends DialogComponent<ReceitaFederalInput, SocioReceitaFederalOutput>  {

    public dialogReturn: SocioReceitaFederalOutput;

    constructor(dialogService: DialogService) {
        super(dialogService);
    }

    public closeDialog() {
        this.result = this.dialogReturn;
        this.close();
    }
}