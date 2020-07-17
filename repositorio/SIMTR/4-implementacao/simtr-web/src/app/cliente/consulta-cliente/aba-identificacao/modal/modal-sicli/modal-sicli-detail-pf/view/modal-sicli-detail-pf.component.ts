import { Component, OnInit, Input , ViewEncapsulation} from '@angular/core';
import { Utils } from '../../../../../../../utils/Utils';

import * as moment from 'moment';

@Component({
  selector: 'modal-sicli-detail-pf',
  templateUrl: './modal-sicli-detail-pf.component.html',
  styleUrls: ['./modal-sicli-detail-pf.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ModalSicliDetailPfComponent implements OnInit {

  @Input() sicliUser : any;

  @Input() data_nascimento : any;

  @Input() cpf : string;

  constructor() { }

  ngOnInit() {
  }

  transformData(data) {
    if( data == null) {
      return 'NÃO INFORMADO';
    }
    return moment(data).format('DD/MM/YYYY');
  }

  formatValor(v: any) {
    return Utils.formatMoney(v,2, " R$:  ", ".", ",");
  }

}
