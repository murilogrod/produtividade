import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateChild} from '@angular/router';
import {Keycloak, KeycloakAuthorization} from '@ebondu/angular2-keycloak';
import {AuthenticationService, ApplicationService, EventService} from '../services/index';
import {EVENTS} from '../constants/constants';


@Injectable()
export class AuthGuard implements CanActivate , CanActivateChild {
 

  constructor(private router: Router,
    private authenticationService: AuthenticationService,
    private appService: ApplicationService,
    private keycloak: Keycloak,
    private keycloakAuthz: KeycloakAuthorization,
    private http: Http,
    private eventService : EventService) {

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    
    if (this.authenticationService.usuarioAutenticado() && !this.appService.isTokenExpired()) {
    
      if (state.url === '/' || state.url.indexOf('login') !== -1) {
      
        this.eventService.broadcast(EVENTS.authorization , true)
        this.appService.setCurrentView('/paeContratacao');
        this.router.navigate(['/paeContratacao']);
        return true;
      }
      this.appService.setCurrentView(state.url.substring(1));
      
      return true;
    }
    
    this.appService.setCurrentView('/login');
    this.router.navigate(['/login'])
    
    return false;
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    this.appService.setCurrentView(state.url.substring(1));
    return true;
  }
}