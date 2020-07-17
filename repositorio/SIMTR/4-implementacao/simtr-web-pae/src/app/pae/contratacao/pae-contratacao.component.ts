import { PaeContratacao } from './../model/pae-contratacao';
import { Component, OnInit,  ViewEncapsulation , AfterViewChecked , AfterViewInit, EventEmitter, Output, ViewChild } from '@angular/core';
import { PaeContratacaoService } from './pae-contratacao.service';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { HeaderSearchService } from './../../services/header-search.service';
import { Router} from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormGroup,  FormBuilder,  Validators,  FormControlName } from '@angular/forms';
import { LoaderService } from '../../services/http-interceptor/loader/loader.service';
import { ApplicationService, EventService, AlertMessageService } from './../../services/index';
import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { PaeContratosComponent} from './pae-contratos/pae-contratos.component';
import { PaeContratoPatch } from '../model/pae-contrato-patch';
import { LOCAL_STORAGE_CONSTANTS } from './../../constants/constants';

declare var $: any;

@Component({
  selector: 'pae-contratacao',
  templateUrl: './pae-contratacao.component.html',
  styleUrls: ['./pae-contratacao.component.css']
})
export class PaeContratacaoComponent extends AlertMessageService implements OnInit, AfterViewChecked , AfterViewInit {
  @ViewChild('tbv') tbv: any;
  
  entidade:  PaeContratacao; 
  itens: any = [];
  tituloModal: String = 'Inclusão de Processo'
  numero_processo: string;
  ano_processo: string;
  consultou = false
  sucessoConsulta = false
  incluir = false;
  limparContratos = false;
  limparApensos = false;
  exibirConsultaContratos = true;
  exibirConsultaApensos = true;
  consultaPorContrato = false;
  limparConsulta = false;
  incluidoSucesso = false;
  alteradoSucesso = false;
  index = 0;
  docPara = 'PRC';
  zerosStr = '00000'
  strNrProcesso: string;
  mensagem = '';
  tipoMensagem = 'success';
  exibirMensagem = false;
  tiposExporta: any = [
    {'label': 'Completo', 'value': true},
    {'label': 'Individual', 'value': false}
  ]
  tipoExportaSelecionado = true
  contratoNaoLocalizado = false
  perfilExporta = false;
  perfilEscrita = false;
  perfilLeitura = false;

  constructor(
    private service: PaeContratacaoService, 
    private applicationService: ApplicationService,
    private loadService : LoaderService ,
    private eventService : EventService
   
  
  ) {
    super();
    this.perfilExporta = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
    this.perfilEscrita = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE')
    this.perfilLeitura = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
    this.clearAllMessages()

    if (!this.perfilLeitura) {
      this.addMessageError('Você não possui perfil para fazer consultas')
    }
    if (!this.perfilEscrita) {
      this.addMessageError('Você não possui perfil para incluir/alterar dados')
    }
    if (!this.perfilExporta) {
      this.addMessageError('Você não possui perfil para exportar dados')
    }

   }

   

  openNext() {
    this.index = (this.index === 3) ? 0 : this.index + 1;
  }

  openPrev() {
    this.index = (this.index === 0) ? 3 : this.index - 1;
  }

  ngOnInit() {
    this.entidade = new PaeContratacao();
  }

  ngOnChange() {
    
  }

  limpaTela(form) {
    if (form) {
      form.control.markAsPristine();
      form.control.markAsUntouched();
    }
    this.entidade = new PaeContratacao();
    this.tituloModal = 'Inclusão de Processo';
    this.limparConsulta = false;
    this.incluidoSucesso = false;
    this.alteradoSucesso = false;
    this.mensagem = ''
    this.tipoMensagem = 'error'
    this.exibirMensagem = false;

  }

  consultar() {
    
    const processo = this.numero_processo
    const ano = this.ano_processo
    if (this.numero_processo === null || this.numero_processo === undefined || Number(this.numero_processo) === 0 ||
      this.ano_processo === null || this.ano_processo === undefined || Number(this.ano_processo) === 0  ) {
        this.mensagem = 'Informe o número e o ano do Processo'
        this.tipoMensagem = 'error'
        //this.exibirMensagem = true
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
        return
      }
    this.refreshForm();
    this.numero_processo = processo;
    this.ano_processo = ano;
    this.incluidoSucesso = false;
    this.incluir = false;
    this.limparConsulta = true;
    this.alteradoSucesso = false;
    this.mensagem = '';
    this.exibirMensagem = false;
    this.tipoMensagem = 'success';
    this.strNrProcesso = this.numero_processo.toString();
    if (this.strNrProcesso.length < 5) {
      this.numero_processo = this.zerosStr.substr(0,5 - this.strNrProcesso.length ) + this.numero_processo
    }
    this.loadService.show()
    this.service.obterProcessoNumeroAno(this.numero_processo, this.ano_processo).subscribe(dados => {
      this.loadService.hide()
      if (dados == null) {
        this.entidade = new PaeContratacao;
        
        this.sucessoConsulta = false;
        //this.incluir = true
        this.clearAllMessages()
        this.addMessageError('Processo não locallizado. Informe os dados do novo processo e pressione o botão Incluir Processo.');
        this.exibirConsultaContratos = true;
        this.exibirConsultaApensos = true;
        this.entidade.numero_processo = this.numero_processo;
        this.entidade.ano_processo = this.ano_processo;
      } else {
          
          this.entidade = dados;
          this.sucessoConsulta = true;
          this.incluir = false;
          this.exibirConsultaContratos = false;
          this.exibirConsultaApensos = true;
      }
      this.consultou = true;
    }, error => {
        
        this.loadService.hide();
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else if (error.message) {
            this.mensagem = 'Problemas de comunicação com o servidor: ' + error.message;
          }

        this.tipoMensagem = 'error'
        //this.exibirMensagem = true;
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
    }); 
  }  

  ngAfterViewInit() : void { }

  ngAfterViewChecked(): void { }
  headerSearch(filtros: any) { }
  cleanHeaderSearch() { }
  novaConsulta() {
    
    this.consultou = false;
  }

  refreshForm() {
    this.entidade = new PaeContratacao;
    this.sucessoConsulta = false
    this.consultou = false
    this.tituloModal = 'Inclusão de Processo';
    this.numero_processo = '';
    this.ano_processo = '';
    this.incluir = false;
    this.limparContratos = true;
    this.limparApensos = true;
    this.exibirConsultaContratos = true;
    this.exibirConsultaApensos = true;
    this.consultaPorContrato = false;
    this.limparConsulta = true;
    this.incluidoSucesso = false;
    this.alteradoSucesso = false;
    this.mensagem = ''
    this.tipoMensagem = 'success'
    this.exibirMensagem = false;
    this.contratoNaoLocalizado = false
    this.clearAllMessages()
    

  }
  quandoPesquisarPorContrato(event) {
    this.refreshForm();
    const params = event.split(';')
    const numero_contrato = params[0];
    const ano_contrato = params[1];
    this.service.obterContratoPorNumeroAno(numero_contrato, ano_contrato).subscribe( dados => {
      if (dados == null) {
        this.contratoNaoLocalizado = true
        this.clearAllMessages()
        this.addMessageError('Contrato não localizado!')
        return
      }
      
      this.entidade = new PaeContratacao
      this.entidade.ano_pregao = dados.processo_administrativo.ano_pregao
      this.entidade.ano_processo = dados.processo_administrativo.ano_processo
      this.ano_processo = dados.processo_administrativo.ano_processo
      this.entidade.numero_pregao = dados.processo_administrativo.numero_pregao
      this.entidade.objeto_contratacao = dados.processo_administrativo.objeto_contratacao
      this.entidade.unidade_contratacao = dados.processo_administrativo.unidade_contratacao
      this.entidade.id =  dados.processo_administrativo.id 
      this.entidade.numero_processo = dados.processo_administrativo.numero_processo 
      this.numero_processo = dados.processo_administrativo.numero_processo 
      this.entidade.matricula_inclusao = dados.processo_administrativo.matricula_inclusao
      this.entidade.data_hora_inclusao = dados.processo_administrativo.data_hora_inclusao
      this.entidade.data_hora_finalizacao = dados.processo_administrativo.data_hora_finalizacao
      this.entidade.matricula_finalizacao = dados.processo_administrativo.matricula_finalizacao
      this.entidade.protocolo_siclg = dados.processo_administrativo.protocolo_siclg
      this.entidade.unidade_demandante = dados.processo_administrativo.unidade_demandante
      const contratos = [] 
      contratos[0] = new PaeContratoPatch
      contratos[0].numero_contrato = dados.numero_contrato
      contratos[0].ano_contrato = dados.ano_contrato
      contratos[0].id = dados.id 
      contratos[0].cpf_fornecedor = dados.cpf_fornecedor
      contratos[0].descricao_contrato = dados.descricao_contrato
      contratos[0].cnpj_fornecedor = dados.cnpj_fornecedor
      this.entidade.contratos = contratos
      this.sucessoConsulta = true;
      this.consultaPorContrato = true;
      this.exibirConsultaContratos = false;
      this.consultou = true;
      
    }, error => {
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else if (error.message) {
          this.mensagem = 'Problemas de comunicação com o servidor: ' + error.message;
          }

        this.tipoMensagem = 'error'
        //this.exibirMensagem = true
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
    });
    
  }
  quandoIncluirProcesso(processo) {
    this.entidade = processo
    this.incluir = false;
    //this.incluidoSucesso = true;
    this.clearAllMessages()
    this.addMessageSuccess('Processo Incluído com Sucesso.')
    this.alteradoSucesso = false;
   
  }

  quandoAlterarProcesso(processo) {
    this.entidade = processo
    this.incluir = false;
    //this.alteradoSucesso = true;
    this.clearAllMessages()
    this.addMessageSuccess('Processo Alterado com Sucesso.')
    this.incluidoSucesso = false;
   
  }

  exportarPdf() {
    this.loadService.show()
    this.service.exportarProcesso(this.entidade.id,this.tipoExportaSelecionado).subscribe( dados => {
      
      const dlnk:any  = document.querySelector('#dwnldLnk');
      const blob = new Blob([dados], {'type':'application/zip'});
      dlnk.href = URL.createObjectURL(blob);
      dlnk.download = 'Processo.' + this.numero_processo + '.'+ this.ano_processo + '.zip'
      dlnk.click();
      this.loadService.hide()
     }, error => {
        
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else if (error.message) {
          this.mensagem = 'Problemas de comunicação com o servidor: ' + error.message;
          }

        
        this.tipoMensagem = 'error'
        //this.exibirMensagem = true 
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
        this.loadService.hide()
        
     })
;
  }
  mudouAnoProcesso(event) {
    this.ano_processo = event
  }
}
