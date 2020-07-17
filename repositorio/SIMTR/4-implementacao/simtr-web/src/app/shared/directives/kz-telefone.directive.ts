
import { Directive } from '@angular/core';
import { 
  AbstractControl, NG_VALIDATORS, Validator
} from '@angular/forms';

import { KzTelfoneValidator } from '../validators';

@Directive({
  selector: '[kz-telefone]',
  providers: [{
    provide: NG_VALIDATORS, 
    useExisting: KzTelefoneValidatorDirective, 
    multi: true 
  }]
})
export class KzTelefoneValidatorDirective implements Validator {

  /**
   * Valida os dados.
   *
   * @param AbstractControl control
   * @return object ou null caso válido
   */
  validate(control: AbstractControl): {[key: string]: any} {
    return KzTelfoneValidator.validate(control);
  }
}
