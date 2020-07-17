import { Utils } from '../../../../../utils/Utils';
import { UtilsCliente } from '../../../../../cliente/consulta-cliente/utils/utils-client';
import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit, ViewEncapsulation, AfterViewInit, ViewChild } from '@angular/core';
import { ConsultaClienteService } from '../../../../../cliente/consulta-cliente/service/consulta-cliente-service';
import { LoaderService } from '../../../../../services/http-interceptor/loader/loader.service';
import { KzCpfValidator, KzCnpjValidator } from '../../../../../shared';
declare var $: any;
import { FormGroup, FormBuilder, Validators, NgForm } from '@angular/forms';
import { VinculoCliente, CampoFormulario } from '../../../../../model';
import { TIPO_MACRO_PROCESSO, MSG_MODAL_PESSOA, TIPO_VINCULO, TIPO_DOCUMENTO, LOCAL_STORAGE_CONSTANTS, UNDESCOR } from 'src/app/constants/constants';
import { environment } from 'src/environments/environment';
import { SelectItem, Dropdown } from 'primeng/primeng';
import { IdentificadorDossieFase } from './model/identificadorDossieEFase';
import { ApplicationService } from 'src/app/services';
import { UtilsModal } from '../Utils/utils-modal';
import { FormularioGenericoService } from 'src/app/documento/formulario-generico/service/formulario-generico.service';
import { RespostaFormulario } from '../../../model-endPoint-dossie-produto/respostaFormulario';
import { TipoCampoExpressao } from 'src/app/documento/formulario-generico/model/formulario-tipo-campo.model';

export interface MessageModel {
	listaTipoRelacionamento: any[];
	listaCliente: VinculoCliente[];
	vinculoCliente: VinculoCliente;
	tipoPessaGeraDossie: any;
	identificadorDossieFase: IdentificadorDossieFase;
}

export interface MessageModelSaida {
	clientePessoaModal: VinculoCliente;
	tipoRelacionamentoModal: any;
}

@Component({
	selector: 'modal-pessoa',
	templateUrl: './modal-pessoa.component.html',
	styleUrls: ['./modal-pessoa.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class ModalPessoaComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit {

	@ViewChild('tipoRelacionamentoDrop')
	tipoRelacionamentoDrop: Dropdown;
	
	//Vinculo do Cliente que está sendo editado.
	vinculoCliente: VinculoCliente;

	//Max Date para o campo Calendar
	maxDate: Date;

	//Configuracao para o campo calendar com formato PT BR
	ptBR: any;

	//Indica se o formulario de criação de usuario está ativo
	inserting = false;

	//Valor do CPF ou CNPJ do campo utilizado para realizar as consultas da tela
	cpfCnpj: string;

	//Controle para indicar se houve consulta realizada com sucesso
	consultaRealizada: boolean = false;

	//Armazena o nome do vinculo, podendo ser razao social ou nome da pessoa
	nomeVinculo: string;

	//Lista dos possiveis tipos de relacionamentos do Vinculo
	listaRelacionamentos: SelectItem[];
	
	//Tipo Relacionamento selecionado
	tipoRelacionamento: SelectItem;
	
	//Fase do processo para buscar possiveis campos de formulario
	identificadorDossieFase: IdentificadorDossieFase;

	//Grupo Formulario utilizado para controle das informacoes na tela
	formulario: FormGroup;

	//Lista de cliente que podem ser relacionados
	listaClienteCombo: SelectItem[];

	//Lista dos clientes ja relacionados, necessario para opcoes do novo relacionado
	listaCliente: VinculoCliente[];
	
	//Vinculo cliente utilizado para armazenar as informacoes apresentadas na tela
	clientePessoa: VinculoCliente = new VinculoCliente();

	//Lista contendo os tipos de relacionamento
	listaTipoRelacionamento: any[];

	//Lista dos campos formulario
	listaCampoForumlario: CampoFormulario[] = [];

	//Tipo de Pessoa que pode ser associado ao dossie
	tipoPessaGeraDossie: any;

	//Lista de portes apresentado ao inserir um novo registro
	tipoPortes: SelectItem[];

	//Tipo de formulario a ser carregado no formulario dinamico
	tipoFormulario: string;

	//Tipo de Relacionado 
	tipoRelacionado: SelectItem;

	//Variavel de controle para botao salvar
	habilitarBtnSalvar = true;

	//Apresenta a mensagem de Relacionado Obrigatorio
	relacionadoObrigatorio = false;

	//Data Nascimento informada no formulario de inserir Cliente
	dataNascimento: Date;

	//Variavel que armazena valor a ser apresentado na consulta do CPF ou CNPJ
	tipoConsulta: string;

	//Variavel que amarzena se o Formulario Dinamico esta valido
	formDinamicoValido: boolean;

	//Lista de campos formularios a serem apresentados de acordo com tipo relacionamento
	campoFormulario: CampoFormulario[] = [];

	messageFormularioVazio: string = "Nâo são esperado dados adicionais para este tipo de relacionamento."

	constructor(
		dialogService: DialogService,
		private clienteService: ConsultaClienteService,
		private formBuilder: FormBuilder,
		private loadService: LoaderService,
		private formularioGenericoService: FormularioGenericoService,
		private appService: ApplicationService
	) {
		super(dialogService);
	}

	ngOnInit() {
		//Inicia o formulario e a validação do campo cpfCnpj
		this.formulario = this.formBuilder.group({
			cpfCnpj: [this.cpfCnpj, [Validators.required, KzCpfValidator]]
		});

		this.nomeVinculo = "";
	
		if(this.vinculoCliente){
			this.carregarFormularioAlteracao();
		}else{
			this.habilitarBtnSalvar = false;
		}
	}

	carregarFormularioAlteracao(){
		this.clientePessoa =  Object.assign({}, this.vinculoCliente);
		this.consultaRealizada = true;
		this.cpfCnpj = this.vinculoCliente.cpf ? Utils.masKcpfCnpj(this.vinculoCliente.cpf) : Utils.masKcpfCnpj(this.vinculoCliente.cnpj);
		this.nomeVinculo = this.vinculoCliente.cpf ? this.vinculoCliente.nome : this.vinculoCliente.razao_social;
		this.carregarTiposRelacionamento(this.isCpfInForm() ? "F" : "J");
		this.tipoRelacionamento = {"value": this.clientePessoa.tipo_relacionamento.id, "label": this.clientePessoa.tipo_relacionamento.nome};
		this.clientePessoa.seqTitularidadeAntiga = this.vinculoCliente.sequencia_titularidade;
		this.carregarComboRelacionados();
		this.montarCampoFormulario(this.tipoRelacionamento.value);
		this.informRelacioandoAtivo();

		this.tipoRelacionado = (this.listaClienteCombo as any[]).find(cliente => cliente.id  == this.clientePessoa.dossie_cliente_relacionado);

		if (this.vinculoCliente && this.vinculoCliente.respostas_formulario) {
			this.campoFormulario.forEach(campo => {
				for (let formulario of this.vinculoCliente.respostas_formulario) {
					if (formulario.campo_formulario === campo.id || formulario.id_campo_formulario === campo.id) {
						UtilsModal.popularCamposFormularioConsulta(campo, formulario);
					}
				}
			});
		}
	}

	

	changeTipoRelacionamento() {
		this.clientePessoa.tipo_relacionamento = this.listaTipoRelacionamento.find(lstTipo => lstTipo.id == this.tipoRelacionamento.value);
		this.montarCampoFormulario(this.tipoRelacionamento.value);
		this.limparCamposRelacionamento();
		this.carregarComboRelacionados();
		this.habilitarBtnSalvar = this.atualizarBtnSalvar();
	}

	atualizarBtnSalvar(){
		if(this.campoFormulario.length > 0 && !this.formDinamicoValido){
			return false;
		}
		
		if(this.clientePessoa.tipo_relacionamento.indica_relacionado && 
			!this.clientePessoa.dossie_cliente_relacionado
		){
			return false;
		}
		if(this.clientePessoa.tipo_relacionamento.indica_sequencia && 
			!this.clientePessoa.sequencia_titularidade){
			return false;
		}
		return true;
	}

	search() {
		this.clientePessoa = new VinculoCliente();
		//Caso o component exista, ele deve ser limpado
		if(this.tipoRelacionamentoDrop){
			this.tipoRelacionamentoDrop.value = null;
		}
		this.consultarCpfCnpj();
	}

	carregarTiposRelacionamento(tipoPessoa) {
		this.listaRelacionamentos = [];
		this.listaTipoRelacionamento.sort(function (a, b) {
			if (a.nome > b.nome) {
				return 1;
			}
			if (a.nome < b.nome) {
				return -1;
			}
			// a must be equal to b
			return 0;
		});

		this.listaTipoRelacionamento.forEach(element => {
			if (element.tipo_pessoa === tipoPessoa || element.tipo_pessoa === "A") {
				let objselected: any = {};
				objselected.value = element.id;
				objselected.label = element.nome;
				this.listaRelacionamentos.push(objselected);
			}
		});
	}

	private informRelacioandoAtivo() {
		let relacionado = this.listaClienteCombo.find(relacionado => this.clientePessoa.vinculoRelacionadoPessoa === relacionado.value);
		if (relacionado) {
			this.tipoRelacionado = relacionado;
		}
	}

	maskCPfCnpj(event) {
		this.cpfCnpj = Utils.masKcpfCnpj(event.target.value);
		this.nomeVinculo = "";
		this.clientePessoa.nome = "";
		if (this.isCpfInForm()) {
			this.formulario = this.formBuilder.group({
				cpfCnpj: [this.cpfCnpj, [Validators.required, KzCpfValidator]]
			});
		} else {
			this.formulario = this.formBuilder.group({
				cpfCnpj: [this.cpfCnpj, [Validators.required, KzCnpjValidator]]
			});
		}
	}

	private montarCampoFormulario(idRelacionado: number) {
		this.formDinamicoValido = true;
		this.listaCampoForumlario = UtilsModal.buscarProcessoLocalStorage(this.appService, this.identificadorDossieFase, TIPO_VINCULO.CLIENTE, idRelacionado);
		this.formularioGenericoService.montarListaCamposExisteExpressao(this.tipoFormulario, this.listaCampoForumlario);
		this.listaCampoForumlario.forEach(formulario => {
			//Caso exista um elemento na lista obrigatorio o Formulario Dinamico deixa de ser valido
			this.formDinamicoValido = this.formDinamicoValido  && formulario.obrigatorio ? false : true;
			this.listaCampoForumlario = this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, formulario, this.listaCampoForumlario, null, null, null);
		});
		this.campoFormulario = Object.assign([], this.listaCampoForumlario);
	}

	  changeRelacionado() {
		if (this.tipoRelacionado) {
			this.clientePessoa.vinculoRelacionadoPessoa = this.tipoRelacionado.value;
			let idDossieClienteRelacionado:number = this.listaCliente.find(cliente => cliente.cpf === Utils.removerTodosCaracteresEspeciais(this.tipoRelacionado.value) || cliente.cnpj === Utils.removerTodosCaracteresEspeciais(this.tipoRelacionado.value)).id;
			this.clientePessoa.dossie_cliente_relacionado = idDossieClienteRelacionado;
		}else{
			this.relacionadoObrigatorio = true;
		}
		this.habilitarBtnSalvar = this.atualizarBtnSalvar();
	}

	private consultarCpfCnpj() {
		this.loadService.show();

		this.clienteService.
			getCliente(Utils.removerTodosCaracteresEspeciais(this.cpfCnpj), this.isCpfInForm())
			.subscribe(response => {
			this.clientePessoa = response;
			this.inserting = false;
			this.nomeVinculo = this.clientePessoa.nome ?  this.clientePessoa.nome : this.clientePessoa.razao_social;
			this.clientePessoa.nome = this.nomeVinculo;
			this.carregarTiposRelacionamento(this.isCpfInForm() ? "F" : "J");
			this.consultaRealizada = true;
			this.habilitarBtnSalvar = false;
			this.tipoRelacionado = null;
			this.clientePessoa.tipo_relacionamento  = null;
			this.montarCampoFormulario(0);
			
			this.loadService.hide();
		}, error => {
			this.clientePessoa = new VinculoCliente();
			this.inserting = this.isEnviromentProduction();
			if (this.inserting && error.status === 404) {
				this.consultaRealizada = false;
				this.carregarFormularioInsert();
			}
			this.loadService.hide();
			throw error;
		});
		return;
	}

	private carregarFormularioInsert() {
		
		this.carregarCpfCnpjInsert();
		
		//Carrega Lista de Portes
		this.tipoPortes = [];
		UtilsCliente.tipoPortes().forEach(elemento => {
			this.tipoPortes.push({ label: elemento.descricao, value: elemento.sigla })
		});

		this.carregarComponentCalendar();
		
	}

	private carregarComponentCalendar(){
		// Carregar valores para o DATE do cadastro, tambem deve ser carregando somente no cadastro
		this.maxDate = new Date();
		this.ptBR = {
			firstDayOfWeek: 1,
			dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
			dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
			dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
			monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
			monthNamesShort: ['Jan', 'Feb', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dec'],
			today: 'Hoje',
			clear: 'Limpar'
		};

	}

	private carregarCpfCnpjInsert() {
		if (this.isCpfInForm()) {
			this.clientePessoa.tipo_pessoa = 'F';
			this.clientePessoa.cpf = Utils.removerTodosCaracteresEspeciais(this.cpfCnpj);
			return;
		}
		this.clientePessoa.tipo_pessoa = 'J';
		this.clientePessoa.cnpj = Utils.removerTodosCaracteresEspeciais(this.cpfCnpj);
		this.clientePessoa.conglomerado = false;
	}

	/**
	 * Toda vez que pesquisar recebe a lista de cliente e remover o cpf ou cnpj informado da lista para a combo Relacionado
	 */
	private carregarComboRelacionados() {
		this.listaClienteCombo = [];
		this.listaCliente.forEach(obj => {
			const cpfCnpj = obj.cpf ? Utils.masKcpfCnpj(obj.cpf) : Utils.masKcpfCnpj(obj.cnpj);
			//Não deve carregar o registro duplicado ou registro do vinculo principal
			if(this.cpfCnpj !== cpfCnpj && 
			!this.listaClienteCombo.find(cliente => cliente.value === this.cpfCnpj)){
				let objselected: any = {};
				objselected.label = cpfCnpj;
				objselected.value = cpfCnpj;
				objselected.id = obj.id;
				this.listaClienteCombo.push(objselected);
			}

		});
	}

	obterProximaSequencia(){
		if(!this.listaCliente || this.listaCliente.length === 0){
			return 1;
		}
		let maxSequencia = Math.max(...this.listaCliente.map(o => o.sequencia_titularidade), 0);
		return maxSequencia ? maxSequencia + 1 : 1;
	}

	verifyCpfCnpj() {
		const field = this.formulario.get('cpfCnpj');
		let param = field.value ? Utils.removerTodosCaracteresEspeciais(field.value) : "";
		this.tipoConsulta = "";

		if (!param || (param.length !== 11 && param.length !== 14)) {
			return true;
		} else if (param.length == 11 && field.errors && field.errors['cpf']) {
			this.tipoConsulta = "CPF inválido";
			return true;
		} else if (param.length == 14 && field.errors && field.errors['cnpj']) {
			this.tipoConsulta = "CNPJ inválido";
			return true;
		}
		return false;
	}

	isCpfInForm() {
		return Utils.removerTodosCaracteresEspeciais(this.cpfCnpj).length <= 11;
	}

	confirm() {
		// nós definimos o resultado do diálogo como verdadeiro ao clicar no botão confirmar,
		if (this.tipoPessaGeraDossie !== TIPO_MACRO_PROCESSO.A && this.tipoPessaGeraDossie !== this.clientePessoa.tipo_pessoa && this.clientePessoa.tipo_relacionamento.principal) {
			Utils.showMessageDialog(this.dialogService, MSG_MODAL_PESSOA.PROCESSO_NAO_PERMITIDO_CPF_CNPJ);
			return;
		}
		
		if(this.validarSequenciaRepetida() && this.validaVinculoPessoaExistente()){
			this.montarVinculoCliente();
			this.retornoModal();
		};
		
		
	}

	private limparCamposRelacionamento() {
		this.clientePessoa.sequencia_titularidade = this.clientePessoa.tipo_relacionamento.indica_sequencia ? this.obterProximaSequencia() : undefined;
		this.clientePessoa.relacionado = undefined;
		this.habilitarBtnSalvar = this.atualizarBtnSalvar();
	}


	/**
	 * Apos verificar se o tipo de dossie e relativo a sequencia do principal
	 */
	private validaVinculoPessoaExistente() {
		if (this.validarExistenciaDeNomeETipoRelacionamento()) {
			Utils.showMessageDialog(this.dialogService, MSG_MODAL_PESSOA.VALIDA_EXISTENCIA_NA_LISTA);
			return false;
		}
		return true;
	}

	private verificarCpfOuCnpj(pessoa: VinculoCliente) {
		return pessoa.cnpj ? pessoa.cnpj : pessoa.cpf;
	}

	private validarExistenciaDeNomeETipoRelacionamento() {
		let retorno = false;

		let validar: boolean = this.vinculoCliente ? this.isTeveAlteracaoNoObjeto() : true;
		
		if(validar){
			this.listaCliente.forEach(cliente => {
				if ((this.verificarCpfOuCnpj(cliente) === Utils.removerTodosCaracteresEspeciais(this.cpfCnpj) && cliente.tipo_relacionamento.nome == this.tipoRelacionamento.label)) {
					retorno = true;
				}
			});
		}
		
		return retorno;
	}

	/**
	 * Apos validar Existencia na lista valida se existe sequencia Repetida
	 */
	private validarSequenciaRepetida() {
		if (this.clientePessoa.tipo_relacionamento.indica_sequencia && this.checkSequenciaRepetida(this.clientePessoa.sequencia_titularidade)) {
			Utils.showMessageDialog(this.dialogService, MSG_MODAL_PESSOA.SEQUENCIA_TITULARIDADE_INFORMADA);
			return false;
		}
		return true;
	}

	/**
	 * Check se existe sequencia
	 * @param seq
	 */
	private checkSequenciaRepetida(seq: number) {
		let retorno = false;
		this.listaCliente.forEach(cliente_1 => {
			if (cliente_1.sequencia_titularidade == seq) {
				retorno = true;
				
			}
		});
		return retorno;
	}

	private retornoModal() {
		this.result = { clientePessoaModal: this.clientePessoa, tipoRelacionamentoModal: this.tipoRelacionamento };
		this.close();
	}

	private montarVinculoCliente() {
		this.clientePessoa.ic_tipo_relacionamento = this.clientePessoa.tipo_relacionamento.nome;
		this.clientePessoa.principal = this.clientePessoa.tipo_relacionamento.principal;
		this.clientePessoa.relacionado = this.clientePessoa.vinculoRelacionadoPessoa;
		this.clientePessoa.respostas_formulario = [];
		this.clientePessoa.campos_formulario = this.campoFormulario;
		
		this.campoFormulario.forEach(campo => {
			let resposta = new RespostaFormulario();
			UtilsModal.preencherlistarResposta(campo, resposta);
			if ((resposta.opcoes_selecionadas && resposta.opcoes_selecionadas.length > 0) || (resposta.resposta && resposta.resposta != "")) {
				this.clientePessoa.respostas_formulario.push(resposta);
			}
		});
	}

	isTeveAlteracaoNoObjeto(){
		if(this.clientePessoa.cpf == this.vinculoCliente.cpf && this.clientePessoa.cnpj == this.vinculoCliente.cnpj 
			&& this.clientePessoa.tipo_relacionamento.nome == this.vinculoCliente.tipo_relacionamento.nome){
				return false;
		}
		return true;
	}

	cancel() {
		this.result = null;
		this.close();
	}

	onInsertUser() {
		if (this.isCpfInForm()) {
			this.clientePessoa.data_nascimento = this.dataNascimento != null ? Utils.transformPrimeNGDateToInsert(this.dataNascimento) : null;
		} else {
			this.clientePessoa.cnpj = Utils.removerTodosCaracteresEspeciais(this.cpfCnpj);
			this.clientePessoa.data_fundacao = this.dataNascimento != null ? Utils.transformPrimeNGDateToInsert(this.dataNascimento) : null;
			this.clientePessoa.nome = this.clientePessoa.razao_social;
		}

		this.clienteService.insertCliente(this.clientePessoa).subscribe(
			response => {
				Utils.showMessageDialog(this.dialogService, 'Usuário adicionado com sucesso').subscribe(isConfirmed => {
					if (isConfirmed) {
						this.inserting = false;
						this.clientePessoa.id = response.id;
						if (this.isCpfInForm()) {
							this.cpfCnpj = Utils.masKcpfCnpj(this.clientePessoa.cpf);
						} else {
							this.cpfCnpj = Utils.masKcpfCnpj(this.clientePessoa.cnpj);
						}
						this.consultarCpfCnpj();
					}
				},
					() => {
						this.loadService.hide();
					});
			},
			() => {
				this.loadService.hide();
			});
	}

	cancelInsert() {
		this.inserting = false;
		this.clientePessoa = new VinculoCliente();
	}

	maskCalendar(event): void {
		event.target.value = Utils.mascaraData(event);
	}

	isEnviromentProduction(): boolean {
		return environment.production ? false : true;
	}

	handleChangeFormValidacao(formv) {
		this.formDinamicoValido = formv.valid;
		this.habilitarBtnSalvar = this.atualizarBtnSalvar();
		
	}

	handleChangeCampoFormulario(input) {
		this.campoFormulario = Object.assign([], input);
	}
}
