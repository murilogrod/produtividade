import { Injectable } from "@angular/core";
import * as moment from 'moment';
import { SortEvent } from 'primeng/primeng';
import { TabelaSinteticoProcesso } from '../model/tabela-sintetico-processo.model';
import { Utils } from './../../../cruds/util/utils';
import { ParametroTabela } from './../model/parametro-tabela';

@Injectable()
export class TabelaSinteticoProcessoComponentPresenter {

	tabelaSinteticoProcesso: TabelaSinteticoProcesso;
	listaSinteticoProcesso: ParametroTabela[];

	constructor() {
	}

	initConfigTable(falhaJBPMs: any[]) {
		this.tabelaSinteticoProcesso.falhaJBPMs = Object.assign(new Array<any>(), falhaJBPMs);
		this.tabelaSinteticoProcesso.columns = this.tabelaSinteticoProcesso.configTableColsComunicacaoJBPM;
		this.carregarFalhaJBPMPorProcesso();
		this.tabelaSinteticoProcesso.totalRecords = this.tabelaSinteticoProcesso.falhaJBPMs.length;
	}

	carregarFalhaJBPMPorProcesso() {
		if (this.tabelaSinteticoProcesso.falhaJBPMs !== undefined && this.tabelaSinteticoProcesso.falhaJBPMs !== []) {
			this.listaSinteticoProcesso = new Array<ParametroTabela>();
			this.tabelaSinteticoProcesso.falhaJBPMs.forEach(falhaJBPM => {	
				if (this.listaSinteticoProcesso.length <= 0) {
					this.listaSinteticoProcesso.push(this.newParamentroTabela(falhaJBPM));
				} else {
					let parametroAux = this.listaSinteticoProcesso.find((sinteticoProcesso) => sinteticoProcesso.processo_id == falhaJBPM.processo_origem_id);
					if (parametroAux) {						
						parametroAux.quantidade += 1;
						if (moment(falhaJBPM.data_hora_falha, 'DD/MM/YYYY HH:mm:ss').valueOf() < 
						    moment(parametroAux.data_hora_inicial, 'DD/MM/YYYY HH:mm:ss').valueOf()) {	
							parametroAux.data_hora_inicial = falhaJBPM.data_hora_falha;
						} else if (moment(falhaJBPM.data_hora_falha, 'DD/MM/YYYY HH:mm:ss').valueOf() >
						           moment(parametroAux.data_hora_final, 'DD/MM/YYYY HH:mm:ss').valueOf()) {	
						           parametroAux.data_hora_final = falhaJBPM.data_hora_falha;
						}
						let indice= this.listaSinteticoProcesso.findIndex(parametro => parametro.processo_id == parametroAux.processo_id);
						this.listaSinteticoProcesso[indice] = parametroAux;
					} else {
						this.listaSinteticoProcesso.push(this.newParamentroTabela(falhaJBPM));
					}
				}				
			});
		}
		this.tabelaSinteticoProcesso.falhaJBPMs = this.listaSinteticoProcesso;
		this.tabelaSinteticoProcesso.falhaJBPMs.sort(function (n1, n2) {
			return n1.processo_id - n2.processo_id;
		});
		this.custumizeRowsPerPageOptions();
	}

	private newParamentroTabela(falhaJBPM: any) {
		let parametro = new ParametroTabela();
		parametro.processo_id = falhaJBPM.processo_origem_id;
		parametro.processo = falhaJBPM.processo_origem_id_nome;
		parametro.bpm_container = falhaJBPM.bpm_container;
		parametro.bpm_processo = falhaJBPM.bpm_processo;
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
			this.tabelaSinteticoProcesso.filteredRecords = event.filteredValue.length;
		} else {
			this.tabelaSinteticoProcesso.filteredRecords = this.tabelaSinteticoProcesso.falhaJBPMs.length;
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
     * @param dataSinteticoProcessos 
     */
	filterFalhaBPMSinteticoProcesso(input: any, dataSinteticoProcessos: any) {
		if (input.length == 0) {
			dataSinteticoProcessos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
			this.tabelaSinteticoProcesso.filteredRecords = 0;
		}
		if (input.length > 1) {
			dataSinteticoProcessos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
		}
	}

	/**
	* Custumiza o array de linhas por páginas; 
	* utilizando o padrão: 15, 30, 50 e último registro
	*/
	private custumizeRowsPerPageOptions() {
		this.tabelaSinteticoProcesso.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.tabelaSinteticoProcesso.falhaJBPMs);
	}

}