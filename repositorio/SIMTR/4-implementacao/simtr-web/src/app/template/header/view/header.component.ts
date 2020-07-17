import { Component, OnInit, AfterViewInit, ViewEncapsulation, AfterViewChecked } from '@angular/core';
import { GlobalError } from 'src/app/global-error/model/global-error';
import { InputOutputHeaderService } from '../input-output-service/input-output-header.service';
import { HeaderComponentPresenter } from '../presenter/header.component.presenter';
import { Header } from '../model/header.model';

declare var $: any;

@Component({
	selector: 'cx-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class HeaderComponent extends InputOutputHeaderService implements OnInit, AfterViewInit, AfterViewChecked {

	headerComponentPresenter: HeaderComponentPresenter;

	constructor(headerComponentPresenter: HeaderComponentPresenter) {
		super();
		this.headerComponentPresenter = headerComponentPresenter;
		this.headerComponentPresenter.header = new Header();
	}

	ngOnInit() {
		this.headerComponentPresenter.init();
	}

	verificarPermissaoManual(roles: string): boolean {
		return this.headerComponentPresenter.checkRolesByUserManual(roles);
	}

	ngAfterViewInit(): void {
		this.headerComponentPresenter.afterViewInit(this);
	}

	ngAfterViewChecked() {
		this.headerComponentPresenter.afterViewChecked();
	}

	atualizarPatriarca() {
		this.headerComponentPresenter.atualizarPatriarca();
	}

	retornarQtdErro() {
		return this.headerComponentPresenter.retornarQtdErro();
	}

	clickSideBar() {
		this.headerComponentPresenter.clickSideBar(this);
	}

	/**Usado para trocar o perfil da aplicação; utilização com environment.mockRoles = true */
	selecionarPerfil() {
		this.headerComponentPresenter.selectProfile(this);
	}

	isApresentaBarraPesquisa() {
		return this.headerComponentPresenter.isApresentaBarraPesquisa();
	}

	isApresentaPerfilUsuario() {
		return this.headerComponentPresenter.isApresentaPerfilUsuario();
	}

	isApresentaNotificacoes() {
		return this.headerComponentPresenter.isApresentaNotificacoes();
	}

	isApresentaWidgetSidebar() {
		return this.headerComponentPresenter.isApresentaWidgetSidebar();
	}

	ativarToogle() {
		this.headerComponentPresenter.activeToogle();
	}

	getNome() {
		return this.headerComponentPresenter.getName();
	}

	getMatricula() {
		return this.headerComponentPresenter.getMatricula();
	}

	getUnidade() {
		return this.headerComponentPresenter.getUnidade();
	}

	getDeMatriculaAndUserName() {
		return this.headerComponentPresenter.getDeMatriculaAndUserName();
	}

	getCoUnidadeAndNoUnidade() {
		return this.headerComponentPresenter.getCoUnidadeAndNoUnidade();
	}

	getPerfil() {
		return this.headerComponentPresenter.getProfile();
	}

	clean() {
		this.headerComponentPresenter.clean();
	}

	search() {
		this.headerComponentPresenter.search();
	}

	getBufferUserImage() {
		return this.headerComponentPresenter.getBufferUserImage();
	}

	openError(error: GlobalError) {
		this.headerComponentPresenter.openError(error);
	}

	limparErros() {
		this.headerComponentPresenter.clearErrors();
	}

	logout() {
		this.headerComponentPresenter.logout();
	}

	isAutenticado() {
		return this.headerComponentPresenter.checkAuthentication();
	}

	carregarPatriarca() {
		this.headerComponentPresenter.carryPatriarch();
	}

}
