import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { MacroProcesso } from '../model/macro-processo';
import { environment } from '../../environments/environment';

@Injectable()
export class MacroProcessoService {
  
  urlBase: string = environment.serverPath
  
  constructor(private http: Http) { }

  getMacroProcessos(): Observable<any>  {
    return this.http.get(this.urlBase + "/macroprocesso");
  
  }//FIM METODO
  
  saveMacroProcessos(macroProcesso: MacroProcesso): Observable<any> {
    
    return this.http.post(this.urlBase + "/macroprocesso", macroProcesso);
  }//FIM METODO
  
  updateMacroProcessos(macroProcesso: MacroProcesso): Observable<any> {
    
    return this.http.put(this.urlBase + "/macroprocesso", macroProcesso);
  }//FIM METODO
  
  deleteMacroProcesso(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/macroprocesso/" + id);
  }
  
} //FIM CLASSE