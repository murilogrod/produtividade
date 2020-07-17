import { Component, OnInit } from '@angular/core';
import { PesquisaCanalPresenter } from '../presenter/pesquisa-canal.component.presenter';
import { PesquisaCanal } from "../model/pesquisa-canal.model";
import { SortEvent } from 'primeng/primeng';
import { AlertMessageService } from 'src/app/services';
import { Router } from '@angular/router';
import { CANAL } from "../../constant.canal";

@Component({
  selector: 'app-pesquisa-canal',
  templateUrl: './pesquisa-canal.component.html',
  styleUrls: ['./pesquisa-canal.component.css']
})
export class PesquisaCanalComponent extends AlertMessageService implements OnInit {

  pesquisaCanalPresenter: PesquisaCanalPresenter;

  constructor(private router: Router,
    pesquisaCanalPresenter: PesquisaCanalPresenter) {
    super();
    this.pesquisaCanalPresenter = pesquisaCanalPresenter;
    this.pesquisaCanalPresenter.pesquisaCanal = new PesquisaCanal();
  }

  ngOnInit() {
    this.pesquisaCanalPresenter.initConfigListaCanais(this);
  }

  novoCanal() {
    this.router.navigate([CANAL.CANAL_NOVO]);
  }

  editarCanal(identificador_canal: string) {
    this.router.navigate([CANAL.CANAL, identificador_canal]);
  }

  confirmarRemocao(identificador_canal: string) {
    this.pesquisaCanalPresenter.confirmRemoveCanal(this, identificador_canal);
  }

  realizarFiltroCanais(input: any, dataCanais: any) {
    this.pesquisaCanalPresenter.filterCanais(input, dataCanais);
  }

  realizarOrdenacao(event: SortEvent) {
    this.pesquisaCanalPresenter.customSort(event);
  }

}
