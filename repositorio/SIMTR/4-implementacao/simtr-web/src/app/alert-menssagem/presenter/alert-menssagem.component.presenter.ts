import { Injectable } from "@angular/core";
import { ModalAlertBoolean } from "../model/modal-alert-boolean";
import { ModalAlertMensagem } from "../model/modal-alert-mensagens";

@Injectable()
export class AlertMenssagemComponentPresenter {
    modalAlert: ModalAlertBoolean;
    modalMsg: ModalAlertMensagem;
}