import { AlertMessageService } from './services/message/alert-message.service';
import {Component, OnInit, AfterViewInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService, ApplicationService} from './services/index';

import { Message } from '@angular/compiler/src/i18n/i18n_ast';
declare var $ : any;


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})

export class AppComponent extends AlertMessageService implements OnInit {

  app: any = {};

  constructor(private authService: AuthenticationService,
    private router: Router,
    private appService: ApplicationService) {
      super();
      this.app = appService.getApp();
      document.title = this.app.id + ' - ' + this.app.name;
  }
  
  ngOnInit() : void {
   
  }

  ajustarLayout() {
    if ($.caixaFixAngular4) {
      $.caixaFixAngular4.fix();
    }
  }

  goLink(link: string) {
    this.router.navigate([link]);
  }

  userLogged(): boolean {
    return this.authService.usuarioAutenticado();
  }

  sideBarClass(): string {
    if (this.authService.usuarioAutenticado()) {
      return 'sidebar-mini';
    }
    return 'sidebar-collapse';
  }
}
