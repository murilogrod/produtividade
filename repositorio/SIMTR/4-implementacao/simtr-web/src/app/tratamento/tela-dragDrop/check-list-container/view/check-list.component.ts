import { Component, ViewEncapsulation, Input, OnChanges, Output, EventEmitter, AfterViewChecked, DoCheck, ChangeDetectorRef, HostListener, OnInit } from "@angular/core";
import { DialogService } from "angularx-bootstrap-modal";
import { Checklist } from "src/app/model/checklist";
import { ApontamentoChecklist } from "src/app/model/apontamento-cheklist";
import { TIPO_ARVORE_DOCUMENTO, LOCAL_STORAGE_CONSTANTS, UNDESCOR, TIPO_MSG_TRATAMENTO } from "src/app/constants/constants";
import { AlertMessageService, LoaderService, ApplicationService } from "src/app/services";
import { MudancaSalvaService } from "src/app/services/mudanca-salva.service";
import { ModalJustificativaApontamentoComponent } from "../modal-justificativa-apontamento/modal-justificativa-apontamento.component";
import { ConjuntoComboVinculoTipoDocSelecionado } from "../model/conjunto-combo-vinculo-tipo-doc-selecionado.model";
import { CheckListComponentPresenter } from "../presenter/check-list.component.presenter";
import { CheckListVisualizacao } from "../model/check-list-visualizacao.model";
import { Msg_checklist } from "../model/msg-checklist";
import { Utils } from "src/app/utils/Utils";
import { DescricaoCheckList } from "../model/descricao-checklist.model";
import { TelaDeTratamentoService } from "src/app/tratamento/tela-de-tratamento.service";

declare var $: any;

@Component({
	selector: 'check-list',
	templateUrl: './check-list.component.html',
	styleUrls: ['./check-list.component.css'],
	encapsulation: ViewEncapsulation.None
})

export class CheckListComponent extends AlertMessageService implements OnChanges, AfterViewChecked, DoCheck {

	@Input() dossieProduto;
	@Input() processoGerador;
	@Input() processoFaseCheckList: number;
	@Input() listDocumentosImagens: any[];
	@Input() idCheckListAtivado: any;
	@Input() alturaMaxheigtChek: number;
	@Input() existeCheckListPrevio: boolean;
	@Input() listaChekList: Checklist[];
	@Input() showPortal: boolean;
	@Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
	@Output() ocultarCheckListChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
	@Output() msg_checkListChanged: EventEmitter<Msg_checklist> = new EventEmitter<Msg_checklist>();
	checkListsAgrupados: any[] = new Array<any>();
	listApontamentosCheckados: boolean[];
	listaJustificativaApontamentos: string[];
	apontamentos: any[];
	existemApontamentosNaoAvalidados: boolean;
	acaoButtonSalvar: boolean = false;
	processoFaseAtual: any;
	apontamentoPreenchido: boolean = false;
	habilitarDesabilitarTratamento: boolean;
	checkEmFoco: Checklist = new Checklist();
	dataChecK: Date;
	checkListPresenter: CheckListComponentPresenter;
	idApontamentoEmFoco: number;

	constructor(
		checkListPresenter: CheckListComponentPresenter,
		private dialogService: DialogService,
		private loadService: LoaderService,
		private appService: ApplicationService,
		private mudancaSalvaService: MudancaSalvaService,
		private cdRef: ChangeDetectorRef,
		private telaDeTratamentoService: TelaDeTratamentoService) {
		super();
		this.checkListPresenter = checkListPresenter;
		this.checkListPresenter.checkListVisualizacao = new CheckListVisualizacao();
	}

	ngOnChanges() {
		if (!this.showPortal) {
			this.clearAllMessages();
			this.alturaMaxheigtChek = $("#checklist").height() - 140;
			this.checkEmFoco = new Checklist();
			if (this.idCheckListAtivado != this.checkListPresenter.checkListVisualizacao.idCheckListAtivado) {
				this.idApontamentoEmFoco = undefined; // Reseta o controle de qual apontamento está em foco.
			}
			this.existemApontamentosNaoAvalidados = true;
			this.listApontamentosCheckados = [];
			$(".slider").removeClass("slider-apontamento-conforme");
			$(".balaoJustificativa").removeClass("item-fa-comments-active").addClass("item-fa-comments");
			this.populaInputsComponentCheckListNoModelVisualizacao();
			let msgChekList = new Msg_checklist();
			if (undefined == this.processoFaseAtual) {
				let geradorDossie: any = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.processoGerador));
				this.processoFaseAtual = geradorDossie.processos_filho.find(fase => fase.id == this.processoFaseCheckList);
				this.populaInputsComponentCheckListNoModelVisualizacao();
				if (!this.checkListPresenter.checaExisteCheckListFase(this.dossieProduto)) {
					msgChekList = this.checkListPresenter.carregaApontamentosDocumentoAtualSelecionado(this.appService, msgChekList, this.dossieProduto);
					this.funcaoDuplicadaMsgError(msgChekList);
					this.loadService.hide();
				}

			} else if (!this.checkListPresenter.checaExisteCheckListFase(this.dossieProduto)) {
				msgChekList = this.checkListPresenter.carregaApontamentosDocumentoAtualSelecionado(this.appService, msgChekList, this.dossieProduto);
				this.funcaoDuplicadaMsgError(msgChekList);
			}
			this.acaoButtonSalvar = false;
		}
	}



	private funcaoDuplicadaMsgError(msgChekList: Msg_checklist) {
		if (msgChekList) {
			if (msgChekList.tipo == TIPO_MSG_TRATAMENTO.TIPO_DUPLICIDADE) {
				let messagesSuccess = new Array<string>();
				messagesSuccess.push(msgChekList.descricao);
				this.alertMessagesErrorChanged.emit(messagesSuccess);
				this.ocultarCheckListChanged.emit(true);
			}
		}
		this.msg_checkListChanged.emit(msgChekList);
	}

	ngAfterViewChecked() {
		this.alturaMaxheigtChek = $("#checklist").height() - 140;
		$(".ui-datatable-scrollable-body").css('max-height', this.alturaMaxheigtChek + "px");
		if (this.checkListPresenter.checkListVisualizacao.apontamentoPreenchido) {
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.forEach((app, idx) => {
				this.invalidaValidaItemDocumentoAtualizarAoCarregar(idx);
				this.checkListPresenter.checkListVisualizacao.apontamentoPreenchido = false;
			});
		}
		this.cdRef.detectChanges();
	}

	ngDoCheck() {
		this.verificarSituacaoHabilitarDesabilitarTratamentoConformeTipoSelecionado();
	}

	@HostListener('window:keydown', ['$event'])
	onKeyDown(event: KeyboardEvent) {
		if ((event.ctrlKey || event.shiftKey) && (
			event.key == 'q' || event.key == 'm' || event.key == 'Q' || event.key == 'M')) {
			event.preventDefault(); // Previne o comportamento padrão do evento de atalho no navegador.

			// Ctrl pressionado, Shift não. KeyCode de outra tecla que não seja Ctrl(17) ou Shift(16) indica que há combinação Ctrl+algo.
			if (event.ctrlKey && !event.shiftKey && event.keyCode != 17 && event.keyCode != 16) {
				if (event.key == 'q' || event.key == 'Q') {
					this.conformizarTodosNaoAnalisados();
				}
				if (event.key == 'm' || event.key == 'M') {
					this.proximoApontamento();
				}
			}
		}

		if (event.key == 'ArrowUp' || event.key == 'ArrowDown') {
			event.preventDefault();
			event.key == 'ArrowUp' ? this.apontamentoAnterior() : this.proximoApontamento();
		}
	}

	/**
	 * Move o foco para o próximo apontamento deste checklist.
	 * Se estiver no início do tratamento, move o foco para o primeiro apontamento.
	 */
	proximoApontamento() {
		if (this.idApontamentoEmFoco == undefined) {
			this.idApontamentoEmFoco = 0
		} else if (this.idApontamentoEmFoco < this.checkListPresenter.checkListVisualizacao.apontamentos.length - 1) {
			this.idApontamentoEmFoco++;
		}
		$(`#check${this.idApontamentoEmFoco}`).prev().focus(); // Coloca o foco no input que está logo antes do slider (prev).

		$('.apontamento-selecionado').removeClass('apontamento-selecionado'); // Remove classe CSS para aplicar no próximo elemento.
		$(`#check${this.idApontamentoEmFoco}`).parent().parent().parent().parent().addClass('apontamento-selecionado');
	}

	/**
	 * Move o foco para o apontamento anterior deste checklist.
	 * Se estiver no início do tratamento, move o foco para o últijmo apontamento.
	 */
	apontamentoAnterior() {
		if (this.idApontamentoEmFoco == undefined) {
			this.idApontamentoEmFoco = this.checkListPresenter.checkListVisualizacao.apontamentos.length - 1;
		} else if (this.idApontamentoEmFoco > 0) {
			this.idApontamentoEmFoco--;
		}
		$(`#check${this.idApontamentoEmFoco}`).prev().focus(); // Coloca o foco no input que está logo antes do slider (prev).

		$('.apontamento-selecionado').removeClass('apontamento-selecionado'); // Remove classe CSS para aplicar no próximo elemento.
		$(`#check${this.idApontamentoEmFoco}`).parent().parent().parent().parent().addClass('apontamento-selecionado');
	}

	/**
	 * Armazena inputs de entrada vindo de outros componentes no model checklistVisualizacao para
	 * as informações poderem ser trafegadas entre a view e o presenter
	 */
	private populaInputsComponentCheckListNoModelVisualizacao() {
		this.checkListPresenter.checkListVisualizacao = new CheckListVisualizacao();
		this.checkListPresenter.checkListVisualizacao.processoFaseAtual = this.processoFaseAtual;
		this.checkListPresenter.checkListVisualizacao.listaChekList = this.listaChekList;
		this.checkListPresenter.checkListVisualizacao.checkEmFoco = this.checkEmFoco;
		this.checkListPresenter.checkListVisualizacao.existeCheckListPrevio = this.existeCheckListPrevio;
		this.checkListPresenter.checkListVisualizacao.idCheckListAtivado = this.idCheckListAtivado;
		this.checkListPresenter.checkListVisualizacao.listDocumentosImagens = this.listDocumentosImagens;
		this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados = this.listApontamentosCheckados;
		this.checkListPresenter.checkListVisualizacao.habilitarDesabilitarTratamento = this.habilitarDesabilitarTratamento;
	}

	/**
	 * Percorre checklist de documentos para chamar o metodo de desabilitar a verificação de um documento
	 */
	private verificarSituacaoHabilitarDesabilitarTratamentoConformeTipoSelecionado() {
		if (this.checkListPresenter.checkListVisualizacao.listaChekList) {
			for (let check of this.checkListPresenter.checkListVisualizacao.listaChekList) {
				this.quandoForDiferenteChecklist(check);
			}
		}
	}

	/**
	 * Desabilita os apontamentos e limpa as respostas feitas anteriormente pelo usuário, caso ele clicke não opção
	 * de não tratar um documento opcional
	 * @param check 
	 */
	private quandoForDiferenteChecklist(check: Checklist) {
		if (this.checkListPresenter.checkListVisualizacao.checkEmFoco.tipo != TIPO_ARVORE_DOCUMENTO.CHECKLIST &&
			check.idDocumento == this.checkListPresenter.checkListVisualizacao.checkEmFoco.idDocumento &&
			this.checkListPresenter.checkListVisualizacao.habilitarDesabilitarTratamento != !check.habilitaVerificacao) {
			this.checkListPresenter.checkListVisualizacao.habilitarDesabilitarTratamento = !check.habilitaVerificacao;
			this.limparCssApontamento();
		}
	}

	/**
	 * Reseta o css de resposta de cada apontamento de um checklist de documento opcional
	 */
	private limparCssApontamento() {
		if (this.checkListPresenter.checkListVisualizacao.habilitarDesabilitarTratamento) {
			let listApontamentosCheckadosAux = this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados;
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.map((apontamentoAtual, indexItemDataTable) => {
				listApontamentosCheckadosAux[indexItemDataTable] = undefined;
				this.sinalizaCheckBoxItemConforme(indexItemDataTable);
				this.desabilitaJustificativa(indexItemDataTable);
			});
			this.existemApontamentosNaoAvalidados = true;
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados = Object.assign([], this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados);
		}
	}

	/**
	 * Metodo que modifica o status do apontamento na tela, para conforme ou rejeitado
	 * @param indexItemDataTable 
	 */
	invalidaValidaItemDocumento(indexItemDataTable: number) {
		this.mudancaSalvaService.setIsMudancaSalva(false);
		if (undefined == this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable] ||
			!this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable] = undefined == this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable] ? false : true;
			this.habilitaJustificativa(indexItemDataTable);
			if (this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
				this.sinalizaCheckBoxItemConforme(indexItemDataTable);
			} else {
				this.sinalizaCheckBoxItemInconforme(indexItemDataTable);
			}
		} else if (this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable] = false;
			this.habilitaJustificativa(indexItemDataTable);
			this.sinalizaCheckBoxItemInconforme(indexItemDataTable);
		}
		this.confirm();
	}

	carregarApontamentoCheckListAnterior() {
		let listaCheckApontamentos = []
		this.encontrarCheckListisVerificadoPorId(this.dossieProduto, listaCheckApontamentos, this.encontrarIdCheckListAnterior());
		this.checkListPresenter.checkListVisualizacao.apontamentos
		listaCheckApontamentos.sort(Utils.ordeNarTadaVerificacaoDecrescente);
		listaCheckApontamentos[0].parecer_apontamentos.forEach((apontamentoAnteriror, index) => {
			this.checkListPresenter.checkListVisualizacao.apontamentos.forEach(apontamentoAtual => {
				if(apontamentoAtual.id == apontamentoAnteriror.identificador_apontamento) {
					let indexpaontamento = this.encontrarPosicaoApontamento(apontamentoAtual);
					this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexpaontamento] = apontamentoAnteriror.aprovado;
					if(apontamentoAnteriror.aprovado) {						
						this.habilitaJustificativa(indexpaontamento);
						this.sinalizaCheckBoxItemConforme(indexpaontamento);
					}else {
						this.habilitaJustificativa(indexpaontamento);
						this.sinalizaCheckBoxItemInconforme(indexpaontamento);
					}
				}
			});
		});
		this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados = Object.assign([], this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados);
		this.conformizarTodosNaoAnalisados();
	}

	private encontrarPosicaoApontamento(apontamentoAtual: any) {
		let indexpaontamento;
		for (let index = 0; index < this.checkListPresenter.checkListVisualizacao.apontamentos.length; index++) {
			if (this.checkListPresenter.checkListVisualizacao.apontamentos[index].id == apontamentoAtual.id) {
				indexpaontamento = index;
				break;
			}
		}
		return indexpaontamento;
	}

	private encontrarIdCheckListAnterior(): number {
		if(Object.keys(this.checkListPresenter.checkListVisualizacao.checkEmFoco).length == 0) {
			return this.checkListPresenter.checkListVisualizacao.idCheckListAtivado.id;
		}

		if(Object.keys(this.checkListPresenter.checkListVisualizacao.checkEmFoco).length != 0) {
			return this.checkListPresenter.checkListVisualizacao.checkEmFoco.idcheck;
		}
	}

	private encontrarCheckListisVerificadoPorId(dossieProduto: any, listaCheckApontamentos: any[], id: number) {
		if (dossieProduto.verificacoes && dossieProduto.verificacoes.length > 0) {
			dossieProduto.verificacoes.forEach(verificacoesCheck => {
				if (verificacoesCheck.checklist.identificador_checklist === id) {
					listaCheckApontamentos.push(verificacoesCheck);
				}
			});
		}
	}

	/**
	 * Regarrega status dos apontamentos de um documento já tratado anteriormente
	 * @param indexItemDataTable 
	 */
	invalidaValidaItemDocumentoAtualizarAoCarregar(indexItemDataTable: number) {
		if (this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
			this.habilitaJustificativa(indexItemDataTable);
			this.sinalizaCheckBoxItemConforme(indexItemDataTable);
		} else if (false == this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
			this.habilitaJustificativa(indexItemDataTable);
			this.sinalizaCheckBoxItemInconforme(indexItemDataTable);
		}
	}

	private sinalizaCheckBoxItemInconforme(indexItemDataTable: number) {
		$("#check" + indexItemDataTable + "").addClass("slider-apontamento-conforme");
	}

	private sinalizaCheckBoxItemConforme(indexItemDataTable: number) {
		$("#check" + indexItemDataTable + "").removeClass("slider-apontamento-conforme");
	}

	private habilitaJustificativa(indexItemDataTable: number) {
		$("#balao" + indexItemDataTable).removeClass("item-fa-comments").addClass("item-fa-comments-active");
	}

	private desabilitaJustificativa(indexItemDataTable: number) {
		$("#balao" + indexItemDataTable).removeClass("item-fa-comments-active").addClass("item-fa-comments");
	}

	/**
	 * Abre o modal para adicionar uma justificativa em apontamento individual
	 * @param indexItemDataTable 
	 */
	abreModalJustificativa(indexItemDataTable: number) {
		if (undefined != this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[indexItemDataTable]) {
			this.dialogService.addDialog(ModalJustificativaApontamentoComponent, {
				modalDescricao: false,
				justificativa: this.checkListPresenter.checkListVisualizacao.listaJustificativaApontamentos[indexItemDataTable],
				descricaoItem: ''
			})
				.subscribe(retorno => {
					if (retorno != undefined) {
						this.checkListPresenter.checkListVisualizacao.listaJustificativaApontamentos[indexItemDataTable] = retorno.motivoApontamento;
						this.confirm();
					}
				},
					() => {
						this.loadService.hide();
					});
		}
	}

	/**
	 * Metodo percorre todos os apontamentos não verificados e preenche os mesmos com o status conforme.
	 */
	conformizarTodosNaoAnalisados() {
		if (!this.checkListPresenter.checkListVisualizacao.habilitarDesabilitarTratamento) {
			let listApontamentosCheckadosAux = this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados;
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.map((apontamentoAtual, indexItemDataTable) => {
				if (undefined == apontamentoAtual) {
					listApontamentosCheckadosAux[indexItemDataTable] = true;
					this.habilitaJustificativa(indexItemDataTable);
					this.sinalizaCheckBoxItemConforme(indexItemDataTable);
				}
			});
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados = this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados;
			this.confirm();
		}
	}

	modalDescricaoHeaderCheckList() {
		const itemCheckList: DescricaoCheckList = new DescricaoCheckList();
		itemCheckList.id = 1;
		itemCheckList.titulo = this.checkListPresenter.checkListVisualizacao.nomeChecklistTitle;
		itemCheckList.descricao = this.checkListPresenter.checkListVisualizacao.orientacaoChecklist;
		
		this.dialogService.addDialog(ModalJustificativaApontamentoComponent, {
			modalDescricao: true,
			justificativa: '',
			descricaoItem: itemCheckList
		});
	}

	/**
	 * Abre o modal de descrição de um apontamento para mostar o texto de descrição.
	 * @param item 
	 */
	modalDescricao(item: any) {
		this.dialogService.addDialog(ModalJustificativaApontamentoComponent, {
			modalDescricao: true,
			justificativa: '',
			descricaoItem: item
		});
	}

	/**
	 * Executa analise da verificação feita pelo usuário para cada apontamento,
	 * determinando se houve aprovação ou rejeição.
	 */
	confirm() {
		let conjuntoComboVinculoTipoDocSelecionado: ConjuntoComboVinculoTipoDocSelecionado = this.verificarApontamentosCheckListFase();
		let tipoChecklistFase = conjuntoComboVinculoTipoDocSelecionado.isChecklistFase ? TIPO_ARVORE_DOCUMENTO.CHECKLIST : conjuntoComboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado[1];
		let checkListPrevioIcomplento = false;
		for (let check of this.checkListPresenter.checkListVisualizacao.listaChekList) {
			if (check.tipo === tipoChecklistFase) {
				if (((conjuntoComboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado &&
					conjuntoComboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado[0] == check.idVinculo) &&
					(conjuntoComboVinculoTipoDocSelecionado.tipoDocComboSelecionado &&
						conjuntoComboVinculoTipoDocSelecionado.tipoDocComboSelecionado[0] == check.idTipoDocumento)) ||
					(conjuntoComboVinculoTipoDocSelecionado.isChecklistFase && this.idCheckListAtivado.id == check.idVinculo &&
						this.idCheckListAtivado.data == check.data_revogacao)) {
					check.listaResposta = [];
					check.existeIncomformidade = false;
					this.checkaExisteRejeicaoTratamentoAtual(check);
					checkListPrevioIcomplento = this.emiteSinalInconformidadeCheckListPrevio(check, conjuntoComboVinculoTipoDocSelecionado, checkListPrevioIcomplento);
					this.armazenaRespostaApontamentosCheckListAtual(check);
					break;
				}
			}
		}
		if (!checkListPrevioIcomplento &&
			!this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.some(respostaApont => undefined == respostaApont)) {
			this.listaCheklistChanged.emit(Object.assign([], this.checkListPresenter.checkListVisualizacao.listaChekList));
			this.mudancaSalvaService.setIsMudancaSalva(false);
			this.messageSucessoSalvarChecklist();
		}
	}

	/**
	 * Envia sinal para o sidebar menu, sinalizando que existe inconformidade na verificação do checklist previo
	 * @param check checklist de apontamentos atual verificado
	 * @param conjuntoComboVinculoTipoDocSelecionado objeto que contém a seleção do vinculo e o documento selecionado no sidebar menu
	 */
	private emiteSinalInconformidadeCheckListPrevio(check: Checklist, conjuntoComboVinculoTipoDocSelecionado: ConjuntoComboVinculoTipoDocSelecionado, checkListPrevioIcomplento: boolean): boolean {
		if (check.existeIncomformidade && conjuntoComboVinculoTipoDocSelecionado.isChecklistFase && check.checklistPrevio) {
			this.telaDeTratamentoService.setExibindoChecklistsPrevios(true);
		} else if (check.checklistPrevio &&
			this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.some(respostaApont => undefined == respostaApont)) {
			checkListPrevioIcomplento = true;
		}
		return checkListPrevioIcomplento;
	}

	/**
	 * Verifica se existe pelomenos um apontamento rejeitado para o documento atual
	 * @param check checklist de apontamentos atual verificado
	 */
	private checkaExisteRejeicaoTratamentoAtual(check: Checklist) {
		if (!this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.some(respostaApont => undefined == respostaApont)) {
			let respostaApont = this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados.some(respostaApont => respostaApont == false);
			if (respostaApont) {
				check.existeIncomformidade = true;
			}
		}
	}

	/**
	 * Guarda as verificações com seus comentários realizadas em cada apontamento pelo usuario.
	 * @param check checklist de apontamentos atual verificado
	 */
	private armazenaRespostaApontamentosCheckListAtual(check: Checklist) {
		this.checkListPresenter.checkListVisualizacao.apontamentos.forEach((apontamento, idx) => {
			let objApont = new ApontamentoChecklist();
			objApont.idApontamento = apontamento.id;
			objApont.verificacaoAprovada = this.checkListPresenter.checkListVisualizacao.listApontamentosCheckados[idx];
			objApont.verificacaoPrevia = false;
			(this.checkListPresenter.checkListVisualizacao.listaJustificativaApontamentos && this.checkListPresenter.checkListVisualizacao.listaJustificativaApontamentos[idx]) ? objApont.comentario = this.checkListPresenter.checkListVisualizacao.listaJustificativaApontamentos[idx] : "";
			check.listaResposta.push(objApont);
		});
	}

	/**
	 * Apresenta mensagem de sucesso para cada verficação de checklist
	 */
	private messageSucessoSalvarChecklist() {
		this.clearAllMessages();
		let messagesSuccess = new Array<string>();
		messagesSuccess.push("Verificação salva com sucesso");
		this.alertMessagesSucessChanged.emit(messagesSuccess);
	}

	/**
	 * Verifica se o apontamento é da fase, caso não for informa a arvore e o documento.
	 */
	private verificarApontamentosCheckListFase() {
		let conjuntoCombovinculoTipoDocSelecionado: ConjuntoComboVinculoTipoDocSelecionado = new ConjuntoComboVinculoTipoDocSelecionado();
		
		conjuntoCombovinculoTipoDocSelecionado.tipoVinculoComboSelecionado = $("#comboDocumentacao_1 option:selected").val() ? $("#comboDocumentacao_1 option:selected").val().split("|") : undefined;
		conjuntoCombovinculoTipoDocSelecionado.tipoDocComboSelecionado = $("#comboDocumento_1 option:selected").val() ? $("#comboDocumento_1 option:selected").val().split("|") : undefined;
	
		if(this.listDocumentosImagens && this.listDocumentosImagens.length > 0){
			let conjuntoComboTemp = this.setarCombosVinculoTipoDocSelecionado();
			if(conjuntoComboTemp){
				conjuntoCombovinculoTipoDocSelecionado = conjuntoComboTemp;
			}
		}
		
		if ((!this.listDocumentosImagens || this.listDocumentosImagens.length == 0) &&
			!conjuntoCombovinculoTipoDocSelecionado.tipoVinculoComboSelecionado &&
			!conjuntoCombovinculoTipoDocSelecionado.tipoDocComboSelecionado) {
			conjuntoCombovinculoTipoDocSelecionado.isChecklistFase = true;
		}

		return conjuntoCombovinculoTipoDocSelecionado;
	}

	private setarCombosVinculoTipoDocSelecionado() {
		let doc = this.listDocumentosImagens[0];
		if (doc && doc.tipo_documento.id && doc.idVinculo && doc.tipo) {
			let comboVinculoTipoDocSelecionado: ConjuntoComboVinculoTipoDocSelecionado = new ConjuntoComboVinculoTipoDocSelecionado();

			comboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado = [];
			comboVinculoTipoDocSelecionado.tipoDocComboSelecionado = [];
			comboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado[0] = doc.idVinculo;
			comboVinculoTipoDocSelecionado.tipoVinculoComboSelecionado[1] = doc.tipo;
			comboVinculoTipoDocSelecionado.tipoDocComboSelecionado[0] = doc.tipo_documento.id;
			comboVinculoTipoDocSelecionado.tipoDocComboSelecionado[1] = doc.tipo;

			return comboVinculoTipoDocSelecionado;
		}
	}
}