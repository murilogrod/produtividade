import { NgModule } from "@angular/core";
import { ComplementacaoComponent } from "./view/complementacao.component";
import { ComplementacaoComponentPresenter } from "./presenter/complementacao.component.presenter";
import { AccordionModule } from "primeng/primeng";
import { CommonModule } from "@angular/common";

@NgModule({
    imports: [
      CommonModule,
      AccordionModule
    ],
    declarations: [
      ComplementacaoComponent
    ],
    providers :[
      ComplementacaoComponentPresenter
    ],
    entryComponents: [

    ],
    exports : [
      ComplementacaoComponent
    ]
  })
  export class ComplementacaoModule { }