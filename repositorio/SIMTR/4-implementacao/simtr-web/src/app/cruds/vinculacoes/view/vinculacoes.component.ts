import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { Vinculacao } from '../../model/vinculacao.model';
import { VinculacoesPresenter } from '../presenter/vinculacoes.component.presenter';
import { Vinculacoes } from '../model/vinculacoes.model';
import { GridVinculacao } from '../model/grid-vinculacao.model';
import { VinculacaoChecklist } from '../../model/vinculacao-checklist';

@Component({
  selector: 'vinculacoes',
  templateUrl: './vinculacoes.component.html',
  styleUrls: ['./vinculacoes.component.css']
})
export class VinculacoesComponent implements OnInit {

  @Input() vinculacoes: Array<Vinculacao>;
  @Input() cloneChecklist: boolean;
  @Output() vinculacoesChanged: EventEmitter<Array<VinculacaoChecklist>> = new EventEmitter<Array<VinculacaoChecklist>>();
  @Output() vinculacoesRemovidasChanged: EventEmitter<Array<Vinculacao>> = new EventEmitter<Array<Vinculacao>>();
  vinculacoesPresenter: VinculacoesPresenter;

  constructor(vinculacoesPresenter: VinculacoesPresenter) {
    this.vinculacoesPresenter = vinculacoesPresenter;
    this.vinculacoesPresenter.vinculacoes = new Vinculacoes();
  }

  ngOnInit() {
    this.vinculacoesPresenter.initConfigListaVinculacoes(this);
  }

  realizarOrdenacao(event: SortEvent) {
    this.vinculacoesPresenter.customSort(event);
  }

  abrirModalVinculacao() {
    this.vinculacoesPresenter.openModalModalVinculacao(this);
  }

  abrirModalVinculacaoConflitante(index: number, gridVinculacao: GridVinculacao) {
    this.vinculacoesPresenter.openModalModalVinculacaoConflitante(this, index, gridVinculacao);
  }

  onFilter(event: any, globalFilter: any) {
    this.vinculacoesPresenter.setCountFilterGlobal(event, globalFilter);
  }

  removerVinculacao(index: number, gridVinculacao: GridVinculacao) {
    this.vinculacoesPresenter.confirmRemoveVinculacao(index, gridVinculacao, this);
  }

}
