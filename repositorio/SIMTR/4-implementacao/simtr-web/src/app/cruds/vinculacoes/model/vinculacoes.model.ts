import { GridVinculacao } from "./grid-vinculacao.model";
import { Vinculacao } from "../../model/vinculacao.model";

export class Vinculacoes {
    configTableColsVinculacoes: any[] = [
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'processo', header: 'PROCESSO' },
        { field: 'fase', header: 'FASE' },
        { field: 'tipoDocumento', header: 'TIPO DOCUMENTO' },
        { field: 'funcaoDocumental', header: 'FUNCÃO', tooltip: true, msg: 'FUNÇÃO DOCUMENTAL' },
        { field: 'dataRevogacao', header: 'DATA REVOGAÇÃO' },
        { field: 'acoes', header: 'AÇÔES' },
    ];
    rowsPerPageOptions: number[] = [];
    vinculacoesGrid: Array<GridVinculacao>;
    vinculacoes: Array<Vinculacao> = new Array<Vinculacao>();
    vinculacoesParaRemocao: Array<Vinculacao> = new Array<Vinculacao>();
    filteredRecords: number = 0;
}