package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author f525904
 *
 */
@ApiModel(
          value = ConstantesNegocioSiric.API_MODEL_AVALIACAO_TOMADOR_PJ,
          description = "Objeto utilizado para representar avaliação tomador pessoa jurídica.")
public class AvaliacaoTomadorPJDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = ConstantesNegocioSiric.PROTOCOLO, required = true, value = "Valor que representa o identificador do Protocolo.")
    private String callbackUrl;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PONTO_ATENDIMENTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_PONTO_ATENDIMENTO, required = true, value = "Atributo que representa o código ponto atendimento.")
    private Long codigoPontoAtendimento;

    @JsonProperty(value = ConstantesNegocioSiric.CLASSE_CONSULTA_SICLI)
    @ApiModelProperty(name = ConstantesNegocioSiric.CLASSE_CONSULTA_SICLI, required = true, value = "Valor que representa o identificador da classe SICLI.")
    private Integer classeConsultaSicli;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = ConstantesNegocioSiric.QUADROS)
    @ApiModelProperty(name = ConstantesNegocioSiric.QUADROS, required = true, value = "Objeto utilizado para representar os atributos do quadro.")
    private List<QuadroDTO> quadros;

    public AvaliacaoTomadorPJDTO() {
        super();
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Long getCodigoPontoAtendimento() {
        return codigoPontoAtendimento;
    }

    public void setCodigoPontoAtendimento(Long codigoPontoAtendimento) {
        this.codigoPontoAtendimento = codigoPontoAtendimento;
    }

    public Integer getClasseConsultaSicli() {
        return classeConsultaSicli;
    }

    public void setClasseConsultaSicli(Integer classeConsultaSicli) {
        this.classeConsultaSicli = classeConsultaSicli;
    }

    public List<QuadroDTO> getQuadros() {
        return quadros;
    }

    public void setQuadros(List<QuadroDTO> quadros) {
        this.quadros = quadros;
    }
}
