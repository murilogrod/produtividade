import { Socio } from "./socio.model";

export class DataInputReceitaFederal {
    cnpj: string;
    relacionamento: string;
    sociosReceitaFederal: Array<Socio>
    clienteLista: any[];
    listaTipoRelacionamento: any[];
    habilitaAlteracao: boolean;
    socioMountError: boolean;
    messageErrorMountSocio: string;
}
