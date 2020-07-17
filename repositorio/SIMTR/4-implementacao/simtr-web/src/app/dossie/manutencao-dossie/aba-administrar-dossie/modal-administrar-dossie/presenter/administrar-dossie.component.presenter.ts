import { Injectable } from "@angular/core";
import { AbaAdministrarDossie } from "../model/aba-administrar-dossie.model";
import { TipoAdministracaoDossie } from "../../model/tipo-administracao-dossie.enum";
import { AdministrarDossieOutput } from "../../model/administrar-dossie-output";
import { ModalAdministrarDossieComponent } from "../view/modal-administrar-dossie.component";

@Injectable()
export class AdministrarDossieComponentPresenter {

    abaAdministrarDossie: AbaAdministrarDossie;

    constructor() { }

    /**
     * Inicializa os titulos da modal 
     * @param dossieProduto 
     */
    inicializaTitutlosModalAdministracaoDossie(dossieProduto: any) {
        this.abaAdministrarDossie.title = JSON.parse(TipoAdministracaoDossie[dossieProduto.tipoAdministracaoDossie]).title;
        this.abaAdministrarDossie.details = JSON.parse(TipoAdministracaoDossie[dossieProduto.tipoAdministracaoDossie]).details;
        if (JSON.parse(TipoAdministracaoDossie[dossieProduto.tipoAdministracaoDossie]).key.indexOf("REMOVER_SITUACAO") == 0) {
            this.inicializaTituloSituacoes(dossieProduto);
        }
        if (JSON.parse(TipoAdministracaoDossie[dossieProduto.tipoAdministracaoDossie]).key.indexOf("EXCLUIR_DOSSIE") == 0) {
            this.formataTipoRelacionamentoExclusaoDossie(dossieProduto);
        }
    }

    /**
     * Inicializa o label de exclusao de dossie produto com id, tomador e nome_tomador
     * @param dossieProduto 
     */
    formataTipoRelacionamentoExclusaoDossie(dossieProduto: any) {
        const vinculoCliente: any = dossieProduto.vinculos_pessoas.find(vp => vp.tipo_relacionamento.principal);
        this.abaAdministrarDossie.details = this.abaAdministrarDossie.details.replace("{id}", dossieProduto.id)
            .replace("{tomador}", vinculoCliente.tipo_relacionamento.nome).replace("{nome_tomador}", vinculoCliente.dossie_cliente.nome);
    }

    /**
     * Inicializa o titulo das situações para a funcionalidade de remoção de situação
     * @param dossieProduto 
     */
    inicializaTituloSituacoes(dossieProduto: any) {
        const lastestSituations: number = dossieProduto.lastestSituations;
        const start: number = Number(dossieProduto.historico_situacoes.length - lastestSituations);
        const end: number = Number(dossieProduto.historico_situacoes.length);
        const unidades: Array<any> = dossieProduto.historico_situacoes.slice(start, end);
        let situacoes: string = "";
        unidades.forEach(unidade => {
            situacoes += unidade.nome + ", ";
        });
        this.abaAdministrarDossie.viewSituations = unidades.length > 0;
        if (unidades.length > 1) {
            const array: string[] = situacoes.split(",");
            let formatResult: string = "";
            for (let i = 0; i < array.length; i++) {
                if (array[i].trim().length > 0) {
                    if (i == array.length - 2) {
                        formatResult = formatResult.substring(0, formatResult.length - 2);
                        formatResult += ` e ${array[i]}.`;
                    } else {
                        formatResult += `${array[i]}, `;
                    }
                }
            }
            situacoes = formatResult;
        } else {
            situacoes = situacoes.replace(",", "").trim() + ".";
        }
        this.abaAdministrarDossie.situations = situacoes;
    }

    /**
     * Monta o objeto de saida da modal de administração de dossie produto, fechando a modal.
     * @param modalAdministrarDossieComponent 
     * @param dossieProduto 
     */
    close(modalAdministrarDossieComponent: ModalAdministrarDossieComponent, dossieProduto: any) {
        var output = this.initOutput(this.abaAdministrarDossie.razaoInclusaoSituacao, dossieProduto.tipoAdministracaoDossie);
        modalAdministrarDossieComponent.dialogReturn = output;
        modalAdministrarDossieComponent.closeDialog();
    }


    /**
     * Inicializa o objeto de saída da modal administração de dossie produto
     * @param razaoInclusaoSituacao 
     * @param tipoAdministracaoDossie 
     */
    private initOutput(razaoInclusaoSituacao: string, tipoAdministracaoDossie: TipoAdministracaoDossie) {
        return <AdministrarDossieOutput>{
            razaoInclusaoSituacao: razaoInclusaoSituacao,
            tipoAdministracaoDossie: tipoAdministracaoDossie
        };
    }

    /**
     * Desabilita o botão confirmar para inclusão de situação para texto menores ou igual a 10 caracteres
     * @param event 
     */
    disabledConfirmButton(event: any) {
        this.abaAdministrarDossie.viewConfirmButton = event.length <= 10;
    }

}