import { Injectable } from "@angular/core";
import { ProdutosDocumentacaoPendenteService } from "../produtos-documentacao-pendente.service";
import { CrudProduto } from "src/app/cruds/model/crud-produto";
import { TIPO_PESSOA } from "src/app/constants/constants";
import { SolicitacaoVerificacaoAutorizacao } from "../model/solicitacao-verificacao-autorizacao.model";
import { ProdutoDossieDigital } from "../model/produto-dossie-digital.model";
import { VinculoCliente } from "src/app/model";
import { RetornoVerificacaoAutorizacao } from "../model/retorno-verificacao-autorizacao.model";
import { LoaderService } from "src/app/services";

@Injectable()
export class GridProdutosDocumentacaoPendenteComponentPresenter{
    solicitacaoVerificacaoAutorizacao: SolicitacaoVerificacaoAutorizacao;
    listProdutoDossieDigital: ProdutoDossieDigital[];
    produtoDossieDigitalSelecionado: ProdutoDossieDigital;
    listRetornoVerificacaoAutorizacao: RetornoVerificacaoAutorizacao[];
    contadorDocumentacaoPendente: number = 0 ;

    constructor(private produtosDocPendenteService: ProdutosDocumentacaoPendenteService,
		private loadService: LoaderService){
        this.listProdutoDossieDigital = new Array<ProdutoDossieDigital>();
        this.solicitacaoVerificacaoAutorizacao = new SolicitacaoVerificacaoAutorizacao();
        this.listRetornoVerificacaoAutorizacao = new Array<RetornoVerificacaoAutorizacao>();
    }

    /**
     * 
     * @param tipoPessoa 
     */
    public consultaProdutosDossieDigitalDisponiveis(tipoPessoa: string){
        this.produtosDocPendenteService.consultaProdutosDisponiveis()
            .subscribe((response: CrudProduto[]) => {
                let listProdutosPorPessoa:CrudProduto[] = this.filtraProdutosDossieDigitalPorTipoPessoa(response, tipoPessoa);
                this.listProdutoDossieDigital = this.converteParaListProdutoDossieDigital(listProdutosPorPessoa);                
            }, error => {
                console.log(error);
                this.loadService.hide();
                throw error;
            });
    }

    /**
     * 
     * @param listProdutosDisponiveis 
     * @param tipoPessoa 
     */
    private filtraProdutosDossieDigitalPorTipoPessoa(listProdutosDisponiveis: CrudProduto[], tipoPessoa: string): CrudProduto[]{
        let listProdutosPorPessoa: CrudProduto[] = listProdutosDisponiveis
            .filter(produto => produto.indicador_dossie_digital && 
                (produto.indicador_tipo_pessoa == tipoPessoa || 
                    produto.indicador_tipo_pessoa == TIPO_PESSOA.AMBOS));
        return listProdutosPorPessoa;
    } 

    /**
     * 
     * @param listProdutosPorPessoa 
     */
    private converteParaListProdutoDossieDigital(listProdutosPorPessoa: CrudProduto[]): ProdutoDossieDigital[]{
        let listProdutoDossieDigital = new Array<ProdutoDossieDigital>();
        for(let produto of listProdutosPorPessoa){
            let produtoDossieDigital: ProdutoDossieDigital = new ProdutoDossieDigital();
            produtoDossieDigital.operacao = produto.operacao_produto;
            produtoDossieDigital.modalidade = produto.modalidade_produto;
            produtoDossieDigital.nome = produto.nome_produto;
            listProdutoDossieDigital.push(produtoDossieDigital);
        }
        return listProdutoDossieDigital;
    }

    /**
     * 
     * @param vinculoCliente 
     */
    public populaObjetoSelecionadoPeloUsuario(vinculoCliente: VinculoCliente){
        this.solicitacaoVerificacaoAutorizacao = new SolicitacaoVerificacaoAutorizacao();
        if(vinculoCliente.tipo_pessoa == TIPO_PESSOA.FISICA){
            this.solicitacaoVerificacaoAutorizacao.cpf_cliente = new Number(vinculoCliente.cpf).valueOf();
        }else{
            this.solicitacaoVerificacaoAutorizacao.cnpj_cliente = new Number(vinculoCliente.cnpj).valueOf();
        }
        this.solicitacaoVerificacaoAutorizacao.operacao = new Number(this.produtoDossieDigitalSelecionado.operacao).valueOf();
        this.solicitacaoVerificacaoAutorizacao.modalidade = new Number(this.produtoDossieDigitalSelecionado.modalidade).valueOf();
    }

    /**
     * 
     */
    public verificarDocPendentesProduto(){
        this.produtosDocPendenteService.simulaAutorizacaoProduto(this.solicitacaoVerificacaoAutorizacao)
            .subscribe((response: RetornoVerificacaoAutorizacao[]) => {
                this.listRetornoVerificacaoAutorizacao = response;
            }, error =>{
                console.log(error);
                this.loadService.hide();
                throw error;
            });
    }
}