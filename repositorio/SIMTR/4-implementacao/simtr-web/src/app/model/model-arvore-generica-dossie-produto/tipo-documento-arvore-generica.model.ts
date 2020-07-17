import { ElementoConteudo } from "../elemento-conteudo";
import { DocumentoArvore } from "../documento-arvore";

export class TipoDocumentoArvoreGenerica {
    id?: number;
    nome?: string;
    indexArvore: number;
    idNodeApresentacao: number;
    tipo_pessoa?: string;
    prazo_validade?: number;
    codigo_tipologia?: string;
    obrigatorio?: boolean;
    funcoes_documentais?: any[];
    documentos?: DocumentoArvore[];
    elemento?: ElementoConteudo;
    permite_reuso: boolean;
}