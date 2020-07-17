import { Component, OnInit } from '@angular/core';
import { TipoRelacionamentoPresenter } from '../presenter/tipo-relacionamento.component.presenter';
import { TipoRelacionamentoModel } from '../model/tipo-relacionamento.model';
import { AlertMessageService } from 'src/app/services';
import { TIPO_RELACIONAMENTO } from '../../constant.tipo-relacionamento';
import { ActivatedRoute } from '@angular/router';
import { TipoRelacionamento } from '../model/tipo-relacionamento';

@Component({
  selector: 'app-tipo-relacionamento',
  templateUrl: './tipo-relacionamento.component.html',
  styleUrls: ['./tipo-relacionamento.component.css']
})
export class TipoRelacionamentoComponent extends AlertMessageService implements OnInit {

  tipoRelacionamentoPresenter: TipoRelacionamentoPresenter;

  constructor(tipoRelacionamentoPresenter: TipoRelacionamentoPresenter,
    private activatedRoute: ActivatedRoute) {
    super();
    this.tipoRelacionamentoPresenter = tipoRelacionamentoPresenter;
    this.tipoRelacionamentoPresenter.tipoRelacionamentoModel = new TipoRelacionamentoModel();
    this.tipoRelacionamentoPresenter.tipoRelacionamento = new TipoRelacionamento();
  }

  ngOnInit() {
    this.tipoRelacionamentoPresenter.initConfigTipoRelacionamento(this.activatedRoute.snapshot.params, this);
  }

  anularReceitaFisica() {
    this.tipoRelacionamentoPresenter.changeReceitaPJ();
  }

  anularReceitaJuridica() {
    this.tipoRelacionamentoPresenter.changeReceitaPF();
  }

  pesquisaTipoRelacionamento() {
    this.tipoRelacionamentoPresenter.navigateUrl([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_PESQUISA]);
  }

  salvarTipoRelacionamento() {
    this.tipoRelacionamentoPresenter.checkUpdateTipoRelacionamento(this, this.activatedRoute.snapshot.params);
  }

}
