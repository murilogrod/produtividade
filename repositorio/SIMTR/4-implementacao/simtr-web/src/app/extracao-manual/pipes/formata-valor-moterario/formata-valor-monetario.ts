import { Pipe, PipeTransform } from '@angular/core';
import { Utils } from 'src/app/utils/Utils';

@Pipe({
  name: 'formataValorMonetario',
})
export class FormataValorMonetario implements PipeTransform {

  transform(value: any): string {
    if (value) {
        let new_value: string;

        new_value =  Utils.aplicarMascaraMonetario(value); 

        return new_value                                    
    }else{
      return '';
    }
  }
}
