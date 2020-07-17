import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Keycloak } from '@ebondu/angular2-keycloak';
import { ApplicationService } from '../../services/application/application.service';
import { AuthenticationService } from '../../services/authentication.service';
import { LOCAL_STORAGE_CONSTANTS } from '../../constants/constants';
import { JwtHelper } from 'angular2-jwt';

import { LoaderService } from './loader/loader.service';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

  private jwtHelper: JwtHelper;

  constructor(private keycloak: Keycloak,
    private appService: ApplicationService,
    private router: Router,
    private authService: AuthenticationService,
    private loaderService: LoaderService) {
    this.jwtHelper = new JwtHelper();
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (req.url.indexOf('/rest') !== -1) {
      req = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.token)}`)
      });
      req = req.clone({
        headers: req.headers.set('Content-Type', 'application/json')
      });
      if (req.responseType === 'json') {
        req = req.clone({
          headers: req.headers.set('Access-Control-Allow-Origin', '*'),
          withCredentials: true,
          responseType: 'json'
        });
      } else {
        req = req.clone({
          headers: req.headers.set('Access-Control-Allow-Origin', '*'),
          withCredentials: true
        });

      }
    }
    
    return next.handle(req).do((ev: HttpEvent<any>) => {
      if (ev instanceof HttpResponse) {
        console.log('processing response', ev);
      }
    }).catch(response => {
      if (response instanceof HttpErrorResponse) {
        console.log('Processing http error', response);
        if (response.status === 0 && response.statusText === 'Unknown Error') {
          const token = this.appService.getToken();
          if (token == null || token === 'undefined') {
            this.router.navigate(['login']);
          } else if (this.appService.isTokenExpired()) {
              this.appService.removeToken();
              try {
                this.authService.updateToken();
              } catch (error) {
                this.router.navigate(['login']);
              }
          }
        }
      }

      return Observable.throw(response);
    }).finally(() => {
    });
  }

  private onEnd(): void {
    this.hideLoader();
  }

  private showLoader(): void {
    this.loaderService.show();
  }

  private hideLoader(): void {
    this.loaderService.hide();
  }

}
