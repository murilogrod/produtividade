import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';
import { Component, OnInit } from '@angular/core';

import { AuthenticationService, EventService, ApplicationService, LoaderService } from '../services';
import { EVENTS, LOCAL_STORAGE_CONSTANTS, UNDESCOR, MIN_FIREFOX_VERSION, BROWSERS_NAME, PATH_NOT_SUPPORTED_PAGE } from '../constants/constants';
import { KeycloakService, KeycloakEventType, KeycloakEvent } from 'keycloak-angular';
import { DossieService } from '../dossie/dossie-service';
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Router } from '../../../node_modules/@angular/router';
import { GLOBAL_ERROR } from '../global-error/utils/error-contants';
import { DeviceDetectorService } from 'ngx-device-detector';

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
	url: string;

	jwtHelper: JwtHelperService = new JwtHelperService();

	constructor(
		private authenticationService: AuthenticationService,
		private dossieService: DossieService,
		private keycloakService: KeycloakService,
		private eventService: EventService,
		private appService: ApplicationService,
		private httpClient: HttpClient,
		private loadService: LoaderService,
		private router: Router,
		private deviceService: DeviceDetectorService) {
	}

	ngOnInit(): void {

		const browser = this.deviceService.browser.toLocaleLowerCase();
		if(browser != BROWSERS_NAME.CHROME) {
			if (browser == BROWSERS_NAME.FIREFOX) {
				const version = parseInt(this.deviceService.browser_version);
				if (version < MIN_FIREFOX_VERSION) {
					window.location.href = PATH_NOT_SUPPORTED_PAGE;
					return;
				}
			} else {
				window.location.href = PATH_NOT_SUPPORTED_PAGE;
				return;
			}
		}
		
		if (this.appService.isInfoPage()) {
			this.router.navigate(['info']);
		} else if (this.appService.isVisualizadorImgExternoPage()) {
			this.router.navigate(['visualizarImagemExterna']);
		} else {
			this.appService.removeInLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
			this.atualizarToken();
			this.inicializarInstanciaMacroProcesso();
		}
	}

	inicializarInstanciaMacroProcesso() {
		this.httpClient.get(environment.keycloakFile).subscribe(data => {
			this.keycloakService.init({
				config: {
					url: data['auth-server-url'],
					realm: data['realm'],
					clientId: data['resource']
				}, loadUserProfileAtStartUp: false,
				initOptions: {
					onLoad: 'login-required',
					checkLoginIframe: true
				}
			}).then(auth => {
				if (auth) {
					this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.token, this.keycloakService.getKeycloakInstance().token);
					const decodedToken = this.jwtHelper.decodeToken(this.keycloakService.getKeycloakInstance().idToken);
					this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.userSSO, JSON.stringify(decodedToken));
					this.eventService.broadcast(EVENTS.authorization, true);
					const userUrl = this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.userUrl);
					this.dossieService.buscarPatriarcar(userUrl);
					this.dossieService.buscarTipologia();
				}
			});
		}, (err: HttpErrorResponse) => {
			console.log(err.message + "Erro ao obter conteúdo do JSON Keycloak.");
		},
			() => {
				this.loadService.hide();
			});
	}

	private atualizarToken() {
		this.keycloakService.keycloakEvents$.subscribe((auth: KeycloakEvent) => {
			if (auth.type == KeycloakEventType.OnTokenExpired) {
				this.authenticationService.updateToken();
			}
		},
			() => {
				this.loadService.hide();
			});
	}


	login() {
	}

	logout() {
		this.authenticationService.logout();
	}

	isSSO() {
		return environment.ssoLogin;
	}
}
