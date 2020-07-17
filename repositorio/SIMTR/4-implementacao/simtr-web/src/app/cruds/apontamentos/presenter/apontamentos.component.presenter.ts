import { Injectable } from "@angular/core";
import { Apontamentos } from "../model/apontamentos.model";
import { Utils } from "../../util/utils";
import { Apontamento } from "../../model/apontamento.model";
import { SortEvent, ConfirmationService } from "primeng/primeng";
import { ModalApontamentoComponent } from "../modal-apontamento/view/modal-apontamento.component";
import { DialogService } from "angularx-bootstrap-modal";
import { ApontamentoDataInput } from "../model/apontamento-data-input";
import { ApontamentoOutput } from "../model/apontamento-output";
import { APONTAMENTO } from "../constant.apontamento";
import { ApontamentosComponent } from "../view/apontamentos.component";

@Injectable()
export class ApontamentosPresenter {

    apontamentos: Apontamentos;

    constructor(private dialogService: DialogService,
        private confirmationService: ConfirmationService) { }

    /**
     * Realiza a configuração da grid de apontamentos       
     * @param apontamentosComponent 
     */
    initConfigListaApontamentos(apontamentosComponent: ApontamentosComponent) {
        this.inicializarListaApontamentos(apontamentosComponent);
        this.custumizeRowsPerPageOptions();
    }

    /**
     * Reordena a grid apontamentos pelo drag drop
     * @param event 
     * @param apontamentosComponent 
     */
    orderApontamentos(event: any, apontamentosComponent: ApontamentosComponent) {
        this.reordernarApontamentos(Number(event.dropIndex));
        this.enviarApontamentos(apontamentosComponent);
    }

    /**
     * Abre a modal apontamento: capturando seu objeto de saida
     * @param apontamento 
     * @param index 
     * @param apontamentosComponent 
     */
    openModalApontamento(index: number, apontamento: Apontamento, apontamentosComponent: ApontamentosComponent) {
        this.dialogService.addDialog(ModalApontamentoComponent, {
            apontamentoDataInput: this.inicializarInputModalApontamento(index, apontamento)
        }).subscribe((apontamentoOutput: ApontamentoOutput) => {
            if (apontamentoOutput) {
                this.adicionarAtualizarApontamentoGrid(apontamentoOutput, apontamentosComponent);
            }
        });
    }

    /**
      * Checagem para remoção de apontamento
     * @param index 
     * @param apontamentosComponent 
     * @param apontamento 
     */
    confirmRemoveApontamento(index: number, apontamento: Apontamento, apontamentosComponent: ApontamentosComponent) {
        this.confirmationService.confirm({
            message: APONTAMENTO.APONTAMENTO_REMOVER,
            accept: () => {
                this.removerApontamento(index, apontamento, apontamentosComponent);
            }
        });
    }

    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.apontamentos.filteredRecords = event.filteredValue.length;
        } else {
            this.apontamentos.filteredRecords = 0;
        }
    }

    /**
     * Reordena os apontamentos conforme o index referencia
     * @param indexReference 
     */
    private reordernarApontamentos(indexReference: number) {
        let newSequence: number = indexReference;
        while (newSequence > 0) {
            this.apontamentos.apontamentos[newSequence - 1].sequencia_apresentacao = newSequence;
            newSequence--;
        }
        for (let i = indexReference; i < this.apontamentos.apontamentos.length; i++) {
            this.apontamentos.apontamentos[i].sequencia_apresentacao = i + 1;
        }
    }

    /**
     * Remove o apontamento pelo index da lista
     * @param index 
     * @param apontamentosComponent 
     * @param apontamento 
     */
    private removerApontamento(index: number, apontamento: Apontamento, apontamentosComponent: ApontamentosComponent) {
        this.apontamentos.apontamentos.splice(index, 1);
        if (index < this.apontamentos.apontamentos.length) {
            this.reordernarApontamentos(index + 1);
        } else {
            this.reordernarApontamentos(index);
        }
        this.custumizeRowsPerPageOptions();
        this.enviarApontamentos(apontamentosComponent);
        this.enviarApontamentosParaRemocao(apontamento, apontamentosComponent);

    }

    /**
     * Cria-se uma lista de apontamentos para remoção
     * @param apontamento 
     * @param apontamentosComponent 
     */
    private enviarApontamentosParaRemocao(apontamento: Apontamento, apontamentosComponent: ApontamentosComponent) {
        if (apontamento.id) {
            this.apontamentos.apontamentosRemovidos.push(apontamento);
            apontamentosComponent.apontamentosRemovidosChanged.emit(this.apontamentos.apontamentosRemovidos);
        }
    }

    /**
     * Inicializa o objeto de entrada da modal apontamento
     * @param index 
     * @param apontamento 
     */
    private inicializarInputModalApontamento(index: number, apontamento: Apontamento): ApontamentoDataInput {
        const apontamentoDataInput: ApontamentoDataInput = new ApontamentoDataInput();
        apontamentoDataInput.index = index;
        apontamentoDataInput.apontamento = Object.assign(new Apontamento(), apontamento);
        apontamentoDataInput.apontamentos = this.apontamentos.apontamentos;
        return apontamentoDataInput;
    }

    /**
     * Atualiza a grid apontamentos: index = -1 ? NOVO : EDICAO 
     * Realiza a ordenação dos apontamentos conforme a entrada da ordem
     * Caalcula os registros por pagina
     * @param apontamentoOutput 
     * @param apontamentosComponent 
     */
    private adicionarAtualizarApontamentoGrid(apontamentoOutput: ApontamentoOutput, apontamentosComponent: ApontamentosComponent) {
        const indexReference: number = apontamentoOutput.apontamentoDataOutput.apontamento.sequencia_apresentacao - 1;
        if (apontamentoOutput.apontamentoDataOutput.index == -1) {
            if (indexReference <= this.apontamentos.apontamentos.length) {
                const apontamento: Apontamento = this.apontamentos.apontamentos.splice(indexReference, 1, apontamentoOutput.apontamentoDataOutput.apontamento)[0];
                if (apontamento) {
                    this.apontamentos.apontamentos.push(apontamento);
                }
                this.reordernarApontamentos(indexReference);
            } else {
                apontamentoOutput.apontamentoDataOutput.apontamento.sequencia_apresentacao = this.apontamentos.apontamentos.length;
                this.apontamentos.apontamentos.push(apontamentoOutput.apontamentoDataOutput.apontamento);
                const start: number = indexReference > this.apontamentos.apontamentos.length - 1 ? this.apontamentos.apontamentos.length - 1 : indexReference - 1;
                this.reordernarApontamentos(start);
            }
        } else {
            if (indexReference < this.apontamentos.apontamentos.length - 1) {
                if (apontamentoOutput.apontamentoDataOutput.ultimaOrdem == apontamentoOutput.apontamentoDataOutput.apontamento.sequencia_apresentacao) {
                    this.apontamentos.apontamentos.splice(indexReference, 1, apontamentoOutput.apontamentoDataOutput.apontamento);
                } else {
                    const apontamento = this.apontamentos.apontamentos.splice(indexReference, 1, apontamentoOutput.apontamentoDataOutput.apontamento)[0];
                    this.apontamentos.apontamentos.splice(apontamentoOutput.apontamentoDataOutput.index, 1, apontamento);
                }
                this.reordernarApontamentos(indexReference);
            } else {
                apontamentoOutput.apontamentoDataOutput.apontamento.sequencia_apresentacao = this.apontamentos.apontamentos.length;
                let start: number = indexReference > this.apontamentos.apontamentos.length - 1 ? this.apontamentos.apontamentos.length - 1 : indexReference - 1;
                start = indexReference == this.apontamentos.apontamentos.length - 1 ? this.apontamentos.apontamentos.length - 1 : start;
                this.apontamentos.apontamentos.splice(start, 1, apontamentoOutput.apontamentoDataOutput.apontamento);
                //this.apontamentos.apontamentos.splice(start+1, 1, apontamentoOutput.apontamentoDataOutput.apontamento);
                //this.apontamentos.apontamentos.splice(apontamentoOutput.apontamentoDataOutput.index, 1, apontamento);
                this.reordernarApontamentos(start);
            }
        }
        this.custumizeRowsPerPageOptions();
        this.enviarApontamentos(apontamentosComponent);
    }

    /**
     * Envia os apontamentos para o componente checklist
     * @param apontamentosComponent 
     */
    private enviarApontamentos(apontamentosComponent: ApontamentosComponent) {
        apontamentosComponent.apontamentosChanged.emit(this.apontamentos.apontamentos);
    }

    /**
     * Pega a lista por @Input e inicializa a lista de apontamentos do componente: apontamentos
     * certificado se a origem dos mesmos é via funcionalidade clone
     * @param apontamentosComponent 
     */
    private inicializarListaApontamentos(apontamentosComponent: ApontamentosComponent) {
        apontamentosComponent.apontamentos.forEach(apontamento => {
            if (apontamento.clone) {
                apontamento.id = undefined;
            }
            this.apontamentos.apontamentos.push(Object.assign({}, apontamento));
        });
        this.ordenarApontamentos();
        apontamentosComponent.apontamentosChanged.emit(this.apontamentos.apontamentos);
    }

    /**
     * Ordenação de apontamentos pela ordem
     */
    private ordenarApontamentos() {
        this.apontamentos.apontamentos.sort((apt1, apt2): number => {
            if (apt1.sequencia_apresentacao < apt2.sequencia_apresentacao)
                return -1;
            if (apt1.sequencia_apresentacao > apt2.sequencia_apresentacao)
                return 1;
            return 0;
        });
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.apontamentos.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.apontamentos.apontamentos);
    }
}