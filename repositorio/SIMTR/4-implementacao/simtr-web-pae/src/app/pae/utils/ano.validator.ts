import { AbstractControl } from '@angular/forms';

export class AnoValidator {
    static validate(control: AbstractControl): {[key: string]: boolean} {
        if (this.anoValido(control.value)) {
            return { 'ano': true };
        }
        return { 'ano': false };
    }

    static anoValido( ano: any): boolean {
        return ( ano >= 2018  && ano <= 2099 );
    }
}
