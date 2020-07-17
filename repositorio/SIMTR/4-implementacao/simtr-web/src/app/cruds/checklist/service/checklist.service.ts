import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Checklist } from '../../model/checklist.model';
import { IncludeChecklist } from '../cadastro/model/include-checklist';
import { UpdateChecklist } from '../cadastro/model/update-checklist';
import { GridChecklist } from '../pesquisa/model/grid-checklist.model';


@Injectable()
export class CheckListService {
    private urlBase: string = environment.serverPath + '/cadastro/v1'
    private cloneChecklist: Checklist;
    private novoChecklist: boolean;
    private edicaoChecklist: boolean;
    private id: number;
    private checklists: Array<GridChecklist>;

    constructor(private http: HttpClient) { }

    get(): Observable<any> {
        return this.http.get(this.urlBase + "/checklist");
    }

    getById(id: number): Observable<any> {
        return this.http.get(this.urlBase + "/checklist/" + id);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(this.urlBase + "/checklist/" + id);
    }

    save(includeChecklist: IncludeChecklist): Observable<IncludeChecklist> {
        return this.http.post<IncludeChecklist>(this.urlBase + "/checklist", includeChecklist);
    }

    update(id: number, updateChecklist: UpdateChecklist): Observable<IncludeChecklist> {
        return this.http.patch<IncludeChecklist>(this.urlBase + "/checklist/" + id, updateChecklist);
    }

    getCloneChecklist(): Checklist {
        return this.cloneChecklist;
    }

    setCloneChecklist(cloneChecklist: Checklist) {
        this.cloneChecklist = cloneChecklist;
    }

    getNovoChecklist(): boolean {
        return this.novoChecklist;
    }

    setNovoChecklist(novoChecklist: boolean) {
        this.novoChecklist = novoChecklist;
    }

    getEdicaoChecklist(): boolean {
        return this.edicaoChecklist;
    }

    setEdicaoChecklist(edicaoChecklist: boolean) {
        this.edicaoChecklist = edicaoChecklist;
    }

    getId(): number {
        return this.id;
    }

    setId(id: number) {
        this.id = id;
    }

    getChecklists(): Array<GridChecklist> {
        return this.checklists;
    }

    setChecklists(checklists: Array<GridChecklist>) {
        this.checklists = checklists;
    }

    existChecklists(): boolean {
        return this.getChecklists() !== undefined && this.getChecklists() !== null;
    }

    existCloneCheckList(): boolean {
        return this.getCloneChecklist() !== undefined && this.getCloneChecklist() !== null;
    }
} 