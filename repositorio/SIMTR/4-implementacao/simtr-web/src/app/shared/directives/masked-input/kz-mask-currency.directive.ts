
import { 
  Directive, 
  HostListener, 
  Input, 
  OnInit,
  ElementRef 
} from '@angular/core';
import { 
  NG_VALUE_ACCESSOR, ControlValueAccessor 
} from '@angular/forms';

@Directive({
  selector: '[kzMaskCurrency]',
  providers: [{
    provide: NG_VALUE_ACCESSOR, 
    useExisting: KzMaskCurrencyDirective, 
    multi: true 
  }]
})
export class KzMaskCurrencyDirective implements ControlValueAccessor, OnInit {

  onTouched: any;
  onChange: any;

  separadorDecimal: string;
  separadorMilhar: string;
  prefixo: string;

  @Input('kzMaskCurrency') kzMask: any;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    this.separadorDecimal = this.kzMask.decimal || ',';
    this.separadorMilhar = this.kzMask.milhar || '.';
    this.prefixo = this.kzMask.prefixo || '';
  }

  writeValue(value: any): void {
    if (value) {
      if (!isNaN(value)) {
        value = value.toFixed(2);
      }
      this.el.nativeElement.value = this.aplicarMascara(String(value));
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  @HostListener('keyup', ['$event']) 
  onKeyup($event: any) {

    let valor: string = this.aplicarMascara($event.target.value);

    if (valor === '') {
      this.onChange('');
      $event.target.value = '';
      return;
    }

    this.verificaSeparadorDecimal(this.separadorDecimal, valor);

    valor = this.formataValorConformeLength($event, valor);

    $event.target.value = valor;

  }


  /**
   * Verifica o tamanho maximo do valor digitado no input e o formata de forma que o maxlength 
   * seja igual o valor configurado; considerando ponto e virgula.
   * Necessário para a diretiva não invalidar o formulário
   * 
   * @param any event
   * 
   */
  formataValorConformeLength($event: any, valor: string) : string {
    let nummask = valor.split(".").length-1 + valor.split(",").length-1;
    let lengthValor = $event.target.maxLength + $event.target.value.split(".").length-1 + $event.target.value.split(",").length-1;

    if($event.target.value.length == lengthValor || valor.length > $event.target.maxLength) {
      let aux = this.aplicarMascara($event.target.value.substring(0, lengthValor - nummask));
      this.verificaSeparadorDecimal(this.separadorDecimal, aux);
      return aux;
    } 

    return valor;
  }

  /**
   * Verifica o separador decimal.
   *
   * @param string separadorDecimal
   * @param string value
   * 
   */
  verificaSeparadorDecimal(separadorDecimal: string, value: string){
    if (this.separadorDecimal === ',') {
      this.onChange(value.replace(/\./g, '').replace(',', '.'));
    } else {
      this.onChange(value.replace(/\,/g, ''));
    }
  }


  @HostListener('blur', ['$event']) 
  onBlur($event: any) {
    const pattern = '0' + this.separadorDecimal + '00';
    if ($event.target.value === pattern) {
      this.onChange('');
      $event.target.value = '';
    }
    
  }

  /**
   * Aplica a máscara a determinado valor.
   *
   * @param string valorConverter
   * @return string
   */
  aplicarMascara(valorConverter: string): string {
    const valorNum = parseInt(valorConverter.replace(/\D/g, ''), 10);
    let valorMask = '';
    let valor: string;

    if (isNaN(valorNum)) {
      return '';
    }

    valor = valorNum.toString();
    switch (valor.length) {
       case 1:
         valorMask = '0' + this.separadorDecimal + 
           '0' + valor;
         break;
       case 2:
         valorMask = '0' + this.separadorDecimal + valor;
         break;
       case 3:
         valorMask = valor.substr(0, 1) + this.separadorDecimal + 
           valor.substr(1, 2);
         break;
       default:
         break;
     }

    if (valorMask === '') {
      let sepMilhar = 0;
      for (let i = (valor.length - 3); i >= 0; i--) {
        if (sepMilhar === 3) {
          valorMask = this.separadorMilhar + valorMask;
          sepMilhar = 0;
        }
        valorMask = valor.charAt(i) + valorMask;
        sepMilhar++;
      }
      valorMask = valorMask + this.separadorDecimal + 
        valor.substr(valor.length - 2, 2);
    }

    if (this.prefixo !== '') {
      valorMask = this.prefixo + ' ' + valorMask;
    }
    
    return valorMask;
  }

}
