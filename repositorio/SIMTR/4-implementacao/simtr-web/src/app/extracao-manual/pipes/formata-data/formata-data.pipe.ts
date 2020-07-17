import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataData'
})
export class FormataData implements PipeTransform {

  transform(value: any, args?: any): any {
    
    if(value) {
      
      let valor = value.toString();
      valor = valor.replace(/\D/g, '')
      
      if(valor.length <= 10) {
          return valor.substring(0,2).concat('/')
                               .concat(valor.substring(2,4))
                               .concat('/')
                               .concat(valor.substring(4,8));
      } 
    }
    return value;
  }

}
