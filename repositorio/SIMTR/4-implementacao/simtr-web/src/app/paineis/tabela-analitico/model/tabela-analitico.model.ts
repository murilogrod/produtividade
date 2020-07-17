export class TabelaAnalitico {
    columns: Array<any>;
    configTableColsComunicacaoJBPM: any[] = [
        { field: 'id', header: 'DOSSIÊ PRODUTO', class: 'W-15' },
        { field: 'unidade_criacao', header: 'UNIDADE', class: 'W-10' },
        { field: 'processo_origem_id_nome', header: 'PROCESSO' },
        { field: 'bpm_instancia', header: 'INSTÂNCIA BPM' },
        { field: 'bpm_container', header: 'CONTAINER BPM'},
        { field: 'bpm_processo', header: 'PROCESSO BPM' },
        { field: 'data_hora_falha', header: 'DATA/HORA FALHA' }
    ];
    rowsPerPageOptions: number[] = [];
    falhaJBPMs: Array<any> = new Array<any>();
    analitico: Array<any> = new Array<any>();
    filteredRecords: number = 0;
    totalRecords: number;
}