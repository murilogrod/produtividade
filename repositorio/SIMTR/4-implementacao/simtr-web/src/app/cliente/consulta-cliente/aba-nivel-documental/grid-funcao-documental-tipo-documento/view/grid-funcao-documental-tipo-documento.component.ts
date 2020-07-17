import { Component, ViewEncapsulation, Input, AfterViewChecked, ChangeDetectorRef, EventEmitter, Output } from "@angular/core";
import { VinculoCliente } from "src/app/model";
import { GridFuncaoDocumentalTipoDocumentoComponentPresenter } from "../presenter/grid-funcao-documental-tipo-documento.component.presenter";
import { TipoDocumentoArvoreGenerica } from "src/app/model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { GridFuncaoDocumentalTipoDocumento } from "../model/grid-funcao-documental-tipo-documento.component.model";

@Component({
    selector: 'grid-funcao-documental-tipo-documento',
    templateUrl: './grid-funcao-documental-tipo-documento.component.html',
    styleUrls: ['./grid-funcao-documental-tipo-documento.component.css'],
    encapsulation: ViewEncapsulation.None
  })
export class GridFuncaoDocumentalTipoDocumentoComponent implements AfterViewChecked{
  @Input() vinculoCliente: VinculoCliente;
  @Output() changeContadorListaDocumentacaoTipoDocumento: EventEmitter<number> = new EventEmitter<number>();
  @Output() changeCloseLoadingToDocumentacaoTipoDocumento: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() changeQtdComponentToDocumentacaoTipoDocumento: EventEmitter<number> = new EventEmitter<number>();
  gridFuncaoTipoDocPresenter: GridFuncaoDocumentalTipoDocumentoComponentPresenter;

  constructor(
    private changeDetectorRef:ChangeDetectorRef,
    gridFuncaoTipoDocPresenter: GridFuncaoDocumentalTipoDocumentoComponentPresenter){
      this.gridFuncaoTipoDocPresenter = gridFuncaoTipoDocPresenter;
      this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc = new GridFuncaoDocumentalTipoDocumento();
  }

  ngAfterViewChecked(){ 
    if(this.vinculoCliente) {
      this.gridFuncaoTipoDocPresenter.organizaFuncoesDocumentaisPorTipoPessoa(this.vinculoCliente.tipo_pessoa);
    }
    this.changeDetectorRef.detectChanges(); 
  }

  onChangeFuncaoDoc(indice){
    if(!isNaN(indice)){
      let indiceNumber: number = Number(indice).valueOf();
      this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.tiposfuncaoDocEsolhida = this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.funcoesDocumentaisPorTipoPessoa[indiceNumber].tipos_documento;
    }else{
      this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.tiposfuncaoDocEsolhida = new Array<TipoDocumentoArvoreGenerica>();
    }
  }

  countRowToProgressBar(){

    if (this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.tiposfuncaoDocEsolhida &&
            this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.tiposfuncaoDocEsolhida.length >= 
                this.gridFuncaoTipoDocPresenter.contadorDocumentoTipoDocumento) {

        this.gridFuncaoTipoDocPresenter.contadorDocumentoTipoDocumento ++; 
        if (this.gridFuncaoTipoDocPresenter.conjuntoFuncTipoDoc.tiposfuncaoDocEsolhida.length == 
            this.gridFuncaoTipoDocPresenter.contadorDocumentoTipoDocumento) {

          this.changeCloseLoadingToDocumentacaoTipoDocumento.emit(false);
        }
        this.changeContadorListaDocumentacaoTipoDocumento.emit(this.gridFuncaoTipoDocPresenter.contadorDocumentoTipoDocumento);
     } else {
        this.changeCloseLoadingToDocumentacaoTipoDocumento.emit(false);
     }
  }
}