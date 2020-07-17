import { Component, OnInit, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { SortEvent, ConfirmationService } from 'primeng/primeng';
import { ModalAtributoModel } from '../modal-atributo/model/modal-atributo-model';
import { ModalAtributoModelConfigTable } from '../modal-atributo/model/modal-atributo-config-table';
import { UtilsGrids } from '../../util/utils-grids';
import { DialogService } from 'angularx-bootstrap-modal';
import { Utils } from "../../util/utils";
import { ModalAtributoComponent } from '../modal-atributo/view/modal-atributo.component';
import { LoaderService } from 'src/app/services';

@Component({
    selector: 'atributo',
    templateUrl: './atributo.component.html',
    styleUrls: ['./atributo.component.css']
})
export class AtributoComponent implements OnInit, OnChanges {

    @Input() disabledAtributo: boolean;
    @Input() listaAtributo: Array<ModalAtributoModel>
    @Output() atributosChanged: EventEmitter<Array<ModalAtributoModel>> = new EventEmitter<Array<ModalAtributoModel>>();
    @Output() atributosRemovidosChanged: EventEmitter<ModalAtributoModel> = new EventEmitter<ModalAtributoModel>();

    configuracaoTableColunas: ModalAtributoModelConfigTable = new ModalAtributoModelConfigTable();
    rowsPerPageOptions: number[] = [];
    atributosRemovidos: Array<ModalAtributoModel> = new Array<ModalAtributoModel>();
    filteredRecords: number = 0;
    countAtributo: number = 0;
    disabledDragdrop = false;

    constructor(private dialogService: DialogService,
        private confirmationService: ConfirmationService,
        private loadService: LoaderService) {
    }

    ngOnInit() {
        this.custumizeRowsPerPageOptions();
    }

    ngOnChanges() {
        this.custumizeRowsPerPageOptions();
    }

    realizarOrdenacao(event: SortEvent) {
        UtilsGrids.customSort(event);
    }

    /**
     * Ordenação pelo drag drop atualizando a ordem de apresentação
     * @param event 
     */
    reordenarAtributos(event: any) {
        if (event.dropIndex > event.dragIndex) {
            const dragIndex: number = event.dragIndex;
            const dropIndex: number = event.dropIndex - 1;
            this.listaAtributo[dragIndex].ordem_apresentacao = dragIndex + 1;
            this.listaAtributo[dragIndex].alterado = true;
            this.listaAtributo[dropIndex].ordem_apresentacao = dropIndex + 1;
            this.listaAtributo[dropIndex].alterado = true;
            let count: number = dragIndex == 1 ? 0 + 1 : dragIndex + 1;
            while (count < dropIndex) {
                let ordem: number = count + 1;
                this.listaAtributo[count].ordem_apresentacao = ordem;
                this.listaAtributo[count].alterado = true;
                count++;
            }
        }
        if (event.dragIndex == event.dropIndex) {
            const dragIndex: number = event.dragIndex;
            const dropIndex: number = event.dropIndex - 1;
            this.listaAtributo[dragIndex].ordem_apresentacao = dragIndex + 1;
            this.listaAtributo[dragIndex].alterado = true;
            this.listaAtributo[dropIndex].ordem_apresentacao = dropIndex + 1;
            this.listaAtributo[dropIndex].alterado = true;

        }
        if (event.dragIndex > event.dropIndex) {
            const dragIndex: number = event.dragIndex;
            const dropIndex: number = event.dropIndex - 1;
            let count: number = dropIndex == -1 ? 0 : dropIndex;
            while (count <= dragIndex) {
                let ordem: number = count + 1;
                this.listaAtributo[count].ordem_apresentacao = ordem;
                this.listaAtributo[count].alterado = true;
                count++;
            }
        }
        this.notificarIntanciaObjetoAlterado();
    }

    /**
     * Desabilita o dragDrop: removendo a coluna reorder
     */
    desabilitarDragDropOrdemApresentacao() {
        this.disabledDragdrop = true;
        this.configuracaoTableColunas.configTableColsAtributos = this.configuracaoTableColunas.configTableColsAtributosDisabledDragDrop;
    }

    /**
     * Habilita o dragDrop: adicionando a coluna reorder
     */
    habilitarDragDropOrdemApresentacao() {
        this.disabledDragdrop = false;
        this.configuracaoTableColunas.configTableColsAtributos = this.configuracaoTableColunas.configTableColsAtributosEnabledDragDrop;
    }

    abrirModalAtributo(index: number, atributo: ModalAtributoModel) {
        let atributoSelecionado: ModalAtributoModel = atributo ? Object.assign({}, atributo) : undefined;
        // preencher index na lista
        for (let i = 0; i < this.listaAtributo.length; i++) {
            let atr = this.listaAtributo[i];
            atr.indexGrid = i;
        }

        this.dialogService.addDialog(ModalAtributoComponent, {
            indexGrid: index,
            id_atributo: atributo && atributo.id ? atributo.id : undefined,
            atributoSelecionado: atributoSelecionado,
            atributosGrid: Object.assign([], this.listaAtributo)
        }).subscribe(result => {
            if (result.isSalvo) {
                if (index === -1) {

                    if (this.listaAtributo.length == 0) {
                        this.listaAtributo.push(result.atributo);
                    } else {

                        // adicono novo elemento na grid
                        let editarIndex: number = this.verificaSeExisteOrdemRetornaIndex(result.atributo.ordem_apresentacao);
                        let isOrdemDuplicada: boolean = this.listaAtributo.some(atributo => atributo.ordem_apresentacao === result.atributo.ordem_apresentacao);

                        if (isOrdemDuplicada) {
                            // adiciona o atributo na ordem existente e retorna o objeto que substituido
                            let atributoAlterado: ModalAtributoModel = this.listaAtributo.splice(editarIndex, 1, result.atributo)[0];
                            atributoAlterado.objetoAlterado = !atributoAlterado.objetoAlterado ? new ModalAtributoModel() : atributoAlterado.objetoAlterado;
                            atributoAlterado.objetoAlterado.ordem_apresentacao = atributoAlterado.ordem_apresentacao;
                            atributoAlterado.alterado = true;
                            // altera substituido para proxima ordem
                            let ordermNaoUsada: number = this.retornaOrdemNaoUsada();
                            if (ordermNaoUsada) {
                                atributoAlterado.ordem_apresentacao = ordermNaoUsada == null ? atributoSelecionado.ordem_apresentacao + 1 : ordermNaoUsada;
                                // adicionar substituido na proxima posição;
                                this.listaAtributo.push(atributoAlterado);
                            } else {
                                this.listaAtributo[editarIndex].alterado = true;
                                this.listaAtributo[editarIndex].ordem_apresentacao = atributoAlterado.objetoAlterado.ordem_apresentacao;
                                this.listaAtributo[editarIndex].objetoAlterado = !this.listaAtributo[editarIndex].objetoAlterado ? new ModalAtributoModel() : this.listaAtributo[editarIndex].objetoAlterado;
                                this.listaAtributo[editarIndex].objetoAlterado.ordem_apresentacao = atributoAlterado.objetoAlterado.ordem_apresentacao;
                                atributoAlterado.ordem_apresentacao = atributoAlterado.objetoAlterado.ordem_apresentacao + 1;
                                atributoAlterado.objetoAlterado.ordem_apresentacao = atributoAlterado.objetoAlterado.ordem_apresentacao + 1;
                                this.listaAtributo.splice(editarIndex + 1, 0, atributoAlterado);
                                const aux: number = (editarIndex + 1) + 1;
                                for (let i = aux; i < this.listaAtributo.length; i++) {
                                    this.listaAtributo[i].alterado = true;
                                    this.listaAtributo[i].ordem_apresentacao = i + 1;
                                    this.listaAtributo[i].objetoAlterado = !this.listaAtributo[i].objetoAlterado ? new ModalAtributoModel() : this.listaAtributo[i].objetoAlterado;
                                    this.listaAtributo[i].objetoAlterado.ordem_apresentacao = i + 1;
                                }
                            }
                        } else {
                            this.listaAtributo.push(result.atributo);
                        }
                    }
                } else {

                    if (atributoSelecionado.ordem_apresentacao !== result.atributo.ordem_apresentacao) {

                        let isOrdemDuplicada: boolean = this.listaAtributo.some(atributo => atributo.ordem_apresentacao === result.atributo.ordem_apresentacao);
                        let ordemDuplicada: number = null;

                        // descobri ordem duplicada
                        this.listaAtributo.forEach(atributo => {
                            if (atributo.ordem_apresentacao === result.atributo.ordem_apresentacao) {
                                ordemDuplicada = result.atributo.ordem_apresentacao;
                            }
                        });

                        if (isOrdemDuplicada && ordemDuplicada) {
                            let indexDaOrdemDuplicada: number = this.verificaSeExisteOrdemRetornaIndex(ordemDuplicada);

                            // atualiza atributo na lista que foi modificado
                            this.listaAtributo[index] = result.atributo;

                            // recupera atributo com ordem duplicada
                            let atributoAlterado = this.listaAtributo[indexDaOrdemDuplicada];

                            // atualiza atributo que teve ordem duplicada
                            let ordermNaoUsada: number = this.retornaOrdemNaoUsada();
                            atributoAlterado.ordem_apresentacao = ordermNaoUsada == null ? atributoSelecionado.ordem_apresentacao : ordermNaoUsada;
                            atributoAlterado.objetoAlterado = !atributoAlterado.objetoAlterado ? new ModalAtributoModel() : atributoAlterado.objetoAlterado;
                            atributoAlterado.objetoAlterado.ordem_apresentacao = atributoAlterado.ordem_apresentacao
                            atributoAlterado.alterado = true;

                            this.listaAtributo[indexDaOrdemDuplicada] = atributoAlterado;

                        } else {
                            // atualiza atributo na lista que foi modificado
                            this.listaAtributo[index] = result.atributo;
                        }

                    } else if (result.atributo.alterado && result.atributo.objetoAlterado) {
                        this.listaAtributo[index] = result.atributo;
                    }
                }
                Utils.ordenarAtributosPelaOrdemApresentacao(this.listaAtributo);
                this.listaAtributo = Object.assign([], this.listaAtributo);

                this.listaAtributo.forEach(atr => console.log(atr.id + ' - ' + atr.ordem_apresentacao));

                this.atributosChanged.emit(this.listaAtributo);
            }
            this.loadService.hide();
            console.log(result);
            this.custumizeRowsPerPageOptions();
        });
    }

    verificaSeExisteOrdemRetornaIndex(ordem: number): number {
        let index: number = null;

        if (this.listaAtributo && this.listaAtributo.length > 0) {

            for (let i = 0; i < this.listaAtributo.length; i++) {
                let atributo = this.listaAtributo[i];

                if (atributo.ordem_apresentacao === ordem) {
                    return i;
                }
            }

        }

        return index;
    }

    retornaOrdemNaoUsada(): number {
        if (this.listaAtributo && this.listaAtributo.length > 0) {

            for (let i = 0; i < this.listaAtributo.length; i++) {
                let atributo = this.listaAtributo[i];

                let index: number = i + 1;
                let ordem: number = atributo.ordem_apresentacao;

                if (index !== ordem) {
                    return index;
                }
            }
        }
        return null;
    }

    /**
      * Checagem para remoção de atributo
     * @param index
     * @param atributo 
     */
    confirmRemoveAtributo(index: number, atributo: ModalAtributoModel) {
        this.confirmationService.confirm({
            message: 'Tem certeza que deseja remover este atributo ?',
            accept: () => {
                this.removerAtributo(index, atributo);
            }
        });
    }

    realizarLoad() {
        this.countAtributo++;
    }

    /**
     * Remove o atributo pelo index da lista
     * @param index 
     * @param atributo 
     */
    removerAtributo(index: number, atributo: ModalAtributoModel) {
        this.listaAtributo.splice(index, 1);
        if (index < this.listaAtributo.length) {
            this.reordernarAtributos(index + 1);
        } else {
            this.reordernarAtributos(index);
        }
        if (this.listaAtributo.length == 0) {
            if (this.disabledDragdrop) {
                this.configuracaoTableColunas.configTableColsAtributos = this.configuracaoTableColunas.configTableColsAtributosEnabledDragDrop;
            }
            this.disabledDragdrop = false;
        }
        this.custumizeRowsPerPageOptions();
        this.enviarAtributosParaRemocao(atributo);
    }

    onFilter(event: any, globalFilter: any) {
        this.setCountFilterGlobal(event, globalFilter);
    }

    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.filteredRecords = event.filteredValue.length;
        } else {
            this.filteredRecords = 0;
        }
    }

    /**
     * Cria-se uma lista de atributos para remoção
     * @param atributo 
     */
    private enviarAtributosParaRemocao(atributo: ModalAtributoModel) {
        if (atributo.id) {
            this.atributosRemovidosChanged.emit(atributo);
        }
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.listaAtributo);
    }

    /**
     * Reordena os atributos conforme o index referencia de remoção
     * @param indexReference 
     */
    private reordernarAtributos(indexReference: number) {
        let newSequence: number = indexReference;
        while (newSequence > 0) {
            this.listaAtributo[newSequence - 1].ordem_apresentacao = newSequence;
            this.atualizarOrdemApresentacaoAtributoExtracao(newSequence);
            newSequence--;
        }
        for (let i = indexReference; i < this.listaAtributo.length; i++) {
            this.listaAtributo[i].ordem_apresentacao = i + 1;
            this.atualizarOrdemApresentacaoAtributoExtracao(i + 1);
        }
    }

    /**
     * Atualiza a ordem apresentacao na lista de atributos apos remoção
     * verificando a instancia do objeto alterado
     * @param sequence 
     */
    private atualizarOrdemApresentacaoAtributoExtracao(sequence: number) {
        this.listaAtributo[sequence - 1].alterado = true;
        if (this.listaAtributo[sequence - 1].objetoAlterado == undefined) {
            this.listaAtributo[sequence - 1].objetoAlterado = new ModalAtributoModel();
        }
        this.listaAtributo[sequence - 1].objetoAlterado.ordem_apresentacao = sequence;
    }

    /**
     * Atribui os atributos alterados em um novo objeto: objetoAlterado
     * utilizacção drag drop.
     */
    private notificarIntanciaObjetoAlterado() {
        this.listaAtributo.forEach(atributo => {
            if (atributo.alterado) {
                if (atributo.objetoAlterado == undefined) {
                    atributo.objetoAlterado = new ModalAtributoModel();
                }
                atributo.objetoAlterado.ordem_apresentacao = atributo.ordem_apresentacao;
            }
        });
    }
}