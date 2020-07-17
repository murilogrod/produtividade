import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/index';
import { LoginComponent } from './login/index';
import { TemplateComponent } from './template/index';


import {PaeContratacaoComponent} from './pae/contratacao/pae-contratacao.component';

const routes: Routes = [
  {
    path: '', component: TemplateComponent, canActivate: [AuthGuard], canActivateChild: [AuthGuard], children:
    [
      { path: 'inicio', component : PaeContratacaoComponent},

      { path: 'principal', component: PaeContratacaoComponent },

      { path: 'paeContratacao', component: PaeContratacaoComponent }
      
    ]
  },
  { path: 'login', component: LoginComponent },
  
    // otherwise redirect to home
  { path: '**', redirectTo: '' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }