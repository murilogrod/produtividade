import { AlertMessageComponent } from './alert-message/alert-message.component';
import { NgModule } from '@angular/core';
import { ClickOutsideModule } from 'ng-click-outside';
import { CommonModule } from '@angular/common';
import { LoginComponent } from '../login';
import { BackComponent } from '../back';
import { TemplateComponent, MenuComponent, HeaderComponent, FooterComponent, MenuFilterPipe, CaixaHeaderWidgetComponent } from '.';
import { LoaderComponent } from '../services';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { RadioButtonModule, TooltipModule, InputMaskModule, ButtonModule, ScrollPanelModule } from 'primeng/primeng';
import { PerfilUsuarioComponent } from './perfil-usuario/perfil-usuario.component';
import { HeaderComponentPresenter } from './header/presenter/header.component.presenter';

@NgModule({
  imports: [
    CommonModule,
    KeycloakAngularModule,
    FormsModule,
    RouterModule,
    ClickOutsideModule,
    RadioButtonModule,
    InputMaskModule,
    ButtonModule,
    TooltipModule,
    ScrollPanelModule
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
    AlertMessageComponent,
    PerfilUsuarioComponent
  ],
  providers: [
    KeycloakService,
    HeaderComponentPresenter
  ],
  exports: [
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


export class TemplateModule {
}
