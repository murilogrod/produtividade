import { ModalMessageComponent } from './modal-message/modal-message.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalConfirmComponent } from './modal-confirm/modal-confirm.component';

@NgModule({
  imports: [
    CommonModule
  ],
  exports : [
    ModalMessageComponent,
    ModalConfirmComponent
  ],
  declarations: [
    ModalMessageComponent,
    ModalConfirmComponent
  ],
  entryComponents : [
    ModalMessageComponent,
    ModalConfirmComponent
  ]
})
export class ModalModule { }
