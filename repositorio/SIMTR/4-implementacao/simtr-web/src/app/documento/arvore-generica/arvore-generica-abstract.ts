import { VinculoArvore } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { NodeApresentacao } from "../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";
import { DocumentImage } from "../documentImage";
import { UtilsCliente } from "../../cliente/consulta-cliente/utils/utils-client";
import { DocumentoArvore } from "../../model/documento-arvore";
import { ConteudoDocumentoArvore } from "../../model/conteudo-documento-arvore";
import { TipoDocumentoArvoreGenerica } from "../../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { FuncaoDocumentalArvoreGenerica } from "../../model/model-arvore-generica-dossie-produto/funcao-documental-arvore-generica";
import { Utils } from "../../utils/Utils";
import { DocumentoGED } from "../../model";
import * as moment from 'moment';
import { AlertMessageService } from "../../services";
import { environment } from "src/environments/environment";
import { SituacaoDocumento } from "../enum-documento/situacao-documento.enum";

export abstract class ArvoreGenericaAbastract<T extends VinculoArvore> extends AlertMessageService {
	static NODE_SELECTED;
	static NODE_TO_EXCLUDE;
	static contadorNode = 0;

	/**
	 * Método que verifica a fase do processo. 
	 * @param processo_filho   
	 * @param vinculoArvore 
	 */
	verificaArvoreProcessoFase(processo_filho: any, vinculoArvore: T): any {
		let processo_fase;
		if (vinculoArvore.idProcessoFase !== undefined) {
			for (let processo_fase_atual of processo_filho.processos_filho) {
				if (vinculoArvore.idProcessoFase === processo_fase_atual.id) {
					processo_fase = processo_fase_atual;
					break;
				}
			}
		}
		return processo_fase;
	}


	/**
	 * Método que seta o nó de apresentação nas pages.
	 * @param noApresentacaoConteudo Nó apresentação
	 */
	public static setarParent(noApresentacaoConteudo: NodeApresentacao) {
		for (const itemDocument of noApresentacaoConteudo.pages) {
			itemDocument.parent = noApresentacaoConteudo;
		}
	}

	/**
	 * Esconde o No na tela
	 * @param node No de visualizacao de documentos
	 */
	public static hideNode(node: NodeApresentacao) {
		node.styleClass = "hide";
	}

	/**
	 * Mostra o No na tela
	 * @param node No de visualizacao de documentos
	 */
	public static showNode(node: NodeApresentacao) {
		undefined != node ? node.styleClass = null : "";
	}

	/**
	 * Método que seta tipo da situação do documento.
	 * @param noApresentacaoConteudo Nó apresentação.  
	 */
	public static setarTipoSituacaoDocumento(noApresentacaoConteudo: NodeApresentacao, envelopeDocumento: DocumentoArvore, dossieCliente: boolean) {
		noApresentacaoConteudo.acao_documento = SituacaoDocumento.CRIADO;
		noApresentacaoConteudo.dossieCliente = dossieCliente;
		noApresentacaoConteudo.documentoArvore = envelopeDocumento;
		noApresentacaoConteudo.setImageIcon(noApresentacaoConteudo);
	}

	/**
	 * Método que cria um nó vazio.
	 * @param label label de apresentação do nó.  
	 */
	static criaNo(objeto: any): NodeApresentacao {
		let node = new NodeApresentacao(objeto.nome == undefined ? objeto.label : objeto.nome);
		node.id = objeto.id ? objeto.id : objeto.tipo_documento ? objeto.tipo_documento.id : undefined;
		node.obrigatorio = objeto.obrigatorio;
		node.quantidade_obrigatorio = !objeto.quantidade_obrigatorios ? 0 : objeto.quantidade_obrigatorios;
		node.noReutilizavel = false;
		node.existeDocumentoReuso = false;
		node.setFolderIcons();
		node.children = [];
		return node;
	}

	/**
	 * Ordena array de elementos de conteudos a partir do NO pai
	 */
	public static ordenaElementosConteudoPartindoNoPai(elementosConteudo: any[]) {
		let elementosConteudoOrdenados: any[] = [];
		let elementosConteudoComFilhos: any[] = [];
		if (elementosConteudo && elementosConteudo.length > 0) {
			let elementosConteudoPai = elementosConteudo.filter(elementoConteudo => !elementoConteudo.identificador_elemento_vinculador);
			elementosConteudoPai = elementosConteudoPai.sort((elemento1, elemento2) => elemento1.identificador_elemento - elemento2.identificador_elemento);
			for (let elementoConteudoPai of elementosConteudoPai) {
				let elementosConteudoFilho = elementosConteudo.filter(elementoConteudo => elementoConteudo.identificador_elemento_vinculador == elementoConteudoPai.identificador_elemento);
				if (elementosConteudoFilho.length == 0) {
					elementosConteudoOrdenados.push(elementoConteudoPai);
				} else {
					elementosConteudoFilho = elementosConteudoFilho.sort((elemento1, elemento2) => elemento1.identificador_elemento - elemento2.identificador_elemento);
					elementosConteudoComFilhos.push(elementoConteudoPai);
					elementosConteudoComFilhos = elementosConteudoComFilhos.concat(elementosConteudoFilho);
				}
			}
			elementosConteudoOrdenados = elementosConteudoOrdenados.concat(elementosConteudoComFilhos);
		}
		return elementosConteudoOrdenados;
	}

	/**
	 * 
	 * @param documentosVinculo 
	 * @param tipoRelacionamento 
	 */
	public static ordenaEstruturaDocumentosPessoaPartindoDoTipoDocumento(documentosVinculo: any[]) {
		let documentosVinculoOrdenadoPorTipoDoc: any[] = null;
		if (undefined != documentosVinculo) {
			let documentosVinculoTipoRelacioTipoDoc = documentosVinculo.filter(documentoVinculo => documentoVinculo.funcao_documental == undefined);
			let documentosVinculoTipoRelacioFuncDoc = documentosVinculo.filter(documentoVinculo => documentoVinculo.tipo_documento == undefined);
			documentosVinculoOrdenadoPorTipoDoc = documentosVinculoTipoRelacioTipoDoc;
			documentosVinculoOrdenadoPorTipoDoc = documentosVinculoOrdenadoPorTipoDoc.concat(documentosVinculoTipoRelacioFuncDoc);
		}
		return documentosVinculoOrdenadoPorTipoDoc;
	}

	/**
	 * 
	 * @param documentosVinculo 
	 * @param tipoRelacionamento 
	 */
	public static ordenaEstruturaDocumentosGarantiaPartindoDoTipoDocumento(documentosVinculo: any[]) {
		let documentosVinculoOrdenadoPorTipoDoc: any[] = null;
		if (undefined != documentosVinculo) {
			let documentosVinculoTipoRelacioTipoDoc = documentosVinculo.filter(documentoVinculo => documentoVinculo.funcoes_documentais.length == 0);
			let documentosVinculoTipoRelacioFuncDoc = documentosVinculo.filter(documentoVinculo => documentoVinculo.tipos_documento.length == 0);
			documentosVinculoOrdenadoPorTipoDoc = documentosVinculoTipoRelacioTipoDoc;
			documentosVinculoOrdenadoPorTipoDoc = documentosVinculoOrdenadoPorTipoDoc.concat(documentosVinculoTipoRelacioFuncDoc);
		}
		return documentosVinculoOrdenadoPorTipoDoc;
	}

	/**
	 * Método que cria envelope documento
	 * @param usuario Usuário que está realizando ação.
	 * @param tipoDocumento Instância de tipoDocumentoArvoreGenerica
	 */
	public static criaEnvelopeDocumento(usuario: string, tipoDocumento: TipoDocumentoArvoreGenerica): DocumentoArvore {
		let doc: DocumentoArvore = new DocumentoArvore();
		doc.data_hora_captura = UtilsCliente.getDataCompleta("N");
		doc.matricula_captura = usuario;
		doc.hora_captura = UtilsCliente.getDataCompleta("H");
		doc.tipo_documento = tipoDocumento;
		return doc;
	}

	public static criaEnvelopeDocumentoExiste(docDb: any): DocumentoArvore {
		let doc: DocumentoArvore = new DocumentoArvore();
		doc.canal_captura = docDb.canal_captura;
		doc.data_hora_captura = docDb.data_hora_captura;
		doc.matricula_captura = docDb.matricula_captura;
		doc.situacaoAtual = docDb.situacaoAtual;
		doc.origem_documento = docDb.origem_documento;
		doc.data_hora_validade = docDb.data_hora_validade;
		doc.tipo_documento = docDb.tipo_documento;
		doc.quantidade_conteudos = docDb.quantidade_conteudos;
		return doc;
	}

	/**
	 * Método que cria um documento.
	 * @param usuario Usuário da ação.
	 * @param image imagem selecionada.
	 */
	public static criaDocumento(usuario: string, image: DocumentImage) {
		let documento: ConteudoDocumentoArvore = new ConteudoDocumentoArvore();
		documento.codigo_ged = "";
		documento.data_captura = UtilsCliente.getDataCompleta("N");
		documento.matricula = usuario;
		documento.uri = [];
		ArvoreGenericaAbastract.criaIdentificadorImagem(image, documento);
		return documento;
	}

	/**
	 * Criar documento aproveitando uma imagem existente.
	 * @param usuario Usuário da ação.
	 * @param imageUri Imagem existente 
	 */
	public static criaDocumentoAproveitandoIdentificadorImagem(usuario: string, imageUri: any[]) {
		let documento: ConteudoDocumentoArvore = new ConteudoDocumentoArvore();
		documento.codigo_ged = "";
		documento.data_captura = UtilsCliente.getDataCompleta("N");
		documento.matricula = usuario;
		documento.uri = imageUri;
		documento.id = imageUri.length > 0 && imageUri[0] ? imageUri[0].id : undefined;
		return documento;
	}

	public static criaDocumentoAproveitandoIdentificadorImagemDB(docDb: any) {
		let documento: ConteudoDocumentoArvore = new ConteudoDocumentoArvore();
		documento.id = docDb.id;
		documento.codigo_ged = docDb.codigo_ged;
		documento.data_captura = docDb.data_hora_captura;
		documento.data_validade = docDb.data_hora_validade;
		documento.matricula = docDb.matricula_captura;
		documento.docReutilizado = docDb.docReutilizado;
		documento.uri = this.montarImagens(docDb.conteudos, docDb.id);
		return documento;
	}

	private static montarImagens(lista: any, id: any) {
		let images = [];
		if (undefined != lista && lista.length > 0) {
			lista.forEach(img => {
				let obj = new DocumentImage();
				obj.id = id;
				obj.image = img.binario;
				obj.sequencia_apresentacao = img.sequencia_apresentacao
				images.push(obj);
			});
		}
		return images;
	}

	/**
	 * Método que popula um documento já existente na base.
	 * @param usuario Usuário da ação.
	 */
	public static populaConteudoDocumento(doc: any) {
		let documento: ConteudoDocumentoArvore = new ConteudoDocumentoArvore();
		documento.id = doc.id;
		documento.codigo_ged = doc.codigo_ged;
		documento.data_captura = doc.data_hora_captura
		documento.data_validade = doc.data_hora_validade;
		documento.matricula = doc.matricula_captura;
		documento.uri = [];
		return documento;
	}

	/**
	 * Pecorrer documentos selecionados e cria documentos.
	 * 
	 * @param arrayDocumentos Documentos selecionados.
	 * @param envelopeDocumento Envelope do Documento.
	 */
	public static percorreArrayECriaDocumentos(arrayDocumentos: ConteudoDocumentoArvore[],
		envelopeDocumento: any): NodeApresentacao[] {
		const result = [];
		for (const documento of arrayDocumentos) {
			result.push(ArvoreGenericaAbastract.criaNoDeApresentacaoDoConteudoDocumento(documento, envelopeDocumento));
		}
		return result;
	}

	/**
	 * Método que cria nó de apresentação do documento selecionado.
	 * @param documento Documento selecionado.
	 * @param envelopeDocumento Envelope documento.
	 */
	private static criaNoDeApresentacaoDoConteudoDocumento(documento: ConteudoDocumentoArvore, envelopeDocumento: any): NodeApresentacao {
		const noApresentacaoConteudo = new NodeApresentacao(envelopeDocumento.data_hora_captura + " - " +
			envelopeDocumento.matricula_captura);
		noApresentacaoConteudo.setUri(documento.uri);
		if (documento.id != undefined) {
			noApresentacaoConteudo.id = documento.id;
			noApresentacaoConteudo.possui_instancia = true;
		}
		noApresentacaoConteudo.numero_processo = envelopeDocumento.nu_processo;
		noApresentacaoConteudo.dossie_produto = envelopeDocumento.dossie_produto;
		noApresentacaoConteudo.data_validade = envelopeDocumento.data_hora_validade;
		noApresentacaoConteudo.situacao = envelopeDocumento.situacao;
		noApresentacaoConteudo.data = documento;
		ArvoreGenericaAbastract.showNode(noApresentacaoConteudo);
		return noApresentacaoConteudo;
	}

	public static defineStatusSituacaoDocuemntoBase(noApresentacaoConteudo: NodeApresentacao, envelopeDocumento: DocumentoArvore, dossieCliente: boolean) {
		noApresentacaoConteudo.dossieCliente = dossieCliente;
		noApresentacaoConteudo.documentoArvore = envelopeDocumento;
		noApresentacaoConteudo.acao_documento = envelopeDocumento.situacaoAtual;
		noApresentacaoConteudo.setImageIcon(noApresentacaoConteudo);
		ArvoreGenericaAbastract.hideNode(noApresentacaoConteudo);
	}

	/**
	 * Método que cria o identificador da imagem selecionada.
	 * @param image  Imagem Selecionada.
	 * @param documento Documento.
	 */
	private static criaIdentificadorImagem(image: DocumentImage, documento: ConteudoDocumentoArvore) {
		var imgStri = JSON.stringify(image);
		documento.uri.push(JSON.parse(imgStri));
	}

	/**
	 * Reorganiza documentos do dossie em funcões documentais, com seus tipos e documentos
	 * @param docs Documentos retonados da consulta do dossie cliente
	 */
	public static reorganizaPorListaFuncaoDocumental(docs: DocumentoArvore[]): FuncaoDocumentalArvoreGenerica[] {
		let funcoesOrganizadas: FuncaoDocumentalArvoreGenerica[] = [];
		let tipos: TipoDocumentoArvoreGenerica[] = [];
		// Varre a Lista de Documentos obtidos do Cliente
		for (const doc of docs) {
			if (doc.tipo_documento.funcoes_documentais.length == 0) {
				continue;
			}
			this.percorreFuncoesDocumentaisDentroTipoDocumento(funcoesOrganizadas, doc);
			// Verifica se já existe algum Tipo na lista
			if (tipos.length > 0) {
				this.insereDocumentoEmSeuTipoApropriado(tipos, doc);
			} else {
				// Inclusão de Tipo de Documento do Cliente, dentro da lista de Tipos
				this.realizaInclusaoListaTipos(doc, tipos);
			}
		}
		return funcoesOrganizadas;
	}

	/**
	 * Reorganiza documentos do dossie em funcões documentais, com seus tipos e documentos
	 * @param docs Documentos retonados da consulta do dossie cliente
	 */
	public static reorganizaPorListaTipoDocumento(docs: DocumentoArvore[]): TipoDocumentoArvoreGenerica[] {
		let tiposOrganizados: TipoDocumentoArvoreGenerica[] = [];
		let tipos: TipoDocumentoArvoreGenerica[] = [];
		// Varre a Lista de Documentos obtidos do Cliente
		for (const doc of docs) {
			if (doc.tipo_documento.funcoes_documentais.length > 0) {
				continue;
			}
			this.percorreTiposDocumentos(tiposOrganizados, doc);
			if (tipos.length > 0) {
				this.insereDocumentoEmSeuTipoApropriado(tipos, doc);
			} else {
				this.realizaInclusaoListaTipos(doc, tipos);
			}
		}
		return tiposOrganizados;
	}

	/**
	 * Insere na lista de tipos de documentos
	 * @param item documento vindo da base de dados
	 * @param tipos lista que contém todos os tipos de documentos com seus respectivos documentos
	 */
	private static realizaInclusaoListaTipos(item: DocumentoArvore, tipos: TipoDocumentoArvoreGenerica[]) {
		item.tipo_documento.documentos = [];
		item.tipo_documento.documentos.push(item);
		tipos.push(item.tipo_documento);
	}

	/**
	 * Verifica se ja existe o tipo na lista de tipos para incluir o tipo ou não com seu respectivo documento
	 * @param tipos lista que contém todos os tipos de documentos com seus respectivos documentos
	 * @param item documento vindo da base de dados
	 */
	private static insereDocumentoEmSeuTipoApropriado(tipos: TipoDocumentoArvoreGenerica[], item: DocumentoArvore) {
		let hasTipo: boolean = false;
		// Varre a lista de Tipos para não incluir Tipo duplicado
		for (let tip of tipos) {
			if (tip.id === item.tipo_documento.id) {
				hasTipo = true;
				// Caso já exista o Tipo, inclui apenas o Documento
				tip.documentos.push(item);
			}
		}
		// Caso não tenha o Tipo dentro da lista, insere
		if (!hasTipo) {
			item.tipo_documento.documentos = [];
			item.tipo_documento.documentos.push(item);
			tipos.push(item.tipo_documento);
		}
	}

	/**
	 * Perrcorre as funcões documentais dentro dos tipos de documentos para inserir as mesmas na lista de funcoesOrganizadas
	 * @param funcoesOrganizadas Lista que contém as funcões documentas com seus tipos de documentos
	 * @param doc documento proveniente da base de dados
	 */
	private static percorreFuncoesDocumentaisDentroTipoDocumento(funcoesOrganizadas: FuncaoDocumentalArvoreGenerica[],
		doc: DocumentoArvore) {
		// Varre as Funções Documentais de dentro do Tipo de Documento
		for (let funcaoDentroTipoDocumento of doc.tipo_documento.funcoes_documentais) {
			funcaoDentroTipoDocumento.tipos_documento = [];
			// Verifica se já existe alguma Função na lista
			if (funcoesOrganizadas.length > 0) {
				// Caso não exista a Função na lista, insere
				if (!this.hasDuplicidadesFuncoes(funcoesOrganizadas, doc)) {
					funcaoDentroTipoDocumento.tipos_documento.push(doc.tipo_documento);
					funcoesOrganizadas.push(funcaoDentroTipoDocumento);
				}
				continue;
			}
			this.inclusaoFuncaoDocumentalComTipoDocumento(funcaoDentroTipoDocumento, doc, funcoesOrganizadas);
		}
	}
	/**
	 * Perrcorre os tipos de documentos para inserir na lista de tiposOrganizados
	 * @param tiposOrganizados lista que contem os tipos de documentos organizados
	 * @param doc documento proveniente da base de dados
	 */
	private static percorreTiposDocumentos(tiposOrganizados: TipoDocumentoArvoreGenerica[], doc: DocumentoArvore) {
		let existeTipoDoc: TipoDocumentoArvoreGenerica = tiposOrganizados.find(tipoDoc => tipoDoc.id == doc.tipo_documento.id);
		if (!existeTipoDoc) {
			tiposOrganizados.push(doc.tipo_documento);
		}
		return tiposOrganizados;
	}

	/**
	 * Inclui na lista de funcoesOrganizadas a funcao documental com o seu tipo de documento
	 * @param funcaoDentroTipoDocumento funcao documental
	 * @param doc documento proveniente da base de dados
	 * @param funcoesOrganizadas Lista que contém as funcões documentas com seus tipos de documentos
	 */
	private static inclusaoFuncaoDocumentalComTipoDocumento(funcaoDentroTipoDocumento: any, doc: DocumentoArvore, funcoesOrganizadas: FuncaoDocumentalArvoreGenerica[]) {
		funcaoDentroTipoDocumento.tipos_documento = [];
		funcaoDentroTipoDocumento.tipos_documento.push(doc.tipo_documento);
		funcoesOrganizadas.push(funcaoDentroTipoDocumento);
	}

	/**
	* Verifica se ja existe a função documental ou não na lista de funcoesOrganizadas 
	* @param doc documento proveniente da base de dados
	* @param funcoesOrganizadas Lista que contém as funcões documentas com seus tipos de documentos
	*/
	private static hasDuplicidadesFuncoes(funcoesOrganizadas: FuncaoDocumentalArvoreGenerica[],
		doc: DocumentoArvore) {
		let hasFuncao: boolean = false;
		for (let funcao of funcoesOrganizadas) {
			for (let funcaoDoc of doc.tipo_documento.funcoes_documentais) {
				if (funcao.id === funcaoDoc.id) {
					hasFuncao = true;
					let hasTipoInFuncao = false;
					hasTipoInFuncao = this.percorreFuncoesDocumentaisEInsereTipoDocumento(funcao, doc, hasTipoInFuncao);
				}
			}
		}
		return hasFuncao;
	}

	/**
	 * Varre a lista de Tipos de Documentos internos da lista de Funções para não incluir Tipos duplicados 
	 * @param funcao funcao documentaol
	 * @param doc documento proveniente da base de dados
	 * @param hasTipoInFuncao fla que indica se ja existe o tipo de documento nessa funcao documental
	 */
	private static percorreFuncoesDocumentaisEInsereTipoDocumento(funcao: FuncaoDocumentalArvoreGenerica,
		doc: DocumentoArvore, hasTipoInFuncao: boolean) {
		for (let ftp of funcao.tipos_documento) {
			if (ftp.id === doc.tipo_documento.id) {
				hasTipoInFuncao = true;
			}
		}
		// Caso não exista ainda o Tipo dentro da Função, insere
		if (!hasTipoInFuncao) {
			funcao.tipos_documento.push(doc.tipo_documento);
		}
		return hasTipoInFuncao;
	}

	/**
	 * Filtra os documentos de uma lista por um tipo específico informado
	 * @param documentos lista de documentos do dossie clientes salvos na base de dados
	 * @param tipoDocumento tipo do documento desejadao para a filtragem na lista
	 */
	filtraParaDocumentosMesmoTipo(documentos: DocumentoArvore[], idTipoDocumento: number): DocumentoArvore[] {
		let documentosMesmoTipo = new Array<DocumentoArvore>();
		for (let doc of documentos) {
			if ((doc.data_hora_validade == null
				|| (moment(doc.data_hora_validade, 'DD/MM/YYYY HH:mm:ss').valueOf() > new Date().getTime()))
				&& doc.tipo_documento.permite_reuso
				&& doc.tipo_documento.id == idTipoDocumento) {
				documentosMesmoTipo.push(doc);
			}
		}
		return documentosMesmoTipo;
	}

	/**
	 * Percorre a lista até achar um No com o mesmo nome dado no parametro
	 * @param label no de apresentação do No da arvore
	 * @param documentos lista visualização de documentos
	 */
	static getNodeApresentacao(label: string, documentos: NodeApresentacao[]): NodeApresentacao {
		let result = null;
		for (const documento of documentos) {
			if (documento.label === label) {
				return documento;
			}
			if (documento.children != null && documento.children.length > 0) {
				result = this.getNodeApresentacao(label, documento.children);
			}
		}
		return result;
	}

	/**
	 * Formata os documentos presentes nos Nos para serem usados no GED
	 * @param lista Lista de Nos de documentos na arvore
	 */
	static formataListaDocumentosConsultaGedFuncaoDocumental(lista: NodeApresentacao[]) {
		let listaGed = [];
		for (const lst of lista) {
			for (const docChild1 of lst.children) {
				if (docChild1.children) {
					for (const docChild2 of docChild1.children) {
						let docGed: DocumentoGED = new DocumentoGED();
						docGed.tipo_documento = docChild1.id;
						docGed.origem_documento = "S";
						docGed.atributos = docChild2.atributos;
						var imgList = [];
						var inserindo = "";
						for (const item of docChild2.uri) {
							if (item != undefined && item.index != undefined) {
								inserindo = "I";
								docGed.mime_type = item.type;
								docGed.indiceDocListPdfOriginal = item.indiceDocListPdfOriginal;
								imgList.push(item);
							}
						}
						docGed.imagens = imgList;
						if (inserindo == "I") {
							listaGed.push({ node: docGed, label: docChild2.label, tipoDocumento: docChild2.parent.label });
						}
					}
				}
			}
		}
		return listaGed;
	}

	static formataListaDocumentosConsultaGedTipoDocumento(lista: NodeApresentacao[]) {
		let listaGed = [];
		for (const lst of lista) {
			ArvoreGenericaAbastract.recursividadeEncontrarDoc(lst, listaGed);
		}
		return listaGed;

	}

	private static recursividadeEncontrarDoc(lst: NodeApresentacao, listaGed: any[]) {
		for (const docChild1 of lst.children) {
			let docGed: DocumentoGED = new DocumentoGED();
			docGed.tipo_documento = docChild1.id;
			docGed.origem_documento = "S";
			docGed.atributos = docChild1.atributos;
			var imgList = [];
			var inserindo = "";
			if (docChild1.leaf) {
				if (docChild1.uri) {
					for (const item of docChild1.uri) {
						if (item != undefined && item.index != undefined) {
							inserindo = "I";
							docGed.mime_type = item.type;
							docGed.indiceDocListPdfOriginal = item.indiceDocListPdfOriginal;
							imgList.push(item);
						}
					}
					docGed.imagens = imgList;
					if (inserindo == "I") {
						listaGed.push({ node: docGed, label: docChild1.label, tipoDocumento: docChild1.parent.label });
					}
				}
			} else {
				if (docChild1.children && docChild1.children.length > 0) {
					this.recursividadeEncontrarDoc(docChild1, listaGed);
				}
			}
		}
	}

	/**
	 * Adiciona em uma lista todas as uris dos documentos dentro desse No
	 * @param children No filho da lista de visulização de documentos
	 */
	static getAllUri(children: NodeApresentacao[]) {
		const list = [];
		for (const item of children) {
			if (item.uri != undefined) {
				if (item.uri.length > 0) {
					item.uri.forEach(img => {
						list.push(img);
					});
				} else {
					list.push(item.uri);
				}
			}
		}
		return list;
	}

	/**
	 * Rece uma lista de Nos e os ordena pela insercao do mais recente
	 * @param listaNosVisualizacao Lista de nos de visualizacao de documentos
	 */
	static ordenaArvorePorDocumentosMaisRecentes(listaNosVisualizacao: NodeApresentacao[]) {
		listaNosVisualizacao.sort(Utils.ordenarDocumentosNodeByLabel);
		for (let doc of listaNosVisualizacao) {
			doc.children.sort(Utils.ordenarDocumentosNodeByLabel);
			for (const docItem of doc.children) {
				docItem.children.sort(Utils.ordenarDocumentosNodeByDataHora);
			}
		}
	}

}