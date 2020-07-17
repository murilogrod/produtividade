import { Injectable } from "@angular/core";
import { AbaVerificacao } from "../model/aba-verificacao.model";
import { VerificacaoDossie } from "src/app/model/verificacao-dossie";
import { AbaVerificacaoComponent } from "../view/aba-verificacao.component";
import { DocumentoVerificado } from "src/app/model/documento-verificado";
import { ApontamentoChecklist } from "src/app/model/apontamento-cheklist";
import { VinculoArvoreCliente } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente";
import { Utils } from "src/app/utils/Utils";
import { VinculoArvoreProduto } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto";
import { VinculoArvoreProcesso } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo";
import { VinculoArvoreGarantia } from "src/app/model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia";
import { SelectItem } from "primeng/primeng";

@Injectable()
export class AbaVerificacaoComponentPresenter {

    abaVerificacao: AbaVerificacao;

    initAbaVerificacao(abaVerificacaoComponent: AbaVerificacaoComponent) {
        this.abaVerificacao.listaFase = [];
        this.abaVerificacao.listaFase.push({ label: 'Exibir Todas as fases', value: '' });
        abaVerificacaoComponent.listaComboFase.forEach(item => {
            this.abaVerificacao.listaFase.push({ label: item.nome, value: item.nome, tooltip: item.nome });
        });
        this.abaVerificacao.filtroFase = this.abaVerificacao.listaFase[1].value;
        this.verificarCarregadaVerificandoExistenciaInstancia(abaVerificacaoComponent);
        this.abaVerificacao.listaSituacao = new Array<SelectItem>();
        this.abaVerificacao.configSelectnconforme.forEach(item => {
            this.abaVerificacao.listaSituacao.push(item);
        });
        this.abaVerificacao.filtroSituacao = this.abaVerificacao.listaSituacao[1].value;
    }

    filtrarGlobal(abaVerificacaoComponent: AbaVerificacaoComponent) {
        abaVerificacaoComponent.listaAbaVerificacao = [];
        this.abaVerificacao.listaAbaVerificacaoOriginal.forEach(elemento => {
            let listaDocumentos = [];
            elemento.listaDocumento.forEach(documento => {
                if ((documento.fase && documento.fase.nome && documento.fase.nome.includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                    (documento.documento && documento.documento.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                    (documento.dataHoraVerificaco && documento.dataHoraVerificaco.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                    (documento.descricaoRealizada && documento.descricaoRealizada.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                    (documento.descricaoAprovado && documento.descricaoAprovado.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                    (documento.unidVerificacao && documento.unidVerificacao.toString().toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase()))) {
                    listaDocumentos.push(documento);
                } else {
                    documento.listaApontamento.forEach(apontamento => {
                        if ((apontamento.tituloApontamento && apontamento.tituloApontamento.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                            (apontamento.descricaoVerificacaoAprovada && apontamento.descricaoVerificacaoAprovada.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                            (apontamento.orientacao && apontamento.orientacao.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase())) ||
                            (apontamento.comentario && apontamento.comentario.toLowerCase().includes(this.abaVerificacao.filtroGlobal.toLowerCase()))) {
                            listaDocumentos.push(documento);
                        }
                    });
                }
            });
            let verificacao = Object.assign({}, elemento);
            verificacao.listaDocumento = listaDocumentos;
            abaVerificacaoComponent.listaAbaVerificacao.push(Object.assign({}, verificacao));
        });
    }

    filtrarPorFase(abaVerificacaoComponent: AbaVerificacaoComponent) {
        abaVerificacaoComponent.listaAbaVerificacao = [];
        this.abaVerificacao.listaAbaVerificacaoOriginal.forEach(elemento => {
            let listaDocumentos = [];
            elemento.listaDocumento.forEach(documento => {
                this.checkFaseSituacao(documento, listaDocumentos);
            });
            let verificacao = Object.assign({}, elemento);
            verificacao.listaDocumento = listaDocumentos;
            abaVerificacaoComponent.listaAbaVerificacao.push(Object.assign({}, verificacao));
        });

    }

    private checkFaseSituacao(documento: any, listaDocumentos: any[]) {
        const selectedFase: boolean = this.abaVerificacao.filtroFase.length > 0;
        const selectedSituacao: boolean = this.abaVerificacao.filtroSituacao.length > 0;
        const valueFase: boolean = documento.fase.nome === this.abaVerificacao.filtroFase;
        const valueAprovadoSemOBS: boolean = this.abaVerificacao.filtroSituacao == 'AP' && documento.aprovado && (documento.listaApontamento.length == 0 || (documento.listaApontamento.length > 0 && !documento.listaApontamento.some(ap => ap.comentario != null)));
        const valueAprovadoComOBS: boolean = this.abaVerificacao.filtroSituacao == 'APO' && documento.aprovado && documento.listaApontamento.length > 0 && documento.listaApontamento.some(ap => ap.comentario != null);
        const reprovado: boolean = this.abaVerificacao.filtroSituacao == 'PER' && !documento.aprovado;
        if (selectedFase && selectedSituacao) {
            if (valueFase && valueAprovadoSemOBS) {
                listaDocumentos.push(documento);
            }
            if (valueFase && valueAprovadoComOBS) {
                listaDocumentos.push(documento);
            }
            if (valueFase && reprovado) {
                listaDocumentos.push(documento);
            }
        } else if (!selectedFase && !selectedSituacao) {
            listaDocumentos.push(documento);
        } else {
            if (valueFase || valueAprovadoSemOBS || valueAprovadoComOBS || reprovado) {
                listaDocumentos.push(documento);
            }
        }
    }

    private vinculoProcesso(vinculo: any, verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {

        if (abaVerificacaoComponent.listaVerificacao.some(f => !f.instancia_documento && f.processo_fase.nome == vinculo.nome)) {
            this.montarVerificacaoFase(verificacao, abaVerificacaoComponent);
        }

        if (vinculo.vinculoProcesso.instancias_documento) {
            vinculo.vinculoProcesso.instancias_documento.forEach(instancia => {
                this.montarVerifcacaoDocumento(instancia, verificacao, abaVerificacaoComponent);
            });
        }

    }

    private montarVerifcacaoDocumento(instancia, verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        for (let verif of abaVerificacaoComponent.listaVerificacao) {
            if (this.abaVerificacao.filtroFase === verif.processo_fase.nome) {
                let docVerificado = new DocumentoVerificado();
                if (verif.instancia_documento && verif.instancia_documento.id == instancia.id && verif.instancia_documento.documento.tipo_documento.id == instancia.documento.tipo_documento.id) {
                    let detalhes = false;
                    docVerificado.id = instancia.id;
                    docVerificado.documento = instancia.label;
                    docVerificado.fase = verif.processo_fase;
                    docVerificado.unidVerificacao = verif.unidade;
                    docVerificado.realizada = verif.realizada;
                    docVerificado.descricaoRealizada = verif.realizada ? 'Sim' : 'Não';
                    docVerificado.aprovado = verif.aprovada;
                    docVerificado.descricaoAprovado = verif.aprovada ? 'Sim' : 'Não';
                    docVerificado.dataHoraVerificaco = verif.data_hora_verificacao;
                    detalhes = this.verificarBtnAcaoAparece(docVerificado, detalhes, verif);
                    docVerificado.documento = verif.instancia_documento.documento.tipo_documento.nome;
                    docVerificado.listaApontamento = [];
                    docVerificado.mostrar = detalhes;
                    this.carregarParecerQuandoExistir(verif, docVerificado);
                    docVerificado.habilitarDetalhes = detalhes;
                    verificacao.listaDocumento.push(docVerificado);
                    this.abaVerificacao.verificacaoCarregada = false;
                }
            }
        }
    }

    private montarVerificacaoFase(verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        abaVerificacaoComponent.listaVerificacao.forEach(fase => {
            if (!fase.instancia_documento) {
                let docVerificado = new DocumentoVerificado();
                let detalhes = false;
                docVerificado.id = fase.checklist.identificador_checklist;
                docVerificado.documento = fase.checklist.nome;
                docVerificado.fase = fase.processo_fase;
                docVerificado.unidVerificacao = fase.unidade;
                docVerificado.aprovado = fase.aprovada;
                docVerificado.dataHoraVerificaco = fase.data_hora_verificacao;
                docVerificado.realizada = fase.realizada;
                docVerificado.descricaoRealizada = fase.realizada ? 'Sim' : 'Não';
                docVerificado.descricaoAprovado = fase.aprovada ? 'Sim' : 'Não';
                detalhes = this.verificarBtnAcaoAparece(docVerificado, detalhes, fase);
                docVerificado.documento = '';
                docVerificado.listaApontamento = [];
                docVerificado.mostrar = detalhes;
                this.carregarParecerQuandoExistir(fase, docVerificado);
                docVerificado.habilitarDetalhes = detalhes;
                verificacao.listaDocumento.push(docVerificado);
            }
        });

    }

    private carregarParecerQuandoExistir(verif: any, docVerificado: DocumentoVerificado) {
        if (verif.parecer_apontamentos) {
            verif.parecer_apontamentos.forEach(apontamento => {
                let apont = new ApontamentoChecklist();
                apont.idApontamento = apontamento.identificador_apontamento;
                apont.tituloApontamento = apontamento.titulo;
                apont.verificacaoAprovada = apontamento.aprovado;
                apont.descricaoVerificacaoAprovada = apontamento.aprovado ? 'Sim' : 'Não';
                apont.comentario = apontamento.comentario_tratamento;
                apont.orientacao = apontamento.orientacao;
                apont.idParecer = apontamento.identificador_parecer;
                docVerificado.listaApontamento.push(apont);
            });
        }
    }

    private verificarBtnAcaoAparece(docVerificado: DocumentoVerificado, detalhes: boolean, verif: any) {
        if (docVerificado.realizada) {
            if (!docVerificado.aprovado) {
                detalhes = !docVerificado.aprovado;
                return detalhes;
            }
            if (docVerificado.aprovado && verif.parecer_apontamentos && verif.parecer_apontamentos.some(p => p.comentario_tratamento && p.comentario_tratamento.length > 0)) {
                detalhes = docVerificado.aprovado;
                return detalhes;
            }
        }
        return detalhes;
    }

    private verificarCarregadaVerificandoExistenciaInstancia(abaVerificacaoComponent: AbaVerificacaoComponent) {
        if (this.abaVerificacao.verificacaoCarregada) {
            abaVerificacaoComponent.listaAbaVerificacao = [];
            abaVerificacaoComponent.listaVinculoArvore.forEach(vinculo => {
                let verificacao = new VerificacaoDossie();
                verificacao.idVinculo = vinculo.id;
                verificacao.nomeVinculo = vinculo.nome;
                verificacao.listaDocumento = [];

                if (vinculo instanceof VinculoArvoreCliente) {
                    verificacao.nomeVinculo += " - " + vinculo.vinculoCliente.nome + ": ";
                    verificacao.nomeVinculo += vinculo.vinculoCliente.cnpj ? Utils.masKcpfCnpj(vinculo.vinculoCliente.cnpj) : Utils.masKcpfCnpj(vinculo.vinculoCliente.cpf);
                    this.vinculoCliente(vinculo, verificacao, abaVerificacaoComponent);
                } else if (vinculo instanceof VinculoArvoreProduto) {
                    verificacao.nomeVinculo = vinculo.vinculoProduto.codigo_operacao + "." + vinculo.vinculoProduto.codigo_modalidade
                        + " - " + vinculo.nome;
                    this.vinculoProduto(vinculo, verificacao, abaVerificacaoComponent);
                } else if (vinculo instanceof VinculoArvoreProcesso) {

                    this.vinculoProcesso(vinculo, verificacao, abaVerificacaoComponent);
                } else if (vinculo instanceof VinculoArvoreGarantia) {
                    verificacao.nomeVinculo = vinculo.vinculoGarantia.codigo_bacen + " - " + vinculo.nome;

                    let nomeCliente = "";
                    if (vinculo.vinculoGarantia.dossies_cliente && vinculo.vinculoGarantia.dossies_cliente.length > 0) {
                        nomeCliente += " - Relacionado: "
                        vinculo.vinculoGarantia.dossies_cliente.forEach(cliente => {
                            if (cliente.tipo_pessoa === "J") {
                                nomeCliente += Utils.masKcpfCnpj(cliente.cnpj) + ",";
                            } else {
                                nomeCliente += Utils.masKcpfCnpj(cliente.cpf) + ",";
                            }
                        });
                    }
                    verificacao.nomeVinculo += nomeCliente.substring(0, nomeCliente.length - 1);
                    this.vinculoGarantias(vinculo, verificacao, abaVerificacaoComponent);
                }
                this.addPushlistaAbaVerificacao(verificacao, abaVerificacaoComponent);
            });
        }
    }

    private addPushlistaAbaVerificacao(verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        abaVerificacaoComponent.listaAbaVerificacao.push(verificacao);
        this.abaVerificacao.listaAbaVerificacaoOriginal = abaVerificacaoComponent.listaAbaVerificacao.slice();
    }

    private vinculoGarantias(vinculo: any, verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        vinculo.vinculoGarantia.instancias_documento.forEach(instancia => {
            this.montarVerifcacaoDocumento(instancia, verificacao, abaVerificacaoComponent);
        });

    }

    private vinculoProduto(vinculo: any, verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        if (vinculo instanceof VinculoArvoreProduto) {
            vinculo.vinculoProduto.instancias_documento.forEach(instancia => {
                this.montarVerifcacaoDocumento(instancia, verificacao, abaVerificacaoComponent);
            });
        }
    }

    private vinculoCliente(vinculo: any, verificacao: VerificacaoDossie, abaVerificacaoComponent: AbaVerificacaoComponent) {
        vinculo.vinculoCliente.instancias_documento.forEach(instancia => {
            this.montarVerifcacaoDocumento(instancia, verificacao, abaVerificacaoComponent);
        });
    }

}