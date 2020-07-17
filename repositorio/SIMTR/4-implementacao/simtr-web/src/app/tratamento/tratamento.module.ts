import { TratamentoService } from './tratamento.service';
import { TelaInicialComponent } from './tela-inicial/tela-inicial.component';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TratamentoDossieComponent } from './tratamento-dossie/tratamento-dossie.component';
import { SidebarMenuComponent } from './sidebar-menu/sidebar-menu.component';
import { TelaDragDropComponent } from './tela-dragDrop/tela-dragDrop.component';
import { ContainerBaseComponent } from './sidebar-menu/container-base/container-base.component';
import { FieldsetModule, AccordionModule, CalendarModule, CarouselModule, DataTableModule, OverlayPanelModule, SliderModule, InputSwitchModule } from 'primeng/primeng';
import { VisualizadorDossieClienteComponent } from './sidebar-menu/container-base/visualizador-dossie-cliente/visualizador-dossie-cliente.component';
import { VisualizadorFormularioComponent } from './sidebar-menu/container-base/visualizador-formulario/visualizador-formulario.component';
import { VisualizadorArvoreDocumentos } from './sidebar-menu/container-base/visualizador-arvore-documentos/visualizador-arvore-documentos.component';
import { ShareModule } from '../shared/share.module';
import { TelaVisualizadorDocumentoComponent } from './tela-visualizador-documento/tela-visualizador-documento.component';
import { TelaWizardComponent } from './tela-tratamento-wizard/tela-tratamento-wizard.component';
import { ModalJustificativaApontamentoComponent } from './tela-dragDrop/check-list-container/modal-justificativa-apontamento/modal-justificativa-apontamento.component';
import { visualizadorCheckListComponent } from './sidebar-menu/container-base/visualizador-check-list/visualizador-check-list.component';
import { VisualizarGarantiaComponent } from './sidebar-menu/container-base/visualizar-garantia/visualizar-garantia.component';
import { VisualizarPessoaComponent } from './sidebar-menu/container-base/visualizar-pessoa/visualizar-pessoa.component';
import { VisualizarProdutoComponent } from './sidebar-menu/container-base/visualizar-produto/visualizar-produto.component';
import { BoxMacroprocessoComponent } from './box-macroprocesso/box-macroprocesso.component';
import { TemplateModule } from '../template/template.module';
import { PortalModule } from '@angular/cdk/portal';
import { OpenVisualizadorComponent } from './tela-visualizador-documento/open-visualizador/open-visualizador.component';
import { VisualizarComponent } from './tela-visualizador-documento/visualizar/visualizar.component';
import { SwitchComponent } from './sidebar-menu/switch/switch.component';
import { ConteudoVisualizacaoDocumentosComponent } from './sidebar-menu/container-base/visualizador-arvore-documentos/conteudo-visualizacao-documentos/view/conteudo-visualizacao-documentos.component';
import { ConteudoVisualizacaoDocumentoPresenter } from './sidebar-menu/container-base/visualizador-arvore-documentos/conteudo-visualizacao-documentos/presenter/conteudo-visualizacao-documento.component.presenter';
import {TooltipModule} from 'primeng/tooltip';
import { CheckListComponent } from './tela-dragDrop/check-list-container/view/check-list.component';
import { CheckListComponentPresenter } from './tela-dragDrop/check-list-container/presenter/check-list.component.presenter';
import { AnalyticsModule } from '../shared/analytics.module';
import { AlertaMenssagemModule } from '../alert-menssagem/alert-menssagem.module';
import { FormularioRespostaGenericoModule } from '../documento/formulario-resposta-generico/formulario-resposta-generico.module';
import { AbrirDocumentoPopupDefaultService } from '../services/abrir-documento-popup-default.service';
import { TelaDeTratamentoService } from './tela-de-tratamento.service';

@NgModule({
  imports: [
    CommonModule,
    FieldsetModule,
    AccordionModule,
    FormsModule,
    ShareModule,
    CarouselModule,
    CalendarModule,
    TemplateModule,
    DataTableModule,
    OverlayPanelModule,
    SliderModule,
    PortalModule,
    TooltipModule,
    AnalyticsModule,
    AlertaMenssagemModule,
    FormularioRespostaGenericoModule,
    SliderModule,
    InputSwitchModule
  ],
  declarations: [
    TelaInicialComponent,
    TratamentoDossieComponent,
    SidebarMenuComponent,
    ContainerBaseComponent,
    VisualizadorDossieClienteComponent,
    VisualizadorFormularioComponent,
    visualizadorCheckListComponent,
    VisualizadorArvoreDocumentos,
    ConteudoVisualizacaoDocumentosComponent,
    TelaDragDropComponent,
    TelaVisualizadorDocumentoComponent,
    TelaWizardComponent,
    CheckListComponent,
    ModalJustificativaApontamentoComponent,
    VisualizarGarantiaComponent,
    VisualizarPessoaComponent,
    VisualizarProdutoComponent,
    BoxMacroprocessoComponent,
    OpenVisualizadorComponent,
    VisualizarComponent,
    SwitchComponent
  ],

  entryComponents:[
    ModalJustificativaApontamentoComponent
  ],

  exports : [
    TelaInicialComponent,
    TratamentoDossieComponent,
    TelaDragDropComponent,
    TelaVisualizadorDocumentoComponent,
    TelaWizardComponent
  ] ,
  providers : [
    TratamentoService,
    ConteudoVisualizacaoDocumentoPresenter,
    CheckListComponentPresenter,
    AbrirDocumentoPopupDefaultService,
    TelaDeTratamentoService
  ]
})
export class TratamentoModule { }
