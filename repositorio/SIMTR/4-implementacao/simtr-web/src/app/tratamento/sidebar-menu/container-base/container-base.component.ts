import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter, AfterViewChecked, OnChanges, ChangeDetectorRef } from '@angular/core';
import { UNDESCOR, LOCAL_STORAGE_CONSTANTS, RADIO_TRATAMENTO_MENU } from '../../../constants/constants';
import { ApplicationService, AlertMessageService } from '../../../services';
import { Checklist } from 'src/app/model/checklist';
import { IdentificadorDossieFase } from '../../../dossie/manutencao-dossie/aba-formulario/modal/modal-pessoa/model/identificadorDossieEFase';
import { TelaDeTratamentoService } from '../../tela-de-tratamento.service';
declare var $: any;

@Component({
    selector: 'container-base',
    templateUrl: './container-base.component.html',
    styleUrls: ['./container-base.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ContainerBaseComponent extends AlertMessageService implements OnInit, AfterViewChecked, OnChanges {

    @Input() processoDossieReFerenciaPatriarca: any;
    @Input() dossieProduto: any;
    @Input() listaChekList: Checklist[];
    @Input() listaDocumentoObrigatorio: Checklist[];
    @Input() idDoPrimeiroInstanciaDocumentoASerCarregado: number;
    @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
    @Output() idCheckListAtivadoChanged: EventEmitter<any> = new EventEmitter<any>();
    @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();
    @Output() handleChangeExpandSingleAll: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Input() singleExpandAllDocuments: boolean;
    
    /** Indica se os checklists prévios estão sendo tratados. 
     * Somente após o tratamento dos checklists prévios os demais itens poderão ser tratados. */
    exibindoChecklistsPrevios: boolean;

	identificadorDossieFase: IdentificadorDossieFase = new IdentificadorDossieFase();
    listaRadio: any[];
    needApplyCssRadios: string;
    mostrarChecklist: boolean;
    mostrarFormulario: boolean;
    mostrarGarantias: boolean;
    mostrarPessoas: boolean;
    mostrarProdutos: boolean;
    garantiasMostrar: boolean;
    cssAplicado = false;
    cloneDossieProduto: any;

    constructor(
        private appService: ApplicationService,
        private cdRef: ChangeDetectorRef,
        private telaDeTratamentoService: TelaDeTratamentoService
    ) {
        super();
    }

    ngOnInit() {
        this.mostrarChecklist = true;
        this.mostrarFormulario = false;
        this.mostrarGarantias = false;
        this.mostrarPessoas = false;
        this.mostrarProdutos = false;
        this.garantiasMostrar = false;
        
        this.identificadorDossieFase.idDossie = this.dossieProduto.processo_dossie.id;
        this.identificadorDossieFase.idFase = this.dossieProduto.processo_fase.id;
        
        this.inicializarListaOpcaoRadio();

        const processoFilho = this.processoDossieReFerenciaPatriarca.processos_filho.find(processo => processo.id === this.dossieProduto.processo_fase.id);
        
        this.telaDeTratamentoService.exibindoChecklistsPrevios.subscribe(
            exibindoChecklistsPrevios => this.exibindoChecklistsPrevios = exibindoChecklistsPrevios);
    }

    ngOnChanges() {
    }

    ngAfterViewChecked() {
        $('input[type="radio"]').on('ifChecked', event => {
            if (this.needApplyCssRadios != event.target.value) {
                this.needApplyCssRadios = event.target.value;
                this.mostrarChecklist = this.needApplyCssRadios == RADIO_TRATAMENTO_MENU.CHECKLIST.toLocaleUpperCase();
                this.mostrarFormulario = this.needApplyCssRadios == RADIO_TRATAMENTO_MENU.FORMULARIO.toLocaleUpperCase();
                this.mostrarGarantias = this.needApplyCssRadios == RADIO_TRATAMENTO_MENU.GARANTIAS.toLocaleUpperCase();
                this.mostrarPessoas = this.needApplyCssRadios == RADIO_TRATAMENTO_MENU.PESSOAS.toLocaleUpperCase();
                this.mostrarProdutos = this.needApplyCssRadios == RADIO_TRATAMENTO_MENU.PRODUTOS.toLocaleUpperCase();
            }
        });
        if (!this.cssAplicado) {
            this.applyCSSRadios();
            this.cssAplicado = true;
        }
        this.cdRef.detectChanges();
    }

    private inicializarListaOpcaoRadio() {
        this.listaRadio = [];
        for (var [key, value] of Object.entries(RADIO_TRATAMENTO_MENU)) {
            let objRadio = {
                "id": key,
                "descricao": value,
                "checked": key == RADIO_TRATAMENTO_MENU.CHECKLIST.toLocaleUpperCase()
            };
            this.listaRadio.push(objRadio);
        }
        this.applyCSSRadios();
    }

    private applyCSSRadios() {
        this.styleRadiosCss();
    }

    private styleRadiosCss() {
        $('input[type="radio"]').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue'
        });
    }

    verificarTitular(tipoRelacionamento: string) {
        let listaTipoRelacionamento = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.dossieProduto.processo_dossie.id)).tipos_relacionamento;
        return listaTipoRelacionamento.some(x => x.principal == true && x.tipo_relacionamento == tipoRelacionamento);
    }

    showTooglePrincipal(): boolean {
        return ((undefined != this.dossieProduto.processo_dossie && (!this.exibindoChecklistsPrevios)) ||
            (undefined != this.dossieProduto.processo_fase && (!this.exibindoChecklistsPrevios)) ||
            (!this.exibindoChecklistsPrevios));
    }

    handleChangeListDocumentosImagem(input) {
        this.listDocumentosImagemChanged.emit(Object.assign([], input));
    }

    handlleChangeEventCheckList(input) {
        this.idCheckListAtivadoChanged.emit(input);
    }

    handleChangeListaChekList(input) {
        this.listaCheklistChanged.emit(Object.assign([], input));
    }

    handleChangeExpandAll(expandMultiple: boolean) {
        this.singleExpandAllDocuments = expandMultiple;
    }
}
