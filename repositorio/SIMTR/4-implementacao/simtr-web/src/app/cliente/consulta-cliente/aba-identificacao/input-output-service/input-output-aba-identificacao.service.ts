import { AlertMessageService } from "src/app/services";
import { Input, Output, EventEmitter } from "@angular/core";
import { VinculoCliente } from "src/app/model";

export abstract class InputOutputAbaIdentificacaoService extends AlertMessageService {

    @Input() cliente: VinculoCliente;
    @Input() production: boolean;
    @Input() inserting: boolean;
    @Input() updateClienteForm: boolean;
    @Input() insertingSicli: boolean;
    @Input() sicliInformation: any = null;
    @Input() cpfCnpj: string;
    @Input() selectedRadio: any;
    @Input() userSSO: boolean;
    @Input() userExiste: boolean;
    @Input() dossiePessoaFisica: boolean;
    @Output() onCancel: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Output() afterInsertUser: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Output() onClienteInicado: EventEmitter<VinculoCliente> = new EventEmitter<VinculoCliente>();
    @Output() userCreateSucessSSO: EventEmitter<boolean> = new EventEmitter<boolean>();

}