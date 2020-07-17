import { Injectable, Output, EventEmitter } from "@angular/core";
import { CheckListVisualizacao } from "../model/check-list-visualizacao.model";
import { TIPO_ARVORE_DOCUMENTO, LOCAL_STORAGE_CONSTANTS, MSG_TRATAMENTO, TIPO_MSG_TRATAMENTO } from "src/app/constants/constants";
import { Checklist } from "src/app/model/checklist";
import * as moment from 'moment';
import { ArvoreGenericaComponent } from "src/app/documento/arvore-generica/arvore-generica.component";
import { ApplicationService } from "src/app/services";
import { Msg_checklist } from "../model/msg-checklist";
import { UtilsArvore } from "src/app/documento/arvore-generica/UtilsArvore";
import { Utils } from "src/app/utils/Utils";
import { BehaviorSubject } from "rxjs";
import { AbrirDocumentoPopupDefaultService } from "src/app/services/abrir-documento-popup-default.service";

@Injectable()
export class CheckListComponentPresenter {
	checkListVisualizacao: CheckListVisualizacao;
	
	constructor(private abrirDocumentoPopupDefaultService: AbrirDocumentoPopupDefaultService){}

	/**
	 * Inicia carregamento de checklists prévios, se houver, e checklists de fase
	 */
	public checaExisteCheckListFase(dossieProduto: any) {
		let existResposta: boolean = false;
		if (this.checkListVisualizacao.idCheckListAtivado && this.checkListVisualizacao.listDocumentosImagens.length == 0) {
			this.checkListVisualizacao.apontamentos = [];
			existResposta = this.priorizaVisualizacaoCheckListsFasePrevios();
			this.verificarExisteCheckListVerificado(dossieProduto);			
			let existRespostaAcarregar: boolean = this.existeRespostaParaCarregar(this.checkListVisualizacao);
			if ((existResposta && !this.checkListVisualizacao.exibindoChecklistsPrevios) || !existRespostaAcarregar) {
				return this.carregaCheckListsFase();
			}
			!existResposta ? this.carregaCheckListsFase() : "";
			this.recarregaCheckListsFaseJaRespondidos();
			existResposta = true;
		}
		return existResposta;
	}

	private verificarExisteCheckListVerificado(dossieProduto: any) {
		if ((this.checkListVisualizacao && (this.checkListVisualizacao.idCheckListAtivado  || this.checkListVisualizacao.checkEmFoco) )&& dossieProduto.verificacoes && dossieProduto.verificacoes.length > 0) {
			if(Object.keys(this.checkListVisualizacao.checkEmFoco).length == 0) {
				this.checkListVisualizacao.existeApontamentoAnterior = dossieProduto.verificacoes.some(check => this.checkListVisualizacao.idCheckListAtivado.id == check.checklist.identificador_checklist);
				return
			}

			if(Object.keys(this.checkListVisualizacao.checkEmFoco).length != 0) {
				this.checkListVisualizacao.existeApontamentoAnterior = dossieProduto.verificacoes.some(check => this.checkListVisualizacao.checkEmFoco.idcheck == check.checklist.identificador_checklist);
				return
			}
		}
	}

	/**
	 * Verfica estrutura do processo fase atual e procura por um checklist de fase prévio para ser carregado.
	 * @param checkListVisualizacao ojeto trafegado entre o componente e o presenter, que contém atributos comuns
	 * entre essas classes.
	 */
	private checkaExisteCheckListsFasePrevios() {
		return this.checkListVisualizacao.processoFaseAtual.checklists
			.filter(checklist => this.checkListVisualizacao.idCheckListAtivado.id == checklist.id
				&& this.checkListVisualizacao.idCheckListAtivado.data == checklist.data_revogacao
				&& checklist.verificacao_previa
				&& undefined == checklist.funcao_documental
				&& undefined == checklist.tipo_documento);
	}

	/**
	 * Verifica se existe checklist da fase não prévios para ser carregado no tratamento do dossie
	 */
	private checkaExisteCheckListsFase() {
		return this.checkListVisualizacao.processoFaseAtual.checklists
			.filter(checklist => this.checkListVisualizacao.idCheckListAtivado.id == checklist.id
				&& this.checkListVisualizacao.idCheckListAtivado.data == checklist.data_revogacao
				&& !checklist.verificacao_previa
				&& !checklist.tipo_documento
				&& !checklist.funcao_documental);
	}

	/**
	 * Verifica se existe respostas anteriormente ja preenchidas para o documento atual visualizado
	 * @param checkListVisualizacao ojeto trafegado entre o componente e o presenter, que contém atributos comuns
	 * entre essas classes.
	 */
	private existeRespostaParaCarregar(checkListVisualizacao: CheckListVisualizacao) {
		let existe = false;
		for (let check of checkListVisualizacao.listaChekList) {
			if (check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST &&
				check.idcheck == checkListVisualizacao.idCheckListAtivado.id &&
				check.data_revogacao == checkListVisualizacao.idCheckListAtivado.data &&
				(check.listaResposta && check.listaResposta.length > 0)) {
				existe = true;
				break;
			}
		}
		return existe;
	}

	/**
	 * Checka se existe checklists de fase não prévios, caso sim, carrega os na tela
	 */
	private carregaCheckListsFase() {
		let existeCheckListFase: boolean = false;
		let listaCheckListsFase: any[] = this.checkaExisteCheckListsFase();
		for (let checkListFase of listaCheckListsFase) {
			this.checkListVisualizacao.nomeChecklistTitle = checkListFase.nome;
			existeCheckListFase = true;
			this.checkListVisualizacao.habilitarDesabilitarTratamento = !checkListFase.habilitaVerificacao;
			this.populaApontamentosDataTableView(checkListFase.apontamentos);
			this.inicializarListaJustificativaEApontamentosCheckados();
		}
		return existeCheckListFase;
	}

	/**
	 * Renderiza os apontamentos na tela
	 * @param apontamentos apontamentos prontos para serem carregados na tela
	 */
	public populaApontamentosDataTableView(apontamentos: any[]) {
		for (let apontamento of apontamentos) {
			this.checkListVisualizacao.apontamentos.push(apontamento);
		}
	}

	/**
	 * Inicializa as litas de resposta de justificativa de apontamentos e
	 * a lista de resposta de apontamentos respondidos;
	 */
	private inicializarListaJustificativaEApontamentosCheckados() {
		if (this.checkListVisualizacao.apontamentos && this.checkListVisualizacao.apontamentos.length > 0) {
			this.checkListVisualizacao.listaJustificativaApontamentos = Array.apply(null, Array(this.checkListVisualizacao.apontamentos.length)).map(function () { return undefined; });
			this.checkListVisualizacao.listApontamentosCheckados = Array.apply(null, Array(this.checkListVisualizacao.apontamentos.length)).map(function () { return undefined; });
		}
	}

	/**
	 * Procura por checklists prévios, caso achar, monta os seu apontamentos na tela primeiro.
	 */
	private priorizaVisualizacaoCheckListsFasePrevios(): boolean {
		let listaChecklistPrevios: any[] = this.checkaExisteCheckListsFasePrevios();
		this.checkListVisualizacao.existeCheckListPrevio = false;
		let retorno = false;
		for (let checkListPrevio of listaChecklistPrevios) {
			this.checkListVisualizacao.nomeChecklistTitle = checkListPrevio.nome;
			this.checkListVisualizacao.orientacaoChecklist = checkListPrevio.orientacao_operador;
			let temCheckListPrevio = this.checkListVisualizacao.listaChekList.some(tm => checkListPrevio.id == tm.idcheck && (tm.listaResposta && tm.listaResposta.length > 0));
			if (!temCheckListPrevio) {
				retorno = !temCheckListPrevio;
			}
			this.checkListVisualizacao.existeCheckListPrevio = true;
			retorno = false;
			this.populaApontamentosDataTableView(checkListPrevio.apontamentos);
			this.inicializarListaJustificativaEApontamentosCheckados();
		}
		return retorno;
	}

	/**
	 * Recarrega as respostas dos checklists prévios ou não prévios.
	 */
	private recarregaCheckListsFaseJaRespondidos() {
		this.checkListVisualizacao.listApontamentosCheckados = [];
		this.checkListVisualizacao.listaJustificativaApontamentos = [];
		if (this.checkListVisualizacao.listaChekList && this.checkListVisualizacao.listaChekList.some(x => x.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST)) {
			for (let check of this.checkListVisualizacao.listaChekList) {
				if (check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && check.idcheck == this.checkListVisualizacao.idCheckListAtivado.id && this.checkListVisualizacao.idCheckListAtivado.data == check.data_revogacao) {
					this.checkListVisualizacao.checkEmFoco = check;
					this.checkListVisualizacao.dataChecK = check.data_revogacao;
					this.checkListVisualizacao.habilitarDesabilitarTratamento = !check.habilitaVerificacao;
					if (check.listaResposta) {
						check.listaResposta.forEach(checkSelecionado => {
							this.checkListVisualizacao.listApontamentosCheckados.push(checkSelecionado.verificacaoAprovada);
							this.checkListVisualizacao.listaJustificativaApontamentos.push(checkSelecionado.comentario);
						});
						this.checkListVisualizacao.apontamentoPreenchido = true;
					} else {
						this.inicializarListaJustificativaEApontamentosCheckados();
					}
				}
			}
		}
	}

	/**
	 * Carrega os apontamentos por menor granularidade
	 */
	public carregaApontamentosDocumentoAtualSelecionado(appService: ApplicationService, msgChekList: any, dossieProduto: any) {
		this.defineIdCheckListParaTodosDocumentosSeremTratados();
		for (let conteudoDoc of this.checkListVisualizacao.listDocumentosImagens) {
			this.checkListVisualizacao.listApontamentosCheckados = [];
			this.checkListVisualizacao.apontamentos = [];
			for(let instancia of dossieProduto.instancias_documento) {
				if(instancia.situacao_atual == "Conforme" && instancia.documento.id == conteudoDoc.id) {					
					let checkDocTratamento = this.checkListVisualizacao.listaChekList.find(checkSerTratado => checkSerTratado.idTipoDocumento && checkSerTratado.idTipoDocumento == conteudoDoc.tipo_documento.id && checkSerTratado.idDocumento == conteudoDoc.id);
					checkDocTratamento.semApontamentos = true;
					let msgChekList = new Msg_checklist();
						msgChekList.descricao = MSG_TRATAMENTO.CHECK_LIST_CONFORME.replace('$$', instancia.situacao_atual.toUpperCase());
						msgChekList.tipo = TIPO_MSG_TRATAMENTO.TIPO_CONFORME;
						return msgChekList;
				}
			}
			if(Object.keys(msgChekList).length == 0) {
				msgChekList = this.selecionaApontamentosPorTipoOuFuncao(conteudoDoc, appService, msgChekList);
			}
			this.inicializarListaJustificativaEApontamentosCheckados();
			this.carregarApontamentosSalvoConformeArvoreId(conteudoDoc.id);
			this.verificarExisteCheckListVerificado(dossieProduto);
		}
		return msgChekList;
	}

	/**
	 * Recarrega as respostas e os comentários dos apontamentos de um documento já verificado anteriormente
	 * @param id id do documento atual selecionado
	 */
	private carregarApontamentosSalvoConformeArvoreId(id: any) {
		for (let check of this.checkListVisualizacao.listaChekList) {
			if (check.idDocumento == id) {
				this.checkListVisualizacao.checkEmFoco = check;
				this.checkListVisualizacao.habilitarDesabilitarTratamento = !check.habilitaVerificacao;
				if (check.listaResposta && check.listaResposta.length > 0) {
					this.checkListVisualizacao.apontamentoPreenchido = true;
					check.listaResposta.forEach((apontamento, idx) => {
						this.checkListVisualizacao.listaJustificativaApontamentos[idx] = apontamento.comentario;
						this.checkListVisualizacao.listApontamentosCheckados[idx] = apontamento.verificacaoAprovada;
					});
				} else {
					break;
				}
			}
		}
	}

	/**
	 * Seleciona o checklist a ser carregado do documento selecionado, dando preferencia ao tipo de documento primeiro,
	 * no qual tem a menor granularidade
	 * @param conteudoDoc Documento selecionado pelo usuário no sidebarmenu
	 */
	private selecionaApontamentosPorTipoOuFuncao(conteudoDoc: any, appService: ApplicationService, msgChekList: any) {
		let checkDocTratamento = this.checkListVisualizacao.listaChekList.find(checkSerTratado => checkSerTratado.idTipoDocumento && checkSerTratado.idTipoDocumento == conteudoDoc.tipo_documento.id && checkSerTratado.idDocumento == conteudoDoc.id);
		if (null != checkDocTratamento) {
			let check = this.checkListVisualizacao.processoFaseAtual.checklists.find(check => check.id == checkDocTratamento.idcheck && check.tipo_documento && check.tipo_documento.id == checkDocTratamento.idTipoDocumento);
			if (check) {
				this.checkListVisualizacao.nomeChecklistTitle = check.nome;
				this.checkListVisualizacao.orientacaoChecklist = check.orientacao_operador;
				this.populaApontamentosDataTableView(check.apontamentos);
				if(check.apontamentos.length == 0) {
					checkDocTratamento.semApontamentos = true;
					let msg = new Msg_checklist();
					this.abrirDocumentoPopupDefaultService.setExisteCheckList(false);
					return this.naoExisteChekList(msg);
				}else {
					checkDocTratamento.semApontamentos = true;
				}
				this.abrirDocumentoPopupDefaultService.setExisteCheckList(true);
				return;
			}
		}

		checkDocTratamento = checkDocTratamento? checkDocTratamento : this.checkListVisualizacao.listaChekList.find(checkSerTratado => checkSerTratado.idFuncaoDocumental && checkSerTratado.idFuncaoDocumental == conteudoDoc.idFuncaoDocumental && checkSerTratado.idDocumento == conteudoDoc.id);
		if (null != checkDocTratamento) {
			msgChekList = this.encontrarIdCheckd(checkDocTratamento, appService);
			let check = this.checkListVisualizacao.processoFaseAtual.checklists.find(check => check.id == checkDocTratamento.idcheck && check.funcao_documental && check.funcao_documental.id == checkDocTratamento.idFuncaoDocumental);
			if (check) {
				this.checkListVisualizacao.nomeChecklistTitle = check.nome;
				this.checkListVisualizacao.orientacaoChecklist = check.orientacao_operador;
				this.populaApontamentosDataTableView(check.apontamentos);
				if(check.apontamentos.length == 0) {
					checkDocTratamento.semApontamentos = true;
					let msg = new Msg_checklist();
					this.abrirDocumentoPopupDefaultService.setExisteCheckList(false);
					return this.naoExisteChekList(msg);
				}
				this.abrirDocumentoPopupDefaultService.setExisteCheckList(true);
			}
			if(msgChekList) {
				checkDocTratamento.semApontamentos = true;
			}
			this.abrirDocumentoPopupDefaultService.setExisteCheckList(false);
			return msgChekList;
		}
	}

	private encontrarIdCheckd(checkDocTratamento: Checklist, appService: ApplicationService) {
		if (!checkDocTratamento.idcheck) {
			let listaTipologia = JSON.parse(appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoTipologia));
			let tipoDocumentoTipologia = listaTipologia.tipos_documento.find(lstipo => lstipo.id == checkDocTratamento.idTipoDocumento);
			let listaFuncao = [];
			this.encontrarCheckListPorTipoDocuemnto(tipoDocumentoTipologia, listaFuncao);
			if(listaFuncao.length == 1) {
				this.encontrarIdFuncaoEIdChekList(tipoDocumentoTipologia, checkDocTratamento);
				return;
			}else {
				let msg = new Msg_checklist();

				// eu comentei esse codigo não faz sentido - juliano 23/04/2020
				// como pode existir check list duplicado se o mesmo não tem idcheck, sendo que idcheck identifica o checklist do tipo documento.
				//
				// if(tipoDocumentoTipologia && tipoDocumentoTipologia.funcoes_documentais && tipoDocumentoTipologia.funcoes_documentais.length > 1) {
				// 	let documentoFaltante = tipoDocumentoTipologia.funcoes_documentais ? tipoDocumentoTipologia.funcoes_documentais[0].nome : "";
				// 		msg.descricao = MSG_TRATAMENTO.CHECK_LIST_DUPLICADO.replace("$$", documentoFaltante);
				// 		msg.tipo = TIPO_MSG_TRATAMENTO.TIPO_DUPLICIDADE
				// 	return msg;
				// }
				
				return this.naoExisteChekList(msg);
			}
		}
	}

	private naoExisteChekList(msg: Msg_checklist) {
		msg.descricao = MSG_TRATAMENTO.CHECK_LIST_NAO_EXISTE;
		msg.tipo = TIPO_MSG_TRATAMENTO.TIPO_INEXISTENTE;
		return msg;
	}

	private encontrarIdFuncaoEIdChekList(tipoDocumentoTipologia: any, checkDocTratamento: Checklist) {
		let encontrouPrimeiroCheklist;
		if(tipoDocumentoTipologia.funcoes_documentais) {
			for (let funcao of tipoDocumentoTipologia.funcoes_documentais) {
				for (let checkList of this.checkListVisualizacao.processoFaseAtual.checklists) {
					if (checkList.funcao_documental && funcao.id == checkList.funcao_documental.id) {
						checkDocTratamento.idcheck = checkList.id;
						checkDocTratamento.idFuncaoDocumental = funcao.id;
						encontrouPrimeiroCheklist = true;
					}
					if(encontrouPrimeiroCheklist) {
						break;
					}
				}
			}
		}
	}

	private encontrarCheckListPorTipoDocuemnto(tipoDocumentoTipologia: any, listaFuncao: any[]) {
		if(tipoDocumentoTipologia && tipoDocumentoTipologia.funcoes_documentais) {
			tipoDocumentoTipologia.funcoes_documentais.forEach(funcao => {
				this.checkListVisualizacao.processoFaseAtual.checklists.forEach(checkList => {
					if (checkList.funcao_documental && funcao.id == checkList.funcao_documental.id) {
						listaFuncao.push(checkList);
					}
				});
			});
		}
	}

	/**
	 * Seleciona o checklist com a menor data de revogação, tendo como preferencia em primeiro o checklist de
	 * tipo de documento.
	 */
	public defineIdCheckListParaTodosDocumentosSeremTratados() {
		if (this.checkListVisualizacao.listaChekList.find(checkSerTratado => !checkSerTratado.idcheck)) {
			for (let checkSerTratado of this.checkListVisualizacao.listaChekList) {
				if (!this.selecionaCheckListTipoDocPelaMenorDataRevogacao(checkSerTratado)) {
					this.selecionaCheckListFuncDocPelaMenorDataRevogacao(checkSerTratado);
				}
			}
		}
	}

	/**
	 * Percorre o processo fase atual e seleciona o checklist de tipo de documento com a menor granulariade
	 * @param checkSerTratado Checklist com a menor data de revogação
	 */
	private selecionaCheckListTipoDocPelaMenorDataRevogacao(checkSerTratado: Checklist) {
		let achoutCheckTipoDoc: boolean = false;
		let checkListTipoDocTratamento = this.checkListVisualizacao.processoFaseAtual.checklists.
			filter(check => check.tipo_documento 
				&& check.tipo_documento.id == checkSerTratado.idTipoDocumento 
				&& this.isDataMaiorQueDataAtual(check.data_revogacao));
				
		if (null != checkListTipoDocTratamento && checkListTipoDocTratamento.length > 0) {
			let checkMenorDataRevogacao = checkListTipoDocTratamento.
				reduce((checkAnterior, checkAtual) => moment(checkAnterior.data_revogacao, 'DD/MM/YYYY').valueOf() <
					moment(checkAtual.data_revogacao, 'DD/MM/YYYY').valueOf() ? checkAnterior : checkAtual);
				checkSerTratado.idcheck = checkMenorDataRevogacao.id;
				achoutCheckTipoDoc = true;
		}
		return achoutCheckTipoDoc;
	}

	/**
	 * Percorre o processo fase atual e seleciona o checklist de funão documental com a menor granulariade
	 * @param checkSerTratado Checklist com a menor data de revogação 
	 */
	private selecionaCheckListFuncDocPelaMenorDataRevogacao(checkSerTratado: Checklist) {
		let checkListFuncaoDocEmTratamento = this.checkListVisualizacao.processoFaseAtual.checklists.
			filter(check => check.funcao_documental 
					&& check.funcao_documental.id == checkSerTratado.idFuncaoDocumental  
					&& this.isDataMaiorQueDataAtual(check.data_revogacao));

		if (null != checkListFuncaoDocEmTratamento && checkListFuncaoDocEmTratamento.length > 0) {
			let checkMenorDataRevogacao = checkListFuncaoDocEmTratamento.
				reduce((checkAnterior, checkAtual) => 
				moment(checkAnterior.data_revogacao, 'DD/MM/YYYY').valueOf() < moment(checkAtual.data_revogacao, 'DD/MM/YYYY').valueOf()  
				&& this.isDataMaiorQueDataAtual(checkAnterior.data_revogacao) ? checkAnterior : checkAtual);
			
				checkSerTratado.idcheck = checkMenorDataRevogacao.id;
		}
	}

	private isDataMaiorQueDataAtual(data: any):boolean{
        return (moment(new Date(data), 'DD/MM/YYYY').valueOf() > moment(new Date(), 'DD/MM/YYYY').valueOf());
    }
}