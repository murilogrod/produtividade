import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {  HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgDragDropModule } from 'ng-drag-drop';
import { BrMasker4Module } from 'brmasker4';
import { TooltipModule} from 'primeng/tooltip';
import { AlertMenssagemComponent } from './view/alert-menssagem.component';
import { AlertMenssagemComponentPresenter } from './presenter/alert-menssagem.component.presenter';
import { HttpInterceptorService } from 'src/app/services';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgDragDropModule.forRoot(),
    BrMasker4Module,
    TooltipModule
  ],
  declarations: [
    AlertMenssagemComponent
  ],
  providers :[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    AlertMenssagemComponentPresenter
  ],
  entryComponents: [],
  exports : [
    AlertMenssagemComponent
  ]
})
export class AlertaMenssagemModule { }
