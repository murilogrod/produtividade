import { Component, OnInit, ViewEncapsulation, AfterViewChecked, AfterViewInit } from '@angular/core';
import { DossieService } from '../../dossie-service';
import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { DocumentoNode } from '../../../model/documentoNode';
import { MotivoSituacao } from '../../../model/motivo-situacao-doc';

export interface MessageModel {
    
}

export interface MessageModelSaida {
    motivoRevogado: string;
}
 
@Component({
  selector: 'app-modal-motivo-revogado',
  templateUrl: './modal-revogar-documento.component.html',
  styleUrls: ['./modal-revogar-documento.component.css']
})
export class ModalRevogarDocumentoComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit{

  constructor(dialogService: DialogService, private dossieService: DossieService) { 
    super(dialogService);
  }
  
  textareaMotivoRevogar: string;
  exibeBotaoSalvar:boolean = false;

  ngOnInit() {    

  }

  motivoRevogar(event) {
    this.exibeBotaoSalvar =  event.length > 10;
  }

  confirm() {
      this.result = {motivoRevogado : this.textareaMotivoRevogar };
      this.close();
  }

  cancel(){
    this.close();
  }
  
}
