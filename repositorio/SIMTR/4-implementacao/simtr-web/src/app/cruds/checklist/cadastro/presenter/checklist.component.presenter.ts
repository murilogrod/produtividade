import { Injectable, ElementRef } from "@angular/core";
import { LoaderService } from "src/app/services";
import { Router } from "@angular/router";
import { CheckListService } from "../../service/checklist.service";
import { Utils } from "src/app/cruds/util/utils";
import { CHECKLIST } from "../../constant.checklist";
import { ChecklistModel } from "../model/checklist-component.model";
import { ChecklistComponent } from "../view/checklist.component";
import { Checklist } from "src/app/cruds/model/checklist.model";
import { IncludeChecklist } from "../model/include-checklist";
import { UpdateChecklist } from "../model/update-checklist";
import { GridChecklist } from "../../pesquisa/model/grid-checklist.model";
import { ApontamentoService } from "../../service/apontamento.service";
import { forkJoin, Observable } from 'rxjs';
import { Apontamento } from "src/app/cruds/model/apontamento.model";
import { IncludeApontamento } from "../model/include-apontamento";
import { UpdateApontamento } from "../model/update-apontamento";
import { Vinculacao } from "src/app/cruds/model/vinculacao.model";
import { VinculacaoService } from "../../service/vinculacao.service";
import { IncludeVinculacao } from "../model/include-vinculacao";
import { VinculacaoChecklist } from "src/app/cruds/model/vinculacao-checklist";
import * as moment from 'moment';

@Injectable()
export class ChecklistPresenter {

    checklistModel: ChecklistModel;
    checklist: Checklist;

    constructor(private checklistService: CheckListService,
        private apontamentoService: ApontamentoService,
        private vinculacaoService: VinculacaoService,
        private router: Router,
        private loadService: LoaderService) {
    }

    /**
     * Inicializa a configuração inicial do checklist: verifica edicao
     * @param params 
     * @param checklistComponent 
     */
    initConfigChecklist(params: any, checklistComponent: ChecklistComponent) {
        this.verificarEdicaoChecklist(params, checklistComponent);
        this.desabilitarInteracaoApontamentosChecklist(params);
        this.verificarParametrizacaoCloneChecklist();
    }

    /**
     * Navega para a url passada como parâmetro
     * @param url 
     */
    navigateUrl(parameters: string[]) {
        this.router.navigate(parameters);
    }

    /**
    * Carrega os checklists para validar a ocorrencia de mesmo nome
    */
    verificarOcorrenciaMesmoNomeChecklists(checklistComponent: ChecklistComponent) {
        this.inicializarUnidadeMinima();
        if (this.checklistService.existChecklists()) {
            this.checklistModel.checklists = this.checklistService.getChecklists();
            this.validarOcorrenciaNomeChecklist(checklistComponent);
        } else {
            this.loadService.show();
            this.checklistService.get().subscribe(checklists => {
                this.checklistModel.checklists = checklists;
                this.validarOcorrenciaNomeChecklist(checklistComponent);
                this.loadService.hide();
            }, error => {
                this.loadService.hide();
                checklistComponent.addMessageError(error);
                throw error;
            });
        }
    }

    /**
     * Verifica a unidade: caso 0 - enviará 1
     * Mínimo 0001
     */
    private inicializarUnidadeMinima() {
        if (this.checklistModel.unidadeZero) {
            this.checklist.unidade_responsavel = 1;
        }
    }

    /**
     * Inicializa os apontamentos no componente checklist
     * @param apontamentos 
     */
    inicializarApontamentos(apontamentos: Array<Apontamento>) {
        this.checklistModel.apontamentos = apontamentos;
    }

    /**
     * Inicializa as vinculacoes no componente checklist
     * @param vinculacoes 
     */
    inicializarVinculacoes(vinculacoes: Array<VinculacaoChecklist>) {
        this.checklistModel.vinculacoes = vinculacoes;
    }

    /**
     * Inicializa os apontamentosRemovidos no componente checklist
     * @param apontamentosRemovidos 
     */
    inicializarApontamentosRemovidos(apontamentosRemovidos: Array<Apontamento>) {
        this.checklistModel.apontamentosRemovidos = apontamentosRemovidos;
    }

    /**
     * Inicializa os vinculacoesRemovidas no componente checklist
     * @param vinculacoesRemovidss 
     */
    inicializarVinculacoesRemovidas(vinculacoesRemovidas: Array<Vinculacao>) {
        this.checklistModel.vinculacoesRemovidas = vinculacoesRemovidas;
    }

    /**
     * Formata uma undiade válida. 
     * devolvendo uma unidade válida com zeros a esquerda
     * @param unidade 
     */
    formatarUnidadeValida(unidade: ElementRef) {
        if (this.checklist.unidade_responsavel.toString().length == 1) {
            const value: string = `000${this.checklist.unidade_responsavel}`;
            unidade.nativeElement.value = value;
        }
        if (this.checklist.unidade_responsavel.toString().length == 2) {
            const value: string = `00${this.checklist.unidade_responsavel}`;
            unidade.nativeElement.value = value;
        }
        if (this.checklist.unidade_responsavel.toString().length == 3) {
            const value: string = `0${this.checklist.unidade_responsavel}`;
            unidade.nativeElement.value = value;
        }
        if (this.checklist.unidade_responsavel == 0) {
            const value: string = `0001`;
            unidade.nativeElement.value = value;
            this.checklistModel.unidadeZero = true;
        }
    }

    /**
     * Redireciona para a pesquisa checklist
     */
    private redirecionarParaPesquisaChecklist() {
        this.loadService.hide();
        this.limparRequisicoes();
        this.navigateUrl([CHECKLIST.CHECKLIST]);
    }

    /**
     * Limpa todas os arrays contendo as requisições
     */
    private limparRequisicoes() {
        this.checklistModel.requesicoesIncludeApontamentos = new Array<Observable<IncludeApontamento>>(0);
        this.checklistModel.requesicoesUpdateApontamentos = new Array<Observable<UpdateApontamento>>(0);
        this.checklistModel.requesicoesApontamentosRemovidos = new Array<Observable<any>>(0);
        this.checklistModel.requesicoesIncludeVinculacoes = new Array<Observable<IncludeVinculacao>>(0);
        this.checklistModel.requesicoesVinculacoesRemovidas = new Array<Observable<any>>(0);
    }

    /**
     * Prepara as requisicoes para remoção: Apontamentos
     * @param checklistComponent 
     */
    private removerApontamentos(checklistComponent: ChecklistComponent) {
        this.checklistModel.apontamentosRemovidos.forEach(apontamento => {
            this.checklistModel.requesicoesApontamentosRemovidos.push(this.apontamentoService.delete(this.checklistModel.ultimoIdChecklist, apontamento.id));
        });
        if (this.checklistModel.requesicoesApontamentosRemovidos.length > 0) {
            this.forkJoinRemoveApontamentos(this.checklistModel.requesicoesApontamentosRemovidos, checklistComponent);
        }
    }

    /**
     * Prepara as requisicoes para remoção: Vinculacoes
     * @param checklistComponent 
     */
    private removerVinculacoes(checklistComponent: ChecklistComponent) {
        this.checklistModel.vinculacoesRemovidas.forEach(vinculacao => {
            this.checklistModel.requesicoesVinculacoesRemovidas.push(this.vinculacaoService.delete(vinculacao.id));
        });
        if (this.checklistModel.requesicoesVinculacoesRemovidas.length > 0) {
            this.forkJoinRemoveVinculacoes(this.checklistModel.requesicoesVinculacoesRemovidas, checklistComponent);
        }
    }

    /**
     * Salva o checklist após sua validação: POST
     * @param checklistComponent 
     */
    private saveChecklist(checklistComponent: ChecklistComponent) {
        this.loadService.show();
        this.checklistService.save(this.inicializarIncludeChecklist()).subscribe(ultimoIdChecklist => {
            this.onSucessSaveChecklist(ultimoIdChecklist, checklistComponent);
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * evento sucesso ao salvar checklist
     * @param ultimoIdChecklist 
     * @param checklistComponent 
     */
    private onSucessSaveChecklist(ultimoIdChecklist: any, checklistComponent: ChecklistComponent) {
        this.checklistService.setNovoChecklist(true);
        const id: number = Number(ultimoIdChecklist);
        this.checklistService.setId(id);
        this.checklistModel.ultimoIdChecklist = id;
        this.salvarApontamentos(checklistComponent);
        this.salvarVinculacoes(checklistComponent);
        if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesIncludeApontamentos,
        this.checklistModel.requesicoesUpdateApontamentos,
        this.checklistModel.requesicoesIncludeVinculacoes])) {
            this.redirecionarParaPesquisaChecklist();
        }
    }

    /**
     * Atualiza o checklist após sua validação: POST
     * @param checklistComponent 
     */
    private updateChecklist(checklistComponent: ChecklistComponent) {
        this.loadService.show();
        this.checklistService.update(this.checklist.id, this.inicializarUpdateChecklist()).subscribe(() => {
            this.onSucessUpadateChecklist(checklistComponent);
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Evento sucesso ao atualizar o checklist
     * @param checklistComponent 
     */
    private onSucessUpadateChecklist(checklistComponent: ChecklistComponent) {
        this.checklistService.setEdicaoChecklist(true);
        this.checklistService.setId(Number(this.checklist.id));
        this.checklistModel.ultimoIdChecklist = this.checklist.id;
        this.salvarApontamentos(checklistComponent);
        this.removerApontamentos(checklistComponent);
        this.salvarVinculacoes(checklistComponent);
        this.removerVinculacoes(checklistComponent);
        this.prepararRedirecionamentoPesquisaChecklist();
    }

    /**
     * Verifica o preenchimento de todos os componentes: checklists, apontamentos e vinculacoes
     * para redirecionar para a pesquisa checklist
     */
    private prepararRedirecionamentoPesquisaChecklist() {
        if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesIncludeApontamentos,
        this.checklistModel.requesicoesUpdateApontamentos,
        this.checklistModel.requesicoesApontamentosRemovidos,
        this.checklistModel.requesicoesIncludeVinculacoes,
        this.checklistModel.requesicoesVinculacoesRemovidas])) {
            this.redirecionarParaPesquisaChecklist();
        }
    }

    /**
     * Verifica qual tipo de atualização de checklist: POST ou PATH
     * @param checklistComponent 
     */
    private verificarTipoAtualizacaoChecklist(checklistComponent: ChecklistComponent) {
        if (this.checklistModel.editar) {
            this.updateChecklist(checklistComponent);
        } else {
            this.saveChecklist(checklistComponent);
        }
    }

    /**
     * Valida a ocorrencia de mesmo nome de checklist
     * @param checklistComponent 
     */
    private validarOcorrenciaNomeChecklist(checklistComponent: ChecklistComponent) {
        let ocorrenciaChecklist: boolean = false;
        if (this.checklistModel.editar) {
            const filterChecklists: Array<GridChecklist> = this.checklistModel.checklists.filter(checklist => checklist.id !== this.checklist.id);
            ocorrenciaChecklist = filterChecklists.some(checklist => checklist.nome.toUpperCase().trim() == this.checklist.nome.toUpperCase().trim());
        } else {
            ocorrenciaChecklist = this.checklistModel.checklists.some(checklist => checklist.nome.toUpperCase().trim() == this.checklist.nome.toUpperCase().trim());
        }
        if (ocorrenciaChecklist) {
            checklistComponent.addMessageError(CHECKLIST.CHECKLIST_EXISTENTE);
        } else {
            this.verificarTipoAtualizacaoChecklist(checklistComponent);
        }
    }

    /**Inicializa o objeto para salvar o checklist */
    private inicializarIncludeChecklist(): IncludeChecklist {
        const includeChecklist: IncludeChecklist = new IncludeChecklist();
        includeChecklist.nome = this.checklist.nome;
        includeChecklist.unidade_responsavel = this.checklist.unidade_responsavel;
        includeChecklist.verificacao_previa = this.checklist.verificacao_previa;
        includeChecklist.orientacao_operador = this.checklist.orientacao_operador;
        return includeChecklist;
    }

    /**Inicializa o objeto para atualizar o checklist */
    private inicializarUpdateChecklist(): UpdateChecklist {
        const updateChecklist: UpdateChecklist = new UpdateChecklist();
        updateChecklist.nome = this.checklist.nome;
        updateChecklist.unidade_responsavel = this.checklist.unidade_responsavel;
        updateChecklist.verificacao_previa = this.checklist.verificacao_previa;
        updateChecklist.orientacao_operador = this.checklist.orientacao_operador;
        return updateChecklist;
    }

    /**
     * Verifica a marcação de clone atraves da funcionalidade de clone: pesquisa checklist
     */
    private verificarParametrizacaoCloneChecklist() {
        if (this.checklistService.existCloneCheckList()) {
            this.checklist = this.checklistService.getCloneChecklist();
            this.renderizarGridApontamentosVinculacoes();
            this.checklistModel.cloneChecklist = true;
            this.checklistService.setCloneChecklist(undefined);
        }
    }

    /**
     * Verifica se é uma edição de checklist
     * @param params 
     * @param checklistComponent 
     */
    private verificarEdicaoChecklist(params: any, checklistComponent: ChecklistComponent) {
        let paramIntUrl: boolean = this.checkParamIdIntUrl(params);
        if (paramIntUrl) {
            this.validarLabelEdicao(paramIntUrl);
            this.checklistModel.showHeader = true;
            const id: number = Number(params.id);
            this.carregarChecklist(id, checklistComponent);
        } else {
            this.checklistModel.mostrarApontamentos = true;
            this.checklistModel.mostrarVinculacoes = true;
            this.checklistModel.showHeader = false;
        }
    }

    /**
     * Recupera o checklist passando id
     * @param id 
     * @param checklistComponent 
     */
    private carregarChecklist(id: number, checklistComponent: ChecklistComponent) {
        this.loadService.show();
        this.checklistService.getById(id).subscribe(checklist => {
            this.checklist = checklist;
            this.checklistModel.nomeChecklistOriginal = this.checklist.nome;
            this.custumizeHeaderGrid();
            this.renderizarGridApontamentosVinculacoes();
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(id, error, checklistComponent);
        });

    }

    /**
     * Mostra as grids: Apontamentos e vinculações conforme a resposta do serviço checklist
     * mesmo não retornando nada; as grids devem aparecer
     */
    private renderizarGridApontamentosVinculacoes() {
        this.checklistModel.mostrarApontamentos = true;
        this.checklistModel.mostrarVinculacoes = true;
    }

    /**
     * Salva os apontamentos: POST ou PATH
     * @param checklistComponent 
     */
    private salvarApontamentos(checklistComponent: ChecklistComponent) {
        this.checklistModel.apontamentos.forEach(apontamento => {
            if (!apontamento.id) {
                this.checklistModel.requesicoesIncludeApontamentos.push(this.apontamentoService.save(this.checklistModel.ultimoIdChecklist, this.inicializarIncludeApontamento(apontamento)));
            }
            if (apontamento.id) {
                this.checklistModel.requesicoesUpdateApontamentos.push(this.apontamentoService.update(this.checklistModel.ultimoIdChecklist, apontamento.id, this.inicializarUpdateApontamento(apontamento)));
            }
        });
        if (this.checklistModel.requesicoesIncludeApontamentos.length > 0) {
            this.forkJoinIncludeApontamentos(this.checklistModel.requesicoesIncludeApontamentos, checklistComponent);
        }
        if (this.checklistModel.requesicoesUpdateApontamentos.length > 0) {
            this.forkJoinUpdateApontamentos(this.checklistModel.requesicoesUpdateApontamentos, checklistComponent);
        }
    }

    /**
     * Realiza as requisicoes para salvar os apontamentos: POST
     * @param requesicoesApontamentos 
     * @param checklistComponent 
     */
    private forkJoinIncludeApontamentos(requesicoesIncludeApontamentos: Observable<IncludeApontamento>[], checklistComponent: ChecklistComponent) {
        forkJoin(requesicoesIncludeApontamentos).subscribe(() => {
            if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesUpdateApontamentos,
            this.checklistModel.requesicoesApontamentosRemovidos,
            this.checklistModel.requesicoesIncludeVinculacoes,
            this.checklistModel.requesicoesVinculacoesRemovidas])) {
                this.atualizarChecklist(checklistComponent);
            }
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Salva as vinculacoes: POST
     * @param checklistComponent 
     */
    private salvarVinculacoes(checklistComponent: ChecklistComponent) {
        this.checklistModel.vinculacoes.forEach(vinculacao => {
            this.checklistModel.requesicoesIncludeVinculacoes.push(this.vinculacaoService.save(this.inicializarIncludeVinculacao(vinculacao)));
        });
        if (this.checklistModel.requesicoesIncludeVinculacoes.length > 0) {
            this.forkJoinIncludeVinculacoes(this.checklistModel.requesicoesIncludeVinculacoes, checklistComponent);
        }
    }

    /**
     * Realiza as requisicoes para salvar as vinculações: POST
     * @param requesicoesApontamentos 
     * @param checklistComponent 
     */
    private forkJoinIncludeVinculacoes(requesicoesIncludeVinculacoes: Observable<IncludeVinculacao>[], checklistComponent: ChecklistComponent) {
        forkJoin(requesicoesIncludeVinculacoes).subscribe(() => {
            if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesApontamentosRemovidos,
            this.checklistModel.requesicoesVinculacoesRemovidas])) {
                this.atualizarChecklist(checklistComponent);
            }
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }


    /**
     * Apos todas atualizações: checklist, apontamentos e vinculacoes
     * é necessário o PATH de checklist para as devidas atualizações de carregamento: 
     * Apontamentos e Vinculacoes
     * @param checklistComponent 
     */
    private atualizarChecklist(checklistComponent: ChecklistComponent) {
        this.checklistService.update(this.checklistService.getId(), this.inicializarUpdateChecklist()).subscribe(() => {
            this.redirecionarParaPesquisaChecklist();
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Realiza as requisicoes para remover as vinculacoes: DELETE
     * @param requesicoesVinculacoesRemovidas 
     * @param checklistComponent 
     */
    private forkJoinRemoveVinculacoes(requesicoesVinculacoesRemovidas: Observable<any>[], checklistComponent: ChecklistComponent) {
        forkJoin(requesicoesVinculacoesRemovidas).subscribe(() => {
            this.atualizarChecklist(checklistComponent);
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Realiza as requisicoes para remover os apontamentos: DELETE
     * @param requesicoesApontamentosRemovidos 
     * @param checklistComponent 
     */
    private forkJoinRemoveApontamentos(requesicoesApontamentosRemovidos: Observable<any>[], checklistComponent: ChecklistComponent) {
        forkJoin(requesicoesApontamentosRemovidos).subscribe(() => {
            if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesVinculacoesRemovidas])) {
                this.atualizarChecklist(checklistComponent);
            }
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Realiza as requisicoes para atualizar os apontamentos: PATH
     * @param requesicoesApontamentos 
     * @param checklistComponent 
     */
    private forkJoinUpdateApontamentos(requesicoesUpdateApontamentos: Observable<UpdateApontamento>[], checklistComponent: ChecklistComponent) {
        forkJoin(requesicoesUpdateApontamentos).subscribe(() => {
            if (this.verificarPreenchimentoFormulario([this.checklistModel.requesicoesApontamentosRemovidos,
            this.checklistModel.requesicoesIncludeVinculacoes,
            this.checklistModel.requesicoesVinculacoesRemovidas])) {
                this.atualizarChecklist(checklistComponent);
            }
        }, error => {
            this.loadService.hide();
            this.onErrorChecklist(undefined, error, checklistComponent);
        });
    }

    /**
     * Inicializa o objeto para salvar a vinculação
     * @param apontamento 
     */
    private inicializarIncludeVinculacao(vinculacaoChecklist: VinculacaoChecklist): IncludeVinculacao {
        const includeVinculacao: IncludeVinculacao = new IncludeVinculacao();
        includeVinculacao.identificador_processo_dossie = vinculacaoChecklist.vinculacao.id_processo_dossie;
        includeVinculacao.identificador_processo_fase = vinculacaoChecklist.vinculacao.id_processo_fase;
        includeVinculacao.identificador_tipo_documento = vinculacaoChecklist.vinculacao.id_tipo_documento;
        includeVinculacao.identificador_funcao_documental = vinculacaoChecklist.vinculacao.id_funcao_documental;
        includeVinculacao.identificador_checklist = this.checklistModel.ultimoIdChecklist;
        includeVinculacao.data_ativacao = vinculacaoChecklist.vinculacaoConflitante ? moment(vinculacaoChecklist.vinculacaoConflitante.data_revogacao).format('DD/MM/YYYY') : undefined;
        return includeVinculacao;
    }

    /**
     * Inicializa o objeto para salvar o apontamento
     * @param apontamento 
     */
    private inicializarIncludeApontamento(apontamento: Apontamento): IncludeApontamento {
        const includeApontamento: IncludeApontamento = new IncludeApontamento();
        includeApontamento.descricao_apontamento = apontamento.descricao;
        includeApontamento.indicativo_informacao = apontamento.pendencia_informacao;
        includeApontamento.indicativo_rejeicao = apontamento.rejeicao_documento;
        includeApontamento.indicativo_seguranca = apontamento.pendencia_seguranca;
        includeApontamento.nome_apontamento = apontamento.nome;
        includeApontamento.ordem = apontamento.sequencia_apresentacao;
        includeApontamento.orientacao_retorno = apontamento.orientacao_operador;
        includeApontamento.tecla_atalho = apontamento.tecla_atalho;
        return includeApontamento;
    }

    /**
     * Inicializa o objeto para atualizar o apontamento
     * @param apontamento 
     */
    private inicializarUpdateApontamento(apontamento: Apontamento): UpdateApontamento {
        const updateApontamento: UpdateApontamento = new UpdateApontamento();
        updateApontamento.descricao_apontamento = apontamento.descricao;
        updateApontamento.indicativo_informacao = apontamento.pendencia_informacao;
        updateApontamento.indicativo_rejeicao = apontamento.rejeicao_documento;
        updateApontamento.indicativo_seguranca = apontamento.pendencia_seguranca;
        updateApontamento.nome_apontamento = apontamento.nome;
        updateApontamento.ordem = apontamento.sequencia_apresentacao;
        updateApontamento.orientacao_retorno = apontamento.orientacao_operador;
        updateApontamento.tecla_atalho = apontamento.tecla_atalho;
        return updateApontamento;
    }

    /**
     * Adiciona os valores: ID e data ultima alteracao no header da grid checklist
     */
    private custumizeHeaderGrid() {
        Utils.custumizeHeaderGrid(this.checklistModel.configGridHeader, this.checklist.id, this.checklist.data_hora_ultima_alteracao);
    }

    /**
     * Altera a label do cadastro checklist: Edição de checklist e Novo Checklist
     * @param paramIntUrl 
     */
    private validarLabelEdicao(paramIntUrl: boolean) {
        this.checklistModel.editar = paramIntUrl;
    }

    /**
     * Verifica existencia parametro ID inteiro na URL
     * @param params 
     */
    private checkParamIdIntUrl(params: any): boolean {
        return Utils.checkParamIntUrl(params.id);
    }

    /**
     * Verifica existencia parametro OP = operacoes checklists inteiro na URL
     * @param params 
     */
    private checkParamOpIntUrl(params: any): boolean {
        return Utils.checkParamIntUrl(params.op);
    }

    /**
     * Lança o erro para o usuário
     * atualiza a variavel errorApi para nao haver o redirecionamento de pagina
     * @param id 
     * @param error 
     * @param checklistComponent 
     */
    private onErrorChecklist(id: number, error: any, checklistComponent: ChecklistComponent) {
        if (error.status == 403) {
            checklistComponent.addMessageError(error.error.mensagem);
        } else if (error.status == 404) {
            this.navigateUrl([CHECKLIST.CHECKLIST, id.toString(), CHECKLIST.CHECKLIST_NOT_FOUND]);
        } else if (error.status == 400) {
            if (error.error["length"]) {
                if (error.error[0].apontamentos) {
                    checklistComponent.addMessageError(error.error[0].apontamentos);
                }
            } else {
                checklistComponent.addMessageError(error.error.detalhe);
                checklistComponent.addMessageError(error.error.mensagem);
            }
        } else if (error.status == 409) {
            checklistComponent.addMessageError(error.error.detalhe);
            checklistComponent.addMessageError(error.error.mensagem);
        } else {
            checklistComponent.addMessageError(JSON.stringify(error));
        }
        throw error;
    }

    /**
     * Certifica se o checklist possui operaçoes
     * nesse caso os apontamentos do checklist é somente leitura
     * @param params 
     */
    private desabilitarInteracaoApontamentosChecklist(params: any) {
        this.checklistModel.disabledChecklist = this.checkParamOpIntUrl(params);
    }

    /**
     * Varre a ocorrencia de todos os filhos do checklist: Apontamentos e Vinculacoes
     * Verificando a existencia de atualização para posterior redirecionamento
     * @param observables 
     */
    private verificarPreenchimentoFormulario(observables: Array<Array<Observable<any>>>): boolean {
        return Utils.verificarPreenchimentoObjetosPersistentes(observables);
    }
}