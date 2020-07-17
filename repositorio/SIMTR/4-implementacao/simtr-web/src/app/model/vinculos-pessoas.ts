import { RespostaFormulario } from "../dossie/manutencao-dossie/model-endPoint-dossie-produto/respostaFormulario";

export class VinculoPessoas {
    dossie_cliente: number;
    dossie_cliente_relacionado?: number;
    dossie_cliente_relacionado_anterior?: number;
    sequencia_titularidade?: number;
    sequencia_titularidade_anterior?: number;
    tipo_relacionamento?: number;
    documentos_utilizados?: any[];
    documentos_novos?: any[];
    exclusao?: boolean;
    respostas_formulario?: RespostaFormulario[];
}