import { GridTipoDocumento } from './../../model/grid-tipo-documento.model';
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { TipoDocumentoService } from 'src/app/cruds/tipo-documento/service/tipo-documento.service';
import { Utils } from "src/app/cruds/util/utils";
import { LoaderService } from "src/app/services";
import { TipoDocumento } from '../model/tipo-documento';
import { TIPO_DOCUMENTO } from './../../constant-tipo-documento';
import { TipoDocumentoModel } from './../model/tipo-documento.model';
import { TipoDocumentoComponent } from './../view/tipo-documento.component';
import { IncludeTipoDocumento } from "../model/include-tipo-documento";
import { UpdateTipoDocumento } from '../model/update-tipo-documento';
import { ModalAtributoModel } from 'src/app/cruds/atributo/modal-atributo/model/modal-atributo-model';
import { IncludeAtributoExtracao } from '../model/include-atributo-extracao';
import { forkJoin, Observable } from 'rxjs';
import { UpdateAtributoExtracao } from '../model/update-atributo-extracao';
import { IncludeOpcaoAtributoExtracao } from '../model/include-opcao-atributo-extracao';
import { ModalOpcaoAtributoModel } from 'src/app/cruds/atributo/modal-atributo/opcao-grid/model/modal-opcao-atributo-model';
import { SelectTipoPessoa } from 'src/app/cruds/model/select-tipo-pessoa.interface';
import { FuncaoDocumentalService } from 'src/app/cruds/funcao-documental/funcao-documental.service';
import { InterfaceFuncaoDocumental } from 'src/app/cruds/model/funcao-documental.interface';
import { GridFuncaoDocumental } from '../model/grid-funcao-documental';
import { ConfirmationService } from 'primeng/primeng';
import { GrowlMessageService } from 'src/app/cruds/growl-message-service/growl-message.service';

@Injectable()
export class TipoDocumentoPresenter {

	tipoDocumentoModel: TipoDocumentoModel;
	tipoDocumento: TipoDocumento;

	constructor(private tipoDocumentoService: TipoDocumentoService,
		private funcaoDocumentalService: FuncaoDocumentalService,
		private growlMessageService: GrowlMessageService,
		private confirmationService: ConfirmationService,
		private router: Router,
		private loadService: LoaderService) { }

    /**
    * Inicializa a configuração inicial do Tipo Documento: verifica edicao
    * @param params 
    * @param tipoDocumentoComponent 
    */
	initConfigTipoDocumento(params: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.getFuncoesDocumentais(params, tipoDocumentoComponent);
	}

    /**
     * Navega para a url passada como parâmetro
     * @param url 
     */
	navigateUrl(parameters: string[]) {
		this.router.navigate(parameters);
	}

	/**
	 * Anula as variaveis de controle de mensagem de atualização de tipo de documento
	 * e retorna para a pagina de pesquisa
	 */
	anularParametrosMemoriaTipoDocumentoVoltaPesquisa() {
		this.navigateUrl([TIPO_DOCUMENTO.TIPO_DOCUMENTO_PESQUISA]);
		this.tipoDocumentoService.setEdicaoTipoDocumento(false);
		this.tipoDocumentoService.setNovoTipoDocumento(false);
		this.tipoDocumentoService.setTipoDocumentoSemAlteracao(false);
	}

	/**
	 * adiciona tag 
	 */
	adicionarTag() {
		if (this.tipoDocumento.tags) {
			this.tipoDocumento.tags.push(this.tipoDocumento.tag_documento.toUpperCase());
			this.tipoDocumento.tag_documento = '';
		}
	}

	/**
	 * Verifica a existencia do nome do tipo de documento com um destes indicadores:
	 * ic_apoio_negocio, ic_dossie_digital e ic_dossie_digital
	 * confirmando a existencia; é notificado ao usuário a impossibilidade de cadastro
	 * utilização de evento blur no campo nome; nesse caso a mensagem de validação será apresentada
	 * no componente growl message
     * @param tipoDocumentoComponent 
	 */
	verificarNomeTipoDocumentoEmConjuntoComIndicadoresDeUnicidade(tipoDocumentoComponent: TipoDocumentoComponent) {
		if (this.tipoDocumento.indicador_uso_apoio_negocio) {
			this.verificarOcorrenciaNomeTipoDocumentoIndicadorApoioNegocio(tipoDocumentoComponent, true);
		}
		if (this.tipoDocumento.indicador_uso_dossie_digital) {
			this.verificarOcorrenciaNomeTipoDocumentoIndicadorDossieDigital(tipoDocumentoComponent, true);

		}
		if (this.tipoDocumento.indicador_uso_processo_administrativo) {
			this.verificarOcorrenciaNomeTipoDocumentoIndicadorProcessoAdministrativo(tipoDocumentoComponent, true);
		}
	}

	/**
	 * Verifica a ocorrencia de nome de tipo de documento
	 */
	verificarOcorrenciaNumeroTipologia() {
		if (!Number.isInteger(Number(this.tipoDocumento.codigo_tipologia))) {
			this.tipoDocumento.codigo_tipologia = '';
		} else {
			if (!this.validarTipologiaDocumento()) {
				this.tipoDocumentoModel.tipoLogiaCadastrada = true;
			} else {
				this.tipoDocumentoModel.tipoLogiaCadastrada = false;
			}
		}
		this.tipoDocumentoModel.tipoLogiaInconssistente = false;
	}

	/**
	 * Verifica a ocorrencia de nome de tipo de documento e indicador apoio negocio
     * @param tipoDocumentoComponent 
	 * @param blurEvent 
	 */
	verificarOcorrenciaNomeTipoDocumentoIndicadorApoioNegocio(tipoDocumentoComponent: TipoDocumentoComponent, blurEvent: boolean) {
		if (!this.validarNomeTipoDocumentoApoioNegocio()) {
			this.tipoDocumentoModel.nomeApoioNegocioCadastrado = true;
			this.tipoDocumentoModel.nomeTipoDocumentoJaCadastrado = this.tipoDocumento.nome;
			const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_NOME_APOIO_NEGOCIO.replace('{nome}', this.tipoDocumento.nome);
			if (blurEvent) {
				this.growlMessageService.showError(TIPO_DOCUMENTO.TIPO_DOCUMENTO_VALIDACAO, msg);
			} else {
				tipoDocumentoComponent.addMessageError(msg);
			}
		} else {
			this.tipoDocumentoModel.nomeApoioNegocioCadastrado = false;
		}
	}

	/**
	 * Verifica a ocorrencia de nome de tipo de documento e indicador dossie digital
     * @param tipoDocumentoComponent 
	 * @param blurEvent 
	 */
	verificarOcorrenciaNomeTipoDocumentoIndicadorDossieDigital(tipoDocumentoComponent: TipoDocumentoComponent, blurEvent: boolean) {
		let msgs: Array<string> = new Array<string>();
		if (!this.validarNomeTipoDocumentoDossieDigital()) {
			this.tipoDocumentoModel.nomeDossieDigitalCadastrado = true;
			this.tipoDocumentoModel.nomeTipoDocumentoJaCadastrado = this.tipoDocumento.nome;
			const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_NOME_DOSSIE_DIGITAL.replace('{nome}', this.tipoDocumento.nome);
			if (blurEvent) {
				this.growlMessageService.showError(TIPO_DOCUMENTO.TIPO_DOCUMENTO_VALIDACAO, msg);
			} else {
				msgs.push(msg);
			}
		} else {
			this.tipoDocumentoModel.nomeDossieDigitalCadastrado = false;
		}
		if (this.tipoDocumento.indicador_uso_dossie_digital == true && !this.tipoDocumentoModel.editar) {
			if (blurEvent) {
				this.growlMessageService.showError(TIPO_DOCUMENTO.TIPO_DOCUMENTO_VALIDACAO, TIPO_DOCUMENTO.TIPO_DOCUMENTO_SIECM_DOSSIE_DIGITAL_PROCESSO);
			} else {
				msgs.push(TIPO_DOCUMENTO.TIPO_DOCUMENTO_SIECM_DOSSIE_DIGITAL_PROCESSO);
			}
			this.tipoDocumentoModel.classeSiecmDossieDigital = true;
		} else {
			this.tipoDocumentoModel.classeSiecmDossieDigital = false;
		}
		this.lancaMensagensErros(msgs, tipoDocumentoComponent);
	}

	/**
	 * Verifica a ocorrencia de nome de tipo de documento e indicador processo administrativo
     * @param tipoDocumentoComponent 
	 * @param blurEvent 
	 */
	verificarOcorrenciaNomeTipoDocumentoIndicadorProcessoAdministrativo(tipoDocumentoComponent: TipoDocumentoComponent, blurEvent: boolean) {
		let msgs: Array<string> = new Array<string>();
		if (!this.validarNomeTipoDocumentoProcessoAdministrativo()) {
			this.tipoDocumentoModel.nomeProcessoAdministrativoCadastrado = true;
			this.tipoDocumentoModel.nomeTipoDocumentoJaCadastrado = this.tipoDocumento.nome;
			const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_NOME_PROCESSO_ADMINISTRATIVO.replace('{nome}', this.tipoDocumento.nome);
			if (blurEvent) {
				this.growlMessageService.showError(TIPO_DOCUMENTO.TIPO_DOCUMENTO_VALIDACAO, msg);
			} else {
				msgs.push(msg);
			}
		} else {
			this.tipoDocumentoModel.nomeProcessoAdministrativoCadastrado = false;
		}
		if (this.tipoDocumento.indicador_uso_processo_administrativo == true && !this.tipoDocumentoModel.editar) {
			if (blurEvent) {
				this.growlMessageService.showError(TIPO_DOCUMENTO.TIPO_DOCUMENTO_VALIDACAO, TIPO_DOCUMENTO.TIPO_DOCUMENTO_SIECM_DOSSIE_DIGITAL_PROCESSO);
			} else {
				msgs.push(TIPO_DOCUMENTO.TIPO_DOCUMENTO_SIECM_DOSSIE_DIGITAL_PROCESSO);
			}
			this.tipoDocumentoModel.classeSiecmProcessoAdministrativo = true;
		} else {
			this.tipoDocumentoModel.classeSiecmProcessoAdministrativo = false;
		}
		this.lancaMensagensErros(msgs, tipoDocumentoComponent);
	}

	/**
	 * inicializa a lista de atributos no componente tipoDocumentoComponent
	 * @param atributos 
	 */
	inicializarAtributos(atributos: Array<ModalAtributoModel>) {
		this.tipoDocumento.atributos_extracao = Object.assign([], atributos);
	}

	/**
	 * Inicializa os atributos para remocacao no componente tipoDocumentoComponent
	 * @param atributoRemovido 
	 */
	inicializarAtributosParaRemocao(atributoRemovido: ModalAtributoModel) {
		if (this.tipoDocumento.atributosExcluidos) {
			let isExiste = this.tipoDocumento.atributosExcluidos.find(atr => atr.id == atributoRemovido.id);
			if (!isExiste) {
				this.tipoDocumento.atributosExcluidos.push(atributoRemovido);
			}
		} else {
			this.tipoDocumento.atributosExcluidos = new Array<ModalAtributoModel>();
			this.tipoDocumento.atributosExcluidos.push(atributoRemovido);
		}
	}

	/**
	 * inicializa a função documental selecionada na grid de funções
	 */
	inicializarFuncaoDocumentalGrid() {
		const gridFuncaoDocumental: GridFuncaoDocumental = new GridFuncaoDocumental();
		gridFuncaoDocumental.index = this.tipoDocumentoModel.selectedFuncaoDocumental.index;
		gridFuncaoDocumental.identificador_funcao_documental = this.tipoDocumentoModel.selectedFuncaoDocumental.key;
		gridFuncaoDocumental.nome_funcao_documental = this.tipoDocumentoModel.selectedFuncaoDocumental.name;
		gridFuncaoDocumental.label_indicador_processo_administrativo = this.tipoDocumentoModel.selectedFuncaoDocumental.administrativo ? 'SIM' : 'NÃO';
		gridFuncaoDocumental.label_indicador_dossie_digital = this.tipoDocumentoModel.selectedFuncaoDocumental.digital ? 'SIM' : 'NÃO';
		gridFuncaoDocumental.label_indicador_apoio_negocio = this.tipoDocumentoModel.selectedFuncaoDocumental.negocio ? 'SIM' : 'NÃO';
		gridFuncaoDocumental.indicador_processo_administrativo = this.tipoDocumentoModel.selectedFuncaoDocumental.administrativo;
		gridFuncaoDocumental.indicador_dossie_digital = this.tipoDocumentoModel.selectedFuncaoDocumental.digital;
		gridFuncaoDocumental.indicador_apoio_negocio = this.tipoDocumentoModel.selectedFuncaoDocumental.negocio;
		gridFuncaoDocumental.data_hora_ultima_alteracao = this.tipoDocumentoModel.selectedFuncaoDocumental.data;
		gridFuncaoDocumental.persistido = false;
		this.tipoDocumentoModel.funcoesDocumentais.push(gridFuncaoDocumental);
		this.removerFuncaoDocumentalDropdown(gridFuncaoDocumental);
		this.custumizeRowsPerPageOptions();
	}

    /**
      * Checagem para remoção de função documental
     * @param gridFuncaoDocumental
     * @param index 
     */
	confirmRemoveFuncaoDocumental(gridFuncaoDocumental: GridFuncaoDocumental, index: number) {
		this.confirmationService.confirm({
			message: TIPO_DOCUMENTO.TIPO_DOCUMENTO_REMOCAO_FUNCAO_DOCUMENTAL,
			accept: () => {
				this.removerFuncaoDocumentalRetornoDropdown(gridFuncaoDocumental, index);
			}
		});
	}

	/**
	 * lança mensagens de erros para usuário referentes a indicadores
	 * @param msgs 
	 * @param tipoDocumentoComponent 
	 */
	private lancaMensagensErros(msgs: string[], tipoDocumentoComponent: TipoDocumentoComponent) {
		msgs.forEach(msg => {
			tipoDocumentoComponent.addMessageError(msg.toString());
		});
	}

	/**
	 * remove a função documental da grid devolvendo para dropdown
	 * estando persistido; será adicionada o identificador da função na lista de exclusão de vínculo
	 * @param gridFuncaoDocumental 
	 * @param index 
	 */
	private removerFuncaoDocumentalRetornoDropdown(gridFuncaoDocumental: GridFuncaoDocumental, index: number) {
		this.tipoDocumentoModel.funcoesDocumentais.splice(index, 1);
		if (gridFuncaoDocumental.persistido) {
			if (this.tipoDocumento.funcoes_documento_exclusao_vinculo) {
				this.tipoDocumento.funcoes_documento_exclusao_vinculo.push(gridFuncaoDocumental.identificador_funcao_documental);
			} else {
				this.tipoDocumento.funcoes_documento_exclusao_vinculo = new Array<number>(0);
				this.tipoDocumento.funcoes_documento_exclusao_vinculo.push(gridFuncaoDocumental.identificador_funcao_documental);
			}
		}
		this.tipoDocumentoModel.funcoesDocumentaisInterface.splice(gridFuncaoDocumental.index, 0, {
			index: gridFuncaoDocumental.index, key: gridFuncaoDocumental.identificador_funcao_documental, name: gridFuncaoDocumental.nome_funcao_documental,
			administrativo: gridFuncaoDocumental.indicador_processo_administrativo, digital: gridFuncaoDocumental.indicador_dossie_digital,
			negocio: gridFuncaoDocumental.indicador_apoio_negocio, data: gridFuncaoDocumental.data_hora_ultima_alteracao
		});
		this.tipoDocumentoModel.funcoesDocumentaisInterface = this.tipoDocumentoModel.funcoesDocumentaisInterface.slice(0);
	}

	/**
	 * Ao adicionar a função na grid; a mesma é removida da dropdown
	 * podendo ser removida da lista exclusao caso tenha o mesmo identificador
	 * @param gridFuncaoDocumental 
	 */
	private removerFuncaoDocumentalDropdown(gridFuncaoDocumental: GridFuncaoDocumental) {
		this.tipoDocumentoModel.funcoesDocumentaisInterface = this.tipoDocumentoModel.funcoesDocumentaisInterface.filter(fdi => fdi.index !== this.tipoDocumentoModel.selectedFuncaoDocumental.index);
		this.tipoDocumentoModel.selectedFuncaoDocumental = undefined;
		if (this.tipoDocumento.funcoes_documento_exclusao_vinculo && this.tipoDocumento.funcoes_documento_exclusao_vinculo.some(fdev => fdev == gridFuncaoDocumental.identificador_funcao_documental)) {
			const index: number = this.tipoDocumento.funcoes_documento_exclusao_vinculo.indexOf(gridFuncaoDocumental.identificador_funcao_documental);
			this.tipoDocumento.funcoes_documento_exclusao_vinculo.splice(index, 1);
		}
	}

	/**
	 * Anula o prazo validade quando marcado validade auto contida
	 */
	anularPrazoValidade() {
		if (this.tipoDocumento.indicador_validade_autocontida) {
			this.tipoDocumento.prazo_validade_dias = null;
		} else {
			this.tipoDocumento.prazo_validade_dias = 1;
		}
	}

	/**
	 * Salva um novo tipo Documento; verificando se é POST ou PATH
	 * @param tipoDocumentoComponent 
	 * @param finalizar 
	 */
	saveUpdateTipoDocumeto(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		if (this.tipoDocumentoModel.editar) {
			this.updateTipoDocumento(tipoDocumentoComponent, finalizar);
		} else {
			this.saveTipoDocumento(tipoDocumentoComponent, finalizar);
		}
	}

	/**
	 * Inicializa o objeto para atualizar o tipo de documento: apenas os campos alterados 
	 * controle para formulario alterado
	 */
	private inicializarUpdateTipoDocumento(): UpdateTipoDocumento {
		let formularioTipoDocumentoAlterado: boolean = false;
		this.tipoDocumento.indicador_tipo_pessoa = this.tipoDocumentoModel.selectedTipoPessoa ? this.tipoDocumentoModel.selectedTipoPessoa.value : null;
		this.tipoDocumento.avatar = this.tipoDocumento.avatar ? this.tipoDocumento.avatar : 'fa fa-clock-o';
		this.tipoDocumento.funcoes_documento_inclusao_vinculo = this.tipoDocumentoModel.funcoesDocumentais.reduce((funcoes, gridFuncaoDocumental) => {
			if (!gridFuncaoDocumental.persistido) {
				funcoes.push(gridFuncaoDocumental.identificador_funcao_documental);
			}
			return funcoes;
		}, new Array<number>());

		return Object.keys(this.tipoDocumento).filter(key => key !== 'atributos_extracao' && key !== 'funcoes_documentais'
			&& key !== 'atributosExcluidos' && key !== 'tag_documento').reduce(
				(update, key) => {
					let copyValue: any = this.tipoDocumentoModel.tipoDocumento[key];
					let changeValue: any = this.tipoDocumento[key];
					if (typeof copyValue === 'string' || copyValue instanceof String) {
						copyValue = copyValue.trim();
					}
					if (typeof changeValue === 'string' || changeValue instanceof String) {
						changeValue = changeValue.trim();
					}
					if (copyValue instanceof Array && changeValue instanceof Array) {
						if (copyValue.toString() !== changeValue.toString()) {
							Reflect.set(update, key, this.tipoDocumento[key]);
							formularioTipoDocumentoAlterado = true;
						}
					} else {
						if (((copyValue !== undefined && copyValue != null) || changeValue !== undefined) && copyValue !== changeValue) {
							Reflect.set(update, key, this.tipoDocumento[key]);
							formularioTipoDocumentoAlterado = true;
						}
					}
					this.tipoDocumentoModel.formularioTipoDocumentoAlterado = formularioTipoDocumentoAlterado;
					return update;
				}, new UpdateTipoDocumento());
	}

    /**
     * Atualiza o tipoDocumento: PATH
	 * verifica se o formulário foi alterado
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private updateTipoDocumento(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		const updateTipoDocumento: UpdateTipoDocumento = this.inicializarUpdateTipoDocumento();

		// remover atributos da lista de alteração que esteja na lista de exclusão
		if (this.tipoDocumento.atributosExcluidos && this.tipoDocumento.atributosExcluidos.length > 0
			&& this.tipoDocumento.atributos_extracao && this.tipoDocumento.atributos_extracao.length > 0) {

			for (let i = 0; i < this.tipoDocumento.atributos_extracao.length; i++) {
				let atributo = this.tipoDocumento.atributos_extracao[i];

				let existEm2Lista: boolean = this.tipoDocumento.atributosExcluidos.some(atr => atributo.id && atributo.id === atr.id);

				if (existEm2Lista) {// significa o mesmo elemento está nas 2 lista
					this.tipoDocumento.atributos_extracao.splice(i, 1);
				}
			}
		}

		const alteracaoAtributos: boolean = this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.alterado || atributoExtracao.id == undefined);
		const remocaoAtributos: boolean = this.tipoDocumento.atributosExcluidos && this.tipoDocumento.atributosExcluidos.length > 0;
		const remocaoOpccoesAtributos: boolean = this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.opcoesExcluidas !== undefined);
		const adicaoFuncaoDocumental: boolean = this.tipoDocumentoModel.funcoesDocumentais.some(fd => !fd.persistido);
		const remocaoFuncaoDocumentao: boolean = this.tipoDocumento.funcoes_documento_exclusao_vinculo && this.tipoDocumento.funcoes_documento_exclusao_vinculo.length > 0;

		if (!this.tipoDocumentoModel.formularioTipoDocumentoAlterado && !alteracaoAtributos && !remocaoAtributos && !remocaoOpccoesAtributos && !adicaoFuncaoDocumental && !remocaoFuncaoDocumentao) {
			if (finalizar) {
				this.tipoDocumentoService.setTipoDocumentoSemAlteracao(true);
				this.tipoDocumentoService.setId(this.tipoDocumentoModel.ultimoIdTipoDocumento);
				this.redirecionarParaPesquisaTipoDocumento();
			} else {
				tipoDocumentoComponent.addMessageWarning(TIPO_DOCUMENTO.TIPO_DOCUMENTO_FORMULARIO_INALTERADO);
			}
		} else if (!this.tipoDocumentoModel.formularioTipoDocumentoAlterado && (alteracaoAtributos || remocaoAtributos || remocaoOpccoesAtributos || adicaoFuncaoDocumental || remocaoFuncaoDocumentao)) {
			this.salvarAtributoExtracao(tipoDocumentoComponent, finalizar);
			this.removerAtributosExtracao(tipoDocumentoComponent, finalizar);
			this.removerOpcoesAtributosExtracao(tipoDocumentoComponent, finalizar);
		} else {
			this.loadService.show();
			this.tipoDocumentoService.update(this.tipoDocumento.id, updateTipoDocumento).subscribe(() => {
				this.onSucessUpdateTipoDocumento(finalizar, tipoDocumentoComponent);
			}, error => {
				this.loadService.hide();
				this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
			});
		}
	}

	/**
	 * Sucesso ao atualizar tipo documento
	 * @param finalizar 
	 * @param tipoDocumentoComponent 
	 */
	private onSucessUpdateTipoDocumento(finalizar: boolean, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.tipoDocumentoModel.ultimoIdTipoDocumento = this.tipoDocumento.id;
		this.salvarAtributoExtracao(tipoDocumentoComponent, finalizar);
		this.removerAtributosExtracao(tipoDocumentoComponent, finalizar);
		this.removerOpcoesAtributosExtracao(tipoDocumentoComponent, finalizar);
		if (this.verificarPreenchimentoFormulario([this.tipoDocumentoModel.requesicoesIncludeAtributos, this.tipoDocumentoModel.requesicoesUpdateAtributos, this.tipoDocumentoModel.requesicoesAtributosRemovidos, this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos])) {
			this.verificarTipoFinalizacao(finalizar, this.tipoDocumento.id, tipoDocumentoComponent);
		}
	}

    /**
     * Salva o tipoDocumento: POST
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private saveTipoDocumento(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		this.loadService.show();
		this.tipoDocumentoService.save(this.inicializarIncludeTipoDocumento()).subscribe(ultimoIdTipoDocumento => {
			this.onSucessSaveTipoDocumento(ultimoIdTipoDocumento, tipoDocumentoComponent, finalizar);
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Inicializa o objeto para salvar o opcao atributo extracao
     * @param atributoExtracao 
     */
	private inicializarIncludeOpcaoAtributoExtracao(opcaoAtributo: ModalOpcaoAtributoModel): IncludeOpcaoAtributoExtracao {
		const includeOpcaoAtributoExtracao: IncludeOpcaoAtributoExtracao = new IncludeOpcaoAtributoExtracao();
		includeOpcaoAtributoExtracao.chave = opcaoAtributo.chave;
		includeOpcaoAtributoExtracao.valor = opcaoAtributo.valor;
		return includeOpcaoAtributoExtracao;
	}

    /**
     * Inicializa o objeto para salvar o atributo extracao
     * @param atributoExtracao 
     */
	private inicializarIncludeAtributoExtracao(atributoExtracao: ModalAtributoModel): IncludeAtributoExtracao {
		const includeAtributoExtracao: IncludeAtributoExtracao = new IncludeAtributoExtracao();
		includeAtributoExtracao.nome_atributo_negocial = atributoExtracao.nome_atributo_negocial;
		includeAtributoExtracao.nome_atributo_retorno = atributoExtracao.nome_atributo_retorno;
		includeAtributoExtracao.nome_atributo_siecm = atributoExtracao.nome_atributo_siecm;
		includeAtributoExtracao.nome_atributo_documento = atributoExtracao.nome_atributo_documento;
		includeAtributoExtracao.nome_objeto_sicli = atributoExtracao.nome_objeto_sicli;
		includeAtributoExtracao.nome_atributo_sicli = atributoExtracao.nome_atributo_sicli;
		includeAtributoExtracao.nome_sicod = atributoExtracao.nome_sicod;
		includeAtributoExtracao.indicador_calculo_data_validade = atributoExtracao.indicador_calculo_data_validade;
		includeAtributoExtracao.indicador_obrigatorio = atributoExtracao.indicador_obrigatorio;
		includeAtributoExtracao.indicador_obrigatorio_siecm = atributoExtracao.indicador_obrigatorio_siecm;
		includeAtributoExtracao.indicador_identificador_pessoa = atributoExtracao.indicador_identificador_pessoa;
		includeAtributoExtracao.indicador_campo_comparacao_receita = atributoExtracao.indicador_campo_comparacao_receita;
		includeAtributoExtracao.indicador_modo_comparacao_receita = atributoExtracao.indicador_modo_comparacao_receita;
		includeAtributoExtracao.tipo_campo = atributoExtracao.tipo_campo;
		includeAtributoExtracao.tipo_atributo_geral = atributoExtracao.tipo_atributo_geral;
		includeAtributoExtracao.tipo_atributo_siecm = atributoExtracao.tipo_atributo_siecm;
		includeAtributoExtracao.tipo_atributo_sicli = atributoExtracao.tipo_atributo_sicli;
		includeAtributoExtracao.tipo_atributo_sicod = atributoExtracao.tipo_atributo_sicod;
		includeAtributoExtracao.percentual_alteracao_permitido = undefined;
		includeAtributoExtracao.identificador_atributo_partilha = atributoExtracao.identificador_atributo_partilha;
		includeAtributoExtracao.indicador_modo_partilha = atributoExtracao.indicador_modo_partilha;
		includeAtributoExtracao.indicador_estrategia_partilha = atributoExtracao.indicador_estrategia_partilha;
		includeAtributoExtracao.indicador_presente_documento = atributoExtracao.indicador_presente_documento;
		includeAtributoExtracao.valor_padrao = atributoExtracao.valor_padrao;
		includeAtributoExtracao.grupo_atributo = atributoExtracao.grupo_atributo;
		includeAtributoExtracao.ordem_apresentacao = atributoExtracao.ordem_apresentacao;
		includeAtributoExtracao.orientacao_preenchimento = atributoExtracao.orientacao_preenchimento;
		includeAtributoExtracao.expressao_interface = atributoExtracao.expressao_interface;
		return includeAtributoExtracao;
	}

    /**
     * Inicializa o objeto para atualizar o atributo extracao
     * @param atributoExtracao 
     */
	private inicializarUpdateAtributoExtracao(atributoExtracao: ModalAtributoModel): UpdateAtributoExtracao {
		const updateAtributoExtracao: UpdateAtributoExtracao = new UpdateAtributoExtracao();
		updateAtributoExtracao.nome_atributo_negocial = Utils.isValidarString(atributoExtracao.nome_atributo_negocial);
		updateAtributoExtracao.nome_atributo_documento = Utils.isValidarString(atributoExtracao.nome_atributo_documento);
		updateAtributoExtracao.nome_atributo_retorno = Utils.isValidarString(atributoExtracao.nome_atributo_retorno);
		updateAtributoExtracao.tipo_campo = Utils.isValidarString(atributoExtracao.tipo_campo);
		updateAtributoExtracao.tipo_atributo_geral = Utils.isValidarString(atributoExtracao.tipo_atributo_geral);
		updateAtributoExtracao.indicador_obrigatorio = atributoExtracao.indicador_obrigatorio ? atributoExtracao.indicador_obrigatorio : undefined;
		updateAtributoExtracao.ativo = atributoExtracao.ativo ? atributoExtracao.ativo : undefined;
		updateAtributoExtracao.nome_atributo_siecm = Utils.isValidarString(atributoExtracao.nome_atributo_siecm);
		updateAtributoExtracao.tipo_atributo_siecm = Utils.isValidarString(atributoExtracao.tipo_atributo_siecm);
		updateAtributoExtracao.indicador_obrigatorio_siecm = atributoExtracao.indicador_obrigatorio_siecm ? atributoExtracao.indicador_obrigatorio_siecm : undefined;
		updateAtributoExtracao.nome_atributo_sicli = Utils.isValidarString(atributoExtracao.nome_atributo_sicli);
		updateAtributoExtracao.nome_objeto_sicli = Utils.isValidarString(atributoExtracao.nome_objeto_sicli);
		updateAtributoExtracao.tipo_atributo_sicli = Utils.isValidarString(atributoExtracao.tipo_atributo_sicli);
		updateAtributoExtracao.nome_sicod = Utils.isValidarString(atributoExtracao.nome_sicod);
		updateAtributoExtracao.tipo_atributo_sicod = Utils.isValidarString(atributoExtracao.tipo_atributo_sicod);
		updateAtributoExtracao.indicador_campo_comparacao_receita = Utils.isValidarString(atributoExtracao.indicador_campo_comparacao_receita);
		updateAtributoExtracao.indicador_modo_comparacao_receita = Utils.isValidarString(atributoExtracao.indicador_modo_comparacao_receita);
		updateAtributoExtracao.indicador_calculo_data_validade = atributoExtracao.indicador_calculo_data_validade ? atributoExtracao.indicador_calculo_data_validade : undefined;
		updateAtributoExtracao.indicador_identificador_pessoa = atributoExtracao.indicador_identificador_pessoa ? atributoExtracao.indicador_identificador_pessoa : undefined;
		updateAtributoExtracao.valor_padrao = Utils.isValidarString(atributoExtracao.valor_padrao);
		updateAtributoExtracao.indicador_presente_documento = atributoExtracao.indicador_presente_documento ? atributoExtracao.indicador_presente_documento : undefined;
		updateAtributoExtracao.orientacao_preenchimento = Utils.isValidarString(atributoExtracao.orientacao_preenchimento);
		updateAtributoExtracao.grupo_atributo = atributoExtracao.grupo_atributo == null ? -1 : atributoExtracao.grupo_atributo;
		updateAtributoExtracao.ordem_apresentacao = atributoExtracao.ordem_apresentacao ? atributoExtracao.ordem_apresentacao : undefined;
		updateAtributoExtracao.expressao_interface = Utils.isValidarString(atributoExtracao.expressao_interface);
		updateAtributoExtracao.indicador_modo_partilha = Utils.isValidarString(atributoExtracao.indicador_modo_partilha);
		updateAtributoExtracao.identificador_atributo_partilha = atributoExtracao.identificador_atributo_partilha ? atributoExtracao.identificador_atributo_partilha : undefined;
		updateAtributoExtracao.indicador_estrategia_partilha = Utils.isValidarString(atributoExtracao.indicador_estrategia_partilha);
		updateAtributoExtracao.percentual_alteracao_permitido = undefined;
		return updateAtributoExtracao;
	}

    /**
     * Prepara as requisicoes para remoção: Opcao de Atributo
     * @param tipoDocumentoComponent 
     */
	private removerOpcoesAtributosExtracao(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		if (this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.opcoesExcluidas !== undefined)) {
			this.tipoDocumento.atributos_extracao.forEach(atributoExtracao => {
				if (atributoExtracao.opcoesExcluidas) {
					atributoExtracao.opcoesExcluidas.forEach(opcaoAtributo => {
						this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos.push(this.tipoDocumentoService.deleteOpcao(this.tipoDocumentoModel.ultimoIdTipoDocumento, atributoExtracao.id, opcaoAtributo.id));
					});
				}
			});
			if (this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos.length > 0) {
				this.forkJoinRemoveOpcaoAtributo(this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos, tipoDocumentoComponent, finalizar);
			}

		}
	}

    /**
     * Prepara as requisicoes para remoção: Atributos
     * @param tipoDocumentoComponent 
     */
	private removerAtributosExtracao(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		if (this.tipoDocumento.atributosExcluidos) {
			this.tipoDocumento.atributosExcluidos.forEach(atributoExtracao => {
				this.tipoDocumentoModel.requesicoesAtributosRemovidos.push(this.tipoDocumentoService.deleteAtributo(this.tipoDocumentoModel.ultimoIdTipoDocumento, atributoExtracao.id));
			});
			if (this.tipoDocumentoModel.requesicoesAtributosRemovidos.length > 0) {
				this.forkJoinRemoveAtributos(this.tipoDocumentoModel.requesicoesAtributosRemovidos, tipoDocumentoComponent, finalizar);
			}
		}
	}

	/**
	 * Salva os atributos e opções atributos.
	 * Atributo: POST e PATCH
	 * OpcaoAtributo: POST
	 * @param tipoDocumentoComponent 
	 * @param finalizar 
	 */
	private salvarAtributoExtracao(tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		this.tipoDocumento.atributos_extracao.forEach(atributoExtracao => {
			if (!atributoExtracao.id) {
				this.tipoDocumentoModel.requesicoesIncludeAtributos.push(this.tipoDocumentoService.saveAtributo(this.tipoDocumentoModel.ultimoIdTipoDocumento, this.inicializarIncludeAtributoExtracao(atributoExtracao)));
			}
			if (atributoExtracao.id && atributoExtracao.alterado && atributoExtracao.objetoAlterado) {
				this.tipoDocumentoModel.requesicoesUpdateAtributos.push(this.tipoDocumentoService.updateAtributo(this.tipoDocumentoModel.ultimoIdTipoDocumento, atributoExtracao.id, this.inicializarUpdateAtributoExtracao(atributoExtracao.objetoAlterado)));
			}
		});
		const reqIncludeAtributo: number = this.tipoDocumentoModel.requesicoesIncludeAtributos.length;
		const reqUpdareAtributo: number = this.tipoDocumentoModel.requesicoesUpdateAtributos.length;
		if (reqIncludeAtributo > 0) {
			this.forkJoinIncludeAtributosExtracao(this.tipoDocumentoModel.requesicoesIncludeAtributos, tipoDocumentoComponent, finalizar);
		}
		if (reqUpdareAtributo > 0) {
			this.forkJoinUpdateAtributosExtracao(this.tipoDocumentoModel.requesicoesUpdateAtributos, tipoDocumentoComponent, finalizar);
		}
		const possuiOpcoesAtributos: boolean = this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.opcoes_atributo && atributoExtracao.opcoes_atributo.length > 0);
		if (reqIncludeAtributo == 0 && reqUpdareAtributo == 0 && possuiOpcoesAtributos) {
			this.salvarOpcaoAtributoExtracao(undefined, tipoDocumentoComponent, finalizar);
		}
	}

    /**
     * Realiza as requisicoes para remover os opcoes de atributo: DELETE
     * @param requesicoesOpcoesAtributosRemovidos 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private forkJoinRemoveOpcaoAtributo(requesicoesOpcoesAtributosRemovidos: Array<Observable<any>>, tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		forkJoin(requesicoesOpcoesAtributosRemovidos).subscribe(() => {
			this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
			this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos = new Array<Observable<any>>(0);
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Realiza as requisicoes para remover os atributosExtracao: DELETE
     * @param requesicoesAtributosRemovidos 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private forkJoinRemoveAtributos(requesicoesAtributosRemovidos: Observable<any>[], tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		forkJoin(requesicoesAtributosRemovidos).subscribe(() => {
			this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Realiza as requisicoes para atualizar os atributos: PATCH
     * @param requesicoesIncludeAtributoExtracao 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private forkJoinUpdateAtributosExtracao(requesicoesUpdateAtributoExtracao: Observable<UpdateAtributoExtracao>[], tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		forkJoin(requesicoesUpdateAtributoExtracao).subscribe(() => {
			const possuiOpcoesAtributos: boolean = this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.opcoes_atributo && atributoExtracao.opcoes_atributo.length > 0);
			if (this.verificarPreenchimentoFormulario([this.tipoDocumentoModel.requesicoesAtributosRemovidos, this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos]) && !possuiOpcoesAtributos) {
				this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
			} else {
				this.salvarOpcaoAtributoExtracao(undefined, tipoDocumentoComponent, finalizar);
			}
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Realiza as requisicoes para salvar as opcoes atributos: POST
     * @param requesicoesIncludeOpcoesAtributos 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private forkJoinIncludeOpcaoAtributosExtracao(requesicoesIncludeOpcoesAtributos: Array<Observable<ModalOpcaoAtributoModel>>, tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		forkJoin(requesicoesIncludeOpcoesAtributos).subscribe(() => {
			this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos = new Array<Observable<ModalOpcaoAtributoModel>>(0);
			if (this.verificarPreenchimentoFormulario([this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos])) {
				this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
			}
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Realiza as requisicoes para salvar os atributos: POST
     * @param requesicoesIncludeAtributoExtracao 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private forkJoinIncludeAtributosExtracao(requesicoesIncludeAtributoExtracao: Observable<IncludeAtributoExtracao>[], tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		forkJoin(requesicoesIncludeAtributoExtracao).subscribe(ultimosIdsAtributoExtracao => {
			const possuiOpcoesAtributos: boolean = this.tipoDocumento.atributos_extracao.some(atributoExtracao => atributoExtracao.opcoes_atributo && atributoExtracao.opcoes_atributo.length > 0);
			if (this.verificarPreenchimentoFormulario([this.tipoDocumentoModel.requesicoesUpdateAtributos, this.tipoDocumentoModel.requesicoesAtributosRemovidos, this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos]) && !possuiOpcoesAtributos) {
				this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
			} else {
				this.salvarOpcaoAtributoExtracao(ultimosIdsAtributoExtracao, tipoDocumentoComponent, finalizar);
			}
		}, error => {
			this.loadService.hide();
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

	/**
	 * Realiza o POST para as opcoes dos atributos extração
	 * @param ultimosIdsAtributoExtracao 
	 * @param tipoDocumentoComponent 
	 * @param finalizar 
	 */
	private salvarOpcaoAtributoExtracao(ultimosIdsAtributoExtracao: IncludeAtributoExtracao[], tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		if (ultimosIdsAtributoExtracao) {
			ultimosIdsAtributoExtracao.forEach(idAtributoExtracao => {
				this.tipoDocumento.atributos_extracao.forEach(atributoExtracao => {
					if (atributoExtracao.opcoes_atributo) {
						atributoExtracao.opcoes_atributo.forEach(opcaoAtributo => {
							if (!opcaoAtributo.id) {
								this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos.push(this.tipoDocumentoService.saveOpcao(this.tipoDocumentoModel.ultimoIdTipoDocumento, Number(idAtributoExtracao), this.inicializarIncludeOpcaoAtributoExtracao(opcaoAtributo)));
							}
						});
					}
				});
			});
		} else {
			this.tipoDocumento.atributos_extracao.forEach(atributoExtracao => {
				if (atributoExtracao.opcoes_atributo) {
					atributoExtracao.opcoes_atributo.forEach(opcaoAtributo => {
						if (!opcaoAtributo.id) {
							this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos.push(this.tipoDocumentoService.saveOpcao(this.tipoDocumentoModel.ultimoIdTipoDocumento, atributoExtracao.id, this.inicializarIncludeOpcaoAtributoExtracao(opcaoAtributo)));
						}
					});
				}
			});
		}
		if (this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos.length > 0) {
			this.forkJoinIncludeOpcaoAtributosExtracao(this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos, tipoDocumentoComponent, finalizar);
		} else {
			this.verificarTipoFinalizacao(finalizar, this.tipoDocumentoModel.ultimoIdTipoDocumento, tipoDocumentoComponent);
		}
	}

    /**
     * evento sucesso ao salvar tipo documento
     * @param ultimoIdTipoDocumento 
     * @param tipoDocumentoComponent 
	 * @param finalizar 
     */
	private onSucessSaveTipoDocumento(ultimoIdChecklist: any, tipoDocumentoComponent: TipoDocumentoComponent, finalizar: boolean) {
		const id: number = Number(ultimoIdChecklist);
		this.tipoDocumentoModel.ultimoIdTipoDocumento = id;
		this.salvarAtributoExtracao(tipoDocumentoComponent, finalizar);
		if (this.verificarPreenchimentoFormulario([this.tipoDocumentoModel.requesicoesIncludeAtributos])) {
			this.verificarTipoFinalizacao(finalizar, id, tipoDocumentoComponent);
		}
	}

	/**
	 * Checagem de tipo de atualização: finalizar = true mantem na pagina; caso contrário redireciona para a pagina pesquisa
	 * grava no serviço de tipo documento referencias para utilizacao na pesquisa de tipo documento
	 * @param finalizar 
	 * @param idTipoDoc 
	 * @param tipoDocumentoComponent 
	 */
	private verificarTipoFinalizacao(finalizar: boolean, idTipoDoc: number, tipoDocumentoComponent: TipoDocumentoComponent) {
		const edicao: boolean = this.tipoDocumentoModel.editar;
		if (edicao) {
			this.tipoDocumentoService.setEdicaoTipoDocumento(true);
			this.tipoDocumentoService.setNovoTipoDocumento(false);
			this.tipoDocumentoService.setTipoDocumentoSemAlteracao(false);
		} else {
			this.tipoDocumentoService.setNovoTipoDocumento(true);
			this.tipoDocumentoService.setEdicaoTipoDocumento(false);
			this.tipoDocumentoService.setTipoDocumentoSemAlteracao(false);
		}
		this.tipoDocumentoService.setId(idTipoDoc);
		if (finalizar) {
			this.redirecionarParaPesquisaTipoDocumento();
		} else {
			this.carregarNovoTipoDocumento(edicao, idTipoDoc, tipoDocumentoComponent);
		}
		this.limparRequisicoes();
	}

	/**
	 * Limpa todos os arrays de requisicoes
	 */
	private limparRequisicoes() {
		this.tipoDocumentoModel.requesicoesIncludeAtributos = new Array<Observable<IncludeAtributoExtracao>>(0);
		this.tipoDocumentoModel.requesicoesUpdateAtributos = new Array<Observable<UpdateAtributoExtracao>>(0);
		this.tipoDocumentoModel.requesicoesIncludeOpcoesAtributos = new Array<Observable<ModalOpcaoAtributoModel>>(0);
		this.tipoDocumentoModel.requesicoesAtributosRemovidos = new Array<Observable<any>>(0);
		this.tipoDocumentoModel.requesicoesOpcoesAtributosRemovidos = new Array<Observable<any>>(0);
	}

	/**
	 * carregamento novo tipo documento; fica na tela quando salvar e continuar
	 * @param edicao 
	 * @param id 
	 * @param tipoDocumentoComponent 
	 */
	private carregarNovoTipoDocumento(edicao: boolean, id: number, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.loadService.show();
		this.tipoDocumentoService.getById(id).subscribe(retorno => {
			this.onSucessNovoTipoDocumento(edicao, retorno, id, tipoDocumentoComponent);
		}, error => {
			this.onErrorTipoDocumento(id, error, tipoDocumentoComponent);
		});
	}

	/**
	 * Sucesso ao carregar novo tipo documento
	 * @param edicao 
	 * @param retorno 
	 * @param id 
	 * @param tipoDocumentoComponent 
	 */
	private onSucessNovoTipoDocumento(edicao: boolean, retorno: any, id: number, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.tipoDocumento = retorno;
		this.realizarCopiaTipoDocumento(retorno);
		this.custumizeHeaderGrid();
		this.tipoDocumentoModel.editar = true;
		this.tipoDocumentoModel.showHeader = true;
		let msg: string;
		if (edicao) {
			msg = TIPO_DOCUMENTO.TIPO_DOCUMENTO_ATUALIZADO_SUCESSO.replace('{id}', id.toString());
		} else {
			msg = TIPO_DOCUMENTO.TIPO_DOCUMENTO_ADICIONADO_SUCESSO.replace('{id}', id.toString());
		}

		Utils.ordenarAtributosPelaOrdemApresentacao(this.tipoDocumento.atributos_extracao);

		tipoDocumentoComponent.addMessageSuccess(msg);
		this.router.navigate([TIPO_DOCUMENTO.TIPO_DOCUMENTO, id]);
		this.loadService.hide();
	}

	/**
     * Redireciona para a pesquisa tipo documento
     */
	private redirecionarParaPesquisaTipoDocumento() {
		this.loadService.hide();
		this.navigateUrl([TIPO_DOCUMENTO.TIPO_DOCUMENTO_PESQUISA]);
	}

    /**
     * inicializa dropdown com todas aos tipos de pessoas
     */
	private getTiposPessoas() {
		this.tipoDocumentoModel.tipoPessoas = Utils.getTiposPessoas();
	}


	/**
	 * inicializa o dropdown com todas as funções documentais
	 * @param params 
	 * @param tipoDocumentoComponent 
	 */
	private getFuncoesDocumentais(params: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		let funcoesDocumentais: InterfaceFuncaoDocumental[] = new Array<InterfaceFuncaoDocumental>();
		this.loadService.show();
		this.funcaoDocumentalService.get().subscribe(funcoes => {
			funcoes.forEach((funcaoDocumental, index) => {
				funcoesDocumentais.push({
					index: index, key: funcaoDocumental.identificador_funcao_documental, name: funcaoDocumental.nome_funcao_documental,
					administrativo: funcaoDocumental.indicador_processo_administrativo, digital: funcaoDocumental.indicador_dossie_digital,
					negocio: funcaoDocumental.indicador_apoio_negocio, data: funcaoDocumental.data_hora_ultima_alteracao
				});
			});
			this.tipoDocumentoModel.funcoesDocumentaisInterface = funcoesDocumentais;
			this.carregamentoTiposDocumentos(params, tipoDocumentoComponent);
			this.loadService.hide();
		}, error => {
			this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
		});
	}

    /**
     * Monta o valor para o dropdown tipo pessoa
     */
	private inicializarTipoPessoa() {
		let code: number;
		let name: string;
		let value: string;
		({ code, name, value } = Utils.carregarTipoPessoa(code, name, value, this.tipoDocumento.indicador_tipo_pessoa));
		this.tipoDocumentoModel.selectedTipoPessoa =
			<SelectTipoPessoa>{
				code: code,
				name: name,
				value: value
			};
	}

	/**Inicializa o objeto para salvar o tipo de documento */
	private inicializarIncludeTipoDocumento(): IncludeTipoDocumento {
		const includeTipoDocumento: IncludeTipoDocumento = new IncludeTipoDocumento();
		includeTipoDocumento.nome = this.tipoDocumento.nome;
		includeTipoDocumento.indicador_tipo_pessoa = this.tipoDocumentoModel.selectedTipoPessoa ? this.tipoDocumentoModel.selectedTipoPessoa.value : undefined;
		includeTipoDocumento.indicador_validade_autocontida = this.tipoDocumento.indicador_validade_autocontida;
		includeTipoDocumento.prazo_validade_dias = this.tipoDocumento.prazo_validade_dias;
		includeTipoDocumento.codigo_tipologia = this.tipoDocumento.codigo_tipologia;
		includeTipoDocumento.classe_siecm = this.tipoDocumento.classe_siecm;
		includeTipoDocumento.indicador_reuso = this.tipoDocumento.indicador_reuso;
		includeTipoDocumento.indicador_uso_apoio_negocio = this.tipoDocumento.indicador_uso_apoio_negocio;
		includeTipoDocumento.indicador_uso_dossie_digital = this.tipoDocumento.indicador_uso_dossie_digital;
		includeTipoDocumento.indicador_uso_processo_administrativo = this.tipoDocumento.indicador_uso_processo_administrativo;
		includeTipoDocumento.arquivo_minuta = this.tipoDocumento.arquivo_minuta;
		includeTipoDocumento.indicador_validacao_cadastral = this.tipoDocumento.indicador_validacao_cadastral;
		includeTipoDocumento.indicador_validacao_documental = this.tipoDocumento.indicador_validacao_documental;
		includeTipoDocumento.indicador_validacao_sicod = this.tipoDocumento.indicador_validacao_sicod;
		includeTipoDocumento.indicador_extracao_externa = this.tipoDocumento.indicador_extracao_externa;
		includeTipoDocumento.indicador_extracao_m0 = this.tipoDocumento.indicador_extracao_m0;
		includeTipoDocumento.indicador_multiplos = this.tipoDocumento.indicador_multiplos;
		includeTipoDocumento.avatar = this.tipoDocumento.avatar ? this.tipoDocumento.avatar : 'fa fa-clock-o';
		includeTipoDocumento.cor_box = this.tipoDocumento.cor_box;
		includeTipoDocumento.indicador_guarda_binario_outsourcing = this.tipoDocumento.indicador_guarda_binario_outsourcing;
		includeTipoDocumento.ativo = true;
		includeTipoDocumento.tags = this.tipoDocumento.tags;
		this.inicializarIdentificadoresFuncoesDocumentais(includeTipoDocumento);
		return includeTipoDocumento;
	}

	/**
	 * devolve o array contendo os identificadores das funções documentais adicionadas na grid
	 * @param includeTipoDocumento 
	 */
	private inicializarIdentificadoresFuncoesDocumentais(includeTipoDocumento: IncludeTipoDocumento) {
		includeTipoDocumento.funcoes_documento_inclusao_vinculo = this.tipoDocumentoModel.funcoesDocumentais.reduce((funcoes, gridFuncaoDocumental) => {
			funcoes.push(gridFuncaoDocumental.identificador_funcao_documental);
			return funcoes;
		}, new Array<number>());
	}

    /**
     * Valida a ocorrencia mesmo nome tipo de documento e ic_apoio negocio
     */
	private validarNomeTipoDocumentoApoioNegocio(): boolean {
		if (this.tipoDocumentoModel.editar) {
			const filterTiposDocumentos: Array<GridTipoDocumento> = this.tipoDocumentoModel.gridTipoDocumentos.filter(tipoDocumento => tipoDocumento.id !== this.tipoDocumento.id);
			const tipoDocumento: GridTipoDocumento = filterTiposDocumentos.find(ocorrenciaTipoDocumento => ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_apoio_negocio == true && this.tipoDocumento.indicador_uso_apoio_negocio == true);
			return !tipoDocumento;
		} else {
			const tipoDocumento: GridTipoDocumento = this.tipoDocumentoModel.gridTipoDocumentos.find(ocorrenciaTipoDocumento => this.tipoDocumento.nome && ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_apoio_negocio == true && this.tipoDocumento.indicador_uso_apoio_negocio == true);
			return !tipoDocumento;
		}
	}

    /**
     * Valida a ocorrencia mesmo nome tipo de documento e ic_dossie_digital
     */
	private validarNomeTipoDocumentoDossieDigital(): boolean {
		if (this.tipoDocumentoModel.editar) {
			const filterTiposDocumentos: Array<GridTipoDocumento> = this.tipoDocumentoModel.gridTipoDocumentos.filter(tipoDocumento => tipoDocumento.id !== this.tipoDocumento.id);
			const tipoDocumento: GridTipoDocumento = filterTiposDocumentos.find(ocorrenciaTipoDocumento => ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_dossie_digital == true && this.tipoDocumento.indicador_uso_dossie_digital == true);
			return !tipoDocumento;
		} else {
			const tipoDocumento: GridTipoDocumento = this.tipoDocumentoModel.gridTipoDocumentos.find(ocorrenciaTipoDocumento => this.tipoDocumento.nome && ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_dossie_digital == true && this.tipoDocumento.indicador_uso_dossie_digital == true);
			return !tipoDocumento;
		}
	}

    /**
     * Valida a ocorrencia mesmo nome tipo de documento e ic_processo_administrativo
     */
	private validarNomeTipoDocumentoProcessoAdministrativo(): boolean {
		if (this.tipoDocumentoModel.editar) {
			const filterTiposDocumentos: Array<GridTipoDocumento> = this.tipoDocumentoModel.gridTipoDocumentos.filter(tipoDocumento => tipoDocumento.id !== this.tipoDocumento.id);
			const tipoDocumento: GridTipoDocumento = filterTiposDocumentos.find(ocorrenciaTipoDocumento => ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_processo_administrativo == true && this.tipoDocumento.indicador_uso_processo_administrativo == true);
			return !tipoDocumento;
		} else {
			const tipoDocumento: GridTipoDocumento = this.tipoDocumentoModel.gridTipoDocumentos.find(ocorrenciaTipoDocumento => this.tipoDocumento.nome && ocorrenciaTipoDocumento.nome.toUpperCase().trim() == this.tipoDocumento.nome.toUpperCase().trim() && ocorrenciaTipoDocumento.indicador_uso_processo_administrativo == true && this.tipoDocumento.indicador_uso_processo_administrativo == true);
			return !tipoDocumento;
		}
	}

    /**
     * Valida a ocorrencia mesmo número de tipologia
     */
	private validarTipologiaDocumento(): boolean {
		if (this.tipoDocumentoModel.editar) {
			const filterTiposDocumentos: Array<GridTipoDocumento> = this.tipoDocumentoModel.gridTipoDocumentos.filter(tipoDocumento => tipoDocumento.id !== this.tipoDocumento.id);
			return !filterTiposDocumentos.some(ocorrenciaTipoDocumento => this.tipoDocumento.codigo_tipologia && ocorrenciaTipoDocumento.codigo_tipologia && this.tipoDocumento.codigo_tipologia.trim().toUpperCase() == ocorrenciaTipoDocumento.codigo_tipologia.trim().toUpperCase());
		} else {
			return !this.tipoDocumentoModel.gridTipoDocumentos.some(ocorrenciaTipoDocumento => this.tipoDocumento.codigo_tipologia && ocorrenciaTipoDocumento.codigo_tipologia && ocorrenciaTipoDocumento.codigo_tipologia.trim().toUpperCase() == this.tipoDocumento.codigo_tipologia.trim().toUpperCase());
		}
	}

    /**
     * Carrega a listagem de tiposDocumentos no componente tipoDocumento
     * para validação de ocorrencia de nome
	 * @param params 
	 * @param tipoDocumentoComponent 
     */
	private carregamentoTiposDocumentos(params: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		if (this.tipoDocumentoService.existTipoDocumentos()) {
			this.tipoDocumentoModel.gridTipoDocumentos = this.tipoDocumentoService.getTipoDocumentos();
			this.carregarIconesVerificacaoEdicaoTipoDocumento(params, tipoDocumentoComponent);
		}
		else {
			this.loadService.show();
			this.tipoDocumentoService.get().subscribe(tiposDocumentos => {
				this.tipoDocumentoModel.gridTipoDocumentos = tiposDocumentos;
				this.carregarIconesVerificacaoEdicaoTipoDocumento(params, tipoDocumentoComponent);
				this.loadService.hide();
			}, error => {
				this.onErrorTipoDocumento(undefined, error, tipoDocumentoComponent);
			});
		}
	}

	/**
	 * Carregamentos icones e verificacao edicao tipo documento
	 * @param params 
	 * @param tipoDocumentoComponent 
	 */
	private carregarIconesVerificacaoEdicaoTipoDocumento(params: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.inicializarDropdownIconesFa();
		this.getTiposPessoas();
		this.verificarEdicaoTipoDoumento(params, tipoDocumentoComponent);
	}

    /**
	 * Verifica se é uma edição de Tipo Documento
	 * @param params 
	 * @param tipoDocumentoComponent 
	 */
	private verificarEdicaoTipoDoumento(params: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		let paramIntUrl: boolean = this.checkParamIntUrl(params);
		if (paramIntUrl) {
			this.validarLabelEdicao(paramIntUrl);
			this.tipoDocumentoModel.showHeader = true;
			const id: number = Number(params.id);
			this.carregarTipoDocumento(id, tipoDocumentoComponent);
		} else {
			this.tipoDocumentoModel.showHeader = false;
			this.custumizeRowsPerPageOptions();
		}
	}

    /**
     * Altera a label do cadastro tipoDocumento: Edição de tipo de documento e Novo Tipo de Documento
     * @param paramIntUrl 
     */
	private validarLabelEdicao(paramIntUrl: boolean) {
		this.tipoDocumentoModel.editar = paramIntUrl;
	}

	/**
	 * Recupera o Tipo Documento passando id
	 * @param id 
	 * @param tipoDocumentoComponent 
	 */
	private carregarTipoDocumento(id: number, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.loadService.show();
		this.tipoDocumentoService.getById(id).subscribe(retorno => {
			this.tipoDocumento = retorno;
			this.tipoDocumentoModel.ultimoIdTipoDocumento = id;
			Utils.ordenarAtributosPelaOrdemApresentacao(this.tipoDocumento.atributos_extracao);
			this.realizarCopiaTipoDocumento(retorno);
			this.custumizeHeaderGrid();
			this.inicializarTipoPessoa();
			this.inicializarFuncoesDocumentais();
			this.validarInconssistenciaTipologia();
			this.loadService.hide();
		}, error => {
			this.onErrorTipoDocumento(id, error, tipoDocumentoComponent);
		});
	}

	/**
	 * Faz uma copia do retorno do tipo documento. Serviço PATCH
	 * utilização para comparação se houve alteração no objeto
	 * @param retorno 
	 */
	private realizarCopiaTipoDocumento(retorno: any) {
		this.tipoDocumentoModel.tipoDocumento = Object.assign(new TipoDocumento, retorno);
		this.tipoDocumentoModel.tipoDocumento.tags = Object.assign(new Array<string>(), this.tipoDocumentoModel.tipoDocumento.tags);
		this.tipoDocumentoModel.tipoDocumento.funcoes_documentais = Object.assign(new Array<number>(), this.tipoDocumentoModel.tipoDocumento.funcoes_documentais);
	}

	/**
	 * Carrega as funções documentais na edição de tipo documento
	 * remove os elementos da grid no dropdown de função documental
	 */
	private inicializarFuncoesDocumentais() {
		this.tipoDocumento.funcoes_documentais.forEach(fd => {
			const gridFuncaoDocumental: GridFuncaoDocumental = new GridFuncaoDocumental();
			this.tipoDocumentoModel.funcoesDocumentaisInterface.forEach(fdi => {
				if (fd.id == fdi.key) {
					gridFuncaoDocumental.index = fdi.index;
					gridFuncaoDocumental.identificador_funcao_documental = fdi.key;
					gridFuncaoDocumental.nome_funcao_documental = fdi.name;
					gridFuncaoDocumental.label_indicador_processo_administrativo = fdi.administrativo ? 'SIM' : 'NÃO';
					gridFuncaoDocumental.label_indicador_dossie_digital = fdi.digital ? 'SIM' : 'NÃO';
					gridFuncaoDocumental.label_indicador_apoio_negocio = fdi.negocio ? 'SIM' : 'NÃO';
					gridFuncaoDocumental.indicador_processo_administrativo = fdi.administrativo;
					gridFuncaoDocumental.indicador_dossie_digital = fdi.digital;
					gridFuncaoDocumental.indicador_apoio_negocio = fdi.negocio;
					gridFuncaoDocumental.data_hora_ultima_alteracao = fdi.data;
					gridFuncaoDocumental.persistido = true;
					this.tipoDocumentoModel.funcoesDocumentais.push(gridFuncaoDocumental);
					let index = this.tipoDocumentoModel.funcoesDocumentaisInterface.indexOf(fdi);
					this.tipoDocumentoModel.funcoesDocumentaisInterface.splice(index, 1);
				}
			});
		});
		this.tipoDocumentoModel.funcoesDocumentaisInterface = this.tipoDocumentoModel.funcoesDocumentaisInterface.slice(0);
		this.custumizeRowsPerPageOptions();
	}

	/**
	 * Validar inconssistencia tipologia em edição de tipo documento
	 */
	private validarInconssistenciaTipologia() {
		if (!Number.isInteger(Number(this.tipoDocumento.codigo_tipologia))) {
			this.tipoDocumentoModel.tipoLogiaInconssistente = true;
		} else {
			this.tipoDocumentoModel.tipoLogiaInconssistente = false;
		}
	}

	/**
	 * Lança o erro para o usuário
	 * @param id 
	 * @param error 
	 * @param tipoDocumentoComponent 
	 */
	private onErrorTipoDocumento(id: number, error: any, tipoDocumentoComponent: TipoDocumentoComponent) {
		this.loadService.hide();
		if (error.status == 403) {
			tipoDocumentoComponent.addMessageError(error.error.mensagem);
		} else if (error.status == 404) {
			this.navigateUrl([TIPO_DOCUMENTO.TIPO_DOCUMENTO, id.toString(), TIPO_DOCUMENTO.TIPO_DOCUMENTO_NOT_FOUND]);
		} else {
			tipoDocumentoComponent.addMessageError(JSON.stringify(error));
		}
		throw error;
	}

	/**
	 * Verifica existencia parametro inteiro na URL
	 * @param params 
	 */
	private checkParamIntUrl(params: any): boolean {
		return Utils.checkParamIntUrl(params.id);
	}

	/**
	 * Inicializa os icones FA para dropdown: ícone
	 */
	private inicializarDropdownIconesFa() {
		this.tipoDocumentoModel.icones = this.tipoDocumentoModel.iconesFa;
	}

	/**
	 * Adiciona os valores: ID e data ultima alteracao no header da grid Tipo Documento
	 */
	private custumizeHeaderGrid() {
		Utils.custumizeHeaderGrid(this.tipoDocumentoModel.configGridHeader, this.tipoDocumento.id, this.tipoDocumento.data_hora_ultima_alteracao);
	}

	/**
     * Varre a ocorrencia de todos os filhos do tipo documento: Atributos e OpcaoAtributo
     * Verificando a existencia de atualização para posterior redirecionamento
     * @param observables 
     */
	private verificarPreenchimentoFormulario(observables: Array<Array<Observable<any>>>): boolean {
		return Utils.verificarPreenchimentoObjetosPersistentes(observables);
	}

	/**
	* Custumiza o array de linhas por páginas; 
	* utilizando o padrão: 15, 30, 50 e último registro
	*/
	private custumizeRowsPerPageOptions() {
		this.tipoDocumentoModel.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.tipoDocumentoModel.funcoesDocumentais);
	}
}