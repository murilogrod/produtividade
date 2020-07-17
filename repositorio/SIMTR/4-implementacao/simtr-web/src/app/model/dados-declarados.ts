import { Atributos } from "./atributos";

export class DadosDeclarados{
        codigo_integracao?: number;
        tipo_documento?: number;
        origem_documento?: string;
        mime_type?: string;
        atributos?: Atributos[];
        imagens?: any[];
        quantidade_conteudos: number;
}