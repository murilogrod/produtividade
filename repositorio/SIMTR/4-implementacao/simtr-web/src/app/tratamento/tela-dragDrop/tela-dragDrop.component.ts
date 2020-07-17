import { Component, OnInit, Input, AfterViewInit, OnChanges, AfterViewChecked, Output, EventEmitter} from '@angular/core';
import { Checklist } from 'src/app/model/checklist';
import { AlertMessageService, LoaderService } from 'src/app/services';
import { Msg_checklist } from './check-list-container/model/msg-checklist';
import { TIPO_MSG_TRATAMENTO } from 'src/app/constants/constants';
import { TelaDeTratamentoService } from '../tela-de-tratamento.service';

declare var $: any;

@Component({
  selector: 'tela-dragDrop',
  templateUrl: './tela-dragDrop.component.html',
  styleUrls: ['./tela-dragDrop.component.css']
})
export class TelaDragDropComponent extends AlertMessageService implements OnInit, OnChanges, AfterViewInit, AfterViewChecked {
    @Input() dossieProduto;
    @Input() processoGerador;
    @Input() processoFaseCheckList: number;
    @Input() idCheckListAtivado: number;
    @Output() listDocumentosImagemChanged: EventEmitter<any[]> = new EventEmitter<any[]>();
    @Input() listDocumentosImagens;
    @Input() listaDocumentacao;

    /** Indica quantos visualizadores de documento estão ativos. Origem: tratamento-dossie. */
    @Input() qtdeVisualizadores: number;

    @Input() layoutHorizontalA: boolean;
    @Input() layoutHorizontalB: boolean;
    @Input() layoutVerticalA: boolean;
    @Input() layoutVerticalB: boolean;
    @Input() hasMapListaCombo;
    @Input() listaChekList: Checklist[];

    /** Quando true, indica que o checklist em exibição é um checklist de fase. */
    exibindoChecklistFase: boolean;

    @Output() listaCheklistChanged: EventEmitter<Checklist[]> = new EventEmitter<Checklist[]>();

    visualizadorDois: boolean;
    alturaInicial: number;
    alturaInicialCorpo: number;
    alutraMaxHeigthImg: number;
    alturaMaxheigtChek: number;
    idCombo:number;
    listDocumentosImagensComparar: any[];
    showPortal: boolean;
    ocultarChecklist: boolean;
    msg_checkList: Msg_checklist = new Msg_checklist();
    ocultarMsg: boolean;
    tituloMsg = 'Orientação';

    constructor(private loadService: LoaderService,
      private telaDeTratamentoService: TelaDeTratamentoService) {
        super();
    }

    ngOnInit() {
      this.telaDeTratamentoService.exibindoChecklistFase.subscribe(
        exibindoChecklistFase => this.exibindoChecklistFase = exibindoChecklistFase);
    }

    ngOnChanges() {
      if(!this.showPortal) {
        this.ocultarChecklist = false;
        this.alteraralturaChecklistMaxheigth();
        this.liberandoVisualizar2();
      }
    }

    ngAfterViewChecked() {
      if(!this.showPortal) {
        this.initResize();
        this.liberandoVisualizar2(); 
      }
    }
    
    ngAfterViewInit() {      
      if(!this.showPortal) {
        this.liberandoVisualizar2(); 
      }      
        
    }

    private alteraralturaChecklistMaxheigth() {      
        this.alturaInicialCorpo = undefined == $('#checklist').height() ? 200 : this.alturaInicialCorpo == 200 ? $('#checklist').height() : this.alturaInicialCorpo;          
        if(this.layoutHorizontalA || this.layoutHorizontalB) {
            this.alturaMaxheigtChek = this.alturaInicialCorpo == 200 ? this.alturaInicialCorpo :  this.alturaInicialCorpo - 160;
        }else {
            this.alturaMaxheigtChek = (( this.alturaInicialCorpo * 2) - 160);
        }
    }

    collapse(idEvento: any) {
        let id_Principal = $("#"+idEvento+"").closest('[data-id]');  
        this.alturaInicial = undefined == this.alturaInicial ? $('.p-grid').height() : this.alturaInicial;      
        
        if($("#"+idEvento+" i").hasClass("fa-chevron-circle-up")) {
            //mostra
            this.mostrarMaisColapse(idEvento, id_Principal);
            this.acoesQuandoMostrarMaisColapsImg(id_Principal);            
            this.acoesQuandoMostrarMaisColapsCheckList(id_Principal);

        }else {
            // ocultar
            this.ocultarColapse(idEvento, id_Principal);
            this.acoesQuandoOcultarColapseNoChecklist(id_Principal);            
            this.acoesQuandoOcultarColapseNoImg(id_Principal);
        }
    }


    private ocultarColapse(idEvento: any, id_Principal: any) {
        $("#" + idEvento + " i").addClass("fa-chevron-circle-up").removeClass("fa-chevron-circle-down");
        $('#' + id_Principal.data('id') + '').addClass("minimizarZero");
    }

    private mostrarMaisColapse(idEvento: any, id_Principal: any) {
        $("#" + idEvento + " i").addClass("fa-chevron-circle-down").removeClass("fa-chevron-circle-up");
        $('#' + id_Principal.data('id') + '').removeClass("minimizarZero");
    }

    private acoesQuandoMostrarMaisColapsCheckList(id_Principal: any) {
        if (id_Principal.data('id') != "checklist") {
            $("#checklist").css('height', this.alturaInicial + "px");
        }
    }

    private acoesQuandoMostrarMaisColapsImg(id_Principal: any) {
        if (id_Principal.data('id') == "checklist") {
            this.alutraMaxHeigthImg = this.alturaInicial;
            $("#corpPrincipalVisualizadorImg_1").css('height', this.alutraMaxHeigthImg + "px");
            $("#corpPrincipalVisualizadorImg_2").css('height', this.alutraMaxHeigthImg + "px");
        }
    }

    private acoesQuandoOcultarColapseNoImg(id_Principal: any) {
        if (id_Principal.data('id') != "checklist") {
            $("#checklist").css('height', (this.alturaInicial * 2) + "px");
        }
    }

    private acoesQuandoOcultarColapseNoChecklist(id_Principal: any) {
        if (id_Principal.data('id') == "checklist") {
            this.alutraMaxHeigthImg = this.alturaInicial * 2;
            $("#corpPrincipalVisualizadorImg_1").css('height', this.alutraMaxHeigthImg + "px");
            $("#corpPrincipalVisualizadorImg_2").css('height', this.alutraMaxHeigthImg + "px");
        }
    }

    private liberandoVisualizar2() {
        $(".boxVisualizar").removeClass("ativoDuasVisualizacao");
        this.visualizadorDois = false;
        if (this.qtdeVisualizadores == 2) {
            $(".boxVisualizar").addClass("ativoDuasVisualizacao");
            this.visualizadorDois = true;
        }
    }

    handleMsgCheckList(input) {
      if(input && (input.tipo == TIPO_MSG_TRATAMENTO.TIPO_INEXISTENTE || input.tipo == TIPO_MSG_TRATAMENTO.TIPO_CONFORME)) {
        this.ocultarMsg = false;
        this.ocultarChecklist = true;
        this.msg_checkList = input;
        return;
      }
      this.ocultarMsg = true;
    }

    handleOcultarCheckList(input){
      this.ocultarChecklist = input;
    }

    handleChangeshowPortal(input){
        this.showPortal = input;
    }

    handleChangeListDocumentosImagem(input){
        this.listDocumentosImagemChanged.emit(Object.assign([], input));
    }

    handleChangeListaChekList(input) {
        this.listaChekList = (Object.assign([], input));
        this.listaCheklistChanged.emit(this.listaChekList);
    }
  
    handlleMessagesError(messages){
      this.alertMessagesErrorChanged.emit(messages);
    }
  
    handlleMessagesInfo(messages){
      this.alertMessagesInfoChanged.emit(messages);
    }
  
    handlleMessagesSucess(messages){
        this.alertMessagesSucessChanged.emit(messages);
    }

    // handleChangeShowPortalChanged(input){
    //   this.showPortalChanged.emit(Object.assign({}, input));
    // }

    initResize(){
        $.ui.plugin.add("resizable", "alsoResizeReverse", {

            start: function() {
              var that = $(this).resizable("instance"),
                o = that.options,
                _store = function(exp) {
                  $(exp).each(function() {
                    var el = $(this);
                    el.data("ui-resizable-alsoResizeReverse", {
                      width: parseInt((el.width() + 25), 10),
                      height: parseInt(el.height(), 10),
                      left: parseInt(el.css("left"), 10),
                      top: parseInt(el.css("top"), 10)
                    });
                  });
                };
          
              if (typeof(o.alsoResizeReverse) === "object" && !o.alsoResizeReverse.parentNode) {
                if (o.alsoResizeReverse.length) {
                  o.alsoResizeReverse = o.alsoResizeReverse[0];
                  _store(o.alsoResizeReverse);
                } else {
                  $.each(o.alsoResizeReverse, function(exp) {
                    _store(exp);
                  });
                }
              } else {
                _store(o.alsoResizeReverse);
              }
            },
          
            resize: function(event, ui) {
              var that = $(this).resizable("instance"),
                o = that.options,
                os = that.originalSize,
                op = that.originalPosition,
                delta = {
                  height: (that.size.height - os.height) || 0,
                  width: (that.size.width - os.width) || 0,
                  top: (that.position.top - op.top) || 0,
                  left: (that.position.left - op.left) || 0
                },
          
                _alsoResizeReverse = function(exp, c) {
                  $(exp).each(function() {
                    var el = $(this),
                      start = $(this).data("ui-resizable-alsoResizeReverse"),
                      style = {},
                      css = c && c.length ?
                      c :
                      el.parents(ui.originalElement[0]).length ? ["width", "height"] : ["width", "height", "top", "left"];
          
                    $.each(css, function(i, prop) {
                      var sum = (start[prop] || 0) - (delta[prop] || 0);
                      if (sum && sum >= 0) {
                        style[prop] = sum || null;
                      }
                    });
          
                    el.css(style);
                  });
                };
          
              if (typeof(o.alsoResizeReverse) === "object" && !o.alsoResizeReverse.nodeType) {
                $.each(o.alsoResizeReverse, function(exp, c) {
                  _alsoResizeReverse(exp, c);
                });
              } else {
                _alsoResizeReverse(o.alsoResizeReverse, null);
              }
            },
          
            stop: function() {
              $(this).removeData("resizable-alsoResizeReverse");
              
            }
          });
          $(function() {
          
            $(".boxResize_left").resizable({
              alsoResizeReverse: ".boxResize_right",
              maxWidth : 900,
              minWidth : 80
            });

            $(".boxResize_bottom").resizable({
              stop: function( event, ui ) {
                $('.box').css('heigth', '-webkit-fill-available');
                $(".visualizarImagem").css('max-height', ($(".corpBoxVizualizador ").height() - 160));
              }
            });

            $(".boxResize_top").resizable({
              stop: function( event, ui ) {
                $('.box').css('heigth', '-webkit-fill-available');
                $(".visualizarImagem").css('max-height', ($(".corpBoxVizualizador ").height() - 160));
              }
            });
          
          });
       
    }
}
