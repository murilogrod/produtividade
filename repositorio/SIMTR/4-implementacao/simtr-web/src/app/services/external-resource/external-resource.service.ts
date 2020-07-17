import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { PROPERTY, EXTERNAL_RESOURCE, INTERCEPTOR_SKIP_HEADER } from "src/app/constants/constants";


@Injectable()
export class ExternalResourceService {

    apikey: string = PROPERTY.APIKEY;

    httpOptions = {
        headers: new HttpHeaders({
            apikey: String(environment.apikey),
            responseType: 'json'
        }).set(INTERCEPTOR_SKIP_HEADER, '').set("Content-Type", "application/json").set("Access-Control-Allow-Origin", "*")
            .set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE")
            .set("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
    };

    constructor(private http: HttpClient) { }

    searchAddress(cep: string): Observable<any> {
        const url: string = environment.urlApiManager + EXTERNAL_RESOURCE.searchAddress + cep;
        return this.http.get(url, this.httpOptions);
    }

    callPhoto(url : string, matricula: string) {
        return this.http.get(environment.serverPath + url + matricula);
    }

}
