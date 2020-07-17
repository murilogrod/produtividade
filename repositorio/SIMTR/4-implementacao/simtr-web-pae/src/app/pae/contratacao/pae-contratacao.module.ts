import { PaeUtils } from './../utils/utilidades/pae-utils';
import { NgModule } from '@angular/core';
import { TabViewModule } from 'primeng/primeng';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule, DataTableModule, SpinnerModule, InputMaskModule } from 'primeng/primeng';
import { ConfirmDialogModule,ConfirmationService } from 'primeng/primeng';
import { PaeContratacaoComponent } from './pae-contratacao.component';
import { PaeContratacaoService } from './pae-contratacao.service';
import { PaeContratosService } from './pae-contratos/pae-contratos.service';
import { TemplateModule } from '../../template/template.module';
import { HttpInterceptorService } from '../../services/index';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { PaeProcessoComponent } from './processo/pae-processo.component';
import { PaeContratosComponent } from './pae-contratos/pae-contratos.component';
import { PaeApensosComponent } from './pae-apensos/pae-apensos.component';
import { DialogModule, DropdownModule, InputTextareaModule} from 'primeng/primeng';
import { PaeProcessoService } from './processo/pae-processo.service';
import { PaeApensosService } from './pae-apensos/pae-apensos.service';
import { PaeApensosContratosComponent } from './pae-apensos-contratos/pae-apensos-contratos.component';
import { PaeApensosContratosService } from './pae-apensos-contratos/pae-apensos-contratos.service';
import { FileUploadModule } from 'ng2-file-upload';
import { PaeDocsComponent } from './pae-docs/pae-docs.component';
import { PaeDocsService } from './pae-docs/pae-docs.service';
import { FormataMimePipe } from './../utils/pipes/formata-mime/formata-mime.pipe';
import { FormataCpfCnpj } from './../utils/pipes/formata-cpf-cnpj/formata-cpf-cnpj.pipe';
import { FormataTituloApenso } from './../utils/pipes/formata-titulo-apenso/formata-titulo-apenso.pipe';
import { FormataDocValido } from './../utils/pipes/formata-doc-valido/formata-doc-valido.pipe';
import { ZerosEsquerda } from './../utils/pipes/zeros-esquerda/zeros-esquerda.pipe';
import { AlertMessageService } from './../../services/message/alert-message.service';

@NgModule({
  imports: [
    CommonModule, 
    TabViewModule,
    DropdownModule,
    FormsModule,
    PanelModule,
    HttpClientModule,
    DataTableModule,
    InputMaskModule,
    DialogModule,
    InputTextareaModule,
    ConfirmDialogModule,
    SpinnerModule,
    TemplateModule,
    FileUploadModule
  ],
  exports: [
    PaeProcessoComponent,
    PaeContratacaoComponent,
    PaeContratosComponent,
    PaeApensosComponent,
    PaeDocsComponent,
    PaeApensosContratosComponent
    
  ],
  declarations: [
    PaeContratacaoComponent,
    PaeProcessoComponent,
    PaeDocsComponent,
    PaeContratosComponent,
    PaeApensosComponent,
    PaeApensosContratosComponent,
    PaeDocsComponent,
    FormataMimePipe,
    FormataCpfCnpj,
    FormataTituloApenso, 
    FormataDocValido,
    ZerosEsquerda
    
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    PaeProcessoService,
    PaeContratacaoService,
    PaeContratosService,
    ConfirmationService,
    PaeContratosComponent,
    PaeApensosService,
    PaeApensosContratosService, 
    PaeDocsService,
    AlertMessageService,
    PaeUtils
  ]

})
export class PaeContratacaoModule { }
