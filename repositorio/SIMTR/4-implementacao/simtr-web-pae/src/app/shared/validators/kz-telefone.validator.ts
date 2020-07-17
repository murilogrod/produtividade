/**
 * Validador de Telefone.
 *
 */

import { AbstractControl } from '@angular/forms';

export class KzTelfoneValidator {

    /**
     * Método estático responsável pela validação dos dados.
     *
     * @param AbstractControl control
     * @return object ou null caso válido
     */
    static validate(control: AbstractControl): {[key: string]: boolean} {
        if (this.telefoneValido(control.value)) {
            return null;
        }
        return { 'telefone': true };
    }

    static telefoneValido(telefone: any): boolean {
        const regex = /^\([0-9]{2}\)([0-9]{5}|[0-9]{4})-[0-9]{4}$/;
        const regexNumber = /^[0-9]{10}$|^[0-9]{11}$/;

        if (telefone != null) {
            if (telefone.indexOf("(") !== -1) {
                return regex.test(telefone);
            } else {
                return regexNumber.test(telefone);
            }
        }

        return false;
    }               
}
