import { Component, ViewEncapsulation, Input, EventEmitter, Output, OnInit, OnChanges } from '@angular/core';
import { Checklist } from 'src/app/model/checklist';
import { SingleExpand } from 'src/app/model/model-tratamento/singleExpand';
import { SWITCH_TRATMENTO } from 'src/app/constants/constants';

@Component({
    selector: 'visualizador-arvore-documentos',
    templateUrl: './visualizador-arvore-documentos.component.html',
    styleUrls: ['./visualizador-arvore-documentos.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class VisualizadorArvoreDocumentos{
    listDocumentosImagens: any[];
    @Input() elementosConteudoProcessoDossieOuFase: any[];
    @Input() processoDossieOuFase: any;
    @Input() cliente: any;
    @Input() documentosVinculoCliente: any;
    @Input() produto: any;
    @Input() documentosProdutosVinculados: any;
    @Input() garantia: any;
    @Input() documentosGarantiasVinculadas: any;
    @Input() listaChekList: Checklist[];
    @Input() listaDocumentoObrigatorio: Checklist[];
    @Input() isMudancaSalva: boolean;
    @Input() singleExpandAllDocuments: boolean;
    @Input() singleExpandAllDocumentsDossieFase: boolean;
    @Input() singleExpandAllDocumentsCliente: boolean;
    @Input() singleExpandAllDocumentsProduto: boolean;
    @Input() singleExpandAllDocumentsGarantias: boolean;
    @Input() idDoPrimeiroInstanciaDocumentoASerCarregado: number;
    @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
    @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();

    /**
     * Conforme vai percorrendo a lista para montar o menu, apos clicar no botão validar entra em destaque o primeiro/Ultimo que falta
     * @param listaDocumentoObrigatorio 
     * @param processoDossieOuFase 
     * @param cliente 
     * @param produto 
     * @param garantia 
     */
    verificarArvore(listaDocumentoObrigatorio: any[], processoDossieOuFase: any, cliente: any, produto: any, garantia: any) {

        if (listaDocumentoObrigatorio && listaDocumentoObrigatorio.length > 0) {
            if (processoDossieOuFase) {
                return listaDocumentoObrigatorio.some(x => x.idVinculo == processoDossieOuFase.id && !x.listaResposta || (x.listaResposta && x.listaResposta.some(resposta => undefined == resposta.verificacaoAprovada)));
            }

            if (produto) {
                return listaDocumentoObrigatorio.some(x => x.idVinculo == produto.id && !x.listaResposta || (x.listaResposta && x.listaResposta.some(resposta => undefined == resposta.verificacaoAprovada)));
            }

            if (garantia) {
                return listaDocumentoObrigatorio.some(x => x.idVinculo == garantia.id && !x.listaResposta || (x.listaResposta && x.listaResposta.some(resposta => undefined == resposta.verificacaoAprovada)));
            }

            if (cliente) {
                return listaDocumentoObrigatorio.some(x => x.idVinculo == cliente.dossie_cliente.id && !x.listaResposta || (x.listaResposta && x.listaResposta.some(resposta => undefined == resposta.verificacaoAprovada)));
            }
        }
    }

    indentificarClienteRelacionado(listaClienteRelacionado: any[]) {
        let listaIdentifica = "";
        listaClienteRelacionado.forEach(cliente => {
            listaIdentifica = listaIdentifica == "" ? cliente.nome : listaIdentifica + ", " + cliente.nome;
        });

        return listaIdentifica;
    }

    handleChangeListDocumentosImagem(input) {
        this.listDocumentosImagemChanged.emit(Object.assign([], input));
    }

    handleChangeListaChekList(input) {
        this.listaCheklistChanged.emit(Object.assign([], input));
    }

    handleChangeExpandSingleAll(singleExpand: SingleExpand) {
        if (singleExpand.tipoArvore.indexOf(SWITCH_TRATMENTO.DOSSIE_FASE) == 0) {
            this.singleExpandAllDocumentsDossieFase = singleExpand.toogleValue;
        } else if (singleExpand.tipoArvore.indexOf(SWITCH_TRATMENTO.CLIENTE) == 0) {
            this.singleExpandAllDocumentsCliente = singleExpand.toogleValue;
        } else if (singleExpand.tipoArvore.indexOf(SWITCH_TRATMENTO.PRODUTO) == 0) {
            this.singleExpandAllDocumentsProduto = singleExpand.toogleValue;
        } else {
            this.singleExpandAllDocumentsGarantias = singleExpand.toogleValue;
        }
    }

    showToogleProcessoDossieOuFase() {
        let showToogle: boolean = false;
        for (let elementoConteudo of this.elementosConteudoProcessoDossieOuFase) {
            if (this.processoDossieOuFase.instancias_documento !== undefined) {
                showToogle = this.verificaInexistenciaDocumentoProcessoDossieOuFase(elementoConteudo, this.processoDossieOuFase.instancias_documento);
                if (showToogle) {
                    break;
                }
            }
        }
        return showToogle;
    }

    /**
     * Verifica se um elemento conteudo não possui um tipo documento na lista de instanciasProcessoDossieOuFase
     * @param elementoConteudo 
     * @param instanciasProcessoDossieOuFase 
     */
    verificaInexistenciaDocumentoProcessoDossieOuFase(elementoConteudo: any, instanciasProcessoDossieOuFase: any): boolean {
        let naoPossuiInstancia: boolean = false;
        if (elementoConteudo.tipo_documento !== null) {
            const instanciaDocumento = instanciasProcessoDossieOuFase.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == elementoConteudo.tipo_documento.id);
            if (!instanciaDocumento) {
                naoPossuiInstancia = true;
            }
        }
        return naoPossuiInstancia;
    }

    showToogleCliente(): boolean {
        let showToogle: boolean = false;
        for (let documentoVinculoCliente of this.documentosVinculoCliente) {
            if (this.cliente.tipo_relacionamento && this.cliente.tipo_relacionamento.id == documentoVinculoCliente.tipo_relacionamento) {
                if (documentoVinculoCliente.funcao_documental && this.cliente.instancias_documento) {
                    if (this.verificaInexistenciaDocumentoClienteProduto(documentoVinculoCliente.funcao_documental.tipos_documento, true)) {
                        showToogle = true;
                        break;
                    }
                }
                if (documentoVinculoCliente.tipo_documento && this.cliente.instancias_documentos) {
                    if (!this.cliente.instancias_documentos.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == documentoVinculoCliente.tipo_documento.id)) {
                        showToogle = true;
                        break;
                    }
                }
            }
        }
        return showToogle;
    }

    showToogleProduto(): boolean {
        let showToogle: boolean = false;
        for (let documentoProdutoVinculado of this.documentosProdutosVinculados) {
            for (let elementoConteudo of documentoProdutoVinculado.elementos_conteudo) {
                for (let instanciaDocumento of this.produto.instancias_documento) {
                    let instancia: boolean = elementoConteudo.identificador_elemento == instanciaDocumento.id_elemento_conteudo;
                    if (!instancia) {
                        showToogle = true;
                        break;
                    }

                }
            }
        }
        return showToogle;
    }

    verificaInexistenciaDocumentoClienteProduto(tiposDocumentos: any, isCliente: boolean): boolean {
        let naoPossuiInstancia: boolean = false;
        for (let tipoDocumento of tiposDocumentos) {
            if (isCliente) {
                const instanciaDocumento = this.cliente.instancias_documento.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == tipoDocumento.id);
                if (!instanciaDocumento) {
                    naoPossuiInstancia = true;
                    break;
                }
            } else {
                const instanciaDocumento = this.produto.instancias_documento.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == tipoDocumento.id);
                if (!instanciaDocumento) {
                    naoPossuiInstancia = true;
                    break;
                }
            }
        }
        return naoPossuiInstancia;
    }

    showToogleGarantia(): boolean {
        let showToogle: boolean = false;
        let documentoGarantiaVinculada: any;
        for (let documentoGarantiaVinculada of this.documentosGarantiasVinculadas) {
            if (!this.garantia.produto && documentoGarantiaVinculada.id == this.garantia.garantia) {
                documentoGarantiaVinculada = true;
                break;
            }

        }
        if (documentoGarantiaVinculada) {
            showToogle = this.verificaInexistenciaDocumentoGarantiaProcesso();
        } else {
            showToogle = this.verificaInexistenciaDocumentoGarantiaProduto();
        }
        return showToogle;
    }

    verificaInexistenciaDocumentoGarantiaProcesso(): boolean {
        let naoPossuiInstancia: boolean = false;
        for (let documentoGarantiaVinculada of this.documentosGarantiasVinculadas) {
            for (let tipoDocumento of documentoGarantiaVinculada.tipos_documento) {
                const instanciaDocumento = this.garantia.instancias_documento.find(instanciaDocumento => tipoDocumento.id == instanciaDocumento.documento.tipo_documento.id);
                if (!instanciaDocumento) {
                    naoPossuiInstancia = true;
                    break;
                }
            }
            for (let funcaoDocumental of documentoGarantiaVinculada.funcoes_documentais) {
                for (let tipoDocumento of funcaoDocumental.tipos_documento) {
                    const instanciaDocumento = this.garantia.instancias_documento.find(instanciaDocumento => tipoDocumento.id == instanciaDocumento.documento.tipo_documento.id);
                    if (!instanciaDocumento) {
                        naoPossuiInstancia = true;
                        break;
                    }
                }
            }
        }
        return naoPossuiInstancia;
    }

    verificaInexistenciaDocumentoGarantiaProduto(): boolean {
        let naoPossuiInstancia: boolean = false;
        for (let documentoProdutoVinculado of this.documentosProdutosVinculados) {
            for (let garantiaVinculada of documentoProdutoVinculado.garantias_vinculadas) {
                for (let tipoDocumento of garantiaVinculada.tipos_documento) {
                    const instanciaDocumento = this.garantia.instancias_documento.find(instanciaDocumento => tipoDocumento.id == instanciaDocumento.documento.tipo_documento.id);
                    if (!instanciaDocumento) {
                        naoPossuiInstancia = true;
                        break;
                    }
                }
                for (let funcaoDocumental of garantiaVinculada.funcoes_documentais) {
                    for (let tipoDocumento of funcaoDocumental.tipos_documento) {
                        const instanciaDocumento = this.garantia.instancias_documento.find(instanciaDocumento => tipoDocumento.id == instanciaDocumento.documento.tipo_documento.id);
                        if (!instanciaDocumento) {
                            naoPossuiInstancia = true;
                            break;
                        }
                    }
                }
            }
        }
        return naoPossuiInstancia;
    }

    showVinculoDossieOuFase() {
        return (this.processoDossieOuFase.instancias_documento && this.processoDossieOuFase.instancias_documento.length > 0) || this.singleExpandAllDocuments;
    }

    showVinculoPessoa(): boolean {
        return (this.cliente.instancias_documento && this.cliente.instancias_documento.length > 0) || this.singleExpandAllDocuments;
    }

    showVinculoProduto(): boolean {
        return (this.produto.instancias_documento && this.produto.instancias_documento.length > 0) || this.singleExpandAllDocuments;
    }

    showVinculoGarantia(): boolean {
        return (this.garantia.instancias_documento && this.garantia.instancias_documento.length > 0) || this.singleExpandAllDocuments;
    }
}