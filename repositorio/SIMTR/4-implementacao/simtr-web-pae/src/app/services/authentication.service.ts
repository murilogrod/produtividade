import {OnInit, Injectable} from '@angular/core';
import {Http, Headers, Response} from '@angular/http';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {Keycloak, KeycloakAuthorization} from '@ebondu/angular2-keycloak';
import {ApplicationService} from './application/application.service'; 
import {LOCAL_STORAGE_CONSTANTS} from '../constants/constants';
import {JwtHelper} from 'angular2-jwt';

@Injectable()
export class AuthenticationService {

  private jwtHelper : JwtHelper;

  constructor(private keycloak: Keycloak,
    private keycloakAuthz: KeycloakAuthorization,
    private http: Http,
    private router: Router,
    private appService: ApplicationService) {
      this.jwtHelper = new JwtHelper();
  }

  login() {
    this.keycloak.login(this.keycloak.config);
  }

  logout() {
    this.appService.clearStorage();
    this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.token)
    let  url = this.keycloak.createLoginUrl({})
    url = url.replace('/login','/paeContratacao')
    this.keycloak.clearToken(this.keycloak.config);
    this.keycloak.logout({});
    this.router.navigate([url])
  }

  updateToken () {
    this.keycloak.updateToken(5).subscribe(response => {
      this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.token);
      this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.token, response)
    }, error => {
      this.logout();
    })
  }

  usuarioAutenticado() {
    if (environment.ssoLogin) {
      const token = this.appService.getToken();
      return token !== null && token !== undefined && token !== 'undefined';
    }

    return true;
  }
}