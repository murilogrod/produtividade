import { ModalAtributoModel } from "src/app/cruds/atributo/modal-atributo/model/modal-atributo-model";
import { FuncaoDocumental } from "./funcao-documental";

export class TipoDocumento {
    id?: number;
    data_hora_ultima_alteracao: string;
    nome: string;
    indicador_tipo_pessoa: string;
    prazo_validade_dias?: number = 1;
    codigo_tipologia: any;
    classe_siecm: string;
    arquivo_minuta: string;
    indicador_validade_autocontida?: boolean = false;
    indicador_reuso?: boolean = false;
    indicador_validacao_cadastral?: boolean = false;
    indicador_validacao_documental?: boolean = false;
    indicador_extracao_externa?: boolean = false;
    indicador_uso_processo_administrativo?: boolean = false;
    indicador_uso_dossie_digital?: boolean = false;
    indicador_uso_apoio_negocio?: boolean = false;
    indicador_multiplos?: boolean = false;
    indicador_extracao_m0?: boolean = false;
    indicador_guarda_binario_outsourcing?: boolean = false;
    indicador_validacao_sicod?: boolean = false;
    naoPodeIncluirTipoDocumento?: boolean = false;
    tag_documento: string;
    tags: Array<string> = new Array<string>();
    cor_box: string = '#ffffff';
    alterado?: boolean = false;
    avatar: string;
    ativo?: boolean = false;
    funcoes_documentais: Array<FuncaoDocumental>;
    atributos_extracao?: ModalAtributoModel[] = new Array<ModalAtributoModel>(0);
    atributosExcluidos?: ModalAtributoModel[] = new Array<ModalAtributoModel>(0);
    funcoes_documento_inclusao_vinculo: Array<number> = new Array<number>(0);
    funcoes_documento_exclusao_vinculo: Array<number> = new Array<number>(0);
}