import { Injectable } from "@angular/core";
import { UserSSOService } from "../service/user-sso-service";
import { UserSSOModel } from "../model/user-sso.model";
import { ModalUserSSOComponent } from "../view/modal-user-sso.component";
import { UserSSOOutput } from "../model/user-sso.output";
import { UserSSO } from "../model/user-sso";
import { LoaderService } from "src/app/services";
import { USER_SSO } from "src/app/constants/constants";
import * as moment from 'moment';

@Injectable()
export class UserSSOComponentPresenter {

    userSSOModel: UserSSOModel;

    constructor(private userSSOService: UserSSOService,
        private loadService: LoaderService) {
    }

    /**
     * Fecha a modal user-sso
     * @param modalUserSSOComponent 
     */
    close(modalUserSSOComponent: ModalUserSSOComponent) {
        var output = this.initOutput(true, true, false, null, null);
        modalUserSSOComponent.dialogReturn = output;
        modalUserSSOComponent.closeDialog();
    }

    /**
     * Salva um novo usuário no SSO
     */
    salvarDadosUsuarioSSO(modalUserSSOComponent: ModalUserSSOComponent) {
        const userSSO: UserSSO = this.inicializaUSerSSO();
        const changeEmail = this.validaEmailEnviado(userSSO);
        this.loadService.show();
        this.userSSOService.enviarDadosUsuarioSSO(userSSO).subscribe(
            response => {
                this.onSucessUserSSO(response, changeEmail, userSSO, modalUserSSOComponent);
            }, error => {
                this.onErrorUserSSO(error, userSSO, modalUserSSOComponent);
                throw error;
            });
    }

    /**
     * Verifica se o email foi alterado
     * @param userSSO 
     */
    validaEmailEnviado(userSSO: UserSSO): boolean {
        if (userSSO.e_mail !== this.userSSOModel.cliente.email) {
            return true;
        }
        return false;
    }

    /**
     * Tratamento para sucesso para o retorno do serviço de inclusão de usuário SSO
     * @param response 
     * @param changeEmail 
     * @param userSSO 
     * @param modalUserSSOComponent 
     */
    private onSucessUserSSO(response: any, changeEmail: boolean, userSSO: UserSSO, modalUserSSOComponent: ModalUserSSOComponent) {
        const msg: string = USER_SSO.USER_SSO_SUCESS;
        console.log(response.mensagem);
        var output = this.initOutput(false, false, changeEmail, userSSO, msg);
        modalUserSSOComponent.dialogReturn = output;
        modalUserSSOComponent.closeDialog();
        this.loadService.hide();
    }


    /**
     * Tratamento de erro para o retorno do serviço de inclusão de usuário SSO
     * @param error 
     * @param userSSO 
     * @param modalUserSSOComponent 
     */
    private onErrorUserSSO(error: any, userSSO: UserSSO, modalUserSSOComponent: ModalUserSSOComponent) {
        if (error.status == 401) {
            var output = this.initOutput(false, true, false, userSSO, error.error.mensagem);
            modalUserSSOComponent.dialogReturn = output;
            modalUserSSOComponent.closeDialog();
        }
        else {
            const msg: string = error.message + ' : ' + error.statusText;
            var output = this.initOutput(false, true, false, userSSO, msg);
            modalUserSSOComponent.dialogReturn = output;
            modalUserSSOComponent.closeDialog();
        }
        this.loadService.hide();
    }


    /**
     * Monta o objeto de saída modal user sso
     * @param close 
     * @param error 
     * @param changeEmail 
     * @param userSSO 
     * @param msg 
     */
    private initOutput(close: boolean, error: boolean, changeEmail: boolean, userSSO: UserSSO, msg: string) {
        if (!close) {
            return <UserSSOOutput>{
                close: false,
                error: error,
                changeEmail: changeEmail,
                email: userSSO.e_mail,
                msg: msg
            };
        } else {
            return <UserSSOOutput>{
                close: true,
            };
        }
    }

    /**
     * Monta o objeto para mandar na requisição para criar um novo usuário no SSO
     */
    private inicializaUSerSSO() {
        const userSSO: UserSSO = new UserSSO();
        userSSO.usuario = this.userSSOModel.cliente.cpf;
        userSSO.nome = this.userSSOModel.cliente.nome;
        userSSO.dtnasc = this.checkValueStringDate();
        userSSO.senha = this.userSSOModel.password;
        userSSO.e_mail = this.userSSOModel.email;
        return userSSO;
    }

    /**
     * O campo data de nascimento pode ser montado como string ou date; 
     * esse metodo devolve sempre no formato DD/MM/YYYY
     */
    private checkValueStringDate(): string {
        const value: any = this.userSSOModel.cliente.data_nascimento;
        if (typeof value === 'string' || value instanceof String) {
            return this.userSSOModel.cliente.data_nascimento.substring(0, 10);
        } else {
            return moment(this.userSSOModel.cliente.data_nascimento).format('DD/MM/YYYY');
        }
    }

    /**
     * Inicializa o email do usuário
     */
    inicializaEmail() {
        if (this.userSSOModel.cliente.email) {
            this.userSSOModel.email = this.userSSOModel.cliente.email;
        }
    }

    /**
     * Verifica se o cliente possui data de nascimento
     */
    emptyDataNascimento(): boolean {
        return this.userSSOModel.cliente.data_nascimento !== null && this.userSSOModel.cliente.data_nascimento !== undefined;
    }


}