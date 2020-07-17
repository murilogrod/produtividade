import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit } from '@angular/core';

declare var $: any;
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControlName
} from '@angular/forms';

import * as moment from 'moment';
import { RespostaFormularioVisualizacao } from '../../../../../model/resposta-formulario-visualizacao.';
import { VinculoGarantia } from '../../../../../model';
import { TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { Utils } from 'src/app/utils/Utils';
export interface MessageModel {
  vinculoGarantia: VinculoGarantia,
  garantia: any;
}

export interface MessageModelSaida {
  vinculoGarantia: any;
}

@Component({
  selector: 'app-modal-garantia-visualizacao',
  templateUrl: './modal-garantia-visualizacao.component.html',
  styleUrls: ['./modal-garantia-visualizacao.component.css']
})
export class ModalGarantiaVisualizacaoComponent extends DialogComponent<MessageModel, MessageModelSaida> 
  implements MessageModel, OnInit {

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  //Vinculo garantia que contem todas as respostas para o vinculo da garantia
  vinculoGarantia: any;

  //Garantia contendo todos os campos que contem no formulario dinamico
  garantia: any;

  //Lista que será carregada com os campos do vinculo garantia e as respostas selecionadas para o cliente
  listaCampos: RespostaFormularioVisualizacao[];
  
  //Metodo inicial da tela. Associa as respostas ao formulario dinamico do vinculo de garantia
  ngOnInit() {
    this.carregarCamposFormularioResposta(); 
  }

  //Associa as respostas ao formulario dinamico ao formulario da garantia.
  carregarCamposFormularioResposta(){
    this.listaCampos = [];
    //Garantia contem o formulario dinamico a ser preenchido
    this.garantia.campos_formulario.forEach(campo => {
      let resposta = this.vinculoGarantia.respostas_formulario.find(resposta => resposta.id_campo_formulario === campo.id || resposta.campo_formulario === campo.id);  
      let valorResposta = "";
      //Caso não seja um campo de multipla seleção, preenche o valor a ser apresentado
      if(resposta && (resposta.resposta || resposta.resposta_aberta)){
        if(campo.tipo_campo === 'DATE'){
          valorResposta = moment(resposta.resposta ? resposta.resposta : resposta.resposta_aberta).format('DD/MM/YYYY');
        }else if(campo.tipo_campo === TIPO_DOCUMENTO.CONTA_CAIXA){
          valorResposta = Utils.validarKeyupTipoCampoContaCaixa(resposta.resposta ? resposta.resposta : resposta.resposta_aberta);
        } else{
          valorResposta = resposta.resposta ? resposta.resposta : resposta.resposta_aberta;
        }
        //Caso seja um campo com multipla seleção, agrega as seleções realizadas com virgula separando cada seleção.
      }else if(resposta && resposta.opcoes_selecionadas && resposta.opcoes_selecionadas.length > 0){
        resposta.opcoes_selecionadas.forEach(opcao => {
          valorResposta += campo.opcoes_disponiveis.find(disponivel => opcao && 
            (disponivel.valor_opcao === opcao || disponivel.valor_opcao === opcao.valor_opcao)).descricao_opcao +", ";
        });
        valorResposta = valorResposta.substring(0, valorResposta.length - 2);
        //Por regra ao apresentar qualquer campo que não tenha sido preenchido deve-se adicionar Não Informado ao campo
      }else{
        valorResposta = "Não Informado";
      }

      this.listaCampos.push({label: campo.label, value: valorResposta, mascara: campo.mascara_campo, tipoCampo: campo.tipo_campo});

    });

  }

  //Metodo de controle para o botão cancelar
  cancel(){
    this.close();
  }

}
