import { AlertMessageComponent } from './alert-message/alert-message.component';
import { KeycloakModule, Keycloak, KeycloakAuthorization, KeycloakHttp } from '@ebondu/angular2-keycloak';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from '../login/index';
import { BackComponent } from '../back/index';
import { TemplateComponent, MenuComponent, HeaderComponent, FooterComponent, MenuFilterPipe, CaixaHeaderWidgetComponent } from './index';
import { LoaderComponent } from '../services/index';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClickOutsideModule } from 'ng-click-outside';

@NgModule({
  imports: [
    CommonModule,
    KeycloakModule.forRoot(),
    FormsModule,
    RouterModule,
    ClickOutsideModule
  ],
  declarations: [
    LoginComponent,
    BackComponent,
    TemplateComponent,
    MenuComponent,
    HeaderComponent,
    FooterComponent,
    MenuFilterPipe,
    CaixaHeaderWidgetComponent,
    LoaderComponent,
    AlertMessageComponent
  ],
  providers : [
    Keycloak,
    KeycloakAuthorization,
    KeycloakHttp,
  ]
  ,
  exports : [
    LoginComponent,
    BackComponent,
    TemplateComponent,
    MenuComponent,
    HeaderComponent,
    FooterComponent,
    MenuFilterPipe,
    CaixaHeaderWidgetComponent,
    LoaderComponent,
    AlertMessageComponent
  ]
})
export class TemplateModule { }
