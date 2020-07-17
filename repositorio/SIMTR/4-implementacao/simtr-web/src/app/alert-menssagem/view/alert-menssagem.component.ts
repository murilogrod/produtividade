import { Component, OnInit, ViewEncapsulation, Input, OnChanges } from "@angular/core";
import { AlertMenssagemComponentPresenter } from "../presenter/alert-menssagem.component.presenter";
import { ModalAlertBoolean } from "../model/modal-alert-boolean";
import { ModalAlertMensagem } from "../model/modal-alert-mensagens";
import { ApplicationService } from "src/app/services";
import { LOCAL_STORAGE_CONSTANTS, MESSAGE_ALERT_MENU } from "src/app/constants/constants";

@Component({
	selector: 'alert-menssagem',
	templateUrl: './alert-menssagem.component.html',
	styleUrls: ['./alert-menssagem.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class AlertMenssagemComponent implements OnInit, OnChanges {
	
	alertMenssagemPresenter: AlertMenssagemComponentPresenter;

	@Input() msgOrientacao: string;
	@Input() ocultarBtnFechar: boolean;
	@Input() tituloMsg: string;
	
	constructor(
		private appService: ApplicationService,
		alertMenssagemPresenter: AlertMenssagemComponentPresenter
	) {
		this.alertMenssagemPresenter = alertMenssagemPresenter;
		this.alertMenssagemPresenter.modalAlert = new ModalAlertBoolean();
		this.alertMenssagemPresenter.modalMsg = new ModalAlertMensagem();
	}

	ngOnInit() {
		this.alertMenssagemPresenter.modalAlert.openMensagem = (this.msgOrientacao && this.msgOrientacao != "") ? true : false;
		this.alertMenssagemPresenter.modalAlert.apresentarTitulo = false;
	}

	ngOnChanges() {		
		this.alertMenssagemPresenter.modalAlert.openMensagem = (this.msgOrientacao && this.msgOrientacao != "") ? true : false;
	}

	onCloseMensagem() {
		this.alertMenssagemPresenter.modalAlert.openMensagem = false;
		this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.msgOrientacaoExcluida, true);
	}

}