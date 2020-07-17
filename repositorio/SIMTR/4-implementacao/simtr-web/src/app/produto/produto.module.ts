import { DocumentoModule } from '../documento/documento.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TabViewModule, AccordionModule, TreeModule, CarouselModule, DataTableModule, SharedModule, CalendarModule, FieldsetModule } from 'primeng/primeng';
import { TemplateModule } from '../template/template.module';
import { FileUploadModule } from 'ng2-file-upload';
import { NgDragDropModule } from 'ng-drag-drop';
import { BrMasker4Module } from 'brmasker4';
import { VisualizadorDocumentosModule } from '../visualizador-documentos/visualizador-documentos.module';
import { ProdutoService } from './produto-service';
import { HttpInterceptorService } from '../services';
import { ProdutoComponent } from './produto.component';
import { ModalProcessosComponent } from '../template/modal/modal-processos/modal-processos.component';
import { ModalModule } from '../template/modal/modal.module';
import { AnalyticsModule } from '../shared/analytics.module';
import { ShareModule } from '../shared/share.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TabViewModule,
    AccordionModule,
    TreeModule,
    CarouselModule,
    DataTableModule,
    SharedModule,
    CalendarModule,
    ShareModule,
    TemplateModule,
    FileUploadModule,
    NgDragDropModule.forRoot(),
    DocumentoModule,
    FieldsetModule,
    BrMasker4Module,
    VisualizadorDocumentosModule,
    ModalModule,
    AnalyticsModule
  ],
  declarations: [
      ProdutoComponent
  ],
  providers :[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    ProdutoService
  ],
  entryComponents: [
    ModalProcessosComponent
  ],
  exports : [
  ]
})
export class ProdutoModule { }
