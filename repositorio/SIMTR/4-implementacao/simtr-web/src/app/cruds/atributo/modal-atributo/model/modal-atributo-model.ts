import { ModalOpcaoAtributoModel } from "../opcao-grid/model/modal-opcao-atributo-model";

export class ModalAtributoModel {
    id?: number; /// não esperado no back-end

    nome_atributo_negocial?: string;
    nome_atributo_documento?: string;
    nome_atributo_retorno?: string;
    tipo_campo?: string;
    tipo_atributo_geral?: string;
    ordem_apresentacao?: number;
    grupo_atributo?: number;
    valor_padrao?: string;
    orientacao_preenchimento?: string;
    ativo?: boolean;
    indicador_obrigatorio?: boolean = false;
    indicador_identificador_pessoa?: boolean = false;
    indicador_calculo_data_validade?: boolean = false;
    indicador_presente_documento?: boolean = false;
    indicador_campo_comparacao_receita?: string;
    indicador_modo_comparacao_receita?: string;
    nome_atributo_siecm?: string;
    tipo_atributo_siecm?: string;
    indicador_obrigatorio_siecm?: boolean = false;
    nome_objeto_sicli?: string;
    nome_atributo_sicli?: string;
    tipo_atributo_sicli?: string;
    nome_sicod?: string;
    tipo_atributo_sicod?: string;
    indicador_modo_partilha?: string;
    indicador_estrategia_partilha?: string;
    identificador_atributo_partilha?: number;
    expressao_interface?: string;
    opcoes_atributo?: ModalOpcaoAtributoModel [];
    
    alterado?: boolean; // não esperado no back-end
    indexGrid?: number; // não esperado no back-end
    objetoAlterado: ModalAtributoModel; // não esperado no back-end
    opcoesExcluidas?: ModalOpcaoAtributoModel []; // não esperado no back-end
}