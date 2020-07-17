
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CrudProdutoComponent } from './produto.component';





const routes: Routes = [
  
      { path: '', component: CrudProdutoComponent }
]


@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CrudProdutoRoutingModule { }