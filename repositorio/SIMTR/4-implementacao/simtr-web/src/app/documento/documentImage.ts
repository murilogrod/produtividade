import { FonteDocumento } from "./enum-fonte-documento/fonte-documento.enum";

export class DocumentImage {
    image?: string;
    source: FonteDocumento;
    binario?:string;
    name?: string;
    primeiraPagina?: boolean;
    checked: boolean;
    ativo?: string;
    id?: string;
    data?: any;
    index?: number;
    indiceDocListPdfOriginal?: number;
    type?: string;
    excluded?: boolean;
    label?: string;
    oculto?:boolean;
    reclassificar?:boolean;
    sequencia_apresentacao?: number;
    altura?: number;
    largura?: number;
    totalPaginas?: number;
    paginaAtual?: number;
    tipoDocumentoId?: number;
    atributos?: any[];
    analise_outsourcing?: boolean;
}