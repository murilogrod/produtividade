import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

import { CrudProduto } from '../model/crud-produto';

@Injectable()
export class CrudProdutoService {
  
  urlBase: string = environment.serverPath + '/cadastro/v1'
  
  constructor(private http: HttpClient) { }

  get(): Observable<any>  {
    return this.http.get(this.urlBase + "/produto");
  }//FIM METODO
  
  save(entidade: CrudProduto): Observable<any> {
    
    return this.http.post(this.urlBase + "/produto", entidade);
  }//FIM METODO
  
  update(id: number, entidade): Observable<any> {
    console.log(entidade)
    return this.http.patch(this.urlBase + "/produto/"+ id, entidade);
  }//FIM METODO
  
  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/produto/" + id);
  }
  
} //FIM CLASSE