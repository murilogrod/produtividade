import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { SolicitacaoVerificacaoAutorizacao } from "./model/solicitacao-verificacao-autorizacao.model";
import { environment } from "src/environments/environment";
import { ARQUITETURA_SERVICOS } from "src/app/constants/constants";
import { Observable } from "rxjs";

@Injectable()
export class ProdutosDocumentacaoPendenteService{
    constructor(private http: HttpClient){}

    public consultaProdutosDisponiveis(): Observable<any>{
        return this.http.get(environment.serverPath +
            ARQUITETURA_SERVICOS.cadastroProduto);
    }

    public simulaAutorizacaoProduto(verificacaoAutorizacao: SolicitacaoVerificacaoAutorizacao): Observable<any>{
        
        return this.http.get(environment.serverPath +
            ARQUITETURA_SERVICOS.autorizacaoProduto + 
                this.montaParamsGetSimulaAutorizacaoProduto(verificacaoAutorizacao));
    } 

    /**
     * 
     * @param verificacaoAutorizacao 
     */
    private montaParamsGetSimulaAutorizacaoProduto(verificacaoAutorizacao: SolicitacaoVerificacaoAutorizacao): string{
        let params: string;
        if(verificacaoAutorizacao.cpf_cliente){
            params = "?cpf_cliente=" + verificacaoAutorizacao.cpf_cliente;
        }else{
            params = "?cnpj_cliente=" +  verificacaoAutorizacao.cnpj_cliente;
        }
        params += "&operacao=" + verificacaoAutorizacao.operacao;
        params += "&modalidade=" + verificacaoAutorizacao.modalidade;
        return params;
    }
}