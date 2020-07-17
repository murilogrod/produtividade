package br.gov.caixa.simtr.controle.vo.siric;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRespostaConsultaSiric;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaAvaliacaoSiricVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesRespostaConsultaSiric.AVALIACAO_BLOQUEADA_TOMADOR)
    private String avaliacaoBloqueadaTomador;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.IDENTIFICACAO_PESSOA)
    private Long identificacaoPessoa;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.IDENTIFICADOR_TIPO_PESSOA)
    private Integer tipoPessoa;

    @JsonProperty(value = ConstantesRespostaConsultaSiric.CODIGO_PROPOSTA)
    private Integer codigoProposta;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.CODIGO_AVALIACAO)
    private Integer codigoAvaliacao;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.DATA_INICIO_VALIDADE)
    private Date dataInicio;

    @JsonProperty(value = ConstantesRespostaConsultaSiric.PRODUTOS)
    private List<RespostaProdutoAvaliacaoSiricVO> produtos;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.MOTIVOS_REPROVACAO)
    private List<String> motivoReprovacao;
    
    @JsonProperty(value = ConstantesRespostaConsultaSiric.DATA_FIM_VALIDADE)
    private Date dataFim;

	public String getAvaliacaoBloqueadaTomador() {
		return avaliacaoBloqueadaTomador;
	}

	public void setAvaliacaoBloqueadaTomador(String avaliacaoBloqueadaTomador) {
		this.avaliacaoBloqueadaTomador = avaliacaoBloqueadaTomador;
	}

	public Long getIdentificacaoPessoa() {
		return identificacaoPessoa;
	}

	public void setIdentificacaoPessoa(Long identificacaoPessoa) {
		this.identificacaoPessoa = identificacaoPessoa;
	}

	public Integer getCodigoProposta() {
		return codigoProposta;
	}

	public void setCodigoProposta(Integer codigoProposta) {
		this.codigoProposta = codigoProposta;
	}

	public Integer getCodigoAvaliacao() {
		return codigoAvaliacao;
	}

	public void setCodigoAvaliacao(Integer codigoAvaliacao) {
		this.codigoAvaliacao = codigoAvaliacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public List<RespostaProdutoAvaliacaoSiricVO> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<RespostaProdutoAvaliacaoSiricVO> produtos) {
		this.produtos = produtos;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(Integer tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public List<String> getMotivoReprovacao() {
		return motivoReprovacao;
	}

	public void setMotivoReprovacao(List<String> motivoReprovacao) {
		this.motivoReprovacao = motivoReprovacao;
	}

    
    
}
