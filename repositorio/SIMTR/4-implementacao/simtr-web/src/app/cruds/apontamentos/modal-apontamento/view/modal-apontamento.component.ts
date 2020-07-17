import { Component, OnInit } from '@angular/core';
import { ModalApontamentoPresenter } from '../presenter/modal-apontamento.component.presenter';
import { ModalApontamento } from '../model/modal-apontamento.model';
import { DialogInputApontamentoReturnResult } from '../dialog-return/dialog-input-apontamento-return-result';
import { DialogService } from 'angularx-bootstrap-modal';
import { ApontamentoDataInput } from '../../model/apontamento-data-input';
import { Apontamento } from 'src/app/cruds/model/apontamento.model';

@Component({
  selector: 'app-modal-apontamento',
  templateUrl: './modal-apontamento.component.html',
  styleUrls: ['./modal-apontamento.component.css']
})
export class ModalApontamentoComponent extends DialogInputApontamentoReturnResult implements OnInit {

  modalApontamentoPresenter: ModalApontamentoPresenter;
  apontamentoDataInput: ApontamentoDataInput;

  constructor(dialogService: DialogService,
    modalApontamentoPresenter: ModalApontamentoPresenter) {
    super(dialogService);
    this.modalApontamentoPresenter = modalApontamentoPresenter;
    this.modalApontamentoPresenter.modalApontamento = new ModalApontamento();
    this.modalApontamentoPresenter.modalApontamento.apontamento = new Apontamento();
  }

  ngOnInit() {
    this.modalApontamentoPresenter.initConfigModalApontamento(this.apontamentoDataInput);
  }

  fechar() {
    this.closeDialog();
  }

  adicionarApontamento() {
    this.modalApontamentoPresenter.adicionarApontamentoGrid(this);
  }

  validarPendenciaSeguranca() {
    this.modalApontamentoPresenter.validarPendenciaInformacao();
  }

  validarPendenciaInformacao() {
    this.modalApontamentoPresenter.validarPendenciaSeguranca();
  }

  validarOcorrenciaNomeApontamento() {
    this.modalApontamentoPresenter.validarOcorrenciaApontamento(this);
  }

  /**
   * Validação nome apontamento: DIV vermelha
   * @param nome 
   * @param validCustom 
   */
  validarNomeApontamento(nome: any, validCustom: boolean): boolean {
    return ((nome.dirty && nome.errors && nome.errors.required) || (validCustom));
  }

}
