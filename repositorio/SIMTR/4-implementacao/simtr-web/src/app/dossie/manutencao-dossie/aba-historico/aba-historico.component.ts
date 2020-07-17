import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AlertMessageService } from '../../../services';
import { historicoSituacoes } from '../../../model/historico_situacoes';

declare var $: any;

@Component({
  selector: 'aba-historico',
  templateUrl: './aba-historico.component.html',
  styleUrls: ['./aba-historico.component.css']
})
export class AbaHistoricoComponent extends AlertMessageService implements OnInit {

    @Input() exibeHistoricoDossieProduto : boolean;
    @Input() hitoriaDossieLista :  historicoSituacoes[];

    ngOnInit() {
  
    }
}