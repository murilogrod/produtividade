import { Component, OnInit, ViewEncapsulation, Input, Output, AfterViewInit, OnChanges, EventEmitter, ChangeDetectorRef, ViewChild, ElementRef } from "@angular/core";

import { ConversorDocumentosUtil } from "src/app/documento/conversor-documentos/conversor-documentos.util.service";
import { BehaviorSubject } from "rxjs";
import { ArquivoPdfOriginal } from "src/app/documento/conversor-documentos/model/arquivo-pdf-original";
import { EmptyVinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/empty-vinculo-arvore";
import { ArvoreTipoDocumento } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/arvore-tipo-documento";
import { AlertMessageService, ApplicationService, LoaderService, EventService } from "src/app/services";
import { ArvoreDinamica, VinculoCliente, FuncaoDocumental, TipoDocumento } from "src/app/model";
import { DocumentoNode } from "src/app/model/documentoNode";
import { NodeApresentacao } from "src/app/model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";
import { DocumentImage } from "src/app/documento/documentImage";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { TIPO_ARVORE, LOCAL_STORAGE_CONSTANTS, EVENTS } from "src/app/constants/constants";
import { TipoDocumentoArvoreGenerica } from "src/app/model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { GerenciadorDocumentosEmArvore } from "src/app/documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util";
import { FileUploader } from "ng2-file-upload";
import { AbaDocumentoComponentPresenter } from "../presenter/aba-documento.component.presenter";
import { TipoDocumentoService } from "../../../../cruds/tipo-documento/service/tipo-documento.service";
import { DialogService } from "angularx-bootstrap-modal";
import { ModalDadosDeclaradosComponent } from "../../../../cliente/consulta-cliente/aba-documentos/modal/modal-dados-declarados/view/modal-dados-declarados.component";
import { ConsultaClienteService } from "../../../../cliente/consulta-cliente/service/consulta-cliente-service";
import { ClassificacaoDocumento } from "src/app/documento/conversor-documentos/model/classificacao-documento";
import { SituacaoDocumento } from "../../../../documento/enum-documento/situacao-documento.enum";

declare var $: any;

@Component({
  selector: 'aba-documento',
  templateUrl: './aba-documento.component.html',
  styleUrls: ['./aba-documento.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AbaDocumentoComponent extends AlertMessageService implements OnInit, OnChanges, AfterViewInit {
  @Input() cliente: VinculoCliente;
  @Input() processo;
  @Input() usarAvaliacaoTomadorVigente;
  @Input() formularioSalvo;
  @Input() dossieProduto;
  @Input() idDossie;
  @Input() habilitaBotoesSalvar: boolean;
  @Input() habilitaAlteracao;
  @Input() selectedDocumentNode: DocumentoNode;
  @Input() habilitaTratamento;
  @Input() hasAlteracaoTrata;
  @Input() exibeLoadArvore = true;
  @Input() documentos: NodeApresentacao[];
  @Input() imagens: DocumentImage[];
  @Input() docFound;
  @Input() docFoundIna;
  @Input() listaExcluir: DocumentoNode[];
  @Input() processoGeraDossie: any;
  @Input() listaVinculoArvore: Array<VinculoArvore>;
  @Input() clickArvoreTipoDocumento: ArvoreTipoDocumento;
  @Output() hasAlteracaoTrataChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() processoGeraDossieChanged: EventEmitter<any> = new EventEmitter<any>();
  @Output() listaVinculoArvoreChanged: EventEmitter<Array<VinculoArvore>> = new EventEmitter<Array<VinculoArvore>>();
  @Input() clienteLista: any[];
  @Input() produtoLista: any[];
  @Input() garantias: any[];
  @Input() msgValidacao: any;
  @Input() listaGed;
  @Input() dossieCliente: boolean;
  @ViewChild('emptyTree') emptyTreeElement: ElementRef;

  tipoDossie: Number = TIPO_ARVORE.ARVORE_DOSSIE_PRODUTO;
  incluirImagem: boolean = true;
  documentoAClassificar: boolean;
  expandirAllTree: boolean = false;
  collapsed: boolean = true;
  collapsedVinculoPessoa: boolean;
  arvoreMontada: boolean;
  srcUpload: string;
  arvoreTipoDocumento: ArvoreTipoDocumento;
  /**
   * Guarda o ultimo tipo de documento classifcado no modal de classificação
   */
  private tipoDocumentoArvoreGenericaAnterior: TipoDocumentoArvoreGenerica;
  imagesReadOnly: DocumentImage[];
  treeEditable = true;
  isExpandedAll = false;
  uploader: FileUploader = new FileUploader(null);
  URLImage: string;
  urlSrc;
  images: DocumentImage[] = [];
  imagesCopy: DocumentImage[] = [];
  nodeSelectedChanged: DocumentoNode;
  indice = 0;
  funcoesDocumentais: FuncaoDocumental[] = [];
  tipoDocumentos: TipoDocumento[] = [];
  arvoreDinamica: ArvoreDinamica[] = [];
  habilitarDocumentoClassificar: boolean;
  qtdPages: number = 0;
  enviar = false;
  nu_situacao_motivo: string;
  nu_motivo: string = "";
  maxHeightbarraArvore: string;
  maxHeigtIncialArvore: number;
  /**
   * idx é o identificador de selecão de vinculo, produto ou garantia escolhida pelo usuario
   */
  idx: number = 0;
  qtdClassificar: number = 0;
  private selecionadoFolhaDoNoDaArvore: NodeApresentacao;

  constructor(
    private applicationService: ApplicationService,
    private conversorDocumentosUtil: ConversorDocumentosUtil,
    private abaDocumentoPresenter: AbaDocumentoComponentPresenter,
    private loadService: LoaderService,
    private tipoDocumentoServico: TipoDocumentoService,
    private dialogService: DialogService,
    private cdRef: ChangeDetectorRef,
    private conversorDocUtil: ConversorDocumentosUtil,
    private clienteService: ConsultaClienteService,
    private eventService: EventService) {
    super();
    this.conversorDocumentosUtil.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(new Array<ArquivoPdfOriginal>());
    this.conversorDocumentosUtil.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(new Array<DocumentImage>());
  }

  handleChangeImages(img) {
    this.forcaInicializarListaDeArvore(img);
    if( img.length == 0) {
      this.documentoAClassificar = this.incluirImagem ? true : false ;
      this.images = this.incluirImagem ? this.imagesCopy : [];
      this.images = Object.assign([], this.images );
    }else {
      this.images = [];
      this.images = Object.assign([], img);
    }
  }

  private forcaInicializarListaDeArvore(img: any) {
    if (this.images && this.images.length > img.length) {
      this.carregarListaVinculosReinicializar();
    }
  }

  handrleChangeClickArvoreTipoDocumento(input) {
    this.clickArvoreTipoDocumento = input;
  }

  handleChangeArvoreTipoDocumento(input){
    this.arvoreTipoDocumento = input;
  }

  handleChangeTipoDocumentoArvoreGenericaAnterior(input) {
    this.tipoDocumentoArvoreGenericaAnterior = input;
  }

  handleChangeSelecionadoFolhaDoNoDaArvore(input) {
    this.selecionadoFolhaDoNoDaArvore = input;
  }

  handlleMessagesWarning(messages) {
    this.messagesWarning = messages
  }

  handlleMessagesInfo(messages) {
    this.messagesInfo = messages;
  }

  handlleMessagesError(messages) {
    this.messagesSuccess = [];
    this.messagesError = Object.assign([], messages);
  }

  handlleMessagesSucess(messages) {
    this.messagesSuccess = messages;
  }

  handleChangeimagesCopy(classificacaoDocumento: ClassificacaoDocumento) {
    this.imagesCopy = classificacaoDocumento.imagensClassificar;
    this.qtdClassificar = classificacaoDocumento.qtdClassificar;
    this.documentoAClassificar = this.imagesCopy.length > 0;
  }

  handleChangeDocumentoAClassificarChange(input) {
    this.documentoAClassificar = input;
    this.incluirImagem = this.documentoAClassificar;
  }

  handleIncluirImagem(input) {
    this.incluirImagem = input;
  }

  handleChangeProcessoGeraDossie(input) {
    this.processoGeraDossie = input;
  }

  handleChangeCliente(input) {
    this.clienteLista = input;
  }

  handleChangeVinculo(input) {
    this.produtoLista = input;
  }

  handleChangeVinculoGarantia(input) {
    this.garantias = input;
  }

  handleChangeListaVinculoArvore(input) {
    this.listaVinculoArvore = input;
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

  handleChangeEmptyVinculoArvore(emptyVinculoArvore: EmptyVinculoArvore) {
    this.abaDocumentoPresenter.verificaEstruturaVaziaArvoresDossie(emptyVinculoArvore, this.listaVinculoArvore);
  }

  handleArvoreTipoDocumentoSelecionado(arvoreTipoDocumento: ArvoreTipoDocumento) {
    this.arvoreTipoDocumento = new ArvoreTipoDocumento();
    this.arvoreTipoDocumento = arvoreTipoDocumento;
  }

  ngOnInit() {
    this.habilitaBotoesSalvar;
    this.documentoAClassificar = false;
    this.habilitarDocumentoClassificar = false;
    this.listaExcluir = [];
    this.maxHeigtIncialArvore = 515;
    this.inicializarTamanhoScroll();
  }

  ngOnChanges(): void {
    this.isExpandedAll = false;
    this.clearAllMessages();
    this.carregarListaVinculosReinicializar();
    this.documentoAClassificar = false;
    this.images = [];
    this.messagemValidacaoCampoObrigatorio();
  }

  private carregarListaVinculosReinicializar() {
    this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
    GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
  }

  ngAfterViewChecked() {
    if (this.listaVinculoArvore && this.listaVinculoArvore.length > 0 && this.listaVinculoArvore[0].noApresentacao && !this.arvoreMontada) {
      this.arvoreMontada = true;
      GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
    }
    this.inicializarTamanhoScroll();
    this.cdRef.detectChanges();
  }

  private inicializarTamanhoScroll() {
    let tamanhoComparador = (+$("#exibirImagem").height() + 360);
    this.maxHeightbarraArvore = this.maxHeigtIncialArvore < +tamanhoComparador ? +tamanhoComparador + "px" : this.maxHeigtIncialArvore + "px";
  }

  checkBodyEmptyTree(vinculoArvore: VinculoArvore): boolean {
    return this.abaDocumentoPresenter.checkBodyEmptyTree(vinculoArvore);
  }

  showButtonsExpandOrCollapse(): boolean {
    return this.documentos != null && this.documentos.length > 0;
  }

  expandirAllTr() {
    this.expandirAllTree = !this.expandirAllTree;
    this.collapsed = !this.expandirAllTree;
    this.collapsedVinculoPessoa = !this.expandirAllTree;
  }

  onNodeSelected(event, idx) {
    this.nodeSelectedChanged = event;
    this.tipoDocumentoArvoreGenericaAnterior = new TipoDocumentoArvoreGenerica();
    this.tipoDocumentoArvoreGenericaAnterior.nome = event.parent.label;
    this.tipoDocumentoArvoreGenericaAnterior.idNodeApresentacao = event.parent.parent.id;
    this.tipoDocumentoArvoreGenericaAnterior.id = idx;
  }

  onSelectedDocumentNode(event) {
    this.selectedDocumentNode = event;
  }

  guardaEscolhaIdxAtual(idx) {
    this.idx = idx;
  }

  clickFile() {
    $("#inputDocument").val("");
  }

  insertDocuments(input) {
    if (input) {
      input.click();
    }
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

  showAtualizarCadastro(){
    
  }

   showModalDadosDeclarados(event, item) {
    //No click para abrir o modal Dados Declarados, não deve levar em consideração o abrir e fechar do fieldset, por isso inverte o valor do collapsed para manter o status atual do fieldset.
    this.collapsedVinculoPessoa = !this.collapsedVinculoPessoa;
    event.preventDefault();
    event.stopPropagation();
    let tipoDocumentoDadosDeclarados;
    let tipoPessoa = 'pessoa-juridica';
    if (item.vinculoCliente.tipo_pessoa === "F") {
      tipoPessoa = 'pessoa-fisica';
    }
    this.loadService.show();
    this.tipoDocumentoServico.consultarDadosDeclarados(tipoPessoa).subscribe(response => {
      tipoDocumentoDadosDeclarados = response.identificador_tipo_documento;
      this.dialogService.addDialog(ModalDadosDeclaradosComponent, {
        dadosDeclarados: response.atributos_extracao,
        idDossie: item.vinculoCliente.id,
        idTipoDocumento: response.identificador_tipo_documento,
        habilitaAlteracao: this.habilitaBotoesSalvar
      }).subscribe(retorno => {
        let promise: Promise<any> = this.clienteService.getClienteById(item.vinculoCliente.id).toPromise();
        promise.then(cliente => {
          let doc = cliente.documentos.filter(doc => doc.tipo_documento.id === tipoDocumentoDadosDeclarados)
            .reduce((docAnterior, docAtual) => docAnterior.id > docAtual.id ? docAnterior : docAtual);
          let nodeDadosDeclarado = item.noApresentacao.filter(node => node.id === tipoDocumentoDadosDeclarados);
          nodeDadosDeclarado[0].children = [];
          doc.tipo_documento.id = doc.tipo_documento.id;
          doc.conteudos = [];
          doc.docReutilizado = true;
          doc.situacaoAtual  = SituacaoDocumento.CRIADO;
          
          GerenciadorDocumentosEmArvore.criaNodeExisteDB(nodeDadosDeclarado[0], doc, doc.matricula_captura, doc.tipo_documento, this.dossieCliente);

          if (retorno && retorno.resultado) {
            let messagesSuccess = new Array<string>();
            messagesSuccess.push('Registro salvo com sucesso.');
            this.alertMessagesSucessChanged.emit(messagesSuccess);
            GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
            this.expandirAllTree = true;
          } else {
            if (retorno) {
              this.addMessageError('Erro ao salvar Dados Declarados.');
              throw retorno.mensagem;
            }
          }
          this.loadService.hide();
        });
      }, error => {
        this.loadService.hide();
        this.addMessageError('Erro ao salvar Dados Declarados.');
        throw error;
      });
    }, error => {
      this.loadService.hide();
      throw error;
    });

  }

  ngAfterViewInit(): void {
    GerenciadorDocumentosEmArvore.setListaVinculoArvore(this.listaVinculoArvore, this.dossieCliente);
  }

  private messagemValidacaoCampoObrigatorio() {
    if (undefined != this.msgValidacao && "" != this.msgValidacao) {
      this.addMessageError(this.msgValidacao);
    }
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

}