package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.CodigoSituacaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author f525904
 *
 */
@ApiModel(
        value = ConstantesNegocioSiric.API_MODEL_RESULTADO_PROPOSTA_OPERACAO,
        description = "Objeto utilizado para representar resposta da operação.")
public class ResultadoPropostaOperacaoDTO implements Serializable {

    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesNegocioSiric.RESULTADO_VALIDACAO)
    @ApiModelProperty(name = ConstantesNegocioSiric.RESULTADO_VALIDACAO, required = true, value = "Resultado(s) validação.")
    private List<String> resultadoValidacao = new ArrayList<>();

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_SITUACAO)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_SITUACAO, required = true, value = "Atributo que representa a situação.", notes = "0 = EM_PROCESSAMENTO | 1 = CONCLUIDO")
    private CodigoSituacaoEnum codigoSituacao;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_AVALIACAO_TOMADOR)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_AVALIACAO_TOMADOR, required = true, value = "Atributo que representa o código da avaliação tomador.")
    private Long codigoAvaliacaoTomador;
    
    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_AVALIACAO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_AVALIACAO_OPERACAO, required = true, value = "Atributo que representa o código avaliação da operação .")
    private Long codigoAvalicaoOperacao;

    public List<String> getResultadoValidacao() {
        return resultadoValidacao;
    }

    public void setResultadoValidacao(List<String> resultadoValidacao) {
        this.resultadoValidacao = resultadoValidacao;
    }

    public CodigoSituacaoEnum getCodigoSituacao() {
        return codigoSituacao;
    }

    public void setCodigoSituacao(CodigoSituacaoEnum codigoSituacao) {
        this.codigoSituacao = codigoSituacao;
    }

    public Long getCodigoAvaliacaoTomador() {
        return codigoAvaliacaoTomador;
    }

    public void setCodigoAvaliacaoTomador(Long codigoAvaliacaoTomador) {
        this.codigoAvaliacaoTomador = codigoAvaliacaoTomador;
    }

    public Long getCodigoAvalicaoOperacao() {
        return codigoAvalicaoOperacao;
    }

    public void setCodigoAvalicaoOperacao(Long codigoAvalicaoOperacao) {
        this.codigoAvalicaoOperacao = codigoAvalicaoOperacao;
    }
}
