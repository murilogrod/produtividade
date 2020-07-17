import { UserSSO } from './../../model/user-sso';
import { UserSIUSR } from './../../model/user-siusr';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationService, AuthenticationService, EventService } from './../../services/index';
import { LOCAL_STORAGE_CONSTANTS, EVENTS } from './../../constants/constants';
import { environment } from './../../../environments/environment';
import { ItemResponse } from './../../interface/ItemResponse';

declare var $: any;

const TELA_LOGIN = 'login';

@Component({
  selector: 'cx-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, AfterViewInit {
  app: any = {};
  userSIUSR: UserSIUSR;
  userSSO: UserSSO;
  nomeUnidade: string;
  filtrosvalor : any = {};
  notificacoes = [];
  currentiView;
  userImage: any;
  userImageDefault: any;
  hasImage = false;
  urlManualUsuario: string;
  currentView;
  userIdToken;
  perfis = [];

  constructor(private appService: ApplicationService, private authService: AuthenticationService, private http: HttpClient, private eventService: EventService) {}

  ngOnInit() {
    this.app = this.appService.getApp();
    this.currentView = this.appService.getCurrentView();
    this.filtrosvalor['filtrosvalor'] = '';
    
  }

  ngAfterViewInit(): void {
    this.eventService.on(EVENTS.authorization, auth => {
      this.init();
    });

    if (this.authService.usuarioAutenticado()) {
      this.init();
    }
  }

  private init() {
    //this.getUrlManualUsuario();
    if (environment.showSSOInfo) {
      this.userSSO = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO));
      this.userIdToken = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.idToken));
    } else {
      this.userSIUSR = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSiusr));
    }
    this.getUserImage();
    this.getPerfis();
  }

  private getUrlManualUsuario() {
    this.http.get<ItemResponse>(environment.serverPath + '/authSSO/manualUsuario').subscribe(response => {
      this.urlManualUsuario = response['manualUsuario'];
    });
  }

  isApresentaBarraPesquisa() {
    return !this.appService.getCurrentView()['hideSearchBar'];
  }

  isApresentaPerfilUsuario() {
    return this.appService.getCurrentView()['id'] !== TELA_LOGIN && !this.appService.getCurrentView()['hideUserProfile'] && this.hasUser();
  }

  private hasUser() {
    if (environment.showSSOInfo) {
      return this.userSSO !== null && this.userSSO !== undefined;
    }
    return this.userSIUSR !== null && this.userSIUSR !== undefined;
  }

  isApresentaNotificacoes() {
    return this.appService.getCurrentView()['id'] !== TELA_LOGIN && !this.appService.getCurrentView()['hideNotifications'];
  }

  isApresentaWidgetSidebar() {
    return this.appService.getCurrentView()['id'] !== TELA_LOGIN && !this.appService.getCurrentView()['hideWidgetSidebar'];
  }

  ativarToogle() {
    if ($.AdminLTE.controlSidebar) {
      $('[data-toggle="control-sidebar"]').unbind('click');
      $.AdminLTE.controlSidebar.activate();
    }
  }

  getDeMatriculaAndUserName() {
    if (environment.showSSOInfo) {
      //return this.userSSO.preferred_username + ' - ' + this.userSSO['given_name'];
      return this.userIdToken.preferred_username + ' - ' + this.userIdToken['no-usuario']
      
    }
    return this.userSIUSR.deMatricula + ' - ' + this.userSIUSR.noUsuario;
  }

  getCoUnidadeAndNoUnidade() {
    if (environment.showSSOInfo) {
      return ('0000' + this.userIdToken['co-unidade']).slice(-4) + '-' + this.userIdToken['no-unidade']
    }

    return ('0000' + this.userSIUSR.coUnidade).slice(-4) + ',' + this.userSIUSR.noUnidade;
  }

  getPerfil() {
    if (environment.showSSOInfo) {
      //TODO colocar algum grupo ou pegar do SIUSR
      if (this.userIdToken['no-funcao']) {
        return this.userIdToken['no-funcao'];
      } else {
        return this.userIdToken['no-cargo'];
      }
    }
    return this.userSIUSR.perfil;
  }

  clean() {
    this.filtrosvalor = {};
    this.eventService.broadcast(EVENTS.cleanHeaderSearch);
  }

  search() {
    this.eventService.broadcast(EVENTS.headerSearch, this.filtrosvalor);
  }

  getBufferUserImage() {
    return 'data:image/jpg;base64,' + this.userImage;
  }

  private getUserImage() {
    const urlImagem = this.app.userImage;

    this.userImageDefault = this.appService.getProperty('userImagePadrao');
    //imagemUsuarioPadrao = 'https://permissoes.correio.corp.caixa.gov.br/ThumbPhoto/'+ matriculaUsuario + '_AD.jpg';
    this.hasImage = false;
    /* if (this.user) {
      const matriculaUsuario = this.user.deMatricula;

      const localStorageImagemUsuario = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userImage + matriculaUsuario);

      if (localStorageImagemUsuario) {
        this.userImage = localStorageImagemUsuario;
      } else {
        urlImagem = urlImagem + matriculaUsuario + '.jpg';
        // urlImagem = 'http://www.gihabvt.es.caixa/atendimento/_inc/foto.asp?req='+ matriculaUsuario;         
        this.http.get(urlImagem, {responseType: 'arraybuffer'}).subscribe(response => {
          this.userImage = this.converterArrayBufferToBase64(response);
          this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.userImage + matriculaUsuario, this.userImage);
          this.hasImage = true;
        }, erro => {
          this.userImageDefault = this.appService.getProperty('userImagePadrao');
          //imagemUsuarioPadrao = 'https://permissoes.correio.corp.caixa.gov.br/ThumbPhoto/'+ matriculaUsuario + '_AD.jpg';
          this.hasImage = false;
          console.log('Erro ao obter imagem do usu�rio ' + matriculaUsuario + ' ' + erro);
        });
      }
    }*/
  }

  private converterArrayBufferToBase64(buffer) {
    let binary = '';
    const bytes = new Uint8Array(buffer);
    const len = bytes.byteLength;

    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }

    return window.btoa(binary);
  }

  logout() {
    this.authService.logout();
  }

  isAutenticado() {
    return this.authService.usuarioAutenticado();
  }

  getPerfis() {
    this.perfis =  this.appService.getPerfis()
    if (this.perfis.length === 0) {
      this.perfis.push('Nenhum')
    }
  }
}
