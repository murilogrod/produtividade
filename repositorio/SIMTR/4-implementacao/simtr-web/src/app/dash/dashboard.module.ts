import { DashboardService } from './dashboard.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TabViewModule, TooltipModule } from 'primeng/primeng';
import { InputSwitchModule } from 'primeng/inputswitch';
import { KeyFilterModule } from 'primeng/keyfilter';
import { TabelaSituacaoComponent } from './tabela-situacao/view/tabela-situacao.component';
import { DashComponent } from './view/dashboard.component';
import { DashboardCompoenentPresenter } from './presenter/dasboard.component.presenter';
import { AnalyticsModule } from '../shared/analytics.module';
import { FormsModule } from '@angular/forms';
import { TemplateModule } from '../template/template.module';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { TabelaSituacaoComponentPresenter } from './tabela-situacao/presenter/tabela-situacao.component.presenter';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';
import { TabelaProcessoComponent } from './tabela-processo/view/tabela-processo.component';
import { TabelaProcessoComponentPresenter } from './tabela-processo/presenter/tabela-processo.component.presenter';

@NgModule({
  imports: [
    CommonModule,
    TableModule,
    TabViewModule,
    AnalyticsModule,
    TooltipModule,
    FormsModule,
    InputSwitchModule,
    KeyFilterModule,
    TemplateModule,
    PanelModule,
    LoaderRelativoModule
  ],
  declarations: [
    DashComponent,
    TabelaSituacaoComponent,
    TabelaProcessoComponent

  ],
  exports: [
    DashComponent
  ],
  providers: [
    DashboardService,
    DashboardCompoenentPresenter,
    TabelaSituacaoComponentPresenter,
    TabelaProcessoComponentPresenter
  ]
})
export class DashboardModule { }
