
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { ARQUITETURA_SERVICOS, PROPERTY } from '../../../constants/constants';
import { Observable } from 'rxjs/Observable';
import { Utils } from '../../../utils/Utils';
import { VinculoCliente } from '../../../model';

@Injectable()
export class ConsultaClienteService {
	constructor(private http: HttpClient) { }

	getCliente(cpfCnpj: string, cpf: boolean): Observable<any> {
		if (cpf) {
			cpfCnpj = Utils.leftPad(cpfCnpj, 11, '0');
			return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/cpf/' + cpfCnpj);
		} else {
			cpfCnpj = Utils.leftPad(cpfCnpj, 14, '0');
			return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/cnpj/' + cpfCnpj);
		}
	}

	getClienteById(idCliente: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/' + idCliente);
	}

	getDadosDeclaradosCliente(idCliente: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/' + idCliente + '/dados-declarados');
	}

	getDocumentoByCliente(idCliente: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.funcaoDocumental + '/getByCliente/' + idCliente);
	}

	getTipoDocumentoByTipoPessoa(icTipo: string): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.tipoDocumento + '/getTipoDocumentoByTipoPessoa/' + icTipo);
	}

	getFuncaoDocumentalByTipoDocumento(idTipoDocumento: string): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.funcaoDocumental + "/" + idTipoDocumento);
	}

	getFuncaoDocumentalByTipoPessoa(): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.documento + "/tipologia");
	}

	insertCliente(cliente: VinculoCliente): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + "/", JSON.stringify(cliente));
	}

	updateCliente(id: number, cliente: VinculoCliente): Observable<any> {
		return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + "/" + id, JSON.stringify(cliente));
	}

	excluiDocumento(idCliente, idDocumento): Observable<any> {
		return this.http.delete(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/' + idCliente + '/documento/' + idDocumento);
	}

	getClienteSicliUnico(cpfCnpj, cpf: boolean): Observable<any> {
		if (cpf) {
			cpfCnpj = Utils.leftPad(cpfCnpj, 11, '0');
			return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/cpf/' + cpfCnpj + '/sicli');
		} else {
			cpfCnpj = Utils.leftPad(cpfCnpj, 14, '0');
			return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/cnpj/' + cpfCnpj + '/sicli');
		}
	}

	getClienteSicpf(cpf): Observable<any> {
		cpf = Utils.leftPad(cpf, 11, '0');
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/cpf/' + cpf + 'sicpf');
	}

	userSSOIsPresent(user: string): Observable<any> {
		const param = new HttpParams().set(PROPERTY.USER, String(user));
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.consultaUsuarioSSO + '/consultarusuario/', { params: param });
	}

}
