import { Component, ViewEncapsulation, OnInit, EventEmitter, Input, ViewChild } from "@angular/core";
import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { DocumentoPost } from "src/app/compartilhados/componentes/formulario-extracao/model/documento-post";
import { FormExtracaoManualService } from "src/app/compartilhados/componentes/formulario-extracao/service/form-extracao-manual.service";
import { LoaderService } from "src/app/services";
import { MESSAGEM_MODAL_EXTRACAO_DE_DADOS } from "src/app/constants/constants";

export interface MessageModel {
    idDocumento: string;
    binario: string;
    mimetype: string;
    idTipoDocumento: number;
    atributos: any[]
}
  
export interface MessageModelSaida {
    tipoRetorno: string;
    sucesso: boolean;
    mensagem: string;
    atributos: any[]
}

@Component({
    selector: 'modal-extracao-dados-documento.component',
    templateUrl: './modal-extracao-dados-documento.component.html',
    styleUrls: ['./modal-extracao-dados-documento.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ModalExtracaoDadosDocumento extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit{
    exibeTodos = false;
    resultadoExtracao: any
    statusForm = false;
    binario: string;
    mimetype: string;
    alturaVisualizador = '100%';
    comprimentoVisualizador = '100%';
    atributos: any[];
    
    idDocumento: string;
    idTipoDocumento: number;
    documentoPost: DocumentoPost;
    messageDeErro: any [] = [];
    documentoNaoPersistido: any;
    
    salvarForm: EventEmitter<any> = new EventEmitter<any>();
    cancelarForm: EventEmitter<any> = new EventEmitter<any>();

    constructor(private serviceExtracaoManual: FormExtracaoManualService, dialogService: DialogService, private loadService: LoaderService){
        super(dialogService);
    }

    exibeErro(error) {
    }

    ngOnInit(){
        if(this.idDocumento){
            this.serviceExtracaoManual.retornaDocumentoPost(this.idDocumento).subscribe(retorno => {
                this.documentoPost = retorno;
                this.mimetype = this.documentoPost.mimetype
                this.binario = this.documentoPost.binario
                this.loadService.hide();
            }, error => {
                this.messageDeErro.push(error);
                this.loadService.hide();
                throw error;
            });

        }else{// condição adicionada pra atender extração manual quando o documento não foi persistindo
            this.documentoNaoPersistido = {};
            this.documentoNaoPersistido.binario = this.binario;
            this.documentoNaoPersistido.mimetype = this.mimetype;
            this.documentoNaoPersistido.atributos = this.atributos;
            this.documentoNaoPersistido.tipo_documento = {id: this.idTipoDocumento, atributos_documento: []};
            this.documentoPost = this.documentoNaoPersistido;
        }
    }

    alterouStatusForm(event) {
        this.statusForm = event.status_extracao
        
        if (this.statusForm) {
            this.resultadoExtracao = event.resultado_extracao
        }
    }

    ocorreuErroForm(event){
        this.messageDeErro.push(event);
    }

    salvar(){
        this.loadService.show();
        this.serviceExtracaoManual.salvoComSucesso.subscribe(retorno => {

            if(retorno.salvo){
                this.result = {tipoRetorno: 'salvar', sucesso: true, mensagem: MESSAGEM_MODAL_EXTRACAO_DE_DADOS.SUCESSO, atributos: []}
            }else{
                this.result = {tipoRetorno: 'salvar', sucesso: false, mensagem: retorno.mensagem, atributos: retorno.atributos}
            }
            this.close();
        });
        this.salvarForm.emit();
    }
    
    cancelar(){
        this.loadService.show();
        if(this.documentoPost){
            
            this.serviceExtracaoManual.cancelamentoCompleto.subscribe(retorno => {
                
                if(retorno.salvo){
                    this.result = {tipoRetorno: 'cancelado', sucesso: true, mensagem: MESSAGEM_MODAL_EXTRACAO_DE_DADOS.CANCELADO, atributos: []}
                }else{
                    this.result = {tipoRetorno: 'cancelado', sucesso: false, mensagem: retorno.mensagem, atributos: []}
                }
                this.close();
            });
            this.cancelarForm.emit();

        }else{
            this.result = {tipoRetorno: 'cancelado', sucesso: true, mensagem: null, atributos: []}
            this.close();
        }
        
    }
}