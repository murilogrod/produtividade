import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private data: any = [];
  private booleanValue;

  constructor() { }

  setData(id, objeto) {
    this.data[id] = objeto;
  }

  getData(id) {
    let retorno = this.data[id];
    this.data = [];
    return retorno;
  }

  getBooleanValue() {
    return this.booleanValue;
  }

  setBooleanValue(booleanValue: boolean) {
    this.booleanValue = booleanValue;
  }
}