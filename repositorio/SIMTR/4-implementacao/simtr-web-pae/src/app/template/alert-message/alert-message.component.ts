import { Component, Input, OnChanges, SimpleChanges, ElementRef, ViewChild, Output, EventEmitter, Renderer} from '@angular/core';
import { STATUS_ALERT } from '../../constants/constants';
declare var $;

@Component({
  selector: 'cx-alert-message',
  templateUrl: './alert-message.component.html',
  styleUrls: ['./alert-message.component.css'],
})
export class AlertMessageComponent implements OnChanges {
  @ViewChild('alertMessages') alertMessages: ElementRef;

  @Input() messagesSuccess: string[];
  @Input() messagesInfo: string[];
  @Input() messagesError: string[];
  @Input() messagesWarning: string[];

  @Output() onCloseMessageSuccess = new EventEmitter<any>();
  @Output() onCloseMessageInfo = new EventEmitter<any>();
  @Output() onCloseMessageError = new EventEmitter<any>();
  @Output() onCloseMessageWarning = new EventEmitter<any>();
  entrouSegundaVez = false;

  constructor(private elementRef: ElementRef) {
    this.messagesSuccess = [];
    this.messagesInfo = [];
    this.messagesError = [];
    this.messagesWarning = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      this.alertMessages.nativeElement.focus();
    }
  }

  fecharAlert(tipoAlert: string) {
    this.entrouSegundaVez = !this.entrouSegundaVez;
    if (!this.entrouSegundaVez && (tipoAlert === STATUS_ALERT.WARNING || tipoAlert === STATUS_ALERT.INFOR)) {        
        this.emitirEventoConformeTipoAlerta(tipoAlert);
        return;
    }
    if(!this.entrouSegundaVez && tipoAlert !== STATUS_ALERT.WARNING && tipoAlert !== STATUS_ALERT.INFOR) {
      this.emitirEventoConformeTipoAlerta(tipoAlert);
    }  
  }

  private emitirEventoConformeTipoAlerta(tipoAlert: string) {
    this.limparMsgSucesso(tipoAlert); 
    this.limparMsgError(tipoAlert); 
    this.limparMsgInfor(tipoAlert); 
    this.limparMsgWarning(tipoAlert); 
  }

  private limparMsgWarning(tipoAlert: string) {
    if (tipoAlert === STATUS_ALERT.WARNING) {
      this.messagesWarning = [];
      this.onCloseMessageWarning.emit();
    }
  }

  private limparMsgInfor(tipoAlert: string) {
    if (tipoAlert === STATUS_ALERT.INFOR) {
      this.messagesInfo = [];
      this.onCloseMessageInfo.emit();
    }
  }

  private limparMsgError(tipoAlert: string) {
    if (tipoAlert === STATUS_ALERT.ERROR) {
      this.messagesError = [];
      this.onCloseMessageError.emit();
    }
  }

  private limparMsgSucesso(tipoAlert: string) {
    if (tipoAlert === STATUS_ALERT.SUCESSO) {
      this.messagesSuccess = [];
      this.onCloseMessageSuccess.emit();
    }
  }
}
