import { Injectable } from "@angular/core";
import { DadosDeclarados } from "../../../../../../model/dados-declarados";
import * as moment from 'moment';
import { Atributos } from "../../../../../../model/atributos";
import { LoaderService, EventService } from "../../../../../../services";
import { DadosDeclaradosModel } from "../model/dados-declarados.model";
import { ConsultaClienteService } from "../../../../service/consulta-cliente-service";
import { OpcaoSelecionadaInativa } from "../model/opcao-selecionada-inativa.model";
import { CEP_BASE, CEP_COMPLEMENTO, EVENTS, DADOS_DECLARADO, TIPO_DOCUMENTO } from 'src/app/constants/constants';
import { Utils } from "src/app/utils/Utils";
@Injectable()
export class ModalDadosDeclaradosPresenter {

  camposHidden: any[] = [];

  constructor(private loadService: LoaderService,
    private clienteService: ConsultaClienteService,
		private eventoService: EventService) { }

  model: DadosDeclaradosModel;

  carregarFormulario(): any {
    this.model.formularioValido = true;
    this.loadService.show();
    let documento = new DadosDeclarados();
    documento.atributos = [];

    this.model.dadosDeclarados.forEach(element => {

      //Carregar Dados declarados para enviar ao REST
      if ((element.indicador_obrigatorio && (!element.valor && (!element.opcoes_selecionadas || (element.opcoes_selecionadas && element.opcoes_selecionadas.length == 0)))) || element.campoInvalido) {
        this.model.formularioValido = false;
      } else if (element.valor || element.opcoes_selecionadas) {

        //Caso o campo estiver sido preenchido esse campo deve ser adicionado ao JSON a ser enviado para o REST
        let atributo = new Atributos();
        atributo.chave = element.nome_documento;
        atributo.valor = this.formatarValor(element.valor, element.tipo_campo);

        // Incializar o atributo opcoes selecionadas para atedenter o requisito do back-end de trata 
        //(Radio, Select e Checkbox) como campos multivaloraveis
        if (element.tipo_campo === "SELECT" || element.tipo_campo === TIPO_DOCUMENTO.INPUT_RADIO) {
          atributo.opcoes_selecionadas = [];
          atributo.opcoes_selecionadas.push(element.valor);
        } else if (element.tipo_campo === TIPO_DOCUMENTO.INPUT_CHECKBOX) {
          atributo.opcoes_selecionadas = [];
          atributo.opcoes_selecionadas = element.opcoes_selecionadas;
        }

        if (element.valor && (element.tipo_campo === TIPO_DOCUMENTO.CEP || element.tipo_campo === TIPO_DOCUMENTO.CEP_ONLINE)) {
          let atributoCepBase = new Atributos();
          let atributoCepComplemento = new Atributos();

          let cepBase = element.valor.substring(0, 5);
          let cepComplemento = element.valor.substring(5);

          let campoHiddeBase = this.retornaCampoHidden(element.nome_documento + CEP_BASE);
          if (campoHiddeBase) {
            atributoCepBase.chave = campoHiddeBase.nome_documento;
            atributoCepBase.valor = cepBase;
            documento.atributos.push(atributoCepBase);
          }

          let campoHiddenComplemento = this.retornaCampoHidden(element.nome_documento + CEP_COMPLEMENTO);
          if (campoHiddenComplemento) {
            atributoCepComplemento.chave = campoHiddenComplemento.nome_documento;
            atributoCepComplemento.valor = cepComplemento;
            documento.atributos.push(atributoCepComplemento);
          }
        }

        if(element.valor && element.tipo_campo === TIPO_DOCUMENTO.CONTA_CAIXA){
          atributo.valor = atributo.valor.replace(/\D/g, '');
        }

        documento.atributos.push(atributo);
      }
    });
    return documento;
  }


  /**
   * Ordena os campos do formulario dados declarados; considerando o a propriedade
   * nu_ordem tabela mtr.mtrtb45_atributo_extracao. Considerando a ordem crescente: menor para o maior
   * @param dadosDeclarados 
   */
  orderFieldsDadosDeclarados(dadosDeclarados: any) {
    this.model.dadosDeclarados = dadosDeclarados;
    this.model.dadosDeclarados = this.model.dadosDeclarados.sort(function (a, b) {
      if (a.ordem_apresentacao > b.ordem_apresentacao) {
        return 1;
      }
      if (a.ordem_apresentacao < b.ordem_apresentacao) {
        return -1;
      }
      return 0;
    });
  }

  /**
   * Remove atributos inativos para montar formulario dados declarados
   * gurandando os mesmos para aparesentação do histórico
   */
  removeAtributosInativosGuardaHistorico() {
    this.model.dadosDeclarados.forEach(dado => {
      dado.opcoesSelecionadasHistorico = this.inicializaOpcoesSelecionadasInativas(dado);
      dado.emptyHistoric = dado.opcoesSelecionadasHistorico.length > 0 ? false : true;
      dado.opcoes = dado.opcoes.filter(op => op.ativo);
    });
  }

  formatarValor(valor: any, tipo: any) {
    if (tipo === "DATE") {
      return moment(valor).format("DD/MM/YYYY");
    }
    return valor;
  }

  carregarModel() {
    this.clienteService.getDadosDeclaradosCliente(this.model.idDossie).subscribe(resposta => {
      if (this.model.dadosDeclarados) {

        this.model.dadosDeclarados.forEach(elemento => {
          elemento.campoInvalido = false;

          resposta.forEach(res => {
            if (res.chave == elemento.nome_documento) {
              if (elemento.tipo_campo === "DATE") {
                if (!res.valor) {
                  // Component apresenta erro sem uma data valida.
                  elemento.valor = new Date();
                } else {
                  elemento.valor = this.formatarValor(res.valor, res.tipo_campo);
                }
              } else if (elemento.tipo_campo === TIPO_DOCUMENTO.INPUT_CHECKBOX) {
                elemento.opcoes_selecionadas = res.opcoes_selecionadas ? res.opcoes_selecionadas : [];
              } else if (elemento.tipo_campo === TIPO_DOCUMENTO.SELECT) {
                elemento.valor = res.opcoes_selecionadas[0] ? res.opcoes_selecionadas[0] : "Selecione";
              } else if (elemento.tipo_campo === TIPO_DOCUMENTO.INPUT_RADIO) {
                elemento.valor = res.opcoes_selecionadas[0] ? res.opcoes_selecionadas[0] : res.valor;
              } else if(elemento.tipo_campo === TIPO_DOCUMENTO.MONETARIO){
                let valorMonetario: number = res.valor ? Number.parseFloat(res.valor) : null;
                elemento.valor = valorMonetario;
              } else if(elemento.tipo_campo === TIPO_DOCUMENTO.DECIMAL){
                elemento.valor = res.valor;
              } else if(elemento.tipo_campo === TIPO_DOCUMENTO.CONTA_CAIXA){
                let resposta: string = Utils.completarContaCaixaComZeroEsquerda(res.valor);
                elemento.valor = resposta;
              } else {
                elemento.valor = res.valor;
              }
              this.atualizarHistoticoDocumentoExistente(elemento, res);

            }

          });
          this.reinicializarHistoricoDocumentoExistente(resposta, elemento);
          
        });

        this.loadService.hide();
        
      }
    }, error => {
      this.reinicializarHistoricoNovoDocumento();
      this.loadService.hide();
      throw error;
    });
  }

  /**
   * Atualiza o histórico geral para documento existente que possui resposta
   * @param elemento 
   * @param res 
   */
  private atualizarHistoticoDocumentoExistente(elemento: any, res: any) {
    elemento.opcoesSelecionadasHistorico = elemento.opcoesSelecionadasHistorico.filter(opsh => opsh.key == res.chave && opsh.value == res.opcoes_selecionadas.filter(os => os == opsh.value)[0]);
    elemento.emptyHistoric = elemento.opcoesSelecionadasHistorico.length > 0 ? false : true;
  }

  /**
   * Limpa o histórico geral para documento existente que possui reposta
   * @param resposta 
   * @param elemento 
   */
  private reinicializarHistoricoDocumentoExistente(resposta: any, elemento: any) {
    if (resposta.length == 0) {
      elemento.opcoesSelecionadasHistorico = [];
      elemento.emptyHistoric = true;
    }
  }

  /**
   * Limpa o historico geral para um novo documento
   */
  private reinicializarHistoricoNovoDocumento() {
    this.model.dadosDeclarados.forEach(elemento => {
      elemento.opcoesSelecionadasHistorico = [];
      elemento.emptyHistoric = true;
    });
  }

  /**
   * Retorna a lista de opcoes atributos inativos para apresentacao do historico
   * @param dado 
   */
  private inicializaOpcoesSelecionadasInativas(dado: any): Array<OpcaoSelecionadaInativa> {
    let opcoesSelecionadasHistorico: Array<OpcaoSelecionadaInativa> = new Array<OpcaoSelecionadaInativa>();
    dado.opcoes.forEach(op => {
      if (!op.ativo) {
        let opcaoSelecionadaAnterior: OpcaoSelecionadaInativa = new OpcaoSelecionadaInativa();
        opcaoSelecionadaAnterior.key = dado.nome_documento;
        opcaoSelecionadaAnterior.value = op.valor;
        opcoesSelecionadasHistorico.push(opcaoSelecionadaAnterior);
      }
    });
    return opcoesSelecionadasHistorico;
  }
  /**
   * Os Campos do HIDDEN não deve aparecerem na tela, são campos de auxiliar que usuário não precisar saber.
   */
  separarCamposHidden() {
    let camposRemovidos = [];

    for (let i = 0; i < this.model.dadosDeclarados.length; i++) {
      let campo = this.model.dadosDeclarados[i];

      if (campo.tipo_campo == 'HIDDEN') {
        camposRemovidos.push(campo);
        this.camposHidden.push(campo);
      }
    }

    for (let i = 0; i < camposRemovidos.length; i++) {
      this.model.dadosDeclarados.splice(this.model.dadosDeclarados.indexOf(camposRemovidos[i]), 1);
    }

  }

  retornaCampoHidden(nome: string): any {
    return this.camposHidden.find((element) => {
      return element.nome_documento.toUpperCase() === nome.toUpperCase();
    });
  }

}