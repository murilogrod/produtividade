import { ViewEncapsulation ,Component, EventEmitter, Input, OnInit, Output, ChangeDetectorRef, AfterViewChecked, OnChanges} from '@angular/core';
import { LoaderService, ApplicationService } from 'src/app/services';
import { LOCAL_STORAGE_CONSTANTS } from 'src/app/constants/constants';
import { ConjuntoComboVinculoTipoDocSelecionado } from '../tela-dragDrop/check-list-container/model/conjunto-combo-vinculo-tipo-doc-selecionado.model';

declare var $: any;

@Component({
  selector: 'tela-visualizador-documento',
  templateUrl: './tela-visualizador-documento.component.html',
  styleUrls: ['./tela-visualizador-documento.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class TelaVisualizadorDocumentoComponent implements OnInit, AfterViewChecked {
  constructor(private changeDetectorRef:ChangeDetectorRef, private loadService: LoaderService) { }

  @Input() dossieProduto: any;
  @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
  @Input() listDocumentosImagens: any[];
  @Input() listDocumentosImagensComparar: any[];
  @Input() visualizadorDois: boolean;
  @Input() idVisualizar: number;
  @Input() listaDocumentacao: any[];
  @Input() alutraMaxHeigthImg: number;
  @Input() idCombo: number;
  @Input() layoutVerticalA: boolean;
  @Input() layoutVerticalB: boolean;
  @Input() hasMapListaCombo;

  @Input() showPortal: boolean;
  @Input() showPopup: boolean;
  @Output() showPortalChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() showPopupChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

  handleChangeShowPortal(input) {
    this.showPortal = input;
    this.showPortal ? $(".incoAddVisualizar").css('display','none') : $(".incoAddVisualizar").css('display','inline-block');
    this.showPortalChanged.emit(input);    
    setTimeout(() => {
      this.loadService.hide();
    }, 3000);
  }

  handleChangeShowPopup(input) {
    this.showPopupChanged.emit(input);
  }

  handleChangeListDocumentosImagem(input){
    this.listDocumentosImagemChanged.emit(Object.assign([], input));
  }

  ngOnInit() {}

  ngAfterViewChecked() {    
    this.changeDetectorRef.detectChanges(); 
  }

}