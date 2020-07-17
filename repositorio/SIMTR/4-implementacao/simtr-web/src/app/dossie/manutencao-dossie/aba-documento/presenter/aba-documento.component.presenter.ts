import { Injectable } from "@angular/core";
import { ConversorDocumentosUtil } from "src/app/documento/conversor-documentos/conversor-documentos.util.service";
import { EmptyVinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/empty-vinculo-arvore";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { VinculoArvoreCliente } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { VinculoArvoreProcesso } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo";
import { VinculoArvoreProduto } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { VinculoArvoreGarantia } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { VinculoProcesso, VinculoCliente, VinculoGarantia, VinculoProduto } from "src/app/model";

@Injectable()
export class AbaDocumentoComponentPresenter {

    constructor(private conversorDocUtil: ConversorDocumentosUtil) { }

	/**
	 * Verifica toda a estrutura de arvores. Caso exista uma arvore vazia; sua apresentação será ocultada.
	 * @param emptyVinculoArvore 
     * @param emptyVinculoArvore 
	 */
    verificaEstruturaVaziaArvoresDossie(emptyVinculoArvore: EmptyVinculoArvore, listaVinculoArvore: Array<VinculoArvore>) {
        listaVinculoArvore.forEach(vinculoArvore => {
            if ((vinculoArvore as VinculoArvoreProcesso).vinculoProcesso != undefined
                && emptyVinculoArvore.vinculoArvore instanceof VinculoArvoreProcesso
                && (vinculoArvore as VinculoArvoreProcesso).vinculoProcesso.nome == (emptyVinculoArvore.vinculoArvore as VinculoArvoreProcesso).vinculoProcesso.nome
                && !vinculoArvore.idProcessoFase && !emptyVinculoArvore.idProcessoFase) {
                vinculoArvore.emptyTreeProcesso = emptyVinculoArvore.emptyVinculoArvore;
            }
            if ((vinculoArvore as VinculoArvoreProcesso).vinculoProcesso != undefined
                && emptyVinculoArvore.vinculoArvore instanceof VinculoArvoreProcesso
                && (vinculoArvore as VinculoArvoreProcesso).vinculoProcesso.nome == (emptyVinculoArvore.vinculoArvore as VinculoArvoreProcesso).vinculoProcesso.nome
                && vinculoArvore.idProcessoFase && emptyVinculoArvore.idProcessoFase) {
                vinculoArvore.emptyTreeProcesso = emptyVinculoArvore.emptyVinculoArvore;
            }
            if ((vinculoArvore as VinculoArvoreCliente).vinculoCliente != undefined
                && emptyVinculoArvore.vinculoArvore instanceof VinculoArvoreCliente
                && !vinculoArvore.ocultarVinculoArvore
                && (vinculoArvore as VinculoArvoreCliente).vinculoCliente.nome == (emptyVinculoArvore.vinculoArvore as VinculoArvoreCliente).vinculoCliente.nome) {
                vinculoArvore.emptyTreeCliente = emptyVinculoArvore.emptyVinculoArvore;
            }
            if ((vinculoArvore as VinculoArvoreProduto).vinculoProduto != undefined
                && emptyVinculoArvore.vinculoArvore instanceof VinculoArvoreProduto
                && !vinculoArvore.ocultarVinculoArvore
                && (vinculoArvore as VinculoArvoreProduto).vinculoProduto.nome == (emptyVinculoArvore.vinculoArvore as VinculoArvoreProduto).vinculoProduto.nome) {
                vinculoArvore.emptyTreeProduto = emptyVinculoArvore.emptyVinculoArvore;
            }
            if ((vinculoArvore as VinculoArvoreGarantia).vinculoGarantia != undefined
                && emptyVinculoArvore.vinculoArvore instanceof VinculoArvoreGarantia
                && !vinculoArvore.ocultarVinculoArvore
                && (vinculoArvore as VinculoArvoreGarantia).vinculoGarantia.nome == (emptyVinculoArvore.vinculoArvore as VinculoArvoreGarantia).vinculoGarantia.nome) {
                vinculoArvore.emptyTreeGarantia = emptyVinculoArvore.emptyVinculoArvore;
            }
        })
    }

    /**
     * Verifica se a estrutra da arvore estará vazia; dessa forma evita renderizações nulas.
     * @param vinculoArvore 
     */
    checkBodyEmptyTree(vinculoArvore: VinculoArvore): boolean {
        const vinculoProcesso: VinculoProcesso = (vinculoArvore as VinculoArvoreProcesso).vinculoProcesso;
        const vinculoCliente: VinculoCliente = (vinculoArvore as VinculoArvoreCliente).vinculoCliente;
        const vinculoProduto: VinculoProduto = (vinculoArvore as VinculoArvoreProduto).vinculoProduto;
        const vinculoGarantia: VinculoGarantia = (vinculoArvore as VinculoArvoreGarantia).vinculoGarantia;
        let emptyTree = true;
        if (vinculoProcesso && !vinculoArvore.idProcessoFase) {
            emptyTree = !vinculoArvore.emptyTreeProcesso;
        }
        if (vinculoProcesso && vinculoArvore.idProcessoFase) {
            emptyTree = !vinculoArvore.emptyTreeProcesso;
        }
        if (vinculoCliente) {
            emptyTree = !vinculoArvore.ocultarVinculoArvore && !vinculoArvore.emptyTreeCliente;
        }
        if (vinculoProduto) {
            emptyTree = !vinculoArvore.ocultarVinculoArvore && !vinculoArvore.emptyTreeProduto;
        }
        if (vinculoGarantia) {
            emptyTree = !vinculoArvore.ocultarVinculoArvore && !vinculoArvore.emptyTreeGarantia;
        }
        return emptyTree;
    }

}