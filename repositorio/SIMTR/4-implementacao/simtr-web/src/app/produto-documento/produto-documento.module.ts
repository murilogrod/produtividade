import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule, SpinnerModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { ProdutoDocumentoComponent } from './produto-documento.component';
import { ProdutoDocumentoService } from './produto-documento.service';
import { TemplateModule } from '../template/template.module';
import {DropdownModule} from 'primeng/primeng';


@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    PanelModule,
    DataTableModule,
    ConfirmDialogModule,
    SpinnerModule,
    TemplateModule,
    DropdownModule
  ],
  exports: [
    ProdutoDocumentoComponent
    
  ],
  declarations: [
    ProdutoDocumentoComponent
  ],
  providers: [
    ProdutoDocumentoService,
    ConfirmationService
  ]

})
export class ProdutoDocumentoModule { }
