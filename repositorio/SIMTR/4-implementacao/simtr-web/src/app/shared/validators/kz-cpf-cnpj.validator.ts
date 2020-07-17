/**
 * Validador de CPF/CNPJ.
 *
 */

import { AbstractControl } from '@angular/forms';

import { KzCpfValidator } from './kz-cpf.validator';
import { KzCnpjValidator } from './kz-cnpj.validator';

export class KzCpfCnpjValidator {

    /**
     * Método estático responsável pela validação dos dados.
     *
     * @param AbstractControl control
     * @return object ou null caso válido
     */
    static validate(control: AbstractControl): { [key: string]: boolean } {
        if (this.cpfCnpjValido(control.value)) {
            return null;
        }
        return { 'cpfCnpj': true };
    }

	/**
     * Valida um CPF/CNPJ.
     *
     * @param cpfCnpj valor do cpf/cnpj a ser validado.
     * @return boolean informando se o cpf/cnpj é válido ou não.
     */
    static cpfCnpjValido(cpfCnpj: any): boolean {

        if (!cpfCnpj) {
            return false;
        }

        var cpfCnpjValor = cpfCnpj.replace(/\D/g, '');

        var cpfCnpjValido = false;

        if (cpfCnpjValor.length === 11) {
            cpfCnpjValido = KzCpfValidator.cpfValido(cpfCnpjValor);
        } else if (cpfCnpjValor.length === 14) {
            cpfCnpjValido = KzCnpjValidator.cnpjValido(cpfCnpjValor);
        }

        return cpfCnpjValido;
    }
}
