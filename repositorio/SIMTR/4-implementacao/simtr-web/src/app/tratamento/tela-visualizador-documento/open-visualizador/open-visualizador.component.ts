import { Component, OnInit, OnDestroy, ViewChild, ComponentFactoryResolver, ApplicationRef, Injector, Input, Output, EventEmitter} from '@angular/core';
import { CdkPortal, DomPortalHost } from '@angular/cdk/portal';
import { Location } from '@angular/common';
import { LocalStorageVisualizador } from 'src/app/model/local-storage-visualizador';
import { ApplicationService, EventService } from 'src/app/services';
import { VISUALIZADOR_EXTERNO, FECHA_MODAL, SITUACAO_GATILHO_POPUP } from 'src/app/constants/constants';
import { AbrirDocumentoPopupDefaultService } from 'src/app/services/abrir-documento-popup-default.service';
import { TelaDeTratamentoService } from '../../tela-de-tratamento.service';

declare var $: any;

@Component({
  selector: 'app-open-visualizador',
  templateUrl: './open-visualizador.component.html',
  styleUrls: ['./open-visualizador.component.css']
})
export class OpenVisualizadorComponent implements OnInit, OnDestroy {
  @Input() showPortal:boolean;
  @Output() showPortalChange:EventEmitter<boolean> = new EventEmitter<boolean>();

  @Input() dossieProduto: any;
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

  /** Quando true, indica que o checklist em exibição é um checklist de fase. */
  exibindoChecklistFase: boolean;

  @Input() opemModal: boolean;
  @Input() listaDocumento: any[];

  handleChangeShowPortal(input) {
    this.showPortal = input;
  }

  // STEP 1: get a reference to the portal
  @ViewChild(CdkPortal) portal: CdkPortal;

  // STEP 2: save a reference to the window so we can close it
  private externalWindow = null;

  private host: DomPortalHost;

  // STEP 3: Inject all the required dependencies for a PortalHost
  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef,
    private eventService : EventService,
    private injector: Injector,
    private location: Location,
    private appService: ApplicationService,
    private abrirDocumentoPopupDefaultService: AbrirDocumentoPopupDefaultService,
		private telaDeTratamentoService: TelaDeTratamentoService){}

  ngOnInit(){
    this.telaDeTratamentoService.exibindoChecklistFase.subscribe(
      exibindoChecklistFase => this.exibindoChecklistFase = exibindoChecklistFase);

    if(this.idCombo == 1){
      let objImagemVisualizar = new LocalStorageVisualizador();
      objImagemVisualizar.alutraMaxHeigthImg = this.alutraMaxHeigthImg;
      objImagemVisualizar.dossieProduto = this.dossieProduto;
      objImagemVisualizar.idDocumento = this.listDocumentosImagens[0].id;
      objImagemVisualizar.listDocumentosImagens = JSON.parse(JSON.stringify(this.listDocumentosImagens))
  
      // deve limpar o objeto listDocumentosImagens para evitar o error "Storage: Setting the value of exceeded the quota".  
      if(objImagemVisualizar.listDocumentosImagens[0]){
        objImagemVisualizar.listDocumentosImagens[0].binario = "";
        objImagemVisualizar.listDocumentosImagens[0].conteudos = [];
      }
  
      objImagemVisualizar.visualizadorDois = this.visualizadorDois;
      objImagemVisualizar.idVisualizar = this.idVisualizar;
      objImagemVisualizar.listaDocumentacao = this.listaDocumentacao;
      objImagemVisualizar.alutraMaxHeigthImg = this.alutraMaxHeigthImg;
      objImagemVisualizar.idCombo = this.idCombo;
      objImagemVisualizar.layoutVerticalA = this.layoutVerticalA;
      objImagemVisualizar.layoutVerticalB = this.layoutVerticalB;
      objImagemVisualizar.hasMapListaCombo = this.hasMapListaCombo;
      objImagemVisualizar.exibindoChecklistFase = this.exibindoChecklistFase;
      objImagemVisualizar.opemModal = this.opemModal;
      objImagemVisualizar.listaDocumento = this.listaDocumento;
  
      this.appService.saveInLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO, JSON.stringify(objImagemVisualizar));
      // STEP 4: create an external window 
      let pathName = location.pathname.split('/');
      this.externalWindow = window.open(pathName[0]+'/visualizarImagemExterna', '', "toolbar=yes, scrollbars=yes, resizable=yes, top=250, left=500, width=1000, height=500");
    
      // if(!this.visualizadorDois) {
      //   this.appService.saveInLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO, JSON.stringify(objImagemVisualizar));
      //   // STEP 4: create an external window    
      //   this.externalWindow = window.open(environment.serverPath.replace(':8080/simtr-api/rest', '')+'/visualizarImagemExterna', '', "toolbar=yes, scrollbars=yes, resizable=yes, top=250, left=500, width=850, height=500");
      // }else {
      //   this.appService.saveInLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO_1, JSON.stringify(objImagemVisualizar));
      //   // STEP 4: create an external window    
      //   this.externalWindow = window.open(environment.serverPath.replace(':8080/simtr-api/rest', '')+'/visualizarImagemExterna_2', '', "toolbar=yes, scrollbars=yes, resizable=yes, top=250, left=500, width=850, height=500");
      // }

      // STEP 5: create a PortalHost with the body of the new window document
      if(this.externalWindow){
        this.abrirDocumentoPopupDefaultService.setExecutouUmaUnicaVez(true);
        this.abrirDocumentoPopupDefaultService.setGatilhoAbrirPopup(SITUACAO_GATILHO_POPUP.EM_ABERTO); 
        this.host = new DomPortalHost(
          this.externalWindow.document.body,
          this.componentFactoryResolver,
          this.applicationRef,
          this.injector
        );
      }    
    

      let loop = setInterval(event =>  {   
        if(this.externalWindow && this.externalWindow.closed) {
            this.appService.removeInLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO);
            this.appService.removeInLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO_2);
            this.abrirDocumentoPopupDefaultService.setGatilhoAbrirPopup(SITUACAO_GATILHO_POPUP.FECHADO);  
            this.showPortalChange.emit(false);
            clearInterval(loop);   
        }  
      }, 500);

      this.eventService.on(FECHA_MODAL.SHOW_PORTAL, () => {
        if(this.externalWindow){
          this.externalWindow.close();
        }
      });

      // STEP 6: Attach the portal
      // host.attach(this.portal);
      }
  }

  ngOnDestroy(){
    // STEP 7: close the window when this component destroyed
    this.fecharOpenVisuzualizador()
  }

  fecharOpenVisuzualizador() {
    this.showPortalChange.emit(false);
    if(this.externalWindow){
      this.externalWindow.close();
    }
  }

}
