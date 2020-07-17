import { Injectable } from "@angular/core";
import { ProdutosDocumentacaoValidaService } from "../produtos-documentacao-valida.service";
import { NivelDocumental } from "../model/nivel-documental.model";
import { ProdutoHabilitado } from "../model/produto-habilitado.model";
import * as moment from 'moment';
import { MILISSEGUNDOS } from "src/app/constants/constants";
import { LoaderService } from "src/app/services";
import { LoadingModelNivelDocumental } from "../../model/loading-model-nivel-documental";

@Injectable()
export class GridProdutosDocumentacaoValidaComponentPresenter{
    nivelDocumental: NivelDocumental;
    
    constructor(private produtosDocsValidosService: ProdutosDocumentacaoValidaService,
        private loadService: LoaderService){}

    /**
     * 
     * @param idDossieCliente 
     */
    public atualizaNivelDocumental( idDossieCliente: number) {
        return this.produtosDocsValidosService.atualizaNivelDocumental(idDossieCliente)
            .subscribe(response => {
                this.converteParaNivelDocumentalModel(response);
            }, error => {
                console.log(error);
                this.loadService.hide();
                throw error;
            });
    } 

    /**
     * 
     * @param dataHoraApuracaoNivel 
     * @param idDossieCliente 
     */
    public verificaDataApuracaoExpirada(idDossieCliente: number) {
        if(!this.nivelDocumental.dataHoraApuracaoNivel){
            return this.atualizaNivelDocumental(idDossieCliente);
        }
        if((new Date().getTime() - moment(this.nivelDocumental.dataHoraApuracaoNivel, 'DD/MM/YYYY HH:mm:ss').valueOf()) > MILISSEGUNDOS.DIA){
            return this.atualizaNivelDocumental(idDossieCliente);
        }
    }

    /**
     * 
     * @param response 
     */
    public converteParaNivelDocumentalModel(response: any) {
        this.nivelDocumental.dataHoraApuracaoNivel = response.data_hora_apuracao_nivel;
        let listProdutosHalitados: ProdutoHabilitado[] = new Array<ProdutoHabilitado>();
        for(let produto_habilitado of response.produtos_habilitados){
            let produtoHabilitado = new ProdutoHabilitado();
            produtoHabilitado.codigoOperacao = produto_habilitado.codigo_operacao;
            produtoHabilitado.codigoModalidade = produto_habilitado.codigo_modalidade
            produtoHabilitado.nome = produto_habilitado.nome;
            listProdutosHalitados.push(produtoHabilitado);
        }
        this.nivelDocumental.produtosHabilitados = listProdutosHalitados;
    }
}