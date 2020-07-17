import { Component, OnInit } from "@angular/core";
import { ViewEncapsulation } from "@angular/core";
import { DialogComponent, DialogService } from "../../../../../../node_modules/angularx-bootstrap-modal";

export interface MessageModelEntrada {
  modalDescricao: boolean;
  justificativa: string;
  descricaoItem: any;
}
  
export interface MessageModelSaida {
  motivoApontamento: string
}

@Component({
    selector: 'modal-justifica-apontamento',
    templateUrl: './modal-justificativa-apontamento.component.html',
    styleUrls: ['./modal-justificativa-apontamento.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ModalJustificativaApontamentoComponent extends DialogComponent<MessageModelEntrada, MessageModelSaida> implements MessageModelEntrada, OnInit{

    constructor(dialogService: DialogService) { 
      super(dialogService);
    }
    
    textareaJustificativaApontamento: string;
    exibeBotaoSalvar:boolean = false;
    modalDescricao: boolean;
    justificativa: string;
    descricaoItem: any;
  
    ngOnInit() {    
      this.justificativa ? this.textareaJustificativaApontamento = this.justificativa :"";
    }
  
    justificativaApontamento(event) {
      this.exibeBotaoSalvar =  event.length > 10;
    }
  
    confirm() {
        this.result = {motivoApontamento : this.textareaJustificativaApontamento };
        this.close();
    }
  
    cancel(){
      this.close();
    }

}