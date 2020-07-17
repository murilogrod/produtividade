import {Component, OnInit, ViewEncapsulation, Input} from '@angular/core';
import { VinculoCliente } from '../../../../model';
import { Utils } from 'src/app/utils/Utils';

@Component({
  selector: 'visualizador-dossie-cliente',
  templateUrl: './visualizador-dossie-cliente.component.html',
  styleUrls: ['./visualizador-dossie-cliente.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class VisualizadorDossieClienteComponent implements OnInit {
  @Input() dossieCliente: VinculoCliente;
  collapsed: any;

  constructor(){
  }

  ngOnInit() {
  }

  mascararTelefone(valor: string) {
    return Utils.mascTelefone(valor);
  }

  mascaraNis(valor: string) {
    return Utils.mascNis(valor);
  }

}
