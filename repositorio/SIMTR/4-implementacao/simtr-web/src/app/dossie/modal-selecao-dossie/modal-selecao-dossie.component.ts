import { Component, OnInit } from '@angular/core';
import { DialogComponent } from 'angularx-bootstrap-modal/dist/dialog.component';
import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { VinculoCliente, Processo, DossieProduto } from '../../model';
import { DossieService } from '../dossie-service';
import { Utils } from '../../utils/Utils';
import { SITUACAO_DOSSIE_PRODUTO } from '../../constants/constants';

export interface MessageModel {
  habilitaOpcoes: boolean;
  habilitaLista: boolean;
  dossiesProduto: DossieProduto[];
  cliente: VinculoCliente;
  listaProcesso: any[];
}

export interface MessageModelSaida {
  cliente: VinculoCliente;
  dossieProduto: DossieProduto;
  opcao: string;
  processo: any[];
}

@Component({
  selector: 'app-modal-selecao-dossie',
  templateUrl: './modal-selecao-dossie.component.html',
  styleUrls: ['./modal-selecao-dossie.component.css']
})
export class ModalSelecaoDossieComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel {

  constructor(dialogService: DialogService,
    private dossieService: DossieService) {
    super(dialogService);
  }

  habilitaOpcoes;
  habilitaLista;
  cliente;
  listaDossie;
  dossieProduto;
  opcao;
  dossiesProduto;
  listaProcesso;

  ngOnInit() {
  }

  confirm(opcao, item){
    if(opcao === SITUACAO_DOSSIE_PRODUTO.LISTAR){
      if(this.dossiesProduto.length > 0){
        //Ordena dossiesProduto
        this.dossiesProduto.sort(Utils.ordenarDossieProduto);
        this.habilitaLista = true;
        this.habilitaOpcoes = false;
      }
    }else if(opcao === SITUACAO_DOSSIE_PRODUTO.NOVO){
      this.result = { cliente : this.cliente.id, opcao : SITUACAO_DOSSIE_PRODUTO.NOVO, dossieProduto: null, processo: this.listaProcesso }
      this.close();
    }else if(opcao === SITUACAO_DOSSIE_PRODUTO.CANCELAR){
      this.close();
    }else{
      if(opcao === SITUACAO_DOSSIE_PRODUTO.CONSULTAR){
        this.opcao = SITUACAO_DOSSIE_PRODUTO.CONSULTAR;
      }else if(opcao === SITUACAO_DOSSIE_PRODUTO.MANTER){
        this.opcao = SITUACAO_DOSSIE_PRODUTO.MANTER;
      }
      this.result = { cliente : this.cliente, opcao : this.opcao, dossieProduto: item, processo: this.listaProcesso }
      this.close();
    }
  }

}
