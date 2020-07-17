import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs-compat';
import "rxjs/add/operator/do";
import "rxjs/add/operator/catch";
import "rxjs/add/operator/finally";
import { ApplicationService } from '../application/application.service';
import { AuthenticationService } from '../authentication.service';
import { LOCAL_STORAGE_CONSTANTS, INTERCEPTOR_SKIP_HEADER } from '../../constants/constants';
import { LoaderService } from './loader/loader.service';
import { GlobalErrorComponentPresenter } from 'src/app/global-error/presenter/global-error.component.presenter';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

  constructor(
    private appService: ApplicationService,
    private router: Router,
    private authService: AuthenticationService,
    private globalErrorPresenter: GlobalErrorComponentPresenter,
    private loaderService: LoaderService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.headers.has(INTERCEPTOR_SKIP_HEADER)) {
      return this.processSkipHeader(req, next);
    } else {
      req = this.processInternalService(req);
      return this.processRequest(next, req);

    }
  }

  private processRequest(next: HttpHandler, req: HttpRequest<any>): Observable<HttpEvent<any>> {
    return next.handle(req).do((ev: HttpEvent<any>) => {
      if (ev instanceof HttpResponse) {
        console.log('processing response', ev);
      }
    }).catch(error => {
      if (error instanceof HttpErrorResponse) {
        if (error.status === 0 && (this.verificarSeEUndefinedOu401(error))) {
          this.globalErrorPresenter.montarListaDeErros(error, this.appService);
          const token = this.appService.getToken();
          if (token == null || token === 'undefined') {
            this.router.navigate(['login']);
          }
          else if (!this.authService.isUpdatingToken()) {
            this.authService.updateToken();
            if (this.authService.isTokenExpired()) {
              this.appService.removeToken();
              this.router.navigate(['login']);
            }
          }
        }
      }
      return Observable.throw(error);
    }).finally(() => {
      this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.textPlain);
    });
  }

  private verificarSeEUndefinedOu401(error: HttpErrorResponse) {
    return error.statusText === 'Unknown Error' || error.status == 401;
  }

  private processInternalService(req: HttpRequest<any>) {
    if (req.url.indexOf('/rest') !== -1) {
      if (!this.appService.isInfoPage()) {
        req = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.token)}`)
        });
      }
      if (!this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.textPlain)) {
        req = req.clone({
          headers: req.headers.set('Content-Type', 'application/json')
        });
      }
      if (req.responseType === 'json') {
        req = req.clone({
          headers: req.headers.set('Access-Control-Allow-Origin', '*'),
          responseType: 'json'
        });
      }
      else {
        req = req.clone({
          headers: req.headers.set('Access-Control-Allow-Origin', '*'),
        });
      }
    }
    return req;
  }

  private processSkipHeader(req: HttpRequest<any>, next: HttpHandler) {
    const headers = req.headers.delete(INTERCEPTOR_SKIP_HEADER);
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.textPlain, INTERCEPTOR_SKIP_HEADER);
    return next.handle(req.clone({ headers })).do((ev: HttpEvent<any>) => {
      if (ev instanceof HttpResponse) {
        console.log('processing response', ev);
      }
    }).catch(response => {
      if (response instanceof HttpErrorResponse) {
        console.log('Processing http error', response);
      }
      return Observable.throw(response);
    }).finally(() => {
    });
    ;
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
