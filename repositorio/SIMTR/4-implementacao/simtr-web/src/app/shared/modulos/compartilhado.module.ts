import { NgModule } from '@angular/core';

import { SomenteNumerosDirective } from "src/app/extracao-manual/diretivas/somente-numeros";
import { FocoAutomaticoDirective } from 'src/app/extracao-manual/diretivas/foco-automatico';
import { CommonModule } from '../../../../node_modules/@angular/common';


@NgModule({
    imports:      [ CommonModule ],
    declarations: [ SomenteNumerosDirective, FocoAutomaticoDirective ],
    exports: [ SomenteNumerosDirective, FocoAutomaticoDirective  ]
  })
  
  export class CompartilhadodModule {}