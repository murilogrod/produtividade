import { Component, ViewEncapsulation, OnInit, Input, EventEmitter, Output} from "@angular/core";
import { GridProdutosDocumentacaoPendenteComponentPresenter } from "../presenter/grid-produtos-documentacao-pendente.component.presenter";
import { VinculoCliente } from "src/app/model";
import { RetornoVerificacaoAutorizacao } from "../model/retorno-verificacao-autorizacao.model";

@Component({
    selector: 'grid-produtos-documentacao-pendente',
    templateUrl: './grid-produtos-documentacao-pendente.component.html',
    styleUrls: ['./grid-produtos-documentacao-pendente.component.css'],
    encapsulation: ViewEncapsulation.None
  })
export class GridProdutosDocumentacaoPendenteComponent implements OnInit{
  @Input() vinculoCliente: VinculoCliente;
  @Output() changeContadorListaDocumentoPendente: EventEmitter<number> = new EventEmitter<number>();
  @Output() changeCloseLoadingToDocumentoPendente: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() changeQtdComponentToDocumentoPendente: EventEmitter<number> = new EventEmitter<number>();
  produtoDocPendentePresenter: GridProdutosDocumentacaoPendenteComponentPresenter;

  constructor(produtoDocPendentePresenter: GridProdutosDocumentacaoPendenteComponentPresenter){
    this.produtoDocPendentePresenter = produtoDocPendentePresenter;
  }

  ngOnInit(){
    if(this.vinculoCliente) {
      this.produtoDocPendentePresenter.listRetornoVerificacaoAutorizacao = new Array<RetornoVerificacaoAutorizacao>();
      this.produtoDocPendentePresenter.produtoDossieDigitalSelecionado = null;
    }
  }
  
  onChangeProduto(indice){
    if(!isNaN(indice)){
      this.produtoDocPendentePresenter.produtoDossieDigitalSelecionado =  this.produtoDocPendentePresenter.listProdutoDossieDigital[indice];
      this.produtoDocPendentePresenter.populaObjetoSelecionadoPeloUsuario(this.vinculoCliente);
    }else{
      this.produtoDocPendentePresenter.produtoDossieDigitalSelecionado = null;
    } 
  }

  verificarDocPendentes(){
    if(this.produtoDocPendentePresenter.produtoDossieDigitalSelecionado){
      this.produtoDocPendentePresenter.verificarDocPendentesProduto();
    }else{
      this.produtoDocPendentePresenter.listRetornoVerificacaoAutorizacao = new Array<RetornoVerificacaoAutorizacao>();
    }
  }

  countRowToProgressBar(){

    if (this.produtoDocPendentePresenter.listRetornoVerificacaoAutorizacao &&
            this.produtoDocPendentePresenter.listRetornoVerificacaoAutorizacao.length >= 
                this.produtoDocPendentePresenter.contadorDocumentacaoPendente) {

        this.produtoDocPendentePresenter.contadorDocumentacaoPendente ++; 
        if (this.produtoDocPendentePresenter.listRetornoVerificacaoAutorizacao.length == 
            this.produtoDocPendentePresenter.contadorDocumentacaoPendente) {

          this.changeCloseLoadingToDocumentoPendente.emit(false);
        }
        this.changeContadorListaDocumentoPendente.emit(this.produtoDocPendentePresenter.contadorDocumentacaoPendente);
     } else {
        this.changeCloseLoadingToDocumentoPendente.emit(false);
     }
  }
}