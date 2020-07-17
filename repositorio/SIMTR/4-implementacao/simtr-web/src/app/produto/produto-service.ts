
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ARQUITETURA_SERVICOS } from '../constants/constants';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ProdutoService {

  constructor(private http: HttpClient) {}

  getDossieProduto(id): Observable<any> {
      return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + id);
  }

  
}
