import { VinculoArvore } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DossieProduto, VinculoProduto } from "../../../../model";
import { VinculoArvoreProduto } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { GerenciadorDocumentosEmArvore } from "../../../../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util";
import { INDEX_ABA_DOCUMENTO_DOSSIE } from "../../../../constants/constants";
import { NodeApresentacao } from "../../../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";

export class ValidadorDocumentosProduto{
    
    /**
   * Quando o Vinculo Árvore for de Produto
   * @param arvore 
   * @param dossieProduto 
   * @param produto_contratado 
   */
   public static validarVinculoArvoreProduto(arvore: VinculoArvore, dossieProduto: DossieProduto, produto_contratado: VinculoProduto) {
        produto_contratado = this.validarDocsArvoreProduto(produto_contratado, arvore, dossieProduto);
        if (dossieProduto.camposValidado) {
          dossieProduto.produtos_contratados.push(produto_contratado);
        }
        else {
            GerenciadorDocumentosEmArvore.destacarArvoreComCampoObrigatorio(arvore, dossieProduto);
        }
        return produto_contratado;
    }
    /**
     * Ações nescessaria para quando for Vinculo Produto
     * @param produto_contratado 
     * @param vinculoArvoreProduto 
     * @param newProduto 
     * @param documentoObrigatorio 
     */
    public static validarDocsArvoreProduto(produto_contratado: VinculoProduto, vinculoArvoreProduto: VinculoArvoreProduto, dossieProduto:DossieProduto) {
        let vinculoProduto = Object.assign({}, vinculoArvoreProduto.vinculoProduto);
        vinculoProduto.codigo_operacao = undefined;
        vinculoProduto.codigo_modalidade = undefined;
        produto_contratado = JSON.parse(JSON.stringify(vinculoProduto));

        if (undefined != vinculoArvoreProduto.noApresentacao && vinculoArvoreProduto.noApresentacao.length > 0) {
        produto_contratado.elementos_conteudos = [];
        vinculoArvoreProduto.classeValidacao = "";
        vinculoArvoreProduto.noApresentacao.forEach(no => {
            let documentoObrigatorio: string;
            documentoObrigatorio = this.validarTodosDocumentosVinculoArvoreProduto(no, produto_contratado, "elementoConteudo", documentoObrigatorio, dossieProduto);   
            if(!dossieProduto.rascunho) {
                GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, vinculoArvoreProduto)
                GerenciadorDocumentosEmArvore.validarQtdObrigatorioDocumentos(no, produto_contratado, documentoObrigatorio, dossieProduto, vinculoArvoreProduto);
            }
        });
        }
        return produto_contratado;
    }

    /**
     * Método responsavel por percorre o noApresentação e encontra os documentos de imagem
     * @param no no a ser percorrido
     * @param processo variavel responsavel por receber o tipo de arvore que esta senco percorrida e vai ser add o elementos imagens
     * @param tipo define se e true pra documento elemento a ser adicionado e falso pra novos documentos para pessoa vinculada
     */
    private static validarTodosDocumentosVinculoArvoreProduto(no: NodeApresentacao, processo: any, tipo:string, documentoObrigatorio:any, dossieProduto:DossieProduto) {
        let pastaVazia = true;
        if((no.children == undefined || no.children.length == 0)){
            documentoObrigatorio = this.montarTipoElementoConteudo(tipo, no, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
            if(!dossieProduto.rascunho) {
                GerenciadorDocumentosEmArvore.validarQtdObrigatorioDocumentos(no, processo, documentoObrigatorio, dossieProduto, processo);
                GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, processo);
            }
        }
        no.children.forEach(noInterno => {
            if (noInterno.children != undefined && noInterno.children.length > 0) {
                this.validarTodosDocumentosVinculoArvoreProduto(noInterno, processo, tipo, documentoObrigatorio, dossieProduto);
            }else {              
                documentoObrigatorio = this.montarTipoElementoConteudo(tipo, noInterno, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
                if(!dossieProduto.rascunho) {
                    GerenciadorDocumentosEmArvore.validarQtdObrigatorioDocumentos(no, processo, documentoObrigatorio, dossieProduto, processo);
                    GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, processo);
                }
            }
        });
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
     * */
    private static montarTipoElementoConteudo(tipo: string, noInterno: NodeApresentacao, processo: any, documentoObrigatorio: any, validar: boolean, pastaVazia: boolean) {
        if (tipo == "elementoConteudo") {
            let elementoConteudo = GerenciadorDocumentosEmArvore.montarElementosConteudos(noInterno);
            if (undefined != elementoConteudo) {
                if(undefined == processo.elementos_conteudos){
                    processo.elementos_conteudos = [];   
                }
                processo.elementos_conteudos.push(elementoConteudo);
            }else {
                documentoObrigatorio = GerenciadorDocumentosEmArvore.informDocumentoObrigatorio(validar, pastaVazia, elementoConteudo, noInterno, documentoObrigatorio);
            }
        }
        return documentoObrigatorio;
    }
}