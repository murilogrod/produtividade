import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/primeng';
import { ShareModule } from '../../../shared/share.module';
import { CepOnlineService } from './service/cep-online-service';

@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    TooltipModule,
    ShareModule
  ],
  exports: [
  ],
  declarations: [
  ],
  providers: [
    CepOnlineService
  ]

})
export class CepOnlineModule {}
