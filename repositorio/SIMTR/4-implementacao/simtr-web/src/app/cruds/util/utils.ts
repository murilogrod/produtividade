import { ProcessoPatriarca } from "src/app/services/model/processo-patriarca.model";
import { InterfaceProcesso } from "../model/processo.interface";
import { FuncaoDocumentalTipologia } from "src/app/services/model/funcao-documental-tipologia.model";
import { InterfaceFuncaoDocumental } from "../model/funcao-documental.interface";
import { TipoDocumentoTipologia } from "src/app/services/model/tipo-documento-tipologia.model";
import { InterfaceTipoDocumento } from "../model/tipo-documento.interface";
import * as moment from 'moment';
import { Observable } from "rxjs";
import { TipoPessoa } from "../model/tipo-pessoa.enum";
import { SelectTipoPessoa } from "../model/select-tipo-pessoa.interface";
import { SelectItem } from 'primeng/primeng';
import { TipoCampo } from '../model/tipo-campo.enum';
import { TipoAtributoGeral } from '../model/tipo-atributo-geral.enum';
import { TipoCampoComparacaoReceita } from '../model/tipo-campo-comparacao-receita.enum';
import { TipoCampoModoComparacaoReceita } from '../model/tipo-campo-modo-comporacao-receita.enum';
import { TipoModoPartilha } from '../model/tipo-modo-partilha';
import { TipoEstrategiaPartilha } from '../model/tipo-estrategia-partilha.enum';
import { ModalAtributoModel } from "../atributo/modal-atributo/model/modal-atributo-model";

export class Utils {

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    static custumizeRowsPerPageOptions(values: any[]): Array<number> {
        const rowsPerPageOption: Array<number> = new Array<number>();
        if (values.length >= 15) {
            rowsPerPageOption.push(15);
        } else if (values.length < 15) {
            rowsPerPageOption.push(values.length);
        }
        if (values.length >= 30) {
            rowsPerPageOption.push(30);
        } else if (values.length > 15 && values.length < 30) {
            rowsPerPageOption.push(values.length);
        }
        if (values.length >= 50) {
            rowsPerPageOption.push(50);
            rowsPerPageOption.push(values.length);
        } else if (values.length > 30 && values.length < 50) {
            rowsPerPageOption.push(values.length);
        }

        return rowsPerPageOption;
    }

    /**
     * Checa parametro inteiro no parametro: params
     * @param params 
     */
    static checkParamIntUrl(params: any): boolean {
        return !Number.isNaN(parseInt(params));
    }

    /**
     * @param configGridHeader 
     * @param id 
     * @param data_hora_ultima_alteracao 
     * Adiciona os valores: ID e data ultima alteracao no header da grid cabeçalho cruds
     */
    static custumizeHeaderGrid(configGridHeader: any[], id: any, data_hora_ultima_alteracao: any) {
        configGridHeader.forEach(header => {
            if (header.index == 0) {
                header.labelValue = id;
            }
            if (header.index == 1) {
                header.labelValue = data_hora_ultima_alteracao;
            }
        });
    }

    /**
     * Ordenacao pelo campo de data
     * @param value1 
     * @param value2 
     * @param result 
     * @param mask 
     */
    static sortDate(value1: any, value2: any, result: any, mask: string) {
        const date1: Date = moment(value1, mask).toDate();
        const date2: Date = moment(value2, mask).toDate();
        const result1: boolean = (moment(date2, mask).valueOf() > moment(date1, mask).valueOf());
        const result2: boolean = (moment(date2, mask).valueOf() < moment(date1, mask).valueOf());
        result = (result1) ? -1 : (result2) ? 1 : 0;
        return result;
    }

    /**
     * Busca todos os processos gera dossie dos macro processos
     * @param processosLocalStorage 
     */
    static buscarProcessosFilhoMacroProcesso(processosLocalStorage: Array<ProcessoPatriarca>): Array<InterfaceProcesso> {
        return this.inicializarProcesso(processosLocalStorage);
    }

    /**
     * Busca todos os processos fase passando o id
     * @param processosLocalStorage 
     */
    static buscarProcessosFasePorIdProcessoGeraDossie(processosLocalStorage: Array<ProcessoPatriarca>): Array<InterfaceProcesso> {
        return this.inicializarProcesso(processosLocalStorage);
    }

    /**
     * Inicializa os processos via local storage: processo gera dossie ou fase.
     * @param processosLocalStorage 
     */
    static inicializarProcesso(processosLocalStorage: ProcessoPatriarca[]) {
        let processos: InterfaceProcesso[] = new Array<InterfaceProcesso>();
        processosLocalStorage.forEach(pls => {
            processos.push({ key: pls.id, name: pls.nome });
        });
        return processos;
    }

    /**
     * Busca todos as funcoes documentais da tipologia
     * @param funcoesDocumentalLocalStorage 
     */
    static buscarFuncoesDocumentaisTipologia(funcoesDocumentalLocalStorage: Array<FuncaoDocumentalTipologia>): Array<InterfaceFuncaoDocumental> {
        let funcaoDocumentais: InterfaceFuncaoDocumental[] = new Array<InterfaceFuncaoDocumental>();
        funcoesDocumentalLocalStorage.forEach((fdls, index) => {
            funcaoDocumentais.push({ index: index, key: fdls.id, name: fdls.nome, administrativo: undefined, digital: undefined, negocio: undefined, data: undefined });
        });
        return funcaoDocumentais;
    }

    /**
     * Busca todos os tipos de documento da tipologia
     * @param tipoDocumentosLocalStorage 
     */
    static buscarTiposDocumentoTipologia(tipoDocumentosLocalStorage: Array<TipoDocumentoTipologia>): Array<InterfaceTipoDocumento> {
        let tipoDocumentos: InterfaceTipoDocumento[] = new Array<InterfaceTipoDocumento>();
        tipoDocumentosLocalStorage.forEach(tdls => {
            tipoDocumentos.push({ key: tdls.id, name: tdls.nome });
        });
        return tipoDocumentos;
    }

    /**
     * Zera o dropdown tipos documentais
     * @param obj 
     */
    static inicializarTiposDocumentos(obj: any) {
        obj.selectedTipoDocumento = undefined;
    }

    /**
     * Zera o dropdown funcoes documentais
     * @param obj 
     */
    static inicializarFuncoesDocumentais(obj: any) {
        obj.selectedFuncaoDocumental = undefined;
    }

    /**
     * Devolve a primeira data viegente de uma vinculação conflitante
     */
    static inicializarPrimeraDataRevogacaoVinculacaoConflitanteVigente(): Date {
        const year: number = new Date().getFullYear();
        const month: number = new Date().getMonth();
        const day: number = new Date().getDate() + 1;
        const newDate = new Date(year, month, day);
        return newDate;
    }

    /**
     * Varre a ocorrencia de todos os filhos de um objeto principal:
     * Verificando a existencia de atualização para posterior redirecionamento ou alguma logica
     * que garanta que todos objetos foram persistidos
     * @param observables 
     */
    static verificarPreenchimentoObjetosPersistentes(observables: Array<Array<Observable<any>>>): boolean {
        const length: number = observables.length;
        let count: number = 0;
        let flag: boolean = true;
        while (count < length) {
            if (observables[count].length !== 0) {
                flag = false;
                break
            }
            count++;
        }
        return flag;
    }

    /**
     * Retorna os tipos de pessoas: AMBAS, PESSOA_FISICA PESSOA_JURIDICA
     * @param tiposPessoas 
     */
    static getTiposPessoas(): Array<SelectTipoPessoa> {
        let tiposPessoas: SelectTipoPessoa[] = new Array<SelectTipoPessoa>();
        for (var tipoPessoa in TipoPessoa) {
            const code: number = JSON.parse(TipoPessoa[tipoPessoa]).code;
            const name: string = JSON.parse(TipoPessoa[tipoPessoa]).name;
            const value: string = JSON.parse(TipoPessoa[tipoPessoa]).value;
            tiposPessoas.push({ code: code, name: name, value: value });
        }
        return tiposPessoas;
    }

    /**
     * Carrega o tipo pessoa conforme o retorno do serviço
     * @param code 
     * @param name 
     * @param value 
     * @param tipo_pessoa 
     */
    static carregarTipoPessoa(code: number, name: string, value: string, tipo_pessoa: string) {
        const AMBAS: string = JSON.parse(TipoPessoa.AMBAS).value;
        const PF: string = JSON.parse(TipoPessoa.PESSOA_FISICA).value;
        const PJ: string = JSON.parse(TipoPessoa.PESSOA_JURIDICA).value;
        switch (tipo_pessoa) {
            case AMBAS:
                code = JSON.parse(TipoPessoa.AMBAS).code;
                name = JSON.parse(TipoPessoa.AMBAS).name;
                value = JSON.parse(TipoPessoa.AMBAS).value;
                break;
            case PF:
                code = JSON.parse(TipoPessoa.PESSOA_FISICA).code;
                name = JSON.parse(TipoPessoa.PESSOA_FISICA).name;
                value = JSON.parse(TipoPessoa.PESSOA_FISICA).value;
                break;
            case PJ:
                code = JSON.parse(TipoPessoa.PESSOA_JURIDICA).code;
                name = JSON.parse(TipoPessoa.PESSOA_JURIDICA).name;
                value = JSON.parse(TipoPessoa.PESSOA_JURIDICA).value;
                break;
        }
        return { code, name, value };
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoCampo(): SelectItem[] {
        let tipoCampos: SelectItem[] = [];

        for (var tipo in TipoCampo) {
            let label: string = JSON.parse(TipoCampo[tipo]).label;
            let value: string = JSON.parse(TipoCampo[tipo]).value;
            tipoCampos.push({ value: value, label: label });
        }

        return tipoCampos;
    }

    static getTipoCampoOrdenados() {
        let tipoCampos: SelectItem[] = this.carregarTipoCampo();

        let tipoCamposOrdenados: SelectItem[] = tipoCampos.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return tipoCamposOrdenados;
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoAtributoGeral(): SelectItem[] {
        let tiposAtributoGeral: SelectItem[] = [];

        for (var tipo in TipoAtributoGeral) {
            let label: string = JSON.parse(TipoAtributoGeral[tipo]).label;
            let value: string = JSON.parse(TipoAtributoGeral[tipo]).value;
            tiposAtributoGeral.push({ value: value, label: label });
        }

        return tiposAtributoGeral;
    }

    static getTipoAtributoGeralOrdenados() {
        let tipoAtributoGeral: SelectItem[] = this.carregarTipoAtributoGeral();

        let tipoAtributoGeralOrdenados: SelectItem[] = tipoAtributoGeral.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return tipoAtributoGeralOrdenados;
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoCampoComparacaoReceita(): SelectItem[] {
        let tipoCampoComparacaoReceita: SelectItem[] = [];

        for (var tipo in TipoCampoComparacaoReceita) {
            let label: string = JSON.parse(TipoCampoComparacaoReceita[tipo]).label;
            let value: string = JSON.parse(TipoCampoComparacaoReceita[tipo]).value;
            tipoCampoComparacaoReceita.push({ value: value, label: label });
        }

        return tipoCampoComparacaoReceita;
    }

    static getTipoCampoComparacaoReceitaOrdenados() {
        let tipoCampoComparacaoReceita: SelectItem[] = this.carregarTipoCampoComparacaoReceita();

        let tipoCampoComparacaoReceitaOrdenados: SelectItem[] = tipoCampoComparacaoReceita.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return tipoCampoComparacaoReceitaOrdenados;
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoCampoModoComparacaoReceita(): SelectItem[] {
        let tipoCampoModoComparacaoReceita: SelectItem[] = [];

        for (var tipo in TipoCampoModoComparacaoReceita) {
            let label: string = JSON.parse(TipoCampoModoComparacaoReceita[tipo]).label;
            let value: string = JSON.parse(TipoCampoModoComparacaoReceita[tipo]).value;
            tipoCampoModoComparacaoReceita.push({ value: value, label: label });
        }

        return tipoCampoModoComparacaoReceita;
    }

    static getTipoCampoModoComparacaoReceitaOrdenados() {
        let tipoCampModoComparacaoReceita: SelectItem[] = this.carregarTipoCampoModoComparacaoReceita();

        let tipoCampoModoComparacaoReceitaOrdenados: SelectItem[] = tipoCampModoComparacaoReceita.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return tipoCampoModoComparacaoReceitaOrdenados;
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoModoPartilha(): SelectItem[] {
        let tipoModoPartilha: SelectItem[] = [];

        for (var tipo in TipoModoPartilha) {
            let label: string = JSON.parse(TipoModoPartilha[tipo]).label;
            let value: string = JSON.parse(TipoModoPartilha[tipo]).value;
            tipoModoPartilha.push({ value: value, label: label });
        }

        return tipoModoPartilha;
    }

    static getTipoModoPartilhaOrdenados() {
        let tipoModoPartilha: SelectItem[] = this.carregarTipoModoPartilha();

        let carregarTipoModoPartilhaOrdenados: SelectItem[] = tipoModoPartilha.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return carregarTipoModoPartilhaOrdenados;
    }

    // pra funcionar com interface SelectItem deve sempre criar o objeto nessa ordem
    // {value: 'primeiro', label: 'segundo'}
    static carregarTipoEstrategiaPartilha(): SelectItem[] {
        let tipoEstrategiaPartilha: SelectItem[] = [];

        for (var tipo in TipoEstrategiaPartilha) {
            let label: string = JSON.parse(TipoEstrategiaPartilha[tipo]).label;
            let value: string = JSON.parse(TipoEstrategiaPartilha[tipo]).value;
            tipoEstrategiaPartilha.push({ value: value, label: label });
        }

        return tipoEstrategiaPartilha;
    }

    static getTipoEstrategiaPartilhaOrdenados() {
        let tipoEstrategiaPartilha: SelectItem[] = this.carregarTipoEstrategiaPartilha();

        let tipoEstrategiaPartilhaOrdenados: SelectItem[] = tipoEstrategiaPartilha.sort((n1, n2) => {
            if (n1.label > n2.label) {
                return 1;
            }

            if (n1.label < n2.label) {
                return -1;
            }

            return 0;
        });

        return tipoEstrategiaPartilhaOrdenados;
    }

    // se estiver vazio '' retorna vazio, quando estiver vazio significa que o back-end vai alterar a propriedade pra null.
    static isValidarString(valor: string) {
        return valor == null || valor == undefined ? undefined : valor;
    }

    // return tru se valor for vazio, null ou undefined
    static isVazio(valor: string) {
        return valor == null || valor == undefined || valor === '' ? true : false;
    }

    /**
	 * Ordena os atributos pela propriedade ordem apresentação
	 */
    static ordenarAtributosPelaOrdemApresentacao(atributos_extracao: Array<ModalAtributoModel>) {
        atributos_extracao.sort((at1, at2) => (at1.ordem_apresentacao > at2.ordem_apresentacao) ? 1 : ((at2.ordem_apresentacao > at1.ordem_apresentacao) ? -1 : 0));
    }
}