import { Directive, ElementRef, HostListener, AfterViewInit, AfterViewChecked } from '@angular/core';
import { Utils } from '../../utils/Utils';

@Directive({
  selector: '[tipo-conta-caixa]'
})
export class TipoContaCaixa {

  constructor(private _el: ElementRef) {}
 
  @HostListener('input', ['$event']) onInputChange(event) {
    let initalValue = this._el.nativeElement.value;
    let resposta = Utils.validarKeyupTipoCampoContaCaixa(initalValue);
    console.log("keyup: " + resposta);
    this._el.nativeElement.value = resposta;
  }

}
