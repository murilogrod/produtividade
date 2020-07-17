import { VinculoProduto, VinculoGarantia, CampoFormulario, DossieProduto } from "../../../model";
import { VinculoPessoas } from "../../../model/vinculos-pessoas";
import { DOSSIE_PRODUTO } from "../../../constants/constants";
import { GerenciadorDossieProduto } from "./gerenciador-dossie-produto.util";
import { environment } from "src/environments/environment";

export class GerenciadorDocumentoDossieProdutoEditado extends GerenciadorDossieProduto{

    /**
     * Realiza a edição do dossiê produto na base de dados.
     * @param opcao campo que define se define se o dossie de produto vai ser finalizado ou não
     */
    public static criaDossieProdutoEdicao(campoFormulario: CampoFormulario[], opcao: any, justificativa: string): DossieProduto{
        let dossieProdutoEditado: DossieProduto = GerenciadorDocumentoDossieProdutoEditado.inicializaObjetoDossieProdutoEditado(opcao, justificativa);
        super.populaRespostaCamposFormulario(campoFormulario, dossieProdutoEditado);
        GerenciadorDossieProduto.validaPreparaDossieProduto(super.getListaVinculoArvore(), dossieProdutoEditado, new VinculoProduto(), new VinculoGarantia(), new VinculoPessoas());
        if(dossieProdutoEditado.listaDocumentosFalta.length == 0) {
            return super.removeDocumentosReplicados(dossieProdutoEditado);
        }
        return dossieProdutoEditado;
    }

    /**
     * Metodo Responsavel por inicializar objeto dossieProdutoEditado
     * @param opcao 
    */
    public static inicializaObjetoDossieProdutoEditado(opcao: any, justificativa: string): DossieProduto{
        let dossieProdutoEditado = new DossieProduto();
        dossieProdutoEditado.produtos_contratados = [];
        dossieProdutoEditado.garantias_informada = [];
        dossieProdutoEditado.vinculos_pessoas = [];
        dossieProdutoEditado.respostas_formulario = [];
        dossieProdutoEditado.elementos_conteudos = [];
        dossieProdutoEditado.rascunho = opcao === DOSSIE_PRODUTO.SALVAR_PARCIAL;
        dossieProdutoEditado.cancelamento = opcao === DOSSIE_PRODUTO.REVOGAR;
        dossieProdutoEditado.finalizacao = opcao === DOSSIE_PRODUTO.SALVAR_ENVIAR;
        dossieProdutoEditado.justificativa = justificativa;
        return dossieProdutoEditado;
    }
}