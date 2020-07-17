
import { Directive, ElementRef, Renderer2, Input, AfterViewInit , AfterViewChecked } from '@angular/core';

@Directive({
  selector: '[kzCountCharacters]',
  host: {
    '(keyup)': '_onKeyUp()',
  }
})
export class KzCountCharacteresDirective implements AfterViewInit, AfterViewChecked {
  
  private _defaultLimit: number = 200;
  private carregado: boolean = false;
  @Input('kzCountCharacters') _limit: number;

  constructor(private _elRef: ElementRef, private _renderer: Renderer2) {}

  private _onKeyUp() {

    let count: any = this._elRef.nativeElement.value.length;
    this._renderer.setValue(this._elRef.nativeElement.nextElementSibling.firstChild ,(this._limit - count) + ' caracteres restantes.');
    
    if (count <= 10) {
      this._renderer.setStyle(this._elRef.nativeElement, 'border-color', 'red');
      this._renderer.setStyle(this._elRef.nativeElement.nextElementSibling, 'color', 'red');
    } else {
      this._renderer.setStyle(this._elRef.nativeElement, 'border-color', 'inherit');
      this._renderer.setStyle(this._elRef.nativeElement.nextElementSibling, 'color', 'inherit');
    }
  }

  ngAfterViewInit():any {
    this._limit = this._limit || this._defaultLimit;
    const span = this._renderer.createElement('span');
    const text = this._renderer.createText((this._limit - this._elRef.nativeElement.value.length) + ' caracteres restantes.');
    this._renderer.appendChild(span, text); 
    this._renderer.appendChild(this._elRef.nativeElement.parentElement, span);
    this._renderer.addClass(this._elRef.nativeElement.nextElementSibling, 'max-words');

  }

  ngAfterViewChecked() {
    if (!this.carregado && this._elRef.nativeElement.value.length > 0) {
      this._renderer.setValue(this._elRef.nativeElement.nextElementSibling.firstChild ,(this._limit - this._elRef.nativeElement.value.length) + ' caracteres restantes.');
      this.carregado = true;
    }
  }

}
