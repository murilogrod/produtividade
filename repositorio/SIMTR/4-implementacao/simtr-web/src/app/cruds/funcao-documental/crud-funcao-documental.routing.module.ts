
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FuncaoDocumentalComponent } from './funcao-documental.component';



const routes: Routes = [
  
      { path: '', component: FuncaoDocumentalComponent }
]


@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CrudFuncaoDocumentalRoutingModule { }