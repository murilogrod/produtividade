export class Apontamento {
    id: number;
    nome: string;
    descricao: string;
    orientacao_operador: string;
    pendencia_informacao: boolean;
    pendencia_seguranca: boolean;
    rejeicao_documento: boolean;
    sequencia_apresentacao: number;
    clone: boolean = false;
    tecla_atalho: string;
}