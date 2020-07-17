import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CrudComposicaoDocumental } from '../model/crud-composicao-documental';


@Injectable()
export class ComposicaoDocumentalService {
  
  urlBase: string = environment.serverPath + '/dossie-digital/v1/cadastro'
  
  constructor(private http: HttpClient) { }

  get(): Observable<any>  {
    return this.http.get(this.urlBase + "/composicao-documental");
  }//FIM METODO
  
  save(entidade: CrudComposicaoDocumental): Observable<any> {
    
    return this.http.post(this.urlBase + "/composicao-documental", entidade);
  }//FIM METODO
  
  update(entidade: CrudComposicaoDocumental): Observable<any> {
    console.log(entidade)
    return this.http.patch(this.urlBase + "/composicao-documental/alterar", entidade);
  }//FIM METODO
  
  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/composicao-documental/" + id);
  }
  
} //FIM CLASSE