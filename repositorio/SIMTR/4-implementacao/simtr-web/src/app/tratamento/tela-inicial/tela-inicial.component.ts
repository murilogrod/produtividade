import { TratamentoService } from '../tratamento.service';
import { Component, OnInit} from '@angular/core';
import { Utils } from '../../utils/Utils';
import { LoaderService, AlertMessageService } from 'src/app/services';
import { MSG_TRATAMENTO } from 'src/app/constants/constants';

declare var $: any;

const CORES_WIDGET = ["#00c0ef", "#f39c12", "#00a65a", "#001f3f", "#dd4b39", "#0073b7", "#111111", "#39cccc", "#3d9970", "#01ff70", "#ff851b"];

@Component({
  selector: 'tela-inicial',
  templateUrl: './tela-inicial.component.html',
  styleUrls: ['./tela-inicial.component.css']
})
export class TelaInicialComponent extends AlertMessageService implements OnInit {

  showChildrenLvl1 = false;
  showChildrenLvl2 = false;
  showChildrenLvl3 = false;
  showChildrenLvl4 = false;
  showChildrenLvl5 = false;
  macroProcessoIdCurrent = 0;

  macroProcessos: any = [];
  macroProcessosOrdPar: any = [];
  macroProcessosOrdImp: any = [];

  processosLvl1: any = [];
  processosLvl2: any = [];
  processosLvl3: any = [];
  processosLvl4: any = [];
  processosLvl5: any = [];

  alive = false;
  processo: number;
  listaCorGradiente: string[] = [];
  listaCorPrincipal: string[] = [];
  sequenciaProcesso: number[] = [];
  cor: string = "#00c0ef";
  hierarquiaCor: number[] = [];
  hierarquiaMaisMenos: number[] = [];
  indicePai: number;

  constructor(
    private tratamentoService: TratamentoService, private loadService: LoaderService
  ) {
    super();
    this.alive = true;
  }

  ngOnInit() {
    this.loadService.show();
    this.tratamentoService.getProcesso().subscribe(resp => {
      if(resp) {
        this.macroProcessos = resp;
        Utils.ordenarPorOrdemAlfabetica(this.macroProcessos);
        this.macroProcessosOrdPar = [];
        this.macroProcessosOrdImp = [];
        this.macroProcessos.forEach((macro, idx) => {
          if(idx % 2 === 0) {
            this.macroProcessosOrdPar.push(macro);
          }
          if(idx % 2 != 0) {
            this.macroProcessosOrdImp.push(macro);
          }
        });
        this.loadService.hide();
        return;
      }
      this.addMessageInfo(MSG_TRATAMENTO.SEM_MACROPROCESSO);
			this.loadService.hide();
    });
  }

}
