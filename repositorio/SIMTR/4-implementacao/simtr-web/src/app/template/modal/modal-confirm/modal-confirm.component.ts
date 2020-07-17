import { Component } from '@angular/core';
import { DialogComponent, DialogService } from 'angularx-bootstrap-modal';

export interface MessageModel {
  title:string;
  msgs:string[];
}

@Component({
  selector: 'app-modal-confirm',
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.css']
})
export class ModalConfirmComponent extends DialogComponent<MessageModel, boolean> implements MessageModel {
  title: string;
  msgs: string[];
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm(resposta) {
    // we set dialog result as true on click on confirm button, 
    // then we can get dialog result from caller code 
   if(resposta === 'S'){
      this.result = true;
    }else{
      this.result = false;
    }
    
    this.close();
  }


}
