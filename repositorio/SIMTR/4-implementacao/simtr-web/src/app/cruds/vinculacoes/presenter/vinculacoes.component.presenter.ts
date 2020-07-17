import { Injectable } from "@angular/core";
import { Vinculacoes } from "../model/vinculacoes.model";
import { Utils } from "../../util/utils";
import { Vinculacao } from "../../model/vinculacao.model";
import { SortEvent, ConfirmationService } from "primeng/primeng";
import { GridVinculacao } from "../model/grid-vinculacao.model";
import { LocalStorageDataService } from "src/app/services/local-storage-data.service";
import { ProcessoPatriarca } from "src/app/services/model/processo-patriarca.model";
import { TipoDocumentoTipologia } from "src/app/services/model/tipo-documento-tipologia.model";
import { FuncaoDocumentalTipologia } from "src/app/services/model/funcao-documental-tipologia.model";
import { DialogService } from "angularx-bootstrap-modal";
import { ModalVinculacaoComponent } from "../modal-vinculacao/view/modal-vinculacao.component";
import { VinculacaoOutput } from "../model/vinculacao-output";
import { VinculacaoDataInput } from "../model/vinculacao-data-input";
import * as moment from 'moment';
import { VINCULACAO } from "../constant.vinculacao";
import { VinculacoesComponent } from "../view/vinculacoes.component";
import { VinculacaoChecklist } from "../../model/vinculacao-checklist";


@Injectable()
export class VinculacoesPresenter {

    vinculacoes: Vinculacoes;

    constructor(private localStorageDataService: LocalStorageDataService,
        private confirmationService: ConfirmationService,
        private dialogService: DialogService) { }

    /**
     * Realiza a configuração da grid de vinculacoes   
     * @param vinculacoesComponent 
     */
    initConfigListaVinculacoes(vinculacoesComponent: VinculacoesComponent) {
        this.custumizeRowsPerPageOptions(vinculacoesComponent.vinculacoes);
        this.converteVinculacoesServicoParaVinculacaoGrid(vinculacoesComponent.vinculacoes);
        this.prepararVinculacoesConflitantesClonadasParaSalvar(vinculacoesComponent);
    }

    /**
     * Ordencao dos campos da grid vinculacoes
     * @param event 
     */
    customSort(event: SortEvent) {
        event.data.sort((data1, data2) => {
            let value1 = data1[event.field];
            let value2 = data2[event.field];
            let result = null;
            if (value1 == null && value2 != null) {
                result = -1;
            } else if (value1 != null && value2 == null) {
                result = 1;
            } else if (value1 == null && value2 == null) {
                result = 0;
            } else if (event.field.indexOf("dataRevogacao") == 0) {
                result = this.sortDataRevogacao(value1, value2, result);
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Abre a modal vinculação: Nova Vinculação
     * @param vinculacoesComponent 
     */
    openModalModalVinculacao(vinculacoesComponent: VinculacoesComponent) {
        this.dialogService.addDialog(ModalVinculacaoComponent, {
            vinculacaoDataInput: this.inicializarInputModalVinculacao(),
        }).subscribe((vinculacaoOutput: VinculacaoOutput) => {
            if (vinculacaoOutput) {
                this.adicionarVinculacaoGrid(vinculacaoOutput, vinculacoesComponent);
            }
        });
    }

    /**
     * Abre a modal vinculação: Edição vinculação conflitante
     * @param vinculacoesComponent 
     */
    openModalModalVinculacaoConflitante(vinculacoesComponent: VinculacoesComponent, indexVinculacaoConflitante: number, gridVinculacao: GridVinculacao) {
        this.dialogService.addDialog(ModalVinculacaoComponent, {
            vinculacaoDataInput: this.inicializarInputModalVinculacaoConflitante(indexVinculacaoConflitante, gridVinculacao),
        }).subscribe((vinculacaoOutput: VinculacaoOutput) => {
            if (vinculacaoOutput) {
                this.adicionarVinculacaoGrid(vinculacaoOutput, vinculacoesComponent);
            }
        });
    }

    /**
     * Checagem para remoção de vinculacao
     * @param index 
     * @param gridVinculacao 
     * @param vinculacoesComponent 
     */
    confirmRemoveVinculacao(index: number, gridVinculacao: GridVinculacao, vinculacoesComponent: VinculacoesComponent) {
        this.confirmationService.confirm({
            message: VINCULACAO.VINCULACAO_REMOVER,
            accept: () => {
                this.removerVinculacao(index, gridVinculacao, vinculacoesComponent);
            }
        });
    }

    /**
     * Remove a vinculacao pelo index da lista
     * @param index 
     * @param gridVinculacao 
     * @param vinculacoesComponent 
     */
    private removerVinculacao(index: number, gridVinculacao: GridVinculacao, vinculacoesComponent: VinculacoesComponent) {
        this.vinculacoes.vinculacoesGrid.splice(index, 1);
        this.vinculacoes.vinculacoes.splice(index, 1);
        this.enviarVinculacoesParaRemocao(gridVinculacao, vinculacoesComponent);
        this.enviarVinculacoesParaSalvar(undefined, vinculacoesComponent);
        this.custumizeRowsPerPageOptions(this.vinculacoes.vinculacoesGrid);
    }

    /**
     * Envia as vinculacoes para remocao: caso tenha ID
     * @param gridVinculacao 
     * @param vinculacoesComponent 
     */
    private enviarVinculacoesParaRemocao(gridVinculacao: GridVinculacao, vinculacoesComponent: VinculacoesComponent) {
        if (gridVinculacao.id) {
            this.vinculacoes.vinculacoesParaRemocao.push(gridVinculacao.vinculacao);
            vinculacoesComponent.vinculacoesRemovidasChanged.emit(this.vinculacoes.vinculacoesParaRemocao);
        }
    }

    /**
     * Cria-se o array de vinculações conflitantes para envio ao checklist
     */
    private inicializarArrayVinculacoesConflitantesEnvioChecklist(): Array<VinculacaoChecklist> {
        const vinculacoesChecklist: Array<VinculacaoChecklist> = new Array<VinculacaoChecklist>();
        this.vinculacoes.vinculacoesGrid.forEach(vinculacaoGrid => {
            const vinculacaoChecklist: VinculacaoChecklist = new VinculacaoChecklist();
            vinculacaoChecklist.vinculacao = vinculacaoGrid.vinculacao;
            vinculacaoChecklist.vinculacaoConflitante = vinculacaoGrid.vinculacaoConflitante;
            vinculacoesChecklist.push(vinculacaoChecklist);
        });
        return vinculacoesChecklist;
    }

    /**
     * Cria-se o array de vinculações para envio ao checklist
     * @param vinculacaoConflitante 
     */
    private inicializarArrayVinculacoesEnvioChecklist(vinculacaoConflitante: Vinculacao): Array<VinculacaoChecklist> {
        const vinculacoesChecklist: Array<VinculacaoChecklist> = new Array<VinculacaoChecklist>();
        this.vinculacoes.vinculacoes.forEach(vinculacao => {
            const vinculacaoChecklist: VinculacaoChecklist = new VinculacaoChecklist();
            vinculacaoChecklist.vinculacao = vinculacao;
            vinculacaoChecklist.vinculacaoConflitante = vinculacaoConflitante;
            vinculacoesChecklist.push(vinculacaoChecklist);
        });
        return vinculacoesChecklist;
    }

    /**
     * Enviar vinculacoes conflitantes para o componente checklist
     * @param vinculacoesComponent 
     */
    private enviarVinculacoesConflitantesParaSalvar(vinculacoesComponent: VinculacoesComponent) {
        vinculacoesComponent.vinculacoesChanged.emit(this.inicializarArrayVinculacoesConflitantesEnvioChecklist());
    }

    /**
     * Enviar vinculacoes para o componente checklist
     * @param vinculacaoConflitante 
     * @param vinculacoesComponent 
     */
    private enviarVinculacoesParaSalvar(vinculacaoConflitante: Vinculacao, vinculacoesComponent: VinculacoesComponent) {
        vinculacoesComponent.vinculacoesChanged.emit(this.inicializarArrayVinculacoesEnvioChecklist(vinculacaoConflitante));
    }

    /**
     * Adiciona uma vinculacao na grid de vinculações
     * para vinculacao conflitante realiza uma edição
     * @param vinculacaoOutput 
     * @param vinculacoesComponent 
     */
    private adicionarVinculacaoGrid(vinculacaoOutput: VinculacaoOutput, vinculacoesComponent: VinculacoesComponent) {
        const gridVinculacao: GridVinculacao = this.inicializarGridVinculacao(vinculacaoOutput.vinculacaoDataOutput.vinculacao,
            vinculacaoOutput.vinculacaoDataOutput.conflitoVinculacao, vinculacaoOutput.vinculacaoDataOutput.vinculacaoConflitante,
            vinculacaoOutput.vinculacaoDataOutput.idChecklistAssociadoVinculacao);
        if (vinculacaoOutput.vinculacaoDataOutput.edicaoConflitoVinculacao) {
            this.vinculacoes.vinculacoes.splice(vinculacaoOutput.vinculacaoDataOutput.indexVinculacaoConflitante, 1, vinculacaoOutput.vinculacaoDataOutput.vinculacao);
            this.vinculacoes.vinculacoesGrid.splice(vinculacaoOutput.vinculacaoDataOutput.indexVinculacaoConflitante, 1, gridVinculacao);
        } else {
            this.vinculacoes.vinculacoes.push(vinculacaoOutput.vinculacaoDataOutput.vinculacao);
            this.vinculacoes.vinculacoesGrid.push(gridVinculacao);
        }
        this.custumizeRowsPerPageOptions(this.vinculacoes.vinculacoesGrid);
        if (!gridVinculacao.id && !vinculacaoOutput.vinculacaoDataOutput.conflitoVinculacao) {
            this.enviarVinculacoesParaSalvar(vinculacaoOutput.vinculacaoDataOutput.vinculacaoConflitante, vinculacoesComponent);
        }
        if (vinculacaoOutput.vinculacaoDataOutput.conflitoVinculacao) {
            this.enviarVinculacoesConflitantesParaSalvar(vinculacoesComponent);
        }
    }

    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.vinculacoes.filteredRecords = event.filteredValue.length;
        } else {
            this.vinculacoes.filteredRecords = 0;
        }
    }

    /**
     * Inicializa o objeto de entrada modal vinculação conflitante: edição
     * @param indexVinculacaoConflitante 
     * @param gridVinculacao 
     */
    private inicializarInputModalVinculacaoConflitante(indexVinculacaoConflitante: number, gridVinculacao: GridVinculacao): VinculacaoDataInput {
        const vinculacaoDataInput: VinculacaoDataInput = new VinculacaoDataInput();
        this.carregarVinculacaoConflitanteSelecionada(indexVinculacaoConflitante, vinculacaoDataInput, gridVinculacao);
        vinculacaoDataInput.vinculacoes = Object.assign(new Array<GridVinculacao>(), this.vinculacoes.vinculacoesGrid);
        vinculacaoDataInput.gridVinculacaoConflitante = gridVinculacao;
        return vinculacaoDataInput;
    }

    /**
     * Inicializa o objeto de entrada modal vinculação
     */
    private inicializarInputModalVinculacao(): VinculacaoDataInput {
        const vinculacaoDataInput: VinculacaoDataInput = new VinculacaoDataInput();
        vinculacaoDataInput.vinculacoes = this.vinculacoes.vinculacoesGrid;
        return vinculacaoDataInput;
    }

    /**
     * carregar objeto edição de vinculação conflitante
     * @param indexVinculacaoConflitante 
     * @param vinculacaoDataInput 
     * @param gridVinculacao 
     */
    private carregarVinculacaoConflitanteSelecionada(indexVinculacaoConflitante: number, vinculacaoDataInput: VinculacaoDataInput, gridVinculacao: GridVinculacao) {
        vinculacaoDataInput.indexVinculacaoConflitante = indexVinculacaoConflitante;
        vinculacaoDataInput.processo = gridVinculacao.processo;
        vinculacaoDataInput.fase = gridVinculacao.fase;
        vinculacaoDataInput.tipoDocumento = gridVinculacao.tipoDocumento;
        vinculacaoDataInput.funcaoDocumental = gridVinculacao.funcaoDocumental;
        vinculacaoDataInput.conflitoVinculacao = gridVinculacao.conflitoVinculacao;
        vinculacaoDataInput.vinculacao = gridVinculacao.vinculacao;
    }

    /**
     * Ordenacao pelo campo data revogação
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortDataRevogacao(value1: any, value2: any, result: any) {
        return Utils.sortDate(value1, value2, result, 'DD/MM/YYYY');
    }

    /**
     * Converte a lista de vinculacoes via serviço para o tipo da grid de vinculacoes
     * certificado se a origem das mesmas é via funcionalidade clone
     * @param vinculacoes 
     */
    private converteVinculacoesServicoParaVinculacaoGrid(vinculacoes: Array<Vinculacao>) {
        const gridVinculacoes: Array<GridVinculacao> = new Array<GridVinculacao>();
        vinculacoes.forEach(vinculacao => {
            let gridVinculacao: GridVinculacao;
            const tipoDocumentoFuncaoDocumental: boolean = ((vinculacao.id_tipo_documento == undefined || vinculacao.id_tipo_documento == null) && (vinculacao.id_funcao_documental == undefined || vinculacao.id_funcao_documental == null));
            if (vinculacao.clone && !tipoDocumentoFuncaoDocumental) {
                vinculacao.id = undefined;
                const vinculacaoConflitante: Vinculacao = new Vinculacao();
                vinculacaoConflitante.id = vinculacao.idVinculacaoConflitante;
                vinculacaoConflitante.data_revogacao = Utils.inicializarPrimeraDataRevogacaoVinculacaoConflitanteVigente();
                const idChecklistAssociadoVinculacao: number = vinculacao.idChecklistAssociadoVinculacao;
                gridVinculacao = this.inicializarGridVinculacao(vinculacao, true, vinculacaoConflitante, idChecklistAssociadoVinculacao);
            } else {
                if (vinculacao.clone && tipoDocumentoFuncaoDocumental) {
                    vinculacao.id = undefined;
                }
                gridVinculacao = this.inicializarGridVinculacao(vinculacao, false, undefined, undefined);
            }
            gridVinculacoes.push(gridVinculacao);
        });
        this.vinculacoes.vinculacoesGrid = gridVinculacoes;
    }

    /**
     * Inicializa um objeto da grid de vinculacao; realizando sua conversao para o tipo adequado
     * @param vinculacao 
     * @param conflitoVinculacao 
     * @param vinculacaoConflitante 
     * @param idChecklistAssociadoVinculacao 
     */
    private inicializarGridVinculacao(vinculacao: Vinculacao, conflitoVinculacao: boolean, vinculacaoConflitante: Vinculacao, idChecklistAssociadoVinculacao: number): GridVinculacao {
        const gridVinculacao: GridVinculacao = new GridVinculacao();
        const processoGeraDossie: ProcessoPatriarca = this.localStorageDataService.buscarProcessoGeraDossiePorId(vinculacao.id_processo_dossie);
        const processoFase: ProcessoPatriarca = this.localStorageDataService.buscarProcessoFasePorId(vinculacao.id_processo_dossie, vinculacao.id_processo_fase);
        const tipoDocumento: TipoDocumentoTipologia = this.localStorageDataService.buscarTipoDocumentoPorId(vinculacao.id_tipo_documento);
        const funcaoDocumentalTipologia: FuncaoDocumentalTipologia = this.localStorageDataService.buscarFuncaoDocumentalPorId(vinculacao.id_funcao_documental);
        gridVinculacao.id = vinculacao.id;
        gridVinculacao.processo = processoGeraDossie && vinculacao.id_processo_dossie ? processoGeraDossie.nome : undefined;
        gridVinculacao.fase = processoFase && vinculacao.id_processo_dossie && vinculacao.id_processo_fase ? processoFase.nome : undefined;
        gridVinculacao.tipoDocumento = tipoDocumento && vinculacao.id_tipo_documento ? tipoDocumento.nome : undefined;
        gridVinculacao.funcaoDocumental = funcaoDocumentalTipologia && vinculacao.id_funcao_documental ? funcaoDocumentalTipologia.nome : undefined;
        const dataRevogacao: string = vinculacao.data_revogacao instanceof Date ? moment(vinculacao.data_revogacao).format('DD/MM/YYYY') : vinculacao.data_revogacao;
        gridVinculacao.dataRevogacao = !vinculacao.clone ? dataRevogacao : undefined;
        gridVinculacao.vinculacao = vinculacao;
        gridVinculacao.conflitoVinculacao = conflitoVinculacao;
        gridVinculacao.vinculacaoConflitante = vinculacaoConflitante;
        this.inicializarMensagemErroVinculacaoConflitante(conflitoVinculacao, vinculacaoConflitante, idChecklistAssociadoVinculacao, gridVinculacao);
        return gridVinculacao;
    }

    /**
     * Atribui no objeto grid vinculacao possivel mensagem de 
     * erro proveniente de vinculação conflitante
     * @param conflitoVinculacao 
     * @param vinculacaoConflitante 
     * @param idChecklistAssociadoVinculacao 
     * @param gridVinculacao 
     */
    private inicializarMensagemErroVinculacaoConflitante(conflitoVinculacao: boolean, vinculacaoConflitante: Vinculacao, idChecklistAssociadoVinculacao: number, gridVinculacao: GridVinculacao) {
        if (conflitoVinculacao) {
            const param1: string = vinculacaoConflitante.id.toString();
            const param2: string = idChecklistAssociadoVinculacao.toString();
            const param3: string = moment(vinculacaoConflitante.data_revogacao).format('DD/MM/YYYY');
            let msg: string = VINCULACAO.VINCULACAO_CHECKLIST_ASSOCIADO.replace('{idVinculacaoConflitante}', param1);
            msg = msg.replace('{idChecklistAssociadoVinculacao}', param2);
            msg = msg.replace('{dataRevogacaoConflitante}', param3);
            gridVinculacao.msgVinculacaoConflitante = msg;
        }
    }

    /**
     * Envia as vinculacoes conflitantes para o componente checklist para salvar
     */
    private prepararVinculacoesConflitantesClonadasParaSalvar(vinculacoesComponent: VinculacoesComponent) {
        if (vinculacoesComponent.cloneChecklist) {
            this.vinculacoes.vinculacoes = vinculacoesComponent.vinculacoes;
            this.enviarVinculacoesConflitantesParaSalvar(vinculacoesComponent);
        }
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     * @param vinculacoes 
     */
    private custumizeRowsPerPageOptions(vinculacoes: Array<any>) {
        this.vinculacoes.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(vinculacoes);
    }

}