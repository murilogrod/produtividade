import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule, DropdownModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { TemplateModule } from 'src/app/template/template.module';
import { ComposicaoDocumentalComponent } from './composicao-documental.component';
import { ComposicaoDocumentalService } from './composicao-documental.service';
import { CrudComposicaodocumentalRoutingModule } from './crud-composicao-documental.routing.module';

@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    PanelModule,
    DropdownModule,
    DataTableModule,
    ConfirmDialogModule,
    TemplateModule,
    CrudComposicaodocumentalRoutingModule
  ],
  exports: [
    ComposicaoDocumentalComponent
    
  ],
  declarations: [
    ComposicaoDocumentalComponent
  ],
  providers: [
    ComposicaoDocumentalService,
    ConfirmationService
  ]

})
export class ComposicaoDocumentalModule { }
