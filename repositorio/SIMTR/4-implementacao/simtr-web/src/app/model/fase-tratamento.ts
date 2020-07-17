import { RespostaCampoFormulario } from "./resposta-campo-formulario";

export class FaseTratamento {
    id?: number;
    nome?: string;
    instancias_documento?: any[];
    respostas_formulario?: RespostaCampoFormulario[];
}