export class IncludeCanal {
    identificador_canal?: number;
    data_hora_ultima_alteracao: string;
    indicador_extracao_m0?: boolean = false;
    indicador_extracao_m30?: boolean = false;
    indicador_extracao_m60?: boolean = false;
    indicador_avaliacao_autenticidade?: boolean = false;
    indicador_atualizacao_documental?: boolean = false;
    indicador_outorga_cadastro_receita?: boolean = false;
    indicador_outorga_cadastro_caixa?: boolean = false;
    indicador_outorga_apimanager?: boolean = false;
    indicador_outorga_siric?: boolean= false;
    canal_caixa?: string;
    descricao_canal?: string;
    sigla_canal?: string;
    nome_cliente_sso?: string;
    url_callback_documento?: string;
    url_callback_dossie?: string;
    unidade_callback_dossie?: number;
}