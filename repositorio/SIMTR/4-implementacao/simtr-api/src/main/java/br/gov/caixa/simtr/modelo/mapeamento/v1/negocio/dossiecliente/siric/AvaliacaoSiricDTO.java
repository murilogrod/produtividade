package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaSiric;
import io.swagger.annotations.ApiModel;

@ApiModel(value = ConstantesConsultaSiric.API_MODEL_V1_AVALIACAO_SIRIC)
public class AvaliacaoSiricDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaSiric.AVALIACAO_APROVADA)
    private Boolean avaliacaoAprovada;
    
    @JsonProperty(value = ConstantesConsultaSiric.CPF)
    private Long cpf;

    @JsonProperty(value = ConstantesConsultaSiric.CNPJ)
    private Long cnpj;
    
    @JsonProperty(value = ConstantesConsultaSiric.CODIGO_PROPOSTA)
    private Integer codigoProposta;
    
    @JsonProperty(value = ConstantesConsultaSiric.CODIGO_AVALIACAO)
    private Integer codigoAvaliacao;
    
    @JsonProperty(value = ConstantesConsultaSiric.DATA_INICIO)
    private Date dataInicio;
    
    @JsonProperty(value = ConstantesConsultaSiric.DATA_FIM)
    private Date dataFim;

    @JsonProperty(value = ConstantesConsultaSiric.MOTIVOS_REPROVACAO)
    private List<String> motivoReprovacao;
    
    @JsonProperty(value = ConstantesConsultaSiric.PRODUTOS)
    private List<ProdutoAvaliacaoSiricDTO> produtos;

	public Boolean getAvaliacaoAprovada() {
		return avaliacaoAprovada;
	}

	public void setAvaliacaoAprovada(Boolean avaliacaoAprovada) {
		this.avaliacaoAprovada = avaliacaoAprovada;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
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

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<String> getMotivoReprovacao() {
		return motivoReprovacao;
	}

	public void setMotivoReprovacao(List<String> motivoReprovacao) {
		this.motivoReprovacao = motivoReprovacao;
	}

	public List<ProdutoAvaliacaoSiricDTO> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoAvaliacaoSiricDTO> produtos) {
		this.produtos = produtos;
	}
    
    
}
