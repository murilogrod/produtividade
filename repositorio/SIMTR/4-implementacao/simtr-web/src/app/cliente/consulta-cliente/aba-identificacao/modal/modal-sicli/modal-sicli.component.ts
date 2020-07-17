import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Utils } from '../../../../../utils/Utils';

import * as moment from 'moment';

export interface MessageModel {
  sicliUser: any;
  cpfCnpj: string;
  cpf: boolean;
  // isSicliList: boolean;
}

@Component({
  selector: 'modal-sicli',
  templateUrl: './modal-sicli.component.html',
  styleUrls: ['./modal-sicli.component.css']
})
export class ModalSicliComponent  extends DialogComponent<MessageModel, boolean> implements MessageModel , OnInit , OnDestroy {

  static sicliUserOriginal;

  sicliUser: any;
  cpf: boolean;
  cpfCnpj: string;
  // isSicliList: boolean;
  data_nascimento_fundacao;

  
  showBackButton = false;
  hasMeioComunicacao = false;
  hasEnderecoNacional = false;
  hasEnderecoExterior = false;
  hasProdutoCaixa = false;
  hasVinculos = false;
  hasMarcasSIDEC = false;
  hasDeclaracaoProposito = false;

  emails = [];
  telefones = [];
  
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm() {
    // we set dialog result as true on click on confirm button, 
    // then we can get dialog result from caller code 
    this.result = true;
    this.close();
  }

  ngOnInit(): void {

    if (ModalSicliComponent.sicliUserOriginal == null) {
      ModalSicliComponent.sicliUserOriginal = this.sicliUser;
    }

  }

  ngOnDestroy () {
    this.close();
  }

  /**
   * Evento disparado pela lista cocli para retornar detalhe
   * @param event 
   */
  onReturnDetail (event) {
    if (event) {
      // this.isSicliList = false;
      this.showBackButton = true;
      this.sicliUser = event.dados.cliente;
      this.ngOnInit();
    }
  }

  /**
   * Retorna visualização para lista de cocli
   * @param data 
   */
  backToList() {
    // this.isSicliList = true;
    this.showBackButton = false;
    this.sicliUser = ModalSicliComponent.sicliUserOriginal;
    this.ngOnInit();
  }

  transformData(data) {
    if( data == null) {
      return 'NÃO INFORMADO';
    }
    return moment(data).format('DD/MM/YYYY');
  }

  formatValor(v: any) {
    return Utils.formatMoney(v,2, " R$:  ", ".", ",");
  }

}
