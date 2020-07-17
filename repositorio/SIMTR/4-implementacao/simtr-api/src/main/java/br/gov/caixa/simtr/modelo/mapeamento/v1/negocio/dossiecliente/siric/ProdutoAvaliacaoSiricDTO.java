package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.ResultadoAvaliacaoProdutoSIRICEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaSiric;

public class ProdutoAvaliacaoSiricDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_CODIGO_OPERACAO)
	private Integer codigoOperacao;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_CODIGO_MODALIDADE)
	private Integer codigoModalidade;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_PRAZO)
	private Integer prazo;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_RATING)
	private String rating;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_RESULTADO_AVALIACAO)
	private ResultadoAvaliacaoProdutoSIRICEnum resultadoAvaliacao;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_VALOR_DISPONIVEL)
	private Double valorDisponivel;

	@JsonProperty(value = ConstantesConsultaSiric.PRODUTO_VALOR_TOTAL)
	private Double valorTotal;

	public Integer getCodigoOperacao() {
		return codigoOperacao;
	}

	public void setCodigoOperacao(Integer codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}

	public Integer getCodigoModalidade() {
		return codigoModalidade;
	}

	public void setCodigoModalidade(Integer codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public ResultadoAvaliacaoProdutoSIRICEnum getResultadoAvaliacao() {
		return resultadoAvaliacao;
	}

	public void setResultadoAvaliacao(ResultadoAvaliacaoProdutoSIRICEnum resultadoAvaliacao) {
		this.resultadoAvaliacao = resultadoAvaliacao;
	}

	public Double getValorDisponivel() {
		return valorDisponivel;
	}

	public void setValorDisponivel(Double valorDisponivel) {
		this.valorDisponivel = valorDisponivel;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

}
