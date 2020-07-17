import { TratamentoModule } from './tratamento/tratamento.module';
import { TemplateModule } from './template/template.module';
import { ConsultaClienteModule } from './cliente/consulta-cliente.module';
import { ModalModule } from './template/modal/modal.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, ErrorHandler } from '@angular/core';
import { HttpModule } from '@angular/http';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import { AuthGuard } from './guards';
import { AuthenticationService, ApplicationService, HttpInterceptorService, EventService, LoaderService } from './services';
import { SharedModule, OverlayPanelModule, SliderModule, CardModule } from 'primeng/primeng';
import { BootstrapModalModule } from 'angularx-bootstrap-modal';
import { MacroProcessoModule } from './macro-processo/macro-processo.module';
import { ProdutoDocumentoModule } from './produto-documento/produto-documento.module';
import { GarantiaProdutoModule } from './garantia-produto/garantia-produto.module';
import { ManutencaoDossieModule } from './dossie';
import { VisualizadorDocumentosModule } from './visualizador-documentos/visualizador-documentos.module';
import { ClassificacaoGenericoModule } from './documento/classificacao-generico/classificacao-generico.module';
import { FormataDocValido } from './extracao-manual/pipes/formata-doc-valido/formata-doc-valido.pipe';
import { FormataMimePipe } from './extracao-manual/pipes/formata-mime/formata-mime.pipe';
import { FormataTituloApenso } from './extracao-manual/pipes/formata-titulo-apenso/formata-titulo-apenso.pipe';
import { ZerosEsquerda } from './extracao-manual/pipes/zeros-esquerda/zeros-esquerda.pipe';
import { InfoComponent } from './info/info.component';
import { ProdutoModule } from './produto/produto.module';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ConversorDocumentosUtil } from './documento/conversor-documentos/conversor-documentos.util.service';
import { GlobalErrorHandlerService } from './global-error/global-erro-handle.service';
import { GlobalErrorComponent } from './global-error/view/global-error.component';
import { DashboardModule } from './dash/dashboard.module';
import { ComunicacaoJBPMModule } from './paineis/comunicacao-jbpm.module'
import { AnalyticsModule } from './shared/analytics.module';
import { ComplementacaoModule } from './complementacao/complementacao.module';
import { ShareModule } from './shared/share.module';
import { RouterService } from './services/router-service';
import { LoaderRelativoModule } from './services/loader-relativo/loader-relativo.module';
import { NgxMaskModule } from 'ngx-mask'
import { GlobalErrorComponentPresenter } from './global-error/presenter/global-error.component.presenter';
import { DataService } from './services/data-service';
import { TratamentoValidarPermissaoService } from './tratamento/tratamento-validar-permissao.service';
import { CrudModule } from './cruds/crud.module';
import { LocalStorageDataService } from './services/local-storage-data.service';
import { VisualizadorExternoModule } from './tratamento/tela-visualizador-documento/visualizador-externo/visualizador-externo.module';
import { DeviceDetectorModule } from 'ngx-device-detector';

@NgModule({
  declarations: [
    AppComponent,
    FormataDocValido,
    FormataMimePipe,
    FormataTituloApenso,
    ZerosEsquerda,
    InfoComponent,
    GlobalErrorComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpModule,
    AppRoutingModule,
    HttpClientModule,
    SharedModule,
    BootstrapModalModule.forRoot({ container: document.body }),
    NgxMaskModule.forRoot(),
    ModalModule,
    ConsultaClienteModule,
    ProdutoModule,
    ManutencaoDossieModule,
    ComplementacaoModule,
    TemplateModule,
    MacroProcessoModule,
    ProdutoDocumentoModule,
    TratamentoModule,
    GarantiaProdutoModule,
    VisualizadorDocumentosModule,
    ClassificacaoGenericoModule,
    RadioButtonModule,
    SliderModule,
    OverlayPanelModule,
    CardModule,
    DashboardModule,
    ShareModule,
    AnalyticsModule,
    LoaderRelativoModule,
    CrudModule,
    VisualizadorExternoModule,
    ComunicacaoJBPMModule,
    DeviceDetectorModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandlerService
    },
    GlobalErrorHandlerService,
    GlobalErrorComponentPresenter,
    AuthenticationService,
    AuthGuard,
    ApplicationService,
    EventService,
    LoaderService,
    ConversorDocumentosUtil,
    RouterService,
    DataService,
    TratamentoValidarPermissaoService,
    LocalStorageDataService
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
