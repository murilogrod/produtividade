package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric.solicitacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuadroAvaliacaoSiric {

	@JsonProperty("linhas_quadro_valor")
	private List<Integer> linhasQuadroValor = null;

	@JsonProperty("codigo_quadro_valor")
	private Integer codigoQuadroValor;
	
	@JsonProperty("identificador_tipo_pessoa")
	private String identificadorTipoPessoa;
	
	@JsonProperty("identificacao_pessoa")
	private String identificacaoPessoa;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("linhas_quadro_valor")
	public List<Integer> getLinhasQuadroValor() {
		return linhasQuadroValor;
	}

	@JsonProperty("linhas_quadro_valor")
	public void setLinhasQuadroValor(List<Integer> linhasQuadroValor) {
		this.linhasQuadroValor = linhasQuadroValor;
	}

	@JsonProperty("codigo_quadro_valor")
	public Integer getCodigoQuadroValor() {
		return codigoQuadroValor;
	}

	@JsonProperty("codigo_quadro_valor")
	public void setCodigoQuadroValor(Integer codigoQuadroValor) {
		this.codigoQuadroValor = codigoQuadroValor;
	}

	@JsonProperty("identificador_tipo_pessoa")
	public String getIdentificadorTipoPessoa() {
		return identificadorTipoPessoa;
	}

	@JsonProperty("identificador_tipo_pessoa")
	public void setIdentificadorTipoPessoa(String identificadorTipoPessoa) {
		this.identificadorTipoPessoa = identificadorTipoPessoa;
	}

	@JsonProperty("identificacao_pessoa")
	public String getIdentificacaoPessoa() {
		return identificacaoPessoa;
	}

	@JsonProperty("identificacao_pessoa")
	public void setIdentificacaoPessoa(String identificacaoPessoa) {
		this.identificacaoPessoa = identificacaoPessoa;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}