import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { PaeContratacaoPatch } from './../../model/pae-contratacao-patch';
import { PaeContratacao } from './../../model/pae-contratacao';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';

@Injectable()

export class PaeProcessoService {
  
  urlBase: string  = environment.serverPath + '/processoadministrativo/v1';
  
  constructor(private http: HttpClient) { }

  atualizarProcesso( processo: PaeContratacaoPatch, id: string ): Observable<any> {
    
    return this.http.patch( environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id, JSON.stringify( processo ) );
  }
  
  incluirProcesso( processo: PaeContratacao ): Observable<any> {
    return this.http.post( environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo', JSON.stringify( processo ) );
  }

} //FIM CLASSE