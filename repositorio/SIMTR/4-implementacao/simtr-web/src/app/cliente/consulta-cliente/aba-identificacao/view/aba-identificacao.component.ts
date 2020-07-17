import { Utils } from '../../../../utils/Utils';
import { Component, ViewEncapsulation, OnInit, AfterContentChecked, AfterViewInit } from '@angular/core';
import { AbaIdentificadorComponentPresenter } from '../presenter/aba-identificacao.component.presenter';
import { AbaIdentificacao } from '../model/aba-identificacao.model';
import { InputOutputAbaIdentificacaoService } from '../input-output-service/input-output-aba-identificacao.service';

@Component({
  selector: 'aba-identificacao-cliente',
  templateUrl: './aba-identificacao.component.html',
  styleUrls: ['./aba-identificacao.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AbaIdentificacaoComponent extends InputOutputAbaIdentificacaoService implements OnInit, AfterContentChecked, AfterViewInit {

  abaIdentificadorComponentPresenter: AbaIdentificadorComponentPresenter;

  constructor(
    abaIdentificadorComponentPresenter: AbaIdentificadorComponentPresenter) {
    super();
    this.abaIdentificadorComponentPresenter = abaIdentificadorComponentPresenter;
    this.abaIdentificadorComponentPresenter.abaIdentificacao = new AbaIdentificacao();
  }

  ngAfterContentChecked() {
    this.abaIdentificadorComponentPresenter.setMaxLengthInput();
  }

  ngAfterViewInit(): void {
    this.abaIdentificadorComponentPresenter.applyCSSRadios();
  }

  ngOnInit(): void {
    this.abaIdentificadorComponentPresenter.inicializaDadosAbaIdentificacao(this);
  }

  isCPF() {
    return this.abaIdentificadorComponentPresenter.isCPF(this.cpfCnpj)
  }


  changeAcao() {
    this.abaIdentificadorComponentPresenter.setMudancaSalva();
  }

  /**
   * Método responsável por mostrar os dados do cliente basedo no SICLI
   */
  showModalSICLI() {
    this.abaIdentificadorComponentPresenter.showModalSICLI(this);
  }

  /**
   * Método responsável por inserir um novo usuário no sistema
   */
  onInsertUser() {
    this.abaIdentificadorComponentPresenter.onInsertUser(this);
  }

  cancel() {
    this.onCancel.emit(true);
  }

  maskCalendar(event): void {
    event.target.value = Utils.mascaraData(event);
  }

  abrirModalUserSSO() {
    this.abaIdentificadorComponentPresenter.abrirModalUserSSO(this);
  }
}
