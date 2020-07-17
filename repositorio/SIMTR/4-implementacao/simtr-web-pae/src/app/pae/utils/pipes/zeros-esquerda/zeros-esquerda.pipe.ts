import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'zerosEsquerda'
})
export class ZerosEsquerda implements PipeTransform {

  transform(value: any, args?: any): any {
    const zeros = '0000000000000000000000000000000000000000'
    if (value === undefined || value === null || value.length === 0 || args === undefined || typeof value !== 'number' || typeof args !== 'number' ) {
      return value
    }
   
    let dif = 0
    let valor = value.toString();
    const tam = valor.length
    valor = valor.replace(/\D/g, '')
    dif = args - tam  
    return zeros.substring(0,dif) + valor
  }

}
