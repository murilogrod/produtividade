import { DialogService } from 'angularx-bootstrap-modal';
import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { VinculoArvore } from '../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { AlertMessageService } from '../../services';
import { TIPO_ARVORE } from '../../constants/constants';
import { Utils } from '../../utils/Utils';
import { ArvoreTipoDocumento } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/arvore-tipo-documento';
import { UtilsArvore } from '../arvore-generica/UtilsArvore';
import { ArvoreGenericaComponent } from '../arvore-generica/arvore-generica.component';

declare var $: any;

@Component({
	selector: 'classificacao-generico',
	templateUrl: './classificacao-generico.component.html',
	styleUrls: ['./classificacao-generico.component.css'],
	encapsulation: ViewEncapsulation.None
})

export class ClassificacaoGenericoComponent extends AlertMessageService implements OnInit, OnChanges {
	constructor() {
		super();
	}

	messagesSuccess: string[];
	messagesInfo: string[];
	messagesError: string[];

	@Input() listaVinculoArvore: Array<VinculoArvore>;
	@Input() tipoDossie: Number;
	@Input() listaIndexArvoreSelecionada: number[] = [];
	@Input() arvoreTipoDocumento: ArvoreTipoDocumento;
	@Input() clickArvoreTipoDocumento: ArvoreTipoDocumento;
	@Input() tipoDocumento: any;
	@Input() idSeletor: string;
	@Input() desativaComboClassificacao: boolean;
	@Output() listaIndexArvoreSelecionadaChange: EventEmitter<number[]> = new EventEmitter<number[]>();
	@Output() tipoDocumentoChange: EventEmitter<any> = new EventEmitter<any>();
	@Output() idSeletorChange: EventEmitter<string> = new EventEmitter<string>();
	@Input()  ocultarComboTipoDocumento: boolean;
	@Output() ocultarComboTipoDocumentoChange: EventEmitter<boolean> = new EventEmitter<boolean>();
	@Input() dossieCliente: boolean;

	elemento: string = "";
	listaArvore: any[];
	listaTipoDocumento: any[];
	habilitarComboTipoDocumento: boolean;
	habilitarComboArvore: boolean;
	habilitarAdicionarArvore: boolean = true;
	listatipoValueArvore: any[];
	tipoValue: any;

	ngOnInit() {
		this.listatipoValueArvore = [];
		this.verificarTipoDossie();
	}

	preencherTipoDocumento() {
		this.onchangeComboListaArvore(this.listatipoValueArvore);
		this.emitrListaArvore();
	}

	ativarClassificacao() {
		this.tipoDocumentoChange.emit(this.tipoValue);
	}

	/**
	 * Verificar o Tipo de Dossiê e conforme resposta oculta e mostra a combo lista de arvore
	 * Habilita e desabilita Lista Tipo de Documento
	 */
	verificarTipoDossie() {
		if (!this.desativaComboClassificacao) {
			this.habilitarComboArvore = this.tipoDossie == TIPO_ARVORE.ARVORE_DOSSIE_CLIENTE;
			this.habilitarComboTipoDocumento = !this.habilitarComboArvore;
			if (this.habilitarComboArvore) {
				this.montarListaTipoDocumento(0);
			} else {
				this.montarListaArvore();
			}
			return;
		}
		return;

	}

	ngOnChanges() {
		if (this.idSeletor != undefined && this.idSeletor != "") {
			this.limparVerificadorSeletor();
		}
		this.clickArvoreTipoDocumento = this.arvoreTipoDocumento && this.arvoreTipoDocumento.clickTipoDocumento ? this.arvoreTipoDocumento : null;
		this.selectArvoreTipoDocumento(this.arvoreTipoDocumento !== undefined);
	}


	private selectArvoreTipoDocumento(value: boolean) {
		if (!this.listaArvore) {
			this.montarListaArvore();
		}
		setTimeout(() => {
			if (value && this.listaArvore && this.arvoreTipoDocumento && this.arvoreTipoDocumento.vinculoArvoreSelecionado) {
				this.listatipoValueArvore = [];
				this.listaTipoDocumento = [];
				for (let i = 0; i < this.listaArvore.length; i++) {
					if (this.listaArvore[i].id == this.arvoreTipoDocumento.vinculoArvoreSelecionado) {
						this.listatipoValueArvore.push(this.listaArvore[i]);
						this.emitrListaArvore();
						this.preencherListaDocumento(this.listaArvore[i].index);
						let tipoDoc = this.listaTipoDocumento.find(tp => tp.id == this.arvoreTipoDocumento.tipoDocumentoSelecionado);				
						this.tipoValue = !tipoDoc ? '' : tipoDoc;
						this.ocultarComboTipoDocumento = !tipoDoc;
						this.ocultarComboTipoDocumentoChange.emit(this.ocultarComboTipoDocumento);
						this.tipoDocumentoChange.emit(this.tipoValue);
						break;
					}
				}
			}else{
				this.tipoValue = null;
			}
		}, 300);
	}

	private emitrListaArvore() {
		this.listatipoValueArvore = Object.assign([], this.listatipoValueArvore);
		this.listaIndexArvoreSelecionadaChange.emit(this.listatipoValueArvore);
	}

	private limparVerificadorSeletor() {
		this.idSeletor = "";
		this.idSeletorChange.emit(this.idSeletor);
	}
	/**
	 * Armazena Lista de Arvore Selecionada Para Classificar
	 */
	onchangeComboListaArvore(lista: any[]) {
		this.onChangeListaTiposDocumentos(null, lista);
	}

	/**
	 * Método para lista Tipos de Documentos
	 * @param idxArvore posição da árvore
	 */
	onChangeListaTiposDocumentos(idxArvore: any, listaArvore: any[]) {
		this.listaTipoDocumento = [];
		this.listaIndexArvoreSelecionada = [];
		if (listaArvore == null) {
			this.preencherListaDocumento(idxArvore);
		} else {
			let listaDocEmComum = [];
			let listaAnterior = [[]];
			this.montarMatriz(listaArvore, listaAnterior);
			listaArvore.forEach((element, idx) => {
				this.carregarTipoDocumento(element.index, idx, listaAnterior, listaDocEmComum);
			});

			this.listaTipoDocumento = listaAnterior.length > 0 && listaDocEmComum.length > 0 ? listaDocEmComum : this.listaTipoDocumento;
			this.habilitarComboTipoDocumento = this.listaTipoDocumento.length == 0;
			this.listaIndexArvoreSelecionada = Object.assign([], this.listaIndexArvoreSelecionada);
			this.listaIndexArvoreSelecionadaChange.emit(this.listaIndexArvoreSelecionada);
		}
	}

	private carregarTipoDocumento(indice: number, idx: number, listaAnterior: any[][], listaDocEmComum: any[]) {
		this.listaIndexArvoreSelecionada.push(indice);
		if (idx == 0) {
			this.preencherListaDocumento(indice);
			listaAnterior[idx][0] = this.listaTipoDocumento;
		}
		else {
			this.listaTipoDocumento = [];
			this.preencherListaDocumento(indice);
			let listaAux = this.listaTipoDocumento;
			this.montarListaTipoDocuemntoEmComum(listaAnterior, listaAux, listaDocEmComum);
			listaAnterior[idx][0] = this.listaTipoDocumento;
		}
	}

	/**
	 * Método responsavel por percorrer e verificar os tipos de documentos em comum
	 * @param listaAnterior matriz que recebe lista de todos os documentos para percorrer
	 * @param listaAux lista atual
	 * @param listaDocEmComum lista em comum
	 */
	private montarListaTipoDocuemntoEmComum(listaAnterior: any[][], listaAux: any[], listaDocEmComum: any[]) {
		this.listaTipoDocumento = [];
		for (let i = 0; i < listaAnterior.length; i++) {
			if (listaAnterior[i][0] != undefined) {
				let existe = false;
				listaAnterior[i][0].forEach(function (el) {
					listaAux.forEach(function (elm) {
						if (el.nome === elm.nome) {
							existe = true;
							if (this.listaDocEmComum.some(function (el) { return elm.nome === el.nome })) {
								listaDocEmComum.push(el);
							}
						}
					});
					if (!existe) {
						listaDocEmComum = [];
					}
				});
			}
		}
	}
	/**
	 * Inicializar a lista de tipo de documento Anterior
	 * @param listaArvore 
	 * @param listaAnterior 
	 */
	private montarMatriz(listaArvore: any[], listaAnterior: any[][]) {
		for (let j = 0; j < listaArvore.length; j++) {
			listaAnterior[j] = [];
		}
	}
	/**
	 * Com base do index da arvore monta os tipo de Documentos
	 * @param idxArvore 
	 */
	private preencherListaDocumento(idxArvore: any) {
		const vinculoArvore = this.listaVinculoArvore[idxArvore];
		this.habilitarAdicionarArvore = true;
		this.habilitarComboTipoDocumento = true;
		if (null != vinculoArvore) {
			this.percorreNode(vinculoArvore.noApresentacao, idxArvore);
		}
	}

	/**
	 * 
	 * @param primeiroNode Primeiro No da árvore
	 * @param idIndex id do NO filho para adicionar a lista
	 */
	private percorreNode(primeiroNode: any, idIndex) {
		primeiroNode.forEach(node => {
			let nodeGlobal: any = {};
			if (null == idIndex || (null != node.id && idIndex != node.id)) {
				idIndex = node.id;
			}
			if (null != node.children && node.children.length > 0) {
				if (!this.percorreFilhoEVerificaSeEOUltimo(node)) {
					this.percorreNode(node.children, idIndex);
				} else {
					this.addListaTipoDocumento(nodeGlobal, idIndex, node);
				}
			} else {
				this.addListaTipoDocumento(nodeGlobal, idIndex, node);
			}
			this.habilitarComboTipoDocumento = this.listaTipoDocumento.length == 0;
		});
	}

	/**
	 * Metodo nescessario para verificar se E o Ultimo FILHO
	 * @param node No Children para verificar se e o Ultimo Filho
	 */
	private percorreFilhoEVerificaSeEOUltimo(node: any) {
		let verificaFinalFolha: boolean = false;
		node.children.forEach(children => {
			if (null == children.children) {
				verificaFinalFolha = true;
			}
		});
		return verificaFinalFolha;
	}

	/**
	 * 
	 * @param nodeGlobal Objeto para montar lista de Documento
	 * @param idIndex Index do NO para setar na lista
	 * @param node Label do NO para setar na lista
	 */
	private addListaTipoDocumento(nodeGlobal: any, idIndex: any, node: any) {
		nodeGlobal.id = idIndex;
		nodeGlobal.nome = node.label;
		nodeGlobal.ativo = ArvoreGenericaComponent.tipologiasDocumentais.tipos_documento.some(tp => tp.id == idIndex && tp.ativo);
		if (nodeGlobal.ativo) {
			this.verificarDuplicidadeListaTipoDocumento(nodeGlobal);
		}
	}

	verificarDuplicidadeListaTipoDocumento(conteudo: any) {
		if (this.listaTipoDocumento != undefined && !this.listaTipoDocumento.some(function (el) { return el.nome === conteudo.nome })) {
			this.listaTipoDocumento.push(conteudo);
		}

	}

	/**
	 * Se lista os Tipos de Tocumento
	 */
	montarListaTipoDocumento(index: any) {
		this.onChangeListaTiposDocumentos(index, null);
	}

	/**
	 * Montar Lista Arvore, caso não seja dossiê Cliente
	 */
	montarListaArvore() {
		this.listaArvore = [];
		for (let i = 0; i < this.listaVinculoArvore.length; i++) {
			let arvGlobal: any = this.inicializaObjetoArvore(this.listaVinculoArvore[i]);
			let x;
			x = Utils.verificarInstanciaArvore(this.listaVinculoArvore[i], x, "identificador");
			arvGlobal.nome = x;
			if (this.listaVinculoArvore[i].noApresentacao && this.listaVinculoArvore[i].noApresentacao.length > 0) {
				arvGlobal.index = i;
				this.listaArvore.push(arvGlobal);
			}
		}
	}

	/**
	 * Inicializa o objeto da select arvore
	 * @param vinculoArvore 
	 */
	private inicializaObjetoArvore(vinculoArvore: VinculoArvore) {
		let arvGlobal: any = {};
		arvGlobal.id = UtilsArvore.castTypeVinculoArvore(vinculoArvore);
		return arvGlobal;
	}

}