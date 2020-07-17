export class TabelaSinteticoProcesso {
    columns: Array<any>;
    configTableColsComunicacaoJBPM: any[] = [
        { field: 'processo', header: 'PROCESSO' },
        { field: 'bpm_container', header: 'CONTAINER BPM'},
        { field: 'bpm_processo', header: 'PROCESSO BPM' },
        { field: 'quantidade', header: 'QUANTIDADE', class: 'W-10' },
        { field: 'data_hora_inicial', header: 'DATA/HORA INICIAL', class: 'W-15', style: "{'text-align':'center'}"},
        { field: 'data_hora_final', header: 'DATA/HORA FINAL', class: 'W-15', style: "{'text-align':'center'}" }
    ];
    rowsPerPageOptions: number[] = [];
    falhaJBPMs: Array<any> = new Array<any>();s
    filteredRecords: number = 0;
    totalRecords: number;
}
