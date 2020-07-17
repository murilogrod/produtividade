import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule, DropdownModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { FuncaoDocumentalComponent } from './funcao-documental.component';
import { FuncaoDocumentalService } from './funcao-documental.service';
import { TemplateModule } from 'src/app/template/template.module';
import { CrudFuncaoDocumentalRoutingModule } from './crud-funcao-documental.routing.module';

@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    PanelModule,
    DropdownModule,
    DataTableModule,
    ConfirmDialogModule,
    TemplateModule,
    CrudFuncaoDocumentalRoutingModule
  ],
  exports: [
    FuncaoDocumentalComponent
    
  ],
  declarations: [
    FuncaoDocumentalComponent
  ],
  providers: [
    FuncaoDocumentalService,
    ConfirmationService
  ]

})
export class FuncaoDocumentalModule { }
