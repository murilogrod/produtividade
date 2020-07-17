import { GridTipoRelacionamento } from "./grid-tipo-relacionamento";

export class PesquisaTipoRelacionamento {
    columns: Array<any>;
    configTableColsRelacionamentos: any[] = [
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'nome', header: 'TIPO DE RELACIONAMENTO' },
        { field: 'tipo_pessoa', header: 'TIPO DE PESSOA' },
        { field: 'indicador_principal', header: 'PRINCIPAL' },
        { field: 'indicador_relacionado', header: 'RELACIONADO' },
        { field: 'indicador_sequencia', header: 'SEQUÊNCIA' },
        { field: 'acoes', header: 'AÇÔES', class: 'W-10' },
    ];
    rowsPerPageOptions: number[] = [];
    filteredRecords: number = 0;
    tiposRelacionamentos: Array<GridTipoRelacionamento> = new Array<GridTipoRelacionamento>();
}