import { ModalShowImageComponent } from './modal-show-image/modal-show-image.component';
import { CalendarModule, TreeModule, CarouselModule, DataTableModule } from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { DocumentoComponent } from './documento.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgDragDropModule} from 'ng-drag-drop';
import { HttpInterceptorService } from '../services';
import { DocumentoService } from './documento-service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ArvoreGenericaComponent } from './arvore-generica/arvore-generica.component';
import { ModalReutilizacaoDocumentosComponent } from './modal-reutilizar/modal-reutilizacao-documentos.component';
import { TemplateModule } from '../template/template.module';
import { UploadDocumentoComponent } from './upload-documento/view/upload-documento.component';
import { UploadDocumentoComponentPresenter } from './upload-documento/presenter/upload-documento.component.presenter';
import { ShareModule } from '../shared/share.module';
import { VisualizadorExpanditoComponentPresenter } from './visualizador-expandido/presenter/visualizador-expandido.component.presenter';
import { VisualizadorExpanditoComponent } from './visualizador-expandido/view/visualizador-expandido.component';
import { FormularioGenericoModule } from './formulario-generico/formulario-generico.modules';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';
import { TooltipModule } from "primeng/tooltip";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    CalendarModule,
    NgDragDropModule,
    TreeModule,
    DataTableModule,
    CarouselModule,
    TemplateModule,
    ShareModule,
    FormularioGenericoModule,
    LoaderRelativoModule,
    TooltipModule
  ],
  declarations: [
    DocumentoComponent,
    ModalShowImageComponent,
    ModalReutilizacaoDocumentosComponent,
    ArvoreGenericaComponent,
    UploadDocumentoComponent,
    VisualizadorExpanditoComponent
  ] ,
  entryComponents : [
    ModalShowImageComponent,
    ModalReutilizacaoDocumentosComponent
  ]
  ,
  exports : [
    DocumentoComponent,
    ModalShowImageComponent,
    ArvoreGenericaComponent,
    UploadDocumentoComponent,
    VisualizadorExpanditoComponent
  ],
  providers :[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    UploadDocumentoComponentPresenter,
    VisualizadorExpanditoComponentPresenter,
    DocumentoService
  ]
})
export class DocumentoModule { }
