import { Component, OnInit, ViewEncapsulation, AfterContentInit } from '@angular/core';
import { AbaVerificacaoComponentPresenter } from '../presenter/aba-verificacao.component.presenter';
import { AbaVerificacao } from '../model/aba-verificacao.model';
import { InputOutputAbaVerificacaoService } from '../input-output-service/input-output-aba-verificacao.service';

declare var $: any;

@Component({
  selector: 'aba-verificacao',
  templateUrl: './aba-verificacao.component.html',
  styleUrls: ['./aba-verificacao.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AbaVerificacaoComponent extends InputOutputAbaVerificacaoService implements OnInit, AfterContentInit{

  abaVerificacaoComponentPresenter: AbaVerificacaoComponentPresenter
  
  constructor(abaVerificacaoComponentPresenter: AbaVerificacaoComponentPresenter) {
    super();
    this.abaVerificacaoComponentPresenter = abaVerificacaoComponentPresenter;
    this.abaVerificacaoComponentPresenter.abaVerificacao = new AbaVerificacao();
  }

  ngOnInit() {
    this.abaVerificacaoComponentPresenter.initAbaVerificacao(this);
  }

  ngAfterContentInit() {
    this.filtrarPorFase();
  }

  filtrarGlobal() {
    this.abaVerificacaoComponentPresenter.filtrarGlobal(this);
  }

  filtrarPorFase() {
    this.abaVerificacaoComponentPresenter.filtrarPorFase(this);
  }

  contadorPosicao() {
    this.abaVerificacaoComponentPresenter.abaVerificacao.contadorPosicacao++;
  }

}
