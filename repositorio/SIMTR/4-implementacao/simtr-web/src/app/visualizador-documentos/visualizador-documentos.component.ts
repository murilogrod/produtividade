import { AfterViewChecked, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation, ChangeDetectorRef } from '@angular/core';
import { DialogService } from 'angularx-bootstrap-modal';
import { PDFJS } from 'pdfjs-dist/build/pdf';
import { BehaviorSubject } from 'rxjs';
import { EVENTS, LOCAL_STORAGE_CONSTANTS, MYME_TYPE, TIPO_ARVORE, TIPO_SELETOR, VISUALIZADOR_DOCUMENTOS, PERFIL_ACESSO, ABA_DOSSIE_CLIENTE } from '../constants/constants';
import { ConversorDocumentosUtil } from '../documento/conversor-documentos/conversor-documentos.util.service';
import { ArquivoPdfOriginal } from '../documento/conversor-documentos/model/arquivo-pdf-original';
import { DocumentImage } from "../documento/documentImage";
import { GerenciadorDocumentosEmArvore } from '../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util';
import { NodeApresentacao } from '../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model';
import { TipoDocumentoArvoreGenerica } from '../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model';
import { ArvoreTipoDocumento } from '../model/model-arvore-generica-dossie-produto/vinculos-model/arvore-tipo-documento';
import { VinculoArvore } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { VinculoArvoreGarantia } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia';
import { ApplicationService, EventService, LoaderService, AuthenticationService } from '../services';
import { MudancaSalvaService } from '../services/mudanca-salva.service';
import { Utils } from '../utils/Utils';
import { ImageChangeService } from '../documento/image-change/Image-change.service';
import { ClassificacaoDocumento } from '../documento/conversor-documentos/model/classificacao-documento';
import { ModalExtracaoDadosDocumento } from './modal-extracao-dados-documento/modal-extracao-dados-documento.component';
import { RouterService } from '../services/router-service';
import { environment } from 'src/environments/environment';
import { VisualizadorDocumentosService } from './visualizador-documentos.service';
import { UtilsArvore } from '../documento/arvore-generica/UtilsArvore';

declare var $: any;

@Component({
    selector: 'visualizador-documentos',
    templateUrl: './visualizador-documentos.component.html',
    styleUrls: ['./visualizador-documentos.component.css'],
    encapsulation: ViewEncapsulation.None
})

export class VisualizadorDocumentosComponent extends ImageChangeService implements OnInit, OnChanges, AfterViewChecked {
    constructor(
        private dialogService: DialogService,
        applicationService: ApplicationService,
        private mudancaSalvaService: MudancaSalvaService,
        private eventService: EventService,
        private cdRef: ChangeDetectorRef,
        private loadService: LoaderService,
        conversorDocumentosUtil: ConversorDocumentosUtil,
        private routerService: RouterService,
        private authenticationService: AuthenticationService, 
        private visualizadorService: VisualizadorDocumentosService) {
        super(conversorDocumentosUtil, applicationService);
        this.isPerfilMostrarBotaoExtracaoExterna = this.applicationService.verificarPerfil('MTRADM', 'MTRSDNTTO', 'MTRSDNTTG','MTRSDNMTZ');
    }

    @Input() documentoAClassificar: boolean;

    @Input() images: DocumentImage[] = [];

    @Input() incluirImagem: boolean;

    @Input() tipoDossie: number;

	@Input() identificadorDossie: string;

    /**
     * Guarda o ultimo tipo de documento classifcado no modal de classificação
     */
    @Input() tipoDocumentoArvoreGenericaAnterior: TipoDocumentoArvoreGenerica;

    @Input() arvoreTipoDocumento: ArvoreTipoDocumento;

    @Input() listaVinculoArvore: Array<VinculoArvore>;

    @Input() clickArvoreTipoDocumento: ArvoreTipoDocumento;

    @Input() dossieCliente: boolean;

    @Input() selecionadoFolhaDoNoDaArvore: NodeApresentacao;

    @Output() imagesChange: EventEmitter<DocumentImage[]> = new EventEmitter<DocumentImage[]>();

    @Output() imagesCopyChange: EventEmitter<DocumentImage[]> = new EventEmitter<DocumentImage[]>();

    @Output() documentoAClassificarChange: EventEmitter<boolean> = new EventEmitter<boolean>();

    @Output() ordemPaginasAlteradaChange: EventEmitter<boolean> = new EventEmitter<boolean>();

    @Output() alteradoArvoreChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

    indexVisualizador: number;

    changes: SimpleChanges;

    idSeletor: string;

    indexArvore: any;

    listaIndexArvoreSelecionada: number[] = [];

    tipoDocumento: any;

    imagensSelecionadas: DocumentImage[] = [];

    tituloTipodaAcao: string;

    usuario: any;

    currentImage = null;

    ativandoDesativandoImagem: boolean = true;

    mostrarImagem: boolean;

    chekarTodoasImg: boolean;

    indUltimoChekado = 0;

    excluirTodasimagem: boolean;

    habilitarBtnAcao: boolean = true;

    desativaComboClassificacao: boolean;

    existeDocumentoFuncao: boolean;

    urlDownload: string;

    manterPdfEmMiniatura: boolean;

    manterPdfEmMiniaturaEstadoAnterior: boolean = false;

    habilitarBtnClassificar: boolean;

    ocultarComboTipoDocumento: boolean;

    isMostrarBotaoModalExtracao: boolean = false;

    isMostrarBotaoExtracaoExterna: boolean = false;

    isAnaliseOutsourcing: boolean = false;

    isDocumentoTemAtributoExtracao: boolean = false;

    tituloHint: string = '';

    atributosDocumentoNaoPersistido: any[] = [];

    isPerfilMostrarBotaoExtracaoExterna: boolean = false;
    
    extracaoHint: string = 'Enviar Documento para o serviço de Outsourcing Documental'

    handleChangeOcultarComboTipoDocumento(input) {
        this.ocultarComboTipoDocumento = input;
    }

    handleChangelistaIndexArvoreSelecionada(input) {
        this.listaIndexArvoreSelecionada = input;
    }

    handleChangeidSeletor(input) {
        this.idSeletor = input;
    }

    handleChangeIndexArvore(input) {
        this.indexArvore = input;
    }

    handleChangeTipoDocumento(input) {
        this.tipoDocumento = input;
    }

    ngOnInit() {
        this.chekarTodoasImg = (this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.marcaTodos) == 'false') ? false : true;
        this.applicationService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.marcaTodos, this.chekarTodoasImg);
        this.indexArvore = null;
        this.definindoTituloClassificaOuReclassifica();
        if (this.images != null && this.images.length > 0) {
            this.currentImage = this.images[0].image;
            let tipoSelecionado: any = this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.TIPO_DOCUMENTO_SELECIONADO_ARVORE);
            this.arvoreTipoDocumento = tipoSelecionado ? JSON.parse(tipoSelecionado) : null;
            this.createIndexForImages();
        }
    }

    /**
     * Funcionalidade que dispara a função de miniatura PDF
     */
    inicializarModoMiniatura() {
        this.applicationService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.miniaturaPDF, this.manterPdfEmMiniatura);
        if (this.images.length > 0
            && !this.images.some(image => (image.id ? true : false))
            && this.images.some(image => !image.reclassificar)
            && this.manterPdfEmMiniaturaEstadoAnterior != this.manterPdfEmMiniatura) {
            this.manterPdfEmMiniaturaEstadoAnterior = this.manterPdfEmMiniatura;
            if (this.manterPdfEmMiniatura) {
                this.images = this.conversorDocumentosUtil.manterPdfsMiniaturaVisualizacao(this.images);
                this.images = Object.assign([], this.images);
                this.emiteImagemAClassificarVisualizador(this.images);
                this.createIndexForImages();
                return;
            }
            this.images = this.conversorDocumentosUtil.addPaginasRestantesPdfVisualizacao(this.images);
            this.images = Object.assign([], this.images);
            this.emiteImagemAClassificarVisualizador(this.images);
        }
        this.createIndexForImages();
    }

    definindoTituloClassificaOuReclassifica() {
        this.tituloTipodaAcao = this.incluirImagem ? "Classificar documentos" : "Reclassificar documentos";
    }

    private createIndexForImages() {
        if (this.images != null) {
            for (let i = 0; i < this.images.length; i++) {
                this.images[i].index = i + 1;
                this.images[i].ativo = this.chekarTodoasImg ? "imagem_Ativa" : '';
                this.images[i].checked = this.chekarTodoasImg;
                this.images[i].oculto = this.chekarTodoasImg;
            }
        }
    }

    carregarUrlDownload(imagens) {
        if (imagens[0].id) {
            let base64 = imagens[0].image;
            if (imagens[0].type != "PDF") {
                var i = new Image();
                i.src = "data:imagem/png;base64," + base64.toString();
                i.onload = () => {
                    imagens[0].largura = i.width;
                    imagens[0].altura = i.height;
                    base64 = this.conversorDocumentosUtil.converteListaImgEmPdf(imagens);
                    this.carregarImagemPDF(base64);
                };
            } else {
                this.carregarImagemPDF(base64);
            }


        } else {
            document.getElementById("linkDownload").style.visibility = "hidden";
        }

    }

    private carregarImagemPDF(base64: any) {
        const urlDownload = Utils.convertBase64ArrayBytesDownload(base64);
        document.getElementById("linkDownload").setAttribute("download", "Documento.pdf");
        document.getElementById("linkDownload").setAttribute("href", urlDownload);
        document.getElementById("linkDownload").style.visibility = "visible";
        return base64;
    }

    ordenarListaImages(obj: any) {
        let i = obj.index;
        this.images.splice(this.images.indexOf(obj), 1);
        if (i <= 0) {
            this.images.unshift(obj);
        } else if (i > this.images.length) {
            this.images.push(obj);
        } else {
            i = i - 1;
            this.images.splice(i, 0, obj);
        }
        if (obj.type == "application/pdf") {
            this.ordemPaginasAlteradaChange.emit(true);
        }
        this.createIndexForImages();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.changes = changes;
        this.definindoTituloClassificaOuReclassifica();
        this.atualizaVisualizacaoImagem(changes);
        if (changes.images) {
            this.convertePdfListaImagem(changes.images.currentValue);
        }
        this.habilitarBtnClassificar = this.images.some(img => img.checked);
        if (this.habilitarBtnClassificar) {
            this.isMostrarBotaoExtracaoExterna = false;
        }
        this.validarBotaoModalExtracaoDeDados();
        this.validarBotaoExtracaoDeDados();
        this.habilitarBtnAcao = this.images.some(img => img.id == undefined);
    }

    validarBotaoModalExtracaoDeDados(){
        //condição adicionada pra atender extração manual quando o documento não foi persistindo 
        let tipoDocumentoId: number;
        if(this.images[0] && this.images[0].tipoDocumentoId ){
            tipoDocumentoId = this.images[0].tipoDocumentoId;

        }else if(this.selecionadoFolhaDoNoDaArvore && this.selecionadoFolhaDoNoDaArvore.documentoArvore 
            && this.selecionadoFolhaDoNoDaArvore.documentoArvore.tipo_documento 
            && this.selecionadoFolhaDoNoDaArvore.documentoArvore.tipo_documento.idNodeApresentacao){

            tipoDocumentoId = this.selecionadoFolhaDoNoDaArvore.documentoArvore.tipo_documento.idNodeApresentacao;
            this.images[0].tipoDocumentoId = tipoDocumentoId;

        }else if(this.changes && this.changes.images && this.changes.images.currentValue && this.changes.images.currentValue.images
            && this.changes.images.currentValue.images[0] && this.changes.images.currentValue.images[0].idTipoDocumento){

            tipoDocumentoId = this.changes.image.currentValue.images[0].idTipoDocumento;
            this.images[0].tipoDocumentoId = tipoDocumentoId;   
        }

        this.isMostrarBotaoModalExtracao = this.hasCredentialModalExtracaoDeDados();
        this.isDocumentoTemAtributoExtracao = this.isExisteAtributoExtracao(tipoDocumentoId);
        this.tituloHint = this.isDocumentoTemAtributoExtracao || !this.isMostrarBotaoModalExtracao ? '' : 'Tipo de documento sem definição de atributos para extração';
    }

    validarBotaoExtracaoDeDados(){
        let exibeBotao
        exibeBotao = this.images.some(img => img.id != undefined)
        if (exibeBotao) {
            exibeBotao = this.isPerfilMostrarBotaoExtracaoExterna && this.isPermiteExtracaoExterna(this.images[0].tipoDocumentoId);
            this.isMostrarBotaoExtracaoExterna = exibeBotao;
            let image:any = this.images[0]
            this.isAnaliseOutsourcing = image.analise_outsourcing;
            if (!this.isAnaliseOutsourcing) { 
                this.extracaoHint = 'Enviar Documento para o serviço de Outsourcing Documental.'
            } else {
                this.extracaoHint = 'Documento já submetido ao serviço externo.'
            }
        }
    }

     clicouExtracaoExterna() {
        if (this.images[0].id) {
            this.loadService.show()
            this.visualizadorService.postExtracaoExterna(this.images[0].id, false).subscribe(resp => {
                let messageSuccess = new Array<string>();
                messageSuccess.push('Operação realizada com sucesso');
                this.isAnaliseOutsourcing = true;
                this.extracaoHint = 'Documento já submetido ao serviço externo.'
                this.alertMessagesSucessChanged.emit(messageSuccess);
                this.loadService.hide()
            }, error => {
                let messageError = new Array<string>();
                if (error.status == 409) {
                    messageError.push(error.error.detalhe);
                    
                } else {
                    messageError.push(error.message);
                }
                this.alertMessagesErrorChanged.emit(messageError);
                this.loadService.hide();
            })
        }
    }

    isExisteAtributoExtracao(tipoDocumentoId: number):boolean{
        let existe = false;
        let tipologia: any = JSON.parse(this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia))

        if (tipologia && tipologia.tipos_documento.length > 0) {
            for(let tipoDocumento of tipologia.tipos_documento){
                if(tipoDocumento.id == tipoDocumentoId){
                    if(tipoDocumento.atributos_documento && tipoDocumento.atributos_documento.length > 0){
                        existe = true;
                        this.atributosDocumentoNaoPersistido = tipoDocumento.atributos_documento;
                        break;
                    }
                }
            }
        }

        return existe;
    }

    isPermiteExtracaoExterna(tipoDocumentoId: number):boolean{
        let existe = false;
        let tipologia: any = JSON.parse(this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia))
        
        if (tipologia && tipologia.tipos_documento.length > 0) {
            for(let tipoDocumento of tipologia.tipos_documento){
                if(tipoDocumento.id == tipoDocumentoId){
                    if(tipoDocumento.codigo_tipologia && tipoDocumento.permite_extracao_externa) {
        
                        existe = true;
                        break;
                    }
                    
                    //if(tipoDocumento.atributos_documento && tipoDocumento.atributos_documento.length > 0){
                        
                    //}
                }
            }
        }

        return existe;
    }

    /**
	 * Verifica se o usuário logado possui a permissao: MTRADM, MTRSDNTTO ou MTRSDNTTG.
	 */
	hasCredentialModalExtracaoDeDados(): boolean {
		const credentials: string = `${PERFIL_ACESSO.MTRADM},${PERFIL_ACESSO.MTRSDNTTO},${PERFIL_ACESSO.MTRSDNTTG}`;
		if (!environment.production) {
			return this.applicationService.hasCredential(credentials, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.applicationService.hasCredential(credentials, this.authenticationService.getRolesSSO());
		}
	}

    private atualizaVisualizacaoImagem(changes: SimpleChanges) {
        this.manterPdfEmMiniatura = (this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.miniaturaPDF) == 'false') ? false : true;
        if (changes.images && changes.images.currentValue != null && changes.images.currentValue.length > 0 && !changes.images.currentValue.find(image => image.type == "PDF")) {
            this.images = changes.images.currentValue;
            this.recuperaDocumentosPdfMiniatura(changes.images.currentValue);
            this.currentImage = changes.images.currentValue[0].image;
            this.mostrarImagem = false;
            this.createIndexForImages();
            if (this.images[0].reclassificar) {
                this.images.forEach((img, idx) => {
                    this.ativandoImagemParaClassificarExibirImagem((+idx + 1), true, true);
                });
                this.definirQtdImagemCarregada();
            } else {
                this.ativandoImagemParaClassificarExibirImagem(!this.verificarChekados() ? this.indUltimoChekado : 0, true, false);
            }
            if (!this.incluirImagem) {
                this.existeDocumentoAClassificar();
            }
        }
        if (this.manterPdfEmMiniatura && this.incluirImagem) {
            if (changes.images || changes.imagesCopy) {
                this.images = this.conversorDocumentosUtil.manterPdfsMiniaturaVisualizacao(changes.images ? changes.images.currentValue : changes.imagesCopy.currentValue);
            }
        } else {
            if (changes.images || changes.imagesCopy) {
                this.images = this.conversorDocumentosUtil.addPaginasRestantesPdfVisualizacao(changes.images ? changes.images.currentValue : changes.imagesCopy.currentValue);
            }
        }
        this.createIndexForImages();
    }

    /**
     * Recupera pdfs caso a funcionalidade miniatura estiver marcado; caso 
     *  contrário é apresentada a documentação completa no visualizador de documentos
     * @param images 
     */
    private recuperaDocumentosPdfMiniatura(images: DocumentImage[]) {
        if (!this.images.some(image => image.id ? true : false)
            && this.images.some(image => !image.reclassificar)
            && this.manterPdfEmMiniaturaEstadoAnterior != this.manterPdfEmMiniatura) {
            this.manterPdfEmMiniaturaEstadoAnterior = this.manterPdfEmMiniatura;
            this.images = this.conversorDocumentosUtil.addPaginasRestantesPdfVisualizacao(images);
        }
    }

    private definirQtdImagemCarregada() {
        let qtdObj = {
            qtd: this.images.length,
            id: this.images[0].id
        };
        this.eventService.broadcast(EVENTS.QTD_IMAGEM_VISUALIZACAO, qtdObj);
    }

    convertePdfListaImagem(arrayImages: any) {
        let messagesError = new Array<string>();
        let arrayImagesCop: Array<any> = new Array<any>();
        arrayImagesCop = arrayImages;
        let promise = Promise.resolve();
        if (!this.incluirImagem) {
            this.carregarUrlDownload(arrayImages);
        }
        arrayImagesCop.forEach(image => {
            if (image.type == "PDF") {
                arrayImages = [];
                let pdfAsArray = this.conversorDocumentosUtil.convertDataURIToBinary(`data:application/pdf;base64,${image.image}`);
                PDFJS.getDocument(pdfAsArray).then(pdfSuccess => {
                    for (let i = 1; i <= pdfSuccess.numPages; i++) {
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
                            promise.then((pdfImgBase64: any) => {
                                this.updateImageToList(arrayImages, pdfImgBase64, image);
                                this.loadService.showProgress(pdfSuccess.numPages, arrayImages.length);
                                if (pdfSuccess.numPages == arrayImages.length) {
                                    this.changes.images.currentValue = arrayImages;
                                    this.atualizaVisualizacaoImagem(this.changes);
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
            } else {
                this.loadService.hide();
            }
        });
    }

    updateImageToList(arrayImages: any, pdfImgBase64: any, image: any) {
        if (arrayImages.length == 0) {
            image.image = pdfImgBase64.substring(pdfImgBase64.indexOf(',') + 1);
            image.type = 'image/png';
            arrayImages.push(image);
        } else {
            let imageCopy = Object.assign({}, image);
            imageCopy.image = pdfImgBase64.substring(pdfImgBase64.indexOf(',') + 1);
            imageCopy.index = arrayImages.length + 1;
            imageCopy.type = 'image/png';
            arrayImages.push(imageCopy);
        }
    }

    imageChecked(item, index) {
        if (this.incluirImagem) {
            let contatdor = this.verificarSeExisteAtivo();
            if (contatdor > 1 || !item.checked) {
                this.ativandoDesativandoImagem = true;
                if (this.images.length > 1 && !this.mostrarImagem) {
                    this.desmarcaCheckarTodos(contatdor, item);
                    const prefixImage = 'imagem_';
                    if (document.getElementById(prefixImage + index) != null) {
                        if (this.images.some(img => img.checked)) {
                            this.ativandoDesativandoImagem = true;
                        }
                        this.ativandoImagemParaClassificarExibirImagem(index, false, false);
                    }
                }
                this.mostrarImagem = false;
            }
        }
    }

    private desmarcaCheckarTodos(contatdor: number, item: any) {
        if (contatdor > 1 && item.checked && this.chekarTodoasImg) {
            this.chekarTodoasImg = !this.chekarTodoasImg;
        }
    }

    private verificarSeExisteAtivo() {
        let contatdor = 0;
        for (let img of this.images) {
            if (img.checked) {
                ++contatdor;
            }
        }
        return contatdor;
    }

    /**
     * emite um evento para o componente do classficador generico para ativar o desativar a combo de classifcação
     * @param ativar 
     */
    private ativaDesativarComboClassificacao(image: any) {
        if (image.id) {
            this.desativaComboClassificacao = true;
        } else {
            this.desativaComboClassificacao = false;
        }
    }

    private ativandoImagemParaClassificarExibirImagem(idx: any, existeCkekado: boolean, reclassificar: boolean) {
        let index = idx == 0 ? 0 : idx - 1;
        this.images[index].checked = this.validarChekadosTodos(existeCkekado, reclassificar, index);
        this.images[index].ativo = this.validarChekadosAtivo(index);
        this.images[index].oculto = this.validarCheckadosOculto(index);
        if (this.images[index].checked) {
            this.ativaDesativarComboClassificacao(this.images[index]);
            this.indexVisualizador = 0;
        }
    }

    private validarCheckadosOculto(index: number): boolean {
        if (this.chekarTodoasImg) {
            return this.chekarTodoasImg;
        }
        return this.images[index].checked ? true : false;
    }

    private validarChekadosAtivo(index: number): string {
        if (this.chekarTodoasImg) {
            return "imagem_Ativa";
        }
        return this.images[index].checked ? "imagem_Ativa" : "";
    }

    private validarChekadosTodos(existeCkekado: boolean, reclassificar: boolean, index: number): boolean {
        if (this.chekarTodoasImg) {
            return this.chekarTodoasImg;
        }
        return existeCkekado || reclassificar ? true : !this.images[index].checked;
    }

    ngAfterViewChecked(): void {
        this.cdRef.detectChanges();
        if (this.ativandoDesativandoImagem) {
            if (this.verificarChekados()) {
                this.ativarPrimeiraImagem(1);
            }
        }
    }

    private verificarChekados() {
        let qtdChekeado = 0;
        this.images.forEach(img => {
            if (img.checked) {
                qtdChekeado = qtdChekeado + 1;
                this.indUltimoChekado = img.index;
            }
        });
        return qtdChekeado == 0;
    }

    private ativarPrimeiraImagem(index) {
        this.ativandoImagemParaClassificarExibirImagem(index, false, false);
        this.indexVisualizador = index == 0 ? index : index - 1;
    }

    removeImage(idx) {
        let index = idx;
        if (this.images != null) {
            this.removeDocumentosVisualizador(index);
        }
    }

    /**
     * Gerencia a remoção de documetos de diversos tipos do visualizador de documentos
     * @param indexImage 
     */
    private removeDocumentosVisualizador(indexImage: number) {
        let indiceDocPdf = this.images[indexImage - 1].indiceDocListPdfOriginal;
        if (undefined != indiceDocPdf) {
            this.conversorDocumentosUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
                arrayArquivosPdfOriginal.splice(indiceDocPdf, 1);
                this.conversorDocumentosUtil.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(arrayArquivosPdfOriginal);
                this.conversorDocumentosUtil.$paginasRestantesPdf.subscribe(arrayPaginasRestantesPdf => {
                    arrayPaginasRestantesPdf = arrayPaginasRestantesPdf.filter(paginaPdf => paginaPdf.indiceDocListPdfOriginal != indiceDocPdf);
                    this.conversorDocumentosUtil.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(arrayPaginasRestantesPdf);
                    this.removerItemListaDeImages(+indexImage - 1);
                    this.removerItemDeImagesCopy();
                    this.existeDocumentoAClassificar();
                    if (this.verificarChekados() && this.images.length > 0) {
                        this.ativandoImagemParaClassificarExibirImagem(indexImage, false, false);
                    }
                },
                    () => {
                        this.loadService.hide();
                    });
            },
                () => {
                    this.loadService.hide();
                });
        } else {
            this.removerItemListaDeImages(+indexImage - 1);
            this.removerItemDeImagesCopy();
            this.existeDocumentoAClassificar();
            if (this.verificarChekados() && this.images.length > 0) {
                this.ativandoImagemParaClassificarExibirImagem(indexImage, false, false);
            }
        }
    }

    private removerItemListaDeImages(index: any) {
        let imageExclusao = this.images[index];
        let totalImagesPdfaAtual = this.images.filter(image => {
            return image.indiceDocListPdfOriginal == imageExclusao.indiceDocListPdfOriginal
        });
        if (totalImagesPdfaAtual.length == imageExclusao.totalPaginas) {
            Utils.showMessageConfirm(this.dialogService, 'Deseja excluir uma página desse pdf?' +
                'Caso sim, esse documento não será possível mais ser visualizado na forma de miniatura.').subscribe(res => {
                    if (res) {
                        this.atribuiTodasImagensResntesPdfComoPrimeiraPagina(index);
                        this.images.splice(index, 1);
                        this.createIndexForImages();
                        this.atulizandoMemoriaDaListaImages();
                        this.removerItemDeImagesCopy();
                    }
                },
                    () => {
                        this.loadService.hide();
                    });
        } else {
            this.images.splice(index, 1);
            this.createIndexForImages();
            this.atulizandoMemoriaDaListaImages();
        }
    }

    private atribuiTodasImagensResntesPdfComoPrimeiraPagina(indexImageExclusao) {
        let imageExclusao = this.images[indexImageExclusao];
        this.images.forEach(image => {
            if (image.indiceDocListPdfOriginal == imageExclusao.indiceDocListPdfOriginal) {
                image.primeiraPagina = true;
            }
        });
    }

    private atulizandoMemoriaDaListaImages() {
        this.images = Object.assign([], this.images);
        this.imagesChange.emit(this.images);
    }

    private removerItemDeImagesCopy() {
        this.imagesCopy = [];
        this.imagesCopy = this.images;
        this.imagesCopy = Object.assign([], this.imagesCopy);
        const classificacaoDocumento: ClassificacaoDocumento = new ClassificacaoDocumento();
        classificacaoDocumento.imagensClassificar = this.imagesCopy;
        classificacaoDocumento.qtdClassificar = this.imagesCopy.length;
        this.imagesCopyChanged.emit(classificacaoDocumento);
    }

    showImagem(index) {
        this.mostrarImagem = true;
        this.indexVisualizador = index - 1;
    }

    existeDocumentoAClassificar() {
        this.documentoAClassificar = !this.incluirImagem ? false : this.imagesCopy.length > 0 ? true : false;
        this.incluirImagem = this.documentoAClassificar;
        this.documentoAClassificarChange.emit(this.documentoAClassificar);
    }

    chekarTodasImagens() {
        this.chekarTodoasImg = !this.chekarTodoasImg;
        this.applicationService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.marcaTodos, this.chekarTodoasImg);
        if (this.chekarTodoasImg) {
            for (let i in this.images) {
                if (!this.images[i].checked) {
                    this.ativandoImagemParaClassificarExibirImagem((+i + 1), false, false);
                }
            }
        } else {
            for (let i in this.images) {
                if (i != "0" && this.images[i].checked) {
                    this.ativandoImagemParaClassificarExibirImagem((+i + 1), false, false);
                } else {
                    this.showImagem((+i + 1));
                }
            }
            this.mostrarImagem = false;
        }
    }

    excluirTodasImagens(eve: any) {
        if (eve.currentTarget.checked) {
            Utils.showMessageConfirm(this.dialogService, 'Deseja excluir todas as Imagens?').subscribe(res => {
                if (res) {
                    this.conversorDocumentosUtil.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(new Array<ArquivoPdfOriginal>());
                    this.conversorDocumentosUtil.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(new Array<DocumentImage>());
                    this.images = [];
                    this.removerItemDeImagesCopy();
                    this.atulizandoMemoriaDaListaImages();
                    this.desabilitarCheckboxDocumentoAClassificar();
                    this.addMessageSuccess('Lista de Imagens, removida com sucesso.');
                } else {
                    this.excluirTodasimagem = false;
                }
            },
                () => {
                    this.loadService.hide();
                });
        }
    }

    private desabilitarCheckboxDocumentoAClassificar() {
        this.documentoAClassificar = false;
        this.documentoAClassificarChange.emit(this.documentoAClassificar);
    }

    acaoClassificar() {
        this.images;
        this.imagesChange;
        this.imagesCopy;
        this.imagesCopyChanged;
        this.alteradoArvoreChanged.emit(true);
        let arquivosPDF: DocumentImage[] = new Array<DocumentImage>();
        let imagensSelecionadasPDF: DocumentImage[] = new Array<DocumentImage>();
        this.selecaoImagensCheked();
        arquivosPDF = this.recuperaArquivosPDFsCompleto(arquivosPDF);
        const msgConfirmaClassificacao: string = this.montaMensagemConfirmacaoClassificacaoParaParteArquivoPDF(arquivosPDF);
        imagensSelecionadasPDF = this.imagensSelecionadas.filter(imagemSelecionada => imagemSelecionada.type.indexOf(MYME_TYPE.APPLICATION_PDF) == 0 || imagemSelecionada.type.indexOf(MYME_TYPE.IMAGE_PDF) == 0);
        if (!this.manterPdfEmMiniatura
            && arquivosPDF.length > 0
            && imagensSelecionadasPDF.length < arquivosPDF.length
            && arquivosPDF.some(pdf => !pdf.primeiraPagina)) {
            Utils.showMessageConfirm(this.dialogService, msgConfirmaClassificacao).subscribe(res => {
                if (res) {
                    this.marcaComoPrimeiraPaginaArquivoCompletoPDF(arquivosPDF);
                    this.realizaClassificacaoDocumentoSelecionado();
                }
            },
                () => {
                    this.loadService.hide();
                });
        } else {
            this.realizaClassificacaoDocumentoSelecionado();
        }
    }

    /**
     * Caso o usuario classifica parte de um pdf. O arquivo completo será marcado como primeira página
     * @param arquivosPDF 
     */
    private marcaComoPrimeiraPaginaArquivoCompletoPDF(arquivosPDF: DocumentImage[]) {
        arquivosPDF.forEach(pdf => {
            this.images.forEach(imagem => {
                const filePDF: boolean = this.fileIsPdf(pdf, imagem);
                if (filePDF && imagem.indiceDocListPdfOriginal == pdf.indiceDocListPdfOriginal) {
                    imagem.primeiraPagina = true;
                }
            })
        });
    }

    /**
     * Monta os nomes dos arquivos pdfs que foram selecionados pelo usuários de maneira incompleta
     * @param arquivosPDF 
     */
    private montaMensagemConfirmacaoClassificacaoParaParteArquivoPDF(arquivosPDF: DocumentImage[]): string {
        let msg: string = "";
        let msgs: string[] = new Array<string>();
        arquivosPDF.forEach(imagem => {
            if (!imagem.primeiraPagina && !msgs.includes(imagem.name)) {
                msgs.push(imagem.name);
                msg += `${imagem.name}, `;
            }
        })
        if (msgs.length > 1) {
            msg = msg.substring(0, msg.length - 2);
        }
        const commaControle: string[] = msg.split(",");
        let msgFormatada: string = "";
        if (msgs.length > 1) {
            commaControle.forEach((msg, index) => {
                if (index == commaControle.length - 1) {
                    msgFormatada = msgFormatada.substring(0, msgFormatada.length - 2);
                    msgFormatada += ` e ${msg}.`;
                } else {
                    msgFormatada += `${msg}, `;
                }
            });
        } else {
            msgFormatada = msg.replace(",", ".").trim();
        }
        let msgClassificacao = VISUALIZADOR_DOCUMENTOS.MSG_CLASSIFICACAO_PARTE_PDF.replace("[0]", msgFormatada);
        return msgClassificacao;
    }

    /**
     * realiza a classificação de arquivo(s) selecionado(s).
     */
    private realizaClassificacaoDocumentoSelecionado() {
        let existePdfSelecinado: boolean = this.imagensSelecionadas.some(imagemSeleciona => imagemSeleciona.type == MYME_TYPE.APPLICATION_PDF && !imagemSeleciona.reclassificar);
        if (this.manterPdfEmMiniatura && existePdfSelecinado) {
            this.recuperaImagensRestantesOrdenadas();
        }
        if (existePdfSelecinado) {
            this.verificaHouveAlteracaoDocsPDFNaClassificacao(this.imagensSelecionadas.length);
        }
        let tipoDocumentoArvoreGenerica: TipoDocumentoArvoreGenerica = this.parametrosObrigatorioParaClassificacao();
        if (this.tipoDossie == TIPO_ARVORE.ARVORE_DOSSIE_CLIENTE) {
            this.indexArvore = 0;
            this.classificarCliente(tipoDocumentoArvoreGenerica);
            this.idSeletor = TIPO_SELETOR.SELETOR_LISTA_TIPO_DOCUMENTO;
            this.limparImagensSelecionadasEManterImagensNaoSelecionadas();
        }
        else {
            this.idSeletor = TIPO_SELETOR.SELETOR_LISTA_ARVORE;
             if(this.classificarProduto(tipoDocumentoArvoreGenerica)){
                this.limparImagensSelecionadasEManterImagensNaoSelecionadas();
             }
        }
        this.mudancaSalvaService.setIsMudancaSalva(false);
    }

    /**
     * Busca todos arquivos pdfs proveniente da seleção de parte de um PDF
     * @param arquivosPDF 
     */
    private recuperaArquivosPDFsCompleto(arquivosPDF: DocumentImage[]): DocumentImage[] {
        this.imagensSelecionadas.forEach(imagemSelecionada => {
            this.images.forEach((imagem) => {
                const filePDF: boolean = this.fileIsPdf(imagemSelecionada, imagem);
                if (filePDF && imagem.indiceDocListPdfOriginal == imagemSelecionada.indiceDocListPdfOriginal) {
                    if (!arquivosPDF.includes(imagem)) {
                        arquivosPDF.push(imagem);
                    }
                }
            });
        });
        return arquivosPDF;
    }

    /**
     * Verifica se parametros passados; se ambos são pdf
     * @param imagemSelecionada 
     * @param imagem 
     */
    private fileIsPdf(imagemSelecionada: DocumentImage, imagem: DocumentImage) {
        const fileImagemSelecionadaPDF: boolean = imagemSelecionada.type == MYME_TYPE.APPLICATION_PDF || imagemSelecionada.type == MYME_TYPE.IMAGE_PDF;
        const fileImagemPDF: boolean = imagem.type == MYME_TYPE.APPLICATION_PDF || imagem.type == MYME_TYPE.IMAGE_PDF;
        const filePDF: boolean = fileImagemSelecionadaPDF && fileImagemPDF;
        return filePDF;
    }

    /**
     * Recupera todas imagens restantes de pdfs que foram previamente habilitados como miniatura; mantendo 
     * sua ordenação original
     */
    private recuperaImagensRestantesOrdenadas() {
        let imagensSelecionadasRestantes: DocumentImage[];
        this.conversorDocumentosUtil.$paginasRestantesPdf.subscribe(arrayPaginasRestantes => {
            imagensSelecionadasRestantes = this.inicializarImagensRestantesSelecionadas(arrayPaginasRestantes, this.imagensSelecionadas);
        },
            () => {
                this.loadService.hide();
            });
        this.imagensSelecionadas = this.conversorDocumentosUtil.ordenaImagensConformeMiniaturaPDF(imagensSelecionadasRestantes, this.imagensSelecionadas);
    }

    /**
     * recupera todas imagens restantes de pdfs que estavam habilitados com opção miniatura
     * @param imagensSelecionadasRestantes 
     * @param arrayPaginasRestantes 
     */
    private inicializarImagensRestantesSelecionadas(arrayPaginasRestantes: DocumentImage[], imagensSelecionadas: DocumentImage[]): DocumentImage[] {
        let imagensSelecionadasRestantes: DocumentImage[] = new Array<DocumentImage>();
        let imagensTratadas: Array<DocumentImage> = new Array<DocumentImage>();
        arrayPaginasRestantes.forEach(documentImage => {
            imagensSelecionadasRestantes.push(documentImage);
        });
        imagensSelecionadas.forEach(imagemSelecionada => {
            imagensSelecionadasRestantes.forEach(imagemRestante => {
                if (imagemSelecionada.checked && imagemRestante.indiceDocListPdfOriginal == imagemSelecionada.indiceDocListPdfOriginal) {
                    imagensTratadas.push(imagemRestante);
                }
            });
        });
        return imagensTratadas;
    }

    private classificarProduto(tipoDocumentoArvoreGenerica: TipoDocumentoArvoreGenerica) {
        this.existeDocumentoFuncao = false;
        let existeTipoDocumento = false;
        this.listaIndexArvoreSelecionada.forEach(arvSelecionada => {
            let arv: any = arvSelecionada;
            this.listaVinculoArvore[arv.index].alterandoVinculo = true;
            if (this.listaVinculoArvore[arv.index] instanceof VinculoArvoreGarantia && !this.existeDocumentoFuncao) {
                for (let no of this.listaVinculoArvore[arv.index].noApresentacao) {
                    if (no.children.length > 0 && !this.existeDocumentoFuncao) {
                        this.encontrarDocumento(no, tipoDocumentoArvoreGenerica);
                    } else if (no.id === tipoDocumentoArvoreGenerica.idNodeApresentacao) {
                        this.existeDocumentoFuncao = true;
                        existeTipoDocumento = true;
                        break;
                    }
                }
            }
        });

        if (!this.existeDocumentoFuncao || existeTipoDocumento) {
            GerenciadorDocumentosEmArvore.criarConteudoNodesByImage(this.imagensSelecionadas, this.usuario, this.listaIndexArvoreSelecionada, tipoDocumentoArvoreGenerica, this.tipoDocumentoArvoreGenericaAnterior, this.dossieCliente);
        } else {
            let messagesError = new Array<string>();
            messagesError.push('Só pode ter um documento por função documental');
            this.alertMessagesErrorChanged.emit(messagesError);
            return false;
        }
        return true;
    }

    private encontrarDocumento(node: NodeApresentacao, tipoDocumentoArvoreGenerica: TipoDocumentoArvoreGenerica) {
        if (node.children) {
            for (let childNode of node.children) {
                if (childNode.leaf != true) {
                    this.encontrarDocumento(childNode, tipoDocumentoArvoreGenerica);
                } else if (childNode.parent.parent) {
                    for (let noChildren of childNode.parent.parent.children) {
                        if (noChildren.id == tipoDocumentoArvoreGenerica.idNodeApresentacao) {
                            this.existeDocumentoFuncao = true;
                            return;
                        }
                    }
                }
            }
        }
    }

    private classificarCliente(tipoDocumentoArvoreGenerica: TipoDocumentoArvoreGenerica) {
        GerenciadorDocumentosEmArvore.criaConteudoNodeByImage(this.imagensSelecionadas, this.usuario, this.indexArvore, tipoDocumentoArvoreGenerica, this.tipoDocumentoArvoreGenericaAnterior, this.dossieCliente);
    }

    private parametrosObrigatorioParaClassificacao() {
        let tipoDocumentoArvoreGenerica: TipoDocumentoArvoreGenerica = new TipoDocumentoArvoreGenerica();
        tipoDocumentoArvoreGenerica.idNodeApresentacao = this.tipoDocumento.id;
        tipoDocumentoArvoreGenerica.nome = this.tipoDocumento.nome;
        this.tipoDocumentoArvoreGenericaAnterior = this.incluirImagem ? undefined : this.tipoDocumentoArvoreGenericaAnterior;
        this.usuario = this.getMatriculaUser();
        return tipoDocumentoArvoreGenerica;
    }

    private limparImagensSelecionadasEManterImagensNaoSelecionadas() {
        let novas = [];
        for (const i1 of this.images) {
            if (!i1.checked) {
                novas.push(i1);
            }
        }
        this.images = novas;
        if (this.incluirImagem) {
            this.atualizarListaCopy();
        }
        this.atulizandoMemoriaDaListaImages();
        if (this.images.length > 0) {
            this.ativarPrimeiraImagem(0);
        }
    }

    atualizarListaCopy() {
        let novas = [];
        for (const i1 of this.imagesCopy) {
            if (!i1.checked) {
                novas.push(i1);
            }
        }
        this.imagesCopy = novas;
        this.imagesCopy = Object.assign([], this.imagesCopy);
        const classificacaoDocumento: ClassificacaoDocumento = new ClassificacaoDocumento();
        classificacaoDocumento.imagensClassificar = this.images;
        classificacaoDocumento.qtdClassificar = this.images.length;
        this.imagesCopyChanged.emit(classificacaoDocumento);
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

    somarMaisUm(index) {
        return index + 1;
    }

    private selecaoImagensCheked() {
        this.imagensSelecionadas = [];
        this.images.forEach(img => {
            if (img.checked) {
                this.imagensSelecionadas.push(img);
            }
        });
    }

    private verificaHouveAlteracaoDocsPDFNaClassificacao(qtd: number) {
        this.conversorDocumentosUtil.$arquivosPdfOringinais.subscribe(arrayArquivosPdfOriginal => {
            arrayArquivosPdfOriginal.forEach((pdf, indice) => {
                let imagesMesmoPdf: DocumentImage[] = this.imagensSelecionadas.filter(images => images.indiceDocListPdfOriginal == indice);
                if (qtd > pdf.quantidadePaginas || pdf.quantidadePaginas > imagesMesmoPdf.length) {
                    pdf.alteradoNaClassfificacao = true;
                }
            });
        },
            () => {
                this.loadService.hide();
            });
    }

    addPosicao(posicao: any) {
        return +posicao + 1;
    }

    abrirModalExtracaoDeDados(){
        this.loadService.show();
        let id: string = this.images[0].id ? this.images[0].id : null;
        let binario = "";
        let minetype = "";
        let atributos: any [] = [];

        atributos = this.selecionadoFolhaDoNoDaArvore.atributos && this.selecionadoFolhaDoNoDaArvore.atributos.length > 0 ?  this.selecionadoFolhaDoNoDaArvore.atributos : this.atributosDocumentoNaoPersistido;

        if(this.images && this.images.length > 1){
            let conteudos = Object.assign([], this.images);
            binario = this.conversorDocumentosUtil.converteListaImgEmPdf(conteudos);
            minetype =  "application/pdf";
        }else{
            binario = this.images[0].image;
            minetype = this.images[0].type;
        }

        this.dialogService.addDialog(ModalExtracaoDadosDocumento, {
            idDocumento: id,
            binario: binario,
            mimetype: minetype,
            idTipoDocumento: this.images[0].tipoDocumentoId,
            atributos: atributos
        }).subscribe(result => {

            if(result.sucesso){
                if(result.mensagem){
                    let messagesSucesso = new Array<string>();
                    messagesSucesso.push(result.mensagem);
                    
                    // Realizar reload da tela somente quando estiver no dossie-cliente
                    if(result.tipoRetorno == 'salvar' && this.dossieCliente){
                        this.applicationService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, messagesSucesso);  
                        this.routerService.recarregaDossieClienteDepoisDeSalvo(this.identificadorDossie, ABA_DOSSIE_CLIENTE.DOCUMENTOS);
                    }else{
                        this.alertMessagesSucessChanged.emit(messagesSucesso);
                        this.loadService.hide();
                    }                    
                }
            }else{
                if(result.mensagem){
                    let messageError = new Array<string>();
                    messageError.push(result.mensagem);
                    this.alertMessagesErrorChanged.emit(messageError);
                    this.loadService.hide();
                }

                // cenário quando recupera atributos do documento quando o mesmo não foi persistido.
                if(result.atributos && result.atributos.length > 0){
                    
                   let listaAtributos: any [] = [];

                    result.atributos.forEach(elemento => {
                        let atributo = {chave: elemento.chave, valor: elemento.valor};
                        listaAtributos.push(atributo);
                    });

                    this.selecionadoFolhaDoNoDaArvore.atributos = listaAtributos;
                }

                this.loadService.hide();
            }
        });

    }
} 