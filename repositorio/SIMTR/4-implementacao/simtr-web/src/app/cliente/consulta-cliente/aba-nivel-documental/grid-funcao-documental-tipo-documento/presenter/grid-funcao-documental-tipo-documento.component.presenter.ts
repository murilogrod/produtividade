import { Injectable } from "@angular/core";
import { ArvoreGenericaComponent } from "src/app/documento/arvore-generica/arvore-generica.component";
import { GridFuncaoDocumentalTipoDocumento } from "../model/grid-funcao-documental-tipo-documento.component.model";

@Injectable()
export class GridFuncaoDocumentalTipoDocumentoComponentPresenter{
    conjuntoFuncTipoDoc: GridFuncaoDocumentalTipoDocumento;
    contadorDocumentoTipoDocumento: number = 0 ;
    
    public organizaFuncoesDocumentaisPorTipoPessoa(tipoPessoa: string): void{
        if(!this.conjuntoFuncTipoDoc.funcoesDocumentaisPorTipoPessoa && 
                    ArvoreGenericaComponent.tipologiasDocumentais){    
            this.conjuntoFuncTipoDoc.funcoesDocumentaisPorTipoPessoa = ArvoreGenericaComponent.separaFuncaoDocumentalPorTipoPessoa(tipoPessoa);
        }
    } 
}