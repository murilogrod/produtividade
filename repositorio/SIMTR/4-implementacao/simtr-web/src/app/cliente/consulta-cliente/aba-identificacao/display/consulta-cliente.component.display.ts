import { VinculoCliente } from "src/app/model";

export class ConsultaClienteDisplay {
    cpfCnpj: string;
    searchDone: boolean = false;
    vinculoCliente: VinculoCliente;
    habilitaNovoProduto: boolean = false;
    userFound: boolean = false;
    searching: boolean = false;
    updateClienteForm: boolean = false;
    isResponseCliente: boolean = false;
    isListaProdutos: boolean = false;
}