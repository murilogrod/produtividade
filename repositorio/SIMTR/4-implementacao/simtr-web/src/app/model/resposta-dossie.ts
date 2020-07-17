import { CampoFormulario, OpcaoCampo } from ".";

export class RespostaDossie {
    id : number;
    campo_formulario : CampoFormulario;
    opcao_campo?: OpcaoCampo;
    resposta?: any;
}