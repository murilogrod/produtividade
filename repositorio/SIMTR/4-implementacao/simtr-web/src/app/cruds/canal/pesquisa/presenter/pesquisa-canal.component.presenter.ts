import { Injectable } from "@angular/core";
import { PesquisaCanal } from "../model/pesquisa-canal.model";
import { CanalService } from "../../service/canal.service";
import { LoaderService } from "src/app/services";
import { SortEvent, ConfirmationService } from "primeng/primeng";
import { PesquisaCanalComponent } from "../view/pesquisa-canal.component";
import { CANAL } from "../../constant.canal";
import { Utils } from "src/app/cruds/util/utils";

@Injectable()
export class PesquisaCanalPresenter {

    pesquisaCanal: PesquisaCanal;

    constructor(private canalService: CanalService,
        private loadService: LoaderService,
        private confirmationService: ConfirmationService) { }

    /**
     * Realiza a configuração da grid de canais
     * @param pesquisaCanalComponent 
     */
    initConfigListaCanais(pesquisaCanalComponent: PesquisaCanalComponent) {
        this.mostrarMensagemSucessoParaAtualizacaoCanal(pesquisaCanalComponent);
        this.reinicializarValoresPadroesAtualizacaoCanal();
        this.pesquisaCanal.columns = this.pesquisaCanal.configTableColsCanais;
        this.recuperarCanais();
    }

    /**
     * Realiza a ordenação dos campos da entidade Canal
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
     * @param dataProdutos 
     */
    filterCanais(input: any, dataCanais: any) {
        if (input.length == 0) {
            dataCanais.filterGlobal(input, 'contains');
        }
        if (input.length > 1) {
            dataCanais.filterGlobal(input, 'contains');
        }
    }

    /**
     * Checagem para remoção de canal
     */
    confirmRemoveCanal(pesquisaCanalComponent: PesquisaCanalComponent, identificador_canal: string) {
        this.confirmationService.confirm({
            message: CANAL.CANAL_REMOVER,
            accept: () => {
                this.removeCanal(pesquisaCanalComponent, identificador_canal);
            }
        });
    }

    /**
     * Remove o canal passando o identificador
     * @param identificador_canal 
     * @param pesquisaCanalComponent 
     */
    private removeCanal(pesquisaCanalComponent: PesquisaCanalComponent, identificador_canal: string) {
        this.loadService.show();
        this.canalService.delete(identificador_canal).subscribe(() => {
            this.recuperarCanais();
            this.loadService.hide();
            pesquisaCanalComponent.addMessageSuccess(CANAL.CANAL_REMOVER_SUCESSO);
        }, error => {
            let mensagem: string = '';
            if (error.error.mensagem) {
                mensagem = error.error.mensagem
            } else {
                mensagem = error.message
            }
            pesquisaCanalComponent.addMessageError(mensagem);
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Recupera os canais
     */
    private recuperarCanais() {
        this.loadService.show();
        this.canalService.get().subscribe(dados => {
            this.pesquisaCanal.canais = dados
            this.custumizeRowsPerPageOptions();
            this.loadService.hide();
        }, error => {
            this.loadService.hide();
            throw error;
        });
    }

    /**
     * Reseta os booleanos que controlam a chamada 
     * da mensagem de atualização de canal
     */
    private reinicializarValoresPadroesAtualizacaoCanal() {
        this.canalService.setEdicaoCanal(false);
        this.canalService.setNovoCanal(false);
    }

    /**
     * Mostra a mensagem de sucesso ao adicionar ou atualizar um canal
     * @param pesquisaCanalComponent 
     */
    private mostrarMensagemSucessoParaAtualizacaoCanal(pesquisaCanalComponent: PesquisaCanalComponent) {
        if (this.canalService.getNovoCanal()) {
            const msg: string = CANAL.CANAL_ADICIONADO_SUCESSO.replace('{id}', this.canalService.getId().toString());
            pesquisaCanalComponent.addMessageSuccess(msg);
        }
        if (this.canalService.getEdicaoCanal()) {
            const msg: string = CANAL.CANAL_ATUALIZADO_SUCESSO.replace('{id}', this.canalService.getId().toString());
            pesquisaCanalComponent.addMessageSuccess(msg);
        }
    }

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.pesquisaCanal.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.pesquisaCanal.canais);
    }

}