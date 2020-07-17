import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrMasker4Module } from 'brmasker4';
import { NgDragDropModule } from 'ng-drag-drop';
import { FileUploadModule } from 'ng2-file-upload';
import { AccordionModule, CalendarModule, CarouselModule, DataTableModule, FieldsetModule, RadioButtonModule, SharedModule, TabViewModule, TreeModule, DropdownModule, CheckboxModule, DialogModule, AutoCompleteModule, OverlayPanelModule, MultiSelectModule } from 'primeng/primeng';
import { DocumentoModule } from '../documento/documento.module';
import { HttpInterceptorService } from '../services';
import { TemplateModule } from '../template/template.module';
import { VisualizadorDocumentosModule } from '../visualizador-documentos/visualizador-documentos.module';
import { DossieService } from './dossie-service';
import { AbaDocumentoComponent } from './manutencao-dossie/aba-documento/view/aba-documento.component';
import { AbaDocumentoComponentPresenter } from './manutencao-dossie/aba-documento/presenter/aba-documento.component.presenter';
import { AbaFormularioComponent } from './manutencao-dossie/aba-formulario/view/aba-formulario.component';
import { ModalGarantiaComponent } from './manutencao-dossie/aba-formulario/modal/modal-garantia/modal-garantia.component';
import { ModalPessoaSimtrComponent } from './manutencao-dossie/aba-formulario/modal/modal-pessoa-simtr/modal-pessoa-simtr.component';
import { ModalPessoaComponent } from './manutencao-dossie/aba-formulario/modal/modal-pessoa/modal-pessoa.component';
import { ModalProdutoComponent } from './manutencao-dossie/aba-formulario/modal/modal-produto/modal-produto.component';
import { AbaHistoricoComponent } from './manutencao-dossie/aba-historico/aba-historico.component';
import { AbaVerificacaoComponent } from './manutencao-dossie/aba-verificacao/view/aba-verificacao.component';
import { AbaVinculoComponent } from './manutencao-dossie/aba-vinculo/view/aba-vinculo.component';
import { AbaVinculoComponentPresenter } from './manutencao-dossie/aba-vinculo/presenter/aba-vinculo.component.presenter';
import { ManutencaoDossieComponent } from './manutencao-dossie/manutencao-dossie.component';
import { ModalRejeicaoDocumentoComponent } from './manutencao-dossie/modal-rejeicao-documento/modal-rejeicao-documento.component';
import { ModalRevogarDocumentoComponent } from './manutencao-dossie/modal-revogar/modal-revogar-documento.component';
import { ManutencaoDossieComponentPresenter } from './manutencao-dossie/presenter/manutencao-dossie.component.presenter';
import { ModalSelecaoDossieComponent } from './modal-selecao-dossie/modal-selecao-dossie.component';
import { AlertaMenssagemModule } from '../alert-menssagem/alert-menssagem.module';
import { TableModule } from 'primeng/table';
import { AbaFormularioDossiePresenter } from './manutencao-dossie/aba-formulario/presenter/aba-formulario.component.presenter';
import { ShareModule } from '../shared/share.module';
import { FormularioGenericoModule } from '../documento/formulario-generico/formulario-generico.modules';
import { AbaVerificacaoComponentPresenter } from './manutencao-dossie/aba-verificacao/presenter/aba-verificacao.component.presenter';
import { TooltipModule } from 'primeng/tooltip';
import { ModalProdutoVisualizacaoComponent } from './manutencao-dossie/aba-formulario/modal/modal-produto-visualizacao/modal-produto-visualizacao.component';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';
import { NgxMaskModule } from 'ngx-mask';
import { ModalGarantiaVisualizacaoComponent } from './manutencao-dossie/aba-formulario/modal/modal-garantia-visualizacao/modal-garantia-visualizacao.component';
import ptBr from '@angular/common/locales/pt';
import { registerLocaleData } from '@angular/common';
import { AbaAdministrarDossieComponent } from './manutencao-dossie/aba-administrar-dossie/view/aba-administrar-dossie.component';
import { AbaAdministrarDossieComponentPresenter } from './manutencao-dossie/aba-administrar-dossie/presenter/aba-administrar-dossie.component.presenter';
import { SpinnerModule } from 'primeng/spinner';
import { ModalAdministrarDossieComponent } from './manutencao-dossie/aba-administrar-dossie/modal-administrar-dossie/view/modal-administrar-dossie.component';
import { AdministrarDossieComponentPresenter } from './manutencao-dossie/aba-administrar-dossie/modal-administrar-dossie/presenter/administrar-dossie.component.presenter';
import { AdministracaoDossieService } from './manutencao-dossie/aba-administrar-dossie/service/administracao-dossie.service';
import { KeyFilterModule } from 'primeng/keyfilter';
import { ModalSocioReceitaFederalComponent } from './manutencao-dossie/aba-vinculo/modal-socio-receita-federal/view/modal-socio-receita-federal.component';
import { SocioReceitaFederalPresenter } from './manutencao-dossie/aba-vinculo/modal-socio-receita-federal/presenter/socio-receita-federal.component.presenter';
registerLocaleData(ptBr)
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TabViewModule,
    AccordionModule,
    TableModule,
    DropdownModule,
    DialogModule,
    FieldsetModule,
    AutoCompleteModule,
    TreeModule,
    CarouselModule,
    DataTableModule,
    SharedModule,
    NgxMaskModule.forRoot(),
    CalendarModule,
    RadioButtonModule,
    CheckboxModule,
    ShareModule,
    TemplateModule,
    FileUploadModule,
    DocumentoModule,
    NgDragDropModule.forRoot(),
    BrMasker4Module,
    VisualizadorDocumentosModule,
    AlertaMenssagemModule,
    FormularioGenericoModule,
    OverlayPanelModule,
    TooltipModule,
    MultiSelectModule,
    LoaderRelativoModule,
    SpinnerModule,
    KeyFilterModule
  ],
  declarations: [
    ManutencaoDossieComponent,
    AbaDocumentoComponent,
    AbaFormularioComponent,
    ModalPessoaComponent,
    ModalProdutoComponent,
    ModalProdutoVisualizacaoComponent,
    ModalGarantiaVisualizacaoComponent,
    ModalGarantiaComponent,
    ModalPessoaSimtrComponent,
    AbaVinculoComponent,
    AbaHistoricoComponent,
    ModalSelecaoDossieComponent,
    ModalRejeicaoDocumentoComponent,
    ModalRevogarDocumentoComponent,
    AbaVerificacaoComponent,
    AbaAdministrarDossieComponent,
    ModalAdministrarDossieComponent,
    ModalSocioReceitaFederalComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    DossieService,
    AbaVinculoComponentPresenter,
    AbaDocumentoComponentPresenter,
    ManutencaoDossieComponentPresenter,
    AbaFormularioDossiePresenter,
    AbaVerificacaoComponentPresenter,
    AbaAdministrarDossieComponentPresenter,
    AdministrarDossieComponentPresenter,
    AdministracaoDossieService,
    SocioReceitaFederalPresenter
  ],
  entryComponents: [
    ModalPessoaComponent,
    ModalProdutoComponent,
    ModalGarantiaVisualizacaoComponent,
    ModalProdutoVisualizacaoComponent,
    ModalGarantiaComponent,
    ModalPessoaSimtrComponent,
    ModalSelecaoDossieComponent,
    ModalRejeicaoDocumentoComponent,
    ModalRevogarDocumentoComponent,
    ModalAdministrarDossieComponent,
    ModalSocioReceitaFederalComponent
  ]
  ,
  exports: [
    ManutencaoDossieComponent,
    AbaDocumentoComponent,
    AbaFormularioComponent,
    ModalPessoaComponent,
    ModalProdutoComponent,
    ModalProdutoVisualizacaoComponent,
    ModalGarantiaComponent,
    ModalPessoaSimtrComponent,
    AbaVinculoComponent,
    AbaHistoricoComponent,
    ModalSelecaoDossieComponent,
    ModalRejeicaoDocumentoComponent,
    ModalRevogarDocumentoComponent
  ]
})
export class ManutencaoDossieModule { }
