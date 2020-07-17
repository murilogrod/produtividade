import { Documento } from ".";
import { ElementoConteudo } from "./elemento-conteudo";

export class TipoDocumento {
    id?: number;
    nome?: string;
    tipo_pessoa?: string;
    prazo_validade?: number;
    validade_autocontida?: boolean;
    codigo_tipologia?: string;
    documentos?: Documento[];
    funcoes_documentais?: any[];
    elemento?: ElementoConteudo;
    regras_documentais?: any[];
    processo_documentos?: any[];
}