import { Component, OnInit } from '@angular/core';
import { DialogService } from 'angularx-bootstrap-modal';
import { UserSSOComponentPresenter } from '../presenter/user-sso.component.presenter';
import { VinculoCliente } from 'src/app/model';
import { UserSSOModel } from '../model/user-sso.model';
import { DialogInputReturnResult } from '../dialog-return/dialog-input-return.result';


@Component({
  selector: 'app-modal-user-sso',
  templateUrl: './modal-user-sso.component.html',
  styleUrls: ['./modal-user-sso.component.css']
})
export class ModalUserSSOComponent extends DialogInputReturnResult implements OnInit {

  userSSOComponentPresenter: UserSSOComponentPresenter
  cliente: VinculoCliente

  constructor(dialogService: DialogService,
    userSSOComponentPresenter: UserSSOComponentPresenter) {
    super(dialogService);
    this.userSSOComponentPresenter = userSSOComponentPresenter;
    this.userSSOComponentPresenter.userSSOModel = new UserSSOModel();
  }

  fechar() {
    this.userSSOComponentPresenter.close(this);
  }

  ngOnInit() {
    this.userSSOComponentPresenter.userSSOModel.cliente = this.cliente;
    this.userSSOComponentPresenter.inicializaEmail();
  }

  enviarDados() {
    this.userSSOComponentPresenter.salvarDadosUsuarioSSO(this);
  }

}
