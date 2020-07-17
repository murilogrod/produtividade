import { Component, Input, Output, ViewEncapsulation, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { LoaderService, EventService, ApplicationService } from 'src/app/services';
import { ArvoreTipoDocumento } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/arvore-tipo-documento';
import { DialogService } from 'angularx-bootstrap-modal';
import { ConversorDocumentosUtil } from '../../conversor-documentos/conversor-documentos.util.service';
import { DocumentoService } from '../../documento-service';
import * as moment from 'moment';
import { PDFJS } from 'pdfjs-dist/build/pdf';
import { Utils } from 'src/app/utils/Utils';
import { MSG_DOSSIE_PRODUTO, LOCAL_STORAGE_CONSTANTS } from 'src/app/constants/constants';
import { DocumentImage } from '../../documentImage';
import { UploadDocumentoComponentPresenter } from '../presenter/upload-documento.component.presenter';
import { GlobalError } from 'src/app/global-error/model/global-error';
import { UtilsErrorGlobal } from 'src/app/global-error/utils/utils-erros';
import { ImageChangeService } from '../../image-change/Image-change.service';
import { FonteDocumento } from '../../enum-fonte-documento/fonte-documento.enum';
declare var $: any;

export interface ModelNovosDocumentos {
	tipoDocumento: string;
	funcaoDocumental: string;
	validade: string;
	ids: {};
}

@Component({
	selector: 'uplaod-document',
	templateUrl: './upload-documento.component.html',
	styleUrls: ['./upload-documento.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class UploadDocumentoComponent extends ImageChangeService {

	@Input() habilitaBotoesSalvar: boolean;

	@Input() images: DocumentImage[];

	@Input() incluirImagem: boolean;
	@Output() incluirImagemChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

	@Input() documentoAClassificar: boolean;
	@Output() documentoAClassificarChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

	@Input() clickArvoreTipoDocumento: ArvoreTipoDocumento;
	@Output() clickArvoreTipoDocumentoChanged: EventEmitter<ArvoreTipoDocumento> = new EventEmitter<ArvoreTipoDocumento>();

	@Input() arvoreTipoDocumento: ArvoreTipoDocumento;
	@Output() arvoreTipoDocumentoChanged: EventEmitter<ArvoreTipoDocumento> = new EventEmitter<ArvoreTipoDocumento>();
	@Input() manterPdfEmMiniatura: boolean;



	manterPdfEmMiniaturaEstadoAnterior: boolean = false;

	qtdPages: number = 0;

	constructor(
		private loadService: LoaderService,
		private dialogService: DialogService,
		conversorDocumentosUtil: ConversorDocumentosUtil,
		private documentoService: DocumentoService,
		private uploadDocumentoPresenter: UploadDocumentoComponentPresenter,
		private cdRef: ChangeDetectorRef,
		private eventService: EventService,
		applicationService: ApplicationService
	) {
		super(conversorDocumentosUtil, applicationService);
	}

	insertDocuments(input) {
		if (input) {
			input.click();
		}
	}

	clickFile() {
		$("#inputDocument").val("");
	}

	/**
	 * 
	 * @param input 
	 */
	onChangeFile(input) {
		// Habilita o load
		this.loadService.show();
		let files: File[] = this.uploadDocumentoPresenter.getAllowedFiles(input.target.files);
		const fileIsNotAllowed = this.uploadDocumentoPresenter.isNotAllowedFile(input.target.files);
		if (this.uploadDocumentoPresenter.existePDFsConsecutivos(input.target.files)) {
			this.uploadDocumentoPresenter.mostraAlertaDocumentosPDFsConsecutivos(this.alertMessagesErrorChanged, this);
			this.loadService.hide();
			return;
		}
		this.images = [];
		this.images = this.imagesCopy.length > 0 ? this.imagesCopy : [];
		this.incluirImagem = true;
		this.incluirImagemChanged.emit(this.incluirImagem);

		for (let file of files) {
			if (file.type == "application/pdf") {
				this.convertPdfToImage(file);
			} else {
				this.addImageToList(file);
			}
		}
		this.documentoAClassificar = true;
		this.documentoAClassificarChanged.emit(this.documentoAClassificar);
		this.uploadDocumentoPresenter.mostraAlertaErroTipoNaoPermitdo(this.alertMessagesErrorChanged, this, fileIsNotAllowed);
		this.clickArvoreTipoDocumento = this.arvoreTipoDocumento;
		this.clickArvoreTipoDocumentoChanged.emit(this.clickArvoreTipoDocumento);
		this.arvoreTipoDocumento = new ArvoreTipoDocumento();
		this.arvoreTipoDocumentoChanged.emit(this.arvoreTipoDocumento);
	}

	/**
	 * 
	 * @param file 
	 */
	convertPdfToImage(file: File) {
		let messages = new Array<string>();
		this.conversorDocumentosUtil.getBase64(file).then(pdfBase64Success => {
			this.uploadDocumentoPresenter.addArquivoPdfOriginal(pdfBase64Success);
			this.documentoService.verificaAssinaturaDigital(pdfBase64Success).subscribe((existeAssinatura: boolean) => {
				this.checaExistePdfAssinado(existeAssinatura);
				let dataArquivo = moment(file.lastModified).toDate();
				let nomeArquivo = file.name;
				this.convertBase64PdfToImage(pdfBase64Success, dataArquivo, nomeArquivo);
			}, error => {
				console.log(error);
				this.loadService.hide();
				throw error;
			});
		}).catch(pdfBase64Error => {
			// Desabilita o load
			this.loadService.hide();
			// Habilita a mensagem de erro
			messages.push(MSG_DOSSIE_PRODUTO.MSG_ERRO_UPLOAD_PDF);
			this.alertMessagesErrorChanged.emit(messages);
			this.clearAllMessages();
		});
	}


	/**
	 * 
	 * @param pdfBase64Success 
	 * @param dataArquivo 
	 * @param nomeArquivo 
	 */
	convertBase64PdfToImage(pdfBase64Success: string, dataArquivo: any, nomeArquivo: any) {
		let messagesError = new Array<string>();
		let pdfAsArray = this.conversorDocumentosUtil.convertDataURIToBinary(`data:application/pdf;base64,${pdfBase64Success}`);
		PDFJS.getDocument(pdfAsArray).then(pdfSuccess => {
			this.uploadDocumentoPresenter.addQuantidadePaginasArquivoPdfOriginal(pdfSuccess.numPages);
			let promise = Promise.resolve();
			let qtdPag = pdfSuccess.numPages;
			for (let i = 1; i <= qtdPag; i++) {
				pdfSuccess.getPage(i).then(pdfPageSuccess => {
					promise = promise.then(function () {
						let scale: number = 1;
						let viewport = pdfPageSuccess.getViewport(scale);
						let canvas: any = document.getElementById('pdfcanvas');
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
					promise.then(pdfImgBase64 => {
						this.addPdfToList(pdfImgBase64, dataArquivo, nomeArquivo, qtdPag);
					});
				}).catch(pdfPageError => {
					// Desabilita o load
					this.loadService.hide();
					// Habilita a mensagem de erro
					messagesError.push(MSG_DOSSIE_PRODUTO.MSG_ERRO_CONVERSAO_PDF);
					this.alertMessagesErrorChanged.emit(messagesError);
					this.clearAllMessages();
				});
			}
		}).catch(pdfError => {
			// Desabilita o load
			this.loadService.hide();
			// Habilita a mensagem de erro
			messagesError.push('Erro ao converter o PDF. Por favor, contate o administrador');
			this.alertMessagesErrorChanged.emit(messagesError);
			this.clearAllMessages();
		});
	}

	/**
	 * Adicionando a imagem
	 * @param pdfImgBase64 
	 * @param data 
	 * @param nomeArquivo 
	 * @param qtdPag 
	 */
	addPdfToList(pdfImgBase64: any, data: any, nomeArquivo: any, qtdPag: number) {
		let manterPdfEmMiniatura: boolean = (this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.miniaturaPDF) == 'false') ? false : true;
		++this.qtdPages;
		this.separaPrimeiraPaginaPdfCasoOpcaoConversaoNaoHabilitada(pdfImgBase64, data, nomeArquivo, qtdPag, this.qtdPages);
		this.uploadDocumentoPresenter.alteraTodosPdfAssinadosDevidoCarregamentoPrevioImagem(this.images);
		this.loadService.showProgress(qtdPag, this.qtdPages);
		// Desabilita o load
		if (this.qtdPages == qtdPag) {
			this.loadService.hide();
			this.qtdPages = 0;
			this.images = Object.assign([], this.images);
			this.emiteImagemAClassificarVisualizador(this.images);
		}
	}

	/**
	 * 
	 * @param pdfImgBase64 Imagem na base 64
	 * @param data 
	 * @param nomeArquivo nome do arquivo pdf inteiro
	 */
	private separaPrimeiraPaginaPdfCasoOpcaoConversaoNaoHabilitada(pdfImgBase64: any, data: any, nomeArquivo: any, qtdTotalPages: number, posicao: number) {

		if (this.manterPdfEmMiniatura && this.qtdPages == 1) {
			this.addImagePdfParaVisualizacao(pdfImgBase64, data, nomeArquivo, qtdTotalPages, posicao);
		} else if (this.manterPdfEmMiniatura && this.qtdPages > 1) {
			let pagina: DocumentImage = this.populaProximaPaginaRestantePdf(pdfImgBase64, data, nomeArquivo, qtdTotalPages);
			this.uploadDocumentoPresenter.guardaImagemRestantePdf(pagina);
		} else {
			this.addImagePdfParaVisualizacao(pdfImgBase64, data, nomeArquivo, qtdTotalPages, posicao);
		}
	}

	/**
	 * Adiciona imagem para visualização em tela pelo visualizador de documentos
	 * @param pdfImgBase64 Imagem na base 64
	 * @param data 
	 * @param nomeArquivo nome do arquivo pdf inteiro
	 */
	private addImagePdfParaVisualizacao(pdfImgBase64: any, data: any, nomeArquivo: any, qtdTotalPages: number, posicao: number) {
		this.images.push({
			indiceDocListPdfOriginal: this.conversorDocumentosUtil.arquivosPdfOringinais.getValue().length - 1,
			image: pdfImgBase64.substring(pdfImgBase64.indexOf(',') + 1),
			name: nomeArquivo,
			primeiraPagina: (this.qtdPages == 1),
			checked: false,
			data: data,
			type: 'application/pdf',
			paginaAtual: posicao,
			totalPaginas: qtdTotalPages,
			source: FonteDocumento.UPLOAD
		});
	}

	/**
	 * Cria objeto que representa uma página do documento inteiro
	 * @param pdfImgBase64 Imagem na base 64
	 * @param data 
	 * @param nomeArquivo nome do arquivo pdf inteiro
	 */
	populaProximaPaginaRestantePdf(pdfImgBase64: any, data: any, nomeArquivo: any, qtdTotalPages: number): DocumentImage {
		let pagina: DocumentImage = new DocumentImage();
		pagina.indiceDocListPdfOriginal = this.conversorDocumentosUtil.arquivosPdfOringinais.getValue().length - 1,
			pagina.image = pdfImgBase64.substring(pdfImgBase64.indexOf(',') + 1),
			pagina.name = nomeArquivo,
			pagina.primeiraPagina = (this.qtdPages == 1),
			pagina.checked = false,
			pagina.data = data,
			pagina.type = 'application/pdf',
			pagina.totalPaginas = qtdTotalPages
		return pagina
	}

	/**
	 * 
	 * @param file 
	 */
	addImageToList(file: File) {
		this.getBase64(file);
	}

	/**
	 * 
	 * @param file 
	 */
	getBase64(file: any) {
		let reader = new FileReader();
		reader.onload = () => {
			var img = new Image();
			img.onload = () => {
				this.convertBase64(file, img.width, img.height);
			};
			img.src = reader.result.toString();
		};
		reader.readAsDataURL(file);
	}

	/**
	 * 
	 * @param file 
	 * @param paramWidth 
	 * @param paramHeight 
	 */
	private convertBase64(file: File, paramWidth, paramHeight) {
		this.conversorDocumentosUtil.getBase64(file).then(imgSuccess => {
			// Adicionando a imagem  
			this.images.push({
				altura: paramHeight,
				largura: paramWidth,
				image: imgSuccess,
				name: file.name,
				checked: false,
				data: moment(file.lastModified).toDate(),
				type: file.type,
				source: FonteDocumento.UPLOAD
			});
			this.images = Object.assign([], this.images);
			this.uploadDocumentoPresenter.alteraTodosPdfsAssinadosPorCarregamentoImagens(this.images);
			this.emiteImagemAClassificarVisualizador(this.images);
			// Desabilita o load
			this.loadService.hide();
		}).catch(imgError => {
			// Desabilita o load
			this.loadService.hide();
			// Habilita a mensagem de erro
			Utils.showMessageDialog(this.dialogService, 'Erro ao fazer upload da Imagem. Por favor, contate o administrador');
		});
	}

	/**
	 * 
	 * @param existeAssinatura 
	 */
	private checaExistePdfAssinado(existeAssinatura: boolean) {
		this.conversorDocumentosUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfsOriginais => {
			let messagesWarning = new Array<string>();
			if (existeAssinatura && this.imagesCopy.length == 0) {
				this.uploadDocumentoPresenter.setArquivoContemAssinaturaDigital(existeAssinatura);
				messagesWarning.push("Este documento está assinado digitalmente. Em caso de carga complementar ou qualquer manipulação no documento (remoção de página, reordenação, etc) fará com que a assinatura seja descartada.");
				this.alertMessagesWarningChanged.emit(messagesWarning);
				this.clearAllMessages();
				return;
			}
			if (existeAssinatura && (this.imagesCopy.length > 0 || arrayArquivosPdfsOriginais.length > 0)) {
				this.uploadDocumentoPresenter.setArquivoContemAssinaturaDigital(existeAssinatura);
				messagesWarning.push("Este documento está assinado digitalmente. Devido ao carregamento prévio de documento a ser classificado a assinatura digital será descartada.");
				this.alertMessagesWarningChanged.emit(messagesWarning);
				this.clearAllMessages();
			}
		},
			() => {
				this.loadService.hide();
			});
	}

	/**
	 * 
	 * @param obj 
	 */
	handleImgCarregadaScanner(obj) {
		this.inicializandoListaimagemAClassificar();
		if (obj instanceof Error) {
			let erroGlobal = new GlobalError();
			erroGlobal.status = 400;
			erroGlobal.name = 'Falha de comunicação com o scanner';
			erroGlobal.message = 'Scanner não localizado';
			UtilsErrorGlobal.validarDigitalizacao(erroGlobal, this.eventService);
		} else {

			if (obj.type == "image/pdf") {
				this.convertBase64PdfToImage(obj.image, obj.data, obj.name);
			} else {
				this.images.push(obj);
				this.images = Object.assign([], this.images);
				this.emiteImagemAClassificarVisualizador(this.images);
			}
		}
	}

	/**
	 * 
	 */
	private inicializandoListaimagemAClassificar() {
		if (!this.incluirImagem) {
			this.images = [];
			this.images = this.imagesCopy.length > 0 ? this.imagesCopy : [];
			this.images = Object.assign([], this.images);
			this.imagesCopy = Object.assign([], this.images);
			this.incluirImagem = true;
			this.documentoAClassificar = this.incluirImagem;
		}
	}
}