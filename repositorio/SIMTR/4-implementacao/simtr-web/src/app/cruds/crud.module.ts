import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ColorPickerModule } from 'primeng/colorpicker';
import { MessageService } from 'primeng/components/common/messageservice';
import { GrowlModule } from 'primeng/growl';
import { KeyFilterModule } from 'primeng/keyfilter';
import { CalendarModule, CardModule, ConfirmationService, ConfirmDialogModule, DataTableModule, DropdownModule, OverlayPanelModule, PanelModule, SpinnerModule, TooltipModule, SelectButtonModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { TemplateModule } from 'src/app/template/template.module';
import { ShareModule } from '../shared/share.module';
import { ModalApontamentoPresenter } from './apontamentos/modal-apontamento/presenter/modal-apontamento.component.presenter';
import { ModalApontamentoComponent } from './apontamentos/modal-apontamento/view/modal-apontamento.component';
import { ApontamentosPresenter } from './apontamentos/presenter/apontamentos.component.presenter';
import { ApontamentosComponent } from './apontamentos/view/apontamentos.component';
import { ModalAtributoComponent } from './atributo/modal-atributo/view/modal-atributo.component';
import { AtributoComponent } from './atributo/view/atributo.component';
import { CanalPresenter } from './canal/cadastro/presenter/canal.component.presenter';
import { CanalComponent } from './canal/cadastro/view/canal.component';
import { PesquisaCanalPresenter } from './canal/pesquisa/presenter/pesquisa-canal.component.presenter';
import { PesquisaCanalComponent } from './canal/pesquisa/view/pesquisa-canal.component';
import { CanalService } from './canal/service/canal.service';
import { ChecklistPresenter } from './checklist/cadastro/presenter/checklist.component.presenter';
import { ChecklistComponent } from './checklist/cadastro/view/checklist.component';
import { ModalCloneInativarRemoverPresenter } from './checklist/pesquisa/modal-clone-inativar-remover-checklist/presenter/modal-clone-inativar-remover.component.presenter';
import { ModalCloneInativarRemoverChecklistComponent } from './checklist/pesquisa/modal-clone-inativar-remover-checklist/view/modal-clone-inativar-remover-checklist.component';
import { PesquisaCheckListPresenter } from './checklist/pesquisa/presenter/pesquisa-checklist.component.presenter';
import { PesquisaChecklistComponent } from './checklist/pesquisa/view/pesquisa-checklist.component';
import { ApontamentoService } from './checklist/service/apontamento.service';
import { CheckListService } from './checklist/service/checklist.service';
import { VinculacaoService } from './checklist/service/vinculacao.service';
import { CrudGridHeaderComponent } from './crud-grid-header/crud-grid-header.component';
import { CrudRoutingModule } from './crud-routing.module';
import { GrowlMessageService } from './growl-message-service/growl-message.service';
import { ValidationFormMessageComponent } from './message/validation-form-message.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { TipoDocumentoPresenter } from './tipo-documento/cadastro/presenter/tipo-documento.component.presenter';
import { TipoDocumentoComponent } from './tipo-documento/cadastro/view/tipo-documento.component';
import { PesquisaTipoDocumentoPresenter } from './tipo-documento/pesquisa/presenter/pesquisa-tipo-documento.component.presenter';
import { PesquisaTipoDocumentoComponent } from './tipo-documento/pesquisa/view/pesquisa-tipo-documento.component';
import { TipoDocumentoService } from './tipo-documento/service/tipo-documento.service';
import { TipoRelacionamentoPresenter } from './tipo-relacionamento/cadastro/presenter/tipo-relacionamento.component.presenter';
import { TipoRelacionamentoComponent } from './tipo-relacionamento/cadastro/view/tipo-relacionamento.component';
import { PesquisaTipoRelacionamentoPresenter } from './tipo-relacionamento/pesquisa/presenter/pesquisa-tipo-relacionamento.component.presenter';
import { PesquisaTipoRelacionamentoComponent } from './tipo-relacionamento/pesquisa/view/pesquisa-tipo-relacionamento.component';
import { TipoRelacionamentoService } from './tipo-relacionamento/service/tipo-relacionamento.service';
import { ModalVinculacaoPresenter } from './vinculacoes/modal-vinculacao/presenter/modal-vinculacao.component.presenter';
import { ModalVinculacaoComponent } from './vinculacoes/modal-vinculacao/view/modal-vinculacao.component';
import { VinculacoesPresenter } from './vinculacoes/presenter/vinculacoes.component.presenter';
import { VinculacoesComponent } from './vinculacoes/view/vinculacoes.component';
import { OpcaoGridComponent } from './atributo/modal-atributo/opcao-grid/view/opcao-grid.component';
import { ExpressaoGridComponent } from './atributo/modal-atributo/expressao-grid/view/expressao-grid.component';
import { FuncaoDocumentalService } from './funcao-documental/funcao-documental.service';
import { LoaderRelativoModule } from '../services/loader-relativo/loader-relativo.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    DropdownModule,
    DataTableModule,
    ConfirmDialogModule,
    TemplateModule,
    CardModule,
    CalendarModule,
    CrudRoutingModule,
    KeyFilterModule,
    TableModule,
    TooltipModule,
    DropdownModule,
    SpinnerModule,
    GrowlModule,
    OverlayPanelModule,
    ShareModule,
    AutoCompleteModule,
    ColorPickerModule,
    LoaderRelativoModule,
    SelectButtonModule
  ],
  declarations: [
    CanalComponent,
    ChecklistComponent,
    PesquisaCanalComponent,
    CrudGridHeaderComponent,
    ValidationFormMessageComponent,
    NotFoundComponent,
    PesquisaChecklistComponent,
    ApontamentosComponent,
    ApontamentosComponent,
    VinculacoesComponent,
    ModalVinculacaoComponent,
    ModalApontamentoComponent,
    ModalCloneInativarRemoverChecklistComponent,
    TipoRelacionamentoComponent,
    PesquisaTipoRelacionamentoComponent,
    PesquisaTipoDocumentoComponent,
    TipoDocumentoComponent,
    ModalAtributoComponent,
    AtributoComponent,
    OpcaoGridComponent,
    ExpressaoGridComponent
  ],
  entryComponents: [
    ModalVinculacaoComponent,
    ModalApontamentoComponent,
    ModalCloneInativarRemoverChecklistComponent,
    ModalAtributoComponent
  ],
  providers: [
    CanalService,
    PesquisaCanalPresenter,
    ChecklistPresenter,
    CanalPresenter,
    ModalVinculacaoPresenter,
    CheckListService,
    ApontamentoService,
    VinculacaoService,
    PesquisaCheckListPresenter,
    ConfirmationService,
    ApontamentosPresenter,
    VinculacoesPresenter,
    ModalApontamentoPresenter,
    MessageService,
    GrowlMessageService,
    ModalCloneInativarRemoverPresenter,
    TipoRelacionamentoPresenter,
    PesquisaTipoRelacionamentoPresenter,
    TipoRelacionamentoService,
    PesquisaTipoDocumentoPresenter,
    TipoDocumentoPresenter,
    TipoDocumentoService,
    FuncaoDocumentalService
  ]
})
export class CrudModule { }
