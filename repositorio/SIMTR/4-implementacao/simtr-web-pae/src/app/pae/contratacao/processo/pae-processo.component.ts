import { ApplicationService } from './../../../services/application/application.service';
import { PaeContratacao } from './../../model/pae-contratacao';
import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { PaeProcessoService } from './pae-processo.service';
import {ConfirmationService} from 'primeng/primeng';
import { PaeContratacaoPatch } from '../../model/pae-contratacao-patch';
import { PaeContratacaoPost } from '../../model/pae-contratacao-post';
import { LoaderService, AlertMessageService } from '../../../services';


declare var $: any;

@Component({
  selector: 'pae-processo',
  templateUrl: './pae-processo.component.html',
  styleUrls: ['./pae-processo.component.css']
})
export class PaeProcessoComponent extends AlertMessageService implements OnInit {
  @Input() processo: PaeContratacao;
  @Input() numero_processo: string;
  @Input() ano_processo: string;
  @Input() consultou: boolean;
  @Output() incluiuProcesso = new EventEmitter<PaeContratacao>();
  @Output() alterouProcesso = new EventEmitter<PaeContratacao>();
  @ViewChild('f') frm: ElementRef;

  itens: any = [];
  tituloModal: String = 'Inclusão de Processo'
  atualizou = false;
  
  entidade: PaeContratacao;
  exibirBotaoIncluir: boolean;
  exibirBotaoAlterar: boolean;
  zerosStr = '00000'
  tipoRetorno = 'success'
  mensagem = ''
  perfilExporta = false;
  perfilEscrita = false;
  perfilLeitura = false;

  constructor(private service: PaeProcessoService, 
    private applicationService: ApplicationService,
    private loadService : LoaderService 
    ) {
        super();
        this.perfilExporta = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
        this.perfilEscrita = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE')
        this.perfilLeitura = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
        this.clearAllMessages()
        if (!this.perfilLeitura) {
          this.addMessageError('Você não possui perfil para consultar processos')
        }
        if (!this.perfilEscrita) {
          this.addMessageError('Você não possui perfil para incluir/alterar dados')
        }
        if (!this.perfilExporta) {
          this.addMessageError('Você não possui perfil para exportar dados')
        }
   }

  ngOnInit() {
    this.entidade = new PaeContratacao;
    if (this.processo.id != null ) {
      this.entidade = this.processo 
      
      this.exibirBotaoAlterar = true;
      this.exibirBotaoIncluir = false;
    } else {
     this.entidade.numero_processo = this.numero_processo
     this.entidade.ano_processo = this.ano_processo
     this.exibirBotaoAlterar = false;
     this.exibirBotaoIncluir = true;
    }
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnChanges() {
    
    if (this.processo === undefined || this.processo.id == null ) {

      this.entidade = this.processo  
      this.limpaTela(this.frm)
    } else {
      if (this.processo.id != null ) {
        this.entidade = this.processo 
        this.exibirBotaoAlterar = true;
        this.exibirBotaoIncluir = false;
      } else {
        this.entidade = new PaeContratacao;
        this.entidade.numero_processo = this.numero_processo
        this.entidade.ano_processo = this.ano_processo
        this.exibirBotaoAlterar = false;
        this.exibirBotaoIncluir = true;
      }
    
    }
  }

  limpaTela(form) {
    if (form) {
      form.control.markAsPristine();
      form.control.markAsUntouched();
    }
    this.atualizou = false;
    //this.incluiu = false; 
    this.tituloModal = 'Inclusão de Processo';
    this.exibirBotaoAlterar = false;
    this.exibirBotaoIncluir = true;

  }
  salvar(form) {
    this.loadService.show()
    if (this.processo.id == null) {
      const entidadePost = new PaeContratacaoPost;
      entidadePost.numero_processo = this.entidade.numero_processo;
      entidadePost.ano_processo = this.entidade.ano_processo;
      entidadePost.ano_pregao = this.entidade.ano_pregao;
      entidadePost.numero_pregao = this.entidade.numero_pregao;
      entidadePost.objeto_contratacao = this.entidade.objeto_contratacao;
      entidadePost.unidade_contratacao = this.entidade.unidade_contratacao;
      entidadePost.unidade_demandante = this.entidade.unidade_demandante
      entidadePost.protocolo_siclg = this.entidade.protocolo_siclg
      
      if (entidadePost.protocolo_siclg === '') {
        entidadePost.protocolo_siclg = null;
        
      }
      this.service.incluirProcesso(entidadePost).subscribe( response => {
        this.processo = response
        this.entidade = response
        //form.control.markAsPristine();
        //form.control.markAsUntouched();
        this.atualizou = false;
        //this.incluiu = true 
        this.exibirBotaoAlterar = true;
        this.exibirBotaoIncluir = false;
        this.incluiuProcesso.emit(this.processo);
        this.loadService.hide()
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
          this.addMessageError(this.mensagem);
     
      });  
    } else {
        
        const entidadePatch = new PaeContratacaoPatch;
        entidadePatch.ano_pregao = this.processo.ano_pregao;
        entidadePatch.numero_pregao = this.processo.numero_pregao;
        entidadePatch.objeto_contratacao = this.processo.objeto_contratacao;
        entidadePatch.unidade_contratacao = this.processo.unidade_contratacao;
        entidadePatch.unidade_demandante = this.processo.unidade_demandante;
        entidadePatch.matricula_finalizacao = this.processo.matricula_finalizacao;
        entidadePatch.protocolo_siclg = this.processo.protocolo_siclg;
        
      if (entidadePatch.protocolo_siclg === '') {
        entidadePatch.protocolo_siclg = null;
        
      }
        this.service.atualizarProcesso(entidadePatch,this.processo.id).subscribe( response => {
          this.atualizou = false;
          //this.incluiu = false;
          this.alterouProcesso.emit(this.processo);
          this.loadService.hide()
        },error => {
          this.loadService.hide()
          
          if (error.error.mensagem) {
              this.mensagem = error.error.mensagem
          } else if (error.message) {
              this.mensagem = 'Situação inesperada: ' + error.message;
            }

          this.tipoRetorno = 'error'
          //this.atualizou = true;
          this.clearAllMessages()
          this.addMessageError(this.mensagem);
        }
      );
    }
  }

  abrirModal(form) {
    this.limpaTela(form);
    
  }

  mudouUnidadeContratacao(event) {
    this.entidade.unidade_contratacao = event
  }

  mudouUnidadeDemandante(event) {
    this.entidade.unidade_demandante = event
  }

  mudouNumeroProcesso(event) {
    this.entidade.numero_processo = event
  }

  mudouNumeroCertame(event) {
    this.entidade.numero_pregao = event
  }
}
