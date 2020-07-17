import { Apontamento } from "../../model/apontamento.model";

export class Apontamentos {

    configTableColsApontamentos: any[] = [
        { field: 'reorder', header: '', class: 'W-5' },
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'nome', header: 'NOME' },
        { field: 'pendencia_informacao', header: 'INFORMAÇÂO', boolean: true },
        { field: 'rejeicao_documento', header: 'REJEIÇÂO', boolean: true },
        { field: 'pendencia_seguranca', header: 'SEGURANÇA', boolean: true },
        { field: 'sequencia_apresentacao', header: 'ORDEM' },
        { field: 'acoes', header: 'AÇÔES' },
    ];
    rowsPerPageOptions: number[] = [];
    apontamentos: Array<Apontamento> = new Array<Apontamento>();
    apontamentosRemovidos: Array<Apontamento> = new Array<Apontamento>();
    filteredRecords: number = 0;
}