import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ARQUITETURA_SERVICOS } from '../constants/constants';
import { environment } from '../../environments/environment';

@Injectable()
export class DocumentoService {
  urlBase: string = environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente;

  constructor (private http: HttpClient) {

  }

  getDocumentById(id: number): Observable<any> {
    return this.http.get(`${environment.serverPath}${ARQUITETURA_SERVICOS.documento}/${id}`);
  }

  verificaAssinaturaDigital(documentoBase64: any){
    console.log(environment.serverPath+ARQUITETURA_SERVICOS.documento+"/assinatura");
    return this.http.post(`${environment.serverPath}${ARQUITETURA_SERVICOS.documento}/assinatura`, documentoBase64);
  }

  insertDocument(id: number, document: any): Observable<any> {
    return this.http.post(this.urlBase + "/" + id + "/documento", JSON.stringify(document));
  }

  deleteDocumento(idCliente: number, idDocumento: number): Observable<any> {
    return this.http.delete(`${environment.serverPath}${ARQUITETURA_SERVICOS.dossieCliente}/${idCliente}/documento/${idDocumento}`);
  }

}
