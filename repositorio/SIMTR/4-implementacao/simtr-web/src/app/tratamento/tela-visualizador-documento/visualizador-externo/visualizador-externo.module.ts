import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { VisualizadorExternoComponent } from "./visualizador-externo.component";
import { TemplateModule } from "src/app/template/template.module";
import { FieldsetModule, AccordionModule, CarouselModule, CalendarModule, DataTableModule, OverlayPanelModule, SliderModule, TooltipModule, InputSwitchModule } from "primeng/primeng";
import { FormsModule } from "@angular/forms";
import { ShareModule } from "src/app/shared/share.module";
import { PortalModule } from "@angular/cdk/portal";
import { AlertaMenssagemModule } from "src/app/alert-menssagem/alert-menssagem.module";
import { VisualizarExternoComponent } from "./visualizar-externo/visualizar-externo.componente";

@NgModule({
    imports: [
        CommonModule,
        TemplateModule,
        FieldsetModule,
        AccordionModule,
        FormsModule,
        ShareModule,
        CarouselModule,
        CalendarModule,
        DataTableModule,
        OverlayPanelModule,
        SliderModule,
        PortalModule,
        TooltipModule,
        AlertaMenssagemModule,
        InputSwitchModule
    ],
    declarations: [
        VisualizadorExternoComponent,
        VisualizarExternoComponent
    ] ,
    entryComponents : [
    ]
    ,
    exports : [
    ],
    providers :[
    ]
  })
  export class VisualizadorExternoModule { }
  