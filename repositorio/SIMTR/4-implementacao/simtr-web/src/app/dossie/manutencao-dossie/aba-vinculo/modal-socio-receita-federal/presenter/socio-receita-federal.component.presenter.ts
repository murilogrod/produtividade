import { Injectable } from "@angular/core";
import { SocioReceitaFederal } from "../model/socio-receita-federal.model";
import { Utils } from "src/app/utils/Utils";
import { Socio } from "../../model/socio.model";
import { DataInputReceitaFederal } from "../../model/data-input-receita-federal";
import { MODAL_SOCIOS_RECEITA_FEDERAL } from "src/app/constants/constants";
import { ModalSocioReceitaFederalComponent } from "../view/modal-socio-receita-federal.component";
import { SocioReceitaFederalOutput } from "../model/socio-receita-federal-output";
import { SortEvent } from "primeng/primeng";
import * as moment from 'moment';


@Injectable()
export class SocioReceitaFederalPresenter {

    socioReceitaFederal: SocioReceitaFederal;
    constructor() { }

    /**
     * Carrega as configurações essenciais para a modal socios receita federal: tabela, tipo relacionamento e categorização de pessoa
     * @param dataInputReceitaFederal 
     */
    initConfigModalSociosReceitaFederal(dataInputReceitaFederal: DataInputReceitaFederal) {
        this.carregarCnpjPrincipal(dataInputReceitaFederal);
        this.inicializaHabilitaAlteracaoVinculoPessoa(dataInputReceitaFederal);
        this.inicializaListaClientes(dataInputReceitaFederal);
        this.inicializarSociosReceitaFederal(dataInputReceitaFederal);
        this.inicializarTipoRelacionamento(dataInputReceitaFederal);
        this.identificarImpedimentoSociosConformeTipoPessoa();
    }

    /**
     * Carrega o cnpj principal e tipo de relacionamento para posterior carregamento de vinculo cliente
     * @param dataInputReceitaFederal 
     */
    carregarCnpjPrincipal(dataInputReceitaFederal: DataInputReceitaFederal) {
        this.socioReceitaFederal.cnpj = dataInputReceitaFederal.cnpj;
        this.socioReceitaFederal.relacionamento = dataInputReceitaFederal.relacionamento;
    }
    /**
     * Recebe o booleano contendo a verificação de inserção de um novo vinculo pessoa; 
     * caso falso; impossibilita a inserção de sócio
     * @param dataInputReceitaFederal 
     */
    private inicializaHabilitaAlteracaoVinculoPessoa(dataInputReceitaFederal: DataInputReceitaFederal) {
        this.socioReceitaFederal.habilitaAlteracao = dataInputReceitaFederal.habilitaAlteracao;
    }


    /**
     * Recebe a lista de vinculos clientes e carrega no model da modal socios receita federal
     * @param dataInputReceitaFederal 
     */
    private inicializaListaClientes(dataInputReceitaFederal: DataInputReceitaFederal) {
        this.socioReceitaFederal.clienteLista = dataInputReceitaFederal.clienteLista;
    }

    /**
     * Recebe a lista de socios do serviço da receita federal e inicializa na modal
     * @param dataInputReceitaFederal 
     */
    private inicializarSociosReceitaFederal(dataInputReceitaFederal: DataInputReceitaFederal) {
        this.socioReceitaFederal.sociosReceitaFederal = dataInputReceitaFederal.sociosReceitaFederal;
        //this.mockPJ();
    }

    //Utilização para mockar PJ
    public mockPJ() {
        this.socioReceitaFederal.sociosReceitaFederal.forEach((socio, index) => {
            if (index == 0) {
                socio.cpf_cnpj = '02426390000191';
            }
            if (index == 1) {
                socio.cpf_cnpj = '29655214000188';
            }
            if (index == 2) {
                socio.cpf_cnpj = '73294378000119';
            }
        });
    }

    /**
     * Filtra os tipos de relacionamentos considerando socio fisico e juridico. usado para validar a adição do sócio
     * @param dataInputReceitaFederal 
     */
    private inicializarTipoRelacionamento(dataInputReceitaFederal: DataInputReceitaFederal) {
        dataInputReceitaFederal.listaTipoRelacionamento.forEach(tipo => {
            this.socioReceitaFederal.listaTipoRelacionamento.push(Object.assign({}, tipo));
        });
        this.socioReceitaFederal.listaTipoRelacionamento = this.socioReceitaFederal.listaTipoRelacionamento.filter(this.verificaCamposChaveTipoRelacionamento());
    }

    /**
     * Faz a verificação das chaves: SOCIOFISICO e SOCIOJURIDICO
     */
    private verificaCamposChaveTipoRelacionamento(): (value: any, index: number, array: any[]) => any {
        return tipo => this.verificarOcorrenciaSocioFisico(tipo) || this.verificarOcorrenciaSocioJuridico(tipo);
    }

    /**
     * Verificação tipo relacionamento para pessoa fisica ou ambas
     * @param tipo 
     */
    private verificarOcorrenciaSocioFisico(tipo: any): boolean {
        return tipo.indica_receita_pf && (tipo.tipo_pessoa == 'F' || tipo.tipo_pessoa == 'A');
    }

    /**
     * Verificação tipo relacionamento para pessoa juridica ou ambas e considera-se o o 
     * relacionamento secundário (principal = false)
     * @param tipo 
     */
    private verificarOcorrenciaSocioJuridico(tipo: any): boolean {
        return tipo.indica_receita_pj && !tipo.principal && (tipo.tipo_pessoa == 'J' || tipo.tipo_pessoa == 'A');
    }

    /**
     * Coloca a mascara de acordo o tipo de pessoa: PF ou PJ
     * @param value 
     */
    formatValue(value: string) {
        const cpf: string = value.substring(3);
        let formattedValue: string;
        if (this.checkInitialDigits(value)) {
            formattedValue = Utils.maskCnpj(value);
        } else if (Utils.verificarCPF(cpf)) {
            formattedValue = Utils.maskCpf(cpf);
        } else if (Utils.verificarCNPJ(value)) {
            formattedValue = Utils.maskCnpj(value);
        } else {
            formattedValue = Utils.maskCpf(cpf);
        }
        return formattedValue;
    }

    /**
     * Atualiza a lista de socios aptos para adição
     * @param sociosChecked 
     */
    atualizarSociosConformeErro(sociosChecked: any) {
        if (sociosChecked.checked) {
            this.socioReceitaFederal.selectedSociosReceitaFederal = this.socioReceitaFederal.selectedSociosReceitaFederal.filter(socio => !socio.isError);
        }
    }

    /**
     * Devolve a lista de sócio selecionados para o componente aba-vinculo
     * @param modalSocioReceitaFederalComponent 
     */
    selectSocios(modalSocioReceitaFederalComponent: ModalSocioReceitaFederalComponent) {
        modalSocioReceitaFederalComponent.dialogReturn =
            <SocioReceitaFederalOutput>{
                cnpj: this.socioReceitaFederal.cnpj,
                selectedSociosReceitaFederal: this.socioReceitaFederal.selectedSociosReceitaFederal,
                listaTipoRelacionamento: this.socioReceitaFederal.listaTipoRelacionamento
            };
        modalSocioReceitaFederalComponent.closeDialog();
    }

    /**
     * Ordencao dos campos
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
            } else if (event.field.indexOf("data_inicio") == 0) {
                result = this.sortDataHoraInicio(value1, value2, result);
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Filtra os sócios conforme campo de pesquisa;
     * com pelo menos 3 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataProdutos 
     */
    filterSocios(input: any, dataSocios: any) {
        if (input.length == 0) {
            dataSocios.filterGlobal(input, 'contains');
        }
        if (input.length > 2) {
            dataSocios.filterGlobal(input, 'contains');
        }
    }

    /**
     * Ordenacao pelo campo data hora início
     * @param value1 
     * @param value2 
     * @param result 
     */
    private sortDataHoraInicio(value1: any, value2: any, result: any) {
        const date1: Date = moment(value1, 'DD.MM.YYYY').toDate();
        const date2: Date = moment(value2, 'DD.MM.YYYY').toDate();
        const result1: boolean = (moment(date2, 'DD.MM.YYYY').valueOf() > moment(date1, 'DD.MM.YYYY').valueOf());
        const result2: boolean = (moment(date2, 'DD.MM.YYYY').valueOf() < moment(date1, 'DD.MM.YYYY').valueOf());
        result = (result1) ? -1 : (result2) ? 1 : 0;
        return result;
    }

    /**
     * Categoriza os socios conforme o impedimento do tipo relacionamento, conforme tipo de pessoa: PJ ou PF
     */
    private identificarImpedimentoSociosConformeTipoPessoa() {
        let count: number = 1;
        this.socioReceitaFederal.sociosReceitaFederal.forEach(socio => {
            const cpf: string = socio.cpf_cnpj.substring(3);
            const cnpj: string = socio.cpf_cnpj;
            if (this.checkInitialDigits(cnpj)) {
                socio.tipo_pessoa = true;
                this.verificaErroPorTipoPessoaImpedimentoVinculo(socio, cnpj);
            } else if (Utils.verificarCPF(cpf)) {
                socio.tipo_pessoa = false;
                this.verificaErroPorTipoPessoaImpedimentoVinculo(socio, cpf);
            } else if (Utils.verificarCNPJ(cnpj)) {
                socio.tipo_pessoa = true;
                this.verificaErroPorTipoPessoaImpedimentoVinculo(socio, cnpj);
            } else {
                socio.tipo_pessoa = false;
                this.verificaErroPorTipoPessoaImpedimentoVinculo(socio, cpf);
            }
            this.inicializarCamposChaveSocio(socio);
            count++;
        });
        this.desabilitaBotaoAdicaoSocioConformeImpedimentos(count);
    }

    /**
     * Inicializa os relacionamentos existentes por vínculo cliente
     * @param socio 
     */
    private inicializarRelacionamentosSocios(socio: Socio) {
        let vinculosSocios: any[] = this.socioReceitaFederal.clienteLista.filter(cliente => this.retirarMascaraCNPJ(cliente) !== this.socioReceitaFederal.cnpj);
        let clientes: any[] = new Array<any>();
        if (socio.tipo_pessoa) {
            clientes = vinculosSocios.filter(vinculo => vinculo.cnpj && vinculo.cnpj == socio.cpf_cnpj);
        } else {
            clientes = vinculosSocios.filter(vinculo => vinculo.cpf && vinculo.cpf == socio.cpf_cnpj.substring(3));
        }
        socio.relacionamentosPermitidos = this.inicializarRelacionamentos(clientes);
    }

    /**
     * Retira a mascara CNPJ
     * @param cliente 
     */
    private retirarMascaraCNPJ(cliente: any): string {
        return cliente.cnpj ? Utils.retiraMascaraCPF(cliente.cnpj) : "";
    }

    /**
     * Retorna os relacionamentos para cada sócio
     * @param clientes 
     */
    private inicializarRelacionamentos(clientes: Array<any>): Array<string> {
        let relacionamentos: string[] = new Array<string>();
        clientes.forEach(cliente => {
            relacionamentos.push(cliente.tipo_relacionamento.nome);
        });
        return relacionamentos;
    }

    /**
     * Verifica a existencia de parametrização conforme tipo de relacionamento
     * @param socio 
     */
    private verificaExistenciaParametrizacao(socio: Socio) {
        if (!socio.isError) {
            socio.isError = !this.socioReceitaFederal.listaTipoRelacionamento.some(tipo => this.filtrarRelacionamentosPermitidos(socio, tipo));
        }
    }

    /**
     * Filtrar os tipos de relacionamentos para validar o impedimento
     * @param socio 
     * @param tipo 
     */
    private filtrarRelacionamentosPermitidos(socio: Socio, tipo: any): boolean {
        const tipoFisico: boolean = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_FISICO.indexOf(socio.type_socio) == 0 && tipo.indica_receita_pf && (tipo.tipo_pessoa == 'F' || tipo.tipo_pessoa == 'A');
        const tipoJuridico: boolean = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_JURIDICO.indexOf(socio.type_socio) == 0 && tipo.indica_receita_pj && (tipo.tipo_pessoa == 'J' || tipo.tipo_pessoa == 'A');
        return tipoFisico || tipoJuridico;
    }

    /**
     * Inicializa o campo chave por sócio: utilização das labels: SOCIOPESSOAJURIDICA e SOCIOPESSOAFISICA
     * @param socio 
     */
    private inicializarCamposChaveSocio(socio: Socio) {
        if (socio.tipo_pessoa) {
            socio.type_socio = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_JURIDICO;
        } else {
            socio.type_socio = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_FISICO;
        }
        this.verificaExistenciaParametrizacao(socio);
    }

    /**
     * Desabilita botão de adição de sócios conforme impedimentos
     * @param count 
     */
    private desabilitaBotaoAdicaoSocioConformeImpedimentos(count: number) {
        if (!this.socioReceitaFederal.habilitaAlteracao || this.socioReceitaFederal.selectedSociosReceitaFederal && count == this.socioReceitaFederal.selectedSociosReceitaFederal.length) {
            this.socioReceitaFederal.adicaoSocio = true;
        }
    }

    /**
     * Verifica a ocorrencia de impedimento de adição de sócio conforme o tipo de relacionamento 
     * e impedimento de adição de vínculo pessoa
     * @param socio 
     * @param cnpj_cpf 
     */
    private verificaErroPorTipoPessoaImpedimentoVinculo(socio: Socio, cnpj_cpf: string) {
        this.inicializarRelacionamentosSocios(socio);
        if (!this.verificaExistenciaVinculoCliente(cnpj_cpf, socio)) {
            socio.msgError = socio.tipo_pessoa ? MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_JURIDICO_TIPO_RELACIONAMENTO : MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_FISICO_TIPO_RELACIONAMENTO;
        }
        if (!this.socioReceitaFederal.habilitaAlteracao) {
            socio.isError = true;
            socio.msgError = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_DOSSIE_CRIADO;
        }
    }

    /**
     * Verifica existencia se o vinculo cliente ja foi adicionado na grid pessoas
     * @param cnpj_cpf 
     * @param type 
     */
    private verificaExistenciaCpfCnpjVinculosCliente(cnpj_cpf: string, type: boolean): boolean {
        if (type) {
            return this.socioReceitaFederal.clienteLista.some(cliente => cliente.cnpj && cliente.cnpj.indexOf(cnpj_cpf) == 0);
        } else {
            return this.socioReceitaFederal.clienteLista.some(cliente => cliente.cpf && cliente.cpf.indexOf(cnpj_cpf) == 0);
        }
    }

    /**
     * Verifica a existencia de vinculo cliente e seu tipo de relacionamento
     * @param cnpj_cpf 
     * @param socio 
     */
    private verificaExistenciaVinculoCliente(cnpj_cpf: string, socio: Socio): boolean {
        let socio_juridico_existente: boolean = this.verificaExistenciaCpfCnpjVinculosCliente(cnpj_cpf, socio.tipo_pessoa);
        let socio_fisico_existente: boolean = this.verificaExistenciaCpfCnpjVinculosCliente(cnpj_cpf, socio.tipo_pessoa);
        let socio_tipo_relacionamento_disponivel = this.verificaTipoRelacionamentoDisponivel(socio);
        if (socio_juridico_existente && !socio_tipo_relacionamento_disponivel) {
            socio.isError = true;
            socio.msgError = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_CLIENTE_EXISTENTE;
        }
        if (socio_fisico_existente && !socio_tipo_relacionamento_disponivel) {
            socio.isError = true;
            socio.msgError = MODAL_SOCIOS_RECEITA_FEDERAL.SOCIO_CLIENTE_EXISTENTE;
        }
        return socio_juridico_existente || socio_fisico_existente;
    }

    /**
     * Verifica se existe relacionamento disponivel por sócio
     * @param socio 
     */
    private verificaTipoRelacionamentoDisponivel(socio: Socio): boolean {
        let relacionamentoDisponivel: boolean = false;
        let relacionamentosPermitidos: any[];
        if (socio.tipo_pessoa) {
            relacionamentosPermitidos = this.socioReceitaFederal.listaTipoRelacionamento.filter(relacionamento => (relacionamento.indica_receita_pj && (relacionamento.tipo_pessoa == 'J' || relacionamento.tipo_pessoa == 'A')));
        } else {
            relacionamentosPermitidos = this.socioReceitaFederal.listaTipoRelacionamento.filter(relacionamento => (relacionamento.indica_receita_pf && (relacionamento.tipo_pessoa == 'F' || relacionamento.tipo_pessoa == 'A')));
        }
        if (this.verificarRelacionamento(socio, relacionamentosPermitidos)) {
            relacionamentoDisponivel = true;
        }

        return relacionamentoDisponivel;
    }

    /**
     * Retorna o resultado da diferença de arrays contendo os relacionamentos de um sócio; 
     * havendo diferença; o socio ainda tem relacionamento disponivel
     * @param socio 
     * @param relacionamentosPermitidos 
     */
    private verificarRelacionamento(socio: Socio, relacionamentosPermitidos: any[]): boolean {
        let relacionamentoCompativel: string[] = new Array<string>();
        relacionamentosPermitidos.forEach(relacionamento => {
            relacionamentoCompativel.push(relacionamento.nome);
        });
        let diff: string[] = relacionamentoCompativel.filter(relacionamento => !socio.relacionamentosPermitidos.includes(relacionamento));
        if (diff.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica a existencia de números nas 3 primeiras posições
     * @param value 
     */
    private checkInitialDigits(value: string): boolean {
        return value.substring(0, 3).indexOf("000") == -1;
    }

}