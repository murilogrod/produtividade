export class CrudTipoDocumento
{
    arquivo_minuta?: string;
    classe_ged?: string;
    codigo_tipologia?: string;
    data_hora_ultima_alteracao?: string;
    identificador_tipo_documento?: string;
    indicador_extracao_externa?: boolean;
    indicador_extracao_m0?: boolean;
    indicador_reuso?: boolean;
    indicador_tipo_pessoa?: string;
    indicador_uso_apoio_negocio?: boolean;
    indicador_uso_dossie_digital?: boolean;
    indicador_uso_processo_administrativo?: boolean;
    indicador_validacao_cadastral?: boolean;
    indicador_validacao_documental?: boolean;
    indicador_validade_autocontida?: boolean;
    indicador_multiplos?: boolean;
    indicador_validacao_sicod?: boolean;
    nome_tipo_documento?: string;
    prazo_validade_dias?: any;
    avatar?: string;
    cor_fundo?: string;
    tags?: any = [];
    ordem_apresentacao?: number;

}