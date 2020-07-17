export class TipoRelacionamento {
    id: number;
    nome: string;
    tipo_pessoa: string;
    indicador_principal: boolean = false;
    indicador_relacionado: boolean = false;
    indicador_sequencia: boolean = false;
    indicador_receita_pf: boolean = false;
    indicador_receita_pj: boolean = false;
    ultima_alteracao: string;
}