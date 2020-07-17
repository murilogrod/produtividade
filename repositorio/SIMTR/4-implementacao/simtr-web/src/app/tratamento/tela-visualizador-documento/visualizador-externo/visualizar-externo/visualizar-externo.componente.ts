import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewEncapsulation, HostListener } from '@angular/core';
import { TIPO_ARVORE_DOCUMENTO } from 'src/app/constants/constants';
import { DocumentImage } from 'src/app/documento/documentImage';
import { DossieService } from 'src/app/dossie/dossie-service';
import { LoaderService, AlertMessageService } from 'src/app/services';
import { ConversorDocumentosUtil } from 'src/app/documento/conversor-documentos/conversor-documentos.util.service';
import { PDFJS } from 'pdfjs-dist/build/pdf';
import { IMAGEM_DEFAUT } from 'src/app/constants/imgDefaut';

declare var $: any;
@Component({
	selector: 'app-visualizar-externo',
	templateUrl: './visualizar-externo.component.html',
	styleUrls: ['./visualizar-externo.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class VisualizarExternoComponent extends AlertMessageService implements OnInit, AfterViewInit, OnChanges {
	constructor(
		private loadService: LoaderService,
		private dossieService: DossieService,
		private conversorDocsUtil: ConversorDocumentosUtil
	) {
		super();
	}

	@Input() dossieProduto: any;
	@Input() listDocumentosImagens: any[];
	
	// significa documento carregado da tela de tras
	@Input() listDocumentosImagens2: any[];

	@Input() listDocumentosImagensComparar: any[];
	@Input() visualizadorDois: boolean;
	@Input() idVisualizar: number;
	@Input() listaDocumentacao: any[];
	@Input() alutraMaxHeigthImg: number;
	@Input() idCombo: number;
	@Input() layoutVerticalA: boolean;
	@Input() layoutVerticalB: boolean;
	@Input() hasMapListaCombo;
	@Input() chekListFase: boolean;

	listaDocumentacao2: any[];
	listaDocumento2: any[];

	rotacaoImg = 0;
	escalaImg = 0.7;
	maisZomm: number = 1;
	rotacionar: number = 0;
	currentImage: any;
	divCanvas: any;
	tipo: any;
	translationOffsetX = 0;
	translationOffsetY = 0;
	imgAtual: number = 0;
	imgAtualCompara: number = 0;
	listaDocumento: any[];
	itemArvore: string;
	itemDoc: string;
	images: DocumentImage[] = [];
	imagesCompara: DocumentImage[] = [];
	updtaeCombo: boolean = false;
	comboDocumentacao;
	comboDocumento;
	inicializarVisualizador: boolean = true;
	filtroBrilhoImg: number = 100;
	filtroContrasteImg: number = 100;
	openShowPortal: boolean;
	textoTeclaDeAtalho: string;
	existeCheckList: boolean;
	showPopup: boolean;

	ngOnInit() {
		this.listaDocumentacao2 = this.listaDocumentacao;
		this.inicializarVisualizador = false;
		this.textoTeclaDeAtalho = this.idVisualizar == 1 ? 'Ctrl+' : 'Ctrl+Shift+';

		if(this.idCombo == 2){

			if(this.listDocumentosImagens2 && this.listDocumentosImagens2[0]){
				this.listDocumentosImagens = Object.assign([], this.listDocumentosImagens2);

				this.inicializarVisualizador = true;
				this.carregarListaImagem();
				if (this.idCombo == 2 && this.listDocumentosImagens && this.listDocumentosImagens[0]) {
					this.setarCombos(this.listDocumentosImagens[0]);
				}
			}else{
				this.listDocumentosImagens = Object.assign([], []);
			}
		}
	}

	ngOnChanges() {
		this.inicializarVisualizador = true;
		this.carregarListaImagem();
		if (this.idCombo == 1 && this.listDocumentosImagens && this.listDocumentosImagens[0]) {
			this.setarCombos(this.listDocumentosImagens[0]);
		}
	}

	ngAfterViewInit() {
		$(".select2ComboDocumentacao"+this.idCombo +", .select2ComboDocumento"+this.idCombo).select2();
		this.loadService.show();
		this.aguardandoDOMfinalizarCarregamento();

		$('#comboDocumentacao_1').on("change", event => {
			// essa condição com idCombo pra garantir que será executado somente quando o componente {idCombo} == 1
			if (!this.updtaeCombo && this.idCombo == 1) {
				this.montarComboDocumento(event.target.value.split('|'));
				this.images = [];
				this.chmarDraImagem();
			}

		});

		$('#comboDocumentacao_2').on("change", event => {
			// essa condição com idCombo pra garantir que será executado somente quando o componente {idCombo} == 2
			if (!this.updtaeCombo && this.idCombo == 2) {
				this.montarComboDocumento(event.target.value.split('|'));
				this.imagesCompara = [];
				this.chmarDraImagemSecundario();
			}
		});

		$('#comboDocumento_1').on("change", event => {
			// essa condição com idCombo pra garantir que será executado somente quando o componente {idCombo} == 1
			if(this.idCombo == 1){
				this.itemDoc = event.target.value.split('|');
				let idDocumentacao = null;
				let valor = $('#comboDocumentacao_1').val();
				idDocumentacao = valor ? valor.split('|') : null;

				// quando executa a primeira vez, todas s opção estão preenchidas mas this.updateCombo esta false
				this.updtaeCombo = (this.itemDoc != "" && this.itemDoc != 'selecione') && (idDocumentacao && idDocumentacao != 'selecione') ? false : true;

				if ((this.itemDoc != "" && this.itemDoc != 'selecione') && !this.updtaeCombo && !this.chekListFase && (idDocumentacao && idDocumentacao != 'selecione')) {
					if(this.itemDoc[1] == TIPO_ARVORE_DOCUMENTO.DOSSIE){
						for (let doc of this.dossieProduto.processo_dossie.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					} else if(this.itemDoc[1] == TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE){
						for (let doc of this.dossieProduto.processo_fase.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					}else{
						for (let doc of this.dossieProduto.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					}
				}else{
					this.imgAtual = 0;
					this.images = [];
					this.chmarDraImagem();
				}
				this.updtaeCombo = false;
			}
		});

		$('#comboDocumento_2').on("change", event => {
			// essa condição com idCombo pra garantir que será executado somente quando o componente {idCombo} == 2
			if(this.idCombo == 2){
				this.itemDoc = event.target.value.split('|');
				let idDocumentacao = null;
				let valor = $('#comboDocumentacao_2').val();
				idDocumentacao = valor ? valor.split('|') : null;
				
				// quando executa a primeira vez, todas s opção estão preenchidas mas this.updateCombo esta false
				this.updtaeCombo = (this.itemDoc != "" && this.itemDoc != 'selecione') && (idDocumentacao && idDocumentacao != 'selecione') ? false : true;
				
				if ((this.itemDoc != "" && this.itemDoc != 'selecione') && !this.updtaeCombo && !this.chekListFase && (idDocumentacao && idDocumentacao != 'selecione')) {
					if(this.itemDoc[1] == TIPO_ARVORE_DOCUMENTO.DOSSIE){
						for (let doc of this.dossieProduto.processo_dossie.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					}else if(this.itemDoc[1] == TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE){
						for (let doc of this.dossieProduto.processo_fase.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					}else{
						for (let doc of this.dossieProduto.instancias_documento) {
							if (doc.documento.tipo_documento.id == this.itemDoc[0] && doc.idVinculo == idDocumentacao[0]) {
								this.listDocumentosImagens = [];
								if (!doc.documento.conteudos && !this.updtaeCombo) {
									this.carregarDocumentoConteudos(doc, event.target.id);
								}
								break;
							}
						}
					}
				}else{
					this.imgAtualCompara = 0;
					this.imagesCompara = [];
					this.chmarDraImagemSecundario();
				}
				this.updtaeCombo = false;
			}
		});
		this.loadService.hide();
	}

	@HostListener('window:keydown', ['$event'])
	onKeyDown(event: KeyboardEvent) {
		if ((event.ctrlKey || event.shiftKey) && (
			event.key == '1' || event.key == '2' || event.key == '3' || event.key == '4' ||
			event.key == ',' || event.key == '.' || event.key == '<' || event.key == '>' ||
			event.key == '-' || event.key == '+' || event.key == '_' || event.key == '=' ||
			event.key == 'j' || event.key == 'J')) {
			event.preventDefault(); // Previne o comportamento padrão do evento de atalho no navegador.

			// Ctrl pressionado, Shift não. KeyCode de outra tecla que não seja Ctrl(17) ou Shift(16) indica que há combinação Ctrl+algo.
			// idVisualizar == 1 indica que é o primeiro componente de visualização (podem haver dois).
			if (event.ctrlKey && !event.shiftKey && event.keyCode != 17 && event.keyCode != 16 && this.idVisualizar == 1) {
				this.tratarTeclaDeAtalhoCapturada(event);
			}

			// Ctrl e Shift pressionados, mais a keyCode de outra tecla que não seja Ctrl(17) ou Shift(16), indicando que há combinação Ctrl+Shift+algo.
			// idVisualizar == 2 indica que é o segundo componente de visualização (podem haver dois).
			if (event.ctrlKey && event.shiftKey && event.keyCode != 17 && event.keyCode != 16 && this.idVisualizar == 2) {
				this.tratarTeclaDeAtalhoCapturada(event);
			}
		}
	}

	/**
	 * Executa tarefas de acordo com o evento de teclado capturado.
	 * Importante verificar se o CTRL ou o SHIFT foram pressionados antes de chamar este método.
	 * @param event O evento de teclado que deverá ser tratado.
	 */
	tratarTeclaDeAtalhoCapturada(event: KeyboardEvent) {
		if (event.key == '1') {
			this.primeiraPagina(); // Vai para a primeira imagem.
		}
		if (event.key == '2') {
			this.paginaAnterior(); // Vai para a imagem anterior.
		}
		if (event.key == '3') {
			this.proximaPagina(); // Vai para a próxima imagem.
		}
		if (event.key == '4') {
			this.ultimaPagina(); // Vai para a última imagem.
		}
		if (event.key == ',' || event.key == '<') {
			this.girarParaEsquerda(); // Rotaciona a imagem para a esquerda.
		}
		if (event.key == '.' || event.key == '>') {
			this.girarParaDireita(); // Rotaciona a imagem para a direita.
		}
		if (event.key == '-' || event.key == '_') {
			this.zoomMais(false); // Reduz o zoom na imagem.
		}
		if (event.key == '+' || event.key == '=') {
			this.zoomMais(true); // Amplia o zoom na imagem.
		}
		// if ((event.key == 'j' || event.key == 'J') && !this.showPortal && !this.visualizadorDois) {
		// 	this.expandirImagem(); // Transporta o visualizador para nova janela.
		// }
	}

	private carregarDocumentoConteudos(doc: any, evento: any) {
		this.loadService.show();
		this.dossieService.getConsultarImagemGet(doc.documento.id).subscribe(response => {
			
			if (evento == "comboDocumento_1") {
				if (response.mime_type == "PDF") {
					this.convertePdfListaImagem(response, null);
				} else {
					this.images = [];
					this.images = Object.assign([], [response.binario ? response.binario : IMAGEM_DEFAUT.img]);
					this.imgAtual = 0;
					this.montarVisualizacaoPrincipal(true);
					this.loadService.hide();
				}
			}

			if (evento == "comboDocumento_2") {
				if (response.mime_type == "PDF") {
					this.convertePdfListaImagem(response, null);
				} else {
					this.imagesCompara = [];
					this.imgAtualCompara = 0;
					this.imagesCompara = Object.assign([], [response.binario ? response.binario : IMAGEM_DEFAUT.img]);
					this.montarVisualizacaoSecundario(true);
					this.loadService.hide();
				}
			}
		}, error => {
			console.log(error);
			this.loadService.hide();
			throw error;
		});
	}

	private montarComboDocumento(evento: any) {
		let objTarge = evento;
		this.listaDocumento = [];
		this.itemArvore = objTarge[0];
		if (this.itemArvore != "") {
			this.comBaseNoTipoDocumentacaoAddDocumento(objTarge);
		}
		this.listaDocumento2 = this.listaDocumento;
	}

	private aguardandoDOMfinalizarCarregamento() {
		this.loadService.show();
		setTimeout(() => {
			this.dossieProduto = Object.assign({}, this.dossieProduto);
			this.carregarListaImagem();
		}, 500);
	}

	private comBaseNoTipoDocumentacaoAddDocumento(objTarge: any) {
		this.addDocumentoDoTipoDossie(objTarge);
		this.addDocumentoDoTipoDossieFase(objTarge);
		this.addDocumentoDoTipoCliente(objTarge);
		this.addDocumentoDoTipoGarantia(objTarge);
		this.addDocumentoDoTipoProduto(objTarge);
	}

	private addDocumentoDoTipoDossie(objTarge: any) {
		if (objTarge[1] == TIPO_ARVORE_DOCUMENTO.DOSSIE) {
			this.dossieProduto.processo_dossie.instancias_documento.forEach(instanciaDossie => {
				if (this.dossieProduto.processo_dossie.id == objTarge[0]) {
					let objDoc = {
						id: instanciaDossie.documento.tipo_documento.id,
						descricao: instanciaDossie.documento.tipo_documento.nome,
						tipo: TIPO_ARVORE_DOCUMENTO.DOSSIE
					};
					this.listaDocumento.push(objDoc);
				}
			});
		}
	}

	private addDocumentoDoTipoDossieFase(objTarge: any) {
		if (objTarge[1] == TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE) {
			this.dossieProduto.processo_fase.instancias_documento.forEach(instanciaFase => {
				if (this.dossieProduto.processo_fase.id == objTarge[0]) {
					let objDoc = {
						id: instanciaFase.documento.tipo_documento.id,
						descricao: instanciaFase.documento.tipo_documento.nome,
						tipo: TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE
					};
					this.listaDocumento.push(objDoc);
				}
			});
		}
	}

	private addDocumentoDoTipoCliente(objTarge: any) {
		if (objTarge[1] == TIPO_ARVORE_DOCUMENTO.CLIENTE) {
			this.dossieProduto.vinculos_pessoas.forEach(cliente => {
				if (cliente.dossie_cliente.id == objTarge[0]) {
					cliente.instancias_documento.forEach(instanciaCliente => {
						let objDoc = {
							id: instanciaCliente.documento.tipo_documento.id,
							descricao: instanciaCliente.documento.tipo_documento.nome,
							tipo: TIPO_ARVORE_DOCUMENTO.CLIENTE
						};
						this.listaDocumento.push(objDoc);
					});
				}
			});
		}
	}

	private addDocumentoDoTipoGarantia(objTarge: any) {
		if (objTarge[1] == TIPO_ARVORE_DOCUMENTO.GARANTIAS) {
			this.dossieProduto.garantias_informadas.forEach(garantia => {
				if (garantia.id == objTarge[0]) {
					garantia.instancias_documento.forEach(instanciaGarantia => {
						let objDoc = {
							id: instanciaGarantia.documento.tipo_documento.id,
							descricao: instanciaGarantia.documento.tipo_documento.nome,
							tipo: TIPO_ARVORE_DOCUMENTO.GARANTIAS
						};
						this.listaDocumento.push(objDoc);
					});
				}
			});
		}
	}

	private addDocumentoDoTipoProduto(objTarge: any) {
		if (objTarge[1] == TIPO_ARVORE_DOCUMENTO.PRODUTO) {
			this.dossieProduto.produtos_contratados.forEach(produto => {
				if (produto.id == objTarge[0]) {
					produto.instancias_documento.forEach(instanciaProduto => {
						let objDoc = {
							id: instanciaProduto.documento.tipo_documento.id,
							descricao: instanciaProduto.documento.tipo_documento.nome,
							tipo: TIPO_ARVORE_DOCUMENTO.PRODUTO
						};
						this.listaDocumento.push(objDoc);
					});
				}
			});
		}
	}

	private carregarListaImagem() {
		if (this.listDocumentosImagens && this.listDocumentosImagens.length > 0 && !this.chekListFase) {
			this.imgAtual = 0;
			this.imagesCompara = undefined != this.listDocumentosImagensComparar ? this.listDocumentosImagensComparar[0].conteudos : [];

			if (this.idCombo == 1 && undefined != this.listDocumentosImagens[0].conteudos) {
				this.images = this.listDocumentosImagens[0].conteudos;
				this.montarVisualizacaoPrincipal(true);
			}

			if (this.idCombo == 2 && undefined != this.listDocumentosImagens[0].conteudos) {
				this.images = this.listDocumentosImagens[0].conteudos;
				this.montarVisualizacaoSecundario(true);
			}
		}
	}

	private setarCombos(doc: any) {
		for (let item of this.hasMapListaCombo) {
			if (doc && doc.tipo_documento.id && item.itemDocumento == doc.tipo_documento.id && doc.idVinculo == item.idDocumentacao) {
				this.comboDocumentacao = "" + item.idDocumentacao + "|" + item.tipo + "";
				this.comboDocumento = "" + item.itemDocumento + "|" + item.tipo + "";
				this.montarComboDocumento(this.comboDocumentacao.split('|'));
				this.updtaeCombo = true;
				this.loadService.show();
				setTimeout(() => {
					this.dossieProduto = Object.assign({}, this.dossieProduto);
					$('#comboDocumentacao_' + this.idCombo ).select2().val("" + this.comboDocumentacao + "").trigger("change");
					$('#comboDocumento_' + this.idCombo ).select2().val("" + this.comboDocumento + "").trigger("change");
					this.fecharLoadAoSetarCombos();
				}, 500);
				break;
			}
		}
	}

	private fecharLoadAoSetarCombos() {
		if (!this.openShowPortal) {
			this.loadService.hide();
		}
		else {
			this.openShowPortal = false;
		}
	}

	private montarVisualizacaoPrincipal(visualizar: boolean) {
		if (visualizar) {
			this.divCanvas = document.getElementById('visualizarImagem_1');
			if (this.divCanvas != null) {
				this.divCanvas.setAttribute('style', ' max-height: ' + '100% !important;');
				this.chmarDraImagem();
			}
		}
	}

	private chmarDraImagem() {
		const canvas: any = document.querySelector('#canvas_1');
		if (null != canvas && this.images.length > 0) {
			this.drawImage(this.images[this.imgAtual], canvas);
		} else {
			this.drawImage("", canvas);
		}
	}

	private chmarDraImagemSecundario() {
		const canvas: any = document.querySelector('#canvas_2');
		if (null != canvas && this.imagesCompara.length > 0) {
			this.drawImage(this.imagesCompara[this.imgAtualCompara], canvas);
		} else {
			this.drawImage("", canvas);
		}
	}

	private montarVisualizacaoSecundario(visualizar: boolean) {
		if (visualizar) {
			this.divCanvas = document.getElementById('visualizarImagem_2');
			if (this.divCanvas != null) {
				this.divCanvas.setAttribute('style', ' max-height: ' + '100% !important;');
				this.chmarDraImagemSecundario();
			}
		}
	}

	paginaAnterior() {
		if(this.idCombo == 1){
			if(this.imgAtual > 0) {
				this.imgAtual--;
				this.chmarDraImagem();
			}
		}else{
			if(this.imgAtualCompara > 0) {
				this.imgAtualCompara--;
				this.chmarDraImagemSecundario();
			}
		}
	}

	proximaPagina() {
		if(this.idCombo == 1){
			if (this.imgAtual < (this.images.length - 1)) {
				this.imgAtual++;
				this.chmarDraImagem();
			}
		}else{
			if (this.imgAtualCompara < (this.imagesCompara.length - 1)) {
				this.imgAtualCompara++;
				this.chmarDraImagemSecundario();
			}
		}		
		
	}

	ultimaPagina() {
		if(this.idCombo == 1){
			if (this.imgAtual < (this.images.length - 1)) {
				this.imgAtual = this.images.length - 1;
				this.chmarDraImagem();
			}
		}else{
			if (this.imgAtualCompara < (this.imagesCompara.length - 1)) {
				this.imgAtualCompara = this.imagesCompara.length - 1;
				this.chmarDraImagemSecundario();
			}
		}
	}

	primeiraPagina() {
		if(this.idCombo == 1){
			if (this.imgAtual > 0) {
				this.imgAtual = 0;
				this.chmarDraImagem();
			}
		}else{
			if (this.imgAtualCompara > 0) {
				this.imgAtualCompara = 0;
				this.chmarDraImagemSecundario();
			}
		}
	}

	private touchScroll() {
		this.touchScrollVisual();
		this.touchScrollVisual2();
	}

	private touchScrollVisual() {
		var viewer = TouchScroll();
		viewer.init({
			id: 'visualizarImagem_1',
			draggable: true,
			wait: false
		});
	}

	private touchScrollVisual2() {
		if (this.visualizadorDois) {
			var viewer2 = TouchScroll();
			viewer2.init({
				id: 'visualizarImagem_2',
				draggable: true,
				wait: false
			});
		}
	}

	drawImage(uri, canvas: any) {
		this.resetaConfigImg();
		if (uri != null) {
			let url = "";
			let rotacao = 0;
			let escala = 0;
			let uriPrinc = "";
			if (typeof uri === "string") {
				uriPrinc = uri;
				url = uri;
				rotacao = 0 || 0;
				escala = 0.7 || 1;
			} else {
				uriPrinc = uri.url;
				url = uri.url;
				rotacao = uri.rotacao || 0;
				escala = uri.escala || 1;
			}

			this.currentImage = uriPrinc;

			const img = new Image();
			img.setAttribute('class', 'brilhoImg');
			img.src = `data:image/png;base64,${uriPrinc}`;
			const cContext = canvas.getContext("2d");
			cContext.save();
			cContext.clearRect(0, 0, canvas.width, canvas.height);
			cContext.beginPath();
			cContext.translate(this.translationOffsetX, this.translationOffsetY);
			img.onload = function () {
				const w = img.width * escala,
					h = img.height * escala;
				let cw = w,
					ch = h,
					cx = 0,
					cy = 0;

				switch (rotacao) {
					case 90:
						cw = h;
						ch = w;
						cy = h * -1;
						break;
					case 180:
						cx = w * -1;
						cy = h * -1;
						break;
					case 270:
						cw = h;
						ch = w;
						cx = w * -1;
						break;
				}
				canvas.setAttribute("width", cw.toString());
				canvas.setAttribute("height", ch.toString());
				cContext.rotate(rotacao * Math.PI / 180);
				cContext.drawImage(img, cx, cy, w, h);
			};
		}
		this.touchScroll();
	}

	resetaConfigImg() {
		this.filtroBrilhoImg = 100;
		this.filtroContrasteImg = 100;
	}

	zoomMais(maisOuMenos: boolean) {
		this.escalaImg = maisOuMenos ? ++this.maisZomm : this.maisZomm > 1 ? --this.maisZomm : (0.7 || 1);
		const img = {
			url: this.currentImage,
			escala: this.escalaImg
		};
		const canvas: any = document.querySelector('#canvas_' + this.idVisualizar + '');
		this.drawImage(img, canvas);
	}

	girarParaDireita() {
		this.rotacaoImg += 90;
		if (this.rotacaoImg >= 360) {
			this.rotacaoImg = 0;
		}
		const img = {
			url: this.currentImage,
			rotacao: this.rotacaoImg,
			escala: this.escalaImg
		};
		const canvas: any = document.querySelector('#canvas_' + this.idVisualizar + '');
		this.drawImage(img, canvas);
	}

	girarParaEsquerda() {
		this.rotacaoImg -= 90;
		if (this.rotacaoImg <= -90) {
			this.rotacaoImg = 270;
		}
		const img = {
			url: this.currentImage,
			rotacao: this.rotacaoImg,
			escala: this.escalaImg
		};
		const canvas: any = document.querySelector('#canvas_' + this.idVisualizar + '');
		this.drawImage(img, canvas);
	}

	/**
	* Converte um PDF multipaginado em uma lista de imagens para visualização
	* @param arrayImages 
	*/
	private convertePdfListaImagem(response: any, documentoMontado: any) {
		let binario: any = response.binario ? response.binario : IMAGEM_DEFAUT.img;
		let messagesError = new Array<string>();
		let promise = Promise.resolve();
		let pdfCanvas = 'pdfcanvas'+this.idCombo;

		if (binario) {
			let arrayImages = [];
			let pdfAsArray = this.conversorDocsUtil.convertDataURIToBinary(`data:application/pdf;base64,${binario}`);
			PDFJS.getDocument(pdfAsArray).then(pdfSuccess => {
				for (let i = 1; i <= pdfSuccess.numPages; i++) {
					this.loadService.showProgress(pdfSuccess.numPages, arrayImages.length);
					pdfSuccess.getPage(i).then(pdfPageSuccess => {
						promise = promise.then(function () {
							let scale: number = 1;
							let viewport = pdfPageSuccess.getViewport(scale);
							let canvas: any = document.getElementById(pdfCanvas);
							let context = canvas.getContext('2d');
							canvas.height = viewport.height;
							canvas.width = viewport.width;
							let task = pdfPageSuccess.render(
								{
									canvasContext: context,
									viewport: viewport
								}
							);
							return task.promise.then(renderPdfSuccess => {
								return canvas.toDataURL();
							});
						});
						promise.then((pdfImgBase64: any) => {
							let imagePng = pdfImgBase64.substring(pdfImgBase64.indexOf(',') + 1);
							arrayImages.push(imagePng);
							this.loadService.showProgress(pdfSuccess.numPages, arrayImages.length);
							if (pdfSuccess.numPages == arrayImages.length) {
								response.mime_type = "PNG";
								response.conteudos = arrayImages;
								if (documentoMontado) {
									documentoMontado.conteudos[0] = response.binario ? response.binario : IMAGEM_DEFAUT.img;
									this.images = Object.assign([], [documentoMontado.binario]);
									this.listDocumentosImagens = [];
									this.imgAtual = 0;
								} else {
									this.images = [];
									this.imagesCompara = [];
									this.images = arrayImages.length > 0 ? Object.assign([], arrayImages) : Object.assign([], [response.binario ? response.binario : IMAGEM_DEFAUT.img]);
									this.imgAtual = 0;
									this.imgAtualCompara = 0;
									this.imagesCompara = this.images;

									if(this.idCombo == 1){
										this.imagesCompara = Object.assign([], []);
										this.montarVisualizacaoPrincipal(true);
									}else{
										this.images = Object.assign([], []);
										this.montarVisualizacaoSecundario(true);
									}
								}
								this.loadService.hide();
							}
						});
					}).catch(pdfPageError => {
						// Habilita a mensagem de erro
						messagesError.push('Erro ao converter page do pdf. Por favor, contate o administrador');
						this.alertMessagesErrorChanged.emit(messagesError);
						this.clearAllMessages();
					});
				}
			}).catch(pdfError => {
				// Habilita a mensagem de erro
				messagesError.push('Erro ao converter o PDF. Por favor, contate o administrador');
				this.alertMessagesErrorChanged.emit(messagesError);
				this.clearAllMessages();
			});
		}
	}
}