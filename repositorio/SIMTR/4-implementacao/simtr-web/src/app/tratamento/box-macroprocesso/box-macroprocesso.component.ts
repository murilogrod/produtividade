import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Router } from '@angular/router';
import { EventService, AlertMessageService } from 'src/app/services';
import { EVENTS } from 'src/app/constants/constants';

@Component({
  selector: 'app-box-macroprocesso',
  templateUrl: './box-macroprocesso.component.html',
  styleUrls: ['./box-macroprocesso.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BoxMacroprocessoComponent extends AlertMessageService implements OnInit {

  constructor(private eventService: EventService,private router: Router) { super() }

  @Input() macroProcesso: any;
  naoExisteProximo: boolean;
  listaDossie: any[];

  ngOnInit() {
    this.eventService.on(EVENTS.alertMessage, msg => {
      this.addMessageWarning(msg);
    });
    this.listaDossie = this.macroProcesso.processos_filho;
  }
  
  chamarTelaTratamento(macro: number, id: number, vazio: boolean) {
    if(!vazio) {
      this.router.navigate(['tratamentoDossie', macro, id]); 
    } 
  }

}
