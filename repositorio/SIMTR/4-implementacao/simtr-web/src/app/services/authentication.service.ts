import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { environment } from '../../environments/environment';
import { CONFIG_SCANER, EVENTS, LOCAL_STORAGE_CONSTANTS, MESSAGE_ALERT_MENU, PERFIL_ACESSO, PRETTY_PROFILE } from '../constants/constants';
import { GLOBAL_ERROR } from '../global-error/utils/error-contants';
import { ConfigScaner } from '../model/configScaner';
import { Utils } from '../utils/Utils';
import { ApplicationService } from './application/application.service';
import { EventService } from './event-service/event-service';
import { Profile } from './model/profile.model';

@Injectable()
export class AuthenticationService {

  private TIMEOUT = 10;

  private tokenExpired = false;

  private updatingToken = false;

  constructor(
    private keycloakService: KeycloakService,
    private appService: ApplicationService,
    private router: Router,
    private eventService: EventService) {
  }

  setUpdatingToken(value) {
    this.updatingToken = value;
  }

  isUpdatingToken() {
    return this.updatingToken;
  }

  login() {
    this.keycloakService.login();
  }

  logout() {
    this.clearRolesInLocalStorage();
    const logoutURL = this.keycloakService.getKeycloakInstance().createLogoutUrl();
    const req = new XMLHttpRequest();
    req.open('POST', logoutURL, true);
    req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    req.withCredentials = true;
    let params = 'client_id=' + encodeURIComponent(this.keycloakService.getKeycloakInstance().clientId);
    params += '&refresh_token=' + encodeURIComponent(this.keycloakService.getKeycloakInstance().refreshToken);
    req.onreadystatechange = () => {
      if (req.readyState == 4) {
        this.inicializarLogin();
        this.router.navigate([logoutURL]);
      }
    };
    req.send(params);
  }

  /**
   * Limpar da sessão constantes utilizadas para troca de perfil MOCK.
   */
  private clearRolesInLocalStorage() {
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.logout, true);
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.rolesInMemory);
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.userUrl);
  }

  private inicializarLogin() {
    this.appService.removeInLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.rolesInMemory);
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.userUrl);
    this.keycloakService.getKeycloakInstance().loginRequired = true;
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.token);
    this.keycloakService.clearToken();
    let checkarTodosVisualizadorDocumento = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.marcaTodos);
    let configScanerSalvo = new ConfigScaner();
    configScanerSalvo = JSON.parse(this.appService.getItemFromLocalStorage(CONFIG_SCANER.SCANNER));
    this.appService.clearStorage();
    this.appService.saveInLocalStorage(CONFIG_SCANER.SCANNER, JSON.stringify(configScanerSalvo));
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.marcaTodos, checkarTodosVisualizadorDocumento);
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.logout, true);
    this.keycloakService.logout();
  }

  updateToken() {
    this.updatingToken = true;
    this.keycloakService.updateToken(-1).then(response => {
      this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.token, this.keycloakService.getKeycloakInstance().token);
      this.updatingToken = false;
      this.tokenExpired = false;
    }, error => {
      console.error('Error: ' + error);
      alert('Erro ao fazer update do token no SSO. Usuário será deslogado');
      this.updatingToken = false;
      this.tokenExpired = false;
      this.logout();
      throw error;
    });

    setTimeout(() => {
      this.updatingToken = false;
      this.tokenExpired = false;
    }, this.TIMEOUT * 1000);
  }

  isTokenExpired() {
    try {
      const tokenExpiredTemp = !this.updatingToken && this.keycloakService.isTokenExpired(this.TIMEOUT);
      if (tokenExpiredTemp && !this.tokenExpired) {
        this.updateToken();
        this.tokenExpired = true;
        return false;
      }
      return tokenExpiredTemp;
    } catch (error) {
      return true;
    }
  }

  usuarioAutenticado() {
    if (environment.ssoLogin) {
      return !this.isTokenExpired();
    }

    return true;
  }

  /**
   * Verifica se um usuário possui um  conjunto determinado de perfis.
   * @param roles 
   * @param numberRole passado na url quando usuario trocar perfil
   */
  hasRoles(routerRotas: any[], numberRole: string): boolean {
    const roles = this.getRoles(numberRole);
    const guard = this.appService.hasRoles(roles, routerRotas);
    if (!guard) {
      this.eventService.broadcast(EVENTS.alertMessage, MESSAGE_ALERT_MENU.MSG_ERRO_SEM_PERFIL);
    }
    return guard;
  }

  /**
   * Retorna as roles para uso; podendo estar mockadas de acordo configuracao do usuário
   * @param numberRole 
   */
  getRoles(numberRole: string): any[] {
    if (!environment.production) {
      return this.getRolesUserSelection(numberRole);
    } else {
      return this.keycloakService.getUserRoles(true);
    }
  }

  /**
   *  Retorna lista de perfis de acordo o perfil passado pelo usuario: função mock
   *  @param numberRole 
   */
  getRolesUserSelection(numberRole: string): any[] {
    let role: string = Utils.getLabelRoleSelected(numberRole);
    let roles: any[] = numberRole ? Utils.getRolesByPerfil(role) : this.getRolesInLocalStorage();
    this.saveRolesInLocalStorage(roles);
    return roles;
  }

  /**
   * Utilzado para salvar os perfis na local storage. Necessário para utilização de troca de perfil
   * @param roles 
   */
  saveRolesInLocalStorage(roles: any[]) {
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.rolesInMemory, roles);
  }

  /**
   * Recupera as roles armazenadas na local storage
   */
  getRolesInLocalStorage(): any[] {
    const rolesInLocalStorage = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.rolesInMemory);
    const roles = rolesInLocalStorage ? rolesInLocalStorage.split(",") : [PERFIL_ACESSO.UMA_AUTHORIZATION];
    const rolesLocalStorage: any[] = Array.from(roles);
    return rolesLocalStorage;
  }

  /**
   * Retorna os perfis do SSO
   */
  getRolesSSO(): any[] {
    let perfis: string[] = [];
    try {
      perfis = this.keycloakService.getUserRoles();
    } catch (e) {
      console.log(`Loading Roles`);
    }
    return perfis;
  }


  /**
   * Retorna os perfis com descrição amigável
   */
  getPrettyProfiles(): Map<number, Array<any>> {
    const mapProfile: Map<number, Array<any>> = new Map<number, Array<any>>();
    let customRoles: Array<Profile> = new Array<Profile>();
    const perfis: Array<string> = this.getRolesSSO();
    const prettyProfiles: any[] = Object.entries(PRETTY_PROFILE);
    prettyProfiles.forEach(pretty => {
      perfis.forEach(perfil => {
        if (perfil.trim().toUpperCase() === pretty[0]) {
          const profile: Profile = new Profile();
          profile.profile = pretty[1].profile;
          profile.nickname = pretty[1].nickname;
          profile.description = pretty[1].description;
          profile.presentation = pretty[1].presentation;
          customRoles.push(profile);
        }
      });
    });
    perfis.forEach(perfil => {
      if (!prettyProfiles.some(pp => pp[1].profile.trim().toUpperCase() === perfil.trim().toUpperCase())) {
        const profile: Profile = new Profile();
        profile.profile = perfil.toUpperCase().trim();
        profile.presentation = false;
        customRoles.push(profile);
      }
    });
    mapProfile.set(1, perfis);
    mapProfile.set(2, customRoles);
    return mapProfile;
  }
}