import { Component, OnInit } from '@angular/core';
import { DialogInputReturnResult } from '../dialog-return/dialog-input-return-result';
import { DialogService } from 'angularx-bootstrap-modal';
import { SocioReceitaFederalPresenter } from '../presenter/socio-receita-federal.component.presenter';
import { SocioReceitaFederal } from '../model/socio-receita-federal.model';
import { DataInputReceitaFederal } from '../../model/data-input-receita-federal';
import { SortEvent } from 'primeng/primeng';

@Component({
  selector: 'app-modal-socio-receita-federal',
  templateUrl: './modal-socio-receita-federal.component.html',
  styleUrls: ['./modal-socio-receita-federal.component.css']
})
export class ModalSocioReceitaFederalComponent extends DialogInputReturnResult implements OnInit {

  socioReceitaFederalPresenter: SocioReceitaFederalPresenter;
  dataInputReceitaFederal: DataInputReceitaFederal;

  constructor(dialogService: DialogService,
    socioReceitaFederalPresenter: SocioReceitaFederalPresenter, ) {
    super(dialogService);
    this.socioReceitaFederalPresenter = socioReceitaFederalPresenter;
    this.socioReceitaFederalPresenter.socioReceitaFederal = new SocioReceitaFederal();
  }

  ngOnInit() {
    this.socioReceitaFederalPresenter.initConfigModalSociosReceitaFederal(this.dataInputReceitaFederal);
  }

  formatarCpfCnpj(cpfcnpj: string): string {
    return this.socioReceitaFederalPresenter.formatValue(cpfcnpj);
  }

  onSociosAllSelect(sociosChecked: any) {
    this.socioReceitaFederalPresenter.atualizarSociosConformeErro(sociosChecked);
  }

  adicionarSocios() {
    this.socioReceitaFederalPresenter.selectSocios(this);
  }

  fechar() {
    this.closeDialog();
  }

  realizarFiltroSocios(input: any, dataSocios: any) {
    this.socioReceitaFederalPresenter.filterSocios(input, dataSocios);
  }

  realizarOrdenacao(event: SortEvent) {
    this.socioReceitaFederalPresenter.customSort(event);
  }

}
