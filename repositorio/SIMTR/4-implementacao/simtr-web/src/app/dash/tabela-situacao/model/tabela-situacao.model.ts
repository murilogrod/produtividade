export class TabelaSituacao {
    cols: any[];
    rowTotal: number;
    selectedRow: number;
    configTable: any[] = [
        { field: 'id', class: 'P-60', header: 'ID', sort: true, ordering: 'id' },
        { field: 'cnpj', class: 'P-140', header: 'CPF/CNPJ', sort: true, ordering: 'cnpj' },
        { field: 'processo_dossie', header: 'Processo', sort: true, ordering: 'processo_dossie' },
        { field: 'processo_fase', header: 'Fase', sort: true, ordering: 'processo_fase' },
        { field: 'unidade_criacao', class: 'P-40', header: 'Unid.', tooltip: true, msg: 'Unidade' },
        { field: 'matricula', class: 'P-60', header: 'Matrícula' },
        { field: 'canal_caixa', class: 'P-40', header: 'Canal' },
        { field: 'situacao_atual', class: 'P-110', header: 'Situação', sort: true, ordering: 'situacao_atual' },
        { field: 'data_hora_situacao', class: 'P-95', header: 'Data/Hora', sort: true, ordering: 'data_hora_situacao' },
        { field: 'fieldUnidadesContratadas', class: 'P-60', header: 'Atend.', tooltip: true, msg: 'Atendimento' },
        { field: 'fieldProdutosContratados', class: 'P-60', header: 'Prod.', tooltip: true, msg: 'Produtos' },
        { field: 'acoes', class: 'P-60', header: 'Ações' }
    ];
}