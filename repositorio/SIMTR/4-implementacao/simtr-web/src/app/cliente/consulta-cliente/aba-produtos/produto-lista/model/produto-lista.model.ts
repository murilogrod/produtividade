export class ProdutoLista {
    cols: Array<any>;
    configTableColsProdutos: any[] = [
        { field: 'id_dossie_produto', header: 'Identif.', tooltip: true, msg: 'Identificador' },
        { field: 'produtos_contratados', header: 'Produto', class: 'W-15' },
        { field: 'processo_fase', header: 'Etapa Processo', class: 'W-20' },
        { field: 'tipo_relacionamento', header: 'Tipo', tooltip: true, msg: 'Tipo Vinculo' },
        { field: 'unidade_criacao', header: 'Uni. Orig.', tooltip: true, msg: 'Unidade Origem' },
        { field: 'situacao_atual', header: 'Situação', tooltip: true, msg: 'Situação Atual' },
        { field: 'data_hora_situacao', header: 'Data', tooltip: true, msg: 'Data Situação' },
        { field: 'unidade_situacao', header: 'Uni. Sit.', tooltip: true, msg: 'Unidade Situação' },
        { field: 'acoes', header: 'Ações' }
    ];
}