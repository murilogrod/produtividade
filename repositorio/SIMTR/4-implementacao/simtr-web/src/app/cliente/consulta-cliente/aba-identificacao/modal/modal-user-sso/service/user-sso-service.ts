import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { ARQUITETURA_SERVICOS } from "src/app/constants/constants";
import { Observable } from "rxjs";
import { UserSSO } from "../model/user-sso";

@Injectable()
export class UserSSOService {

    constructor(private http: HttpClient) { }

    enviarDadosUsuarioSSO(userSSO: UserSSO): Observable<any> {
        return this.http.post<UserSSO>(environment.serverPath + ARQUITETURA_SERVICOS.inclusaoUsuarioSSO + "/", userSSO);
    }
}