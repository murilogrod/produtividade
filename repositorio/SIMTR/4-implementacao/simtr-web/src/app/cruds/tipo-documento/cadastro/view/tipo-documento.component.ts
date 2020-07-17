import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AlertMessageService } from 'src/app/services';
import { TipoDocumentoModel } from '../model/tipo-documento.model';
import { TipoDocumentoPresenter } from '../presenter/tipo-documento.component.presenter';
import { ModalAtributoModel } from 'src/app/cruds/atributo/modal-atributo/model/modal-atributo-model';
import { TipoDocumento } from '../model/tipo-documento';
import { UtilsGrids } from '../../../util/utils-grids';
import { SortEvent } from 'primeng/primeng';
import { GridFuncaoDocumental } from '../model/grid-funcao-documental';

@Component({
  selector: 'app-tipo-documento',
  templateUrl: './tipo-documento.component.html',
  styleUrls: ['./tipo-documento.component.css']
})
export class TipoDocumentoComponent extends AlertMessageService implements OnInit {

  tipoDocumentoPresenter: TipoDocumentoPresenter

  constructor(tipoDocumentoPresenter: TipoDocumentoPresenter,
    private activatedRoute: ActivatedRoute) {
    super();
    this.tipoDocumentoPresenter = tipoDocumentoPresenter;
    this.tipoDocumentoPresenter.tipoDocumentoModel = new TipoDocumentoModel();
    this.tipoDocumentoPresenter.tipoDocumento = new TipoDocumento();
  }

  ngOnInit() {
    this.tipoDocumentoPresenter.initConfigTipoDocumento(this.activatedRoute.snapshot.params, this);
  }

  adaptarNomeTipoDocumento($event: any) {
    this.tipoDocumentoPresenter.tipoDocumento.nome = $event.value.toUpperCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }

  pesquisaTipoDocumento() {
    this.tipoDocumentoPresenter.anularParametrosMemoriaTipoDocumentoVoltaPesquisa();
  }

  salvarTipoDocumento(finalizar: boolean) {
    this.tipoDocumentoPresenter.saveUpdateTipoDocumeto(this, finalizar);
  }

  handleChangeAtributos(atributos: Array<ModalAtributoModel>) {
    this.tipoDocumentoPresenter.inicializarAtributos(atributos);
  }

  handleChangeAtributosRemovidos(atributoRemovido: ModalAtributoModel) {
    this.tipoDocumentoPresenter.inicializarAtributosParaRemocao(atributoRemovido);
  }

  pegaCor() {
    return this.tipoDocumentoPresenter.tipoDocumento.cor_box;
  }

  removerFuncaoDocumental(gridFuncaoDocumental: GridFuncaoDocumental, index: number) {
    this.tipoDocumentoPresenter.confirmRemoveFuncaoDocumental(gridFuncaoDocumental, index);
  }

  adicionarFuncaoDocumental() {
    this.tipoDocumentoPresenter.inicializarFuncaoDocumentalGrid();
  }

  realizarLoad() {
    this.tipoDocumentoPresenter.tipoDocumentoModel.countFuncaoDocumental++;
  }

  realizarOrdenacao(event: SortEvent) {
    UtilsGrids.customSort(event);
  }

  mudouValidadeAutoContida() {
    this.tipoDocumentoPresenter.anularPrazoValidade();
  }

  incluirTag() {
    this.tipoDocumentoPresenter.adicionarTag();
  }

  validarTipologia() {
    this.tipoDocumentoPresenter.verificarOcorrenciaNumeroTipologia();
  }

  verificarOcorrenciaNomeTipoDocumentoIndicadores() {
    this.tipoDocumentoPresenter.verificarNomeTipoDocumentoEmConjuntoComIndicadoresDeUnicidade(this);
  }

  verificarOcorrenciaNomeApoioNegocio() {
    this.tipoDocumentoPresenter.verificarOcorrenciaNomeTipoDocumentoIndicadorApoioNegocio(this, false);
  }

  verificarOcorrenciaNomeDossieDigital() {
    this.tipoDocumentoPresenter.verificarOcorrenciaNomeTipoDocumentoIndicadorDossieDigital(this, false);
  }

  verificarOcorrenciaNomeProcessoAdministrativo() {
    this.tipoDocumentoPresenter.verificarOcorrenciaNomeTipoDocumentoIndicadorProcessoAdministrativo(this, false);
  }
}
