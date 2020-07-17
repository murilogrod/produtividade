import {
  Component,
  ViewEncapsulation,
  Input,
  Output,
  EventEmitter,
  ElementRef,
  ViewChild
} from '@angular/core';


import { ApplicationService, LoaderService, AlertMessageService } from './../../../services/index';

import { FileUploader } from 'ng2-file-upload';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';
import { environment } from './../../../../environments/environment';

import {PaeContratoPost} from './../../model/pae-contrato-post';
import { PaeContratosService } from './pae-contratos.service';
import { PaeContratacao } from './../../model/pae-contratacao';
import { PaeContratoPatch } from './../../model/pae-contrato-patch';
import { PaeUtils } from './../../utils/utilidades/pae-utils';

declare var $: any;
@Component({
  selector: 'pae-contrato',
  templateUrl: './pae-contratos.component.html',
  styleUrls: ['./pae-contratos.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PaeContratosComponent extends AlertMessageService {
  @Input() processo: PaeContratacao;
  @Input() limparContratos: boolean;
  @Input() exibirConsultaContratos: boolean;
  @Input() consultaPorContrato: boolean;
  @Input() contratoNaoLocalizado: boolean;
  @Output() pesquisouContrato = new EventEmitter<string>();
  @ViewChild('f') frm: ElementRef;
  @ViewChild('tbv') tbv: any;
  
  indiceTab = 0;

  uploader: FileUploader = new FileUploader(null);

  //listaGed: DocumentoGED[] = [];

  showCategorizar = true;

  incluirEntidade = false;
  
  atualizou = false;
  apensos: any = [];
  larguraTabela = 'col-md-6 row'
  mostrarContratos = true;
  mostrarDados = false;
  labelIncAlt = 'INCLUIR'
  contratoSelecionado: any;
  mensagem = '';
  sucessoConsulta = false;
  exibirPesquisa = true;
  entidade: any;
  numero_contrato: string;
  ano_contrato: string;
  tpApenso = 'ac';
  limparApensos = true;
  exibirFiltro = 'block';
  exibirConsultaApensos = false;
  limparConsulta = false;
  index = 0
  docPara = 'CTR'
  tipoRetorno = 'success';
  validador: PaeUtils = new PaeUtils
  itensDisplay = 10;
  tiposExporta: any = [
    {'label': 'Exportação Completa', 'value': true},
    {'label': 'Exportação Individual', 'value': false}
  ]
  tipoExportaSelecionado = true
  exibirBotao = false;
  perfilExporta = false;
  perfilLeitura = false;
  perfilEscrita = false;
  labelFiltro = 'OCULTAR LISTA';
  infoRegistros = ''
  objPaginar;
  constructor(
    
    private applicationService: ApplicationService,
    private service: PaeContratosService,
    private loadService : LoaderService,
    private util: PaeUtils

  ) {
    super();
    this.perfilExporta = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
    this.perfilEscrita = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE')
    this.perfilLeitura = this.applicationService.verificarPerfil('MTRADM,MTRPAEOPE,MTRTEC')
    
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnInit() {
    this.incluirEntidade = false;
    this.entidade = new PaeContratoPost;
    this.entidade.cnpj_fornecedor = '';
    this.contratoSelecionado = this.entidade
    
  
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnChanges() {
    
    if (this.processo === undefined || this.processo.id == null) {
      this.contratoSelecionado = {}
      this.exibirPesquisa = true;
      this.exibirBotao = false;
      this.labelIncAlt = 'INCLUIR';
      this.numero_contrato = '';
      this.ano_contrato = '';
      this.limparApensos = true;
      this.index = 0;
      this.atualizou = false;
      this.infoRegistros = ''
      this.objPaginar = null
      this.limpaTela(this.frm);
    } else {
      this.infoRegistros = ''
      this.objPaginar = null
      this.exibirBotao = true
      
    }

  }
 
  // tslint:disable-next-line:use-life-cycle-interface
  ngDoCheck() {
      if (!this.objPaginar && this.processo.contratos) {
        const x = {
          first: 0,
          rows: this.itensDisplay
        }
        this.paginar(x)
    }
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

  salvarContrato(form) {
    const ret = this.validador.validaCpfCnpj(this.entidade.cnpj_fornecedor)
    
    if (!ret) {
      this.mensagem = 'CPF/CNPJ inválido!';
      //this.atualizou = true;
      this.clearAllMessages()
      this.addMessageError(this.mensagem)
      this.tipoRetorno = 'error'
      return
    } else {
      this.mensagem = '';
      this.atualizou = false;
      this.tipoRetorno = 'success'
    }
    this.entidade.cnpj_fornecedor = this.entidade.cnpj_fornecedor.replace(/\D/g, '')
    this.loadService.show()
    if (this.labelIncAlt === 'INCLUIR') {
       
        this.service.incluirContrato(this.processo.id,this.entidade).subscribe( response => {
          //this.atualizou = true;
          this.processoRefresh();
          this.mensagem = 'Contrato incluido com sucesso';
          this.limpaTela(form)
          
          this.loadService.hide()
          this.clearAllMessages()
          this.addMessageSuccess(this.mensagem);
        }, error => {
            this.loadService.hide()
            this.tipoRetorno = 'error'
            if (error.error.mensagem) {
              this.mensagem = error.error.mensagem;
            } else {
                this.mensagem = 'Situação inesperada: ' + error.message;
            }
            
            //this.atualizou = true;
            this.clearAllMessages()
            this.addMessageError(this.mensagem)
          
        });
    } else {
        const contratoPatch = new PaeContratoPatch
        contratoPatch.id_processo_vinculado = this.processo.id
        contratoPatch.numero_contrato = this.entidade.numero_contrato
        contratoPatch.ano_contrato = this.entidade.ano_contrato
        contratoPatch.descricao_contrato = this.entidade.descricao_contrato
        contratoPatch.cnpj_fornecedor = this.entidade.cnpj_fornecedor.replace(/\D/g, '')
        contratoPatch.unidade_operacional = this.entidade.unidade_operacional
        
        this.service.alterarContrato(this.entidade.id,contratoPatch).subscribe( response => {
          //this.atualizou = true;
          if (!this.consultaPorContrato) {
            this.processoRefresh();
          } else {
              this.atualizarContratoGrid();
          }
          this.mensagem = 'Contrato alterado com sucesso';
          this.limpaTela(form)
          this.loadService.hide()
          this.clearAllMessages()
          this.addMessageSuccess(this.mensagem)
        }, error => {
            this.loadService.hide()
            this.tipoRetorno = 'error'
            if (error.error.mensagem) {
              this.mensagem = error.error.mensagem;
            } else {
                this.mensagem = 'Situação inesperada: ' + error.message;
            }
            
            //this.atualizou = true;
            this.clearAllMessages()
            this.addMessageError(this.mensagem)
          
        });
    }
  }

  atualizarContratoGrid() {
    this.contratoSelecionado.numero_contrato = this.entidade.numero_contrato
    this.contratoSelecionado.ano_contrato = this.entidade.ano_contrato
    this.contratoSelecionado.cnpj_fornecedor = this.entidade.cnpj_fornecedor
    this.contratoSelecionado.descricao_contrato = this.entidade.descricao_contrato
    this.contratoSelecionado.cpf_fornecedor = this.entidade.cpf_fornecedor
  }

  limpaTela(form) {
    
    this.incluirEntidade = false;
    this.entidade = new PaeContratoPost;
    this.sucessoConsulta = false;
    if (this.tbv) {
      this.tbv.activeIndex = 0;
    }
    this.exibirContratos();
    if (form) {
      form.control.markAsPristine();
      form.control.markAsUntouched();
    }
  }
  processoRefresh() {
    this.objPaginar = null
    this.infoRegistros = ''
    this.service.obterProcessoPorId(this.processo.id).subscribe( dados => {
      this.processo = dados;
      const x = {
        first: 0,
        rows: this.processo.contratos.length
      }
      this.paginar(x)
    });
  }
  consultar() {
    
    this.loadService.show()
    this.service.obterContratoPorId(this.contratoSelecionado.id).subscribe( dados => {
      this.entidade = dados;
      this.contratoSelecionado = dados;
      
      if (this.contratoSelecionado.cpf_fornecedor != null) {
        this.entidade.cnpj_fornecedor = this.contratoSelecionado.cpf_fornecedor;
      }
      if (this.contratoSelecionado.cnpj_fornecedor != null) {
        this.entidade.cnpj_fornecedor = this.contratoSelecionado.cnpj_fornecedor;
      }
      this.loadService.hide()
    }, error => {
        this.loadService.hide()
        this.tipoRetorno = 'error'
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem;
        } else {
            this.mensagem = 'Situação inesperada: ' + error.message;
        }

        //this.atualizou = true;
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
    });

    this.labelIncAlt = 'ALTERAR'
    this.sucessoConsulta = true;
    this.exibirPesquisa = false;
  }
  
  exibirContratos() {
    this.mostrarContratos = true;
    this.mostrarDados = false;
    if (this.tbv) {
      this.tbv.activeIndex = 0
    }
    this.larguraTabela = 'col-md-6'
  }

  exibirDados() {
    this.mostrarContratos = false;
    this.mostrarDados = true;
    this.larguraTabela = 'col-md-12'
    this.limparApensos = true;
  }

  exibirDocumentos() {
    this.mostrarContratos = false;
    this.mostrarDados = true;
    this.larguraTabela = 'col-md-12'
    this.limparApensos = true;
    
  }

  cancelarOperacao(form) {
    this.entidade = new PaeContratoPost;
    this.exibirContratos();
    this.labelIncAlt = 'INCLUIR'
    this.contratoSelecionado = new PaeContratoPost
    this.sucessoConsulta = false;
    this.mensagem = ''
    this.tipoRetorno = 'success'
    this.atualizou = false
    if (form) {
      form.control.markAsPristine();
      form.control.markAsUntouched();
    }
    this.clearAllMessages();
  }

  novoContrato() {
    this.entidade = new PaeContratoPost;
    this.labelIncAlt = 'INCLUIR'
    this.exibirDados()
  }

  onRowSelect(event) {
    this.atualizou = false;
    this.consultar();
  }
  pesquisarContrato() {
    if (this.numero_contrato == null || this.numero_contrato === undefined || this.numero_contrato === '' ||
      this.ano_contrato == null || this.ano_contrato === undefined || this.ano_contrato === '') {
        this.mensagem = 'Informe o número e o ano do contrato!';
        //this.atualizou = true;
        this.tipoRetorno = 'error'
        this.contratoNaoLocalizado = false;
        this.clearAllMessages()
        this.addMessageWarning(this.mensagem)
        return;
    }
    const contratoAno = this.numero_contrato + ';' + this.ano_contrato;
    this.buscouPorContrato(contratoAno);
    
  }

  buscouPorContrato(contratoAno: string) {
    this.pesquisouContrato.emit(contratoAno);
  }

  mudouAba(event) {

    if (event.index === 1) {
      this.exibirDados();
      this.limparApensos = true;
    } else {
        if (event.index === 0) {
          this.exibirContratos();
        } else {
          if (event.index === 2) {
            this.exibirDocumentos();
          }
        }
    }
  }
  alterouCpfCnpj(event) {
   this.entidade.cnpj_fornecedor = event
  }

  exportarPdf() {
    this.loadService.show()
    this.service.exportarContrato(this.entidade.id, this.tipoExportaSelecionado).subscribe( dados => {
      
      const dlnk:any  = document.querySelector('#dwnldLnk');
      const blob = new Blob([dados], {'type':'application/zip'});
      dlnk.href = URL.createObjectURL(blob);
      dlnk.download = 'Contrato.' + this.entidade.numero_contrato + '.'+ this.entidade.ano_contrato + '.zip'
      dlnk.click();
      this.loadService.hide()
     }, error => {
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem;
        } else {
            this.mensagem = 'Situação inesperada: ' + error.message;
        }
       
        this.tipoRetorno = 'error'
        //this.atualizou = true 
        this.loadService.hide()
        this.clearAllMessages()
        this.addMessageError(this.mensagem)
        
     });
  }

  mudouGestorOperacional(event) {
    this.entidade.unidade_operacional = event
  }

  mudouUnidadeContratacao(event) {
    this.entidade.unidade_contratacao = event
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
    this.infoRegistros  = this.util.paginar(event, this.processo.contratos.length)
  }
}
