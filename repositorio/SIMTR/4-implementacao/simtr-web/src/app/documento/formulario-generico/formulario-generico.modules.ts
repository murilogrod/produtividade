import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { TabViewModule, AccordionModule, DropdownModule, DialogModule, FieldsetModule, AutoCompleteModule, TreeModule, CarouselModule, DataTableModule, SharedModule, CalendarModule, RadioButtonModule, CheckboxModule, FileUploadModule } from "primeng/primeng";
import { TableModule } from "primeng/table";
import { ShareModule } from "src/app/shared/share.module";
import { TemplateModule } from "src/app/template/template.module";
import { NgDragDropModule } from "ng-drag-drop";
import { BrMasker4Module } from "brmasker4";
import { AlertaMenssagemModule } from "src/app/alert-menssagem/alert-menssagem.module";
import { FormularioGenericoComponent } from "./view/formulario-generico.component";
import { HttpInterceptorService } from "src/app/services";
import { FormularioGenericoService } from "./service/formulario-generico.service";
import { FormularioGenericoComponentPresenter } from "./presenter/formulario-generico.component.presenter";
import { LoaderRelativoModule } from "src/app/services/loader-relativo/loader-relativo.module";
import { CepOnlineModule } from "src/app/compartilhados/componentes/cep-online/cep-online.module";

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
		CalendarModule,
		RadioButtonModule,
		CheckboxModule,
		ShareModule,
		TemplateModule,
		FileUploadModule,
		NgDragDropModule.forRoot(),
		BrMasker4Module,
		AlertaMenssagemModule,
		LoaderRelativoModule,
		CepOnlineModule
	],
	declarations: [
		FormularioGenericoComponent
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpInterceptorService,
			multi: true
		},
		FormularioGenericoService,
		FormularioGenericoComponentPresenter
	],
	entryComponents: [
		FormularioGenericoComponent
	]
	,
	exports: [
		FormularioGenericoComponent
	]
})
export class FormularioGenericoModule { }
