import { DocumentoNode } from "./documentoNode";
import { ObrigatorioArvore } from "./obrigatorio-arvore";
import { FuncaoDocumental, TipoDocumento } from ".";
import { VinculoCliente } from "./model-arvore-generica-dossie-produto/vinculos-model/vinculo-cliente";

export class ArvoreDinamica{
    cliente?: VinculoCliente;
    ic_tipo_vinculo?: string;
    tipo_vinculo?: string;
    campo?: string;
    valor?: string;
    documentos: DocumentoNode[];
    obrigatorios: ObrigatorioArvore[];
    funcoes_documentais?: FuncaoDocumental[];
    tipos_documentos?: TipoDocumento[];
    idVinculo?: number;
}