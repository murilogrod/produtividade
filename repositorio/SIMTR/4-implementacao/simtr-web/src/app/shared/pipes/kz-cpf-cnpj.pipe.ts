
import { Pipe, PipeTransform } from '@angular/core';

import { KzCpfPipe } from './kz-cpf.pipe';
import {KzCnpjPipe} from './kz-cnpj.pipe';
 
@Pipe({
	name: 'kzCpfCnpj'
})
export class KzCpfCnpjPipe implements PipeTransform {

    private kzCpfPipe: KzCpfPipe;
    private kzCnpjPipe: KzCnpjPipe;

    /**
     * Construtor da classe.
     */
    constructor() {
        this.kzCpfPipe = new KzCpfPipe();
        this.kzCnpjPipe = new KzCnpjPipe();
    }

	/**
	 * Formata um CPF/CNPJ ou retorna seu valor passado caso inválido. 
     * O CPF/CNPJ informado deve ser composto por 11 ou 14 caracteres 
     * numéricos respectivamente.
	 *
	 * @param string cpfCnpj
	 * @return string
	 */
	transform(cpfCnpj: string): string {
 		 if (!cpfCnpj) {
            return '';
        }
    
        const cpfCnpjValor = cpfCnpj.replace(/\D/g, '');
    
        if (cpfCnpjValor.length === 11) {
            cpfCnpj = this.kzCpfPipe.transform(cpfCnpjValor);
        } else if (cpfCnpjValor.length === 14) {
            cpfCnpj = this.kzCnpjPipe.transform(cpfCnpjValor);
        }
        
        return cpfCnpj;
	}
}
