import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { IncludeApontamento } from '../cadastro/model/include-apontamento';
import { Observable } from 'rxjs';
import { UpdateApontamento } from '../cadastro/model/update-apontamento';

@Injectable()
export class ApontamentoService {
    private urlBase: string = environment.serverPath + '/cadastro/v1'

    constructor(private http: HttpClient) { }

    save(idChecklist: number, includeApontamento: IncludeApontamento): Observable<IncludeApontamento> {
        const url: string = `${this.urlBase}/checklist/${idChecklist}/apontamento`;
        return this.http.post<IncludeApontamento>(url, includeApontamento);
    }

    update(idChecklist: number, idApontamento: number, updateApontamento: UpdateApontamento): Observable<UpdateApontamento> {
        const url: string = `${this.urlBase}/checklist/${idChecklist}/apontamento/${idApontamento}`;
        return this.http.patch<IncludeApontamento>(url, updateApontamento);
    }

    delete(idChecklist: number, idApontamento: number): Observable<any> {
        const url: string = `${this.urlBase}/checklist/${idChecklist}/apontamento/${idApontamento}`;
        return this.http.delete(url);
    }

} 