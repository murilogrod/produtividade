import { Component, OnInit, Input, ChangeDetectorRef, AfterViewChecked } from '@angular/core';
import { SortEvent } from 'primeng/primeng';
import { TabelaProcessoComponentPresenter } from '../presenter/tabela-processo.component.presenter';
import { TabelaProcesso } from '../model/tabela-processo.model';

@Component({
	selector: 'tabela-processo',
	templateUrl: './tabela-processo.component.html',
	styleUrls: ['./tabela-processo.component.css']
})
export class TabelaProcessoComponent implements OnInit, AfterViewChecked {
	@Input() listaDossies: any[];
	@Input() tipoTabela: string;

	tabelaProcessoPresenter: TabelaProcessoComponentPresenter;

	constructor(private cdRef: ChangeDetectorRef,
		tabelaProcessoPresenter: TabelaProcessoComponentPresenter) {
		this.tabelaProcessoPresenter = tabelaProcessoPresenter;
		this.tabelaProcessoPresenter.tabelaProcesso = new TabelaProcesso();
	}

	ngOnInit() {
		this.tabelaProcessoPresenter.initConfigTable(this.listaDossies);
	}

	ngAfterViewChecked() {
		this.cdRef.detectChanges();
	}

	formatarData(value: string): string {
		return this.tabelaProcessoPresenter.formatDateViewDashboard(value);
	}

	formatarCpfCnpj(cnpj: string, cpf: string): string {
		return this.tabelaProcessoPresenter.formatLikePerson(cnpj, cpf);
	}

	formatarUnidadesProdutosContratados(values: string, product: boolean): string {
		return this.tabelaProcessoPresenter.formatUnidadesProdutosContratados(values, product);
	}

	formataTextoCaixaBaixa(value: string) {
		return this.tabelaProcessoPresenter.setLowerCaseText(value);
	}

	manterDossie(dossie) {
		this.tabelaProcessoPresenter.redirectManterManutecaoDossie(dossie);
	}

	consultarDossie(dossie) {
		this.tabelaProcessoPresenter.redirectConsultarManutecaoDossie(dossie);
	}

	verificarSituacao(dossie: any) {
		return this.tabelaProcessoPresenter.checkSituation(dossie);
	}

	verificarUnidadeAutorizada(dossie: any) {
		return this.tabelaProcessoPresenter.checkUnidadeAutorizada(dossie);
	}

	formatarHint(value: string): string {
		return this.tabelaProcessoPresenter.formatTooltipUnidadeProduto(value);
	}

	realizarOrdenacao(event: SortEvent) {
		this.tabelaProcessoPresenter.customSort(event);
	}
}
