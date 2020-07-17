import { Component, Input } from '@angular/core';

@Component({
  selector: 'crud-grid-header',
  templateUrl: './crud-grid-header.component.html',
  styleUrls: ['./crud-grid-header.component.css']
})
export class CrudGridHeaderComponent  {

  @Input() width: string;
  @Input() classIcon: string;
  @Input() labelTitle: string;
  @Input() labelValue: string;

}