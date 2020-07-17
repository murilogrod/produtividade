import { Injectable, EventEmitter, Output } from '@angular/core';


/**
 * Responsável por armazenar a variavél global na qual irá informar 
 * se as mudanças nos campos da página vigente foram salvas. 
 */

@Injectable({
  providedIn: 'root'
})
export class MudancaSalvaService {

  private isMudancaSalva: boolean;
  private limitMaxAdministrarDossie: number;
  private garantias: Array<any> = new Array<any>();

  @Output() loadDossieProdutoAdmnistrarDossie: EventEmitter<number> = new EventEmitter<number>();
  @Output() loading: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() {
  }

  public getIsMudancaSalva() {
    return this.isMudancaSalva;
  }

  public setIsMudancaSalva(isMudancaSalva: boolean) {
    this.isMudancaSalva = isMudancaSalva;
  }

  public getLimitMaxAdministrarDossie() {
    return this.limitMaxAdministrarDossie;
  }

  public setLimitMaxAdministrarDossie(limitMaxAdministrarDossie: number) {
    this.limitMaxAdministrarDossie = limitMaxAdministrarDossie;
  }

  public getGarantias() {
    return this.garantias;
  }

  public setGarantias(garantias: Array<any>) {
    this.garantias = garantias;
  }
}
