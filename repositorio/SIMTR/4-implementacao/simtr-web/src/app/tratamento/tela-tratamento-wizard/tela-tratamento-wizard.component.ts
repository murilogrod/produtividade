import { Component, OnInit, Input, OnChanges, Output, EventEmitter} from '@angular/core';
import { LOCAL_STORAGE_CONSTANTS, UNDESCOR} from 'src/app/constants/constants';
import { ApplicationService, AlertMessageService } from 'src/app/services';
declare var $: any;

@Component({
  selector: 'tela-wizard',
  templateUrl: './tela-tratamento-wizard.component.html',
  styleUrls: ['./tela-tratamento-wizard.component.css']
})
export class TelaWizardComponent extends AlertMessageService implements OnInit, OnChanges {
    constructor(
      private appService: ApplicationService
    ) {
      super();
    }
    
    @Input() dossieProduto;

    /** O ID do processo fase selecionado para exibição. */
    @Output() idFaseSelecionada = new EventEmitter<number>();
    
    listaFase: any;
    larguraWizard: string;
    wizard:boolean = false;
    processoAtivo: number;
    idProcessoFase: number;
    processoAnterior: number;
    posicaoFase: number;

    ngOnInit() {    
    }

    ngOnChanges() {
        this.motarWizardInicial();
    }

    private motarWizardInicial() {
        if (undefined != this.dossieProduto && !this.wizard) {
            this.processoAtivo = this.dossieProduto.processo_fase.id;
            this.idProcessoFase = this.processoAtivo;
            let dossie = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.dossieProduto.processo_dossie.id));
            this.listaFase = dossie.processos_filho;
            this.encontraPosicaoListaDaFase();
            this.wizard = true;
            this.calculoLarguraWizard();
            this.selecionaEtapa(this.idProcessoFase);
        }
    }

    selecionaEtapa(idFase) {
        this.idFaseSelecionada.emit(idFase); 
        this.idProcessoFase = idFase;
        let encontrou = false;
        this.listaFase.forEach((etapa, idx) => {
            if (!encontrou) {
              encontrou = etapa.id == this.idProcessoFase;
              this.definirParametroParaAtivarSetaAnteriorDoWizard(encontrou, etapa, idx);
            }
        }); 
    }

    private encontraPosicaoListaDaFase() {
      this.listaFase.forEach((fase, idx) => {
        if (fase.id == this.processoAtivo) {
          this.posicaoFase = idx;
          return;
        }
      });
    }

    /**
     * A parti do momento que encontrou receber true fica verificando se nao existe fase depois caso existe fica limitado a sempre manter o processo anterior
     * @param encontrou 
     * @param etapa 
     * @param idx 
     */
    private definirParametroParaAtivarSetaAnteriorDoWizard(encontrou: boolean, etapa: any, idx: any) {
      if (!encontrou) {
        this.processoAnterior = etapa.id;
      }
      else {
        this.EncontrouAFaseAtivaVerificandoAcoesParaManterAfaseAnteriorAtiva(idx);
      }
    }

    /**
     * faz a Verificacao se a idProcessoFase e diferente do processoAnterior se se o index e igual ele limpa se não manter o Anterior
     * @param idx 
     */
    private EncontrouAFaseAtivaVerificandoAcoesParaManterAfaseAnteriorAtiva(idx: any) {
      if (this.idProcessoFase != this.processoAnterior) {
        if (this.listaFase.length == idx || idx == 0) {
          //quando e o ultimo
          this.processoAnterior = null;
        }
        else {
          this.processoAnterior = this.processoAnterior;
        }
      }else {
        //quando e o primeiro
        this.processoAnterior = null;
      }
    }

    private calculoLarguraWizard() {
        this.larguraWizard = ""+ 100 / (this.listaFase.length) +"%";
    }
}