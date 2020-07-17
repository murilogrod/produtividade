package br.gov.caixa.simtr.controle.vo.siric;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRespostaConsultaSiric;

public class RespostaProdutoAvaliacaoSiricVO implements Serializable {
	
	 private static final long serialVersionUID = 1L;

	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_RATING)
	    private String rating;
	    
	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_RESULTADO_AVALIACAO)
	    private Integer resultadoAvaliacao;

	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_VALOR_CALCULADO_TOTAL)
	    private Double valorTotal;
	    
	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_VALOR_DISPONIVEL)
	    private Double valorDisponivel;
	    
	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_CODIGO_MODALIDADE)
	    private Integer codigoModalidade;
	    
	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_PRAZO_MESES)
	    private Integer prazoMeses;
	    
	    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTO_CODIGO)
	    private Integer codigoProduto;
	    
	    public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public Integer getResultadoAvaliacao() {
			return resultadoAvaliacao;
		}

		public void setResultadoAvaliacao(Integer resultadoAvaliacao) {
			this.resultadoAvaliacao = resultadoAvaliacao;
		}

		public Double getValorTotal() {
			return valorTotal;
		}

		public void setValorTotal(Double valorTotal) {
			this.valorTotal = valorTotal;
		}

		public Double getValorDisponivel() {
			return valorDisponivel;
		}

		public void setValorDisponivel(Double valorDisponivel) {
			this.valorDisponivel = valorDisponivel;
		}

		public Integer getCodigoModalidade() {
			return codigoModalidade;
		}

		public void setCodigoModalidade(Integer codigoModalidade) {
			this.codigoModalidade = codigoModalidade;
		}

		public Integer getPrazoMeses() {
			return prazoMeses;
		}

		public void setPrazoMeses(Integer prazoMeses) {
			this.prazoMeses = prazoMeses;
		}

		public Integer getCodigoProduto() {
			return codigoProduto;
		}

		public void setCodigoProduto(Integer codigoProduto) {
			this.codigoProduto = codigoProduto;
		}
	    
	 
}
