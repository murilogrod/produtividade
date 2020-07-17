import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CanalPresenter } from '../presenter/canal.component.presenter';
import { Canal } from '../model/canal.model';
import { IncludeCanal } from "../model/include-canal.model";
import { CANAL } from '../../constant.canal';
import { AlertMessageService } from 'src/app/services';

@Component({
  selector: 'app-canal',
  templateUrl: './canal.component.html',
  styleUrls: ['./canal.component.css']
})
export class CanalComponent extends AlertMessageService implements OnInit {

  canalPresenter: CanalPresenter

  constructor(canalPresenter: CanalPresenter,
    private activatedRoute: ActivatedRoute) {
    super();
    this.canalPresenter = canalPresenter;
    this.canalPresenter.canal = new Canal();
    this.canalPresenter.includeCanal = new IncludeCanal();
  }

  ngOnInit() {
    this.canalPresenter.initConfigCanal(this.activatedRoute.snapshot.params, this);
  }

  pesquisaCanal() {
    this.canalPresenter.navigateUrl([CANAL.CANAL_PESQUISA]);
  }

  salvarCanal() {
    this.canalPresenter.checkUpdateCanal(this, this.activatedRoute.snapshot.params);
  }

}
