package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.TipoEventoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author f525904
 *
 */

@ApiModel(
        value = ConstantesNegocioSiric.API_MODEL_PROPOSTA_OPERACAO_RESPOSTA,
        description = "Objeto utilizado para representar resposta da operação.")
public class PropostaOperacaoRespostaDTO implements Serializable{
    
    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesNegocioSiric.PROTOCOLO)
    @ApiModelProperty(name = ConstantesNegocioSiric.PROTOCOLO, required = true, value = "Valor que representa o identificador do Protocolo.")
    private String protocolo;
    
    @JsonProperty(value = ConstantesNegocioSiric.RESULTADO)
    @ApiModelProperty(name = ConstantesNegocioSiric.RESULTADO, required = true, value = "Objeto utilizado para representar os atributos de um Resultado Proposta Operacao.")
    private ResultadoPropostaOperacaoDTO resultado;
    
    @JsonProperty(value = ConstantesNegocioSiric.TIPO_EVENTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.TIPO_EVENTO, required = true, value = "Enum utilizado para representar o Tipo de Evento.", 
    notes = "RECEBIDA = proposta.recebida | PESQUISA_CONTIGENCIADA = proposta.pesquisa.contigenciada | AVALIACAO_ERRO = proposta.avaliacao.erro | CONCLUIDA = proposta.concluida ")
    private TipoEventoEnum tipoEvento;
    
    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public ResultadoPropostaOperacaoDTO getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoPropostaOperacaoDTO resultado) {
        this.resultado = resultado;
    }

    public TipoEventoEnum getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEventoEnum tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}
