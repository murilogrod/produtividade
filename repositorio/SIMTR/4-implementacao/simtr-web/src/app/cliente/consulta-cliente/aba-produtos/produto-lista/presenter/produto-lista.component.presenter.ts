import { LocalStorageDataService } from './../../../../../services/local-storage-data.service';
import { Injectable, ElementRef } from "@angular/core";
import { ProdutoLista } from "../model/produto-lista.model";
import { Router } from "@angular/router";
import { TratamentoService } from "src/app/tratamento/tratamento.service";
import { LoaderService } from "src/app/services";
import { DataService } from "src/app/services/data-service";
import { ProdutoListaComponent } from "../view/produto-lista.component";
import { SITUACAO_DOSSIE_ATUAL } from "src/app/constants/constants";
import { TratamentoValidarPermissaoService } from "src/app/tratamento/tratamento-validar-permissao.service";
import { SortEvent } from "primeng/primeng";
import * as moment from 'moment';

@Injectable()
export class ProdutoListaComponentPresenter {

    produtoLista: ProdutoLista;

    constructor(private router: Router,
        private tratamentoService: TratamentoService,
        private loadService: LoaderService,
        private dataService: DataService,
        private tratamentoValidarPermissaoService: TratamentoValidarPermissaoService,
        private localStorageDataService: LocalStorageDataService) { }

    initConfigListaProdutos(lista: any[]) {
        this.inicializarColunaProdutos(lista);
        this.produtoLista.cols = this.produtoLista.configTableColsProdutos;
    }

    /**
     * Filtra os produtos conforme as propriedades do processo;
     * com pelo menos 3 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataProdutos 
     */
    filterProdutos(input: any, dataProdutos: any) {
        if (input.length == 0) {
            dataProdutos.filterGlobal(input, 'contains');
        }
        if (input.length > 2) {
            dataProdutos.filterGlobal(input, 'contains');
        }
    }

    selectDossieProduto(produtoListaComponent: ProdutoListaComponent, idDossieProduto) {
        this.loadService.show();

        this.tratamentoService.selecionaDossieProduto(idDossieProduto).subscribe(response => {

            this.loadService.hide();
            this.dataService.setData(idDossieProduto, response);
            this.router.navigate(['tratamentoDossie', idDossieProduto]);

        }, error => {
            this.loadService.hide();

            if (error.error && error.error.detalhe) {
                let detalhe = error.error.detalhe.replace("[", "").replace("]", "");
                produtoListaComponent.addMessageError(detalhe);
            }

            throw error;
        });
    }

    navigateManutencaoDossie(id: any, opcao: any) {
        this.router.navigate(['manutencaoDossie', id, opcao]);
    }

    showButtonTratamento(produto: any) {
        return produto != undefined && produto.tratamento_seletivo && produto.tratamento_seletivo == true
            && this.tratamentoValidarPermissaoService.hasCredentialParaTratameto() && this.tratamentoValidarPermissaoService.isPermissaoUnidadeDeTratamento(produto.unidades_tratamento)
            && produto.situacao_atual == SITUACAO_DOSSIE_ATUAL.AGUARDANDO_TRATAMENTO;
    }

    /**
     * Extrai a data sem apresentar a hora
     * @param value 
     */
    formatDateViewProdutos(value: string): string {
        return value.substring(0, 10);
    }

    /**
     * Retorna a lista de produtos entre virgulas
     * @param produtos 
     */
    formatProdutos(produtos: any[]) {
        let result: string = "";
        produtos.forEach(prod => {
            result += prod.nome + ", ";
        });
        return result.substring(0, result.length - 2);
    }

    /**
     * Ordencao dos campos: 'id_dossie_produto','produtos','processo_fase.nome','tipo_relacionamento.nome', 'unidade_criacao', 
     * 'situacao_atual', 'data_hora_situacao', 'unidade_situacao'
     * @param event 
     */
    customSort(event: SortEvent) {
        event.data.sort((data1, data2) => {
            let value1 = data1[event.field];
            let value2 = data2[event.field];
            let result = null;
            if (value1 == null && value2 != null) {
                result = -1;
            } else if (value1 != null && value2 == null) {
                result = 1;
            } else if (value1 == null && value2 == null) {
                result = 0;
            } else if (event.field.indexOf("produtos_contratados") == 0) {
                result = this.sortArrayProdutos(value1, value2, result);
            } else if (event.field.indexOf("processo_fase") == 0 || event.field.indexOf("tipo_relacionamento") == 0) {
                result = (value1.nome < value2.nome) ? -1 : (value1.nome > value2.nome) ? 1 : 0;
            } else if (event.field.indexOf("data_hora_situacao") == 0) {
                result = this.sortDataHoraSituacao(value1, value2, result);
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Ordenacao pelo campo data hora situacao
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortDataHoraSituacao(value1: any, value2: any, result: any) {
        const date1: Date = moment(value1, 'DD/MM/YYYY HH:mm:ss').toDate();
        const date2: Date = moment(value2, 'DD/MM/YYYY HH:mm:ss').toDate();
        const result1: boolean = (moment(date2, 'DD/MM/YYYY HH:mm:ss').valueOf() > moment(date1, 'DD/MM/YYYY HH:mm:ss').valueOf());
        const result2: boolean = (moment(date2, 'DD/MM/YYYY HH:mm:ss').valueOf() < moment(date1, 'DD/MM/YYYY HH:mm:ss').valueOf());
        result = (result1) ? -1 : (result2) ? 1 : 0;
        return result;
    }

    /**
     * Ordenacao pelo campo produtos contratos
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortArrayProdutos(value1: any, value2: any, result: any) {
        let result1: string = "";
        let result2: string = "";
        value1.forEach(p => {
            result1 += p.nome;
        });
        value2.forEach(p => {
            result2 += p.nome;
        });
        result = (result1 < result2) ? -1 : (result1 > result2) ? 1 : 0;
        return result;
    }

    /**
     * Inicializa a coluna produtos proveniente de array: produtos_contratados; 
     * para facilitar a indexação de pesquisa.
     * @param lista 
     */
    private inicializarColunaProdutos(lista: any[]) {
        if (lista && lista.length > 0) {
            lista.forEach(prod => {
                this.formatColumnProdutos(prod);
            });
        }
    }

    /**
     * Inicializa a coluna produtos
     * @param prod 
     */
    private formatColumnProdutos(prod: any) {
        if (prod.produtos_contratados.length > 0) {
            prod.produtos = this.formatProdutos(prod.produtos_contratados);
        }
    }

    /**
     * Verifica a partir do nome do processo se a unidade autorizada esta autorizada 
     * @param dossie 
     */
    checkUnidadeAutorizada(dossie: any) {
        let processoFilho = this.localStorageDataService.buscarProcessoPorNome(dossie.processo_dossie.nome);
        if (processoFilho == undefined) {
            return false;
        } else {
            return processoFilho.unidade_autorizada;
        }
    }
}