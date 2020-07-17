import { ChangeDetectorRef, Component, Input, OnChanges, ViewEncapsulation } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { TabelaAnalitico } from './../model/tabela-analitico.model';
import { TabelaAnaliticoComponentPresenter } from './../presenter/tabela-analitico.component.presenter';

declare var $: any;

@Component({
  selector: 'tabela-analitico',
  templateUrl: './tabela-analitico.component.html',
  styleUrls: ['./tabela-analitico.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class TabelaAnaliticoComponent implements OnChanges {
  @Input() falhaJBPMs: any[];

  tabelaAnaliticoComponentPresenter: TabelaAnaliticoComponentPresenter;

  constructor(private cdRef: ChangeDetectorRef,
    tabelaAnaliticoComponentPresenter: TabelaAnaliticoComponentPresenter) {
    this.tabelaAnaliticoComponentPresenter = tabelaAnaliticoComponentPresenter;
    this.tabelaAnaliticoComponentPresenter.tabelaAnalitico = new TabelaAnalitico();
  }

  ngOnChanges() {
    this.tabelaAnaliticoComponentPresenter.initConfigTable(this.falhaJBPMs);
  }

  realizarOrdenacao(event: SortEvent) {
    this.tabelaAnaliticoComponentPresenter.customSort(event);
  }

  onFilter(event: any, globalFilter: any) {
    this.tabelaAnaliticoComponentPresenter.setCountFilterGlobal(event, globalFilter);
  }

  filtroFalhaBPMAnalitico(input: any, dataAnaliticos: any) {
    this.tabelaAnaliticoComponentPresenter.filterFalhaBPMAnalitico(input, dataAnaliticos);
  }

}
