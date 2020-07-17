import {
  Component,
  ViewEncapsulation,
  Input,
  ViewChild,
  ElementRef
} from '@angular/core';

import { ApplicationService, AlertMessageService, LoaderService } from './../../../services/index';
import { PaeApensoPost } from './../../model/pae-apenso-post';
import { PaeApensoPatch } from './../../model/pae-apenso-patch';
import { SelectItem, DataTable } from 'primeng/primeng';
import { PaeApensosService } from './pae-apensos.service';
import { PaeContratacao } from './../../model/pae-contratacao';
import { PaeUtils } from './../../utils/utilidades/pae-utils';

@Component({
  selector: 'pae-apensos',
  templateUrl: './pae-apensos.component.html',
  styleUrls: ['./pae-apensos.component.css'],
  encapsulation: ViewEncapsulation.None
  
})

export class PaeApensosComponent extends AlertMessageService {
  @Input() processo: PaeContratacao;
  @Input() exibirConsultaApensos: boolean;
  @ViewChild('f') frm: ElementRef;
  @ViewChild('tbv') tbv: any;
  @ViewChild('dt') datatable: DataTable;
  
  URLImage: string;

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
  docPara = 'APSP';
  tipoRetorno = 'Sucess';
  validador: PaeUtils = new PaeUtils;
  mensSiclg = false;
  itensDisplay = 10;
  exibirSiclg = false;
  exibirBotao = false;
  apensoAnexo = true;
  cpf_cnpj = '';
  perfilExporta = false;
  perfilEscrita = false;
  perfilLeitura = false;
  exibirFiltro = 'block';
  labelFiltro = 'OCULTAR LISTA';
  objPaginar;
  infoRegistros = ''
  constructor(
    private applicationService: ApplicationService,
    private util: PaeUtils,
    private service: PaeApensosService,
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
    this.entidade.cnpj_fornecedor = ''
    this.tipoApensos = [ 
      {'label': 'Anexo', 'value': 'AX'},
      {'label': 'Demandas de Orgãos de Controle', 'value': 'OC'},
      {'label': 'Penalidade Processo', 'value': 'PP'}
    ]
    
    this.tipoApensoSelecionado = this.tipoApensos[0].value ;
    this.entidade.tipo_apenso = this.tipoApensoSelecionado;
    this.classifica();
   }      
  
  // tslint:disable-next-line:use-life-cycle-interface
  ngOnChanges() {
   
    if (this.processo === undefined || this.processo.id == null) {
     this.entidade = new PaeApensoPost 
     this.apensoSelecionado = {}
     this.sucessoConsulta = false;
     this.exibirPesquisa = true;
     this.labelIncAlt = 'INCLUIR'
     this.exibirApensos();
     this.atualizou = false;
     this.exibirBotao = false;
     this.limpaTela(this.frm)
     this.infoRegistros = ''
      this.objPaginar = null
    } else {
      this.classifica();
      this.testaTipoApenso();
      this.exibirBotao = true;
      this.infoRegistros = ''
      this.objPaginar = null
    }
  }

    // tslint:disable-next-line:use-life-cycle-interface
    ngDoCheck() {
      if (!this.objPaginar && this.processo.apensos) {
        const x = {
          first: 0,
          rows: this.itensDisplay
        }
        this.paginar(x)
      }
    }

  testaTipoApenso() {
    if (this.processo.apensos === undefined || this.processo.apensos === null) {
      return;
    }
    
    for (let i = 0; i < this.processo.apensos.length; i++) {
      if (this.processo.apensos[i].tipo_apenso.sigla === 'AX') {
        this.processo.apensos[i].descricao_apenso = this.processo.apensos[i].titulo_apenso;
      }
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

  novaPenalidade() {
    this.incluirApenso = true;
    this.incluiu = false;
    this.atualizou = false;

  }

  salvarApenso(form) {

    this.entidade.tipo_apenso = this.tipoApensoSelecionado
 
    if (! this.consisteDados()) {
      return;
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
       
        this.service.incluirApenso(this.processo.id,this.entidade).subscribe( () => {
          //this.atualizou = true;
          this.processoRefresh();
          this.tipoRetorno = 'success'
          this.mensagem = 'Apenso incluido com sucesso';
          this.limpaTela(form);
          this.loadService.hide()
          this.clearAllMessages()
          this.addMessageSuccess(this.mensagem)
        }, error => {
          this.loadService.hide()
          if (error.error.mensagem) {
            this.mensagem = error.error.mensagem
          } else if (error.message) {
              this.mensagem = 'Situação inesperada: ' + error.message;
          }
          
            this.tipoRetorno = 'error'
          //this.atualizou = true;
          this.clearAllMessages();
          this.addMessageError(this.mensagem)

        });
    } else {
        const apensoPatch = new PaeApensoPatch
        
        if (this.apensoSelecionado.tipo_apenso = 'AX') {
          apensoPatch.titulo_apenso = this.apensoSelecionado.titulo_apenso
        } 
        apensoPatch.id_processo_vinculado = this.processo.id
        apensoPatch.tipo_apenso = this.tipoApensoSelecionado
        apensoPatch.descricao_apenso = this.entidade.descricao_apenso
        if (this.entidade.cnpj_fornecedor !== null && this.entidade.cnpj_fornecedor !== undefined && this.entidade.cnpj_fornecedor !== '') {
          apensoPatch.cnpj_fornecedor = this.entidade.cnpj_fornecedor.replace(/\D/g, '')
        }
        apensoPatch.protocolo_siclg = this.entidade.protocolo_siclg
        
        this.service.alterarApenso(this.entidade.id,apensoPatch).subscribe( () => {
          //this.atualizou = true;
          this.tipoRetorno = 'success'
          this.processoRefresh();
          this.mensagem = 'Apenso alterado com sucesso';
          this.limpaTela(form);
          this.loadService.hide();
          this.clearAllMessages();
          this.addMessageSuccess(this.mensagem)

        }, error => {
            this.loadService.hide();
            this.tipoRetorno = 'error'
            if (error.error.mensagem) {
              this.mensagem = error.error.mensagem
            } else if (error.message) {
                this.mensagem = 'Situação inesperada: ' + error.message;
              }
            
            //this.atualizou = true;
            this.clearAllMessages();
            this.addMessageError(this.mensagem)
  

        });
      }
  }

  consisteDados() {
    let ret = true;
    if (this.tipoApensoSelecionado !== 'AX') {
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
      this.clearAllMessages();
      this.addMessageError(this.mensagem)

      return false
    }
    if ((this.entidade.protocolo_siclg == null || this.entidade.protocolo_siclg === '') &&
         (this.tipoApensoSelecionado === 'PP')) {
      this.mensagem = 'Protocolo SICLG não informado';
      //this.atualizou = true;
      this.tipoRetorno = 'error'
      this.clearAllMessages();
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
    this.tbv.activeIndex = 0
    form.control.markAsPristine();
    form.control.markAsUntouched();

  }
  processoRefresh() {
    this.objPaginar = null;
    this.service.obterProcessoPorId(this.processo.id).subscribe( dados => {
      this.processo = dados;
      this.testaTipoApenso();
      const x = {
        first: 0,
        rows: this.processo.apensos.length
      }
      this.paginar(x)

    });
  }

  consultar() {
    this.loadService.show();
    this.service.obterApensoPorId(this.apensoSelecionado.id).subscribe( dados => {
      this.loadService.hide()
      this.entidade = dados;
      this.apensoSelecionado = dados;
      
      if (this.entidade.cnpj_fornecedor) {
        this.cpf_cnpj = this.entidade.cnpj_fornecedor
      } else {
        if (this.entidade.cpf_fornecedor) {
          this.cpf_cnpj = this.entidade.cpf_fornecedor
        }
      }

      this.tipoApensoSelecionado = this.apensoSelecionado.tipo_apenso.sigla
     
      if (this.tipoApensoSelecionado === 'PP') {
        this.mensSiclg = true
        this.exibirSiclg = true
        this.apensoAnexo = false
      } else {
        if (this.tipoApensoSelecionado === 'OC') {
          this.mensSiclg = false
          this.exibirSiclg = false
          this.apensoAnexo = false
        } else {
          if (this.tipoApensoSelecionado === 'AX') {
            this.mensSiclg = false
            this.exibirSiclg = false
            this.apensoAnexo = true
            this.entidade.descricao_apenso = this.entidade.titulo_apenso
    
          }
        }
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
    //this.incluiu = false;
    //this.atualizou = false;
  }

  exibirDocumentos() {
    this.mostrarApensos = false;
    this.mostrarDados = true;
    this.larguraTabela = 'col-md-12'
  }

  cancelarOperacao(form) {
    this.entidade = new PaeApensoPost;
    this.apensoSelecionado = this.entidade
    this.exibirApensos();
    this.labelIncAlt = 'INCLUIR'
    this.sucessoConsulta = false;
    this.mensagem = ''
    this.tipoRetorno = 'success'
    this.atualizou = false
    form.control.markAsPristine();
    form.control.markAsUntouched();
    this.clearAllMessages();
  }

  novoApenso() {
    this.entidade = new PaeApensoPost;
    this.labelIncAlt = 'INCLUIR'
    this.cpf_cnpj = '';
    this.atualizou = false
    this.exibirDados()
  }
  onRowSelect() {
    this.atualizou = false;
    this.consultar();
 }

  mudouTipoApenso() {
    this.entidade.tipo_apenso = this.tipoApensoSelecionado
    if (this.tipoApensoSelecionado === 'PP') {
      this.mensSiclg = true
      this.exibirSiclg = true
      this.apensoAnexo = false
      this.entidade.titulo_apenso = null
    } else {
      if (this.tipoApensoSelecionado === 'OC') {
        this.mensSiclg = false
        this.exibirSiclg = false
        this.entidade.protocolo_siclg = null;
        this.entidade.titulo_apenso = null
        this.apensoAnexo = false
      } else {
        if (this.tipoApensoSelecionado === 'AX') {
          this.mensSiclg = false
          this.exibirSiclg = false
          this.entidade.protocolo_siclg = null;
          this.entidade.titulo_apenso = null
          this.entidade.cnpj_fornecedor = ''
          this.apensoAnexo = true
  
        }
      }
    }
  }
  
  mudouAba(event) {

    if (event.index === 0) {
      this.exibirApensos();
    } else {
        if (event.index === 1) {
          this.exibirDocumentos();
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
      dlnk.download = 'Apenso.Processo.'+ this.entidade.processo_administrativo.numero_processo + '.' + this.entidade.processo_administrativo.ano_processo + '.' + this.entidade.cnpj_fornecedor + '.zip'
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
        this.clearAllMessages();
        this.addMessageError(this.mensagem)

        
     });
  }  
 
  classifica() {
    
    if (this.processo.apensos !== undefined && this.processo.apensos !== []) {
      let x = []
      x = this.processo.apensos

      x.sort((n1 , n2 ) : number => {
        if (n1.id < n2.id) return -1;
        if (n1.id > n2.id) return 1;
        return 0;
      });
      this.processo.apensos = x;
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
    this.infoRegistros  = this.util.paginar(event, this.processo.apensos.length)
  }
}
