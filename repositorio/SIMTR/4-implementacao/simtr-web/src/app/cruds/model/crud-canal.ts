export class CrudCanal {
    identificador_canal?: number;
    indicador_extracao_m0?: boolean;
    indicador_extracao_m30?: boolean;
    indicador_extracao_m60?: boolean;
    indicador_avaliacao_autenticidade?: boolean;
    indicador_atualizacao_documental?: boolean;
    indicador_outorga_cadastro_receita?: boolean;
    indicador_outorga_cadastro_caixa?: boolean;
    indicador_outorga_apimanager? : boolean;    
    indicador_outorga_siric? : boolean;
    canal_caixa?: string;
    descricao_canal?: string;
    sigla_canal?: string;
    nome_cliente_sso?: string;
    url_callback_documento?:string;
    url_callback_dossie?:string;
    unidade_callback_dossie?:number;
}