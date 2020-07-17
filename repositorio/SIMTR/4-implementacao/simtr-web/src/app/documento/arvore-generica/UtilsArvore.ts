import { NodeAbstract } from '../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-abstract.model';
import { TipoDocumentoArvoreGenerica } from '../../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model';
import { NodeApresentacao } from '../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model';
import { VinculoArvore } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { VinculoProcesso, VinculoProduto, VinculoGarantia, VinculoCliente } from 'src/app/model';
import { VinculoArvoreProcesso } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo';
import { VinculoArvoreCliente } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente';
import { VinculoArvoreGarantia } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia';
import { VinculoArvoreProduto } from 'src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto';
import { SituacaoDocumento } from 'src/app/documento/enum-documento/situacao-documento.enum';
import * as moment from 'moment';
import { DocumentoArvore } from 'src/app/model/documento-arvore';
import { StatusDocumento } from 'src/app/model/model-tratamento/status-documento.enum';
export class UtilsArvore {

	static removeNodeIfEmptyFirstLevel(tree: NodeAbstract[]) {
		if (tree != null) {
			for (let i = 0; i < tree.length; i++) {
				const item = tree[i];
				if (item.children && item.children.length == 0) {
					tree.splice(i, 1);
				}
			}
		}
	}

	/**
	 * 
	 * @param tree 
	 * @param node 
	 * @param tipoArvore  1 - Arvore Dossie cliente, 2 -  Arvore Dossie pro
	 */
	static removeNodeInTree(tree: NodeAbstract[], node: NodeAbstract, tipoArvore: number) {
		if (tree != null) {
			for (let i = 0; i < tree.length; i++) {
				const item = tree[i];
				if (item.label === node.label && item.parent === node.parent) {
					tree.splice(i, 1);
					if (node.parent.children && node.parent.children.length == 0 && node.parent.parent) {
						this.removeNodeInTree(node.parent.parent.children, tipoArvore === 1 ? node.parent : node, tipoArvore);
					}
					return;
				}
				if (item.children != null && item.children.length > 0) {
					this.removeNodeInTree(item.children, node, tipoArvore);
				}
			}
		}
	}

	static removeInsertOption(tree: NodeAbstract[], label: string) {
		if (tree != null) {
			for (const item of tree) {
				if (item.label === label) {
					if (item.uri) {
						let noApresentacaoConteudo: NodeApresentacao = (item as NodeApresentacao);
						noApresentacaoConteudo.acao_documento = 'A';
						noApresentacaoConteudo.setImageIcon(noApresentacaoConteudo);
						item.possui_instancia = true;
						for (const itemUri of item.uri) {
							itemUri.insert = null;
							return;
						}
					}
				}
				if (item.children != null && item.children.length > 0) {
					this.removeInsertOption(item.children, label);
				}
			}
		}
	}

	/**
	 * Retorna a cor do icone conforme estágio do ciclo de vida do documento
	 * @param status 
	 */
	static getIconDocument(noApresentacaoConteudo: NodeApresentacao): string {
		let icon = "";
		if (noApresentacaoConteudo.dossieCliente) {
			icon = this.getIconDocumentDossieCliente(noApresentacaoConteudo);
		} else {
			icon = this.getIconDocumentDossieProduto(noApresentacaoConteudo);
		}
		return icon;
	}

	/**
	 * Retorna o ícone do documento do dossie cliente
	 * @param noApresentacaoConteudo 
	 */
	static getIconDocumentDossieCliente(noApresentacaoConteudo: NodeApresentacao): string {
		let icon = "fa fa-circle-gray";
		const dossieMarcado: boolean = noApresentacaoConteudo.documentoArvore.dossie_digital;
		const dataNegocioNaoDefinida: boolean = !noApresentacaoConteudo.documentoArvore.data_hora_validade;
		const dataNegocioDefinida: boolean = noApresentacaoConteudo.documentoArvore.data_hora_validade !== undefined && noApresentacaoConteudo.documentoArvore.data_hora_validade !== null;
		const dataNegocioNaoDefinidaDossieMarcado: boolean = dataNegocioNaoDefinida && dossieMarcado;
		const dataNegocioDefinidaDossieMarcadoMaiorDataAtual: boolean = dataNegocioDefinida && moment(noApresentacaoConteudo.documentoArvore.data_hora_validade, 'DD/MM/YYYY HH:mm:ss').valueOf() > new Date().getTime() && !dossieMarcado;
		const dataNegocioNaoDefinidaDossieDemarcado: boolean = dataNegocioNaoDefinida && dossieMarcado==false;
		const dataNegocioDefinidaDossieDesmarcadoMaiorDataAtual: boolean = dataNegocioDefinida && moment(noApresentacaoConteudo.documentoArvore.data_hora_validade, 'DD/MM/YYYY HH:mm:ss').valueOf() > new Date().getTime() && !dossieMarcado;
		const dataNegocioDefinidaMenorDataAtual: boolean = dataNegocioDefinida && moment(noApresentacaoConteudo.documentoArvore.data_hora_validade, 'DD/MM/YYYY HH:mm:ss').valueOf() < new Date().getTime();

		if (dataNegocioNaoDefinidaDossieMarcado || dataNegocioDefinidaDossieMarcadoMaiorDataAtual) {
			noApresentacaoConteudo.acao_documento = SituacaoDocumento.CONFORME;
			icon = "fa fa-shield";
		}

		if (dataNegocioNaoDefinidaDossieDemarcado || dataNegocioDefinidaDossieDesmarcadoMaiorDataAtual) {
			noApresentacaoConteudo.acao_documento = SituacaoDocumento.CONFORME;
			icon = "fa fa-circle-green";
		}

		if (dataNegocioDefinidaMenorDataAtual) {
			noApresentacaoConteudo.acao_documento = SituacaoDocumento.INVALIDO;
			icon = "fa fa-ban";
		}

		return icon;
	}

	/**
	 * Ordena os documentos do vínculo processo por data captura
	 * @param vinculoCliente 
	 */
	static ordenaDocumentosPorDataHoraCapturaVinculoProcesso(vinculoProcesso: VinculoProcesso) {
		if (vinculoProcesso.instancias_documento && vinculoProcesso.instancias_documento.length > 0) {
			vinculoProcesso.instancias_documento.sort((documentoArvoreUM: any, documentoArvoreDOIS: any) => {
				const dataCapturaUM: Date = moment(documentoArvoreUM.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				const dataCapturaDOIS: Date = moment(documentoArvoreDOIS.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				return this.getTime(dataCapturaUM) - this.getTime(dataCapturaDOIS);
			});
		}
	}

	/**
	 * Ordena os documentos do vínculo cliente por data captura
	 * @param vinculoCliente 
	 */
	static ordenaDocumentosPorDataHoraCapturaVinculoCliente(vinculoCliente: VinculoCliente, dossieCliente: boolean) {
		if (dossieCliente && vinculoCliente.documentos && vinculoCliente.documentos.length > 0) {
			vinculoCliente.documentos.sort((documentoArvoreUM: DocumentoArvore, documentoArvoreDOIS: DocumentoArvore) => {
				const dataCapturaUM: Date = moment(documentoArvoreUM.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				const dataCapturaDOIS: Date = moment(documentoArvoreDOIS.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				return this.getTime(dataCapturaUM) - this.getTime(dataCapturaDOIS);
			});
		}
		if (!dossieCliente && vinculoCliente.instancias_documento && vinculoCliente.instancias_documento.length > 0) {
			vinculoCliente.instancias_documento.sort((documentoArvoreUM: any, documentoArvoreDOIS: any) => {
				const dataCapturaUM: Date = moment(documentoArvoreUM.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				const dataCapturaDOIS: Date = moment(documentoArvoreDOIS.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				return this.getTime(dataCapturaUM) - this.getTime(dataCapturaDOIS);
			});
		}
	}

	/**
	 * Ordena os documentos do vínculo produto por data captura
	 * @param vinculoCliente 
	 */
	static ordenaDocumentosPorDataHoraCapturaVinculoProduto(vinculoProduto: VinculoProduto) {
		if (vinculoProduto.instancias_documento && vinculoProduto.instancias_documento.length > 0) {
			vinculoProduto.instancias_documento.sort((documentoArvoreUM: any, documentoArvoreDOIS: any) => {
				const dataCapturaUM: Date = moment(documentoArvoreUM.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				const dataCapturaDOIS: Date = moment(documentoArvoreDOIS.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				return this.getTime(dataCapturaUM) - this.getTime(dataCapturaDOIS);
			});
		}
	}

	/**
	 * Ordena os documentos do vínculo garantia por data captura
	 * @param vinculoCliente 
	 */
	static ordenaDocumentosPorDataHoraCapturaVinculoGarantia(vinculoGarantia: VinculoGarantia) {
		if (vinculoGarantia.instancias_documento && vinculoGarantia.instancias_documento.length > 0) {
			vinculoGarantia.instancias_documento.sort((documentoArvoreUM: any, documentoArvoreDOIS: any) => {
				const dataCapturaUM: Date = moment(documentoArvoreUM.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				const dataCapturaDOIS: Date = moment(documentoArvoreDOIS.documento.data_hora_captura, 'DD/MM/YYYY HH:mm:ss').toDate();
				return this.getTime(dataCapturaUM) - this.getTime(dataCapturaDOIS);
			});
		}
	}


	/**
	 * Utilitário para deterninar a diferença entre datas - parametro com data atual
	 * @param date 
	 */
	static getTime(date?: Date): number {
		return date != null ? new Date(date).getTime() : 0;
	}

	/**
	 * Retorna o documento do dossie produto
	 * @param noApresentacaoConteudo 
	 */
	static getIconDocumentDossieProduto(noApresentacaoConteudo: NodeApresentacao): string {
		let icon = "fa fa-circle-gray";
		let situacao: string = "";
		if (noApresentacaoConteudo.documentoArvore.tipo_documento.id) {
			situacao = this.getSituacaoDocumento(SituacaoDocumento[noApresentacaoConteudo.acao_documento.toUpperCase()], noApresentacaoConteudo.acao_documento);
		}
		switch (situacao) {
			case SituacaoDocumento.PENDENTE:
				icon = "fa fa-circle-yellow";
				break;
			case SituacaoDocumento.QUARENTENA:
				icon = "fa fa-circle-yellow";
				break;
			case SituacaoDocumento.INVALIDO:
				icon = "fa fa-circle-blue";
				break;
			case SituacaoDocumento.SUBSTITUIDO:
				icon = "fa fa-circle-blue";
				break;
			case SituacaoDocumento.IGNORADO:
				icon = "fa fa-circle-blue";
				break;
			case SituacaoDocumento.CRIADO:
				icon = "fa fa-circle-gray";
				break;
			case SituacaoDocumento.CONFORME:
				icon = "fa fa-circle-green";
				break;
			case SituacaoDocumento.REJEITADO:
				icon = "fa fa-ban";
				break;
			case SituacaoDocumento.RECUSADO:
				icon = "fa fa-ban";
				break;
			case SituacaoDocumento.REPROVADO:
				icon = "fa fa-ban";
				break;
			default:
				icon = "fa fa-circle-gray";
				break;
		}
		return icon;
	}

	static getNodeInTree(newTree: NodeAbstract[], node: NodeAbstract): NodeAbstract {
		if (newTree != null) {
			for (const item of newTree) {
				if (item.label === node.label && item.parent === node.parent) {
					return item;
				}
				if (item.children != null && item.children.length > 0) {
					const newNode = this.getNodeInTree(item.children, node);
					if (newNode != null) {
						return newNode;
					}
				}
			}
		}
	}

	static getAllNodesSameTypeDocument(newTree: NodeAbstract[], listNodesSameType: NodeAbstract[], tipoDocuomento: TipoDocumentoArvoreGenerica): NodeAbstract[] {
		if (newTree != null) {
			for (const item of newTree) {
				if (item.label === tipoDocuomento.nome) {
					listNodesSameType.push(item);
				}
				if (item.children != null && item.children.length > 0) {
					this.getAllNodesSameTypeDocument(item.children, listNodesSameType, tipoDocuomento);
				}
			}
		}
		return listNodesSameType;
	}

	static getNodeInTreeByTypeDocument(newTree: NodeAbstract[],
		tipoDocuomento: TipoDocumentoArvoreGenerica): NodeAbstract {
		if (newTree != null) {
			for (const item of newTree) {
				if (item.label === tipoDocuomento.nome) {
					return item;
				}
				if (item.children != null && item.children.length > 0) {
					const newNode = this.getNodeInTreeByTypeDocument(item.children, tipoDocuomento);
					if (newNode != null) {
						return newNode;
					}
				}
			}
		}
	}

	static setFalseMatchFilterForChildren(node: NodeAbstract) {
		if (node.children != null && node.children.length > 0) {
			for (const item of node.children) {
				item.matchFilter = false;
				this.setFalseMatchFilterForChildren(item);
			}
		}
	}

	static grabNextNodeFilteredInNodes(nodes: NodeAbstract[]) {
		if (nodes != null) {
			for (const item of nodes) {
				if (item.matchFilter) {
					item.matchFilter = false;
					this.setFalseMatchFilterForChildren(item);
					return item;
				}
				const retorno = this.grabNextNodeFilteredInNode(item);
				if (retorno != null) {
					return retorno;
				}
			}
		}
	}

	static grabNextNodeFilteredInNode(node: NodeAbstract): NodeAbstract {
		if (node.matchFilter) {
			node.matchFilter = false;
			this.setFalseMatchFilterForChildren(node);
			return node;
		}
		const retorno = this.grabNextNodeFilteredInNodes(node.children);
		if (retorno != null) {
			return retorno;
		}
	}

	static verifyNodes(nodes: NodeAbstract[], filtro: string) {
		for (const item of nodes) {
			const lowerCase = item.label.toLocaleLowerCase();
			if (lowerCase.indexOf(filtro.toLocaleLowerCase()) !== -1) {
				item.matchFilter = true;
			}
			this.verifyNode(item, filtro);
		}
	}

	static verifyNode(node: NodeAbstract, filtro: string) {
		if (node.children != null && node.children.length > 0) {
			for (const item of node.children) {
				const lowerCase = item.label.toLocaleLowerCase();
				if (lowerCase.indexOf(filtro.toLocaleLowerCase()) !== -1) {
					item.matchFilter = true;
				}
				if (item.children != null && item.children.length > 0) {
					this.verifyNodes(item.children, filtro);
				}
			}
		}
	}

	/**
	 * Retorna um ID unico de acordo o tipo de árvore
	 * @param vinculoArvore 
	 */
	static castTypeVinculoArvore(vinculoArvore: VinculoArvore): number {
		const vinculoProcesso: VinculoProcesso = (vinculoArvore as VinculoArvoreProcesso).vinculoProcesso;
		const vinculoCliente: VinculoCliente = (vinculoArvore as VinculoArvoreCliente).vinculoCliente;
		const vinculoProduto: VinculoProduto = (vinculoArvore as VinculoArvoreProduto).vinculoProduto;
		const vinculoGarantia: VinculoGarantia = (vinculoArvore as VinculoArvoreGarantia).vinculoGarantia;
		let idValue: number;
		if (vinculoProcesso) {
			idValue = vinculoArvore.idProcessoFase ? vinculoArvore.idProcessoFase : vinculoArvore.id;
		}
		if (vinculoCliente) {
			idValue = vinculoCliente.id;
		}
		if (vinculoProduto) {
			idValue = vinculoProduto.id;
		}
		if (vinculoGarantia) {
			idValue = vinculoGarantia.id ? vinculoGarantia.id : Number(vinculoGarantia.garantia);
		}

		return idValue;
	}

	static definrCorTipoArvore(no: NodeApresentacao) {
		no.labelVerdeEscuro = ((no.children.length >= 1 && no.quantidade_obrigatorio == no.children.length) || (!no.obrigatorio && no.children.length >= 1));
		no.labelVermelhoEscuro = no.children.length == 0 && no.obrigatorio;
		no.labelLaranjaEscuro = no.children.length != 0 && no.obrigatorio && no.quantidade_obrigatorio > no.children.length;
	}

	static definrCorTipoArvoreFuncao(no: NodeApresentacao, qtdImg: number) {
		let qtdDocsValidos:number =  qtdImg - this.achaDocumentoStatusNegativo(no).length;
		if (no.quantidade_obrigatorio > 0) {
			no.labelVerdeEscuro =  no.obrigatorio && qtdDocsValidos >= no.quantidade_obrigatorio
										|| !no.obrigatorio && qtdDocsValidos >= no.quantidade_obrigatorio;
			no.labelVermelhoEscuro = qtdDocsValidos == 0 && no.obrigatorio;
			no.labelLaranjaEscuro = qtdDocsValidos != 0 && no.obrigatorio && no.quantidade_obrigatorio > qtdDocsValidos;
		} else {
			no.labelBold = no.quantidade_obrigatorio == 0 && qtdDocsValidos >= 1;
		}
	}

	static definrCorTipoArvoreInicialFuncao(no: NodeApresentacao, obrigatorio: boolean) {
		no.labelVermelhoEscuro =  this.existeAlgumDocumentoStatusNegativo(no)
									&& !this.existeAlgumDocumentoStatusPositivo(no) 
									|| obrigatorio && no.children.length == 0;
		no.labelVerdeEscuro = obrigatorio && (no.children.length > 0 ? true : false) && this.existeAlgumDocumentoStatusPositivo(no);
		no.labelBold = !obrigatorio && (no.children.length > 0 ? true : false) && this.existeAlgumDocumentoStatusPositivo(no);
	}

	private static existeAlgumDocumentoStatusNegativo(no: NodeApresentacao){
		if(no.children.length > 0){
			return no.children.some(nodeDoc => nodeDoc.acao_documento == SituacaoDocumento.INVALIDO 
				|| nodeDoc.acao_documento == SituacaoDocumento.REJEITADO
					|| nodeDoc.acao_documento == SituacaoDocumento.SUBSTITUIDO
						|| nodeDoc.acao_documento == SituacaoDocumento.REPROVADO
							|| nodeDoc.acao_documento == SituacaoDocumento.RECUSADO);
		}
		return false;
	}

	static existeAlgumDocumentoStatusPositivo(no: NodeApresentacao){
		if(no.children.length > 0){
			return no.children.some(nodeDoc => nodeDoc.acao_documento == SituacaoDocumento.CRIADO 
				|| nodeDoc.acao_documento == SituacaoDocumento.CONFORME
					|| nodeDoc.acao_documento == SituacaoDocumento.PENDENTE
						|| nodeDoc.acao_documento == SituacaoDocumento.QUARENTENA);
		}
		return false;
	}

	/**
	 * Verifica se o NO folha que representa o documento está com status negativo.
	 * @param nodeDoc No folha do documento na árvore
	 */
	static isDocumentoStatusNegativo(nodeDoc: NodeApresentacao){
		if(nodeDoc.acao_documento == SituacaoDocumento.INVALIDO
			|| nodeDoc.acao_documento == SituacaoDocumento.REJEITADO
				|| nodeDoc.acao_documento == SituacaoDocumento.SUBSTITUIDO
					|| nodeDoc.acao_documento == SituacaoDocumento.REPROVADO
						|| nodeDoc.acao_documento == SituacaoDocumento.RECUSADO){
			return true;
		}
		return false;
	}

	private static achaDocumentoStatusNegativo(noFuncao: NodeApresentacao){
		let arrayDocsNegados = new Array<NodeApresentacao>();
		if(noFuncao.children.length > 0){
			for(let nodeTipo of noFuncao.children){
				let arrayDocs = nodeTipo.children.filter(nodeDoc => nodeDoc.acao_documento == SituacaoDocumento.INVALIDO 
					|| nodeDoc.acao_documento == SituacaoDocumento.REJEITADO
						|| nodeDoc.acao_documento == SituacaoDocumento.SUBSTITUIDO
							|| nodeDoc.acao_documento == SituacaoDocumento.REPROVADO
								|| nodeDoc.acao_documento == SituacaoDocumento.RECUSADO);
				if(arrayDocs.length > 0){
					arrayDocsNegados = arrayDocsNegados.concat(arrayDocs);
				}
			}
		}
		return arrayDocsNegados;
	}

	/**
	 * Recupera o status do documento conforme situação passada.
	 * @param situacaoDocumento 
	 * @param situacaoAUX 
	 */
	static getSituacaoDocumento(situacaoDocumento: SituacaoDocumento, situacaoAUX: string): SituacaoDocumento {
		let situacao: SituacaoDocumento;
		for (var obj in SituacaoDocumento) {
			if (situacaoDocumento && situacaoDocumento.toUpperCase().indexOf(obj) == 0) {
				situacao = obj as SituacaoDocumento;
				break;
			}
		}
		if (!situacao) {
			throw new Error("Situação de documento não encontrada: " + situacaoAUX);
		}
		return SituacaoDocumento[situacao.toUpperCase()];
	}

}
