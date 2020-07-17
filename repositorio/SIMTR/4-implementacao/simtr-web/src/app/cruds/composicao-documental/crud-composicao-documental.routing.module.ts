
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ComposicaoDocumentalComponent } from './composicao-documental.component';




const routes: Routes = [
  
      { path: '', component: ComposicaoDocumentalComponent }
]


@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CrudComposicaodocumentalRoutingModule { }