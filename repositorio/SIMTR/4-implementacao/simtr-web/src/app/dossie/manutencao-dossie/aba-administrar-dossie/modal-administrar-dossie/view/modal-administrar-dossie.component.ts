import { Component, OnInit } from '@angular/core';
import { DialogInputReturnResult } from '../dialog-return/dialog-input-return.result';
import { DialogService } from 'angularx-bootstrap-modal';
import { AdministrarDossieComponentPresenter } from '../presenter/administrar-dossie.component.presenter';
import { AbaAdministrarDossie } from '../model/aba-administrar-dossie.model';

@Component({
  selector: 'app-modal-administrar-dossie',
  templateUrl: './modal-administrar-dossie.component.html',
  styleUrls: ['./modal-administrar-dossie.component.css']
})
export class ModalAdministrarDossieComponent extends DialogInputReturnResult implements OnInit {

  administrarDossieComponentPresenter: AdministrarDossieComponentPresenter
  dossieProduto: any;

  constructor(dialogService: DialogService,
    administrarDossieComponentPresenter: AdministrarDossieComponentPresenter) {
    super(dialogService);
    this.administrarDossieComponentPresenter = administrarDossieComponentPresenter;
    this.administrarDossieComponentPresenter.abaAdministrarDossie = new AbaAdministrarDossie();
  }

  ngOnInit() {
    this.administrarDossieComponentPresenter.inicializaTitutlosModalAdministracaoDossie(this.dossieProduto);
  }

  fechar() {
    this.closeDialog();
  }

  /**
   * Pega o evento de digitação text area para desabilitar o botão confirmar
   * @param event 
   */
  disabledConfirmButton(event: any) {
    this.administrarDossieComponentPresenter.disabledConfirmButton(event);
  }

  /**
   * Confirma a requisição do usuário e fecha a modal
   */
  confirmarRequisicao() {
    this.administrarDossieComponentPresenter.close(this, this.dossieProduto);
  }


}
