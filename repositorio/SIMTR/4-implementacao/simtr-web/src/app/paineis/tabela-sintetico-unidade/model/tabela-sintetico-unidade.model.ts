export class TabelaSinteticoUnidade {
    columns: Array<any>;
    configTableColsComunicacaoJBPM: any[] = [
        { field: 'unidade', header: 'UNIDADE', class: 'W-5' },
        { field: 'quantidade', header: 'QUANTIDADE' },
        { field: 'data_hora_inicial', header: 'DATA/HORA INICIAL' },
        { field: 'data_hora_final', header: 'DATA/HORA FINAL' }
    ];
    rowsPerPageOptions: number[] = [];
    falhaJBPMs: Array<any> = new Array<any>();
    filteredRecords: number = 0;
    totalRecords: number;
}