import { Component, OnInit, ViewEncapsulation, AfterViewChecked, Input, EventEmitter, Output, AfterContentChecked, AfterViewInit, HostListener } from '@angular/core';
import {
	CampoFormulario, RespostaDossie, ArvoreDinamica, DossieProduto, DocumentoGED,
	VinculoGarantia, VinculoProduto, VinculoCliente, VinculoProcesso
} from '../../model';
import { Processo } from '../../model';
import { ActivatedRoute, Router, RouterStateSnapshot } from '@angular/router';
import { DossieService } from '../dossie-service';
import { ApplicationService, AlertMessageService } from '../../services';
import { LoaderService } from '../../services/http-interceptor/loader/loader.service';
import { NgForm } from '@angular/forms';
import { UtilsCliente } from '../../cliente/consulta-cliente/utils/utils-client';
import { Utils } from '../../utils/Utils';
import { UtilsManutencao } from './Utils/utils-manutencaoDossie';
import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { ConsultaClienteService } from '../../cliente/consulta-cliente/service/consulta-cliente-service';
import { VinculoArvore } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { VinculoArvoreCliente } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente';
import { VinculoArvoreProcesso } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo';
import { GerenciadorDocumentosDossieProduto } from './gerenciador-documento-dossie-produto/gerenciador-documento-dossie-produto-util';
import { LOCAL_STORAGE_CONSTANTS, UNDESCOR, ALERT_SALVAR_DOSSIE_PRODUTO, SITUACAO_DOSSIE_PRODUTO, DOSSIE_PRODUTO, SITUACAO_DOSSIE_BANCO, TIPO_DOCUMENTO, MESSAGE_ALERT_MENU, SITUACAO_DOSSIE_ATUAL, PARAMETRO_DEFINI_CONSULTA_MANUTENCAO_DOSSIE, ARQUITETURA_SERVICOS, MSG_VALIDAR_SUCESSO } from '../../constants/constants';
import { RespostaCampoFormulario } from '../../model/resposta-campo-formulario';
import { historicoSituacoes } from '../../model/historico_situacoes';
import { historico } from '../../model/historico';
import { GerenciadorDocumentoDossieProdutoEditado } from './gerenciador-documento-dossie-produto/gerenciador-documento-dossie-produto-editado.util';
import { GerenciadorDossieProduto } from './gerenciador-documento-dossie-produto/gerenciador-dossie-produto.util';
import { VinculoArvoreProduto } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto';
import { VinculoArvoreGarantia } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia';
import { ModalRevogarDocumentoComponent } from './modal-revogar/modal-revogar-documento.component';
import { Location } from '@angular/common';
import { MudancaSalvaService } from '../../services/mudanca-salva.service';
import { CanComponenteDeactivate } from '../../guards/can-deactivate.guard';
import { Observable } from 'rxjs';
import { VerificacaoDossie } from '../../model/verificacao-dossie';
import { ConjuntoMacroProcessoGeraDossieUriParams } from '../../model/model-produto/conjunto-macro-processo-gera-dossie-uri-params.model';
import { ManutencaoDossieComponentPresenter } from './presenter/manutencao-dossie.component.presenter';
import { GerenciadorDocumentosEmArvore } from 'src/app/documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util';
import { DossieProdutoModel } from './model-endPoint-dossie-produto/dossieProdutoModel';
import { DossieProdutoModelPath } from 'src/app/dossie/manutencao-dossie/model-endPoint-dossie-produto/dossieProdutoModelPath';
import { IdentificadorDossieFase } from './aba-formulario/modal/modal-pessoa/model/identificadorDossieEFase';
import { TipoTelaErro } from 'src/app/global-error/utils/tipo-tela-erro';
import { TratamentoValidarPermissaoService } from 'src/app/tratamento/tratamento-validar-permissao.service';
import { DataService } from 'src/app/services/data-service';
import { TratamentoService } from 'src/app/tratamento/tratamento.service';
import { InstanciaDocumento } from 'src/app/model/instancia_documento';
declare var $: any;

@Component({
	selector: 'manutencao-dossie',
	templateUrl: './manutencao-dossie.component.html',
	styleUrls: ['./manutencao-dossie.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class ManutencaoDossieComponent extends AlertMessageService implements OnInit, AfterViewChecked, AfterContentChecked, AfterViewInit, CanComponenteDeactivate {
	@HostListener('window:beforeunload', ['$event'])
	public beforeUnloadWindow($event) {
		if (this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO) {
			var confirmationMessage = MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA;
			($event || window.event).returnValue = confirmationMessage; //Gecko + IE
			return confirmationMessage;
		}
		return (this.dossieProduto && !this.dossieProduto.situacao_atual) || (this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual != SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO);
	}
	@HostListener('window:unload', ['$event'])
	public HandleOnClose($event) {
		this.manutencaoDossiePresenter.retornaSituacaoQuandoForEmAlimentacaoOuEmComplementacao(this.dossieProduto);
	}
	constructor(
		private appService: ApplicationService,
		private dossieService: DossieService,
		private route: ActivatedRoute,
		private dialogService: DialogService,
		private loadService: LoaderService,
		private location: Location,
		private router: Router,
		private applicationService: ApplicationService,
		private clienteService: ConsultaClienteService,
		private mudancaSalvaService: MudancaSalvaService,
		private manutencaoDossiePresenter: ManutencaoDossieComponentPresenter,
		private tratamentoValidarPermissaoService: TratamentoValidarPermissaoService,
		private dataService: DataService,
		private tratamentoService: TratamentoService
	) {
		super();
	}

	situacaoCancelada = false;
	situacaoFinalizada = false;
	macroprocessoValues: any;
	processoValues: any;
	processo: any;
	macroprocesso;
	processoCompleto = new Processo();
	habilitaAbas: boolean = false;
	habilitarBtnRevogar: boolean = false;
	situacaoEmAlimentacao: boolean;
	situacaoComplementacao: boolean;
	habilitarBtnManipular: boolean = false;
	habilitarBtnEfetuarTratamento: boolean = false;
	justificativaRevogar: string = null;
	index: number;
	msgValidacao: any[];
	processoTipoPessoa: any;
	processoAtivo: number;
	processoAnterior: number;
	refDossie: any;
	msgOrientacao: string;
	situacaoRascunhoOuNovo: boolean = false;
	iniciando: boolean;
	entrouPrimeiraVez: boolean;
	cancelarDossie: boolean;
	fecharBrowser: boolean;
	ocultarOrientacao: boolean;
	/**
	 * Montando Lista de ArvoreVinculo
	 */
	listaVinculoArvore: Array<VinculoArvore>;
	listaVinculoArvoreChanged: EventEmitter<Array<VinculoArvore>> = new EventEmitter<Array<VinculoArvore>>();

	// listaDocumentos;
	// @Output() listaDocumentosChanged: EventEmitter<ArvoreDinamica[]> = new EventEmitter<ArvoreDinamica[]>();
	cliente: VinculoCliente = new VinculoCliente();
	usarAvaliacaoTomadorVigente;
	formularioSalvo: boolean = false;
	@Input() cpfCnpj;
	dossieProduto;
	dossieProdutoExcessao;
	habilitaBotoes = false;
	campoFormulario: CampoFormulario[] = [];
	// Novo formulário
	camposFormulario: RespostaCampoFormulario[] = [];
	respostaDossie: RespostaDossie[] = [];
	searchDone = false;
	userFound = false;
	hasClienteValido = false;
	exibeHistoricoDossieProduto = false;
	exibeVerificacao = false;
	clienteLista: VinculoCliente[];
	novosVinculos: VinculoCliente[];
	hitoriaDossieLista: historicoSituacoes[];
	@Input() clientePessoa: VinculoCliente;
	arvoreDinamica: ArvoreDinamica[] = [];
	exibeVincularPessoa = true;
	exibeVincularProduto = true;
	exibeVincularGarantia = true;
	alterValueRadioIdCheck = true;
	needApplyCssRadios = false;
	needApplyCssRadiosContent = false;
	radioIdsToCheck: string[] = [];
	garantias: VinculoGarantia[];
	produtoLista: VinculoProduto[];
	habilitaDadosCliente: boolean;
	enviar = false;
	idDossie: number = 0;
	@Output() dossieProdutoChanged: EventEmitter<DossieProduto> = new EventEmitter<DossieProduto>();
	@Output() formularioSalvoChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
	listaGed: DocumentoGED[] = [];
	@Output() listaGedChanged: EventEmitter<DocumentoGED[]> = new EventEmitter<DocumentoGED[]>();
	habilitaBotoesSalvar: boolean = false;
	itemObrigatorio: string;
	hasObrigatorio = false;
	habilitaAlteracao: boolean = true;
	naoExisteVinculoPessoaPrincipal: boolean = true;
	exibeMacroProcesso: boolean = false;
	listaProcessos: Processo[] = [];
	listaEtapas: any;
	primeiroVinculoPessoa: number = 0;
	listaDossie: VinculoCliente = new VinculoCliente;
	contadorProcessos: number = 0;
	processosSelecionados: string = "";
	nuEtapa: number = 0;
	tipo_pessoa: string = "";
	habilitaTratamento: boolean = false;
	hasAlteracaoTrata: boolean = false;
	indiceProcesso: number = 0;
	noEtapa: string = "";
	mensagemErroSalvar: string;
	listaTipoRelacionamento: any[] = [];

	tomador: VinculoCliente = new VinculoCliente();
	exibeLoadArvore: boolean;

	idDossieParams: any;
	opcaoParams: any;
	tipoParams: any;
	etapaParams: any;
	macroProcessoGeraDossieUriParams: ConjuntoMacroProcessoGeraDossieUriParams;
	processoParams: any;
	posicaoFase: number;

	nomeProcessoEscolhido: string;
	tipoProcessoEscolhido: string;
	@Input() idProcessoFase: number;
	@Output() idProcessoFaseChanged: EventEmitter<number> = new EventEmitter<number>();
	processoPatriarca: any;
	novoDossieProdutoCpfCnpj: string;
	@Output() listaProcessoChanged: EventEmitter<any> = new EventEmitter<any>();
	@Input() processoEscolhido: any;
	@Output() processoEscolhidoChanged: EventEmitter<any> = new EventEmitter<any>();
	clienteTitle: any;
	cpfTitle: string;
	cnpjTitle: string;
	cssFase: string;
	listaComboFase: any[];
	listaAbaVerificacao: VerificacaoDossie[];
	listaVerificacao: any[];
	dossieProdutoObj: DossieProdutoModel;
	eventCloseBrowser = false;
	tituloMsg = 'Orientação da Fase';

	// Declaração da variável que recebe os produtos vinculados para um determinado processo. 
	produtosVinculados: any[] = [];

	identificadorDossieFase: IdentificadorDossieFase = new IdentificadorDossieFase();

	@Output() hasAlteracaoTrataChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
	//objeto que guarda as validacoes feitas nos campos da aba-formulario
	formValidacao: NgForm;

	@Input() dossieCliente: boolean;

	changeProduto: boolean = false;
	changeGarantia: boolean = false;

	ngAfterViewInit(): void {
		GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
	}

	private init() {
		this.ocultarOrientacao = false;
		this.loadService.hide();
		this.justificativaRevogar = null;
		this.processoAtivo = undefined;
		this.mudancaSalvaService.setIsMudancaSalva(true);
		this.dossieSalvoDiferentedeFalse();
		this.setandoValoresParametrizados();
		this.clienteLista = [];
		this.listaVinculoArvore = [];
		this.situacaoDossieProdutoDirentedeNovo();
		this.situacaoDossieProdutoIgualANovo();
		this.cpfCnpjDiferenteDeNullOuUndefined();
		this.tipoParamsForDiferenteDeUndefined();
		this.verificarUnidadeAutorizada();
		this.loadService.hide();
	}

	ngOnInit() {
		this.init();
	}

	goBack() {
		this.loadService.show();
		this.dossieProduto;
		if (this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO) {
			this.loadService.hide();
			this.cancelarDossie = true;
			this.manutencaoDossiePresenter.voltarDossieProdutoEmAlimentacao(this.dossieProduto, 'dashboard');
			return;
		} else if (this.dossieProduto && this.validarTipoSituacaoAtualParaAcaoVoltar() && this.dossieProduto.situacao_atual != SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO) {
			let dossieProdutoeDT: DossieProduto = null;
			dossieProdutoeDT = this.montarDossieProdutoCasoExista();
			dossieProdutoeDT.retorno = true;
			this.validarAcaoVoltar(dossieProdutoeDT);
			return;
		} else if (this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.AGUARDANDO_COMPLEMENTACAO) {
			this.manutencaoDossiePresenter.retornaSituacaoQuandoForEmAlimentacaoOuEmComplementacao(this.dossieProduto);
		}
		this.retornoSemValidacao();
	}

	private validarTipoSituacaoAtualParaAcaoVoltar() {
		return this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_TRATAMENTO
			|| this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_ALIMENTACAO
			|| this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.PENDENTE_INFORMACAO;
	}


	private montarDossieProdutoCasoExista() {
		if (this.manutencaoDossiePresenter.verificaDossieProdutoJaExiste(this.dossieProduto)) {
			return GerenciadorDocumentoDossieProdutoEditado.criaDossieProdutoEdicao(this.campoFormulario, DOSSIE_PRODUTO.SALVAR_PARCIAL, this.dossieProduto ? this.dossieProduto.justificativa : undefined);
		}
		return GerenciadorDocumentosDossieProduto.criaDossieProduto(this.campoFormulario, DOSSIE_PRODUTO.SALVAR_PARCIAL);
	}

	private retornoSemValidacao() {
		let existCpfCnpj = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.cpfCnpjDossieProduto);
		existCpfCnpj = this.verificarExisteVinculoPrincilpalListaCliente(existCpfCnpj);

		if (undefined != existCpfCnpj && existCpfCnpj != "undefined") {
			this.loadService.hide();
			this.router.navigate(['principal', existCpfCnpj]);
			return;
		}
		this.loadService.hide();
		this.location.back();
	}
	/**
	 * Encontra na Lista o vinculo principla relativo a sequencia ou relacionado com principal true
	 * @param existCpfCnpj 
	 */
	private verificarExisteVinculoPrincilpalListaCliente(existCpfCnpj) {
		let clientePrincipalRelacionado = this.clienteLista.find(c => c.dossie_cliente_relacionado && c.principal);
		let clientePrincipalSequencial = this.clienteLista.find(c => c.sequencia_titularidade && c.sequencia_titularidade == 1 && c.principal);
		if (existCpfCnpj == "undefined" && (clientePrincipalRelacionado || clientePrincipalSequencial)) {
			existCpfCnpj = clientePrincipalRelacionado ? undefined == clientePrincipalRelacionado.cnpj ? clientePrincipalRelacionado.cpf : clientePrincipalRelacionado.cnpj : existCpfCnpj;
			existCpfCnpj = clientePrincipalSequencial ? undefined == clientePrincipalSequencial.cnpj ? clientePrincipalSequencial.cpf : clientePrincipalSequencial.cnpj : existCpfCnpj;
		}
		return existCpfCnpj;
	}

	private validarAcaoVoltar(dossieProduto: DossieProduto) {
		this.dossieService.atualizaDossieProduto(this.dossieProduto.id, dossieProduto).subscribe(response => {
			this.loadService.hide();
			let existCpfCnpj = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.cpfCnpjDossieProduto);
			this.caminhoParavoltar(existCpfCnpj);
		}, error => {
			this.loadService.hide();
			this.addMessageError(MESSAGE_ALERT_MENU.MSG_ERRO_SALVAR_DOSSIE);
			console.log(error);
			throw error;
		});
	}

	private caminhoParavoltar(existCpfCnpj: string) {
		this.verificarExisteVinculoPrincilpalListaCliente(existCpfCnpj);
		if (undefined != existCpfCnpj && existCpfCnpj != "undefined") {
			this.router.navigate(['principal', existCpfCnpj]);
			return;
		}
		this.location.back();
	}

	canDeactivate(nextState?: RouterStateSnapshot): boolean | Observable<boolean> {
		if (this.mudancaSalvaService.getIsMudancaSalva()) {
			if (this.dossieProduto && this.dossieProduto.situacao_atual && (this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.EM_ALIMENTACAO)) {
				this.cancelarDossie = this.cancelarDossie ? nextState.url == "dashboard" : false;
				if (this.entrouPrimeiraVez || this.cancelarDossie) {
					return true;
				}
				this.cancelarDossie = false;
				this.entrouPrimeiraVez = true;
				this.loadService.hide();
				this.manutencaoDossiePresenter.voltarDossieProdutoEmAlimentacao(this.dossieProduto, nextState.url);
				return false;
			}
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.cpfCnpjDossieProduto, undefined);
			return true;
		} else {
			return Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA);
		}
	}

	preencheFormulario() {
		this.populaCamposRespondidos();
		this.configuraCamposFormulario();
	}

	private populaCamposRespondidos() {
		if (this.dossieProduto !== undefined) {
			this.camposFormulario = this.dossieProduto.respostas_formulario;
			for (let lstResp of this.camposFormulario) {
				for (let lstProc of this.campoFormulario) {
					if (lstResp.id_campo_formulario === lstProc.id) {

						if ((lstResp.nome_campo == 'data_vigencia' && lstResp.resposta_aberta != undefined) || (lstResp.tipo_campo == TIPO_DOCUMENTO.DATE && lstResp.resposta_aberta != undefined)) {
							lstProc.resposta_aberta = new Date(lstResp.resposta_aberta);
							continue;
						}

						if (lstResp.tipo_campo == TIPO_DOCUMENTO.SELECT) {
							if (lstResp.opcoes_selecionadas != undefined) {
								if (lstResp && lstResp.opcoes_selecionadas && lstResp.opcoes_selecionadas.length > 0) {
									lstProc.resposta_aberta = lstResp.opcoes_selecionadas[0].valor_opcao;
								}
							}
							continue;
						}

						if (lstResp.tipo_campo == TIPO_DOCUMENTO.INPUT_RADIO) {
							if (lstResp && lstResp.opcoes_selecionadas && lstResp.opcoes_selecionadas.length > 0) {
								lstProc.opcoes_disponiveis.forEach(opc => {
									for (let opcaoRsp of lstResp.opcoes_selecionadas) {
										if (opc.valor_opcao === opcaoRsp.valor_opcao) {
											lstProc.resposta_aberta = opc.valor_opcao;
										}
									}
								});
							}
							continue;
						}

						if (lstResp.tipo_campo == TIPO_DOCUMENTO.INPUT_CHECKBOX) {
							if (lstResp && lstResp.opcoes_selecionadas && lstResp.opcoes_selecionadas.length > 0) {
								lstProc.opcoes_selecionadas = lstResp.opcoes_selecionadas.length > 0 ? lstResp.opcoes_selecionadas : null;
								lstProc.string_selecionadas = [];
								lstProc.opcoes_disponiveis.forEach(opc => {
									for (let opcaoRsp of lstResp.opcoes_selecionadas) {
										if (opc.valor_opcao === opcaoRsp.valor_opcao) {
											lstProc.string_selecionadas.push(opc.valor_opcao);
											opc.ativo = true;
										}
									}
								});
							}
							continue;
						}

						if(lstResp.tipo_campo == TIPO_DOCUMENTO.MONETARIO || lstResp.tipo_campo == TIPO_DOCUMENTO.DECIMAL){
							let valorMonetario: number = lstResp.resposta_aberta != '' && lstResp.resposta_aberta != undefined ? Number.parseFloat(lstResp.resposta_aberta) : null;
							lstProc.resposta_aberta = valorMonetario;
							continue;
						}

						lstProc.resposta_aberta = lstResp.resposta_aberta;
					}
				}
			}
		}
	}

	private configuraCamposFormulario() {
		if (this.campoFormulario != null) {
			this.needApplyCssRadios = true;
			let codAvalTom = '';
			let dataVigenciaAtiva = '';
			this.applyCSSRadios();
			this.hasClienteValido = true;
			for (let item of this.campoFormulario) {
				this.configuraValoresDefault(item, codAvalTom, dataVigenciaAtiva);
			}
		}
	}

	private configuraValoresDefault(item: CampoFormulario, codAvalTom, dataVigenciaAtiva) {
		if (item.nome_campo === 'codigo_avaliacao_tomador') {
			codAvalTom = item.resposta_aberta;
		}
		if (item.nome_campo === 'data_vigencia') {
			if (item.resposta_aberta != null && UtilsCliente.validarDataMaior(UtilsCliente.getDataCompleta('N'),
				UtilsCliente.converteData(item.resposta_aberta))) {
				dataVigenciaAtiva = item.resposta_aberta;
			}
		}
	}

	private preencheProcessoRetorno(processoAtivo: any) {
		this.montarProcessoDossieAtivo();
		if (processoAtivo.id == this.dossieProduto.processo_fase.id) {
			this.montarProcessoAtivo();
		} else {
			this.criaVinculoArvoreProcessoFase(processoAtivo);
		}
	}

	/**
	 * Quando o Processo Dossiê é consultar ou manter chama aqui
	 */
	private montarProcessoDossieAtivo() {
		let vinculoProcesso: VinculoProcesso = this.dossieProduto.processo_dossie;
		if(this.dossieProduto && this.dossieProduto.processo_dossie) {
			this.verificarSeExisteInstanciaDocumentoElementoConteudo(!this.dossieProduto.processo_dossie.instancias_documento, this.processo.elementos_conteudo, vinculoProcesso);
		}
		let vinculoArvoreProcesso = new VinculoArvoreProcesso();
		vinculoArvoreProcesso.id = vinculoProcesso.id;
		vinculoArvoreProcesso.nome = vinculoProcesso.nome;
		vinculoArvoreProcesso.vinculoProcesso = vinculoProcesso;
		vinculoArvoreProcesso.alterandoVinculo = false;
		this.listaVinculoArvore.push(vinculoArvoreProcesso);
	}

	/**
	 * 
	 * @param naoExiste //Pela regra so vai retorna existe se estiver voltando uma fase.
	 * @param listaDocumento //lista no patricarca relativo a elemento conteudo ou tipo de documento conforme processo.
	 * @param vinculo //vinculo a qual vai ser inserido a lista de instancia caso exista.
	 */
	private verificarSeExisteInstanciaDocumentoElementoConteudo(naoExiste: boolean, listaDocumento: any[], vinculo: any) {
		let listaInstancia: Array<InstanciaDocumento> = new Array<InstanciaDocumento>();
		if (this.processo && listaDocumento.length > 0 && this.dossieProduto.instancias_documento && naoExiste) {
			listaDocumento.forEach(elemento => {
				this.dossieProduto.instancias_documento.forEach(instancia => {
					if(instancia.id_elemento_conteudo && elemento.identificador_elemento == instancia.id_elemento_conteudo) {
						listaInstancia.push(instancia);
					}
				});
			});
		}
		if(listaInstancia.length > 0) {
			vinculo.instancias_documento = listaInstancia;
		}
	}

	/**
	 * Quando o  Dossiê esta em faze de consultar ou manter chama aqui
	 */
	private montarProcessoAtivo() {
		let vinculoProcessoFase: VinculoProcesso = this.dossieProduto.processo_fase;
		let faseApercorrer = this.montarProcessoFaseAtual();
		if(this.dossieProduto && this.dossieProduto.processo_dossie) {
			this.verificarSeExisteInstanciaDocumentoElementoConteudo(!this.dossieProduto.processo_fase.instancias_documento, faseApercorrer.elementos_conteudo, vinculoProcessoFase);		
		}
		let vinculoArvoreProcessoFase = new VinculoArvoreProcesso();
		vinculoArvoreProcessoFase.id = this.dossieProduto.processo_dossie.id;
		vinculoArvoreProcessoFase.nome = vinculoProcessoFase.nome;
		vinculoArvoreProcessoFase.idProcessoFase = vinculoProcessoFase.id;
		vinculoArvoreProcessoFase.vinculoProcesso = vinculoProcessoFase;
		vinculoArvoreProcessoFase.alterandoVinculo = false;
		this.listaVinculoArvore.push(vinculoArvoreProcessoFase);
	}

	criaNovoVinculoPessoa() {
		if (this.idDossieParams && this.idDossieParams != PARAMETRO_DEFINI_CONSULTA_MANUTENCAO_DOSSIE.DOSSIE_PRODUTO) {
			this.clienteService.getClienteById(this.idDossieParams).subscribe(response => {
				this.naoExisteVinculoPessoaPrincipal = false;
				/**
				 * Definindo que a lista ira ser de Vinculo Cliente para o modal atual
				//  */
				let vinculoArvoreCliente = new VinculoArvoreCliente();
				vinculoArvoreCliente.vinculoCliente = new VinculoCliente();
				vinculoArvoreCliente.id = this.processoEscolhido.id;
				vinculoArvoreCliente.nome = this.processoEscolhido.nome;
				vinculoArvoreCliente.alterandoVinculo = false;

				vinculoArvoreCliente.vinculoCliente = response;
				this.novoDossieProdutoCpfCnpj = vinculoArvoreCliente.vinculoCliente.cnpj ? vinculoArvoreCliente.vinculoCliente.cpf : vinculoArvoreCliente.vinculoCliente.cnpj;
				let tipoRelacionamentoDossie = this.tipoRelacionamentoDossie();
				vinculoArvoreCliente.vinculoCliente.tipo_relacionamento = tipoRelacionamentoDossie ? tipoRelacionamentoDossie.find(x => x.principal) : null;

				if (vinculoArvoreCliente.vinculoCliente.tipo_relacionamento) {
					vinculoArvoreCliente.vinculoCliente.ic_tipo_relacionamento = vinculoArvoreCliente.vinculoCliente.tipo_relacionamento.nome;
				}

				vinculoArvoreCliente.vinculoCliente.principal = true;
				let indicaSequencia;
				let indica_relacionado;

				if(tipoRelacionamentoDossie && tipoRelacionamentoDossie.length > 0){
					indicaSequencia = tipoRelacionamentoDossie.find(x => x.principal == true).indica_sequencia;
					indica_relacionado = tipoRelacionamentoDossie.find(x => x.principal == true).indica_relacionado;
				}
				 
				if (indicaSequencia) {
					vinculoArvoreCliente.vinculoCliente.sequencia_titularidade = 1;
				}

				vinculoArvoreCliente.vinculoCliente.indica_sequencia = indicaSequencia;
				vinculoArvoreCliente.vinculoCliente.indica_relacionado = indica_relacionado;
				vinculoArvoreCliente.vinculoCliente.documentos = response.documentos;
				vinculoArvoreCliente.vinculoCliente.dossies_produto = [];
				vinculoArvoreCliente.vinculoCliente.produtos_habilitados = [];
				vinculoArvoreCliente.vinculoCliente.relacionado = null;
				/**
				 * Adicionando na lista
				 */
				this.clienteTitle = vinculoArvoreCliente.vinculoCliente;
				this.clienteLista.push(vinculoArvoreCliente.vinculoCliente);

				this.listaVinculoArvore.push(vinculoArvoreCliente);
				/**
				 * Alterando a memoria para ser observado
				 */
				this.clienteLista = Object.assign([], this.clienteLista);
				this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
			},
				() => {
					this.loadService.hide();
				});
			this.exibeLoadArvore = true;
		}
	}

	private carregarWizard(idProcessoDossie: number, fase: number) {
		if (this.opcaoParams != SITUACAO_DOSSIE_PRODUTO.TRATAMENTO) {
			this.loadService.show();
			this.montarWizard(idProcessoDossie);
			this.informacoesBasicarParaCriarEditarVisualizarDossieProduto(this.refDossie);
			this.montarDossieNovo(fase);
			this.montarDossieExistente(fase);
			this.carregarObjetoFormulário();
			// if (fase) {
			this.loadService.hide();
			// }
			this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
		}
	}

	private montarWizard(idProcessoDossie: number) {
		if (!this.refDossie) {
			if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.NOVO) {
				this.refDossie = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.macroProccesso + UNDESCOR + idProcessoDossie));
			} else {
				this.refDossie = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + idProcessoDossie));
			}
		}
	}

	private montarDossieExistente(fase) {
		if (this.opcaoParams != SITUACAO_DOSSIE_PRODUTO.NOVO) {
			this.naoExisteVinculoPessoaPrincipal = false;
			this.addIdDossieProdutoNaTela(this.dossieProduto.id);
			this.idProcessoFase = null == fase ? this.dossieProduto.processo_fase.id : fase;
			this.cssFase = this.calcularLarguraWizardFporFase();
			let processoFase = this.listaEtapas.find(x => x.id == this.idProcessoFase);
			this.msgOrientacao = processoFase ? processoFase.orientacao_usuario : this.msgOrientacao;
			let encontrou = false;
			this.encontrandoFaseAnteriorDaAtiva(encontrou);
			if (undefined == this.processoAtivo) {
				this.processoAtivo = this.idProcessoFase;
			}
			this.identificadorDossieFase.idDossie = this.dossieProduto.processo_dossie.id;
			this.identificadorDossieFase.idFase = this.processoAtivo;
			this.encontraPosicaoListaDaFase();
			this.definirTomadorQuandoConsutlaOuManter();
			this.processoEscolhido = this.processo;
			this.listaTipoRelacionamento = this.processoEscolhido.tipos_relacionamento;
			this.montarProdutoVinculadosProcessoFaseeDossie(processoFase);
			this.tipoProcessoEscolhido = this.processoEscolhido.nome;
			this.preencheProcessoRetorno(processoFase);
			this.preencherRetornosVinculos();
			this.verificarPermissaoDeEfetuaTratamento();
		}
	}

	private addIdDossieProdutoNaTela(id: any) {
		$(".labelId").remove();
		$("#titiloTela .box-title").append(" <span class='labelId'> : " + id + "</span>");
	}

	private encontrandoFaseAnteriorDaAtiva(encontrou: boolean) {
		this.listaEtapas.forEach((etapa, idx) => {
			if (!encontrou) {
				encontrou = etapa.id == this.idProcessoFase;
				this.definirParametroParaAtivarSetaAnteriorDoWizard(encontrou, etapa, idx);
			}
		});
	}

	/**
	 * A parti do momento que encontrou receber true fica verificando se nao existe fase depois caso existe fica limitado a sempre manter o processo anterior
	 * @param encontrou 
	 * @param etapa 
	 * @param idx 
	 */
	private definirParametroParaAtivarSetaAnteriorDoWizard(encontrou: boolean, etapa: any, idx: any) {
		if (!encontrou) {
			this.processoAnterior = etapa.id;
		} else {
			this.EncontrouAFaseAtivaVerificandoAcoesParaManterAfaseAnteriorAtiva(idx);
		}
	}
	/**
	 * faz a Verificacao se a idProcessoFase e diferente do processoAnterior se se o index e igual ele limpa se não manter o Anterior
	 * @param idx 
	 */
	private EncontrouAFaseAtivaVerificandoAcoesParaManterAfaseAnteriorAtiva(idx: any) {
		if (this.idProcessoFase != this.processoAnterior) {
			if (this.listaEtapas && this.listaEtapas.length == idx || idx == 0) {
				//quando e o ultimo
				this.processoAnterior = null;
			}
			else {
				this.processoAnterior = this.processoAnterior;
			}
		} else {
			//quando e o primeiro
			this.processoAnterior = null;
		}
	}

	private preencherRetornosVinculos() {
		this.retornoCliente();
		this.retornoGarantia();
		this.retornoProduto();
		this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
		this.listaVinculoArvoreChanged.emit(this.listaVinculoArvore);
	}

	private encontraPosicaoListaDaFase() {
		this.listaEtapas.forEach((fase, idx) => {
			if (fase.id == this.processoAtivo) {
				this.posicaoFase = idx;
				return;
			}
		});
	}

	private definirTomadorQuandoConsutlaOuManter() {
		this.dossieProduto.vinculos_pessoas.forEach(cliente => {
			if (cliente.tipo_relacionamento === 'TOMADOR_PRIMEIRO_TITULAR') {
				this.clienteTitle = cliente.dossie_cliente;
			}
		});
	}

	private retornoGarantia() {
		if (this.dossieProduto && this.dossieProduto.garantias_informadas && this.dossieProduto.garantias_informadas.length > 0) {
			this.garantias = [];
			for (const item of this.dossieProduto.garantias_informadas) {
				let garantia: VinculoGarantia = Object.assign({}, item);
				garantia.nome = item.nome_garantia;
				garantia.produto_operacao = item.produto_operacao;
				garantia.operacao = item.produto_operacao;
				garantia.produto_modalidade = item.produto_modalidade;
				garantia.produto_nome = item.produto_nome;
				garantia.valor_garantia = item.valor;
				garantia.clientes_avalistas = [];
				this.montarApresentacaoRetornoGarantia(item, garantia);
				this.garantias.push(garantia);
				let vinculoArvoreGarantia = new VinculoArvoreGarantia();
				vinculoArvoreGarantia.alterandoVinculo = false;
				vinculoArvoreGarantia.id = this.dossieProduto.processo_dossie.id;
				vinculoArvoreGarantia.idProcessoFase = this.identificadorDossieFase.idFase;
				vinculoArvoreGarantia.nome = garantia.nome;
				vinculoArvoreGarantia.vinculoGarantia = garantia;
				this.listaVinculoArvore.push(vinculoArvoreGarantia);
			}
		}
	}

	private retornoProduto() {
		if (this.dossieProduto && this.dossieProduto.produtos_contratados && this.dossieProduto.produtos_contratados.length > 0) {
			this.produtoLista = [];
			for (const item of this.dossieProduto.produtos_contratados) {
				let vinculoProduto: VinculoProduto = Object.assign({}, item);
				vinculoProduto.id = item.id;
				vinculoProduto.codigo_operacao = item.codigo_operacao;
				vinculoProduto.codigo_modalidade = item.codigo_modalidade;
				// this.verificarSeExisteInstanciaDocumentoElementoConteudo(!this.dossieProduto.processo_dossie.instancias_documento, faseApercorrer.elementos_conteudo, vinculoProduto);	
				this.produtoLista.push(vinculoProduto);
				let vinculoArvoreProduto = new VinculoArvoreProduto();
				vinculoArvoreProduto.alterandoVinculo = false;
				vinculoArvoreProduto.id = this.dossieProduto.processo_dossie.id;
				vinculoArvoreProduto.nome = vinculoProduto.nome;
				vinculoArvoreProduto.vinculoProduto = vinculoProduto;
				this.listaVinculoArvore.push(vinculoArvoreProduto);
			}
		}
	}

	private retornoCliente() {
		if (this.dossieProduto && this.dossieProduto.vinculos_pessoas && this.dossieProduto.vinculos_pessoas.length > 0) {
			let tipoRelacionamentoDossie = this.tipoRelacionamentoDossie();
			for (let cl = 0; cl < this.dossieProduto.vinculos_pessoas.length; cl++) {
				let vinculoCliente: VinculoCliente = Object.assign({}, this.dossieProduto.vinculos_pessoas[cl].dossie_cliente);
				vinculoCliente.tipo_relacionamento = this.dossieProduto.vinculos_pessoas[cl].tipo_relacionamento;
				vinculoCliente.vinculoEditar = true;
				vinculoCliente.respostas_formulario = [];
				this.dossieProduto.respostas_formulario.forEach(resposta => {
					if (resposta.id_vinculo_pessoa) {
						vinculoCliente.respostas_formulario.push(resposta);
					}
				});
				vinculoCliente.ic_tipo_relacionamento = vinculoCliente.tipo_relacionamento.nome;
				vinculoCliente.tipo_pessoa = this.dossieProduto.vinculos_pessoas[cl].dossie_cliente.tipo_pessoa;
				vinculoCliente.instancias_documento = this.dossieProduto.vinculos_pessoas[cl].instancias_documento;
				vinculoCliente.relacionado = undefined != this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado ? undefined == this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado.cnpj ? this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado.cpf : this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado.cnpj : "";
				vinculoCliente.dossie_cliente_relacionado = undefined != this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado ? this.dossieProduto.vinculos_pessoas[cl].dossie_cliente_relacionado.id : undefined;
				vinculoCliente.principal = tipoRelacionamentoDossie.some(x => x.principal == true && x.id == vinculoCliente.tipo_relacionamento.id);
				let indicaSequencia = tipoRelacionamentoDossie.some(x => x.id == vinculoCliente.tipo_relacionamento.id && x.indica_sequencia);
				vinculoCliente.sequencia_titularidade = !this.dossieProduto.vinculos_pessoas[cl].sequencia_titularidade && indicaSequencia ? 1 : this.dossieProduto.vinculos_pessoas[cl].sequencia_titularidade;
				if (vinculoCliente.principal) {
					if ((indicaSequencia && vinculoCliente.sequencia_titularidade == 1) || (!indicaSequencia)) {
						this.clienteTitle = vinculoCliente;
					}
				}
				this.clienteLista.push(vinculoCliente);
				this.exibeLoadArvore = true;
				let vinculoArvoreCliente = new VinculoArvoreCliente();
				vinculoArvoreCliente.alterandoVinculo = false;
				vinculoArvoreCliente.id = this.dossieProduto.processo_dossie.id;
				vinculoArvoreCliente.nome = vinculoCliente.tipo_relacionamento.nome;
				vinculoArvoreCliente.vinculoCliente = vinculoCliente;
				this.listaVinculoArvore.push(vinculoArvoreCliente);
			}
		}
	}

	private tipoRelacionamentoDossie() {
		return JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.processoEscolhido.id)).tipos_relacionamento;
	}

	private montarApresentacaoRetornoGarantia(item: any, garantia: VinculoGarantia) {
		item.dossies_cliente.forEach((pessoa, idx) => {

			let campoCpfCnpj = undefined == pessoa.cpf ? pessoa.cnpj : pessoa.cpf;
			let cpfCnpjFormatado = campoCpfCnpj != undefined && campoCpfCnpj != '' ? Utils.masKcpfCnpj(campoCpfCnpj) : null;

			if (idx == 0) {
				garantia.relacionado = cpfCnpjFormatado;
				garantia.clientes_avalistas.push(pessoa.id);
			}
			else {
				garantia.relacionado = garantia.relacionado + ", " + cpfCnpjFormatado;
				garantia.clientes_avalistas.push(pessoa.id);
			}
		});
	}

	/**
	 * Carregar Objetos Formulario
	 */
	private carregarObjetoFormulário() {
		this.campoFormulario = this.listaEtapas.find(x => x.id == this.idProcessoFase).campos_formulario;
		this.preencheFormulario();
	}

	/**
	 * Ações nescessaria para montar novo Dossiê Produto
	 * @param fase quando carrega a primeira vez esta null 
	 */
	private montarDossieNovo(fase: number) {
		if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.NOVO) {
			this.processoPatriarca = this.processo;
			let processo = this.processoParams.split(',');
			this.processoPatriarca.processos_filho.forEach(dossie => {
				this.montarListaTipoDocumento(dossie, processo);
			});
			this.nomeProcessoEscolhido = this.processo.nome;
			this.definirNomeTipoProcessoEscolhido(processo[1]);
			this.listaEtapas = this.listaEtapas.find(x => x.id == processo[1]).processos_filho;
			this.cssFase = this.calcularLarguraWizardFporFase();
			this.idProcessoFase = null == fase ? this.listaEtapas.find(x => x.sequencia == 1).id : fase;
			let encontrou = false;
			this.encontrandoFaseAnteriorDaAtiva(encontrou);
			if (undefined == this.processoAtivo) {
				this.processoAtivo = this.idProcessoFase;
			}
			let processoFase = this.listaEtapas.find(x => x.id == this.idProcessoFase);
			this.msgOrientacao = processoFase ? processoFase.orientacao_usuario : this.msgOrientacao;
			this.montarProdutoVinculadosProcessoFaseeDossie(processoFase);
			this.identificadorDossieFase.idDossie = +processo[1];
			this.identificadorDossieFase.idFase = this.idProcessoFase;
			this.criaNovoVinculoPessoa();
			this.criaVinculoArvoreProcessoDossie();
			this.criaVinculoArvoreProcessoFase(processoFase);
			this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
			this.habilitaAbas = true;
		}
	}

	private calcularLarguraWizardFporFase(): string {
		return "" + (100 / this.listaEtapas.length) + "%";
	}

	/**
	 * Informações nescessaria para montar Dossiê Produto nas ações NOVO/MANTER/CONSULTAR
	 * @param response 
	 */
	private informacoesBasicarParaCriarEditarVisualizarDossieProduto(response: any) {
		this.processo = response;
		this.defineExibicaoProcessoDossieEscolhido();
		this.listaEtapas = this.processo ? this.processo.processos_filho : [];
		this.listaTipoRelacionamento = [];
	}

	private montarListaTipoDocumento(dossie: any, processo: any) {
		if (dossie.id == +processo[1]) {
			this.listaTipoRelacionamento = dossie.tipos_relacionamento;
		}
	}

	private montarProdutoVinculadosProcessoFaseeDossie(processoFase: any) {
		// Carrega o objeto produtos vinculados
		this.produtosVinculados = [];
		// Processo ESCOLHIDO
		if (undefined != this.processoEscolhido && this.processoEscolhido.produtos_vinculados && this.processoEscolhido.produtos_vinculados.length > 0) {
			for (var i = 0; i < this.processoEscolhido.produtos_vinculados.length; i++) {
				this.processoEscolhido.produtos_vinculados[i].alterandoVinculo = false;
				this.produtosVinculados.push(this.processoEscolhido.produtos_vinculados[i]);
			}
		}
	}

	private definirNomeTipoProcessoEscolhido(processo: any) {
		this.processoEscolhido = this.extraiProcessoDossieEscolhido(processo);
		this.tipoProcessoEscolhido = this.processoEscolhido.nome;
	}

	/**
	 * Cria objeto de vinculo arvore processo para ser add na lista de vinculos arvore
	 */
	private criaVinculoArvoreProcessoDossie() {
		let vinculoProcesso: VinculoProcesso = new VinculoProcesso();
		let vinculoArvoreProcesso: VinculoArvoreProcesso = new VinculoArvoreProcesso();
		vinculoArvoreProcesso.alterandoVinculo = false;
		vinculoArvoreProcesso.vinculoProcesso = vinculoProcesso;
		vinculoArvoreProcesso.id = this.processoEscolhido.id;
		vinculoArvoreProcesso.nome = this.processoEscolhido.nome;
		this.listaVinculoArvore.push(vinculoArvoreProcesso);
	}

	/**
	 * Cria objeto de vinculo arvore processo fase para ser add na lista de vinculos arvore
	 * @param processoFase Processo Fase atual
	 */
	private criaVinculoArvoreProcessoFase(processoFase: any) {
		let vinculoProcesso: VinculoProcesso = new VinculoProcesso();
		let vinculoArvoreProcesso: VinculoArvoreProcesso = new VinculoArvoreProcesso();
		let faseApercorrer = this.montarProcessoFaseAtual();
		if(this.dossieProduto && this.dossieProduto.processo_dossie) {
			this.verificarSeExisteInstanciaDocumentoElementoConteudo(!this.dossieProduto.processo_fase.instancias_documento, faseApercorrer.elementos_conteudo, vinculoProcesso);		
		}
		vinculoArvoreProcesso.vinculoProcesso = vinculoProcesso;
		vinculoArvoreProcesso.id = this.processoEscolhido.id;
		vinculoArvoreProcesso.idProcessoFase = processoFase.id;
		vinculoArvoreProcesso.nome = processoFase.nome;
		vinculoArvoreProcesso.alterandoVinculo = false;
		this.listaVinculoArvore.push(vinculoArvoreProcesso);
	}

	/**
	 * Buscar pelo id da fase o processo no patriarca
	 */
	private montarProcessoFaseAtual() {
		let faseApercorrer;
		this.processo.processos_filho.forEach(processo => {
			if (this.idProcessoFase == processo.id) {
				faseApercorrer = processo;
			}
		});
		return faseApercorrer;
	}

	private defineExibicaoProcessoDossieEscolhido(): void {
		if (undefined == this.processoEscolhido) {
			this.configuraNomeProcessoEscolhido();
			this.tipoProcessoEscolhido = this.processo.nome;
		}
	}

	private configuraNomeProcessoEscolhido() {
		this.nomeProcessoEscolhido = this.processoPatriarca ? this.processoPatriarca.nome : this.refDossie.nome;
	}

	private extraiProcessoDossieEscolhido(param) {
		return this.processo.processos_filho.find(x => x.id == param);
	}

	/**
	 * Se o a variavel dossieSalvo for diferente de FALSE entrara no if
	 * @param dossieSalvo 
	 */
	private dossieSalvoDiferentedeFalse() {
		let dossieSalvo = UtilsManutencao.dossieSalvo(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo));
		if (dossieSalvo) {
			this.ocultarOrientacao = UtilsManutencao.dossieSalvo(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo)) ? true : false;
			this.addMessageSuccess(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo));
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, false);
		}
	}

	private tipoParamsForDiferenteDeUndefined() {
		if (this.tipoParams !== undefined) {
			this.habilitaDadosCliente = false;
			this.habilitaAlteracao = false;
			this.habilitaBotoesSalvar = false;
		}
	}

	/**
	 * Ações nescessarias quando cpfCnpj existir
	 */
	private cpfCnpjDiferenteDeNullOuUndefined() {
		if (this.cpfCnpj !== null && this.cpfCnpj !== undefined) {
			this.cliente.cnpj = this.cpfCnpj;
		}
	}

	/**
	 * Quando o parametro opcaoParams for igual a NOVO
	 */
	private situacaoDossieProdutoIgualANovo() {
		if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.NOVO) {
			this.situacaoRascunhoOuNovo = true;
			let proc = this.processoParams.split(',');
			this.processoTipoPessoa = proc[0];
			this.habilitarDesabilitarBtnsAcoes(true);
			this.carregarWizard(this.processoTipoPessoa, null);
		}
	}

	/**
	 * Quando o atributo opcaoParams for igual a: 
	 * Situação == CONSULTAR || 
	 * Situação == MANTER  || 
	 * Situação == TRATAMENTO || 
	 */
	private situacaoDossieProdutoDirentedeNovo() {
		this.listaVinculoArvore = [];
		//this.loadService.show();
		if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.MANTER) {
			this.dossieService.postDossieProduto(this.idDossieParams).subscribe(response => {
				if (response.alteracao && response.situacao_atual) {
					this.montarDossieProdutoConsultado(response);
				}
				else {
					this.addMessageWarning(MESSAGE_ALERT_MENU.MSG_DOSSIE_IMPOSSIBILITADO);
					this.montarDossieProdutoConsultado(response);
					this.habilitarBtnManipular = false;
				}
			},
				() => {
					this.loadService.hide();
				});
		} else if (this.idDossieParams != PARAMETRO_DEFINI_CONSULTA_MANUTENCAO_DOSSIE.DOSSIE_PRODUTO && this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.CONSULTAR || this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.TRATAMENTO) {
			this.dossieService.getDossieProduto(this.idDossieParams).subscribe(response => {
				this.montarDossieProdutoConsultado(response);
				this.mudancaSalvaService.loadDossieProdutoAdmnistrarDossie.emit(this.mudancaSalvaService.getLimitMaxAdministrarDossie());
			},
				() => {
					this.loadService.hide();
				});
		} else if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.COMPLEMENTACAO) {
			this.situacaoComplementacao = true;
			this.dossieService.postDossieComplementacao(this.idDossieParams).subscribe(response => {
				if (response.alteracao && response.situacao_atual) {
					this.montarDossieProdutoConsultado(response);
				} else {
					this.addMessageWarning(MESSAGE_ALERT_MENU.MSG_DOSSIE_IMPOSSIBILITADO);
					this.montarDossieProdutoConsultado(response);
					this.habilitarBtnManipular = false;
				}
			},
				() => {
					this.loadService.hide();
				});
		}
	}

	private montarDossieProdutoConsultado(response: any) {
		this.dossieProduto = response;
		this.habilitarBtnManipular = UtilsManutencao.tipoSituacao(this.dossieProduto.alteracao, this.dossieProduto.situacao_atual);
		this.situacaoRascunhoOuNovo = (this.dossieProduto.alteracao && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.RASCUNHO);
		this.dossieProduto.situacaoComplementacao = (this.dossieProduto.alteracao && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_COMPLEMENTACAO);
		this.situacaoEmAlimentacao = response.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO;
		this.habilitarDesabilitarBtnsAcoes(this.dossieProduto.alteracao);
		this.habilitaAbas = true;
		this.exibeHistoricoDossieProduto = true;
		this.habilitarBtnRevogar = response.situacao_atual == SITUACAO_DOSSIE_BANCO.RASCUNHO || response.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO;
		this.hitoriaDossieLista = this.ordernarHistorico(response.historico_situacoes); 
		this.exibeVerificacao = response.verificacoes && response.verificacoes.length > 0;
		this.listaVerificacao = response.verificacoes;
		this.situacaoCancelada = response.situacao_atual === SITUACAO_DOSSIE_ATUAL.CANCELADO;
		this.situacaoFinalizada = response.situacao_atual.indexOf(SITUACAO_DOSSIE_ATUAL.FIANLIZADO) > -1 ? true : false;
		
		
		this.acaoNescessariaQuandoOpcaoParamsForTratamento();
	}
	ordernarHistorico(historico_situacoes: any): historicoSituacoes[] {
		historico_situacoes.sort((val1, val2)=> {
			return new Date(val1.data_hora_inclusao).getTime()  - new Date(val2.data_hora_inclusao).getTime() 
		});
		return historico_situacoes;
	}

	/**
	 * Quando opcaoParams for igual a Tratamento
	 */
	private acaoNescessariaQuandoOpcaoParamsForTratamento() {
		if (this.opcaoParams != SITUACAO_DOSSIE_PRODUTO.TRATAMENTO) {
			this.montarProcessoPatriacaConformeDossieProduto(null);
			this.carregarWizard(this.dossieProduto.processo_dossie.id, null);
			this.hasClienteValido = true;
		}
	}

	/**
	 * Busca no UtilsManutencao o método responsavel por setar as variaveis de Parametros
	 */
	private setandoValoresParametrizados() {
		let parametrosManutencaoDossie = UtilsManutencao.setarValorParametros(this.route, this.loadService);
		this.idDossieParams = parametrosManutencaoDossie.idDossieParams;
		this.opcaoParams = parametrosManutencaoDossie.opcaoParams;
		this.processoParams = parametrosManutencaoDossie.processoParams;
		this.etapaParams = parametrosManutencaoDossie.etapaParams;
		this.macroProcessoGeraDossieUriParams = parametrosManutencaoDossie.macroProcessoGeraDossieUriParams;
		this.tipoParams = parametrosManutencaoDossie.tipoParams;
	}

	/**
	 * monta uma lista para ser usada na aba verificação
	 * @param response 
	 */
	private listaComboFaseAbaVerificacao(dossie: any) {
		this.listaComboFase = [];
		dossie.processos_filho.forEach(fase => {
			let obj = {
				"id": fase.id,
				"nome": fase.nome
			};
			this.listaComboFase.push(obj);
		});
	}

	/**
	 * Com base na opcaoParam defini se e consulta ou salvar
	 */

	private habilitarDesabilitarBtnsAcoes(habilitar: boolean) {
		this.quandoForConsultar();
		this.quandoForManterOuNovo(habilitar);
	}

	/**
	 * ação quando poder manter ou um novo dossie produto
	 */
	private quandoForManterOuNovo(habilitar: boolean) {
		if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.MANTER || this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.NOVO || this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.COMPLEMENTACAO) {
			this.habilitaBotoesSalvar = habilitar;
			this.habilitaAlteracao = habilitar;
			this.situacaoRascunhoOuNovo = habilitar;
			this.situacaoEmAlimentacao = this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO;
			this.habilitarBtnRevogar = this.dossieProduto && this.dossieProduto.situacao_atual && (this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.RASCUNHO || this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO);

		}
	}

	/**
	 * ação quando for apenas uma consulta ao dossie produto
	 */
	private quandoForConsultar() {
		if (this.opcaoParams == SITUACAO_DOSSIE_PRODUTO.CONSULTAR) {
			this.habilitaAlteracao = false;
			this.habilitaBotoesSalvar = false;
		}
	}

	private montarProcessoPatriacaConformeDossieProduto(processoTipoPessoa: any) {
		let patriarcaCompleto = this.buscarProcessoPatriarca();
		let encontradoDossie = false;
		encontradoDossie = this.encontrarProcessoTipoDossie(patriarcaCompleto, encontradoDossie, processoTipoPessoa);
	}

	private encontrarProcessoTipoDossie(patriarcaCompleto: any, encontradoDossie: boolean, processoTipoPessoa: any) {
		patriarcaCompleto.forEach(tipoDossie => {
			if (!encontradoDossie) {
				for (let dossie of tipoDossie.processos_filho) {
					if (this.dossieProduto == undefined && tipoDossie.id == processoTipoPessoa) {
						this.processoPatriarca = tipoDossie;
						encontradoDossie = true;
						this.listaComboFaseAbaVerificacao(dossie);
						break;
					}
					else if (dossie.id == this.dossieProduto.processo_dossie.id) {
						this.processoPatriarca = tipoDossie;
						encontradoDossie = true;
						this.listaComboFaseAbaVerificacao(dossie);
						break;
					}
				}
			}
		});
		return encontradoDossie;
	}

	private buscarProcessoPatriarca() {
		let patriarcaCompleto = undefined;
		while (undefined == patriarcaCompleto) {
			patriarcaCompleto = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoPatriarca));
		}
		return patriarcaCompleto;
	}

	getUnidadeUser() {
		let userSSO = JSON.parse(this.applicationService.getUserSSO());
		return userSSO['co-unidade'];
	}

	getMatriculaUser() {
		let userSSO = JSON.parse(this.applicationService.getUserSSO());
		let usuario;
		if (userSSO != undefined) {
			usuario = userSSO.preferred_username;
		} else {
			usuario = "c000001"
		}
		return usuario;
	}

	ngAfterContentChecked() {
		this.montarChekarRadios();
		if ((this.clienteLista && this.clienteLista.length > 0 && !this.iniciando) || (this.idDossieParams == PARAMETRO_DEFINI_CONSULTA_MANUTENCAO_DOSSIE.DOSSIE_PRODUTO && !this.iniciando)) {
			this.loadService.hide();
			this.iniciando = true;
		}
	}

	ngAfterViewChecked(): void {
		this.checkarRadios();
	}

	private checkarRadios() {
		if (this.needApplyCssRadios) {
			this.applyCSSRadios();
			this.needApplyCssRadios = false;
			this.montarChekarRadios();
		}
	}

	private montarChekarRadios() {
		if (this.radioIdsToCheck != null && this.radioIdsToCheck.length > 0) {
			for (const item of this.radioIdsToCheck) {
				$('input[id="' + item + '"]').iCheck('check');
			}
			this.radioIdsToCheck = [];
		}
	}

	selecionaEtapa(id: number, idProcessoFase: number) {
		if (id != idProcessoFase) {
			this.acaoWizardClick(id);
		}
	}

	/**
	 * Mundaça de Wizard quando for um novo dossie Produto
	 * @param id da fase que esta sendo selecionada
	 */
	private acaoWizardClick(id: number) {
		if (this.mudancaSalvaService.getIsMudancaSalva()) {
			this.atualizarWizard(id);
		} else {
			this.modalAtualizarWirzad(id);
		}
	}

	/**
	 * chama ação mudar de Wizard com base na fase do Dossiê
	 * @param id 
	 */
	private atualizarWizard(id: number) {
		this.clienteLista = [];
		this.produtoLista = [];
		this.garantias = [];
		this.listaVinculoArvore = [];
		this.habilitarBtnManipular = id == this.processoAtivo;
		this.habilitaDesabilitaAcoes(id == this.processoAtivo && this.opcaoParams != SITUACAO_DOSSIE_PRODUTO.CONSULTAR);
		this.ocultarOrientacao = false;
		if (null != this.processoTipoPessoa) {
			this.carregarWizard(this.processoTipoPessoa, id);
		}
		if (undefined != this.dossieProduto && undefined != this.dossieProduto.processo_dossie.id) {
			this.carregarWizard(this.dossieProduto.processo_dossie.id, id);
		}
		this.mudancaSalvaService.setIsMudancaSalva(true);
		this.notificarAtualizacaoClientesProdutosGarantias();
	}

	/**
	 * Novas alterações: clientes, produtos ou garantias 
	 * são notificadas na aba vinculos; utilizado ao alterar as etapas: breadcumb
	 */
	private notificarAtualizacaoClientesProdutosGarantias() {
		let changeProduto: boolean = false;
		let changeGarantia: boolean = false;
		const novosVinculos: Array<VinculoCliente> = this.clienteLista.filter(vinculo => vinculo.vinculoNovo && vinculo.vinculoNovo == true);
		if (this.produtoLista) {
			const novosProdutos: Array<VinculoProduto> = this.produtoLista.filter(produto => produto.vinculoNovo && produto.vinculoNovo == true);
			changeProduto = novosProdutos.length > 0;
		}
		if (this.garantias) {
			const novasGarantias: Array<VinculoGarantia> = this.garantias.filter(garantias => garantias.vinculoNovo && garantias.vinculoNovo == true);
			changeGarantia = novasGarantias.length > 0;
		}
		this.novosVinculos = novosVinculos;
		this.changeProduto = changeProduto;
		this.changeGarantia = changeGarantia;
	}

	/**
	 * Verifica se atualizou a faze ativa foi atualizada
	 * @param id 
	 */
	private modalAtualizarWirzad(id: number) {
		Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA).subscribe(resbtn => {
			if (resbtn) {
				this.atualizarWizard(id);
			}
		},
			() => {
				this.loadService.hide();
			});
	}

	/**
	 * habilitar e desabilitar ações da tela conforme paramentro boolean
	 */
	private habilitaDesabilitaAcoes(habilitar: boolean) {
		this.habilitaBotoesSalvar = habilitar;
		this.habilitaAlteracao = habilitar;
	}

	private applyCSSRadios() {
		this.styelRadiosCss();
		this.styelCheckboxCss();
		$('input[type="radio"]').on('ifChecked', event => {
			if (this.campoFormulario != null) {
				for (const item of this.campoFormulario) {
					if (item.nome_campo === event.target.name && this.alterValueRadioIdCheck) {
						item.opcoes_disponiveis.forEach(opcaoRadio => {
							if (opcaoRadio.valor_opcao == event.target.value) {
								item.resposta_aberta = event.target.value;
							}
						});
					}
					this.mudancaSalvaService.setIsMudancaSalva(false);
				}
				this.alterValueRadioIdCheck = true;
			}
		});

		$('input[type="checkbox"]').on('ifChanged', event => {
			if (this.campoFormulario != null) {
				for (const item of this.campoFormulario) {
					if (event.target.checked && item.tipo_campo === 'CHECKBOX') {
						if (item.nome_campo === event.target.name) {
							let existCheck = false;
							item.opcoes_disponiveis.forEach(checkbox => {
								if (event.target.value == checkbox.valor_opcao) {
									undefined == item.opcoes_selecionadas ? item.opcoes_selecionadas = [] : item.opcoes_selecionadas;
									let opcaoSelecionada = item.opcoes_disponiveis.find(x => x.valor_opcao == event.target.value);
									if (item.opcoes_selecionadas && item.opcoes_selecionadas.length > 0) {
										for (let opc of item.opcoes_selecionadas) {
											if (opc.valor_opcao === event.target.value) {
												existCheck = true;
												break;
											}
										}
									}
									if (!existCheck) {
										item.opcoes_selecionadas.push(opcaoSelecionada);
									}
								}
							});
						}
					}
					if (!event.target.checked && item.tipo_campo === 'CHECKBOX') {
						let indxRemove = [];
						if (item.nome_campo === event.target.name && item.opcoes_selecionadas && item.opcoes_selecionadas.length > 0) {
							for (let i = 1; i <= item.opcoes_selecionadas.length; i++) {
								if (item.opcoes_selecionadas[i - 1].valor_opcao && event.target.value == item.opcoes_selecionadas[i - 1].valor_opcao) {
									indxRemove.push(i - 1);
								}
							}
							indxRemove.forEach(idx => {
								item.opcoes_selecionadas.splice(idx, 1);
							});
						}
					}
					this.mudancaSalvaService.setIsMudancaSalva(false);
				}
			}
		});
	}

	private styelRadiosCss() {
		$('input[type="radio"]').iCheck({
			checkboxClass: 'icheckbox_square-blue',
			radioClass: 'iradio_square-blue'
		});
	}

	private styelCheckboxCss() {
		//Definido classe pra não entra em conflito com os toogle
		$('.ckeckboxForm').iCheck({
			checkboxClass: 'icheckbox_square-blue',
			radioClass: 'iradio_square-blue'
		});
	}

	handleChangeListaVinculoArvore(input) {
		this.listaVinculoArvore = Object.assign([], input);
	}

	handleChangeCliente(input) {
		this.clienteLista = Object.assign([], input);
	}

	handleChangeVinculo(input) {
		this.produtoLista = Object.assign([], input);
	}

	handleChangeVinculoGarantia(input) {
		this.garantias = Object.assign([], input);
	}

	handleChangeExibeLoad(input) {
		this.exibeLoadArvore = input;
	}

	handleChangeSomenteLeitura(input) {
		this.usarAvaliacaoTomadorVigente = input;
	}

	handleDossieProduto(input) {
		this.dossieProduto = input;
	}

	handleChangeArvoreDinamica(input) {
		this.arvoreDinamica = input;
	}

	handlePrimeiroVinculoPessoa(input) {
		this.primeiroVinculoPessoa = input;
		this.cpfCnpj = input;
		this.habilitaBotoesSalvar = true;
	}

	handleHasAlteracaoTrata(input) {
		this.hasAlteracaoTrata = input;
	}

	handleChangeFormValidacao(input) {
		this.formValidacao = input;
	}

	handlleMessagesError(messages) {
		this.clearAllMessages();
		this.addMessageError(messages[0]);
	}

	handlleMessagesWarning(messages) {
		this.clearAllMessages();
		this.addMessageWarning(messages[0]);
	}

	handlleMessagesInfo(messages) {
		this.clearAllMessages();
		this.addMessageInfo(messages[0]);
	}

	handlleMessagesSucess(messages) {
		this.clearAllMessages();
		this.addMessageSuccess(messages[0]);
	}

	onCloseMessageSuccess() {
		this.messagesSuccess = [];
	}

	onCloseMessageInfo() {
		this.messagesInfo = [];
	}

	onCloseMessageError() {
		this.messagesError = [];
	}

	onCloseMessageWarning() {
		this.messagesWarning = [];
	}

	/**
	 * Cuida do evento de mudança de abas
	 * @param event
	 */
	handleChange(event) {
		this.applyCSSRadios();
		if ($.caixaFixAngular) {
			this.index = event.index;
			$.caixaFixAngular.fix();
		}
	}

	/**
	 * Método Responsavel pelo Salvar Parcial ou Salvar
	 */
	salvar(opcao) {
		this.index = undefined;
		this.clearAllMessages();
		//this.limparCampoFormularioOculto();
		let breackValidar: boolean;
		this.loadService.show();
		let msg: any[];
		let index: number;
		let novoDossieProduto = new DossieProduto();
		GerenciadorDossieProduto.setListaVinculoArvore(this.listaVinculoArvore);
		let tipoRelacionamentoPrincipal = this.tipoRelacionamentoDossie().find(tp => tp.principal);
		let existeVinculoClientePrincipal = this.manutencaoDossiePresenter.validarExistenciaVinculoClientePrincipal(this.listaVinculoArvore);
		if (!existeVinculoClientePrincipal) {
			this.addMessageError(ALERT_SALVAR_DOSSIE_PRODUTO.VINCULO_CLIENTE_PRINCIPAL + tipoRelacionamentoPrincipal.tipo_relacionamento);
			this.loadService.hide();
			return;
		}
		novoDossieProduto = this.manutencaoDossiePresenter.montarNovoDossieProdutoOuEditar(this.dossieProduto, novoDossieProduto, opcao, this.campoFormulario, this.justificativaRevogar);
		({ msg, index } = this.manutencaoDossiePresenter.validarQuandoRascunhoFalse(breackValidar, msg, novoDossieProduto, this.index, this.clienteLista, this.formValidacao, this.processo, this.processoAtivo, this.msgValidacao));
		this.index = index;
		if (this.dossieProduto && !novoDossieProduto.rascunho && !novoDossieProduto.cancelamento && !novoDossieProduto.situacaoComplementacao && msg.length == 0) {
			this.dossieProduto.situacao_atual = SITUACAO_DOSSIE_ATUAL.CRIADO;
		}
		if (msg && msg.length == 0 && opcao != DOSSIE_PRODUTO.VALIDAR_DOSSIE) {
			this.dossieProdutoExcessao = this.dossieProduto;
			this.mudancaSalvaService.setIsMudancaSalva(true);
			if (this.manutencaoDossiePresenter.verificaDossieProdutoJaExiste(this.dossieProduto)) {
				this.manutencaoDossiePresenter.montarEdicaoDossieProduto(this.listaVinculoArvore, novoDossieProduto);
				this.manutencaoDossiePresenter.encontrarListadeImgPngEConvertePDF(novoDossieProduto);
				let pathDossieProduto = this.manutencaoDossiePresenter.montarPathDossieProduto(novoDossieProduto, opcao);
				this.salvaDossieProdutoEditado(pathDossieProduto, this.dossieProduto.rascunho);
				return;
			}
			this.manutencaoDossiePresenter.encontrarListadeImgPngEConvertePDF(novoDossieProduto);
			let dossieProdutoObj = this.manutencaoDossiePresenter.carregarProdutosCadastrados(novoDossieProduto);
			this.salvaDossieProduto(dossieProdutoObj);
		} else {
			if (msg && msg.length == 0) {
				this.addMessageSuccess(MSG_VALIDAR_SUCESSO.DOSSIE_PRODUTO)
			} else {
				msg.forEach(ms => {
					this.addMessageError(ms);
				});
			}
			this.loadService.hide();
		}
	}

	/**
	 * Método para Revogar Dossiê Produto
	 */
	revogar() {
		Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_REVOGAR_DOSSIE).subscribe(resbtn => {
			if (resbtn) {
				this.dialogService.addDialog(ModalRevogarDocumentoComponent)
					.subscribe(retorno => {
						if (retorno != undefined) {
							this.justificativaRevogar = retorno.motivoRevogado;
							this.salvar(DOSSIE_PRODUTO.REVOGAR);
						}
					},
						() => {
							this.loadService.hide();
						});
			}
		});
	}
	/**
	 * Método para Salvar Dossiê Produto Path
	 * @param dossieProduto 
	 */
	private salvaDossieProdutoEditado(dossieProduto: DossieProdutoModelPath, rascunho: boolean) {
		this.loadService.show();
		this.dossieService.atualizaDossieProduto(this.dossieProduto.id, dossieProduto).subscribe(
			response => {
				this.loadService.hide();
				if (dossieProduto.justificativa) {
					this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, MESSAGE_ALERT_MENU.MSG_SALVO_DOSSIE_REVOGADO_SUCESSO);
				} else {
					this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, MESSAGE_ALERT_MENU.MSG_SALVO_DOSSIE_SUCESSO);
				}
				this.mudancaSalvaService.setIsMudancaSalva(true);

				let rascunho = this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.RASCUNHO;
				this.recarregaDossieProdutoDepoisDeSalvo(rascunho, this.dossieProduto.cancelamento);
			},
			error => {
				this.dossieProduto = this.dossieProdutoExcessao;
				this.addMessageError(this.manutencaoDossiePresenter.tratarErrorSalvarOuEditar(error));
				this.loadService.hide();
				throw error;
			});
	}

	/**
	 * Salva um dossie produto pela primeira vez.
	 * @param newProduto objeto de dossie produto
	 */
	private salvaDossieProduto(newProduto: DossieProdutoModel) {
		this.loadService.show();
		this.dossieService.insertDossieProdutoModel(newProduto).subscribe(
			response => {
				this.dossieProduto = response;
				this.mudancaSalvaService.setIsMudancaSalva(true);
				this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.cpfCnpjDossieProduto, this.novoDossieProdutoCpfCnpj);
				this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, MESSAGE_ALERT_MENU.MSG_SALVO_DOSSIE_SUCESSO);
				this.recarregaDossieProdutoDepoisDeSalvo(newProduto.rascunho, false);
			},
			error => {
				this.dossieProduto = this.dossieProdutoExcessao;
				this.loadService.hide();
				this.addMessageError(this.manutencaoDossiePresenter.tratarErrorSalvarOuEditar(error));
				error.tipoTela = TipoTelaErro.DOSSIE;
				throw error;
			});
	}

	/**
	 * Recarrega o dossie produto na tela para montar os campos com informações de status 
	 * adicionais vindas do banco.
	 */
	private recarregaDossieProdutoDepoisDeSalvo(rascunho: boolean, cancelamento) {
		if (rascunho || cancelamento) {
			this.router.navigateByUrl('/manutencaoDossie', { skipLocationChange: true }).then(() =>
				this.router.navigate(['manutencaoDossie', this.dossieProduto.id, 'MANTER']));
			return;
		} else if (this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_COMPLEMENTACAO) {
			this.router.navigate(['complementacao']);
			return;

		}
		this.router.navigateByUrl('/manutencaoDossie', { skipLocationChange: true }).then(() =>
			this.router.navigate(['manutencaoDossie', this.dossieProduto.id, 'CONSULTAR']));
	}

	sair() {
		if (this.hasAlteracaoTrata) {
			// this.salvar("SALVAR_TRATA");
			this.router.navigate(['']);
		} else {
			this.dossieService.updateSituacaoDossieTratamento(this.idDossie).subscribe(
				response => {
					this.router.navigate(['tratamento']);
				},
				() => {
					this.loadService.hide();
				});
		}
	}

	sairConsulta() {
		this.router.navigate(['dashboard']);
	}

	manipularDossie() {
		if (this.dossieProduto && this.dossieProduto.situacao_atual && this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_TRATAMENTO) {
			Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_DOSSIE_FILA_TRATAMENTO).subscribe(resbtn => {
				if (resbtn) {
					this.dossieService.updateSituacaoDossieProduto(this.dossieProduto.id).subscribe(
						response => {
							this.dossieProduto = Object.assign([], response);
							this.hitoriaDossieLista = Object.assign([], response.historico_situacoes);

							this.manipularDossieProduto(response);
						},
						() => {
							this.loadService.hide();
						});
				}
			},
				() => {
					this.loadService.hide();
				});
		} else {
			this.dossieService.updateSituacaoDossieProduto(this.dossieProduto.id).subscribe(
				response => {
					this.dossieProduto = response;
					this.hitoriaDossieLista = Object.assign([], response.historico_situacoes);
					this.manipularDossieProduto(response);
				},
				() => {
					this.loadService.hide();
				});
		}
	}

	private manipularDossieProduto(response: any) {
		if (response.alteracao) {
			this.addMessageSuccess(MESSAGE_ALERT_MENU.MSG_DOSSIE_CAPTURADO);
			this.carregarTelaManipular(response.alteracao);
		}
		else {
			this.addMessageWarning(MESSAGE_ALERT_MENU.MSG_DOSSIE_IMPOSSIBILITADO);
		}
	}

	carregarTelaManipular(habilitar: boolean) {
		this.habilitarBtnManipular = false;
		this.opcaoParams = SITUACAO_DOSSIE_PRODUTO.MANTER;
		this.habilitarDesabilitarBtnsAcoes(habilitar);
	}

	limparCampoFormularioOculto() {

		if (this.campoFormulario) {
			this.campoFormulario.forEach(campo => {

				if (!campo.showCampo) {
					campo.resposta_aberta = null;
				}

			});
		}

	}

	/**
	 * Verifica se o usuário logado possui a permissao: MTRADM
	 */
	hasCredentialAdministrarDossie(): boolean {
		return this.manutencaoDossiePresenter.hasCredentialAdministrarDossie() && this.dossieProduto;
	}

	verificarPermissaoDeEfetuaTratamento() {
		if (this.dossieProduto && this.dossieProduto.id && this.dossieProduto.processo_dossie && this.dossieProduto.processo_dossie.id) {
			let geradorDossie = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.dossieProduto.processo_dossie.id));

			this.habilitarBtnEfetuarTratamento = this.tratamentoValidarPermissaoService.hasCredentialParaTratameto()
				&& this.tratamentoValidarPermissaoService.isPermissaoUnidadeDeTratamento(this.dossieProduto.unidades_tratamento)
				&& this.dossieProduto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_TRATAMENTO
				&& geradorDossie.tratamento_seletivo && geradorDossie.tratamento_seletivo == true;
		} else {
			this.habilitarBtnEfetuarTratamento = false;
		}
	}

	tratamentoDossie() {
		this.loadService.show();

		this.tratamentoService.selecionaDossieProduto(this.dossieProduto.id).subscribe(response => {

			this.loadService.hide();
			this.dataService.setData(this.dossieProduto.id, response);
			this.router.navigate(['tratamentoDossie', this.dossieProduto.id]);

		}, error => {
			this.loadService.hide();

			if (error.error && error.error.detalhe) {
				let detalhe = error.error.detalhe.replace("[", "").replace("]", "");
				this.addMessageError(detalhe);
			}
			throw error;
		});

	}
	/**
	 * Atualiza o dossie produto apos: adição ou remocao de situação ou maniupulação de unidades manipuladoras
	 * @param historicoAtualizado 
	 */
	handlleChangeLoadDossieProduto() {
		this.init();
	}

	verificarUnidadeAutorizada() {
		if (this.dossieProduto !== undefined && this.dossieProduto.processo_dossie !== undefined){
			return this.manutencaoDossiePresenter.verificarUnidadeAutorizada(this.dossieProduto.processo_dossie.nome);
		} 
	}

}