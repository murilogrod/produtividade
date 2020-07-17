import { GridChecklist } from "./grid-checklist.model";
import { InterfaceTipoDocumento } from "../../../model/tipo-documento.interface";
import { InterfaceProcesso } from "../../../model/processo.interface";
import { InterfaceFuncaoDocumental } from "../../../model/funcao-documental.interface";

export class PesquisaChecklist {
    columns: Array<any>;
    configTableColsChecklists: any[] = [
        { field: 'id', header: 'ID', class: 'W-5' },
        { field: 'nome', header: 'CHECKLIST', class: 'W-15' },
        { field: 'unidade_responsavel', header: 'UNIDADE' },
        { field: 'verificacao_previa', header: 'PRÉVIO' },
        { field: 'data_hora_criacao', header: 'CRIAÇÃO', tooltip: true, msg: 'DATA DE CRIAÇÃO' },
        { field: 'ativo', header: 'ATIVO' },
        { field: 'quantidade_apontamentos', header: 'APONT.', tooltip: true, msg: 'APONTAMENTOS' },
        { field: 'vinculacoes', header: 'VINC.', tooltip: true, msg: 'VINCULAÇÕES' },
        { field: 'quantidade_associacoes', header: 'OPERAÇÔES' },
        { field: 'acoes', header: 'AÇÕES', class: 'W-7' },
    ];
    rowsPerPageOptions: number[] = [];
    processos: Array<InterfaceProcesso>;
    fases: Array<InterfaceProcesso>;
    tiposDocumento: Array<InterfaceTipoDocumento>;
    funcoesDocumentais: Array<InterfaceFuncaoDocumental>;
    selectedProcesso: InterfaceProcesso;
    selectedFase: InterfaceProcesso;
    selectedTipoDocumento: InterfaceTipoDocumento;
    selectedFuncaoDocumental: InterfaceFuncaoDocumental;
    inactiveChecklist: boolean = false;
    emptyProcesso: boolean = true;
    inactiveChecklists: Array<GridChecklist> = new Array<GridChecklist>();
    activeChecklists: Array<GridChecklist> = new Array<GridChecklist>();
    verificacaoPreviaMarcadaChecklists: Array<GridChecklist> = new Array<GridChecklist>();
    verificacaoPreviaDesmarcadaChecklists: Array<GridChecklist> = new Array<GridChecklist>();
    tempChecklists: Array<GridChecklist> = new Array<GridChecklist>();
    checklists: Array<GridChecklist> = new Array<GridChecklist>();
    verificacaoPreviaFiltro: boolean = false;
    situacaoFiltro: boolean = true;
    totalRecords: number;
    filteredRecords: number = 0;
}