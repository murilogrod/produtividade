import { LocalStorageDataService } from './../../../services/local-storage-data.service';
import { Injectable } from "@angular/core";
import { TabelaSituacao } from "../model/tabela-situacao.model";
import { SITUACAO_DOSSIE_BANCO, SITUACAO_DOSSIE_PRODUTO } from "src/app/constants/constants";
import { Router } from "@angular/router";
import { UtilsDash } from "../../utils-dash/utils-dash";
import { SortEvent } from "primeng/primeng";
@Injectable()
export class TabelaSituacaoComponentPresenter {

    tabelaSituacao: TabelaSituacao;

    constructor(private router: Router,
                private localStorageDataService: LocalStorageDataService) { }

    /**
     * Carrega a configuração da tabela
     * @param listaDossies 
     */
    initConfigTable(listaDossies: any[]) {
        this.tabelaSituacao.cols = this.tabelaSituacao.configTable;
        this.tabelaSituacao.rowTotal = listaDossies.length;
    }

    /**
     * Extrai a data sem apresentar a hora
     * @param value 
     */
    formatDateViewDashboard(value: string): string {
        return value.substring(0, 10);
    }

    /**
     * Formata as unidades/produtos; para mostrar apenas 1 unidade/produto em tela
     * @param values 
     * @param product 
     */
    formatUnidadesProdutosContratados(values: string, product: boolean): string {
        let regex: string;
        if (product) {
            regex = " ";
        } else {
            regex = ","
        }
        const split: string[] = values.split(regex);
        let value: string = split[0];
        if (split.length > 1) {
            value += " ..."
        }
        return value;
    }

    /**
     * Verifica o tipo de situação para apresentação de funcionalidade de edição
     * @param situacao_atual 
     */
    checkSituation(situacao_atual: string) {
        return SITUACAO_DOSSIE_BANCO.RASCUNHO == situacao_atual
            || SITUACAO_DOSSIE_BANCO.PENDENTE_INFORMACAO == situacao_atual
            || SITUACAO_DOSSIE_BANCO.AGUARDANDO_ALIMENTACAO == situacao_atual;
    }

    /**
     * Retonar o valor do texto todo minusculo para posterior capitalização
     * @param value 
     */
    setLowerCaseText(value: string) {
        return value.toLowerCase();
    }

    /**
     * Rediciona para manutençãoDossie para manter
     * @param dossie 
     */
    redirectManterManutecaoDossie(dossie) {
        this.router.navigate(['manutencaoDossie', dossie.id, SITUACAO_DOSSIE_PRODUTO.MANTER]);
        return;
    }

    /**
     * Rediciona para manutençãoDossie para consultar
     * @param dossie 
     */
    redirectConsultarManutecaoDossie(dossie) {
        this.router.navigate(['manutencaoDossie', dossie.id, SITUACAO_DOSSIE_PRODUTO.CONSULTAR]);
        return;
    }

    /**
     * Adiciona zeros a esquerda caso necessário e realiza a formatação 
     * de acordo o tipo
     * @param cnpj 
     * @param cpf 
     */
    formatLikePerson(cnpj: string, cpf: string): string {
        return UtilsDash.formatLikePerson(cnpj, cpf);
    }

    /**
     * Formata a tooltip para a coluna atendimento e produtos; colocando uma barra como separador
     * @param value 
     */
    formatTooltipUnidadeProduto(value: string): string {
        const typeRegex: boolean = value.indexOf(",") > 0 ? true : false;
        const split: string[] = value.split(typeRegex ? "," : " ");
        let formattedValue: string = "";
        split.forEach(obj => {
            if (obj.trim().length > 0) {
                formattedValue += obj + " / ";
            }
        });
        formattedValue = formattedValue.substring(0, formattedValue.length - 2).replace("-", " / ");
        return formattedValue;
    }

    /**
     * Realiza a ordenação dos campos grid tabela situacao
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
            } else if (event.field.indexOf("data_hora_criacao") == 0) {
                result = this.sortDataHoraCriacao(value1, value2, result);
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Ordenacao pelo campo data hora criação
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortDataHoraCriacao(value1: any, value2: any, result: any) {
        return UtilsDash.sortDate(value1, value2, result, 'DD/MM/YYYY HH:mm:ss');
    }

    /**
     * Verifica a partir do nome do processo se a unidade autorizada esta autorizada 
     * @param dossie 
     */
    checkUnidadeAutorizada(dossie: any) {
        let processoFilho = this.localStorageDataService.buscarProcessoPorNome(dossie.processo_dossie);
        return processoFilho.unidade_autorizada;
    }

}