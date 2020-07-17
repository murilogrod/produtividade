package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;

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
        value = ConstantesNegocioSiric.API_MODEL_AVALIACAO_OPERACAO_SIMPLES_PJDTO,
        description = "Objeto utilizado para representar solicitação de avaliação da operação.")
public class AvaliacaoOperacaoSimplesPJDTO implements Serializable {
    
    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesNegocioSiric.CNPJ)
    @ApiModelProperty(name = ConstantesNegocioSiric.CNPJ, value = "CNPJ do para avaliação da operação simples pessoa juridica.", example = "99.999.999/9999-99")
    private String cnpj;
    
    @JsonProperty(value = ConstantesNegocioSiric.CALLBACK_URL)
    @ApiModelProperty(name = ConstantesNegocioSiric.CALLBACK_URL, required = true, value = "Atributo que representa a URL callback.")
    private String callbackUrl;
    
    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PROPOSTA)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_PROPOSTA, required = true, value = "Atributo que representa o código proposta.")
    private Long codigoProposta;
    
    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PONTO_ATENDIMENTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_PONTO_ATENDIMENTO, required = true, value = "Atributo que representa o código ponto atendimento.")
    private Long codigoPontoAtendimento;
    
    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_PRODUTO, required = true, value = "Atributo que representa o código produto.")
    private Long codigoProduto;
    
    @JsonProperty(value = ConstantesNegocioSiric.INDICADOR_TOMADOR)
    @ApiModelProperty(name = ConstantesNegocioSiric.INDICADOR_TOMADOR, required = true, value = "Valor que representa o indicador do tomador.")
    private Integer indicadorTomador;

    @JsonProperty(value = ConstantesNegocioSiric.CLASSE_CONSULTA_SICLI)
    @ApiModelProperty(name = ConstantesNegocioSiric.CLASSE_CONSULTA_SICLI, required = true, value = "Valor que representa o identificador da classe SICLI.")
    private Integer classeConsultaSicli;
    
    @JsonProperty(value = ConstantesNegocioSiric.VALOR_FINANCIAMENTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.VALOR_FINANCIAMENTO, required = true, value = "Valor que representa o valor financiamento.")
    private Double valorFinanciamento;
    
    @JsonProperty(value = ConstantesNegocioSiric.PRAZO_MESES)
    @ApiModelProperty(name = ConstantesNegocioSiric.PRAZO_MESES, required = true, value = "Valor que representa o prazo de meses.")
    private Integer prazoMeses;
    
    @JsonProperty(value = ConstantesNegocioSiric.PRESTACAO_NECESSARIA_FINANCIAMENTO)
    @ApiModelProperty(name = ConstantesNegocioSiric.PRESTACAO_NECESSARIA_FINANCIAMENTO, required = true, value = "Valor que representa a prestação necessaria para financimento.")
    private Double prestacaoNecessariaFinanciamento;
    
    @JsonProperty(value = ConstantesNegocioSiric.TAXA_JUROS)
    @ApiModelProperty(name = ConstantesNegocioSiric.TAXA_JUROS, required = true, value = "Valor que representa a taxa de juros.")
    private Double taxaJuros;

    public Long getCodigoProposta() {
        return codigoProposta;
    }

    public void setCodigoProposta(Long codigoProposta) {
        this.codigoProposta = codigoProposta;
    }

    public Long getCodigoPontoAtendimento() {
        return codigoPontoAtendimento;
    }

    public void setCodigoPontoAtendimento(Long codigoPontoAtendimento) {
        this.codigoPontoAtendimento = codigoPontoAtendimento;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getIndicadorTomador() {
        return indicadorTomador;
    }

    public void setIndicadorTomador(Integer indicadorTomador) {
        this.indicadorTomador = indicadorTomador;
    }

    public Integer getClasseConsultaSicli() {
        return classeConsultaSicli;
    }

    public void setClasseConsultaSicli(Integer classeConsultaSicli) {
        this.classeConsultaSicli = classeConsultaSicli;
    }

    public Double getValorFinanciamento() {
        return valorFinanciamento;
    }

    public void setValorFinanciamento(Double valorFinanciamento) {
        this.valorFinanciamento = valorFinanciamento;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public Double getPrestacaoNecessariaFinanciamento() {
        return prestacaoNecessariaFinanciamento;
    }

    public void setPrestacaoNecessariaFinanciamento(Double prestacaoNecessariaFinanciamento) {
        this.prestacaoNecessariaFinanciamento = prestacaoNecessariaFinanciamento;
    }

    public Double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(Double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}
