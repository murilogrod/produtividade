import { Injectable } from "@angular/core";
import { DialogService, DialogOptions } from "angularx-bootstrap-modal";
import { ConsultaClienteService } from "src/app/cliente/consulta-cliente/service/consulta-cliente-service";
import { ModalSicliComponent } from "src/app/cliente/consulta-cliente/aba-identificacao/modal/modal-sicli/modal-sicli.component";
import { MSG_ABA_VINCULO, MSG_MODAL_PESSOA } from "src/app/constants/constants";
import { VinculoCliente, VinculoGarantia, VinculoProduto } from "src/app/model";
import { VinculoArvore } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { VinculoArvoreCliente } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { VinculoArvoreGarantia } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { VinculoArvoreProduto } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { LoaderService } from "src/app/services";
import { MudancaSalvaService } from "src/app/services/mudanca-salva.service";
import { Utils } from "src/app/utils/Utils";
import { AbaVinculo } from "../model/aba-vinculo.model";
import { IdentificadorDossieFase } from "../../aba-formulario/modal/modal-pessoa/model/identificadorDossieEFase";
import { LoadingAbaVinculo } from "../model/loading-aba-vinculo.model";
import { TipoVinculo } from "src/app/documento/enum-tipo-vinculo/tipo-vinculo.enum";
import { ModalSocioReceitaFederalComponent } from "../modal-socio-receita-federal/view/modal-socio-receita-federal.component";
import { DossieService } from "src/app/dossie/dossie-service";
import { Socio } from "../model/socio.model";
import { AbaVinculoComponent } from "../view/aba-vinculo.component";
import { DataInputReceitaFederal } from "../model/data-input-receita-federal";
import { SocioReceitaFederalOutput } from "../modal-socio-receita-federal/model/socio-receita-federal-output";
import { PorteEmpresa } from "src/app/cliente/consulta-cliente/enum-porte/porte-empresa.enum";
import { DocumentoArvore } from "src/app/model/documento-arvore";
import { TipoRelacionamento } from "src/app/model/tipo-relacionamento";

@Injectable()
export class AbaVinculoComponentPresenter {

    abaVinculo: AbaVinculo;
    vinculoProduto: VinculoProduto = new VinculoProduto();
    garantia: VinculoGarantia = new VinculoGarantia();
    loadingAbaVinculo: LoadingAbaVinculo;

    constructor(private dialogService: DialogService,
        private mudancaSalvaService: MudancaSalvaService,
        private clienteService: ConsultaClienteService,
        private loadService: LoaderService,
        private dossieService: DossieService) {
    }

    /**
     * Aguarda o carregamento do vinculo cliente
     * (caso houver) para iniciar e finalizar o loading.
     * @param clienteLista 
     */
    aguardarCarregamentoVinculosCliente(clienteLista: any[], params: any) {
        if (clienteLista.length == 0 && params.id.indexOf("dossieProduto") !== 0) {
            this.loadService.show();
        } else {
            this.loadService.hide();
        }
    }

    /**
     * Recupera os novos vinculos: clientes, produtos e garantias.
     * apenas para vinculos não persistidos: em memória
     * utilização ao trocar etapa no breadcumb
     * @param identificadorDossieFase 
     * @param changeProduto 
     * @param changeGarantia 
     */
    carregarVinculosClientesProdutosGarantias(identificadorDossieFase: IdentificadorDossieFase, changeProduto: boolean, changeGarantia: boolean) {
        if (this.abaVinculo.novosVinculos && this.abaVinculo.clienteLista && this.abaVinculo.clienteLista.length > 0) {
            this.abaVinculo.novosVinculos.forEach(novoVinculo => {
                if (!this.abaVinculo.clienteLista.includes(novoVinculo)) {
                    this.montarVinculoCliente(novoVinculo, true);
                }
            });
        }
        const addVinculoArvoreNovoProduto: boolean = this.abaVinculo.listaVinculoArvore.some(vinculoArvore => {
            if (vinculoArvore instanceof VinculoArvoreProduto && (vinculoArvore as VinculoArvoreProduto).vinculoProduto.vinculoNovo) {
                return true;
            } else {
                return false;
            }
        });
        const addVinculoArvoreNovaGarantia: boolean = this.abaVinculo.listaVinculoArvore.some(vinculoArvore => {
            if (vinculoArvore instanceof VinculoArvoreGarantia && (vinculoArvore as VinculoArvoreGarantia).vinculoGarantia.vinculoNovo) {
                return true;
            } else {
                return false;
            }
        });
        if (!addVinculoArvoreNovoProduto && changeProduto) {
            const length: number = this.abaVinculo.produtoLista.length;
            const produtos: Array<VinculoProduto> = Object.assign(new Array<VinculoProduto>(), this.abaVinculo.produtoLista);
            this.abaVinculo.produtoLista.splice(0, length);
            produtos.forEach(produto => {
                const vinculoProduto: VinculoProduto = Object.assign(new VinculoProduto, produto);
                this.inicializaVinculoProduto({ vinculoProduto: vinculoProduto });
            });
        }
        if (!addVinculoArvoreNovaGarantia && changeGarantia) {
            const length: number = this.abaVinculo.garantias.length;
            const garantias: Array<any> = this.mudancaSalvaService.getGarantias();
            this.abaVinculo.garantias.splice(0, length);
            garantias.forEach(garantia => {
                const vinculoGarantia: VinculoGarantia = Object.assign(new VinculoGarantia, garantia);
                this.inicializaVinculoGarantia(vinculoGarantia, identificadorDossieFase);
            });
        }
    }

    /**
     * adicao vinculo produto lista produto
     */
    preencheProdutoRetorno() {
        this.abaVinculo.produtoLista = [];
        for (let dp = 0; dp < this.abaVinculo.cliente.produtos.length; dp++) {
            this.vinculoProduto = Object.assign({}, this.abaVinculo.cliente.produtos[dp]);
            this.abaVinculo.produtoLista.push(this.vinculoProduto);
        }
    }

    /**
     * adicção garantia lista garantias
     */
    loadGarantia() {
        this.abaVinculo.garantias = [];
        for (const item of this.abaVinculo.cliente.garantias) {
            this.garantia = Object.assign({}, item);
            this.abaVinculo.garantias.push(this.garantia);
        }
    }


    /**
     * Método responsável por mostrar os dados do cliente basedo no SICLI
     * @param user 
     */
    showModalSICLI(user: VinculoCliente) {
        const cpfCnpj = user.tipo_pessoa === 'F' ? user.cpf : user.cnpj;
        const isCPF = user.tipo_pessoa === 'F' ? true : false;
        this.clienteService.getClienteSicliUnico(cpfCnpj, isCPF).subscribe(response => {
            this.dialogService.addDialog(ModalSicliComponent, {
                sicliUser: response,
                cpf: isCPF,
                cpfCnpj: cpfCnpj
            });
        }, error => {
            this.retornoErros(error, isCPF);
            this.loadService.hide();
            throw error;
        });

    }

    /**
     * Quando retorna erro os tipo de erros possiveis e Tratados
     */
    private retornoErros(error: any, isCPF: boolean) {
        if (error.status == 404 && isCPF) {
            Utils.showMessageDialog(this.dialogService, 'Cliente solicitado não foi localizado no SICLI com o CPF informado.');
        }
        else if (error.status == 404 && !isCPF) {
            Utils.showMessageDialog(this.dialogService, 'Cliente solicitado não foi localizado no SICLI com o CNPJ informado.');
        }
        else if (error.status == 500) {
            Utils.showMessageDialog(this.dialogService, 'Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.');
        }
        else if (error.status == 503 && isCPF) {
            Utils.showMessageDialog(this.dialogService, 'Falha ao consumir serviço do SICLI com o CPF informado.');
        }
        else if (error.status == 503 && !isCPF) {
            Utils.showMessageDialog(this.dialogService, 'Falha ao consumir serviço do SICLI com o CNPJ informado.');
        }
    }


    /**
     * Inicializa o objeto vinculoProduto
     * @param retorno 
     * @param processoEscolhido 
     * @param produtoEncontrado 
     * @param idProcessoFase 
     * @param produtoLista 
     */
    inicializaVinculoProduto(retorno: any) {
        let vinculoArvoreProduto = new VinculoArvoreProduto();
        vinculoArvoreProduto.vinculoProduto = new VinculoProduto();
        let produtoEncontrado: boolean = false;
        if (retorno) {
            if (this.abaVinculo.produtoLista === undefined) {
                this.abaVinculo.produtoLista = [];
            }
            vinculoArvoreProduto.id = this.abaVinculo.processoEscolhido.id;
            vinculoArvoreProduto.nome = this.abaVinculo.processoEscolhido.nome;
            vinculoArvoreProduto.alterandoVinculo = true;
            vinculoArvoreProduto.vinculoProduto = retorno.vinculoProduto;
            vinculoArvoreProduto.vinculoProduto.elementos_conteudos = [];
            // Desenvolvendo metodo de seta tipo Documento
            if (null != this.abaVinculo.processoEscolhido.produtos_vinculados) {
                produtoEncontrado = this.encontraTipoDeElementoDoProduto(this.abaVinculo.processoEscolhido.produtos_vinculados, retorno.vinculoProduto.id);
            }
            //Quando o produto não e do DOSSIE, seta-se o id do Processo Fase
            if (!produtoEncontrado) {
                vinculoArvoreProduto.vinculoProduto.elementos_conteudos = [];
                vinculoArvoreProduto.idProcessoFase = this.abaVinculo.idProcessoFase;
            }

            /**
            * Adicionando na lista
            */
            this.abaVinculo.produtoLista.push(vinculoArvoreProduto.vinculoProduto);
            this.abaVinculo.listaVinculoArvore.push(vinculoArvoreProduto);

            /**
             * Alterando a memoria para ser observado
             */
            this.abaVinculo.produtoLista = Object.assign([], this.abaVinculo.produtoLista);
            this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);

            this.verificarGarantiaInformada();
            this.abaVinculo.produtoListaChanged.emit(this.abaVinculo.produtoLista);
            this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
            this.mudancaSalvaService.setIsMudancaSalva(false);
            this.abaVinculo.showGarantiaProduto = this.abaVinculo.processoEscolhido.produtos_vinculados.find(produtoVinculado => produtoVinculado.id == vinculoArvoreProduto.vinculoProduto.id).garantias_vinculadas.length > 0;
            if (this.abaVinculo.showGarantiaProduto) {
                this.abaVinculo.produtoGarantiaLista.push(vinculoArvoreProduto.vinculoProduto);
            }
        }
    }

    /**
     * Método responsável por habilitar o vínculo de garantia.
     */
    private verificarGarantiaInformada() {
        var resposta = false;
        this.abaVinculo.produtoLista.forEach(produto => {
            this.abaVinculo.produtosVinculados.forEach(produtoVinculado => {
                if (produto.codigo_modalidade == produtoVinculado.codigo_modalidade
                    && produto.codigo_operacao == produtoVinculado.codigo_operacao
                    && produto.nome == produtoVinculado.nome
                    && (produtoVinculado.garantias_vinculadas && produtoVinculado.garantias_vinculadas.length > 0 && produtoVinculado.garantias_vinculadas != null)
                ) {
                    resposta = true;
                }
            });
        });

    }

    /**
     * Verifica se existe produto conforme codigo operação
     * @param lista 
     * @param id 
     */
    private encontraTipoDeElementoDoProduto(lista, id): boolean {
        let produtoEncontrado: boolean = false;
        for (let i = 0; i < lista.length; i++) {
            if (lista != undefined && lista[i].codigo_operacao == id) {
                produtoEncontrado = true;
                break;
            }
        }
        return produtoEncontrado;
    }

    /**
     * Ordena as listas: produtos, garantias e clientes
     */
    ordenarListasOrdemAlfabetica(differ: any) {
        if (this.abaVinculo.produtoLista && differ.diff(this.abaVinculo.produtoLista)) {
            Utils.ordenarPorOrdemAlfabetica(this.abaVinculo.produtoLista);
        }
        if (this.abaVinculo.garantias && differ.diff(this.abaVinculo.garantias)) {
            Utils.ordenarPorOrdemAlfabetica(this.abaVinculo.garantias);
        }
        if (this.abaVinculo.clienteLista && differ.diff(this.abaVinculo.clienteLista)) {
            Utils.ordenarPorOrdemAlfabetica(this.abaVinculo.clienteLista);
        }
    }

    /**
     * Mantem as garantias em serviço
     * para posterior utilizacao: consulta, alteração de estado: ex: troca etapa breadcumb
     * @param retorno 
     */
    manterGarantiasServico(garantia: any) {
        if (!this.mudancaSalvaService.getGarantias().includes(garantia)) {
            this.mudancaSalvaService.getGarantias().push(garantia);
        }
    }

    /**
     * Inicializa o objeto vinculoGarantia
     * @param retorno 
     * @param processoEscolhido 
     */
    inicializaVinculoGarantia(retorno: any, identificadorDossieFase: IdentificadorDossieFase) {
        let vinculoArvoreGarantia = new VinculoArvoreGarantia();
        vinculoArvoreGarantia.vinculoGarantia = new VinculoGarantia();

        if (retorno) {
            if (this.abaVinculo.garantias === undefined) {
                this.abaVinculo.garantias = [];
            }
            vinculoArvoreGarantia.alterandoVinculo = true;
            vinculoArvoreGarantia.id = identificadorDossieFase.idDossie;
            vinculoArvoreGarantia.idProcessoFase = identificadorDossieFase.idFase;
            vinculoArvoreGarantia.nome = this.abaVinculo.processoEscolhido.nome;
            vinculoArvoreGarantia.alterandoVinculo = true;
            /**
             * Campos Obrigatorio Para adicionar Uma Garantia
             */
            if (retorno.garantia.produtoVinculado) {
                vinculoArvoreGarantia.vinculoGarantia.produto = retorno.garantia.produtoVinculado.id;
                vinculoArvoreGarantia.vinculoGarantia.operacao = retorno.garantia.produtoVinculado.codigo_operacao;
                vinculoArvoreGarantia.vinculoGarantia.modalidade = retorno.garantia.produtoVinculado.codigo_modalidade;
                vinculoArvoreGarantia.vinculoGarantia.produto_operacao = retorno.garantia.produtoVinculado.codigo_operacao;
                vinculoArvoreGarantia.vinculoGarantia.produto_modalidade = retorno.garantia.produtoVinculado.codigo_modalidade;
                vinculoArvoreGarantia.vinculoGarantia.produto_nome = retorno.garantia.produtoVinculado.nome;
                vinculoArvoreGarantia.vinculoGarantia.descricao = retorno.garantia.produtoVinculado.nome;
            }

            vinculoArvoreGarantia.vinculoGarantia.garantia = retorno.garantia.id;
            vinculoArvoreGarantia.vinculoGarantia.nome = retorno.garantia.nome;
            vinculoArvoreGarantia.vinculoGarantia.indicador_fidejussoria = retorno.garantia.indicador_fidejussoria;
            this.montarApresentacaoRelacionado(retorno, vinculoArvoreGarantia);
            vinculoArvoreGarantia.vinculoGarantia.documento = [];
            vinculoArvoreGarantia.vinculoGarantia.valor_garantia = retorno.garantia.valor_garantia;
            vinculoArvoreGarantia.vinculoGarantia.vinculoNovo = retorno.garantia.vinculoNovo;
            vinculoArvoreGarantia.vinculoGarantia.codigo_bacen = retorno.garantia.codigo_bacen;

            if (retorno.garantia.respostas_formulario && retorno.garantia.respostas_formulario.length > 0) {
                vinculoArvoreGarantia.vinculoGarantia.respostas_formulario = retorno.garantia.respostas_formulario;
            }

            /**
           * Método responsavel Definir de Onde vem a Garantia se do DOSSIE ou da FASE do DOSSIE 
           */
            this.percorrerProcessoEscolhidoEAtribuirDeOndeGarantia(retorno.garantia.garantiaVinculada, vinculoArvoreGarantia, this.abaVinculo.processoEscolhido, this.abaVinculo.idProcessoFase);
            /**
            * Adicionando na lista
            */
            this.abaVinculo.garantias.push(vinculoArvoreGarantia.vinculoGarantia);
            this.abaVinculo.listaVinculoArvore.push(vinculoArvoreGarantia);
            /**
             * Alterando a memoria para ser observado
             */
            this.abaVinculo.garantias = Object.assign([], this.abaVinculo.garantias);
            this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);
            /**
             * Alterando a memoria para ser observado
             */
            this.abaVinculo.garantiasChanged.emit(this.abaVinculo.garantias);
            this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
            this.mudancaSalvaService.setIsMudancaSalva(false);
        }
    }

    /**
     * Montar apresentação quando o vinculo garantia tem mais de uma pessoa relacionada
     * @param retorno 
     * @param vinculoArvoreGarantia 
     */
    private montarApresentacaoRelacionado(retorno, vinculoArvoreGarantia: VinculoArvoreGarantia) {
        vinculoArvoreGarantia.vinculoGarantia.clientes_avalistas = [];
        if (undefined != retorno.garantia.clientes_avalistas && retorno.garantia.clientes_avalistas.length > 0) {
            retorno.garantia.clientes_avalistas.forEach((pessoa, idx) => {

                let cpfCnpj = pessoa.label != undefined && pessoa.label != '' ? Utils.masKcpfCnpj(pessoa.label) : null;

                if (idx == 0) {
                    vinculoArvoreGarantia.vinculoGarantia.relacionado = cpfCnpj;
                    vinculoArvoreGarantia.vinculoGarantia.clientes_avalistas.push(pessoa.value);
                }
                else {
                    vinculoArvoreGarantia.vinculoGarantia.relacionado = vinculoArvoreGarantia.vinculoGarantia.relacionado + ", " + cpfCnpj;
                    vinculoArvoreGarantia.vinculoGarantia.clientes_avalistas.push(pessoa.value);
                }
            });
        }
    }

    private percorrerProcessoEscolhidoEAtribuirDeOndeGarantia(apAux: string, vinculoArvoreGarantia: VinculoArvoreGarantia, processoEscolhido: any, idProcessoFase: any) {
        if (null != processoEscolhido.processos_filho) {
            //Recebendo Lista de Garantia de ProcessoDossie
            const grantiaDossie = processoEscolhido.documentos_garantia;
            //Percorrendo Fases do Prosseço dossie
            for (let index = 0; index < processoEscolhido.processos_filho.length; index++) {
                //Recebendo Lista de Garantia de ProcessoFase
                const garantiaFase = processoEscolhido.processos_filho[index].documentos_garantia;
                //Comparando se as Garantias são iguais
                if (processoEscolhido.processos_filho[index].id == idProcessoFase) {
                    //Percorrendo a Garantia do Dossiegi
                    if (null != grantiaDossie) {
                        for (let d = 0; d < grantiaDossie.length; d++) {
                            const garantiaDossieAdd = grantiaDossie[d];
                            this.verificarSeGarantiaEDossieOuFase(garantiaDossieAdd, apAux, vinculoArvoreGarantia, processoEscolhido.id, idProcessoFase);
                        }
                    }
                    if (null != garantiaFase) {
                        //Percorrendo a Garantia da Fase
                        for (let g = 0; g < garantiaFase.length; g++) {
                            const garantiaAdd = garantiaFase[g];
                            this.verificarSeGarantiaEDossieOuFase(garantiaAdd, apAux, vinculoArvoreGarantia, null, idProcessoFase);
                        }
                    }
                }
            }
        }
    }

    /**
     * Verifica tipo dossie: Dossie e fase
     * @param garantiaAdd 
     * @param apAux 
     * @param vinculoArvoreGarantia 
     * @param id 
     * @param idProcessoFase 
     */
    private verificarSeGarantiaEDossieOuFase(garantiaAdd: any, apAux: string, vinculoArvoreGarantia: VinculoArvoreGarantia, id: number, idProcessoFase: any) {
        //Verificando se existe a Garantia
        if (garantiaAdd.garantia.id == +apAux[0]) {
            //Se existe id e por que e do Processo Dossie
            if (null != id) {
                vinculoArvoreGarantia.id = id;
            } else {
                //Garantia da Fase
                vinculoArvoreGarantia.idProcessoFase = idProcessoFase;
            }
        }
    }

    /**
     * Ação ao remover produto
     */
    removeProduto(index: number, produto: any, alertMessagesWarning: any) {
        if (this.abaVinculo.garantias != null && this.abaVinculo.garantias.length > 0) {
            let vinculosGarantias;
            let contador = 0;
            let garantia;
            garantia = this.existeGarantiaVinculada(this.abaVinculo.garantias, produto.id);
            if (garantia != null) {
                this.loadService.show();
                vinculosGarantias = contador > 0 ? vinculosGarantias + ', ' + garantia.codigo_bacen + ' - ' + garantia.nome : garantia.codigo_bacen + ' - ' + garantia.nome;
                setTimeout(() => {
                    let msgs = [];
                    msgs.push(contador > 1 ? MSG_ABA_VINCULO.EXCLUIR_GARANTIAS.replace('$$', vinculosGarantias) : MSG_ABA_VINCULO.EXCLUIR_GARANTIA.replace('$$', vinculosGarantias));
                    alertMessagesWarning.showAlertMessageWarning(msgs);
                    this.loadService.hide();
                }, 500);
            } else {
                this.confirmRemoveProduto(index);
            }
        } else {
            this.confirmRemoveProduto(index);
        }
    }

    /**
     * Metodo responsavel por excluir vinculo Cliente
     * @param index 
     * @param cnpj 
     * @param cpf 
     * @param tipoRel 
     */
    removePessoa(index: number, cnpj: string, cpf: string, tipoRel: String, alertMessagesWarning: any) {
        let valor = cnpj != null ? cnpj : cpf;
        let msgInfor = '';
        let garantiaVinculo, pessoaVinculo;
        let contador = 0;
        ({ pessoaVinculo, msgInfor, contador } = this.existeVinculoRelacionadoAPessoa(valor, msgInfor, contador));
        if (!pessoaVinculo) {
            ({ garantiaVinculo, msgInfor, contador } = this.existeVinculoRelacionadoAPessoaGarantia(valor, msgInfor, contador));
        }
        this.validarAcaoASerTomadaQuandoExcluir(pessoaVinculo, garantiaVinculo, valor, tipoRel, index, msgInfor, contador, alertMessagesWarning);
    }

    /**
     * Quando esta inserindo ou editando um vinculo que não esta em base de dados adiciona a lista de cliente e de listaVinculoArvore
     * @param vinculoArvoreCliente 
     */
    private adicionarListaVinculoArvore(vinculoArvoreCliente: VinculoArvoreCliente) {
        vinculoArvoreCliente.alterandoVinculo = true;
        this.abaVinculo.clienteLista.push(vinculoArvoreCliente.vinculoCliente);
        this.abaVinculo.listaVinculoArvore.push(vinculoArvoreCliente);
        this.abaVinculo.qtdVincPessoa = this.abaVinculo.clienteLista.length;
        this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);
        this.abaVinculo.clienteLista = Object.assign([], this.abaVinculo.clienteLista);
        this.inicializarContadorSocios();
        this.abaVinculo.clienteListaChanged.emit(this.abaVinculo.clienteLista);
        this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
        /**
         * Criar Task para implementar o retorno quando abrir o dossie Produto pelo link
         */
        if (this.abaVinculo.clienteLista.length === 1) {
            if (this.abaVinculo.clienteLista[0].tipo_relacionamento === "TOMADOR_PRIMEIRO_TITULAR") {
                this.abaVinculo.primeiroVinculoPessoa.emit(this.abaVinculo.clienteLista[0].cnpj !== undefined ? this.abaVinculo.clienteLista[0].cnpj : this.abaVinculo.clienteLista[0].cpf);
            }
        }
    }

    /**
     * Realiza a contagem de socios para mostrar o loading e temporarizador da inserção do vinculo cliente
     */
    private inicializarContadorSocios() {
        if (this.abaVinculo.selectedSociosReceitaFederal.length > 0) {
            this.loadingAbaVinculo.countSocio = this.loadingAbaVinculo.countSocio + 1;
            if (this.loadingAbaVinculo.countSocio == this.abaVinculo.selectedSociosReceitaFederal.length) {
                this.mudancaSalvaService.loading.emit(false);
            }
        }
    }

    /**
     * Método responsavel por definir se o vinculo existe no banco ou apenas no fronte
     * @param clientiEditado 
     * @param idx 
     * @param vinculoArvoreCliente 
     */
    private definirSeAtualizarOuRemove(clienteEditado: any, idx: any, vinculoArvoreCliente: VinculoArvoreCliente) {
        if (clienteEditado.vinculoNovo) {
            this.removerClienteListaVinculoArvore(idx);
            this.adicionarListaVinculoArvore(vinculoArvoreCliente);
        }
        else {
            this.atualizarRelacionado(idx, clienteEditado.relacionado, clienteEditado.dossie_cliente_relacionado, clienteEditado.dossie_cliente_relacionado_anterior);
        }
    }

    existeVinculoClientePrincipalNoDossieProduto() {
        let tipoPrincipal = this.abaVinculo.listaTipoRelacionamento.find(tp => tp.principal);
        return this.abaVinculo.listaVinculoArvore.some(vinculoArvore => {
            return (vinculoArvore instanceof VinculoArvoreCliente) && vinculoArvore.vinculoCliente.tipo_relacionamento.principal == tipoPrincipal.principal;
        });
    }

    /**
     * 
     * @param inserir 
     * @param vinculoPessoa 
     * @param pessoa 
     * @param editar 
     * @param comboTipoRelacionamento 
     * @param comboRelacionado 
     * @param guardRelacioandoAntigo 
     * @param guardIdRelacionadoAntigo 
     */
    casoforEditarPrecisaDessasAlteracoes(inserir: boolean, vinculoPessoa: any, pessoa: any, editar: any, comboTipoRelacionamento: any, comboRelacionado: any, guardRelacioandoAntigo: any, guardIdRelacionadoAntigo: any) {
        if (!inserir) {
            this.abaVinculo.cnpj = undefined == vinculoPessoa.cpf ? vinculoPessoa.cnpj : vinculoPessoa.cpf;
            this.abaVinculo.icTipoPessoa = vinculoPessoa.tipo_pessoa;
            pessoa = vinculoPessoa;
            editar = (undefined != vinculoPessoa.vinculoEditar && !vinculoPessoa.vinculoNovo) ? vinculoPessoa.vinculoEditar : false;
            ({ comboTipoRelacionamento, comboRelacionado, guardRelacioandoAntigo, guardIdRelacionadoAntigo } = this.montarValorComboParaMostraModal(vinculoPessoa, comboTipoRelacionamento, comboRelacionado, guardRelacioandoAntigo, guardIdRelacionadoAntigo));
        }
        return { pessoa, editar };
    }

    /**
     * Metodo responsavel por popular variaveis para armazena dados antigos para caso o vinculo seja alterado e depois clicado em cancelar
     * @param vinculoPessoa 
     * @param comboTipoRelacionamento 
     * @param comboRelacionado 
     * @param guardRelacioandoAntigo 
     * @param guardIdRelacionadoAntigo 
     */
    private montarValorComboParaMostraModal(vinculoPessoa: any, comboTipoRelacionamento: any, comboRelacionado: any, guardRelacioandoAntigo: any, guardIdRelacionadoAntigo: any) {
        guardRelacioandoAntigo = vinculoPessoa.relacionado;
        guardIdRelacionadoAntigo = vinculoPessoa.dossie_cliente_relacionado;
        vinculoPessoa.indica_relacionado = (undefined != vinculoPessoa.relacionado && "" != vinculoPessoa.relacionado) ? true : false;
        vinculoPessoa.indica_sequencia = !vinculoPessoa.indica_relacionado;
        comboTipoRelacionamento = vinculoPessoa.tipo_relacionamento + "|" + vinculoPessoa.principal + "|" + vinculoPessoa.indica_relacionado + "|" + vinculoPessoa.indica_sequencia;
        comboRelacionado = vinculoPessoa.relacionado + "|" + vinculoPessoa.dossie_cliente_relacionado;
        return { comboTipoRelacionamento, comboRelacionado, guardRelacioandoAntigo, guardIdRelacionadoAntigo };
    }



    /**
     * Monta o objet vinculo cliente
     * @param retorno 
     * @param inserir 
     */
    montarVinculoCliente(retorno: any, inserir: boolean) {
        if (retorno) {
            /**
               * Definindo que a lista ira ser de Vinculo Cliente para o modal atual
               */
            let vinculoCliente: VinculoCliente = retorno.clientePessoaModal ? retorno.clientePessoaModal : retorno;
            let documentos: DocumentoArvore[] = retorno.clientePessoaModal ? retorno.clientePessoaModal.documentos : retorno.documentos;
            let vinculoArvoreCliente = new VinculoArvoreCliente();
            vinculoArvoreCliente.vinculoCliente = new VinculoCliente();
            vinculoArvoreCliente.id = this.abaVinculo.processoEscolhido.id;
            vinculoArvoreCliente.nome = this.abaVinculo.processoEscolhido.nome;
            vinculoArvoreCliente.vinculoCliente = vinculoCliente;
            vinculoArvoreCliente.vinculoCliente.documentos = documentos;
            vinculoArvoreCliente.vinculoCliente.dossies_produto = [];
            vinculoArvoreCliente.vinculoCliente.produtos_habilitados = [];

            this.mudancaSalvaService.setIsMudancaSalva(false);
            if (!inserir) {
                if (this.abaVinculo.clienteLista && this.abaVinculo.clienteLista.length == 1) {
                    this.abaVinculo.clienteLista.forEach((clientiEditado, idx) => {
                        if (clientiEditado.id == vinculoArvoreCliente.vinculoCliente.id) {
                            this.removerClienteListaVinculoArvore(idx);
                        }
                    });
                    this.adicionarListaVinculoArvore(vinculoArvoreCliente);
                } else {
                    this.abaVinculo.clienteLista.forEach((clientiEditado, idx) => {
                        if (clientiEditado.id == vinculoArvoreCliente.vinculoCliente.id) {
                            this.montarListaDeClinte(clientiEditado, idx, vinculoArvoreCliente);
                        }
                    });
                }
            } else {
                vinculoArvoreCliente.vinculoCliente.vinculoNovo = inserir;
                this.adicionarListaVinculoArvore(vinculoArvoreCliente);
            }
            this.abaVinculo.exibeLoadArvoreChanged.emit(true);
            this.mudancaSalvaService.setIsMudancaSalva(false);
        }
    }


    private montarListaDeClinte(clientiEditado: any, idx: any, vinculoArvoreCliente: VinculoArvoreCliente) {
        clientiEditado.alterandoVinculo = true;
        const valor = this.retornaCpfOuCnpj(clientiEditado);
        if (valor == this.abaVinculo.cliente.cnpj && clientiEditado.principal){
            return;
        }
        this.definirSeAtualizarOuRemove(clientiEditado, idx, vinculoArvoreCliente);
    }

    /**
     * Quando o objeto existe na base de dados deve apenas atualziar
     * @param index 
     * @param novoRelacioando 
     * @param idRelacionado 
     */
    private atualizarRelacionado(index: number, novoRelacioando: any, idRelacionado: any, idRelacionadoAnterior: any) {
        const vincCliente = this.abaVinculo.clienteLista[index];
        this.abaVinculo.listaVinculoArvore.forEach((vinculoArvore) => {
            if (vinculoArvore instanceof VinculoArvoreCliente) {
                if (vincCliente.id === vinculoArvore.vinculoCliente.id && vincCliente.indica_relacionado && vincCliente.tipo_relacionamento == vinculoArvore.vinculoCliente.tipo_relacionamento) {
                    vinculoArvore.alterandoVinculo = true;
                    vinculoArvore.vinculoCliente.dossie_cliente_relacionado = idRelacionado;
                    vinculoArvore.vinculoCliente.dossie_cliente_relacionado_anterior = idRelacionadoAnterior;
                    vinculoArvore.vinculoCliente.relacionado = novoRelacioando;
                } else if (vincCliente.id === vinculoArvore.vinculoCliente.id && vincCliente.indica_sequencia) {
                    vinculoArvore.alterandoVinculo = true;
                    vinculoArvore.vinculoCliente.seqTitularidade = vincCliente.seqTitularidade;
                    vinculoArvore.vinculoCliente.seqTitularidadeAntiga = vincCliente.seqTitularidadeAntiga;
                    vinculoArvore.vinculoCliente.sequencia_titularidade = vincCliente.sequencia_titularidade;
                }
            }
        });
    }


    /**
     * Método responsvael por excluir da lista de Arvore
     * @param index 
     */
    private removerClienteListaVinculoArvore(index: number) {
        let vinculoArvoreNovoExcluir: VinculoArvore = undefined;
        const vincCliente = this.abaVinculo.clienteLista[index];
        this.abaVinculo.clienteLista.splice(index, 1);
        this.abaVinculo.listaVinculoArvore.forEach((vinculoArvore) => {
            if (vinculoArvore instanceof VinculoArvoreCliente) {
                if (vincCliente.id === vinculoArvore.vinculoCliente.id && vincCliente.tipo_relacionamento == vinculoArvore.vinculoCliente.tipo_relacionamento) {
                    if (vincCliente.vinculoNovo) {
                        vinculoArvoreNovoExcluir = vinculoArvore;
                    } else {
                        vinculoArvore.alterandoVinculo = true;
                        vinculoArvore.vinculoCliente.exclusao = true;
                        vinculoArvore.ocultarVinculoArvore = true;
                    }
                }
            }
        });
        if (vinculoArvoreNovoExcluir) {
            this.abaVinculo.listaVinculoArvore.splice(this.abaVinculo.listaVinculoArvore.indexOf(vinculoArvoreNovoExcluir), 1);
            this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);
            this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
        }

        this.abaVinculo.clienteListaChanged.emit(this.abaVinculo.clienteLista);
    }

    /**
     * Verifica existencia vinculo garantia
     * @param garantiaVinculo 
     * @param qtdVinculo 
     * @param msgInfor 
     */
    private existeVinculoGarantia(garantiaVinculo: any, qtdVinculo: any, msgInfor: string, alertMessagesWarning: any) {
        if (garantiaVinculo) {
            let msg = qtdVinculo > 1 ? MSG_MODAL_PESSOA.VINCULADA_AS_GARANTIAS.replace("$$", msgInfor) : MSG_MODAL_PESSOA.VINCULADA_A_GARANTIA.replace("$$", msgInfor);
            let msgs = [];
            msgs.push(msg);
            alertMessagesWarning.showAlertMessageWarning(msgs);
        }
    }

    /**
     * Dois tipo de validação quando uma confirmando a Exclusão, e outra se existe vinculos com essa PESSOA.
     * @param existeVinculo 
     * @param valor 
     * @param tipoRel 
     * @param index 
     */
    private validarAcaoASerTomadaQuandoExcluir(pessoaVinculo: any, garantiaVinculo: any, valor: string, tipoRel: String, index: number, msgInfor: string, qtdVinculo, alertMessagesWarning: any) {
        if (!garantiaVinculo && !pessoaVinculo) {
            Utils.showMessageConfirm(this.dialogService, MSG_MODAL_PESSOA.CONFIRMA_EXCLUIR).subscribe(res => {
                if (res) {
                    if (valor == this.abaVinculo.cliente.cnpj && this.abaVinculo.cliente.principal){
                        return;
                    }
                    this.removerClienteListaVinculoArvore(index);

                }
            },
                () => {
                    this.loadService.hide();
                });
            return;
        }

        this.existeVinculosASerRemovidoAntes(pessoaVinculo, garantiaVinculo, qtdVinculo, msgInfor, alertMessagesWarning);
    }

    /**
     * Vinculos com remoção antecipada
     * @param pessoaVinculo 
     * @param garantiaVinculo 
     * @param qtdVinculo 
     * @param msgInfor 
     */
    private existeVinculosASerRemovidoAntes(pessoaVinculo: any, garantiaVinculo: any, qtdVinculo: any, msgInfor: string, alertMessagesWarning: any) {
        this.loadService.show();
        setTimeout(() => {
            this.existeVinculoGarantia(garantiaVinculo, qtdVinculo, msgInfor, alertMessagesWarning);
            this.existeVinculoPessoa(pessoaVinculo, qtdVinculo, msgInfor, alertMessagesWarning);
            this.loadService.hide();
        }, 500);
    }

    /**
     * Verifica existencia vinculo Pessoa
     * @param pessoaVinculo 
     * @param qtdVinculo 
     * @param msgInfor 
     */
    private existeVinculoPessoa(pessoaVinculo: any, qtdVinculo: any, msgInfor: string, alertMessagesWarning: any) {
        if (pessoaVinculo) {
            let msg = qtdVinculo > 1 ? MSG_MODAL_PESSOA.VINCULADA_RELACIONADOS.replace("$$", msgInfor) : MSG_MODAL_PESSOA.VINCULADA_RELACIONADO.replace("$$", msgInfor);
            let msgs = [];
            msgs.push(msg);
            alertMessagesWarning.showAlertMessageWarning(msgs);
        }
    }

    /**
     * Verifica se existe vinculo com Garanta
     * @param valor 
     */
    private existeVinculoRelacionadoAPessoaGarantia(valor: string, msgInfor: any, contador: any) {
        let garantiaVinculo = false;
        if (this.abaVinculo.garantias && this.abaVinculo.garantias.length > 0) {
            for (let garantia of this.abaVinculo.garantias) {
                if (garantia.relacionado && Utils.removerTodosCaracteresEspeciais(garantia.relacionado).indexOf(Utils.removerTodosCaracteresEspeciais(valor)) != -1) {
                    garantiaVinculo = !garantiaVinculo ? (garantia.relacionado && Utils.removerTodosCaracteresEspeciais(garantia.relacionado).indexOf(Utils.removerTodosCaracteresEspeciais(valor)) != -1) : garantiaVinculo;
                    if (msgInfor.indexOf(garantia.nome) == -1) {
                        msgInfor = contador > 0 ? msgInfor + ', ' + garantia.codigo_bacen + ' - ' + garantia.nome : garantia.codigo_bacen + ' - ' + garantia.nome;
                    }
                    contador++;
                }

            }
        }
        return { garantiaVinculo, msgInfor, contador };
    }

    /**
     * Acao ao confirmar remoção produto
     * @param index 
     */
    private confirmRemoveProduto(index: number) {
        Utils.showMessageConfirm(this.dialogService, MSG_MODAL_PESSOA.CONFIRMA_EXCLUIR).subscribe(res => {
            this.onSucessConfirmRemoveProduto(res, index);
        },
            () => {
                this.loadService.hide();
            });
    }

    /**
     * Verifica se existe garantia Vinculada
     * @param lista 
     * @param idProduto 
     */
    private existeGarantiaVinculada(lista: VinculoGarantia[], idProduto: number) {
        for (let item of lista) {
            if (+item.produto == idProduto) {
                return item;
            }
        }
        return null;
    }

    /**
     * Verifica se existe vinculo com Pessoa
     * @param valor 
     */
    private existeVinculoRelacionadoAPessoa(valor: string, msgInfor: any, contador: any) {
        let clienteRelacioando = this.abaVinculo.clienteLista.find(c => this.existeClienteCpfNaLista(valor, c) || this.existeCnpjNaListaDeCliente(valor, c));
        let pessoaVinculo = false;
        for (let cliente of this.abaVinculo.clienteLista) {
            if (!cliente.sequencia_titularidade && Utils.removerTodosCaracteresEspeciais(valor) == Utils.removerTodosCaracteresEspeciais(cliente.relacionado)) {
                pessoaVinculo = true;
                if (msgInfor.indexOf(cliente.relacionado) == -1) {
                    msgInfor = contador > 0 ? msgInfor + ', ' + this.mascaraCnpjCpf(this.retornaCpfOuCnpj(cliente)) : this.mascaraCnpjCpf(this.retornaCpfOuCnpj(cliente));
                }
                contador++;
            }

        }
        return { pessoaVinculo, msgInfor, contador };
    }

    /**
     * quando a lista for do tipo Juridico o o vinculo principal não pode repertir
     */
    filtrarLista() {
        let listaModalTipoRelacionamento = [];
        this.abaVinculo.listaTipoRelacionamento.forEach(relacionamento => {
            !relacionamento.indica_relacionado && !relacionamento.indica_sequencia ? relacionamento : listaModalTipoRelacionamento.push(relacionamento);
        });
        return listaModalTipoRelacionamento;
    }

    /**
     * responsavel por mascarar CNPJ ou CPF
     * @param v 
     */
    mascaraCnpjCpf(v: any): void {
        if (undefined != v && null != v) {
            return Utils.masKcpfCnpj(v);
        }
    }


    /**
     * Retorna string cpf ou cnpj
     * @param cliente 
     */
    private retornaCpfOuCnpj(cliente) {
        return cliente.cpf ? cliente.cpf : cliente.cnpj;
    }

    /**
     * Verifica cpf em lista
     * @param valor 
     * @param cliente 
     */
    private existeClienteCpfNaLista(valor: string, cliente: any) {
        return cliente.cpf && Utils.removerTodosCaracteresEspeciais(valor) == Utils.removerTodosCaracteresEspeciais(cliente.cpf);
    }

    /**
     * Verifica cnpj em lista
     * @param valor 
     * @param cliente 
     */
    private existeCnpjNaListaDeCliente(valor: string, cliente: any): boolean {
        return cliente.cnpj && Utils.removerTodosCaracteresEspeciais(valor) == Utils.removerTodosCaracteresEspeciais(cliente.cnpj);
    }

    /**
     * Ação ao remover produto
     * @param res 
     * @param index 
     */
    onSucessConfirmRemoveProduto(res: boolean, index: number) {
        if (res) {
            let vinculoArvoreNovoExcluir: VinculoArvore = null;
            const vincProduto = this.abaVinculo.produtoLista[index];
            this.abaVinculo.produtoLista.splice(index, 1);
            this.abaVinculo.produtoGarantiaLista.splice(index, 1);
            this.abaVinculo.listaVinculoArvore.forEach((vinculoArvore) => {
                if (vinculoArvore instanceof VinculoArvoreProduto) {
                    if (vincProduto.id === vinculoArvore.vinculoProduto.id) {
                        if (vinculoArvore.vinculoProduto.vinculoNovo) {
                            vinculoArvoreNovoExcluir = vinculoArvore;
                        }
                        else {
                            vinculoArvore.vinculoProduto.exclusao = true;
                            vinculoArvore.ocultarVinculoArvore = true;
                        }
                    }
                }
            });
            if (vinculoArvoreNovoExcluir) {
                this.abaVinculo.listaVinculoArvore.splice(this.abaVinculo.listaVinculoArvore.indexOf(vinculoArvoreNovoExcluir), 1);
            }
        }
        this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);
        this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
        this.abaVinculo.produtoListaChanged.emit(this.abaVinculo.produtoLista);
    }

    /**
     * Remove a garantia do serviço que mantem em memória
     * @param index 
     */
    private removerGarantiaServico(index: number) {
        this.mudancaSalvaService.getGarantias().splice(index, 1);
    }

    /**
     * Ação ao remover garantia.
     * @param res 
     * @param index 
     */
    onSucessConfirmRemoveGarantia(res: boolean, index: number) {
        if (res) {
            let vinculoArvoreNovoExcluir: VinculoArvore = null;
            const vincGarantia = this.abaVinculo.garantias[index];
            this.abaVinculo.garantias.splice(index, 1);
            this.removerGarantiaServico(index);
            this.abaVinculo.listaVinculoArvore.forEach((vinculoArvore) => {
                if (vinculoArvore instanceof VinculoArvoreGarantia) {
                    if (vincGarantia.id === vinculoArvore.vinculoGarantia.id) {
                        if (vincGarantia.vinculoNovo) {
                            vinculoArvoreNovoExcluir = vinculoArvore;
                        }
                        else {
                            vinculoArvore.vinculoGarantia.exclusao = true;
                            vinculoArvore.ocultarVinculoArvore = true;
                        }
                    }
                }
            });
            if (vinculoArvoreNovoExcluir) {
                this.abaVinculo.listaVinculoArvore.splice(this.abaVinculo.listaVinculoArvore.indexOf(vinculoArvoreNovoExcluir), 1);
            }
        }
        this.abaVinculo.listaVinculoArvore = Object.assign([], this.abaVinculo.listaVinculoArvore);
        this.abaVinculo.listaVinculoArvoreChanged.emit(this.abaVinculo.listaVinculoArvore);
        this.abaVinculo.garantiasChanged.emit(this.abaVinculo.garantias);
    }


    /**
     * Renderização de botões
     * @param pessoa 
     */
    habilitarBtns(pessoa: any) {
        return (pessoa.principal && pessoa.sequencia_titularidade == 1) || (pessoa.principal && !pessoa.sequencia_titularidade);
    }

    /**
     * Retorna tipo relacionamento
     * @param input 
     */
    decideTipoRelacionamento(input: string): string {
        if (input === 'CONJUGE') {
            return 'CÔNJUGE';
        } else if (input === 'CONJUGE_SOCIO') {
            return 'CÔNJUGE SÓCIO';
        } else if (input === 'EMPRESA_CONGLOMERADO') {
            return 'EMPRESA CONGLOMERADO';
        } else if (input === 'SOCIO') {
            return 'SÓCIO';
        } else if (input === 'SEGUNDO_TITULAR') {
            return 'SEGUNDO TITULAR';
        } else if (input === 'TOMADOR_PRIMEIRO_TITULAR') {
            return 'TOMADOR/PRIMEIRO TITULAR';
        } else {
            return input;
        }
    }


    /**
     * Retorna forma garantia
     * @param input 
     */
    decideFormaGaratia(input: string): string {
        if (input === 'SDD') {
            return 'Saldo Devedor';
        } else if (input === 'VRC') {
            return 'Valor Contratado';
        } else if (input === 'PMT') {
            return 'Parcela';
        } else if (input === 'LMD') {
            return 'Limite Disponibilizado';
        } else if (input === 'LMC') {
            return 'Limite Contratado';
        }
    }

    /**
     * Retorna o periodo de juros conforme input
     * @param input 
     */
    decidePeriodoJuros(input: string): string {
        if (input) {
            if (input === 'D') {
                return 'Diário';
            }
            if (input === 'M') {
                return 'Mensal';
            }
            if (input === 'A') {
                return 'Anual';
            }
        }
        return 'Período Não Informado';
    }

    /**
     * Responsavel por contar o indice da tabela para efeito de load
     * @param tipoVinculo 
     */
    countRowToProgressBar(tipoVinculo: TipoVinculo) {
        switch (tipoVinculo) {
            case TipoVinculo.PESSOA:
                this.loadingAbaVinculo.countPessoa++;
                break;
            case TipoVinculo.PRODUTO:
                this.loadingAbaVinculo.countProduto++;
                break;
            case TipoVinculo.GARANTIA:
                this.loadingAbaVinculo.countGarantia++;
                break;
        }
    }

    /**
     * Apos a busca de socios na receita federal a modal contrendo a lista de sócios será aberta
     * @param cliente 
     * @param abaVinculoComponent 
     */
    private openModalSocioReceitaFederal(cliente: any, abaVinculoComponent: AbaVinculoComponent) {
        const options: DialogOptions = <DialogOptions>{ closeByClickingOutside: true };
        this.dialogService.addDialog(ModalSocioReceitaFederalComponent, {
            dataInputReceitaFederal: this.inicializarObjetoEntradaModalReceitaFederal(abaVinculoComponent, cliente)
        }, options).subscribe((socioReceitaFederalOutput: SocioReceitaFederalOutput) => {
            this.onSucessModalSocioReceitaFederal(socioReceitaFederalOutput, abaVinculoComponent);
        });
    }

    /**
     * Retorno com sucesso Modal socios receita federal
     * @param socioReceitaFederalOutput 
     * @param abaVinculoComponent 
     */
    private onSucessModalSocioReceitaFederal(socioReceitaFederalOutput: SocioReceitaFederalOutput, abaVinculoComponent: AbaVinculoComponent) {
        if (socioReceitaFederalOutput) {
            this.abaVinculo.selectedSociosReceitaFederal = socioReceitaFederalOutput.selectedSociosReceitaFederal;
            this.loadingAbaVinculo.possuiNovosSocios = true;
            socioReceitaFederalOutput.selectedSociosReceitaFederal.forEach(socio => {
                this.processarAdicaoSocioVinculoCliente(socio, abaVinculoComponent, socioReceitaFederalOutput);
            });
            this.mudancaSalvaService.loading.subscribe(value => {
                this.loadingAbaVinculo.possuiNovosSocios = value;
                this.loadingAbaVinculo.countSocio = 0;
            });
        }
    }

    /**
     * Realiza a adição de socio como vinculo cliente
     * @param socio 
     * @param abaVinculoComponent 
     * @param socioReceitaFederalOutput 
     */
    private processarAdicaoSocioVinculoCliente(socio: Socio, abaVinculoComponent: AbaVinculoComponent, socioReceitaFederalOutput: SocioReceitaFederalOutput) {
        socioReceitaFederalOutput.listaTipoRelacionamento.forEach(relacionamento => {
            const cpf: boolean = !socio.tipo_pessoa;
            let vinculoCliente: VinculoCliente = new VinculoCliente();
            if (relacionamento.indica_receita_pj && socio.tipo_pessoa && (relacionamento.tipo_pessoa == 'J' || relacionamento.tipo_pessoa == 'A')) {
                this.pesquisarSocioComoVinculoCliente(cpf, socio, vinculoCliente, relacionamento, socioReceitaFederalOutput, abaVinculoComponent);
            }
            if (relacionamento.indica_receita_pf && !socio.tipo_pessoa && (relacionamento.tipo_pessoa == 'F' || relacionamento.tipo_pessoa == 'A')) {
                this.pesquisarSocioComoVinculoCliente(cpf, socio, vinculoCliente, relacionamento, socioReceitaFederalOutput, abaVinculoComponent);
            }
        });
    }

    /**
     * Busca o socio como vinculo no SIMTR
     * @param cpf ]
     * @param socio 
     * @param vinculoCliente 
     * @param relacionamento 
     * @param socioReceitaFederalOutput 
     * @param abaVinculoComponent 
     */
    private pesquisarSocioComoVinculoCliente(cpf: boolean, socio: Socio, vinculoCliente: VinculoCliente, tipoRelacionamento: TipoRelacionamento, socioReceitaFederalOutput: SocioReceitaFederalOutput, abaVinculoComponent: AbaVinculoComponent) {
        this.clienteService.getCliente(cpf ? socio.cpf_cnpj.substring(3) : socio.cpf_cnpj, cpf).subscribe(response => {
            vinculoCliente = response;
            this.inicializarVinculoClientePorSocio(abaVinculoComponent, cpf, vinculoCliente, tipoRelacionamento, socio, socioReceitaFederalOutput.cnpj);
            const clienteAdicionado: boolean = this.verificarOcorrenciaVinculoCliente(abaVinculoComponent, socio, tipoRelacionamento);
            if (!clienteAdicionado) {
                this.montarVinculoCliente(vinculoCliente, true);
            }
        }, error => {
            if (error.status == 404) {
                this.insereNovoVinculoClienteSocio(abaVinculoComponent, cpf, vinculoCliente, socio, socioReceitaFederalOutput.cnpj, tipoRelacionamento, socioReceitaFederalOutput);
            } else {
                abaVinculoComponent.addMessageError(JSON.stringify(error));
            }
        });
    }

    /**
     * Verifica a ocorrencia de vinculo cliente na grid de pessoas
     * @param abaVinculoComponent 
     * @param socio 
     * @param tipoRelacionamento 
     */
    private verificarOcorrenciaVinculoCliente(abaVinculoComponent: AbaVinculoComponent, socio: Socio, tipoRelacionamento: TipoRelacionamento): boolean {
        if (socio.tipo_pessoa) {
            return abaVinculoComponent.clienteLista.some(cliente => cliente.cnpj == socio.cpf_cnpj && cliente.tipo_relacionamento.nome == tipoRelacionamento.nome);
        } else {
            return abaVinculoComponent.clienteLista.some(cliente => cliente.cpf == socio.cpf_cnpj.substring(3) && cliente.tipo_relacionamento.nome == tipoRelacionamento.nome);
        }
    }

    /**
     * Cria o objeto vinculo cliente baseado no socio; caso o mesmo ainda nao esteja persistido no SIMTR
     * @param abaVinculoComponent 
     * @param cpf 
     * @param vinculoCliente 
     * @param socio 
     * @param cnpj 
     * @param tipoRelacionamento 
     * @param socioReceitaFederalOutput 
     */
    private insereNovoVinculoClienteSocio(abaVinculoComponent: AbaVinculoComponent, cpf: boolean, vinculoCliente: VinculoCliente, socio: Socio, cnpj: string, tipoRelacionamento: TipoRelacionamento, socioReceitaFederalOutput: SocioReceitaFederalOutput) {
        this.inicializarVinculoClientePorSocio(abaVinculoComponent, cpf, vinculoCliente, tipoRelacionamento, socio, cnpj);
        this.clienteService.insertCliente(vinculoCliente).subscribe(() => {
            this.pesquisarSocioComoVinculoCliente(cpf, socio, vinculoCliente, tipoRelacionamento, socioReceitaFederalOutput, abaVinculoComponent);
        }, error => {
            if (error.status == 409) {
                this.pesquisarSocioComoVinculoCliente(cpf, socio, vinculoCliente, tipoRelacionamento, socioReceitaFederalOutput, abaVinculoComponent);
            } else {
                abaVinculoComponent.addMessageError(JSON.stringify(error));
            }
        });
    }

    /**
     * Monta o vinculo cliente pelo socio recebido receita federal
     * @param abaVinculoComponent 
     * @param cpf 
     * @param vinculoCliente 
     * @param tipoRelacionamento 
     * @param socio 
     * @param cnpj 
     */
    private inicializarVinculoClientePorSocio(abaVinculoComponent: AbaVinculoComponent, cpf: boolean, vinculoCliente: VinculoCliente, tipoRelacionamento: TipoRelacionamento, socio: Socio, cnpj: string) {
        if (cpf) {
            vinculoCliente.tipo_relacionamento = tipoRelacionamento;
            vinculoCliente.ic_tipo_relacionamento = tipoRelacionamento.nome;
            vinculoCliente.nome = socio.nome_socio;
            vinculoCliente.tipo_pessoa = 'F';
            vinculoCliente.cpf = socio.cpf_cnpj.substring(3);
            if (tipoRelacionamento.indica_relacionado) {
                vinculoCliente.dossie_cliente_relacionado = abaVinculoComponent.clienteLista.find(cliente => cliente.cnpj == cnpj).id;
                vinculoCliente.relacionado = cnpj;
            }
            vinculoCliente.indica_relacionado = true;
            vinculoCliente.indica_sequencia = false;
        } else {
            vinculoCliente.tipo_relacionamento = tipoRelacionamento;
            vinculoCliente.ic_tipo_relacionamento = tipoRelacionamento.nome;
            vinculoCliente.nome = socio.nome_socio;
            vinculoCliente.razao_social = socio.nome_socio;
            vinculoCliente.tipo_pessoa = 'J';
            vinculoCliente.cnpj = socio.cpf_cnpj;
            vinculoCliente.sigla_porte = PorteEmpresa.DEMAIS;
            vinculoCliente.conglomerado = false;
            if (tipoRelacionamento.indica_relacionado) {
                vinculoCliente.dossie_cliente_relacionado = abaVinculoComponent.clienteLista.find(cliente => cliente.cnpj == cnpj).id;
                vinculoCliente.relacionado = cnpj;
            }
            vinculoCliente.indica_relacionado = true;
            vinculoCliente.indica_sequencia = false;
        }
    }

    /**
     * Inicializa o objeto de entrada da modal socios receita federal
     * @param abaVinculoComponent 
     * @param cliente 
     */
    private inicializarObjetoEntradaModalReceitaFederal(abaVinculoComponent: AbaVinculoComponent, cliente: any): DataInputReceitaFederal {
        const dataInputReceitaFederal: DataInputReceitaFederal = new DataInputReceitaFederal();
        dataInputReceitaFederal.cnpj = cliente.cnpj;
        dataInputReceitaFederal.relacionamento = cliente.tipo_relacionamento.nome;
        dataInputReceitaFederal.sociosReceitaFederal = this.abaVinculo.sociosReceitaFederal;
        dataInputReceitaFederal.clienteLista = abaVinculoComponent.clienteLista;
        dataInputReceitaFederal.listaTipoRelacionamento = abaVinculoComponent.listaTipoRelacionamento;
        dataInputReceitaFederal.habilitaAlteracao = abaVinculoComponent.habilitaAlteracao;
        this.verificarErroMontagemCamposSocios(dataInputReceitaFederal);
        return dataInputReceitaFederal;
    }
    /**
     * Busca os socios de uma pessos juridica
     * @param cliente 
     * @param abaVinculoComponent 
     */
    consultarSociosReceitaFederal(cliente: any, abaVinculoComponent: AbaVinculoComponent) {
        this.loadService.show();
        this.dossieService.buscarSocios(cliente.cnpj).subscribe(response => {
            this.abaVinculo.sociosReceitaFederal = this.onSucessSociosReceitaFederal(response);
            this.loadService.hide();
            this.openModalSocioReceitaFederal(cliente, abaVinculoComponent);
        }, error => {
            this.loadService.hide();
            if (error.error && error.error["codigo-retorno"] && error.error["codigo-retorno"].indexOf("0003") == 1) {
                abaVinculoComponent.addMessageWarning("Não foram localizados registros de Sócios no cadastro da Receita Federal");
            } else if (error.error && error.error.codRetorno && error.error.codRetorno.indexOf("0003") == 1) {
                abaVinculoComponent.addMessageWarning("Não foram localizados registros de Sócios no cadastro da Receita Federal");
            } else if (error.error && error.error["codigo_http"] && error.error["codigo_http"] == "403") {
                abaVinculoComponent.addMessageError(error.error.mensagem);
                abaVinculoComponent.addMessageError(error.error.detalhe);
            } else if (error.error && error.error["codigo-erro"] && error.error["codigo-erro"] == "401") {
                abaVinculoComponent.addMessageError('Código retorno = ' + error.error["codigo-retorno"]);
                abaVinculoComponent.addMessageError(error.error["mensagem-retorno"]);
            } else if (error.status == 503) {
                abaVinculoComponent.addMessageError("Serviço temporariamente indisponível. Tente mais tarde.");
            } else {
                abaVinculoComponent.addMessageError(JSON.stringify(error));
            }
        });
    }

    /**
     * Monta a lista de sócios receita federal
     * @param response 
     * @param abaVinculoComponent 
     */
    private onSucessSociosReceitaFederal(response: any): Array<Socio> {
        const socios: Socio[] = new Array<Socio>();
        var sociosArr = response['socios'];
        for (var i in sociosArr) {
            socios.push(this.montarSocio(sociosArr[i]));
        }
        return socios
    }

    /**
     * Verifica se algum campo de sócio não foi montado corretamente
     * @param dataInputReceitaFederal 
     */
    private verificarErroMontagemCamposSocios(dataInputReceitaFederal: DataInputReceitaFederal) {
        if (this.abaVinculo.sociofieldsErrors.size > 0) {
            const msg: string = 'Os campos: ' + Array.from(this.abaVinculo.sociofieldsErrors).toString() + ' não foram montados corretamente, os mesmos podem comprometer a funcionalidade de sócios.';
            dataInputReceitaFederal.socioMountError = true;
            dataInputReceitaFederal.messageErrorMountSocio = msg;
        } else {
            dataInputReceitaFederal.socioMountError = false;
        }
    }

    /**
     * Monta o objeto socio para o componente modal receita federal
     * @param resposta 
     */
    private montarSocio(resposta: any): Socio {
        let socio = new Socio();
        if (resposta) {
            socio.nome_socio = resposta['nome'];
            socio.codigo_qualificacao = resposta['qualificacao'] ? resposta['qualificacao'].substring(0, 2) : undefined;
            socio.descricao_qualificacao = resposta['qualificacao'] ? resposta['qualificacao'].split('-')[1] : undefined;
            socio.cpf_cnpj = resposta['cpf-cnpj'];
            socio.pc_capital_social = resposta['pc-capital-social'];
            socio.data_inicio = resposta['data-entrada'];
            socio.cpf_representante = resposta['cpf-representante'];
            socio.nome_representante = resposta['nome-representante'];
            socio.vinculo_pendente = resposta['vinculo-pendente'];
        }
        this.initFieldErrorsSocios(resposta);
        return socio;
    }

    /**
     * Inicializa a lista de possiveis campos com erros de socios
     * @param resposta 
     */
    private initFieldErrorsSocios(resposta: any) {
        const fieldsErrors: Set<string> = new Set<string>();
        if (resposta['nome'] == undefined) {
            fieldsErrors.add(" nome do sócio");
        }
        if (resposta['qualificacao'] == undefined) {
            fieldsErrors.add(" qualificação do sócio");
        }
        if (resposta['cpf-cnpj'] == undefined) {
            fieldsErrors.add(" CPF/CNPJ sócio");
        }
        if (resposta['pc-capital-social'] == undefined) {
            fieldsErrors.add(" capital social do sócio");
        }
        if (resposta['data-entrada'] == undefined) {
            fieldsErrors.add(" data entrada sócio");
        }
        if (resposta['cpf-representante'] == undefined) {
            fieldsErrors.add(" CPF representante do sócio");
        }
        if (resposta['nome-representante'] == undefined) {
            fieldsErrors.add(" nome do representante do sócio");
        }
        if (resposta['vinculo-pendente'] == undefined) {
            fieldsErrors.add(" vínculo pendente do sócio");
        }
        this.abaVinculo.sociofieldsErrors = fieldsErrors;
    }

}