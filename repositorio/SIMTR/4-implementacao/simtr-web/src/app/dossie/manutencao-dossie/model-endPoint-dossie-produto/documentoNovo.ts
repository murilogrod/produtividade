import { Atributos } from "src/app/model/atributos";

export class DocumentoNovo { 
    tipo_documento?: number;
    origem_documento?: string;
    mime_type?: string;
    binario?: string;
    atributos?: Atributos[];
    quantidade_conteudos: number;
}
