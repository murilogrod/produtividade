import { Component, OnInit } from '@angular/core';
import { DialogInputCloneInativarRemoverChecklistReturnResult } from '../dialog-return/dialog-input-clone-inativar-remover-checklist-return-result';
import { DialogService } from 'angularx-bootstrap-modal';
import { ModalCloneInativarRemover } from '../model/modal-clone-inativar-remover.model';
import { ModalCloneInativarRemoverPresenter } from '../presenter/modal-clone-inativar-remover.component.presenter';
import { CloneInativarRemoverChecklistDataInput } from '../../model/clone-inativar-remover-checklist-data-input';

@Component({
  selector: 'modal-clone-inativar-remover-checklist',
  templateUrl: './modal-clone-inativar-remover-checklist.component.html',
  styleUrls: ['./modal-clone-inativar-remover-checklist.component.css']
})
export class ModalCloneInativarRemoverChecklistComponent extends DialogInputCloneInativarRemoverChecklistReturnResult implements OnInit {

  cloneInativarRemoverChecklistDataInput: CloneInativarRemoverChecklistDataInput;
  modalCloneInativarRemoverPresenter: ModalCloneInativarRemoverPresenter;

  constructor(dialogService: DialogService,
    modalCloneInativarRemoverPresenter: ModalCloneInativarRemoverPresenter) {
    super(dialogService);
    this.modalCloneInativarRemoverPresenter = modalCloneInativarRemoverPresenter;
    this.modalCloneInativarRemoverPresenter.modalCloneInativarRemover = new ModalCloneInativarRemover();
  }

  ngOnInit() {
    this.modalCloneInativarRemoverPresenter.initConfigModalCloneInativarRemover(this.cloneInativarRemoverChecklistDataInput);
  }

  inativarChecklist() {
    this.modalCloneInativarRemoverPresenter.atualizarDataInativacaoChecklist(this);
  }

  removerChecklist() {
    this.modalCloneInativarRemoverPresenter.removerChecklist(this);
  }

  clonarChecklist() {
    this.modalCloneInativarRemoverPresenter.recuperarChecklistParaClone(this);
  }

  fechar() {
    this.closeDialog();
  }

}
