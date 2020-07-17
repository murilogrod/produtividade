import { DocumentoNovo } from "./documentoNovo";
import { RespostaFormulario } from "./respostaFormulario";

export class VinculosPessoas {
    dossie_cliente?: number;
    dossie_cliente_relacionado?: number;
    sequencia_titularidade?: number;
    tipo_relacionamento?: number;
    documentos_utilizados?: any[];
    documentos_novos?: DocumentoNovo[];
    exclusao?: boolean;
    dossie_cliente_relacionado_anterior?: number;
    sequencia_titularidade_anterior?: number;
    respostas_formulario?: RespostaFormulario[];
}