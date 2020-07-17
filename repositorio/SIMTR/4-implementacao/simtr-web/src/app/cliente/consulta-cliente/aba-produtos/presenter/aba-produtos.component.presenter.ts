import { Injectable } from "@angular/core";
import { AbaProduto } from "../model/aba-produto.model";
import { Utils } from "src/app/utils/Utils";


@Injectable()
export class AbaProdutoComponentPresenter {

    abaProduto: AbaProduto;

    constructor() { }

    initAbaProdutos(produtos: any) {
        this.abaProduto.processosPatriarca = this.separarProcessoPatriarca(produtos);
        this.ordenarDossies();
    }

    expandirAllTr() {
        this.abaProduto.expandirListaProduto = !this.abaProduto.expandirListaProduto;
        this.abaProduto.collapsed = !this.abaProduto.expandirListaProduto;
        this.abaProduto.primeiraVez = false;
    }

    expTodosOuPrimeiro(idxZero: number) {
        if (idxZero == 0 && this.abaProduto.primeiraVez) {
            return this.abaProduto.primeiraVez;
        }
        return this.abaProduto.expandirListaProduto;
    }

    enabledAll(processPatriarca: any) {
        return (processPatriarca && processPatriarca.length > 1) || (processPatriarca && processPatriarca.length > 0 && processPatriarca[0].processosDossie && processPatriarca[0].processosDossie.length > 1);
    }

    private separarProcessoPatriarca(produtos: any) {
        let processosPatriarca: any[] = [];
        for (const prod of produtos) {
            if (processosPatriarca.length > 0) {
                let procPatriarca = this.verificarProcessosPatriarca(processosPatriarca, prod);
                if (procPatriarca !== null) {
                    let procDossie = this.verificarProcessosDossie(procPatriarca.processosDossie, prod);
                    if (procDossie !== null) {
                        procDossie.dossiesProduto.push(prod);
                    } else {
                        this.criarProcessosDossie(procPatriarca.processosDossie, prod);
                    }
                } else {
                    this.criarProcessosPatriarca(processosPatriarca, prod);
                }
            } else {
                processosPatriarca.push(prod.processo_patriarca);
                processosPatriarca[0].processosDossie = [];
                processosPatriarca[0].processosDossie.push(prod.processo_dossie);
                processosPatriarca[0].processosDossie[0].dossiesProduto = [];
                processosPatriarca[0].processosDossie[0].dossiesProduto.push(prod);
            }
        }
        return processosPatriarca;
    }


    private criarProcessosPatriarca(processosPatriarca: any[], prod: any) {
        processosPatriarca.push(prod.processo_patriarca);
        for (let procPatriarca of processosPatriarca) {
            if (procPatriarca.id == prod.processo_patriarca.id) {
                procPatriarca.processosDossie = [];
                procPatriarca.processosDossie.push(prod.processo_dossie);
                procPatriarca.processosDossie[0].dossiesProduto = [];
                procPatriarca.processosDossie[0].dossiesProduto.push(prod);
            }
        }
    }

    private criarProcessosDossie(processosDossie: any[], prod: any) {
        processosDossie.push(prod.processo_dossie);
        for (let procDossie of processosDossie) {
            if (procDossie.id == prod.processo_dossie.id) {
                procDossie.dossiesProduto = [];
                procDossie.dossiesProduto.push(prod);
            }
        }
    }

    private verificarProcessosDossie(processosDossie: any[], prod: any) {
        for (let procDossie of processosDossie) {
            if (procDossie.id == prod.processo_dossie.id) {
                return procDossie;
            }
        }
        return null;
    }

    private verificarProcessosPatriarca(processosPatriarca: any[], prod: any) {
        for (let procPatriarca of processosPatriarca) {
            if (procPatriarca.id == prod.processo_patriarca.id) {
                return procPatriarca;
            }
        }
        return null;
    }

    // Ordenando a tabela Dossiês
    private ordenarDossies() {
        for (let procPatriarca of this.abaProduto.processosPatriarca) {
            for (let procDossie of procPatriarca.processosDossie) {
                procDossie.dossiesProduto.sort(Utils.ordenarDossieProduto);
            }
        }
    }

}