import { Directive, ElementRef, Renderer2, Input, AfterViewInit, AfterViewChecked, OnChanges } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[contadorCaracteres]',
  host: {
    '(keyup)': '_onKeyUp()'
  }
})
export class ContadorCaracteresDirective implements  AfterViewInit, AfterViewChecked {
  
  private _defaultLimit: number = 200;
  private carregado: boolean = false;
  @Input('contadorCaracteres') _limit: number;

  constructor(private _elRef: ElementRef, private _renderer: Renderer2) {}

  private _onKeyUp() {
    let count: any = this._elRef.nativeElement.value.trim().length;
    this._renderer.setValue(this._elRef.nativeElement.nextElementSibling.firstChild ,(this._limit - count) + ' caracteres restantes.');
  }

  ngAfterViewInit():any {
    this._limit = this._limit || this._defaultLimit;
    const span = this._renderer.createElement('span');
    const text = this._renderer.createText((this._limit - this._elRef.nativeElement.value.trim().length) + ' caracteres restantes.');
    this._renderer.appendChild(span, text); 
    this._renderer.appendChild(this._elRef.nativeElement.parentElement, span);
    this._renderer.addClass(this._elRef.nativeElement.nextElementSibling, 'max-words');
  }

  ngAfterViewChecked() {
    let count: any = this._elRef.nativeElement.value.trim().length;
    this._renderer.setValue(this._elRef.nativeElement.nextElementSibling.firstChild ,(this._limit - count) + ' caracteres restantes.');
  }
}
