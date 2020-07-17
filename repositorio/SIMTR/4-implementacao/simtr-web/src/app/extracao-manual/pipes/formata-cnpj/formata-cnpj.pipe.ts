import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataCnpj'
})
export class FormataCnpj implements PipeTransform {

  transform(value: any, args?: any): any {
    
    if(value) {
      
      let valor = value.toString();
      valor = valor.replace(/\D/g, '')
      
      if(valor.length <= 14) {
         
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
