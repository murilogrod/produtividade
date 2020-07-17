import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Apontamento } from '../../model/apontamento.model';
import { ApontamentosPresenter } from '../presenter/apontamentos.component.presenter';
import { Apontamentos } from '../model/apontamentos.model';
import { SortEvent } from 'primeng/primeng';
import { UtilsGrids } from '../../util/utils-grids';

@Component({
  selector: 'apontamentos',
  templateUrl: './apontamentos.component.html',
  styleUrls: ['./apontamentos.component.css']
})
export class ApontamentosComponent implements OnInit {

  @Input() apontamentos: Array<Apontamento>
  @Input() disabledApontamentos: boolean;
  @Output() apontamentosChanged: EventEmitter<Array<Apontamento>> = new EventEmitter<Array<Apontamento>>();
  @Output() apontamentosRemovidosChanged: EventEmitter<Array<Apontamento>> = new EventEmitter<Array<Apontamento>>();
  apontamentosPresenter: ApontamentosPresenter;


  constructor(apontamentosPresenter: ApontamentosPresenter) {
    this.apontamentosPresenter = apontamentosPresenter;
    this.apontamentosPresenter.apontamentos = new Apontamentos();
  }

  ngOnInit() {
    this.apontamentosPresenter.initConfigListaApontamentos(this);
  }

  realizarOrdenacao(event: SortEvent) {
    UtilsGrids.customSort(event);
  }

  reordenarApontamentos(event: DragEvent) {
    this.apontamentosPresenter.orderApontamentos(event, this);
  }

  removerApontamento(index: number, apontamento: Apontamento) {
    this.apontamentosPresenter.confirmRemoveApontamento(index, apontamento, this);
  }

  abrirModalApontamento(index: number, apontamento: Apontamento) {
    this.apontamentosPresenter.openModalApontamento(index, apontamento, this);
  }

  onFilter(event: any, globalFilter: any) {
    this.apontamentosPresenter.setCountFilterGlobal(event, globalFilter);
  }

}
