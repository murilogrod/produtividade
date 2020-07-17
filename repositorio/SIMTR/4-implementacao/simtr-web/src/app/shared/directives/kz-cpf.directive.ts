
import { Directive } from '@angular/core';
import { 
  AbstractControl, NG_VALIDATORS, Validator
} from '@angular/forms';

import { KzCpfValidator } from '../validators';

@Directive({
  selector: '[kz-cpf]',
  providers: [{
    provide: NG_VALIDATORS, 
    useExisting: KzCpfValidatorDirective, 
    multi: true 
  }]
})
export class KzCpfValidatorDirective implements Validator {

  /**
   * Valida os dados.
   *
   * @param AbstractControl control
   * @return object ou null caso válido
   */
  validate(control: AbstractControl): {[key: string]: any} {
    return KzCpfValidator.validate(control);
  }
}
