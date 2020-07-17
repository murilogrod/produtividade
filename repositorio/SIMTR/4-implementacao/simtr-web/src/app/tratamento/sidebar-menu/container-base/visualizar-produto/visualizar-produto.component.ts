import { Component, Input, ViewEncapsulation, OnInit } from '@angular/core';
import { Utils } from 'src/app/utils/Utils';
import { RespostaCampoFormulario } from 'src/app/model/resposta-campo-formulario';

@Component({
  selector: 'visualizar-produto',
  templateUrl: './visualizar-produto.component.html',
  styleUrls: ['./visualizar-produto.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class VisualizarProdutoComponent implements OnInit {
  @Input() produtoVinculado: any;
  @Input() dossieProduto: any;
  respostaCampoFormulario: RespostaCampoFormulario [] = []
  collapsed:any;

  ngOnInit() {
    this.respostaCampoFormulario = this.produtoVinculado.respostas_formulario;
  }

  formatValor(v: any) {
    return Utils.formatMoney(v,2, " R$: ", ".", ",");
  }
}
