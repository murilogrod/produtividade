import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { PaeContratacao } from './../../model/pae-contratacao';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';
import { PaeContratoPost } from './../../model/pae-contrato-post';
import { PaeContratoPatch } from './../../model/pae-contrato-patch';

@Injectable()

export class PaeContratosService {
  
  urlBase: string  = environment.serverPath + '/processoadministrativo/v1';
  
  constructor(private http: HttpClient) { }

  obterProcessoPorId( id: string): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id);
  }

  incluirContrato(nrProcesso: string, contrato: PaeContratoPost) : Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + nrProcesso + '/contrato', JSON.stringify(contrato));
  }

  alterarContrato(nrContrato: string, contrato: PaeContratoPatch) : Observable<any> {
    return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/' + nrContrato, JSON.stringify(contrato));
  }

  obterContratoPorId(idContrato: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/' + idContrato);
  }

  exportarContrato( id: string, tipo: boolean): Observable<any> {
    return this.http.get (environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/' + id + '/exportar/' + tipo, {responseType: 'blob'})
  }
} //FIM CLASSE