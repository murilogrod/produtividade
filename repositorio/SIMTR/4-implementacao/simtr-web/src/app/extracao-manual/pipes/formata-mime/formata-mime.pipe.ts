import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formataMime'
})
export class FormataMimePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    
    const values = value.split('/')
    if (values.length === 2) {
      return values[1].toUpperCase();
    }
    return value;
  }

}
