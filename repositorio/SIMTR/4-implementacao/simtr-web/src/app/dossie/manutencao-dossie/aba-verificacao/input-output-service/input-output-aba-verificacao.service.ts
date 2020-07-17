import { AlertMessageService } from "src/app/services";
import { Input, ViewChild, ElementRef } from "@angular/core";

export abstract class InputOutputAbaVerificacaoService extends AlertMessageService {

    @Input() listaComboFase: any[];
    @Input() listaVerificacao: any[];
    @Input() listaVinculoArvore: any[];
    @Input() listaAbaVerificacao: any[];
    @ViewChild('gb') gb: ElementRef;

}