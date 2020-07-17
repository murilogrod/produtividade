import { GlobalError } from "../model/global-error";
import { GLOBAL_ERROR } from "./error-contants";
import { TitulosError } from "./titulo-erros";

declare var $: any;

export class UtilsErrorGlobal {

    static montarObjetoErroGlobal(erroGlobal: GlobalError, error: any) {
        erroGlobal.header = GLOBAL_ERROR.TITULO_GRID;
        erroGlobal.ok = error.ok;
        erroGlobal.status = error.status;
        erroGlobal.titulo = this.tituloError(error);
        erroGlobal.statusText = error.statusText;
        erroGlobal.url = error.url;
        erroGlobal.stack = error.error && error.error.stacktrace ? error.error.stacktrace : error.stack;

        if(error.error) {

            // Quando o status é 0 servidor não encontrado
            if(error.status == TitulosError.ERROR_0.tipo){
                erroGlobal.detalhe = TitulosError.ERROR_0.detalhe;
            }else{
                erroGlobal.error = !error.error.detalhe ? error.error : null;
                erroGlobal.detalhe = this.detalheError(erroGlobal, error);
            }
            erroGlobal.url = erroGlobal.url ? erroGlobal.url : error.error.target.__zone_symbol__xhrURL;
            erroGlobal.titulo = erroGlobal.titulo +" "+ erroGlobal.url;
        }
        erroGlobal.message = this.messageError(erroGlobal, error);
        return erroGlobal;
    }

    static messageError(erroGlobal: GlobalError, error: any) {
        return  UtilsErrorGlobal.tratarMsg(erroGlobal, error); 
    }

    static detalheError(erroGlobal: GlobalError, error: any) {
        return  UtilsErrorGlobal.detalheUndefined(erroGlobal, error); 
    }

    private static detalheUndefined(erroGlobal: GlobalError, error: any) {
        let detalhe: string;
        
        switch (erroGlobal.status) {
            case TitulosError.ERROR_0.tipo:
                detalhe = TitulosError.ERROR_0.detalhe;
                break;
            default:
                detalhe = error.error && error.error.detalhe ? error.error.detalhe : null;
        }

        return detalhe;
    }

    private static tratarMsg(erroGlobal: GlobalError, error: any) {
        let message: string;
        switch (erroGlobal.status) {
            case TitulosError.ERROR_0.tipo:
                message = erroGlobal.url ? TitulosError.ERROR_0.menssage + " : " + erroGlobal.url : TitulosError.ERROR_0.menssage ;
                break;
            default:
                message = error.error && error.error.mensagem ? error.error.mensagem : error.message;
        }
        return message;
    }

	static tituloError(error: any) {
		let titulo: string;
		switch (error.status) {
			case TitulosError.ERROR_0.tipo:
				titulo = TitulosError.ERROR_0.descrica;
                break;
            case 400:
                titulo = "Falha de comunicação com o serviço" 
                break;
            case 500:
                titulo = "Falha de comunicação com o serviço"
            case 404:
                titulo = "Falha de comunicação com o serviço"     
                break;        
            default:
                titulo = TitulosError.ERROR_JAVA_SCRIPT;
		}
		return titulo;
	}
    
    static validarDigitalizacao(erroGlobal: any, eventService: any) {
        erroGlobal.titulo = 'Scanner';
        eventService.broadcast(GLOBAL_ERROR.CAMINHO, erroGlobal);
    }
}