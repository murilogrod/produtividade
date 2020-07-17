import { TelaInicialComponent } from './tratamento/tela-inicial/tela-inicial.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards';
import { LoginComponent } from './login';
import { TemplateComponent } from './template';

import { ManutencaoDossieComponent } from './dossie/manutencao-dossie/manutencao-dossie.component';
import { MacroProcessoComponent } from './macro-processo/macro-processo.component';

import { GarantiaProdutoComponent } from './garantia-produto/garantia-produto.component';
import { TratamentoDossieComponent } from './tratamento/tratamento-dossie/tratamento-dossie.component';
import { InfoComponent } from './info/info.component';
import { CanDeactivateGuard } from './guards/can-deactivate.guard';
import { ProdutoComponent } from './produto/produto.component';
import { PERFIL_ACESSO } from './constants/constants';
import { VisualizadorExternoComponent } from './tratamento/tela-visualizador-documento/visualizador-externo/visualizador-externo.component';
import { PerfilUsuarioComponent } from './template/perfil-usuario/perfil-usuario.component';
import { DashComponent } from './dash/view/dashboard.component';
import { ConsultaClienteComponent } from './cliente/consulta-cliente/view/consulta-cliente.component';
import { ComplementacaoComponent } from './complementacao/view/complementacao.component';
import { ExtracaoManualComponent } from './extracao-manual/extracao-manual.component';
import { ComunicacaoJBPMComponent } from './paineis/view/comunicacao-jbpm.component';

const routes: Routes = [
  {
    path: '', component: TemplateComponent, canActivate: [AuthGuard], canActivateChild: [AuthGuard], children:
      [
        {
          path: 'perfil', component: PerfilUsuarioComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.UMA_AUTHORIZATION
            ]
          }
        },

        {
          path: 'perfil/:perfil', component: PerfilUsuarioComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.UMA_AUTHORIZATION
            ]
          }
        },

        {
          path: 'tratamento', component: TelaInicialComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRSDNTTO,
              PERFIL_ACESSO.MTRSDNTTG
            ]
          }
        },

        {
          path: 'principal', component: ConsultaClienteComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ
            ]
          }
        },

        {
          path: 'principal/:cpfCnpj', component: ConsultaClienteComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ
            ]
          }
        },

        {
          path: 'principal/:cpfCnpj/:aba', component: ConsultaClienteComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ
            ]
          }
        },

        {
          path: 'dossieProduto', component: ProdutoComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ
            ]
          }
        },
        {
          path: 'complementacao', component: ComplementacaoComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRSDNTTO,
              PERFIL_ACESSO.MTRSDNTTG
            ]
          }
        },
        {
          path: 'tratamentoDossie/:macro/:id', component: TratamentoDossieComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRSDNTTO,
              PERFIL_ACESSO.MTRSDNTTG
            ]
          }
        },

        {
          path: 'tratamentoDossie/:idDossieProduto', component: TratamentoDossieComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRSDNTTO,
              PERFIL_ACESSO.MTRSDNTTG
            ],
          }
        },

        {
          path: 'manutencaoDossie/:id/:opcao/:processo', component: ManutencaoDossieComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ
            ]
          }
        },

        {
          path: 'manutencaoDossie/:id/:opcao', component: ManutencaoDossieComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRAUD,
              PERFIL_ACESSO.MTRTEC,
              PERFIL_ACESSO.MTRSDNOPE,
              PERFIL_ACESSO.MTRSDNMTZ,
              PERFIL_ACESSO.MTRSDNTTO,
              PERFIL_ACESSO.MTRSDNTTG
            ]
          }
        },

        {
          path: 'cadastroMacroProcesso', component: MacroProcessoComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRTEC
            ]
          }
        },

        {
          path: 'cadastroFuncaoDocumental', loadChildren: './cruds/funcao-documental/funcao-documental.module#FuncaoDocumentalModule', canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRTEC
            ]
          }
        },

        {
          path: 'cadastroComposicaoDocumental', loadChildren: './cruds/composicao-documental/composicao-documental.module#ComposicaoDocumentalModule', canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRTEC
            ]
          }
        },
        {
          path: 'cadastroProduto', loadChildren: './cruds/produto/produto.module#CrudProdutoModule', canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRTEC
            ]
          }
        },

        {
          path: 'cadastroGarantiaProduto', component: GarantiaProdutoComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRTEC
            ]
          }
        },

        {
          path: 'dashboard', component: DashComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.UMA_AUTHORIZATION
            ]
          }
        },

        {
          path: 'comunicacaoJBPM', component: ComunicacaoJBPMComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM
            ]
          }
        },

        {
          path: 'extracaoManual', component: ExtracaoManualComponent, canActivate: [AuthGuard],
          data: {
            roles: [
              PERFIL_ACESSO.MTRADM,
              PERFIL_ACESSO.MTRSDNEXT
            ]
          }
        }
      ]

  },
  { path: 'login', component: LoginComponent },
  { path: 'info', component: InfoComponent },
  { path: 'visualizarImagemExterna', component: VisualizadorExternoComponent },
  { path: 'visualizarImagemExterna_2', component: VisualizadorExternoComponent },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }