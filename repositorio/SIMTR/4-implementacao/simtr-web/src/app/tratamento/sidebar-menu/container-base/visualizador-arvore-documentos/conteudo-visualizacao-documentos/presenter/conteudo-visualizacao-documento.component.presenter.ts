import { Injectable } from "@angular/core";
import { TipoDocumentoArvoreGenerica } from "src/app/model/model-arvore-generica-dossie-produto/tipo-documento-arvore-generica.model";
import { ElementoConteudo } from "src/app/model/elemento-conteudo";

@Injectable()
export class ConteudoVisualizacaoDocumentoPresenter {

    private singleExpandAllDocuments: boolean = false;
    private funcaoDocumental: boolean = false;
    private tipoDocumento: number;

    /**
     * Habilita o conteudo vazio da pasta arvore cliente de acordo selecao usuario.
     * @param vinculo 
     * @param tipoDocumento 
     * @param singleExpandAllDocuments 
     * @param singleExpandAllDocumentsCliente 
     */
    verificaExistenciaInstanciaDocumentoCliente(vinculo: any, tipoDocumento: TipoDocumentoArvoreGenerica, singleExpandAllDocumentsCliente: boolean): boolean {
        let verificainstanciaDocumento: boolean = false;
        const instanciaDocumento = vinculo.instancias_documento.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == tipoDocumento.id);
        const userExpandAll = !instanciaDocumento && this.singleExpandAllDocuments;
        const userExpandAllCliente = !instanciaDocumento && singleExpandAllDocumentsCliente;
        verificainstanciaDocumento = userExpandAll || userExpandAllCliente;
        return verificainstanciaDocumento;
    }

    /**
     * Habilita o conteudo vazio da pasta arvore(s) garantia(s) de acordo selecao usuario.
     * @param vinculo 
     * @param tipoDocumento 
     * @param singleExpandAllDocumentsGarantias 
     */
    verificaExistenciaInstanciaDocumentoGarantia(vinculo: any, tipoDocumento: TipoDocumentoArvoreGenerica, singleExpandAllDocumentsGarantias: boolean): boolean {
        let verificainstanciaDocumento: boolean = false;
        const instanciaDocumento = vinculo.instancias_documento.find(instanciaDocumento => instanciaDocumento.documento.tipo_documento.id == tipoDocumento.id);
        const userExpandAll = !instanciaDocumento && this.singleExpandAllDocuments;
        const userExpandAllGarantias = !instanciaDocumento && singleExpandAllDocumentsGarantias;
        verificainstanciaDocumento = userExpandAll || userExpandAllGarantias;
        return verificainstanciaDocumento;
    }

    /**
     * Habilita o conteudo vazio da pasta arvore processo de acordo selecao usuario.
     * @param vinculo 
     * @param elementoConteudo 
     * @param singleExpandAllDocumentsDossieFase 
     */
    verificaExistenciaElementoConteudoProcesso(vinculo: any, elementoConteudo: ElementoConteudo, singleExpandAllDocumentsDossieFase: boolean): boolean {
        let verificaElementoConteudo: boolean = false;
        if (!elementoConteudo.identificador_elemento_vinculador) {
            return verificaElementoConteudo = false;
        }
        let tipoArvoreUser = this.singleExpandAllDocuments || singleExpandAllDocumentsDossieFase;
        if (undefined == vinculo.instancias_documento) {
            if (tipoArvoreUser) {
                verificaElementoConteudo = true;
            } else {
                verificaElementoConteudo = false;
            }
        } else {
            const instanciaDocumento = vinculo.instancias_documento.find(instanciaDocumento => instanciaDocumento.id_elemento_conteudo == elementoConteudo.identificador_elemento);
            const userExpandAll = !instanciaDocumento && this.singleExpandAllDocuments;
            const userExpandAllDossieFase = !instanciaDocumento && singleExpandAllDocumentsDossieFase;
            verificaElementoConteudo = userExpandAll || userExpandAllDossieFase;
        }
        return verificaElementoConteudo;
    }

    /**
     *  Habilita o conteudo vazio da pasta arvore produto de acordo selecao usuario.
     * @param vinculo 
     * @param elementoConteudo 
     * @param singleExpandAllDocumentsProduto 
     */
    verificaExistenciaElementoConteudoProduto(vinculo: any, elementoConteudo: ElementoConteudo, singleExpandAllDocumentsProduto: boolean): boolean {
        let verificaElementoConteudo: boolean = false;
        if (!elementoConteudo.identificador_elemento_vinculador) {
            return verificaElementoConteudo = false;
        }
        let tipoArvoreUser = this.singleExpandAllDocuments || singleExpandAllDocumentsProduto;
        if (undefined == vinculo.instancias_documento) {
            if (tipoArvoreUser) {
                verificaElementoConteudo = true;
            } else {
                verificaElementoConteudo = false;
            }
        } else {
            const instanciaDocumento = vinculo.instancias_documento.find(instanciaDocumento => instanciaDocumento.id_elemento_conteudo == elementoConteudo.identificador_elemento);
            const userExpandAll = !instanciaDocumento && this.singleExpandAllDocuments;
            const userExpandAllProduto = !instanciaDocumento && singleExpandAllDocumentsProduto;
            verificaElementoConteudo = userExpandAll || userExpandAllProduto;
        }

        return verificaElementoConteudo;
    }

    /**
     * Parametro geral da selecao de todas árvores
     * @param singleExpandAllDocuments 
     */
    setSingleExpandAllDocuments(singleExpandAllDocuments: boolean) {
        this.singleExpandAllDocuments = singleExpandAllDocuments;
    }

    existeDocumentoChildrenCliente(documentoVinculoCliente:any, vinculo: any, singleExpandAllDocumentsCliente: boolean) {
        let lista = [];
        for(let instancia of vinculo.instancias_documento) {
            if(undefined == documentoVinculoCliente.funcao_documental || (documentoVinculoCliente.tipo_documento && instancia.documento.tipo_documento.id === documentoVinculoCliente.tipo_documento.id)) {
                let obj = {
                    "id" : instancia.documento.tipo_documento.id,
                    "tipo" : "tipoDocumento"
                };
                lista.push(obj);
                this.funcaoDocumental = true;
                this.tipoDocumento = instancia.documento.tipo_documento.id
            }
            if(singleExpandAllDocumentsCliente && documentoVinculoCliente.funcao_documental || (documentoVinculoCliente.funcao_documental)) {
                for(let tipoDoc of documentoVinculoCliente.funcao_documental.tipos_documento) {
                    if(tipoDoc.id === instancia.documento.tipo_documento.id) { 
                        let obj = {
                            "id" : instancia.documento.tipo_documento.id,
                            "tipo" : "funcaoDocumental"
                        };
                        lista.push(obj);
                    }
                }
            }
        }
        if(this.funcaoDocumental){
            let obj = {
                "id" : this.tipoDocumento,
                "tipo" : "tipoDocumento"
            };
            lista.push(obj);
        }
        return lista;
    }

    existeDocumentoChildrenDossieFase(documentoVinculoDossieFase:any, vinculo: any, singleExpandAllDocumentsDossieFase: boolean) {
        let lista = [];
        if(vinculo.instancias_documento) {
            for(let instancia of vinculo.instancias_documento) {
    
                if(undefined == documentoVinculoDossieFase.identificador_elemento_vinculador || singleExpandAllDocumentsDossieFase || (documentoVinculoDossieFase.tipo_documento && instancia.documento.tipo_documento.id === documentoVinculoDossieFase.tipo_documento.id)) {
                    let obj = {
                        "id" : instancia.documento.tipo_documento.id,
                        "tipo" : "tipoDocumento",
                        "vinculador" : documentoVinculoDossieFase.identificador_elemento_vinculador
                    };
                    lista.push(obj);
                }
            }
        }
        return lista;
    }

    existeDocumentoChildrenGarantias(documentoVinculoGarantias:any, vinculo: any, singleExpandAllDocumentsGarantia: boolean) {
        let lista = [];
        for(let instancia of vinculo.instancias_documento) {

            if(singleExpandAllDocumentsGarantia || (documentoVinculoGarantias.tipos_documento && documentoVinculoGarantias.tipos_documento.length > 0)) {
                documentoVinculoGarantias.tipos_documento.forEach(tipoDoc => {
                    if(instancia.documento.tipo_documento.id === tipoDoc.id) {
                        let obj = {
                            "id" : instancia.documento.tipo_documento.id,
                            "tipo" : "tipoDocumento",
                            "arvore": documentoVinculoGarantias.nome_garantia ? documentoVinculoGarantias.nome_garantia : documentoVinculoGarantias.nome
                        };
                        lista.push(obj);
                    }
                });
            }
            if(singleExpandAllDocumentsGarantia && documentoVinculoGarantias.funcoes_documentais || (documentoVinculoGarantias.funcoes_documentais)) {
                if(documentoVinculoGarantias.funcoes_documentais && documentoVinculoGarantias.funcoes_documentais.length > 0) {
                    documentoVinculoGarantias.funcoes_documentais.forEach(funcaoDocumental => {
                        for(let tipoDoc of funcaoDocumental.tipos_documento) {
                            if(tipoDoc.id === instancia.documento.tipo_documento.id ) { 
                                let obj = {
                                    "id" : instancia.documento.tipo_documento.id,
                                    "tipo" : "funcaoDocumental",
                                    "arvore": documentoVinculoGarantias.nome_garantia ? documentoVinculoGarantias.nome_garantia : documentoVinculoGarantias.nome
                                };
                                lista.push(obj);
                            }
                        }
                    });
                }
            }
        }
        return lista;
    }

    existeDocumentoChildrenProduto(elementoConteudo:any, vinculo: any, singleExpandAllDocumentsProduto: boolean) {
        let lista = [];
        if(vinculo.instancias_documento) {
            for(let instancia of vinculo.instancias_documento) {
    
                if(undefined == elementoConteudo.identificador_elemento_vinculador || singleExpandAllDocumentsProduto || (elementoConteudo.tipo_documento && instancia.documento.tipo_documento.id === elementoConteudo.tipo_documento.id)) {
                    let obj = {
                        "id" : instancia.documento.tipo_documento.id,
                        "tipo" : "tipoDocumento",
                        "vinculador" : elementoConteudo.identificador_elemento_vinculador
                    };
                    lista.push(obj);
                }
            }
        }
        return lista;
    }

}