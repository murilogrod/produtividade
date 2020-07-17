import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from "@angular/core";
import { ModalOpcaoAtributoConfigTable } from "../model/modal-opcao-atributo-config-table";
import { Utils } from "../../../../util/utils";
import { SortEvent } from "primeng/primeng";
import { UtilsGrids } from "src/app/cruds/util/utils-grids";
import { ModalOpcaoAtributoModel } from "../model/modal-opcao-atributo-model";
import { GrowlMessageService } from "src/app/cruds/growl-message-service/growl-message.service";

@Component({
    selector: 'opcao-grid',
    templateUrl: './opcao-grid.component.html'
  })
  export class OpcaoGridComponent implements OnInit, OnChanges {

    @Input() listaOpcoes: Array<ModalOpcaoAtributoModel>;
    @Input() listaOpcoesExcluidas: Array<ModalOpcaoAtributoModel>; 

    @Output() opcoesChanged: EventEmitter<Array<ModalOpcaoAtributoModel>> = new EventEmitter<Array<ModalOpcaoAtributoModel>>();
    @Output() opcoesExcluidasChanged: EventEmitter<Array<ModalOpcaoAtributoModel>> = new EventEmitter<Array<ModalOpcaoAtributoModel>>();
    
    opcaoChave: string;
    opcaoValor: string;

    toolTipFieldSetOpcao: string = 'Opções pré-definidas para alguns Tipos de Atributo, como por exemplo para os casos de Atributo do Tipo List, Bool, etc.';
    toolTipOpcaoLabel: string = 'Valor da opção que será exibido para o usuario no campo do formulário.';
    toolTipOpcaoValue: string = 'Valor que será definido como value do campo e será enviado para fins de processamento no sistema.';

    constructor(private growlMessageService: GrowlMessageService){}

    configuracaoTableColunasOpcao: ModalOpcaoAtributoConfigTable = new ModalOpcaoAtributoConfigTable();
    rowsPerPageOptions: number[] = [];
    filteredRecords: number = 0;

    ngOnInit(){
        this.listaOpcoesExcluidas = [];
        if(this.listaOpcoes && this.listaOpcoes.length > 0){
            this.custumizeRowsPerPageOptions();
        }
    }

    ngOnChanges() {}

    /**
     * Custumiza o array de linhas por páginas; 
     * utilizando o padrão: 15, 30, 50 e último registro
     */
    private custumizeRowsPerPageOptions() {
        this.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.listaOpcoes);
    }

    realizarOrdenacaoOpcao(event: SortEvent) {
        UtilsGrids.customSort(event);
    }

    onFilter(event: any, globalFilter: any) {
        this.setCountFilterGlobal(event, globalFilter);
    }
    
    /**
     * Conta o numeros de registro atraves do filtro global
     * @param event 
     * @param globalFilter 
     */
    setCountFilterGlobal(event: any, globalFilter: any) {
        if (globalFilter.value.length > 0) {
            this.filteredRecords = event.filteredValue.length;
        } else {
            this.filteredRecords = 0;
        }
    }

    adicionarNovaLinhaNaGridOpcao(){
        if((this.opcaoChave && this.opcaoChave.trim().length > 0) &&
            (this.opcaoValor && this.opcaoValor.trim().length > 0)){
            
            let novaOpcao = new ModalOpcaoAtributoModel();
            novaOpcao.chave = this.opcaoChave; // VALUE
            novaOpcao.valor = this.opcaoValor; // LABEL
            
            let isChaveValueDuplicada = this.listaOpcoes.some(op => op.chave.toUpperCase().trim() == novaOpcao.chave.toUpperCase().trim());
            let isValorLabelDuplicada = this.listaOpcoes.some(op => op.valor.toUpperCase().trim() == novaOpcao.valor.toUpperCase().trim());

            if(isChaveValueDuplicada || isValorLabelDuplicada){

                let msg = "";
                if(isValorLabelDuplicada){
                    msg = "Label";
                }

                if(isChaveValueDuplicada){
                    msg += msg != "" ? " e Valor já existe!" : "Valor já existe!";
                }else{
                    msg = "Label já existe!";
                }

                this.growlMessageService.showError("Validação!", msg);
            }else{
                this.opcaoChave = null;
                this.opcaoValor = null;

                this.listaOpcoes.push(novaOpcao);
                this.listaOpcoes = Object.assign([], this.listaOpcoes);

                this.opcoesChanged.emit(this.listaOpcoes);
                this.custumizeRowsPerPageOptions();
            }

        }else{
            this.growlMessageService.showError("Validação!", "Label é Valor são obrigatórios!");
        }
    }

    removerOpcao(index: number, opcao: ModalOpcaoAtributoModel){
        if(opcao.id){
            this.listaOpcoesExcluidas.push(Object.assign({ModalOpcaoAtributoModel}, opcao));
            this.listaOpcoes.splice(index, 1);

            this.listaOpcoes = Object.assign([], this.listaOpcoes);

            this.opcoesChanged.emit(this.listaOpcoes);
            this.opcoesExcluidasChanged.emit(this.listaOpcoesExcluidas);
            this.custumizeRowsPerPageOptions();
        }else{
            this.listaOpcoes.splice(index, 1);

            this.listaOpcoes = Object.assign([], this.listaOpcoes);

            this.opcoesChanged.emit(this.listaOpcoes);
            this.custumizeRowsPerPageOptions();
        }
    }
  }