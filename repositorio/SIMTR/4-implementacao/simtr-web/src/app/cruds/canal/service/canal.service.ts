
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { IncludeCanal } from '../cadastro/model/include-canal.model';
import { UpdateCanal } from '../cadastro/model/update-canal.model';

@Injectable()
export class CanalService {

  urlBase: string = environment.serverPath + '/cadastro/v1'

  novoCanal: boolean;
  edicaoCanal: boolean;
  id: number;

  constructor(private http: HttpClient) { }

  get(): Observable<any> {
    return this.http.get(this.urlBase + "/canal");
  }//FIM METODO

  getById(id: number): Observable<any> {
    return this.http.get(this.urlBase + "/canal/" + id);
  }//FIM METODO

  save(entidade: IncludeCanal): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.urlBase + "/canal", entidade, { observe: 'response' });
  }//FIM METODO

  update(id: number, entidade: UpdateCanal): Observable<any> {
    return this.http.patch(this.urlBase + "/canal/" + id, entidade);
  }//FIM METODO

  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/canal/" + id);
  }

  public getNovoCanal() {
    return this.novoCanal;
  }

  public setNovoCanal(novoCanal: boolean) {
    this.novoCanal = novoCanal;
  }

  public getEdicaoCanal() {
    return this.edicaoCanal;
  }

  public setEdicaoCanal(edicaoCanal: boolean) {
    this.edicaoCanal = edicaoCanal;
  }

  public getId() {
    return this.id;
  }

  public setId(id: number) {
    this.id = id;
  }

} //FIM CLASSE