import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Utils } from "../utils/Utils";

@Injectable()
export class RouterService{
    
    constructor(private router: Router, private utils: Utils){
    }

     /**
     * 
     * @param router 
     * @param cpfCnpj 
     * @param aba 
     * Recarrega o dossie cliente quando salvar Documentos 
     * adicionais vindas do banco.
     */
    recarregaDossieClienteDepoisDeSalvo(cpfCnpj: any, aba: any) {
        this.router.navigateByUrl('/principal', { skipLocationChange: true }).then(() =>
            this.router.navigate(['principal', Utils.removerTodosCaracteresEspeciais(cpfCnpj), aba]));
    }
}