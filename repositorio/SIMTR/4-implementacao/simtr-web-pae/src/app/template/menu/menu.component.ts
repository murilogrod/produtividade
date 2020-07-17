import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {AuthenticationService, ApplicationService} from '../../services/index';


@Component({
  selector: 'cx-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  app: any;
  currentview: any = {}
  filtromenu: string;

  constructor(private router: Router,
    private authenticationService: AuthenticationService,
    private appService: ApplicationService) {
    this.app = this.appService.getApp();
  }

  ngOnInit() {
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigateByUrl('/login');
  }

  hasCredential(credential) {
    return this.appService.hasCredential(credential , null);
  }

  showMenuSearch(): boolean {
    if (this.app && this.app.hidemenusearch) {
      return this.app.hidemenusearch;
    }

    return true;
  }

  goLink(id: string) {
    this.router.navigate([id]);
    if (this.router.url.indexOf(id) !== -1) {
      location.reload();
    } 
  }

  toggleSubMenu(id: string) {
    document.getElementById(id).classList.toggle('collapse');
  }

}
