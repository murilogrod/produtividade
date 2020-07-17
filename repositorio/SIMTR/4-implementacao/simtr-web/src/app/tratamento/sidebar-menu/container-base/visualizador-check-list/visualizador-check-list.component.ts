import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { Checklist } from 'src/app/model/checklist';
import { TIPO_ARVORE_DOCUMENTO } from 'src/app/constants/constants';
import { LoaderService } from 'src/app/services';
import { UtilsTratamnto } from 'src/app/tratamento/utils/utils-tratamento';
import * as moment from 'moment';
import { TelaDeTratamentoService } from 'src/app/tratamento/tela-de-tratamento.service';
declare var $: any;

@Component({
    selector: 'visualizador-check-list',
    templateUrl: './visualizador-check-list.component.html',
    styleUrls: ['./visualizador-check-list.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class visualizadorCheckListComponent implements OnInit, OnChanges {
    @Input() checkListFaseAtualDossie: any[];
    @Input() listaDocumentoObrigatorio: Checklist[];
    listCheckListPrevioApresentacao: any[];
    listCheckListApresentacao: any[];
    @Output() idCheckListAtivadoChanged: EventEmitter<any> = new EventEmitter<any>();
    @Input() listaChekList: Checklist[];
    @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
    @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
    @Input() singleExpandAllDocuments: boolean;

    /** Indica se os checklists prévios estão sendo tratados. 
     * Somente após o tratamento dos checklists prévios os demais itens poderão ser tratados. */
    exibindoChecklistsPrevios: boolean;

    constructor(
        private loadService: LoaderService,
        private telaDeTratamentoService: TelaDeTratamentoService){
    }

    ngOnInit() {
        this.carregaCheckListsFasePrevios();
        this.carregaCheckListsFaseAtual();
        this.listaCheklistChanged.emit(Object.assign([], this.listaChekList));
        if(this.listCheckListPrevioApresentacao.length > 0 ){
            const checklist = this.listCheckListPrevioApresentacao.find(checklist => checklist.habilitaVerificacao); 
            this.habilitaAnaliseCheckList(checklist);
        }else if(this.listCheckListApresentacao.length > 0){
            const checklist = this.listCheckListApresentacao.find(checklist => checklist.habilitaVerificacao); 
            this.habilitaAnaliseCheckList(checklist);
        }
        
        this.telaDeTratamentoService.exibindoChecklistsPrevios.subscribe(
            exibindoChecklistsPrevios => this.exibindoChecklistsPrevios = exibindoChecklistsPrevios);
    }

    ngOnChanges() {
        this.verificaCheckListPreviaFoiInformado();
    }

    private verificaCheckListPreviaFoiInformado() {
        if (this.listaChekList.length > 0) {
            this.loadService.show();
            let existeCheckListFasePrevioRejeitado = this.listaChekList.some(checklist => checklist.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && checklist.checklistPrevio && (!checklist.listaResposta || checklist.existeIncomformidade));
            this.ativarDesativarCamposDocumentos(existeCheckListFasePrevioRejeitado);
            this.loadService.hide();
        }
    }

    private ativarDesativarCamposDocumentos(existeCheckListFasePrevioRejeitado: boolean) {
        if (!existeCheckListFasePrevioRejeitado) {
            this.telaDeTratamentoService.setExibindoChecklistsPrevios(false);
            return;
        }
        this.telaDeTratamentoService.setExibindoChecklistsPrevios(true);
    }

    private carregaCheckListsFasePrevios() {
        this.listCheckListPrevioApresentacao = [];
        let listaChecklistPrevio: any[] = this.checkListFaseAtualDossie.filter(checklist => this.isDataAtualMenorQueParametro(checklist.data_revogacao)  && checklist.verificacao_previa && undefined == checklist.funcao_documental && undefined == checklist.tipo_documento);
        this.telaDeTratamentoService.setExibindoChecklistsPrevios(false);
        for (let checklistPrevio of listaChecklistPrevio) {
            this.telaDeTratamentoService.setExibindoChecklistsPrevios(true);
            let checkListView: any = {};
            checkListView.idCheckList = checklistPrevio.id;
            checkListView.nome = checklistPrevio.nome;
            checkListView.habilitaVerificacao = true;
            checkListView.tipo = TIPO_ARVORE_DOCUMENTO.CHECKLIST;
            checkListView.data_revogacao = checklistPrevio.data_revogacao;
            checkListView.orientacao = checklistPrevio.orientacao_operador;
            this.listCheckListPrevioApresentacao.push(checkListView);
            this.addNaListaChecklistaDaFase(checklistPrevio);
        }
    }

    private carregaCheckListsFaseAtual() {
        this.listCheckListApresentacao = [];
        let listaChecklistFase: any[] = this.checkListFaseAtualDossie.filter(checklist => this.isDataAtualMenorQueParametro(checklist.data_revogacao)  && !checklist.verificacao_previa && undefined == checklist.funcao_documental && undefined == checklist.tipo_documento);
        for (let currentCheck of listaChecklistFase) {
            let checkListView: any = {};
            checkListView.idCheckList = currentCheck.id;
            checkListView.nome = currentCheck.nome;
            checkListView.habilitaVerificacao = true;
            checkListView.tipo = TIPO_ARVORE_DOCUMENTO.CHECKLIST;
            checkListView.data_revogacao = currentCheck.data_revogacao;
            this.listCheckListApresentacao.push(checkListView);
            this.addNaListaChecklistaDaFase(currentCheck);
        }
    }

    private isDataAtualMenorQueParametro(data: any):boolean{
        return (moment(new Date(data), 'DD/MM/YYYY').valueOf() > moment(new Date(), 'DD/MM/YYYY').valueOf());
    }

    /**
     * Quando começar a montar o menu de tratamento verifica se existe checklista da fase e inseri da lista de Check list
     */
    private addNaListaChecklistaDaFase(check: any) {
        this.listaChekList = this.listaChekList ? this.listaChekList : [];
        if (!this.listaChekList.some(ck => ck.idcheck == check.id && ck.data_revogacao == check.data_revogacao)) {
            let obj = new Checklist();
            obj.tipo = "CHECKLIST";
            obj.checklistPrevio = check.verificacao_previa ? true : false;
            obj.idVinculo = check.id;
            obj.idcheck = check.id;
            obj.nomeVinculo = check.nome;
            obj.habilitaVerificacao = true;
            obj.data_revogacao = check.data_revogacao;
            obj.orientacao = check.orientacao;
            this.listaChekList.push(obj);
        }
    }

    verificarDocumento(currentCheck: any) {
        for (let check of this.listaChekList) {
            if (currentCheck.tipo === TIPO_ARVORE_DOCUMENTO.CHECKLIST && check.idVinculo == currentCheck.idCheckList) {
                check.habilitaVerificacao = !check.habilitaVerificacao;
                currentCheck.ativo = check.habilitaVerificacao;
                break;
            }
        }
    }

    existeIncomformidadeDoc(checkApresentado: any) {
        let checkListDoc: Checklist = this.listaChekList.find(ck => ck.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && ck.idVinculo == checkApresentado.idCheckList && checkApresentado.data_revogacao == ck.data_revogacao);
        return UtilsTratamnto.verificaStatusDocumento(checkListDoc);
    }

    desabilitarChecklist(checkApresentado: any){
        let checkListDoc: Checklist = this.listaChekList.find(ck => ck.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && ck.idVinculo == checkApresentado.idCheckList && checkApresentado.data_revogacao == ck.data_revogacao);
        checkListDoc.habilitaVerificacao = !checkApresentado.habilitaVerificacao;
    }

    habilitaAnaliseCheckList(currentCheck: any) {
        let obj = {
            'id': currentCheck.idCheckList,
            'data': currentCheck.data_revogacao
        }
        this.idCheckListAtivadoChanged.emit(obj);
        let images: any[] = [];
        this.telaDeTratamentoService.setExibindoChecklistFase(true);
        this.listDocumentosImagemChanged.emit(Object.assign([], images));
    }

    alterarIcon(idToogle: any) {
        if ($("#" + idToogle + "").hasClass("openToogle")) {
            this.alterarIconParaAberto(idToogle);
        } else {
            this.alterarIconFechado(idToogle);
        }
    }

    verificarCkelistFase(listaDocumentoObrigatorio: any[]) {
        if (listaDocumentoObrigatorio && listaDocumentoObrigatorio.length > 0 && listaDocumentoObrigatorio.find(check => check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST)) {
            if(listaDocumentoObrigatorio.find(check => check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && (undefined == check.listaResposta || (check.listaResposta && check.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada))))){
                return true;
            }
            return false;
        }
    }

    verificarCkelistFasePrevia(listaDocumentoObrigatorio: any[]) {
        if (listaDocumentoObrigatorio && listaDocumentoObrigatorio.length > 0 && listaDocumentoObrigatorio.find(check => check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && check.checklistPrevio)) {
            if(listaDocumentoObrigatorio.find(check => check.tipo == TIPO_ARVORE_DOCUMENTO.CHECKLIST && check.checklistPrevio && (undefined == check.listaResposta || (check.listaResposta && check.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada))))){
                return true;
            }
            return false;
        }
    }

    private alterarIconFechado(idToogle: any) {
        $("#" + idToogle + "").addClass("openToogle");
        $("#" + idToogle + " .fa").removeClass("fa-caret-up").addClass("fa-caret-down");
    }

    private alterarIconParaAberto(idToogle: any) {
        $("#" + idToogle + "").removeClass("openToogle");
        $("#" + idToogle + " .fa").removeClass("fa-caret-down").addClass("fa-caret-up");
    }

    /**
     * Verificacao para apresentar checklist Fase.
     */
    showCheckListFase(): boolean {
        return !this.exibindoChecklistsPrevios;
    }
}