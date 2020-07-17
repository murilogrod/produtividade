import { Component, ViewEncapsulation, Input } from "@angular/core";
import { VinculoCliente } from "src/app/model";
import { LoadingModelNivelDocumental } from "../model/loading-model-nivel-documental";

@Component({
    selector: 'aba-nivel-documental',
    templateUrl: './aba-nivel-documental.component.html',
    styleUrls: ['./aba-nivel-documental.component.css'],
    encapsulation: ViewEncapsulation.None
  })
export class AbaNivelDocumentalComponent{
  @Input() vinculoCliente: VinculoCliente;
  loadingModel: LoadingModelNivelDocumental;

  constructor() {
		this.loadingModel = new LoadingModelNivelDocumental();
  }

  // Documento Valido
  handleChangeContadorListaDocumentacaoValida(input) {
    this.loadingModel.countDocumentacaoValida = input;
  }

  handleChangeCloseLoadingToDocumentacaoValida(input) {
    this.loadingModel.documentacaoValida = input;
  }

  handleChangeQtdComponentToDocumentacaoValida(input) {
    this.loadingModel.countTotalDocumentoValidacao = input;
  } 
  
  // Documento Pendente
  handleChangeContadorListaDocumentacaoPendente(input) {
    this.loadingModel.countDocumentacaoPendente = input;
  }

  handleChangeCloseLoadingToDocumentoPendente(input) {
    this.loadingModel.documentacaoPendente = input;
  }
  
  handleChangeQtdComponentToDocumentoPendente(input) {
    this.loadingModel.countTotalDocumentoPendente = input;
  }
  
  // Tipo Documento
  handleChangeContadorListaDocumentacaoTipoDocumento(input) {
    this.loadingModel.countDocumentacaoTipoDocumento = input;
  }

  handleChangeCloseLoadingToDocumentoTipoDocumento(input) {
    this.loadingModel.documentacaoTipoDocumento = input;
  }
  
  handleChangeQtdComponentToDocumentoTipoDocumento(input) {
    this.loadingModel.countTotalDocumentoTipoDocumento = input;
  }
} 