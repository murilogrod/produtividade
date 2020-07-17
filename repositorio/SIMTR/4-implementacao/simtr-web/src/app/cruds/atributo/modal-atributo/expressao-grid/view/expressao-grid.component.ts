import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from "@angular/core";
import { Utils } from "../../../../util/utils";
import { SortEvent, SelectItem } from 'primeng/primeng';
import { UtilsGrids } from "src/app/cruds/util/utils-grids";
import { GrowlMessageService } from "src/app/cruds/growl-message-service/growl-message.service";
import { ExpressaoGridModel } from '../model/expressao-grid.model';
import { ExpressaoGridConfigTable } from '../model/expressao-grid-config-table';

@Component({
    selector: 'expressao-grid',
    templateUrl: './expressao-grid.component.html'
  })
  export class ExpressaoGridComponent implements OnInit, OnChanges {

    @Input() expressao_interface: string;
    @Input() listaAtributos: Array<SelectItem>;
    listaExpressoes: Array<ExpressaoGridModel>;
    @Output() expressaoInterfaceChange: EventEmitter<string> = new  EventEmitter<string>();
    
    //incio toolTip
    toolTipFieldSetExressao: string = 'Atributo que armazena a expressão condicional a ser aplicada pelo Java script para determinar se o Atributo será exibido como um campo no formulário montado para extração de dados ou não. Por exemplo, através desta montagem didática de expressão condicional é possível dizer que este Atributo somente será exibido no formulário caso a resposta ao atributo "Gênero" for "Feminino", ou caso a resposta a um outro atributo específico for "Não". Leia o tooltip de cada campo para maiores detalhes.';
    toolTipAtributo: string = 'Atributo do Tipo de Documento que será utilizado para a validação condicional. Deve-se observar que a exibição do novo Atributo não será exatamente abaixo do Atibuto utilizado na condição, mas sim na Ordem estabelecida para ele dentro do formulário (opção "Odem" acima).';
    toolTipValorResposta: string = 'Valor esperado da resposta ao referido atributo para que o sistema possa exibir o novo Atributo no formulário.';
    toolTipAgrupamento: string = 'Agrupamento das expressões condicionais. Dentro de um agrupamento (mesmo número) as expressões são somadas (AND) para atender a condição de exibição no novo Atributo. Quando há mais de um grupo entende-se que há uma opção entre os grupos (cláusula OR), sendo que ao atender apenas um grupo o novo Atributo é exibido.';
    toolTipSemAtributos: string = 'Não há atributo previamente vinculado que permita fazer exibição condicional.';
    // fim toolTip

    idAtributo: number;
    valorResposta: string;
    agrupamento: number = 1;
    limiteAgrupamento: number = 1;
    isDisabledExpressao: boolean = false;

    rowsPerPageOptions: number[] = [];

    constructor(private growlMessageService: GrowlMessageService){}

    configuracaoTableColunasExpressao: ExpressaoGridConfigTable = new ExpressaoGridConfigTable();

    ngOnInit(){
        this.isDisabledExpressao = !this.listaAtributos || this.listaAtributos.length == 0 ? true : false;
        this.listaExpressoes = new Array<ExpressaoGridModel>();
       if(this.expressao_interface && this.expressao_interface !== ''){
            this.converteStringParaListaDeExpressao();
            this.descobriLimiteAgrupamento();
            this.custumizeRowsPerPageOptions();
       }
    }

    ngOnChanges() {}

    private custumizeRowsPerPageOptions() {
        this.rowsPerPageOptions = [];
        this.rowsPerPageOptions = Utils.custumizeRowsPerPageOptions(this.listaExpressoes);
    }

    descobriLimiteAgrupamento(){
        if(this.listaExpressoes && this.listaExpressoes.length > 0){
            let listaAgrupamentos: number [] = [];

            this.listaExpressoes.forEach(expressao => {

                if(listaAgrupamentos.length == 0 && expressao.agrupamento){
                    listaAgrupamentos.push(expressao.agrupamento);
                }else{
                    if(!listaAgrupamentos.some(elemento => elemento === expressao.agrupamento)){
                        listaAgrupamentos.push(expressao.agrupamento);
                    }
                }
            });

            this.limiteAgrupamento = listaAgrupamentos.length == 0 ? 1 : listaAgrupamentos.length + 1;

        }else{
            this.limiteAgrupamento = 1;
        }
    }

    reordenarExpressao(event: any) {
        this.reordernarExpressao(Number(event.dropIndex));
    }

    private reordernarExpressao(indexReference: number) {
        let newSequence: number = indexReference;
        while (newSequence > 0) {
            this.listaExpressoes[newSequence - 1];
            newSequence--;
        }
    }

    adicionarNovaExpressaoNaGrid(){
        if(this.idAtributo && this.idAtributo && this.valorResposta && this.agrupamento){
            
            let selectItem = this.listaAtributos.find(elemento => elemento.value == this.idAtributo);

            let expressao = new ExpressaoGridModel();
            expressao.id  = selectItem.value;
            expressao.nome = selectItem.label;
            expressao.valorResposta = this.valorResposta;
            expressao.agrupamento = this.agrupamento;
            
            this.listaExpressoes.push(expressao);

            this.listaExpressoes = Object.assign([], this.listaExpressoes);

            // limpar campos
            this.idAtributo = null;
            this.valorResposta = null;
            this.agrupamento = 1;

            this.converteListaDeExpressaoToString();
            this.descobriLimiteAgrupamento();
            this.custumizeRowsPerPageOptions();

        }else{
            this.growlMessageService.showError("Validação!", "Atributo, Valor Resposta e Agrupamento são obrigatórios!");
        }
    }

    removerExpressao(index: number, expressao: ExpressaoGridModel){
        if(expressao.id && expressao.valorResposta){
            let agrupamentoExcluido: number = this.listaExpressoes[index].agrupamento;
            this.listaExpressoes.splice(index, 1);

            this.listaExpressoes = Object.assign([], this.listaExpressoes);

            this.converteListaDeExpressaoToString();
            this.descobriLimiteAgrupamento();

            if(this.listaExpressoes.length > 0 && !this.listaExpressoes.some(expressao => agrupamentoExcluido > expressao.agrupamento)){
                this.converteStringParaListaDeExpressao();
            }
            
            this.custumizeRowsPerPageOptions();
        }
    }

    converteListaDeExpressaoToString(){
        if(this.listaExpressoes && this.listaExpressoes.length > 0){

            var json = Object.values(this.listaExpressoes.reduce((hashMap, expressao) => {
                                                                                           // saida do objeto 
                hashMap[expressao.agrupamento] = [...hashMap[expressao.agrupamento] || [], {campo_resposta: expressao.id, valor: expressao.valorResposta}];
               return hashMap;
            }, new Object()) );

            let expresaoCondicional = JSON.stringify(json);
            console.log(expresaoCondicional);

            this.expressao_interface = expresaoCondicional;
            this.expressaoInterfaceChange.emit(expresaoCondicional);
        }else{
            this.expressaoInterfaceChange.emit(null);
        }
    }

    converteStringParaListaDeExpressao(){
        let expressoes: any [] = JSON.parse(this.expressao_interface);
        this.listaExpressoes = new Array<ExpressaoGridModel>();
        console.log(expressoes);

        if(expressoes){
            for(let i = 0; i < expressoes.length; i++){
                let agrupamento = 1 + i;
                let listaRegras:any [] = expressoes[i];

                if(listaRegras && listaRegras.length > 0){
                    for(let regra of listaRegras){

                        let selectItem = this.listaAtributos.find(elemento => elemento.value == regra.campo_resposta);

                        if(selectItem){// se não encontra significa que o atributo da expressão foi excluido, então não deve ser adicionado na lista de expressão.
                            let expressao = new ExpressaoGridModel();
                            expressao.id  = selectItem.value;
                            expressao.nome = selectItem.label;
                            expressao.valorResposta = regra.valor;
                            expressao.agrupamento = agrupamento;
                            
                            this.listaExpressoes.push(expressao);
                        }

                    }
                }
            }

        }
    }
  }