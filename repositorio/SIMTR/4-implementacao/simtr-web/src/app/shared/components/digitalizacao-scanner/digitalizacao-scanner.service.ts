import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from '../../../../../node_modules/rxjs';

@Injectable({
  providedIn: 'root'
})
export class DigitalizacaoScannerService {

  constructor(private http : Http) { }

  getDadosScanner(urlScanner : string) : Observable<any>{
    
    return this.http.get(urlScanner);  
     
  }
}
