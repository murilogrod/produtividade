import { Injectable } from "@angular/core";
import { Header } from "../model/header.model";
import { Utils } from "src/app/utils/Utils";
import { HeaderComponent } from "../view/header.component";
import { Router } from "@angular/router";
import { ApplicationService, AuthenticationService, EventService, LoaderService } from "src/app/services";
import { LOCAL_STORAGE_CONSTANTS, EVENTS, MESSAGE_ALERT_MENU, URL_CACH, PERFIL_ACESSO } from "src/app/constants/constants";
import { GLOBAL_ERROR } from "src/app/global-error/utils/error-contants";
import { GlobalError } from "src/app/global-error/model/global-error";
import { environment } from "src/environments/environment";
import { DialogService } from "angularx-bootstrap-modal";
import { DossieService } from "src/app/dossie/dossie-service";
import { ExternalResourceService } from "src/app/services/external-resource/external-resource.service";

declare var $: any;

const DATA_IMAGE: string = "data:image/jpg;base64,";
const DEFAULT_USER_IMAGE: string = "userImagePadrao";
const FUNCAO: string = "no-funcao";
const NO_UNIDADE: string = "no-unidade";
const CO_UNIDADE: string = "co-unidade";
const FOUR_ZERO: string = "0000";
const TELA_LOGIN: string = 'login';
const ID: string = 'id';
const HIDE_NOTIFICATIONS: string = 'hideNotifications'
const HIDE_WIDGET_SIDEBAR: string = 'hideWidgetSidebar';
const HIDE_USER_PROFILE: string = 'hideUserProfile';
const HIDE_SEARCH_BAR: string = 'hideSearchBar';
const PERFIL: string = 'perfil';
const REPLACE_PROFILE: string = '[0]';
const FILTRO_VALOR: string = 'filtrosvalor';

@Injectable()
export class HeaderComponentPresenter {

    header: Header;

    constructor(
        private router: Router,
        private appService: ApplicationService,
        private authService: AuthenticationService,
        private eventService: EventService,
        private loadService: LoaderService,
        private dialogService: DialogService,
        private dossieService: DossieService,
        private externalService: ExternalResourceService) { }

    init() {
        this.header.app = this.appService.getApp();
        this.header.currentView = this.appService.getCurrentView();
        this.header.filtrosvalor[FILTRO_VALOR] = '';
        this.header.isMockRole = !environment.production;
        // this.header.isMockRole = false;
        // this.header.presentationProfiles = [{description : 'teste' , nickname : 'teste' , presentation: true, profile : 'teste'}];
        // this.header.withoutPresentationProfiles = [{description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPEDE'},
        // {description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPEDE'},
        // {description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPEDE'},
        // {description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPEDESADF'},
        // {description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPEDEGFDf'},
        // {description : 'teste' , nickname : 'teste' , presentation: true, profile : 'MTROPESDF'}];

        let lista = this.appService.getItemFromLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
        if (lista && lista.length > 0) {
            this.header.listaErroGlobal = JSON.parse(lista);
        }
    }

    afterViewInit(headerComponent: HeaderComponent): void {
        this.eventService.on(EVENTS.authorization, auth => {
            this.afterInit(headerComponent);
        });

        if (this.authService.usuarioAutenticado()) {
            this.afterInit(headerComponent);
        }
    }

    /**
     * Verifica as permissões do usuário para visualização de manual
     * @param roles 
     */
    checkRolesByUserManual(roles: string): boolean {
        if (!environment.production) {
            return this.appService.hasCredential(roles, this.authService.getRolesInLocalStorage());
        } else {
            return this.appService.hasCredential(roles, this.authService.getRolesSSO());
        }
    }

    afterViewChecked() {
        let listar = this.retornarQtdErro();
        if ((listar && (this.header.listaErroGlobal.length < listar.length))) {
            this.header.listaErroGlobal = listar;
        }
    }

    atualizarPatriarca() {
        this.dossieService.buscarPatriarcar(URL_CACH.DASHBOARD);
    }

    retornarQtdErro() {
        let lista = this.appService.getItemFromLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
        return JSON.parse(lista);
    }

    clickSideBar(headerComponent: HeaderComponent) {
        this.header.toogleSidebar = !this.header.toogleSidebar;
        headerComponent.toogleSideBarEvent.emit(this.header.toogleSidebar);
    }

    /**Usado para trocar o perfil da aplicação; utilização com environment.mockRoles = true */
    selectProfile(headerComponent: HeaderComponent) {
        let label = Utils.getLabelRoleSelected(this.header.selectedRole);
        let msg = MESSAGE_ALERT_MENU.MSG_ALERTA_TROCA_PERFIL.replace(REPLACE_PROFILE, label);
        if (this.header.changeSelectedRole !== this.header.selectedRole) {
            Utils.showMessageConfirm(this.dialogService, msg).subscribe(res => {
                if (res) {
                    this.header.changeSelectedRole = this.header.selectedRole;
                    this.router.navigate([`${PERFIL}/${this.header.selectedRole}`]);
                    this.header.role = Utils.getLabelRoleSelected(this.header.selectedRole);
                    let roles: any[] = Utils.getRolesByPerfil(this.header.role);
                    headerComponent.onChangeRoleChanged.emit(roles);
                } else {
                    this.header.selectedRole = this.header.changeSelectedRole;
                }
            },
                () => {
                    this.loadService.hide();
                });
        }
    }

    isApresentaBarraPesquisa() {
        return !this.appService.getCurrentView()[HIDE_SEARCH_BAR];
    }

    isApresentaPerfilUsuario() {
        return this.appService.getCurrentView()[ID] !== TELA_LOGIN && !this.appService.getCurrentView()[HIDE_USER_PROFILE] && this.hasUser();
    }

    isApresentaNotificacoes() {
        return this.appService.getCurrentView()[ID] !== TELA_LOGIN && !this.appService.getCurrentView()[HIDE_NOTIFICATIONS];
    }

    isApresentaWidgetSidebar() {
        return this.appService.getCurrentView()[ID] !== TELA_LOGIN && !this.appService.getCurrentView()[HIDE_WIDGET_SIDEBAR];
    }

    activeToogle() {
        if ($.AdminLTE.controlSidebar && $("html, body").hasClass("sidebar-collapse")) {
            $('[data-toggle="control-sidebar"]').unbind('click');
            $.AdminLTE.controlSidebar.activate();
        }
    }

    getName() {
        if (environment.showSSOInfo) {
            return this.header.userSSO.given_name;
        }
        return this.header.userSIUSR.noUsuario;
    }


    getMatricula() {
        if (environment.showSSOInfo) {
            return this.header.userSSO.preferred_username;
        }
        return this.header.userSIUSR.deMatricula;
    }

    getUnidade() {
        if (environment.showSSOInfo) {
            return (FOUR_ZERO + this.header.userSSO[CO_UNIDADE]).slice(-4);
        }

        return (FOUR_ZERO + this.header.userSIUSR.coUnidade).slice(-4);
    }

    getDeMatriculaAndUserName() {
        if (environment.showSSOInfo) {
            return this.header.userSSO.preferred_username + ' - ' + this.header.userSSO.given_name;
        }
        return this.header.userSIUSR.deMatricula + ' - ' + this.header.userSIUSR.noUsuario;
    }

    getCoUnidadeAndNoUnidade() {
        if (environment.showSSOInfo) {
            return (FOUR_ZERO + this.header.userSSO[CO_UNIDADE]).slice(-4) + ',' + this.header.userSSO[NO_UNIDADE];
        }
        return (FOUR_ZERO + this.header.userSIUSR.coUnidade).slice(-4) + ',' + this.header.userSIUSR.noUnidade;
    }

    getProfile() {
        if (environment.showSSOInfo) {
            const funcao = this.header.userSSO[FUNCAO];
            if (funcao) {
                return funcao;
            }
            if (this.header.role) { 
                return this.header.role;   
            }
            return 'Não Informado';
        }
        return this.header.userSIUSR.perfil;
    }

    clean() {
        this.header.filtrosvalor = {};
        this.eventService.broadcast(EVENTS.cleanHeaderSearch);
    }

    search() {
        this.eventService.broadcast(EVENTS.headerSearch, this.header.filtrosvalor);
    }

    getBufferUserImage() {
        return DATA_IMAGE + this.header.userImage;
    }

    openError(error: GlobalError) {
        this.eventService.broadcast(GLOBAL_ERROR.CAMINHO, error);
    }

    clearErrors() {
        this.header.listaErroGlobal = [];
        this.appService.removeInLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
    }

    logout() {
        this.authService.logout();
    }

    checkAuthentication() {
        return this.authService.usuarioAutenticado();
    }

    carryPatriarch() {
        this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.token);
    }

    private getUserImage() {
        this.externalService.callPhoto(this.header.app.userImage, this.header.userSSO.preferred_username).subscribe( (data : any) => {
            if (data) {
                this.header.userImage = data.image;
                this.header.hasImage = true;
            }
        }, error => {
            console.error('Erro ao consumir serviço de foto');
            console.error(error);
        });
        this.header.userImageDefault = this.appService.getProperty(DEFAULT_USER_IMAGE);
        this.header.hasImage = false;
    }

    private hasUser() {
        if (environment.showSSOInfo) {
            return this.header.userSSO !== null && this.header.userSSO !== undefined;
        }
        return this.header.userSIUSR !== null && this.header.userSIUSR !== undefined;
    }

    private afterInit(headerComponent: HeaderComponent) {
        if (environment.showSSOInfo) {
            this.header.userSSO = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO));
        } else {
            this.header.userSIUSR = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSiusr));
        }
        this.getUserImage();
        if (environment.production) {
            const mapProfile: Map<number, Array<any>> = this.authService.getPrettyProfiles();
            this.header.presentationProfiles = mapProfile.get(2).filter(pp => pp.presentation);
            this.header.withoutPresentationProfiles = mapProfile.get(2).filter(pp => !pp.presentation);
            headerComponent.onChangeRoleChanged.emit(mapProfile.get(1));
        }
    }

}