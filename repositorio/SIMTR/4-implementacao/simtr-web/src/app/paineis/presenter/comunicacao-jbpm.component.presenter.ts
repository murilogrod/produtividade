import { Injectable } from "@angular/core";
import { SortEvent } from 'primeng/primeng';
import { LoaderService } from "src/app/services";
import { ComunicacaoJBPMService } from './../comunicacao-jbpm.service';
import { ComunicacaoJBPM } from './../model/comunicacao-jbpm.model';

@Injectable()
export class ComunicacaoJBPMComponentPresenter {

	comunicacaoJBPM: ComunicacaoJBPM;

	constructor(private comunicacaoJBPMService: ComunicacaoJBPMService,
		        private loadService: LoaderService) {
	}

	initComunicacaoJBPM() {
		this.recuperarFalhaJBPM();
	}

	recuperarFalhaJBPM() {
		this.loadService.show();
		this.comunicacaoJBPMService.getConsultaFalhaBPM().subscribe(dados => {
			this.comunicacaoJBPM.falhaJBPMs = dados;
			this.classificaItens();
			this.loadService.hide();
		}, error => {
			this.loadService.hide();
			throw error;
		});
	}

	classificaItens() {
        if (this.comunicacaoJBPM.falhaJBPMs !== undefined && this.comunicacaoJBPM.falhaJBPMs !== []) {
            this.comunicacaoJBPM.falhaJBPMs.sort(function (n1, n2) {
                return n1.id - n2.id;
            });
            for (let item of this.comunicacaoJBPM.falhaJBPMs) {
				
				item.bpm_processo = item.bpm_processo ? item.bpm_processo.normalize("NFD").replace(/[\u0300-\u036f]/g, '') : "";
				
				if (item.processo_origem) {
					item.processo_origem_id = item.processo_origem.id;
					item.processo_origem_nome = item.processo_origem.nome;
					item.processo_origem_id_nome = item.processo_origem.id  + " - " + item.processo_origem.nome;
				}
            }
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

}