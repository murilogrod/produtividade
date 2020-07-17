import { Injectable } from "@angular/core";
import { ConfirmationService, SortEvent } from "primeng/primeng";
import { TipoDocumentoService } from 'src/app/cruds/tipo-documento/service/tipo-documento.service';
import { Utils } from "src/app/cruds/util/utils";
import { LoaderService } from "src/app/services";
import { TIPO_DOCUMENTO } from './../../constant-tipo-documento';
import { PesquisaTipoDocumento } from './../model/pesquisa-tipo-documento.model';
import { PesquisaTipoDocumentoComponent } from './../view/pesquisa-tipo-documento.component';
import { SituacaoTipoDocumento } from "../model/situacao-tipo-documento.enum";

@Injectable()
export class PesquisaTipoDocumentoPresenter {

    pesquisaTipoDocumento: PesquisaTipoDocumento;
    public itens: any = [];

    constructor(private tipoDocumentoService: TipoDocumentoService,
        private loadService: LoaderService,
        private confirmationService: ConfirmationService) { }

    initConfigTipoDocumento(pesquisaTipoDocumentoComponent: PesquisaTipoDocumentoComponent) {
        this.mostrarMensagemSucessoParaAtualizacaoTipoDocumento(pesquisaTipoDocumentoComponent);
        this.reinicializarValoresPadroesAtualizacaoTipoDocumento();
        this.pesquisaTipoDocumento.columns = this.pesquisaTipoDocumento.configTableColsTipoDocumento;
        this.recuperarTipoDocumentos();
    }

    /**
     * Realiza a ordenação dos campos da entidade tipo documento
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
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }
            return (event.order * result);
        });
    }

    /**
     * Filtra os canais conforme as propriedades do mesmo;
     * com pelo menos 2 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataTipoDocumentos 
     */
    filterTipoDocumentos(input: any, dataTipoDocumentos: any) {
        if (input.length == 0) {
            dataTipoDocumentos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
            this.pesquisaTipoDocumento.filteredRecords = 0;
        }
        if (input.length > 1) {
            dataTipoDocumentos.filterGlobal(input.normalize("NFD").replace(/[\u0300-\u036f]/g, ''), 'contains');
        }
    }

    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.pesquisaTipoDocumento.filteredRecords = event.filteredValue.length;
        } else {
            this.pesquisaTipoDocumento.filteredRecords = this.pesquisaTipoDocumento.tipoDocumentos.length;
        }
    }

    /**
     * Checagem para remoção de Tipo Documento
     */
    confirmRemoveTipoDocumento(pesquisaTipoDocumentoComponent: PesquisaTipoDocumentoComponent, identificador_tipo_documento: string) {
        this.confirmationService.confirm({
            message: TIPO_DOCUMENTO.TIPO_DOCUMENTO_REMOVER,
            accept: () => {
                this.removeTipoDocumento(pesquisaTipoDocumentoComponent, identificador_tipo_documento);
            }
        });
    }

    recuperarTipoDocumentos() {
        this.loadService.show();
        this.tipoDocumentoService.get().subscribe(dados => {
            this.inicializarTiposDocumentos(dados);
            this.classificaItens();
            this.custumizeRowsPerPageOptions();
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Filtra os tipos de documentos conforme situação passada
     * @param situacaoTipoDocumento 
     */
    filtrarDocumentosConformeSituacao(situacaoTipoDocumento: SituacaoTipoDocumento) {
        if (situacaoTipoDocumento == SituacaoTipoDocumento.ATIVO) {
            this.pesquisaTipoDocumento.tipoDocumentos = this.pesquisaTipoDocumento.tipoDocumentosAtivos;
        } else if (situacaoTipoDocumento == SituacaoTipoDocumento.INATIVO) {
            this.pesquisaTipoDocumento.tipoDocumentos = this.pesquisaTipoDocumento.tipoDocumentosInativos;
        } else {
            this.pesquisaTipoDocumento.tipoDocumentos = this.pesquisaTipoDocumento.tipoDocumentosGeral;
        }
        this.classificaItens();
    }

    /**
     * Mostra a mensagem de sucesso ao adicionar ou atualizar um Tipo Documento
     * @param pesquisaTipoDocumentoComponent 
     */
    private mostrarMensagemSucessoParaAtualizacaoTipoDocumento(pesquisaTipoDocumentoComponent: PesquisaTipoDocumentoComponent) {
        if (this.tipoDocumentoService.getNovoTipoDocumento()) {
            const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_ADICIONADO_SUCESSO.replace('{id}', this.tipoDocumentoService.getId().toString());
            pesquisaTipoDocumentoComponent.addMessageSuccess(msg);
        }
        if (this.tipoDocumentoService.getEdicaoTipoDocumento()) {
            const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_ATUALIZADO_SUCESSO.replace('{id}', this.tipoDocumentoService.getId().toString());
            pesquisaTipoDocumentoComponent.addMessageSuccess(msg);
        }
        if (this.tipoDocumentoService.getTipoDocumentoSemAlteracao()) {
            const msg: string = TIPO_DOCUMENTO.TIPO_DOCUMENTO_SEM_ALTERACAO.replace('{id}', this.tipoDocumentoService.getId().toString());
            pesquisaTipoDocumentoComponent.addMessageWarning(msg);
        }
    }

    /**
     * Reseta os booleanos que controlam a chamada 
     * da mensagem de atualização de Tipo Documento
     */
    private reinicializarValoresPadroesAtualizacaoTipoDocumento() {
        this.tipoDocumentoService.setEdicaoTipoDocumento(false);
        this.tipoDocumentoService.setNovoTipoDocumento(false);
    }

    /**
     * Remove o canal passando o identificador
     * @param identificador_canal 
     * @param pesquisaTipoDocumentoComponent 
     */
    private removeTipoDocumento(pesquisaTipoDocumentoComponent: PesquisaTipoDocumentoComponent, identificador_tipo_documento: string) {
        this.loadService.show();
        this.tipoDocumentoService.delete(identificador_tipo_documento).subscribe(() => {
            this.recuperarTipoDocumentos();
            this.classificaItens();
            this.loadService.hide();
            pesquisaTipoDocumentoComponent.addMessageSuccess(TIPO_DOCUMENTO.TIPO_DOCUMENTO_REMOVER_SUCESSO);
        }, error => {
            let mensagem: string = '';
            if (error.error.mensagem) {
                mensagem = error.error.mensagem
            } else {
                mensagem = error.message
            }
            pesquisaTipoDocumentoComponent.addMessageError(mensagem);
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Classifica itens atribuindo booleano e variaveis string
     */
    private classificaItens() {
        if (this.pesquisaTipoDocumento.tipoDocumentos !== undefined && this.pesquisaTipoDocumento.tipoDocumentos !== []) {
            this.pesquisaTipoDocumento.tipoDocumentos.sort(function (n1, n2) {
                return n1.id - n2.id;
            });
            for (let item of this.pesquisaTipoDocumento.tipoDocumentos) {
                item.nome = item.nome.normalize("NFD").replace(/[\u0300-\u036f]/g, '');
                item.indicador_tipo_pessoa_label = item.indicador_tipo_pessoa == ('F')
                    ? 'Fisica' : item.indicador_tipo_pessoa == ('J')
                        ? 'Juridica' : 'Ambos';
                item.indicador_uso_apoio_negocio_label = item.indicador_uso_apoio_negocio ? 'Sim' : 'Nao';
                item.indicador_uso_dossie_digital_label = item.indicador_uso_dossie_digital ? 'Sim' : 'Nao';
                item.indicador_uso_processo_administrativo_label = item.indicador_uso_processo_administrativo ? 'Sim' : 'Nao';
                item.ativo_label = item.ativo ? 'Sim' : 'Nao';
            }
        }
        this.pesquisaTipoDocumento.totalRecords = this.pesquisaTipoDocumento.tipoDocumentos.length;
    }

    /**
     * Inicializa os arrays contendo os tipos de documentos: ativos, inativos e geral
     * lista oficial componente: tipoDocumentos
     * mantem lista geral em serviço de memória: 
     * uso em validação de cadastro de tipo de documento
     * @param dados 
     */
    private inicializarTiposDocumentos(dados: any) {
        this.pesquisaTipoDocumento.tipoDocumentosGeral = dados;
        this.pesquisaTipoDocumento.tipoDocumentosAtivos = dados.filter(data => data.ativo);
        this.pesquisaTipoDocumento.tipoDocumentosInativos = dados.filter(data => !data.ativo);
        this.pesquisaTipoDocumento.tipoDocumentos = this.pesquisaTipoDocumento.tipoDocumentosAtivos;
        this.tipoDocumentoService.setTipoDocumentos(dados);
    }

    /**
    * Custumiza o array de linhas por páginas; 
    * utilizando o padrão: 15, 30, 50 e último registro
    */
    private custumizeRowsPerPageOptions() {
        this.pesquisaTipoDocumento.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.pesquisaTipoDocumento.tipoDocumentos);
    }

}