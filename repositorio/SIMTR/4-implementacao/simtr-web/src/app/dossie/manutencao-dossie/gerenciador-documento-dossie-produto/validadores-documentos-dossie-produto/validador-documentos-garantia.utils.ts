import { VinculoArvoreGarantia } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { VinculoArvore } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DossieProduto, VinculoGarantia } from "../../../../model";
import { GerenciadorDocumentosEmArvore } from "../../../../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util";
import { NodeApresentacao } from "../../../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";
import { INDEX_ABA_DOCUMENTO_DOSSIE } from "src/app/constants/constants";
import { UtilsArvore } from "src/app/documento/arvore-generica/UtilsArvore";

export class ValidadorDocumentosGarantia {
	/**
   * Quando o Vinculo for de Garantias
   * @param arvore 
   * @param dossieProduto 
   * @param garantias_informadas 
   */
	public static validarVinculoArvoreGarantia(arvore: VinculoArvore, dossieProduto: DossieProduto, garantiaInformada: VinculoGarantia) {
		garantiaInformada = this.validarDocsArvoreGarantia(garantiaInformada, arvore, dossieProduto);
		if (dossieProduto.camposValidado) {
			dossieProduto.garantias_informada.push(garantiaInformada);
		} else {
			GerenciadorDocumentosEmArvore.destacarArvoreComCampoObrigatorio(arvore, dossieProduto);
		}
		return garantiaInformada;
	}

	/**
	 * Ações Nescessaria para Vinculo Garantia
	 * @param garantias_informadas 
	 * @param vinculoArvoreGarantia 
	 * @param newProduto 
	 * @param documentoObrigatorio 
	 */
	private static validarDocsArvoreGarantia(garantiaInformada: VinculoGarantia, vinculoArvoreGarantia: VinculoArvoreGarantia, dossieProduto: DossieProduto) {
		garantiaInformada = JSON.parse(JSON.stringify(vinculoArvoreGarantia.vinculoGarantia));
		garantiaInformada.documento = null;
		if (undefined != vinculoArvoreGarantia.noApresentacao && vinculoArvoreGarantia.noApresentacao.length > 0) {
			let documentoObrigatorio: string;
			vinculoArvoreGarantia.classeValidacao = "";
			vinculoArvoreGarantia.noApresentacao.forEach(no => {
				documentoObrigatorio = this.validarTodosDocumentosVinculoArvoreGarantia(no, garantiaInformada, "garantias", documentoObrigatorio, dossieProduto);
				this.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, vinculoArvoreGarantia)
				this.validarQtdObrigatorioDocumentosGarantia(no, garantiaInformada, documentoObrigatorio, dossieProduto, vinculoArvoreGarantia);
			});
		}
		return garantiaInformada;
	}

	/**
	 * Método responsavel por percorre o noApresentação e encontra os documentos de imagem
	 * @param no no a ser percorrido
	 * @param processo variavel responsavel por receber o tipo de arvore que esta senco percorrida e vai ser add o elementos imagens
	 * @param tipo define se e true pra documento elemento a ser adicionado e falso pra novos documentos para pessoa vinculada
	 */
	private static validarTodosDocumentosVinculoArvoreGarantia(no: NodeApresentacao, processo: any, tipo: string, documentoObrigatorio: any, dossieProduto: DossieProduto) {
		let pastaVazia = true;
		if ((no.children == undefined || no.children.length == 0)) {
			documentoObrigatorio = this.montarTipoDocumentoGarantia(tipo, no, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
		} else {
			no.children.forEach(noInterno => {
				if (noInterno.children != undefined && noInterno.children.length > 0) {
					this.validarTodosDocumentosVinculoArvoreGarantia(noInterno, processo, tipo, documentoObrigatorio, dossieProduto);
				} else {
					documentoObrigatorio = this.montarTipoDocumentoGarantia(tipo, noInterno, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
				}
			});
		}
		return documentoObrigatorio;
	}

	/**
	 * 
	 * @param tipo 
	 * @param noInterno 
	 * @param processo 
	 * @param documentoObrigatorio 
	 * @param validar 
	 * @param pastaVazia 
	 */
	private static montarTipoDocumentoGarantia(tipo: string, noInterno: NodeApresentacao, processo: any, documentoObrigatorio: any, validar: boolean, pastaVazia: boolean) {
		if (tipo === "garantias") {
			let documento = GerenciadorDocumentosEmArvore.montarDocumento(noInterno);
			if (undefined != documento) {
				processo.documento == null ? processo.documento = [] : "";
				processo.documento.push(documento);
			} else {
				if(noInterno.obrigatorio || noInterno.quantidade_maxima_obrigatoria > 0 || noInterno.quantidade_obrigatorio > 0 || UtilsArvore.isDocumentoStatusNegativo(noInterno)) {
					documentoObrigatorio = "";
					documentoObrigatorio = GerenciadorDocumentosEmArvore.informDocumentoObrigatorio(validar, pastaVazia, documento, noInterno, documentoObrigatorio);
				}
			}
		}
		return documentoObrigatorio;
	}

	/**
   * Validar Campos Obrigatorio Global
   * @param no 
   * @param processo 
   * @param newProduto 
   * @param aba 
   * @param documentoObrigatorio 
   */
	public static validarCampoObrigatorio(no, dossieProduto: DossieProduto, aba: number, documentoObrigatorio: string, arvore: any) {
		let qtdDocumento = this.verificarQtdObrigatoria(no, 0);
		if (!dossieProduto.rascunho && qtdDocumento < no.quantidade_obrigatorio) {
			dossieProduto.camposValidado = false;
			dossieProduto.indexAba = aba;
		}

		if (!dossieProduto.rascunho && undefined != documentoObrigatorio && documentoObrigatorio != "") {
			dossieProduto.camposValidado = false;
			dossieProduto.indexAba = aba;
			dossieProduto.descricaoDocumento = documentoObrigatorio;
			arvore.classeValidacao = "campoObrigatorioArvore";
			let msg = {
				"msg": "Na árvore " + arvore.vinculoGarantia.nome + " é obrigatório o documento : " + documentoObrigatorio
			}
			dossieProduto.listaDocumentosFalta.push(msg);
			documentoObrigatorio = "";
		}
	}

	/**
	 * 
	 * @param no 
	 * @param qtdDocumento 
	 */
	private static verificarQtdObrigatoria(no: any, qtdDocumento: number) {
		let qtdDoc = qtdDocumento;
		no.children.forEach(noInterno => {
			if (undefined != noInterno.children) {
				qtdDoc = this.verificarQtdObrigatoria(noInterno, qtdDoc);
			} else if(!UtilsArvore.isDocumentoStatusNegativo(no)){
				qtdDoc += noInterno.uri.length;
			}
		});
		return qtdDoc;
	}

	/**
	 * Validar QTD Obrigatorio para Docuentos Garantia
	 */
	private static validarQtdObrigatorioDocumentosGarantia(no, processo: any, documentoObrigatorio: any, dossieProduto: DossieProduto, vinculoArvoreGarantia: VinculoArvoreGarantia) {
		let qtdUri = GerenciadorDocumentosEmArvore.verificarExistenciaDeUri(no);
		if (qtdUri == 0 && !dossieProduto.rascunho && (!documentoObrigatorio || (documentoObrigatorio == "" || documentoObrigatorio.length > 0))) {
			if(no.obrigatorio || no.quantidade_obrigatorio > 0 || no.quantidade_maxima_obrigatoria > 0) {
				GerenciadorDocumentosEmArvore.definirAcoesQuandoCampoObrigatorio(dossieProduto, vinculoArvoreGarantia);
				dossieProduto.descricaoDocumento = "É necessário informar no máximo 1 documento. Para " + no.label + " na árvore " + processo.nome;
				let msg = {
					"msg": dossieProduto.descricaoDocumento
				}
				dossieProduto.listaDocumentosFalta.push(msg);
			}
		}
	}
}