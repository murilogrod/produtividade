import { TabelaSinteticoUnidadeComponentPresenter } from './tabela-sintetico-unidade/presenter/tabela-sintetico-unidade.component.presenter';
import { TabelaSinteticoUnidadeComponent } from './tabela-sintetico-unidade/view/tabela-sintetico-unidade.component';
import { TabelaSinteticoProcessoComponent } from './tabela-sintetico-processo/view/tabela-sintetico-processo.component';
import { TabelaProcessoComponent } from './../dash/tabela-processo/view/tabela-processo.component';
import { TabelaSinteticoProcessoComponentPresenter } from './tabela-sintetico-processo/presenter/tabela-sintetico-processo.component.presenter';
import { TabelaAnaliticoComponent } from './tabela-analitico/view/tabela-analitico.component';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { KeyFilterModule } from 'primeng/keyfilter';
import { PanelModule } from 'primeng/panel';
import { TabViewModule, TooltipModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';
import { AnalyticsModule } from '../shared/analytics.module';
import { TemplateModule } from '../template/template.module';
import { ComunicacaoJBPMService } from './comunicacao-jbpm.service';
import { ComunicacaoJBPMComponentPresenter } from './presenter/comunicacao-jbpm.component.presenter';
import { TabelaAnaliticoComponentPresenter } from './tabela-analitico/presenter/tabela-analitico.component.presenter';
import { ComunicacaoJBPMComponent } from './view/comunicacao-jbpm.component';

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
    ComunicacaoJBPMComponent,
    TabelaAnaliticoComponent,
    TabelaSinteticoProcessoComponent,
    TabelaSinteticoUnidadeComponent
  ],
  exports: [
    ComunicacaoJBPMComponent
  ],
  providers: [
    ComunicacaoJBPMService,
    ComunicacaoJBPMComponentPresenter,
    TabelaAnaliticoComponentPresenter,
    TabelaSinteticoUnidadeComponentPresenter,
    TabelaSinteticoProcessoComponentPresenter
  ]
})
export class ComunicacaoJBPMModule { }
