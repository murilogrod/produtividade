
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CrudFuncaoDocumental } from '../model/crud-funcao-documental';
import { environment } from '../../../environments/environment';

@Injectable()
export class FuncaoDocumentalService {
  
  urlBase: string = environment.serverPath + '/cadastro/v1'
  urlDossie: string = environment.serverPath + '/dossie-digital/v1/cadastro'
  constructor(private http: HttpClient) { }

  get(): Observable<any>  {
      return this.http.get(this.urlBase + "/funcao-documental");
  }//FIM METODO
  
  getById(id:number) {
    return this.http.get(this.urlBase + "/funcao-documental/" + id + "?tipos=true");
  }

  getTipoDocumento(): Observable<any>  {
    return this.http.get(this.urlBase + "/tipo-documento?funcoes=false&atributos=false");
  
  }//FIM METODO

  save(entidade: CrudFuncaoDocumental): Observable<any> {
    
    return this.http.post(this.urlBase + "/funcao-documental/", entidade);
  }//FIM METODO
  
  update(id, entidade: CrudFuncaoDocumental): Observable<any> {
    return this.http.patch(this.urlBase + "/funcao-documental/" + id, entidade);
  }//FIM METODO
  
  delete(id: string): Observable<any> {
    console.log(this.urlBase + "/funcao-documental/" + id )
    return this.http.delete(this.urlBase + "/funcao-documental/" + id);
  }
  
} //FIM CLASSE