
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PanelModule, DataTableModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import { MacroProcessoComponent } from './macro-processo.component';
import { MacroProcessoService } from './macro-processo.service';
import { TemplateModule } from '../template/template.module';

@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    PanelModule,
    DataTableModule,
    ConfirmDialogModule,
    TemplateModule
  ],
  exports: [
    MacroProcessoComponent
    
  ],
  declarations: [
    MacroProcessoComponent
  ],
  providers: [
    MacroProcessoService,
    ConfirmationService
  ]

})
export class MacroProcessoModule { }
