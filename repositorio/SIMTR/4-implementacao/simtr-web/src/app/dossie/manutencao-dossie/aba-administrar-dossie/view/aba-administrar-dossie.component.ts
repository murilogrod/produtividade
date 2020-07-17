import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AbaAdministrarDossieComponentPresenter } from '../presenter/aba-administrar-dossie.component.presenter';
import { AbaAdministrarDossie } from '../model/aba-administrar-dossie.model';
import { AlertMessageService } from 'src/app/services';

@Component({
  selector: 'aba-administrar-dossie',
  templateUrl: './aba-administrar-dossie.component.html',
  styleUrls: ['./aba-administrar-dossie.component.css']
})
export class AbaAdministrarDossieComponent extends AlertMessageService implements OnInit {

  @Input() dossieProduto: any;
  @Output() loadDossieProdutoChanged: EventEmitter<void> = new EventEmitter<void>();

  abaAdministrarDossieComponentPresenter: AbaAdministrarDossieComponentPresenter

  constructor(abaAdministrarDossieComponentPresenter: AbaAdministrarDossieComponentPresenter) {
    super();
    this.abaAdministrarDossieComponentPresenter = abaAdministrarDossieComponentPresenter;
    this.abaAdministrarDossieComponentPresenter.abaAdministrarDossie = new AbaAdministrarDossie();
  }

  ngOnInit() {
    this.abaAdministrarDossieComponentPresenter.initDataConfigAdminDossieProduto(this.dossieProduto);
  }

  abrirModalAdministracaoDossie(tipoAdministracaoDossie: string) {
    this.abaAdministrarDossieComponentPresenter.openModalAdminDossie(this, tipoAdministracaoDossie);
  }

  adicionarUnidade() {
    this.abaAdministrarDossieComponentPresenter.adicionarUnidade();
  }

  removerUnidade(i: number) {
    this.abaAdministrarDossieComponentPresenter.removerUnidade(i);
  }

}
