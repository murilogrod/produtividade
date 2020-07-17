import { RespostaCampoFormulario } from "../../../model/resposta-campo-formulario";
import { TIPO_DOCUMENTO } from "../../../constants/constants";
import { CampoFormulario, DossieProduto, VinculoGarantia, VinculoProduto } from "../../../model";
import { VinculoArvoreGarantia } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { VinculoArvoreProcesso } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo";
import { VinculoArvoreCliente } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { VinculoArvoreProduto } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { VinculoPessoas } from "../../../model/vinculos-pessoas";
import { VinculoArvore } from "../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { ValidadorDocumentosProduto } from "./validadores-documentos-dossie-produto/validador-documentos-produto.util";
import { ValidadorDocumentosPessoa } from "./validadores-documentos-dossie-produto/validador-documentos-pessoa.utils";
import { ValidadorDocumentosGarantia } from "./validadores-documentos-dossie-produto/validador-documentos-garantia.utils";
import { ValidadorDocumentosProcesso } from "./validadores-documentos-dossie-produto/validador-documentos-processo.utils";
import { Utils } from 'src/app/utils/Utils';

export class GerenciadorDossieProduto{

    private static listaVinculoArvore: Array<VinculoArvore>;
    private static listaVinculoArvoreExclusao: Array<VinculoArvore> = [];

    /**
     * Guarda a referencia da lista de arvore de documentos.
     * @param listaVinculoArvore lista de arvore de documentos.
     */
    public static setListaVinculoArvore(listaVinculoArvore: Array<VinculoArvore>){
        this.listaVinculoArvore = listaVinculoArvore;
    }

    public static getListaVinculoArvore(): Array<VinculoArvore> {
      return this.listaVinculoArvore;
    }
    
    /**
     * Mapea as informações das repostas dos campos de formulário no objeto de respostas_formulario.
     * @param campoFormulario array contém os campos de formularios editados
     * @param dossieProduto objeto do dossiê de produto
     */
    public static populaRespostaCamposFormulario(campoFormulario: CampoFormulario[], dossieProduto: any){
      dossieProduto.respostas_formulario = this.preencherFormularioResposta(campoFormulario);
    }

    public static preencherFormularioResposta(campoFormulario: CampoFormulario[]):any[]{
      let respostaPreenchidas: any [] = [];
  
      if(campoFormulario && campoFormulario.length > 0){
        for (let campo of campoFormulario) {
          
          let respostaCampo: RespostaCampoFormulario = new RespostaCampoFormulario();
          respostaCampo.campo_formulario = campo.id;
          respostaCampo.resposta = campo.resposta_aberta;
  
          if(campo.resposta_aberta && campo.tipo_campo === TIPO_DOCUMENTO.INPUT_RADIO){
            respostaCampo.opcoes_selecionadas = [];
            let opcaoSelecionada: any = campo.opcoes_disponiveis.find(opcaoSelecionada => opcaoSelecionada.valor_opcao === campo.resposta_aberta).valor_opcao;
            respostaCampo.resposta =  null;
            respostaCampo.opcoes_selecionadas.push(opcaoSelecionada);
          }
  
          if(campo.tipo_campo === TIPO_DOCUMENTO.INPUT_CHECKBOX) {
            respostaCampo.opcoes_selecionadas = [];
            if(campo.string_selecionadas && campo.string_selecionadas.length > 0){
              campo.string_selecionadas.forEach((opcao) => {
                respostaCampo.resposta =  null;
                respostaCampo.opcoes_selecionadas.push(opcao);
              });
            }
          }
  
          if(campo.tipo_campo === TIPO_DOCUMENTO.SELECT) {
            respostaCampo.opcoes_selecionadas = [];
            if(campo.resposta_aberta) {
              let opcaoSelecionada: any = campo.opcoes_disponiveis.find(x => x.valor_opcao === campo.resposta_aberta).valor_opcao;
              respostaCampo.resposta =  null;
              respostaCampo.opcoes_selecionadas.push(opcaoSelecionada);
            }
          }
  
          if(campo.tipo_campo === TIPO_DOCUMENTO.CONTA_CAIXA && respostaCampo.resposta){
            respostaCampo.resposta = respostaCampo.resposta.replace(/\D/g, '');
            respostaPreenchidas.push(respostaCampo);
            
          }else if(respostaCampo.campo_formulario && (respostaCampo.resposta || (respostaCampo.opcoes_selecionadas && respostaCampo.opcoes_selecionadas.length > 0))) {
            respostaPreenchidas.push(respostaCampo);
          }
        }
      }
  
      return respostaPreenchidas;
    }
      /**
   * Metodo responsavel por encontra o vinculo, e chamar ações nescessaria para cada vinculo
   * @param lista 
   * @param newProduto 
   * @param produto_contratado 
   * @param garantias_informadas 
   * @param vinculo_pessoa 
   */
  static validaPreparaDossieProduto(listaVinculoArvore : Array<VinculoArvore>, dossieProduto:DossieProduto, 
        produto_contratado:VinculoProduto, garantias_informadas:VinculoGarantia,  vinculo_pessoa:VinculoPessoas): DossieProduto {        
    if(undefined != listaVinculoArvore) {
      dossieProduto.processo_origem =listaVinculoArvore[0].id;//configura valor do processo dossie de origem             
      dossieProduto.camposValidado = true; 
      dossieProduto.nCamposObrigatoirio = false;
      dossieProduto.listaDocumentosFalta = [];
      dossieProduto.lstDescricaoDoc = [];
      for( let vinculoArvore of listaVinculoArvore){
        this.verificaTipoVinculoArvoreAtualParaIniciarVaidacao(vinculoArvore, dossieProduto, produto_contratado, garantias_informadas, vinculo_pessoa);
        vinculo_pessoa = new VinculoPessoas();
        produto_contratado = new VinculoProduto();
        garantias_informadas = new VinculoGarantia();
      }
      // this.removeArvoreCasoVinculoFoiExcluido(listaVinculoArvore);
    }
    return dossieProduto;
  }

  /**
   * 
   * @param vinculoArvore 
   * @param dossieProduto 
   * @param produto_contratado 
   * @param garantias_informadas 
   * @param vinculo_pessoa 
   */
  private static verificaTipoVinculoArvoreAtualParaIniciarVaidacao(vinculoArvore: VinculoArvore, dossieProduto:DossieProduto,
            produto_contratado:VinculoProduto, garantias_informadas:VinculoGarantia, vinculo_pessoa:VinculoPessoas){
    if (vinculoArvore instanceof VinculoArvoreCliente) {
      vinculo_pessoa = ValidadorDocumentosPessoa.validarVinculoArvorePessoa(vinculoArvore, dossieProduto, vinculo_pessoa);
    }
    if (vinculoArvore instanceof VinculoArvoreProduto) {
      produto_contratado = ValidadorDocumentosProduto.validarVinculoArvoreProduto(vinculoArvore, dossieProduto, produto_contratado);
    }
    if(vinculoArvore instanceof VinculoArvoreGarantia){
        garantias_informadas = ValidadorDocumentosGarantia.validarVinculoArvoreGarantia(vinculoArvore, dossieProduto, garantias_informadas);
    }
    if(vinculoArvore instanceof VinculoArvoreProcesso){
      ValidadorDocumentosProcesso.validarVinculoArvoreProcesso(vinculoArvore, dossieProduto);
    }
    this.addArvoreDocumentosListaExclusao(vinculoArvore, vinculo_pessoa, produto_contratado, garantias_informadas);
  }

  /**
   * Adicona na lista a arvore cujo vinculo foi excluido na aba vinculos.
   * @param arvoreDocumentos Arvore cujo o vinculo de pessoa, produto, garantia que ela representa foi excluido
   */
  private static addArvoreDocumentosListaExclusao(arvoreDocumentos: VinculoArvore,  
        vinculo_pessoa:VinculoPessoas, produto_contratado:VinculoProduto, garantias_informadas:VinculoGarantia){
    if(undefined != vinculo_pessoa.exclusao){
      this.listaVinculoArvoreExclusao.push(arvoreDocumentos);
    }
    if(undefined != produto_contratado.exclusao){
      this.listaVinculoArvoreExclusao.push(arvoreDocumentos);
    }
    if(undefined != garantias_informadas.exclusao){
      this.listaVinculoArvoreExclusao.push(arvoreDocumentos);
    }
  }

  /**
   * Remove  a arovre cujo vinculo foi excluido na aba vinculos.
   * @param listaVinculoArvore 
   * @param arvoreDocumentos 
   * @param vinculo_pessoa 
   * @param produto_contratado 
   * @param garantias_informadas 
   */
  private static removeArvoreCasoVinculoFoiExcluido(listaVinculoArvore : Array<VinculoArvore>){
      this.listaVinculoArvoreExclusao.forEach(arvoreAExcluir => {
        let indice = listaVinculoArvore.indexOf(arvoreAExcluir);
        listaVinculoArvore.splice(indice, 1);
    });
  }

  /**
   * Remove  os documentos de mesmo tipo que foram replicados na classificação de documentos
   * antes de enviar o dossieProduto para ser salvo no banco.
   * @param dossieProduto 
   */
  public static removeDocumentosReplicados(dossieProduto: DossieProduto): DossieProduto{
    for(let vinculoPessoa of dossieProduto.vinculos_pessoas){
      let documentosNovosUnicos: Array<any> = new Array<any>();
      if(vinculoPessoa.documentos_novos) {
        for(let documentoNovo of vinculoPessoa.documentos_novos){
          if(!documentosNovosUnicos.find(documento => documento.tipo_documento == documentoNovo.tipo_documento)){
            documentosNovosUnicos.push(documentoNovo);
          }
        }
        vinculoPessoa.documentos_novos = documentosNovosUnicos;
      }
    }
    for(let produtoVinculado of dossieProduto.produtos_contratados){
      let documentosUnicosProduto: Array<any> = new Array<any>();
      if(produtoVinculado && produtoVinculado.elementos_conteudos && produtoVinculado.elementos_conteudos.length > 0) {
        for(let elementoConteudoProduto of produtoVinculado.elementos_conteudos){
          if(!documentosUnicosProduto.find(elementoConteudo => elementoConteudo.documento.tipo_documento == elementoConteudoProduto.documento.tipo_documento)){
            documentosUnicosProduto.push(elementoConteudoProduto);
          }
        }
        produtoVinculado.elementos_conteudos = documentosUnicosProduto;
      }
    }
    for(let garantia of dossieProduto.garantias_informada){
      let documentosNovosGarantia: Array<any> = new Array<any>();
      if(garantia.documento) {
        for(let documentoNovo of garantia.documento){
          if(!documentosNovosGarantia.find(documento => documento.tipo_documento == documentoNovo.tipo_documento)){
            documentosNovosGarantia.push(documentoNovo);
          }
        }
        garantia.documento = documentosNovosGarantia;
      }
    }
    return dossieProduto;
  }   
}