import { Component, OnInit } from "@angular/core";
import { ModalAtributoModel } from 'src/app/cruds/atributo/modal-atributo/model/modal-atributo-model';
import { DialogComponent, DialogService } from "angularx-bootstrap-modal";
import { LoaderService } from "src/app/services";
import { SelectItem, SortEvent } from "primeng/primeng";
import { ModalOpcaoAtributoModel } from "../opcao-grid/model/modal-opcao-atributo-model";
import { Utils } from '../../../util/utils';
import { flattenStyles } from "@angular/platform-browser/src/dom/dom_renderer";
import { GrowlMessageService } from '../../../growl-message-service/growl-message.service';

export interface ModalEntradaDeDados {
    indexGrid: number;
    id_atributo: number;
    atributoSelecionado: ModalAtributoModel;
    atributosGrid: ModalAtributoModel[];
}

export interface ModalSaidaDeDados {
    atributo: ModalAtributoModel;
    isSalvo: boolean;
}

@Component({
    selector: 'modal-atributo.component',
    templateUrl: './modal-atributo.component.html',
    styleUrls: ['./modal-atributo.component.css']
})
export class ModalAtributoComponent extends DialogComponent<ModalEntradaDeDados, ModalSaidaDeDados> implements ModalEntradaDeDados, OnInit {

    constructor(dialogService: DialogService, private loadService: LoaderService, private growlMessageService: GrowlMessageService) {
        super(dialogService);
    }

    REGEX_A_Z_0_9_HIFEN_UNDERLINE = /[^a-zA-Z0-9\-_]/g;

    // incio toolTip
    toolTipNomeNegocial = 'Nome do Atributo identificado pelo negócio, ou seja, o nome do atributo utilizado no dia-a-dia e que pode ser apresentado como algum label no sistema quando necessário.';
    toolTipNomeDocumento = 'Nome do Atributo identificado pela integração com outros sistemas, ou seja, o nome utilizado nas representações JSON/XML encaminhadas nos serviços de integração.';
    toolTipNomeRetorno = 'Nome do Atributo retornado pelo serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.';
    toolTipTipoCampo = 'Tipo de campo que deverá ser utilizado pela interface para recebimento da informação do Atributo quando necessário preenchimento manual.';
    toolTipTipoAtributoGeral = 'Tipo definido para o Atributo nas ações internas do SIMTR como geração de minutas, validação da informação, etc.';
    toolTipOrdem = 'Ordem do Atributo na  exibição do formulário montado a partir da extração de dados do documento.';
    toolTipGrupo = 'Agrupamento que o Atributo faz parte. Em determinadas situações é necessário que ao inforar um determinando atributo, obriga-se que outros campos tenham a informação definida, mesmo que estes sejam de carater opicional, tendo em vista que este grupo compõe um bloco de informações. Ex: Ao informar uma renda declarada, deve ser informado o tipo de renda, valor e ocupação.';
    toolTipValorPadrao = 'Valor padrão opcional para o Atributo a ser utilizado no envio das informação para as integrações quando não informado ou não capturado no documento.';
    toolTipOrientacaoOperador = 'Orientação ao usuário sobre a forma adequada de preencher o campo do Atributo no caso de extração manual dos dados do documento.';
    toolTipAtivo = 'Indica se o Atributo está ativo, ou seja, se poderá ser utilizado como retorno de extração, poderá ser exibido em formulários, etc.';
    toolTipObrigatorio = 'Indica se o Atributo é de captura obrigatória na extração para o Tipo de Documento associado.';
    toolTipIdentificadorPessoa = 'Indica se o Atributo extraído do documento deve ser utilizado como identificador de CPF/CNPJ para associação do documento a um determinado dossiê de cliente.';
    toolTipCalculoDataValidade = 'Indica se o Atributo extraído do documento deve ser utilizado no cálculo da data de validade, baseado nas regras dos campos "Validade Auto Contida" e "Prazo Validade (dias)" do cadastro do Tipo de Documento. Para cada Tipo de Documento só pode haver um Atributo com este campo marcado.';
    toolTipAtributoPresenteNoDocumento = 'Indica se o Atributo pode ser encontrado no documento ou se  trata de um metadado necessário em alguma integração de sistema. Neste último caso, esse dado muitas vezes pode ser obtido através de uma interação com o cliente. Por exemplo, para o Tipo de Documento "Conta de Luz" pode ser necessário um atributo "indicador_correspondencia", ou seja, deverá neste caso ser registrado se o referido endereço é de Correspondência do cliente. Esta informação não existe no documento, devendo ser informada pelo próprio cliente.';
    toolTipCampoComparacao = 'Campo de retorno do SICPF que deve ser utilizado para uma possivel comparação com o Atributo.';
    toolTipModoComparacao = 'Forma de comparação do Atributo com o campo de retorno o SICPF.';
    toolTipNomeAtributoSiecm = 'Nome do atributo do SIECM que tem relação com o Atributo do documento.';
    toolTipTipoAtributoSiecm = 'Tipo definido junto a classe documental no SIECM para o Atributo.';
    toolTipObrigatorioGed = 'Indica se a extração deste Atributo é obrigatória para o armazenamento no SIECM.';
    toolTipNomeObjetoSicli = 'Nome do objeto que agrupa os dados de atualização do Atributo para o SICLI. Este campo deve ser utilizado em conjunto com os campos "Nome Atributo" e "Tipo Atributo", para que o SIMTR possa montar o objeto a ser enviado para o SICLI.';
    toolTipNomeAtributoSicli = 'Nome do atributo utilizado para atualização do SICLI. Este campo deve ser utilizado em conjunto com os campos  "Nome Objeto" e "Tipo Atributo", para que o SIMTR possa montar o objeto a ser enviado para o SICLI.';
    toolTipTipoAtributoSicli = 'Tipo do atributo utilizado para atualização do SICLI. Este campo deve ser utilizado em conjunto com os campos "Nome Objeto" e "Nome Atributo", para que o SIMTR possa montar o objeto a ser enviado para o SICLI.';
    toolTipNomeAtributoSicod = 'Nome do Atributo do SICOD que tem relação com o atributo do documento.';
    toolTipTipoAtributoSicod = 'Tipo definido para o Atributo para envio de atualizações ao SICOD.';
    toolTipModoPartilha = 'Forma de partilha da informação de um atributo indicando qual parte da informação deve ser enviada para outro atributo.';
    toolTipEstrategiaPartilha = 'Indica como a partilha do campo deverá ser feita, quando for o caso. Por exemplo: RECEITA_MAE signfica que a informalção do campo deve ser analisada baseado no nome da mãe obtido junto ao cadastro da receita federal.';
    toolTipAtributoDestinoPartilha = 'Atributo de destino da informação sobresalente após a realização da partilha (split) do dado deste Atributo utilizando-se o Modo e a Estratégia de partilha definidos.';
    // fim toolTip

    // entrada de dados
    indexGrid: number;
    id_atributo: number;
    atributoSelecionado: ModalAtributoModel;
    atributosGrid: ModalAtributoModel[];
    isAcaoEditar: boolean = false;
    limiteOrdem: number = 1;
    limiteGrupo: number = 1;

    // selectItens
    atributosJaPersistidos: SelectItem[] = [];
    tipoCampoSelectItem: SelectItem[] = [];
    tipoAtributoGeralSelectItem: SelectItem[] = [];
    tipoCampoComparacaoReceitaSelectItem: SelectItem[] = [];
    tipoCampoModoComparacaoReceitaSelectItem: SelectItem[] = [];
    tipoModoPartilhaSelectItem: SelectItem[] = [];
    tipoEstrategiasPartilhaSelectItem: SelectItem[] = [];


    // saida de dados
    atributo: ModalAtributoModel;

    // validação de formulário
    isNomeNegocialDuplicado: boolean;
    isNomeDcumentoDuplicado: boolean;
    isNomeAtributoRetornoDuplicado: boolean;
    isPartilhaDisabled: boolean = true;
    isIdentificadorPessoaDuplicado: boolean = false;
    isCalculoDataValidadeDupllicado: boolean = false;

    // validação partilha
    isModoPartilhaDeveSerPreenchido: boolean = false;
    isEstrategiaPartilhaDeveSerPreenchido: boolean = false;

    // validacao SIECM
    isTipoAtributoSiecmDeveSerPreenchido: boolean = false;

    //validação RECEITA
    isCampoComparacaoReceita: boolean = false;

    // validacao SICLI
    isNomeObjetoSicli: boolean = false;
    isTipoAtributoSicli: boolean = false;

    // validacao SICOD
    isTipoAtributoSicod: boolean = false;

    ngOnInit() {
        this.tipoCampoSelectItem = Utils.getTipoCampoOrdenados();
        this.tipoAtributoGeralSelectItem = Utils.getTipoAtributoGeralOrdenados();
        this.tipoCampoComparacaoReceitaSelectItem = Utils.getTipoCampoComparacaoReceitaOrdenados();
        this.tipoCampoModoComparacaoReceitaSelectItem = Utils.getTipoCampoModoComparacaoReceitaOrdenados();
        this.tipoModoPartilhaSelectItem = Utils.getTipoModoPartilhaOrdenados();
        this.tipoEstrategiasPartilhaSelectItem = Utils.getTipoEstrategiaPartilhaOrdenados();

        if (this.atributoSelecionado) {// edição
            this.isAcaoEditar = true;
            // esse objeto atributoSelecionado fica intacto pra se comparado no final se teve alteração no objeto.
            this.atributo = Object.assign({}, this.atributoSelecionado);
            this.descobrirLimiteOrdemApresentacao();

        } else {// novo
            this.descobrirLimiteOrdemApresentacao();
            this.atributo = new ModalAtributoModel();
            this.atributo.ordem_apresentacao = this.limiteOrdem;
            this.atributo.ativo = true;
            this.atributo.indicador_obrigatorio = false;
            this.atributo.indicador_presente_documento = false;
            this.atributo.indicador_calculo_data_validade
            this.atributo.indicador_identificador_pessoa = false;

            this.atributo.opcoes_atributo = [];
        }
        this.preencherListaAtributosJaPersistidos();
    }

    preencherListaAtributosJaPersistidos() {
        if (this.atributosGrid && this.atributosGrid.length > 0) {
            this.atributosGrid.forEach(atr => {
                if (atr.id) {
                    this.atributosJaPersistidos.push(Object.assign({}, { value: atr.id, label: atr.nome_atributo_negocial }));
                }
            });
            this.isPartilhaDisabled = false;
        } else {
            this.isPartilhaDisabled = true;
        }
    }

    validarEntradaKeyup(event) {
        let valorSemCaracteresEspeciais = event.target.value.replace(this.REGEX_A_Z_0_9_HIFEN_UNDERLINE, '');
        event.target.value = valorSemCaracteresEspeciais;
    }

    descobrirLimiteOrdemApresentacao() {
        if (this.isAcaoEditar) {
            this.limiteOrdem = this.atributosGrid && this.atributosGrid.length > 0 ? this.atributosGrid.length : 1;
        } else {
            this.limiteOrdem = this.atributosGrid && this.atributosGrid.length > 0 ? this.atributosGrid.length + 1 : 1;
        }
    }

    validarFormulario(campo: any) {
        return (campo.dirty && campo.errors && campo.errors.required);
    }

    validarNomeNegocialDuplicado() {
        let nomeDuplicado: boolean = false;

        if (this.atributo.nome_atributo_negocial && this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (this.atributo.nome_atributo_negocial.toUpperCase().trim() == atr.nome_atributo_negocial.toUpperCase().trim() && this.indexGrid != atr.indexGrid) {
                    nomeDuplicado = true;
                    break;
                }
            }
        }
        this.isNomeNegocialDuplicado = nomeDuplicado;
    }

    validarNomeDocumentolDuplicado() {
        let nomeDuplicado: boolean = false;

        if (this.atributo.nome_atributo_documento && this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (this.atributo.nome_atributo_documento.toUpperCase().trim() == atr.nome_atributo_documento.toUpperCase().trim() && this.indexGrid != atr.indexGrid) {
                    nomeDuplicado = true;
                    break;
                }
            }
        }
        this.isNomeDcumentoDuplicado = nomeDuplicado;

        if (this.atributo.nome_atributo_documento) {
            let valorSemCaracteresEspeciais = this.atributo.nome_atributo_documento.replace(this.REGEX_A_Z_0_9_HIFEN_UNDERLINE, '');
            this.atributo.nome_atributo_documento = valorSemCaracteresEspeciais;
        }
    }

    validarNomeAtributoRetornolDuplicado() {
        let nomeDuplicado: boolean = false;

        if (this.atributo.nome_atributo_retorno && this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (atr.nome_atributo_retorno && this.atributo.nome_atributo_retorno.toUpperCase().trim() == atr.nome_atributo_retorno.toUpperCase().trim() && this.indexGrid != atr.indexGrid) {
                    nomeDuplicado = true;
                    break;
                }
            }
        }
        this.isNomeAtributoRetornoDuplicado = nomeDuplicado;

        let valorSemCaracteresEspeciais = this.atributo.nome_atributo_retorno.replace(this.REGEX_A_Z_0_9_HIFEN_UNDERLINE, '');
        this.atributo.nome_atributo_retorno = valorSemCaracteresEspeciais;
    }

    validarIdentificadorPessoaDuplicado() {
        let identificadorPessoaJaExiste: boolean = false;

        if (this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (this.atributo.indicador_identificador_pessoa && atr.indicador_identificador_pessoa && this.indexGrid != atr.indexGrid) {
                    identificadorPessoaJaExiste = true;
                    break;
                }
            }
        }
        this.isIdentificadorPessoaDuplicado = identificadorPessoaJaExiste;
    }

    validarCalculoDadaValidadeDuplicado() {
        let calculoDataValidadeJaExiste: boolean = false;

        if (this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (this.atributo.indicador_calculo_data_validade && atr.indicador_calculo_data_validade && this.indexGrid != atr.indexGrid) {
                    calculoDataValidadeJaExiste = true;
                    break;
                }
            }
        }
        this.isCalculoDataValidadeDupllicado = calculoDataValidadeJaExiste;
    }

    salvar() {
        this.loadService.show();
        this.verificarSeAtributoFoiAlterado();
        this.result = { atributo: this.atributo, isSalvo: true };
        this.close();
    }

    validarPartilha() {
        if (this.atributo.identificador_atributo_partilha !== null) {
            this.isModoPartilhaDeveSerPreenchido = !this.atributo.indicador_modo_partilha ? true : false;
            this.isEstrategiaPartilhaDeveSerPreenchido = !this.atributo.indicador_estrategia_partilha ? true : false;
            this.validarAtributoDestinoJaFoiSelecionado();
        } else {
            this.isModoPartilhaDeveSerPreenchido = false;
            this.isEstrategiaPartilhaDeveSerPreenchido = false;
        }
    }

    validarSiecm() {
        if (this.atributo.nome_atributo_siecm) {
            this.isTipoAtributoSiecmDeveSerPreenchido = !this.atributo.tipo_atributo_siecm ? true : false;
        } else {
            this.isTipoAtributoSiecmDeveSerPreenchido = false;
        }
    }

    validarCampoComparacaoReceita() {
        if (this.atributo.indicador_campo_comparacao_receita) {
            this.isCampoComparacaoReceita = !this.atributo.indicador_modo_comparacao_receita ? true : false;
        } else {
            this.isCampoComparacaoReceita = false;
        }
    }

    validarNomeAtributoSicli() {
        if (this.atributo.nome_atributo_sicli && this.atributo.nome_atributo_sicli !== '') {

            this.isNomeObjetoSicli = Utils.isVazio(this.atributo.nome_objeto_sicli);
            this.isTipoAtributoSicli = Utils.isVazio(this.atributo.tipo_atributo_sicli);
        } else {
            this.isNomeObjetoSicli = false;
            this.isTipoAtributoSicli = false;
        }
    }

    validarSicod() {
        if (this.atributo.nome_sicod && this.atributo.nome_sicod !== '') {
            this.isTipoAtributoSicod = Utils.isVazio(this.atributo.tipo_atributo_sicod);
        } else {
            this.isTipoAtributoSicod = false;
        }
    }

    validarAtributoDestinoJaFoiSelecionado() {
        let atributoDestinoJaFoiSelecionado: string;

        if (this.atributo.identificador_atributo_partilha !== null && this.atributosGrid && this.atributosGrid.length > 0) {

            for (let atr of this.atributosGrid) {
                if (this.atributo.identificador_atributo_partilha === atr.identificador_atributo_partilha && this.indexGrid != atr.indexGrid) {
                    atributoDestinoJaFoiSelecionado = atr.id ? atr.id + ' - ' + atr.nome_atributo_negocial : atr.nome_atributo_negocial;
                    break;
                }
            }
        }

        if (atributoDestinoJaFoiSelecionado) {
            let msg: string = 'Atributo Extração indicado já esta sendo partilhado por outro atribbuto. Atributo indicado: ';
            this.growlMessageService.showError("Validação!", msg + atributoDestinoJaFoiSelecionado);
        }
    }

    fechar() {
        this.loadService.show();
        this.result = { atributo: this.atributoSelecionado, isSalvo: false };
        this.close();
    }

    handleOpcoesAtributosChanged(input: ModalOpcaoAtributoModel[]) {
        this.atributo.opcoes_atributo = input;
    }

    handleOpcoesExcluidasAtributosChanged(input: ModalOpcaoAtributoModel[]) {
        this.atributo.opcoesExcluidas = input;
    }

    handleExpressaoInterfaceChanged(expressaoCondicioanl: string) {
        this.atributo.expressao_interface = expressaoCondicioanl;
    }

    verificarSeAtributoFoiAlterado() {
        let alterou: boolean = false;

        this.atributo.indicador_estrategia_partilha = this.atributo.indicador_estrategia_partilha == '' ? null : this.atributo.indicador_estrategia_partilha;
        this.atributo.indicador_modo_partilha = this.atributo.indicador_modo_partilha == '' ? null : this.atributo.indicador_modo_partilha;
        this.atributo.tipo_atributo_sicli = this.atributo.tipo_atributo_sicli == '' ? null : this.atributo.tipo_atributo_sicli;
        this.atributo.tipo_atributo_sicod = this.atributo.tipo_atributo_sicod == '' ? null : this.atributo.tipo_atributo_sicod;
        this.atributo.tipo_atributo_siecm = this.atributo.tipo_atributo_siecm == '' ? null : this.atributo.tipo_atributo_siecm;

        if (this.atributo.id) {
            if (this.atributo.nome_atributo_negocial !== this.atributoSelecionado.nome_atributo_negocial) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_atributo_negocial = this.atributo.nome_atributo_negocial;
            }

            if (this.atributo.nome_atributo_documento !== this.atributoSelecionado.nome_atributo_documento) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_atributo_documento = this.atributo.nome_atributo_documento;
            }

            if (this.atributo.nome_atributo_retorno !== this.atributoSelecionado.nome_atributo_retorno) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_atributo_retorno = this.atributo.nome_atributo_retorno;
            }

            if (this.atributo.tipo_campo !== this.atributoSelecionado.tipo_campo) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.tipo_campo = this.atributo.tipo_campo;
            }

            if (this.atributo.tipo_atributo_geral !== this.atributoSelecionado.tipo_atributo_geral) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.tipo_atributo_geral = this.atributo.tipo_atributo_geral;
            }

            if (this.atributo.ordem_apresentacao !== this.atributoSelecionado.ordem_apresentacao) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.ordem_apresentacao = this.atributo.ordem_apresentacao;
            }

            if (this.atributo.grupo_atributo !== this.atributoSelecionado.grupo_atributo) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.grupo_atributo = this.atributo.grupo_atributo;
            }

            if (this.atributo.valor_padrao !== this.atributoSelecionado.valor_padrao) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.valor_padrao = this.atributo.valor_padrao;
            }

            if (this.atributo.orientacao_preenchimento !== this.atributoSelecionado.orientacao_preenchimento) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.orientacao_preenchimento = this.atributo.orientacao_preenchimento;
            }

            if (this.atributo.ativo !== this.atributoSelecionado.ativo) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.ativo = this.atributo.ativo;
            }

            if (this.atributo.indicador_obrigatorio !== this.atributoSelecionado.indicador_obrigatorio) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_obrigatorio = this.atributo.indicador_obrigatorio;
            }

            if (this.atributo.indicador_identificador_pessoa !== this.atributoSelecionado.indicador_identificador_pessoa) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_identificador_pessoa = this.atributo.indicador_identificador_pessoa;
            }

            if (this.atributo.indicador_calculo_data_validade !== this.atributoSelecionado.indicador_calculo_data_validade) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_calculo_data_validade = this.atributo.indicador_calculo_data_validade;
            }

            if (this.atributo.indicador_presente_documento !== this.atributoSelecionado.indicador_presente_documento) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_presente_documento = this.atributo.indicador_presente_documento;
            }

            if (this.atributo.indicador_campo_comparacao_receita !== this.atributoSelecionado.indicador_campo_comparacao_receita) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_campo_comparacao_receita = this.atributo.indicador_campo_comparacao_receita;
            }

            if (this.atributo.indicador_modo_comparacao_receita !== this.atributoSelecionado.indicador_modo_comparacao_receita) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_modo_comparacao_receita = this.atributo.indicador_modo_comparacao_receita;
            }

            if (this.atributo.nome_atributo_siecm !== this.atributoSelecionado.nome_atributo_siecm) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_atributo_siecm = this.atributo.nome_atributo_siecm;
            }

            if (this.atributo.tipo_atributo_siecm !== this.atributoSelecionado.tipo_atributo_siecm) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.tipo_atributo_siecm = this.atributo.tipo_atributo_siecm;
            }

            if (this.atributo.indicador_obrigatorio_siecm !== this.atributoSelecionado.indicador_obrigatorio_siecm) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_obrigatorio_siecm = this.atributo.indicador_obrigatorio_siecm;
            }

            if (this.atributo.nome_objeto_sicli !== this.atributoSelecionado.nome_objeto_sicli) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_objeto_sicli = this.atributo.nome_objeto_sicli;
            }

            if (this.atributo.nome_atributo_sicli !== this.atributoSelecionado.nome_atributo_sicli) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_atributo_sicli = this.atributo.nome_atributo_sicli;
            }

            if (this.atributo.tipo_atributo_sicli !== this.atributoSelecionado.tipo_atributo_sicli) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.tipo_atributo_sicli = this.atributo.tipo_atributo_sicli;
            }

            if (this.atributo.nome_sicod !== this.atributoSelecionado.nome_sicod) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.nome_sicod = this.atributo.nome_sicod;
            }

            if (this.atributo.tipo_atributo_sicod !== this.atributoSelecionado.tipo_atributo_sicod) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.tipo_atributo_sicod = this.atributo.tipo_atributo_sicod;
            }

            if (this.atributo.indicador_modo_partilha !== this.atributoSelecionado.indicador_modo_partilha) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_modo_partilha = this.atributo.indicador_modo_partilha;
            }

            if (this.atributo.indicador_estrategia_partilha !== this.atributoSelecionado.indicador_estrategia_partilha) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.indicador_estrategia_partilha = this.atributo.indicador_estrategia_partilha;
            }

            if (this.atributo.identificador_atributo_partilha !== this.atributoSelecionado.identificador_atributo_partilha) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                if (this.atributo.identificador_atributo_partilha === null) {
                    this.atributo.identificador_atributo_partilha = -1;
                    this.atributo.objetoAlterado.identificador_atributo_partilha = this.atributo.identificador_atributo_partilha;
                } else {
                    this.atributo.objetoAlterado.identificador_atributo_partilha = this.atributo.identificador_atributo_partilha;
                }
            }

            if (this.atributo.expressao_interface !== this.atributoSelecionado.expressao_interface) {
                this.verificarSeObjetoAlteradoFoiCriado();
                alterou = true;
                this.atributo.objetoAlterado.expressao_interface = this.atributo.expressao_interface;
            }

            if (this.atributo.opcoes_atributo && this.atributo.opcoes_atributo.length > 0) {

                if (this.atributo.opcoesExcluidas && this.atributo.opcoesExcluidas.length > 0) {
                    alterou = true;

                } else {
                    for (let opcao of this.atributo.opcoes_atributo) {
                        if (!opcao.id) {
                            alterou = true;
                            break;
                        }
                    }
                }
            }
        }

        this.atributo.alterado = alterou;
    }

    verificarSeObjetoAlteradoFoiCriado() {
        if (!this.atributo.objetoAlterado) {
            this.atributo.objetoAlterado = new ModalAtributoModel();
        }
    }

    // isNullOuVazio(valor: string): boolean{
    //    return  valor == undefined || valor == null ? true : valor.trim() === '';
    // }
}