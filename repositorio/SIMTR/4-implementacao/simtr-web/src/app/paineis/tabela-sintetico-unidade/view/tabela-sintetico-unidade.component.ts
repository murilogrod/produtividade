import { Component, Input, OnChanges, ViewEncapsulation } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { TabelaSinteticoUnidade } from '../model/tabela-sintetico-unidade.model';
import { TabelaSinteticoUnidadeComponentPresenter } from '../presenter/tabela-sintetico-unidade.component.presenter';

declare var $: any;

@Component({
  selector: 'tabela-sintetico-unidade',
  templateUrl: './tabela-sintetico-unidade.component.html',
  styleUrls: ['./tabela-sintetico-unidade.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class TabelaSinteticoUnidadeComponent implements OnChanges {
  @Input() falhaJBPMs: any [];

  tabelaSinteticoUnidadeComponentPresenter: TabelaSinteticoUnidadeComponentPresenter;

  constructor(tabelaSinteticoUnidadeComponentPresenter: TabelaSinteticoUnidadeComponentPresenter) {
    this.tabelaSinteticoUnidadeComponentPresenter = tabelaSinteticoUnidadeComponentPresenter;
    this.tabelaSinteticoUnidadeComponentPresenter.tabelaSinteticoUnidade = new TabelaSinteticoUnidade();
  }

  ngOnChanges() {
    this.tabelaSinteticoUnidadeComponentPresenter.initConfigTable(this.falhaJBPMs);
  }

  realizarOrdenacao(event: SortEvent) {
    this.tabelaSinteticoUnidadeComponentPresenter.customSort(event);
  }

  onFilter(event: any, globalFilter: any) {
    this.tabelaSinteticoUnidadeComponentPresenter.setCountFilterGlobal(event, globalFilter);
  }

  filtroSinteticoPorUnidade(input: any, dataSinteticoUnidades: any) {
    this.tabelaSinteticoUnidadeComponentPresenter.filtroSinteticoPorUnidade(input, dataSinteticoUnidades);
  }

}
