import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { TemplateModule } from '../template/template.module';


import {
  KzCepPipe,
  KzCpfPipe,
  KzCnpjPipe,
  KzCpfCnpjPipe,
  KzCpfValidatorDirective,
  KzCnpjValidatorDirective,
  KzCpfCnpjValidatorDirective,
  KzTelefoneValidatorDirective,
  ModalUtilComponent,
  KzPaginacaoComponent,
  KzMaskDirective,
  KzMaskCurrencyDirective,
  KzPikadayDirective
} from '.';
import { CxOnlyNumberDirective } from './directives/cx-only-number.directive';
import { DigitalizacaoScannerComponent } from './components/digitalizacao-scanner/digitalizacao-scanner.component';
import { LoaderService } from '../services';
import { ExternalResourceService } from '../services/external-resource/external-resource.service';
import { KzCountCharacteresDirective } from './directives/kz-count-characters.directive';  
import { ContadorCaracteresDirective } from './directives/contador-caracteres.directive';
import { TipoContaCaixa } from './directives/tipo-conta-caixa.directive';

@NgModule({
  imports: [CommonModule, FormsModule, TemplateModule],
  declarations: [
    KzCepPipe,
    KzCpfPipe,
    KzCnpjPipe,
    KzCpfCnpjPipe,
    KzCpfValidatorDirective,
    KzCnpjValidatorDirective,
    KzCpfCnpjValidatorDirective,
    KzTelefoneValidatorDirective,
    ModalUtilComponent,
    KzPaginacaoComponent,
    KzMaskDirective,
    KzMaskCurrencyDirective,
    KzPikadayDirective,
    CxOnlyNumberDirective,
    DigitalizacaoScannerComponent,
    KzCountCharacteresDirective,
    ContadorCaracteresDirective,
    TipoContaCaixa
  ],
  providers: [CookieService,
    LoaderService, 
    ExternalResourceService],
  exports: [
    KzCepPipe,
    KzCpfPipe,
    KzCnpjPipe,
    KzCpfCnpjPipe,
    KzCpfValidatorDirective,
    KzCnpjValidatorDirective,
    KzCpfCnpjValidatorDirective,
    KzTelefoneValidatorDirective,
    ModalUtilComponent,
    KzPaginacaoComponent,
    KzMaskDirective,
    KzMaskCurrencyDirective,
    KzPikadayDirective,
    CommonModule,
    FormsModule,
    CxOnlyNumberDirective,
    DigitalizacaoScannerComponent,
    KzCountCharacteresDirective,
    ContadorCaracteresDirective,
    TipoContaCaixa
  ]
})
export class ShareModule { }
