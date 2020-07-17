import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { AbaProdutoComponentPresenter } from '../presenter/aba-produtos.component.presenter';
import { AbaProduto } from '../model/aba-produto.model';

@Component({
  selector: 'aba-produtos',
  templateUrl: './aba-produtos.component.html',
  styleUrls: ['./aba-produtos.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AbaProdutosComponent implements OnInit {
  @Input() produtos;
  @Input() cliente;

  abaProdutoComponentPresenter: AbaProdutoComponentPresenter;

  constructor(abaProdutoComponentPresenter: AbaProdutoComponentPresenter) {
    this.abaProdutoComponentPresenter = abaProdutoComponentPresenter;
    this.abaProdutoComponentPresenter.abaProduto = new AbaProduto();
  }

  ngOnInit() {
    this.abaProdutoComponentPresenter.initAbaProdutos(this.produtos);
  }

  expandirAllTr() {
    this.abaProdutoComponentPresenter.expandirAllTr();
  }

  expTodosOuPrimeiro(idxZero: number) {
    return this.abaProdutoComponentPresenter.expTodosOuPrimeiro(idxZero);
  }

  mostraIconeJuridco(texto: string) {
    return texto == "PESSOA JURÍDICA"
  }

  mostraIconeFisica(texto: string) {
    return texto == "PESSOA FÍSICA"
  }

  habilitarExpandirTodos(processPatriarca: any) {
    return this.abaProdutoComponentPresenter.enabledAll(processPatriarca)
  }

}
