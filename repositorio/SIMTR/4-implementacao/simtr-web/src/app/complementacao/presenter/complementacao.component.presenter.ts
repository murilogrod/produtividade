import { Injectable } from "@angular/core";
import { LoaderService } from "src/app/services";
import { DossieService } from "src/app/dossie/dossie-service";
import { Complementacao } from "../model/complementacao.model";

@Injectable()
export class ComplementacaoComponentPresenter{
    complementacao: Complementacao;
    
    constructor(private dossieService: DossieService,
                private loadService: LoaderService,){}

    buscarProcessosContemDossiesAguardandoComplementacao(){
        //LOADING
        this.loadService.show();
        this.dossieService.buscarProcessosAguardandoComplementacao()
          .subscribe(processos => { 
            this.complementacao.listaProcessos = processos;
            this.loadService.hide();
          }, error => {
            console.log(error);
            this.loadService.hide();
            throw error;
          });
    }
}