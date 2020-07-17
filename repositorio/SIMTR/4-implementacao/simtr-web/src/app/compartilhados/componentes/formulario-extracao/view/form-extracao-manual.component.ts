import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { FormGroup } from '@angular/forms';
import { AlertMessageService, ApplicationService } from 'src/app/services';
import { Utils } from 'src/app/utils/Utils';
import { LoaderService } from '../../../../services/http-interceptor/loader/loader.service';
import { CnpjQuestion } from '../model/cnpj-question';
import { CpfCnpjQuestion } from '../model/cpf-cnpj-question';
import { CpfQuestion } from '../model/cpf-question';
import { DataQuestion } from '../model/data-question';
import { DocumentoPost } from '../model/documento-post';
import { EmailQuestion } from '../model/email-question';
import { MonetarioQuestion } from '../model/monetario.question';
import { DecimalQuestion } from '../model/decimal.question';
import { NumberQuestion } from '../model/number-question';
import { QuestionBase } from "../model/question-base";
import { SelectSnQuestion } from '../model/select-sn-question';
import { SelectVfQuestion } from '../model/select-vf-question';
import { TelefoneDDDQuestion } from '../model/telefone-ddd-question';
import { TextareaQuestion } from '../model/textarea-question';
import { TextboxQuestion } from "../model/textbox-question";
import { FormExtracaoManualService } from '../service/form-extracao-manual.service';
import { UtilsCompartilhado } from '../utilidades/utils-compartilhado';
import { ContaCaixaQuestion } from '../model/conta-caixa.question';

declare var $: any;

@Component({
  selector: 'form-extracao-manual',
  templateUrl: './form-extracao-manual.component.html',
  styleUrls: ['./form-extracao-manual.component.css']
})

export class FormExtracaoManualComponent extends AlertMessageService implements OnInit {
  @ViewChild('f') frm : any
  @Input() documentoPost: DocumentoPost
  @Input() exibeTodos: boolean
  
  @Input() salvarForm: EventEmitter<any> = new EventEmitter<any>();
  @Input() cancelarForm: EventEmitter<any> = new EventEmitter<any>();
  @Input() rejeitarForm: EventEmitter<any> = new EventEmitter<any>();

  @Output() statusForm: EventEmitter<any> = new EventEmitter<any>();
  @Output() preencheuCampos: EventEmitter<any> = new EventEmitter<any>();
  @Output() erro: EventEmitter<any> = new EventEmitter<any>();
  

  questions: QuestionBase<any>[] = [];
  questoes: QuestionBase<any>[] = [];
  form: FormGroup;
  payLoad = '';
  public display = 'none'
  public visualizar = false;
  public atributos;
  private tiposDocumento = [];
  public tiposDocCombo = [];
  public tipoDocSelecionado;
  
  public exibeSalvar = true;
  public habilitaComboTipoDoc = false;
  public resumo_situacao = []
  public formData: FormGroup;
  
  public docPara = 'documento'
  public exibirImg = true;
  public larguraTabela = 'col-md-6 row'
  public diminuiu = true;
  public ehPdf = false;
  public escalaImg = 0.4
  public rotacaoImg = 0
  
  public exibeRejeitar = false;
  public rejeicaoSelecionada = '';
  public mensagem = '';
  public tipoMensagem = 'info';
  public exibirMensagem = false;
  
  private utils: UtilsCompartilhado = new UtilsCompartilhado();
  public mensValida = ''
  public hoje = new Date().toISOString().substr(0, 10);
  public tipoIcone = 'info'
  public erroForm = true;
  public valoresAtributos = []
  public resultadoExtracao: any
  public conteudoForm = []
  private grupos = [[]]
  private isGruposOk = false
  separadorDecimal: string = ',';

  tiposRejeicao: any = [
    {'label': 'Documento invalido', 'value': 'DOC001'},
    {'label': 'Documento Incompleto ', 'value': 'DOC002'},
    {'label': 'Documento com informação obstruída', 'value': 'DOC003'},
  ]

  rejeicoes = []

  constructor(private service: FormExtracaoManualService,
        private loadService: LoaderService, private appService: ApplicationService
    ) {
      super();

  }

  ngOnInit(): void {
    
    this.obterTipologia();
    this.salvarForm.subscribe(dados =>{
      if (this.form.valid) {
        if (dados) {
          this.documentoPost.tipo_documento = dados.tipo_documento
        }
        this.salvar()
      }
    });
    
    this.cancelarForm.subscribe(dados => {
      this.cancelar()
  
    });

    this.rejeitarForm.subscribe(dados => {
      this.rejeicaoSelecionada = dados.codigo_rejeicao
      this.rejeitar()
    });

  }

  ngOnChanges() {
    if(!this.tiposDocumento || this.tiposDocumento.length == 0){
      this.obterTipologia();
    }

    if (this.documentoPost) {
      if (this.documentoPost.tipo_documento.id != 0) {
        this.tipoDocSelecionado = this.tiposDocumento[this.documentoPost.tipo_documento.id].identificador_tipo_documento
      }
      if (!this.documentoPost.executa_classificacao) {
        this.habilitaComboTipoDoc = !this.documentoPost.executa_classificacao
      }
      
      this.montaForm(this.documentoPost.tipo_documento.id)
    }
    
  }


  ngDoCheck() {
    let x: any
    x = {
      status_extracao: false
    }
    
    if (this.form ) {
      this.isGruposOk = this.verificaGrupos()
      x = {
        status_extracao: this.form.valid && this.isGruposOk
      }
      if (this.form.valid && this.isGruposOk) {
        this.retornaAtributos()
      }  
    }
   
    this.statusForm.emit(x);  
   
  }

  verificaGrupos() {
    let retorno = true
    for (let i = 0; i < this.grupos.length; i++) {
      if (this.grupos[i].length > 0) {
        retorno = this.verificaAtributosGrupos(i)
        if (retorno == false) {
          return false
        }
      }
    }
    return retorno
  }

  verificaAtributosGrupos(grupo) {
    let conta = 0
    for (let j = 0; j < this.grupos[grupo].length; j++) {
      if (this.grupos[grupo][j].length > 0) {
        if (this.form.controls[this.grupos[grupo][j]].value.length > 0) {
          conta++
        } 
      }
    }
    
    if (this.grupos[grupo].length != 0 && conta != 0 && conta != this.grupos[grupo].length) {
      return false
    }

    return true 
  }

  verificaAtributosNaoPreechidosGrupo(grupo) {
    let atributos = ''
    for (let j = 0; j < this.grupos[grupo].length; j++) {
      if (this.grupos[grupo][j].length > 0) {
        if (this.form.controls[this.grupos[grupo][j]].value.length == 0) {
          atributos = atributos + this.questoes[this.grupos[grupo][j]].label + ', '
        } 
      }
    }
    
    return atributos.substr(0, atributos.length - 2) 
  }

  retornaAtributos() {
    let resultado: any = {}
    let valores = this.form.value
    let chaves = Object.keys(valores)
    let atributos = []

    for (let i = 0; i < chaves.length; i++) {
      let x = {
        chave: chaves[i],
        valor: valores[chaves[i]]
      }
      atributos.push(x)
    }
    resultado.atributos = atributos
    
    if (JSON.stringify(atributos) != JSON.stringify(this.conteudoForm)) {
      this.conteudoForm = atributos
      
      this.preencheuCampos.emit(JSON.stringify(atributos))    
    }
    

  }

  montaResultado(){
    let resultado: any = {}
    let valores = this.form.value
    let chaves = Object.keys(valores)
    let atributos = []
    
    if (this.documentoPost == undefined) {
      this.retornaErro(500, 'Formulário não pode ser salvo.', 'Erro no processamento do formulário. A entrada para este componente não foi informada.')
      return
    }

    if (!this.form.valid) {
      this.retornaErro(500, 'Formulário não pode ser salvo.', 'Erro no processamento do formulário. Formulário com campos inconsistentes.')
      return
    }

    //resultado.codigo_fornecedor = this.documentoPost.codigo_controle
    if (this.documentoPost.tipo_documento) {
      resultado.tipo_documento = this.documentoPost.tipo_documento.id
    } else {
      resultado.tipo_documento = this.tipoDocSelecionado
    }
    for (let i = 0; i < chaves.length; i++) {
      let x = {
        chave: chaves[i],
        valor: valores[chaves[i]],
        indice_assertividade: 100
      }
      atributos.push(x)
    }
    resultado.atributos = atributos
    
    return resultado
  }

  exibeErro(error) {
    this.erro.emit(error)

  }

  retornaErro(erro, mensagem, detalhe) {
    let error = {
      error: {
        'codigo_http': erro,
        'mensagem': mensagem,
        'detalhe': detalhe,
        'stacktrace': ''
      }
    }
    this.erro.emit(error)
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
 
  montaForm(tipo) {
    
    this.questions = []
    this.valoresAtributos = []
    this.grupos = [[]]
    
    
    if (this.documentoPost.atributos != null && this.documentoPost.atributos.length > 0 ) {
      
      for (let i = 0; i < this.documentoPost.atributos.length; i++) {
        this.valoresAtributos[this.documentoPost.atributos[i].chave] = this.documentoPost.atributos[i].valor
      }
    }
        
    if (this.tiposDocumento[tipo] != undefined) {
      this.classificaPorOrdemApresentacao(tipo)
      for (let i = 0; i < this.tiposDocumento[tipo].atributos_documento.length; i++) {
        this.montaElementoForm(this.tiposDocumento[tipo].atributos_documento[i]);
      }
    
      this.form = this.service.toFormGroup(this.questions)
      this.loadService.hide();
    }
  }

  montaElementoForm(atributo) {
    let atr;
    
    if (atributo.grupo_atributo != null) {
      if (this.grupos[atributo.grupo_atributo] == undefined) {
        this.grupos[atributo.grupo_atributo] = []
      }
      this.grupos[atributo.grupo_atributo].push(atributo.nome_documento)       
    }

    if (atributo.tipo_campo == "TEXT" && (this.exibeTodos || atributo.presente_documento)) {
      atr =  new TextboxQuestion({
        key: atributo.nome_documento,
        label: atributo.nome_negocial,
        required: atributo.obrigatorio,
        type: 'text',
        grupo: atributo.grupo_atributo,
        value: this.valoresAtributos[atributo.nome_documento],
        order: atributo.ordem_apresentacao
      })
      this.questions.push(atr)
      this.questoes[atr.key] = atr
    } else {
        if (atributo.tipo_campo == "CPF" && (this.exibeTodos || atributo.presente_documento)) {
          atr =  new CpfQuestion({
            key: atributo.nome_documento,
            label: atributo.nome_negocial,
            required: atributo.obrigatorio,
            type: 'cpf',
            grupo: atributo.grupo_atributo,
            value: this.valoresAtributos[atributo.nome_documento],
            order: atributo.ordem_apresentacao
          })
          this.questions.push(atr)
          this.questoes[atr.key] = atr
      } else {
            if (atributo.tipo_campo == "DATE" && (this.exibeTodos || atributo.presente_documento)) {
              atr =  new DataQuestion({
                key: atributo.nome_documento,
                label: atributo.nome_negocial,
                required: atributo.obrigatorio,
                type: 'data',
                grupo: atributo.grupo_atributo,
                value: this.valoresAtributos[atributo.nome_documento],
                order: atributo.ordem_apresentacao
              })
              this.questions.push(atr)
              this.questoes[atr.key] = atr
            } else {
                if (atributo.tipo_campo == "EMAIL" && (this.exibeTodos || atributo.presente_documento)) {
                  atr =  new EmailQuestion({
                    key: atributo.nome_documento,
                    label: atributo.nome_negocial,
                    required: atributo.obrigatorio,
                    type: 'email',
                    grupo: atributo.grupo_atributo,
                    value: this.valoresAtributos[atributo.nome_documento],
                    order: atributo.ordem_apresentacao
                  })
                  this.questions.push(atr)
                  this.questoes[atr.key] = atr
              } else {
                  if (atributo.tipo_campo == "PASSWORD" && (this.exibeTodos || atributo.presente_documento)) {
                    atr =  new TextboxQuestion({
                      key: atributo.nome_documento,
                      label: atributo.nome_negocial,
                      required: atributo.obrigatorio,
                      type: 'password',
                      grupo: atributo.grupo_atributo,
                      value: this.valoresAtributos[atributo.nome_documento],
                      order: atributo.ordem_apresentacao
                    })
                    this.questions.push(atr)
                    this.questoes[atr.key] = atr
                } else {
                    if (atributo.tipo_campo == "TEXTAREA" && (this.exibeTodos || atributo.presente_documento)) {
                      atr =  new TextareaQuestion({
                        key: atributo.nome_documento,
                        label: atributo.nome_negocial,
                        required: atributo.obrigatorio,
                        type: 'textarea',
                        grupo: atributo.grupo_atributo,
                        value: this.valoresAtributos[atributo.nome_documento],
                        order: atributo.ordem_apresentacao
                      })
                      this.questions.push(atr)
                      this.questoes[atr.key] = atr
                  } else {
                      if (atributo.tipo_campo == "NUMBER" && (this.exibeTodos || atributo.presente_documento)) {
                        atr =  new NumberQuestion({
                          key: atributo.nome_documento,
                          label: atributo.nome_negocial,
                          required: atributo.obrigatorio,
                          type: 'number',
                          grupo: atributo.grupo_atributo,
                          value: this.valoresAtributos[atributo.nome_documento],
                          order: atributo.ordem_apresentacao
                        })
                        this.questions.push(atr)
                        this.questoes[atr.key] = atr
                    } else {
                        if (atributo.tipo_campo == "SELECT_SN" && (this.exibeTodos || atributo.presente_documento)) {
                          atr =  new SelectSnQuestion({
                            key: atributo.nome_documento,
                            label: atributo.nome_negocial,
                            required: atributo.obrigatorio,
                            type: 'select_sn',
                            grupo: atributo.grupo_atributo,
                            value: this.valoresAtributos[atributo.nome_documento],
                            order: atributo.ordem_apresentacao
                          })
                          this.questions.push(atr)
                          this.questoes[atr.key] = atr
                      } else {
                          if (atributo.tipo_campo == "SELECT_VF" && (this.exibeTodos || atributo.presente_documento)) {
                            atr =  new SelectVfQuestion({
                              key: atributo.nome_documento,
                              label: atributo.nome_negocial,
                              required: atributo.obrigatorio,
                              type: 'select_vf',
                              grupo: atributo.grupo_atributo,
                              value: this.valoresAtributos[atributo.nome_documento],
                              order: atributo.ordem_apresentacao
                            })
                            this.questions.push(atr)
                            this.questoes[atr.key] = atr
                        } else {
                            if (atributo.tipo_campo == "CNPJ" && (this.exibeTodos || atributo.presente_documento)) {
                              atr =  new CnpjQuestion({
                                key: atributo.nome_documento,
                                label: atributo.nome_negocial,
                                required: atributo.obrigatorio,
                                type: 'cnpj',
                                grupo: atributo.grupo_atributo,
                                value: this.valoresAtributos[atributo.nome_documento],
                                order: atributo.ordem_apresentacao
                              })
                              this.questions.push(atr)
                              this.questoes[atr.key] = atr
                          } else {
                              if (atributo.tipo_campo == "CPF_CNPJ" && (this.exibeTodos || atributo.presente_documento)) {
                                atr =  new CpfCnpjQuestion({
                                  key: atributo.nome_documento,
                                  label: atributo.nome_negocial,
                                  required: atributo.obrigatorio,
                                  type: 'cpfcnpj',
                                  grupo: atributo.grupo_atributo,
                                  value: this.valoresAtributos[atributo.nome_documento],
                                  order: atributo.ordem_apresentacao
                                })
                                this.questions.push(atr)
                                this.questoes[atr.key] = atr
                            } else {
                                if (atributo.tipo_campo == "TELEFONE_DDD" && (this.exibeTodos || atributo.presente_documento)) {
                                  atr =  new TelefoneDDDQuestion({
                                    key: atributo.nome_documento,
                                    label: atributo.nome_negocial,
                                    required: atributo.obrigatorio,
                                    type: 'telefone_ddd',
                                    grupo: atributo.grupo_atributo,
                                    value: this.valoresAtributos[atributo.nome_documento],
                                    order: atributo.ordem_apresentacao
                                  })
                                  this.questions.push(atr)
                                  this.questoes[atr.key] = atr
                              } else{
                                if(atributo.tipo_campo == "MONETARIO"){
                                  atr =  new MonetarioQuestion({
                                    key: atributo.nome_documento,
                                    label: atributo.nome_negocial,
                                    required: atributo.obrigatorio,
                                    type: 'monetario',
                                    grupo: atributo.grupo_atributo,
                                    value: this.valoresAtributos[atributo.nome_documento],
                                    order: atributo.ordem_apresentacao
                                  });
                                  this.questions.push(atr);
                                  this.questoes[atr.key] = atr
                                } else {
                                  if(atributo.tipo_campo == "DECIMAL"){
                                    atr =  new DecimalQuestion({
                                      key: atributo.nome_documento,
                                      label: atributo.nome_negocial,
                                      required: atributo.obrigatorio,
                                      type: 'decimal',
                                      grupo: atributo.grupo_atributo,
                                      value: this.valoresAtributos[atributo.nome_documento],
                                      order: atributo.ordem_apresentacao
                                    })
                                    this.questions.push(atr)
                                    this.questoes[atr.key] = atr
                                  }else{
                                    if(atributo.tipo_campo == "CONTA_CAIXA"){
                                      atr =  new ContaCaixaQuestion({
                                        key: atributo.nome_documento,
                                        label: atributo.nome_negocial,
                                        required: atributo.obrigatorio,
                                        type: 'conta_caixa',
                                        grupo: atributo.grupo_atributo,
                                        value: this.valoresAtributos[atributo.nome_documento],
                                        order: atributo.ordem_apresentacao
                                      })
                                      this.questions.push(atr)
                                      this.questoes[atr.key] = atr
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
        }
      }
  }

  isValid(question, tipo) { 
    
    let questionL = question.toLowerCase()

    if ((tipo == 'cpf' || tipo == 'cnpj' || tipo == 'cpfcnpj') && this.form.controls[question].value.length >= 11 ) {
     
      if (!this.utils.validaCpfCnpj(this.form.controls[question].value)) {
        this.mensValida = 'valor inválido!'
        this.form.controls[question].setErrors({'incorrect': true});
        return false
      }
      this.form.controls[question].setErrors(null);
      return true;
    }
    
    if (tipo == 'data'  && this.form.controls[question].value.length >= 8 && this.form.controls[question].value.length <= 10) {
      
      let dataFormatada = Utils.formataDataComBarra(this.form.controls[question].value);
      this.form.controls[question].setValue(dataFormatada);

      if (!this.utils.checkDate(dataFormatada)) {
        this.mensValida = ' não é uma data válida!'
        this.form.controls[question].setErrors({'incorrect': true});
        return false
      }
      this.form.controls[question].setErrors(null);
      return true;
    }

    if (!this.form.controls[question].valid) {
      this.mensValida = 'Campo com valor inválido'
      return false
    } 
    
    if (this.form.controls[question].value.length > 0 && this.questoes[question].grupo != null ) {
      
        if (!this.verificaAtributosGrupos(this.questoes[question].grupo)) {
          let campos = ''
          campos = this.verificaAtributosNaoPreechidosGrupo(this.questoes[question].grupo)
          this.mensValida = 'preencha também -> ' + campos
          return false
        } else {
            this.mensValida = ''
            return true
        }
     
    }

    return true;
  }

  isTouched(question) {
    return this.form.controls[question].touched; 

  }

  mudouTipoDocumento(tipo) {
    this.montaForm(tipo)
  
  }

  voltar() {
    
    this.limpaMensagem();
    
    this.visualizar = false;
    this.display = 'none'
  
  }

  cancelar() {

    if(this.documentoPost.codigo_controle){
      this.service.cancelarDocumento(this.documentoPost.codigo_controle).subscribe( () => {
      
        console.log('SUCESSO CANCELAMENTO')
        this.service.cancelamentoCompleto.emit({salvo: true, mensagem: 'SUCESSO CANCELAMENTO'})
  
      }, error => {
  
        this.service.cancelamentoCompleto.emit({salvo: false, mensagem: error})
        this.exibeErro(error);
        throw error;
  
      }); 

    }else{// condição para atender quando o documento não foi salvo ainda.
      this.service.cancelamentoCompleto.emit({salvo: false, mensagem: ""})
    }

  }

  salvar() {
   
    this.realizarAlteracoesAntesDeSalvar();
    let dadosForm = this.montaResultado();

    if(this.documentoPost.codigo_controle){
      this.service.patchDocumento(this.documentoPost.codigo_controle, dadosForm).subscribe( () => {
      
        console.log('SALVOU COM SUCESSO');
        this.service.salvoComSucesso.emit({salvo: true, mensagem: 'SALVOU COM SUCESSO'});
  
      },error => {
  
        this.service.salvoComSucesso.emit({salvo: false, mensagem: error});
        this.exibeErro(error);
        throw error;
  
      });

    }else{// condição para atender quando o documento não foi salvo ainda.
      this.service.salvoComSucesso.emit({salvo: false, mensagem: "", atributos: dadosForm.atributos});
    }
  
  }

  rejeitar() {
    let resultado: any = {};
    this.limpaMensagem();
    //resultado.codigo_controle = this.documentoPost.codigo_controle
    resultado.tipo_documento = this.documentoPost.tipo_documento.id
    resultado.codigo_rejeicao = this.rejeicaoSelecionada
    
    this.service.patchDocumento(this.documentoPost.codigo_controle, resultado).subscribe( () =>{
      console.log('SUCESSO REJEIÇÃO!!!')      
    },
      error => {
      
      this.exibeErro(error)
      throw error;
    });
  }

  limpaMensagem() {
    this.tipoIcone = 'info'
    this.mensagem = '';
    this.tipoMensagem = 'info';
    this.exibirMensagem = false;
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

  classificaPorOrdemApresentacao(tipo) {
    
    if (this.tiposDocumento[tipo].atributos_documento !== undefined && this.tiposDocumento[tipo].atributos_documento !== []) {
      let x = []
      x = this.tiposDocumento[tipo].atributos_documento

      x.sort((n1 , n2 ) : number => {
        if (n1.ordem_apresentacao < n2.ordem_apresentacao) return -1;
        if (n1.ordem_apresentacao > n2.ordem_apresentacao) return 1;
        return 0;
      });
      this.tiposDocumento[tipo].atributos_documento = x;
    }

  }

  realizarAlteracoesAntesDeSalvar(){

    if(this.questions && this.questions.length > 0){
      this.questions.forEach(question => {
        let campo: any = question as any;

        let value: any = this.form.controls[campo.key].value;

        if ((campo.type == 'monetario' || campo.type == 'decimal') && value != '' && value != undefined) {
          
          let valor = Utils.aplicarMascaraMonetario(value);
          if(valor !== '' && valor.indexOf(',') !== -1){
            let valorMonetario = valor.replace(/\./g, '').replace(',', '.');
            this.form.controls[campo.key].setValue(valorMonetario);
          }
        }else if(campo.type == 'conta_caixa' && value && value != ''){
          let valorSemMascara = value.replace(/\D/g, '');
          this.form.controls[campo.key].setValue(valorSemMascara);
        } 

      });
    }

  }

  completarContaCaixaComZeroEsquerda(question){
    let value: any = this.form.controls[question.key].value;
    if (value && value != '') {
			let resposta: string = Utils.completarContaCaixaComZeroEsquerda(value);
			this.form.controls[question.key].setValue(resposta);
		}
  }

}
