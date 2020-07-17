import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PesquisaChecklist } from '../model/pesquisa-checklist.model';
import { AlertMessageService } from 'src/app/services';
import { SortEvent } from 'primeng/primeng';
import { GridChecklist } from '../model/grid-checklist.model';
import { CHECKLIST } from '../../constant.checklist';
import { PesquisaCheckListPresenter } from '../presenter/pesquisa-checklist.component.presenter';

@Component({
  selector: 'app-checklist',
  templateUrl: './pesquisa-checklist.component.html',
  styleUrls: ['./pesquisa-checklist.component.css']
})
export class PesquisaChecklistComponent extends AlertMessageService implements OnInit {

  pesquisaCheckListPresenter: PesquisaCheckListPresenter;

  constructor(pesquisaCheckListPresenter: PesquisaCheckListPresenter) {
    super();
    this.pesquisaCheckListPresenter = pesquisaCheckListPresenter;
    this.pesquisaCheckListPresenter.pesquisaChecklist = new PesquisaChecklist();
  }

  ngOnInit() {
    this.pesquisaCheckListPresenter.initConfigListaChecklist(this);
  }

  novoChecklist() {
    this.pesquisaCheckListPresenter.navigateUrl([CHECKLIST.CHECKLIST_NOVO]);
  }

  editarChecklist(gridChecklist: GridChecklist) {
    if (gridChecklist.quantidade_associacoes > 0) {
      this.pesquisaCheckListPresenter.navigateUrl([CHECKLIST.CHECKLIST, gridChecklist.id, gridChecklist.quantidade_associacoes]);
    } else {
      this.pesquisaCheckListPresenter.navigateUrl([CHECKLIST.CHECKLIST, gridChecklist.id]);
    }
  }

  realizarOrdenacao(event: SortEvent) {
    this.pesquisaCheckListPresenter.customSort(event);
  }

  selecionarFasesProcessoGeraDossie() {
    this.pesquisaCheckListPresenter.getFasesGeraDossie();
  }

  anularRegistrosFiltrados() {
    this.pesquisaCheckListPresenter.anularRegistrosFiltrados();
  }

  mostrarChecklistsPorSituacao() {
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  filtrarChecklistsPorProcesso() {
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  filtrarChecklistsPorProcessoGeraDossieFase() {
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  filtrarChecklistsPorTipoDocumento() {
    this.pesquisaCheckListPresenter.inicializarFuncoesDocumentais();
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  filtrarChecklistsPorFuncaoDocumental() {
    this.pesquisaCheckListPresenter.inicializarTiposDocumentos();
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  realizarFiltroSituacaoChecklist() {
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  realizarFiltroVerificacaoPrevia() {
    this.pesquisaCheckListPresenter.filtrarPorSelecaoFiltros();
  }

  realizarFiltroChecklists(input: any, dataChecklists: any) {
    this.pesquisaCheckListPresenter.filterChecklists(input, dataChecklists);
  }

  abrirModalRemocaoChecklist(gridChecklist: GridChecklist) {
    this.pesquisaCheckListPresenter.openModalCloneInativarRemoverChecklist(this, gridChecklist, false);
  }

  abrirModalCloneChecklist(gridChecklist: GridChecklist) {
    this.pesquisaCheckListPresenter.openModalCloneInativarRemoverChecklist(this, gridChecklist, true);
  }

  onFilter(event: any, globalFilter: any) {
    this.pesquisaCheckListPresenter.setCountFilterGlobal(event, globalFilter);
  }

}
