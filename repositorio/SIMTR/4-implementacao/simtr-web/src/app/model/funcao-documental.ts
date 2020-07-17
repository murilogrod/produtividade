import { TipoDocumento } from ".";

export class FuncaoDocumental
 {
    id?: number;
    nome?: string;
    ativo?: string;
    tipos_documento?: TipoDocumento[];
    regras_documentais?: any[];
    processo_documentos?: any[];
}