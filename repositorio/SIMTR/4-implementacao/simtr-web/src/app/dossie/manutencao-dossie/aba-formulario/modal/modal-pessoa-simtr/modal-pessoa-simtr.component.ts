import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { VinculoCliente } from '../../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculo-cliente';
import { DialogComponent } from 'angularx-bootstrap-modal/dist/dialog.component';
import { Component, OnInit } from '@angular/core';
import { PorteEmpresa } from 'src/app/cliente/consulta-cliente/enum-porte/porte-empresa.enum';
import { RespostaFormularioVisualizacao } from 'src/app/model/resposta-formulario-visualizacao.';
import * as moment from 'moment';
import { CampoFormulario } from 'src/app/model';
import { TIPO_VINCULO, TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { UtilsModal } from '../Utils/utils-modal';
import { ApplicationService } from 'src/app/services';
import { FormularioGenericoService } from 'src/app/documento/formulario-generico/service/formulario-generico.service';
import { IdentificadorDossieFase } from '../modal-pessoa/model/identificadorDossieEFase';
import { Utils } from 'src/app/utils/Utils';

export interface EntryDataModalPessoaSimtrComponent {
  user: VinculoCliente;
  porte: string;
  identificadorDossieFase: IdentificadorDossieFase;
}

@Component({
  selector: 'modal-pessoa-simtr',
  templateUrl: './modal-pessoa-simtr.component.html',
  styleUrls: ['./modal-pessoa-simtr.component.css']
})
export class ModalPessoaSimtrComponent extends DialogComponent<EntryDataModalPessoaSimtrComponent, string> implements OnInit, EntryDataModalPessoaSimtrComponent {
  user: VinculoCliente;
  isCPF = false;
  porte: string;
  identificadorDossieFase: IdentificadorDossieFase;

  //Lista que será carregada com os campos do vinculo cliente e as respostas selecionadas para o cliente
  listaCampos: RespostaFormularioVisualizacao[];

  //Lista de campos formularios a serem apresentados de acordo com tipo relacionamento
  campoFormulario: CampoFormulario[] = [];
  
  listaCampoForumlario: CampoFormulario[] = [];

  //Tipo de formulario a ser carregado no formulario dinamico
	tipoFormulario: string;

  constructor(dialogService: DialogService, private appService: ApplicationService, private formularioGenericoService: FormularioGenericoService) {
    super(dialogService);
  }

  ngOnInit() {
    this.isCPF = this.user.tipo_pessoa === 'F';
    this.result = this.user.porte;
    this.user.porte = this.user.porte ? this.user.porte : this.user.sigla_porte;
    this.user.porte = PorteEmpresa[this.user.porte ? this.user.porte : this.porte];
    this.carregarCamposFormulario();
    this.carregarCamposFormularioComResposta();
  }

  carregarCamposFormulario(){
    this.listaCampoForumlario = UtilsModal.buscarProcessoLocalStorage(this.appService, this.identificadorDossieFase, TIPO_VINCULO.CLIENTE, this.user.tipo_relacionamento.id);
		this.formularioGenericoService.montarListaCamposExisteExpressao(this.tipoFormulario, this.listaCampoForumlario);
		this.listaCampoForumlario.forEach(formulario => {
			this.listaCampoForumlario = this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, formulario, this.listaCampoForumlario, null, null, null);
		});
		this.campoFormulario = Object.assign([], this.listaCampoForumlario);
  }

  //Associa as respostas ao formulario dinamico ao formulario do produto.
  carregarCamposFormularioComResposta(): any {
    this.listaCampos = [];
    //Produto contem o formulario dinamico a ser preenchido

    if(this.campoFormulario && this.campoFormulario.length > 0){
      this.campoFormulario.forEach(campo => {
        //Respostas selecionadas para o produto do cliente
        let resposta = this.user.respostas_formulario.find(resposta => resposta.id_campo_formulario === campo.id || resposta.campo_formulario === campo.id);  
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
    
  }

  public closeDialog() {
    this.close();
  }

}
