import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { AlertMessageService } from 'src/app/services';
import { ComunicacaoJBPMComponentPresenter } from '../presenter/comunicacao-jbpm.component.presenter';
import { ComunicacaoJBPM } from './../model/comunicacao-jbpm.model';
declare var $: any;

@Component({
  selector: 'app-comunicacao-jbpm',
  templateUrl: './comunicacao-jbpm.component.html',
  styleUrls: ['./comunicacao-jbpm.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class ComunicacaoJBPMComponent extends AlertMessageService implements OnInit {

  comunicacaoJBPMPresenter: ComunicacaoJBPMComponentPresenter;

  constructor(comunicacaoJBPMPresenter: ComunicacaoJBPMComponentPresenter) {
    super();
    this.comunicacaoJBPMPresenter = comunicacaoJBPMPresenter;
    this.comunicacaoJBPMPresenter.comunicacaoJBPM = new ComunicacaoJBPM();
  }

  ngOnInit() {
    this.comunicacaoJBPMPresenter.initComunicacaoJBPM();
  }

  realizarOrdenacao(event: SortEvent) {
    this.comunicacaoJBPMPresenter.customSort(event);
  }

}
