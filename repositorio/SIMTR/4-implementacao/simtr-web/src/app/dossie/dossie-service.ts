import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ARQUITETURA_SERVICOS, LOCAL_STORAGE_CONSTANTS, UNDESCOR } from '../constants/constants';
import { Observable } from 'rxjs/Observable';
import { DossieProduto, DocumentoGED } from '../model';
import { DossieProdutoModel } from './manutencao-dossie/model-endPoint-dossie-produto/dossieProdutoModel';
import { DossieProdutoModelPath } from './manutencao-dossie/model-endPoint-dossie-produto/dossieProdutoModelPath';
import { ApplicationService, LoaderService } from '../services';
import { Router } from '@angular/router';


@Injectable()
export class DossieService {

	constructor(
		private http: HttpClient,
		private appService: ApplicationService,
		private loadService: LoaderService,
		private router: Router
	) {

	}

	getClienteProcesso(idCliente: number, idProcesso: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/buscaDossieCliente/' +
			idCliente + '/processo/' + idProcesso);
	}

	getProcessoByMacroprocesso(id: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processo + '/getByMacroprocesso/' + id);
	}

	getOperacaoByProcesso(id: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.operacao + '/getProdutoByProcesso/' + id);
	}

	insertDossieProduto(newProduto: DossieProduto): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/', JSON.stringify(newProduto));
	}

	insertDossieProdutoModel(newProduto: DossieProdutoModel): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/', JSON.stringify(newProduto));
	}

	atualizaDossieProduto(idDossieProduto: number, dossieProduto: DossieProdutoModelPath) {
		return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + idDossieProduto + '/', JSON.stringify(dossieProduto));
	}

	atualizCadastroCaixaDossieCliente(idDossieCliente: number) {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/' + idDossieCliente + '/cadastro-caixa', {});
	}

	pathRetornaDossieProduto(idDossieProduto: number, retorno: any) {
		return this.http.patch(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + idDossieProduto + '/', JSON.stringify(retorno));
	}

	updateSituacaoDossie(newProduto: DossieProduto, unidade, matricula): Observable<any> {
		return this.http.put(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/updateSituacaoDossie/unidade/' + unidade + '/matricula/' + matricula, JSON.stringify(newProduto));
	}

	getDocumentoByClienteProcesso(id: number, idCliente: number, tipoCliente: string, icTipo, idDossie: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.funcaoDocumental + '/getByProcessoCliente/' + id +
			'/cliente/' + idCliente + '/tipoCliente/' + tipoCliente + '/icTipo/' + icTipo + '/dossie/' + idDossie);
	}

	getDocumentoByProdutoProcesso(id: number, idCliente: number, icTipo, idDossie: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.funcaoDocumental + '/getByProcessoProduto/' + id +
			'/cliente/' + idCliente + '/icTipo/' + icTipo + '/dossie/' + idDossie);
	}

	insertDocumento(listaDocumentoGedDTO: DocumentoGED[], dossieProduto: number, matricula, unidade): Observable<any> {
		return this.http.put(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/inserir/' + JSON.stringify(dossieProduto) + "/matricula/" + matricula + "/unidade/" + unidade + "/lista/", JSON.stringify(listaDocumentoGedDTO));
	}

	insertDocumentoCliente(newDocumento: DocumentoGED, idCliente: number): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + "/" + JSON.stringify(idCliente) + "/documento", JSON.stringify(newDocumento));
	}

	getDossieTratamento(situacao: string, processo: number, etapa: number, unidade, matricula): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieCliente + '/buscaMaisAntigoPorSituacao/' +
			situacao + '/processo/' + processo + '/etapa/' + etapa + '/unidade/' + unidade + "/matricula/" + matricula);
	}

	updateSituacaoDossieTratamento(idDossie): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + idDossie + '/retornafilatratamento', JSON.stringify(null));
	}

	updateSituacaoDossieProduto(idDossie): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + idDossie, JSON.stringify(null));
	}

	getDossieByCliente(idCliente: number, idDossie: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/getDossieProduto/' + idCliente + "/dossie/" + idDossie);
	}

	getMotivoRejeicao(): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.situacaodocumento + '/getMotivoRejeicao');
	}

	getProcessos(): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processo + '/patriarca');
	}

	getProcesso(processoId: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.processo + '/' + processoId);
	}

	getDossieProduto(id): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + id);
	}

	postDossieProduto(id): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + id, null);
	}

	buscarProcessosAguardandoComplementacao(): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + "/complementacao/processo")
	}

	postDossieComplementacao(id: number): Observable<any> {
		return this.http.post(environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + "/complementacao/processo/" + id, null)
	}

	getTipologia(): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.documento + '/tipologia');
	}

	getConsultarImagemGet(id: number): Observable<any> {
		return this.http.get(environment.serverPath + ARQUITETURA_SERVICOS.documento + '/' + id)
			.catch((error: any) => Observable.throw(error.json().error || 'Error'));
	}

	buscarPatriarcar(userUrl: string) {
		this.loadService.show();
		this.getProcessos().subscribe(response => {
			this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoPatriarca);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoPatriarca, JSON.stringify(response.processos));
			this.criarInstanciaLocalStoreMacroProcesso(response);
			this.buscarTipologia();
			this.loadService.hide();
			if (userUrl) {
				this.router.navigate([userUrl]);
				return;
			}
			this.router.navigate(['dashboard']);
		},
			() => {
				this.loadService.hide();
			});
	}

	/**
	 * Adiciona o resultado do ENDPOINT Patriarca no LocalStorage
	 * @param response resultado do endPoint patriarca
	 */
	private criarInstanciaLocalStoreMacroProcesso(response: any) {
		response.processos.forEach(macroProcesso => {
			this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.macroProccesso + UNDESCOR + macroProcesso.id);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.macroProccesso + UNDESCOR + macroProcesso.id, JSON.stringify(macroProcesso));
			this.criarInstanciaLocalStoreDossie(macroProcesso);
		});
	}
	/**
	 * Percorrer e define o LocalStorage para Cada Dossiê
	 * @param macroProcesso 
	 */
	private criarInstanciaLocalStoreDossie(macroProcesso: any) {
		macroProcesso.processos_filho.forEach(dossie => {
			this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + dossie.id);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + dossie.id, JSON.stringify(dossie));
		});
	}

	buscarTipologia() {
		this.getTipologia().subscribe(response => {
			this.appService.removeInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia);
			this.appService.saveInLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia, JSON.stringify(response));
		}, () => {
			this.loadService.hide();
		});
	}

	buscarSocios(cnpj: String): Observable<any> {
		let corpo = { servico: ARQUITETURA_SERVICOS.endereco_consulta_cnpj_receita_federal.replace("{cnpj}", String(cnpj)), verbo: "GET" };
		return this.http.post(`${environment.serverPath}${ARQUITETURA_SERVICOS.API_MANAGER}`, corpo);
	}
}
