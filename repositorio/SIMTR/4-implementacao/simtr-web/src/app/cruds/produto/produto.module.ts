import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule, DropdownModule, CardModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';


import { TemplateModule } from 'src/app/template/template.module';


import { CrudProdutoComponent } from './produto.component';
import { CrudProdutoService } from './produto.service';
import { CrudProdutoRoutingModule } from './crud-produto.routing.module';
import { CompartilhadodModule } from './../../shared/modulos/compartilhado.module';


@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    PanelModule,
    DropdownModule,
    DataTableModule,
    ConfirmDialogModule,
    TemplateModule,
    CompartilhadodModule,
    CardModule,
    CrudProdutoRoutingModule
  ],
  exports: [
    CrudProdutoComponent
    
  ],
  declarations: [
    CrudProdutoComponent
  ],
  providers: [
    CrudProdutoService,
    ConfirmationService
  ]

})
export class CrudProdutoModule { }
