import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { PaeContratacao } from './../../model/pae-contratacao';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';
import { PaeApensoPost } from './../../model/pae-apenso-post';
import { PaeApensoPatch } from './../../model/pae-apenso-patch';

@Injectable()

export class PaeApensosService {
  
  urlBase: string  = environment.serverPath + '/processoadministrativo/v1';
  
  constructor(private http: HttpClient) { }

  obterProcessoPorId( id: string): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id);
  }

  incluirApenso(nrProcesso: string, apenso: PaeApensoPost) : Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + nrProcesso + '/apenso', JSON.stringify(apenso));
  }

  alterarApenso(nrApenso: string, apenso: PaeApensoPatch) : Observable<any> {
    return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/apenso/' + nrApenso, JSON.stringify(apenso));
  }

  obterApensoPorId(idApenso: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/apenso/' + idApenso);
  }
  
  exportarApenso( id: string): Observable<any> {
    return this.http.get (environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/apenso/' + id + '/exportar', {responseType: 'blob'})
  }
} //FIM CLASSE