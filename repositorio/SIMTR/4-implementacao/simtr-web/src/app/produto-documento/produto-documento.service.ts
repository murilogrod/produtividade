import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { CrudProdutoDocumento } from '../model/crud-produto-documento';
import { environment } from '../../environments/environment';

@Injectable()

export class ProdutoDocumentoService {
  
  urlBase: string = environment.serverPath;
  
  constructor(private http: Http) { }

  get(): Observable<any>  {
    return this.http.get(this.urlBase + "/produto-documento");
  
  }//FIM METODO
  
  getProduto(): Observable<any>  {
    return this.http.get(this.urlBase + "/produto");
  
  }//FIM METODO

  getProcesso(): Observable<any>  {
    return this.http.get(this.urlBase + "/processo");
  
  }//FIM METODO

  getTipoDocumento(): Observable<any>  {
    return this.http.get(this.urlBase + "/combo-tipo-documento");
  
  }//FIM

  save(entidade: CrudProdutoDocumento): Observable<any> {
    
    return this.http.post(this.urlBase + "/produto-documento", entidade);
  }//FIM METODO
  
  update(entidade: CrudProdutoDocumento): Observable<any> {
    
    return this.http.put(this.urlBase + "/produto-documento", entidade);
  }//FIM METODO
  
  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/produto-documento/" + id);
  }
  
} //FIM CLASSE