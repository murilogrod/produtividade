import { GridChecklist } from "../../pesquisa/model/grid-checklist.model";
import { Apontamento } from "src/app/cruds/model/apontamento.model";
import { Vinculacao } from "src/app/cruds/model/vinculacao.model";
import { Observable } from "rxjs";
import { IncludeApontamento } from "./include-apontamento";
import { UpdateApontamento } from "./update-apontamento";
import { IncludeVinculacao } from "./include-vinculacao";
import { VinculacaoChecklist } from "src/app/cruds/model/vinculacao-checklist";

export class ChecklistModel {
    showHeader: boolean;
    configGridHeader: any[] = [
        { width: '30%', classIcon: 'fa fa-desktop fa-4x', labelTitle: 'ID', index: 0 },
        { width: '100%', classIcon: 'fa fa-clock-o fa-4x', labelTitle: 'DATA DA ÚLTIMA ATUALIZAÇÃO', index: 1 }
    ];
    controlError: boolean;
    editar: boolean;
    mostrarApontamentos: boolean;
    mostrarVinculacoes: boolean;
    nomeChecklistOriginal: string;
    ultimoIdChecklist: number;
    checklists: Array<GridChecklist> = new Array<GridChecklist>();
    apontamentos: Array<Apontamento> = new Array<Apontamento>();
    vinculacoes: Array<VinculacaoChecklist> = new Array<VinculacaoChecklist>();
    apontamentosRemovidos: Array<Apontamento> = new Array<Apontamento>();
    vinculacoesRemovidas: Array<Vinculacao> = new Array<Vinculacao>();
    redirectPesquisaChecklist: boolean = false;
    requesicoesIncludeApontamentos: Array<Observable<IncludeApontamento>> = new Array<Observable<IncludeApontamento>>(0);
    requesicoesUpdateApontamentos: Array<Observable<UpdateApontamento>> = new Array<Observable<UpdateApontamento>>(0);
    requesicoesApontamentosRemovidos: Array<Observable<any>> = new Array<Observable<any>>(0);
    requesicoesIncludeVinculacoes: Array<Observable<IncludeVinculacao>> = new Array<Observable<IncludeVinculacao>>(0);
    requesicoesVinculacoesRemovidas: Array<Observable<any>> = new Array<Observable<any>>(0);
    disabledChecklist: boolean = false;
    cloneChecklist: boolean = false;
    unidadeZero: boolean = false;
}