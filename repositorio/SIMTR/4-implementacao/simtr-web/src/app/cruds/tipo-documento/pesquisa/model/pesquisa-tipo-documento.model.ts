import { GridTipoDocumento } from "../../model/grid-tipo-documento.model";
import { SituacaoTipoDocumento } from "./situacao-tipo-documento.enum";

export class PesquisaTipoDocumento {
    columns: Array<any>;
    configTableColsTipoDocumento: any[] = [
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'nome', header: 'TIPO DOCUMENTO' },
        { field: 'codigo_tipologia', header: 'CÓD TIPOLOGIA' },
        { field: 'indicador_tipo_pessoa_label', header: 'TIPO PESSOA' },
        { field: 'indicador_uso_processo_administrativo_label', header: 'PAE'},
        { field: 'indicador_uso_dossie_digital_label', header: 'DOSSIÊ DIGITAL' },
        { field: 'indicador_uso_apoio_negocio_label', header: 'NEGÓCIO' },
        { field: 'ativo_label', header: 'ATIVO' },
        { field: 'acoes', header: 'AÇÔES'},
    ];
    configSelectButtonOptions: any[] = [
        {label: 'Ativos', value: SituacaoTipoDocumento.ATIVO, icon: 'fa fa-check'},
        {label: 'Inativos', value: SituacaoTipoDocumento.INATIVO, icon: 'fa fa-ban'},
        {label: 'Todos', value: SituacaoTipoDocumento.TODAS, icon: 'fa fa-plus'},
    ];
    selectedStatus: string = SituacaoTipoDocumento.ATIVO;
    rowsPerPageOptions: number[] = [];
    tipoDocumentos: Array<GridTipoDocumento> = new Array<GridTipoDocumento>();
    tipoDocumentosInativos: Array<GridTipoDocumento> = new Array<GridTipoDocumento>();
    tipoDocumentosAtivos: Array<GridTipoDocumento> = new Array<GridTipoDocumento>();
    tipoDocumentosGeral: Array<GridTipoDocumento> = new Array<GridTipoDocumento>();
    filteredRecords: number = 0;
    totalRecords: number;
}