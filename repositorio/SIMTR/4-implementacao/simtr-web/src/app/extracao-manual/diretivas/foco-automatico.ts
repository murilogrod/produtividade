import { Directive, ElementRef, Input, HostListener } from '@angular/core';


@Directive({
  selector: '[focoAutomatico]'
  
})
export class FocoAutomaticoDirective  {
  @Input() ordem: number;
  constructor(private _el: ElementRef) {
    
   }

   ngAfterViewInit() {
    if (this.ordem == 1) {
      this._el.nativeElement.focus();
    }
  }
}