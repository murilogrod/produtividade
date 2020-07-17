import { Component, OnInit, ViewEncapsulation, OnChanges, AfterViewInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { HeaderSearchService } from '../services/header-search.service';
import { ProdutoService } from './produto-service';
import { LoaderService, EventService } from '../services';
import { DialogService } from 'angularx-bootstrap-modal';
import { SITUACAO_DOSSIE_PRODUTO } from '../constants/constants';
import { ModalProcessosComponent } from '../template/modal/modal-processos/modal-processos.component';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-produto',
  templateUrl: './produto.component.html',
  styleUrls: ['./produto.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ProdutoComponent extends HeaderSearchService implements OnInit, OnChanges, AfterViewInit {

  constructor(
    private produtoService: ProdutoService,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private router: Router,
    private loadService: LoaderService,
    private eventService : EventService,
  ) {
    super(eventService);
  }

  headerSearch(filtros: any) {
  }
  
  cleanHeaderSearch() {
  }

  idDossie: string;
  idConsultado: string;
  formulario: FormGroup;
  searchDone: boolean = false;
  userFound: boolean = false;
  inserting: boolean = true;
  searching: boolean = false;
  existeDossie: boolean = true;

  ngOnInit() {
    this.formulario = this.formBuilder.group({
      idDossie: [this.idDossie, [Validators.required]]
    });
  }

  ngOnChanges() {

  }  

  ngAfterViewInit() {
    $("#idDossie").on('keydown', function(e) {
      var numero = (e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105);
      var controlos = [8, 37, 39, 86, 109, 46, 190].includes(e.keyCode);
      if (!numero && !controlos) return e.preventDefault();
    });
  }

  disableSearch() {
    return !this.idDossie || this.idDossie == "";
  }

  hideSearchForm() {
    return this.inserting || this.searching;
  }

  /**
   * Limpa todos atributos
   */
  private resetAll() {
    this.userFound = false;
    this.inserting = false;
    this.searchDone = false;
  }

  applyCssError() {
    return {
      'has-error': !this.existeDossie
    };
  }
  
  chamaConsulta() {
    this.search(true)
  }

  abreModalProcessos(){
    this.dialogService.addDialog(ModalProcessosComponent, {
    }).subscribe(retorno => {
      if(retorno){
        let processo = ""+ retorno.processosSelecionados.idMacroProcesso+","+ retorno.processosSelecionados.idProcessoGeraDossie+"";
        this.router.navigate(['manutencaoDossie', 'dossieProduto', SITUACAO_DOSSIE_PRODUTO.NOVO, processo]);
      }
    },
    () => {
      this.loadService.hide();
    });
  }

  search(chamarUrl : boolean) {
    this.resetAll();
    this.loadService.show();
    this.idConsultado = this.idDossie;
 
    this.produtoService.getDossieProduto(this.idDossie).subscribe(response => {
        if(response) {
          this.router.navigate(['manutencaoDossie', response.id, SITUACAO_DOSSIE_PRODUTO.CONSULTAR]);
        }
        if(!response) {
          this.quandoNaoEncontrarDossieLancarExcessao();
        }
        this.loadService.hide();
    },
    () => {
      this.loadService.hide();
    });

  }
  

  /**
   * Quando não encontrar Dossiê Produto esses campos habilita msg
   */
  private quandoNaoEncontrarDossieLancarExcessao() {
    this.existeDossie = false;
    this.userFound = false;
    this.searchDone = true;
  }
}
