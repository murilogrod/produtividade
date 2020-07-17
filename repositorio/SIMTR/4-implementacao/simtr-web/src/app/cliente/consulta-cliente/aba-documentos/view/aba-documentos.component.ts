import { DocumentImage } from '../../../../documento/documentImage';
import {
	Component,
	OnInit,
	ViewEncapsulation,
	Input,
	Output,
	AfterViewInit,
	OnChanges,
	EventEmitter,
	AfterViewChecked,
	ChangeDetectorRef,
	ViewChild,
	ElementRef
} from "@angular/core";
import { ConsultaClienteService } from "../../service/consulta-cliente-service";
import { FuncaoDocumental, VinculoCliente, DocumentoGED } from "../../../../model";
import { DocumentoNode } from "../../../../model/documentoNode";
import { ApplicationService, LoaderService, AlertMessageService, EventService } from "../../../../services";
import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { DossieService } from '../../../../dossie/dossie-service';
import { TIPO_ARVORE, EVENTS, ABA_DOSSIE_CLIENTE, MESSAGE_ALERT_MENU, LOCAL_STORAGE_CONSTANTS } from '../../../../constants/constants';
import { NodeApresentacao } from '../../../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model';
import { VinculoArvore } from '../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { GerenciadorDocumentosEmArvore } from '../../../../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util';
import { ArvoreGenericaAbastract } from '../../../../documento/arvore-generica/arvore-generica-abstract';
import { UtilsArvore } from '../../../../documento/arvore-generica/UtilsArvore';
import { TipoDocumentoArvoreGenerica } from '../../../../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model';
import { Router } from '@angular/router';
import { MudancaSalvaService } from '../../../../services/mudanca-salva.service';
import { TipoDocumentoService } from 'src/app/cruds/tipo-documento/service/tipo-documento.service';
import { ModalDadosDeclaradosComponent } from '../modal/modal-dados-declarados/view/modal-dados-declarados.component';
import { ConversorDocumentosUtil } from 'src/app/documento/conversor-documentos/conversor-documentos.util.service';
import { ArquivoPdfOriginal } from 'src/app/documento/conversor-documentos/model/arquivo-pdf-original';
import { BehaviorSubject } from 'rxjs';
import { AbaDocumentosComponentPresenter } from '../presenter/aba-documentos.component.presenter';
import { Utils } from 'src/app/utils/Utils';
import { ClassificacaoDocumento } from 'src/app/documento/conversor-documentos/model/classificacao-documento';
import { AbaDocumentos } from '../model/aba-documentos.model';



declare var $: any;

@Component({
	selector: "aba-documentos",
	templateUrl: "./aba-documentos.component.html",
	styleUrls: ["./aba-documentos.component.css"],
	encapsulation: ViewEncapsulation.None
})
export class AbaDocumentosComponent extends AlertMessageService implements OnInit, AfterViewInit, OnChanges, AfterViewChecked {

	@Input() listaVinculoArvore: Array<VinculoArvore>;
	@Input() cliente: VinculoCliente;
	@Input() idUltimoDossieProdutoCadastrado: number;
	@Input() cpfCnpj: string;
	@Input() selectedRadio;
	@Input() userExiste;
	@Input() docFound;
	@Input() docFoundIna;
	@Input() docEmpty;
	@Input() imagens: DocumentImage[];
	@Input() listaExcluir: NodeApresentacao[];
	@Input() funcoes: FuncaoDocumental[];
	@Input() dossieCliente: boolean;
	@Input() dossiePessoaFisica: boolean;
	@Output() insertDocumentosChange: EventEmitter<boolean> = new EventEmitter<boolean>();
	@ViewChild('cartaoAssinatura') cartaoAssinatura: ElementRef;

	/**
	 * Guarda o ultimo tipo de documento classifcado no modal de classificação
	 */
	@Input() tipoDocumentoArvoreGenericaAnterior: TipoDocumentoArvoreGenerica;
	@Output() tipoDocumentoArvoreGenericaAnteriorChange: EventEmitter<TipoDocumentoArvoreGenerica> = new EventEmitter<TipoDocumentoArvoreGenerica>();

	private selecionadoFolhaDoNoDaArvore: NodeApresentacao;

	treeEditable: boolean = false;

	tipoDossie: Number = TIPO_ARVORE.ARVORE_DOSSIE_CLIENTE;

	isExpandedAll = false;

	images: DocumentImage[] = [];

	imagesCopy: DocumentImage[] = [];

	documentoAClassificar: boolean;

	habilitarDocumentoClassificar: boolean;

	incluirImagem: boolean = true;

	imagemDoc = "";

	showBtn = false;

	nrDossies = 0;

	selectedFile: DocumentoNode;

	showTabs = false;

	searchDone = false;

	alteradoArvore: boolean;

	indice = 0;

	listaGed: any[] = [];

	showCategorizar: boolean = true;

	showReclassificar: boolean = false;

	qtdPages: number = 0;

	qtdClassificar: number = 0;

	abaDocumentosPresenter: AbaDocumentosComponentPresenter

	constructor(
		private clienteService: ConsultaClienteService,
		private loadService: LoaderService,
		private dialogService: DialogService,
		private dossieService: DossieService,
		private applicationService: ApplicationService,
		private eventService: EventService,
		private router: Router,
		private mudancaService: MudancaSalvaService,
		private tipoDocumentoServico: TipoDocumentoService,
		private conversorDocUtil: ConversorDocumentosUtil,
		abaDocumentosPresenter: AbaDocumentosComponentPresenter,
		private cdRef: ChangeDetectorRef
	) {
		super();
		this.conversorDocUtil.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(new Array<ArquivoPdfOriginal>());
		this.conversorDocUtil.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(new Array<DocumentImage>());
		this.abaDocumentosPresenter = abaDocumentosPresenter;
		this.abaDocumentosPresenter.abaDocumentos = new AbaDocumentos();
	}

	ngOnInit() {
		this.documentoAClassificar = false;
		this.habilitarDocumentoClassificar = false;
		this.listaExcluir = [];
		this.userExiste;
		this.init();
	}

	init() {
		this.abaDocumentosPresenter.abaDocumentos.cartaoAssinatura = this.cartaoAssinatura;
		this.abaDocumentosPresenter.abaDocumentos.cpfDossieCliente = this.cpfCnpj;
	}

	insertDocuments(input) {
		if (input) {
			input.click();
		}
	}

	handleAlteradoArvore(input) {
		this.alteradoArvore = input;
	}

	handleChangeImages(img) {
		this.images = [];
		this.images = img;
	}

	handlleOrdemPaginasAlteradaChange(input) {
		this.abaDocumentosPresenter.setArquivosAteradosClassificacao(input);
	}

	handleChangeTipoDocumentoArvoreGenericaAnterior(input) {
		this.tipoDocumentoArvoreGenericaAnterior = input;
	}

	handleChangeSelecionadoFolhaDoNoDaArvore(input) {
		this.selecionadoFolhaDoNoDaArvore = input;
	}

	onMessageEmited(event) {
		this.receiveMessage(event);
	}

	handlleMessagesWarning(messages) {
		this.messagesWarning = messages
	}

	handlleMessagesError(messages) {
		this.messagesError = messages;
	}

	handlleMessagesInfo(messages) {
		this.messagesInfo = messages;
	}

	handlleMessagesSucess(messages) {
		this.messagesSuccess = messages;
	}

	handleChangeimagesCopy(classificacaoDocumento: ClassificacaoDocumento) {
		this.imagesCopy = classificacaoDocumento.imagensClassificar;
		this.qtdClassificar = classificacaoDocumento.qtdClassificar;
		this.documentoAClassificar = this.imagesCopy.length > 0;
	}

	handleIncluirImagem(input) {
		this.incluirImagem = input;
	}

	handleChangeDocumentoAClassificarChange(input) {
		this.documentoAClassificar = input;
		this.incluirImagem = this.documentoAClassificar;
	}

	handleChangeListaExcluir(event) {
		this.listaExcluir.push(event);
		if (this.images && this.images.length > 0) {
			for (let i = 0; i < this.images.length; i++) {
				if (this.images[i].label == event.label) {
					this.images[i].excluded = true;
					break;
				}
			}
		}
	}

	ngAfterViewInit(): void {
		this.isExpandedAll = false;
	}

	ngOnChanges(): void {
		this.isExpandedAll = false;
		GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
	}

	ngAfterViewChecked() {
		this.cdRef.detectChanges();
	}

	showButtonsExpandOrCollapse(): boolean {
		return true;
	}

	onFuncoesChange(event) {
		this.funcoes = event;
	}

	mostraClassificarDocumentos() {
		let manterPdfEmMiniatura: boolean = (this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.miniaturaPDF) == 'false') ? false : true;
		let imagesRecuperadas = new Array<DocumentImage>();
		if (manterPdfEmMiniatura && !this.incluirImagem) {
			imagesRecuperadas = this.conversorDocUtil.addPaginasRestantesPdfVisualizacao(this.imagesCopy);
		}
		this.images = !this.documentoAClassificar ? imagesRecuperadas.length > 0 ? imagesRecuperadas : this.imagesCopy : [];
		this.incluirImagem = !this.documentoAClassificar;
	}

	abrirCadastroCaixa() {
		Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAR_CADASTRO_CAIXA).subscribe(confirm => {
			if (confirm) {
				this.dossieService.atualizCadastroCaixaDossieCliente(this.cliente.id).subscribe(retorno => {
					this.abaDocumentosPresenter.recarregaDossieClienteDepoisDeSalvo(this.router, this.cpfCnpj, ABA_DOSSIE_CLIENTE.DOCUMENTOS);
				}, error => {
					this.addMessageFalhaAtualizacaoCadastroCaixa(error);
				});
			}
		});
	}

	private addMessageFalhaAtualizacaoCadastroCaixa(error: any){
		let messages = new Array<string>();
		if (error.status === 428 || error.status === 412) {
			messages.push('Não foram localizados documentos aptos no dossie do cliente para executar a atualização cadastral.');
			this.alertMessagesErrorChanged.emit(messages);
		} else {
			messages.push(error.message);
			this.alertMessagesErrorChanged.emit(messages);
		}
		this.clearAllMessages();
		throw error;
	}

	abrirDadosDeclarados() {
		if (this.alteradoArvore) {
			Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA).subscribe(confirm => {
				if (confirm) {
					this.openDadosDeclarados();
				}
			},
				() => {
					this.loadService.hide();
				});
		} else {
			this.openDadosDeclarados();
		}
	}

	private openDadosDeclarados() {
		let tipoPessoa = 'pessoa-juridica';
		if (this.cliente.tipo_pessoa === "F") {
			tipoPessoa = 'pessoa-fisica';
		}
		this.loadService.show();
		this.tipoDocumentoServico.consultarDadosDeclarados(tipoPessoa).subscribe(response => {
			this.dialogService.addDialog(ModalDadosDeclaradosComponent, {
				dadosDeclarados: response.atributos_extracao,
				idDossie: this.cliente.id,
				idTipoDocumento: response.identificador_tipo_documento,
				habilitaAlteracao: true
			}).subscribe(retorno => {
				if (retorno && retorno.resultado) {
					let messagesSuccess = new Array<string>();
					messagesSuccess.push('Registro salvo com sucesso.');
					this.alertMessagesSucessChanged.emit(messagesSuccess);
					this.abaDocumentosPresenter.recarregaDossieClienteDepoisDeSalvo(this.router, this.cpfCnpj, ABA_DOSSIE_CLIENTE.DOCUMENTOS);
				} else if (retorno) {
					this.addMessageError('Erro ao salvar Dados Declarados.');
					throw retorno.mensagem;
				}

			}, error => {
				this.addMessageError('Erro ao salvar Dados Declarados.');
				this.loadService.hide();
				throw error;
			});
		},
			() => {
				this.loadService.hide();
			});
	}

	salvar() {
		this.loadService.show();
		let url = this.router.url.split('/');
		let cpfCnpj = url[2];
		if (null == this.listaVinculoArvore[0].noApresentacao || this.listaVinculoArvore[0].noApresentacao.length == 0) {
			let messagesInfo = new Array<string>();
			messagesInfo.push('Click em "Novo documento" para prosseguir.');
			this.alertMessagesInfoChanged.emit(messagesInfo);
			this.clearAllMessages();
		} else {
			this.loadService.show();
			let listaGedFuncaoDocumental: any[] = [];
			const errorOnSave = [];
			this.listaGed = ArvoreGenericaAbastract.formataListaDocumentosConsultaGedTipoDocumento(this.listaVinculoArvore[0].noApresentacao);
			listaGedFuncaoDocumental = ArvoreGenericaAbastract.formataListaDocumentosConsultaGedFuncaoDocumental(this.listaVinculoArvore[0].noApresentacao);

			if (listaGedFuncaoDocumental && listaGedFuncaoDocumental.length > 0) {
				this.listaGed.concat(listaGedFuncaoDocumental);
			}

			let callsToMake = [];
			if (this.listaExcluir.length > 0) {
				for (var i = 0; i < this.listaExcluir.length; i++) {
					let nodeApresentacao: NodeApresentacao = this.listaExcluir[i];
					callsToMake.push({ call: this.clienteService.excluiDocumento(this.cliente.id, nodeApresentacao.id), type: 'E', node: nodeApresentacao, ok: false, index: i });
				}
			}

			if (this.listaGed.length > 0) {
				for (var i = 0; i < this.listaGed.length; i++) {
					this.listaGed[i] = this.conversorDocUtil.verificaHouveAlteracaoAlgumPDFDossieCliente(this.listaGed[i]);
					const obj: any = this.dossieService.insertDocumentoCliente(this.listaGed[i].node, this.cliente.id);
					callsToMake.push({
						call: obj, type: 'I', label: this.listaGed[i].label,
						tipoDocumento: this.listaGed[i].tipoDocumento, ok: false, index: i
					});
				}
			}

			if (callsToMake.length > 0) {
				this.eventService.on(EVENTS.finishSaveDossieCliente, () => {
					if (errorOnSave.length > 0) {
						let messagesError = new Array<string>();
						messagesError.push('Erro ao salvar registro.');
						for (const item of errorOnSave) {
							messagesError.push(item);
						}
						this.alertMessagesErrorChanged.emit(messagesError);
					} else {
						let messagesSuccess = new Array<string>();
						messagesSuccess.push('Registro salvo com sucesso.');
						this.alertMessagesSucessChanged.emit(messagesSuccess);
						this.abaDocumentosPresenter.recarregaDossieClienteDepoisDeSalvo(this.router, cpfCnpj, ABA_DOSSIE_CLIENTE.DOCUMENTOS);
					}
					this.loadService.hide();
					UtilsArvore.removeNodeIfEmptyFirstLevel(this.listaVinculoArvore[0].noApresentacao);
				}, true);

				for (let i = 0; i < callsToMake.length; i++) {
					const item = callsToMake[i];
					item.call.subscribe(res => {
						if (item.type == 'E') {
							this.listaExcluir.splice(item.index, 1);
							UtilsArvore.removeNodeInTree(this.listaVinculoArvore[0].noApresentacao, item.node.parent, TIPO_ARVORE.ARVORE_DOSSIE_CLIENTE);
						} else if (item.type == 'I') {
							UtilsArvore.removeInsertOption(this.listaVinculoArvore[0].noApresentacao, item.label);
							// this.insertDocumentosChange.emit(true);
						}
						item.ok = true;
						if (this.allCallsAreOK(callsToMake)) {
							this.eventService.broadcast(EVENTS.finishSaveDossieCliente);
						}
					}, error => {
						if (item.type == 'E') {
							errorOnSave.push('Erro na exclusão do tipo de documento ' + item.node.parent.label);
						} else if (item.type == 'I') {
							errorOnSave.push('Erro na inclusão do tipo de documento ' + item.tipoDocumento);
						}
						item.ok = true;
						if (this.allCallsAreOK(callsToMake)) {
							this.eventService.broadcast(EVENTS.finishSaveDossieCliente);
						}
						this.loadService.hide();
						throw error;
					});
				}
			} else {
				this.loadService.hide();
			}
		}
		this.images = [];
		this.habilitarDocumentoClassificar = false;
		this.mudancaService.setIsMudancaSalva(true);
	}

	allCallsAreOK(list) {
		for (let i = 0; i < list.length; i++) {
			if (list[i].ok == false) {
				return false;
			}
		}
		return true;
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

	obterCartaoAssinatura() {
		this.abaDocumentosPresenter.obterCartaoAssinatura(this);
	}

}
