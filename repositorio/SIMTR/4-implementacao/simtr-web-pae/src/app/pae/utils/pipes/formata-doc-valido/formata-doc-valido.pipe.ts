import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataDocValido'
})
export class FormataDocValido implements PipeTransform {

  transform(value: any, args?: any): any {
    
    if (value) {
      return 'Não'
    }
    return 'Sim';
  }

}
