import { DocumentoModule } from '../documento/documento.module';
import { ModalSicliListCocliComponent } from './consulta-cliente/aba-identificacao/modal/modal-sicli/modal-sicli-list-cocli/modal-sicli-list-cocli.component';
import { ModalSicliDetailPjComponent } from './consulta-cliente/aba-identificacao/modal/modal-sicli/modal-sicli-detail-pj/modal-sicli-detail-pj.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TabViewModule, AccordionModule, TreeModule, CarouselModule, DataTableModule, SharedModule, CalendarModule, FieldsetModule, CheckboxModule, RadioButtonModule, AutoCompleteModule, DropdownModule } from 'primeng/primeng';
import { TemplateModule } from '../template/template.module';
import { AbaProdutosComponent } from './consulta-cliente/aba-produtos/view/aba-produtos.component';
import { ProdutoDetalheComponent } from './consulta-cliente/aba-produtos/produto-detalhe/produto-detalhe.component';
import { ModalSicliComponent } from './consulta-cliente/aba-identificacao/modal/modal-sicli/modal-sicli.component';
import { HttpInterceptorService } from '../services';
import { ConsultaClienteService } from './consulta-cliente/service/consulta-cliente-service';
import { FileUploadModule } from 'ng2-file-upload';
import { NgDragDropModule } from 'ng-drag-drop';
import { BrMasker4Module } from 'brmasker4';
import { ProdutoListaComponent } from './consulta-cliente/aba-produtos/produto-lista/view/produto-lista.component';
import { VisualizadorDocumentosModule } from '../visualizador-documentos/visualizador-documentos.module';
import { ModalProcessoPorPessoaComponent } from './consulta-cliente/modal-processo-por-pessoa/modal-processo-por-pessoa.component';
import { TipoDocumentoService } from '../cruds/tipo-documento/service/tipo-documento.service';
import { ModalDadosDeclaradosComponent } from './consulta-cliente/aba-documentos/modal/modal-dados-declarados/view/modal-dados-declarados.component';
import { GridFuncaoDocumentalTipoDocumentoComponentPresenter } from './consulta-cliente/aba-nivel-documental/grid-funcao-documental-tipo-documento/presenter/grid-funcao-documental-tipo-documento.component.presenter';
import { GridProdutosDocumentacaoPendenteComponentPresenter } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-pendente/presenter/grid-produtos-documentacao-pendente.component.presenter';
import { GridProdutosDocumentacaoValidaComponentPresenter } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-valida/presenter/grid-produtos-documentacao-valida.component.presenter';
import { GridFuncaoDocumentalTipoDocumentoComponent } from './consulta-cliente/aba-nivel-documental/grid-funcao-documental-tipo-documento/view/grid-funcao-documental-tipo-documento.component';
import { GridProdutosDocumentacaoPendenteComponent } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-pendente/view/grid-produtos-documentacao-pendente.component';
import { GridProdutosDocumentacaoValidaComponent } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-valida/view/grid-produtos-documentacao-valida.component';
import { ProdutosDocumentacaoValidaService } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-valida/produtos-documentacao-valida.service';
import { ProdutosDocumentacaoPendenteService } from './consulta-cliente/aba-nivel-documental/grid-produtos-documentacao-pendente/produtos-documentacao-pendente.service';
import { TooltipModule } from 'primeng/tooltip';
import { AbaDocumentosComponent } from './consulta-cliente/aba-documentos/view/aba-documentos.component';
import { AbaDocumentosComponentPresenter } from './consulta-cliente/aba-documentos/presenter/aba-documentos.component.presenter';
import { ModalDadosDeclaradosPresenter } from './consulta-cliente/aba-documentos/modal/modal-dados-declarados/presenter/modal-dados-declarados.presenter';
import { AnalyticsModule } from '../shared/analytics.module';
import { AbaNivelDocumentalComponent } from './consulta-cliente/aba-nivel-documental/view/aba-nivel-documental.component';
import { ConsultarClienteComponentPresenter } from './consulta-cliente/presenter/consulta-cliente.compoenent.presenter';
import { AbaIdentificadorComponentPresenter } from './consulta-cliente/aba-identificacao/presenter/aba-identificacao.component.presenter';
import { ModalSicliDetailPfComponent } from './consulta-cliente/aba-identificacao/modal/modal-sicli/modal-sicli-detail-pf/view/modal-sicli-detail-pf.component';
import { ConsultaClienteComponent } from './consulta-cliente/view/consulta-cliente.component';
import { AbaDocumentosService } from './consulta-cliente/aba-documentos/service/aba-documentos.service';
import { ModalUserSSOComponent } from './consulta-cliente/aba-identificacao/modal/modal-user-sso/view/modal-user-sso.component';
import { UserSSOComponentPresenter } from './consulta-cliente/aba-identificacao/modal/modal-user-sso/presenter/user-sso.component.presenter';
import { UserSSOService } from './consulta-cliente/aba-identificacao/modal/modal-user-sso/service/user-sso-service';
import { AbaIdentificacaoComponent } from './consulta-cliente/aba-identificacao/view/aba-identificacao.component';
import { KeyFilterModule } from 'primeng/keyfilter';
import { ShareModule } from '../shared/share.module';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';
import { CepOnlineModule } from '../compartilhados/componentes/cep-online/cep-online.module';
import { AbaProdutoComponentPresenter } from './consulta-cliente/aba-produtos/presenter/aba-produtos.component.presenter';
import { ProdutoListaComponentPresenter } from './consulta-cliente/aba-produtos/produto-lista/presenter/produto-lista.component.presenter';
import { TableModule } from 'primeng/table';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TabViewModule,
    AccordionModule,
    TreeModule,
    CarouselModule,
    DataTableModule,
    SharedModule,
    CalendarModule,
    DropdownModule,
    CheckboxModule,
    RadioButtonModule,
    ShareModule,
    TemplateModule,
    FileUploadModule,
    NgDragDropModule.forRoot(),
    DocumentoModule,
    FieldsetModule,
    BrMasker4Module,
    VisualizadorDocumentosModule,
    TooltipModule,
    KeyFilterModule,
    AnalyticsModule,
    LoaderRelativoModule,
    CepOnlineModule,
    TableModule
  ],
  declarations: [
    ConsultaClienteComponent,
    AbaIdentificacaoComponent,
    AbaDocumentosComponent,
    AbaProdutosComponent,
    ProdutoDetalheComponent,
    AbaNivelDocumentalComponent,
    GridProdutosDocumentacaoValidaComponent,
    GridProdutosDocumentacaoPendenteComponent,
    GridFuncaoDocumentalTipoDocumentoComponent,
    ModalSicliComponent,
    ModalSicliDetailPfComponent,
    ModalSicliDetailPjComponent,
    ModalSicliListCocliComponent,
    ModalProcessoPorPessoaComponent,
    ProdutoListaComponent,
    ModalDadosDeclaradosComponent,
    ModalUserSSOComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    ConsultaClienteService,
    TipoDocumentoService,
    ProdutosDocumentacaoValidaService,
    ProdutosDocumentacaoPendenteService,
    UserSSOService,
    AbaDocumentosService,
    AbaIdentificadorComponentPresenter,
    AbaDocumentosComponentPresenter,
    ModalDadosDeclaradosPresenter,
    GridProdutosDocumentacaoValidaComponentPresenter,
    GridProdutosDocumentacaoPendenteComponentPresenter,
    GridFuncaoDocumentalTipoDocumentoComponentPresenter,
    ConsultarClienteComponentPresenter,
    UserSSOComponentPresenter,
    AbaProdutoComponentPresenter,
    ProdutoListaComponentPresenter
  ],
  entryComponents: [
    ModalSicliComponent,
    ModalProcessoPorPessoaComponent,
    ModalDadosDeclaradosComponent,
    ModalUserSSOComponent
  ],
  exports: [
    ConsultaClienteComponent,
    AbaIdentificacaoComponent,
    AbaDocumentosComponent,
    AbaProdutosComponent,
    ProdutoDetalheComponent,
    ModalSicliComponent,
    ModalSicliDetailPjComponent,
    ModalSicliListCocliComponent,
    ModalProcessoPorPessoaComponent,
    ModalDadosDeclaradosComponent,
    ModalUserSSOComponent
  ]
})
export class ConsultaClienteModule { }
