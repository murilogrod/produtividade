import { Component, OnInit, Input, Output, EventEmitter, OnChanges, AfterViewChecked, ViewEncapsulation, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { DocumentImage } from '../documentImage';
import { DossieService } from '../../dossie/dossie-service';
import { NodeApresentacao } from '../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model';
import { VinculoArvore } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { ArvoreGenericaAbastract } from './arvore-generica-abstract';
import { VinculoArvoreCliente } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente';
import { VinculoArvoreGarantia } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia';
import { VinculoArvoreProduto } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto';
import { TipoDocumentoArvoreGenerica } from '../../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model';
import { VinculoArvoreProcesso } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo';
import { FuncaoDocumentalArvoreGenerica } from '../../model/model-arvore-generica-dossie-produto/funcao-documental-arvore-generica';
import { NodeAbstract } from '../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-abstract.model';
import { GerenciadorDocumentosEmArvore } from '../gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util';
import { TIPO_ARVORE, EVENTS, LOCAL_STORAGE_CONSTANTS, LINKS, STATUS_DOCUMENTO } from '../../constants/constants';
import { DocumentoArvore } from '../../model/documento-arvore';
import { DialogService } from 'angularx-bootstrap-modal';
import { ModalReutilizacaoDocumentosComponent } from '../modal-reutilizar/modal-reutilizacao-documentos.component';
import { UtilsArvore } from './UtilsArvore';
import { ConsultaClienteService } from '../../cliente/consulta-cliente/service/consulta-cliente-service';
import * as moment from 'moment';
import { LoaderService, EventService, ApplicationService } from 'src/app/services';
import { MudancaSalvaService } from '../../services/mudanca-salva.service';
import { EmptyVinculoArvore } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/empty-vinculo-arvore';
import { ArvoreTipoDocumento } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/arvore-tipo-documento';
import { SituacaoDocumento } from '../enum-documento/situacao-documento.enum';
import { FormExtracaoManualService } from '../../compartilhados/componentes/formulario-extracao/service/form-extracao-manual.service';
import { IMAGEM_DEFAUT } from 'src/app/constants/imgDefaut';
import { FonteDocumento } from '../enum-fonte-documento/fonte-documento.enum';

declare var $: any;

@Component({
	selector: 'arvore-generica-documento',
	templateUrl: './arvore-generica.component.html',
	styleUrls: ['./arvore-generica.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class ArvoreGenericaComponent<T extends VinculoArvore> extends ArvoreGenericaAbastract<T> implements OnInit, OnChanges, AfterViewChecked {
	@Input() processoGeraDossie: any;
	@Input() dossieCliente: boolean;
	@Input() habilitaBotoesSalvar: boolean;
	@Input() listaVinculoArvore: T[];
	@Input() listaNosVisualizacao: NodeApresentacao[];
	@Input() indiceArvoreAtual: number;
	@Input() images: DocumentImage[] = [];
	@Input() treeEditable;
	@Input() listaExcluir: NodeApresentacao[];
	@Input() selectedDocumentNode: NodeApresentacao;
	@Input() tratamento;
	@Input() incluirImagem: boolean;
	@Input() expandirAllTree: boolean;
	@Input() activeUpload: boolean;
	@Input() vinculoArvore: VinculoArvore;
	@Output() imagensChange: EventEmitter<DocumentImage[]> = new EventEmitter<DocumentImage[]>();
	@Output() listaExcluirChange: EventEmitter<NodeApresentacao> = new EventEmitter<NodeApresentacao>();
	@Output() incluirImagemChange: EventEmitter<boolean> = new EventEmitter<boolean>();
	@Output() emptyTreeProcesso: EventEmitter<EmptyVinculoArvore> = new EventEmitter<EmptyVinculoArvore>();
	@Output() emptyTreeCliente: EventEmitter<EmptyVinculoArvore> = new EventEmitter<EmptyVinculoArvore>();
	@Output() emptyTreeProduto: EventEmitter<EmptyVinculoArvore> = new EventEmitter<EmptyVinculoArvore>();
	@Output() emptyTreeGarantia: EventEmitter<EmptyVinculoArvore> = new EventEmitter<EmptyVinculoArvore>();
	@Output() arvoreTipoDocumentoSelecionadoChange: EventEmitter<ArvoreTipoDocumento> = new EventEmitter<ArvoreTipoDocumento>();
	@Output() selecionadoFolhaDoNoDaArvoreChange: EventEmitter<NodeApresentacao> = new EventEmitter<NodeApresentacao>();


	/**
	 * Defini se esta visualizando novos documentos ou ira reclassificar
	 */
	@Input() documentoAClassificar: boolean;
	@Output() documentoAClassificarChange: EventEmitter<boolean> = new EventEmitter<boolean>();

	/**
	 * Guarda o ultimo tipo de documento classifcado no modal de classificação
	 */
	@Input() tipoDocumentoArvoreGenericaAnterior: TipoDocumentoArvoreGenerica;
	@Output() tipoDocumentoArvoreGenericaAnteriorChange: EventEmitter<TipoDocumentoArvoreGenerica> = new EventEmitter<TipoDocumentoArvoreGenerica>();

	listaCopy: NodeApresentacao[];
	listFiltered: NodeApresentacao[];
	filtro = '';
	static tipologiasDocumentais: any;
	selectedFile: NodeApresentacao;
	static listaObrigatorios: String[] = [];
	ocultarArvore: boolean;
	expandirTodos?: boolean;
	exibirInativos?: boolean;
	habilitarExclusao?: boolean;
	habilitarReuso?: boolean;
	arovrePermiteReuso?: boolean;
	isPossuiDocNaoSalvo: boolean = false;
	posicaoAtual?: number = 1;

	constructor(
		private clienteService: ConsultaClienteService,
		private dossieService: DossieService,
		private dialogService: DialogService,
		private loadService: LoaderService,
		private cdRef: ChangeDetectorRef,
		private eventService: EventService,
		private mudancaSalvaService: MudancaSalvaService,
		private appService: ApplicationService,
		private servicoFormExtracaoManual: FormExtracaoManualService
	) {
		super();
	}

	ngAfterViewChecked() {
		this.cdRef.detectChanges();
	}

	ngOnInit() {
		$('[data-toggle="tooltip"]').tooltip();
		this.expandirTodos = false;
		this.exibirInativos = false;
		this.habilitarExclusao = false;
		this.habilitarReuso = false;
		this.listaNosVisualizacao = [];
		this.listaExcluir = [];
		this.ocultarArvore = false;
		ArvoreGenericaAbastract.contadorNode = 0;
		if (ArvoreGenericaComponent.tipologiasDocumentais == undefined) {
			this.buscarTipologia();
		} else {
			this.criaArvoresPorTipoContidoLista();
		}
	}

	private buscarTipologia() {
		ArvoreGenericaComponent.tipologiasDocumentais = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia));
		this.criaArvoresPorTipoContidoLista();
	}

	ngOnChanges() {
		this.atualizarStatusSituacaoCorArvore();
	}

	private atualizarStatusSituacaoCorArvore() {
		GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
		let vinculoArvore: VinculoArvore = this.listaVinculoArvore[this.indiceArvoreAtual];
		if (vinculoArvore.ocultarVinculoArvore) {
			this.ocultarArvore = vinculoArvore.ocultarVinculoArvore;
		}
		this.listaVinculoArvore.forEach(no => {
			if (undefined != no.noApresentacao) {
				no.noApresentacao.forEach(node => {
					this.expandirTodos = this.expandirAllTree;
					this.expandRecursive(node, this.expandirTodos);
					if (!this.dossieCliente) {
						if (node.children.length > 0) {
							this.percorrernoChildre(node);
						}
						else {
							this.quandoForDossieProdutoAplicarCor(node);
						}
					}
				});
			}
		});
		this.eventService.on(EVENTS.QTD_IMAGEM_VISUALIZACAO, idQtd => {
			this.encontrarNoApresentacao(idQtd, this.listaNosVisualizacao);
		});
	}

	/**
	 * Aplicar avaliação se o documento foi contemplado pela regra de negocio ou não
	 */
	quandoForDossieProdutoAplicarCor(node: NodeApresentacao) {
		UtilsArvore.definrCorTipoArvore(node);
	}

	/**
	 * Encontrar o no do documento ou encontra se e função ou uma pasta de documento
	 * @param node 
	 */
	private percorrernoChildre(node: NodeApresentacao) {
		node.children.forEach(noChildren => {
			this.noChildrenDocumento(noChildren, node);
			this.noChildrenTipoDocumento(noChildren, node);
		});
	}
	/**
	 * Quando ainda for uma pasta de documento ou mesmo uma função entra aqui
	 */
	private noChildrenTipoDocumento(noChildren: NodeApresentacao, node: NodeApresentacao) {
		if (!noChildren.leaf) {
			let { eFuncao, qtdImg } = this.definirQtdESeEFuncao(node);
			if (noChildren.children.length) {
				if (!eFuncao) {
					if (node.obrigatorio) {
						UtilsArvore.definrCorTipoArvoreFuncao(node, qtdImg);
					}
					UtilsArvore.definrCorTipoArvore(node);
				} else {
					UtilsArvore.definrCorTipoArvoreFuncao(node, qtdImg);
				}
				UtilsArvore.definrCorTipoArvoreInicialFuncao(noChildren, noChildren.obrigatorio);
			} else {
				UtilsArvore.definrCorTipoArvoreFuncao(node, qtdImg);
				UtilsArvore.definrCorTipoArvoreInicialFuncao(noChildren, noChildren.obrigatorio);
			}
		}
	}

	/**
	 * Quando for o no do documento
	 * @param noChildren 
	 * @param node 
	 */
	private noChildrenDocumento(noChildren: NodeApresentacao, node: NodeApresentacao) {
		if (noChildren.leaf) {
			UtilsArvore.definrCorTipoArvoreInicialFuncao(node, node.obrigatorio);
		}
	}

	private definirQtdESeEFuncao(node: NodeApresentacao) {
		let eFuncao = false;
		let qtdImg = 0;
		node.children.forEach(nChild => {
			nChild.children.forEach(child => {
				if (child.leaf) {
					eFuncao = true;
					qtdImg++;
				}
			});
		});
		return { eFuncao, qtdImg };
	}

	private encontrarNoApresentacao(idQtd: any, listaNode) {
		this.loadService.show();
		for (let noApresentacao of listaNode) {
			if (noApresentacao.children && noApresentacao.children.length > 0) {
				this.encontrarNoApresentacao(idQtd, noApresentacao.children)
			} else if (!noApresentacao.children) {
				if (noApresentacao.pages && noApresentacao.pages.length > 0) {
					for (let noApesenta of noApresentacao.pages) {
						if (idQtd.id === noApesenta.id) {
							noApesenta.qtdImg = idQtd.qtd;
						}
					}
				}
			}
		}
		this.listaNosVisualizacao = Object.assign([], this.listaNosVisualizacao);
		this.loadService.hide();
	}
	/**
	 * Utilizado para mostrar o numero de quantidade pagina de um documento
	 * @param node 
	 */
	encontrarNodePage(node): boolean {
		const qtd: number = this.calcularQuantidadeImagens(node);
		if (qtd > 0) {
			if (node.dossieCliente) {
				return node.documentoArvore.quantidade_conteudos > 0 ? node.documentoArvore.quantidade_conteudos : qtd;
			} else {
				return node.documentoArvore.quantidade_conteudos > 0 ? node.documentoArvore.quantidade_conteudos : qtd;
			}
		}
	}

	/**
	 * Retorna a quantidade de imagens
	 * @param node 
	 */
	private calcularQuantidadeImagens(node: any): number {
		let qtd: number = 0;
		if (node && node.pages && node.pages.length > 0) {
			for (let no of node.pages) {
				if (node.documentoArvore && node.documentoArvore.quantidade_conteudos) {
					qtd = node.documentoArvore.quantidade_conteudos;
					break;
				} else {
					qtd = node.documentoArvore.id ? no.qtdImg : no.uri.length;
				}
			}
		}
		return qtd;
	}

	lengthNodeChildren(noChildren) {
		return noChildren && noChildren.length;
	}

	/**
	 * Realiza inserção de um documento reutlizado selecionado pelo usuário na pasta desejada.
	 * @param node No pai selecionado para adicionar um documento reutilizável
	 */
	aplicaReusoDocumento(node: NodeApresentacao) {
		let vinculoArvoreCliente: VinculoArvoreCliente = this.listaVinculoArvore[this.indiceArvoreAtual];
		vinculoArvoreCliente.alterandoVinculo = true;
		let documentosPorTipo: DocumentoArvore[] = super.filtraParaDocumentosMesmoTipo(vinculoArvoreCliente.vinculoCliente.documentos, node.id);
		if (documentosPorTipo && documentosPorTipo.length == 1) {
			let docDB: DocumentoArvore = documentosPorTipo.pop();
			docDB.tipo_documento.idNodeApresentacao = docDB.tipo_documento.id;
			docDB.docReutilizado = true;

			if (this.verificaDocReutilizadoJaExisteNaPasta(node, docDB)) {
				return;
			}

			docDB.situacaoAtual = !docDB.situacaoAtual ? SituacaoDocumento.CRIADO : docDB.situacaoAtual;
			GerenciadorDocumentosEmArvore.criaNodeExisteDB(node, docDB, docDB.matricula_captura, docDB.tipo_documento, this.dossieCliente);
			node.existeDocumentoReuso = false;
			this.atualizarStatusSituacaoCorArvore();
			return;
		}
		this.dialogService.addDialog(ModalReutilizacaoDocumentosComponent, {
			documentos: documentosPorTipo
		})
			.subscribe(retorno => {
				if (retorno != undefined) {
					retorno.docDBSelecionado.tipo_documento.idNodeApresentacao = retorno.docDBSelecionado.tipo_documento.id;
					retorno.docDBSelecionado.docReutilizado = true;
					if (this.verificaDocReutilizadoJaExisteNaPasta(node, retorno.docDBSelecionado)) {
						return;
					}
					retorno.docDBSelecionado.situacaoAtual = !retorno.docDBSelecionado.situacaoAtual ? SituacaoDocumento.CRIADO : retorno.docDBSelecionado.situacaoAtual;
					GerenciadorDocumentosEmArvore.criaNodeExisteDB(node, retorno.docDBSelecionado,
						retorno.docDBSelecionado.matricula_captura, retorno.docDBSelecionado.tipo_documento, this.dossieCliente);
					node.existeDocumentoReuso = false;
				}
			});
	}

	/**
	 * Impede que um documento já selecionado para reutilização seja adiconado na mesma pasta.
	 * @param node No pai selecionado para adicionar um documento reutilizável
	 * @param docDBSelecionado documento selecionado para reutilização
	 */
	private verificaDocReutilizadoJaExisteNaPasta(node: NodeApresentacao, docDBSelecionado: DocumentoArvore): boolean {
		for (let doc of node.children) {
			if (doc.pages[0].id == docDBSelecionado.id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Inicia a criação das arvores a partir do tipo de vinculo contido na listaVinculoArvore
	 */
	private criaArvoresPorTipoContidoLista() {
		this.verificaTipoVinculoArvore(this.listaVinculoArvore[this.indiceArvoreAtual]);
		this.listaCopy = Object.assign([], this.listaNosVisualizacao);
	}

	/**
	 * Identifica os tipos de instancias de objetos vinculo de árvore
	 * @param vinculoArvore Recebe objeto a ser manipulado pela árvore. Objeto pode conter os 
	 * dados de cliente, ou produto, ou garantia, ou processo.
	 */
	private verificaTipoVinculoArvore(vinculoArvore: T) {
		if (vinculoArvore.id == undefined) {
			this.criaEstruturaDeNosDeVinculoArvoreDossieCliente(vinculoArvore);
			return;
		}
		if (vinculoArvore instanceof VinculoArvoreCliente) {
			this.criaEstruturaDeNosDeVinculoArvoreCliente(vinculoArvore);
			this.verificaEstruturaVaziaArvoresDossie(vinculoArvore);
			return;
		}
		if (vinculoArvore instanceof VinculoArvoreProduto) {
			this.criaEstruturaDeNosDeVinculoArvoreProduto(vinculoArvore);
			this.verificaEstruturaVaziaArvoresDossie(vinculoArvore);
			return;
		}
		if (vinculoArvore instanceof VinculoArvoreGarantia) {
			this.criaEstruturaDeNosDeVinculoArvoreGarantia(vinculoArvore);
			this.verificaEstruturaVaziaArvoresDossie(vinculoArvore);
			return;
		}
		if (vinculoArvore instanceof VinculoArvoreProcesso) {
			this.criaEstruturaDeNosDeVinculoArvoreProcesso(vinculoArvore);
			this.verificaEstruturaVaziaArvoresDossie(vinculoArvore);
		}
	}

	/**
	 * Verifica toda a estrutura de arvores do objeto vinculoArvore; caso exista uma arvore vazia; sua apresentação será ocultada.
	 * Em desenvolvimento os atributos: arvoreCliente, arvoreProcesso, arvoreProduto e arvoreGarantia; esvaziam a arvore para efeito de teste
	 * @param vinculoArvoreCliente 
	 */
	private verificaEstruturaVaziaArvoresDossie(vinculoArvore: VinculoArvore): any {
		let arvoreProcesso: boolean = false;
		let arvoreCliente: boolean = false;
		let arvoreProduto: boolean = false;
		let arvoreGarantia: boolean = false;
		if (vinculoArvore instanceof VinculoArvoreProcesso) {
			this.verificaEstruturaProcessoVazia(vinculoArvore, arvoreProcesso);
		}
		if (vinculoArvore instanceof VinculoArvoreCliente) {
			this.verificaEstruturaClienteVazia(vinculoArvore, arvoreCliente);
		}
		if (vinculoArvore instanceof VinculoArvoreProduto) {
			this.verificaEstruturaProdutoVazia(vinculoArvore, arvoreProduto);
		}
		if (vinculoArvore instanceof VinculoArvoreGarantia) {
			this.verificaEstruturaGarantiaVazia(vinculoArvore, arvoreGarantia);
		}
	}


	/**
	* Verifica se a estrutura da arvore processo está vazia; caso verdadeiro; 
	 * toda estrutra será ocultada
	 * @param vinculoArvoreProcesso 
	 * @param arvoreProcesso Utilizado para esvaziar a arvore processo..utilização para validar a ocultação. Fins de teste.
	 */
	private verificaEstruturaProcessoVazia(vinculoArvoreProcesso: VinculoArvoreProcesso, arvoreProcesso: boolean) {
		const emptyVinculoArvore = new EmptyVinculoArvore();
		vinculoArvoreProcesso.noApresentacao = arvoreProcesso ? [] : vinculoArvoreProcesso.noApresentacao;
		emptyVinculoArvore.emptyVinculoArvore = vinculoArvoreProcesso.noApresentacao.length == 0;
		emptyVinculoArvore.vinculoArvore = vinculoArvoreProcesso;
		emptyVinculoArvore.idProcessoFase = vinculoArvoreProcesso.idProcessoFase;
		this.emptyTreeProcesso.emit(emptyVinculoArvore);
	}

	/**
	* Verifica se a estrutura da arvore cliente está vazia; caso verdadeiro; 
	 * toda estrutra será ocultada
	 * @param vinculoArvoreCliente 
	 * @param arvoreCliente Utilizado para esvaziar a arvore cliente..utilização para validar a ocultação. Fins de teste.
	 */
	private verificaEstruturaClienteVazia(vinculoArvoreCliente: VinculoArvoreCliente, arvoreCliente: boolean) {
		const emptyVinculoArvore = new EmptyVinculoArvore();
		vinculoArvoreCliente.noApresentacao = arvoreCliente ? [] : vinculoArvoreCliente.noApresentacao;
		emptyVinculoArvore.emptyVinculoArvore = vinculoArvoreCliente.noApresentacao.length == 0;
		emptyVinculoArvore.vinculoArvore = vinculoArvoreCliente;
		this.emptyTreeCliente.emit(emptyVinculoArvore);
	}

	/**
	* Verifica se a estrutura da arvore produto está vazia; caso verdadeiro; 
	 * toda estrutra será ocultada
	 * @param vinculoArvoreProduto 
	 * @param arvoreProduto Utilizado para esvaziar a arvore produto..utilização para validar a ocultação. Fins de teste.
	 */
	private verificaEstruturaProdutoVazia(vinculoArvoreProduto: VinculoArvoreProduto, arvoreProduto: boolean) {
		const emptyVinculoArvore = new EmptyVinculoArvore();
		vinculoArvoreProduto.noApresentacao = arvoreProduto ? [] : vinculoArvoreProduto.noApresentacao;
		emptyVinculoArvore.emptyVinculoArvore = vinculoArvoreProduto.noApresentacao.length == 0;
		emptyVinculoArvore.vinculoArvore = vinculoArvoreProduto;
		this.emptyTreeProduto.emit(emptyVinculoArvore);
	}

	/**
	* Verifica se a estrutura da arvore garantia está vazia; caso verdadeiro; 
	 * toda estrutra será ocultada
	 * @param vinculoArvoreProduto 
	 * @param arvoreGarantia Utilizado para esvaziar a arvore garantia..utilização para validar a ocultação. Fins de teste.
	 */
	private verificaEstruturaGarantiaVazia(vinculoArvoreGarantia: VinculoArvoreGarantia, arvoreGarantia: boolean) {
		const emptyVinculoArvore = new EmptyVinculoArvore();
		vinculoArvoreGarantia.noApresentacao = arvoreGarantia ? [] : vinculoArvoreGarantia.noApresentacao;
		emptyVinculoArvore.emptyVinculoArvore = vinculoArvoreGarantia.noApresentacao.length == 0;
		emptyVinculoArvore.vinculoArvore = vinculoArvoreGarantia;
		this.emptyTreeGarantia.emit(emptyVinculoArvore);
	}

	/**
	 * Inicia a criação da estrutura de arvore de dossie cliente pelo tipo de pessoa
	 * @param vinculoArvore Objeto de vinculoCliente. Ex: PF OU PJ 
	 */
	private criaEstruturaDeNosDeVinculoArvoreDossieCliente(vinculoArvore: T) {
		this.refreshAvoreClienteAposInsercao();
		this.listaNosVisualizacao = [];
		let vinculoArvoreCliente: VinculoArvoreCliente = vinculoArvore as VinculoArvoreCliente;
		let listaFuncaoDocumental: FuncaoDocumentalArvoreGenerica[] = ArvoreGenericaComponent.separaFuncaoDocumentalPorTipoPessoa(vinculoArvoreCliente.vinculoCliente.tipo_pessoa);
		let listaTipoDocuemnto: TipoDocumentoArvoreGenerica[] = ArvoreGenericaComponent.separaTipoDocumentoPorTipoPessoa(vinculoArvoreCliente.vinculoCliente.tipo_pessoa);

		if (vinculoArvoreCliente.vinculoCliente.documentos != undefined &&
			vinculoArvoreCliente.vinculoCliente.documentos.length > 0) {
			GerenciadorDocumentosEmArvore.montaArvoreDocumentosExistenteDossie(vinculoArvoreCliente,
				this.listaNosVisualizacao, this.dossieCliente);
			this.subtraiTiposDocumentosJaExistentesAPartirFuncaoDocumental(listaFuncaoDocumental);
			this.subtraiTiposDocumentosJaExistentesAPartirTipoDocumento(listaTipoDocuemnto);
			this.criaNosArvoreDossieClienteTipoDocumento(listaTipoDocuemnto);
			this.criaNosArvoreDossieClienteFuncaoDocumental(listaFuncaoDocumental);
			vinculoArvore.noApresentacao = this.listaNosVisualizacao;
			return;
		}

		this.criaNosArvoreDossieClienteTipoDocumento(listaTipoDocuemnto);
		this.criaNosArvoreDossieClienteFuncaoDocumental(listaFuncaoDocumental);
		ArvoreGenericaAbastract.ordenaArvorePorDocumentosMaisRecentes(this.listaNosVisualizacao);
		vinculoArvore.noApresentacao = this.listaNosVisualizacao;
	}

	/**
	 * Remove arvore cliente anterior a inserção dos documentos
	 */
	private refreshAvoreClienteAposInsercao() {
		if (this.listaVinculoArvore && this.listaVinculoArvore.length > 1) {
			this.listaVinculoArvore.splice(0, 1);
		}
	}

	/**
	 * Subtrai da listaFuncaoDocumentao daquele tipo de pessoa os tipos de documentos ja existentes vindos da base
	 * @param listaFuncaoDocumental Lista que contem todas as funcoes documentais para cada tipo de pessoa
	 */
	private subtraiTiposDocumentosJaExistentesAPartirFuncaoDocumental(listaFuncaoDocumental: FuncaoDocumentalArvoreGenerica[]) {
		for (let funcaoDocumental of listaFuncaoDocumental) {
			let tiposDocumentosFaltantes = new Array<TipoDocumentoArvoreGenerica>();
			tiposDocumentosFaltantes = funcaoDocumental.tipos_documento.map(tipo => tipo);
			for (let tipoDocumento of funcaoDocumental.tipos_documento) {
				let node: NodeAbstract = UtilsArvore.getNodeInTreeByTypeDocument(this.listaNosVisualizacao, tipoDocumento);
				if (node != null) {
					tiposDocumentosFaltantes = tiposDocumentosFaltantes.filter(tipoDoc => tipoDoc.id != tipoDocumento.id);
				}
			}
			funcaoDocumental.tipos_documento = tiposDocumentosFaltantes;
		}
	}

	private subtraiTiposDocumentosJaExistentesAPartirTipoDocumento(listaTipoDocumento: TipoDocumentoArvoreGenerica[]) {
		let tiposDocumentosFaltantes = new Array<TipoDocumentoArvoreGenerica>();
		tiposDocumentosFaltantes = listaTipoDocumento.map(tipo => tipo);
		for (let tipoDocumento of listaTipoDocumento) {
			let node: NodeAbstract = UtilsArvore.getNodeInTreeByTypeDocument(this.listaNosVisualizacao, tipoDocumento);
			if (node != null) {
				tiposDocumentosFaltantes = tiposDocumentosFaltantes.filter(tipoDoc => tipoDoc.id != tipoDocumento.id);
			}
		}
		listaTipoDocumento = tiposDocumentosFaltantes;
	}

	/**
	 * Percorre a tipologia e separa as funcoes documentais com seus documentos pelo tipo de pessoa
	 * @param icTipoPessoa tipo da pessoa
	 */
	public static separaFuncaoDocumentalPorTipoPessoa(icTipoPessoa: string) {
		let listaSeparadaPorTipoPessoa: FuncaoDocumentalArvoreGenerica[] = [];
		for (let funcaoDocumental of ArvoreGenericaComponent.tipologiasDocumentais.funcoes_documentais) {
			let funcDocumental: FuncaoDocumentalArvoreGenerica = new FuncaoDocumentalArvoreGenerica;
			if (funcaoDocumental.tipos_documentos && funcaoDocumental.tipos_documentos.length > 0) {
				let listaTipoDocumento: TipoDocumentoArvoreGenerica[] = [];
				for (let tipoDocumento of funcaoDocumental.tipos_documentos) {
					if (tipoDocumento.tipo_pessoa == icTipoPessoa || tipoDocumento.tipo_pessoa == "A") {
						let tpDocumento: TipoDocumentoArvoreGenerica = new TipoDocumentoArvoreGenerica;
						tpDocumento.id = tipoDocumento.id;
						tpDocumento.nome = tipoDocumento.nome;
						tpDocumento.tipo_pessoa = tipoDocumento.tipo_pessoa;
						tpDocumento.codigo_tipologia = tipoDocumento.codigo_tipologia;
						listaTipoDocumento.push(tpDocumento);
					}
				}
				if (listaTipoDocumento && listaTipoDocumento.length > 0) {
					funcDocumental.id = funcaoDocumental.id;
					funcDocumental.nome = funcaoDocumental.nome;
					funcDocumental.tipos_documento = listaTipoDocumento;
					listaSeparadaPorTipoPessoa.push(funcDocumental);
				}
			}
		}

		return listaSeparadaPorTipoPessoa;
	}

	public static separaTipoDocumentoPorTipoPessoa(icTipoPessoa: string) {
		let listaTipoDocumento: TipoDocumentoArvoreGenerica[] = [];
		for (let tipoDocumento of ArvoreGenericaComponent.tipologiasDocumentais.tipos_documento) {
			if (tipoDocumento.tipo_pessoa == icTipoPessoa || tipoDocumento.tipo_pessoa == "A") {
				let tpDocumento: TipoDocumentoArvoreGenerica = new TipoDocumentoArvoreGenerica;
				tpDocumento.id = tipoDocumento.id;
				tpDocumento.nome = tipoDocumento.nome;
				tpDocumento.tipo_pessoa = tipoDocumento.tipo_pessoa;
				tpDocumento.codigo_tipologia = tipoDocumento.codigo_tipologia;
				listaTipoDocumento.push(tpDocumento);
			}
		}
		return listaTipoDocumento;
	}

	/**
	 * Cria os nos da arvorede de documentos do dossie cliente
	 * @param listaFuncaoDocumental lista das funcões documentais para um tipo de pessoa específico
	 */
	private criaNosArvoreDossieClienteFuncaoDocumental(listaFuncaoDocumental: FuncaoDocumentalArvoreGenerica[]) {
		for (let funcaoDocumental of listaFuncaoDocumental) {
			let node = this.listaNosVisualizacao.find(node => node.label == funcaoDocumental.nome);
			if (node == undefined) {
				node = ArvoreGenericaAbastract.criaNo(funcaoDocumental);
				node.id = ArvoreGenericaAbastract.contadorNode++;
				ArvoreGenericaAbastract.hideNode(node);
				this.listaNosVisualizacao.push(node);
			}
			for (let tipo_documento of funcaoDocumental.tipos_documento) {
				let nodeChildren = ArvoreGenericaAbastract.criaNo(tipo_documento);
				nodeChildren.parent = node;
				ArvoreGenericaAbastract.hideNode(nodeChildren);
				node.children.push(nodeChildren);
			}
		}
	}

	private criaNosArvoreDossieClienteTipoDocumento(listaTipoDocuemnto: TipoDocumentoArvoreGenerica[]) {
		for (let tipodocumento of listaTipoDocuemnto) {
			let node = this.listaNosVisualizacao.find(node => node.label == tipodocumento.nome);
			if (node == undefined) {
				node = ArvoreGenericaAbastract.criaNo(tipodocumento);
				ArvoreGenericaAbastract.hideNode(node);
				this.listaNosVisualizacao.push(node);
			}
		}
	}

	/**
	 * Verica se o objeto de cliente a ser manipulado pela árvore está no nivel de processo ou processo fáse.
	 * @param vinculoArvore Objeto de vinculoCliente. Ex: TITULAR, AVALISATA, TOMADOR 
	 */
	private criaEstruturaDeNosDeVinculoArvoreCliente(vinculoArvore: T) {
		this.arovrePermiteReuso = true;
		this.listaNosVisualizacao = [];
		if (undefined != this.processoGeraDossie) {
			for (let processo_filho of this.processoGeraDossie.processos_filho) {
				if (processo_filho.id === vinculoArvore.id) {
					let processo_fase = super.verificaArvoreProcessoFase(processo_filho, vinculoArvore)
					if (processo_fase === undefined) {
						this.criaNoAPartirDoTipoDeRelacionamentoDePessoaEscolhido(vinculoArvore as VinculoArvoreCliente,
							processo_filho);
						break;
					}
					this.criaNoAPartirDoTipoDeRelacionamentoDePessoaEscolhido(vinculoArvore as VinculoArvoreCliente,
						processo_fase)
					break;
				}
			}
		}
		vinculoArvore.noApresentacao = this.listaNosVisualizacao;
	}

	/**
	 * Acha o tipo de relacionamento de pessoa comparando os tipos no processo partriarca.
	 * Caso ache um correspondente irá criar a árvore correspondente ao tipo de relacionamento.
	 * @param vinculoArvoreCliente Objeto de vinculo de cliente. Ex: TITULAR, AVALISATA, TOMADOR 
	 * @param processo_filho No da estrutura do patriarca, pode ser o NO de processo dossie ou NO filho do processo fase
	 */
	private criaNoAPartirDoTipoDeRelacionamentoDePessoaEscolhido(vinculoArvoreCliente: VinculoArvoreCliente,
		processo_filho: any) {
		processo_filho.documentos_vinculo = ArvoreGenericaAbastract.ordenaEstruturaDocumentosPessoaPartindoDoTipoDocumento(processo_filho.documentos_vinculo);
		for (let documento_vinculo of processo_filho.documentos_vinculo) {
			if (vinculoArvoreCliente.vinculoCliente.tipo_relacionamento.id === documento_vinculo.tipo_relacionamento && (documento_vinculo.tipo_documento || documento_vinculo.funcao_documental)) {
				this.criaNoAPartirDoTipoDeConteudoExistenteEmCadaDocumentoVinculo(vinculoArvoreCliente, documento_vinculo);
			}
		}
	}

	/**
	 * Verifica se o ducumento vínculo atual possui uma função documental ou
	 * um tipo de documento que contém uma funão documental.
	 * @param documento_vinculo Objeto do tipo de relacionamento, pode conter funcao documental ou tipo de documento
	 */
	private criaNoAPartirDoTipoDeConteudoExistenteEmCadaDocumentoVinculo(vinculoArvoreCliente: VinculoArvoreCliente,
		documento_vinculo: any) {
		if (documento_vinculo.funcao_documental !== undefined) {
			this.criaNosParaCadaFuncaoDocumental(documento_vinculo);
			return;
		}
		if (vinculoArvoreCliente.vinculoCliente.instancias_documento) {
			documento_vinculo.tipo_documento.instancias_documento = vinculoArvoreCliente.vinculoCliente.instancias_documento;
		}
		this.criaNosParaTipoDeDocumento(documento_vinculo);
	}

	/**
	 * Cria NO e seus respectivos filhos a partir da função documental que contém dentro dela o tipo de documento.
	 * @param documento_vinculo Objeto do tipo de relacionamento, pode conter funcao documental ou tipo de documento
	 */
	private criaNosParaCadaFuncaoDocumental(documento_vinculo: any) {
		let node = ArvoreGenericaAbastract.criaNo(documento_vinculo.funcao_documental);
		this.definirObrigatoriedadeEQTDFuncaoDocumental(node, documento_vinculo);
		for (let tipo_documento of documento_vinculo.funcao_documental.tipos_documento) {
			let nodeChildren = ArvoreGenericaAbastract.criaNo(tipo_documento);
			nodeChildren.parent = node;
			node.children.push(nodeChildren);
		}
		node.id = ArvoreGenericaAbastract.contadorNode++;
		this.listaNosVisualizacao.push(node);
	}

	/**
	 * Cria No e seus respectilhos filhos a partir do tipo de documento que contém dentro dele uma função documental.
	 * @param documento_vinculo Objeto do tipo de relacionamento, pode conter funcao documental ou tipo de documento.
	 */
	private criaNosParaTipoDeDocumento(documento_vinculo: any) {
		let node = null;
		node = ArvoreGenericaAbastract.criaNo(documento_vinculo.tipo_documento);
		this.definirObrigatoriedadeEQTDFuncaoDocumental(node, documento_vinculo);
		this.listaNosVisualizacao.push(node);
	}

	/**
	 * Verifica se o vinculo de clinte ja possui documentos provenientes de outro dossieCliente
	 * realizado pelo mesmo cliente.
	 */
	private verificaExistenciaDocumentosReutilizaveis(vinculoArvoreCliente: VinculoArvoreCliente, nodeChildren: NodeApresentacao) {
		if (!this.habilitarReuso) {
			nodeChildren.noReutilizavel = false;
			nodeChildren.existeDocumentoReuso = false;
			return;
		}
		if (undefined != vinculoArvoreCliente.vinculoCliente.documentos
			&& vinculoArvoreCliente.vinculoCliente.documentos.length > 0) {
			for (let doc of vinculoArvoreCliente.vinculoCliente.documentos) {
				if ((doc.data_hora_validade == null
					|| (moment(doc.data_hora_validade, 'DD/MM/YYYY HH:mm:ss').valueOf() > new Date().getTime()))
					&& doc.tipo_documento.permite_reuso
					&& doc.tipo_documento.id == nodeChildren.id) {
					nodeChildren.noReutilizavel = true;
					nodeChildren.existeDocumentoReuso = true;
					break;
				}
			}
		}
	}

	/**
	 * Verica se existe documentos no dossie cliente, caso não exista consulta os mesmo na API
	 * @param vinculoArvoreCliente guarda arvore de documentos do cliente
	 */
	private vericaDocumentosPeloIdDossieCliente(vinculoArvoreCliente: VinculoArvoreCliente): Promise<any> {
		if (undefined != vinculoArvoreCliente.vinculoCliente.documentos
			&& vinculoArvoreCliente.vinculoCliente.documentos.length > 0) {
			return;
		}
		return this.clienteService.getClienteById(vinculoArvoreCliente.vinculoCliente.id).toPromise();
	}

	/**
	 * Define documetos do No como obrigatório
	 * @param node node de apresentação da arvore
	 */
	private definirObrigatoriedadeEQTDFuncaoDocumental(node: NodeApresentacao, documentoVinculo: any) {
		node.obrigatorio = documentoVinculo.obrigatorio;
		node.obrigatorio ? node.quantidade_obrigatorio = 1 : node.quantidade_obrigatorio = 0;
	}

	/**
	 * 
	 * @param node 
	 */
	private definirObrigatoriedadeQtdDocsGarantia(node: NodeApresentacao, obrigatorio: boolean) {
		node.obrigatorio = obrigatorio;
		node.quantidade_maxima_obrigatoria = obrigatorio ? 1 : 0;
	}

	/**
	 * Verica se o objeto de produto a ser manipulado pela árvore está no nivel de processo ou processo fáse.
	 * @param vinculoArvore Objeto de vinculoProduto.
	 */
	private criaEstruturaDeNosDeVinculoArvoreProduto(vinculoArvore: T) {
		this.listaNosVisualizacao = [];
		if (undefined != this.processoGeraDossie) {
			for (let processo_filho of this.processoGeraDossie.processos_filho) {
				if (processo_filho.id === vinculoArvore.id) {
					this.criaNoAPartirDoProdutoEscolhido(vinculoArvore as VinculoArvoreProduto, processo_filho);
					break;
				}
			}
		}
		vinculoArvore.noApresentacao = this.listaNosVisualizacao;
	}

	/**
	 * Acha o tipo de produto escolhido comparando os tipos no processo partriarca.
	 * Caso ache um correspondente irá criar a árvore correspondente ao tipo de produto.
	 * @param vinculoArvoreProduto Objeto de vinculo de produto.
	 * @param processo_filho No da estrutura do patriarca, pode ser o NO de processo dossie ou NO filho do processo fase
	 */
	private criaNoAPartirDoProdutoEscolhido(vinculoArvoreProduto: VinculoArvoreProduto,
		processo_filho: any) {
		for (let produto_vinculado of processo_filho.produtos_vinculados) {
			if (vinculoArvoreProduto.vinculoProduto.id == produto_vinculado.id) {
				produto_vinculado.instancias_documento = vinculoArvoreProduto.vinculoProduto.instancias_documento;
				this.criaNoAPartirDosElementosDeConteudoDoProduto(produto_vinculado);
			}
		}
	}

	/**
	 * Cria os Nos da arvore de um prudoto_vinculado que tem um tipo_documento com uma função documental
	 * @param produto_vinculado Objeto que contém os elementos de conteudo do produto.
	 */
	private criaNoAPartirDosElementosDeConteudoDoProduto(produto_vinculado: any) {
		let node = null;
		let identificadorElementoPai = null;
		let documento: any = null;
		produto_vinculado.elementos_conteudo = ArvoreGenericaAbastract.ordenaElementosConteudoPartindoNoPai(produto_vinculado.elementos_conteudo);
		for (let elemento_conteudo of produto_vinculado.elementos_conteudo) {
			documento = this.verificaExistenciaDocumentoEmInstancias(produto_vinculado, elemento_conteudo);
			if (elemento_conteudo.identificador_elemento_vinculador == null
				&& elemento_conteudo.tipo_documento) {
				node = null;
				elemento_conteudo.tipo_documento.documento = documento;
				node = ArvoreGenericaAbastract.criaNo(elemento_conteudo.tipo_documento);
				node.parent = undefined;
				this.defineObrigatoriedadeQtdDocsElementoConteudo(elemento_conteudo, node);
				node.identificador_elemento = elemento_conteudo.identificador_elemento;
				node.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.listaNosVisualizacao.push(node);
				continue;
			}
			if (elemento_conteudo.identificador_elemento_vinculador == null
				&& !elemento_conteudo.tipo_documento) {
				identificadorElementoPai = elemento_conteudo.identificador_elemento;
				node = null;
				elemento_conteudo.documento = documento;
				node = ArvoreGenericaAbastract.criaNo(elemento_conteudo);
				node.id = ArvoreGenericaAbastract.contadorNode++;
				node.identificador_elemento = elemento_conteudo.identificador_elemento;
				node.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.listaNosVisualizacao.push(node);
				continue;
			}
			if (node !== null && elemento_conteudo.identificador_elemento_vinculador == identificadorElementoPai) {
				elemento_conteudo.tipo_documento.documento = documento;
				let nodeChildren = ArvoreGenericaAbastract.criaNo(elemento_conteudo.tipo_documento);
				nodeChildren.identificador_elemento = elemento_conteudo.identificador_elemento;
				nodeChildren.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.defineObrigatoriedadeQtdDocsElementoConteudo(elemento_conteudo, nodeChildren);
				nodeChildren.parent = node;
				node.children.push(nodeChildren);
			}
		}
	}

	/**
	 * Verifica se existe um documento na lista de instancias de documentos
	 * @param obj_vinculado 
	 * @param tipo_documento 
	 */
	private verificaExistenciaDocumentoEmInstancias(obj_vinculado: any, tipo_documento: any): any {
		let documento: any = null;
		if (obj_vinculado.instancias_documento) {
			obj_vinculado.instancias_documento.forEach(inst => {
				if (tipo_documento.tipo_documento && inst.documento.tipo_documento.id == tipo_documento.tipo_documento.id) {
					documento = inst.documento;
				} else {
					if (tipo_documento && inst.documento.tipo_documento.id == tipo_documento.id) {
						documento = inst.documento;
					}
				}
			});
		}
		return documento;
	}

	/**
	 * Verica se o objeto de garantia a ser manipulado pela árvore está no nivel de processo ou processo fáse.
	 * @param vinculoArvore Objeto de vinculoGarantia.
	 */
	private criaEstruturaDeNosDeVinculoArvoreGarantia(vinculoArvore: T) {
		this.listaNosVisualizacao = [];
		for (let processo_filho of this.processoGeraDossie.processos_filho) {
			if (processo_filho.id === vinculoArvore.id) {
				this.criaNoAPartirDaGarantiaEscolhida(vinculoArvore as VinculoArvoreGarantia, processo_filho);
				break;
			}
		}
		vinculoArvore.noApresentacao = this.listaNosVisualizacao;
	}

	/**
	 * Acha o tipo de garantia escolhida comparando os tipos no processo partriarca.
	 * Caso ache um correspondente irá criar a árvore correspondente ao tipo de garantai.
	 * @param vinculoArvoreGarantia Objeto de vinculo de garantia.
	 * @param processo_filho No da estrutura do patriarca, pode ser o NO de processo dossie ou NO filho do processo fase
	 */
	private criaNoAPartirDaGarantiaEscolhida(vinculoArvoreGarantia: VinculoArvoreGarantia,
		processo_filho: any) {
		processo_filho.garantias_vinculadas = ArvoreGenericaAbastract.ordenaEstruturaDocumentosGarantiaPartindoDoTipoDocumento(processo_filho.garantias_vinculadas);
		if (undefined != vinculoArvoreGarantia.vinculoGarantia.produto && "" != vinculoArvoreGarantia.vinculoGarantia.produto) {
			let garantiaVinculadaAoProduto = this.localizaGarantiaVinculadaAoProduto(vinculoArvoreGarantia, processo_filho);
			if (garantiaVinculadaAoProduto) {
				garantiaVinculadaAoProduto.instancias_documento = vinculoArvoreGarantia.vinculoGarantia.instancias_documento;
				this.criaNoAPartirDosTiposDeDocumentoGarantia(garantiaVinculadaAoProduto);
				return;
			}
		}
		for (let garantiaVinculada of processo_filho.garantias_vinculadas) {
			if (vinculoArvoreGarantia.vinculoGarantia.nome == garantiaVinculada.nome_garantia) {
				garantiaVinculada.instancias_documento = vinculoArvoreGarantia.vinculoGarantia.instancias_documento;
				this.criaNoAPartirDosTiposDeDocumentoGarantia(garantiaVinculada);
			}
		}
	}

	/**
	 * 
	 * @param vinculoArvoreGarantia Objeto de vinculo de garantia.
	 * @param processo_filho No da estrutura do patriarca, pode ser o NO de processo dossie ou NO filho do processo fase
	 */
	private localizaGarantiaVinculadaAoProduto(vinculoArvoreGarantia: VinculoArvoreGarantia, processo_filho: any) {
		for (let produto of processo_filho.produtos_vinculados) {
			let idProdutoAtual = Number(produto.id).valueOf();
			if (vinculoArvoreGarantia.vinculoGarantia.produto == idProdutoAtual && produto.garantias_vinculadas) {
				for (let garantiaVinculadaAoProduto of produto.garantias_vinculadas) {
					if (vinculoArvoreGarantia.vinculoGarantia.nome == garantiaVinculadaAoProduto.nome_garantia) {
						return garantiaVinculadaAoProduto;
					}
				}
			}
		}
	}

	/**
	 * Cria os Nos da arvore de uma garantia que tem um tipo_documento com uma função documental
	 * @param garantias_vinculadas Objeto que contém os tipo de documento com suas respectivas funções documentais.
	 */
	private criaNoAPartirDosTiposDeDocumentoGarantia(garantiaVinculada: any) {
		let documento: any = null;
		if (garantiaVinculada && (garantiaVinculada.tipos_documento && garantiaVinculada.tipos_documento.length > 0)) {
			for (let tipoDocumento of garantiaVinculada.tipos_documento) {
				documento = this.verificaExistenciaDocumentoEmInstancias(garantiaVinculada, tipoDocumento);
				tipoDocumento.documento = documento;
				let node = ArvoreGenericaAbastract.criaNo(tipoDocumento);
				this.definirObrigatoriedadeQtdDocsGarantia(node, false);
				this.listaNosVisualizacao.push(node);
			}
		}
		if (garantiaVinculada && (garantiaVinculada.funcoes_documentais && garantiaVinculada.funcoes_documentais.length > 0)) {
			for (let funcaoDocumental of garantiaVinculada.funcoes_documentais) {
				let node = null;
				for (let tipoDocumento of funcaoDocumental.tipos_documento) {
					documento = this.verificaExistenciaDocumentoEmInstancias(garantiaVinculada, tipoDocumento);
					if (node === null) {
						funcaoDocumental.documento = documento;
						node = ArvoreGenericaAbastract.criaNo(funcaoDocumental);
						this.definirObrigatoriedadeQtdDocsGarantia(node, false);
					}
					tipoDocumento.documento = documento;
					let nodeChildren = ArvoreGenericaAbastract.criaNo(tipoDocumento);
					nodeChildren.parent = node;
					node.children.push(nodeChildren);
				}
				if (node !== null) {
					node.id = ArvoreGenericaAbastract.contadorNode++;
					this.listaNosVisualizacao.push(node);
				}
			}
		}
	}

	/**
	 * Verica se o objeto de processo a ser manipulado pela árvore está no nivel de processo ou processo fáse.
	 * @param vinculoArvore Objeto de vinculoProduto.
	 */
	private criaEstruturaDeNosDeVinculoArvoreProcesso(vinculoArvore: T) {
		this.listaNosVisualizacao = [];
		for (let processo_filho of this.processoGeraDossie.processos_filho) {
			if (processo_filho.id === vinculoArvore.id) {
				let processo_fase = this.verificaArvoreProcessoFase(processo_filho, vinculoArvore)
				if (processo_fase === undefined) {
					this.criaNoAPartirDoProcessoEscolhido(processo_filho);
					break;
				}
				this.criaNoAPartirDoProcessoEscolhido(processo_fase);
				break;
			}
		}
		vinculoArvore.noApresentacao = this.listaNosVisualizacao;
	}

	/**
	 * Acha o tipo de processo escolhido comparando os tipos no processo partriarca.
	 * Caso ache um correspondente irá criar a árvore correspondente ao tipo de processo.
	 * @param vinculoArvoreProcesso Objeto de vinculo de processo.
	 * @param processo_filho No da estrutura do patriarca, pode ser o NO de processo dossie ou NO filho do processo fase
	 */
	private criaNoAPartirDoProcessoEscolhido(processo_filho: any) {
		processo_filho.elementos_conteudo = ArvoreGenericaAbastract.ordenaElementosConteudoPartindoNoPai(processo_filho.elementos_conteudo);
		if (processo_filho.elementos_conteudo != undefined) {
			this.criaNoAPartirDosElementosDeConteudoDoProcesso(processo_filho);
		}
	}

	/**
	 * Cria os Nos da arvore de um processo que tem um elemento de conteudo
	 * @param processo_filho processo atual
	 */
	private criaNoAPartirDosElementosDeConteudoDoProcesso(processo_filho: any) {
		let node = null;
		let identificadorElementoPai = null;
		processo_filho.elementos_conteudo = ArvoreGenericaAbastract.ordenaElementosConteudoPartindoNoPai(processo_filho.elementos_conteudo);
		for (let elemento_conteudo of processo_filho.elementos_conteudo) {
			if (elemento_conteudo.identificador_elemento_vinculador == null
				&& elemento_conteudo.tipo_documento) {
				node = null;
				node = ArvoreGenericaAbastract.criaNo(elemento_conteudo.tipo_documento);
				node.parent = undefined;
				this.defineObrigatoriedadeQtdDocsElementoConteudo(elemento_conteudo, node);
				node.identificador_elemento = elemento_conteudo.identificador_elemento;
				node.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.listaNosVisualizacao.push(node);
				continue;
			}
			if (elemento_conteudo.identificador_elemento_vinculador == null
				&& !elemento_conteudo.tipo_documento) {
				identificadorElementoPai = elemento_conteudo.identificador_elemento;
				node = null;
				node = ArvoreGenericaAbastract.criaNo(elemento_conteudo);
				node.id = ArvoreGenericaAbastract.contadorNode++;
				node.identificador_elemento = elemento_conteudo.identificador_elemento;
				node.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.listaNosVisualizacao.push(node);
				continue;
			}
			if (node !== null && elemento_conteudo.identificador_elemento_vinculador == identificadorElementoPai) {
				let nodeChildren = ArvoreGenericaAbastract.criaNo(elemento_conteudo.tipo_documento);
				nodeChildren.identificador_elemento = elemento_conteudo.identificador_elemento;
				nodeChildren.identificador_elemento_vinculador = elemento_conteudo.identificador_elemento_vinculador;
				this.defineObrigatoriedadeQtdDocsElementoConteudo(elemento_conteudo, nodeChildren);
				nodeChildren.parent = node;
				node.children.push(nodeChildren);
			}
		}
	}

	private defineObrigatoriedadeQtdDocsElementoConteudo(elemento_conteudo: any, node: NodeApresentacao) {
		node.obrigatorio = null == elemento_conteudo.obrigatorio ? 0 : elemento_conteudo.obrigatorio;
		node.quantidade_obrigatorio = !elemento_conteudo.quantidade_obrigatorios ? 0 : elemento_conteudo.quantidade_obrigatorios;
	}

	/**
	 * Expande todos os nos da arvore para mostrar os seus conteudos
	 */
	expandAll() {
		let vinculoArvore: VinculoArvore = this.listaVinculoArvore[this.indiceArvoreAtual];
		if (vinculoArvore.noApresentacao) {
			vinculoArvore.noApresentacao.forEach(node => {
				this.expandRecursive(node, this.expandirTodos);
			});
		}
	}

	/**
	 * Percorre a arvore de modo recursivo até chegar no Nó folha para configurar o parametro de expansão do Nó
	 * @param node No da arvore
	 * @param isExpand indicará se no será expandido ou não
	 */
	private expandRecursive(node: NodeAbstract, isExpand: boolean) {
		node.expanded = isExpand;
		if (node.children) {
			node.children.forEach(childNode => {
				if (childNode.leaf != true) {
					this.expandRecursive(childNode, isExpand);
				} else if ((childNode as NodeApresentacao).acao_documento == "rejeitado") {
					ArvoreGenericaAbastract.hideNode(childNode as NodeApresentacao);
				}
			});
		}
	}

	/**
	 * Verifica se a arvore selecionada atual é de vinculo cliente e busca por documentos de dossie cliente
	 * que possam ser reutilizados
	 */
	habilitarReusoDocumentos() {
		let vinculoArvore: VinculoArvore = this.listaVinculoArvore[this.indiceArvoreAtual];
		if (vinculoArvore instanceof VinculoArvoreCliente) {
			let existeDocumentoReutilizavelArvore = false;
			if (this.habilitarReuso) {
				let promise: Promise<any> = this.vericaDocumentosPeloIdDossieCliente(vinculoArvore);
				if (undefined == promise) {
					existeDocumentoReutilizavelArvore = this.percorreArvoreDocumentos(vinculoArvore, existeDocumentoReutilizavelArvore);
					this.mostraMsgCasoNaoExistaDocumentosReutilizaveis(existeDocumentoReutilizavelArvore);
					return;
				}
				promise.then(response => {
					(vinculoArvore as VinculoArvoreCliente).vinculoCliente.documentos = response.documentos;
					existeDocumentoReutilizavelArvore = this.percorreArvoreDocumentos(vinculoArvore, existeDocumentoReutilizavelArvore);
					this.mostraMsgCasoNaoExistaDocumentosReutilizaveis(existeDocumentoReutilizavelArvore);
				});
			} else {
				this.percorreArvoreDocumentos(vinculoArvore, existeDocumentoReutilizavelArvore);
			}
		}
	}

	/**
	 * Mostra dialogo para usuario indicando que não existe documento para ser reutilizado nesse vinculo.
	 */
	private mostraMsgCasoNaoExistaDocumentosReutilizaveis(existeDocumentoReutilizavelArvore: boolean) {
		if (!existeDocumentoReutilizavelArvore) {
			this.clearAllMessages();
			this.addMessageError('Não existem documentos de dossiê cliente para serem reutilizados nesse vínculo.');
		}
	}

	/**
	 * Percorre arvore de documentos verificando se existe documentos reutilizados
	 * @param vinculoArvore instantica que guarda o tipo de vinculo usado na árvore
	 */
	private percorreArvoreDocumentos(vinculoArvore: VinculoArvore, existeDocumentoReutilizavelArvore: boolean): boolean {
		vinculoArvore.noApresentacao.forEach(node => {
			existeDocumentoReutilizavelArvore = this.verificaReutilizacaoTipoDocumento(node, vinculoArvore, existeDocumentoReutilizavelArvore);
		});
		return existeDocumentoReutilizavelArvore;
	}

	/**
	 * Verifica se para o tipo de documnto do NO possui documentos que possam 
	 * ser reutilizados
	 * @param node node NO de arvore de documentos.
	 * @param vinculoArvore instantica que guarda o tipo de vinculo usado na árvore
	 */
	private verificaReutilizacaoTipoDocumento(node: NodeAbstract, vinculoArvore: VinculoArvore,
		existeDocumentoReutilizavelArvore: boolean): boolean {
		if (node.children && node.children.length > 0) {
			let naoEPasta = false;
			for (let child of node.children) {
				if (!child.leaf) {
					this.verificaExistenciaDocumentosReutilizaveis(vinculoArvore, child as NodeApresentacao);
					if ((child as NodeApresentacao).existeDocumentoReuso) {
						existeDocumentoReutilizavelArvore = true;
					}
				} else {
					naoEPasta = true;
					break;
				}
			}
			if (!naoEPasta) {
				return existeDocumentoReutilizavelArvore;
			}
		}
		this.verificaExistenciaDocumentosReutilizaveis(vinculoArvore, node as NodeApresentacao);
		if ((node as NodeApresentacao).existeDocumentoReuso) {
			existeDocumentoReutilizavelArvore = true;
		}
		return existeDocumentoReutilizavelArvore;
	}

	/**
	 * Mostra Documentos ativos ou inativos dependendo da escolha do usuario na tela
	 */
	showAtivosInativos() {
		let vinculoArvore: VinculoArvore = this.listaVinculoArvore[this.indiceArvoreAtual];
		vinculoArvore.noApresentacao.forEach(node => {
			this.buscaDocumentosAtivoOuInativos(node, this.exibirInativos);
		});
	}

	/**
	 * Busca de modo recursivo os ducumentos a serem exibidos na tela
	 * @param node No de Visualizacao de docuemento
	 * @param isInativo variavel booleana: true para docs inativos e false para docs ativos
	 */
	private buscaDocumentosAtivoOuInativos(node: NodeApresentacao, isInativo) {
		if (node.children) {
			node.children.forEach(childNode => {
				if (!childNode.leaf) {
					this.buscaDocumentosAtivoOuInativos(childNode, isInativo);
				} else {
					this.mostraDocsInativos(childNode, isInativo);
					this.escondeDocsInativos(childNode, isInativo);
				}
			});
		}
	}

	/**
	 * Mostra documentos ativos
	 * @param node No de Visualizacao de docuemento
	 * @param isInativo variavel booleana: true para docs inativos e false para docs ativos
	 */
	private escondeDocsInativos(node: NodeApresentacao, isInativo: boolean) {
		if (!isInativo && UtilsArvore.isDocumentoStatusNegativo(node)) {
			ArvoreGenericaAbastract.hideNode(node);
		}
	}

	/**
	 * Mostra documentos inativos
	 * @param node No de Visualizacao de docuemento
	 * @param isInativo variavel booleana: true para docs inativos e false para docs ativos
	 */
	private mostraDocsInativos(node: NodeApresentacao, isInativo: boolean) {
		if (isInativo && UtilsArvore.isDocumentoStatusNegativo(node)) {
			node.parent.expanded = true;
			if (node.parent.parent) {
				node.parent.parent.expanded = true;
			}
			ArvoreGenericaAbastract.showNode(node);
		}
	}

	/**
	 * Método que emite informação sobre o nó selecionado.
	 * @param node 
	 */
	onSelect(node, indiceArvoreAtual) {
		this.selecionadoFolhaDoNoDaArvoreChange.emit(node);
		this.selectedDocumentNode = node;
		this.selectedDocumentNode.partialSelected = true;
		this.definindoNoAnterior(node, indiceArvoreAtual);
	}

	/**
	 * Configura as informações do tipo de documento selecionado anterior em uma determinada
	 * arvore para reclassificação.
	 * @param node no anterior
	 * @param indiceArvoreAtual indice da ultima arvore selecionada pelo usuario
	 */
	private definindoNoAnterior(node: any, indiceArvoreAtual: any) {
		this.tipoDocumentoArvoreGenericaAnterior = new TipoDocumentoArvoreGenerica();
		this.tipoDocumentoArvoreGenericaAnterior.nome = node.parent.label;
		this.tipoDocumentoArvoreGenericaAnterior.indexArvore = indiceArvoreAtual;
		this.tipoDocumentoArvoreGenericaAnterior.idNodeApresentacao = node.parent.parent == undefined ? node.parent.id : node.parent.parent.id;
		this.tipoDocumentoArvoreGenericaAnteriorChange.emit(this.tipoDocumentoArvoreGenericaAnterior);
	}

	/**
	 * Método que verifica nó selecionado.
	 * @param $event Evento selecionado
	 * 
	 */
	nodeSelect($event, indiceArvoreAtual) {
		if ($event.originalEvent.target.innerText != LINKS.REUTILIZAR) {
			this.activeUpload = false;
			if ($event.node.leaf || ($event.node.children.length > 0 && $event.node.children[0].leaf)) {
				this.selectedFile = undefined;
				this.images = [];
				if ($event.node.leaf) {
					this.selectedFile = $event.node
				}
				if ($event.node.children
					&& $event.node.children[0].acao_documento != STATUS_DOCUMENTO.REJEITADO) {
					this.selectedFile = $event.node.children[0]
				}
				if (this.selectedFile !== undefined) {
					this.loadService.show();
					this.drawImagePrev(this.selectedFile);
					this.selecionadoFolhaDoNoDaArvoreChange.emit(this.selectedFile);
				}
			}
			if (this.vinculoArvore) {
				this.carregarDadosArvoreTipoDocumento($event);
			}
			
			this.definirComoReclassificacao($event, indiceArvoreAtual);
		}
	}

	/**
	 * Inicializa o objeto arvoreTipoDocumento; que contem propriedades do tipoDocumento e arvore selecionado
	 * @param $event 
	 */
	private carregarDadosArvoreTipoDocumento($event: any) {
		let arvoreTipoDocumento = new ArvoreTipoDocumento();
		if (this.activeUpload) {
			arvoreTipoDocumento.clickTipoDocumento = false;
		} else {
			this.images = [];
			arvoreTipoDocumento.clickTipoDocumento = true;
		}
		arvoreTipoDocumento.vinculoArvoreSelecionado = UtilsArvore.castTypeVinculoArvore(this.vinculoArvore);
		this.recuperaIdentificadorLabelTipoDocumento($event.node, arvoreTipoDocumento);
		arvoreTipoDocumento.actionMoment = new Date();
		if (arvoreTipoDocumento.tipoDocumentoSelecionado > 0) {
			this.imagensChange.emit(this.images);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.TIPO_DOCUMENTO_SELECIONADO_ARVORE, JSON.stringify(arvoreTipoDocumento));			
			this.arvoreTipoDocumentoSelecionadoChange.emit(arvoreTipoDocumento);
		} else {
			this.imagensChange.emit(this.images);
		}
	}

	/**
	 * Inicializa o id e label de acordo o no da arvore; sendo folha considera o pai
	 * @param $event 
	 * @param arvoreTipoDocumento 
	 */
	private recuperaIdentificadorLabelTipoDocumento(node: NodeApresentacao, arvoreTipoDocumento: ArvoreTipoDocumento) {
		arvoreTipoDocumento.tipoDocumentoSelecionado = node.leaf ? node.parent.id : node.id;
		arvoreTipoDocumento.nomeDocumentoSelecionado = node.leaf ? node.parent.label : node.label;
	}

	private definirComoReclassificacao(event: any, indiceArvoreAtual) {
		if ((undefined == event.node.children
			&& event.node.acao_documento != STATUS_DOCUMENTO.REJEITADO)
			|| (event.node.children.length > 0
				&& event.node.children[0].leaf
				&& event.node.children[0].acao_documento != STATUS_DOCUMENTO.REJEITADO)) {
			this.documentoAClassificar = false;
			this.documentoAClassificarChange.emit(this.documentoAClassificar);
			this.incluirImagem = false;
			this.incluirImagemChange.emit(this.incluirImagem);
			if (event.node.leaf) {
				this.selectedDocumentNode = event.node;
				this.selectedDocumentNode.partialSelected = true;
				this.definindoNoAnterior(event.node, indiceArvoreAtual);
			}
			if (event.node.children && event.node.children.length > 0 && event.node.children[0].leaf) {
				this.selectedDocumentNode = event.node.children[0];
				this.selectedDocumentNode.partialSelected = true;
				this.definindoNoAnterior(event.node.children[0], indiceArvoreAtual);
			}
		}
	}

	/**
	 * Método responsável por preparar apresentação da imagem.
	 * @param imagem 
	 */
	drawImagePrev(imagem) {
		if (!imagem) return false;
		const page = imagem.pages[0];
		this.images = [];
		this.activeUpload = true;
		if (page.data.id != null) {
			this.recuperarImagem(page, imagem);
		} else {
			this.adicionarImagensVisualizacao(page, imagem);
		}
	}


	/**
	 * Método que adiciona imagem na lista de apresentação
	 * @param page 
	 * @param imagem 
	 */
	private adicionarImagensVisualizacao(page, imagem) {
		for (const item of imagem.uri) {
			this.images.push(
				{
					image: item.image,
					checked: true,
					data: page.data,
					reclassificar: true,
					primeiraPagina: item.primeiraPagina,
					indiceDocListPdfOriginal: item.indiceDocListPdfOriginal,
					totalPaginas: item.totalPaginas,
					type: item.type,
					source: FonteDocumento.UPLOAD
				}
			);
		}
		this.imagensChange.emit(this.images);
		this.loadService.hide();
	}

	/**
	 * Método responsável por recuperar imagem.
	 * @param page 
	 * @param imagem 
	 */
	private recuperarImagem(page, imagem) {
		this.dossieService.getConsultarImagemGet(page.data.id).subscribe(response => {
			page.uri = [];
			this.images = [];
			page.uri.push(response.binario ? response.binario : IMAGEM_DEFAUT.img);
			imagem.uri[0].push(response.binario ? response.binario : IMAGEM_DEFAUT.img);
			this.images.push(
				{
					id: response.id,
					image: response.binario ? response.binario : IMAGEM_DEFAUT.img,
					checked: true,
					data: page.data,
					excluded: imagem.removido_arvore,
					label: imagem.label,
					type: response.mime_type,
					reclassificar: true,
					tipoDocumentoId: response.tipo_documento.id,
					analise_outsourcing: response.analise_outsourcing,
					source: FonteDocumento.UPLOAD
				}
			);			
			this.imagensChange.emit(this.images);
		}, error => {
			this.loadService.hide();
			let msg = error.error ? error.error.mensagem : error.message;
			this.addMessageError(msg);
			throw error;
		});
	}

	verificarArvoreNova(): boolean {
		let listaNOsArvore = this.listaVinculoArvore[this.indiceArvoreAtual].noApresentacao;
		if (listaNOsArvore) {
			this.isPossuiDocNaoSalvo = true;
			listaNOsArvore.forEach(node => {
				this.isPossuiDocumentoSalvo(node);
			});
		}
		return !this.isPossuiDocNaoSalvo;
	}

	/**
	 * Percorre a arvore de modo recursivo até chegar no Nó folha para verificar se existe documentos já salvos
	 * @param node No da arvore
	 * @param isExpand indicará se no será expandido ou não
	 */
	private isPossuiDocumentoSalvo(node: NodeAbstract) {
		if (node.children) {
			node.children.forEach(childNode => {
				if (childNode.leaf != true) {
					this.isPossuiDocumentoSalvo(childNode);
				} else {
					this.verificarSeDocumentoExisteNoBanco(childNode as NodeApresentacao);
				}
			});
		}
	}

	/**
	 * Verifica se documento carregado possui instância já salva no banco.
	 * @param childNode 
	 */
	private verificarSeDocumentoExisteNoBanco(childNode: NodeApresentacao) {
		let nodeApresentacao = childNode;
		if (nodeApresentacao.leaf && nodeApresentacao.pages) {
			nodeApresentacao.pages.forEach(page => {
				if (page.possui_instancia) {
					this.isPossuiDocNaoSalvo = false;
					return;
				}
			});
		}

	}

	/**
	 * Adiciona documentos a serem excluidos na lista de de documentos a excluir da arvore
	 */
	addDocumentoExclusao() {
		if (this.selectedDocumentNode != undefined && this.selectedDocumentNode.selectable) {
			this.listaNosVisualizacao.forEach(node => {
				this.replicaMarcacaoParaMesmoTipoDocumento(node, this.selectedDocumentNode.selectable);
			});
			this.listaExcluir.push(this.selectedDocumentNode.pages[0]);
			return;
		}
		if (this.selectedDocumentNode != undefined && !this.selectedDocumentNode.selectable) {
			this.listaNosVisualizacao.forEach(node => {
				this.replicaMarcacaoParaMesmoTipoDocumento(node, this.selectedDocumentNode.selectable);
			});
			this.listaExcluir.splice(this.listaExcluir.indexOf(this.selectedDocumentNode.pages[0]), 1);
		}

	}

	/**
	 * Replica o a marcação do nó apresentação selecionado
	 * @param node 
	 * @param selectable 
	 */
	private replicaMarcacaoParaMesmoTipoDocumento(node: NodeAbstract, selectable: boolean) {
		if (node.children) {
			node.children.forEach(childNode => {
				if (childNode.leaf != true) {
					this.replicaMarcacaoParaMesmoTipoDocumento(childNode, selectable);
				} else if (childNode.parent.id == this.selectedDocumentNode.id) {
					childNode.selectable = selectable;
					this.atualizarListaExclusaoNoApresentacao(childNode as NodeApresentacao, selectable);
				}
			});
		}
	}

	/**
	 * Atualiza a lista de atualização do no apresentação
	 * @param childNode 
	 * @param selectable 
	 */
	private atualizarListaExclusaoNoApresentacao(childNode: NodeApresentacao, selectable: boolean) {
		if (selectable) {
			this.listaExcluir.push(childNode.pages[0]);
		} else {
			this.listaExcluir.splice(this.listaExcluir.indexOf(childNode.pages[0]), 1);
		}
	}

	/**
	 * Exclui os documentos que não possuem instância diretamente. Caso possuirem instancia,
	 * emite-se um evento para serem excluidos ao salvar o dossie clinte no banco
	 */
	excluiDocumentos() {
		for (let nodeAExcluir of this.listaExcluir) {
			if (nodeAExcluir.possui_instancia && (nodeAExcluir.uri && nodeAExcluir.uri.length != 0)) {
				nodeAExcluir.parent.setRemovidoArvore();
				this.listaExcluirChange.emit(nodeAExcluir);
				this.mudancaSalvaService.setIsMudancaSalva(false);
				continue;
			}
			this.verificaPastaContemDocsReutilizaveis(nodeAExcluir);
			UtilsArvore.removeNodeInTree(this.listaVinculoArvore[this.indiceArvoreAtual].noApresentacao, nodeAExcluir.parent, TIPO_ARVORE.ARVORE_DOSSIE_PRODUTO);
		}
		this.limparVisualizadorAoExcluir();
	}

	/**
	 * Para cada exclusão de documentos feita na pasta, é verificado se a pasta possui documentos
	 * que possam ser reutilizados para o tipo de documento que ela representa.
	 */
	private verificaPastaContemDocsReutilizaveis(node: NodeApresentacao, ) {
		if (node.parent.parent.noReutilizavel && node.parent.parent.children.length == 1) {
			node.parent.parent.existeDocumentoReuso = true;
		}
	}

	private limparVisualizadorAoExcluir() {
		if (this.listaExcluir.length > 0) {
			this.listaExcluir = [];
			this.images = [];
			this.imagensChange.emit(this.images);
		}
	}

	/**
	 * Busca se existe tal termo usado no campo de filtro nos Nos de uma determinada arvore.
	 * @param input termo de pesquisa do usuario na tela
	 */
	onInputFiltro(input) {
		if (input && input.value.length > 1) {
			//Marca os nodes que batem com o filtro
			UtilsArvore.verifyNodes(this.listaCopy, input.value);
			const nodesSelected: NodeApresentacao[] = [];
			this.recuperarNodesSelecionados(nodesSelected);
			const newTree: NodeApresentacao[] = [];
			this.montarArvoreDeAcordoNoSelecionado(nodesSelected, newTree);
		} else {
			this.listaNosVisualizacao = Object.assign([], this.listaCopy);
		}
	}

	/**
	 * Re-monta a árvore de acordo com os nodes selecionados
	 * @param nodesSelected no selecionado a partir do termo de busca
	 * @param newTree nova lista de nos que ira guardar os nos contidos no filtro de busca
	 */
	private montarArvoreDeAcordoNoSelecionado(nodesSelected: NodeApresentacao[], newTree: NodeApresentacao[]) {
		if (nodesSelected && nodesSelected.length > 0) {
			for (const item of nodesSelected) {
				if (item.parent == null) {
					if (UtilsArvore.getNodeInTree(newTree, item) == null) {
						const newItem = Object.assign({}, item);
						newTree.push(newItem);
					}
				}
				else {
					this.criarListaArvoreConformeFiltro(newTree, item);
				}
			}
			this.listaNosVisualizacao = newTree;
		}
		else {
			this.listaNosVisualizacao = [];
		}
	}

	/**
	 * Recupera os nodes que foram selecionado
	 * @param nodesSelected Nos selecionados pelo filtro de presquisa
	 */
	private recuperarNodesSelecionados(nodesSelected: NodeApresentacao[]) {
		for (const item of this.listaCopy) {
			let auxNode = null;
			do {
				auxNode = UtilsArvore.grabNextNodeFilteredInNode(item);
				if (auxNode) {
					nodesSelected.push(auxNode);
				}
			} while (auxNode != null);
		}
	}

	/**
	 * Metodo que ira cria a lista de arvore conforme consulta
	 * @param newTree Nova Arvore a ser montada
	 * @param item O No da Arvore a ser montada
	 */
	private criarListaArvoreConformeFiltro(newTree: NodeApresentacao[], item: NodeApresentacao) {
		let parent: NodeApresentacao = UtilsArvore.getNodeInTree(newTree, item.parent) as NodeApresentacao;
		if (parent == null) {
			parent = Object.assign({}, item.parent);
			parent.expanded = true;
			parent.children = [];
			if (parent.parent) {
				this.montarNodeNaArvore(newTree, parent);
			}
			else {
				newTree.push(parent);
			}
		}
		const newItem = Object.assign({}, item);
		newItem.expanded = true;
		newItem.parent = parent;
		parent.children.push(newItem);
	}

	/**
	 * Responsavel por monta o novo documento e verificar se existe a consulta
	 * @param newTree nova documento
	 * @param parent Parente na qual existe a consulta
	 */
	private montarNodeNaArvore(newTree: NodeApresentacao[], parent: NodeApresentacao) {
		let rootParent: NodeApresentacao = UtilsArvore.getNodeInTree(newTree, parent.parent) as NodeApresentacao;
		if (rootParent == null) {
			rootParent = Object.assign({}, parent.parent);
			rootParent.children = [];
			newTree.push(rootParent);
		}
		rootParent.children.push(parent);
	}

	/**
	 * Contando quantidade de nos que cada arvore tem
	 */
	posicaoAtualNode() {
		this.posicaoAtual++
	}
}