import { CrudTipoDocumento } from "./crud-tipo-documento";

export class CrudFuncaoDocumental
 {
    identificador_funcao_documental?: string;
    nome_funcao_documental?: string;
    indicador_processo_administrativo?: boolean = true;
    indicador_dossie_digital?: boolean = true;
    indicador_apoio_negocio?: boolean = true ;
    tipos_documento_inclusao_vinculo?: number[]; // Usado no POST e PATCH, carrega apenas o id dos tipos documentais que serão vinculados
    tipos_documento_exclusao_vinculo?: number[]; // Usado no PATCH, carrega apenas o id dos tipos documentais que serão desvinculados
    tipos_documento?: CrudTipoDocumento[]; // Usado no GET, traz o id e o nome dos tipos documentais
    data_hora_ultima_alteracao?: string;
}