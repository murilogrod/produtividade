import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { HttpInterceptorService } from "../http-interceptor/http-interceptor.service";
import { ErrorHandler, NgModule } from "@angular/core";
import { GlobalErrorHandlerService } from "src/app/global-error/global-erro-handle.service";
import { CommonModule } from "@angular/common";
import { KeycloakAngularModule } from "keycloak-angular";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { ClickOutsideModule } from "ng-click-outside";
import { LoaderRelativoComponent } from "./view/loader-relativo.component";
import { TooltipModule } from 'primeng/primeng';

@NgModule({
  declarations: [LoaderRelativoComponent],
  imports: [
    CommonModule,
    KeycloakAngularModule,
    FormsModule,
    RouterModule,
    ClickOutsideModule,
    TooltipModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandlerService
    }
  ],
  exports: [
    LoaderRelativoComponent
  ],
  bootstrap: [LoaderRelativoComponent],
})
export class LoaderRelativoModule { }
