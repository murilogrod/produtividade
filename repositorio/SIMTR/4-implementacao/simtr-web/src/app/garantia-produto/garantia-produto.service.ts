import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { CrudGarantiaProduto } from '../model/crud-garantia-produto';
import { environment } from '../../environments/environment';

@Injectable()

export class GarantiaProdutoService {
  
  urlBase: string = environment.serverPath;
  
  constructor(private http: Http) { }

  get(): Observable<any>  {
    return this.http.get(this.urlBase + "/garantia-produto");
  
  }//FIM METODO
  
  getProduto(): Observable<any>  {
    return this.http.get(this.urlBase + "/combo-produto");
  
  }//FIM METODO

  getGarantia(): Observable<any>  {
    return this.http.get(this.urlBase + "/combo-garantia");
  
  }//FIM METODO


  save(entidade: CrudGarantiaProduto): Observable<any> {
    
    return this.http.post(this.urlBase + "/garantia-produto", entidade);
  }//FIM METODO
  
  delete(nu_produto: string, nu_garantia: string ): Observable<any> {
    return this.http.delete(this.urlBase + "/garantia-produto/" + nu_garantia + "/" + nu_produto);
  }
  
} //FIM CLASSE