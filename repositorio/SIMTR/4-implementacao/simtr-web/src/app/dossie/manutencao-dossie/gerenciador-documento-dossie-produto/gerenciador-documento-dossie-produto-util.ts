import { DossieProduto, VinculoProduto, VinculoGarantia, Documento, VinculoCliente, CampoFormulario } from "../../../model";
import { VinculoPessoas } from "../../../model/vinculos-pessoas";
import { DOSSIE_PRODUTO } from "../../../constants/constants";
import { GerenciadorDossieProduto } from "./gerenciador-dossie-produto.util";
import { environment } from "src/environments/environment";

export class GerenciadorDocumentosDossieProduto extends GerenciadorDossieProduto{  
  
  /**
   * Metodo Responsavel por inicializar objetos
   * @param opcao 
   */
  public static inicializarObjetosParaSalvar(opcao: any): DossieProduto {
    let newProduto = new DossieProduto();
    newProduto.produtos_contratados = [];
    newProduto.garantias_informada = [];
    newProduto.vinculos_pessoas = [];
    newProduto.respostas_formulario = [];
    newProduto.elementos_conteudos = [];
    newProduto.rascunho = opcao === DOSSIE_PRODUTO.SALVAR_PARCIAL;
    return newProduto;
  }

  /**
     * Cria dossiê produto para ser salvo na base de dados.
     * @param opcao campo que define se define se o dossie de produto é rascunho ou não
     */
    public static criaDossieProduto(campoFormulario: CampoFormulario[], opcao: any): DossieProduto{
      let dossieProduto: DossieProduto = GerenciadorDocumentosDossieProduto.inicializarObjetosParaSalvar(opcao);
      super.populaRespostaCamposFormulario(campoFormulario, dossieProduto);
      GerenciadorDossieProduto.validaPreparaDossieProduto(super.getListaVinculoArvore(), dossieProduto, new VinculoProduto(), new VinculoGarantia(), new VinculoPessoas());
      if(dossieProduto.listaDocumentosFalta.length == 0) {
        return super.removeDocumentosReplicados(dossieProduto); 
      }
      return dossieProduto;
  }
}