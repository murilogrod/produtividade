

import { Component, OnInit } from '@angular/core';

import { SelectItem } from 'primeng/primeng';
import { ProdutoDocumentoService } from './produto-documento.service';
import { ConfirmationService} from 'primeng/primeng';
import { CrudProdutoDocumento } from '../model/crud-produto-documento';
import { LoaderService } from '../services';

declare var $: any;

@Component({
  selector: 'crud-produto-documento',
  templateUrl: './produto-documento.component.html',
  styleUrls: ['./produto-documento.component.css']
})
export class ProdutoDocumentoComponent implements OnInit {
  entidade:  CrudProdutoDocumento; 
  itens: any = [];
  produtos: SelectItem[];
  processos: SelectItem[];
  tipoDocumento: SelectItem[];
  
  produtoSelecionado;

  processoSelecionado;

  tipoDocumentoSelecionado;

  tituloModal: String = "Associação de Produto/Processo/Tipo de Documento"
  
  constructor(private service: ProdutoDocumentoService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

  }

  ngOnInit() {
    this.entidade = new CrudProdutoDocumento();
    this.service.get().subscribe(dados => {
      this.itens = dados.json();
    },
    () => {
      this.loadService.hide();
    });    
    this.service.getProduto().subscribe(dados => {
      this.produtos = dados.json();
      this.produtoSelecionado = this.produtos[0].value
      this.entidade.nu_produto = this.produtoSelecionado
    },
    () => {
      this.loadService.hide();
    });    
    this.service.getProcesso().subscribe(dados => {
      this.processos = dados.json();
      this.processoSelecionado = this.processos[0].value
      this.entidade.nu_processo = this.processoSelecionado
    },
    () => {
      this.loadService.hide();
    }); 
    this.service.getTipoDocumento().subscribe(dados => {
      this.tipoDocumento = dados.json();
      this.tipoDocumentoSelecionado = this.tipoDocumento[0].value
      this.entidade.nu_tipo_documento = this.tipoDocumentoSelecionado
      console.log(this.entidade)
    },
    () => {
      this.loadService.hide();
    });   

  }

  limpaTela(form) {
    form.control.markAsPristine();
    form.control.markAsUntouched();
    this.entidade = new CrudProdutoDocumento();
    this.tituloModal = "Associação de Produto/Processo/Tipo de Documento";
    this.produtoSelecionado = this.produtos[0].value
    this.processoSelecionado = this.processos[0].value
    this.tipoDocumentoSelecionado = this.tipoDocumento[0].value

  }
  salvar(form) {
    this.entidade.nu_produto = this.produtoSelecionado
    this.entidade.nu_processo = this.processoSelecionado
    this.entidade.nu_tipo_documento = this.tipoDocumentoSelecionado
    console.log(this.entidade);
    if (this.entidade.nu_produto_documento == null) {
      this.service.save(this.entidade).subscribe( () => {
          $('#incluir').modal('toggle')
          this.service.get().subscribe(dados => {
            this.itens = dados.json();
        },
        () => {
          this.loadService.hide();
        });
      },
      () => {
        this.loadService.hide();
      });
  
    }
    else {
      this.service.update(this.entidade).subscribe( () => {
        this.limpaTela(form)  
        $('#incluir').modal('toggle')  
        this.service.get().subscribe(dados => {
            this.itens = dados.json();
        },
        () => {
          this.loadService.hide();
        });        
      },
      () => {
        this.loadService.hide();
      });
    }
  }

  alterar(dados) {
    this.tituloModal = "Alteração de Produto/Processo/Tipo de Documento";
    this.entidade = Object.assign({}, dados);
    this.produtoSelecionado = dados.nu_produto
    this.processoSelecionado = dados.nu_processo
    this.tipoDocumentoSelecionado = dados.nu_tipo_documento
  }

  excluir(dados, form) {
  
    this.confirmationService.confirm({
      message: 'Você confirma a exclusão deste Produto/Processo/Tipo de Documento?',
      accept: () => {
        this.service.delete(dados.nu_produto_documento).subscribe( () => {
          this.service.get().subscribe(dados => {
            this.itens = dados.json();
          },
          () => {
            this.loadService.hide();
          });
        },
        () => {
          this.loadService.hide();
        });
      }
   });
  
  }
  abrirModal(form) {
    this.limpaTela(form);
  }
  
  mudouProduto() {
    this.entidade.nu_produto = this.produtoSelecionado
    console.log(this.entidade.nu_produto)
  }

  mudouProcesso() {
    this.entidade.nu_processo = this.processoSelecionado
    console.log(this.entidade.nu_processo)
  }

  mudouTipoDocumento() {
    this.entidade.nu_tipo_documento = this.tipoDocumentoSelecionado
    console.log(this.entidade.nu_tipo_documento)
  }

}
