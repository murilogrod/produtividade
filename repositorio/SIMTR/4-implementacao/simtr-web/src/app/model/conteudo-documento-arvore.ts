import { TipoDocumentoArvoreGenerica } from "./model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";

export class ConteudoDocumentoArvore {
    codigo_ged?: string;
    formato?: string;
    id?: number;
    sequencia_apresentacao?: number;
    uri?: string[];
    data_captura?: string;
    matricula?: string;
    data_validade?: string;
    docReutilizado: boolean;
    tipo?: TipoDocumentoArvoreGenerica;
}