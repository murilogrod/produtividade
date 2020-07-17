import { Injectable } from "@angular/core";
import { GlobalError } from "../model/global-error";
import { ApplicationService } from "src/app/services";
import { AnalyticsService } from "src/app/services/analytics/analytics.service";
import { UtilsErrorGlobal } from "../utils/utils-erros";
import { GLOBAL_ERROR } from "../utils/error-contants";
import { HttpErrorResponse } from "@angular/common/http";

@Injectable()
export class GlobalErrorComponentPresenter {

    listaErros: GlobalError[];
    constructor( private analytics: AnalyticsService) {}

    montarListaDeErros(error: any, appService: ApplicationService) {
      let erroGlobal = new GlobalError();
      this.listaErros = [];

      if (error instanceof HttpErrorResponse) {
        //Backend returns unsuccessful response codes such as 404, 500 etc.  
        this.montarListaErrorExistente(appService);
        erroGlobal = UtilsErrorGlobal.montarObjetoErroGlobal(erroGlobal, error);
      }
      else {
        //A client-side or network error occurred.
        if(!error.stack.includes("ExpressionChangedAfterItHasBeenCheckedError") && !error.message.includes("ExpressionChangedAfterItHasBeenCheckedError")) {
          this.montarListaErrorExistente(appService);
          erroGlobal = UtilsErrorGlobal.montarObjetoErroGlobal(erroGlobal, error);
        }
          
      }
      
      if( Object.keys(erroGlobal).length != 0 && !this.listaErros.some(erro => erro.message === erroGlobal.message && erro.titulo === erroGlobal.titulo)) {
        console.log(error);
        this.listaErros.push(erroGlobal);
        appService.removeInLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
        appService.saveInLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE, JSON.stringify(this.listaErros));
      }
      this.analytics.trackException(error.message);
    }
  
    montarListaErrorExistente(appService: ApplicationService) {
      let lista = appService.getItemFromLocalStorage(GLOBAL_ERROR.CAMINHO_LOCALSTORE);
      if (lista && lista.length > 0) {
        this.listaErros = JSON.parse(lista);
      }
    }

}