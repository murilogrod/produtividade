import { Component, OnInit, ViewChild, ViewEncapsulation, ElementRef, AfterViewChecked, ChangeDetectorRef, AfterContentInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertMessageService, ApplicationService } from 'src/app/services';
import { SITUACAO_DOSSIE_PRODUTO, LOCAL_STORAGE_CONSTANTS, PROPERTY, EVENTO_DASHBOARD } from 'src/app/constants/constants';
import { UnidadeAnalisada } from '../model/unidade-analisada.model';
import { Dashboard } from '../model/dashboard.model';
import { DashboardViewChild } from '../model/dashboard.viewchild.model';
import { DashboardCompoenentPresenter } from '../presenter/dasboard.component.presenter';
import { LoadingModel } from '../tabela-situacao/model/loading.model';
declare var $: any;

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class DashComponent extends AlertMessageService implements OnInit, AfterViewChecked {
  @ViewChild('legenda') legenda: ElementRef;
  @ViewChild('resumoProcesso') resumoProcesso: ElementRef;
  dashboardPresenter: DashboardCompoenentPresenter;

  constructor(
    dashboardPresenter: DashboardCompoenentPresenter,
    private cdRef: ChangeDetectorRef,
    private router: Router,
    private applicationService: ApplicationService) {
    super();
    this.dashboardPresenter = dashboardPresenter;
    this.dashboardPresenter.unidadeAnalisada = new UnidadeAnalisada();
    this.dashboardPresenter.dashboard = new Dashboard();
    this.dashboardPresenter.dashboardViewChild = new DashboardViewChild();
    this.dashboardPresenter.loadingModel = new LoadingModel();
  }

  ngOnInit() {
    this.dashboardPresenter.getDados(this);
    this.init();
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  private init() {
    this.dashboardPresenter.dashboardViewChild.legenda = this.legenda;
    this.dashboardPresenter.dashboardViewChild.resumoProcesso = this.resumoProcesso;
    this.dashboardPresenter.dashboard.eventoProcesso = EVENTO_DASHBOARD.TODOS;
    this.dashboardPresenter.dashboard.eventoSituacao = EVENTO_DASHBOARD.TODOS;
    this.dashboardPresenter.unidadeAnalisada.unidade = JSON.parse(this.applicationService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO))[PROPERTY.CO_UNIDADE];
  }

  atualizarInformacoes() {
    this.handleChangeSituacaoProcesso(true);
    this.dashboardPresenter.getCustomDados(this);
  }

  pegaCor(situacao) {
    return this.dashboardPresenter.pegaCor(situacao);
  }

  pegaIcone(situacao) {
    return this.dashboardPresenter.pegaIcone(situacao);
  }

  pegaQtde(situacao) {
    return this.dashboardPresenter.pegaQtde(situacao);
  }

  changeTabResumoSituacao(item) {
    this.dashboardPresenter.changeTabResumoSituacao(item);
    this.filtraEventoSituacao(item);
  }

  changeTabResumoProcesso(evt) {
    this.dashboardPresenter.changeTabResumoProcesso(evt);
    this.filtraEventoProcesso(this.dashboardPresenter.dashboard.eventoProcesso);
  }

  formataSituacao(situacao) {
    return this.dashboardPresenter.formataSituacao(situacao);
  }

  consultarDossie(dossie) {
    this.router.navigate(['manutencaoDossie', dossie.id, SITUACAO_DOSSIE_PRODUTO.CONSULTAR]);
    return;
  }

  manterDossie(dossie) {
    this.router.navigate(['manutencaoDossie', dossie.id, SITUACAO_DOSSIE_PRODUTO.MANTER]);
    return;
  }

  filtraEventoSituacao(sitauacao: string) {
    this.dashboardPresenter.dashboard.eventoSituacao = sitauacao;
		this.dashboardPresenter.dashboard.listaSituacao = [];
		this.dashboardPresenter.carregarListaDossieSituacao(sitauacao);
  }

  filtraEventoProcesso(processo: string) {
    this.dashboardPresenter.dashboard.eventoProcesso = processo;
    this.dashboardPresenter.dashboard.listaProcesso  = [];
    if(processo === EVENTO_DASHBOARD.TODOS) {
      this.dashboardPresenter.dashboard.listaProcesso = this.dashboardPresenter.dashboard.estat.dossies_produto;
    }else {
      this.dashboardPresenter.dashboard.listaProcesso  = this.dashboardPresenter.getDossiesProcesso(processo, this.dashboardPresenter.dashboard.estat);
    }
  }

  hasCredentialUnidadeAnalisada(): boolean {
    return this.dashboardPresenter.hasCredentialUnidadeAnalisada();
  }

  handleChangeSituacaoProcesso(input) {
    this.dashboardPresenter.loadingModel.situacaoProcesso = input;
    this.dashboardPresenter.loadingModel.resunoProcesso = input;
    this.dashboardPresenter.loadingModel.resumoSituacao = input;
  }

  handleChangeContadorLista(input) {
    this.dashboardPresenter.dashboard.contadorLista = input;
  }

}
