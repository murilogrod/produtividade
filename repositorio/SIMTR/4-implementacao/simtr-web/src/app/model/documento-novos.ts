import { Atributos } from './atributos';
import { TipoDocumento } from './tipo-documento';
import { Conteudo } from './conteudos';

export class DocumentoNovos {
    
    id?: number;
    obrigatorio?: boolean;
    exigencia_validacao?: boolean;
    identificador_elemento?: number;
    tipo_documento?: TipoDocumento;
    mime_type?: any[];
    atributos?: Atributos[];
    conteudos?: Conteudo[];
    origem_documento?: string;
    indiceDocListPdfOriginal?: number;
}