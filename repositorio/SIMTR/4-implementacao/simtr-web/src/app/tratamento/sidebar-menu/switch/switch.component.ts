import { Component, OnInit, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { SingleExpand } from 'src/app/model/model-tratamento/singleExpand';
import { SWITCH_TRATMENTO } from 'src/app/constants/constants';

@Component({
	selector: 'switch',
	templateUrl: './switch.component.html',
	styleUrls: ['./switch.component.css']
})
export class SwitchComponent implements OnChanges {

	@Input() multiple: boolean;

	@Input() title: string;
	@Input() tooltip: string;
	@Input() tipoArvore: string;

	@Output() handleChangeExpandAll: EventEmitter<any> = new EventEmitter<any>();

	@Input() singleExpandAllDocuments: boolean;

	@Output() handleChangeExpandSingleAll: EventEmitter<any> = new EventEmitter<any>();

	expandMultiple: boolean;

	expandSingle: boolean;

	constructor() { }
	ngOnChanges() {
		const singleExpand = new SingleExpand();
		this.expandDossieFase(singleExpand, this.singleExpandAllDocuments);
		this.expandClientes(singleExpand, this.singleExpandAllDocuments);
		this.expandProdutos(singleExpand, this.singleExpandAllDocuments);
		this.expandGarantias(singleExpand, this.singleExpandAllDocuments);
	}

	private expandDossieFase(singleExpand: SingleExpand, ativo:boolean) {
		singleExpand.tipoArvore = SWITCH_TRATMENTO.DOSSIE_FASE;
		singleExpand.toogleValue = ativo;
		this.expandSingle = this.singleExpandAllDocuments;
		this.handleChangeExpandSingleAll.emit(singleExpand);
	}

	private expandClientes(singleExpand: SingleExpand, ativo:boolean) {
		singleExpand.tipoArvore = SWITCH_TRATMENTO.CLIENTE;
		singleExpand.toogleValue = ativo;
		this.expandSingle = this.singleExpandAllDocuments;
		this.handleChangeExpandSingleAll.emit(singleExpand);
	}

	private expandProdutos(singleExpand: SingleExpand, ativo:boolean) {
		singleExpand.tipoArvore = SWITCH_TRATMENTO.PRODUTO;
		singleExpand.toogleValue = ativo;
		this.expandSingle = this.singleExpandAllDocuments;
		this.handleChangeExpandSingleAll.emit(singleExpand);
	}

	private expandGarantias(singleExpand: SingleExpand, ativo:boolean) {
		singleExpand.tipoArvore = SWITCH_TRATMENTO.GARANTIAS;
		singleExpand.toogleValue = ativo;
		this.expandSingle = this.singleExpandAllDocuments;
		this.handleChangeExpandSingleAll.emit(singleExpand);
	}
	expandMultipleAll() {
		this.handleChangeExpandAll.emit(this.expandMultiple);
	}

	expandSingleAll() {
		const singleExpand = new SingleExpand();
		singleExpand.tipoArvore = this.tipoArvore;
		singleExpand.toogleValue = this.expandSingle;
		this.expandSingle = this.singleExpandAllDocuments;
		this.handleChangeExpandSingleAll.emit(singleExpand);
	}

}
