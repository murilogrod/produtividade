import { LoaderService } from '../../../../../../services/http-interceptor/loader/loader.service';
import { ConsultaClienteService } from '../../../../service/consulta-cliente-service';
import { Component, OnInit, Input , Output , EventEmitter} from "@angular/core";

@Component({
  selector: "modal-sicli-list-cocli",
  templateUrl: "./modal-sicli-list-cocli.component.html",
  styleUrls: ["./modal-sicli-list-cocli.component.css"]
})
export class ModalSicliListCocliComponent implements OnInit {
  @Input() listCocli;

  @Input() cpf;

  @Output() onReturnDetail : EventEmitter<any> = new EventEmitter<any>();

  firstValue: any;

  cpfCnpj : string;

  error = false;

  constructor(private clienteService : ConsultaClienteService,
    private loadService : LoaderService) {}

  ngOnInit() {
    this.firstValue = this.listCocli['0'];
    this.cpfCnpj = this.cpf ? ('' + this.firstValue.CPF.doc.numero + this.firstValue.CPF.doc.dv) : ( '' + this.firstValue.CNPJ.doc.num + this.firstValue.CNPJ.doc.dv);
  }

  searchClienteByCocli(cocli) {
    this.loadService.show();
    /*this.clienteService.getClienteByCocli(cocli,this.cpf).subscribe(res => {
      this.onReturnDetail.emit(res);
    } , error => {
      this.error = true;
    }, () => {
      this.loadService.hide();
    });
    */
  }
}
