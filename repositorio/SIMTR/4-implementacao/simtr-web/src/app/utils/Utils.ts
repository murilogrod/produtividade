import { DialogService } from "angularx-bootstrap-modal/dist/dialog.service";
import { ModalConfirmComponent } from '../template/modal/modal-confirm/modal-confirm.component';
import { FuncaoDocumental } from '../model';
import { TipoDocumento } from '../model';
import { Documento } from '../model';
import { UtilsCliente } from '../cliente/consulta-cliente/utils/utils-client';
import { VinculoArvore } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { VinculoArvoreCliente } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-cliente';
import { VinculoArvoreProduto } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-produto';
import { VinculoArvoreGarantia } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-garantia';
import { VinculoArvoreProcesso } from '../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore-processo';
import { NodeAbstract } from '../model/model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-abstract.model';
import { ModalMessageComponent } from "../template/modal/modal-message/modal-message.component";
import { GRUPO_PERFIL, TIPO_PERFIL } from "../constants/constants";
declare var $: any;

export class Utils {

  static separadorDecimal: string = ',';
  static separadorMilhar: string = '.';
  static prefixo: string = '';

  

  static extraiHostUrl(urlScanner: string) {
    let host: string;
    if (urlScanner.indexOf("//") > -1) {
      host = urlScanner.split('/')[2];
    } else {
      host = host ? host.split('/')[0] : urlScanner;
    }
    host = host.split(':')[0];
    host = host.split('?')[0];
    return host;
  }

  static formatMoney(valor, places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : " ";
    thousand = thousand || ",";
    decimal = decimal || ".";
    let number = valor,
      negative = number < 0 ? "-" : "";
    let i: any = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "";
    let j: number;
    j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
  };

  static validarEmail(v: any) {
    const validEmailRegEx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (validEmailRegEx.test(v)) {
      return true;
    } else {
      return false;
    }
  }

  static mascTelefone(valor: string) {
    valor = valor.replace(/\D/g, "");
    valor = valor.replace(/^(\d{2})(\d)/g, "($1) $2");
    valor = valor.replace(/(\d)(\d{4})$/, "$1-$2");
    return valor;
  }

  static mascNis(valor: string) {
    valor = valor.replace(/\D/g, "");
    valor = valor.replace(/^(\d{3})(\d)/, "$1.$2");
    valor = valor.replace(/^(\d{3})\.(\d{5})(\d)/, "$1.$2.$3");
    valor = valor.replace(/(\d{3})\.(\d{5})\.(\d{2})(\d)/, "$1.$2.$3-$4");
    return valor;
  }

  static transformPrimeNGDateToInsert(value: any) {
    return value.getDate() + '/' + (value.getMonth() + 1) + '/' + value.getFullYear();
  }

  static checkDate(value: any): boolean {
    return !isNaN(Date.parse(value));
  }

  static pad(num: string, size: number): string {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
  }

  static leftPad(value: any, size: number, pad: string): string {
    let s = value + '';
    while (s.length < size) s = pad + s;
    return s;
  }

  static showMessageDialogMultipleMessages(dialogService: DialogService, messages: string[]) {
    return dialogService
      .addDialog(ModalMessageComponent, {
        title: '',
        msgs: messages
      });
  }

  static ordenarPorOrdemAlfabetica(lista: any[]) {
    lista.sort(function (a, b) {
      return a.nome < b.nome ? -1 : a.nome > b.nome ? 1 : 0;
    });
  }

  static ordenarPorOrdenaCodigoBacen(lista: any[]) {
    if (lista && lista.length > 0) {
      lista.sort(function (a, b) {
        return a.codigo_bacen < b.codigo_bacen ? -1 : a.codigo_bacen > b.codigo_bacen ? 1 : 0;
      });
    }
  }

  static showMessageDialog(dialogService: DialogService, message: string) {
    const msg: string[] = [];
    msg.push(message);
    return dialogService
      .addDialog(ModalMessageComponent, {
        title: '',
        msgs: msg
      });
  }

  static showMessageDialogTitle(dialogService: DialogService, title: string, messages: string[]) {
    return dialogService
      .addDialog(ModalMessageComponent, {
        title: title,
        msgs: messages
      });
  }

  /**
   * Retorna o nome da label do perfil
   */
  static getLabelRoleSelected(selectedRole: string): string {
    let label: string;
    switch (selectedRole) {
      case '5': label = GRUPO_PERFIL.TRATAMENTO;
        break;
      case '2': label = GRUPO_PERFIL.DOSSIE_CLIENTE;
        break;
      case '3': label = GRUPO_PERFIL.DOSSIE_PRODUTO;
        break;
      case '4': label = GRUPO_PERFIL.EXTRACAO_MANUAL;
        break;
      case '6': label = GRUPO_PERFIL.TES_MODAL_EXTRACAO;
        break;
      default:
        label = GRUPO_PERFIL.ADMIN;
    }
    return label;
  }

  /**
   * Retorna lista de perfis de acordo o perfil passado pelo usuario: função mock
   * @param role 
   */
  static getRolesByPerfil(role: string): any[] {
    let roles: any[];
    switch (role) {
      case GRUPO_PERFIL.TRATAMENTO: roles = TIPO_PERFIL.ROLE_TRATAMENTO;
        break;
      case GRUPO_PERFIL.DOSSIE_CLIENTE: roles = TIPO_PERFIL.ROLE_DOSSIE_CLIENTE;
        break;
      case GRUPO_PERFIL.DOSSIE_PRODUTO: roles = TIPO_PERFIL.ROLE_DOSSIE_PRODUTO;
        break;
      case GRUPO_PERFIL.EXTRACAO_MANUAL: roles = TIPO_PERFIL.ROLE_EXTRACAO_MANUAL;
        break;
      case GRUPO_PERFIL.TES_MODAL_EXTRACAO: roles = TIPO_PERFIL.ROLE_TEST_MODAL_EXTRACAO;
        break;
      default:
        roles = TIPO_PERFIL.ROLE_ADMIN;
    }
    return roles;
  }

  static showMessageConfirm(dialogService: DialogService, message: string) {
    const msg: string[] = [];
    msg.push(message);
    return dialogService
      .addDialog(ModalConfirmComponent, {
        title: '',
        msgs: msg
      });
  }

  //Método para ordenar Dossiê Produto
  static ordenarDossieProduto(A: any, B: any) {
    if (A.id_dossie_produto > B.id_dossie_produto) {
      return -1;
    }
    if (B.id_dossie_produto > A.id_dossie_produto) {
      return 1;
    }
    return 0;
  }

  //Método para remover acentos
  static removerAcentos(newStringComAcento: string) {
    let retorno = newStringComAcento;
    let mapaAcentosHex = {
      a: /[\xE0-\xE6]/g,
      A: /[\xC0-\xC6]/g,
      e: /[\xE8-\xEB]/g,
      E: /[\xC8-\xCB]/g,
      i: /[\xEC-\xEF]/g,
      I: /[\xCC-\xCF]/g,
      o: /[\xF2-\xF6]/g,
      O: /[\xD2-\xD6]/g,
      u: /[\xF9-\xFC]/g,
      U: /[\xD9-\xDC]/g,
      c: /\xE7/g,
      C: /\xC7/g,
      n: /\xF1/g,
      N: /\xD1/g,
    };
    for (const letra in mapaAcentosHex) {
      let expressaoRegular = mapaAcentosHex[letra];
      retorno = retorno.replace(expressaoRegular, letra);
    }
    return retorno;
  }


  //Método para ordenar a função documental pelo nome
  static ordenarFuncaoDocumental(A: FuncaoDocumental, B: FuncaoDocumental) {
    if (Utils.removerAcentos(A.nome) > Utils.removerAcentos(B.nome)) {
      return 1;
    }
    if (Utils.removerAcentos(B.nome) > Utils.removerAcentos(A.nome)) {
      return -1;
    }
    return 0;
  }

  //Método para ordenar o tipo de documento pelo nome
  static ordenarTiposDocumentos(A: TipoDocumento, B: TipoDocumento) {
    if (Utils.removerAcentos(A.nome) > Utils.removerAcentos(B.nome)) {
      return 1;
    }
    if (Utils.removerAcentos(B.nome) > Utils.removerAcentos(A.nome)) {
      return -1;
    }
    return 0;
  }

  static ordenarDocumentosNodeByLabel(A: NodeAbstract, B: NodeAbstract) {
    if (Utils.removerAcentos(A.label) > Utils.removerAcentos(B.label)) {
      return 1;
    }
    if (Utils.removerAcentos(B.label) > Utils.removerAcentos(A.label)) {
      return -1;
    }
    return 0;
  }

  static ordeNarTadaVerificacaoDecrescente(a: any, b: any) {
    if (Utils.formataHora(a.data_hora_verificacao) > Utils.formataHora(b.data_hora_verificacao)) {
      return 1;
    }

    if (Utils.formataHora(a.data_hora_verificacao) < Utils.formataHora(b.data_hora_verificacao)) {
      return -1;
    }
    return 0;
  }

  //Método para ordenar documento a partir da data e hora 
  static ordenarDocumentos(A: Documento, B: Documento) {
    let arrayA = [];
    let arrayB = [];
    arrayA[0] = A.data_hora_captura;
    arrayB[0] = UtilsCliente.converteData(B.data_hora_captura);
    arrayA[1] = Utils.formataHora(A.hora_captura);
    arrayB[1] = Utils.formataHora(B.hora_captura);
    return Utils.retornaOrdenacao(arrayA, arrayB);
  }


  //Método para ordenar documento a partir da data e hora 
  static ordenarDocumentosNodeByDataHora(A: NodeAbstract, B: NodeAbstract) {
    let arrayA = A.label.split(" ");
    let arrayB = B.label.split(" ");
    return Utils.retornaOrdenacao(arrayA, arrayB);
  }

  private static retornaOrdenacao(arrayA, arrayB) {
    arrayB[0] = UtilsCliente.converteData(arrayB[0]);
    arrayA[1] = Utils.formataHora(arrayA[1]);
    arrayB[1] = Utils.formataHora(arrayB[1]);
    if (Utils.verificarData(arrayA[0], arrayB[0]) == 0) {
      if (arrayA[1].localeCompare(arrayB[1]) > 0) {
        return -1;
      } else {
        return 1;
      }
    } else if (Utils.verificarData(arrayA[0], arrayB[0]) > 0) {
      return -1;
    } else if (Utils.verificarData(arrayA[0], arrayB[0]) < 0) {
      return 1;
    } else {
      return 0;
    }
  }

  /*Método para verificar a dataInicio:
      Maior: return 1
      Menor: return -1
      Igual: return 0
  */
  private static verificarData(dataInicio, dataFim) {
    const dataBanco = UtilsCliente.converteData(dataInicio);
    if (Date.parse(dataBanco) == Date.parse(dataFim)) {
      return 0;
    } else if (Date.parse(dataBanco) > Date.parse(dataFim)) {
      return 1;
    } else if (Date.parse(dataBanco) < Date.parse(dataFim)) {
      return -1;
    }
  }

  //Método para formatar hora do tipo 10:20:20 para 102020
  private static formataHora(hora: String) {
    return hora.replace(/:/g, "");
  }

  //Transformar true -> 1 e false -> 0
  public static transformarBoolParaNumero(booleano: String) {
    if (booleano === "true") {
      return 1;
    } else {
      return 0;
    }
  }

  static hex(c) {
    var s = "0123456789abcdef";
    var i = parseInt(c);
    if (i == 0 || isNaN(c))
      return "00";
    i = Math.round(Math.min(Math.max(0, i), 255));
    return s.charAt((i - i % 16) / 16) + s.charAt(i % 16);
  }

  static convertToHex(rgb) {
    return this.hex(rgb[0]) + this.hex(rgb[1]) + this.hex(rgb[2]);
  }

  static trim(s) { return (s.charAt(0) == '#') ? s.substring(1, 7) : s }

  static convertToRGB(hex) {
    var color = [];
    color[0] = parseInt((this.trim(hex)).substring(0, 2), 16);
    color[1] = parseInt((this.trim(hex)).substring(2, 4), 16);
    color[2] = parseInt((this.trim(hex)).substring(4, 6), 16);
    return color;
  }

  static generateColor(colorStart, colorEnd, colorCount) {

    var start = this.convertToRGB(colorStart);

    var end = this.convertToRGB(colorEnd);

    var len = colorCount;

    var alpha = 0.0;

    var saida = [];

    for (let i = 0; i < len; i++) {
      var c = [];
      alpha += (1.0 / len);

      c[0] = start[0] * alpha + (1 - alpha) * end[0];
      c[1] = start[1] * alpha + (1 - alpha) * end[1];
      c[2] = start[2] * alpha + (1 - alpha) * end[2];

      saida.push(this.convertToHex(c));

    }

    return saida;

  }

  static ordenacaoAlfabetica(): (a: any, b: any) => number {
    return function (a, b) {
      if (a.nome < b.nome)
        return -1;
      if (a.nome > b.nome)
        return 1;
      return 0;
    };
  }

  static ordenacaoAlfabeticaInstanciaArvore(): (a: VinculoArvore, b: VinculoArvore) => number {
    return function (a, b) {
      let x, y;
      x = Utils.verificarInstanciaArvore(a, x, null);
      y = Utils.verificarInstanciaArvore(b, y, null);
      if (x < y)
        return -1;
      if (x > y)
        return 1;
      return 0;
    };
  }
  /**
   * 
   * @param arraySequencial Ordena Vetor em Ordem Crescente
   */
  static ordenaArrayOrdemCrescente(arraySequencial: any[]) {
    if (arraySequencial.length > 1) {
      for (var i = 1; i < arraySequencial.length; i++) {
        for (var j = 0; j < i; j++) {
          if (arraySequencial[i] < arraySequencial[j]) {
            var aux = arraySequencial[i];
            arraySequencial[i] = arraySequencial[j];
            arraySequencial[j] = aux;
          }
        }
      }
    }
  }

  static verificarInstanciaArvore(vinculoArvore: VinculoArvore, p: any, identifica: any) {
    if (vinculoArvore instanceof VinculoArvoreCliente) {
      identifica = undefined != vinculoArvore.vinculoCliente.cnpj ? vinculoArvore.vinculoCliente.cnpj : vinculoArvore.vinculoCliente.cpf;
      p = vinculoArvore.vinculoCliente.tipo_relacionamento && vinculoArvore.vinculoCliente.tipo_relacionamento.nome ? vinculoArvore.vinculoCliente.tipo_relacionamento.nome : "";
      p = p + (identifica != null ? " - " + identifica : "");
    }
    if (vinculoArvore instanceof VinculoArvoreProduto) {
      identifica = vinculoArvore.vinculoProduto.codigo_operacao;
      p = vinculoArvore.vinculoProduto.nome + (identifica != null ? " - " + identifica : "");
    }
    if (vinculoArvore instanceof VinculoArvoreGarantia) {
      identifica = undefined != vinculoArvore.vinculoGarantia.descricao ? vinculoArvore.vinculoGarantia.descricao : "";
      identifica = undefined != vinculoArvore.vinculoGarantia.relacionado ? "" != identifica ? identifica + " - " + vinculoArvore.vinculoGarantia.relacionado : vinculoArvore.vinculoGarantia.relacionado : "" != identifica ? identifica : "";
      p = vinculoArvore.vinculoGarantia.nome + (identifica != "" ? " - " + identifica : "");
    }
    if (vinculoArvore instanceof VinculoArvoreProcesso) {
      p = vinculoArvore.nome;
    }
    return p;
  }

  /**
   * Converte base64 para array de bytes; devolvendo o link para download
   * @param base64 
   */
  static convertBase64ArrayBytesDownload(base64: any) {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: "pdf" });
    const urlDownload = URL.createObjectURL(blob);
    return urlDownload;
  }

  /**
   * retira a formatação do CPF
   * Ex: 000.000.000-00 / 00000000000
   * @param value 
   */
  static retiraMascaraCPF(value: string) {
    return value.replace(/\D/g, "");
  }


  static masKcpfCnpj(v) {
    //Remove tudo o que não é dígito
    v = v.replace(/\D/g, "");

    if (v.length <= 11) { //CPF
      return Utils.maskCpf(v);
    } else { //CNPJ
      return Utils.maskCnpj(v);
    }
  }

  public static maskCnpj(v: any) {
    //Coloca ponto entre o segundo e o terceiro dígitos
    v = v.replace(/^(\d{2})(\d)/, "$1.$2");
    //Coloca ponto entre o quinto e o sexto dígitos
    v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
    //Coloca uma barra entre o oitavo e o nono dígitos
    v = v.replace(/\.(\d{3})(\d)/, ".$1/$2");
    //Coloca um hífen depois do bloco de quatro dígitos
    v = v.replace(/(\d{4})(\d)/, "$1-$2");
    return v;
  }

  public static maskCpf(v: any) {
    //Coloca um ponto entre o terceiro e o quarto dígitos
    v = v.replace(/(\d{3})(\d)/, "$1.$2");
    //Coloca um ponto entre o terceiro e o quarto dígitos
    //de novo (para o segundo bloco de números)
    v = v.replace(/(\d{3})(\d)/, "$1.$2");
    //Coloca um hífen entre o terceiro e o quarto dígitos
    v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    return v;
  }

  static verificarBooleanString(icTipo: string): boolean {
    return icTipo === 'true' ? true : false;
  }

  static mascaraData(event) {

    event.target.value = event.target.value.replace("//", "/").replace(/[^\d,\/]/g, "");
    let pass = event.target.value;
    let expr = /[0123456789]/;

    for (let i = 0; i < pass.length; i++) {
      // charAt -> retorna o caractere posicionado no índice especificado
      var lchar = event.target.value.charAt(i);
      var nchar = event.target.value.charAt(i + 1);

      if (i == 0) {
        // search -> retorna um valor inteiro, indicando a posição do inicio da primeira
        // ocorrência de expReg dentro de instStr. Se nenhuma ocorrencia for encontrada o método retornara -1
        // instStr.search(expReg);
        if ((lchar.search(expr) != 0) || (lchar > 3)) {
          event.target.value = "";
        }

      } else if (i == 1) {

        if (lchar.search(expr) != 0) {
          // substring(indice1,indice2)
          // indice1, indice2 -> será usado para delimitar a string
          var tst1 = event.target.value.substring(0, (i));
          event.target.value = tst1;
          continue;
        }

        if ((nchar != '/') && (nchar != '')) {
          var tst1 = event.target.value.substring(0, (i) + 1);

          if (nchar.search(expr) != 0)
            var tst2 = event.target.value.substring(i + 2, pass.length);
          else
            var tst2 = event.target.value.substring(i + 1, pass.length);

          event.target.value = tst1 + '/' + tst2;
        }

      } else if (i == 4) {

        if (lchar.search(expr) != 0) {
          var tst1 = event.target.value.substring(0, (i));
          event.target.value = tst1;
          continue;
        }

        if ((nchar != '/') && (nchar != '')) {
          var tst1 = event.target.value.substring(0, (i) + 1);

          if (nchar.search(expr) != 0)
            var tst2 = event.target.value.substring(i + 2, pass.length);
          else
            var tst2 = event.target.value.substring(i + 1, pass.length);

          event.target.value = tst1 + '/' + tst2;
        }
      }

      if (i >= 6) {
        if (lchar.search(expr) != 0) {
          var tst1 = event.target.value.substring(0, (i));
          event.target.value = tst1;
        }
      }
    }

    if (pass.length > 10) {
      event.target.value = event.target.value.substring(0, 10);
    }
    return event.path[0].value;
  }

  static removerTodosCaracteresEspeciais(especialChar) {
    return especialChar ? especialChar.replace(/[^\w\s]/gi, '') : "";
  }

  /**
   * Verifica formato cep válido
   * padrão 99999-99
   * @param cep 
   */
  static matchCep(cep: string): boolean {
    const cepRegex: any = /[0-9]{5}-[\d]{3}/g;
    return cepRegex.test(cep);
  }


  /**
   * Busca o valor de um parâmetro de uma url
   * ex: teste?param=value
   * property = param
   * return value
   * @param url 
   * @param property 
   * @param value 
   */
  static getParamInUrl(url: string, property: string, value: string): boolean {
    var obj = new Array();
    var arr = url.split(property);
    if (arr.length > 1) {
      var temp = arr[1].split('=');
      obj[property] = temp[1];
    }
    if (obj[property] == value) {
      return true;
    } else {
      return false;
    }
  }

  static formataDataComBarra(data: string): any {

    if (data) {
      let value = data.toString();
      value = data.replace(/\D/g, '')

      if (value.length == 8) {
        return value.substring(0, 2).concat('/')
          .concat(value.substring(2, 4))
          .concat('/')
          .concat(value.substring(4, 8));

      }
    }

    return null;
  }

  static verificarCNPJ(strCnpj): boolean {

    strCnpj = strCnpj.replace(/[^\d]+/g, '');

    if (strCnpj == '') return false;

    if (strCnpj.length != 14)
      return false;

    // Elimina CNPJs invalidos conhecidos
    if (strCnpj == "00000000000000" ||
      strCnpj == "11111111111111" ||
      strCnpj == "22222222222222" ||
      strCnpj == "33333333333333" ||
      strCnpj == "44444444444444" ||
      strCnpj == "55555555555555" ||
      strCnpj == "66666666666666" ||
      strCnpj == "77777777777777" ||
      strCnpj == "88888888888888" ||
      strCnpj == "99999999999999")
      return false;

    // Valida DVs
    let tamanho = strCnpj.length - 2
    let numeros = strCnpj.substring(0, tamanho);
    let digitos = strCnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
        pos = 9;
    }
    let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0))
      return false;

    tamanho = tamanho + 1;
    numeros = strCnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
        pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(1))
      return false;

    return true;

  }

  static verificarCPF(strCpf: string): boolean {
    if (!/[0-9]{11}/.test(strCpf)) return false;
    if (strCpf === "00000000000") return false;

    let soma = 0;

    for (let i = 1; i <= 9; i++) {
      soma += parseInt(strCpf.substring(i - 1, i)) * (11 - i);
    }

    let resto = soma % 11;

    if (resto === 10 || resto === 11 || resto < 2) {
      resto = 0;
    } else {
      resto = 11 - resto;
    }

    if (resto !== parseInt(strCpf.substring(9, 10))) {
      return false;
    }

    soma = 0;

    for (let i = 1; i <= 10; i++) {
      soma += parseInt(strCpf.substring(i - 1, i)) * (12 - i);
    }
    resto = soma % 11;

    if (resto === 10 || resto === 11 || resto < 2) {
      resto = 0;
    } else {
      resto = 11 - resto;
    }

    if (resto !== parseInt(strCpf.substring(10, 11))) {
      return false;
    }

    return true;
  }

  /**
   * Recebe um valor retira os acentos e espacoes e devolve em caixa alta
   * @param value 
   */
  static removeAccentSpaceUpperCase(value): string {
    value = value.trim().replace(/ /g, '');
    value = value.replace(new RegExp('[ÁÀÂÃ]', 'gi'), 'a');
    value = value.replace(new RegExp('[ÉÈÊ]', 'gi'), 'e');
    value = value.replace(new RegExp('[ÍÌÎ]', 'gi'), 'i');
    value = value.replace(new RegExp('[ÓÒÔÕ]', 'gi'), 'o');
    value = value.replace(new RegExp('[ÚÙÛ]', 'gi'), 'u');
    value = value.replace(new RegExp('[Ç]', 'gi'), 'c');
    value = value.toUpperCase();
    return value;
  }

  static aplicarMascaraMonetario(valorConverter: string): string {
    const valorNum = parseInt(valorConverter.replace(/\D/g, ''), 10);
    let valorMask = '';
    let valor: string;

    if (isNaN(valorNum)) {
      return '';
    }

    valor = valorNum.toString();
    switch (valor.length) {
       case 1:
         valorMask = '0' + this.separadorDecimal + 
           '0' + valor;
         break;
       case 2:
         valorMask = '0' + this.separadorDecimal + valor;
         break;
       case 3:
         valorMask = valor.substr(0, 1) + this.separadorDecimal + 
           valor.substr(1, 2);
         break;
       default:
         break;
     }

    if (valorMask === '') {
      let sepMilhar = 0;
      for (let i = (valor.length - 3); i >= 0; i--) {
        if (sepMilhar === 3) {
          valorMask = this.separadorMilhar + valorMask;
          sepMilhar = 0;
        }
        valorMask = valor.charAt(i) + valorMask;
        sepMilhar++;
      }
      valorMask = valorMask + this.separadorDecimal + 
        valor.substr(valor.length - 2, 2);
    }

    if (this.prefixo !== '') {
      valorMask = this.prefixo + ' ' + valorMask;
    }
    
    return valorMask;
  }

  static validarKeyupTipoCampoContaCaixa(value: string):string{
    // formato contas
    // SIDEC: AAAA.PPP.CCCCCCCC-D
    // NSGD:  AAAA.PPPP.CCCCCCCCCCCC-D 

    let tamanhoMaximoSidec = 16;
    let tamanhoMaximoNsgd = 21;
    let regexLimpar = /\D/g;

    if(value && value !== ''){

      let valorLimpo = value.replace(regexLimpar, '');
      let valorRetorno: string = '';
      let tamanho: number = valorLimpo.length;

      if(tamanho == 5){
        valorRetorno = valorLimpo.substring(0, 4);
        valorRetorno += '.';
        valorRetorno += valorLimpo.substring(4, 5);

      }else if(tamanho > 5 && tamanho <= 8){

        valorRetorno = valorLimpo.substring(0, 4);
        valorRetorno += '.';
        
        if(tamanho == 8){
          let tipoConta: string = valorLimpo.substring(4, 7);

          if(this.isContaSidec(tipoConta)){
            valorRetorno += tipoConta;
            valorRetorno += '.';
            valorRetorno += valorLimpo.substring(7, tamanho);
          }else{
            valorRetorno += valorLimpo.substring(4, tamanho);
          }

        }else{
          valorRetorno += valorLimpo.substring(4, tamanho);
        }

      }else if(tamanho > 8){

        valorRetorno = valorLimpo.substring(0, 4);
        valorRetorno += '.';
        
        let tipoConta: string = valorLimpo.substring(4, 7);
        
        if(this.isContaSidec(tipoConta)){
          tamanho = tamanho > tamanhoMaximoSidec ? tamanhoMaximoSidec : tamanho;

          if(tamanho > 15){
            valorRetorno += tipoConta;
            valorRetorno += '.';
            valorRetorno += valorLimpo.substring(7, 15);
            valorRetorno += '-';
            valorRetorno += valorLimpo.substring(15, 16);
          }else{
            valorRetorno += tipoConta;
            valorRetorno += '.';
            valorRetorno += valorLimpo.substring(7, tamanho);
          }

        }else{
          tamanho = tamanho > tamanhoMaximoNsgd ? tamanhoMaximoNsgd : tamanho;

          if(tamanho > 20){
            valorRetorno += valorLimpo.substring(4, 8);
            valorRetorno += '.';
            valorRetorno += valorLimpo.substring(8, 20);
            valorRetorno += '-';
            valorRetorno += valorLimpo.substring(20, 21);
          }else{
            valorRetorno += valorLimpo.substring(4, 8);
            valorRetorno += '.';
            valorRetorno += valorLimpo.substring(8, tamanho);
          }
         
        }

      }else{// menor que 4
        valorRetorno = valorLimpo;
      }

      return valorRetorno
    }else{
      return null;
    }

  }

  static isContaSidec(operacao: string):boolean{
    let isSidec: boolean = false;
    let sidecContas: string [] = ['001', '003', '006', '007', '008', '010', '011', '013', '017', '019', '020', '022', '023', '043', '093'];

    if(operacao){
      for(let op of sidecContas){
        if(op === operacao){
          isSidec = true;
          break;
        }
      }
    }

    return isSidec;
  }

  static completarContaCaixaComZeroEsquerda(value: string):string{
    let valorRetorno: string = '';
    let regexLimpar = /\D/g;
    let tamanhoMinimoSidec = 9;
    let tamanhoMinimoNsgd = 10;
    let tamanhoMaximoSidec = 16;
    let tamanhoMaximoNsgd = 21;

    if(value && value !== ''){
      let valorLimpo = value.replace(regexLimpar, '');
      let tamanho: number = valorLimpo.length;

      if(tamanho >= tamanhoMinimoSidec){

        let tipoConta: string = valorLimpo.substring(4, 7);
        
        if(this.isContaSidec(tipoConta)){

          //descobri quantidade de zero a preenhcer
          let quantidadeDeZeros: number = tamanhoMaximoSidec - tamanho;
          let complementoEmZeros: string = this.getQuantidadeEmZeros(quantidadeDeZeros);

          let primeiraParte: string = valorLimpo.substring(0, 7);
          let parterFinal: string = valorLimpo.substring(7, tamanho);

          let contaCaixa = primeiraParte + complementoEmZeros + parterFinal;

          let contaCaixaFormatada: string = Utils.validarKeyupTipoCampoContaCaixa(contaCaixa);
          return contaCaixaFormatada;

        }else if(tamanho >= tamanhoMinimoNsgd){
          
          //descobri quantidade de zero a preenhcer
          let quantidadeDeZeros: number = tamanhoMaximoNsgd - tamanho;
          let complementoEmZeros: string = this.getQuantidadeEmZeros(quantidadeDeZeros);

          let primeiraParte: string = valorLimpo.substring(0, 8);
          let parterFinal: string = valorLimpo.substring(8, tamanho);

          let contaCaixa = primeiraParte + complementoEmZeros + parterFinal;

          let contaCaixaFormatada: string = Utils.validarKeyupTipoCampoContaCaixa(contaCaixa);
          return contaCaixaFormatada;

        }else{
          return Utils.validarKeyupTipoCampoContaCaixa(valorLimpo);
        }

      }else{
        return Utils.validarKeyupTipoCampoContaCaixa(valorLimpo);
      }

    }

    return valorRetorno;
  }

  static getQuantidadeEmZeros(quantidade: number):string{
    let resposta: string = '';

    for(let i = 0; i < quantidade; i++){
      resposta += '0';
    }

    return resposta;
  }

}