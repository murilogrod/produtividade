import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit} from '@angular/core';

declare var $: any;
import { VinculoGarantia } from '../../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculo-garantia';
import { VinculoProduto } from '../../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculo-produto';
import { VinculoCliente, CampoFormulario } from '../../../../../model';
import { Utils } from '../../../../../utils/Utils';
import { IdentificadorDossieFase } from '../modal-pessoa/model/identificadorDossieEFase';
import { NgForm } from '@angular/forms';
import { FormularioGenericoService } from 'src/app/documento/formulario-generico/service/formulario-generico.service';
import { FormularioEnumTipoFormulario } from 'src/app/documento/formulario-generico/model/formulario-enum-tipo-formulario';
import { MudancaSalvaService } from 'src/app/services/mudanca-salva.service';
import { ApplicationService, LoaderService } from 'src/app/services';
import { UtilsModal } from '../Utils/utils-modal';
import { TIPO_VINCULO, LOCAL_STORAGE_CONSTANTS, UNDESCOR } from 'src/app/constants/constants';
import { SelectItem } from 'primeng/primeng';
import { RespostaFormulario } from '../../../model-endPoint-dossie-produto/respostaFormulario';

export interface MessageModel {
	produtos: VinculoProduto[];
	produtosVinculados: any[];
	processoEscolhido: any;
	processo: any;
	clientes: VinculoCliente[];
	identificadorDossieFase: IdentificadorDossieFase;
}

export interface MessageModelSaida {
	garantia: VinculoGarantia;
}

@Component({
	selector: 'app-modal-garantia',
	templateUrl: './modal-garantia.component.html',
	styleUrls: ['./modal-garantia.component.css']
})
export class ModalGarantiaComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit {
	constructor(dialogService: DialogService,
		private formularioGenericoService: FormularioGenericoService,
		private mudancaSalvaService: MudancaSalvaService,
		private appService: ApplicationService,
		private loadService: LoaderService) {
		super(dialogService);
	}

	habilitaAlteracao: boolean = true;
	hasClienteValido: boolean = true;
	campoFormulario: CampoFormulario[] = [];
	tipoFormulario: string;
	formValidacaoStatico: NgForm;

	produtos;
	listaProdutos;
	clientes;
	garantiaTela: VinculoGarantia = new VinculoGarantia();
	relacionadoPessoaLista: any;
	garantias: VinculoGarantia[] = [];
	garantiasVinculadoProdutosProcessos: VinculoGarantia[] = [];
	garantiasVinculadoProcessos: VinculoGarantia[] = [];

	clienteSelecionado: VinculoCliente = new VinculoCliente();

	isPercentualGarantia = false;
	isValorGarantia = false;

	messagesSuccess: string[];
	messagesInfo: string[];
	messagesError: string[];
	cpfCnpj: string;
	produtosVinculados: any[];
	produtosClone: VinculoProduto[];
	listaGarantiasProcesso: any[];
	processoEscolhido: any;
	processo: any;
	habilitarProduto: boolean = false;
	habilitarRelacionado: boolean = true;
	habilitar: boolean = true;
	formularioDinamico: boolean = false;

	habilitarDesabilitaComboMultiple: boolean = true;
	identificadorDossieFase: IdentificadorDossieFase;
	habilitarMenssagem = false;
	messageFormularioVazio: string = "Nâo são esperado dados adicionais para este tipo de garantia."
	produtosSelectItem: SelectItem[] = [];
	garantiaSelectItem: SelectItem[] = [];
	clientesSelectItem: SelectItem[] = [];

	ngOnInit() {
		this.habilitar = true;
		this.messagesSuccess = [];
		this.messagesInfo = [];
		this.messagesError = [];
		
		this.produtosClone = this.produtos;
		this.listaProdutos = this.produtos;

		this.carregarListaGarantias();

		this.tipoFormulario = FormularioEnumTipoFormulario.GARANTIA;

		// Preencher o dropdown do produto
		if (this.listaProdutos && this.listaProdutos.length > 0) {

			this.produtosSelectItem.push({ label: 'Selecione', value: null })

			this.listaProdutos.forEach(elemento => {
				let labelTemp = elemento.codigo_operacao + '.' + elemento.codigo_modalidade + '-' + elemento.nome;
				this.produtosSelectItem.push({ label: labelTemp, value: elemento });
			});

		}

		this.preencherMultiSelectCliente();
		this.preencherGarantiaSelect();
	}

	// Preencher o multiselect do cliente
	private preencherMultiSelectCliente(){
		if (this.clientes && this.clientes.length > 0) {
			this.clientes.forEach(element => {
				let obj: any = {};
					obj.label = Utils.masKcpfCnpj(element.cnpj ? element.cnpj : element.cpf);
					obj.value = element.id;
				this.clientesSelectItem.push(obj);
			});
		}
	}

	private carregarListaGarantias() {
		this.garantiasVinculadoProdutosProcessos = [];
		let buscarGarantiasProcesso = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.processoEscolhido.id));
		if (buscarGarantiasProcesso) {
			buscarGarantiasProcesso.garantias_vinculadas.forEach(garantiasVinculadas => {
				this.garantiasVinculadoProdutosProcessos.push(garantiasVinculadas);
			});
			Utils.ordenarPorOrdenaCodigoBacen(this.garantiasVinculadoProdutosProcessos);
			this.garantiasVinculadoProcessos = this.garantiasVinculadoProdutosProcessos;
			this.garantias = Object.assign([], this.garantiasVinculadoProcessos);
		}
	}

	selecionarRelacionado(event) {
		
		if (event && event.value.length > 0) {
			this.garantiaTela.clientes_avalistas = [];
			event.value.forEach(element => {
				this.garantiaTela.clientes_avalistas.push(element);
			});

			this.habilitarFormulario();
		}
	}

	confirm() {
		this.garantiaTela.respostas_formulario = [];

		this.garantias.forEach(elmentoGarantia => {
			if (elmentoGarantia.id == +this.garantiaTela.garantiaVinculada.id) {
				this.garantiaTela.nome = elmentoGarantia.nome_garantia;
				this.garantiaTela.indicador_fidejussoria = elmentoGarantia.indicador_fidejussoria;
				this.garantiaTela.codigo_bacen = elmentoGarantia.codigo_bacen;
				this.garantiaTela.id = this.garantiaTela.garantiaVinculada.id;

			}
		});
		
		this.campoFormulario.forEach(campo => {
			let resposta = new RespostaFormulario();
			if((campo.opcoes_selecionadas && campo.opcoes_selecionadas.length > 0) || (campo.resposta_aberta  && campo.resposta_aberta != "") || (campo.string_selecionadas && campo.string_selecionadas.length > 0) ) {
				UtilsModal.preencherlistarResposta(campo, resposta);      
				this.garantiaTela.respostas_formulario.push(resposta);        
			}
		});

		this.carregarListaGarantias();
		this.preencherGarantiaSelect();

		if (!this.garantiaTela.indicador_fidejussoria || (this.garantiaTela.indicador_fidejussoria && undefined != this.garantiaTela.clientes_avalistas && this.garantiaTela.clientes_avalistas.length > 0)) {

			this.garantiaTela.vinculoNovo = true;
			this.result = { garantia: this.garantiaTela };
			this.close();
		} else {
			Utils.showMessageDialog(this.dialogService, 'Para o tipo de garantia selecionado é obrigatório realizar pelo menos um vínculo de pessoa.');
		}
	}

	habilitarFormulario() {
		this.habilitar = true;

		if (this.garantiaTela.garantiaVinculada) {
			if (this.garantiaTela.garantiaVinculada.indicador_fidejussoria == true) {
				if (this.garantiaTela.valor_garantia && this.garantiaTela.clientes_avalistas && this.garantiaTela.clientes_avalistas.length > 0) {
					this.habilitar = false;
				}
			} else if (this.garantiaTela.valor_garantia) {
				this.habilitar = false;
			}
		}
	}

	cancel() {
		// we set dialog result as true on click on confirm button,
		// then we can get dialog result from caller code
		this.result = null;
		this.close();
	}

	clearAllMessages() {
		this.messagesError = [];
		this.messagesInfo = [];
		this.messagesSuccess = [];
	}

	onChangeProdutos() {
		this.clearAllMessages();
		this.garantias = [];
		this.garantiaSelectItem = Object.assign([], []);

		if (this.garantiaTela.produtoVinculado && this.garantiaTela.produtoVinculado.id) {

			let produto = this.produtosVinculados.find(produto => 
				produto.codigo_operacao == this.garantiaTela.produtoVinculado.codigo_operacao &&
				produto.codigo_modalidade == this.garantiaTela.produtoVinculado.codigo_modalidade);

			if (produto) {
				this.garantias = [];
				this.garantias = produto.garantias_vinculadas;
				Utils.ordenarPorOrdenaCodigoBacen(this.garantias);
				this.preencherGarantiaSelect();

			} else {
				this.garantiaTela.garantiaVinculada = null;
				this.tipoGarantiaCarregarFormularioDinamico();
				this.messagesInfo.push('Este produto não possui garantia vinculada');
			}
		} else {
			this.garantiaTela.produtoVinculado = null;
			this.garantiaTela.garantiaVinculada = null;
			this.carregarListaGarantias();
			this.preencherGarantiaSelect();
		}
	}

	// preencher dropdown garantia
	preencherGarantiaSelect() {

		if (this.garantias && this.garantias.length > 0) {

			this.garantiaSelectItem = [];

			this.garantiaSelectItem.push({ label: 'Selecione', value: null })

			this.garantias.forEach(elemento => {

				let labelTemp = elemento.codigo_bacen + '.' + elemento.nome_garantia;
				this.garantiaSelectItem.push({ label: labelTemp, value: elemento });

			});
			this.garantiaSelectItem = Object.assign([], this.garantiaSelectItem);

		} else {
			this.garantiaSelectItem = Object.assign([], []);
			this.garantiaTela.garantiaVinculada = null;
		}

	}

	onChangeGarantia() {
		if (this.garantiaTela.garantiaVinculada && this.garantiaTela.garantiaVinculada.id) {
			this.produtos;

			if (null != this.produtosClone) {
				this.produtosClone.forEach(produto => {
					if (null != produto.listaGarantia) {
						this.produtos = [];
						produto.listaGarantia.forEach(garantia => {
							garantia.forEach(garant => {
								if (garant.id == this.garantiaTela.garantiaVinculada.id) {
									this.produtos.push(produto);
								}
							});
						});
					}
				});

				this.produtos = Object.assign([], this.produtos);
			}

			if (this.garantiaTela.garantiaVinculada.indicador_fidejussoria == true) {
				this.habilitarDesabilitaComboMultiple = false;
			} else {
				this.garantiaTela.clientes_avalistas = [];
				this.habilitarDesabilitaComboMultiple = true;
			}

		} else {
			this.produtos = this.produtosClone;
			this.garantiaTela.garantiaVinculada = null;
			this.habilitarDesabilitaComboMultiple = true;
		}
		this.tipoGarantiaCarregarFormularioDinamico();
		this.habilitarFormulario();
		this.habilitarMenssagem = true;
	}

	tipoGarantiaCarregarFormularioDinamico() {
		this.loadService.show();
		if (this.garantiaTela.garantiaVinculada && this.garantiaTela.garantiaVinculada.id) {
			let tipoGarantia = this.garantiaTela.garantiaVinculada.id;
			this.campoFormulario = UtilsModal.buscarProcessoLocalStorage(this.appService, this.identificadorDossieFase, TIPO_VINCULO.GARANTIA, tipoGarantia);
			this.formularioGenericoService.montarListaCamposExisteExpressao(this.tipoFormulario, this.campoFormulario);
		} else {
			this.campoFormulario = Object.assign([], []);
			this.formularioGenericoService.montarListaCamposExisteExpressao(this.tipoFormulario, this.campoFormulario);

		}
		this.formularioDinamico = this.campoFormulario.some(campo => campo.obrigatorio);
		this.loadService.hide();
	}

	changeAcao(formulario: CampoFormulario) {
		this.mudancaSalvaService.setIsMudancaSalva(false);
		this.campoFormulario = this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, formulario, this.campoFormulario, [], [], []);
	}

	handleChangeFormValidacao(formValidacao) {
		this.formularioDinamico = !formValidacao.form.valid;
	}

	handleChangeCampoFormulario(input) {
		this.campoFormulario = Object.assign([], input);
	}
}
