export class IncludeTipoDocumento {
    nome: string;
    indicador_tipo_pessoa: string;
    indicador_validade_autocontida: boolean;
    prazo_validade_dias: number;
    codigo_tipologia: string;
    classe_siecm: string;
    indicador_reuso: boolean;
    indicador_uso_apoio_negocio: boolean;
    indicador_uso_dossie_digital: boolean;
    indicador_uso_processo_administrativo: boolean;
    arquivo_minuta: string;
    indicador_validacao_cadastral: boolean;
    indicador_validacao_documental: boolean;
    indicador_validacao_sicod: boolean;
    indicador_extracao_externa: boolean;
    indicador_extracao_m0: boolean;
    indicador_multiplos: boolean;
    avatar: string;
    cor_box: string;
    indicador_guarda_binario_outsourcing: boolean;
    ativo: boolean;
    tags: Array<string>;
    funcoes_documento_inclusao_vinculo: Array<number> = new Array<number>();
}