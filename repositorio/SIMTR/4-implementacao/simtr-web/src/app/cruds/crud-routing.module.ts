import { TipoDocumentoComponent } from './tipo-documento/cadastro/view/tipo-documento.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../guards';
import { PERFIL_ACESSO } from '../constants/constants';
import { CanalComponent } from './canal/cadastro/view/canal.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { PesquisaChecklistComponent } from './checklist/pesquisa/view/pesquisa-checklist.component';
import { ChecklistComponent } from './checklist/cadastro/view/checklist.component';
import { TipoRelacionamentoComponent } from './tipo-relacionamento/cadastro/view/tipo-relacionamento.component';

import { PesquisaTipoDocumentoComponent } from './tipo-documento/pesquisa/view/pesquisa-tipo-documento.component';
import { PesquisaCanalComponent } from './canal/pesquisa/view/pesquisa-canal.component';
import { PesquisaTipoRelacionamentoComponent } from './tipo-relacionamento/pesquisa/view/pesquisa-tipo-relacionamento.component';

const routes: Routes = [
  {
    path: 'cadastro/canal', component: PesquisaCanalComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/canal/:id', component: CanalComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/canal/novo', component: CanalComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/canal/:id/:notfound', component: NotFoundComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/checklist', component: PesquisaChecklistComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/checklist/:id', component: ChecklistComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },

  {
    path: 'cadastro/checklist/:id/:op', component: ChecklistComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },


  {
    path: 'cadastro/checklist/:id/:notfound', component: NotFoundComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },

  {
    path: 'cadastro/checklist/novo', component: ChecklistComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-relacionamento', component: PesquisaTipoRelacionamentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-relacionamento/:id', component: TipoRelacionamentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-relacionamento/novo', component: TipoRelacionamentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-relacionamento/:id/:notfound', component: NotFoundComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  
  {
    path: 'cadastro/tipo-documento', component: PesquisaTipoDocumentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-documento/:id', component: TipoDocumentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-documento/novo', component: TipoDocumentoComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },
  {
    path: 'cadastro/tipo-documento/:id/:notfound', component: NotFoundComponent, canActivate: [AuthGuard],
    data: {
      roles: [
        PERFIL_ACESSO.MTRADM,
        PERFIL_ACESSO.MTRTEC
      ]
    }
  },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
]

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CrudRoutingModule { }
