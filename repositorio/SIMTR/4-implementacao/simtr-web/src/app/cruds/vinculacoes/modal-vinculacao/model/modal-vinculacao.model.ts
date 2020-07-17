import { InterfaceProcesso } from "../../../model/processo.interface";
import { InterfaceTipoDocumento } from "../../../model/tipo-documento.interface";
import { InterfaceFuncaoDocumental } from "../../../model/funcao-documental.interface";
import { Vinculacao } from "src/app/cruds/model/vinculacao.model";
import { GridChecklist } from "src/app/cruds/checklist/pesquisa/model/grid-checklist.model";
import { VinculacaoChecklistConflitante } from "./vinculacao-checklist-conflitante";

export class ModalVinculacao {
    ptBR: any;

    calendarProperties = {
        firstDayOfWeek: 1,
        dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Feb', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dec'],
        today: 'Hoje',
        clear: 'Limpar'
    };

    dataRevogacao: Date;
    dataRevogacaoVigente: Date;
    errorDataRevogacao: string;
    showErrorDataRevogacao: boolean = false;
    showErrorOcorrenciaDataRevogacao: boolean = false;
    conflitoVinculacao: boolean = false;
    processos: Array<InterfaceProcesso>;
    fases: Array<InterfaceProcesso>;
    tiposDocumento: Array<InterfaceTipoDocumento>;
    funcoesDocumentais: Array<InterfaceFuncaoDocumental>;
    selectedProcesso: InterfaceProcesso;
    selectedFase: InterfaceProcesso;
    selectedTipoDocumento: InterfaceTipoDocumento;
    selectedFuncaoDocumental: InterfaceFuncaoDocumental;
    emptyProcesso: boolean = true;
    vinculacaoConflitante: Vinculacao;
    invalid: boolean = false;
    activeChecklists: Array<GridChecklist>;
    ocorrenciaVinculacao: boolean = false;
    ocorrenciaVinculacaoFase: boolean = false;
    ocorrenciaVinculacaoTipoDocumento: boolean = false;
    ocorrenciaVinculacaoFuncaoDocumental: boolean = false;
    vinculacaoConflitanteRemovida: boolean = false;
    editar: boolean = false;
    vinculacoesConflitante: Set<VinculacaoChecklistConflitante> = new Set<VinculacaoChecklistConflitante>();
    msgDataRevogacaoError: string;
}