import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateChild } from '@angular/router';
import { AuthenticationService, ApplicationService, EventService } from '../services';
import { EVENTS, ROLES, PERFIL, LOCAL_STORAGE_CONSTANTS } from '../constants/constants';
declare var $: any;

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {


  constructor(private router: Router,
    private authenticationService: AuthenticationService,
    private appService: ApplicationService,
    private http: HttpClient,
    private eventService: EventService) {

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this.authenticationService.isTokenExpired()) {
      if (state.url === '/' || state.url.indexOf('login') !== -1) {
        this.eventService.broadcast(EVENTS.authorization, true)
        this.router.navigate(['principal']);
        return true;
      }
      this.appService.setCurrentView(state.url.substring(1));
      return this.isAuthGuard(route);
    }
    this.storageUserUrl(state);
    this.appService.setCurrentView('login');
    this.router.navigate(["login"]);
    return false;
  }


  /**
   * Utilizado para salvar a url digitada pelo usuario manualmemte; exceto quando é efetuado o logoff.
   * @param state 
   */
  storageUserUrl(state: RouterStateSnapshot) {
    const logoff = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.logout);
    if (!logoff) {
      this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.userUrl, state.url.substring(1));
    } else {
      this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.logout);
    }
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    $(".ui-tooltip").css('display', 'none');
    this.appService.setCurrentView(state.url.substring(1));
    return true;
  }

  /**
   * 
   * @param route Verifica a regra da Rota
   */
  isAuthGuard(route: ActivatedRouteSnapshot): boolean {
    const routerRotas = route.data[ROLES] !== undefined ? route.data[ROLES] : [];
    const numberRole: string = route.params[PERFIL];
    return routerRotas.length == 0 ? true : this.authenticationService.hasRoles(routerRotas, numberRole);
  }
}