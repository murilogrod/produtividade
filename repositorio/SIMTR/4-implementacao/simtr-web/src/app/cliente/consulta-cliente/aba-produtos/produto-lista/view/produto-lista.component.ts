import { Component, OnInit, Input } from '@angular/core';
import { AlertMessageService } from '../../../../../services';
import { ProdutoListaComponentPresenter } from '../presenter/produto-lista.component.presenter';
import { ProdutoLista } from '../model/produto-lista.model';
import { SortEvent } from 'primeng/primeng';

@Component({
  selector: 'produto-lista',
  templateUrl: './produto-lista.component.html',
  styleUrls: ['./produto-lista.component.css']
})
export class ProdutoListaComponent extends AlertMessageService implements OnInit {
  @Input() lista: any[];

  produtoListaComponentPresenter: ProdutoListaComponentPresenter;

  constructor(
    produtoListaComponentPresenter: ProdutoListaComponentPresenter
  ) {
    super();
    this.produtoListaComponentPresenter = produtoListaComponentPresenter;
    this.produtoListaComponentPresenter.produtoLista = new ProdutoLista();
  }

  ngOnInit() {
    this.produtoListaComponentPresenter.initConfigListaProdutos(this.lista);
  }

  formatarHintProdutos(produtos: any[]): string {
    return this.produtoListaComponentPresenter.formatProdutos(produtos);
  }

  formatarData(value: string): string {
    return this.produtoListaComponentPresenter.formatDateViewProdutos(value);
  }

  goProduto(id, opcao) {
    this.produtoListaComponentPresenter.navigateManutencaoDossie(id, opcao);
  }

  selecionaDossieProduto(idDossieProduto) {
    this.produtoListaComponentPresenter.selectDossieProduto(this, idDossieProduto);

  }

  habilitaBotaoParaTratamento(produto: any) {
    return this.produtoListaComponentPresenter.showButtonTratamento(produto);
  }

  realizarFiltroProdutos(input: any, dataProdutos: any) {
    this.produtoListaComponentPresenter.filterProdutos(input, dataProdutos);
  }

  realizarOrdenacao(event: SortEvent) {
    this.produtoListaComponentPresenter.customSort(event);
  }

  handlleMessagesError(messages) {
    this.messagesError = messages;
  }

  verificarUnidadeAutorizada(dossie: any) {
		return this.produtoListaComponentPresenter.checkUnidadeAutorizada(dossie);
  }
  
}
