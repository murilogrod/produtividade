import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit, AfterViewInit} from '@angular/core';

declare var $: any;
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControlName
} from '@angular/forms';
import { VinculoProduto } from '../../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculo-produto';
import { CampoFormulario } from '../../../../../model';
import { UtilsModal } from '../Utils/utils-modal';
import { TIPO_VINCULO } from '../../../../../constants/constants';
import { ApplicationService, LoaderService } from '../../../../../services';
import { IdentificadorDossieFase } from '../modal-pessoa/model/identificadorDossieEFase';
import { FormularioEnumTipoFormulario } from '../../../../../documento/formulario-generico/model/formulario-enum-tipo-formulario';
import { FormularioGenericoService } from '../../../../../documento/formulario-generico/service/formulario-generico.service';
import { RespostaFormulario } from '../../../model-endPoint-dossie-produto/respostaFormulario';


export interface MessageModel {
  produtosVinculados: any[];
  identificadorDossieFase: IdentificadorDossieFase;
}

export interface MessageModelSaida {
  vinculoProduto: VinculoProduto;
}

@Component({
  selector: 'app-modal-produto',
  templateUrl: './modal-produto.component.html',
  styleUrls: ['./modal-produto.component.css']
})
export class ModalProdutoComponent extends DialogComponent<MessageModel, MessageModelSaida> 
  implements MessageModel, OnInit {

  constructor(dialogService: DialogService,
    private appService: ApplicationService,
    private loadService: LoaderService,
    private formularioGenericoService: FormularioGenericoService) {
    super(dialogService);
    this.appService = appService.getApp();
  }
  habilitarMenssagem: boolean = false;
  messageFormularioVazio: string = "Nâo são esperado dados adicionais para este tipo de produto."
  
  //Atributo que mantem o estado do formulario
  formDinamicoValido: boolean;

  //Atributo que deve ser retornado ao fechar a modal e concluir a acao de salvar
  vinculoProduto: VinculoProduto = new VinculoProduto();
  
  //Lista de produtos disponiveis na tela
  produtosVinculados: any[];

  //Objeto com o ID do dossie e o ID da fase do processo
  identificadorDossieFase: IdentificadorDossieFase;

  //Tipo do formulario utilizado para carregar o formulario generico
  tipoFormulario: FormularioEnumTipoFormulario.PRODUTO;

  //Id do produto selecionado que sera usado para carregar os produtos selecionados
  idProdutoSelecionado: number;

  //Campos genericos a serem preenchidos de acordo com o produto selecionado
  campoFormulario: CampoFormulario[] = [];

  //Metodo inicial da tela que quando o produto vier carregado(editar um produto), ja carrega a lista de atributos a serem preenchidos
  ngOnInit() {
    this.changeProduto();  
  }

  //Metodo que trata a alteração de produtos na tela carregando os campos a serem informados de acordo com o produto selecionado
  changeProduto(){
    this.campoFormulario = [];
    if(this.idProdutoSelecionado){
      this.loadService.show();
      
      //Busca todos os campos a serem informados
      this.campoFormulario = UtilsModal.buscarProcessoLocalStorage(this.appService, this.identificadorDossieFase, TIPO_VINCULO.PRODUTO, this.idProdutoSelecionado);
      
      //Monta os campos caso algum deles seja composto de expressão para formulario dinamico 
      this.formularioGenericoService.montarListaCamposExisteExpressao(FormularioEnumTipoFormulario.PRODUTO ,this.campoFormulario);
      
      //Verifica se há campos obrigatorios para bloquear o botão salvar e somente habilitar apos o preenchimento dos campos
      this.formDinamicoValido = this.campoFormulario.length === 0 || this.campoFormulario.some(campo => campo.obrigatorio);
      
      this.habilitarMenssagem = true;

      this.loadService.hide();
    }
  }

  //Metodo de controle para o botão salvar, carrega as informações do produto e envia para a aba vinculo tratar
  confirm() {
    let produtoVinculado = this.produtosVinculados.find(prod => prod.id == this.idProdutoSelecionado);

    this.vinculoProduto.vinculoNovo = true;
    this.vinculoProduto.id = produtoVinculado.id;
    this.vinculoProduto.nome = produtoVinculado.nome;
    this.vinculoProduto.codigo_modalidade = produtoVinculado.codigo_modalidade;
    this.vinculoProduto.codigo_operacao = produtoVinculado.codigo_operacao;
    this.vinculoProduto.respostas_formulario = [];
    
		this.campoFormulario.forEach(campo => {
			let resposta = new RespostaFormulario();
      if((campo.opcoes_selecionadas && campo.opcoes_selecionadas.length > 0) || (campo.resposta_aberta  && campo.resposta_aberta != "") ) {
          UtilsModal.preencherlistarResposta(campo, resposta);      
          this.vinculoProduto.respostas_formulario.push(resposta);        
      }
		});


    //Carregar os campos no novo vinculo
    this.result = {vinculoProduto : this.vinculoProduto};
    this.close();
  }

  //Metodo de controle para o botão cancelar
  cancel(){
    this.result = null;
    this.close();
  }

  //Valida o form sempre que houver mudança no formulario
  handleChangeCampoFormulario(form){
     this.formDinamicoValido = form.valid; 
  }
  //Captura todas as mudanças do formulario e valida se o formulario permanece valido, seta o valor na variavel a ser utilizada no formulario
   handleChangeFormValidacao(form) { 
     this.formDinamicoValido = form.valid; 
  } 
    

}
