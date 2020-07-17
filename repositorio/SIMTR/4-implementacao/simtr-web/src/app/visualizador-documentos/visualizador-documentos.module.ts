import { CalendarModule, TreeModule, CarouselModule, FieldsetModule, FileUploadModule } from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgDragDropModule} from 'ng-drag-drop';
import { HttpInterceptorService } from '../services';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { DocumentoService } from '../documento/documento-service';
import { ClassificacaoGenericoModule } from '../documento/classificacao-generico/classificacao-generico.module';
import {TooltipModule} from 'primeng/tooltip';
import { DocumentoModule } from '../documento';
import { VisualizadorDocumentosComponent } from './visualizador-documentos.component';
import { TemplateModule } from '../template/template.module';
import { ModalExtracaoDadosDocumento } from './modal-extracao-dados-documento/modal-extracao-dados-documento.component';
import { ExtracaoManualModule } from '../extracao-manual/extracao-manual.module';
import { CompartilhadodModule } from '../shared/modulos/compartilhado.module';
import { AnalyticsModule } from '../shared/analytics.module';
import { FormularioExtracaoManualModule } from '../compartilhados/componentes/formulario-extracao/formulario-extracao-manual.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    CalendarModule,
    NgDragDropModule,
    TreeModule,
    CarouselModule,
    FieldsetModule,
    ClassificacaoGenericoModule,
    TooltipModule,
    DocumentoModule,
    TemplateModule,
    CompartilhadodModule,
    AnalyticsModule,
    ExtracaoManualModule,
    FormularioExtracaoManualModule
  ],
  declarations: [
    VisualizadorDocumentosComponent,
    ModalExtracaoDadosDocumento
  ] ,
  entryComponents : [
    ModalExtracaoDadosDocumento
  ]
  ,
  exports : [
    VisualizadorDocumentosComponent,
    ModalExtracaoDadosDocumento
  ],
  providers :[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    DocumentoService
  ]
})
export class VisualizadorDocumentosModule { }
