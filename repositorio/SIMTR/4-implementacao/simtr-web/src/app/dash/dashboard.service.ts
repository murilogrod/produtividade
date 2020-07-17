import { Injectable } from '@angular/core';
import { ARQUITETURA_SERVICOS, PROPERTY } from '../constants/constants';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { UnidadeAnalisada } from './model/unidade-analisada.model';
@Injectable()
export class DashboardService {

  constructor(private http: HttpClient) { }

  getDados(): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dashboard + '/pessoal/visao-unidade');
  }

  getCustomDados(unidadeAnalisada: UnidadeAnalisada): Observable<any> {
    const params = new HttpParams().set(PROPERTY.CGC, String(unidadeAnalisada.unidade)).set(PROPERTY.VINCULADAS, String(unidadeAnalisada.vinculadas));
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dashboard + '/pessoal/visao-unidade/', { params: params });
  }
}
