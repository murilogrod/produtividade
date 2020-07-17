import { Injectable } from "@angular/core";
import { ModalVinculacao } from "../model/modal-vinculacao.model";
import { LocalStorageDataService } from "src/app/services/local-storage-data.service";
import { ProcessoPatriarca } from "src/app/services/model/processo-patriarca.model";
import { InterfaceProcesso } from "../../../model/processo.interface";
import { FuncaoDocumentalTipologia } from "src/app/services/model/funcao-documental-tipologia.model";
import { InterfaceFuncaoDocumental } from "../../../model/funcao-documental.interface";
import { TipoDocumentoTipologia } from "src/app/services/model/tipo-documento-tipologia.model";
import { InterfaceTipoDocumento } from "../../../model/tipo-documento.interface";
import { Utils } from "src/app/cruds/util/utils";
import { VinculacaoDataInput } from "../../model/vinculacao-data-input";
import { Vinculacao } from "src/app/cruds/model/vinculacao.model";
import { ModalVinculacaoComponent } from "../view/modal-vinculacao.component";
import { VinculacaoOutput } from "../../model/vinculacao-output";
import { VinculacaoDataOutput } from "../../model/vinculacao-data-output";
import * as moment from 'moment';
import { GrowlMessageService } from "src/app/cruds/growl-message-service/growl-message.service";
import { VINCULACAO } from "../../constant.vinculacao";
import { CheckListService } from "src/app/cruds/checklist/service/checklist.service";
import { LoaderService } from "src/app/services";
import { GridChecklist } from "src/app/cruds/checklist/pesquisa/model/grid-checklist.model";
import { VinculacaoChecklistConflitante } from "../model/vinculacao-checklist-conflitante";


@Injectable()
export class ModalVinculacaoPresenter {

    modalVinculacao: ModalVinculacao;

    constructor(private localStorageDataService: LocalStorageDataService,
        private checkListService: CheckListService,
        private loadService: LoaderService,
        private growlMessageService: GrowlMessageService) { }

    /**
     * Realiza a configuração modal vinculacao: carregamento combos
     * processos, fucoes documentais e tipos documentos, 
     * dados componente calendário e carregamento checklists ativos.
     * Carregamento vinculacao conflitante
     * @param vinculacaoDataInput 
     */
    initConfigModalVinculacao(vinculacaoDataInput: VinculacaoDataInput) {
        this.getProcessos();
        this.getFuncoesDocumentais();
        this.getTiposDocumento();
        this.configDataRevogacao();
        this.carregarChecklistsAtivosVerificacaoVinculacaoConflitante(vinculacaoDataInput);
    }

    /**
     * Zera o dropdown tipos documentos
     */
    inicializarTiposDocumentos() {
        Utils.inicializarTiposDocumentos(this.modalVinculacao);
    }

    /**
    * Zera o dropdown funcoes documentais
    */
    inicializarFuncoesDocumentais() {
        Utils.inicializarFuncoesDocumentais(this.modalVinculacao);
    }

    /**
     * Busca as fases do processo gera dossie selecionado
     */
    getFasesGeraDossie() {
        if (this.modalVinculacao.selectedProcesso) {
            this.modalVinculacao.fases = this.buscarProcessosFasePorIdProcessoGeraDossie(this.modalVinculacao.selectedProcesso.key);
            if (this.modalVinculacao.fases.length > 0) {
                this.modalVinculacao.invalid = false;
                this.modalVinculacao.emptyProcesso = false;
            } else {
                this.modalVinculacao.invalid = true;
                this.modalVinculacao.emptyProcesso = true;
                this.modalVinculacao.selectedFase = undefined;
            }
        }
    }

    /**
     * Fecha a modal vinculacao devolvedo seu objeto de saida
     * @param modalVinculacaoComponent 
     */
    adicionarVinculacaoGrid(modalVinculacaoComponent: ModalVinculacaoComponent) {
        modalVinculacaoComponent.dialogReturn =
            <VinculacaoOutput>{
                vinculacaoDataOutput: this.inicializarVinculacaoSelecionada(modalVinculacaoComponent.vinculacaoDataInput)
            };
        modalVinculacaoComponent.closeDialog();
    }

    /**
     * Verifica se a data de revogação é menor que a data atual + 1
     */
    verificarDataRevogacaoFutura() {
        if (this.modalVinculacao.dataRevogacao.getTime() < this.modalVinculacao.dataRevogacaoVigente.getTime()) {
            this.modalVinculacao.showErrorDataRevogacao = true;
        } else {
            this.modalVinculacao.showErrorDataRevogacao = false;
        }
        this.verificarOcorrenciaVinculacaoComMesmaDataRevogacao();
    }

    /**
     * Controle para renderização bloco data ativação: conflito vinculacao
     * Verificando a lista completa de checklists ativos para filtragem de vinculação
     * conflitante
     * @param vinculacaoDataInput 
     */
    validarConflitoVinculacao(vinculacaoDataInput: VinculacaoDataInput) {
        this.validarOcorrenciaVinculacaoMemoria(vinculacaoDataInput);
        this.validarVinculacaoConflitante(true);
        if (this.validarVinculacaoConflitante(false) && !this.modalVinculacao.invalid) {
            this.modalVinculacao.conflitoVinculacao = true;
            this.modalVinculacao.dataRevogacao = this.modalVinculacao.dataRevogacaoVigente;
            this.growlMessageService.showError(VINCULACAO.VINCULACAO_VALIDACAO, VINCULACAO.VINCULACAO_CONFLITO);
        } else {
            this.modalVinculacao.conflitoVinculacao = false;
            this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = false;
            this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = false;
            this.modalVinculacao.ocorrenciaVinculacaoFase = false;
            this.modalVinculacao.dataRevogacao = undefined;
        }
        this.verificarOcorrenciaVinculacaoComMesmaDataRevogacao();
    }

    /**
     * Verifica ocorrencia de vinculacao na grid em memória de vinculações
     * @param vinculacaoDataInput 
     */
    validarOcorrenciaVinculacaoMemoria(vinculacaoDataInput: VinculacaoDataInput) {
        this.modalVinculacao.ocorrenciaVinculacao = this.validarOcorrenciaVinculacao(vinculacaoDataInput);
        if (this.modalVinculacao.ocorrenciaVinculacao) {
            this.growlMessageService.showError('Validação', VINCULACAO.VINCULACAO_REPETIDA);
            this.modalVinculacao.invalid = true;
        } else {
            this.modalVinculacao.invalid = false;
        }
    }

    /**
     * Validar o dropdown fase/processo: DIV vermelha
     * @param fase 
     */
    validarFaseProcesso(faseProcesso: any): boolean {
        return ((faseProcesso.dirty && faseProcesso.errors && faseProcesso.errors.required) || (this.modalVinculacao.ocorrenciaVinculacaoFase) || (this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento) || (this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental));
    }

    /**
     * Condições para desabilitar botão: Adicionar Vinculação
     * @param vinculacaoForm 
     * @param processo 
     * @param fase 
     */
    disabledAddButton(vinculacaoForm: any, processo: any, fase: any): boolean {
        let disabled = true;
        if (processo.value && fase.value) {
            disabled = this.modalVinculacao.showErrorDataRevogacao || this.modalVinculacao.showErrorOcorrenciaDataRevogacao || !vinculacaoForm.valid || this.modalVinculacao.invalid;
        }
        return disabled;
    }

    /**
     * Caso a vinculacao conflitante tenha a mesma data de outra já persistida; 
     * o erro será lançado em tela impendindo a adição dessa vinculação.
     */
    private verificarOcorrenciaVinculacaoComMesmaDataRevogacao() {
        if (this.validarOcorrenciaDataRevogacaoVinculacaoConflitante()) {
            this.modalVinculacao.showErrorOcorrenciaDataRevogacao = true;
        } else {
            this.modalVinculacao.showErrorOcorrenciaDataRevogacao = false;
        }
    }

    /**
     * Verifica se existe alguma vinculacao conflitante com data revogação igual
     * a uma existente já persistida
     */
    private validarOcorrenciaDataRevogacaoVinculacaoConflitante(): boolean {
        return Array.from(this.modalVinculacao.vinculacoesConflitante).some(vinculacaoConflitante => {
            if (vinculacaoConflitante && vinculacaoConflitante.vinculacao && vinculacaoConflitante.vinculacao.data_revogacao && this.modalVinculacao.dataRevogacao) {
                const value1: Date = moment(vinculacaoConflitante.vinculacao.data_revogacao, 'DD/MM/YYYY').toDate();
                const value2: Date = this.modalVinculacao.dataRevogacao;
                const matchDataRevogacao: boolean = value1.getTime() == value2.getTime();
                if (matchDataRevogacao) {
                    const param1: string = vinculacaoConflitante.vinculacao.id.toString();
                    const param2: string = vinculacaoConflitante.idChecklistVinculacaoConflitante.toString();
                    let msg: string = VINCULACAO.VINCULACAO_DATA_USADA.replace('{idVinculacaoConflitante}', param1);
                    msg = msg.replace('{idChecklistAssociadoVinculacao}', param2);
                    this.modalVinculacao.msgDataRevogacaoError = msg;
                }
                return matchDataRevogacao;
            }
        })
    }

    /**
     * Verifica a existencia de checklists ativos no service: checklist
     * inicializa os checklist ativos do componente: modalVinculacao
     * carrega uma possivel vinculação conflitante: edição
     * @param vinculacaoDataInput 
     */
    private carregarChecklistsAtivosVerificacaoVinculacaoConflitante(vinculacaoDataInput: VinculacaoDataInput) {
        if (this.checkListService.existChecklists()) {
            this.modalVinculacao.activeChecklists = this.checkListService.getChecklists().filter(checklist => checklist.data_hora_inativacao == undefined);
            this.carregarVinculacaoConflitante(vinculacaoDataInput);
        } else {
            this.carregarChecklistsAtivos(vinculacaoDataInput);
        }
    }

    /**
     * Verifica a ocorrencia de vinculação na grid 
     * @param vinculacaoDataInput 
     */
    private validarOcorrenciaVinculacao(vinculacaoDataInput: VinculacaoDataInput): boolean {
        let vinculacaoConflitante: boolean = false;
        this.removerVinculacaoConflitante(vinculacaoDataInput);
        for (let i: number = 0; i < vinculacaoDataInput.vinculacoes.length; i++) {
            const tipoDocumento: boolean = vinculacaoDataInput.vinculacoes[i].vinculacao.id_tipo_documento == undefined || vinculacaoDataInput.vinculacoes[i].vinculacao.id_tipo_documento == null;
            const funcaoDocumental: boolean = vinculacaoDataInput.vinculacoes[i].vinculacao.id_funcao_documental == undefined || vinculacaoDataInput.vinculacoes[i].vinculacao.id_funcao_documental == null;
            if (this.modalVinculacao.selectedProcesso && this.modalVinculacao.selectedFase && this.modalVinculacao.selectedFuncaoDocumental &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_dossie == this.modalVinculacao.selectedProcesso.key &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_fase == this.modalVinculacao.selectedFase.key && this.modalVinculacao.selectedFuncaoDocumental &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_funcao_documental == this.modalVinculacao.selectedFuncaoDocumental.key) {
                this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = true;
                this.modalVinculacao.ocorrenciaVinculacaoFase = false;
                this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = false;
                vinculacaoConflitante = true;
                break;
            } else if (this.modalVinculacao.selectedProcesso && this.modalVinculacao.selectedFase && this.modalVinculacao.selectedTipoDocumento &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_dossie == this.modalVinculacao.selectedProcesso.key &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_fase == this.modalVinculacao.selectedFase.key && this.modalVinculacao.selectedTipoDocumento &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_tipo_documento == this.modalVinculacao.selectedTipoDocumento.key) {
                vinculacaoConflitante = true;
                this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = true;
                this.modalVinculacao.ocorrenciaVinculacaoFase = false;
                this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = false;
                break;
            } else if (this.modalVinculacao.selectedProcesso && this.modalVinculacao.selectedFase &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_dossie == this.modalVinculacao.selectedProcesso.key &&
                vinculacaoDataInput.vinculacoes[i].vinculacao.id_processo_fase == this.modalVinculacao.selectedFase.key &&
                tipoDocumento && funcaoDocumental) {
                vinculacaoConflitante = true;
                this.modalVinculacao.ocorrenciaVinculacaoFase = true;
                this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = false;
                this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = false;
                break;
            } else {
                vinculacaoConflitante = false;
                this.modalVinculacao.ocorrenciaVinculacaoFase = false;
                this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = false;
                this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = false;
            }
        }
        return vinculacaoConflitante;
    }

    /**
     * Remove a vinculacao conflitante; a mesma nao precisa de validação por ocorrencia
     * cenário possivel apenas para edição
     * @param vinculacaoDataInput 
     */
    private removerVinculacaoConflitante(vinculacaoDataInput: VinculacaoDataInput) {
        if (vinculacaoDataInput.conflitoVinculacao && !this.modalVinculacao.vinculacaoConflitanteRemovida) {
            this.modalVinculacao.vinculacaoConflitanteRemovida = true;
            vinculacaoDataInput.vinculacoes.splice(vinculacaoDataInput.indexVinculacaoConflitante, 1);
        }
    }

    /**
     * Ordenação de checklists: maior ID
     * @param activesChecklists 
     */
    private ordenarChecklistsAtivosPorId(activesChecklists: Array<GridChecklist>) {
        activesChecklists.sort((ck1, ck2): number => {
            if (ck1.id > ck2.id)
                return -1;
            if (ck1.id < ck2.id)
                return 1;
            return 0;
        });
    }

    /**
     * Validar a vinculação; caso exista uma igual; 
     * a data de ativação será necessária para revogar a vinculacao conflitante
     * @param copiaVinculacoesConflitantes 
     */
    private validarVinculacaoConflitante(copiaVinculacoesConflitantes: boolean): boolean {
        let vinculacaoExistente: boolean = false;
        let conflitoChecado: boolean = false;
        const activesChecklists: Array<GridChecklist> = this.modalVinculacao.activeChecklists;
        this.ordenarChecklistsAtivosPorId(activesChecklists);
        const tipoDocumento: boolean = !this.modalVinculacao.conflitoVinculacao ? this.modalVinculacao.selectedTipoDocumento !== undefined && this.modalVinculacao.selectedTipoDocumento !== null : this.modalVinculacao.selectedTipoDocumento && this.modalVinculacao.selectedTipoDocumento.key !== undefined && this.modalVinculacao.selectedTipoDocumento.key !== null;
        const funcaoDocumental: boolean = !this.modalVinculacao.conflitoVinculacao ? this.modalVinculacao.selectedFuncaoDocumental !== undefined && this.modalVinculacao.selectedFuncaoDocumental !== null : this.modalVinculacao.selectedFuncaoDocumental && this.modalVinculacao.selectedFuncaoDocumental.key !== undefined && this.modalVinculacao.selectedFuncaoDocumental.key !== null;
        if (this.modalVinculacao.selectedProcesso && this.modalVinculacao.selectedFase && funcaoDocumental && !conflitoChecado) {
            for (let i: number = 0; i < activesChecklists.length; i++) {
                if (activesChecklists[i].vinculacoes) {
                    vinculacaoExistente = activesChecklists[i].vinculacoes.some(vinculacao => {
                        const conflito: boolean = vinculacao.id_processo_dossie == this.modalVinculacao.selectedProcesso.key &&
                            vinculacao.id_processo_fase == this.modalVinculacao.selectedFase.key &&
                            vinculacao.id_funcao_documental == this.modalVinculacao.selectedFuncaoDocumental.key;
                        if (conflito) {
                            this.modalVinculacao.vinculacaoConflitante = vinculacao;
                        }
                        return conflito;
                    });
                    if (vinculacaoExistente && !copiaVinculacoesConflitantes) {
                        this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = true;
                        this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = false;
                        this.modalVinculacao.ocorrenciaVinculacaoFase = false;
                        break;
                    } else {
                        this.inicializarVinculacoesConflitantes(activesChecklists, i);
                    }
                }
            }
            conflitoChecado = true;
        }
        if (this.modalVinculacao.selectedProcesso && this.modalVinculacao.selectedFase && tipoDocumento && !conflitoChecado) {
            for (let i: number = 0; i < activesChecklists.length; i++) {
                if (activesChecklists[i].vinculacoes) {
                    vinculacaoExistente = activesChecklists[i].vinculacoes.some(vinculacao => {
                        const conflito: boolean = vinculacao.id_processo_dossie == this.modalVinculacao.selectedProcesso.key &&
                            vinculacao.id_processo_fase == this.modalVinculacao.selectedFase.key &&
                            vinculacao.id_tipo_documento == this.modalVinculacao.selectedTipoDocumento.key;
                        if (conflito) {
                            this.modalVinculacao.vinculacaoConflitante = vinculacao;
                        }
                        return conflito;
                    });
                    if (vinculacaoExistente && !copiaVinculacoesConflitantes) {
                        this.modalVinculacao.ocorrenciaVinculacaoTipoDocumento = true;
                        this.modalVinculacao.ocorrenciaVinculacaoFuncaoDocumental = false;
                        this.modalVinculacao.ocorrenciaVinculacaoFase = false;
                        break;
                    } else {
                        this.inicializarVinculacoesConflitantes(activesChecklists, i);
                    }
                }
            }
            conflitoChecado = true;
        }
        return vinculacaoExistente;
    }

    /**
     * Prepara a lista de possiveis vinculações conflitantes; para validação posterior de data
     * @param activesChecklists 
     * @param i 
     */
    private inicializarVinculacoesConflitantes(activesChecklists: GridChecklist[], i: number) {
        const vinculacaoChecklistConflitante: VinculacaoChecklistConflitante = new VinculacaoChecklistConflitante();
        vinculacaoChecklistConflitante.vinculacao = this.modalVinculacao.vinculacaoConflitante;
        vinculacaoChecklistConflitante.idChecklistVinculacaoConflitante = activesChecklists[i].id;
        this.modalVinculacao.vinculacoesConflitante.add(vinculacaoChecklistConflitante);
    }

    /**
     * Configuração do componente calendario: data ativaçao vinculacao 
     */
    private configDataRevogacao() {
        this.modalVinculacao.ptBR = this.modalVinculacao.calendarProperties;
        this.modalVinculacao.dataRevogacaoVigente = Utils.inicializarPrimeraDataRevogacaoVinculacaoConflitanteVigente();
        this.modalVinculacao.errorDataRevogacao = moment(this.modalVinculacao.dataRevogacaoVigente).format('DD/MM/YYYY');
    }

    /**
     * Carrega os checklists ativos para validação de vinculação conflitante
     * @param vinculacaoDataInput 
     */
    private carregarChecklistsAtivos(vinculacaoDataInput: VinculacaoDataInput) {
        this.loadService.show();
        this.checkListService.get().subscribe(dados => {
            this.modalVinculacao.activeChecklists = dados.filter(checklist => checklist.data_hora_inativacao == undefined);
            this.carregarVinculacaoConflitante(vinculacaoDataInput);
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            this.growlMessageService.showError(VINCULACAO.VINCULACAO_VALIDACAO, error);
            throw error;
        });
    }

    /**
     * Devolve uma vinculacao selecionada para o componente vinculacoes
     * @param vinculacaoDataInput 
     */
    private inicializarVinculacaoSelecionada(vinculacaoDataInput: VinculacaoDataInput): VinculacaoDataOutput {
        const vinculacao: Vinculacao = new Vinculacao();
        const vinculacaoDataOutput: VinculacaoDataOutput = new VinculacaoDataOutput();
        vinculacao.data_revogacao = undefined;
        vinculacao.id_funcao_documental = this.modalVinculacao.selectedFuncaoDocumental ? this.modalVinculacao.selectedFuncaoDocumental.key : undefined;
        vinculacao.id_processo_dossie = this.modalVinculacao.selectedProcesso.key;
        vinculacao.id_tipo_documento = this.modalVinculacao.selectedTipoDocumento ? this.modalVinculacao.selectedTipoDocumento.key : undefined;
        vinculacao.id_processo_fase = this.modalVinculacao.selectedFase.key;
        vinculacaoDataOutput.conflitoVinculacao = this.modalVinculacao.conflitoVinculacao;
        vinculacaoDataOutput.edicaoConflitoVinculacao = vinculacaoDataInput.conflitoVinculacao;
        vinculacaoDataOutput.indexVinculacaoConflitante = vinculacaoDataInput.indexVinculacaoConflitante;
        if (this.modalVinculacao.conflitoVinculacao) {
            vinculacaoDataOutput.vinculacaoConflitante = this.modalVinculacao.vinculacaoConflitante;
            vinculacaoDataOutput.vinculacaoConflitante.data_revogacao = this.modalVinculacao.dataRevogacao;
            vinculacaoDataOutput.idChecklistAssociadoVinculacao = this.recuperarIdetificacaoChecklistAssociadoVinculacaoConflitante(this.modalVinculacao.vinculacaoConflitante.id);
        }
        vinculacaoDataOutput.vinculacao = vinculacao;
        return vinculacaoDataOutput;
    }

    /**
     * Devolve o id do checklist associado a vinculacao conflitante
     * @param idVinculacao 
     */
    private recuperarIdetificacaoChecklistAssociadoVinculacaoConflitante(idVinculacao): number {
        const activeChecklists: Array<GridChecklist> = this.modalVinculacao.activeChecklists;
        let idChecklistAssociadoVinculacao: number;
        for (let i: number = 0; i < activeChecklists.length; i++) {
            if (activeChecklists[i].vinculacoes) {
                const vinculacaoAssociada: boolean = activeChecklists[i].vinculacoes.some(vinculacao => vinculacao.id == idVinculacao);
                if (vinculacaoAssociada) {
                    idChecklistAssociadoVinculacao = activeChecklists[i].id;
                    break;
                }
            }
        }
        return idChecklistAssociadoVinculacao;
    }

    /**
     * Carrega a vinculação conflitante para edição, desabilita o dropdown fase
     * @param vinculacaoDataInput 
     */
    private carregarVinculacaoConflitante(vinculacaoDataInput: VinculacaoDataInput) {
        if (vinculacaoDataInput.vinculacao && vinculacaoDataInput.conflitoVinculacao) {
            this.modalVinculacao.selectedProcesso = <InterfaceProcesso>{
                key: vinculacaoDataInput.vinculacao.id_processo_dossie,
                name: vinculacaoDataInput.processo
            }
            this.modalVinculacao.fases = this.buscarProcessosFasePorIdProcessoGeraDossie(vinculacaoDataInput.vinculacao.id_processo_dossie);
            this.modalVinculacao.selectedFase = <InterfaceProcesso>{
                key: vinculacaoDataInput.vinculacao.id_processo_fase,
                name: vinculacaoDataInput.fase
            }
            this.modalVinculacao.emptyProcesso = false;
            this.modalVinculacao.selectedTipoDocumento = <InterfaceTipoDocumento>{
                key: vinculacaoDataInput.vinculacao.id_tipo_documento,
                name: vinculacaoDataInput.tipoDocumento
            }
            this.modalVinculacao.selectedFuncaoDocumental = <InterfaceFuncaoDocumental>{
                key: vinculacaoDataInput.vinculacao.id_funcao_documental,
                name: vinculacaoDataInput.funcaoDocumental
            }
            this.modalVinculacao.conflitoVinculacao = true;
            this.modalVinculacao.editar = true;
            this.validarVinculacaoConflitante(true);
            this.validarVinculacaoConflitante(false);
            this.modalVinculacao.dataRevogacao = vinculacaoDataInput.gridVinculacaoConflitante.vinculacaoConflitante.data_revogacao;
            this.growlMessageService.showError(VINCULACAO.VINCULACAO_VALIDACAO, VINCULACAO.VINCULACAO_CONFLITO);
        }
    }

    /**
     * inicializa dropdown com todos os processos
     */
    private getProcessos() {
        this.modalVinculacao.processos = this.buscarProcessosFilhoMacroProcesso();
    }

    /**
     * inicializa dropdown com todos as Funções Documentais
     */
    private getFuncoesDocumentais() {
        this.modalVinculacao.funcoesDocumentais = this.buscarFuncoesDocumentaisTipologia();
    }

    /**
     * inicializa dropdown com todos as Funções Documentais
     */
    private getTiposDocumento() {
        this.modalVinculacao.tiposDocumento = this.buscarTiposDocumentoTipologia();
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
     * @param id 
     */
    private buscarProcessosFasePorIdProcessoGeraDossie(id: number): Array<InterfaceProcesso> {
        const processosLocalStorage: Array<ProcessoPatriarca> = this.localStorageDataService.buscarProcessosFasePassandoIdProcessoGeraDossie(id);
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
}