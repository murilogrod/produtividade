import { Injectable } from "@angular/core";
import { ModalApontamento } from "../model/modal-apontamento.model";
import { ApontamentoDataInput } from "../../model/apontamento-data-input";
import { ModalApontamentoComponent } from "../view/modal-apontamento.component";
import { ApontamentoOutput } from "../../model/apontamento-output";
import { ApontamentoDataOutput } from "../../model/apontamento-data-output";
import { GrowlMessageService } from "src/app/cruds/growl-message-service/growl-message.service";
import { APONTAMENTO } from "../../constant.apontamento";
import { Apontamento } from "src/app/cruds/model/apontamento.model";

@Injectable()
export class ModalApontamentoPresenter {

    modalApontamento: ModalApontamento;

    constructor(private growlMessageService: GrowlMessageService) { }

    /**
     * Realiza a configuração modal apontamento: checagem edição apontamento
     * @param apontamentoDataInput 
     */
    initConfigModalApontamento(apontamentoDataInput: ApontamentoDataInput) {
        this.carregarApontamento(apontamentoDataInput);
    }

    /**
     * Fecha a modal apontamento devolvedo seu objeto de saida
     * @param modalApontamentoComponent 
     */
    adicionarApontamentoGrid(modalApontamentoComponent: ModalApontamentoComponent) {
        this.modalApontamento.ocorrenciaApontamento = this.verificarOcorrenciaApontamento(modalApontamentoComponent);
        if (!this.modalApontamento.ocorrenciaApontamento) {
            modalApontamentoComponent.dialogReturn =
                <ApontamentoOutput>{
                    apontamentoDataOutput: this.inicializarObjetoSaidaModalApontamento(modalApontamentoComponent)
                };
            modalApontamentoComponent.closeDialog();
        } else {
            this.growlMessageService.showError(APONTAMENTO.APONTAMENTO_VALIDACAO, APONTAMENTO.APONTAMENTO_EXISTE_APONTAMENTO);
        }
    }

    /**
     * Validar regra: O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo
     */
    validarPendenciaInformacao() {
        if (this.modalApontamento.apontamento.pendencia_seguranca && this.modalApontamento.apontamento.pendencia_informacao) {
            this.modalApontamento.pendenciaInformacaoSeguranca = true;
            this.growlMessageService.showError(APONTAMENTO.APONTAMENTO_VALIDACAO, APONTAMENTO.APONTAMENTO_PENDENCIA_INFORMCACAO_SEGURANCA);
        } else {
            this.modalApontamento.pendenciaInformacaoSeguranca = false;
        }
    }

    /**
     * Validar regra: O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo
     */
    validarPendenciaSeguranca() {
        if (this.modalApontamento.apontamento.pendencia_informacao && this.modalApontamento.apontamento.pendencia_seguranca) {
            this.modalApontamento.pendenciaInformacaoSeguranca = true;
            this.growlMessageService.showError(APONTAMENTO.APONTAMENTO_VALIDACAO, APONTAMENTO.APONTAMENTO_PENDENCIA_INFORMCACAO_SEGURANCA);
        } else {
            this.modalApontamento.pendenciaInformacaoSeguranca = false;
        }
    }

    /**
     * Valida a ocorrencia nome apontamento
     * nesse ponto aqui é utilizado para certificar que o nome é compatível
     * evento: change - input nome apontamento
     * @param modalApontamentoComponent 
     */
    validarOcorrenciaApontamento(modalApontamentoComponent: ModalApontamentoComponent) {
        this.modalApontamento.ocorrenciaApontamento = this.verificarOcorrenciaApontamento(modalApontamentoComponent);
    }

    /**
     * Realiza a validação para verificar ocorrencia de apontamento
     * @param modalApontamentoComponent 
     */
    private verificarOcorrenciaApontamento(modalApontamentoComponent: ModalApontamentoComponent): boolean {
        let ocorrenciaApontamento: boolean = false;
        let apontamentoExistente: boolean = false;
        if (modalApontamentoComponent.apontamentoDataInput.index == -1) {
            apontamentoExistente = modalApontamentoComponent.apontamentoDataInput.apontamentos.some(apontamento => apontamento.nome.trim().toUpperCase() == this.modalApontamento.apontamento.nome.trim().toUpperCase());
        } else {
            apontamentoExistente = modalApontamentoComponent.apontamentoDataInput.apontamentos.some(apontamento => apontamento.nome.trim().toUpperCase() == this.modalApontamento.apontamento.nome.trim().toUpperCase() && this.modalApontamento.ultimoNome.trim().toUpperCase() !== apontamento.nome.trim().toUpperCase());
        }
        if (apontamentoExistente) {
            ocorrenciaApontamento = true;
        }
        return ocorrenciaApontamento;
    }

    /**
     * inicializa o objeto de saido: modal apontamento: index = -1; novo apontamento
     * @param modalApontamentoComponent 
     */
    private inicializarObjetoSaidaModalApontamento(modalApontamentoComponent: ModalApontamentoComponent): ApontamentoDataOutput {
        const apontamentoDataOutput: ApontamentoDataOutput = new ApontamentoDataOutput();
        apontamentoDataOutput.index = modalApontamentoComponent.apontamentoDataInput.index > -1 ? modalApontamentoComponent.apontamentoDataInput.index : -1;
        apontamentoDataOutput.ultimoNome = this.modalApontamento.ultimoNome;
        apontamentoDataOutput.ultimaOrdem = this.modalApontamento.ultimaOrdem;
        apontamentoDataOutput.apontamento = this.modalApontamento.apontamento;
        return apontamentoDataOutput;
    }

    /**
     * Carrega o apontamento: index = -1 = Novo Apontamento
     * @param apontamentoDataInput 
     */
    private carregarApontamento(apontamentoDataInput: ApontamentoDataInput) {
        if (apontamentoDataInput.index > -1) {
            this.modalApontamento.ultimoNome = apontamentoDataInput.apontamento.nome
            this.modalApontamento.ultimaOrdem = apontamentoDataInput.apontamento.sequencia_apresentacao;
            this.modalApontamento.apontamento = apontamentoDataInput.apontamento;
            this.modalApontamento.editar = true;
        } else {
            this.inicializarValoresPadrao(apontamentoDataInput.apontamentos);
        }
    }

    /**
     *  Para novo apontamento os valores default são carregados
     * @param apontamentos 
     */
    private inicializarValoresPadrao(apontamentos: Array<Apontamento>) {
        this.modalApontamento.editar = false;
        this.modalApontamento.apontamento.pendencia_informacao = false;
        this.modalApontamento.apontamento.pendencia_seguranca = false;
        this.modalApontamento.apontamento.rejeicao_documento = false;
        this.modalApontamento.apontamento.sequencia_apresentacao = apontamentos.length + 1;
    }

}