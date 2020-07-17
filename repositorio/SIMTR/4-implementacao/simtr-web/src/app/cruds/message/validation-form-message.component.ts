import { Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'validation-form-message',
  template: `
    <div *ngIf="isFormControlError()" class="error">
      {{ text }}
    </div>
  `,
  styles: []
})
export class ValidationFormMessageComponent {

  @Input() error: string;
  @Input() control: FormControl;
  @Input() text: string;

  isFormControlError(): boolean {
    return this.control && this.control.dirty && this.control.hasError(this.error);
  }

}
