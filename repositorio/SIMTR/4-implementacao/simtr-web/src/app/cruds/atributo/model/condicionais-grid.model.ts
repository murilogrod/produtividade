import {CondicionalModel} from 'src/app/cruds/atributo/model/condicional.model';

export class CondicionaisGridModel {

    configTableColsCondicionais: any[] = [
        { field: 'reorder', header: '', class: 'W-5' },
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'nome', header: 'ATRIBUTO' },
        { field: 'valor_resposta', header: 'VALOR RESPOSTA', boolean: true },
        { field: 'agrupamento_expressao', header: 'AGRUPAMENTO EXPRESSÃO', boolean: true },
        { field: 'acoes', header: 'AÇÔES' },
    ];
    rowsPerPageOptions: number[] = [];
    condicionais: Array<CondicionalModel> = new Array<CondicionalModel>();
    condicionaisRemovidos: Array<CondicionalModel> = new Array<CondicionalModel>();
    filteredRecords: number = 0;
}