import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { ARQUITETURA_SERVICOS, INTERCEPTOR_SKIP_HEADER } from "src/app/constants/constants";
import { Observable } from "rxjs";
import { InclusaoSituacao } from "../model/inclusao-situacao.model";
import { AtualizarUnidadeManipuladora } from "../model/atualizar-unidade-manipuladora";
import { RemoverUltimasSituacoes } from "../model/remover-ultimas-situacoes";

@Injectable()
export class AdministracaoDossieService {

    constructor(private http: HttpClient) { }

    /**
     * Realiza um post para incluir uma nova situacao de dossie
     * @param inclusaoSituacao 
     */
    alterarSituacaoDossie(inclusaoSituacao: InclusaoSituacao): Observable<any> {
        let url: string = environment.serverPath + ARQUITETURA_SERVICOS.incluirNovaSituacaoDossie.replace("{id}", String(inclusaoSituacao.id)).replace("{tipo-situacao}", String(inclusaoSituacao.situacao));
        const headers = new HttpHeaders().set(INTERCEPTOR_SKIP_HEADER, '');
        return this.http.post<any>(url, inclusaoSituacao.observacao, { headers });
    }
    
    /**
     * Realiza um DELETE para remover as últimas situações do dossie produto
     * @param removerUltimasSituacoes 
     */
    removerUltimasSituacoes(removerUltimasSituacoes: RemoverUltimasSituacoes): Observable<any> {
        const url: string = ARQUITETURA_SERVICOS.exclusaoUltimasSituacoesDossieProduto.replace("{id}", String(removerUltimasSituacoes.id)).replace("{quantidade}", String(removerUltimasSituacoes.quantidade));
        return this.http.delete<any>(environment.serverPath + url);
    }

    /**
     * Realiza um PUT para atualizar as unidades Manipuladoras do Tratamento
     * @param atualizarUnidadeManipuladora 
     */
    atualizarUnidadesManipuladoras(atualizarUnidadeManipuladora: AtualizarUnidadeManipuladora): Observable<any> {
        const url: string = ARQUITETURA_SERVICOS.atualizarUnidadesManipuladoras.replace("{id}", String(atualizarUnidadeManipuladora.id));
        return this.http.put<any>(environment.serverPath + url, atualizarUnidadeManipuladora.unidades);
    }

    excluirDossieProduto(idDossieProduto: number){
        let url: string = ARQUITETURA_SERVICOS.exclusaoDossieProduto.replace("{id}", String(idDossieProduto))
        return this.http.delete<any>(environment.serverPath + url);
    }
}