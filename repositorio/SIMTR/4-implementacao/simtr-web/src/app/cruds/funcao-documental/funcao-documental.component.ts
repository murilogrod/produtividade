import { Component, OnInit, ViewChild } from '@angular/core';
import { FuncaoDocumentalService } from './funcao-documental.service';
import { ConfirmationService, SelectItem } from 'primeng/primeng';
import { CrudFuncaoDocumental } from '../model/crud-funcao-documental';
import { SELECT_VALUE_ACCESSOR } from '../../../../node_modules/@angular/forms/src/directives/select_control_value_accessor';
import { LoaderService } from 'src/app/services';

declare var $: any;

@Component({
  selector: 'crud-funcao-documental',
  templateUrl: './funcao-documental.component.html',
  styleUrls: ['./funcao-documental.component.css']
})
export class FuncaoDocumentalComponent implements OnInit {
  @ViewChild('f') form: HTMLElement;
  entidade: CrudFuncaoDocumental; 
  public itens: any = [];
  public tituloModal: String = "Inclusão de Função Documental"
  public telaCadastro = false;
  private emModoEditar = false;
  public tiposDocumento: SelectItem[] = []
  private tiposDocumentoOriginal: SelectItem[] = []
  public tipoDocumentoSelecionado;
  private tiposDocumentoId = [];
  public tiposDocumentoView = []
  private tiposDocumentoParaInclusaoPatch: number[] = [];
  private tiposDocumentoParaExclusaoPatch: number[] = [];
  public naoPodeIncluirTipoDocumento = false;
  public usoDossieDigital = false;
  public usoPae = false;
  public usoApoioNegocio = false;
  public mensagem = '';
  public exibirMensagem = false;
  public tipoMensagem = 'info'
  public tipoIcone = 'info'

  constructor(private service: FuncaoDocumentalService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

   }

  ngOnInit() {
    this.entidade = new CrudFuncaoDocumental();
    this.service.get().subscribe(dados => {
      
      this.itens = dados
      
    },
    () => {
        this.loadService.hide();
    }); 

    this.service.getTipoDocumento().subscribe(dados => {
      
      if (dados) {
        for (let i = 0; i < dados.length; i++) {
          let x = {
            label: dados[i].nome_tipo_documento,
            value: dados[i].identificador_tipo_documento
          }
          this.tiposDocumento.push(x)
          this.tiposDocumentoId[x.value] = dados[i];
        }
        this.tiposDocumentoOriginal = this.tiposDocumento
        if (this.tiposDocumento.length > 0) {
          this.tipoDocumentoSelecionado = this.tiposDocumento[0].value
          this.classificaTipoDocs()
        }
      }      
    },
    () => {
        this.loadService.hide();
    });   

  }

  limpaTela(form) {
    form.control.markAsPristine();
    form.control.markAsUntouched();
    this.entidade = new CrudFuncaoDocumental();
    this.tituloModal = "Inclusão de Função Documental";
    this.naoPodeIncluirTipoDocumento = false
    this.tiposDocumento = this.tiposDocumentoOriginal
    this.tiposDocumentoView = []
    this.usoApoioNegocio = false;
    this.usoDossieDigital = false;
    this.usoPae = false
    //this.exibirMensagem = false
    //this.mensagem = ''
    //this.tipoIcone = 'info'
    //this.tipoMensagem = 'info'

  }
  salvar(form) {
    this.entidade.tipos_documento = this.tiposDocumentoView
    this.entidade.nome_funcao_documental = this.entidade.nome_funcao_documental.toUpperCase();
    this.entidade.indicador_apoio_negocio = this.usoApoioNegocio;
    this.entidade.indicador_dossie_digital = this.usoDossieDigital;
    this.entidade.indicador_processo_administrativo = this.usoPae;
    
    if (this.entidade.identificador_funcao_documental == null) { // Cenário de inclusão da função documental (POST)
      this.service.save(this.getFuncaoDocumentalParaPost()).subscribe( () => {

          this.limpaTela(form)  
          this.mensagem = 'Registro cadastrado com sucesso.'
          this.tipoIcone = 'info'
          this.tipoMensagem = 'info'
          this.exibirMensagem = true
          this.telaCadastro = false;
          this.emModoEditar = false;
          
          this.service.get().subscribe(dados => {
            this.itens = dados;
        });
      }, error => {

        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else {
          this.mensagem = error.message
        }
        
        this.tipoIcone = 'ban'
        this.tipoMensagem = 'error'
        this.exibirMensagem = true
        this.loadService.hide();
        throw error;
      }); 
  
    }
    else { // Cenário de alteração da função documental (PATCH)      
      this.service.update(this.entidade.identificador_funcao_documental, this.getFuncaoDocumentalParaPatch())
        .subscribe(() => {
          this.mensagem = 'Registro alterado com sucesso.'
          this.tipoIcone = 'info'
          this.tipoMensagem = 'info'
          this.exibirMensagem = true
          this.service.get().subscribe(dados => {
            
            this.itens = dados;
            this.cancelar(form);
        });
        
      }, error => {
        if (error.error.mensagem) {
          this.mensagem = error.error.mensagem
        } else {
          this.mensagem = error.message
        }        
        this.tipoIcone = 'ban'
        this.tipoMensagem = 'error'
        this.exibirMensagem = true
        this.loadService.hide();
        throw error;
      }); 
    }
  }

  async alterar(funcaoDocumental) {
    this.exibirMensagem = false
    this.mensagem = ''
    this.tipoIcone = 'info'
    this.tipoMensagem = 'info'
    this.tituloModal = "Alteração de Função Documental";
    this.service.getById(funcaoDocumental.identificador_funcao_documental).subscribe( resp => {
      
      this.entidade = resp
      
      this.setUsosForm()
      
      // Limpa as listas de inclusão e exclusão do PATCH
      this.tiposDocumentoParaInclusaoPatch = [];
      this.tiposDocumentoParaExclusaoPatch = [];

      if (this.entidade.tipos_documento) {
        this.preparaCombo()
        this.tiposDocumentoView = this.entidade.tipos_documento;
        this.classificaTipoDocView();
      } else {
        this.tiposDocumentoView = [];
      }
      
      if (this.tiposDocumento.length == 0) {
          this.naoPodeIncluirTipoDocumento = true
      }  else {
          this.naoPodeIncluirTipoDocumento = false
      }

      this.telaCadastro = true
      this.emModoEditar = true;
      },
      () => {
          this.loadService.hide();
      }); 
  }

  private preparaCombo() {
    
    this.tiposDocumento = this.tiposDocumentoOriginal
    for (let i = 0; i < this.entidade.tipos_documento.length; i++) {
      let x = {
        label: this.entidade.tipos_documento[i].nome_tipo_documento,
        value: this.entidade.tipos_documento[i].identificador_tipo_documento
      }
      let vet = this.tiposDocumento.filter(function(k, index, arr){
        
        return k.value != x.value  ;
      });
      this.tiposDocumento = vet
    }
    
  }


  excluir(dados, form) {
  
    this.confirmationService.confirm({
      message: 'Você confirma a exclusão deste Registro?',
      accept: () => {
        this.service.delete(dados.identificador_funcao_documental).subscribe( () => {
          this.exibirMensagem = true
          this.mensagem = 'Registro excluído com sucesso!'
          this.tipoIcone = 'info'
          this.tipoMensagem = 'info'
          this.service.get().subscribe(dados => {
            this.itens = dados;
          });
        }, error => {
            if (error.error.mensagem) {
              this.mensagem = error.error.mensagem
            } else {
              this.mensagem = error.message
            }
            
            this.tipoIcone = 'ban'
            this.tipoMensagem = 'error'
            this.exibirMensagem = true
            this.loadService.hide();
            throw error;
        }); 
      }
   });
  
  }

  abrirModal() {
    this.exibirMensagem = false
    this.mensagem = ''
    this.tipoIcone = 'info'
    this.tipoMensagem = 'info'
    this.telaCadastro = true;
    this.emModoEditar = false;
    this.entidade = {};
    this.naoPodeIncluirTipoDocumento = false
   
    
  }

  cancelar(f) {
    this.limpaTela(f)
    this.telaCadastro = false;
    this.emModoEditar = false;
  }

  mudouTipoDocumento() {
    
  }

  excluirTipoDoc(tipo) {
    let idTipoDocumentoParaExclusao: number = tipo.identificador_tipo_documento;
    this.tiposDocumentoView = this.excluiDaLista(tipo,this.tiposDocumentoView)
    let x = {
      value: tipo.identificador_tipo_documento,
      label: tipo.nome_tipo_documento
    }
    this.tiposDocumento.push(x)
    this.tipoDocumentoSelecionado = this.tiposDocumento[0].value
    this.naoPodeIncluirTipoDocumento = false
    this.classificaTipoDocs();    
    if (this.tiposDocumentoParaInclusaoPatch.includes(idTipoDocumentoParaExclusao)) {
      // Se o tipo de documento estava na lista para inclusão e o usuário o remove, significa apenas que tenho que remove-lo da lista de inclusão.
      this.tiposDocumentoParaInclusaoPatch = this.tiposDocumentoParaInclusaoPatch.filter(idTipoDocumento => idTipoDocumento != idTipoDocumentoParaExclusao);
    } else {
      // Senão, basta adicionar o tipo de documento na lista de exclusão.
      this.tiposDocumentoParaExclusaoPatch.push(idTipoDocumentoParaExclusao);
    }
  }

  excluiDaLista(x, array) {
    
    let vet = array.filter(function(k, index, arr){
     
      if (k.identificador_tipo_documento) {
          return k.identificador_tipo_documento != x.identificador_tipo_documento  ;
      } else if (k.value) {
          return k.value != x.value  ;
      }
    });
    
    return vet
  }

  async incluirDocumento() {
    let idTipoDocumentoSelecionado: number = this.tipoDocumentoSelecionado;
    // Inclui o tipo de documento na lista de vinculados
    let tipoDocumentoParaInclusao = {
      identificador_tipo_documento: this.tipoDocumentoSelecionado,
      nome_tipo_documento: this.tiposDocumentoId[this.tipoDocumentoSelecionado].nome_tipo_documento
    }
    this.tiposDocumentoView = [...this.tiposDocumentoView, tipoDocumentoParaInclusao]
    await this.classificaTipoDocView()
    if (this.tiposDocumentoParaExclusaoPatch.includes(idTipoDocumentoSelecionado)) {
      // Se o tipo de documento estava na lista para exclusão e o usuário o adiciona, significa apenas que tenho que remove-lo da lista de exclusão.
      this.tiposDocumentoParaExclusaoPatch = this.tiposDocumentoParaExclusaoPatch.filter(idTipoDocumento => idTipoDocumento != idTipoDocumentoSelecionado);
    } else {
      // Senão, basta adicionar o tipo de documento na lista de inclusão.
      this.tiposDocumentoParaInclusaoPatch.push(idTipoDocumentoSelecionado);
    }

    // Remove o tipo de documento da lista de seleção
    let tipoDocumentoARemover = {
      value: this.tipoDocumentoSelecionado,
      label: this.tiposDocumentoId[this.tipoDocumentoSelecionado].nome_tipo_documento
    }
    this.tiposDocumento = this.excluiDaLista(tipoDocumentoARemover,this.tiposDocumento)
    if (this.tiposDocumento.length > 0) {
      this.tipoDocumentoSelecionado = this.tiposDocumento[0].value
    } else  {
        this.naoPodeIncluirTipoDocumento = true
    }
  }

  classificaItens() {
    
    if (this.itens !== undefined && this.itens !== []) {
      this.itens.sort((n1 , n2 ) : number => {
        if (n1.nome_funcao_documental < n2.nome_funcao_documental) return -1;
        if (n1.nome_funcao_documental > n2.nome_funcao_documental) return 1;
        return 0;
      });
    }

  }

  classificaTipoDocs() {
    
    if (this.tiposDocumento !== undefined && this.tiposDocumento !== []) {
      this.tiposDocumento.sort((n1 , n2 ) : number => {
        if (n1.label < n2.label) return -1;
        if (n1.label > n2.label) return 1;
        return 0;
      });
    }

  }

  async classificaTipoDocView() {
    
    if (this.tiposDocumentoView !== undefined && this.tiposDocumentoView !== []) {
      this.tiposDocumentoView.sort((n1 , n2 ) : number => {
        if (n1.nome_funcao_documental < n2.nome_funcao_documental) return -1;
        if (n1.nome_funcao_documental > n2.nome_funcao_documental) return 1;
        return 0;
      });
      //this.tiposDocumentoView = [...this.tiposDocumentoView,this.tiposDocumentoView]
    }

  }

  setUsosForm(){
    this.usoApoioNegocio = this.entidade.indicador_apoio_negocio
    this.usoDossieDigital = this.entidade.indicador_dossie_digital
    this.usoPae = this.entidade.indicador_processo_administrativo
  }

  getFuncaoDocumentalParaPost(): CrudFuncaoDocumental {
    return {
      nome_funcao_documental: this.entidade.nome_funcao_documental,
      indicador_processo_administrativo: this.entidade.indicador_processo_administrativo,
      indicador_dossie_digital: this.entidade.indicador_dossie_digital,
      indicador_apoio_negocio: this.entidade.indicador_apoio_negocio,
      tipos_documento_inclusao_vinculo: this.entidade.tipos_documento.map(tipoDocumento => parseInt(tipoDocumento.identificador_tipo_documento))
    };
  }

  getFuncaoDocumentalParaPatch(): CrudFuncaoDocumental {
    return {
      nome_funcao_documental: this.entidade.nome_funcao_documental,
      indicador_processo_administrativo: this.entidade.indicador_processo_administrativo,
      indicador_dossie_digital: this.entidade.indicador_dossie_digital,
      indicador_apoio_negocio: this.entidade.indicador_apoio_negocio,
      tipos_documento_inclusao_vinculo: this.tiposDocumentoParaInclusaoPatch,
      tipos_documento_exclusao_vinculo: this.tiposDocumentoParaExclusaoPatch
    };
  }
  
}
