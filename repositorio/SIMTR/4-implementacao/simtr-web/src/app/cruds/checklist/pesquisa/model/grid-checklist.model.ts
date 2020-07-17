import { Vinculacao } from "../../../model/vinculacao.model";

export class GridChecklist {
    data_hora_criacao: string;
    data_hora_inativacao: string;
    id: number;
    nome: string;
    verificacao_previa: boolean;
    quantidade_apontamentos: number;
    quantidade_associacoes: number;
    unidade_responsavel: boolean;
    vinculacoes: Array<Vinculacao>;
}