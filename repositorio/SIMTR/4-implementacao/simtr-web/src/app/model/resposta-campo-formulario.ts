
export class RespostaCampoFormulario{
    id_campo_formulario: number;
    id_processo_fase: number;
    label_campo: string;
    nome_campo: string;
    resposta_aberta: string;
    tipo_campo: string;

    campo_formulario?: number;
    resposta?: string;
    opcoes_selecionadas?: any[];
    ativo?: boolean;

}