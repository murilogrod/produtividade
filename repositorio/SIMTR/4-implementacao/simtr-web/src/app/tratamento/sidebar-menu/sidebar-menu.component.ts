import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { DossieProduto } from '../../model';
import { Checklist } from 'src/app/model/checklist';
import { ApontamentoChecklist } from 'src/app/model/apontamento-cheklist';

@Component({
  selector: 'sidebar-menu',
  templateUrl: './sidebar-menu.component.html',
  styleUrls: ['./sidebar-menu.component.css']
})
export class SidebarMenuComponent {
  @Input() processoDossieReFerenciaPatriarca: any;
  @Input() dossieProduto: DossieProduto;  
  @Input() listaChekList: ApontamentoChecklist[];
  @Input() listaDocumentoObrigatorio: Checklist[];
  @Input() idDoPrimeiroInstanciaDocumentoASerCarregado: number;
  @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
  @Output() idCheckListAtivadoChanged: EventEmitter<any> = new EventEmitter<any>();
  @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
  
  constructor(){
  }

  handleChangeListDocumentosImagem(input){
    this.listDocumentosImagemChanged.emit(Object.assign([], input));
  }

  handlleChangeEventCheckList(input){
    this.idCheckListAtivadoChanged.emit(input);
  }

  handleChangeListaChekList(input) {
    this.listaCheklistChanged.emit(Object.assign([], input));
  }
}
