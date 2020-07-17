import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChecklistPresenter } from '../presenter/checklist.component.presenter';
import { AlertMessageService } from 'src/app/services';
import { CHECKLIST } from '../../constant.checklist';
import { ChecklistModel } from '../model/checklist-component.model';
import { Checklist } from 'src/app/cruds/model/checklist.model';
import { Apontamento } from 'src/app/cruds/model/apontamento.model';
import { Vinculacao } from 'src/app/cruds/model/vinculacao.model';
import { VinculacaoChecklist } from 'src/app/cruds/model/vinculacao-checklist';

@Component({
  selector: 'app-checklist',
  templateUrl: './checklist.component.html',
  styleUrls: ['./checklist.component.css']
})
export class ChecklistComponent extends AlertMessageService implements OnInit {

  checklistPresenter: ChecklistPresenter;
  @ViewChild('unidade') unidade: ElementRef;

  constructor(checklistPresenter: ChecklistPresenter,
    private activatedRoute: ActivatedRoute) {
    super();
    this.checklistPresenter = checklistPresenter;
    this.checklistPresenter.checklistModel = new ChecklistModel();
    this.checklistPresenter.checklist = new Checklist();
  }

  ngOnInit() {
    this.checklistPresenter.initConfigChecklist(this.activatedRoute.snapshot.params, this);
  }

  pesquisaChecklist() {
    this.checklistPresenter.navigateUrl([CHECKLIST.CHECKLIST_PESQUISA]);
  }

  handleChangeApontamentos(apontamentos: Array<Apontamento>) {
    this.checklistPresenter.inicializarApontamentos(apontamentos);
  }

  handleChangeApontamentosRemovidos(apontamentosRemovidos: Array<Apontamento>) {
    this.checklistPresenter.inicializarApontamentosRemovidos(apontamentosRemovidos);
  }

  handleChangeVinculacoes(vinculacoesChecklists: Array<VinculacaoChecklist>) {
    this.checklistPresenter.inicializarVinculacoes(vinculacoesChecklists);
  }

  handleChangeVinculacoesRemovidas(vinculacoesRemovidas: Array<Vinculacao>) {
    this.checklistPresenter.inicializarVinculacoesRemovidas(vinculacoesRemovidas);
  }

  formatarUnidade() {
    this.checklistPresenter.formatarUnidadeValida(this.unidade);
  }

  salvarChecklist() {
    this.checklistPresenter.verificarOcorrenciaMesmoNomeChecklists(this);
  }

}
