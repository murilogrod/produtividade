import { ArvoreGenericaAbastract } from "../../../../../../documento/arvore-generica/arvore-generica-abstract";
import { Component, Input, ViewEncapsulation, OnInit, Output, EventEmitter, OnChanges, AfterContentInit} from "@angular/core";
import { DossieService } from "src/app/dossie/dossie-service";
import { AlertMessageService, LoaderService, ApplicationService, EventService } from "src/app/services";
import { Checklist } from "src/app/model/checklist";
import { TIPO_ARVORE_DOCUMENTO, UNDESCOR, MESSAGE_ALERT_MENU, LOCAL_STORAGE_CONSTANTS, FECHA_MODAL } from "src/app/constants/constants";
import { Utils } from "../../../../../../utils/Utils";
import { DialogService } from "angularx-bootstrap-modal";
import { ConversorDocumentosUtil } from "src/app/documento/conversor-documentos/conversor-documentos.util.service";
import { PDFJS } from 'pdfjs-dist/build/pdf';
import { TipoDocumentoArvoreGenerica } from "src/app/model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { ElementoConteudo } from "src/app/model/elemento-conteudo";
import { ConteudoVisualizacaoDocumentoPresenter } from "../presenter/conteudo-visualizacao-documento.component.presenter";
import { UtilsTratamnto } from "src/app/tratamento/utils/utils-tratamento";
import { SituacaoDocumento } from "src/app/documento/enum-documento/situacao-documento.enum";
import { AbrirDocumentoPopupDefaultService } from "src/app/services/abrir-documento-popup-default.service";
import { TelaDeTratamentoService } from "src/app/tratamento/tela-de-tratamento.service";
import { IMAGEM_DEFAUT } from "src/app/constants/imgDefaut";

declare var $: any;

@Component({
    selector: 'conteudo-visualizacao-documentos',
    templateUrl: './conteudo-visualizacao-documentos.component.html',
    styleUrls: ['./conteudo-visualizacao-documentos.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ConteudoVisualizacaoDocumentosComponent extends AlertMessageService implements OnInit, OnChanges, AfterContentInit {
    @Input() elementosConteudoProcessoDossieOuFase: any[];
    @Input() documentosVinculoCliente: any;
    @Input() documentosProdutosVinculados: any;
    @Input() documentosGarantiasVinculadas: any;
    @Input() listaChekList: Checklist[];
    @Input() vinculo: any;
    @Input() tipoArvore: any;
    @Input() listaDocumentoObrigatorio: Checklist[];
    @Input() singleExpandAllDocuments: boolean;
    @Input() singleExpandAllDocumentsDossieFase: boolean;
    @Input() singleExpandAllDocumentsCliente: boolean;
    @Input() singleExpandAllDocumentsProduto: boolean;
    @Input() singleExpandAllDocumentsGarantias: boolean;
    @Input() idDoPrimeiroInstanciaDocumentoASerCarregado: number;
    @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
    @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
    documentoGarantiaVinculadaProduto: any;
    listaDocumentacao: any[];
    listItemNumber: number;
    listaCliente: any[];
    listaGarantia: any[];
    listaDossieFase: any[];
    listaProduto: any[];

    constructor(
        private appService: ApplicationService,
        private dossieService: DossieService,
        private loadService: LoaderService,
        private dialogService: DialogService,
        private eventService: EventService,
        private conversorDocsUtil: ConversorDocumentosUtil,
        private conteudoVisualizacaoDocumentoPresenter: ConteudoVisualizacaoDocumentoPresenter,
        private abrirDocumentoPopupDefaultService: AbrirDocumentoPopupDefaultService,
        private telaDeTratamentoService: TelaDeTratamentoService
    ) {
        super();
    }

    ngOnInit() {
        this.precorrerDossieOuFaseHabilitarTratamento();
        this.percorrerClienteHabilitandoTratamento();
        this.percorreOrdenaElementosConteudoProdutosVinculados();        
        this.percorrerhabilitarTratamentoGarantias();
        this.localizaGarantiaVinculadaProdutoAtual();
        this.listItemNumber = Math.floor(Math.random() * (999999 - 100000)) + 100000;
        this.montarListaCheckListdaArvore();
    }

    ngOnChanges() {
        this.conteudoVisualizacaoDocumentoPresenter.setSingleExpandAllDocuments(this.singleExpandAllDocuments);
    }

    ngAfterContentInit(){
        this.carregarChecklistDocumento();
    }

    //Verifica se há checklist de fase ou previo antes de carregar o documento para tratamento.
    //Caso não haja checklist, seleciona o primeiro documento disponivel para tratamento.
    private carregarChecklistDocumento(){
        if(this.listaChekList.filter(checklist => checklist.tipo === "CHECKLIST" || checklist.checklistPrevio).length === 0){
            let instanciaDocumento;
            if(this.elementosConteudoProcessoDossieOuFase && this.elementosConteudoProcessoDossieOuFase.length > 0){
             instanciaDocumento = this.vinculo.instancias_documento.find(instancia => 
                this.elementosConteudoProcessoDossieOuFase.find(elemento => instancia.id_elemento_conteudo == elemento.identificador_elemento)
             );   
           
            }else if(this.documentosProdutosVinculados && this.documentosProdutosVinculados.length > 0){
                let documentoProduto = this.documentosProdutosVinculados.find(docProdVinc => docProdVinc.id === this.vinculo.id);
                instanciaDocumento = this.vinculo.instancias_documento.find(instancia => instancia.id_elemento_conteudo === documentoProduto.identificador_elemento);   
           
            }else if(this.documentosVinculoCliente && this.documentosVinculoCliente.length > 0){
                let docVinc = this.documentosVinculoCliente.filter(docVinc => docVinc.tipo_relacionamento === this.vinculo.tipo_relacionamento.id)
                docVinc.forEach(doc => {
                    if(this.existeDocumentoChildrenCliente(doc) && !instanciaDocumento){
                        instanciaDocumento = this.vinculo.instancias_documento.find(instancia => (instancia.documento.tipo_documento && doc.tipo_documento && instancia.documento.tipo_documento.id === doc.tipo_documento.id) || 
                        (instancia.documento.tipo_documento && doc.funcao_documental && doc.funcao_documental.tipos_documento.find(tDoc => tDoc.id === instancia.documento.tipo_documento.id)));   
                    }
                });
            } else if(this.documentosGarantiasVinculadas && this.documentosGarantiasVinculadas.length > 0 && !this.vinculo.produto){
                this.documentosGarantiasVinculadas.forEach(garantia => {
                    if(garantia.id === this.vinculo.garantia && this.existeDocumentoChildrenGarantias(garantia)){
                        instanciaDocumento = this.vinculo.instancias_documento.find(instancia => 
                            this.documentosGarantiasVinculadas.find(elemento => instancia.id_elemento_conteudo === elemento.elementoConteudoProduto.identificador_elemento)
                        );   
                     }
                });
           
            }else if(this.documentoGarantiaVinculadaProduto && this.documentoGarantiaVinculadaProduto.length > 0){
                instanciaDocumento = this.vinculo.instancias_documento.find(instancia => 
                    this.documentoGarantiaVinculadaProduto.fin(elemento => instancia.id_elemento_conteudo === elemento.elementoConteudoProduto.identificador_elemento)
                );   
            }

            let idVinculo;
            if(this.tipoArvore == "CLIENTE"){
                idVinculo = this.vinculo.dossie_cliente.id;
            }else {
                idVinculo = this.vinculo.id;
            }
            
            if(instanciaDocumento && (this.idDoPrimeiroInstanciaDocumentoASerCarregado == instanciaDocumento.id)){
                this.enviaDocsVisualizacao(instanciaDocumento, idVinculo, undefined, this.tipoArvore);
            }
        }
    }

    private percorrerClienteHabilitandoTratamento() {
        this.documentosVinculoCliente = ArvoreGenericaAbastract.ordenaEstruturaDocumentosPessoaPartindoDoTipoDocumento(this.documentosVinculoCliente);
        if (this.documentosVinculoCliente && this.documentosVinculoCliente.length > 0) {
            this.documentosVinculoCliente.forEach(cliente => {
                cliente.habilitaVerificacao = true;
            });
        }
    }

    private precorrerDossieOuFaseHabilitarTratamento() {
        this.elementosConteudoProcessoDossieOuFase = ArvoreGenericaAbastract.ordenaElementosConteudoPartindoNoPai(this.elementosConteudoProcessoDossieOuFase);
        if (this.elementosConteudoProcessoDossieOuFase && this.elementosConteudoProcessoDossieOuFase.length > 0) {
            this.elementosConteudoProcessoDossieOuFase.forEach(dossieOuFase => {
                dossieOuFase.habilitaVerificacao = true;
            });
        }
    }

    private percorrerhabilitarTratamentoGarantias() {
        this.documentosGarantiasVinculadas = ArvoreGenericaAbastract.ordenaEstruturaDocumentosGarantiaPartindoDoTipoDocumento(this.documentosGarantiasVinculadas);
        if (this.documentosGarantiasVinculadas && this.documentosGarantiasVinculadas.length > 0) {
            this.documentosGarantiasVinculadas.forEach(garantia => {
                garantia.habilitaVerificacao = true;
            });
        }
    }

    private localizaGarantiaVinculadaProdutoAtual() {
        if (this.documentosProdutosVinculados) {
            for (let produtoVinculado of this.documentosProdutosVinculados) {
                this.documentoGarantiaVinculadaProduto = produtoVinculado.garantias_vinculadas.find(garantia => this.vinculo.produto && garantia.id == this.vinculo.garantia);
                if (this.documentoGarantiaVinculadaProduto) {
                    break;
                }
            }
        }
    }

    /**
     * chamada para montar por tipo de check a lista a ser fulturamente maninpulada por conter ou nao informações
     */
    private montarListaCheckListdaArvore() {
        this.listaChekList = this.listaChekList ? this.listaChekList : [];
        let obj;
        obj = this.checkCliente(obj);
        obj = this.checkDossieOuFase(obj);
        obj = this.checkProdutos(obj);
        obj = this.checkGarantias(obj);
        this.listaCheklistChanged.emit(Object.assign([], this.listaChekList));
    }

    private checkCliente(obj: any) {
        if (this.tipoArvore == TIPO_ARVORE_DOCUMENTO.CLIENTE) {
            this.vinculo.instancias_documento.forEach(instanciaDocumento => {
                if (!this.listaChekList.some(ck => ck.idInstancia && ck.idInstancia === instanciaDocumento.id && ck.idVinculo == this.vinculo.dossie_cliente.id)) {
                    obj = new Checklist();
                    obj.situacaoConforme = (SituacaoDocumento.CONFORME === instanciaDocumento.situacao_atual);
                    obj.idInstancia = instanciaDocumento.id;
                    obj.tipo = this.tipoArvore;
                    obj.idVinculo = this.vinculo.dossie_cliente.id;
                    obj.nomeVinculo = this.vinculo.tipo_relacionamento;
                    obj.idDocumento = instanciaDocumento.documento.id;
                    obj.obrigatorio = this.verificandoTipoDocumentoObrigatorio(instanciaDocumento);
                    let ifFuncaoDoc, idFuncaoDoc;
                    ({ ifFuncaoDoc, idFuncaoDoc } = this.verificandoSeFuncaoDocumentalClienteObrigatorio(obj.obrigatorio, instanciaDocumento, ifFuncaoDoc, idFuncaoDoc));
                    obj.isfuncaoDocumental = ifFuncaoDoc;
                    obj.idFuncaoDocumental = obj.isfuncaoDocumental ? idFuncaoDoc : undefined;
                    obj.idTipoDocumento = instanciaDocumento.documento.tipo_documento.id;
                    obj.descricaoTipoDocumento = instanciaDocumento.documento.tipo_documento.nome;
                    obj.habilitaVerificacao = !obj.situacaoConforme;
                    instanciaDocumento.situacaoConforme = obj.situacaoConforme;
                    instanciaDocumento.habilitaVerificacao = !obj.situacaoConforme;
                    this.listaChekList.push(obj);
                }
            });
        }
        return obj;
    }

    private verificandoSeFuncaoDocumentalClienteObrigatorio(obrigatorioDoc: any, instanciaDocumento: any, ifFuncaoDoc: any, idFuncaoDoc: any) {
        ifFuncaoDoc = false;
        if (!obrigatorioDoc) {
            for (let doc of this.documentosVinculoCliente) {
                if (doc.funcao_documental && !ifFuncaoDoc) {
                    for (let tipoDoc of doc.funcao_documental.tipos_documento) {
                        if (tipoDoc.id == instanciaDocumento.documento.tipo_documento.id) {
                            ifFuncaoDoc = true;
                            idFuncaoDoc = doc.funcao_documental.id;
                            break;
                        }
                    }
                }
                if (ifFuncaoDoc) {
                    break;
                }
            }
        }
        return { ifFuncaoDoc, idFuncaoDoc };
    }

    private verificandoTipoDocumentoObrigatorio(instanciaDocumento: any) {
        let obrigatorioDoc: any = false;
        for (let doc of this.documentosVinculoCliente) {
            if (doc.tipo_documento && doc.tipo_documento.id == instanciaDocumento.documento.tipo_documento.id) {
                obrigatorioDoc = doc.obrigatorio;
                break;
            }
        }
        return obrigatorioDoc;
    }

    verificarTipoDOSSIE_FASE(id) {
        return JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + id)) ? TIPO_ARVORE_DOCUMENTO.DOSSIE : TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE;
    }

    private checkDossieOuFase(obj: any) {
        if (this.tipoArvore === (TIPO_ARVORE_DOCUMENTO.DOSSIE + UNDESCOR + TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE)) {
            if (this.vinculo.instancias_documento) {
                this.vinculo.instancias_documento.forEach(instanciaDocumento => {
                    if (!this.listaChekList.find(checkExists => checkExists.idDocumento == instanciaDocumento.documento.id && checkExists.idInstancia == instanciaDocumento.id)
                        && !this.listaChekList.some(ck => ck.idInstancia === instanciaDocumento.id && ck.idVinculo === instanciaDocumento.idVinculo)) {
                        obj = new Checklist();
                        obj.situacaoConforme = (SituacaoDocumento.CONFORME === instanciaDocumento.situacao_atual);
                        obj.idInstancia = instanciaDocumento.id;
                        obj.tipo = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.vinculo.id)) ? TIPO_ARVORE_DOCUMENTO.DOSSIE : TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE;
                        obj.idVinculo = this.vinculo.id;
                        obj.nomeVinculo = this.vinculo.nome;
                        obj.idDocumento = instanciaDocumento.documento.id;
                        obj.obrigatorio = this.elementosConteudoProcessoDossieOuFase.find(x => x.tipo_documento && x.tipo_documento.id == instanciaDocumento.documento.tipo_documento.id).obrigatorio;
                        obj.idTipoDocumento = instanciaDocumento.documento.tipo_documento.id;
                        obj.descricaoTipoDocumento = instanciaDocumento.documento.tipo_documento.nome;
                        obj.habilitaVerificacao = !obj.situacaoConforme;
                        instanciaDocumento.situacaoConforme = obj.situacaoConforme;
                        instanciaDocumento.habilitaVerificacao = !obj.situacaoConforme;
                        this.listaChekList.push(obj);
                    }
                });
            }
        }
        return obj;
    }

    private checkProdutos(obj: any) {
        if (this.tipoArvore === TIPO_ARVORE_DOCUMENTO.PRODUTO) {
            this.vinculo.instancias_documento.forEach(instanciaDocumento => {
                if (!this.listaChekList.some(ck => ck.idInstancia === instanciaDocumento.id && ck.idVinculo === instanciaDocumento.idVinculo)) {
                    obj = new Checklist();
                    obj.situacaoConforme = (SituacaoDocumento.CONFORME === instanciaDocumento.situacao_atual);
                    obj.idInstancia = instanciaDocumento.id;
                    obj.tipo = TIPO_ARVORE_DOCUMENTO.PRODUTO;
                    obj.idVinculo = this.vinculo.id;
                    obj.nomeVinculo = this.vinculo.nome;
                    obj.idDocumento = instanciaDocumento.documento.id;
                    obj.idElementoConteudo = instanciaDocumento.id_elemento_conteudo;
                    obj.habilitaVerificacao = !obj.situacaoConforme;
                    instanciaDocumento.situacaoConforme = obj.situacaoConforme;
                    instanciaDocumento.habilitaVerificacao = !obj.situacaoConforme;
                    for (let prod of this.documentosProdutosVinculados) {
                        for (let elem of prod.elementos_conteudo) {
                            if (elem.identificador_elemento == instanciaDocumento.id_elemento_conteudo && elem.tipo_documento) {
                                if (elem.tipo_documento.id == instanciaDocumento.documento.tipo_documento.id) {
                                    obj.obrigatorio = elem.obrigatorio;
                                    break;
                                }
                            }
                        }
                    }
                    obj.idTipoDocumento = instanciaDocumento.documento.tipo_documento.id;
                    obj.descricaoTipoDocumento = instanciaDocumento.documento.tipo_documento.nome;
                    this.listaChekList.push(obj);
                }
            });
        }
        return obj;
    }

    private checkGarantias(obj: any) {
        if (this.tipoArvore === TIPO_ARVORE_DOCUMENTO.GARANTIAS) {
            this.vinculo.instancias_documento.forEach(instanciaDocumento => {
                if (!this.listaChekList.some(ck => ck.idInstancia === instanciaDocumento.id && ck.idVinculo === instanciaDocumento.idVinculo)) {
                    obj = new Checklist();
                    obj.situacaoConforme = (SituacaoDocumento.CONFORME === instanciaDocumento.situacao_atual);
                    obj.idInstancia = instanciaDocumento.id;
                    obj.tipo = TIPO_ARVORE_DOCUMENTO.GARANTIAS;
                    obj.idVinculo = this.vinculo.id;
                    obj.nomeVinculo = this.vinculo.nome_garantia;
                    obj.idDocumento = instanciaDocumento.documento.id;
                    obj.idGarantiaInformada = instanciaDocumento.id_garantia_informada;
                    obj.habilitaVerificacao = !obj.situacaoConforme;
                    instanciaDocumento.situacaoConforme = obj.situacaoConforme;
                    instanciaDocumento.habilitaVerificacao = !obj.situacaoConforme;
                    let encontrou = false;
                    encontrou = this.verificarGarantiaVinculadaDossie(encontrou, instanciaDocumento, obj);
                    if (!encontrou) {
                        this.verificarGarantiaVinculadaAoProduto(encontrou, instanciaDocumento, obj);
                    }
                    this.listaChekList.push(obj);
                }
            });
        }
        return obj;
    }

    verificarGarantiaVinculadaDossie(encontrou: boolean, instanciaDocumento: any, obj: any) {
        if (this.documentosGarantiasVinculadas && this.documentosGarantiasVinculadas.length > 0) {
            for (let docgarantia of this.documentosGarantiasVinculadas) {
                if (this.vinculo.codigo_bacen == docgarantia.codigo_bacen) {
                    encontrou = this.quandoForTipoDocumento(docgarantia, instanciaDocumento, obj, encontrou);
                    if (encontrou) {
                        break;
                    } else {
                        if (!obj.obrigatorio) {
                            if (docgarantia.funcao_documental) {
                                for (let tipoDoc of docgarantia.funcao_documental.tipos_documentos) {
                                    encontrou = this.quandoForFuncaoDocumental(tipoDoc, instanciaDocumento, obj, docgarantia, encontrou);
                                    if (encontrou) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (encontrou) {
                    break;
                }
            }
        }
        return encontrou;
    }

    verificarGarantiaVinculadaAoProduto(encontrou: boolean, instanciaDocumento: any, obj: any) {
        this.documentosProdutosVinculados.forEach(produto => {
            if (produto.garantias_vinculadas && produto.garantias_vinculadas.length > 0) {
                for (let garantiaVinculadaProduto of produto.garantias_vinculadas) {
                    if (this.vinculo.codigo_bacen == garantiaVinculadaProduto.codigo_bacen) {
                        encontrou = this.quandoForTipoDocumento(garantiaVinculadaProduto, instanciaDocumento, obj, encontrou);
                        if (encontrou) {
                            break;
                        } else {
                            if (!obj.obrigatorio) {
                                if (garantiaVinculadaProduto.funcoes_documentais) {
                                    for (let funcao of garantiaVinculadaProduto.funcoes_documentais) {
                                        for (let tipoDoc of funcao.tipos_documento) {
                                            encontrou = this.quandoForFuncaoDocumental(tipoDoc, instanciaDocumento, obj, garantiaVinculadaProduto, encontrou);
                                            if (encontrou) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (encontrou) {
                        break;
                    }
                }
            }
        });
    }

    private quandoForFuncaoDocumental(tipoDoc: any, instanciaDocumento: any, obj: any, docgarantia: any, encontrou: boolean) {
        if (tipoDoc.id == instanciaDocumento.documento.tipo_documento.id) {
            obj.isfuncaoDocumental = true;
            this.encontraIdFuncaoDocumental(docgarantia, tipoDoc, obj);
            obj.obrigatorio = true;
            obj.idTipoDocumento = tipoDoc.id;
            obj.descricaoTipoDocumento = tipoDoc.nome;
            encontrou = true;
        }
        return encontrou;
    }

    private encontraIdFuncaoDocumental(docgarantia: any, tipoDoc: any, obj: any) {
        for (let funcao of docgarantia.funcoes_documentais) {
            for (let tipo of funcao.tipos_documento) {
                if (tipoDoc.id == tipo.id) {
                    obj.idFuncaoDocumental = funcao.id;
                    return
                }
            }
        }
    }

    private quandoForTipoDocumento(docgarantia: any, instanciaDocumento: any, obj: any, encontrou: boolean) {
        for (let documento of docgarantia.tipos_documento) {
            if (documento.id == instanciaDocumento.documento.tipo_documento.id) {
                obj.isfuncaoDocumental = false;
                obj.obrigatorio = true;
                obj.idTipoDocumento = instanciaDocumento.documento.tipo_documento.id;
                obj.descricaoTipoDocumento = instanciaDocumento.documento.tipo_documento.nome;
                encontrou = true;
            }
        }
        return encontrou;
    }

    private percorreOrdenaElementosConteudoProdutosVinculados() {
        if (undefined != this.documentosProdutosVinculados) {
            let arrayProdutosVinculadosAux: any[] = this.documentosProdutosVinculados;
            arrayProdutosVinculadosAux.forEach((produtoVinculado, index) => {
                let elementosConteudosOrdenados: any[] = ArvoreGenericaAbastract.ordenaElementosConteudoPartindoNoPai(produtoVinculado.elementos_conteudo);
                this.documentosProdutosVinculados[index].elementos_conteudo = elementosConteudosOrdenados;
                this.documentosProdutosVinculados[index].habilitaVerificacao = true;
            });
        }
    }

    existeIncomformidadeDoc(idDocumento: string, idVinculo: string) {
        let checkListDoc: Checklist = this.listaChekList.find(checkListDoc => checkListDoc.idDocumento == idDocumento && checkListDoc.idVinculo == idVinculo);
        return UtilsTratamnto.verificaStatusDocumento(checkListDoc);
    }

    enviaDocsVisualizacao(param: any, idVinculo: any, idFuncaoDocumental: any, tipo: string) {
        tipo = tipo != (TIPO_ARVORE_DOCUMENTO.DOSSIE + UNDESCOR + TIPO_ARVORE_DOCUMENTO.FASE_DOSSIE) ? tipo : this.verificarTipoDOSSIE_FASE(idVinculo);
        this.loadService.show();

        this.eventService.broadcast(FECHA_MODAL.SHOW_PORTAL);
        
        this.carregaDocs(param, idVinculo, idFuncaoDocumental, tipo);
    }

    verificarSeAbriDocumentoEmPopup(){
        let isDocumentoEmNovaJanela = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.ABRIR_DOCUMENTO_EM_NOVA_JANELA);

        if(isDocumentoEmNovaJanela != null && isDocumentoEmNovaJanela != undefined && isDocumentoEmNovaJanela == 'true'){
            this.abrirDocumentoPopupDefaultService.setExisteCheckList(false);
            this.abrirDocumentoPopupDefaultService.setShowPopup(true);
        }else{
            this.abrirDocumentoPopupDefaultService.setShowPopup(false);
        }
    }

    verificarDocumento(documentoATratar: any, obj: any) {
        for (let doc of this.listaChekList) {
            this.loadService.show();
            if (obj === TIPO_ARVORE_DOCUMENTO.PRODUTO && doc.tipo != TIPO_ARVORE_DOCUMENTO.CHECKLIST) {
                if (documentoATratar.identificador_elemento === doc.idElementoConteudo && documentoATratar.tipo_documento.id === doc.idTipoDocumento) {
                    this.verificarAcaoNescessariaCasoDocumentoJatenhaSidoTratado(doc, doc);
                    break;
                }
            }
            if (obj != TIPO_ARVORE_DOCUMENTO.PRODUTO && doc.tipo != TIPO_ARVORE_DOCUMENTO.CHECKLIST) {
                if (documentoATratar.documento.id === doc.idDocumento) {
                    this.verificarAcaoNescessariaCasoDocumentoJatenhaSidoTratado(doc, obj);
                    break;
                }
            }
        }
        this.loadService.hide();
    }

    private verificarAcaoNescessariaCasoDocumentoJatenhaSidoTratado(doc: Checklist, obj: any) {
        if (doc.listaResposta && doc.listaResposta.length > 0) {
            Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA).subscribe(res => {
                if (res) {
                    doc.existeIncomformidade = false;
                    doc.listaResposta = undefined;
                    doc.habilitaVerificacao = !doc.habilitaVerificacao;
                }
            },
                () => {
                    this.loadService.hide();
                });
        }
        else {
            doc.habilitaVerificacao = !doc.habilitaVerificacao;
        }
    }

    habilitarDestabilitarTratamentoChecked(id: any) {
        return this.listaChekList.find(c => c.idDocumento == id).habilitaVerificacao;
    }

    carregaDocs(param: any, idVinculo: any, idFuncaoDocumental: any, tipo: string) {
        let documentos: any[] = [];
        this.loadService.show();
        this.telaDeTratamentoService.setExibindoChecklistFase(false);

        this.dossieService.getConsultarImagemGet(param.documento.id).subscribe(response => {
            
            response.idVinculo = idVinculo;
            response.idFuncaoDocumental = idFuncaoDocumental;
            response.tipo = tipo;

            if (response.mime_type == "PDF") {
                this.convertePdfListaImagem(response);
            } else {
                response.conteudos = [];
                response.conteudos.push(response.binario ? response.binario : IMAGEM_DEFAUT.img);
                documentos.push(response);
                this.listDocumentosImagemChanged.emit(Object.assign({}, documentos));
                this.verificarSeAbriDocumentoEmPopup();
            }
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            console.log(error);
            throw error;
        });
    }

    /**
     * Converte um PDF multipaginado em uma lista de imagens para visualização
     * @param arrayImages 
     */
    convertePdfListaImagem(response: any) {
        let binario: any = response.binario ? response.binario : IMAGEM_DEFAUT.img;
        let messagesError = new Array<string>();
        let promise = Promise.resolve();
        let canvas: any = document.getElementById('pdfcanvas');
        let arrayImages = [];

        if(binario && canvas){
            let pdfAsArray = this.conversorDocsUtil.convertDataURIToBinary(`data:application/pdf;base64,${binario}`);
            PDFJS.getDocument(pdfAsArray).then(pdfSuccess => {
                for (let i = 1; i <= pdfSuccess.numPages; i++) {
                    this.loadService.showProgress(pdfSuccess.numPages, arrayImages.length);
                    pdfSuccess.getPage(i).then(pdfPageSuccess => {
                        promise = promise.then(function () {
                            let scale: number = 1;
                            let viewport = pdfPageSuccess.getViewport(scale);
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
                                let documentos = new Array<any>();
                                documentos.push(response);
                                this.listDocumentosImagemChanged.emit(Object.assign({}, documentos));
                                this.verificarSeAbriDocumentoEmPopup();
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
        }else{
            response.conteudos = arrayImages;
            let documentos = new Array<any>();
            documentos.push(response);
            this.listDocumentosImagemChanged.emit(Object.assign({}, documentos));
            this.verificarSeAbriDocumentoEmPopup();
        }
    }

    /**
     * Habilita o conteudo vazio da pasta de ducumentos de acordo selecao usuario.
     * @param tipoDocumento 
     */
    verificaExistenciaInstanciaDocumentoCliente(tipoDocumento: TipoDocumentoArvoreGenerica, mostrar: boolean): boolean {
        let exibir = mostrar ? true : this.singleExpandAllDocumentsCliente;
        return this.conteudoVisualizacaoDocumentoPresenter.verificaExistenciaInstanciaDocumentoCliente(this.vinculo, tipoDocumento, exibir);
    }

    /**
     * Habilita o conteudo vazio da pasta de ducumentos de acordo selecao usuario.
     * @param tipoDocumento 
     */
    verificaExistenciaInstanciaDocumentoGarantia(tipoDocumento: TipoDocumentoArvoreGenerica): boolean {
        return this.conteudoVisualizacaoDocumentoPresenter.verificaExistenciaInstanciaDocumentoGarantia(this.vinculo, tipoDocumento, this.singleExpandAllDocumentsGarantias);
    }

    /**
     * Habilita o conteudo vazio da pasta de ducumentos de acordo selecao usuario.
     * @param elementoConteudo 
     */
    verificaExistenciaElementoConteudoProcesso(elementoConteudo: ElementoConteudo): boolean {
        return this.conteudoVisualizacaoDocumentoPresenter.verificaExistenciaElementoConteudoProcesso(this.vinculo, elementoConteudo, this.singleExpandAllDocumentsDossieFase);
    }

    /**
     * Habilita o conteudo vazio da pasta de ducumentos de acordo selecao usuario.
     * @param elementoConteudo 
     */
    verificaExistenciaElementoConteudoProduto(elementoConteudo: ElementoConteudo): boolean {
        return this.conteudoVisualizacaoDocumentoPresenter.verificaExistenciaElementoConteudoProduto(this.vinculo, elementoConteudo, this.singleExpandAllDocumentsProduto);
    }

    existeDocumentoChildrenCliente(documentoVinculoCliente: any) {
        let resposta: boolean = false;
        let lista = this.conteudoVisualizacaoDocumentoPresenter.existeDocumentoChildrenCliente(documentoVinculoCliente, this.vinculo, this.singleExpandAllDocumentsCliente);
        this.listaCliente = [];
        lista.forEach(obj => {
            if (!this.listaCliente.some(function (el) { return (el.id === obj.id && el.tipo === obj.tipo) })) {
                this.listaCliente.push(obj);
            }
        });
        if (this.singleExpandAllDocumentsCliente) {
            resposta = this.singleExpandAllDocumentsCliente ? this.singleExpandAllDocumentsCliente : this.listaCliente.length > 0;
        } else {
            resposta = this.singleExpandAllDocuments ? this.singleExpandAllDocuments : this.listaCliente.length > 0;
        }
        return resposta;
    }

    apresentarDocumentoVazioCliente(tipoDocumento: any, tipo: string) {
        return this.singleExpandAllDocumentsCliente ? this.singleExpandAllDocumentsCliente : this.listaCliente.some(function (el) { return (el.id === tipoDocumento.id && el.tipo === tipo) });
    }

    apresentarDocumentoVazioClienteFuncaoDocumental(documentoVinculoCliente: any, tipo: string) {
        let docVazioFuncaoDocumental: boolean = false;
        for (let tipoDocumento of documentoVinculoCliente.funcao_documental.tipos_documento) {
            docVazioFuncaoDocumental = this.listaCliente.some(function (el) { return (el.id === tipoDocumento.id && el.tipo === tipo) });
            if (docVazioFuncaoDocumental) {
                break;
            }
        }
        return this.singleExpandAllDocumentsCliente ? this.singleExpandAllDocumentsCliente : docVazioFuncaoDocumental;
    }

    apresentarDocumentoVazioGarantia(tipoDocumento: any, tipo: string) {
        return this.singleExpandAllDocumentsGarantias ? this.singleExpandAllDocumentsGarantias : this.listaGarantia.some(function (el) { return (el.id === tipoDocumento.id && el.tipo === tipo) });
    }


    existeDocumentoChildrenGarantias(documentoGarantiaVinculada: any) {
        let lista = this.conteudoVisualizacaoDocumentoPresenter.existeDocumentoChildrenGarantias(documentoGarantiaVinculada, this.vinculo, this.singleExpandAllDocumentsGarantias);
        this.listaGarantia ? this.listaGarantia : this.listaGarantia = [];
        lista.forEach(obj => {
            if (!this.listaGarantia.some(function (el) { return (el.id === obj.id && el.tipo === obj.tipo) })) {
                this.listaGarantia.push(obj)
            }
        });
        let resposta;
        if (this.singleExpandAllDocumentsGarantias) {
            resposta = this.singleExpandAllDocumentsGarantias ? this.singleExpandAllDocumentsGarantias : this.listaGarantia.length > 0;
        } else {
            resposta = this.singleExpandAllDocuments ? this.singleExpandAllDocuments : this.listaGarantia.length > 0;
        }
        return resposta;
    }

    existeDocumentoChildrenDossieFase(documentoVinculoDossieFase: any) {
        let lista = this.conteudoVisualizacaoDocumentoPresenter.existeDocumentoChildrenDossieFase(documentoVinculoDossieFase, this.vinculo, this.singleExpandAllDocumentsDossieFase);
        this.listaDossieFase = [];
        lista.forEach(obj => {
            if (!this.listaDossieFase.some(function (el) { return (el.id === obj.id && el.tipo === obj.tipo) })) {
                this.listaDossieFase.push(obj)
            }
        });
        let resposta;
        if (this.singleExpandAllDocumentsDossieFase) {
            resposta = this.singleExpandAllDocumentsDossieFase ? this.singleExpandAllDocumentsDossieFase : this.listaDossieFase.length > 0;
        } else {
            resposta = this.singleExpandAllDocuments ? this.singleExpandAllDocuments : this.listaDossieFase.length > 0;
        }
        return resposta;
    }

    apresentarDocumentoVazioDossieFase(elementoConteudoProcesso: any, tipo: string) {
        if (elementoConteudoProcesso && elementoConteudoProcesso.tipo_documento && this.listaDossieFase) {
            const toogle = (this.singleExpandAllDocuments && this.singleExpandAllDocumentsDossieFase) || this.singleExpandAllDocumentsDossieFase;
            return toogle ? toogle : this.listaDossieFase.some(function (el) { return (el.id === elementoConteudoProcesso.tipo_documento.id && el.tipo === tipo && el.vinculador == elementoConteudoProcesso.identificador_elemento_vinculador) });
        }
        return true;
    }

    existeDocumentoChildrenProduto(elementoConteudoProduto: any) {
        let lista = this.conteudoVisualizacaoDocumentoPresenter.existeDocumentoChildrenProduto(elementoConteudoProduto, this.vinculo, this.singleExpandAllDocumentsProduto);
        this.listaProduto = [];
        lista.forEach(obj => {
            if (!this.listaProduto.some(function (el) { return (el.id === obj.id && el.tipo === obj.tipo) })) {
                this.listaProduto.push(obj);
            }
        });
        let resposta = true;
        if (this.singleExpandAllDocumentsProduto) {
            resposta = this.singleExpandAllDocumentsProduto ? this.singleExpandAllDocumentsProduto : this.listaProduto.length > 0;
        } else {
            resposta = this.singleExpandAllDocuments ? this.singleExpandAllDocuments : this.listaProduto.length > 0;
        }
        return resposta;
    }

    apresentarDocumentoVazioProduto(elemento: any, tipo: string) {
        if (elemento.tipo_documento && this.listaProduto) {
            const toogle = (this.singleExpandAllDocuments && this.singleExpandAllDocumentsProduto) || this.singleExpandAllDocumentsProduto;
            return toogle ? toogle : this.listaProduto.some(function (el) { return (el.id === elemento.tipo_documento.id && el.tipo === tipo && el.vinculador == elemento.identificador_elemento_vinculador) });
        }
        return true;
    }
}
