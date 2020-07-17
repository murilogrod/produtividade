import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataCpf'
})
export class FormataCpf implements PipeTransform {

  transform(value: any, args?: any): any {
    
    if(value) {
      
      let valor = value.toString();
      valor = valor.replace(/\D/g, '')
      
      if(valor.length <= 11) {
          return valor.substring(0,3).concat('.')
                               .concat(valor.substring(3,6))
                               .concat('.')
                               .concat(valor.substring(6,9))
                               .concat('-')
                               .concat(valor.substring(9,11))
      }
  }
  return value;
  }

}
