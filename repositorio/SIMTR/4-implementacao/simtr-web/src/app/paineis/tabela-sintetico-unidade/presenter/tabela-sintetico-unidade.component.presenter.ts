import { Injectable } from "@angular/core";
import { SortEvent } from 'primeng/primeng';
import { TabelaSinteticoUnidade } from '../model/tabela-sintetico-unidade.model';
import { Utils } from './../../../cruds/util/utils';
import * as moment from 'moment';
import { ParametroTabela } from './../model/parametro-tabela';

@Injectable()
export class TabelaSinteticoUnidadeComponentPresenter {

	tabelaSinteticoUnidade: TabelaSinteticoUnidade;
	listaSinteticoUnidade: ParametroTabela[];

	constructor() {
	}

	initConfigTable(falhaJBPMs: any[]) {
		this.tabelaSinteticoUnidade.falhaJBPMs = Object.assign(new Array<any>(), falhaJBPMs);
		this.tabelaSinteticoUnidade.columns = this.tabelaSinteticoUnidade.configTableColsComunicacaoJBPM;
		this.carregarFalhaJBPMPorUnidade();
		this.tabelaSinteticoUnidade.totalRecords = this.tabelaSinteticoUnidade.falhaJBPMs.length;
	}

	carregarFalhaJBPMPorUnidade() {
		if (this.tabelaSinteticoUnidade.falhaJBPMs !== undefined && this.tabelaSinteticoUnidade.falhaJBPMs !== []) {
			this.listaSinteticoUnidade = new Array<ParametroTabela>();
			this.tabelaSinteticoUnidade.falhaJBPMs.forEach(falhaJBPM => {	
				if (this.listaSinteticoUnidade.length <= 0) {
					this.listaSinteticoUnidade.push(this.newParamentroTabela(falhaJBPM));
				} else {
					let parametroAux = this.listaSinteticoUnidade.find((sinteticoUnidade) => sinteticoUnidade.unidade == falhaJBPM.unidade_criacao);
					if (parametroAux) {						
						parametroAux.quantidade += 1;
						if (moment(falhaJBPM.data_hora_falha, 'DD/MM/YYYY HH:mm:ss').valueOf() < 
						    moment(parametroAux.data_hora_inicial, 'DD/MM/YYYY HH:mm:ss').valueOf()) {	
							parametroAux.data_hora_inicial = falhaJBPM.data_hora_falha;
						} else if (moment(falhaJBPM.data_hora_falha, 'DD/MM/YYYY HH:mm:ss').valueOf() >
						           moment(parametroAux.data_hora_final, 'DD/MM/YYYY HH:mm:ss').valueOf()) {	
						           parametroAux.data_hora_final = falhaJBPM.data_hora_falha;
						}
						let indice= this.listaSinteticoUnidade.findIndex(parametro => parametro.unidade == parametroAux.unidade);
						this.listaSinteticoUnidade[indice] = parametroAux;
					} else {
						this.listaSinteticoUnidade.push(this.newParamentroTabela(falhaJBPM));
					}
				}				
			});
		}
		this.tabelaSinteticoUnidade.falhaJBPMs = this.listaSinteticoUnidade;
		this.tabelaSinteticoUnidade.falhaJBPMs.sort(function (n1, n2) {
			return n1.unidade - n2.unidade;
		});
		this.custumizeRowsPerPageOptions();
	}

	private newParamentroTabela(falhaJBPM: any) {
		let parametro = new ParametroTabela();
		parametro.unidade = falhaJBPM.unidade_criacao;
		parametro.quantidade += 1;
		parametro.data_hora_inicial = falhaJBPM.data_hora_falha;
		parametro.data_hora_final = falhaJBPM.data_hora_falha;
		return parametro;
	}

	/**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
	setCountFilterGlobal(event: any, globalFilter: any) {
		if (globalFilter.value.length > 0) {
			this.tabelaSinteticoUnidade.filteredRecords = event.filteredValue.length;
		} else {
			this.tabelaSinteticoUnidade.filteredRecords = this.tabelaSinteticoUnidade.falhaJBPMs.length;
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
     * @param dataSinteticoUnidades 
     */
	filtroSinteticoPorUnidade(input: any, dataSinteticoUnidades: any) {
		if (input.length == 0) {
			dataSinteticoUnidades.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
			this.tabelaSinteticoUnidade.filteredRecords = 0;
		}
		if (input.length > 1) {
			dataSinteticoUnidades.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
		}
	}

	/**
	* Custumiza o array de linhas por páginas; 
	* utilizando o padrão: 15, 30, 50 e último registro
	*/
	private custumizeRowsPerPageOptions() {
		this.tabelaSinteticoUnidade.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.tabelaSinteticoUnidade.falhaJBPMs);
	}
}