import { Component, OnInit, ViewEncapsulation, AfterViewChecked } from '@angular/core';
import { AlertMessageService } from '../../../services';
import { ActivatedRoute } from '@angular/router';
import { ConsultarClienteComponentPresenter } from '../presenter/consulta-cliente.compoenent.presenter';
import { ConsultaCliente } from '../model/consuta-cliente-model';

@Component({
	selector: 'consulta-cliente',
	templateUrl: './consulta-cliente.component.html',
	styleUrls: ['./consulta-cliente.component.css']
})
export class ConsultaClienteComponent extends AlertMessageService implements OnInit, AfterViewChecked {

	consultaClientePresenter: ConsultarClienteComponentPresenter;
	route: ActivatedRoute;

	constructor(
		route: ActivatedRoute,
		consultaClientePresenter: ConsultarClienteComponentPresenter
	) {
		super();
		this.consultaClientePresenter = consultaClientePresenter;
		this.route = route;
		this.consultaClientePresenter.consultaCliente = new ConsultaCliente();
		this.consultaClientePresenter.consultaCliente.route = this.route;
	}

	ngAfterViewChecked(): void {
		this.consultaClientePresenter.isNewSearch();
	}

	ngOnInit() {
		this.consultaClientePresenter.inicializar(this);
		this.consultaClientePresenter.montagemFormularioCombaseNoTipoCpfOuCnpj(this);
	}

	isCpfInForm() {
		return this.consultaClientePresenter.isCpfInForm();
	}

	onClienteInicado(event) {
		this.consultaClientePresenter.onClienteIniciado(event);
	}

	chamaConsulta() {
		this.search(true)
	}

	handlleMessagesWarning(messages) {
		this.messagesWarning = messages
	}

	handlleMessagesError(messages) {
		this.messagesError = messages;
	}

	handlleMessagesInfo(messages) {
		this.messagesInfo = messages;
	}

	handleUserCreateSucessSSO(userCreateSucessSSO: boolean) {
		this.consultaClientePresenter.changeUserSucessSSO(userCreateSucessSSO);
	}

	handlleMessagesSucess(messages) {
		this.consultaClientePresenter.handlleMessagesSucess(messages, this);
	}

	isCriaDossie() {
		return this.consultaClientePresenter.isCriaDossie();
	}

	search(chamarUrl: boolean) {
		this.consultaClientePresenter.search(chamarUrl, this);
	}

	verifyCpfCnpj() {
		return this.consultaClientePresenter.verifyCpfCnpj();
	}

	maskCPfCnpj() {
		this.consultaClientePresenter.maskCPfCnpj();
	}

	/**
	 * Cuida do evento de mudança de abas
	 * @param event 
	 */
	handleChange(event) {
		this.consultaClientePresenter.handleChange();
	}

	/**
	 * Habilita inclusão manual
	 */
	insertUser() {
		this.consultaClientePresenter.insertUser();
	}

	/**
	 * Cancelamento inclusão
	 * @param event 
	 */
	onCancel(event) {
		this.consultaClientePresenter.onCancel(event, this);
	}

	/**
	 * Evento lançado após o usuário ser inserido
	 */
	afterInsertUser(event) {
		this.consultaClientePresenter.afterInsertUser(event);
	}

	/**
	 * Limpa tudo para realizar uma nova consulta
	 */
	goNewSearch() {
		this.consultaClientePresenter.goNewSearch(this);
	}

	goLink() {
		this.consultaClientePresenter.goLink();
	}

	/**
	 * Verifica se o usuário logado possui a permissao: MTRDOSOPE
	 */
	hasCredentialNivelDocumental(): boolean {
		return this.consultaClientePresenter.hasCredentialNivelDocumental();
	}

}
