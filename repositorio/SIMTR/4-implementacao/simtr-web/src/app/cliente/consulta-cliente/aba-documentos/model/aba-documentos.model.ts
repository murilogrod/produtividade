import { Utils } from "src/app/utils/Utils";
import { ElementRef } from "@angular/core";

export class AbaDocumentos {
    cpfDossieCliente: string
    cartaoAssinatura: ElementRef;

    /**
     * Retorna o cpf sem formatação
     */
    getCPFDossieClienteSemFormatacao(): string {
        return Utils.retiraMascaraCPF(this.cpfDossieCliente);
    }
}