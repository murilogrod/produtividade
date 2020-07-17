import { Component, AfterViewInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { ComplementacaoComponentPresenter } from "../presenter/complementacao.component.presenter";
import { Complementacao } from "../model/complementacao.model";
import { SITUACAO_DOSSIE_PRODUTO } from "../../constants/constants";

@Component({
    selector: 'complementacao',
    templateUrl: './complementacao.component.html',
    styleUrls: ['./complementacao.component.css'],
    encapsulation: ViewEncapsulation.None
  })
  
  export class ComplementacaoComponent implements AfterViewInit{
      complementacaoPresenter: ComplementacaoComponentPresenter;

      constructor(private router: Router, complementacaoPresenter: ComplementacaoComponentPresenter){
        this.complementacaoPresenter = complementacaoPresenter;
        this.complementacaoPresenter.complementacao = new Complementacao();
      }

      ngAfterViewInit(){
        this.buscarProcessosIndicamDossiesAguardandoComplementacao();
      }

      private buscarProcessosIndicamDossiesAguardandoComplementacao(){
        this.complementacaoPresenter.buscarProcessosContemDossiesAguardandoComplementacao();
      }

      chamarTelaManutencaoDossieProduto(idProcesso: number) {
        this.router.navigate(['manutencaoDossie', idProcesso, SITUACAO_DOSSIE_PRODUTO.COMPLEMENTACAO]); 
      }
  }