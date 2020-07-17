import { ModalMessageComponent } from './modal-message/modal-message.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalConfirmComponent } from './modal-confirm/modal-confirm.component';
import { ModalProcessosComponent } from './modal-processos/modal-processos.component';
import { FormsModule } from '../../../../node_modules/@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  exports : [
    ModalMessageComponent,
    ModalConfirmComponent,
    ModalProcessosComponent
  ],
  declarations: [
    ModalMessageComponent,
    ModalConfirmComponent,
    ModalProcessosComponent
  ],
  entryComponents : [
    ModalMessageComponent,
    ModalConfirmComponent
  ]
})
export class ModalModule { }
