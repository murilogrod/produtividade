import { Component, OnInit } from '@angular/core';
import { DialogComponent, DialogService } from 'angularx-bootstrap-modal';
import { ApplicationService, AuthenticationService } from '../../../services';
import { LOCAL_STORAGE_CONSTANTS, PERFIL_ACESSO } from '../../../constants/constants';
import { ConjuntoMacroProcessoGeraDossieUriParams } from '../../../model/model-produto/conjunto-macro-processo-gera-dossie-uri-params.model';
import { ConversorDocumentosUtil } from 'src/app/documento/conversor-documentos/conversor-documentos.util.service';
import { BehaviorSubject } from 'rxjs';
import { ArquivoPdfOriginal } from 'src/app/documento/conversor-documentos/model/arquivo-pdf-original';
import { DocumentImage } from 'src/app/documento/documentImage';
import { environment } from 'src/environments/environment';

export interface MessageModel {}

export interface MessageModelSaida {
  processosSelecionados: any;
}

declare var $: any;

@Component({
  selector: 'modal-processos',
  templateUrl: './modal-processos.component.html',
  styleUrls: ['./modal-processos.component.css']
})
export class ModalProcessosComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit{
  listaMacroProcessos: any;
  listaMacroProcessosAux: any;
  listaProcessosGeraDossie: any;
  listaProcessosGeraDossieAux: any;
  conjuntoMacroGeraDossieSelecionado: ConjuntoMacroProcessoGeraDossieUriParams;
  exibeBotaoNovo:boolean;
  exibirProcessosOcultos?:boolean;
  isMTRADM:boolean;
  
  constructor(
    private appService: ApplicationService,
    dialogService: DialogService,
    private authenticationService: AuthenticationService,
    private conversorDocumentosUtil: ConversorDocumentosUtil
  ) {
    super(dialogService);
  }

  ngOnInit() {
    this.conjuntoMacroGeraDossieSelecionado = new ConjuntoMacroProcessoGeraDossieUriParams();
    this.exibirProcessosOcultos = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.MOSTRA_PROCESSOS_OCULTOS));
    this.exibirProcessosOcultos = this.exibirProcessosOcultos == null ? false : this.exibirProcessosOcultos;
    this.isMTRADM = this.hasCredentialMTRADM();
    this.listaMacroProcessos = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoPatriarca));
    // VERIFICA QUAIS PROCESSOS ESTÃO HABILITADOS PARA A UNIDADE DO USUARIO
    this.listaMacroProcessos = this.verificarUnidadeAutorizada(this.listaMacroProcessos);
    //se ao carregar o macroprocesso a opção exibirProcessosOcultos = true, entao guarda a lista de ocultos na lista auxiliar
    if(this.exibirProcessosOcultos){
      this.listaMacroProcessosAux = this.listaMacroProcessos.filter(processo => processo.indicador_interface);
    }else{
      this.listaMacroProcessosAux = this.listaMacroProcessos;
    }
    this.listaMacroProcessos = this.filtraProcesosHabilitadosVisualizacao(this.listaMacroProcessos);
  } 
 
  /**
	 * Verifica se o usuário logado possui a permissao de MTRADM.
	 */
	hasCredentialMTRADM(): boolean {
		const credencial: string = `${PERFIL_ACESSO.MTRADM}`;
		if (!environment.production) {
			return this.appService.hasCredential(credencial, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.appService.hasCredential(credencial, this.authenticationService.getRolesSSO());
		}
  }
  
  /**
   * Filtra somente processos que estejam marcados para visualização na tela
   * @param listaProcessos 
   */
  private filtraProcesosHabilitadosVisualizacao(listaProcessos: any): any{
    if(!this.isMTRADM || this.isMTRADM && !this.exibirProcessosOcultos){
      listaProcessos = listaProcessos.filter(processo => processo.indicador_interface);
    }
    return listaProcessos;
  }

  showProcessosOcultos(){
    this.exibirProcessosOcultos = !this.exibirProcessosOcultos;
    this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.MOSTRA_PROCESSOS_OCULTOS, this.exibirProcessosOcultos);
    
    let listaMacroProcessosTemp = this.listaMacroProcessos;
    this.listaMacroProcessos = this.listaMacroProcessosAux;
    this.listaMacroProcessos = Object.assign([], this.listaMacroProcessos);
    this.listaMacroProcessosAux = listaMacroProcessosTemp;
    
    if(this.listaProcessosGeraDossie){
      let listaProcessosGeraDossieTemp = this.listaProcessosGeraDossie;
      this.listaProcessosGeraDossie = this.listaProcessosGeraDossieAux;
      this.listaProcessosGeraDossie = Object.assign([], this.listaProcessosGeraDossie);
      this.listaProcessosGeraDossieAux = listaProcessosGeraDossieTemp;
    }
  }

  onChangeMacroProcesso(indice){
    let indiceNumber: number = Number(indice).valueOf();
    if(isNaN(indiceNumber)){
      this.exibeBotaoNovo = false;
      this.conjuntoMacroGeraDossieSelecionado.idMacroProcesso = null;
      return;
    }
    let macroProcessoEscolhido: any = this.listaMacroProcessos[indiceNumber];
    this.conjuntoMacroGeraDossieSelecionado.idMacroProcesso = macroProcessoEscolhido.id;
    if(macroProcessoEscolhido.processos_filho.length == 0){
      this.listaProcessosGeraDossie = [];
      this.listaProcessosGeraDossieAux = [];
      return;
    } else {
      macroProcessoEscolhido.processos_filho = macroProcessoEscolhido.processos_filho.filter(processo => processo.unidade_autorizada);
    }
    //se ao carregar processo filho do macroprocesso a opção exibirProcessosOcultos = true, entao guarda a lista de ocultos na lista auxiliar
    if(this.exibirProcessosOcultos){
      this.listaProcessosGeraDossieAux = macroProcessoEscolhido.processos_filho.filter(processo => processo.indicador_interface);
    }else{
      this.listaProcessosGeraDossieAux = macroProcessoEscolhido.processos_filho;
    }
    this.listaProcessosGeraDossie = this.filtraProcesosHabilitadosVisualizacao(macroProcessoEscolhido.processos_filho);
  }

  onChangeProcessoGeraDossie(indice){
    let indiceNumber: number = Number(indice).valueOf();
    if(isNaN(indiceNumber)){
      this.exibeBotaoNovo = false;
      this.conjuntoMacroGeraDossieSelecionado.idProcessoGeraDossie = null;
      return;
    }
    let processoGeraDossieEscolhido: any = this.listaProcessosGeraDossie[indiceNumber];
    this.conjuntoMacroGeraDossieSelecionado.idProcessoGeraDossie = processoGeraDossieEscolhido.id;
    if(this.conjuntoMacroGeraDossieSelecionado.idMacroProcesso && this.conjuntoMacroGeraDossieSelecionado.idProcessoGeraDossie){
      this.exibeBotaoNovo = true;
    }
  }

  confirm() {
    this.result = {processosSelecionados: this.conjuntoMacroGeraDossieSelecionado};
    this.conversorDocumentosUtil.arquivosPdfOringinais = new BehaviorSubject<Array<ArquivoPdfOriginal>>(new Array<ArquivoPdfOriginal>());
    this.conversorDocumentosUtil.paginasRestantesPdf = new BehaviorSubject<Array<DocumentImage>>(new Array<DocumentImage>());
    this.close();
  }

  cancel() {
    this.result = null;
    this.close();
  }

  verificarUnidadeAutorizada(listaMacroProcessos: any) : any {
    if (listaMacroProcessos !== undefined) {
        listaMacroProcessos = listaMacroProcessos.filter(processo => processo.unidade_autorizada);
    }
    return listaMacroProcessos;
  }

}
