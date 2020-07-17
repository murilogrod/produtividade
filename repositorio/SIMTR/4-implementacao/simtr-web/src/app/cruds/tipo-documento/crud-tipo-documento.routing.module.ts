
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PesquisaTipoDocumentoComponent } from './pesquisa/view/pesquisa-tipo-documento.component';

const routes: Routes = [
  
      { path: '', component: PesquisaTipoDocumentoComponent }
]


@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CrudTipoDocumentoRoutingModule { }