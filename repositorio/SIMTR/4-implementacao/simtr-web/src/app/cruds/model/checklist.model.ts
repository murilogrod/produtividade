import { Apontamento } from "./apontamento.model";
import { Vinculacao } from "./vinculacao.model";

export class Checklist {
    id: number;
    data_hora_criacao: string;
    data_hora_inativacao: string;
    data_hora_ultima_alteracao: string;
    nome?: string;
    unidade_responsavel: number;
    verificacao_previa: boolean = false;
    orientacao_operador: string;
    quantidade_associacoes: number;
    apontamentos: Array<Apontamento> = new Array<Apontamento>();
    vinculacoes: Array<Vinculacao> = new Array<Vinculacao>();
}