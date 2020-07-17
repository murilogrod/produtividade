import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { TabViewModule, AccordionModule, DropdownModule, DialogModule, FieldsetModule, AutoCompleteModule, TreeModule, CarouselModule, DataTableModule, SharedModule, CalendarModule, RadioButtonModule, CheckboxModule, FileUploadModule, TooltipModule } from "primeng/primeng";
import { TableModule } from "primeng/table";
import { ShareModule } from "src/app/shared/share.module";
import { TemplateModule } from "src/app/template/template.module";
import { NgDragDropModule } from "ng-drag-drop";
import { BrMasker4Module } from "brmasker4";
import { AlertaMenssagemModule } from "src/app/alert-menssagem/alert-menssagem.module";
import { HttpInterceptorService } from "src/app/services";
import { FormularioRespostaGenericoComponent } from "./formulario-resposta-generico";
import { NgxMaskModule } from "ngx-mask";

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		BrowserAnimationsModule,
		HttpClientModule,
		NgxMaskModule.forRoot(),
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
		FieldsetModule,
		TooltipModule
	],
	declarations: [
		FormularioRespostaGenericoComponent
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpInterceptorService,
			multi: true
		}
	],
	entryComponents: [
		FormularioRespostaGenericoComponent
	]
	,
	exports: [
		FormularioRespostaGenericoComponent
	]
})
export class FormularioRespostaGenericoModule { }
