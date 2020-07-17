import { Utils } from "src/app/utils/Utils";
import * as moment from 'moment';

export class UtilsDash {

	/**
	 * Adiciona zeros a esquerda caso necessário e realiza a formatação 
 	 * de acordo o tipo
	 * @param cnpj 
	 * @param cpf 
	 */
	static formatLikePerson(cnpj: string, cpf: string): string {
		let formattedValue: string = cnpj ? cnpj : cpf;
		if (cpf) {
			formattedValue = Utils.pad(cpf, 11);
			formattedValue = Utils.maskCpf(formattedValue);
		} else {
			formattedValue = Utils.pad(cnpj, 14);
			formattedValue = Utils.maskCnpj(formattedValue);
		}
		return formattedValue;
	}

    /**
     * Ordenacao pelo campo de data
     * @param value1 
     * @param value2 
     * @param result 
     * @param mask 
     */
	static sortDate(value1: any, value2: any, result: any, mask: string) {
		const date1: Date = moment(value1, mask).toDate();
		const date2: Date = moment(value2, mask).toDate();
		const result1: boolean = (moment(date2, mask).valueOf() > moment(date1, mask).valueOf());
		const result2: boolean = (moment(date2, mask).valueOf() < moment(date1, mask).valueOf());
		result = (result1) ? -1 : (result2) ? 1 : 0;
		return result;
	}
}