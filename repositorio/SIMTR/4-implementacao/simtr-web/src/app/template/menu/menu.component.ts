import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService, ApplicationService, EventService } from '../../services';
import { environment } from 'src/environments/environment';
import { EVENTS } from 'src/app/constants/constants';


@Component({
  selector: 'cx-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  app: any;
  currentview: any = {}
  filtromenu: string;
  audioOn = false;

  @Input() changeRoles;

  constructor(private router: Router,
    private authenticationService: AuthenticationService,
    private appService: ApplicationService,
    private eventService : EventService) {
    this.app = this.appService.getApp();
  }

  ngOnInit() {
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigateByUrl('/login');
  }

  hasCredential(credential) {
    if (!environment.production) {
      return this.appService.hasCredential(credential, this.changeRoles ? this.changeRoles : this.authenticationService.getRolesInLocalStorage());
    } else {
      return this.appService.hasCredential(credential, this.changeRoles);
    }
  }

  showMenuSearch(): boolean {
    if (this.app && this.app.hidemenusearch) {
      return !this.app.hidemenusearch;
    }

    return true;
  }

  showAcessibilidade() : boolean {
    if (this.app && this.app.hideacessibilidade) {
      return !this.app.hideacessibilidade;
    }

    return true;
  }

  changeAudio() {
    this.audioOn = !this.audioOn;
  }

  emitFontUpEvent() {
    this.eventService.broadcast(EVENTS.eventFontUp);
  }

  emitFontDownEvent() {
    this.eventService.broadcast(EVENTS.eventFontDown);
  }

  emitContrastEvent() {
    this.eventService.broadcast(EVENTS.eventContrast);
  }

  decideClassAudio() {
    return this.audioOn ? 'btn acessibilidade audio' : 'btn acessibilidade';
  }

  goLink(id: string) {
    this.router.navigate([id]);
  }

  toggleSubMenu(id: string) {
    document.getElementById(id).classList.toggle('collapse');
  }

}
