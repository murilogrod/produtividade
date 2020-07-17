import { Component, OnInit } from '@angular/core';
import { DialogComponent, DialogService } from 'angularx-bootstrap-modal';

export interface MessageModel {
  title:string;
  msgs:string[];
}

@Component({
  selector: 'cx-modal-message',
  templateUrl: './modal-message.component.html',
  styleUrls: ['./modal-message.component.css']
})
export class ModalMessageComponent extends DialogComponent<MessageModel, boolean> implements MessageModel {
  title: string;
  msgs: string[];
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm() {
    // we set dialog result as true on click on confirm button, 
    // then we can get dialog result from caller code 
    this.result = true;
    this.close();
  }

}
