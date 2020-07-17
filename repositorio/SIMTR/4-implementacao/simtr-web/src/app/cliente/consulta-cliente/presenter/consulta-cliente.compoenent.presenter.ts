import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { DialogService } from "angularx-bootstrap-modal";
import { ConsultaCliente } from "../model/consuta-cliente-model";
import { ApplicationService, LoaderService, AuthenticationService } from "../../../services";
import { LOCAL_STORAGE_CONSTANTS, SITUACAO_DOSSIE_PRODUTO, SITUACAO_DOSSIE_ATUAL, ARVORE_DOCUMENTOS, PERFIL_ACESSO, MSG_CONSULTA_DOSSIE_CLIENTE, UNDESCOR } from "../../../constants/constants";
import { VinculoCliente } from "../../../model";
import { VinculoArvore } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { VinculoArvoreCliente } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { ModalSelecaoDossieComponent } from "../../../dossie/modal-selecao-dossie/modal-selecao-dossie.component";
import { environment } from 'src/environments/environment';
import { ModalProcessoPorPessoaComponent } from "../modal-processo-por-pessoa/modal-processo-por-pessoa.component";
import { Utils } from '../../../utils/Utils';
import { MudancaSalvaService } from "src/app/services/mudanca-salva.service";
import { ConsultaClienteComponent } from "../view/consulta-cliente.component";
import { ModalSicliComponent } from "../aba-identificacao/modal/modal-sicli/modal-sicli.component";
import { ConsultaClienteService } from "../service/consulta-cliente-service";
import { Validators, FormBuilder } from "@angular/forms";
import { KzCpfValidator, KzCnpjValidator } from "src/app/shared";
import { UtilsCliente } from "../utils/utils-client";
import { AbaIdentificadorComponentPresenter } from "../aba-identificacao/presenter/aba-identificacao.component.presenter";

declare var $: any;

@Injectable()
export class ConsultarClienteComponentPresenter {

	consultaCliente: ConsultaCliente;
	cpfCnpj?: string;

	constructor(
		private router: Router,
		private formBuilder: FormBuilder,
		private dialogService: DialogService,
		private appService: ApplicationService,
		private authenticationService: AuthenticationService,
		private mudancaSalvaService: MudancaSalvaService,
		private clienteService: ConsultaClienteService,
		private abaIdentificacaoPresenter: AbaIdentificadorComponentPresenter,
		private loadService: LoaderService
	) { }

	private initValueInserirUser() {
		this.consultaCliente.userFound = true;
		this.consultaCliente.searchDone = true;
		this.consultaCliente.inserting = true;
		this.consultaCliente.searching = false;
	}

	resetAll(ModalSicliComponent: any) {
		this.consultaCliente.userFound = false;
		this.consultaCliente.inserting = false;
		this.consultaCliente.searching = false;
		this.consultaCliente.searchDone = false;
		this.consultaCliente.docFound = false;
		this.consultaCliente.isNewSearch = true;
		this.consultaCliente.sicliInformation = null;
		this.consultaCliente.sicliError = false;
		ModalSicliComponent.sicliUserOriginal = null;
		this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, false);
	}

	setVinculoCliente(vinculoCliente: VinculoCliente, listaVinculoArvore: Array<VinculoArvore>) {
		if (listaVinculoArvore && listaVinculoArvore.length > 0) {
			(listaVinculoArvore[0] as VinculoArvoreCliente).vinculoCliente = vinculoCliente;
			return vinculoCliente;
		}
		return null
	}

	montarListaDeDossieProdutoOuChamarDossieProduto(retorno: any, listaProdutos: any[], listaVinculoArvore: any[]) {
		listaProdutos = [];
		let listaProcesso:any[] = [];
		listaProcesso.push(retorno.processosSelecionados.idMacroProcesso);
		listaProcesso.push(retorno.processosSelecionados.idProcessoGeraDossie);
		for (const lst of this.getVinculoCliente(listaVinculoArvore).dossies_produto) {
			let hasDossie = false;
			if (listaProcesso.length === 2) {
				if (lst.processo_patriarca.id === listaProcesso[0] && lst.processo_dossie.id === listaProcesso[1]) {
					for (const lstP of listaProdutos) {
						if (lst.id_dossie_produto === lstP.id_dossie_produto) {
							hasDossie = true;
						}
					}
				}
			} else {
				if (lst.processo_patriarca.id.toString() === listaProcesso[1] && lst.processo_dossie.id.toString() === listaProcesso[2]) {
					for (const lstP of listaProdutos) {
						if (lst.id_dossie_produto === lstP.id_dossie_produto) {
							hasDossie = true;
						}
					}
				}
			}
			if (!hasDossie) {
				listaProdutos.push(lst);
			}
		}
		if (listaProdutos.length > 0) {
			this.showModalSelecao(true, false, listaProdutos, this.getVinculoCliente(listaVinculoArvore), listaProcesso);
		} else {
			this.router.navigate(['manutencaoDossie', this.getVinculoCliente(listaVinculoArvore).id, SITUACAO_DOSSIE_PRODUTO.NOVO, { processo: listaProcesso }]);
		}
	}

	getVinculoCliente(listaVinculoArvore: any): VinculoCliente {
		return (listaVinculoArvore[0] as VinculoArvoreCliente).vinculoCliente
	}

	showModalSelecao(habilitaOpcoes, habilitaLista, dossiesProduto, cliente, listaProcesso) {
		this.dialogService
			.addDialog(ModalSelecaoDossieComponent, {
				habilitaOpcoes: habilitaOpcoes,
				habilitaLista: habilitaLista,
				dossiesProduto: dossiesProduto,
				cliente: cliente,
				listaProcesso: listaProcesso
			})
			.subscribe(retorno => {
				if (retorno.opcao == SITUACAO_DOSSIE_PRODUTO.NOVO) {
					this.router.navigate(['manutencaoDossie', retorno.cliente, retorno.opcao, retorno.processo.toString()]);
				} else {
					this.router.navigate(['manutencaoDossie', retorno.dossieProduto != null ? retorno.dossieProduto.id_dossie_produto : 0, retorno.opcao]);
				}
			},
				() => {
					this.loadService.hide();
				});
	}
	inicializarVinculoArvoreCliente(vinculoArvoreCliente: VinculoArvoreCliente, lista: Array<VinculoArvore>) {
		vinculoArvoreCliente.nome = ARVORE_DOCUMENTOS;
		vinculoArvoreCliente.vinculoCliente = new VinculoCliente();
		lista = new Array<VinculoArvore>();
		lista.push(vinculoArvoreCliente);
		return lista;
	}


	goLink() {
		let clienteAux = new VinculoCliente();
		if (this.getVinculoCliente(this.consultaCliente.listaVinculoArvore) === null) {
			clienteAux.tipo_pessoa = this.isCpfInForm() ? "F" : "J";
		} else {
			clienteAux = this.getVinculoCliente(this.consultaCliente.listaVinculoArvore);
		}
		this.dialogService
			.addDialog(ModalProcessoPorPessoaComponent, {
				cliente: clienteAux
			})
			.subscribe(retorno => {
				if (retorno) {
					this.montarListaDeDossieProdutoOuChamarDossieProduto(retorno, this.consultaCliente.listaProdutos, this.consultaCliente.listaVinculoArvore);
				}
			});
	}

	isCpfInForm() {
		return Utils.removerTodosCaracteresEspeciais(this.consultaCliente.cpfCnpj).length == 11;
	}

	search(chamarUrl: boolean, consultaClienteComponent: ConsultaClienteComponent) {
		this.cpfCnpj = this.consultaCliente.cpfCnpj;
		this.resetAll(ModalSicliComponent);
		this.loadService.show();
		if (chamarUrl) {
			this.router.navigateByUrl('/principal', { skipLocationChange: true }).then(() =>
				this.router.navigate(['principal', Utils.removerTodosCaracteresEspeciais(this.cpfCnpj)]));
		} else {
			this.carregarDadosClientes(consultaClienteComponent);
		}
	}

	private verificarListaDeProduto(response: any) {
		if (response.dossies_produto != null && response.dossies_produto.length > 0) {
			this.consultaCliente.listaProdutos = response.dossies_produto;

			// verificar se processo gerador permiter ir direto para tela de tratamento
			this.consultaCliente.listaProdutos.forEach(dossieProduto => {

				if (dossieProduto.processo_dossie.id) {
					let referenciaProcessoGerador =
						JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + dossieProduto.processo_dossie.id));

					if (referenciaProcessoGerador && referenciaProcessoGerador.gera_dossie && referenciaProcessoGerador.gera_dossie == true
						&& referenciaProcessoGerador.tratamento_seletivo) {
						dossieProduto.tratamento_seletivo = referenciaProcessoGerador.tratamento_seletivo;
					}
				}

			});
		}
	}

	/**
	 * Guarda identificador do ultimo dossie de produto cadastrado pelo pf ou pj
	 * @param dossiesProduto lista de dossie de produto relacionado ao cliente
	 */
	private guardaIndentificadorUltimoDossieProdutoCadastrado(dossiesProduto: any[]){
		if(dossiesProduto != null && dossiesProduto.length > 0){
			let listaDossiesProdutoCriado = dossiesProduto.filter(dossieProduto =>
				   dossieProduto.tipo_relacionamento.principal
				&& dossieProduto.situacao_atual != SITUACAO_DOSSIE_ATUAL.RASCUNHO 
				&& dossieProduto.situacao_atual != SITUACAO_DOSSIE_ATUAL.FIANLIZADO
				&& dossieProduto.situacao_atual != SITUACAO_DOSSIE_ATUAL.FINALIZADO_CONFORME
				&& dossieProduto.situacao_atual != SITUACAO_DOSSIE_ATUAL.FINALIZADO_INCONFORME
				&& dossieProduto.situacao_atual != SITUACAO_DOSSIE_ATUAL.CANCELADO);
			if(listaDossiesProdutoCriado.length > 0){
				let ultimoDossieProduto = listaDossiesProdutoCriado.reduce((dossieProdutoAnterior, dossieProdutoAtual) => 
						dossieProdutoAnterior.id_dossie_produto < dossieProdutoAtual.id_dossie_produto  ? dossieProdutoAtual : dossieProdutoAnterior);
				this.consultaCliente.idUltimoDossieProdutoCadastrado =	ultimoDossieProduto.id_dossie_produto;
			}
		}
	}

	private loadCliente() {
		if (this.getVinculoCliente(this.consultaCliente.listaVinculoArvore) != null) {
			this.consultaCliente.userFound = true;
			this.consultaCliente.searching = true;
			this.consultaCliente.updateClienteForm = true;
		}
		this.isCPF();
		if (this.consultaCliente.dossiePessoaFisica) {
			this.checkUserSSO();
		}
	}

	private recarregaVariaveisPesquisaCasoErroConsultaDossieCliente(url: string) {
		this.consultaCliente.cpfCnpj = Utils.masKcpfCnpj(url.substring(url.lastIndexOf('/') + 1));
	}

	private carregarDadosClientes(consultaClienteComponent: ConsultaClienteComponent) {
		this.clienteService.getCliente(Utils.removerTodosCaracteresEspeciais(this.consultaCliente.cpfCnpj), this.isCpfInForm()).subscribe(response => {
			this.consultaCliente.searchDone = true;
			this.consultaCliente.userExiste = true;
			this.consultaCliente.listaProdutos = [];
			this.consultaCliente.cliente = this.setVinculoCliente(response, this.consultaCliente.listaVinculoArvore);
			this.consultaCliente.habilitaNovoProduto = true;
			this.verificarListaDeProduto(response);
			this.guardaIndentificadorUltimoDossieProdutoCadastrado(response.dossies_produto);
			this.loadCliente();
			this.loadService.hide();
		}, error => {
			if (error.status == 404) {
				this.recarregaVariaveisPesquisaCasoErroConsultaDossieCliente(error.url);
				this.consultaCliente.updateClienteForm = false;
				this.consultaCliente.userFound = false;
				this.consultaCliente.searchDone = true;
				if (this.isCpfInForm()) {
					this.consultaCliente.msgConsultaDossie = MSG_CONSULTA_DOSSIE_CLIENTE.CPF_NAO_ENCONTRADO;
				}
				else {
					this.consultaCliente.msgConsultaDossie = MSG_CONSULTA_DOSSIE_CLIENTE.CNPJ_NAO_ENCONTRADO;
				}
				this.consultaCliente.sicliError = true;
			}
			else if (error.status == 500) {
				consultaClienteComponent.addMessageError('Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.');
			}
			this.loadService.hide();
			throw error;
		});
	}

	/**
	 * Verifica se o usuário logado possui a permissao: MTRDOSOPE ou MTRADM.
	 */
	hasCredentialNivelDocumental(): boolean {
		const credentials: string = `${PERFIL_ACESSO.MTRADM},${PERFIL_ACESSO.MTRDOSOPE}`;
		if (!environment.production) {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesSSO());
		}
	}

	isNewSearch() {
		if (this.consultaCliente.isNewSearch) {
			this.consultaCliente.isNewSearch = false;
		}
	}

	inicializar(consultaClienteComponent: ConsultaClienteComponent) {
		this.mudancaSalvaService.setIsMudancaSalva(true);
		this.verificarSeERetornoDoMetodoSalvar(consultaClienteComponent);
		this.consultaCliente.route.params.subscribe((params: any) => {
			this.consultaCliente.cpfCnpjDossieProduto = params['cpfCnpj'] ? Utils.removerTodosCaracteresEspeciais(params['cpfCnpj']) : params['cpfCnpj'];
			this.consultaCliente.indexAba = undefined == params['aba'] ? 0 : params['aba'];
		});
		if (this.consultaCliente.isNewSearch) {
			this.consultaCliente.cpfCnpjDossieProduto = null;
		}
		this.consultaCliente.verBotaoCriaDossie = this.isCriaDossie();
		this.consultaCliente.productionEnvironment = environment.production;
	}

	private verificarSeERetornoDoMetodoSalvar(consultaClienteComponent: ConsultaClienteComponent) {
		let messagesSucess = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo);
		if (messagesSucess && messagesSucess != "false") {
			consultaClienteComponent.clearAllMessages();
			consultaClienteComponent.addMessageSuccess(messagesSucess);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, false);
		}
	}

	isCriaDossie() {
		return this.abaIdentificacaoPresenter.isEnviromentProduction();
	}

	/**
	 * Limpa tudo para realizar uma nova consulta
	 */
	goNewSearch(consultaClienteComponent: ConsultaClienteComponent) {
		this.resetAll(ModalSicliComponent);
		this.consultaCliente.cliente = this.setVinculoCliente(null, this.consultaCliente.listaVinculoArvore);
		this.inicializar(consultaClienteComponent);
		this.router.navigate(['principal']);
	}

	/**
 * Evento lançado após o usuário ser inserido
 */
	afterInsertUser(event) {
		if (event) {
			this.consultaCliente.inserting = false;
			this.consultaCliente.userFound = true;
			this.consultaCliente.searching = true;
			this.consultaCliente.habilitaNovoProduto = true;
		}
	}

	/**
 * Cancelamento inclusão
 * @param event 
 */
	onCancel(event, consultaClienteComponent: ConsultaClienteComponent) {
		if (event) {
			this.resetAll(ModalSicliComponent);
			this.consultaCliente.cliente = this.setVinculoCliente(null, this.consultaCliente.listaVinculoArvore);
			this.inicializar(consultaClienteComponent);
		}
	}

	/**
 * Habilita inclusão manual
 */
	insertUser() {
		this.resetAll(ModalSicliComponent);
		this.initValueInserirUser();
	}

	maskCPfCnpj() {
		if (this.consultaCliente.cpfCnpj) {
			this.consultaCliente.cpfCnpj = Utils.masKcpfCnpj(this.consultaCliente.cpfCnpj);
			if (this.isCpfInForm()) {
				this.consultaCliente.formulario = this.formBuilder.group({
					cpfCnpj: [this.consultaCliente.cpfCnpj, [Validators.required, KzCpfValidator]]
				});
			} else {
				this.consultaCliente.formulario = this.formBuilder.group({
					cpfCnpj: [this.consultaCliente.cpfCnpj, [Validators.required, KzCnpjValidator]]
				});
			}
		}
	}

	verifyCpfCnpj(): boolean {
		const field = this.consultaCliente.formulario.get('cpfCnpj');
		let param = field.value ? Utils.removerTodosCaracteresEspeciais(field.value) : "";
		this.consultaCliente.tipoConsulta = "";

		if (!param || (param.length !== 11 && param.length !== 14)) {
			this.resetAll(ModalSicliComponent);
			return true;
		} else if (param.length == 11 && field.errors && field.errors['cpf']) {
			this.resetAll(ModalSicliComponent);
			this.consultaCliente.tipoConsulta = "CPF inválido";
			return true;
		} else if (param.length == 14 && field.errors && field.errors['cnpj']) {
			this.resetAll(ModalSicliComponent);
			this.consultaCliente.tipoConsulta = "CNPJ inválido";
			return true;
		}

		return false;
	}

	montagemFormularioCombaseNoTipoCpfOuCnpj(consultaClienteComponent: ConsultaClienteComponent) {
		let vinculoArvoreCliente = new VinculoArvoreCliente();
		if (undefined == this.consultaCliente.cpfCnpjDossieProduto) {
			this.consultaCliente.cliente = vinculoArvoreCliente.vinculoCliente;
			this.consultaCliente.formulario = this.formBuilder.group({
				cpfCnpj: [null, [Validators.required, KzCpfValidator]]
			});
			this.consultaCliente.cpfCnpj = null;
			this.loadService.hide();
		}
		else {
			this.consultaCliente.cpfCnpj = Utils.masKcpfCnpj(this.consultaCliente.cpfCnpjDossieProduto);
			this.consultaCliente.formulario = this.formBuilder.group({
				cpfCnpj: [this.consultaCliente.cpfCnpj, [Validators.required, KzCpfValidator]]
			});
			this.search(false, consultaClienteComponent);
		}
		this.consultaCliente.listaVinculoArvore = this.inicializarVinculoArvoreCliente(vinculoArvoreCliente, this.consultaCliente.listaVinculoArvore)
	}

	onClienteIniciado(event) {
		this.consultaCliente.cliente = this.setVinculoCliente(event, this.consultaCliente.listaVinculoArvore);
	}

	handlleMessagesSucess(messages, consultaClienteComponent: ConsultaClienteComponent) {
		let messagesString: string = messages[0];
		if (messagesString.includes("atualizado")) {
			consultaClienteComponent.messagesSuccess = messages;
			return;
		}
		this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.dossieSalvo, messages);
	}

	handleChange() {
		if ($.caixaFixAngular) {
			$.caixaFixAngular.fix();
		}
	}

	/**
	 * Verifica se o usuário pessoa fisica possui cadastro no SSO
	 * @param consultaClienteComponent 
	 */
	checkUserSSO() {
		this.clienteService.userSSOIsPresent(Utils.retiraMascaraCPF(this.consultaCliente.cpfCnpj)).subscribe(() => {
			this.consultaCliente.userSSO = true;
			this.loadService.hide();
		}, () => {
			this.consultaCliente.userSSO = false;
			this.loadService.hide();
		});
	}

	/**
	 * Verifica se cliente é uma pessoa física
	 */
	private isCPF() {
		this.consultaCliente.dossiePessoaFisica = UtilsCliente.isCPF(this.consultaCliente.cpfCnpj);
	}

	/**
	 * Notifica a criação de um novo usuario SSO
	 * @param userCreateSucessSSO 
	 */
	changeUserSucessSSO(userCreateSucessSSO: boolean) {
		this.consultaCliente.userSSO = userCreateSucessSSO;
	}
}