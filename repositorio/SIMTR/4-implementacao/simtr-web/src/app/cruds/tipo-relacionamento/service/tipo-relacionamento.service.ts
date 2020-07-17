import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { environment } from '../../../../environments/environment';
import { Observable } from "rxjs";
import { IncludeTipoRelacionamento } from "../cadastro/model/include-tipo-relacionamento.model";
import { UpdateTipoRelacionamento } from "../cadastro/model/update-tipo-relacionamento.model";
import { GridTipoRelacionamento } from "../pesquisa/model/grid-tipo-relacionamento";

@Injectable()
export class TipoRelacionamentoService {

    private urlBase: string = environment.serverPath + '/cadastro/v1'
    private novoTipoRelacionamento: boolean;
    private edicaoTipoRelacionamento: boolean;
    private id: number;
    private tiposRelacionamentos: Array<GridTipoRelacionamento>;

    constructor(private http: HttpClient) { }

    get(): Observable<any> {
        return this.http.get(this.urlBase + "/tipo-relacionamento");
    }

    getById(id: number): Observable<any> {
        return this.http.get(this.urlBase + "/tipo-relacionamento/" + id);
    }

    delete(id: string): Observable<any> {
        return this.http.delete(this.urlBase + "/tipo-relacionamento/" + id);
    }

    save(includeTipoRelacionamento: IncludeTipoRelacionamento): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.urlBase + "/tipo-relacionamento", includeTipoRelacionamento, { observe: 'response' });
    }

    update(id: number, updateTipoRelacionamento: UpdateTipoRelacionamento): Observable<any> {
        return this.http.patch(this.urlBase + "/tipo-relacionamento/" + id, updateTipoRelacionamento);
    }

    public getNovoTipoRelacionamento() {
        return this.novoTipoRelacionamento;
    }

    public setNovoTipoRelacionamento(novoTipoRelacionamento: boolean) {
        this.novoTipoRelacionamento = novoTipoRelacionamento;
    }

    public getEdicaoTipoRelacionamento() {
        return this.edicaoTipoRelacionamento;
    }

    public setEdicaoTipoRelacionamento(edicaoTipoRelacionamento: boolean) {
        this.edicaoTipoRelacionamento = edicaoTipoRelacionamento;
    }

    public getId() {
        return this.id;
    }

    public setId(id: number) {
        this.id = id;
    }

    public getTiposRelacionamentos() {
        return this.tiposRelacionamentos;
    }

    public setTiposRelacionamentos(tiposRelacionamentos: Array<GridTipoRelacionamento>) {
        this.tiposRelacionamentos = tiposRelacionamentos;
    }

    existTiposRelacionamentos(): boolean {
        return this.getTiposRelacionamentos() !== undefined && this.getTiposRelacionamentos() !== null;
    }

}