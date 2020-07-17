
import { Directive, HostListener, Input, ElementRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Directive({
  selector: '[kzMask]',
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: KzMaskDirective,
    multi: true
  }]
})
export class KzMaskDirective implements ControlValueAccessor {

  onTouched: any;
  onChange: any;

  @Input('kzMask') kzMask: string;

  constructor(private el: ElementRef) { }

  writeValue(value: any): void {
    if (value) {
      this.el.nativeElement.value = this.aplicarMascara(value);
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.el.nativeElement.disabled = isDisabled;
  }

  @HostListener('input', ['$event'])
  onInputValue($event : any) {
    this.onTouched(true);
    this.onKeydown($event);
  }

  @HostListener('keydown', ['$event'])
  onKeydown($event: any) {
    this.onTouched(true);
    let valor = $event.target.value;

    // retorna caso pressionado backspace
    if ($event.keyCode === 8) {
      this.onChange(valor);
      const regex = /\D/g;
      if (regex.test(valor.charAt(valor.length - 1))) {
        valor = valor.substring(0 , valor.length - 1);
      }
      $event.target.value = valor;
      return;
    }

    valor = $event.target.value.replace(/\D/g, '');

    const pad = this.kzMask.replace(/\D/g, '').replace(/9/g, '_');
    if (valor.length <= pad.length) {
      this.onChange(valor);
    }

    
    $event.target.value = this.aplicarMascara(valor);
  }

  @HostListener('blur', ['$event'])
  onBlur($event: any) {
    this.onTouched(true);
    if ($event.target.value.length === this.kzMask.length) {
      this.onKeydown($event);
    }
    return;
  }

  /**
   * Aplica a máscara a determinado valor.
   *
   * @param string valor
   * @return string
   */
  aplicarMascara(valor: string): string {
    valor = valor.replace(/\D/g, '');
    let pad = this.kzMask.replace(/\D/g, '').replace(/9/g, '_');
    let valorMask = valor + pad.substring(0, pad.length - valor.length);
    let valorMaskPos = 0;

    valor = '';
    for (let i = 0; i < this.kzMask.length; i++) {
      if (isNaN(parseInt(this.kzMask.charAt(i)))) {
        valor += this.kzMask.charAt(i);
      } else {
        valor += valorMask[valorMaskPos++];
      }
    }

    if (valor.indexOf('_') > -1) {
      valor = valor.substr(0, valor.indexOf('_'));
    }

    return valor;
  }
}
