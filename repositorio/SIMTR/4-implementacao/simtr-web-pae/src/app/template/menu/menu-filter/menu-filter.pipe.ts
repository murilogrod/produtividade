import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'menuFilter',
  pure: false
})
export class MenuFilterPipe implements PipeTransform {

  transform(items: any[], args?: any): any {
    if (!items || !args) {
      return items;
    }
    return items.filter(item => item.title.toUpperCase().indexOf(args.toUpperCase()) !== -1);
  }

}
