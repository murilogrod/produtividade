import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataTelefoneDDD'
})
export class FormataTelefoneDDD implements PipeTransform {

  transform(value: any, args?: any): any {
    
    let valor = ''
    if(value) {
      
      valor = value.toString();
      valor = valor.replace(/\D/g, '')
      if(valor.length <= 10) {
          return '(' + valor.substring(0,2).concat(') ')
                               .concat(valor.substring(2,6))
                               .concat('-')
                               .concat(valor.substring(6,10))
      } else {
          return '(' + valor.substring(0,2).concat(') ')
          .concat(valor.substring(2,7))
          .concat('-')
          .concat(valor.substring(7,11))
      }
    }

    return value;
  
  }
}
