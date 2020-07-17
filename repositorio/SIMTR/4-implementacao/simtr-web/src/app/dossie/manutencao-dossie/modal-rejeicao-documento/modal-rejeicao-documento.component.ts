import { Component, OnInit, ViewEncapsulation, AfterViewChecked, AfterViewInit } from '@angular/core';
import { DossieService } from '../../dossie-service';
import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { DocumentoNode } from '../../../model/documentoNode';
import { MotivoSituacao } from '../../../model/motivo-situacao-doc';
import { LoaderService } from 'src/app/services';

export interface MessageModel {
  nodeSelected: DocumentoNode
}

export interface MessageModelSaida {
  nu_situacao_motivo: string;
  nu_motivo : string;
}
 
@Component({
  selector: 'app-modal-rejeicao-documento',
  templateUrl: './modal-rejeicao-documento.component.html',
  styleUrls: ['./modal-rejeicao-documento.component.css']
})
export class ModalRejeicaoDocumentoComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit{
  exibeBotaoSalvar:boolean = false;
  listaSituacao: MotivoSituacao[] = [];
  situacaoRejeicao: number;
  motivoRejeicao: number;
  nodeSelected; 
  nu_situacao_motivo: string;
  nu_motivo: string = "";
  exibeComboMotivo: boolean = true;
  listaMotivo: MotivoSituacao[] = [];

  constructor(dialogService: DialogService, private dossieService: DossieService,
		private loadService: LoaderService) { 
    super(dialogService);
  }

  ngOnInit() {
    this.dossieService.getMotivoRejeicao().subscribe(
      response => {
        this.listaSituacao = response;
        for(let lst = 0; lst < this.listaSituacao.length; lst++){
          if(this.listaSituacao[lst].nome === "Criado"){
            this.listaSituacao.splice(lst, 1);

            for(const listS of this.listaSituacao){
              if(listS.nome == "Rejeitado"){
                this.listaMotivo = listS.motivos_situacao;
              }
            }

          }
        }
      },
			() => {
				this.loadService.hide();
			});
  }

  selecionaMotivo(input){
    if(input == "Selecione"){
      this.exibeBotaoSalvar = false;
    }else{
      this.nu_motivo = input;
      this.nu_situacao_motivo = "2";
      this.exibeBotaoSalvar = true;
    }
    
  }

  confirm() {
      this.result = {nu_situacao_motivo : this.nu_situacao_motivo, nu_motivo : this.nu_motivo };
      this.close();
  }

  cancel(){
    this.close();
  }
  
}
