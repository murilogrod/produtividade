import { Situacao } from "./situacao.interface";

export class AbaAdministrarDossie {
    situacoes: Situacao[];
    selectedSituacaoDossieProduto: Situacao;
    lastestSituations: number = 0;
    unidade: number;
    unidades: Array<number> = new Array<number>();
    limitMin: number = 0;
    limitMax: number = 1;

    isDisabledInclusaoSituacao(): boolean {
        return this.selectedSituacaoDossieProduto == undefined;
    }

    isDisabledRemoverSituacao(): boolean {
        return this.lastestSituations == 0;
    }
}