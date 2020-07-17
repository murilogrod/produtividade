import { ChangeDetectorRef, Component, Input, OnChanges, ViewEncapsulation } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { TabelaSinteticoProcesso } from '../model/tabela-sintetico-processo.model';
import { TabelaSinteticoProcessoComponentPresenter } from '../presenter/tabela-sintetico-processo.component.presenter';

declare var $: any;

@Component({
  selector: 'tabela-sintetico-processo',
  templateUrl: './tabela-sintetico-processo.component.html',
  styleUrls: ['./tabela-sintetico-processo.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class TabelaSinteticoProcessoComponent implements OnChanges {
  @Input() falhaJBPMs: any[];

  tabelaSinteticoProcessoComponentPresenter: TabelaSinteticoProcessoComponentPresenter;

  constructor(private cdRef: ChangeDetectorRef,
    tabelaSinteticoProcessoComponentPresenter: TabelaSinteticoProcessoComponentPresenter) {
    this.tabelaSinteticoProcessoComponentPresenter = tabelaSinteticoProcessoComponentPresenter;
    this.tabelaSinteticoProcessoComponentPresenter.tabelaSinteticoProcesso = new TabelaSinteticoProcesso();
  }

  ngOnChanges() {
    this.tabelaSinteticoProcessoComponentPresenter.initConfigTable(this.falhaJBPMs);
  }

  realizarOrdenacao(event: SortEvent) {
    this.tabelaSinteticoProcessoComponentPresenter.customSort(event);
  }

  onFilter(event: any, globalFilter: any) {
    this.tabelaSinteticoProcessoComponentPresenter.setCountFilterGlobal(event, globalFilter);
  }

  filtroFalhaBPMSinteticoProcesso(input: any, dataSinteticoProcessos: any) {
    this.tabelaSinteticoProcessoComponentPresenter.filterFalhaBPMSinteticoProcesso(input, dataSinteticoProcessos);
  }

}
