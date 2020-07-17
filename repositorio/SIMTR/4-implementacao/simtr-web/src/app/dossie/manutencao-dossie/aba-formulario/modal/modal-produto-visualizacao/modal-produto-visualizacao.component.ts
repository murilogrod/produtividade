import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit } from '@angular/core';

declare var $: any;
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControlName
} from '@angular/forms';
import { VinculoProduto } from '../../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculo-produto';
import * as moment from 'moment';
import { RespostaFormularioVisualizacao } from '../../../../../model/resposta-formulario-visualizacao.';
import { TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { Utils } from 'src/app/utils/Utils';
export interface MessageModel {
  vinculoProduto: VinculoProduto,
  produto: any;
}

export interface MessageModelSaida {
  vinculoProduto: VinculoProduto;
}

@Component({
  selector: 'app-modal-produto-visualizacao',
  templateUrl: './modal-produto-visualizacao.component.html',
  styleUrls: ['./modal-produto-visualizacao.component.css']
})
export class ModalProdutoVisualizacaoComponent extends DialogComponent<MessageModel, MessageModelSaida> 
  implements MessageModel, OnInit {

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  //Vinculo produto que contem todas as respostas para o vinculo do produto
  vinculoProduto: VinculoProduto;

  //Produto contendo todos os campos que contem no formulario dinamico
  produto: any;

  //Lista que será carregada com os campos do vinculo produto e as respostas selecionadas para o cliente
  listaCampos: RespostaFormularioVisualizacao[];
  
  //Metodo inicial da tela. 
  ngOnInit() {
    this.carregarCamposFormularioComResposta();
  }

  //Associa as respostas ao formulario dinamico ao formulario do produto.
  carregarCamposFormularioComResposta(): any {
    this.listaCampos = [];
    //Produto contem o formulario dinamico a ser preenchido
    this.produto.campos_formulario.forEach(campo => {
      //Respostas selecionadas para o produto do cliente
      let resposta = this.vinculoProduto.respostas_formulario.find(resposta => resposta.id_campo_formulario === campo.id || resposta.campo_formulario === campo.id);  
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
        this.listaCampos.push({label: campo.label, value: valorResposta, mascara: campo.mascara_campo, tipoCampo: campo.tipo_campo});
        //Caso seja um campo com multipla seleção, agrega as seleções realizadas com virgula separando cada seleção.
      }else if(resposta && resposta.opcoes_selecionadas && resposta.opcoes_selecionadas.length > 0){
        resposta.opcoes_selecionadas.forEach(opcao => {
          valorResposta += campo.opcoes_disponiveis.find(disponivel => disponivel.valor_opcao === opcao || disponivel.valor_opcao === opcao.valor_opcao).descricao_opcao +", ";
        });
        valorResposta = valorResposta.substring(0, valorResposta.length - 2);
        this.listaCampos.push({label: campo.label, value: valorResposta, mascara: campo.mascara_campo, tipoCampo: campo.tipo_campo});
        //Por regra ao apresentar qualquer campo que não tenha sido preenchido deve-se adicionar Não Informado ao campo
      }else{
        valorResposta = "Não Informado";
        this.listaCampos.push({label: campo.label, value: valorResposta, mascara: "", tipoCampo: campo.tipo_campo});
      }

    });
  }

  //Metodo de controle para o botão cancelar
  cancel(){
    this.close();
  }

}
