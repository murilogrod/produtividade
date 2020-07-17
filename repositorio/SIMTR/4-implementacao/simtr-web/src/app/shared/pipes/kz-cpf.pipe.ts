import { Utils } from '../../utils/Utils';

import { Pipe, PipeTransform } from '@angular/core';
 
@Pipe({
	name: 'kzCpf'
})
export class KzCpfPipe implements PipeTransform {

	/**
	 * Formata um CPF ou retorna seu valor passado caso inválido. 
     * O CPF informado deve ser composto por 11 caracteres numéricos.
	 *
	 * @param string cpf
	 * @return string
	 */
	transform(cpf: string): string {
 		if (!cpf) {
            return '';
        }
    
        let cpfValor = cpf.replace(/\D/g, '');
    
        if (cpfValor.length !== 11) {
            cpf = Utils.leftPad(cpf, 11, '0');
            cpfValor = cpf.replace(/\D/g, '');
        }
        
        const cpfLista = cpfValor.match(/^(\d{3})(\d{3})(\d{3})(\d{2})$/);
        
        if (cpfLista && cpfLista.length === 5) {
            cpf = cpfLista[1] + '.' + cpfLista[2] + '.' + cpfLista[3] + '-' + cpfLista[4];
        }
        
        return cpf;
	}
}
