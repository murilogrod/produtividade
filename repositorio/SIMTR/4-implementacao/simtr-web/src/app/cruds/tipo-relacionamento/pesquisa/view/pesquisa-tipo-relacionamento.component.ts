import { Component, OnInit } from '@angular/core';
import { PesquisaTipoRelacionamento } from '../model/pesquisa-tipo-relacionamento.model';
import { AlertMessageService } from 'src/app/services';
import { Router } from '@angular/router';
import { TIPO_RELACIONAMENTO } from '../../constant.tipo-relacionamento';
import { SortEvent } from 'primeng/primeng';
import { PesquisaTipoRelacionamentoPresenter } from '../presenter/pesquisa-tipo-relacionamento.component.presenter';

@Component({
  selector: 'app-pesquisa-tipo-relacionamento',
  templateUrl: './pesquisa-tipo-relacionamento.component.html',
  styleUrls: ['./pesquisa-tipo-relacionamento.component.css']
})
export class PesquisaTipoRelacionamentoComponent extends AlertMessageService implements OnInit {

  pesquisaTipoRelacionamentoPresenter: PesquisaTipoRelacionamentoPresenter;

  constructor(private router: Router,
    pesquisaTipoRelacionamentoPresenter: PesquisaTipoRelacionamentoPresenter) {
    super();
    this.pesquisaTipoRelacionamentoPresenter = pesquisaTipoRelacionamentoPresenter;
    this.pesquisaTipoRelacionamentoPresenter.pesquisaTipoRelacionamento = new PesquisaTipoRelacionamento();
  }

  ngOnInit() {
    this.pesquisaTipoRelacionamentoPresenter.initConfigListaTiposRelacionamentos(this);
  }

  novoTipoRelacionamento() {
    this.router.navigate([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_NOVO]);
  }

  editarTipoRelacionamento(id: string) {
    this.router.navigate([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO, id]);
  }

  confirmarRemocao(id: string) {
    this.pesquisaTipoRelacionamentoPresenter.confirmRemoveTipoRelacionamento(this, id);
  }

  realizarFiltroTiposRelacionamentos(input: any, dataTiposRelacionamentos: any) {
    this.pesquisaTipoRelacionamentoPresenter.filterTiposRelacionamentos(input, dataTiposRelacionamentos);
  }

  realizarOrdenacao(event: SortEvent) {
    this.pesquisaTipoRelacionamentoPresenter.customSort(event);
  }

  onFilter(event: any, globalFilter: any) {
    this.pesquisaTipoRelacionamentoPresenter.setCountFilterGlobal(event, globalFilter);
  }

}
