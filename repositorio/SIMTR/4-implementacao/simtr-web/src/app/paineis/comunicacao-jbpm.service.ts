import { GridComunicacaoJBPM } from './model/grid-comunicacao-jbpm.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';
import { ARQUITETURA_SERVICOS } from '../constants/constants';
@Injectable()
export class ComunicacaoJBPMService {

  private falhaJBPMs: Array<GridComunicacaoJBPM>;

  constructor(private http: HttpClient) { }

  getConsultaFalhaBPM(): Observable<any> {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.administracao + '/dossie-produto/falha-bpm');
  }

  public getFalhaJBPMs(): Array<GridComunicacaoJBPM> {
    return this.falhaJBPMs;
  }

  public setFalhaJBPMs(falhaJBPMs: Array<GridComunicacaoJBPM>) {
    this.falhaJBPMs = falhaJBPMs;
  }
 
}
