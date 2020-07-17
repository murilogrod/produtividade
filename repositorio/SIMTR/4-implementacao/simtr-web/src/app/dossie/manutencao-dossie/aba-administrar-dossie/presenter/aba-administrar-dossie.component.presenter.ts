import { Injectable } from "@angular/core";
import { AbaAdministrarDossie } from "../model/aba-administrar-dossie.model";
import { Situacao } from "../model/situacao.interface";
import { DialogService } from "angularx-bootstrap-modal";
import { ModalAdministrarDossieComponent } from "../modal-administrar-dossie/view/modal-administrar-dossie.component";
import { TipoAdministracaoDossie } from "../model/tipo-administracao-dossie.enum";
import { AdministracaoDossieService } from "../service/administracao-dossie.service";
import { InclusaoSituacao } from "../model/inclusao-situacao.model";
import { AdministrarDossieOutput } from "../model/administrar-dossie-output";
import { AdministracaoDossieSituacao } from "src/app/dossie/manutencao-dossie/aba-administrar-dossie/model/administracao-dossie-situacao.enum";
import { AbaAdministrarDossieComponent } from "../view/aba-administrar-dossie.component";
import { RemoverUltimasSituacoes } from "../model/remover-ultimas-situacoes";
import { AtualizarUnidadeManipuladora } from "../model/atualizar-unidade-manipuladora";
import { MudancaSalvaService } from "src/app/services/mudanca-salva.service";
import { LoaderService } from "src/app/services";
import { Router } from "@angular/router";
import { ConfirmationService } from "primeng/primeng";


@Injectable()
export class AbaAdministrarDossieComponentPresenter {

    abaAdministrarDossie: AbaAdministrarDossie;

    constructor(private dialogService: DialogService,
        private administracaoDossieService: AdministracaoDossieService,
        private mudancaSalvaService: MudancaSalvaService,
        private router: Router,
        private confirmationService: ConfirmationService,
        private loadService: LoaderService) { }

    /**
     * inicializa dropdown com todas as situações do dossie
     */
    getSituacoesDossie() {
        let situacoes: Situacao[] = new Array<Situacao>();
        for (var situacao in AdministracaoDossieSituacao) {
            const key: string = JSON.parse(AdministracaoDossieSituacao[situacao]).key;
            const label: string = JSON.parse(AdministracaoDossieSituacao[situacao]).name;
            const value: string = JSON.parse(AdministracaoDossieSituacao[situacao]).code;
            situacoes.push({ key: key, name: label, code: value });
        }
        this.abaAdministrarDossie.situacoes = situacoes;
    }

    /**
     * Abre a modal de administracao do dossie
     * @param abaAdministrarDossieComponent 
     * @param tipoAdministracaoDossie 
     */
    openModalAdminDossie(abaAdministrarDossieComponent: AbaAdministrarDossieComponent, tipoAdministracaoDossie: string) {
        abaAdministrarDossieComponent.dossieProduto.tipoAdministracaoDossie = tipoAdministracaoDossie;
        abaAdministrarDossieComponent.dossieProduto.lastestSituations = this.abaAdministrarDossie.lastestSituations;
        this.dialogService.addDialog(ModalAdministrarDossieComponent, {
            dossieProduto: abaAdministrarDossieComponent.dossieProduto
        }).subscribe(administrarDossieOutput => {
            if (administrarDossieOutput) {
                this.onSucessModalAdministracaoDossie(abaAdministrarDossieComponent, administrarDossieOutput);
            }
        });
    }

    /**
     * Chamada de sucesso do retorno da modal de administracao do dossie
     * @param abaAdministrarDossieComponent 
     * @param administrarDossieOutput 
     */
    onSucessModalAdministracaoDossie(abaAdministrarDossieComponent: AbaAdministrarDossieComponent, administrarDossieOutput: AdministrarDossieOutput) {
        switch (administrarDossieOutput.tipoAdministracaoDossie) {
            case JSON.parse(TipoAdministracaoDossie.NOVA_SITUACAO).key:
                this.onSucessIncluirSituacaoModalAdminDossie(abaAdministrarDossieComponent, administrarDossieOutput);
                break;
            case JSON.parse(TipoAdministracaoDossie.REMOVER_SITUACAO).key:
                this.onSucessRemoverUltimasSituacoes(abaAdministrarDossieComponent, administrarDossieOutput);
                break;
            case JSON.parse(TipoAdministracaoDossie.ATUALIZAR_UNIDADES).key:
                this.onSucessAtualizarUnidadesManipuladoras(abaAdministrarDossieComponent, administrarDossieOutput);
                break;
            case JSON.parse(TipoAdministracaoDossie.EXCLUIR_DOSSIE).key:
                this.onSucessExcluirDossie(abaAdministrarDossieComponent);
                break;
        }
    }

    /**
     * Realiza a exclusão do Dossiê Produto
     * 
     */
    onSucessExcluirDossie(abaAdministrarDossieComponent: AbaAdministrarDossieComponent){
        this.administracaoDossieService.excluirDossieProduto(abaAdministrarDossieComponent.dossieProduto.id).subscribe(() => {
            abaAdministrarDossieComponent.addMessageSuccess("Dossiê de Produto excluído com sucesso. Você será direcionado para o Dashboard");
            setTimeout(() => {
                this.router.navigate(['dashboard']);
            },5000);


            // this.confirmationService.confirm({
            //     message: "Dossiê de Produto excluído com sucesso. Você será direcionado para o Dashboard",
            //     accept: () => {
            //         this.router.navigate(['dashboard']);
            //     }
            // });
        }, error => {
            abaAdministrarDossieComponent.addMessageError(error.error.mensagem);
            throw error;
        });
    }

    /**
     * Realiza a chamada para realizar o PUT para atualizar as unidades manipuladoras
     * @param abaAdministrarDossieComponent 
     * @param administrarDossieOutput 
     */
    onSucessAtualizarUnidadesManipuladoras(abaAdministrarDossieComponent: AbaAdministrarDossieComponent, administrarDossieOutput: AdministrarDossieOutput) {
        const atualizarUnidadeManipuladora: AtualizarUnidadeManipuladora = this.initAtualizarUnidadeManipuladora(abaAdministrarDossieComponent.dossieProduto);
        this.administracaoDossieService.atualizarUnidadesManipuladoras(atualizarUnidadeManipuladora).subscribe(() => {
            
            this.atualizarDossieProduto(administrarDossieOutput, abaAdministrarDossieComponent);
        }, error => {
            abaAdministrarDossieComponent.addMessageError(error.error.mensagem);
            throw error;
        });
    }

    /**
     * Realiza a chamada para realizar o delete para remover as últimas situações
     * @param abaAdministrarDossieComponent 
     * @param administrarDossieOutput 
     */
    onSucessRemoverUltimasSituacoes(abaAdministrarDossieComponent: AbaAdministrarDossieComponent, administrarDossieOutput: AdministrarDossieOutput) {
        const removerUltimasSituacoes: RemoverUltimasSituacoes = this.initRemoverUltimasSituacoes(abaAdministrarDossieComponent.dossieProduto);
        this.administracaoDossieService.removerUltimasSituacoes(removerUltimasSituacoes).subscribe(() => {
            abaAdministrarDossieComponent.addMessageSuccess("Situações removidas com sucesso !");
            this.atualizarDossieProduto(administrarDossieOutput, abaAdministrarDossieComponent);
        }, error => {
            abaAdministrarDossieComponent.addMessageError(error.error.mensagem);
            throw error;
        });
    }

    /**
     * Atualiza o dossie produto de acordo com a inclusão, remoção de uma nova situação ou manipulação das unidades manipuladoras
     * @param dossieProduto 
     * @param administrarDossieOutput 
     * @param abaAdministrarDossieComponent
     */
    atualizarDossieProduto(administrarDossieOutput: AdministrarDossieOutput, abaAdministrarDossieComponent: AbaAdministrarDossieComponent) {
        let limitMax: number = 0;
        if (administrarDossieOutput.tipoAdministracaoDossie.indexOf(JSON.parse(TipoAdministracaoDossie.NOVA_SITUACAO).key) == 0) {
            limitMax = this.abaAdministrarDossie.limitMax + 1;
        }
        if (administrarDossieOutput.tipoAdministracaoDossie.indexOf(JSON.parse(TipoAdministracaoDossie.REMOVER_SITUACAO).key) == 0) {
            limitMax = this.abaAdministrarDossie.limitMax - this.abaAdministrarDossie.lastestSituations;
        }
        abaAdministrarDossieComponent.loadDossieProdutoChanged.emit();
        this.loadService.show();
        this.mudancaSalvaService.setLimitMaxAdministrarDossie(limitMax);
        this.mudancaSalvaService.loadDossieProdutoAdmnistrarDossie.subscribe(limitMaxAdministrarDossie => {
            this.abaAdministrarDossie.lastestSituations = limitMaxAdministrarDossie;
            this.abaAdministrarDossie.limitMax = limitMaxAdministrarDossie
            this.loadService.hide();
        });
    }

    /**
     * Chamada de sucesso do retorno modal admin dossie para posterior requisição de nova situação
     * @param abaAdministrarDossieComponent 
     * @param administrarDossieOutput 
     */
    onSucessIncluirSituacaoModalAdminDossie(abaAdministrarDossieComponent: AbaAdministrarDossieComponent, administrarDossieOutput: AdministrarDossieOutput) {
        const inclusaoSituacao: InclusaoSituacao = this.initInclusaoSituacao(abaAdministrarDossieComponent.dossieProduto, administrarDossieOutput);
        this.administracaoDossieService.alterarSituacaoDossie(inclusaoSituacao).subscribe(() => {
            abaAdministrarDossieComponent.addMessageSuccess("Nova situação incluída com sucesso !");
            this.atualizarDossieProduto(administrarDossieOutput, abaAdministrarDossieComponent);
        }, error => {
            abaAdministrarDossieComponent.addMessageError(error.error.mensagem);
            throw error;
        });
    }

    /**
     * Monta o objeto para a requisicao de uma nova situacao de dossie
     * @param dossieProduto 
     * @param administrarDossieOutput 
     */
    initInclusaoSituacao(dossieProduto: any, administrarDossieOutput: AdministrarDossieOutput): InclusaoSituacao {
        const inclusaoSituacao: InclusaoSituacao = new InclusaoSituacao();
        inclusaoSituacao.id = dossieProduto.id;
        const jsonSelectedValueSituacao: any = this.abaAdministrarDossie.selectedSituacaoDossieProduto;
        const jsonAdministracaoDossieSituacao = AdministracaoDossieSituacao[String(jsonSelectedValueSituacao.key)];
        const administracaoDossieSituacaoApi: string = JSON.parse(jsonAdministracaoDossieSituacao).key;
        inclusaoSituacao.situacao = administracaoDossieSituacaoApi;
        inclusaoSituacao.observacao = administrarDossieOutput.razaoInclusaoSituacao;
        return inclusaoSituacao;
    }

    /**
     * Monta o objeto para a requisicao de remoção das ultimas situações do dossie produto
     * @param dossieProduto 
     */
    initRemoverUltimasSituacoes(dossieProduto: any): RemoverUltimasSituacoes {
        const removerUltimasSituacoes: RemoverUltimasSituacoes = new RemoverUltimasSituacoes();
        removerUltimasSituacoes.id = dossieProduto.id;
        removerUltimasSituacoes.quantidade = this.abaAdministrarDossie.lastestSituations;
        return removerUltimasSituacoes;
    }

    /**
     * Monta o objeto para a requisicao de atualização das unidade manipuladoras do dossie produto
     * @param dossieProduto 
     */
    initAtualizarUnidadeManipuladora(dossieProduto: any): AtualizarUnidadeManipuladora {
        const atualizarUnidadeManipuladora: AtualizarUnidadeManipuladora = new AtualizarUnidadeManipuladora();
        atualizarUnidadeManipuladora.id = dossieProduto.id;
        atualizarUnidadeManipuladora.unidades = this.abaAdministrarDossie.unidades;
        return atualizarUnidadeManipuladora;
    }

    /**
     * Adiciona a unidade em memória; aceitando apenas unidades distintas
     */
    adicionarUnidade() {
        if (this.abaAdministrarDossie.unidade > 0 && this.abaAdministrarDossie.unidade && !this.abaAdministrarDossie.unidades.find(unidade => unidade == this.abaAdministrarDossie.unidade)) {
            this.abaAdministrarDossie.unidades.push(this.abaAdministrarDossie.unidade);
        }
    }

    /**
     * Inicializa os dados de configuração da tela administracao dossie produto: dropdown e spinner.
     * @param dossieProduto 
     */
    initDataConfigAdminDossieProduto(dossieProduto: any) {
        this.getSituacoesDossie();
        this.getLimitMinHistoricoDossieProduto(dossieProduto);
        this.getLimitMaxHistoricoDossieProduto(dossieProduto);
    }

    /**
     * Remove a unidade em memória
     * @param i 
     */
    removerUnidade(i: number) {
        this.abaAdministrarDossie.unidades.splice(i, 1);
    }

    /**
     * Retorna o limite maximo de situações aptas para remoção
     * @param dossieProduto 
     */
    getLimitMaxHistoricoDossieProduto(dossieProduto: any) {
        this.abaAdministrarDossie.limitMax = dossieProduto.historico_situacoes.length > 10 ? 10 : dossieProduto.historico_situacoes.length - 1;
        if (this.abaAdministrarDossie.limitMax > 0) {
            this.abaAdministrarDossie.lastestSituations = 1;
        }
    }

    /**
     * Retorna o limite mínimo de situações aptas para remoção
     * @param dossieProduto 
     */
    getLimitMinHistoricoDossieProduto(dossieProduto: any) {
        this.abaAdministrarDossie.limitMin = dossieProduto.historico_situacoes.length > 1 ? 1 : 0;
    }

}