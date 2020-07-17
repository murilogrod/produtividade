import { TemplateModule } from './template/template.module';
import { ModalModule } from './template/modal/modal.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule  , ReactiveFormsModule} from '@angular/forms';

import { AppComponent } from './app.component';

import { AppRoutingModule } from './app.routing.module';
import { AuthGuard } from './guards/index';
import { AuthenticationService, ApplicationService, HttpInterceptorService, EventService , LoaderComponent , LoaderService} from './services/index';

import { DataTableModule, SharedModule, DropdownModule, SelectItem } from 'primeng/primeng';
import {BootstrapModalModule} from 'angularx-bootstrap-modal';
import { PaeContratacaoModule} from './pae/contratacao/pae-contratacao.module';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpModule,
    AppRoutingModule,
    HttpClientModule,
    SharedModule,
    BootstrapModalModule.forRoot({container:document.body}),
    ModalModule,
    TemplateModule,
    PaeContratacaoModule
    
    
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: HttpInterceptorService,
    multi: true
  },
    AuthenticationService,
    AuthGuard,
    ApplicationService,
    EventService,
    LoaderService
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
