import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { ARQUITETURA_SERVICOS } from '../../../constants/constants';
import { PaeDocumentoGet } from '../../model/pae-documento-get';
import { PaeDocumentoPatch } from '../../model/pae-documento-patch';

@Injectable()

export class PaeDocsService {
  
  urlBase: string  = environment.serverPath + '/processoadministrativo/v1';
  constructor(private http: HttpClient) { }

  obterProcessoPorId( id: string): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + id);
  }

  obterDocumentoPorId( id: string): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/documento/' + id);
  }

  incluirDocumento(nrProcesso: string, documento: PaeDocumentoGet) : Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/processo/' + nrProcesso + '/documento', JSON.stringify(documento));
  }

  incluirDocumentoContrato(nrContrato: string, documento: PaeDocumentoGet) : Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/' + nrContrato + '/documento', JSON.stringify(documento));
  }

  incluirDocumentoApenso(nrApenso: string, documento: PaeDocumentoGet) : Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/apenso/' + nrApenso + '/documento', JSON.stringify(documento));
  }

  alterarDocumento(idDocumento: string, documento: PaeDocumentoPatch) : Observable<any> {
    return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/documento/' + idDocumento, JSON.stringify(documento));
  }

  downloadDocumento(idDocumento: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/documento/' + idDocumento + '/exportar');
  }


  obterContratoPorId(idContrato: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/contrato/' + idContrato);
  }

  obterApensoPorId(idApenso: string) : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/apenso/' + idApenso);
  }

  obterTiposDocumentos() : Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.cadastro + '/tipo-documento/processo-administrativo');
  }

  deletarDocumentoPorId(idDocumento: string, justificativa: string) : Observable<any> {
      return this.http.delete(environment.serverPath + ARQUITETURA_SERVICOS.processoAdministrativo + '/documento/' + idDocumento + '/' + justificativa);
  }

} //FIM CLASSE