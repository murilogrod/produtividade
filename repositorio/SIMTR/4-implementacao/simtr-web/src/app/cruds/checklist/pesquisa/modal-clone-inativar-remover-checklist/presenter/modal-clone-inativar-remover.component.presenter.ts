import { Injectable } from "@angular/core";
import { ModalCloneInativarRemover } from "../model/modal-clone-inativar-remover.model";
import { CheckListService } from "../../../service/checklist.service";
import { LoaderService } from "src/app/services";
import { ModalCloneInativarRemoverChecklistComponent } from "../view/modal-clone-inativar-remover-checklist.component";
import { GrowlMessageService } from "src/app/cruds/growl-message-service/growl-message.service";
import { CloneInativarRemoverChecklistOutput } from "../../model/clone-inativar-remover-checklist-output";
import { CloneInativarRemoverChecklistDataOutput } from "../../model/clone-inativar-remover-checklist-data-output";
import { CHECKLIST } from "../../../constant.checklist";
import { CloneInativarRemoverChecklistDataInput } from "../../model/clone-inativar-remover-checklist-data-input";
import { Checklist } from "src/app/cruds/model/checklist.model";

@Injectable()
export class ModalCloneInativarRemoverPresenter {
    modalCloneInativarRemover: ModalCloneInativarRemover;

    constructor(private checkListService: CheckListService,
        private loadService: LoaderService,
        private growlMessageService: GrowlMessageService) { }


    /**
     * Configuracao inicial para a modal clone inativar remover checklist
     * @param cloneInativarRemoverChecklistDataInput 
     */
    initConfigModalCloneInativarRemover(cloneInativarRemoverChecklistDataInput: CloneInativarRemoverChecklistDataInput) {
        this.validarRemocaoChecklist(cloneInativarRemoverChecklistDataInput);
    }

    /**
     * Realiza a requisicao para remocao de checklist
     * @param modalCloneInativarRemoverChecklistComponent 
     */
    removerChecklist(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        this.loadService.show();
        this.checkListService.delete(modalCloneInativarRemoverChecklistComponent.cloneInativarRemoverChecklistDataInput.gridChecklist.id).subscribe(() => {
            this.loadService.hide();
            this.realizarAtualizacaoGridChecklistAposRemocao(modalCloneInativarRemoverChecklistComponent);
        }, error => {
            if (error.error.mensagem) {
                this.modalCloneInativarRemover.msgError = error.error.mensagem;
            } else {
                this.modalCloneInativarRemover.msgError = error.message;
            }
            this.growlMessageService.showError("Validação", this.modalCloneInativarRemover.msgError);
            this.loadService.hide();
            throw error;
        });
    }


    /**
     * Inativa o checklist; atualizando sua data de inativação
     * @param gridChecklist 
     */
    atualizarDataInativacaoChecklist(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        this.loadService.show();
        this.checkListService.delete(modalCloneInativarRemoverChecklistComponent.cloneInativarRemoverChecklistDataInput.gridChecklist.id).subscribe(() => {
            this.realizarAtualizacaoGridChecklistAposInativacao(modalCloneInativarRemoverChecklistComponent);
            this.loadService.hide();
        }, error => {
            if (error.error.mensagem) {
                this.modalCloneInativarRemover.msgError = error.error.mensagem;
            } else {
                this.modalCloneInativarRemover.msgError = error.message;
            }
            this.loadService.hide();
            this.growlMessageService.showError("Validação", this.modalCloneInativarRemover.msgError);
            throw error;
        });
    }

    /**
     * Recupera o checklist para copia-lo
     * @param modalCloneInativarRemoverChecklistComponent 
     */
    recuperarChecklistParaClone(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        this.loadService.show();
        this.checkListService.getById(modalCloneInativarRemoverChecklistComponent.cloneInativarRemoverChecklistDataInput.gridChecklist.id).subscribe(updateChecklist => {
            this.checkListService.setCloneChecklist(this.inicializarCopiaChecklistClone(updateChecklist));
            this.realizarRotaParaCloneChecklist(modalCloneInativarRemoverChecklistComponent);
            this.loadService.hide();
        }, error => {
            if (error.status == 403) {
                this.growlMessageService.showError("Validação", error.error.mensagem);
            } else {
                this.growlMessageService.showError("Validação", JSON.stringify(error));
            }
            this.loadService.hide();
        });
    }

    /**
     * Fecha a modal clone inativar remover: devolvendo uma copia de checklist
     * conforme parametrização do usuario: basico, apontamentos e vinculacoes
     * @param modalCloneInativarRemoverChecklistComponent 
     */
    private realizarRotaParaCloneChecklist(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        modalCloneInativarRemoverChecklistComponent.dialogReturn =
            <CloneInativarRemoverChecklistOutput>{
                cloneInativarRemoverChecklistDataOutput: this.inicializarSaidaModalCloneInativarRemoverChecklist(modalCloneInativarRemoverChecklistComponent, false, false, true)

            };
        modalCloneInativarRemoverChecklistComponent.closeDialog();
    }

    /**
     * Fecha a modal clone inativar remover: apos remoção lógica checklist
     * atualizando grid checklist
     * @param modalCloneInativarChecklistComponent 
     */
    private realizarAtualizacaoGridChecklistAposInativacao(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        modalCloneInativarRemoverChecklistComponent.dialogReturn =
            <CloneInativarRemoverChecklistOutput>{
                cloneInativarRemoverChecklistDataOutput: this.inicializarSaidaModalCloneInativarRemoverChecklist(modalCloneInativarRemoverChecklistComponent, true, false, false)

            };
        modalCloneInativarRemoverChecklistComponent.closeDialog();
    }

    /**
     * Fecha a modal clone inativar remover: apos remoção física checklist
     * atualizando grid checklist
     * @param modalCloneInativarChecklistComponent 
     */
    private realizarAtualizacaoGridChecklistAposRemocao(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent) {
        modalCloneInativarRemoverChecklistComponent.dialogReturn =
            <CloneInativarRemoverChecklistOutput>{
                cloneInativarRemoverChecklistDataOutput: this.inicializarSaidaModalCloneInativarRemoverChecklist(modalCloneInativarRemoverChecklistComponent, false, true, false)

            };
        modalCloneInativarRemoverChecklistComponent.closeDialog();
    }

    /**
     * Monta a saida da modal clone inativar remover checklist
     * podendo ser uma inativação de checklist ou uma cópia de checklist: função clone
     * @param modalCloneInativarChecklistComponent 
     * @param inativarChecklist 
     * @param removerChecklist 
     * @param cloneChecklist 
     */
    private inicializarSaidaModalCloneInativarRemoverChecklist(modalCloneInativarRemoverChecklistComponent: ModalCloneInativarRemoverChecklistComponent, inativarChecklist: boolean, removerChecklist: boolean, cloneChecklist: boolean): CloneInativarRemoverChecklistDataOutput {
        const cloneInativarRemoverChecklistDataOutput: CloneInativarRemoverChecklistDataOutput = new CloneInativarRemoverChecklistDataOutput();
        cloneInativarRemoverChecklistDataOutput.id = modalCloneInativarRemoverChecklistComponent.cloneInativarRemoverChecklistDataInput.gridChecklist.id;
        cloneInativarRemoverChecklistDataOutput.checklist = cloneChecklist ? this.checkListService.getCloneChecklist() : undefined;
        cloneInativarRemoverChecklistDataOutput.inativarChecklist = inativarChecklist;
        cloneInativarRemoverChecklistDataOutput.removerChecklist = removerChecklist;
        cloneInativarRemoverChecklistDataOutput.cloneChecklist = cloneChecklist;
        return cloneInativarRemoverChecklistDataOutput;
    }

    /**
     * Realiza a validação da exclusão do checklist; verificando
     * a ocorrencia de vinculações e operações associadas.
     * Nesse caso será dada a opcao para o usuário clonar, remover ou inativar o checklist
     * @param cloneInativarRemoverChecklistDataInput 
     */
    private validarRemocaoChecklist(cloneInativarRemoverChecklistDataInput: CloneInativarRemoverChecklistDataInput) {
        if (cloneInativarRemoverChecklistDataInput.cloneChecklist) {
            this.modalCloneInativarRemover.msgTitleModalCloneInativarRemover = CHECKLIST.CHECKLIST_COPIA;
        } else {
            if (cloneInativarRemoverChecklistDataInput.gridChecklist.vinculacoes && cloneInativarRemoverChecklistDataInput.gridChecklist.vinculacoes.length > 0 && cloneInativarRemoverChecklistDataInput.gridChecklist.quantidade_associacoes > 0) {
                this.modalCloneInativarRemover.onlyCloneChecklist = true;
                this.modalCloneInativarRemover.msgTitleModalCloneInativarRemover = CHECKLIST.CHECKLIST_TITLE_OPERACOES;
                const msg: string = CHECKLIST.CHECKLIST_OPERACOES.replace('{id}', cloneInativarRemoverChecklistDataInput.gridChecklist.id.toString());
                this.modalCloneInativarRemover.msgInativarExcluirChecklist = msg;
            } else if (cloneInativarRemoverChecklistDataInput.gridChecklist.vinculacoes && cloneInativarRemoverChecklistDataInput.gridChecklist.vinculacoes.length > 0 && cloneInativarRemoverChecklistDataInput.gridChecklist.quantidade_associacoes == 0) {
                this.modalCloneInativarRemover.onlyCloneChecklist = false;
                this.modalCloneInativarRemover.msgTitleModalCloneInativarRemover = CHECKLIST.CHECKLIST_CONFIRMACAO_EXCLUSAO;
                const msg: string = CHECKLIST.CHECKLIST_REMOVER_SEM_VINCULACAO_ASSOCIACAO_OPERACAO.replace('{id}', cloneInativarRemoverChecklistDataInput.gridChecklist.id.toString());
                this.modalCloneInativarRemover.msgInativarExcluirChecklist = msg;
            } else if (!cloneInativarRemoverChecklistDataInput.gridChecklist.vinculacoes && cloneInativarRemoverChecklistDataInput.gridChecklist.quantidade_associacoes == 0) {
                this.modalCloneInativarRemover.msgTitleModalCloneInativarRemover = CHECKLIST.CHECKLIST_CONFIRMACAO_EXCLUSAO;
                const msg: string = CHECKLIST.CHECKLIST_REMOVER_SEM_ASSOCIACAO_OPERACAO.replace('{id}', cloneInativarRemoverChecklistDataInput.gridChecklist.id.toString());
                this.modalCloneInativarRemover.msgInativarExcluirChecklist = msg;
                this.modalCloneInativarRemover.onlyCloneChecklist = false;
            } else {
                this.modalCloneInativarRemover.msgTitleModalCloneInativarRemover = CHECKLIST.CHECKLIST_CONFIRMACAO_EXCLUSAO;
                this.modalCloneInativarRemover.msgInativarExcluirChecklist = CHECKLIST.CHECKLIST_REMOVER;
                this.modalCloneInativarRemover.onlyCloneChecklist = false;
            }
        }
    }

    /**
     * Utilização para fazer uma copia do checklist conforme 
     * parametrização informada pelo usuário
     * @param cloneChecklist 
     */
    private inicializarCopiaChecklistClone(cloneChecklist: Checklist): Checklist {
        const checklist: Checklist = new Checklist();
        if (this.modalCloneInativarRemover.cloneBasico) {
            checklist.nome = cloneChecklist.nome;
            checklist.unidade_responsavel = cloneChecklist.unidade_responsavel;
            checklist.verificacao_previa = cloneChecklist.verificacao_previa;
            checklist.orientacao_operador = cloneChecklist.orientacao_operador;
        }
        if (this.modalCloneInativarRemover.cloneApontamentos) {
            this.marcarCloneApontamento(cloneChecklist);
            checklist.apontamentos = cloneChecklist.apontamentos;

        }
        if (this.modalCloneInativarRemover.cloneVinculacoes) {
            this.marcarCloneVinculacao(cloneChecklist);
            checklist.vinculacoes = cloneChecklist.vinculacoes;
        }
        return checklist;
    }

    /**
     * marca apontamentos do checklist clonado
     * @param cloneChecklist 
     */
    private marcarCloneApontamento(cloneChecklist: Checklist) {
        cloneChecklist.apontamentos.forEach(cloneApontamento => {
            cloneApontamento.clone = true;
        });
    }

    /**
     * marca vinculações do checklist clonado
     * @param cloneChecklist 
     */
    private marcarCloneVinculacao(cloneChecklist: Checklist) {
        cloneChecklist.vinculacoes.forEach(cloneVinculacao => {
            cloneVinculacao.clone = true;
            cloneVinculacao.dataRevogacaoVinculacaoConflitante = cloneVinculacao.data_revogacao;
            cloneVinculacao.idChecklistAssociadoVinculacao = cloneChecklist.id;
            cloneVinculacao.idVinculacaoConflitante = cloneVinculacao.id;
        });
    }

}