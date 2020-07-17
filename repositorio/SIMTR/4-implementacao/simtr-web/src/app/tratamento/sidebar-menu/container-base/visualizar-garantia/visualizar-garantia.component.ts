import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Utils } from 'src/app/utils/Utils';
import { RespostaCampoFormulario } from 'src/app/model/resposta-campo-formulario';

@Component({
  selector: 'visualizar-garantia',
  templateUrl: './visualizar-garantia.component.html',
  styleUrls: ['./visualizar-garantia.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class VisualizarGarantiaComponent implements OnInit {
  @Input() garantiaVinculada: any;
  @Input() dossieProduto: any;
  respostasFormulario: RespostaCampoFormulario[] = []
  collapsed:any;

  ngOnInit() {
    this.respostasFormulario = this.garantiaVinculada.respostas_formulario;
  }

  formatValor(v: any) {
    return Utils.formatMoney(v,2, " R$:  ", ".", ",");
  }

}
