import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { IncludeVinculacao } from '../cadastro/model/include-vinculacao';

@Injectable()
export class VinculacaoService {
    private urlBase: string = environment.serverPath + '/cadastro/v1'

    constructor(private http: HttpClient) { }

    save(includeVinculacao: IncludeVinculacao): Observable<IncludeVinculacao> {
        return this.http.post<IncludeVinculacao>(this.urlBase + "/vinculacao-checklist", includeVinculacao);
    }

    delete(idVinculacao: number): Observable<any> {
        const url: string = `${this.urlBase}/vinculacao-checklist/${idVinculacao}`;
        return this.http.delete(url);
    }

} 