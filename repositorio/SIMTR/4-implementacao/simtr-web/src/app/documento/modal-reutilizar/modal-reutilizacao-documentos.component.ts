import { DocumentoArvore } from "../../model/documento-arvore";
import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { Component, OnInit, ViewEncapsulation } from "@angular/core";

export interface MessageModelEntrada {
    documentos: DocumentoArvore[]; 
}
  
export interface MessageModelSaida {
    docDBSelecionado: DocumentoArvore;
}

@Component({
    selector: 'modal-reutilizacao-documentos',
    templateUrl: './modal-reutilizacao-documentos.component.html',
    styleUrls: ['./modal-reutilizacao-documentos.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ModalReutilizacaoDocumentosComponent extends DialogComponent<MessageModelEntrada, MessageModelSaida> implements MessageModelEntrada, OnInit{
    documentos: DocumentoArvore[];
    tipoDocumentoClassificar: String;
    checkboxControllArray: Boolean[];
    indexUltimaCheckBoxSelecionada: number;
    docDBSelecionado: DocumentoArvore;

    constructor(dialogService: DialogService){
        super(dialogService);
    }

    ngOnInit():void{
        this.tipoDocumentoClassificar = this.documentos[0].tipo_documento.nome;
        this.checkboxControllArray = Array.apply(null, Array(this.documentos.length)).map(function () { return false; })
    }

    separaDocSelecionado(item: DocumentoArvore, indexUltimaCheckBoxSelecionada: number):void{
        if(this.indexUltimaCheckBoxSelecionada != undefined){
            this.checkboxControllArray[this.indexUltimaCheckBoxSelecionada] = false;
        }
        this.indexUltimaCheckBoxSelecionada = indexUltimaCheckBoxSelecionada;
        this.docDBSelecionado = item;
    }

    separaDataDaHora(dataHoraCaptura:string):string{
        let arrayDataHora:string[] = this.separaDataHora(dataHoraCaptura);
        return arrayDataHora[0];
    }

    separaHoraDaData(dataHoraCaptura:string):string{
        let arrayDataHora:string[] = this.separaDataHora(dataHoraCaptura);
        return arrayDataHora[1];
    }

    private separaDataHora(dataHoraCaptura:string):string[]{
        return dataHoraCaptura.split(" ");
    }

    confirm(){
        this.result = {docDBSelecionado: this.docDBSelecionado};
        this.close();
    }

    cancel(){
        this.close();
    }
}