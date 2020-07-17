import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataCpfCnpj'
})
export class FormataCpfCnpj implements PipeTransform {

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
      } else {
          return valor.substring(0,2).concat('.')
                               .concat(valor.substring(2,5))
                               .concat('.')
                               .concat(valor.substring(5,8))
                               .concat('/')
                               .concat(valor.substring(8,12))
                               .concat('-')
                               .concat(valor.substring(12,14))
      }
  }
  return value;
  }

}
