import { VinculoArvore } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DossieProduto } from "../../../../model";
import { VinculoArvoreCliente } from "../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { VinculoPessoas } from "../../../../model/vinculos-pessoas";
import { GerenciadorDocumentosEmArvore } from "../../../../documento/gerenciandor-documentos-em-arvore/gerenciador-documentos-em-arvore.util";
import { INDEX_ABA_DOCUMENTO_DOSSIE, TIPO_VINCULO } from "../../../../constants/constants";
import { NodeApresentacao } from "../../../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";
import { Utils } from "src/app/utils/Utils";

export class ValidadorDocumentosPessoa{
    
    /**
   * Quando o Vinculo Arvore for relacionado a Pessoa
   * @param arvore 
   * @param dossieProduto 
   * @param vinculo_pessoa 
   */
  public static validarVinculoArvorePessoa(arvore: VinculoArvore, dossieProduto: DossieProduto, vinculo_pessoa: VinculoPessoas) {
      vinculo_pessoa = new VinculoPessoas();
      this.validarDocsArvoreCliente(vinculo_pessoa, arvore, dossieProduto);
      if (dossieProduto.camposValidado) {
        dossieProduto.vinculos_pessoas.push(vinculo_pessoa);
      }else {
        GerenciadorDocumentosEmArvore.destacarArvoreComCampoObrigatorio(arvore, dossieProduto);
      }
      return vinculo_pessoa;
  }

  /**
   * Ações nescessaria para quando for vinculo Cliente
   * @param vinculo_pessoa 
   * @param vinculoArvoreCliente 
   * @param newProduto 
   * @param documentoObrigatorio 
   */
  private static validarDocsArvoreCliente(vinculo_pessoa: VinculoPessoas, vinculoArvoreCliente: VinculoArvoreCliente, dossieProduto:DossieProduto) { 
    vinculo_pessoa.dossie_cliente = vinculoArvoreCliente.vinculoCliente.id;
    vinculo_pessoa.dossie_cliente_relacionado = undefined != vinculoArvoreCliente.vinculoCliente.dossie_cliente_relacionado ? vinculoArvoreCliente.vinculoCliente.dossie_cliente_relacionado : undefined;
    vinculo_pessoa.sequencia_titularidade = undefined != vinculoArvoreCliente.vinculoCliente.sequencia_titularidade ? vinculoArvoreCliente.vinculoCliente.sequencia_titularidade : null ;
    vinculo_pessoa.tipo_relacionamento = vinculoArvoreCliente.vinculoCliente.tipo_relacionamento.id;
    vinculo_pessoa.dossie_cliente_relacionado = vinculoArvoreCliente.vinculoCliente.dossie_cliente_relacionado;
    vinculo_pessoa.dossie_cliente_relacionado_anterior = vinculoArvoreCliente.vinculoCliente.dossie_cliente_relacionado_anterior;
    vinculo_pessoa.sequencia_titularidade_anterior = vinculoArvoreCliente.vinculoCliente.seqTitularidadeAntiga;
    vinculo_pessoa.exclusao = vinculoArvoreCliente.vinculoCliente.exclusao;
    vinculo_pessoa.respostas_formulario = [];
    if(vinculoArvoreCliente.vinculoCliente.respostas_formulario && vinculoArvoreCliente.vinculoCliente.respostas_formulario.length > 0) {
      vinculo_pessoa.respostas_formulario = vinculoArvoreCliente.vinculoCliente.respostas_formulario;
    }
    if(!vinculo_pessoa.exclusao) {
      if (undefined != vinculoArvoreCliente.noApresentacao && vinculoArvoreCliente.noApresentacao.length > 0) {
        vinculoArvoreCliente.classeValidacao = "";
        vinculo_pessoa.documentos_utilizados = [];
        vinculo_pessoa.documentos_novos = [];
        vinculoArvoreCliente.noApresentacao.forEach(no => {
          let documentoObrigatorio: string;
          this.validarTodosDocumentosVinculoArvoreCliente(no, vinculo_pessoa, TIPO_VINCULO.CLIENTE, documentoObrigatorio, dossieProduto, vinculoArvoreCliente);
          this.validarQtdObrigatorioDocumentoNovos(no, vinculo_pessoa, documentoObrigatorio, dossieProduto, vinculoArvoreCliente);
        });
      }
    }
  }

    /**
     * Método responsavel por percorre o noApresentação e encontra os documentos de imagem
     * @param no no a ser percorrido
     * @param processo variavel responsavel por receber o tipo de arvore que esta senco percorrida e vai ser add o elementos imagens
     * @param tipo define se e true pra documento elemento a ser adicionado e falso pra novos documentos para pessoa vinculada
     */
    private static validarTodosDocumentosVinculoArvoreCliente(no: NodeApresentacao, processo: any, tipo:string, documentoObrigatorio:any, dossieProduto:DossieProduto, vinculoArvoreCliente: VinculoArvoreCliente) {
      let pastaVazia = true;
      if((no.children == undefined || no.children.length == 0)){
        documentoObrigatorio = this.montarTipoNovosDocumentos(tipo, no, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
        documentoObrigatorio = this.montarDocumentosUtilizados(tipo, no, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
        ValidadorDocumentosPessoa.montarListaDocumentosFalta(dossieProduto, no, processo, documentoObrigatorio, vinculoArvoreCliente);
      }
      no.children.forEach(noInterno => {
        if (noInterno.children != undefined && noInterno.children.length > 0) {
            this.validarTodosDocumentosVinculoArvoreCliente(noInterno, processo, tipo, documentoObrigatorio, dossieProduto, vinculoArvoreCliente);
        }else {              
            documentoObrigatorio = this.montarTipoNovosDocumentos(tipo, noInterno, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
            documentoObrigatorio = this.montarDocumentosUtilizados(tipo, noInterno, processo, documentoObrigatorio, dossieProduto.rascunho, pastaVazia);
            ValidadorDocumentosPessoa.montarListaDocumentosFalta(dossieProduto, no, processo, documentoObrigatorio, vinculoArvoreCliente);
        }
      });
      if (!dossieProduto.rascunho) {
        this.validarQtdObrigatorioDocumentoNovos(no, processo, documentoObrigatorio, dossieProduto, vinculoArvoreCliente);
      }
    }

  private static montarListaDocumentosFalta(dossieProduto: DossieProduto, no: NodeApresentacao, processo: any, documentoObrigatorio: any, vinculoArvoreCliente: VinculoArvoreCliente) {
    if (!dossieProduto.rascunho) {
      GerenciadorDocumentosEmArvore.validarCampoObrigatorio(no, dossieProduto, INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO, documentoObrigatorio, vinculoArvoreCliente);
    }
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
    private static montarTipoNovosDocumentos(tipo: string, noInterno: NodeApresentacao, processo: any, documentoObrigatorio: any, validar: boolean, pastaVazia: boolean) {
      if (tipo === "cliente") {
          let documentos_novos = GerenciadorDocumentosEmArvore.montarDocumentoNovos(noInterno);
          if (undefined != documentos_novos) {
              processo.documentos_novos.push(documentos_novos);
          }else {
              documentoObrigatorio = GerenciadorDocumentosEmArvore.informDocumentoObrigatorio(validar, pastaVazia, documentos_novos, noInterno, documentoObrigatorio);
          }
      }
      return documentoObrigatorio;
    }

    /**
     * Preenche os ids dos documentos reutilizados do dossie cliente em uma lista.
     * 
     * @param tipo tipo de vinculo da arvore de docuentos
     * @param noInterno NO da arvore de documentos
     * @param processo tipo de vinculo manipulado pela arvore: cliente, produto, garantia
     * @param documentoObrigatorio flaz de sinalização de documento obrigatório
     * @param validar boolean que sinaliza se o dossie é rascunho ou não
     * @param pastaVazia flag que sinaliza se a pasta da arvore está vazia, inicia com true
     */
    private static montarDocumentosUtilizados(tipo: string, noInterno: NodeApresentacao, processo: any, 
            documentoObrigatorio: any, validar: boolean, pastaVazia: boolean) {
        if (tipo === TIPO_VINCULO.CLIENTE) {
            let idDocumentoUtilizado:number = GerenciadorDocumentosEmArvore.selecionaDocumentoReutilizado(noInterno);
            if (undefined != idDocumentoUtilizado) {
                processo.documentos_utilizados.push(idDocumentoUtilizado);
            }else {
                documentoObrigatorio = GerenciadorDocumentosEmArvore.informDocumentoObrigatorio(validar, pastaVazia, idDocumentoUtilizado, noInterno, documentoObrigatorio);
            }
        }
        return documentoObrigatorio;
    }


    /**
     * Validar Qtd Obrigatoria para Vinculo Clientes
     * @param no 
     * @param processo 
     * @param documentoObrigatorio 
     * @param dossieProduto 
     * @param vinculoArvoreCliente 
     */
    private static validarQtdObrigatorioDocumentoNovos(no, processo: any, documentoObrigatorio: any, dossieProduto: DossieProduto, vinculoArvoreCliente: VinculoArvoreCliente) {
      let qtdUri = GerenciadorDocumentosEmArvore.verificarExistenciaDeUri(no);
      if (!dossieProduto.rascunho && ((no.obrigatorio && (no.quantidade_obrigatorio && no.quantidade_obrigatorio > qtdUri)) || (no.obrigatorio && qtdUri == 0 && (no.quantidade_obrigatorio > 0 && undefined == documentoObrigatorio || documentoObrigatorio == "")))) {
          GerenciadorDocumentosEmArvore.definirAcoesQuandoCampoObrigatorio(dossieProduto, vinculoArvoreCliente);
          dossieProduto.descricaoDocumento = "É necessário informar no mínimo " + no.quantidade_obrigatorio + " documentos. Para " + no.label + " na árvore " + vinculoArvoreCliente.vinculoCliente.tipo_relacionamento.nome + " - " + vinculoArvoreCliente.vinculoCliente.nome + ": " + (vinculoArvoreCliente.vinculoCliente.cnpj ? Utils.masKcpfCnpj(vinculoArvoreCliente.vinculoCliente.cnpj)  : Utils.masKcpfCnpj(vinculoArvoreCliente.vinculoCliente.cpf)) ;
          let msg = {
              "msg": dossieProduto.descricaoDocumento
          }
          dossieProduto.listaDocumentosFalta.push(msg)
      }
    }  
}