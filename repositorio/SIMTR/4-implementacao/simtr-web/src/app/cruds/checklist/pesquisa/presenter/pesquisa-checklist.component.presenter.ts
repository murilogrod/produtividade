import { Injectable } from "@angular/core";
import { LoaderService } from "src/app/services";
import { CheckListService } from "../../service/checklist.service";
import { PesquisaChecklist } from "../model/pesquisa-checklist.model";
import { PesquisaChecklistComponent } from "../view/pesquisa-checklist.component";
import { LocalStorageDataService } from "src/app/services/local-storage-data.service";
import { Utils } from "src/app/cruds/util/utils";
import { GridChecklist } from "../model/grid-checklist.model";
import { ProcessoPatriarca } from "src/app/services/model/processo-patriarca.model";
import { InterfaceProcesso } from "../../../model/processo.interface";
import { FuncaoDocumentalTipologia } from "src/app/services/model/funcao-documental-tipologia.model";
import { InterfaceFuncaoDocumental } from "../../../model/funcao-documental.interface";
import { TipoDocumentoTipologia } from "src/app/services/model/tipo-documento-tipologia.model";
import { InterfaceTipoDocumento } from "../../../model/tipo-documento.interface";
import { Vinculacao } from "../../../model/vinculacao.model";
import { SortEvent } from "primeng/primeng";
import { CHECKLIST } from "../../constant.checklist";
import { DialogService, DialogOptions } from "angularx-bootstrap-modal";
import { ModalCloneInativarRemoverChecklistComponent } from "../modal-clone-inativar-remover-checklist/view/modal-clone-inativar-remover-checklist.component";
import { CloneInativarRemoverChecklistOutput } from "../model/clone-inativar-remover-checklist-output";
import { CloneInativarRemoverChecklistDataOutput } from "../model/clone-inativar-remover-checklist-data-output";
import { CloneInativarRemoverChecklistDataInput } from "../model/clone-inativar-remover-checklist-data-input";
import { Router } from "@angular/router";


@Injectable()
export class PesquisaCheckListPresenter {

    pesquisaChecklist: PesquisaChecklist;

    constructor(private checkListService: CheckListService,
        private localStorageDataService: LocalStorageDataService,
        private loadService: LoaderService,
        private router: Router,
        private dialogService: DialogService) { }

    /**
     * Realiza a configuração da grid de checklists
     * Mostra mensagem de sucesso caso tenha atualização
     * @param pesquisaCanalComponent 
     */
    initConfigListaChecklist(pesquisaChecklistComponent: PesquisaChecklistComponent) {
        this.mostrarMensagemSucessoParaAtualizacaoChecklist(pesquisaChecklistComponent);
        this.reinicializarValoresPadroesAtualizacaoChecklist();
        this.getProcessos();
        this.getFuncoesDocumentais();
        this.getTiposDocumento();
        this.recuperarChecklists();
    }

    /**
     * Navega para outro componente
     * @param url 
     */
    navigateUrl(url: any[]) {
        this.router.navigate(url);
    }

    /**
     * Zera o dropdown tipos documentos
     */
    inicializarTiposDocumentos() {
        Utils.inicializarTiposDocumentos(this.pesquisaChecklist);
    }

    /**
    * Zera o dropdown funcoes documentais
    */
    inicializarFuncoesDocumentais() {
        Utils.inicializarFuncoesDocumentais(this.pesquisaChecklist);
    }

    /**
     * Filtra os checklists conforme as propriedades do mesmo;
     * com pelo menos 2 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataChecklists 
     */
    filterChecklists(input: any, dataChecklists: any) {
        if (input.length == 0) {
            dataChecklists.filterGlobal(input, 'contains');
            this.pesquisaChecklist.filteredRecords = 0;
        }
        if (input.length > 1) {
            dataChecklists.filterGlobal(input, 'contains');
        }
    }

    /**
     * Busca as fases do processo gera dossie selecionado
     */
    getFasesGeraDossie() {
        if (this.pesquisaChecklist.selectedProcesso) {
            this.pesquisaChecklist.fases = this.buscarProcessosFasePorIdProcessoGeraDossie();
            if (this.pesquisaChecklist.fases.length > 0) {
                this.pesquisaChecklist.emptyProcesso = false;
            } else {
                this.pesquisaChecklist.emptyProcesso = true;
            }
        }
    }

    /**
     * Ordencao dos campos da grid checklist
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
            } else if (event.field.indexOf("data_hora_criacao") == 0) {
                result = this.sortDataHoraCriacao(value1, value2, result);
            } else if (event.field.indexOf("ativo") == 0) {
                result = this.sortDataInativacao(data1, data2, result);
            } else if (value1 == null && value2 == null) {
                result = 0;
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Mostra a mensagem de sucesso ao adicionar ou atualizar um checklist
     * @param pesquisaChecklistComponent 
     */
    private mostrarMensagemSucessoParaAtualizacaoChecklist(pesquisaChecklistComponent: PesquisaChecklistComponent) {
        if (this.checkListService.getNovoChecklist()) {
            const msg: string = CHECKLIST.CHECKLIST_ADICIONADO_SUCESSO.replace('{id}', this.checkListService.getId().toString());
            pesquisaChecklistComponent.addMessageSuccess(msg);
        }
        if (this.checkListService.getEdicaoChecklist()) {
            const msg: string = CHECKLIST.CHECKLIST_ATUALIZADO_SUCESSO.replace('{id}', this.checkListService.getId().toString());
            pesquisaChecklistComponent.addMessageSuccess(msg);
        }
    }

    /**
     * Reseta os booleanos que controlam a chamada 
     * da mensagem de atualização de checklist
     */
    private reinicializarValoresPadroesAtualizacaoChecklist() {
        this.checkListService.setEdicaoChecklist(false);
        this.checkListService.setNovoChecklist(false);
    }

    /**
     * Ordenação por ativação checklist
     * @param data1 
     * @param data2 
     * @param result 
     */
    private sortDataInativacao(data1: any, data2: any, result: any) {
        const dt1: boolean = data1.data_hora_inativacao !== undefined && data1.data_hora_inativacao !== null;
        const dt2: boolean = data2.data_hora_inativacao !== undefined && data2.data_hora_inativacao !== null;
        if (!dt1 && dt2) {
            result = 1;
        }
        else if (dt1 && !dt2) {
            result = -1;
        }
        else {
            result = 0;
        }
        return result;
    }

    /**
     * Ajusta o sumario da grid checklist conforme a adição de checklists ativos e inativos
     */
    anularRegistrosFiltrados() {
        this.pesquisaChecklist.filteredRecords = 0;
        if (this.pesquisaChecklist.inactiveChecklist) {
            this.pesquisaChecklist.totalRecords = this.pesquisaChecklist.inactiveChecklists.length;
        } else {
            this.pesquisaChecklist.totalRecords = this.pesquisaChecklist.checklists.length;
        }
    }

    /**
     * Filtra todos os checklists conforme o tipo documento selecionado; 
     * checando os filtros: gera dossie, fase, situacao, situacao checklist, 
     * tipo documento e funcao documental caso marcados
     */
    filtrarPorSelecaoFiltros() {
        const processoChecklists: Set<GridChecklist> = new Set<GridChecklist>();
        let checklists: Array<GridChecklist> = this.inicializarChecklistConformeSituacao();
        this.pesquisaChecklist.tempChecklists = checklists;
        checklists.forEach(obj => {
            if (obj.vinculacoes) {
                obj.vinculacoes.forEach(vinculacao => {
                    const condition: string = this.montarCondicionalFiltros(obj, vinculacao);
                    if (eval(condition)) {
                        processoChecklists.add(obj);
                    }
                });
            }
        });
        const filtrosDesmarcados: boolean = this.verificarDesmarcacaoFiltros();
        if (filtrosDesmarcados) {
            this.fitrarPorVerificacaoPreviaSituacao();
        } else {
            this.pesquisaChecklist.checklists = Array.from(processoChecklists);
        }
        if (this.pesquisaChecklist.checklists.length == this.pesquisaChecklist.totalRecords) {
            this.pesquisaChecklist.filteredRecords = 0;
            this.pesquisaChecklist.totalRecords = this.pesquisaChecklist.checklists.length;
        } else {
            this.pesquisaChecklist.filteredRecords = this.pesquisaChecklist.checklists.length;
        }
    }

    /**
     * Abre a modal clone inativar remover checklist
     * @param pesquisaChecklistComponent 
     * @param checklist 
     * @param cloneChecklist 
     */
    openModalCloneInativarRemoverChecklist(pesquisaChecklistComponent: PesquisaChecklistComponent, gridChecklist: GridChecklist, cloneChecklist: boolean) {
        const options: DialogOptions = <DialogOptions>{ closeByClickingOutside: true };
        this.dialogService.addDialog(ModalCloneInativarRemoverChecklistComponent, {
            cloneInativarRemoverChecklistDataInput: this.inicializarObjetoEntradaModalCloneInativarRemoverChecklist(gridChecklist, cloneChecklist)
        }, options).subscribe((cloneInativarRemoverChecklistOutput: CloneInativarRemoverChecklistOutput) => {
            if (cloneInativarRemoverChecklistOutput) {
                this.realizarRemocaoInativacaoCopiaChecklist(pesquisaChecklistComponent, cloneInativarRemoverChecklistOutput.cloneInativarRemoverChecklistDataOutput);
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
            this.pesquisaChecklist.filteredRecords = event.filteredValue.length;
        } else {
            this.pesquisaChecklist.filteredRecords = this.pesquisaChecklist.checklists.length;
        }
    }

    /**
     * Inicializa o objeto de entrada modal clone inativar remover checklist
     * @param gridChecklist 
     * @param cloneChecklist 
     */
    private inicializarObjetoEntradaModalCloneInativarRemoverChecklist(gridChecklist: GridChecklist, cloneChecklist: boolean): CloneInativarRemoverChecklistDataInput {
        const cloneInativarRemoverChecklistDataInput: CloneInativarRemoverChecklistDataInput = new CloneInativarRemoverChecklistDataInput();
        cloneInativarRemoverChecklistDataInput.gridChecklist = gridChecklist;
        cloneInativarRemoverChecklistDataInput.cloneChecklist = cloneChecklist;
        return cloneInativarRemoverChecklistDataInput;

    }

    /**
     * Retorno modal clone inativar remover checklist devolve uma copia 
     * de checklist, removação ou  apenas uma inativação de checklist
     * @param pesquisaChecklistComponent 
     * @param cloneInativarChecklistDataOutput 
     */
    private realizarRemocaoInativacaoCopiaChecklist(pesquisaChecklistComponent: PesquisaChecklistComponent, cloneInativarRemoverChecklistDataOutput: CloneInativarRemoverChecklistDataOutput) {
        if (cloneInativarRemoverChecklistDataOutput.inativarChecklist) {
            const msg: string = CHECKLIST.CHECKLIST_INATIVADO_SUCESSO.replace('{id}', cloneInativarRemoverChecklistDataOutput.id.toString());
            pesquisaChecklistComponent.addMessageSuccess(msg);
            this.reinicializarPesquisaChecklist();
        }
        if (cloneInativarRemoverChecklistDataOutput.removerChecklist) {
            const msg: string = CHECKLIST.CHECKLIST_REMOVER_SUCESSO.replace('{id}', cloneInativarRemoverChecklistDataOutput.id.toString());
            pesquisaChecklistComponent.addMessageSuccess(msg);
            this.reinicializarPesquisaChecklist();
        }
        if (cloneInativarRemoverChecklistDataOutput.cloneChecklist) {
            this.navigateUrl([CHECKLIST.CHECKLIST_NOVO]);
        }

    }

    /**
     * Devolve o padrão para a pesquisa checklist
     */
    private reinicializarPesquisaChecklist() {
        this.pesquisaChecklist.inactiveChecklist = false;
        this.recuperarChecklists();
        this.filtrarPorSelecaoFiltros();
    }

    /**
     * Ordenacao pelo campo data hora criação
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortDataHoraCriacao(value1: any, value2: any, result: any) {
        return Utils.sortDate(value1, value2, result, 'DD/MM/YYYY HH:mm:ss');
    }

    /**
     * Verifica os filtros: verificacao previa e situacao
     */
    private fitrarPorVerificacaoPreviaSituacao() {
        if (!this.pesquisaChecklist.verificacaoPreviaFiltro && this.pesquisaChecklist.situacaoFiltro) {
            this.pesquisaChecklist.checklists = this.pesquisaChecklist.tempChecklists;
        }
        else if (this.pesquisaChecklist.verificacaoPreviaFiltro && !this.pesquisaChecklist.situacaoFiltro) {
            this.pesquisaChecklist.checklists = this.pesquisaChecklist.tempChecklists.filter(checklist => checklist.verificacao_previa == true);
        }
        else if (!this.pesquisaChecklist.verificacaoPreviaFiltro && this.pesquisaChecklist.situacaoFiltro) {
            this.pesquisaChecklist.checklists = this.pesquisaChecklist.tempChecklists.filter(checklist => checklist.data_hora_inativacao == (undefined || null));
        }
        else if (!this.pesquisaChecklist.verificacaoPreviaFiltro && !this.pesquisaChecklist.situacaoFiltro) {
            this.pesquisaChecklist.checklists = this.pesquisaChecklist.tempChecklists.filter(checklist => checklist.verificacao_previa == false && checklist.data_hora_inativacao !== (undefined || null));
        }
        else {
            this.pesquisaChecklist.checklists = this.pesquisaChecklist.tempChecklists.filter(checklist => checklist.verificacao_previa == true && checklist.data_hora_inativacao == (undefined || null));
        }
    }

    /**
     * Devolve os checklist conforme a sua situação: ativo e inativo
     */
    private inicializarChecklistConformeSituacao() {
        let checklists: Array<GridChecklist> = new Array<GridChecklist>();
        if (this.pesquisaChecklist.inactiveChecklist) {
            checklists = this.pesquisaChecklist.inactiveChecklists;
        }
        else {
            checklists = this.pesquisaChecklist.activeChecklists;
        }
        return checklists;
    }

    /**
     * Verifica a seleção dos dropdown: gera dossie, fase, situacao,  
     * tipo documento e funcao documental
     * @param vinculacao 
     * @param checklist 
     */
    private montarCondicionalFiltros(checklist: GridChecklist, vinculacao: Vinculacao): string {
        const roles: Array<string> = new Array<string>();
        const inactiveChecklist: boolean = this.pesquisaChecklist.inactiveChecklist;
        const activeCondition: boolean = inactiveChecklist ? checklist.data_hora_inativacao !== undefined && checklist.data_hora_inativacao !== null : checklist.data_hora_inativacao == undefined || checklist.data_hora_inativacao == null;
        const verificacaoPrevia: boolean = this.pesquisaChecklist.verificacaoPreviaFiltro;
        const situacao: boolean = this.pesquisaChecklist.situacaoFiltro;
        const verificacaoPreviaCondition: boolean = verificacaoPrevia ? checklist.verificacao_previa == true : checklist.verificacao_previa == false;
        const situacaoCondition: boolean = situacao ? checklist.data_hora_inativacao == (undefined || null) : checklist.data_hora_inativacao !== (undefined || null);
        let operatorAnd: boolean = false;

        if (this.pesquisaChecklist.selectedProcesso) {
            const roleFilter: string = ` ${vinculacao.id_processo_dossie} == ${this.pesquisaChecklist.selectedProcesso.key} && ${activeCondition} && ${verificacaoPreviaCondition} && ${situacaoCondition} `;
            roles.push(roleFilter);
            operatorAnd = true;
        }
        if (this.pesquisaChecklist.selectedFase) {
            const roleFilter: string = ` && ${vinculacao.id_processo_fase} == ${this.pesquisaChecklist.selectedFase.key} && ${activeCondition} && ${verificacaoPreviaCondition} && ${situacaoCondition} `;
            roles.push(roleFilter);
            operatorAnd = true;
        }

        if (this.pesquisaChecklist.selectedTipoDocumento) {
            const roleFilter: string = ` ${operatorAnd ? '&&' : ''} ${vinculacao.id_tipo_documento} == ${this.pesquisaChecklist.selectedTipoDocumento.key} && ${activeCondition} && ${verificacaoPreviaCondition} && ${situacaoCondition} `;
            roles.push(roleFilter);
            operatorAnd = true;
        }
        if (this.pesquisaChecklist.selectedFuncaoDocumental) {
            const roleFilter: string = ` ${operatorAnd ? '&&' : ''} ${vinculacao.id_funcao_documental} == ${this.pesquisaChecklist.selectedFuncaoDocumental.key} && ${activeCondition} && ${verificacaoPreviaCondition} && ${situacaoCondition} `;
            roles.push(roleFilter);
        }

        return roles.toString().replace(/,/g, '');
    }

    /**
     * Verifica se os filtros: processo, fase, tipo documento e 
     * funcao documental estão desmarcados
     */
    private verificarDesmarcacaoFiltros(): boolean {
        const processo: boolean = this.pesquisaChecklist.selectedProcesso != undefined && this.pesquisaChecklist.selectedProcesso !== null;
        const fase: boolean = this.pesquisaChecklist.selectedFase != undefined && this.pesquisaChecklist.selectedFase !== null;
        const tipoDocumento: boolean = this.pesquisaChecklist.selectedTipoDocumento != undefined && this.pesquisaChecklist.selectedTipoDocumento !== null;
        const funcaoDocumental: boolean = this.pesquisaChecklist.selectedFuncaoDocumental !== undefined && this.pesquisaChecklist.selectedFuncaoDocumental !== null;
        return !processo && !fase && !tipoDocumento && !funcaoDocumental;
    }

    /**
     * inicializa dropdown com todos os processos
     */
    private getProcessos() {
        this.pesquisaChecklist.processos = this.buscarProcessosFilhoMacroProcesso();
    }

    /**
     * inicializa dropdown com todos as Funções Documentais
     */
    private getFuncoesDocumentais() {
        this.pesquisaChecklist.funcoesDocumentais = this.buscarFuncoesDocumentaisTipologia();
    }

    /**
     * inicializa dropdown com todos as Funções Documentais
     */
    private getTiposDocumento() {
        this.pesquisaChecklist.tiposDocumento = this.buscarTiposDocumentoTipologia();
    }

    /**
     * Recupera os checklists
     */
    private recuperarChecklists() {
        this.loadService.show();
        this.checkListService.get().subscribe(dados => {
            this.filtrarChecklistsAtivos(dados);
            this.custumizeRowsPerPageOptions();
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            throw error;
        });
    }

    /**
     *  Padrão: checklists ativos: Guardando um copia de ativos e inativos
     * @param dados 
     */
    private filtrarChecklistsAtivos(dados: any) {
        this.checkListService.setChecklists(dados);
        this.pesquisaChecklist.activeChecklists = dados.filter(checklist => checklist.data_hora_inativacao == undefined);
        this.pesquisaChecklist.inactiveChecklists = dados.filter(checklist => checklist.data_hora_inativacao !== undefined);
        this.pesquisaChecklist.checklists = this.pesquisaChecklist.activeChecklists;
        this.pesquisaChecklist.tempChecklists = this.pesquisaChecklist.checklists;
        this.pesquisaChecklist.totalRecords = this.pesquisaChecklist.checklists.length;
    }

    /**
     * Busca todos os processos gera dossie dos macro processos
     */
    private buscarProcessosFilhoMacroProcesso(): Array<InterfaceProcesso> {
        const processosLocalStorage: Array<ProcessoPatriarca> = this.localStorageDataService.buscarProcesosGeraDossiePeloMacroProcesso();
        return Utils.buscarProcessosFilhoMacroProcesso(processosLocalStorage);
    }

    /**
     * Busca todos os processos fase passando o id
     */
    private buscarProcessosFasePorIdProcessoGeraDossie(): Array<InterfaceProcesso> {
        const processosLocalStorage: Array<ProcessoPatriarca> = this.localStorageDataService.buscarProcessosFasePassandoIdProcessoGeraDossie(this.pesquisaChecklist.selectedProcesso.key);
        return Utils.buscarProcessosFasePorIdProcessoGeraDossie(processosLocalStorage);
    }

    /**
     * Busca todos as funcoes documentais da tipologia
     */
    private buscarFuncoesDocumentaisTipologia(): Array<InterfaceFuncaoDocumental> {
        const funcoesDocumentalLocalStorage: Array<FuncaoDocumentalTipologia> = this.localStorageDataService.buscarFuncoesDocumentaisTipologia();
        return Utils.buscarFuncoesDocumentaisTipologia(funcoesDocumentalLocalStorage);
    }

    /**
     * Busca todos os tipos de documento da tipologia
     */
    private buscarTiposDocumentoTipologia(): Array<InterfaceTipoDocumento> {
        const tipoDocumentosLocalStorage: Array<TipoDocumentoTipologia> = this.localStorageDataService.buscarTiposDocumentosTipologia();
        return Utils.buscarTiposDocumentoTipologia(tipoDocumentosLocalStorage);
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.pesquisaChecklist.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.pesquisaChecklist.checklists);
    }

}