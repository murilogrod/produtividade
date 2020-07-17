import { ARQUITETURA_SERVICOS } from '../constants/constants';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Verificacao } from '../model/model-tratamento/verificacao.model';

@Injectable()
export class TratamentoService {
  constructor(private http: HttpClient) {}

  getProcesso() {
    return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie + '/processo');
  }
  /**
   * Consulta Dossie em Tratamento
   * @param idProcesso 
   */
  capturaDossieTratamento(idProcesso: string){
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie + '/processo/' + idProcesso, JSON.stringify({}));
  }

  /**
   * Cancela o Dossie em tratamento
   */
  cancelaDossieTratamento(idProcesso: string){
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie  + '/dossie-produto/'+ idProcesso + '/retorna-fila', JSON.stringify({}));
  }

  executaTratamentoDossie(idDossieProduto: number, verificacao: Verificacao[]){
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie + '/dossie-produto/' + idDossieProduto, JSON.stringify(verificacao));
  }

  selecionaDossieProduto(idDossieProduto: string){
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie + '/seletivo/dossie-produto/' + idDossieProduto, JSON.stringify({}));
  }

  renovarTempoSessao(idDossieProduto: string){
    return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.tratamentoDossie + '/dossie-produto/' + idDossieProduto + '/renovar-tempo', JSON.stringify({}));
  }

}
