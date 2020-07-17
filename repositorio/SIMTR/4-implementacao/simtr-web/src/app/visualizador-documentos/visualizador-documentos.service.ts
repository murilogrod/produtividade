import { ApplicationService } from './../services/application/application.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ARQUITETURA_SERVICOS, INTERCEPTOR_SKIP_HEADER } from '../constants/constants';



@Injectable({
  providedIn: 'root'
})
export class VisualizadorDocumentosService {
  
  urlBase: string  = environment.serverPath;
  constructor(private http: HttpClient, private appService: ApplicationService) { }

  postExtracaoExterna(idDoc, cabecalho): Observable<any> {

    const headers = new HttpHeaders().set('Processo', 'CORONAVIRUS');
    
    if (!cabecalho) {
        return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.outsourcing + '/documento/' + idDoc,JSON.stringify(null));
    } else {
        return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.outsourcing + '/documento/' + idDoc,JSON.stringify(null), { headers });
    }
    
  }
 
}
