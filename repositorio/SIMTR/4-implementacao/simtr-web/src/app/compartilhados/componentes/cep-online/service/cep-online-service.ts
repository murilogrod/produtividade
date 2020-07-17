import { Injectable, Output, EventEmitter } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { ARQUITETURA_SERVICOS, DADOS_DECLARADO, FORMULARIO_DINAMICO } from "src/app/constants/constants";
import { CepOnlineModel } from "../model/cep-online-model";
import { LocalidadeModel } from "../model/localidade-model";
import { TipoLogradouroModel } from "../model/tipo-logradouro-model";
import { CampoFormulario } from "src/app/model";
import { CepOnlineCampoModel } from "../model/cep-online-campo-model";
import { LoaderService } from "src/app/services";

const CEP_ONLINE: string = "CEP_ONLINE";
const CEP_ONLINE_DEP: string = "CEP_ONLINE_DEP";

const _LOCALIDADE_CODIGO = "_LOCALIDADE_CODIGO";
const _LOCALIDADE_NOME = "_LOCALIDADE_NOME";
const _LOCALIDADE_TIPO = "_LOCALIDADE_TIPO";
const _CODIGO_LOGRADOURO =  "_CODIGO_LOGRADOURO";
const _LOGRADOURO = "_LOGRADOURO";
const _COMPLEMENTO = "_COMPLEMENTO";
const _LIMITE_INFERIOR = "_LIMITE_INFERIOR";
const _LIMITE_SUPERIOR =  "_LIMITE_SUPERIOR";
const _CODIGO = "_CODIGO";
const _BAIRRO = "_BAIRRO";
const _SIGLA_UF = "_SIGLA_UF";
const _CODIGO_TRECO =  "_CODIGO_TRECO";
const _TIPO_LOGRADOURO_TIPO = "_TIPO_LOGRADOURO_TIPO";
const _TIPO_LOGRADOURO_SIGLA =  "_TIPO_LOGRADOURO_SIGLA";

@Injectable()
export class CepOnlineService {

    listaSufixoCepOnline: string [] = [
        _LOCALIDADE_CODIGO,
        _LOCALIDADE_NOME,
        _LOCALIDADE_TIPO,
        _CODIGO_LOGRADOURO,
        _LOGRADOURO,
        _COMPLEMENTO,
        _LIMITE_INFERIOR,
        _LIMITE_SUPERIOR,
        _CODIGO,
        _BAIRRO,
        _SIGLA_UF,
        _CODIGO_TRECO,
        _TIPO_LOGRADOURO_TIPO,
        _TIPO_LOGRADOURO_SIGLA
    ]

    constructor(private http: HttpClient,
        private loadService: LoaderService) { }

    cep(cep: string): Observable<any>{
        var cepValor = cep.replace(/\D/g, '');
        var cepLista = cepValor.match(/^(\d{5})(\d{3})$/);
        cep = cepLista[1] + '-' + cepLista[2];

        let corpo = {servico: ARQUITETURA_SERVICOS.ENDERECO_WEB_SERVICE_CEP+cep, verbo: "GET"};
        return this.http.post(`${environment.serverPath}${ARQUITETURA_SERVICOS.API_MANAGER}`, corpo);
    }

    montarObjetoCep(resposta: any):CepOnlineModel{
        if(resposta){
            let cep = new CepOnlineModel();
            cep.cep = resposta.cep;
            cep.codigoLogradouro = resposta['codigo-logradouro'];
            cep.logradouro = resposta.logradouro;
            cep.complemento = resposta.complemento;
            cep.limiteInferior = resposta['limite-inferior'];
            cep.limiteSuperior = resposta['limite-superior'];
            cep.codigo = resposta.codigo;
            cep.bairro = resposta.bairro;
            cep.siglaUf = resposta['sigla-uf'];
            cep.codigoTreco = resposta['codigo-treco'];

            if(resposta.localidade){
                let localidade = new LocalidadeModel();
                localidade.codigo = resposta.localidade.codigo;
                localidade.nome = resposta.localidade.nome;
                localidade.tipo = resposta.localidade.tipo;
                cep.localidade = localidade;
            }else{
                cep.localidade = null;
            }

            if(resposta['tipo-logradouro']){
                let tipoLogradouro = new TipoLogradouroModel();
                tipoLogradouro = resposta['tipo-logradouro'];
                cep.tipoLogradouro = tipoLogradouro;
            }else{
                cep.tipoLogradouro = null;
            }

            return cep;
        }
        return null;
    }

    initDadosDeclarados(cep: CepOnlineModel, camposDadosDeclarado: any [], palavraChaveCampo: string){
        let camposDadosDeclaradosSelecionados: any [] = [];
        let listaDeCampos: CepOnlineCampoModel [] = [];

        if(camposDadosDeclarado && camposDadosDeclarado.length > 0){

            for(let elemento of camposDadosDeclarado){
                let campo = new CepOnlineCampoModel();
                campo.id = elemento.nome_documento;
                campo.label = elemento.nome_negocial;
                campo.value = elemento.valor;
                campo.nomeDocumento = elemento.nome_documento;
                campo.tipoCampo = elemento.tipo_campo;

                if(campo.nomeDocumento && this.existeCampo(campo, palavraChaveCampo)){
                    listaDeCampos.push(campo);
                    camposDadosDeclaradosSelecionados.push(elemento);
                }
            }

            if(camposDadosDeclaradosSelecionados && camposDadosDeclaradosSelecionados.length > 0){
                this.loadService.show();
                this.preencherCamposDependenteDoCep(cep, listaDeCampos, camposDadosDeclaradosSelecionados, palavraChaveCampo, DADOS_DECLARADO, camposDadosDeclarado);
            }
        }
        return;
    }

    initFormularioDinamico(cep: CepOnlineModel, camposFormularioDinamico: CampoFormulario [], palavraChaveCampo: string){
        let camposFormularioSelecionados: CampoFormulario [] = [];
        let listaDeCampos: CepOnlineCampoModel [] = [];

        if(camposFormularioDinamico && camposFormularioDinamico.length > 0){

            for(let elemento of camposFormularioDinamico){
                let campo = new CepOnlineCampoModel();
                campo.id = elemento.id.toString();
                campo.label = elemento.label ? elemento.label : elemento.label_campo;
                campo.value = elemento.resposta ? elemento.resposta : elemento.resposta_aberta;
                campo.nomeDocumento = elemento.nome_campo;
                campo.tipoCampo = elemento.tipo_campo;

                if(campo.nomeDocumento && this.existeCampo(campo, palavraChaveCampo)){
                    listaDeCampos.push(campo);
                    camposFormularioSelecionados.push(elemento);
                }
            }

            if(camposFormularioSelecionados && camposFormularioSelecionados.length > 0){
                this.loadService.show();
                this.preencherCamposDependenteDoCep(cep, listaDeCampos, camposFormularioSelecionados, palavraChaveCampo, FORMULARIO_DINAMICO, camposFormularioDinamico);
            }
        }
        return;
    }

    private existeCampo(campo: CepOnlineCampoModel, palavraChaveCampo: string):boolean{
        let existe: boolean = false;

        for(let sufixo of this.listaSufixoCepOnline){
            if(palavraChaveCampo.toLocaleUpperCase() + sufixo === campo.nomeDocumento.toLocaleUpperCase()){
                existe = true;
                break;
            }
        }

        return existe;
    }

    preencherCamposDependenteDoCep(cep: CepOnlineModel, listaDeCampos: CepOnlineCampoModel [], camposSelecionados: any [], palavraChaveCampo: string, tipoComponente: string, listaPrincipal: any []){

        if(listaDeCampos && listaDeCampos.length > 0){
            for(let i = 0; i < listaDeCampos.length; i++){
                let campo = listaDeCampos[i];
                let campoSelecionado = camposSelecionados[i];

                for(let sufixo of this.listaSufixoCepOnline){
                    let nomeDoCampoCompleto = palavraChaveCampo.toLocaleUpperCase() + sufixo;

                    // identificar campos dependentes correspondente ao cep
                    if(nomeDoCampoCompleto === campo.nomeDocumento.toLocaleUpperCase()){
                        campo.value = this.retornaValor(sufixo, cep);
                        
                        if(tipoComponente == FORMULARIO_DINAMICO){

                            campoSelecionado.resposta = campo.value;
                            campoSelecionado.resposta_aberta = campo.value;

                        }else{
                           campoSelecionado.valor = campo.value;
                        }
                        
                    }
                }
            }
        }

        // atulizar lista principal passada via parametro
        camposSelecionados.forEach(selecionado =>{
            
            if(tipoComponente == DADOS_DECLARADO){
                listaPrincipal.forEach(campoPrincipal =>{

                    if(selecionado.nome_documento == campoPrincipal.nome_documento){
                        campoPrincipal.valor = selecionado.valor;
                    }
    
                });

            }else{
                // FORMULARIO_DINAMICO
                listaPrincipal.forEach(campoPrincipal =>{

                    if(selecionado.id == campoPrincipal.id){
                        campoPrincipal.resposta = selecionado.resposta;
                        campoPrincipal.resposta_aberta = selecionado.resposta_aberta;
                        campoPrincipal.respostas_formulario = selecionado.respostas_formulario;
                    }

                });
            }   
           
        });

        return;
    }

    retornaValor(sufixo: string, cep: CepOnlineModel):any{
        let valor: any;

        if(!cep){
            valor = null;
            return valor;
        }

        switch(sufixo){
            case _LOCALIDADE_CODIGO:
                valor = cep.localidade && cep.localidade.codigo ? cep.localidade.codigo : null;       
            break;
            case _LOCALIDADE_NOME:
                valor = cep.localidade && cep.localidade.nome ? cep.localidade.nome.trim() : "";       
            break;
            case _LOCALIDADE_TIPO:
                valor = cep.localidade && cep.localidade.tipo ? cep.localidade.tipo.trim() : "";       
            break;
            case _CODIGO_LOGRADOURO:
                valor = cep.codigoLogradouro ? cep.codigoLogradouro : null;       
            break;
            case _LOGRADOURO:
                valor = cep.logradouro ? cep.logradouro.trim() : "";       
            break;
            case _COMPLEMENTO:
                valor = cep.complemento ? cep.complemento.trim() : "";       
            break;
            case _LIMITE_INFERIOR:
                valor = cep.limiteInferior ? cep.limiteInferior.trim() : "";       
            break;
            case _LIMITE_SUPERIOR:
                valor = cep.limiteSuperior ? cep.limiteSuperior.trim() : "";       
            break;
            case _CODIGO:
                valor = cep.codigo ? cep.codigo : null;       
            break;
            case _BAIRRO:
                valor = cep.bairro ? cep.bairro.trim() : "";       
            break;
            case _SIGLA_UF:
                valor = cep.siglaUf ? cep.siglaUf.trim() : "";       
            break;
            case _CODIGO_TRECO:
                valor = cep.codigoTreco ? cep.codigoTreco : null;       
            break;
            case _TIPO_LOGRADOURO_TIPO:
                valor = cep.tipoLogradouro && cep.tipoLogradouro.tipo ? cep.tipoLogradouro.tipo.trim() : "";       
            break;
            case _TIPO_LOGRADOURO_SIGLA:
                valor = cep.tipoLogradouro && cep.tipoLogradouro.sigla ? cep.tipoLogradouro.sigla.trim() : "";       
            break;
            default:
                valor = "";
            break;
        }
        return valor;
    }
}