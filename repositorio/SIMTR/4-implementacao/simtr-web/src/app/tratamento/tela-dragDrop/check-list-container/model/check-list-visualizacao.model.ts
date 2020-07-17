import { Checklist } from "src/app/model/checklist";

export class CheckListVisualizacao{
    processoFaseAtual: any;
    listaChekList: Checklist[];
    checkEmFoco: Checklist;
    dataChecK: Date;
    nomeChecklistTitle: string; 
    orientacaoChecklist: string; 
    existeCheckListPrevio: boolean;
    exibindoChecklistsPrevios: boolean;
    idCheckListAtivado: any; 
    listDocumentosImagens: any[]; 
    apontamentos: any[];
    apontamentoPreenchido: boolean = false;
    listaJustificativaApontamentos: string[];
    listApontamentosCheckados: boolean[];
    habilitarDesabilitarTratamento:boolean;
    existeApontamentoAnterior?: boolean;
}