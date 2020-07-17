import { IdentificadorDossieFase } from "../modal-pessoa/model/identificadorDossieEFase";
import { UNDESCOR, LOCAL_STORAGE_CONSTANTS, TIPO_VINCULO } from "src/app/constants/constants";
import { ApplicationService } from "src/app/services";
import { CampoFormulario } from "src/app/model";
import { TipoCampoExpressao } from "src/app/documento/formulario-generico/model/formulario-tipo-campo.model";
import { RespostaFormulario } from "../../../model-endPoint-dossie-produto/respostaFormulario";

export class UtilsModal {

    static buscarProcessoLocalStorage(appService: ApplicationService, identificadorDossieFase: IdentificadorDossieFase, tipoVinculo: string, parametro: number) {
        let campoFormulario: CampoFormulario[] = [];
        let geradorDossie = JSON.parse(localStorage.getItem('SIMTR-processoDossie_' + identificadorDossieFase.idDossie + ''));
        if (geradorDossie && geradorDossie.processos_filho && geradorDossie.processos_filho.length > 0) {
            if (tipoVinculo == TIPO_VINCULO.CLIENTE) {
                for (let tipo of geradorDossie.tipos_relacionamento) {
                    if (tipo.id == parametro) {
                        campoFormulario = (tipo && tipo.campos_formulario) ? tipo.campos_formulario : [];
                        break
                    };
                }
            }
            if (tipoVinculo == TIPO_VINCULO.PRODUTO) {
                for (let produto of geradorDossie.produtos_vinculados) {
                    if (produto.id == parametro) {
                        campoFormulario = (produto && produto.campos_formulario) ? produto.campos_formulario : [];
                        break
                    }
                }
            }
            if (tipoVinculo == TIPO_VINCULO.GARANTIA) {
                for (let garantia of geradorDossie.garantias_vinculadas) {
                    if (garantia.id == parametro) {
                        campoFormulario = garantia.campos_formulario ? garantia.campos_formulario : [];
                        break
                    }
                }
            }
        }
        return campoFormulario;
    }


    static preencherlistarResposta(campo: CampoFormulario, resposta: RespostaFormulario) {
        switch (campo.tipo_campo) {
            case TipoCampoExpressao.TEXT:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.MONETARIO:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.CONTA_CAIXA:
                this.popularCampoRespostaContaCaixa(resposta, campo);
                break;    
            case TipoCampoExpressao.DECIMAL:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.CEP:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.CEP_ONLINE:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.HIDDEN:
                this.popularCampoResposta(resposta, campo);
                break;                    
            case TipoCampoExpressao.COLOR:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.DATE:
                this.popularCampoResposta(resposta, campo);
                break;
            case TipoCampoExpressao.SELECT:
                resposta.campo_formulario = campo.id;
                if (campo.string_selecionadas && campo.string_selecionadas.length > 0) {
                    this.setarNValoresOpcoesSelecionadas(resposta, campo);
                }
                else {
                    this.setarValorOpcoesSelecionadas(resposta, campo);
                }
                break;
            case TipoCampoExpressao.RADIO:
                resposta.campo_formulario = campo.id;
                this.setarValorOpcoesSelecionadas(resposta, campo);
                break;
            case TipoCampoExpressao.CHECKBOX:
                resposta.campo_formulario = campo.id;
                this.setarNValoresOpcoesSelecionadas(resposta, campo);
                break;
        }
        return resposta;
    }

    static setarNValoresOpcoesSelecionadas(resposta: RespostaFormulario, campo: CampoFormulario) {
        resposta.opcoes_selecionadas = [];
        if (campo.string_selecionadas && campo.string_selecionadas.length > 0) {
            campo.string_selecionadas.forEach(opcao => {
                resposta.opcoes_selecionadas.push(opcao);
            });
        }
        return resposta;
    }

    static setarValorOpcoesSelecionadas(resposta: RespostaFormulario, campo: CampoFormulario) {
        resposta.opcoes_selecionadas = [];
        resposta.opcoes_selecionadas.push(campo.resposta_aberta);
        return resposta;
    }

    static popularCampoResposta(resposta: RespostaFormulario, campo: CampoFormulario) {
        resposta.campo_formulario = campo.id;
        resposta.resposta = campo.resposta_aberta;
        return resposta;
    }

    static popularCampoRespostaContaCaixa(resposta: RespostaFormulario, campo: CampoFormulario) {
        resposta.campo_formulario = campo.id;
        resposta.resposta = campo.resposta_aberta;

        if(resposta.resposta && resposta.resposta !== ''){
            resposta.resposta = resposta.resposta.replace(/\D/g, '');
        }
        
        return resposta;
    }

    static popularCamposFormularioConsulta(campo: any, resposta: any) {
        switch (campo.tipo_campo) {
            case TipoCampoExpressao.TEXT:
                campo.resposta_aberta = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                break;
            case TipoCampoExpressao.COLOR:
                campo.resposta_aberta = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                break;
            case TipoCampoExpressao.DATE:
                campo.resposta_aberta = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                break;
            case TipoCampoExpressao.SELECT:
                UtilsModal.setarPrimeiroValorOpcoesSelecionadas(resposta, campo);
                break;
            case TipoCampoExpressao.RADIO:
                UtilsModal.setarPrimeiroValorOpcoesSelecionadas(resposta, campo);
                break;
            case TipoCampoExpressao.CHECKBOX:
                UtilsModal.preencherString_selecionadas(resposta, campo);
                break;
           case TipoCampoExpressao.MONETARIO:
                let respostaMonetario = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                let valorMonetario: number = respostaMonetario ? Number.parseFloat(respostaMonetario) : null;
                campo.resposta_aberta = valorMonetario;
                break;
            case TipoCampoExpressao.DECIMAL:
                let respostaDecimal = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                let valorDecimal: number = respostaDecimal ? Number.parseFloat(respostaDecimal) : null;
                campo.resposta_aberta = valorDecimal;
                break;    
            default:
                campo.resposta_aberta = resposta.resposta_aberta ? resposta.resposta_aberta : resposta.resposta;
                break;
        }
        return campo;
    }

    private static setarPrimeiroValorOpcoesSelecionadas(resposta: any, campo: any) {
        if (resposta.opcoes_selecionadas && resposta.opcoes_selecionadas[0] && resposta.opcoes_selecionadas.length > 0) {

            campo.resposta_aberta = resposta.opcoes_selecionadas[0].valor_opcao ? resposta.opcoes_selecionadas[0].valor_opcao : resposta.opcoes_selecionadas[0];
        }
    }

    private static preencherString_selecionadas(resposta: any, campo: any) {
        if (resposta.opcoes_selecionadas && resposta.opcoes_selecionadas.length > 0) {
            campo.string_selecionadas = [];
            resposta.opcoes_selecionadas.forEach(objetoRespota => {
                campo.string_selecionadas.push(objetoRespota.valor_opcao ? objetoRespota.valor_opcao : objetoRespota);
            });
        }
    }
}