import { Utils } from './../../../cruds/util/utils';
import { Injectable } from "@angular/core";
import { SortEvent } from 'primeng/primeng';
import { ComunicacaoJBPM } from './../../model/comunicacao-jbpm.model';
import { TabelaAnalitico } from './../model/tabela-analitico.model';

@Injectable()
export class TabelaAnaliticoComponentPresenter {

	tabelaAnalitico: TabelaAnalitico;

	constructor() {
	}

	initConfigTable(falhaJBPMs: any[]) {
		this.tabelaAnalitico.falhaJBPMs = Object.assign(new Array<any>(), falhaJBPMs);
		this.tabelaAnalitico.columns = this.tabelaAnalitico.configTableColsComunicacaoJBPM;
		this.custumizeRowsPerPageOptions();
		this.tabelaAnalitico.totalRecords = falhaJBPMs.length;
	}

	/**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
	setCountFilterGlobal(event: any, globalFilter: any) {
		if (globalFilter.value.length > 0) {
			this.tabelaAnalitico.filteredRecords = event.filteredValue.length;
		} else {
			this.tabelaAnalitico.filteredRecords = this.tabelaAnalitico.falhaJBPMs.length;
		}
	}

	/**
     * Realiza a ordenação dos campos da entidade Comunicacao Falha JBPM
     * @param event 
     */
	customSort(event: SortEvent) {
		event.data.sort((data1, data2) => {
			let value1 = data1[event.field];
			let value2 = data2[event.field];
			let result = null;
			if (value1 == null && value2 != null) {
				result = -1;
			} else if (value1 != null && value2 == null) {
				result = 1;
			} else if (value1 == null && value2 == null) {
				result = 0;
			} else {
				result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
			}
			return (event.order * result);
		});
	}

	/**
     * Filtra as falhas JBPM conforme as propriedades do mesmo;
     * Com pelo menos 2 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataAnaliticos 
     */
	filterFalhaBPMAnalitico(input: any, dataAnaliticos: any) {
		if (input.length == 0) {
			dataAnaliticos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
			this.tabelaAnalitico.filteredRecords = 0;
		}
		if (input.length > 1) {
			dataAnaliticos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
		}
	}

	/**
	* Custumiza o array de linhas por páginas; 
	* utilizando o padrão: 15, 30, 50 e último registro
	*/
	private custumizeRowsPerPageOptions() {
		this.tabelaAnalitico.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.tabelaAnalitico.falhaJBPMs);
	}

}