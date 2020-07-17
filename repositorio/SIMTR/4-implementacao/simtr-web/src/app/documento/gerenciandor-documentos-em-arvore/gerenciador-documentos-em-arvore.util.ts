import { VinculoArvore } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore";
import { DocumentImage } from "../documentImage";
import { TipoDocumentoArvoreGenerica } from "../../model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { NodeApresentacao } from "../../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-apresentacao.model";
import { ArvoreGenericaAbastract } from "../arvore-generica/arvore-generica-abstract";
import { ConteudoDocumentoArvore } from "../../model/conteudo-documento-arvore";
import { UtilsCliente } from "../../cliente/consulta-cliente/utils/utils-client";
import { Utils } from "../../utils/Utils";
import { VinculoArvoreProduto } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { TIPO_ARVORE, INDEX_ABA_DOCUMENTO_DOSSIE } from "../../constants/constants";
import { VinculoArvoreCliente } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { FuncaoDocumentalArvoreGenerica } from "../../model/model-arvore-generica-dossie-produto/funcao-documental-arvore-generica";
import { ElementoConteudo } from "../../model/elemento-conteudo";
import { Documento, DossieProduto, VinculoProcesso, VinculoCliente, VinculoProduto, VinculoGarantia } from "../../model";
import { DocumentoNovos } from "../../model/documento-novos";
import { Atributos } from "../../model/atributos";
import { VinculoArvoreProcesso } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo";
import { VinculoArvoreGarantia } from "../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { UtilsArvore } from "../arvore-generica/UtilsArvore";
import { NodeAbstract } from "src/app/model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-abstract.model";
import { DocumentoArvore } from "src/app/model/documento-arvore";

export class GerenciadorDocumentosEmArvore {
    private static listaVinculoArvore: Array<VinculoArvore>;

    public static setListaVinculoArvore(listaVinculoArvore: Array<VinculoArvore>, dossieCliente: boolean) {
        this.ordenarDocumentosPorDataCaptura(listaVinculoArvore, dossieCliente);
        this.listaVinculoArvore = listaVinculoArvore;
        this.carregaDocumentosSalvosNoBd(dossieCliente);
    }

    /**
     * Ordena os documentos conforme a data de capturação
     * @param listaVinculoArvore 
     * @param dossieCliente 
     */
    public static ordenarDocumentosPorDataCaptura(listaVinculoArvore: Array<VinculoArvore>, dossieCliente: boolean) {
        for (let i: number = 0; i < listaVinculoArvore.length; i++) {
            const vinculoProcesso: VinculoProcesso = (listaVinculoArvore[i] as VinculoArvoreProcesso).vinculoProcesso;
            const vinculoCliente: VinculoCliente = (listaVinculoArvore[i] as VinculoArvoreCliente).vinculoCliente;
            const vinculoProduto: VinculoProduto = (listaVinculoArvore[i] as VinculoArvoreProduto).vinculoProduto;
            const vinculoGarantia: VinculoGarantia = (listaVinculoArvore[i] as VinculoArvoreGarantia).vinculoGarantia;
            if (vinculoProcesso) {
                UtilsArvore.ordenaDocumentosPorDataHoraCapturaVinculoProcesso(vinculoProcesso);
            }
            if (vinculoCliente) {
                UtilsArvore.ordenaDocumentosPorDataHoraCapturaVinculoCliente(vinculoCliente, dossieCliente);
            }
            if (vinculoProduto) {
                UtilsArvore.ordenaDocumentosPorDataHoraCapturaVinculoProduto(vinculoProduto);
            }
            if (vinculoGarantia) {
                UtilsArvore.ordenaDocumentosPorDataHoraCapturaVinculoGarantia(vinculoGarantia);
            }
        }
    }

    /**
    * Método responsável por adicionar um documento em uma árvore
    * 
    * @param images lista que contém o conteúdo de um ou mais documentos
    * @param usuario login do usuario logado
    * @param indiceArvoreSelecionada índice da árvore selecionada na combo do modal de classificação
    * @param tipoDocumentoSelecionado tipo de documento selecionado na combo do modal de classificação
    */
    public static criaConteudoNodeByImage(images: DocumentImage[], usuario: string, indiceArvoreSelecionada: number,
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, tipoDocumentoSelecionadoAnteiror: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {

        let nosArvoreSelecionada: NodeApresentacao[] = GerenciadorDocumentosEmArvore.listaVinculoArvore[indiceArvoreSelecionada].noApresentacao;
        GerenciadorDocumentosEmArvore.removerNoArvoreQuandoForReclassificar(tipoDocumentoSelecionadoAnteiror);

        let noPaiEscolhido: NodeApresentacao = UtilsArvore.
            getNodeInTreeByTypeDocument(nosArvoreSelecionada, tipoDocumentoSelecionado) as NodeApresentacao;
        GerenciadorDocumentosEmArvore.criaNode(noPaiEscolhido, images, usuario, tipoDocumentoSelecionado, dossieCliente);
        GerenciadorDocumentosEmArvore.reaproveitaDocumentoInseridoParaArvoresComMesmoTipoDocumento(images,
            usuario, tipoDocumentoSelecionado, indiceArvoreSelecionada, dossieCliente);
    }

    /**
     *Cria instânscia de documento buscada no banco na arvore de documentos
     */
    public static criaConteudoNodeByImageExistente(docDb: any, usuario: string, indiceArvoreSelecionada: number, tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        let nosArvoreSelecionada: NodeApresentacao[] = GerenciadorDocumentosEmArvore.listaVinculoArvore[indiceArvoreSelecionada].noApresentacao;
        if (undefined != nosArvoreSelecionada) {
            let listNodesMesmoTipoEscolhidos: NodeAbstract[] = new Array<NodeAbstract>();
            UtilsArvore.getAllNodesSameTypeDocument(nosArvoreSelecionada, listNodesMesmoTipoEscolhidos,
                tipoDocumentoSelecionado);
            GerenciadorDocumentosEmArvore.addDocumentosExistentesEmNOs(listNodesMesmoTipoEscolhidos as NodeApresentacao[], docDb, usuario, tipoDocumentoSelecionado, dossieCliente);
        }
    }

    /**
     * Replica a instância do documento já existente para o mesmo tipo.
     * @param nosPastaEscolhidos 
     * @param docDb 
     * @param usuario 
     * @param tipoDocumentoSelecionado 
     */
    public static addDocumentosExistentesEmNOs(nosPastaEscolhidos: NodeApresentacao[], docDb: any, usuario: string,
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        nosPastaEscolhidos.forEach(noPaiEscolhido => {
            GerenciadorDocumentosEmArvore.criaNodeExisteDB(noPaiEscolhido, docDb, usuario, tipoDocumentoSelecionado, dossieCliente);
        });
    }

    /**
     * Método de verificação para excluir o no da arvore que esta sendo reclassificado
     * @param tipoDocumentoSelecionadoAnteiror No da arvore que esta sendo Retirado a imagem
     */
    private static removerNoArvoreQuandoForReclassificar(tipoDocumentoSelecionadoAnteiror: TipoDocumentoArvoreGenerica) {
        if (tipoDocumentoSelecionadoAnteiror != undefined) {
            let nosArvoreSelecionadaArvoreAnterior: NodeApresentacao[] = GerenciadorDocumentosEmArvore.
                listaVinculoArvore[tipoDocumentoSelecionadoAnteiror.indexArvore].noApresentacao;
            let noPaiClassificacaoDaArvoreAnterior: NodeApresentacao = UtilsArvore.getNodeInTreeByTypeDocument(nosArvoreSelecionadaArvoreAnterior, tipoDocumentoSelecionadoAnteiror) as NodeApresentacao;
            for (let noFilhoApresentacao of noPaiClassificacaoDaArvoreAnterior.children) {
                if (noFilhoApresentacao.partialSelected) {
                    UtilsArvore.removeNodeInTree(nosArvoreSelecionadaArvoreAnterior, noFilhoApresentacao, TIPO_ARVORE.ARVORE_DOSSIE_PRODUTO);
                }
            }
        }
    }

    /**
     * Método responsavel pro Classificar e Reclassificar quando existe uma lista de Arvore
     * @param images 
     * @param usuario 
     * @param listaIndiceArvoreSelecionada 
     * @param tipoDocumentoSelecionado 
     * @param tipoDocumentoSelecionadoAnteiror 
     */
    public static criarConteudoNodesByImage(images: DocumentImage[], usuario: string, listaIndiceArvoreSelecionada: number[],
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, tipoDocumentoSelecionadoAnteiror: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        listaIndiceArvoreSelecionada.forEach((arvoreSelecionada, idx) => {
            let arv: any = arvoreSelecionada;
            let index = (arv.index || arv.index == 0) ? arv.index : arvoreSelecionada;
            let nosArvoreSelecionada: NodeApresentacao[] = GerenciadorDocumentosEmArvore.listaVinculoArvore[index].noApresentacao;
            if (idx === 0) {
                GerenciadorDocumentosEmArvore.removerNoArvoreQuandoForReclassificar(tipoDocumentoSelecionadoAnteiror);
                let listNodesMesmoTipoEscolhidos: NodeAbstract[] = new Array<NodeAbstract>();
                UtilsArvore.getAllNodesSameTypeDocument(nosArvoreSelecionada, listNodesMesmoTipoEscolhidos,
                    tipoDocumentoSelecionado);
                GerenciadorDocumentosEmArvore.addDocumentosEmNOs(listNodesMesmoTipoEscolhidos as NodeApresentacao[], images, usuario, tipoDocumentoSelecionado, dossieCliente);
            }
            if (listaIndiceArvoreSelecionada.length > 1) {
                GerenciadorDocumentosEmArvore.reaproveitaDocumentoInseridoParaArvoresComMesmoTipoDocumento(images,
                    usuario, tipoDocumentoSelecionado, index, dossieCliente);
            }
        });
    }

    public static addDocumentosEmNOs(nosPastaEscolhidos: NodeApresentacao[], images: DocumentImage[], usuario: string,
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        nosPastaEscolhidos.forEach(noPaiEscolhido => {
            GerenciadorDocumentosEmArvore.criaNode(noPaiEscolhido, images, usuario, tipoDocumentoSelecionado, dossieCliente);
        });
    }

    public static carregaDocumentosSalvosNoBd(dossieCliente: boolean) {
        this.listaVinculoArvore.forEach((vinculoArvore, idx) => {
            if (undefined != vinculoArvore.noApresentacao && vinculoArvore.noApresentacao.length > 0) {
                if (vinculoArvore instanceof VinculoArvoreCliente) {
                    this.verificarExisteInstanciaDocumento(vinculoArvore.vinculoCliente, idx, dossieCliente);
                }
                if (vinculoArvore instanceof VinculoArvoreProduto) {
                    this.verificarExisteInstanciaDocumento(vinculoArvore.vinculoProduto, idx, dossieCliente);
                }
                if (vinculoArvore instanceof VinculoArvoreProcesso) {
                    this.verificarExisteInstanciaDocumento(vinculoArvore.vinculoProcesso, idx, dossieCliente);
                }
                if (vinculoArvore instanceof VinculoArvoreGarantia) {
                    this.verificarExisteInstanciaDocumento(vinculoArvore.vinculoGarantia, idx, dossieCliente);
                }
            }
        });
        this.listaVinculoArvore = Object.assign([], this.listaVinculoArvore);
    }

    /**
     * Se existe instancias de documento setar na pasta o documento
     * @param arvore tipo de arvore a ser verificada (Cliente, Produto, Processo ou Garantia)
     */
    public static verificarExisteInstanciaDocumento(vinculo: any, idx: any, dossieCliente: boolean) {
        if (undefined != vinculo.instancias_documento && vinculo.instancias_documento.length > 0) {
            vinculo.instancias_documento.forEach(instanciaDoc => {
                let documentoDb = instanciaDoc.documento;
                documentoDb.situacaoAtual = instanciaDoc.situacao_atual;
                documentoDb.tipo_documento.idNodeApresentacao = documentoDb.tipo_documento.id;
                this.criaConteudoNodeByImageExistente(documentoDb, documentoDb.matricula_captura, idx,
                    documentoDb.tipo_documento, dossieCliente);
            });
            vinculo.instancias_documento = undefined;
        }
    }

    /**
     * Cria o NO folha de visualização do documento classificado.
     * @param noPaiEscolhido No da árvore que guardará o documento que foi classificado
     * @param images lista que contém o conteúdo de um ou mais documentos
     * @param usuario matrícula do usuário que realizou o upload do documento
     * @param tipoDocumentoSelecionado contém o tipo do documento. Ex: CNH, RG, CONTRACHEQUE
     */
    private static criaNode(noPaiEscolhido: NodeApresentacao, images: DocumentImage[], usuario: string,
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        let noApresentacaoConteudo = new NodeApresentacao(UtilsCliente.getDataCompleta("S") + " - " + usuario);
        let arrayDocumentos: ConteudoDocumentoArvore[] = [];
        let envelopeDocumento: DocumentoArvore = ArvoreGenericaAbastract.criaEnvelopeDocumento(usuario, tipoDocumentoSelecionado);
        let documento: ConteudoDocumentoArvore = ArvoreGenericaAbastract.criaDocumentoAproveitandoIdentificadorImagem(usuario, images)
        noApresentacaoConteudo.id = tipoDocumentoSelecionado.idNodeApresentacao;
        noApresentacaoConteudo.leaf = true;
        arrayDocumentos.push(documento);

        noApresentacaoConteudo.pages = ArvoreGenericaAbastract.percorreArrayECriaDocumentos(arrayDocumentos, envelopeDocumento);
        noApresentacaoConteudo.uri = ArvoreGenericaAbastract.getAllUri(noApresentacaoConteudo.pages);
        ArvoreGenericaAbastract.setarParent(noApresentacaoConteudo);
        ArvoreGenericaAbastract.setarTipoSituacaoDocumento(noApresentacaoConteudo, envelopeDocumento, dossieCliente);
        noPaiEscolhido.children.unshift(noApresentacaoConteudo);
        noPaiEscolhido.existeDocumentoReuso = false;
        noApresentacaoConteudo.parent = noPaiEscolhido;
        ArvoreGenericaAbastract.showNode(noApresentacaoConteudo);
        ArvoreGenericaAbastract.showNode(noPaiEscolhido);
        ArvoreGenericaAbastract.showNode(noPaiEscolhido.parent);
    }

    public static criaNodeExisteDB(noPaiEscolhido: NodeApresentacao, docDb: any, usuario: string, tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, dossieCliente: boolean) {
        if (noPaiEscolhido) {
            let noApresentacaoConteudo = new NodeApresentacao(docDb.data_hora_captura + " - " + usuario);
            let arrayDocumentos: ConteudoDocumentoArvore[] = [];
            let envelopeDocumento: DocumentoArvore = ArvoreGenericaAbastract.criaEnvelopeDocumentoExiste(docDb);
            let documento: ConteudoDocumentoArvore = ArvoreGenericaAbastract.criaDocumentoAproveitandoIdentificadorImagemDB(docDb)
            noApresentacaoConteudo.id = tipoDocumentoSelecionado.idNodeApresentacao;
            noApresentacaoConteudo.leaf = true;
            arrayDocumentos.push(documento);

            noApresentacaoConteudo.pages = ArvoreGenericaAbastract.percorreArrayECriaDocumentos(arrayDocumentos, envelopeDocumento);
            noApresentacaoConteudo.uri = ArvoreGenericaAbastract.getAllUri(noApresentacaoConteudo.pages);
            ArvoreGenericaAbastract.setarParent(noApresentacaoConteudo);
            ArvoreGenericaAbastract.defineStatusSituacaoDocuemntoBase(noApresentacaoConteudo, envelopeDocumento, dossieCliente);
            noPaiEscolhido.children.unshift(noApresentacaoConteudo);
            noApresentacaoConteudo.parent = noPaiEscolhido;
            if(!UtilsArvore.isDocumentoStatusNegativo(noApresentacaoConteudo)){
                ArvoreGenericaAbastract.showNode(noApresentacaoConteudo);
                ArvoreGenericaAbastract.showNode(noPaiEscolhido);
                ArvoreGenericaAbastract.showNode(noPaiEscolhido.parent);
            }
            noPaiEscolhido.children.sort(Utils.ordenarDocumentosNodeByDataHora);
        }
    }

    /**
     * Reaproveita documentos em ávores do tipo produto em que o tipo de documento requerido é 
     * igual ao documento que foi inserido pelo usuário.
     * @param images lista que contém o conteúdo de um ou mais documentos
     * @param usuario matrícula do usuário que realizou o upload do documento
     * @param tipoDocumentoSelecionado contém o tipo do documento. Ex: CNH, RG, CONTRACHEQUE
     * @param indiceArvoreSelecionada índice da árvore selecionada na combo do modal de classificação
     */
    private static reaproveitaDocumentoInseridoParaArvoresComMesmoTipoDocumento(images: DocumentImage[], usuario: string,
        tipoDocumentoSelecionado: TipoDocumentoArvoreGenerica, indiceArvoreSelecionada: number, dossieCliente: boolean) {
        GerenciadorDocumentosEmArvore.listaVinculoArvore.forEach((vinculoArvore, indexVinculoArvore) => {
            if ((vinculoArvore instanceof VinculoArvoreProduto || vinculoArvore instanceof VinculoArvoreCliente) && indiceArvoreSelecionada != indexVinculoArvore) {
                let noPaiEscolhido: NodeApresentacao = UtilsArvore.getNodeInTreeByTypeDocument(vinculoArvore.noApresentacao,
                    tipoDocumentoSelecionado) as NodeApresentacao;
                if (noPaiEscolhido !== undefined) {
                    GerenciadorDocumentosEmArvore.criaNode(noPaiEscolhido, images, usuario, tipoDocumentoSelecionado, dossieCliente);
                }
            }
        });
    }

    /**
    * Monta arvore de dossie cliente com documentos vindo da base de dados
    * @param vinculoArvoreCliente objeto de dossie cliente
    */
    public static montaArvoreDocumentosExistenteDossie(vinculoArvoreCliente: VinculoArvoreCliente,
        listaNosVisualizacao: NodeApresentacao[], dossieCliente: boolean) {
        let docs: DocumentoArvore[] = vinculoArvoreCliente.vinculoCliente.documentos;
        let tipoDocumentos: TipoDocumentoArvoreGenerica[] = ArvoreGenericaAbastract.reorganizaPorListaTipoDocumento(docs);
        let funcoesDocumentais: FuncaoDocumentalArvoreGenerica[] = ArvoreGenericaAbastract.reorganizaPorListaFuncaoDocumental(docs);
        this.recriaNoTipoDocumentoSemFuncaoDocumental(tipoDocumentos, listaNosVisualizacao, dossieCliente);
        this.recriaNoFuncoesDocumentais(funcoesDocumentais, listaNosVisualizacao, dossieCliente);
    }

    /**
     * Percorre as funcoes documentais proveniente da base de dados e recria os Nos das mesmas
     * @param funcoesDocumentais funcões documentas com seus tipos de documentos
     * @param listaNosVisualizacao lista de Nos arvore de documentos
     */
    private static recriaNoFuncoesDocumentais(funcoesDocumentais: FuncaoDocumentalArvoreGenerica[],
        listaNosVisualizacao: NodeApresentacao[], dossieCliente: boolean) {
        let funcaoDocumental: FuncaoDocumentalArvoreGenerica = new FuncaoDocumentalArvoreGenerica();
        for (const funcao of funcoesDocumentais) {
            funcaoDocumental = new FuncaoDocumentalArvoreGenerica();
            funcaoDocumental.id = funcao.id;
            funcaoDocumental.nome = funcao.nome;
            let noApresentacaoPai: NodeApresentacao = ArvoreGenericaAbastract.getNodeApresentacao(funcao.nome, listaNosVisualizacao);

            if (noApresentacaoPai == null) {
                noApresentacaoPai = ArvoreGenericaAbastract.criaNo(funcao);
                noApresentacaoPai.id = ArvoreGenericaAbastract.contadorNode++;
            }

            if (funcao.tipos_documento === null) {
                funcao.tipos_documento = [];
            }
            this.recriaNoTipoDocumentos(funcao, noApresentacaoPai, dossieCliente);
            listaNosVisualizacao.push(noApresentacaoPai);
        }
    }

    /**
     * Percorre os tipos de documentos proveniente da base de dados e recria os Nos dos mesmos
     * @param funcao funcao documental com seus tipos de documentos
     * @param noApresentacaoPai no que representa a funcao documental
     * @param listaNosVisualizacao lista de Nos arvore de documentos
     */
    private static recriaNoTipoDocumentos(funcao: FuncaoDocumentalArvoreGenerica,
        noApresentacaoPai: NodeApresentacao, dossieCliente: boolean) {
        for (const tipo of funcao.tipos_documento) {
            noApresentacaoPai.expanded = true;
            let noApresentacaoTipo: NodeApresentacao = ArvoreGenericaAbastract.getNodeApresentacao(tipo.nome, noApresentacaoPai.children);
            if (noApresentacaoTipo === null) {
                noApresentacaoTipo = ArvoreGenericaAbastract.criaNo(tipo);
            }
            this.recriaConteudoDocumentosDoNo(tipo, noApresentacaoTipo, dossieCliente);
            noApresentacaoPai.children.push(noApresentacaoTipo);
        }
    }

    /**
     * 
     * @param tipo 
     * @param listaNosVisualizacao 
     */
    private static recriaNoTipoDocumentoSemFuncaoDocumental(tiposDocumentos: TipoDocumentoArvoreGenerica[],
        listaNosVisualizacao: NodeApresentacao[], dossieCliente: boolean) {
        for (let tipo of tiposDocumentos) {
            let noApresentacaoTipo: NodeApresentacao = ArvoreGenericaAbastract.getNodeApresentacao(tipo.nome, listaNosVisualizacao);
            if (noApresentacaoTipo === null) {
                noApresentacaoTipo = ArvoreGenericaAbastract.criaNo(tipo);
            }
            this.recriaConteudoDocumentosDoNo(tipo, noApresentacaoTipo, dossieCliente);
            listaNosVisualizacao.push(noApresentacaoTipo);
        }
    }

    /**
     * Percorre os documentos proveniente da base de dados e recria os Nos dos mesmos
     * @param tipo tipo de documento da funcao documental
     * @param noApresentacaoTipo No que representa o tipo de documento
     */
    private static recriaConteudoDocumentosDoNo(tipo: TipoDocumentoArvoreGenerica, noApresentacaoTipo: NodeApresentacao, dossieCliente: boolean) {
        if (tipo.documentos) {
            for (const envelopeDocumento of tipo.documentos) {
                envelopeDocumento.conteudoDocumentos = [];
                const noApresentacaoConteudo = new NodeApresentacao(envelopeDocumento.data_hora_captura + " - " +
                    envelopeDocumento.matricula_captura);
                envelopeDocumento.conteudoDocumentos.push(ArvoreGenericaAbastract.populaConteudoDocumento(envelopeDocumento));
                noApresentacaoConteudo.pages = ArvoreGenericaAbastract.percorreArrayECriaDocumentos(envelopeDocumento.conteudoDocumentos, envelopeDocumento);
                noApresentacaoConteudo.uri = ArvoreGenericaAbastract.getAllUri(noApresentacaoConteudo.pages);
                noApresentacaoConteudo.leaf = true;
                ArvoreGenericaAbastract.setarParent(noApresentacaoConteudo);
                ArvoreGenericaAbastract.defineStatusSituacaoDocuemntoBase(noApresentacaoConteudo, envelopeDocumento, dossieCliente);
                noApresentacaoTipo.children.unshift(noApresentacaoConteudo);
                noApresentacaoConteudo.parent = noApresentacaoTipo;
                if(!UtilsArvore.isDocumentoStatusNegativo(noApresentacaoConteudo)){
                    ArvoreGenericaAbastract.showNode(noApresentacaoConteudo);
                }
            }
        }
    }

    /**
    *  Inicializar Validacação Vinculo Arvore Processo
    * @param arvore 
    * @param dossieProduto 
    */
    public static inicializarValidacao(arvore: VinculoArvoreProcesso, dossieProduto: DossieProduto) {
        arvore.classeValidacao = "";
        dossieProduto.descricaoDocumento = "";
    }

    /**
   * Validar Campos Obrigatorio Global
   * @param no 
   * @param processo 
   * @param newProduto 
   * @param aba 
   * @param documentoObrigatorio 
   */
    public static validarCampoObrigatorio(no, dossieProduto: DossieProduto, aba: number, documentoObrigatorio: string, arvore: any) {
        let qtdDocumento = this.verificarQtdObrigatoria(no, 0);
        if (!dossieProduto.rascunho && qtdDocumento < no.quantidade_obrigatorio) {
            dossieProduto.camposValidado = false;
            dossieProduto.indexAba = aba;
        }

        if (!dossieProduto.rascunho && undefined != documentoObrigatorio && documentoObrigatorio != "") {
            dossieProduto.camposValidado = false;
            dossieProduto.indexAba = aba;
            dossieProduto.descricaoDocumento = documentoObrigatorio;
            arvore.classeValidacao = "campoObrigatorioArvore";
            let msg;
            if (arvore instanceof VinculoArvoreCliente) {
                msg = {
                    "msg": "Na árvore " + arvore.vinculoCliente.ic_tipo_relacionamento + " - " + arvore.vinculoCliente.nome + ": " + (arvore.vinculoCliente.cnpj ? Utils.masKcpfCnpj(arvore.vinculoCliente.cnpj) : Utils.masKcpfCnpj(arvore.vinculoCliente.cpf)) + " é obrigatório o documento : " + documentoObrigatorio
                }
            } else if (arvore instanceof VinculoArvoreProcesso) {
                msg = {
                    "msg": "Na árvore " + arvore.nome + ": " + " é obrigatório o documento : " + documentoObrigatorio
                }
            } else if (arvore instanceof VinculoArvoreProduto) {
                msg = {
                    "msg": "Na árvore " + arvore.vinculoProduto.nome + ": " + " é obrigatório o documento : " + documentoObrigatorio
                }
            } else if(arvore.numero_contrato || arvore.periodo_juros || arvore.prazo || arvore.prazo_carencia || arvore.prazo_contrato || arvore.taxa_juros  || arvore.valor_contrato){
                msg = {
                    "msg": "Na árvore " + arvore.nome + ": " + " é obrigatório o documento : " + documentoObrigatorio
                }  
            }else if (arvore instanceof VinculoArvoreGarantia) {
                msg = {
                    "msg": "Na árvore " + arvore.nome + ": " + " é obrigatório o documento : " + documentoObrigatorio
                }
            }
            if (msg) {
                dossieProduto.listaDocumentosFalta.push(msg);
            }
            documentoObrigatorio = "";
        }
    }

    /**
     * 
     * @param validar 
     * @param pastaVazia 
     * @param elementoOuDocNovosOuDocUtilizados 
     * @param noInterno 
     * @param documentoObrigatorio 
     */
    public static informDocumentoObrigatorio(validar: boolean, pastaVazia: boolean, elementoOuDocNovosOuDocUtilizados: any, noInterno: NodeApresentacao, documentoObrigatorio: any) {
        if (!validar && pastaVazia && undefined == elementoOuDocNovosOuDocUtilizados && (noInterno.obrigatorio || UtilsArvore.isDocumentoStatusNegativo(noInterno) && !UtilsArvore.existeAlgumDocumentoStatusPositivo(noInterno.parent))) {
            pastaVazia = false;
            if(UtilsArvore.isDocumentoStatusNegativo(noInterno)){
                documentoObrigatorio = noInterno.parent.label;
            }else{
                documentoObrigatorio = noInterno.label;
            }
        }
        return documentoObrigatorio;
    }

    /**
     * 
     * @param no 
     * @param qtdDocumento 
     */
    private static verificarQtdObrigatoria(no: any, qtdDocumento: number) {
        let qtdDoc = qtdDocumento;
        no.children.forEach(noInterno => {
            if (undefined != noInterno.children) {
                qtdDoc = this.verificarQtdObrigatoria(noInterno, qtdDoc);
            } else if(!UtilsArvore.isDocumentoStatusNegativo(noInterno)){
                qtdDoc += noInterno.uri.length;
            }
        });
        return qtdDoc;
    }

    /**
     * Validar Qtd Obrigatoria de Documentos
     * @param no 
     * @param processo 
     * @param documentoObrigatorio 
     * @param dossieProduto 
     */
    public static validarQtdObrigatorioDocumentos(no, processo: any, documentoObrigatorio: any, dossieProduto: DossieProduto, arvore: any) {
        let qtdDocumentoNoAtual = this.verificarQtdObrigatoria(no, 0);
        if (!dossieProduto.rascunho && no.obrigatorio && undefined != processo.elementos_conteudos && no.quantidade_obrigatorio > qtdDocumentoNoAtual && (undefined == documentoObrigatorio || documentoObrigatorio == "")) {
            dossieProduto.camposValidado = false;
            dossieProduto.nCamposObrigatoirio = false;
            dossieProduto.descricaoDocumento = "É necessário informar no mínimo " + no.quantidade_obrigatorio + " documentos. Para " + no.label + " na árvore " + arvore.nome;
            arvore.classeValidacao = "campoObrigatorioArvore";
            let msg = {
                "msg": dossieProduto.descricaoDocumento
            }
            dossieProduto.listaDocumentosFalta.push(msg)
        }
    }

    /**
    * Metodo responsavel pra da enfase no arvore que falta documento
    * @param arvore 
    * @param newProduto 
    */
    public static destacarArvoreComCampoObrigatorio(arvore: any, dossieProduto: DossieProduto) {
        if (!dossieProduto.camposValidado) {
            dossieProduto.camposValidado = false;
            if (!dossieProduto.nCamposObrigatoirio) {
                this.descricaoMsgERROCampoObrigatorio(dossieProduto, arvore);
            }
        }
    }

    /**
    * Definindo Descrição da arvore e do documento que falta
    * @param newProduto 
    * @param arvore 
    */
    private static descricaoMsgERROCampoObrigatorio(newProduto: DossieProduto, arvore: any) {
        let descDoc = newProduto.descricaoDocumento;
        if (descDoc != "") {
            newProduto.descricaoDocumento = "";
            newProduto.lstDescricaoDoc.forEach(desc => {
                newProduto.descricaoDocumento = "Na árvore " + desc.arvore + " é obrigatório o documento : " + desc.documento;

                let msg = {
                    "msg": newProduto.descricaoDocumento
                }
                newProduto.listaDocumentosFalta.push(msg)
            });
        } else {
            arvore.classeValidacao = ""
        }
    }

    /**
  * Verificação se exsite documento de imagem
  * @param no 
  */
    public static verificarExistenciaDeUri(no: any) {
        let qtdUri = 0;
        if (no.children.length > 0) {
            no.children.forEach(noChildren => {
                if (undefined != noChildren.children) {
                    noChildren.children.forEach(lastNo => {
                        if (lastNo.uri.length > 0) {
                            qtdUri = qtdUri + 1;
                        }
                    });
                }

                if (undefined == noChildren.children) {
                    qtdUri = qtdUri + noChildren.uri.length;
                }

            });
        }
        return qtdUri;
    }

    /**
     * 
     * @param arvore 
     */
    private static encontrarTituloArvore(arvore: any) {
        let label;
        if (arvore instanceof VinculoArvoreCliente) {
            label = arvore.vinculoCliente.tipo_relacionamento.nome;

        }
        if (arvore instanceof VinculoArvoreGarantia) {
            label = arvore.vinculoGarantia.nome_garantia;
        }
        if (arvore instanceof VinculoArvoreProduto) {
            label = arvore.vinculoProduto.nome;
        }
        if (arvore instanceof VinculoArvoreProcesso) {
            label = arvore.nome;
        }
        return label;
    }

    /**
     * Definir Ações Quando campo Obrigatorio
     * @param newProduto 
     * @param processo 
     */
    public static definirAcoesQuandoCampoObrigatorio(dossieProduto: DossieProduto, processo: any) {
        dossieProduto.camposValidado = false;
        dossieProduto.nCamposObrigatoirio = true;
        dossieProduto.indexAba = INDEX_ABA_DOCUMENTO_DOSSIE.ABA_DOCUMENTO;
        processo.classeValidacao = "campoObrigatorioArvore";
    }

    /**
     * metodo para definir os conteudos do elmento
     * @param noInterno ultimo no, agora e seta as informações
     */
    public static montarDocumentoNovos(noInterno: any) {
        if (undefined != noInterno.uri && undefined == noInterno.uri[0].id && undefined != noInterno.uri[0].image && undefined != noInterno.uri[0].image) {
            let documentos_novos = new DocumentoNovos();
            documentos_novos.conteudos = [];
            documentos_novos.origem_documento = "S";
            documentos_novos.tipo_documento = noInterno.id;
            documentos_novos.atributos = [];
            if (undefined != noInterno.atributos) {
                for (let atributo of noInterno.atributos) {
                    let atribu = GerenciadorDocumentosEmArvore.montarAtributo(atributo);
                    documentos_novos.atributos.push(atribu);
                }
            }
            for (let img of noInterno.uri) {
                documentos_novos.mime_type = img.type;
                documentos_novos.indiceDocListPdfOriginal = img.indiceDocListPdfOriginal;
                documentos_novos.conteudos.push({
                    img: img.image,
                    altura: img.altura,
                    largura: img.largura,
                    source: img.source
                });
            }
            //mecher aqui
            return documentos_novos;
        }
    }

    /**
     * Seleciona os ids dos documentos reutilzados.
     * @param noInterno documento reutilzado
     */
    public static selecionaDocumentoReutilizado(noInterno: any): number {
        if (undefined != noInterno.uri && undefined == noInterno.uri[0].image && noInterno.pages[0].data.docReutilizado) {
            let idDocumentoUtilizado: number = noInterno.pages[0].id;
            return idDocumentoUtilizado;
        }
    }

    /**
     * 
     * @param noInterno 
     */
    public static montarElementosConteudos(noInterno: any) {
        if (undefined != noInterno.uri && undefined == noInterno.uri[0].id && !UtilsArvore.isDocumentoStatusNegativo(noInterno) && undefined != noInterno.uri[0].image && noInterno.uri.length > 0) {
            let elementoConteudo = new ElementoConteudo;
            elementoConteudo.identificador_elemento = noInterno.parent.identificador_elemento;
            elementoConteudo.documento = new Documento();
            elementoConteudo.documento.conteudos = [];
            elementoConteudo.documento.tipo_documento = noInterno.id;
            elementoConteudo.documento.origem_documento = "S";
            elementoConteudo.documento.atributos = [];
            if (undefined != noInterno.atributos) {
                for (let atributo of noInterno.atributos) {
                    let atribu = GerenciadorDocumentosEmArvore.montarAtributo(atributo);
                    elementoConteudo.documento.atributos.push(atribu);
                }
            }
            for (let img of noInterno.uri) {
                elementoConteudo.documento.mime_type = img.type;
                elementoConteudo.documento.indiceDocListPdfOriginal = img.indiceDocListPdfOriginal;
                elementoConteudo.documento.conteudos.push({
                    img: img.image,
                    altura: img.altura,
                    largura: img.largura,
                    source: img.source 
                });
            }
            return elementoConteudo;
        }
    }
    private static montarAtributo(atributo: Atributos) {
        let atribu = new Atributos();
        atribu.chave = atributo.chave ? atributo.chave : "";
        atribu.valor = atributo.valor ? atributo.valor : "";
        GerenciadorDocumentosEmArvore.atribuirOpcoesSelecionadas(atributo, atribu);
        return atribu;
    }

    private static atribuirOpcoesSelecionadas(atributo: Atributos, atribu: Atributos) {
        if (atributo.opcoes_selecionadas && atributo.opcoes_selecionadas.length > 0) {
            atribu.opcoes_selecionadas = [];
            atributo.opcoes_selecionadas.forEach(atrb => {
                atribu.opcoes_selecionadas.push(atrb);
            });
        }
    }

    /**
     * 
     * @param noInterno 
     */
    public static montarDocumento(noInterno: any) {
        if (undefined != noInterno.uri && undefined == noInterno.uri[0].id && undefined != noInterno.uri[0].image && noInterno.uri.length > 0) {
            let documento = new Documento();
            documento.tipo_documento = noInterno.id;
            documento.origem_documento = "S";
            documento.atributos = [];
            documento.conteudos = [];
            if (undefined != noInterno.atributos) {
                for (let atributo of noInterno.atributos) {
                    let atribu = GerenciadorDocumentosEmArvore.montarAtributo(atributo);
                    documento.atributos.push(atribu);
                }
            }
            for (let img of noInterno.uri) {
                documento.mime_type = img.type;
                documento.indiceDocListPdfOriginal = img.indiceDocListPdfOriginal;
                documento.conteudos.push({
                    img: img.image,
                    altura: img.altura,
                    largura: img.largura,
                    source: img.source
                });
            }
            return documento;
        }
    }
}