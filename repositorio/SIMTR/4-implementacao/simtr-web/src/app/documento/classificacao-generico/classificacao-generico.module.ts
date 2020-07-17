import { CalendarModule, TreeModule, CarouselModule, FieldsetModule, MultiSelectModule, DropdownModule} from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgDragDropModule} from 'ng-drag-drop';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ClassificacaoGenericoComponent } from './classificacao-generico.component';
import { HttpInterceptorService } from '../../services';
import { DocumentoService } from '../documento-service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    CalendarModule,
    NgDragDropModule,
    TreeModule,
    CarouselModule,
    FieldsetModule,
    MultiSelectModule,
    DropdownModule
  ],
  declarations: [
    ClassificacaoGenericoComponent
  ] ,
  entryComponents : []
  ,
  exports : [
    ClassificacaoGenericoComponent
  ],
  providers :[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    DocumentoService
  ]
})
export class ClassificacaoGenericoModule { }
