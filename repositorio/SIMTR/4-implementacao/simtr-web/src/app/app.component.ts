import { Component, OnInit, AfterViewInit, ChangeDetectorRef, OnChanges, AfterContentChecked } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService, ApplicationService, AlertMessageService, EventService } from './services';
import { Location } from '@angular/common';
import { EVENTS } from './constants/constants';
import { AnalyticsService } from './services/analytics/analytics.service';
import { environment } from '../environments/environment';

declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})

export class AppComponent extends AlertMessageService implements OnInit, AfterViewInit, AfterContentChecked {

  app: any = {};

  existeToken: boolean;

  changeRoles: any[];

  constructor(
    private cd: ChangeDetectorRef,
    private authService: AuthenticationService,
    private router: Router,
    private location: Location,
    private appService: ApplicationService,
    private eventService: EventService, 
    private analytics: AnalyticsService
  ) {
    super();
    this.app = appService.getApp();
    document.title = this.app.id + ' - ' + this.app.name;
    analytics.init(environment.analyticsId, "WEB");
  }

  ngOnInit(): void {
    this.existeToken = this.appService.getExisteToken();
    this.appService.setInfoPage(location.pathname == '/info');
    this.appService.setVisualizadorImgExternoPage(location.pathname == '/visualizarImagemExterna');
    if(this.isInfoPage() || this.isVisualizadorImgExternoPage()){
      this.existeToken = true;
    }
  }

  ngAfterContentChecked() {
    if(!this.existeToken) {
      this.existeToken = this.appService.getExisteToken();
    }
  }

  ngAfterViewInit() {
    this.cd.detectChanges();
    this.appService.setInfoPage(location.pathname == '/info');
    this.eventService.on(EVENTS.alertMessage, msg => {
      this.addMessageWarning(msg);
    });
    this.appService.setVisualizadorImgExternoPage(location.pathname == '/visualizarImagemExterna');
  }

  isInfoPage() {
    return this.appService.isInfoPage();
  }

  isVisualizadorImgExternoPage() {
    return this.appService.isVisualizadorImgExternoPage();
  }
  handlleChangeRole(changeRoles: any[]) {
    this.changeRoles = changeRoles;
  }

  goBack() {
    this.location.back();
  }

  ajustarLayout() {
    if ($.caixaFixAngular) {
      $.caixaFixAngular.fix();
    }
  }

  goLink(link: string) {
    this.router.navigate([link]);
  }

  userLogged(): boolean {
    return this.authService.usuarioAutenticado();
  }

  sideBarClass(): string {
    return 'sidebar-mini';
  }

}
