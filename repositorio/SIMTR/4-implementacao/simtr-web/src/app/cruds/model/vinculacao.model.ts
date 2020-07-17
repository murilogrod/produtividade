export class Vinculacao {
    id: number;
    data_revogacao: Date;
    id_funcao_documental: number;
    id_processo_dossie: number;
    id_processo_fase: number;
    id_tipo_documento: number;
    clone: boolean = false;
    idVinculacaoConflitante: number;
    idChecklistAssociadoVinculacao: number;
    dataRevogacaoVinculacaoConflitante: Date;
}