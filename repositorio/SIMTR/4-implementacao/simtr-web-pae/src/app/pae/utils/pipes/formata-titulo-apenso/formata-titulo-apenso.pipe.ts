import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataTituloApenso'
})
export class FormataTituloApenso implements PipeTransform {

  transform(value: any, args?: any): any {
    
    if(value) {
      let valor = value.toString();
      valor = valor.replace(' ', '_')
      return valor      
  }
  return value;
  }

}
