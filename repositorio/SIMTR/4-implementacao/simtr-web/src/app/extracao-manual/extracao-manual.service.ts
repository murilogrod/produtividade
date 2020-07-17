import { ApplicationService } from './../services/application/application.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ARQUITETURA_SERVICOS } from '../constants/constants';

import { DocumentoPatch } from './../compartilhados/componentes/formulario-extracao/model/documento-patch';

@Injectable({
  providedIn: 'root'
})
export class ExtracaoManualService {

  urlBase: string  = environment.serverPath;
  constructor(private http: HttpClient, private appService: ApplicationService) { }

  obterPendentes( ): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.extracaoDados + '/tipo-documento/pendente');
  }
  
  obterDocumentoFila(idTipoDoc: number): Observable<any> {
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.extracaoDados + '/tipo-documento/' + idTipoDoc,JSON.stringify(idTipoDoc));
  }

  patchDocumento(documento: DocumentoPatch) {
    return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.extracaoDados + '/resultado', JSON.stringify(documento));
  }
}
