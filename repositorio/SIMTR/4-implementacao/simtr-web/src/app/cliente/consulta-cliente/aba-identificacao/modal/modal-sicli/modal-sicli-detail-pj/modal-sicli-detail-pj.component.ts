import { Component, OnInit, Input , ViewEncapsulation} from "@angular/core";
import { Utils } from '../../../../../../utils/Utils';

import * as moment from "moment";

@Component({
  selector: "modal-sicli-detail-pj",
  templateUrl: "./modal-sicli-detail-pj.component.html",
  styleUrls: ["./modal-sicli-detail-pj.component.css"],
  encapsulation: ViewEncapsulation.None
})
export class ModalSicliDetailPjComponent implements OnInit {
  @Input() sicliUser: any;

  @Input() data_fundacao: any;

  @Input() cnpj : string;

  constructor() {}

  ngOnInit() {}

  transformData(data) {
    if( data == null) {
      return 'NÃO INFORMADO';
    }
    return moment(data).format("DD/MM/YYYY");
  }

  formatValor(v: any) {
    return Utils.formatMoney(v,2, " R$:  ", ".", ",");
  }

}
