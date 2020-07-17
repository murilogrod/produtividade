import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ARQUITETURA_SERVICOS } from 'src/app/constants/constants';
import { environment } from 'src/environments/environment';

@Injectable()
export class AbaDocumentosService {

    constructor(private http: HttpClient) { }

    obterCartaoAssinatura(cpf: string): Observable<any> {
        const url: string = `${environment.serverPath}${ARQUITETURA_SERVICOS.cartaoAssinatura}/cartao-assinatura/cpf/${cpf}`;
        let requestOptions: Object = {
            responseType: 'text'
        }
        return this.http.get(url, requestOptions);
    }

}
