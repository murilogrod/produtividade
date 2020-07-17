
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { GridTipoDocumento } from '../model/grid-tipo-documento.model';
import { IncludeTipoDocumento } from '../cadastro/model/include-tipo-documento';
import { UpdateTipoDocumento } from '../cadastro/model/update-tipo-documento';
import { IncludeAtributoExtracao } from '../cadastro/model/include-atributo-extracao';
import { UpdateAtributoExtracao } from '../cadastro/model/update-atributo-extracao';
import { IncludeOpcaoAtributoExtracao } from '../cadastro/model/include-opcao-atributo-extracao';

@Injectable()
export class TipoDocumentoService {

  urlBase: string = environment.serverPath + '/cadastro/v1'

  private novoTipoDocumento: boolean;
  private edicaoTipoDocumento: boolean;
  private tipoDocumentoSemAlteracao: boolean;
  private id: number;
  private tipoDocumentos: Array<GridTipoDocumento>;

  constructor(private http: HttpClient) { }

  get(): Observable<any> {
    return this.http.get(this.urlBase + "/tipo-documento");
  }

  getById(id: number): Observable<any> {
    return this.http.get(this.urlBase + "/tipo-documento/" + id);
  }

  save(entidade: IncludeTipoDocumento): Observable<any> {
    return this.http.post(this.urlBase + "/tipo-documento", entidade);
  }

  consultarDadosDeclarados(tipoPessoa: string): Observable<any> {
    return this.http.get(this.urlBase + "/tipo-documento/dados-declarados/" + tipoPessoa);
  }

  saveAtributo(idTipoDoc: number, atributo: IncludeAtributoExtracao): Observable<any> {
    return this.http.post(this.urlBase + "/tipo-documento/" + idTipoDoc + '/atributo-extracao', atributo);
  }

  saveOpcao(idTipoDoc: number, idAtributo: number, opcao: IncludeOpcaoAtributoExtracao): Observable<any> {
    return this.http.post(this.urlBase + "/tipo-documento/" + idTipoDoc + '/atributo-extracao/' + idAtributo + '/opcao', opcao);
  }

  deleteOpcao(idTipoDoc, idAtributo, idOpcao): Observable<any> {
    return this.http.delete(this.urlBase + "/tipo-documento/" + idTipoDoc + '/atributo-extracao/' + idAtributo + '/opcao/' + idOpcao);
  }

  updateAtributo(idTipoDoc: number, idAtributo: number, atributo: UpdateAtributoExtracao): Observable<any> {
    return this.http.patch(this.urlBase + "/tipo-documento/" + idTipoDoc + '/atributo-extracao/' + idAtributo, atributo);
  }

  update(idDoc, entidade: UpdateTipoDocumento): Observable<any> {
    return this.http.patch(this.urlBase + "/tipo-documento/" + idDoc, entidade);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(this.urlBase + "/tipo-documento/" + id);
  }

  deleteAtributo(idTipoDoc: number, idAtributo: number): Observable<any> {
    return this.http.delete(this.urlBase + "/tipo-documento/" + idTipoDoc + '/atributo-extracao/' + idAtributo);
  }

  public getNovoTipoDocumento() {
    return this.novoTipoDocumento;
  }

  public setNovoTipoDocumento(novoTipoDocumento: boolean) {
    this.novoTipoDocumento = novoTipoDocumento;
  }

  public getEdicaoTipoDocumento() {
    return this.edicaoTipoDocumento;
  }

  public setEdicaoTipoDocumento(edicaoTipoDocumento: boolean) {
    this.edicaoTipoDocumento = edicaoTipoDocumento;
  }

  public getId() {
    return this.id;
  }

  public setId(id: number) {
    this.id = id;
  }

  public getTipoDocumentos(): Array<GridTipoDocumento> {
    return this.tipoDocumentos;
  }

  public setTipoDocumentos(tipoDocumentos: Array<GridTipoDocumento>) {
    this.tipoDocumentos = tipoDocumentos;
  }

  public getTipoDocumentoSemAlteracao(): boolean {
    return this.tipoDocumentoSemAlteracao;
  }

  public setTipoDocumentoSemAlteracao(tipoDocumentoSemAlteracao: boolean) {
    this.tipoDocumentoSemAlteracao = tipoDocumentoSemAlteracao;
  }

  public existTipoDocumentos(): boolean {
    return this.getTipoDocumentos() !== undefined && this.getTipoDocumentos() !== null;
  }

}