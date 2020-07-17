import { Injectable } from '@angular/core';
import * as data from './../../../assets/project.json';
import { LOCAL_STORAGE_CONSTANTS } from '../../constants/constants';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable()
export class ApplicationService {

  private app: any = {};

  private appName;

  private currentiView: any = {};

  private isInfo = false;

  private isVisualizadorExterno = false;

  private jwtHelper : JwtHelperService = new JwtHelperService();

  constructor() {
    this.app = data['default'];
    this.appName = this.app['id'];

  }

  /**
   * Utilizado para verificar a(s) credenciai(s) da aplicação.
   * @param credentials 
   * @param ssoRoles 
   */
  hasCredential(credentials: string, ssoRoles: any[]): boolean {
    const roles: any[] = credentials.split(",");
    return this.hasRoles(ssoRoles, roles);
  }

  /**
   * Verifica se um usuário possui um  conjunto determinado de perfis.
   * @param ssoRoles 
   * @param roles 
   */
  hasRoles(ssoRoles: any[], roles: string[]): boolean {
    let flag = false;
    if (ssoRoles) {
      ssoRoles.forEach(ssoObj => {
        roles.forEach(obj => {
          if (ssoObj === obj) {
            flag = true;
          }
        });
      });
    }
    return flag;
  }

  saveInLocalStorage(id: string, value: any) {
    localStorage.setItem(this.appName + '-' + id, value);
  }

  removeInLocalStorage(id: string) {
    localStorage.removeItem(this.appName + '-' + id);
  }

  getItemFromLocalStorage(id: string) {
    return localStorage.getItem(this.appName + '-' + id);
  }

  clearStorage(): void {
    localStorage.clear();
  }

  setCurrentView(value: string) {
    for (const view of this.app['views']) {
      if (view.id === value) {
        this.currentiView = view;
        break;
      }
    }
  }

  getExisteToken() {
    return this.getToken() != null && this.getToken() != "undefined";
  }

  getToken(): string {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.token);
  }

  getUserSSO() {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.userSSO);
  }

  getUserSIUSR() {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.userSiusr);
  }

  removeToken() {
    localStorage.removeItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.token);
  }

  getCurrentView() {
    return this.currentiView;
  }

  getWidgetsProperties(id: string) {
    return this.app['widgets'][id];
  }

  getProperty(id: string) {
    return this.app[id];
  }

  getApp() {
    return this.app;
  }

  setInfoPage(value) {
    this.isInfo = value;
  }

  isInfoPage() {
    return this.isInfo;
  }

  setVisualizadorImgExternoPage(value) {
    this.isVisualizadorExterno = value;
  }

  isVisualizadorImgExternoPage() {
    return this.isVisualizadorExterno;
  }

  verificarPerfil(... perfis) {
    const token: any = this.jwtHelper.decodeToken(this.getToken());

    if( token.realm_access.roles) {
      const x = perfis.toString();
      
      const y = x.split(',')
      
      for (let i = 0; i < y.length; i++) {
        if (token.realm_access.roles.includes(y[i])) {
          return true;
        }
      }
      return false;
    }
  }

}
