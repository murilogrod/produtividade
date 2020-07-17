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
public class EnviarAvaliacaoSiricDTO {

	@JsonProperty("relacionamentos")
	private List<RelacionamentoAvaliacaoSiric> relacionamentos;

	@JsonProperty("quadros")
	private List<QuadroAvaliacaoSiric> quadros;
	
	@JsonProperty("callback_url")
	private String callbackUrl;
	
	@JsonProperty("prazo_meses")
	private Integer prazoMeses;
	
	@JsonProperty("taxa_juros")
	private Integer taxaJuros;
	
	@JsonProperty("tipo_garantia")
	private Integer tipoGarantia;
	
	@JsonProperty("ano_veiculo")
	private Integer anoVeiculo;
	
	@JsonProperty("prestacao_necessaria_financiamento")
	private Integer prestacaoNecessariaFinanciamento;
	
	@JsonProperty("comprovacao_aluguel")
	private String comprovacaoAluguel;
	
	@JsonProperty("codigo_correspondente")
	private Integer codigoCorrespondente;
	
	@JsonProperty("valor_margem_consignavel")
	private Integer valorMargemConsignavel;
	
	@JsonProperty("modelo_validacao_tomador")
	private Integer modeloValidacaoTomador;
	
	@JsonProperty("indicador_convenio")
	private String indicadorConvenio;
	
	@JsonProperty("valor_financiamento")
	private Integer valorFinanciamento;
	
	@JsonProperty("cnpj_convenente")
	private String cnpjConvenente;
	
	@JsonProperty("sistema_armotizacao")
	private Integer sistemaArmotizacao;
	
	@JsonProperty("codigo_proposta")
	private Integer codigoProposta;
	
	@JsonProperty("modelo_avaliacao_tomador")
	private Integer modeloAvaliacaoTomador;
	
	@JsonProperty("codigo_convenio")
	private Integer codigoConvenio;
	
	@JsonProperty("classe_consulta_sicli")
	private Integer classeConsultaSicli;
	
	@JsonProperty("valor_operacao")
	private Integer valorOperacao;
	
	@JsonProperty("codigo_modalidade")
	private Integer codigoModalidade;
	
	@JsonProperty("origem_recurso")
	private Integer origemRecurso;
	
	@JsonProperty("codigo_ponto_atendimento")
	private Integer codigoPontoAtendimento;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	@JsonProperty("relacionamentos")
	public List<RelacionamentoAvaliacaoSiric> getRelacionamentos() {
		return relacionamentos;
	}

	@JsonProperty("relacionamentos")
	public void setRelacionamentos(List<RelacionamentoAvaliacaoSiric> relacionamentos) {
		this.relacionamentos = relacionamentos;
	}

	@JsonProperty("quadros")
	public List<QuadroAvaliacaoSiric> getQuadros() {
		return quadros;
	}

	@JsonProperty("quadros")
	public void setQuadros(List<QuadroAvaliacaoSiric> quadros) {
		this.quadros = quadros;
	}

	@JsonProperty("callback_url")
	public String getCallbackUrl() {
		return callbackUrl;
	}

	@JsonProperty("callback_url")
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@JsonProperty("prazo_meses")
	public Integer getPrazoMeses() {
		return prazoMeses;
	}

	@JsonProperty("prazo_meses")
	public void setPrazoMeses(Integer prazoMeses) {
		this.prazoMeses = prazoMeses;
	}

	@JsonProperty("taxa_juros")
	public Integer getTaxaJuros() {
		return taxaJuros;
	}

	@JsonProperty("taxa_juros")
	public void setTaxaJuros(Integer taxaJuros) {
		this.taxaJuros = taxaJuros;
	}

	@JsonProperty("tipo_garantia")
	public Integer getTipoGarantia() {
		return tipoGarantia;
	}

	@JsonProperty("tipo_garantia")
	public void setTipoGarantia(Integer tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	@JsonProperty("ano_veiculo")
	public Integer getAnoVeiculo() {
		return anoVeiculo;
	}

	@JsonProperty("ano_veiculo")
	public void setAnoVeiculo(Integer anoVeiculo) {
		this.anoVeiculo = anoVeiculo;
	}

	@JsonProperty("prestacao_necessaria_financiamento")
	public Integer getPrestacaoNecessariaFinanciamento() {
		return prestacaoNecessariaFinanciamento;
	}

	@JsonProperty("prestacao_necessaria_financiamento")
	public void setPrestacaoNecessariaFinanciamento(Integer prestacaoNecessariaFinanciamento) {
		this.prestacaoNecessariaFinanciamento = prestacaoNecessariaFinanciamento;
	}

	@JsonProperty("comprovacao_aluguel")
	public String getComprovacaoAluguel() {
		return comprovacaoAluguel;
	}

	@JsonProperty("comprovacao_aluguel")
	public void setComprovacaoAluguel(String comprovacaoAluguel) {
		this.comprovacaoAluguel = comprovacaoAluguel;
	}

	@JsonProperty("codigo_correspondente")
	public Integer getCodigoCorrespondente() {
		return codigoCorrespondente;
	}

	@JsonProperty("codigo_correspondente")
	public void setCodigoCorrespondente(Integer codigoCorrespondente) {
		this.codigoCorrespondente = codigoCorrespondente;
	}

	@JsonProperty("valor_margem_consignavel")
	public Integer getValorMargemConsignavel() {
		return valorMargemConsignavel;
	}

	@JsonProperty("valor_margem_consignavel")
	public void setValorMargemConsignavel(Integer valorMargemConsignavel) {
		this.valorMargemConsignavel = valorMargemConsignavel;
	}

	@JsonProperty("modelo_validacao_tomador")
	public Integer getModeloValidacaoTomador() {
		return modeloValidacaoTomador;
	}

	@JsonProperty("modelo_validacao_tomador")
	public void setModeloValidacaoTomador(Integer modeloValidacaoTomador) {
		this.modeloValidacaoTomador = modeloValidacaoTomador;
	}

	@JsonProperty("indicador_convenio")
	public String getIndicadorConvenio() {
		return indicadorConvenio;
	}

	@JsonProperty("indicador_convenio")
	public void setIndicadorConvenio(String indicadorConvenio) {
		this.indicadorConvenio = indicadorConvenio;
	}

	@JsonProperty("valor_financiamento")
	public Integer getValorFinanciamento() {
		return valorFinanciamento;
	}

	@JsonProperty("valor_financiamento")
	public void setValorFinanciamento(Integer valorFinanciamento) {
		this.valorFinanciamento = valorFinanciamento;
	}

	@JsonProperty("cnpj_convenente")
	public String getCnpjConvenente() {
		return cnpjConvenente;
	}

	@JsonProperty("cnpj_convenente")
	public void setCnpjConvenente(String cnpjConvenente) {
		this.cnpjConvenente = cnpjConvenente;
	}

	@JsonProperty("sistema_armotizacao")
	public Integer getSistemaArmotizacao() {
		return sistemaArmotizacao;
	}

	@JsonProperty("sistema_armotizacao")
	public void setSistemaArmotizacao(Integer sistemaArmotizacao) {
		this.sistemaArmotizacao = sistemaArmotizacao;
	}

	@JsonProperty("codigo_proposta")
	public Integer getCodigoProposta() {
		return codigoProposta;
	}

	@JsonProperty("codigo_proposta")
	public void setCodigoProposta(Integer codigoProposta) {
		this.codigoProposta = codigoProposta;
	}

	@JsonProperty("modelo_avaliacao_tomador")
	public Integer getModeloAvaliacaoTomador() {
		return modeloAvaliacaoTomador;
	}

	@JsonProperty("modelo_avaliacao_tomador")
	public void setModeloAvaliacaoTomador(Integer modeloAvaliacaoTomador) {
		this.modeloAvaliacaoTomador = modeloAvaliacaoTomador;
	}

	@JsonProperty("codigo_convenio")
	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}

	@JsonProperty("codigo_convenio")
	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	@JsonProperty("classe_consulta_sicli")
	public Integer getClasseConsultaSicli() {
		return classeConsultaSicli;
	}

	@JsonProperty("classe_consulta_sicli")
	public void setClasseConsultaSicli(Integer classeConsultaSicli) {
		this.classeConsultaSicli = classeConsultaSicli;
	}

	@JsonProperty("valor_operacao")
	public Integer getValorOperacao() {
		return valorOperacao;
	}

	@JsonProperty("valor_operacao")
	public void setValorOperacao(Integer valorOperacao) {
		this.valorOperacao = valorOperacao;
	}

	@JsonProperty("codigo_modalidade")
	public Integer getCodigoModalidade() {
		return codigoModalidade;
	}

	@JsonProperty("codigo_modalidade")
	public void setCodigoModalidade(Integer codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}

	@JsonProperty("origem_recurso")
	public Integer getOrigemRecurso() {
		return origemRecurso;
	}

	@JsonProperty("origem_recurso")
	public void setOrigemRecurso(Integer origemRecurso) {
		this.origemRecurso = origemRecurso;
	}

	@JsonProperty("codigo_ponto_atendimento")
	public Integer getCodigoPontoAtendimento() {
		return codigoPontoAtendimento;
	}

	@JsonProperty("codigo_ponto_atendimento")
	public void setCodigoPontoAtendimento(Integer codigoPontoAtendimento) {
		this.codigoPontoAtendimento = codigoPontoAtendimento;
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