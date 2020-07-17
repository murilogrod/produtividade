import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { RespostaCampoFormulario } from 'src/app/model/resposta-campo-formulario';

@Component({
  selector: 'visualizar-pessoa',
  templateUrl: './visualizar-pessoa.component.html',
  styleUrls: ['./visualizar-pessoa.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class VisualizarPessoaComponent implements OnInit {
  @Input() pessoaVinculada: any;
  @Input() dossieProduto: any;
  respostasFormulario: RespostaCampoFormulario[] = []
  collapsed:any;

  ngOnInit() {
    this.respostasFormulario = this.pessoaVinculada.respostas_formulario;
  }

}
