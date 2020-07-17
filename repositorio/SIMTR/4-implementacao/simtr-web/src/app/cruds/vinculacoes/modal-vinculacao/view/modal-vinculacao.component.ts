import { Component, OnInit } from '@angular/core';
import { ModalVinculacaoPresenter } from '../presenter/modal-vinculacao.component.presenter';
import { ModalVinculacao } from '../model/modal-vinculacao.model';
import { VinculacaoDataInput } from '../../model/vinculacao-data-input';
import { DialogInputVinculacaoReturnResult } from '../dialog-return/dialog-input-vinculacao-return-result';
import { DialogService } from 'angularx-bootstrap-modal';
import { Utils } from 'src/app/utils/Utils';

@Component({
  selector: 'app-modal-vinculacoes',
  templateUrl: './modal-vinculacao.component.html',
  styleUrls: ['./modal-vinculacao.component.css']
})
export class ModalVinculacaoComponent extends DialogInputVinculacaoReturnResult implements OnInit {

  modalVinculacaoPresenter: ModalVinculacaoPresenter;
  vinculacaoDataInput: VinculacaoDataInput;

  constructor(modalVinculacaoPresenter: ModalVinculacaoPresenter,
    dialogService: DialogService) {
    super(dialogService);
    this.modalVinculacaoPresenter = modalVinculacaoPresenter;
    this.modalVinculacaoPresenter.modalVinculacao = new ModalVinculacao();
  }

  ngOnInit() {
    this.modalVinculacaoPresenter.initConfigModalVinculacao(this.vinculacaoDataInput);
  }

  selecionarFasesProcessoGeraDossie() {
    this.modalVinculacaoPresenter.getFasesGeraDossie();
  }

  anularTiposDocumentos() {
    this.modalVinculacaoPresenter.inicializarTiposDocumentos();
  }

  anularFuncoesDocumentais() {
    this.modalVinculacaoPresenter.inicializarFuncoesDocumentais();
  }

  adicionarVinculo() {
    this.modalVinculacaoPresenter.adicionarVinculacaoGrid(this);
  }

  maskCalendar(event): void {
    event.target.value = Utils.mascaraData(event);
  }

  onSelecDataRevogacao() {
    this.modalVinculacaoPresenter.verificarDataRevogacaoFutura();
  }

  validarOcorrenciaVinculacao() {
    this.modalVinculacaoPresenter.validarOcorrenciaVinculacaoMemoria(this.vinculacaoDataInput);
  }

  validarVinculacaoAtual() {
    this.modalVinculacaoPresenter.validarConflitoVinculacao(this.vinculacaoDataInput);
  }

  fechar() {
    this.closeDialog();
  }

  validarProcesso(processo: any): boolean {
    return this.modalVinculacaoPresenter.validarFaseProcesso(processo);
  }

  validarFase(fase: any): boolean {
    return this.modalVinculacaoPresenter.validarFaseProcesso(fase);
  }

  desabilitarAdicaoVinculacao(vinculacaoForm: any, processo: any, fase: any): boolean {
    return this.modalVinculacaoPresenter.disabledAddButton(vinculacaoForm, processo, fase);
  }

}
