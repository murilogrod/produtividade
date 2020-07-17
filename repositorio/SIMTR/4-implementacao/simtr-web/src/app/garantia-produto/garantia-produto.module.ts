import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule, SpinnerModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { GarantiaProdutoComponent } from './garantia-produto.component';
import { GarantiaProdutoService } from './garantia-produto.service';
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
    GarantiaProdutoComponent
    
  ],
  declarations: [
    GarantiaProdutoComponent
  ],
  providers: [
    GarantiaProdutoService,
    ConfirmationService
  ]

})
export class GarantiaProdutoModule { }
