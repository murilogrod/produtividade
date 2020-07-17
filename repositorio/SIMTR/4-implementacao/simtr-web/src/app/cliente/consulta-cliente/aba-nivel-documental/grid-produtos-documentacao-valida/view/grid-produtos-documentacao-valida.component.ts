import { Component, ViewEncapsulation, Input, OnInit, Output, EventEmitter} from "@angular/core";
import { VinculoCliente } from "src/app/model";
import { GridProdutosDocumentacaoValidaComponentPresenter } from "../presenter/grid-produtos-documentacao-valida.component.presenter";
import { NivelDocumental } from "../model/nivel-documental.model";
import { LoadingModelNivelDocumental } from "../../model/loading-model-nivel-documental";

@Component({
    selector: 'grid-produtos-documentacao-valida',
    templateUrl: './grid-produtos-documentacao-valida.component.html',
    styleUrls: ['./grid-produtos-documentacao-valida.component.css'],
    encapsulation: ViewEncapsulation.None
  })
export class GridProdutosDocumentacaoValidaComponent implements OnInit {
  @Input() vinculoCliente: VinculoCliente;
  @Output() changeContadorListaDocumentacaoValida: EventEmitter<number> = new EventEmitter<number>();
  @Output() changeCloseLoadingToDocumentacaoValida: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() changeQtdComponentToDocumentacaoValida: EventEmitter<number> = new EventEmitter<number>();
  gridProdutosDocsValidosPresenter: GridProdutosDocumentacaoValidaComponentPresenter;

  constructor(gridProdutosDocsValidosPresenter: GridProdutosDocumentacaoValidaComponentPresenter){
    this.gridProdutosDocsValidosPresenter = gridProdutosDocsValidosPresenter;
    gridProdutosDocsValidosPresenter.nivelDocumental = new NivelDocumental();
  }

  ngOnInit(){
    if(this.vinculoCliente) {
      this.gridProdutosDocsValidosPresenter.
          converteParaNivelDocumentalModel(this.vinculoCliente);
      this.gridProdutosDocsValidosPresenter.
          verificaDataApuracaoExpirada(this.vinculoCliente.id);
      this.changeQtdComponentToDocumentacaoValida.emit(this.gridProdutosDocsValidosPresenter.nivelDocumental.produtosHabilitados.length);
    }
  }

  atualizaNivelDocumental(){
     this.gridProdutosDocsValidosPresenter.atualizaNivelDocumental(this.vinculoCliente.id);
  }

  countRowToProgressBar(){

    if (this.gridProdutosDocsValidosPresenter.nivelDocumental.produtosHabilitados &&
          this.gridProdutosDocsValidosPresenter.nivelDocumental.produtosHabilitados.length >= 
             this.gridProdutosDocsValidosPresenter.nivelDocumental.contadorDocumentacaoValida) {

        this.gridProdutosDocsValidosPresenter.nivelDocumental.contadorDocumentacaoValida ++; 
        if (this.gridProdutosDocsValidosPresenter.nivelDocumental.produtosHabilitados.length == 
            this.gridProdutosDocsValidosPresenter.nivelDocumental.contadorDocumentacaoValida) {

          this.changeCloseLoadingToDocumentacaoValida.emit(false);
        }
        this.changeContadorListaDocumentacaoValida.emit(this.gridProdutosDocsValidosPresenter.nivelDocumental.contadorDocumentacaoValida);
     } else {
      this.changeCloseLoadingToDocumentacaoValida.emit(false);
   }
  }
 
}