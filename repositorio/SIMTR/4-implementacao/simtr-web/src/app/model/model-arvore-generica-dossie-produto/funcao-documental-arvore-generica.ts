import { TipoDocumentoArvoreGenerica } from "./tipo-documento-arvore-generica.model";

export class FuncaoDocumentalArvoreGenerica
 {
    id?: number;
    nome?: string;
    ativo?: string;
    tipos_documento?: TipoDocumentoArvoreGenerica[];
    regras_documentais?: any[];
    processo_documentos?: any[];
}