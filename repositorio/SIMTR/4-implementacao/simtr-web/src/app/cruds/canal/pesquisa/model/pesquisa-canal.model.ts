export class PesquisaCanal {
    columns: Array<any>;
    configTableColsCanais: any[] = [
        { field: 'identificador_canal', header: 'ID', class: 'W-5' },
        { field: 'sigla_canal', header: 'SIGLA CANAL' },
        { field: 'descricao_canal', header: 'DESCRIÇÂO CANAL' },
        { field: 'nome_cliente_sso', header: 'NOME CLIENTE SSO' },
        { field: 'indicador_extracao_m0', header: 'EXTRAÇÂO M0', class: 'W-10', tooltip: true, msg: 'INDICADOR EXTRAÇÃO M0' },
        { field: 'acoes', header: 'AÇÔES', class: 'W-10' },
    ];
    rowsPerPageOptions: number[] = [];
    canais: Array<any> = new Array<any>();
}