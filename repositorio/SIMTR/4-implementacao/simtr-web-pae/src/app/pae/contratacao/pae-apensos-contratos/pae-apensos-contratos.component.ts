import { PaeApensoPatch } from './../../model/pae-apenso-patch';
import {
  Component,
  ViewEncapsulation,
  Input,
  ViewChild,
  ElementRef
} from '@angular/core';


import { ApplicationService, LoaderService, AlertMessageService } from './../../../services/index';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';
import { environment } from './../../../../environments/environment';
import {PaeApensoPost} from './../../model/pae-apenso-post';
import {PaeApensoProcPatch} from './../../model/pae-apenso-proc-patch';

import { SelectItem, DataTable } from 'primeng/primeng';

import { PaeApensosContratosService } from './pae-apensos-contratos.service';

import { PaeContratacao } from './../../model/pae-contratacao';
import { PaeContratoGet } from '../../model/pae-contrato-get';
import { PaeUtils } from '../../utils/utilidades/pae-utils';

declare var $: any;
@Component({
  selector: 'pae-apensos-contratos',
  templateUrl: './pae-apensos-contratos.component.html',
  styleUrls: ['./pae-apensos-contratos.component.css'],
  encapsulation: ViewEncapsulation.None
  
})
export class PaeApensosContratosComponent extends AlertMessageService {
  @Input() processo: PaeContratacao;
  @Input() contrato: any;
  @Input() exibirConsultaApensos: boolean;
  @Input() limparContratos: boolean;
  @ViewChild('f') frm: ElementRef;
  @ViewChild('tbv') tbv: any;
  @ViewChild('dt') datatable: DataTable;
  indiceTab = 0;
  incluirApenso = false;
  incluiu = false;
  atualizou = false;
  apensos: any = [];
  apensoSelecionado: any;
  tipoApensoSelecionado;
  incluirEntidade = false;
  labelIncAlt = 'INCLUIR';
  sucessoConsulta = false;
  exibirPesquisa = false;
  mostrarApensos = true;
  larguraTabela = 'col-md-6 row';
  tipoApensos: SelectItem[];
  entidade: any;
  mensagem = '';
  mostrarDados = false;
  docPara = 'APSC'
  tipoRetorno = 'Sucess';
  validador: PaeUtils = new PaeUtils
  itensDisplay = 10;
  exibirSiclg = true;
  exibirBotao = false;
  apensoAnexo = true;
  cpf_cnpj = ''
  perfilExporta = false;
  perfilLeitura = false;
  perfilEscrita = false;
  exibirFiltro = 'block';
  labelFiltro = 'OCULTAR LISTA'
  infoRegistros = '';
  objPaginar;

  constructor(
    private util:PaeUtils,
    private applicationService: ApplicationService,
    private service: PaeApensosContratosService,
    private loadService : LoaderService 

  ) {
    super();
    this.perfilExporta = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
    this.perfilEscrita = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE')
    this.perfilLeitura = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnInit() {
    
    this.incluirApenso = false;
    this.entidade = new PaeApensoPost;
    this.contrato = new PaeContratoGet;
    
   
    this.tipoApensos = [ 
      {'label': 'Anexo', 'value': 'AX'},
      {'label': 'Contratação', 'value': 'CT'},
      { 'label': 'Gestão Formal', 'value': 'GF' },
      { 'label': 'Gestão Operacional', 'value': 'GO' },
      { 'label': 'Pagamento', 'value': 'PG' },
      { 'label': 'Penalidade de Contrato', 'value': 'PC' },
      { 'label': 'Ressarcimento de Contrato', 'value': 'RC' }
    ]
    
    
    this.tipoApensoSelecionado = this.tipoApensos[0].value;
    this.classifica();
   }      
  
  // tslint:disable-next-line:use-life-cycle-interface
  ngOnChanges() {
    if (this.processo === undefined || this.processo.id == null || this.contrato === undefined || this.contrato.numero_contrato == null) {
      this.entidade = new PaeApensoPost 
      this.contrato = new PaeContratoGet
      this.contrato.apensos = []
      this.apensoSelecionado = {}
      this.limpeza();
      this.limpaTela(this.frm)   
      this.exibirBotao = false;  
      this.infoRegistros = ''
      this.objPaginar = null
    } else {
      this.infoRegistros = ''
      this.objPaginar = null
      this.sucessoConsulta = false
      this.entidade = new PaeApensoPost 
      this.cpf_cnpj = ''
      this.classifica()
      this.testaTipoApenso();
      this.exibirBotao = true;
    }
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngDoCheck() {
    if (!this.objPaginar && this.contrato.apensos) {
      const x = {
        first: 0,
        rows: this.itensDisplay
      }
      this.paginar(x)
    }
  }

  limpeza() {

    this.sucessoConsulta = false;
    this.exibirPesquisa = true;
    this.labelIncAlt = 'INCLUIR'
    this.atualizou = false;
    this.indiceTab = 0;
    this.mensagem = ''
    
    this.exibirApensos();
  }
  getUnidadeUser() {
    const userSSO = JSON.parse(this.applicationService.getUserSSO());
    return userSSO['co-unidade'];
  }

  getMatriculaUser() {
    const userSSO = JSON.parse(this.applicationService.getUserSSO());
    let usuario;
    if(userSSO !== undefined) {
      usuario = userSSO.preferred_username;
    }else {
      usuario = 'c000001'
    }
    return usuario;
  }

  novaPenalidade() {
    this.incluirApenso = true;
    this.incluiu = false;
    this.atualizou = false;

  }

  salvarApenso(form) {
    if (!this.consisteDados()) {
      return
    }

    if (this.entidade.protocolo_siclg === '') {
      this.entidade.protocolo_siclg = null
    }
    this.cpf_cnpj = this.cpf_cnpj.replace(/\D/g, '')
    
    if (this.cpf_cnpj !== null && this.cpf_cnpj !== undefined && this.cpf_cnpj !== '') {
     
      if (this.cpf_cnpj.length <= 11) {
        this.entidade.cpf_fornecedor = this.cpf_cnpj
        this.entidade.cnpj_fornecedor = null
      } else {
          this.entidade.cnpj_fornecedor = this.cpf_cnpj
          this.entidade.cpf_fornecedor = null
      }
    } else {
        this.entidade.cnpj_fornecedor = null;
        this.entidade.cpf_fornecedor = null;
    }
    this.loadService.show()
    if (this.labelIncAlt === 'INCLUIR') {
        this.entidade.tipo_apenso = this.tipoApensoSelecionado
        
        
        this.service.incluirApenso(this.contrato.id,this.entidade).subscribe( response => {
          //this.atualizou = true;
          this.tipoRetorno = 'success'
          this.mensagem = 'Apenso incluido com sucesso';
          this.limpaTela(form)
          this.contratoRefresh();
          this.loadService.hide()
          this.clearAllMessages()
          this.addMessageSuccess(this.mensagem)
        }, error => {
            this.loadService.hide()
            if (error.error) {
              if (error.error.mensagem) {
                  this.mensagem = error.error.mensagem
              } else {
                  this.mensagem = 'Situação inesperada: ' + error.status + '-' + error.message;
              }
            } else if (error.message) {
                this.mensagem = 'Situação inesperada: ' + error.status + '-' + error.message;
              } else {
                this.mensagem = 'Não foi possível identificar o erro retornado pela API'
              }

            this.tipoRetorno = 'error'
            //this.atualizou = true;
            this.clearAllMessages()
            this.addMessageError(this.mensagem);
        });
    } else {
        
        const apensoPatch = new PaeApensoProcPatch
        
        if (this.apensoSelecionado.tipo_apenso.sigla === 'AX' || this.apensoSelecionado.tipo_apenso.sigla === 'CT') {
          
          apensoPatch.titulo_apenso = this.apensoSelecionado.titulo_apenso
        } 
        apensoPatch.tipo_apenso = this.tipoApensoSelecionado
        apensoPatch.descricao_apenso = this.entidade.descricao_apenso
        if (this.entidade.cnpj_fornecedor !== null && this.entidade.cnpj_fornecedor !== undefined && this.entidade.cnpj_fornecedor !== '') {
          apensoPatch.cnpj_fornecedor = this.entidade.cnpj_fornecedor.replace(/\D/g, '')
        }  else {
          apensoPatch.cnpj_fornecedor = null;
        }
        apensoPatch.protocolo_siclg = this.entidade.protocolo_siclg
        
        this.service.alterarApenso(this.entidade.id,apensoPatch).subscribe( response => {
          //this.atualizou = true;
          this.contratoRefresh();
          this.mensagem = 'Apenso alterado com sucesso';
          this.limpaTela(form)
          this.loadService.hide()
          this.clearAllMessages()
          this.addMessageSuccess(this.mensagem);
        }, error => {
          this.loadService.hide()
          if (error.error.mensagem) {
            this.mensagem = error.error.mensagem
          } else if (error.message) {
              this.mensagem = 'Situação inesperada: ' + error.message;
            }

          this.tipoRetorno = 'error'
          //this.atualizou = true;
          this.clearAllMessages()
          this.addMessageError(this.mensagem)
        });
      }
  }

  consisteDados() {
    let ret = true;
    if (this.tipoApensoSelecionado !== 'AX'  && this.tipoApensoSelecionado !== 'CT') {
      ret = this.validador.validaCpfCnpj(this.cpf_cnpj)
    }
    
    if (!ret) {
      if (this.cpf_cnpj.length === 14) {
        this.mensagem = 'CPF inválido!';  
      } else {
        if (this.cpf_cnpj.length === 18) {
          this.mensagem = 'CNPJ inválido!';
        } else {
          this.mensagem = 'O número informado não é um CPF/CNPJ'
        }
      }
      //this.atualizou = true;
      this.tipoRetorno = 'error'
      this.clearAllMessages()
      this.addMessageError(this.mensagem)
      return false
    }
    if ((this.entidade.protocolo_siclg == null || this.entidade.protocolo_siclg === '') && (this.tipoApensoSelecionado === 'PC' || this.tipoApensoSelecionado === 'RC')) {
      this.mensagem = 'Protocolo SICLG não informado';
      //this.atualizou = true;
      this.tipoRetorno = 'error'
      this.clearAllMessages()
      this.addMessageError(this.mensagem)
      return false
    }

    this.mensagem = '';
    this.atualizou = false;
    this.tipoRetorno = 'success'
    return true;
  }
  limpaTela(form) {
    this.incluirEntidade = false;
    this.entidade = new PaeApensoPost;
    this.sucessoConsulta = false;
    this.cpf_cnpj = ''
    this.labelIncAlt = 'INCLUIR'
    this.exibirApensos();
    this.tbv.activeIndex = 0;
    form.control.markAsPristine();
    form.control.markAsUntouched();


  }
  processoRefresh() {
    this.service.obterProcessoPorId(this.processo.id).subscribe( dados => {
      this.processo = dados;
      this.testaTipoApenso();

    });
  }

  contratoRefresh() {
    this.objPaginar = null
    this.service.obterContratoPorId(this.contrato.id).subscribe( dados => {
      this.contrato = dados;
      const x = {
        first: 0,
        rows: this.contrato.apensos.length
      }
      this.paginar(x)
      this.testaTipoApenso();
    });
  }
  
  consultar() {
    this.loadService.show()
    this.service.obterApensoPorId(this.apensoSelecionado.id).subscribe( dados => {
      this.loadService.hide();
      this.entidade = dados;
      if (this.entidade.cnpj_fornecedor) {
        this.cpf_cnpj = this.entidade.cnpj_fornecedor
      } else {
        if (this.entidade.cpf_fornecedor) {
          this.cpf_cnpj = this.entidade.cpf_fornecedor
        }
      }

      this.apensoSelecionado = dados;
      
      this.tipoApensoSelecionado = this.apensoSelecionado.tipo_apenso.sigla
      this.apensoAnexo = false;
      if (this.tipoApensoSelecionado === 'RC' || this.tipoApensoSelecionado === 'PC') {
        this.exibirSiclg = true
      } else if (this.tipoApensoSelecionado === 'AX' || this.tipoApensoSelecionado === 'CT') {
          this.exibirSiclg = false
          this.apensoAnexo = true
          this.entidade.descricao_apenso = this.entidade.titulo_apenso
  
      } else {
          this.exibirSiclg = true
          this.apensoAnexo = false
      }
      
      

      if (this.apensoSelecionado.cpf_fornecedor != null) {
        this.entidade.cnpj_fornecedor = this.apensoSelecionado.cpf_fornecedor;
      }
      if (this.apensoSelecionado.cnpj_fornecedor != null) {
        this.entidade.cnpj_fornecedor = this.apensoSelecionado.cnpj_fornecedor;
      }
    });
    
    this.labelIncAlt = 'ALTERAR';
    this.sucessoConsulta = true;
    this.exibirPesquisa = false;
  
    
  }

  
  exibirApensos() {
    this.mostrarApensos = true;
    this.mostrarDados = false;
    this.tbv.activeIndex = 0
    this.larguraTabela = 'col-md-6'
  }

  exibirDados() {
    this.mostrarApensos = false;
    this.mostrarDados = true;
    this.larguraTabela = 'col-md-12'
  }

  cancelarOperacao(form) {
    this.entidade = new PaeApensoPost;
    this.apensoSelecionado = this.entidade;
    this.exibirApensos();
    this.labelIncAlt = 'INCLUIR'
    this.mensagem = ''
    this.tipoRetorno = 'success'
    this.cpf_cnpj = '';
    this.atualizou  = false;
    this.sucessoConsulta = false;
    form.control.markAsPristine();
    form.control.markAsUntouched();
    this.clearAllMessages();
  }

  novoApenso() {
    this.entidade = new PaeApensoPost;
    this.labelIncAlt = 'INCLUIR'
    this.cpf_cnpj = ''
    this.atualizou = false
    this.exibirDados()
  }

  selecionouApenso() {
    
    this.atualizou = false;
    this.consultar();
   }

   mudouTipoApenso() {
    this.entidade.tipo_apenso = this.tipoApensoSelecionado
    if (this.tipoApensoSelecionado === 'RC' || this.tipoApensoSelecionado === 'PC') {
      this.exibirSiclg = true
      this.apensoAnexo = false
    } else {
      if (this.tipoApensoSelecionado === 'AX' || this.tipoApensoSelecionado === 'CT') {
        this.exibirSiclg = false
        this.entidade.protocolo_siclg = null;
        this.entidade.titulo_apenso = null
        this.entidade.cnpj_fornecedor = ''
        this.apensoAnexo = true

      } else {
        this.exibirSiclg = true
        this.apensoAnexo = false
        this.entidade.protocolo_siclg = null
      }
    }
    
  }

   
   mudouAba(event) {

    if (event.index === 0) {
      this.exibirApensos();
    } else {
        if (event.index === 1) {
          this.exibirDados();
        }

    }
  }
  alterouCpfCnpj(event) {
   this.cpf_cnpj = event
  }

  alterouTitulo(event) {
    this.entidade.titulo_apenso = event
  }

  exportarPdf() {
    this.loadService.show()
    
    this.service.exportarApenso(this.entidade.id).subscribe( dados => {
      
      const dlnk:any  = document.querySelector('#dwnldLnk');
      const blob = new Blob([dados], {'type':'application/zip'});
      dlnk.href = URL.createObjectURL(blob);
      dlnk.download = 'Apenso.Contrato.' + this.entidade.contato_administrativo.numero_contrato + '.' + this.entidade.cnpj_fornecedor + '.zip'
      dlnk.click();
      this.loadService.hide()
     }, error => {
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else if (error.message) {
            this.mensagem = 'Situação inesperada: ' + error.message;
          }

        
        this.tipoRetorno = 'error'
        //this.atualizou = true 
        this.loadService.hide()
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
     });
  }  

  classifica() {
    
    if (this.contrato.apensos !== undefined && this.contrato.apensos !== []) {
      this.contrato.apensos.sort((n1 , n2 ) : number => {
        if (n1.id < n2.id) return -1;
        if (n1.id > n2.id) return 1;
        return 0;
      });
    }

  }

  testaTipoApenso() {
    if (this.contrato.apensos === undefined || this.contrato.apensos === null) {
      return;
    }
    
    for (let i = 0; i < this.contrato.apensos.length; i++) {
      if (this.contrato.apensos[i].tipo_apenso.sigla === 'AX' || this.contrato.apensos[i].tipo_apenso.sigla === 'CT') {
        this.contrato.apensos[i].descricao_apenso = this.contrato.apensos[i].titulo_apenso;
      }
    }
  }
  
  ocultarFiltro() {
    if (this.exibirFiltro === 'block') {
        this.exibirFiltro = 'none'
    } else {
        this.exibirFiltro = 'block'
    }
    if (this.labelFiltro === 'OCULTAR LISTA') {
      this.labelFiltro = 'EXIBIR LISTA';
    } else {
      this.labelFiltro = 'OCULTAR LISTA'
    }
    
  }

  paginar(event)  {
    this.objPaginar = event
    this.infoRegistros  = this.util.paginar(event, this.contrato.apensos.length)
  }
}
