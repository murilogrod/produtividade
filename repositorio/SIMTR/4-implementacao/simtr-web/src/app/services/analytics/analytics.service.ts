import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { LoaderService } from '../http-interceptor/loader/loader.service';

const VERSION: string = '2.0.3';
const INIT_EVENT = "INIT";
const INIT_AGENT = "AGENT";
const NOTE = "NOTE";
const ISP = "ISP";
const SESSION_EVENT = "SESSION";
const PAGE = "page";
const ERROR = "error";
const WARN = "warn";
const TIMEOUT = 120000;

export interface RespostaAnalytics {
  code: number,
  dados: string,
  message: string,
  temErro: boolean,
  token: string,
  msg: string,
  msgsErro: string,
  org: string,
  isp: string,
  user: {token: string}
}

@Injectable()
export class AnalyticsService {
  evaluationRate: number = null;
  userFeedback: number = null;
  userAgent: string = null;
  token: string = null;
  provedor: string = null;
  session: number = null;
  category: string = "";
  enabled: boolean = true;

  constructor(private httpClient: HttpClient, private loadService: LoaderService) {
    var vm = this;
        
    if (window && window.navigator && window.navigator.userAgent) {
      vm.userAgent = window.navigator.userAgent;
    }    
  }
  
  private makeid() {
    return this.getRandom(1, 2147483647);
  }
  private getRandom(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  private getStamp(name) {
    var result = null;
    try {
      result = window.localStorage.getItem(name);
      if (result == "" || result == "null") {
        result = null;
      }
    } catch (ignore) {
    }
    return result;
  }
  private setStamp(name, value) {
    try {
      window.localStorage.setItem(name, value);
    } catch (ignore) {
    }
  }

  setEnable(value) {
    this.enabled = value;
  }
  setSession(id) {
    this.session = id;
  }
  setCategory(id) {
    this.category = id;
  }
  isEnabled() {
    return this.enabled;
  }


  private montarJson(key, session, category, action, label, value, device) {
    var vm = this;

    var json = {
      chave: null,
      sessao: "",
      categoria: null,
      acao: null,
      etiqueta: null,
      valor: "",
      nomeSistemaOperacional: "",
      versaoSistemaOperacional: "",
      nomeNavegador: "",
      versaoNavegador: "",
      dispositivo: "",
      userAgent: null,
      token: ""
    };
    json.chave = key;

    
    if (session) {
      json.sessao = session + "";
    }
    json.categoria = category;
    json.acao = action;
    json.etiqueta = label;
    if (value) {
      json.valor = value;
    }

    if (device != null && device["soname"]) {
      json.nomeSistemaOperacional = device["soname"];
    }

    if (device != null && device["sover"]) {
      json.versaoSistemaOperacional = device["sover"];
    }

    if (device != null && device["navname"]) {
      json.nomeNavegador = device["navname"];
    }

    if (device != null && device["navver"]) {
      json.versaoNavegador = device["navver"];
    }

    if (device != null && device["devname"]) {
      json.dispositivo = device["devname"];
    }

    json.userAgent = vm.userAgent;

    if (vm.token != null) {
      json.token = vm.token;
    }
    return json;
  }
  private enviarEventoRest(json) {
    var url = null;
    var vm = this;

    if (vm.token == null) {
      url = environment.urlCaixaAnalytics + "ws/evento/registrar";
    } else {
      url = environment.urlCaixaAnalytics + "ws/evento/registrarAutenticado";
    }

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };

    this.httpClient.post<RespostaAnalytics>(url, json, httpOptions).subscribe(
      response => {
        if (!response.temErro) {
          //console.info(res.msgsErro[0]);
        } else if (response.temErro) {
          console.error(response.msgsErro[0] + ": " + json);
        }
      },
      error => {
        console.error("Ocorreu um erro ao enviar o evento: " + error.statusText);
        this.loadService.hide();
        throw error;
      });

  }

  private registerEvent(category, action, label, value) {
    var vm = this;

    var json = vm.montarJson(environment.analyticsId, vm.session, category, action, label, value, null);    
    var jsontext = JSON.stringify(json);

    vm.enviarEventoRest(jsontext);
  }

  private getISPService() {
    var vm = this;
    
    this.httpClient.get<RespostaAnalytics>("https://extreme-ip-lookup.com/json/").subscribe(
      response => {
        try {
          if (response) {
            vm.provedor = response.org;
            if (!vm.provedor) {
              vm.provedor = response.isp;
            }
            vm.trackEvent("ISP", vm.provedor);
          }
        } catch (ignore) {
        }
      },
      error => {
        console.error("Não foi possível obter as informações do provedor");
        this.loadService.hide();
        throw error;
      });
  }

  private registrarProvedor() {
    var vm = this;
    
    this.httpClient.get<RespostaAnalytics>(environment.urlCaixaAnalytics + "ws/json/checarProvedor").subscribe(
      response => {
        if (!response.temErro) {
          var res = response;
          vm.provedor = res.message;
          vm.trackEvent("ISP", vm.provedor);
        } else if (response.temErro) {
          vm.getISPService();
        } 
      },
      error => {
        console.error("Ocorreu um erro ao verificar se o provedor já está cadastrado: " + error.statusText);
        this.loadService.hide();
        throw error;
      });
  }

  private getEvaluationRest() {
    var vm = this;

    this.httpClient.get<RespostaAnalytics>(environment.urlCaixaAnalytics + "ws/json/listarEvaluationUserFeedback?json=evaluationRate").subscribe(
      response => {
        if (!response.temErro) {
          var response = response;
          vm.evaluationRate = JSON.parse(response.message);
        } else if (response.temErro) {
          console.error(response.msgsErro[0]);
        }
      },
      error => {
        console.error("Ocorreu um erro ao obter o json Evaluation: " + error.statusText);
        this.loadService.hide();
        throw error;
      });
  }

  private getUserFeedbackRest() {
    var vm = this;

    this.httpClient.get<RespostaAnalytics>(environment.urlCaixaAnalytics + "ws/json/listarEvaluationUserFeedback?json=userFeedback").subscribe(
      response => {
        if (!response.temErro) {
          var res = response;
          vm.userFeedback = JSON.parse(res.message);
        } else if (response.temErro) {
          console.error(response.msgsErro[0]);
        }
      },
      error => {
        console.error("Ocorreu um erro ao obter o json UserFeedback: " + error.statusText);
        this.loadService.hide();
        throw error;
      });
  }

  private configs(id, newcategory) {
    var vm = this;
    if (!id) return;

    vm.getEvaluationRest();
    vm.getUserFeedbackRest();

    if (newcategory) vm.category = newcategory;
    vm.session = vm.makeid();
    var lastsession = vm.getStamp(vm.category);

    vm.enabled = true;

    vm.trackEvent(INIT_EVENT, lastsession);

    if (vm.userAgent != null) {
      vm.trackEvent(INIT_AGENT, vm.userAgent);
    }

    vm.setStamp(vm.category, vm.session);
    vm.registrarProvedor();

  }
  init(id, newcategory) {
    this.configs(id, newcategory);
  }
  initAuth(user, pass, id, newcategory, devmode) {
    this.auth(user, pass, id, newcategory, devmode);
  }
  auth(user, pass, id, newcategory, devmode) {
    var vm = this;
    var json = { usuario: user, senha: pass };

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };

    this.httpClient.post<RespostaAnalytics>(environment.urlCaixaAnalytics + "ws/autenticacao/autenticarInterface", JSON.stringify(json), httpOptions).subscribe(
      response => {
        console.info(response);
        if (!response.temErro) {
          vm.token = response.user.token;
        } else if (response.temErro) {
          console.error(response.message + ": " + json);
        }
      },
      error => {
        var cause = null;
        if (error != null && error.responseText) cause = error.responseText
        if (cause == null && error != null && error.statusText) cause = error.statusText;
        console.error("Ocorreu um erro ao se autenticar " + error.statusText);
        this.loadService.hide();
        throw error;
      });

    vm.configs(id, newcategory);
  }

  
  trackWarn(label, value = null) {
    if (!this.enabled) return;
    if (!environment.analyticsId) return;

    this.registerEvent(this.category, WARN, label, value);
  }
  trackException(label, value = null) {
    if (!this.enabled) return;
    if (!environment.analyticsId) return;

    this.registerEvent(this.category, ERROR, label, value);
  }
  trackEvent(action, label = null, value = null) {
    if (!this.enabled) return;
    if (!environment.analyticsId) return;

    this.registerEvent(this.category, action, label, value);
  }
  trackPage(page, value) {
    if (!this.enabled) return;
    if (!environment.analyticsId) return;

    this.registerEvent(this.category, PAGE, page, value);
  }
  getEvaluationRate() {
    return this.evaluationRate;
  }
  getUserFeedback() {
    return this.userFeedback;
  }
  isFeedbackReady() {
    return this.userFeedback != null && this.evaluationRate != null;
  }


  
}