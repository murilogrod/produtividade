import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { ARQUITETURA_SERVICOS } from "src/app/constants/constants";

@Injectable()
export class ProdutosDocumentacaoValidaService{
    constructor(private http: HttpClient){}

    public atualizaNivelDocumental(idDossieCliente: number){
        return this.http.post(environment.serverPath + 
                ARQUITETURA_SERVICOS.dossieCliente + "/" + 
                    idDossieCliente + 
                        "/nivel-documental", {});
    } 
}