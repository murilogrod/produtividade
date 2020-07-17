import { Component, OnInit, Input, ChangeDetectorRef, AfterViewChecked, Output, EventEmitter, OnChanges, ViewChild } from '@angular/core';
import { TabelaSituacaoComponentPresenter } from '../presenter/tabela-situacao.component.presenter';
import { TabelaSituacao } from '../model/tabela-situacao.model';
import { Table } from 'primeng/table';
import { SortEvent } from 'primeng/primeng';

@Component({
	selector: 'tabela-situacao',
	templateUrl: './tabela-situacao.component.html',
	styleUrls: ['./tabela-situacao.component.css']
})
export class TabelaSituacaoComponent implements OnInit, AfterViewChecked {
	@Input() listaDossies: any[];
	@Input() tipoTabela: string;

	tabelaSituacaoPresenter: TabelaSituacaoComponentPresenter;

	constructor(private cdRef: ChangeDetectorRef,
		tabelaSituacaoPresenter: TabelaSituacaoComponentPresenter) {
		this.tabelaSituacaoPresenter = tabelaSituacaoPresenter;
		this.tabelaSituacaoPresenter.tabelaSituacao = new TabelaSituacao();
	}

	ngOnInit() {
		this.tabelaSituacaoPresenter.initConfigTable(this.listaDossies);
	}

	ngAfterViewChecked() {
		this.cdRef.detectChanges();
	}

	formatarData(value: string): string {
		return this.tabelaSituacaoPresenter.formatDateViewDashboard(value);
	}

	formatarCpfCnpj(cnpj: string, cpf: string): string {
		return this.tabelaSituacaoPresenter.formatLikePerson(cnpj, cpf);
	}

	formatarUnidadesProdutosContratados(values: string, product: boolean): string {
		return this.tabelaSituacaoPresenter.formatUnidadesProdutosContratados(values, product);
	}

	formataTextoCaixaBaixa(value: string) {
		return this.tabelaSituacaoPresenter.setLowerCaseText(value);
	}

	manterDossie(dossie) {
		this.tabelaSituacaoPresenter.redirectManterManutecaoDossie(dossie);
	}

	consultarDossie(dossie) {
		this.tabelaSituacaoPresenter.redirectConsultarManutecaoDossie(dossie);
	}

	verificarSituacao(situacao_atual: string) {
		return this.tabelaSituacaoPresenter.checkSituation(situacao_atual);
	}

	formatarHint(value: string): string {
		return this.tabelaSituacaoPresenter.formatTooltipUnidadeProduto(value);
	}

	realizarOrdenacao(event: SortEvent) {
		this.tabelaSituacaoPresenter.customSort(event);
	}

	verificarUnidadeAutorizada(dossie: any) {
		return this.tabelaSituacaoPresenter.checkUnidadeAutorizada(dossie);
	}
}
