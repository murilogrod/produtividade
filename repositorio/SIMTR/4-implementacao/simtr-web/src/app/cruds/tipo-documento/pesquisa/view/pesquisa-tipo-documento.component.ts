import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SortEvent, SelectItem } from 'primeng/primeng';
import { AlertMessageService } from 'src/app/services';
import { PesquisaTipoDocumento } from "../model/pesquisa-tipo-documento.model";
import { PesquisaTipoDocumentoPresenter } from '../presenter/pesquisa-tipo-documento.component.presenter';
import { TIPO_DOCUMENTO } from './../../constant-tipo-documento';

@Component({
  selector: 'app-tipo-documento',
  templateUrl: './pesquisa-tipo-documento.component.html',
  styleUrls: ['./pesquisa-tipo-documento.component.css']
})
export class PesquisaTipoDocumentoComponent extends AlertMessageService implements OnInit {

  pesquisaTipoDocumentoPresenter: PesquisaTipoDocumentoPresenter;

  constructor(private router: Router,
    pesquisaTipoDocumentoPresenter: PesquisaTipoDocumentoPresenter) {
    super();
    this.pesquisaTipoDocumentoPresenter = pesquisaTipoDocumentoPresenter;
    this.pesquisaTipoDocumentoPresenter.pesquisaTipoDocumento = new PesquisaTipoDocumento();
  }

  ngOnInit() {
    this.pesquisaTipoDocumentoPresenter.initConfigTipoDocumento(this);
  }

  atualizarTiposDocumentosConformeSituacao(selectedValue: any) {
    this.pesquisaTipoDocumentoPresenter.filtrarDocumentosConformeSituacao(selectedValue.option.value);
  }

  novoTipoDocumento() {
    this.router.navigate([TIPO_DOCUMENTO.TIPO_DOCUMENTO_NOVO]);
  }

  editarTipoDocumento(identificador_tipo_documento: string) {
    this.router.navigate([TIPO_DOCUMENTO.TIPO_DOCUMENTO, identificador_tipo_documento]);
  }

  confirmarRemocao(identificador_tipo_documento: string) {
    this.pesquisaTipoDocumentoPresenter.confirmRemoveTipoDocumento(this, identificador_tipo_documento);
  }

  realizarFiltroTipoDocumento(input: any, dataTipoDocumentos: any) {
    this.pesquisaTipoDocumentoPresenter.filterTipoDocumentos(input, dataTipoDocumentos);
  }

  realizarOrdenacao(event: SortEvent) {
    this.pesquisaTipoDocumentoPresenter.customSort(event);
  }

  onFilter(event: any, globalFilter: any) {
    this.pesquisaTipoDocumentoPresenter.setCountFilterGlobal(event, globalFilter);
  }

}
