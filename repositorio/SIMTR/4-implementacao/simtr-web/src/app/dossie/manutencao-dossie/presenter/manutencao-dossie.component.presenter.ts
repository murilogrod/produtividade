import { LocalStorageDataService } from './../../../services/local-storage-data.service';
import { Injectable } from "@angular/core";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DossieProduto, VinculoCliente } from "src/app/model";
import { VinculoArvoreCliente } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { VinculoArvoreGarantia } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { VinculoArvoreProduto } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { ConversorDocumentosUtil } from "src/app/documento/conversor-documentos/conversor-documentos.util.service";
import { MESSAGE_ALERT_MENU, DOSSIE_PRODUTO, STATUS_FORM, INDEX_ABA_DOCUMENTO_DOSSIE, ALERT_SALVAR_DOSSIE_PRODUTO, TIPO_DOCUMENTO, SITUACAO_DOSSIE_BANCO, ARQUITETURA_SERVICOS, LOCAL_STORAGE_CONSTANTS, SITUACAO_DOSSIE_ATUAL, PERFIL_ACESSO } from "src/app/constants/constants";
import { GerenciadorDocumentoDossieProdutoEditado } from "../gerenciador-documento-dossie-produto/gerenciador-documento-dossie-produto-editado.util";
import { GerenciadorDocumentosDossieProduto } from "../gerenciador-documento-dossie-produto/gerenciador-documento-dossie-produto-util";
import { Utils } from "src/app/utils/Utils";
import { NgForm } from "@angular/forms";
import { AlertMessageService, LoaderService, ApplicationService, AuthenticationService } from "src/app/services";
import { DossieProdutoModelPath } from "src/app/dossie/manutencao-dossie/model-endPoint-dossie-produto/dossieProdutoModelPath";
import { DocumentoNovo } from "../model-endPoint-dossie-produto/documentoNovo";
import { GarantiasInformadas } from "src/app/dossie/manutencao-dossie/model-endPoint-dossie-produto/garantiasInformadas";
import { ElementosConteudo } from "../model-endPoint-dossie-produto/elementos-conteudo";
import { ProdutoContratado } from "../model-endPoint-dossie-produto/produtoContratado";
import { RespostaFormulario } from "../model-endPoint-dossie-produto/respostaFormulario";
import { VinculosPessoas } from "../model-endPoint-dossie-produto/vinculos-pessoas";
import { DossieProdutoModel } from "../model-endPoint-dossie-produto/dossieProdutoModel";
import { DialogService } from "angularx-bootstrap-modal";
import { DossieService } from "../../dossie-service";
import { Router } from "@angular/router";
import { environment } from "src/environments/environment";
declare var $: any;

@Injectable()
export class ManutencaoDossieComponentPresenter extends AlertMessageService {
	
	constructor(
		private appService: ApplicationService,
		private conversorDocUtil: ConversorDocumentosUtil,
		private loadService: LoaderService,
		private dossieService: DossieService,
		private router: Router,
		private dialogService: DialogService,
		private authenticationService: AuthenticationService,
		private localStorageDataService: LocalStorageDataService
	) { super() }

	montarEdicaoDossieProduto(listaVinculoArvore: VinculoArvore[], dossieProduto: DossieProduto) {
		let dossieProdutoAux = dossieProduto;
		listaVinculoArvore.forEach(vinculoArvore => {
			this.verificarVinculoClientePath(vinculoArvore, dossieProdutoAux, dossieProduto);
			this.verificarVinculoGarantiaPath(vinculoArvore, dossieProdutoAux, dossieProduto);
			this.verificarVinculoProcessoPath(vinculoArvore, dossieProdutoAux, dossieProduto);
		});
		return dossieProduto;
	}

	/**
	 * Montar documento Generico
	 * @param documento
	 */
	carregarDocumento(documento: any) {
		let documentoConteudo = new DocumentoNovo();
		documentoConteudo.tipo_documento = documento.tipo_documento ? documento.tipo_documento : documento.tipo_documento.id;
		documentoConteudo.origem_documento = documento.origem_documento;
		documentoConteudo.mime_type = documento.mime_type;
		documentoConteudo.atributos = [];
		documentoConteudo.atributos = documento.atributos;
		documentoConteudo.quantidade_conteudos = documento.quantidade_conteudos;
		if (documento.binario) {
			documentoConteudo.binario = documento.binario;
		}
		return documentoConteudo;
	}

	/**
	 *  Metodo generico para transformar lista de Elementos conteudo
	 * @param elementos Elementos conteudos a serem carregados para envio ao backend
	 */
	carregarElementosConteudo(elementos: any) {
		let elementos_conteudo = [];
		elementos.forEach(elemento => {
			let elementoConteudo = new ElementosConteudo();
			elementoConteudo.identificador_elemento = elemento.identificador_elemento;
			elementoConteudo.documento = this.carregarDocumento(elemento.documento);
			elementos_conteudo.push(elementoConteudo);
		});
		return elementos_conteudo;
	}

	/**
	 * Montar o PATH para alteração do dossie Produto
	 * @param dossieProduto 
	 */
	montarPathDossieProduto(dossieProduto: DossieProduto, opcao: string) {
		let dossieProdutoPath = new DossieProdutoModelPath();
		dossieProdutoPath.cancelamento = dossieProduto.cancelamento;
		dossieProdutoPath.finalizacao = opcao == DOSSIE_PRODUTO.SALVAR_ENVIAR;
		dossieProdutoPath.retorno = dossieProduto.retorno;
		this.addJustificativaQuandoHouver(dossieProduto, dossieProdutoPath);
		this.addElementosConteudos(dossieProduto, dossieProdutoPath);
		this.addGarantiaInformadas(dossieProduto, dossieProdutoPath);
		this.addVinculosPessoas(dossieProduto, dossieProdutoPath);
		this.addProdutosContratados(dossieProduto, dossieProdutoPath);
		this.addRespostaFormulario(dossieProduto, dossieProdutoPath);
		return dossieProdutoPath;
	}

	private addProdutosContratados(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.produtos_contratados && dossieProduto.produtos_contratados.length > 0) {
			dossieProdutoPath.produtos_contratados = [];
			dossieProduto.produtos_contratados.forEach(produto => {
				let produtoPath = new ProdutoContratado();
				produtoPath.exclusao = produto.exclusao;
				produtoPath.id = produto.id;
				produtoPath.elementos_conteudo = [];

				if (produto.elementos_conteudos) {
					produtoPath.elementos_conteudo = this.carregarElementosConteudo(produto.elementos_conteudos);
				}

				if (produto.respostas_formulario && produto.respostas_formulario.length > 0) {
					this.montarFormularioEditavel(produto, produtoPath);
				}

				dossieProdutoPath.produtos_contratados.push(produtoPath);
			});
		}
	}

	/**
	 * Montar vinculos Pessoas Do dossie Produto
	 * @param dossieProduto 
	 * @param dossieProdutoPath 
	 */
	private addVinculosPessoas(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.vinculos_pessoas && dossieProduto.vinculos_pessoas.length > 0) {
			dossieProdutoPath.vinculos_pessoas = [];
			dossieProduto.vinculos_pessoas.forEach(vc_pessoa => {
				let vinculoPessoa = new VinculosPessoas();
				vinculoPessoa.dossie_cliente = vc_pessoa.dossie_cliente;
				vinculoPessoa.tipo_relacionamento = vc_pessoa.tipo_relacionamento;
				vinculoPessoa.exclusao = vc_pessoa.exclusao;
				this.informaSequenciaTitularidade(vc_pessoa, vinculoPessoa);
				this.informaSequenciaTitularidadeAnterior(vc_pessoa, vinculoPessoa);
				this.informaSeEExcluisaoPessoa(vc_pessoa, vinculoPessoa);
				this.informaClienteRelacionado(vc_pessoa, vinculoPessoa);
				this.informaClienteRelacionadoAnterior(vc_pessoa, vinculoPessoa);
				this.informaDocumentosNovos(vc_pessoa, vinculoPessoa);
				this.informaDocumentosReUtilizados(vc_pessoa, vinculoPessoa);
				this.informaListaFormularioAbaVinculo(vinculoPessoa, vc_pessoa, dossieProdutoPath);
			});
		}
	}

	private informaListaFormularioAbaVinculo(vinculoPessoa: VinculosPessoas, vc_pessoa: any, dossieProdutoPath: DossieProdutoModelPath) {
		vinculoPessoa.respostas_formulario = [];
		if (vc_pessoa.respostas_formulario && vc_pessoa.respostas_formulario.length > 0) {
			this.montarFormularioEditavel(vc_pessoa, vinculoPessoa);
		}
		dossieProdutoPath.vinculos_pessoas.push(vinculoPessoa);
	}

	private montarFormularioEditavel(vinculo: any, vinculoPath: any) {
		if (vinculo.respostas_formulario && vinculo.respostas_formulario.length > 0) {
			vinculoPath.respostas_formulario = [];
			vinculo.respostas_formulario.forEach(campo => {
				if (!campo.id_campo_formulario) {
					vinculoPath.respostas_formulario.push(campo);
				}
			});
		}
	}

	/**
	 * Montar Documentos Novos do Vinculo Pessoa
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaDocumentosNovos(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.documentos_novos && vc_pessoa.documentos_novos.length > 0) {
			vinculoPessoa.documentos_novos = [];
			vc_pessoa.documentos_novos.forEach(dcNovos => {
				let documento = new DocumentoNovo();
				documento.atributos = dcNovos.atributos;
				documento.binario = (dcNovos.conteudos && dcNovos.conteudos.length > 0) ? dcNovos.conteudos[0].img : dcNovos.binario;
				documento.mime_type = dcNovos.mime_type;
				documento.origem_documento = dcNovos.origem_documento;
				documento.tipo_documento = dcNovos.tipo_documento;
				documento.quantidade_conteudos = dcNovos.quantidade_conteudos;
				vinculoPessoa.documentos_novos.push(documento);
			});
		}
	}

	/**
	 * Montar Documentos ReUtilizados pasasndo uma lista de IDS
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaDocumentosReUtilizados(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.documentos_utilizados && vc_pessoa.documentos_utilizados.length > 0) {
			vinculoPessoa.documentos_utilizados = [];
			vc_pessoa.documentos_utilizados.forEach(vcDcUtilizados => {
				vinculoPessoa.documentos_utilizados.push(vcDcUtilizados);
			});
		}
	}

	/**
	 * Se houver Cliente Relacionado Anterior Método para o Patch de alteração
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaClienteRelacionadoAnterior(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.dossie_cliente_relacionado_anterior) {
			vinculoPessoa.dossie_cliente_relacionado_anterior = vc_pessoa.dossie_cliente_relacionado_anterior;
		}
	}

	/**
	 * Se houver Informa o Cliente Relacionado Patch alteração
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaClienteRelacionado(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.dossie_cliente_relacionado) {
			vinculoPessoa.dossie_cliente_relacionado = vc_pessoa.dossie_cliente_relacionado;
		}
	}

	/**
	 * Informa se Esta excluido o vinculo Pessoa
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaSeEExcluisaoPessoa(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.exclusao) {
			vinculoPessoa.exclusao = vc_pessoa.exclusao;
		}
	}

	/**
	 * Informa a sequencia de titularidade caso houver
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaSequenciaTitularidade(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.sequencia_titularidade) {
			vinculoPessoa.sequencia_titularidade = vc_pessoa.sequencia_titularidade;
		}
	}

	/**
	 * Caso seja uma lateração do sequencial informa o anterior
	 * @param vc_pessoa 
	 * @param vinculoPessoa 
	 */
	private informaSequenciaTitularidadeAnterior(vc_pessoa: any, vinculoPessoa: VinculosPessoas) {
		if (vc_pessoa.informaSequenciaTitularidadeAnterior) {
			vinculoPessoa.sequencia_titularidade_anterior = vc_pessoa.informaSequenciaTitularidadeAnterior;
		}
	}

	/**
	 * insere as resposta do Formulario ao endPoind do PATCH de alteração
	 * @param dossieProduto 
	 * @param dossieProdutoPath 
	 */
	private addRespostaFormulario(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.respostas_formulario && dossieProduto.respostas_formulario.length > 0) {
			dossieProdutoPath.respostas_formulario = [];
			dossieProdutoPath.respostas_formulario = dossieProduto.respostas_formulario;
		}
	}

	/**
	 * Adicionar Garantias informada para o dossie Produto
	 * @param dossieProduto 
	 */
	private addGarantiaInformadas(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.garantias_informada && dossieProduto.garantias_informada.length > 0) {
			dossieProdutoPath.garantias_informadas = [];
			dossieProduto.garantias_informada.forEach(garant => {
				let garantia = new GarantiasInformadas();
				garantia.id = garant.id;
				garantia.identificador_garantia = garant.garantia;
				garantia.valor_garantia = garant.valor_garantia;

				if (garant.respostas_formulario && garant.respostas_formulario.length > 0) {
					this.montarFormularioEditavel(garant, garantia);
				}

				this.informaExcluisaoGarantia(garant, garantia);
				this.informaGarantiaProduto(garant, garantia);
				this.informaClienteFidjussoria(garant, garantia);
				this.addDocumentosNovosGarantias(garantia, garant);
				dossieProdutoPath.garantias_informadas.push(garantia)
			});
		}
	}

	/**
	 * adicionar clientes avalista da garantia informada
	 * @param garant 
	 * @param garantia 
	 */
	private informaClienteFidjussoria(garant: any, garantia: GarantiasInformadas) {
		if (garant.clientes_avalistas && garant.clientes_avalistas.length > 0) {
			garantia.clientes_avalistas = garant.clientes_avalistas;
		}
	}

	private informaGarantiaProduto(garant: any, garantia: GarantiasInformadas) {
		if (garant.produto) {
			garantia.identificador_produto = garant.produto;
		}
	}

	private informaExcluisaoGarantia(garant: any, garantia: GarantiasInformadas) {
		if (garant.exclusao) {
			garantia.exclusao = garant.exclusao;
		}
	}

	/**
	 * Add Elementos Conteudos com dados nescessarios para PATH de edição
	 * @param dossieProduto 
	 */
	private addElementosConteudos(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.elementos_conteudos && dossieProduto.elementos_conteudos.length > 0) {
			dossieProdutoPath.elementos_conteudo = [];
			dossieProdutoPath.elementos_conteudo = this.carregarElementosConteudo(dossieProduto.elementos_conteudos);
		}
	}

	/**
	 * Se for revogação deve havera uma Justificativa
	 * @param dossieProduto 
	 * @param dossieProdutoPath 
	 */
	private addJustificativaQuandoHouver(dossieProduto: DossieProduto, dossieProdutoPath: DossieProdutoModelPath) {
		if (dossieProduto.justificativa && dossieProduto.justificativa != "") {
			dossieProdutoPath.justificativa = dossieProduto.justificativa;
		}
	}

	/**
	 * Pega a lista de PNG e converte em PDF
	 * @param dossieProduto 
	 */
	encontrarListadeImgPngEConvertePDF(dossieProduto: DossieProduto) {
		if (dossieProduto.garantias_informada && dossieProduto.garantias_informada.length > 0) {
			let garInformadasAux = Object.assign(dossieProduto.garantias_informada);
			for (let i = 0; i < garInformadasAux.length; i++) {
				if (dossieProduto.garantias_informada[i].documento) {
					dossieProduto.garantias_informada[i].documento.forEach(documento => {
						this.conversorDocUtil.converteDocumentosDoosieProduto(documento);
					});
				}
			}
		}
		if (dossieProduto.elementos_conteudos && dossieProduto.elementos_conteudos.length > 0) {
			let elmentConteudosAux = Object.assign(dossieProduto.elementos_conteudos);
			for (let i = 0; i < elmentConteudosAux.length; i++) {
				this.conversorDocUtil.converteDocumentosDoosieProduto(dossieProduto.elementos_conteudos[i].documento);
			}
		}
		if (dossieProduto.produtos_contratados && dossieProduto.produtos_contratados.length > 0) {
			dossieProduto.produtos_contratados.forEach(produto => {
				if (produto.elementos_conteudos && produto.elementos_conteudos.length > 0) {
					let auxProduto = Object.assign(produto.elementos_conteudos);
					for (let i = 0; i < auxProduto.length; i++) {
						this.conversorDocUtil.converteDocumentosDoosieProduto(produto.elementos_conteudos[i].documento);
					}
				}
			});
		}
		if (dossieProduto.vinculos_pessoas && dossieProduto.vinculos_pessoas.length > 0) {
			dossieProduto.vinculos_pessoas.forEach(pessoa => {
				if (pessoa.documentos_novos && pessoa.documentos_novos.length > 0) {
					let docnovosAux = Object.assign(pessoa.documentos_novos);
					for (let i = 0; i < docnovosAux.length; i++) {
						this.conversorDocUtil.converteDocumentosDoosieProduto(pessoa.documentos_novos[i]);
					}
				}
			});
		}
	}

	/**
	 * Verificar se Vinculo Produto foi alterado.
	 * @param vinculoArvore 
	 * @param dossieProdutoAux 
	 * @param dossieProduto 
	 */
	private verificarVinculoProcessoPath(vinculoArvore: VinculoArvore, dossieProdutoAux: DossieProduto, dossieProduto: DossieProduto) {
		if (vinculoArvore instanceof VinculoArvoreProduto) {
			if (!vinculoArvore.alterandoVinculo) {
				dossieProdutoAux.produtos_contratados.forEach((produto, index) => {
					if (produto.codigo_operacao == vinculoArvore.vinculoProduto.codigo_operacao && produto.id == vinculoArvore.vinculoProduto.id) {
						dossieProduto.produtos_contratados.splice(index, 1);
					}
				});
			}
		}
	}

	/**
	 * Verificar se Vinculo Garantia foi alterado.
	 * @param vinculoArvore 
	 * @param dossieProdutoAux 
	 * @param dossieProduto 
	 */
	private verificarVinculoGarantiaPath(vinculoArvore: VinculoArvore, dossieProdutoAux: DossieProduto, dossieProduto: DossieProduto) {
		if (vinculoArvore instanceof VinculoArvoreGarantia) {
			if (!vinculoArvore.alterandoVinculo) {
				dossieProdutoAux.garantias_informada.forEach((garantia, index) => {
					if (garantia.codigo_bacen == vinculoArvore.vinculoGarantia.codigo_bacen && garantia.id == vinculoArvore.vinculoGarantia.id) {
						dossieProduto.garantias_informada.splice(index, 1);
					}
				});
			}
		}
	}
	/**
	 * verificar vinculo Cliente
	 * @param vinculoArvore 
	 * @param dossieProdutoAux 
	 * @param dossieProduto 
	 */
	private verificarVinculoClientePath(vinculoArvore: VinculoArvore, dossieProdutoAux: DossieProduto, dossieProduto: DossieProduto) {
		if (vinculoArvore instanceof VinculoArvoreCliente) {
			if (!vinculoArvore.alterandoVinculo) {
				dossieProdutoAux.vinculos_pessoas.forEach((pessoa, index) => {
					let idTipo = pessoa.tipo_relacionamento.id ? pessoa.tipo_relacionamento.id : pessoa.tipo_relacionamento;
					if (idTipo == vinculoArvore.vinculoCliente.tipo_relacionamento.id && pessoa.dossie_cliente == vinculoArvore.vinculoCliente.id) {
						dossieProduto.vinculos_pessoas.splice(index, 1);
					}
				});
			}
		}
	}

	/**
	 * Verificar se existe retorno do back como Erro e caso não exista manda um statico
	 * @param error 
	 */
	tratarErrorSalvarOuEditar(error: any): string {
		return (error.error && error.error.mensagem) ? error.error.mensagem : MESSAGE_ALERT_MENU.MSG_ERRO_SALVAR_DOSSIE;
	}


	montarNovoDossieProdutoOuEditar(dossieProduto: DossieProduto, novoDossieProduto: DossieProduto, opcao: any, campoFormulario: any, justificativaRevogar: any) {

		if (this.verificaDossieProdutoJaExiste(dossieProduto)) {
			novoDossieProduto = GerenciadorDocumentoDossieProdutoEditado.criaDossieProdutoEdicao(campoFormulario, opcao, novoDossieProduto.justificativa);
			novoDossieProduto.justificativa = (opcao === DOSSIE_PRODUTO.REVOGAR) ? justificativaRevogar : null;
			if (novoDossieProduto.justificativa) {
				dossieProduto.situacao_atual = SITUACAO_DOSSIE_ATUAL.CANCELADO;
			}
			novoDossieProduto.situacaoComplementacao = (dossieProduto && dossieProduto.situacaoComplementacao) ? dossieProduto.situacaoComplementacao : false;
		} else {
			novoDossieProduto = GerenciadorDocumentosDossieProduto.criaDossieProduto(campoFormulario, opcao);
			novoDossieProduto.situacaoComplementacao = (dossieProduto && dossieProduto.situacaoComplementacao) ? dossieProduto.situacaoComplementacao : false;
		}
		return novoDossieProduto;
	}

	/**
	 * Verifica se é um caso de dossiê produto em edição.
	 */
	verificaDossieProdutoJaExiste(dossieProduto): boolean {
		if (dossieProduto != undefined) {
			return true;
		}
		return false;
	}


	/**
	 * Responsavel de validar se a sequencia de Titularidade esta correta quando ouver
	 */
	private validarSequenciaTitularidade(clienteLista: VinculoCliente[]) {
		let existeFuro = false;
		let qtd = 0;
		let arraySequencial = [];
		clienteLista.forEach(cli => {
			if (undefined != cli.sequencia_titularidade) {
				arraySequencial.push(cli.sequencia_titularidade);
			}
		});

		Utils.ordenaArrayOrdemCrescente(arraySequencial);
		qtd = arraySequencial.length;
		arraySequencial.forEach((numero, idx) => {
			if (numero != (idx + 1)) {
				existeFuro = true;
			}
		});

		return { existeFuro, qtd };
	}

	validarQuandoRascunhoFalse(breackValidar: boolean, msg: any[], novoDossieProduto: DossieProduto, index: number, clienteLista: VinculoCliente[], formValidacao: NgForm, processo: any, processoAtivo: number, msgValidacao: any[]) {
		msg = [];
		if (!novoDossieProduto.rascunho && !novoDossieProduto.cancelamento) {
			let qtd = 0;
			let existeFuro;
			({ existeFuro, qtd } = this.validarSequenciaTitularidade(clienteLista));
			if (!breackValidar && existeFuro) {
				index = INDEX_ABA_DOCUMENTO_DOSSIE.ABA_VINCULO;
				msg.push(ALERT_SALVAR_DOSSIE_PRODUTO.SEQUENCIA_TITULARIDADE_FALHA.replace('$$', (qtd + 1).toString()));
				breackValidar = true;
			}
			if (!breackValidar && undefined != formValidacao && formValidacao.form.status === STATUS_FORM.VALID && novoDossieProduto.camposValidado) {
				if (novoDossieProduto.respostas_formulario) {
					for (let campoForm of novoDossieProduto.respostas_formulario) {
						let dossieAtual;
						for (let macroProcess of processo.processos_filho) {
							if (macroProcess.processos_filho) {
								for (let process of macroProcess.processos_filho) {
									if (process.id === processoAtivo) {
										dossieAtual = process;
									}
								}
							}
							else {
								if (macroProcess.id === processoAtivo) {
									dossieAtual = macroProcess;
								}
							}
						}
						let { idCampoEmail, idCampoEmailObrigatorio } = this.buscarInformacoesRelativoAoEmail(dossieAtual);
						if (!breackValidar && idCampoEmail == campoForm.campo_formulario && !Utils.validarEmail(campoForm.resposta) && idCampoEmailObrigatorio) {
							index = INDEX_ABA_DOCUMENTO_DOSSIE.ABA_FORMULARIO;
							msg.push(ALERT_SALVAR_DOSSIE_PRODUTO.CAMPOS_FORMULARIO_EMAIL_OBRIGATORIO);
							breackValidar = true;
						}
					}
				}
			}
			else if (!breackValidar && undefined != formValidacao && formValidacao.form.status === STATUS_FORM.INVALID) {
				index = INDEX_ABA_DOCUMENTO_DOSSIE.ABA_FORMULARIO;
				msg.push(ALERT_SALVAR_DOSSIE_PRODUTO.CAMPOS_FORMULARIO_OBRIGATORIO);
				breackValidar = true;
			}
			if (!breackValidar && undefined != novoDossieProduto.indexAba) {
				index = novoDossieProduto.indexAba;
				msgValidacao = [];
				if (novoDossieProduto.listaDocumentosFalta && novoDossieProduto.listaDocumentosFalta.length > 0) {
					this.mensagemlistaDocumentoObrigatorio(novoDossieProduto, msg);
				}
				else {
					msg.push(msg);
				}
				breackValidar = true;
			}
		}
		return { msg, index };
	}

	private mensagemlistaDocumentoObrigatorio(novoDossieProduto: DossieProduto, msg: any[]) {
		let listaItemMostrado = [];
		novoDossieProduto.listaDocumentosFalta.forEach(msglst => {
			if (listaItemMostrado.length > 0) {
				let existeList = listaItemMostrado.filter(ex => ex.msg == msglst.msg);
				if (existeList.length == 0) {
					msg.push(msglst.msg);
					listaItemMostrado.push(msglst);
				}

			} else {
				msg.push(msglst.msg);
				listaItemMostrado.push(msglst);
			}
		});
		this.loadService.hide();
	}

	private buscarInformacoesRelativoAoEmail(dossieAtual: any) {
		let idCampoEmail;
		let idCampoEmailObrigatorio;
		if (dossieAtual.campos_formulario.some(f => f.tipo_campo == TIPO_DOCUMENTO.EMAIL)) {
			idCampoEmail = dossieAtual.campos_formulario.find(f => f.tipo_campo == TIPO_DOCUMENTO.EMAIL).codigo_campo;
			idCampoEmailObrigatorio = dossieAtual.campos_formulario.find(f => f.tipo_campo == TIPO_DOCUMENTO.EMAIL).obrigatorio;
		}
		return { idCampoEmail, idCampoEmailObrigatorio };
	}

	validarExistenciaVinculoClientePrincipal(listaVinculoArvore: Array<VinculoArvore>) {
		let existe;
		for (let vinculoArvore of listaVinculoArvore) {
			if (vinculoArvore instanceof VinculoArvoreCliente) {
				existe = vinculoArvore.vinculoCliente.principal;
				if (existe) {
					break;
				}
			}
		}
		return existe;
	}

	validarEmail(email: string) {
		return Utils.validarEmail(email);
	}

	/**
	 * Montar Post para salvar dossie Produto a primeira vez
	 * @param dossieProduto 
	 */
	carregarProdutosCadastrados(dossieProduto: DossieProduto) {
		let dossieProdutoObj = new DossieProdutoModel();
		dossieProdutoObj.rascunho = dossieProduto.rascunho;
		dossieProdutoObj.processo_origem = dossieProduto.processo_origem;
		dossieProdutoObj.produtos_contratados = [];
		dossieProdutoObj.vinculos_pessoas = [];
		dossieProdutoObj.garantias_informadas = [];
		dossieProdutoObj.respostas_formulario = [];
		dossieProdutoObj.elementos_conteudo = [];

		this.montarElementosConteudosProcessoFaseOuDossie(dossieProduto, dossieProdutoObj);
		this.montarProdutosContratados(dossieProduto, dossieProdutoObj);
		this.montarVinculoPessoa(dossieProduto, dossieProdutoObj);
		this.montarGarantiasInformadas(dossieProduto, dossieProdutoObj);
		this.montarRespostaFormulario(dossieProduto, dossieProdutoObj);
		return dossieProdutoObj;
	}

	private montarVinculoPessoa(dossieProduto: DossieProduto, dossieProdutoObj: DossieProdutoModel) {
		if (dossieProduto.vinculos_pessoas && dossieProduto.vinculos_pessoas.length > 0) {
			dossieProduto.vinculos_pessoas.forEach(pessoa => {
				let pessoaConteudo = new VinculosPessoas();
				pessoaConteudo.dossie_cliente = pessoa.dossie_cliente;
				pessoaConteudo.dossie_cliente_relacionado = pessoa.dossie_cliente_relacionado;
				pessoaConteudo.exclusao = pessoa.exclusao;
				pessoaConteudo.sequencia_titularidade = pessoa.sequencia_titularidade;
				pessoaConteudo.tipo_relacionamento = pessoa.tipo_relacionamento;
				this.addRespostaFormularioVinculoPessoa(pessoaConteudo, pessoa);
				pessoaConteudo.documentos_novos = this.carregarDocumentosVinculo(pessoa.documentos_novos);
				if (pessoa.documentos_utilizados && pessoa.documentos_utilizados.length > 0) {
					pessoaConteudo.documentos_utilizados = [];
					this.informaDocumentosReUtilizados(pessoa, pessoaConteudo);
				}
				delete pessoaConteudo.dossie_cliente_relacionado_anterior;
				delete pessoaConteudo.sequencia_titularidade_anterior;
				dossieProdutoObj.vinculos_pessoas.push(pessoaConteudo);
			});
		}
	}

	private addRespostaFormularioVinculoPessoa(pessoaConteudo: VinculosPessoas, pessoa: any) {
		pessoaConteudo.respostas_formulario = [];
		if (pessoa.respostas_formulario && pessoa.respostas_formulario.length > 0) {
			pessoaConteudo.respostas_formulario = pessoa.respostas_formulario;
		}
	}

	private montarRespostaFormulario(dossieProduto: DossieProduto, dossieProdutoObj: DossieProdutoModel) {
		if (dossieProduto.respostas_formulario && dossieProduto.respostas_formulario.length > 0) {
			dossieProduto.respostas_formulario.forEach(resposta => {
				let respostaConteudo = new RespostaFormulario();
				respostaConteudo.campo_formulario = resposta.campo_formulario;
				respostaConteudo.resposta = resposta.resposta;
				if (resposta.opcoes_selecionadas) {
					respostaConteudo.opcoes_selecionadas = resposta.opcoes_selecionadas;
				}
				dossieProdutoObj.respostas_formulario.push(respostaConteudo);
			});
		}
	}

	private montarGarantiasInformadas(dossieProduto: DossieProduto, dossieProdutoObj: DossieProdutoModel) {
		if (dossieProduto.garantias_informada && dossieProduto.garantias_informada.length > 0) {
			dossieProduto.garantias_informada.forEach(garantia => {
				let garantiaConteudo = new GarantiasInformadas();
				garantiaConteudo.clientes_avalistas = garantia.clientes_avalistas;
				garantiaConteudo.identificador_garantia = garantia.garantia;
				garantiaConteudo.identificador_produto = garantia.produto;
				garantiaConteudo.valor_garantia = garantia.valor_garantia;

				if (garantia.respostas_formulario && garantia.respostas_formulario.length > 0) {
					garantiaConteudo.respostas_formulario = garantia.respostas_formulario;
				}

				//  garantiaConteudo.documentos_utilizados = garantia.documentos_utilizados;
				this.addDocumentosNovosGarantias(garantiaConteudo, garantia);
				dossieProdutoObj.garantias_informadas.push(garantiaConteudo);
			});
		}
	}

	private addDocumentosNovosGarantias(garantiaConteudo: GarantiasInformadas, garantia: any) {
		garantiaConteudo.documentos_novos = [];
		if (garantia.documento && garantia.documento.length > 0) {
			garantia.documento.forEach(documento => {
				garantiaConteudo.documentos_novos.push(this.carregarDocumento(documento));
			});
		}
	}

	private montarProdutosContratados(dossieProduto: DossieProduto, dossieProdutoObj: DossieProdutoModel) {
		if (dossieProduto.produtos_contratados && dossieProduto.produtos_contratados.length > 0) {
			dossieProduto.produtos_contratados.forEach(produto => {
				let novoProduto = new ProdutoContratado();
				novoProduto.id = produto.id;
				novoProduto.elementos_conteudo = [];

				if (produto.elementos_conteudos) {
					novoProduto.elementos_conteudo = this.carregarElementosConteudo(produto.elementos_conteudos);
				}

				if (produto.respostas_formulario && produto.respostas_formulario.length > 0) {
					novoProduto.respostas_formulario = produto.respostas_formulario;
				}

				dossieProdutoObj.produtos_contratados.push(novoProduto);
			});
		}
	}

	private montarElementosConteudosProcessoFaseOuDossie(dossieProduto: DossieProduto, dossieProdutoObj: DossieProdutoModel) {
		if (dossieProduto.elementos_conteudos && dossieProduto.elementos_conteudos.length > 0) {
			dossieProdutoObj.elementos_conteudo = this.carregarElementosConteudo(dossieProduto.elementos_conteudos);
		}
	}

	carregarDocumentosVinculo(documentos: any) {
		let documentosConteudo = [];
		if(documentos && documentos.length > 0){
			documentos.forEach(documento => {
				documentosConteudo.push(this.carregarDocumento(documento));
			});
		}
		return documentosConteudo;
	}

	voltarDossieProdutoEmAlimentacao(dossieProduto: any, url: string) {
		this.loadService.hide();
		Utils.showMessageConfirm(this.dialogService, MESSAGE_ALERT_MENU.MSG_ALTERACAO_DESCARTADA).subscribe(res => {
			if (res) {
				this.voltarDossieProdutoEmAlimentacaoLink(dossieProduto, url);
			}
		},
			() => {
				this.loadService.hide();
			});
	}

	voltarDossieProdutoEmAlimentacaoLink(dossieProduto: any, url: string) {
		let obj = {
			"cancelamento": false,
			"finalizacao": false,
			"retorno": true
		};
		this.dossieService.pathRetornaDossieProduto(dossieProduto.id, obj).subscribe(response => {
			this.loadService.hide();
			if (url) {
				this.router.navigate([url]);
			}
		}, error => {
			this.loadService.hide();
			this.router.navigate([url]);
			throw error;
		});
	}

	retornaSituacaoQuandoForEmAlimentacaoOuEmComplementacao(dossieProduto) {
		if (dossieProduto && dossieProduto.situacao_atual && (dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.EM_ALIMENTACAO || dossieProduto.situacao_atual == SITUACAO_DOSSIE_BANCO.AGUARDANDO_COMPLEMENTACAO)) {
			let obj = {
				"cancelamento": false,
				"finalizacao": false,
				"retorno": true
			};
			let urlPatch = environment.serverPath + ARQUITETURA_SERVICOS.dossieProduto + '/' + dossieProduto.id + '/';
			$.ajax({
				type: "PATCH",
				headers: {
					'Authorization': `Bearer ${this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.token)}`
				},
				async: false,
				url: urlPatch,
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify(obj)
			});
		}
	}

	/**
	 * Verifica se o usuário logado possui a permissao: MTRADM
	 */
	hasCredentialAdministrarDossie(): boolean {
		const credentials: string = `${PERFIL_ACESSO.MTRADM}`;
		if (!environment.production) {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesSSO());
		}
	}

	/**
     * Verifica a partir do nome do processo se a unidade autorizada esta autorizada 
     * @param nome 
     */
	verificarUnidadeAutorizada(nome: string) {
        let processoFilho = this.localStorageDataService.buscarProcessoPorNome(nome);
        return processoFilho.unidade_autorizada;
	}
}