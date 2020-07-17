import {Injectable} from '@angular/core';
import * as data from './../../../assets/project.json';
import {LOCAL_STORAGE_CONSTANTS, ARQUITETURA_SERVICOS} from './../../constants/constants';
import {JwtHelper} from 'angular2-jwt';

@Injectable()
export class ApplicationService {

  private app: any = {};

  private appName;

  private currentiView: any = {};

  private jwtHelper : JwtHelper;

 
  constructor() {
    this.app = data;
    this.appName = this.app['id'];
    this.jwtHelper = new JwtHelper();
    
  }

  hasCredential(funcionalidade : string , acao : string) : boolean {
    const user = JSON.parse(this.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userSiusr));
    if (user != null) {
      for (const item of user.credenciais) {
        if (funcionalidade === item.noFuncionalidade) {
          if (acao) {
            if (acao === item.noAcao) {
              return true;
            } else {
              continue;
            }
          } else {
            return true;
          }
        } 
      }
    }

    return false;
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

  isTokenExpired() {
    const token = this.getToken();
    return token !== null && token !== undefined && token !== 'undefined' && this.jwtHelper.isTokenExpired(token);
  }

  getToken () : string {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.token);
  }

  getIdToken () : string {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.idToken);
  }
  getUserSSO () {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.userSSO);
  }

  getUserSIUSR() {
    return localStorage.getItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.userSiusr);
  }

  removeToken() {
    localStorage.removeItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.token);
    localStorage.removeItem(this.appName + '-' + LOCAL_STORAGE_CONSTANTS.idToken);
    localStorage.clear()

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

  registraPerfil(token) {
    const permissoes = []
    
    if( token.realm_access.roles) {
      for (let i = 0; i < token.realm_access.roles.length; i++) {
          permissoes.push(token.realm_access.roles[i]);
      }
    }
    this.saveInLocalStorage('permissoes', permissoes)
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

  getPerfis() {
    const token: any = this.jwtHelper.decodeToken(this.getToken());
    const perms = [];
    if( token.realm_access.roles) {
      for (let i = 0; i < token.realm_access.roles.length; i++) {
        if (token.realm_access.roles[i].substring(0,3) === 'MTR') {
          perms.push(token.realm_access.roles[i])
        }
      }
      return perms;
    }
  }

}
