import { VinculoArvore } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DossieProduto } from "../../../../model";
import { VinculoArvoreProcesso } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo";
import { ProcessoOrigem } from "../../../../model/processo-origem";
import { GerenciadorDocumentosEmArvore } from "../../../../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util";
import { INDEX_ABA_DOCUMENTO_DOSSIE } from "../../../../constants/constants";
import { NodeApresentacao } from "../../../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";

export class ValidadorDocumentosProcesso{

     /**
   * Quando a garantia for do vinculo Garantias do Processo
   * @param arvore 
   * @param dossieProduto
   */
   public static validarVinculoArvoreProcesso(arvore: VinculoArvore, dossieProduto: DossieProduto) {
    GerenciadorDocumentosEmArvore.inicializarValidacao(arvore, dossieProduto);
    let processo_origem = new ProcessoOrigem();
    processo_origem.elementos_conteudos = [];
    processo_origem = this.validarDocsArvoreProcesso(processo_origem, arvore, dossieProduto);
    if (dossieProduto.camposValidado) {
        processo_origem.elementos_conteudos.forEach(elemento => {
            dossieProduto.elementos_conteudos.push(elemento);
        });
    } else {
        GerenciadorDocumentosEmArvore.destacarArvoreComCampoObrigatorio(arvore, dossieProduto);
    }
  }

  /**
   * Ações Nescessaria para Vinculo Processo
   * @param processo_origem 
   * @param vinculoArvoreProcesso 
   * @param newProduto 
   * @param documentoObrigatorio 
   */
  private static validarDocsArvoreProcesso(processo_origem: ProcessoOrigem, vinculoArvoreProcesso: VinculoArvoreProcesso, dossieProduto:DossieProduto) {    
    if (undefined != vinculoArvoreProcesso.noApresentacao && vinculoArvoreProcesso.noApresentacao.length > 0) {
      let documentoObrigatorio: string;
      vinculoArvoreProcesso.noApresentacao.forEach(no => {
        vinculoArvoreProcesso.classeValidacao = "";        
        this.validarTodosDocumentosVinculoArvoreProcesso(no, processo_origem, "elementoConteudo", documentoObrigatorio, dossieProduto, vinculoArvoreProcesso);
      });
    }
    return processo_origem;
  }

  /**
     * Método responsavel por percorre o noApresentação e encontra os documentos de imagem
     * @param no no a ser percorrido
     * @param processo variavel responsavel por receber o tipo de arvore que esta senco percorrida e vai ser add o elementos imagens
     * @param tipo define se e true pra documento elemento a ser adicionado e falso pra novos documentos para pessoa vinculada
     */
    private static validarTodosDocumentosVinculoArvoreProcesso(no: NodeApresentacao, processo: any, tipo:string, documentoObrigatorio:any, dossieProduto:DossieProduto, vinculoArvoreProcesso: VinculoArvoreProcesso) {
      let pastaVazia = true;
      if((no.children == undefined || no.children.length == 0)){
        documentoObrigatorio = this.montarTipoElementoConteudo(tipo, no, processo, documentoObrigatorio,  dossieProduto.rascunho, pastaVazia);
        if(!dossieProduto.rascunho) {
          GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, vinculoArvoreProcesso);
          GerenciadorDocumentosEmArvore.validarQtdObrigatorioDocumentos(no, processo, documentoObrigatorio, dossieProduto, vinculoArvoreProcesso);
        }
      }
      no.children.forEach(noInterno => {
          if (noInterno.children != undefined && noInterno.children.length > 0) {
              this.validarTodosDocumentosVinculoArvoreProcesso(noInterno, processo, tipo, documentoObrigatorio,  dossieProduto, vinculoArvoreProcesso);
          }else {              
              documentoObrigatorio = this.montarTipoElementoConteudo(tipo, noInterno, processo, documentoObrigatorio,  dossieProduto.rascunho, pastaVazia);
              if(!dossieProduto.rascunho) {
                GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, vinculoArvoreProcesso);
                GerenciadorDocumentosEmArvore.validarQtdObrigatorioDocumentos(no, processo, documentoObrigatorio, dossieProduto, vinculoArvoreProcesso);
              }
          }
      });
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