import { JwtHelper } from 'angular2-jwt';
import { environment } from './../../environments/environment';
import {Component, OnInit, AfterViewInit, AfterViewChecked} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http'

import {AuthenticationService, EventService, ApplicationService} from '../services/index';
import {Keycloak, KeycloakAuthorization} from '@ebondu/angular2-keycloak';
import {EVENTS, LOCAL_STORAGE_CONSTANTS} from '../constants/constants';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent implements OnInit {

  static alreadyLogged = false;

  model: any = {};
  user = {};
  loading = false;

  jwtHelper: JwtHelper = new JwtHelper();

  decodedAToken = '';
  decodedIdToken = ''
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private keycloak: Keycloak, private keycloakAuthz: KeycloakAuthorization, 
    private http: HttpClient,
    private eventService: EventService,
    private appService: ApplicationService) {
  }

  ngOnInit(): void {
   
    Keycloak.tokenExpiredObs.subscribe(expired => {
      if (expired) {
    
        this.authenticationService.updateToken();
      }
    });

    this.keycloak.config = environment.keycloakFile;

    this.keycloakAuthz.init();

    this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false
    });
    
    Keycloak.authSuccessObs.subscribe(auth => {
      if (auth) {
    
        this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.token);
        this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.idToken);
        this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.token, this.keycloak.accessToken);
        this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.idToken, this.keycloak.idToken);
        
        this.decodedAToken = this.jwtHelper.decodeToken(this.keycloak.accessToken);
        this.decodedIdToken = this.jwtHelper.decodeToken(this.keycloak.idToken);
        
        if (environment.production && this.decodedIdToken['no-usuario'] != null) {
          this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO, JSON.stringify(this.decodedAToken));
          this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.idToken, JSON.stringify(this.decodedIdToken));
        } else {
          this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO, JSON.stringify(this.decodedIdToken));
        }
        this.eventService.broadcast(EVENTS.authorization, auth);
        this.appService.setCurrentView('/principal');
        this.router.navigate(['/principal'])
      }
    });

  }


  login() {
    
  }

  logout() {
    this.authenticationService.logout();
  }

  // tslint:disable-next-line:member-ordering
  transform

  isSSO() {
    return environment.ssoLogin;
  }
}
