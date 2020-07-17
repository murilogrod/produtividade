import { LoaderService } from './../services/http-interceptor/loader/loader.service';
import { ImagemService } from './../compartilhados/services/imagem.service';
import { DocumentoPost } from './../compartilhados/componentes/formulario-extracao/model/documento-post';

import { Component, OnInit, ViewEncapsulation, ViewChild,  EventEmitter} from "@angular/core";

import { ExtracaoManualService } from "./extracao-manual.service";


import { FormGroup } from '@angular/forms';
import { QuestionBase } from "./../compartilhados/componentes/formulario-extracao/model/question-base";

import { Utils } from './../compartilhados/utilidades/utils';
import { AlertMessageService, ApplicationService } from '../services';


declare var $: any;

@Component({
  selector: 'app-extracao-manual',
  templateUrl: './extracao-manual.component.html',
  styleUrls: ['./extracao-manual.component.css'],
  encapsulation: ViewEncapsulation.None
  
  
})

export class ExtracaoManualComponent extends AlertMessageService implements OnInit {
  @ViewChild('f') frm : any
  
  
  questions: QuestionBase<any>[] = [];
  
  payLoad = '';
  public display = 'none'
  public visualizar = false;
  public atributos;
  private tiposDocumento = [];
  private tiposDocCombo = [];
  public tipoDocSelecionado;
  private totais = [];
  public exibeSalvar = true;
  public habilitaComboTipoDoc = false;
  public resumo_situacao = []
  public formData: FormGroup;
  private documentoPost: DocumentoPost;
  public docPara = 'documento'
  public exibirImg = true;
  public larguraTabela = 'col-md-6 row'
  public diminuiu = true;
  public ehPdf = false;
  public escalaImg = 0.4
  public rotacaoImg = 0
  private imagemCorrente;
  public exibeRejeitar = false;
  public rejeicaoSelecionada = '';
  public mensagem = '';
  public tipoMensagem = 'info';
  public exibirMensagem = false;
  private voltarParaFila = true;
  private utils: Utils = new Utils();
  public mensValida = ''
  public hoje = new Date().toISOString().substr(0, 10);
  public tipoIcone = 'info'
  public erroForm = true;
  public statusForm = false;
  public resultadoExtracao: any
  public codigo_controle = ''
  public exibeTodos = false;
  public binario = ''
  public mimetype = ''
  public alturaVisualizador = '515px'
  public comprimentoVisualizador = '800px'

  salvarForm: EventEmitter<any> = new EventEmitter<any>();
  rejeitarForm: EventEmitter<any> = new EventEmitter<any>();
  cancelarForm: EventEmitter<any> = new EventEmitter<any>(false);
  
  tiposRejeicao: any = [
    {'label': 'Documento invalido', 'value': 'DOC001'},
    {'label': 'Documento Incompleto ', 'value': 'DOC002'},
    {'label': 'Documento com informação obstruída', 'value': 'DOC003'},
  ]

  rejeicoes = []

  constructor(private service: ExtracaoManualService, private srvImagem: ImagemService,
        private loadService: LoaderService, private appService: ApplicationService
    ) {
      super();

  }

  ngOnInit(): void {
    this.obterPendentes();
    this.obterTipologia();
    this.montaRejeicoes();
    this.rejeicaoSelecionada = this.tiposRejeicao[0].value
  }

  montaRejeicoes() {
    for (let i = 0; i < this.tiposRejeicao.length; i++) {
      this.rejeicoes[this.tiposRejeicao[i].value] = this.tiposRejeicao[i].label
    }
  }

  obterPendentes() {
      this.limpaMensagem();
      this.loadService.show()
      this.service.obterPendentes().subscribe( resp => {
      if (resp == null || resp == undefined) {
        this.exibeMensagemFaltaDados()
        
        return
      }
      this.resumo_situacao = resp
      
      for (let i = 0; i < resp.length; i++) {
        this.totais[resp[i].id_tipo_documento] = resp[i].quantidade
        this.resumo_situacao[i].nome_tipo_documento = this.resumo_situacao[i].nome_tipo_documento.substring(0,35);
      }
      this.loadService.hide()
    }, error => {
        this.loadService.hide()
        if (error.status == 404) {
          this.exibeMensagemFaltaDados()
        } else {
            this.exibeErro(error)
        }
        throw error;
    });
  }

  exibeErro(error) {
    this.mensagem = 'Ocorreu uma situação inesperada! Código de retorno: ' + error.error.codigo_http + ' - ' + 
        error.error.mensagem + ' - ' + error.error.detalhe
    this.tipoMensagem = 'error'
    this.tipoIcone = 'ban'
    this.exibirMensagem = true
    this.addMessageError(this.mensagem)
    this.loadService.hide()
    throw error

  }

  exibeMensagemFaltaDados(){
    this.resumo_situacao = []
    this.mensagem = 'Não existem registros na Fila para tratamento.'
    this.tipoMensagem = 'info'
    this.tipoIcone = 'info'
    this.exibirMensagem = true
    this.loadService.hide()
  }

  obterTipologia() {
    let tipologia: any = JSON.parse(this.appService.getItemFromLocalStorage('tipologia'))
    
    if (tipologia.tipos_documento.length > 0) {
      let conta = 0
      for (let i = 0; i < tipologia.tipos_documento.length; i++) {
        
          this.tiposDocumento[tipologia.tipos_documento[i].id] = tipologia.tipos_documento[i]
          this.tiposDocCombo[conta] = {'label': tipologia.tipos_documento[i].nome, 'value': tipologia.tipos_documento[i].id }
          conta++;
        
      }
      
      this.classifica()      
    }
  }

  pegaCor(situacao) {
    
    if (this.tiposDocumento[situacao] != undefined) {
      return this.tiposDocumento[situacao].cor_fundo
    }
  }
  
 
  pegaIcone(situacao) {
    if (this.tiposDocumento[situacao] != undefined) {
      return this.tiposDocumento[situacao].avatar
    }
  }

  pegaQtde(situacao) {
    if (this.totais[situacao]) {
      return this.totais[situacao]
    } else
        return 0
  }

  getDocFila(tipo) {
    this.limpaMensagem()
    this.loadService.show() 
    
    if (this.tiposDocumento[tipo] != undefined) {
      this.tipoDocSelecionado = this.tiposDocumento[tipo].id
    } else {
        let x = {
          error: {
            codigo_http: 500,
            mensagem: 'Tipo de Documento com id = ' + tipo + ' não existe na Tipologia',
            detalhe: 'Contate a equipe desenvolvimento SIMTR'
          }
        }
        this.loadService.hide()
        this.exibeErro(x)
        return
    }
    
   
    this.service.obterDocumentoFila(tipo).subscribe(resp => {
      this.loadService.hide()
      
      if (resp) {
        this.documentoPost = resp
        this.codigo_controle = resp.codigo_controle
        this.exibeRejeitar = true
        if (!resp.executa_classificacao) {
          this.habilitaComboTipoDoc = !resp.executa_classificacao
        }
        this.visualizar = true
        this.display = 'block'
        this.mimetype = this.documentoPost.mimetype
        this.binario = this.documentoPost.binario
      }
    }, error => {
        
        if (error.status == 404) {
            this.cancelar();
        } else {
            this.exibeErro(error);
        }
        throw error;
    });

    
  }

  alterouStatusForm(event) {
    this.statusForm = event.status_extracao
    
    if (this.statusForm) {
        this.resultadoExtracao = event.resultado_extracao
    }
  }

  ocorreuErroForm(event){
    this.addMessageError(event)
    this.exibeErro(event)
  }

  voltar() {
    this.cancelarForm.emit()
  }

  cancelouForm(event){
    this.limpaMensagem();
    this.obterPendentes();
    this.visualizar = false;
    this.display = 'none'
  }

  cancelar() {
    
    this.limpaMensagem();
    this.obterPendentes();
    this.visualizar = false;
    this.display = 'none'
  
  }
  salvarEProximo () {
    this.voltarParaFila = false
    this.salvar()
  }

  salvarSair() {
    this.voltarParaFila = true
    this.salvar();
  
  }
  
  voltaMenu() {
    this.voltarParaFila = true
  
  }

  proximoDaFila() {
    this.voltarParaFila = false
  
  }

 
  salvar() {
   
    this.salvarForm.emit()
    if (this.voltarParaFila) {
      this.cancelar()
    } else {
        this.getDocFila(this.documentoPost.tipo_documento.id)
    }

  }

  rejeitar() {
    
    let resultado: any = {};
    this.limpaMensagem();
    resultado.codigo_rejeicao = this.rejeicaoSelecionada

    this.rejeitarForm.emit(resultado)
    if (this.voltarParaFila) {
      this.cancelar()
    } else {
        this.getDocFila(this.documentoPost.tipo_documento.id)
    }
    $('#rejeitar').modal('toggle')
  }

  limpaMensagem() {
    this.tipoIcone = 'info'
    this.mensagem = '';
    this.tipoMensagem = 'info';
    this.exibirMensagem = false;
    this.loadService.hide()
  }

  classifica() {
    
    if (this.tiposDocCombo !== undefined && this.tiposDocCombo !== []) {
      let x = []
      x = this.tiposDocCombo

      x.sort((n1 , n2 ) : number => {
        if (n1.label < n2.label) return -1;
        if (n1.label > n2.label) return 1;
        return 0;
      });
      this.tiposDocCombo = x;
    }

  }

  preencheuCampos(event){
    //console.log(event)
  }

  insertDocuments(input) {
    if (input) {
     
      input.click();
    }
  }
 

}
