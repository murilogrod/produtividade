package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric.solicitacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelacionamentoAvaliacaoSiric {

	@JsonProperty("identificador_tipo_pessoa_reciproco")
	private String identificadorTipoPessoaReciproco;

	@JsonProperty("identificacao_pessoa_reciproco")
	private String identificacaoPessoaReciproco;
	
	@JsonProperty("linhas_quadro_valor")
	private List<Integer> linhasQuadroValor;
	
	@JsonProperty("valor_relacionamento")
	private Integer valorRelacionamento;
	
	@JsonProperty("codigo_pergunta")
	private Integer codigoPergunta;
	
	@JsonProperty("data_fim")
	private String dataFim;
	
	@JsonProperty("data_inicio")
	private String dataInicio;
	
	@JsonProperty("codigo_produto")
	private Integer codigoProduto;
	
	@JsonProperty("identificador_tipo_pessoa")
	private String identificadorTipoPessoa;
	
	@JsonProperty("identificacao_pessoa")
	private String identificacaoPessoa;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	public String getIdentificadorTipoPessoaReciproco() {
		return identificadorTipoPessoaReciproco;
	}

	public void setIdentificadorTipoPessoaReciproco(String identificadorTipoPessoaReciproco) {
		this.identificadorTipoPessoaReciproco = identificadorTipoPessoaReciproco;
	}

	public String getIdentificacaoPessoaReciproco() {
		return identificacaoPessoaReciproco;
	}

	public void setIdentificacaoPessoaReciproco(String identificacaoPessoaReciproco) {
		this.identificacaoPessoaReciproco = identificacaoPessoaReciproco;
	}

	public List<Integer> getLinhasQuadroValor() {
		return linhasQuadroValor;
	}

	public void setLinhasQuadroValor(List<Integer> linhasQuadroValor) {
		this.linhasQuadroValor = linhasQuadroValor;
	}

	public Integer getValorRelacionamento() {
		return valorRelacionamento;
	}

	public void setValorRelacionamento(Integer valorRelacionamento) {
		this.valorRelacionamento = valorRelacionamento;
	}

	public Integer getCodigoPergunta() {
		return codigoPergunta;
	}

	public void setCodigoPergunta(Integer codigoPergunta) {
		this.codigoPergunta = codigoPergunta;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Integer getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getIdentificadorTipoPessoa() {
		return identificadorTipoPessoa;
	}

	public void setIdentificadorTipoPessoa(String identificadorTipoPessoa) {
		this.identificadorTipoPessoa = identificadorTipoPessoa;
	}

	public String getIdentificacaoPessoa() {
		return identificacaoPessoa;
	}

	public void setIdentificacaoPessoa(String identificacaoPessoa) {
		this.identificacaoPessoa = identificacaoPessoa;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}