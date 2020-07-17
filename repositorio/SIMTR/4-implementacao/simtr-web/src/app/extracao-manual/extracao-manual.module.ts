import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExtracaoManualComponent } from './extracao-manual.component';
import { DropdownModule, InputMaskModule } from '../../../node_modules/primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '../../../node_modules/@angular/forms';
import { ExtracaoManualService } from './extracao-manual.service';
import { FileUploadModule } from '../../../node_modules/ng2-file-upload';
import { Utils } from './../utils/Utils';
import { AnalyticsModule } from '../shared/analytics.module';
import { CompartilhadodModule } from './../shared/modulos/compartilhado.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptorService } from '../services';
import { FormularioExtracaoManualModule } from '../compartilhados/componentes/formulario-extracao/formulario-extracao-manual.module';
import { VisualizadorDocumentoComponent } from '../compartilhados/componentes/visualizador-documento/visualizador-documento.component';

@NgModule({
  declarations: [
    ExtracaoManualComponent,
    VisualizadorDocumentoComponent
  ],
  imports: [
    CommonModule,
    DropdownModule,
    InputMaskModule,
    FormsModule,
    ReactiveFormsModule, 
    FileUploadModule,
    CompartilhadodModule,
    AnalyticsModule,
    FormularioExtracaoManualModule
  ], 
  providers: [    
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    ExtracaoManualService, 
    Utils
  ],
  exports: [
    ExtracaoManualComponent,
    VisualizadorDocumentoComponent
  ],
  entryComponents: []
})
export class ExtracaoManualModule { }
