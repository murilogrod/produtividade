import { Pipe, PipeTransform } from '@angular/core';
import { Utils } from 'src/app/utils/Utils';

@Pipe({
  name: 'formataContaCaixa',
})
export class FormataContaCaixa implements PipeTransform {

  transform(value: any): string {

    if (value) {
        let new_value: string;

        new_value =  Utils.validarKeyupTipoCampoContaCaixa(value);

        return new_value                                    
    }else{
      return '';
    }
  }
  
}
