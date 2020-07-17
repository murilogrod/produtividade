import { Component, OnInit } from '@angular/core';
import {  SelectItem } from 'primeng/primeng';
import { GarantiaProdutoService } from './garantia-produto.service';
import {ConfirmationService} from 'primeng/primeng';
import { CrudGarantiaProduto } from '../model/crud-garantia-produto';
import { LoaderService } from '../services';


declare var $: any;

@Component({
  selector: 'crud-garantia-produto',
  templateUrl: './garantia-produto.component.html',
  styleUrls: ['./garantia-produto.component.css']
})
export class GarantiaProdutoComponent implements OnInit {
  entidade:  CrudGarantiaProduto; 
  itens: any = [];
  produtos: SelectItem[];
  garantias: SelectItem[];
  produtoSelecionado;
  garantiaSelecionado;

  tituloModal: String = "Associação de Produto/Garantia"
  
  constructor(private service: GarantiaProdutoService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

  }

  ngOnInit() {
    this.entidade = new CrudGarantiaProduto();
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
    this.service.getGarantia().subscribe(dados => {
      this.garantias = dados.json();
      this.garantiaSelecionado = this.garantias[0].value
      this.entidade.nu_garantia = this.garantiaSelecionado
    },
    () => {
      this.loadService.hide();
    });  
  }

  limpaTela(form) {
    form.control.markAsPristine();
    form.control.markAsUntouched();
    this.entidade = new CrudGarantiaProduto();
    this.tituloModal = "Associação de Garantia/Produto";
    this.produtoSelecionado = this.produtos[0].value
    this.garantiaSelecionado = this.garantias[0].value

  }
  salvar(form) {
    this.entidade.nu_produto = this.produtoSelecionado
    this.entidade.nu_garantia = this.garantiaSelecionado
    console.log(this.entidade);
    if (this.entidade.nu_produto != null && this.entidade.nu_garantia != null) {
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
  }

  excluir(dados, form) {
  
    this.confirmationService.confirm({
      message: 'Você confirma a exclusão deste Produto/Garantia?',
      accept: () => {
        this.service.delete(dados.nu_produto,dados.nu_garantia).subscribe( () => {
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
  }

  mudouGarantia() {
    this.entidade.nu_garantia = this.garantiaSelecionado
  }
}
