import { Injectable } from "@angular/core";
import { PesquisaTipoRelacionamento } from "../model/pesquisa-tipo-relacionamento.model";
import { TipoRelacionamentoService } from "../../service/tipo-relacionamento.service";
import { LoaderService } from "src/app/services";
import { ConfirmationService, SortEvent } from "primeng/primeng";
import { PesquisaTipoRelacionamentoComponent } from "../view/pesquisa-tipo-relacionamento.component";
import { TIPO_RELACIONAMENTO } from "../../constant.tipo-relacionamento";
import { Utils } from "src/app/cruds/util/utils";

@Injectable()
export class PesquisaTipoRelacionamentoPresenter {
    pesquisaTipoRelacionamento: PesquisaTipoRelacionamento;

    constructor(private tipoRelacionamentoService: TipoRelacionamentoService,
        private loadService: LoaderService,
        private confirmationService: ConfirmationService) { }

    /**
     * Realiza a configuração da grid de tipo relacionamentos
     * @param pesquisaTipoRelacionamentoComponent 
     */
    initConfigListaTiposRelacionamentos(pesquisaTipoRelacionamentoComponent: PesquisaTipoRelacionamentoComponent) {
        this.mostrarMensagemSucessoParaAtualizacaoTipoRelacionamento(pesquisaTipoRelacionamentoComponent);
        this.reinicializarValoresPadroesAtualizacaoTipoRelacionamento();
        this.pesquisaTipoRelacionamento.columns = this.pesquisaTipoRelacionamento.configTableColsRelacionamentos;
        this.recuperarTipoRelacionamentos();
    }

    /**
     * Realiza a ordenação dos campos da entidade Tipo de Relacionamento
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
     * Filtra os tipos de relacionamentos conforme as propriedades do mesmo;
     * com pelo menos 2 caracteres ou reseta sem nada digitado.
     * @param input 
     * @param dataProdutos 
     */
    filterTiposRelacionamentos(input: any, dataTiposRelacionamentos: any) {
        if (input.length == 0) {
            dataTiposRelacionamentos.filterGlobal(input, 'contains');
        }
        if (input.length > 1) {
            dataTiposRelacionamentos.filterGlobal(input, 'contains');
        }
    }

    /**
     * Checagem para remoção de tipo de relacionamento
     * @param pesquisaTipoRelacionamentoComponent 
     * @param id 
     */
    confirmRemoveTipoRelacionamento(pesquisaTipoRelacionamentoComponent: PesquisaTipoRelacionamentoComponent, id: string) {
        this.confirmationService.confirm({
            message: TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_REMOVER,
            accept: () => {
                this.removeTipoRelacionamento(pesquisaTipoRelacionamentoComponent, id);
            }
        });
    }

    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.pesquisaTipoRelacionamento.filteredRecords = event.filteredValue.length;
        } else {
            this.pesquisaTipoRelacionamento.filteredRecords = 0;
        }
    }

    /**
     * Remove o tipo relacionamento passando o id
     * @param pesquisaTipoRelacionamentoComponent 
     * @param id 
     */
    private removeTipoRelacionamento(pesquisaTipoRelacionamentoComponent: PesquisaTipoRelacionamentoComponent, id: string) {
        this.loadService.show();
        this.tipoRelacionamentoService.delete(id).subscribe(() => {
            this.recuperarTipoRelacionamentos();
            this.loadService.hide();
            pesquisaTipoRelacionamentoComponent.addMessageSuccess(TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_REMOVER_SUCESSO);
        }, error => {
            let mensagem: string = '';
            if (error.error.mensagem) {
                mensagem = error.error.mensagem
            } else {
                mensagem = error.message
            }
            pesquisaTipoRelacionamentoComponent.addMessageError(mensagem);
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Mostra a mensagem de sucesso ao adicionar ou atualizar um tipo de relacionamento
     * @param pesquisaTipoRelacionamentoComponent 
     */
    private mostrarMensagemSucessoParaAtualizacaoTipoRelacionamento(pesquisaTipoRelacionamentoComponent: PesquisaTipoRelacionamentoComponent) {
        if (this.tipoRelacionamentoService.getNovoTipoRelacionamento()) {
            const msg: string = TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_ADICIONADO_SUCESSO.replace('{id}', this.tipoRelacionamentoService.getId().toString());
            pesquisaTipoRelacionamentoComponent.addMessageSuccess(msg);
        }
        if (this.tipoRelacionamentoService.getEdicaoTipoRelacionamento()) {
            const msg: string = TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_ATUALIZADO_SUCESSO.replace('{id}', this.tipoRelacionamentoService.getId().toString());
            pesquisaTipoRelacionamentoComponent.addMessageSuccess(msg);
        }
    }

    /**
     * Reseta os booleanos que controlam a chamada 
     * da mensagem de atualização de tipo de relacionamento
     */
    private reinicializarValoresPadroesAtualizacaoTipoRelacionamento() {
        this.tipoRelacionamentoService.setEdicaoTipoRelacionamento(false);
        this.tipoRelacionamentoService.setNovoTipoRelacionamento(false);
    }

    /**
     * Recupera os tipos de relacionamentos
     */
    private recuperarTipoRelacionamentos() {
        this.loadService.show();
        this.tipoRelacionamentoService.get().subscribe(dados => {
            this.tipoRelacionamentoService.setTiposRelacionamentos(dados);
            this.pesquisaTipoRelacionamento.tiposRelacionamentos = dados
            this.custumizeRowsPerPageOptions();
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.pesquisaTipoRelacionamento.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.pesquisaTipoRelacionamento.tiposRelacionamentos);
    }


}