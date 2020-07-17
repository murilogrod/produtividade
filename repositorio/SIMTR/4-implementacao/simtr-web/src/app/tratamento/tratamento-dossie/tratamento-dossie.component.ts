import { Component, OnInit, Output, EventEmitter, AfterViewChecked, ChangeDetectorRef, AfterContentInit, Input, HostListener } from '@angular/core';
import { ActivatedRoute, RouterStateSnapshot, Router } from '@angular/router';
import { TratamentoService } from '../tratamento.service';
import { LoaderService, ApplicationService, EventService, } from 'src/app/services';
import { TIPO_ARVORE_DOCUMENTO, TIPO_LAYOUT, LOCAL_STORAGE_CONSTANTS, UNDESCOR, MESSAGE_ALERT_MENU, EVENTS, DOSSIE_PRODUTO, MSG_VALIDAR_SUCESSO, FECHA_MODAL, SITUACAO_GATILHO_POPUP } from 'src/app/constants/constants';
import { FaseTratamento } from 'src/app/model/fase-tratamento';
import { Location } from '@angular/common';
import { Checklist } from 'src/app/model/checklist';
import { Utils } from '../../utils/Utils';
import { DialogService } from '../../../../node_modules/angularx-bootstrap-modal';

import { CanComponenteDeactivate } from '../../guards/can-deactivate.guard';
import { Observable, of } from '../../../../node_modules/rxjs';
import { MudancaSalvaService } from '../../services/mudanca-salva.service';
import { HeaderSearchService } from 'src/app/services/header-search.service';
import { Verificacao } from '../../model/model-tratamento/verificacao.model';
import { ParecerApontamento } from '../../model/model-tratamento/parecer-apontamento.model';
import { VerificacaoInvalida } from '../../model/model-tratamento/verificacao-invalida.model';
import { TipoTelaErro } from 'src/app/global-error/utils/tipo-tela-erro';
import { DataService } from 'src/app/services/data-service';
import { AbrirDocumentoPopupDefaultService } from 'src/app/services/abrir-documento-popup-default.service';
import { TelaDeTratamentoService } from '../tela-de-tratamento.service';
declare var $: any;

@Component({
	selector: 'tratamento-dossie',
	templateUrl: './tratamento-dossie.component.html',
	styleUrls: ['./tratamento-dossie.component.css']
})
export class TratamentoDossieComponent extends HeaderSearchService implements OnInit, AfterViewChecked, CanComponenteDeactivate {
	listDocumentosImagens: any[];
	listaDocumentacao: any[];
	dossieProduto: any;
	dossieConsulta: any;

	/** O ID do processo fase selecionado para exibição. A seleção é feita na tela-wizard. */
	idFaseSelecionada: number;

	/** Indica quantos visualizadores de documento estão ativos. */
	qtdeVisualizadores: number;
	
	/** Indica se a tela-dragDrop deve ser exibida. A tela-dragDrop contêm os visualizadores de documentos e checklists. */
	mostrarTelaDragDrop: boolean = false;

	processoDossieReFerenciaPatriarca: any;
	processoFaseCheckList: number;
	idCheckListAtivado: number;
	layoutHorizontalA: boolean = false;
	layoutHorizontalB: boolean = false;
	layoutVerticalA: boolean = true;
	layoutVerticalB: boolean = false;
	hasMapListaCombo: any[];
	listaChekList: Checklist[];
	listaDocumentoObrigatorio: any[];
	
	/** Quando true, indica que o checklist em exibição é um checklist de fase. */
	exibindoChecklistFase: boolean = false;

	buscandoMacroProcesso: boolean = false;
	@Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
	observa: Observable<boolean>;
	isTratamentoFinalizado: boolean = false;
    processoGerador: number;
	habilitar: boolean;
	isDocumentoEmNovaJanela: boolean = false;
	idDoPrimeiroInstanciaDocumentoASerCarregado: number;

	// usado no temporizador
	sleep: number = 0;
	minuto: number = 0;
	segundo: number = 0;
	tempoDeSessao: number = 0; // o tempo da sessão é em minutos
	sessaoTemporizador: number = 0;
	totalDeSegundos: number = 0;
	minutoFormatado: string = "";
	segundoFormatado: string = "";
	trintaPorCentroDoTotalDeSegundos: number = 0;
	habilitarTemporizador: boolean = false;
	temporizador: any;
	cronometro: any;
	existeCheckList: boolean;
	isHabilitarGatilhoPopup: boolean;
	toolTipAbrirPopup: string = "";

	constructor(
		private changeDetectorRef: ChangeDetectorRef,
		private route: ActivatedRoute,
		private rota: Router,
		private tratamentoService: TratamentoService,
		private loadService: LoaderService,
		private appService: ApplicationService,
		private location: Location,
		private dialogService: DialogService,
		private eventService: EventService,
		private mudancaSalvaService: MudancaSalvaService,
		private dataService: DataService,
		private abrirDocumentoPopupDefaultService: AbrirDocumentoPopupDefaultService,
		private telaDeTratamentoService: TelaDeTratamentoService
	) {
		super(eventService);
	}

	headerSearch(filtros: any) {}

	cleanHeaderSearch() {}

	ngOnInit() {
		this.inicializarTelaDeTratamento(false);
		this.oculutaMenuPrincipal();
		this.verificarValorDefaultAbrirDocumentoEmNovaJanela();
		
		this.abrirDocumentoPopupDefaultService.$getExisteCheckList.subscribe(resposta => {
			this.existeCheckList = resposta;

			this.atualizarTooTipAbrirPopup();
		});

		this.abrirDocumentoPopupDefaultService.$getGatilhoAbrirPopup.subscribe(resposta => {
			
			if(resposta && (resposta == SITUACAO_GATILHO_POPUP.ABRIR  || resposta == SITUACAO_GATILHO_POPUP.EM_ABERTO)){
				this.isHabilitarGatilhoPopup = true;
			}else{
				this.isHabilitarGatilhoPopup = false;
			}
			 
			this.atualizarTooTipAbrirPopup();
		});

		this.abrirDocumentoPopupDefaultService.setExecutouUmaUnicaVez(false);
		this.atualizarTooTipAbrirPopup();

		this.telaDeTratamentoService.exibindoChecklistFase.subscribe(
			exibindoChecklistFase => this.exibindoChecklistFase = exibindoChecklistFase);
	}

	async inicializarTemporizador(){
		if(!this.processoDossieReFerenciaPatriarca || this.processoDossieReFerenciaPatriarca.tempo_tratamento == 0){
			this.tempoDeSessao = 10; /// valor mímimo pra não cancelar a tela antes de mesmo de abrir
		}else{
			this.tempoDeSessao = this.processoDossieReFerenciaPatriarca.tempo_tratamento;
		}
		
		this.habilitarTemporizador = false;
		clearInterval(this.temporizador);
		clearInterval(this.cronometro);
		this.calcularDelay();
		let promiseCronometro =  this.promiseCronometoDecrescente();
		promiseCronometro.then();
	}

	ngAfterViewChecked() {
		this.changeDetectorRef.detectChanges();
	}

	abrirPopup(){
		if(!this.isHabilitarGatilhoPopup && this.existeCheckList && this.listDocumentosImagens[0] && this.listDocumentosImagens.length > 0){
			this.abrirDocumentoPopupDefaultService.setGatilhoAbrirPopup(SITUACAO_GATILHO_POPUP.ABRIR);
		}
		this.atualizarTooTipAbrirPopup();
	}

	@HostListener('window:keydown', ['$event'])
	onKeyDown(event: KeyboardEvent) {
		if ((event.ctrlKey || event.shiftKey) && (
			event.key == '6' || event.key == '7' || event.key == '8' || event.key == '9' ||
			event.key == 'd' || event.key == 'D' || event.key == 'r' || event.key == 'R' || event.key == 'j' || event.key == 'J')) {
			event.preventDefault(); // Previne o comportamento padrão do evento de atalho no navegador.

			// Ctrl pressionado, Shift não. KeyCode de outra tecla que não seja Ctrl(17) ou Shift(16) indica que há combinação Ctrl+algo.
			if (event.ctrlKey && !event.shiftKey && event.keyCode != 17 && event.keyCode != 16) {
				if (event.key == '6') {
					this.cancelaTratamento();
				}
				if (event.key == '7') {
					this.validarChecklist(true);
				}
				if (event.key == '8') {
					this.finalizarTratamento(true);
				}
				if (event.key == '9') {
					this.buscarProximoDocumentoParaTratar();
				}
				if (event.key == 'd' || event.key == 'D') {
					this.habilitarDuasVisualizacaoImg();
				}
				if ((event.key == 'r' || event.key == 'R') && this.habilitarTemporizador) {
					this.renovarTempoSessao();
				}
				if ((event.key == 'j' || event.key == 'J') 
					&& (this.isDocumentoEmNovaJanela && !this.isHabilitarGatilhoPopup && this.existeCheckList && this.listDocumentosImagens.length > 0)) {
					this.abrirPopup();
					this.atualizarTooTipAbrirPopup();
				}
			}
		}
	}

	private inicializarTelaDeTratamento(buscarProximo: boolean) {
		this.loadService.show();
		this.listaChekList = [];
		this.qtdeVisualizadores = 1;
		$('.select2').select2();
		this.carregarDossieProduto(buscarProximo);
		this.definirInicioIconAdImgCompara(true);
		$('#layout').on("change", event => {
			let tipoLayout = event.target.value;
			this.qtdeVisualizadores = 1;
			if (tipoLayout != "") {
				this.layoutHorizontalA = tipoLayout == TIPO_LAYOUT.HORIZONTAL_A;
				this.layoutVerticalA = tipoLayout == TIPO_LAYOUT.VERTICAL_A;
				this.layoutHorizontalB = tipoLayout == TIPO_LAYOUT.HORIZONTAL_B;
				this.layoutVerticalB = tipoLayout == TIPO_LAYOUT.VERTICAL_B;
				this.definirInicioIconAdImgCompara(true);
			}
		});
		this.mudancaSalvaService.setIsMudancaSalva(true);
	}

	private definirInicioIconAdImgCompara(inicial: boolean) {
		let inicialIcon = (this.layoutVerticalA || this.layoutVerticalB) ? false : inicial ? true : false;
		this.girarIconeHorizontalVertical(inicialIcon);
	}

	/**
	 * Carregar Dossie Produto conforme no ID do processo informado no ROUTER
	 */
	private carregarDossieProduto(buscarProximo: boolean) {
		let idProcesso: string = this.buscandoMacroProcesso ? this.route.snapshot.paramMap.get('macro') : this.route.snapshot.paramMap.get('id');
		let idDossieProduto: string =  this.route.snapshot.paramMap.get('idDossieProduto');
		this.loadService.show();
		
		if(idDossieProduto){
			let dossieProduto: any = this.dataService.getData(idDossieProduto);
			this.dossieConsulta = dossieProduto;
			this.idFaseSelecionada = this.dossieConsulta.processo_fase.id;
			this.processoGerador = this.dossieConsulta.processo_dossie.id;
			this.dossieProduto = Object.assign({}, this.dossieConsulta);
			this.carregarWizard(this.dossieProduto.processo_dossie.id, this.dossieProduto.processo_fase.id);

			// INICIALIZAR SESSÃO
			let promiseIncializarTemporizador = this.inicializarTemporizador();
			promiseIncializarTemporizador.then();

		}else{
			this.tratamentoService.capturaDossieTratamento(idProcesso).subscribe(response => {
				this.buscandoMacroProcesso = false;
				this.dossieConsulta = response;
				this.idFaseSelecionada = this.dossieConsulta.processo_fase.id;
				this.processoGerador = this.dossieConsulta.processo_dossie.id;
				this.dossieProduto = Object.assign({}, this.dossieConsulta);
				this.carregarWizard(this.dossieProduto.processo_dossie.id, this.dossieProduto.processo_fase.id);

				// INICIALIZAR SESSÃO
				let promiseIncializarTemporizador = this.inicializarTemporizador();
				promiseIncializarTemporizador.then();

			}, error => {
				if (buscarProximo && !this.buscandoMacroProcesso) {
					this.buscandoMacroProcesso = true;
					this.carregarDossieProduto(true);
				} else {
					this.achaAposFinalizarTratamento(true);
				}
				console.log(error);
				this.loadService.hide();
				throw error;
			});
		}
	}

	/**
	 * carregar dados Conforme A faze do Wizard, passando o dossie e a fase a ser montada
	 * @param idDossie 
	 * @param idFase 
	 */
	private carregarWizard(idDossie: number, idFase: number) {
		this.processoFaseCheckList = idFase;
		this.metodoBuscaDossieReferenciaPatriarca(idDossie);
		this.loadService.hide();
	}

	/**
	 * Buscar Processo Dossie pelo Id, referente a consulta do Patriarca 
	 * @param processoDossieId 
	 */
	private metodoBuscaDossieReferenciaPatriarca(processoDossieId: number) {
		this.processoDossieReFerenciaPatriarca = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + processoDossieId));
		this.montarFaseProcesso();
		this.identificarPrimeiroDocumentoASerCarregado();
	}

	/**
	 * monatr o dossie Fase do processo ativo do wizard, conforme referencia do Patriarca
	 */
	private montarFaseProcesso() {
		if (this.idFaseSelecionada == this.dossieConsulta.processo_fase.id) {
			this.dossieProduto.processo_fase = this.dossieConsulta.processo_fase;
		}
		else {
			let obtFase = new FaseTratamento();
			for (let faseRefPatriarca of this.processoDossieReFerenciaPatriarca.processos_filho) {
				if (faseRefPatriarca.id == this.idFaseSelecionada) {
					this.montarDossieFaseReferentePatriarca(obtFase, faseRefPatriarca);
				}
			}
			this.dossieProduto.processo_fase = obtFase;
		}
	}

	/**
	 * montando a fase do processo dossie ativo
	 * as informações são relativas a fase Referente a consulta Do patriarca
	 * @param obtFase 
	 * @param faseRefPatriarca 
	 */
	private montarDossieFaseReferentePatriarca(obtFase: FaseTratamento, faseRefPatriarca: any) {
		obtFase.id = faseRefPatriarca.id;
		obtFase.nome = faseRefPatriarca.nome;
		this.montarElementosConteudosRefPatriarca(obtFase, faseRefPatriarca);
		this.montarRespostaFormularioRefPatriarca(obtFase, faseRefPatriarca);
	}

	/**
	 * Montar resposta do formulario conforme a fase do wizard selecionada ou com base na consulta inicial
	 * @param obtFase 
	 * @param faseRefPatriarca 
	 */
	private montarRespostaFormularioRefPatriarca(obtFase: FaseTratamento, faseRefPatriarca: any) {
		obtFase.respostas_formulario = [];
		faseRefPatriarca.campos_formulario.forEach(respostaForm => {
			obtFase.respostas_formulario.push(this.dossieConsulta.respostas_formulario.find(r => r.id == respostaForm.id));
		});
	}

	identificarPrimeiroDocumentoASerCarregado(){
	  this.idDoPrimeiroInstanciaDocumentoASerCarregado = this.dossieProduto.instancias_documento[0].id;
	}

	/**
	 * montando elemento Conteudo do processo fase.
	 * @param obtFase 
	 * @param faseRefPatriarca 
	 */
	private montarElementosConteudosRefPatriarca(obtFase: FaseTratamento, faseRefPatriarca: any) {
		obtFase.instancias_documento = [];
		faseRefPatriarca.elementos_conteudo.forEach(elementoConteudo => {
			if (undefined != elementoConteudo.identificador_elemento) {
				const doc = this.dossieConsulta.instancias_documento.find(x => x.id_elemento_conteudo == elementoConteudo.identificador_elemento);
				if (undefined != doc) {
					obtFase.instancias_documento.push(doc);
				}
			}
		});
	}

	/**
	 * Lista Comobo DOcumentação da aba visualizar Tratamento
	 */
	private comboListarDocumentacao() {
		this.listaDocumentacao = [];
		this.hasMapListaCombo = [];
		this.documentacaoDossie();
		this.documentacaoFase();
		this.documentacaoGarantias();
		this.documentacaoProdutos();
		this.documentacaoClientes();
	}


	/**
	 * Percorre o Vinculo CLiente
	 */
	private documentacaoClientes() {
		if (this.dossieProduto.vinculos_pessoas.length > 0) {
			this.dossieProduto.vinculos_pessoas.forEach(pessoa => {
				this.documentacaoDossieCliente(pessoa);
			});
		}
	}

	/**
	 * adicionar na Combo Lista Documentação conforme a pessoa
	 * @param pessoa 
	 */
	private documentacaoDossieCliente(pessoa: any) {
		let obj = {
			"descricao": pessoa.tipo_relacionamento.nome,
			"id": pessoa.dossie_cliente.id,
			"tipo": TIPO_ARVORE_DOCUMENTO.CLIENTE
		};
		this.listaDocumentacao.push(obj);
		this.listaHasMapCombos(pessoa.dossie_cliente.id, TIPO_ARVORE_DOCUMENTO.CLIENTE, pessoa.instancias_documento);
		
		// adicionar o vinculo da pessoa com instancia de documento
		if(pessoa.instancias_documento &&  pessoa.instancias_documento.length > 0){

			this.dossieProduto.instancias_documento.forEach(instanciaDoc => {
				
				pessoa.instancias_documento.forEach(pessoaInstanciaDoc => {
					
					if(instanciaDoc.id == pessoaInstanciaDoc.id){
						instanciaDoc.idVinculo = pessoa.dossie_cliente.id
					}
				});

			});
		}
	}

	/**
	 * Adiciona na Combo Lista Documentação conforme o produto
	 */
	private documentacaoProdutos() {
		if (this.dossieProduto.produtos_contratados.length > 0) {
			this.dossieProduto.produtos_contratados.forEach(produto => {
				if (produto.instancias_documento.length > 0) {
					let obj = {
						"descricao": produto.nome,
						"id": produto.id,
						"tipo": TIPO_ARVORE_DOCUMENTO.PRODUTO
					};
					this.listaDocumentacao.push(obj);
					this.listaHasMapCombos(produto.id, TIPO_ARVORE_DOCUMENTO.PRODUTO, produto.instancias_documento);
				
					// adicionar o vinculo da produto com instancia de documento
					this.dossieProduto.instancias_documento.forEach(instanciaDoc => {

						produto.instancias_documento.forEach(produtoInstanciaDoc => {
							
							if(instanciaDoc.id == produtoInstanciaDoc.id){
								instanciaDoc.idVinculo = produto.id;
							}
						});

					});
				
				}
			});
		}
	}


	/**
	 * Adiciona na Combo Lista Documentação conforme o Garantias
	 */
	private documentacaoGarantias() {
		if (this.dossieProduto.garantias_informadas.length > 0) {
			this.dossieProduto.garantias_informadas.forEach(garantia => {
				if (garantia.instancias_documento.length > 0) {
					let obj = {
						"descricao": garantia.nome_garantia,
						"id": garantia.id,
						"tipo": TIPO_ARVORE_DOCUMENTO.GARANTIAS
					};
					this.listaDocumentacao.push(obj);
					this.listaHasMapCombos(garantia.id, TIPO_ARVORE_DOCUMENTO.GARANTIAS, garantia.instancias_documento);
					
					// adicionar o vinculo da garantia com instancia de documento
					this.dossieProduto.instancias_documento.forEach(instanciaDoc => {
						
						garantia.instancias_documento.forEach(garantiaInstanciaDoc => {
							
							if(instanciaDoc.id == garantiaInstanciaDoc.id){
								instanciaDoc.idVinculo = garantia.id;
							}

						});
						
					});
				}
			});
		}
	}


	/**
	 * Adiciona na Combo Lista Documentação conforme o Dossie Fase
	 */
	private documentacaoFase() {
		if (this.dossieProduto.processo_fase.instancias_documento && this.dossieProduto.processo_fase.instancias_documento.length > 0) {
			let objFase = {
				"descricao": this.dossieProduto.processo_fase.nome,
				"id": this.dossieProduto.processo_fase.id,
				"tipo": TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE
			};	
			this.listaDocumentacao.push(objFase);
			this.listaHasMapCombos(this.dossieProduto.processo_fase.id, TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE, this.dossieProduto.processo_fase.instancias_documento)
		
			if(this.dossieProduto.instancias_documento && this.dossieProduto.instancias_documento.length > 0){
				// adicionar o vinculo da fase com instancia de documento
				this.dossieProduto.instancias_documento.forEach(instanciaDoc => {

					this.dossieProduto.processo_fase.instancias_documento.forEach(faseInstanciaDoc => {
						if(instanciaDoc.id == faseInstanciaDoc.id){
							instanciaDoc.idVinculo = this.dossieProduto.processo_fase.id;
							faseInstanciaDoc.idVinculo = this.dossieProduto.processo_fase.id;
						}
					});

				});
			}
		}
	}


	/**
	 * Adiciona na Combo Lista Documentação conforme o Dossie
	 */
	private documentacaoDossie() {
		if (this.dossieProduto.processo_dossie.instancias_documento && this.dossieProduto.processo_dossie.instancias_documento.length > 0) {
			this.hasMapListaCombo;
			let objDossie = {
				"descricao": this.dossieProduto.processo_dossie.nome,
				"id": this.dossieProduto.processo_dossie.id,
				"tipo": TIPO_ARVORE_DOCUMENTO.DOSSIE
			};
			this.listaDocumentacao.push(objDossie);
			this.listaHasMapCombos(this.dossieProduto.processo_dossie.id, TIPO_ARVORE_DOCUMENTO.DOSSIE, this.dossieProduto.processo_dossie.instancias_documento);

			if(this.dossieProduto.instancias_documento && this.dossieProduto.instancias_documento.length > 0){
				// adicionar o vinculo da processoDossie com instancia de documento
				this.dossieProduto.instancias_documento.forEach(instanciaDoc => {
					
					this.dossieProduto.processo_dossie.instancias_documento.forEach(processoDossieInstanciaDoc => {
						if(instanciaDoc.id == processoDossieInstanciaDoc.id){
							instanciaDoc.idVinculo = this.dossieProduto.processo_dossie.id;
							processoDossieInstanciaDoc.idVinculo = this.dossieProduto.processo_dossie.id;
						}
					});
				});
			}
		}
	}

	private listaHasMapCombos(idDocumentacao, tipo: string, lista: any[]) {
		lista.forEach(item => {
			let objInstDoc = {
				"idDocumentacao": idDocumentacao,
				"itemDocumento": item.documento.tipo_documento.id,
				"tipo": tipo
			}
			this.hasMapListaCombo.push(objInstDoc);
		});
	}

	/**
	 * responsavel por girar o icone que add duas visualização, e informa se e pra mostrar duas ou so uma visualização de documento
	 */
	habilitarDuasVisualizacaoImg() {
		if (!this.exibindoChecklistFase) {
			this.qtdeVisualizadores = this.qtdeVisualizadores == 1 && !this.exibindoChecklistFase ? 2 : 1;

			if (this.layoutVerticalA || this.layoutHorizontalB) {
				this.girarIconeHorizontalVertical(this.qtdeVisualizadores != 1)
			} else {
				this.girarIconeHorizontalVertical(this.qtdeVisualizadores == 1);
			}
		}
	}

	/**
	 * Com base na informação booleana girar ele add ou remove a classe
	 * @param girar 
	 */
	private girarIconeHorizontalVertical(girar: boolean) {
		$(".incoAddVisualizar .fa").removeClass("horizontal");
		if (girar) {
			$(".incoAddVisualizar .fa").addClass("horizontal");
		}
	}

	oculutaMenuPrincipal(){
		$("#menu-principal").click();
	}
	/**
	 * Ocultar ou mostrar o Menu da tela de tratamento
	 */
	changeCollapseSidebar() {
		var selector = $("[data-toggle='toggle']").data("target");
		if (!$("#collapse-sidebar-menu").hasClass("corpSide in")) {
			$(selector).toggleClass('in');
			$(".corpWizardDivisor").animate({ width: "96%" }, 3000);
			$("#iconMenuSidebar").css('display', 'none');	
		} else {
			$(".corpWizardDivisor").animate({ width: "79%" }, 500);
			$(selector).removeClass('in');
			$("#iconMenuSidebar").css('display', 'block');
		}
	}

	habilitarBtnFinalizarEProximoPrevioIncoforme() {
		let habilitarBtnInconformePrevia = this.listaChekList.some(ck => ck.checklistPrevio && ck.existeIncomformidade);
		let habilitarBtnFinalizarFinal = this.listaChekList.some(ck => !ck.checklistPrevio && ck.habilitaVerificacao && ck.listaResposta && ck.listaResposta.length > 0);
		return habilitarBtnInconformePrevia ? habilitarBtnInconformePrevia : habilitarBtnFinalizarFinal;
	}

	handlleChangeEventCheckList(input) {
		this.mostrarTelaDragDrop = true;
		this.idCheckListAtivado = input;
		this.comboListarDocumentacao();
	}

	handleChangeListDocumentos(input) {
		this.mostrarTelaDragDrop = true;
		this.listDocumentosImagens = Object.assign([], input);
		this.comboListarDocumentacao();
	}

	handleChangeListDocumentosImagem(input) {
		this.listDocumentosImagens = [];
		this.listDocumentosImagens = Object.assign([], input);
	}

	onChangeFaseSelecionada(idFaseSelecionada: number) {
		this.mostrarTelaDragDrop = false;
		this.idFaseSelecionada = idFaseSelecionada;
		this.montarFaseProcesso();
	}

	handleChangeListaChekList(input) {
		this.listaChekList = (Object.assign([], input));
	}

	handlleMessagesError(messages) {
		this.messagesSuccess = [];
		this.messagesError = Object.assign([], messages);
	}

	handlleMessagesInfo(messages) {
		this.messagesInfo = messages;
	}

	handlleMessagesSucess(messages) {
		this.messagesError = [];
		this.messagesSuccess = [];
		this.messagesSuccess.push(messages[0]);
	}

	onCloseMessageSuccess() {
		this.messagesSuccess = [];
	}

	/**
	 * Cancela o tratamento do dossie em atendimento
	 */
	cancelaTratamento() {
		this.isTratamentoFinalizado = false;
		this.rota.navigate(['tratamento']);
	}
	/**
	 * Validar checkList apontados
	 */
	validarChecklist(mostraMsg) {
		this.listaDocumentoObrigatorio = [];
		this.messagesError = [];
		if (this.validarDocumentosConforme()) {
			if (mostraMsg) {
				let textMsgSucesso = MSG_VALIDAR_SUCESSO.TRATAMENTO;
				this.messagesSuccess = [];
				this.addMessageSuccess(textMsgSucesso);
			}
			this.loadService.hide();
			return true;
		}
		this.loadService.hide();
		return false;
	}

	habilitarBtnFinalizar() {
		for (let check of this.listaChekList) {
			let existeVeirifacaoPrevia = this.listaChekList.some(checklist => checklist.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && checklist.checklistPrevio && (!checklist.listaResposta || checklist.existeIncomformidade));
			if (existeVeirifacaoPrevia) {
				if (!check.listaResposta && check.habilitaVerificacao && check.checklistPrevio) {
					if (check.habilitaVerificacao) {
						this.habilitar = true;
					}
				}
			}
			if (!existeVeirifacaoPrevia) {
				if (!check.listaResposta && check.habilitaVerificacao) {
					if (check.habilitaVerificacao) {
						this.habilitar = true;
					}
				}
			}
		}
	}

	/**
	 * Metodo responsavel por percorrer todo dossie e verificar Campos Obrigatorio!
	 */
	private validarDocumentosConforme() {
		for (let check of this.listaChekList) {
			let existeVeirifacaoPrevia = this.listaChekList.find(checkList => checkList.checklistPrevio && !checkList.listaResposta || (checkList.checklistPrevio && checkList.listaResposta  && checkList.listaResposta.some(resposta=> undefined == resposta.verificacaoAprovada || false == resposta.verificacaoAprovada)));
			if (check.habilitaVerificacao && existeVeirifacaoPrevia) { 
				if (check.checklistPrevio && !check.listaResposta || (check.listaResposta && check.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada) && check.habilitaVerificacao)) {
					let textMsg;
					if (check.habilitaVerificacao) {
						check.existeIncomformidade = true;
						textMsg = this.quandoForCheckList(check, textMsg);
						textMsg = this.quandoFordeDocumentoArvare(check, textMsg);
						this.addMessageError(textMsg);
						this.listaDocumentoObrigatorio.push(check);
						this.listaDocumentoObrigatorio = Object.assign([], this.listaDocumentoObrigatorio);
					}
				}
			}
			if (check.habilitaVerificacao && !existeVeirifacaoPrevia) {
				if (!check.semApontamentos && !check.checklistPrevio && !check.listaResposta || (check.listaResposta && check.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada) && check.habilitaVerificacao)) {
					let textMsg;
					if (check.habilitaVerificacao) {
						check.existeIncomformidade = true;
						textMsg = this.quandoForCheckList(check, textMsg);
						textMsg = this.quandoFordeDocumentoArvare(check, textMsg);
						this.addMessageError(textMsg);
						this.listaDocumentoObrigatorio.push(check);
						this.listaDocumentoObrigatorio = Object.assign([], this.listaDocumentoObrigatorio);
					}
				}
			}
		}
		return this.listaDocumentoObrigatorio && this.listaDocumentoObrigatorio.length == 0;
	}

	/**
	 * Monta o texto quando a lista for vazia e o apontamento do CHECLIST da FASE
	 * @param check 
	 * @param textMsg 
	 */
	private quandoFordeDocumentoArvare(check: Checklist, textMsg: any) {
		if (check.tipo != TIPO_ARVORE_DOCUMENTO.CHECKLIST) {
			textMsg = "O Vínculo " + (check.nomeVinculo.nome ? check.nomeVinculo.nome : check.nomeVinculo) + " contém o documento " + check.descricaoTipoDocumento + " não verificado.";
		}
		return textMsg;
	}

	/**
	 * Monta o texto quando a lista for de qualquer documento da arvore
	 * @param check 
	 * @param textMsg 
	 */
	private quandoForCheckList(check: Checklist, textMsg: any) {
		if (check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST) {
			textMsg = "O Checkilist da fase contém o documento " + check.nomeVinculo + " não verificado.";
		}
		return textMsg;
	}

	finalizarTratamento(finalizar: boolean) {
		this.loadService.show();
		this.isTratamentoFinalizado = true;
		this.mudancaSalvaService.setIsMudancaSalva(true);
		if (this.validarChecklist(false)) {
			// fechar popup 
			this.eventService.broadcast(FECHA_MODAL.SHOW_PORTAL);
			let listaVerificacoes: Array<Verificacao> = this.populaVerificacoesDocumentosRealizadas();
			this.tratamentoService.executaTratamentoDossie(this.dossieProduto.id, listaVerificacoes).subscribe(response => {
				this.eventService.broadcast(EVENTS.alertMessage, MESSAGE_ALERT_MENU.MSG_FININALIZADO_TRATAMENTO);
				if (finalizar) {
					this.loadService.hide();
					clearInterval(this.temporizador);
					clearInterval(this.cronometro);
					this.rota.navigate(['tratamento']);
				} else {
					this.achaAposFinalizarTratamento(finalizar);
				}

			}, error => {
				let verificacaoInvalida: VerificacaoInvalida = error.error;
				this.messagesError = [];
				error.tipoTela = TipoTelaErro.TRATAMENTO;
				this.tratarMenssagemError(verificacaoInvalida);
				this.loadService.hide();
				throw error;
			});
		}
	}

	private tratarMenssagemError(verificacaoInvalida: VerificacaoInvalida) {
		if (verificacaoInvalida.checklists_incompletos && verificacaoInvalida.checklists_incompletos.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Checklist Incompletos!");
		} else if (verificacaoInvalida.checklists_inesperados && verificacaoInvalida.checklists_inesperados.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Checklist Inesperado!");
		} else if (verificacaoInvalida.checklists_nao_documentais_ausentes && verificacaoInvalida.checklists_nao_documentais_ausentes.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Checklist Não Documentais Ausentes ");
		} else if (verificacaoInvalida.checklists_nao_documentais_replicados && verificacaoInvalida.checklists_nao_documentais_replicados.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Checklist Não Documentais Replicados ");
		} else if (verificacaoInvalida.identificadores_instancia_invalida && verificacaoInvalida.identificadores_instancia_invalida.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Identificador da Instância Invalida");
		} else if (verificacaoInvalida.instancias_pendentes && verificacaoInvalida.instancias_pendentes.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Instâncias Pendentes");
		} else if (verificacaoInvalida.instancias_verificacao_replicada && verificacaoInvalida.instancias_verificacao_replicada.length > 0) {
			this.addMessageError("Erro ao salvar tratamento: Instâncias Verificada Replicada ");
		} else {
			this.addMessageError(verificacaoInvalida.mensagem);
			this.addMessageError(verificacaoInvalida.detalhe);
		}
	}

	private achaAposFinalizarTratamento(finalizar: boolean) {
		if (finalizar) {
			this.loadService.hide();
			this.eventService.broadcast(EVENTS.alertMessage, MESSAGE_ALERT_MENU.MSG_FINALIZAR_TRATAMENTO);
			this.rota.navigate(['tratamento']);
		}else {
			this.inicializarTelaDeTratamento(true);
		}
	}

	buscarProximoDocumentoParaTratar() {
		this.finalizarTratamento(false);
	}

	/**
	 * 
	 */
	private populaVerificacoesDocumentosRealizadas(): Array<Verificacao> {
		let listaVerificacao = new Array<Verificacao>();
		for (let check of this.listaChekList) {
			// dever ser enviado pro back somente documento que contenha CheckList "idcheck"
			if(!check.situacaoConforme && check.idcheck) {
				let verificacaoDocumento = new Verificacao();
				this.documentoObrigatorioEcomApontamentosPreenchidos(check, verificacaoDocumento);
				this.documentosNaoObrigatorioESemApontamentos(check, verificacaoDocumento);
				if(Object.keys(verificacaoDocumento).length != 0) {
					listaVerificacao.push(verificacaoDocumento);
				}
			}
		}
		return listaVerificacao;
	}

	private documentosNaoObrigatorioESemApontamentos(check: Checklist, verificacaoDocumento: Verificacao) {
		if (!check.habilitaVerificacao) {
			this.dasdosNescessariosParaFinalizar(check, verificacaoDocumento);
		}
	}

	private documentoObrigatorioEcomApontamentosPreenchidos(check: Checklist, verificacaoDocumento: Verificacao) {
		if (check.listaResposta && check.listaResposta.length > 0 && check.habilitaVerificacao) {
			verificacaoDocumento = this.dasdosNescessariosParaFinalizar(check, verificacaoDocumento);
			if (check.listaResposta) {
				verificacaoDocumento.parecer_apontamentos = new Array<ParecerApontamento>();
				for (let resultadoApontamento of check.listaResposta) {
					let apontamento = new ParecerApontamento();
					apontamento.identificador_apontamento = parseInt(resultadoApontamento.idApontamento);
					apontamento.aprovado = resultadoApontamento.verificacaoAprovada;
					apontamento.comentario = resultadoApontamento.comentario;
					verificacaoDocumento.parecer_apontamentos.push(apontamento);
				}
			}
		}
		return verificacaoDocumento
	}

	private dasdosNescessariosParaFinalizar(check: Checklist, verificacaoDocumento: Verificacao) {
		if (TIPO_ARVORE_DOCUMENTO.CHECKLIST != check.tipo) {
			verificacaoDocumento.identificador_instancia_documento = parseInt(check.idInstancia);
		}
		verificacaoDocumento.identificador_checklist = check.idcheck;
		verificacaoDocumento.analise_realizada = check.habilitaVerificacao;
		return verificacaoDocumento;
	}

	canDeactivate(nextState?: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {

		if (this.mudancaSalvaService.getIsMudancaSalva()) {
			this.habilitarMenuHeaderArquitetura();
			if (!this.isTratamentoFinalizado) {
				this.loadService.show();
				if(this.dossieProduto && this.dossieProduto.id) {					
					clearInterval(this.temporizador);
					clearInterval(this.cronometro);
					this.cancelarTratamentoService();
				}else {
					return true;
				}
			}
			return true;
		} else {
			Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA).subscribe(res => {
				if (res) {
					this.habilitarMenuHeaderArquitetura();
					this.mudancaSalvaService.setIsMudancaSalva(true);
					this.rota.navigateByUrl(nextState.url);
					this.tratamentoService.cancelaDossieTratamento(this.dossieProduto.id).subscribe(response => {
						this.loadService.hide();
					}, error => {
							this.loadService.hide();
							console.log(error);
							throw error;
				    });
				}
			},
			() => {
			  this.loadService.hide();
			});
			return false;
		}
	}

	private cancelarTratamentoService() {
		this.tratamentoService.cancelaDossieTratamento(this.dossieProduto.id).subscribe(response => {
			this.rota.navigate(['tratamento']);
		}, error => {
			this.loadService.hide();
			console.log(error);
			throw error;
		});
	}

	private habilitarMenuHeaderArquitetura() {
		$("html, body").removeClass("sidebar-collapse");
		$(".sidebar-toggle").show();
	}

	calcularDelay(){
		this.minuto = this.tempoDeSessao - 1; // a pedido do Fábio -1 minuto pra evitar concorrência
		this.totalDeSegundos = this.minuto * 60; // total de segundos corresponde a 100%  do temporiazador
		this.trintaPorCentroDoTotalDeSegundos = (this.totalDeSegundos + 60) * 0.3; // 30% em total do tempo passado pelo gerador dossiê
		this.trintaPorCentroDoTotalDeSegundos = Number.parseInt(this.trintaPorCentroDoTotalDeSegundos.toString());
	}

	promiseTemporizadorDescrescente(){
		return new Promise(resolve => {
			this.temporizadorDecrescente();
		});
	}

	temporizadorDecrescente(){
	 	this.temporizador = setInterval(()=> {
			
			this.sessaoTemporizador--;

			if(this.sessaoTemporizador === 0){
				return;
			}

		}, this.sleep * 1000);
	}

	promiseCronometoDecrescente(){
		return new Promise(resolve => {
			this.cronometroDecrescente();
		});
	}

	cronometroDecrescente(){
		this.cronometro = setInterval(() => {
			
			this.totalDeSegundos--;

			// calcular minutos
			this.minuto = this.totalDeSegundos / 60;
			this.minuto = Number.parseInt(this.minuto.toString());
			this.minutoFormatado = this.minuto < 10 ? "0" + this.minuto.toString() : this.minuto.toString();

			// calcular segundos
			this.segundo = this.totalDeSegundos % 60;
			this.segundoFormatado = this.segundo < 10 ? "0" + this.segundo.toString() : this.segundo.toString();

			// habilitar função pra renovar tempo sessão
			if(this.trintaPorCentroDoTotalDeSegundos === this.totalDeSegundos){
				this.habilitarTemporizador = true;
				this.sleep = this.totalDeSegundos / 100; // calcular o deleay do temporizador
				this.sessaoTemporizador = 100;
				let promiseTemporiazador =  this.promiseTemporizadorDescrescente();
				promiseTemporiazador.then();
			}

			if(this.totalDeSegundos === 0){
				this.tempoEsgostadoCancelarTratamento();
			}

		}, 1000);
	}

	renovarTempoSessao(){
		this.tratamentoService.renovarTempoSessao(this.dossieProduto.id).subscribe(response => {
			this.inicializarTemporizador();
		}, error => {
			throw error;
		});
	}

	tempoEsgostadoCancelarTratamento(){
		this.isTratamentoFinalizado = true;
		this.mudancaSalvaService.setIsMudancaSalva(true);

		clearInterval(this.temporizador);
		clearInterval(this.cronometro);
		this.cancelarTratamentoService();
	}

	verificarValorDefaultAbrirDocumentoEmNovaJanela(){
		let isDocumentoEmNovaJanela = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.ABRIR_DOCUMENTO_EM_NOVA_JANELA);
		if(isDocumentoEmNovaJanela == null || isDocumentoEmNovaJanela == undefined){
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.ABRIR_DOCUMENTO_EM_NOVA_JANELA, this.isDocumentoEmNovaJanela);
		}else{
			this.isDocumentoEmNovaJanela = isDocumentoEmNovaJanela == 'true' ? true : false;
		}
	}

	atualizarPropriedadeAbrirDocumento(){
		this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.ABRIR_DOCUMENTO_EM_NOVA_JANELA, this.isDocumentoEmNovaJanela);

		if(this.isDocumentoEmNovaJanela && !this.isHabilitarGatilhoPopup && this.existeCheckList && this.listDocumentosImagens && this.listDocumentosImagens.length > 0){
			this.abrirPopup();
		}

		this.atualizarTooTipAbrirPopup();
	}

	atualizarTooTipAbrirPopup(){
		
		if(this.isHabilitarGatilhoPopup){
			this.toolTipAbrirPopup = "Janela popup já esta aberta!";
		}else if(!this.listDocumentosImagens || this.listDocumentosImagens.length == 0){
			this.toolTipAbrirPopup = "Checklist sem documento!";
		}else if(!this.existeCheckList){
			this.toolTipAbrirPopup = "Documento sem checklist!";
		}else{
			this.toolTipAbrirPopup = "Abrir em nova janela (Ctrl+J)";
		}
	}
}