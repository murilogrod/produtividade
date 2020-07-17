import {Component, OnInit, ViewEncapsulation, Input, AfterViewInit} from '@angular/core';
import { RespostaCampoFormulario } from '../../../../model/resposta-campo-formulario';
import { ApplicationService } from 'src/app/services';
import { LOCAL_STORAGE_CONSTANTS, UNDESCOR, TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { InputMask } from 'primeng/primeng';
declare var $: any;

@Component({
  selector: 'visualizador-formulario',
  templateUrl: './visualizador-formulario.component.html',
  styleUrls: ['./visualizador-formulario.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class VisualizadorFormularioComponent implements OnInit, AfterViewInit {
    @Input() respostasCampoFormulario: RespostaCampoFormulario[];
    @Input() dossieProduto: any;
    habilitaAlteracao: boolean = false;
    collapsed: any;
    titulo: string = 'Informações do Formulário';

    constructor(private appService: ApplicationService){}

    ngOnInit() {
    }

    ngAfterViewInit() {
      this.styelRadiosCss();
      this.styelCheckboxCss();
    }

    private styelRadiosCss() {
      $('input[type="radio"]').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue'
      });
    }

    private styelCheckboxCss() {
      //Definido classe pra não entra em conflito com os toogle
      $('.ckeckboxForm').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue'
      });
    }
}
