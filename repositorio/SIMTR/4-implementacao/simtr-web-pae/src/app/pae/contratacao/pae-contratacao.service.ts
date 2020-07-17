import { PaeApensoPost } from './../model/pae-apenso-post';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { PaeContratacao } from './../model/pae-contratacao';
import { ARQUITETURA_SERVICOS } from '../../constants/constants';
import { RequestOptions, ResponseContentType } from '../../../../node_modules/@angular/http';
import { Config } from '../../../../node_modules/protractor';
@Injectable()

export class PaeContratacaoService {
  
  urlBase: string  = environment.serverPath + '/processoadministrativo/v1';
  
  constructor(private http: HttpClient) { }

  get(numero: String, ano: String): Observable<any>  {
    return this.http.get(this.urlBase + '/numero/'+numero+'-'+ano);
  
  }//FIM METODO
  
  save(entidade: PaeContratacao): Observable<any> {
    
    return this.http.post(this.urlBase + '/processo', entidade);
  }//FIM METODO
  
  update(entidade: PaeContratacao): Observable<any> {
    
    return this.http.patch(this.urlBase + '/processo', entidade);
  }//FIM METODO
  
  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + '/processo/' + id);
  }
  
  obterProcessoNumeroAno( numero: string, ano: string ): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/numero/' + numero + '-' + ano );
  }

  obterProcessoPorId( id: string): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id);
  }

  obterApensos () {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/tipoapenso');
  }
  
  gravarApenso(nrProcesso: string, apenso: PaeApensoPost) : Observable<any> {

    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + nrProcesso + '/apenso', JSON.stringify(apenso));
  }

  obterContratoPorNumeroAno(nrContrato: string, anoContrato: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/numero/' + nrContrato + '-' + anoContrato);
  }

  exportarProcesso( id: string, tipo: boolean): Observable<any> {
    return this.http.get (environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id + '/exportar/' + tipo, {responseType: 'blob'})
  }
} //FIM CLASSE