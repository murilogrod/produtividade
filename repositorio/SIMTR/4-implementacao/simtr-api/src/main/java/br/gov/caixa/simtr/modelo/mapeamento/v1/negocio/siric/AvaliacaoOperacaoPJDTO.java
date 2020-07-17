package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.SiricComprovacaoAluguelEnum;
import br.gov.caixa.simtr.modelo.enumerator.SiricIndicadorConvenioEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;

/**
 * 
 * @author f525904
 *
 */

@ApiModel(
        value = ConstantesNegocioSiric.API_MODEL_AVALIACAO_OPERACAO_PJDTO,
        description = "Objeto utilizado para representar solicitação de avaliação da operação.")
public class AvaliacaoOperacaoPJDTO implements Serializable {

    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesNegocioSiric.RELACIONAMENTOS)
    private List<RelacionamentoPropostaDTO> relacionamentos;

    @JsonProperty(value = ConstantesNegocioSiric.QUADROS)
    private List<QuadroDTO> quadros;

    @JsonProperty(value = ConstantesNegocioSiric.CALLBACK_URL)
    private String callbackUrl;

    @JsonProperty(value = ConstantesNegocioSiric.PRESTACAO_NECESSARIA_FINANCIAMENTO)
    private Double prestacaoNecessariaFinanciamento;

    @JsonProperty(value = ConstantesNegocioSiric.INDICADOR_CONVENIO)
    private SiricIndicadorConvenioEnum indicadorConvenio;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_CORRESPONDENTE)
    private Long codigoCorrespondente;

    @JsonProperty(value = ConstantesNegocioSiric.ORIGEM_RECURSO)
    private Integer origemRecurso;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_CONVENIO)
    private Long codigoConvenio;

    @JsonProperty(value = ConstantesNegocioSiric.VALOR_OPERACAO)
    private Double valorOperacao;

    @JsonProperty(value = ConstantesNegocioSiric.VALOR_FINANCIAMENTO)
    private Double valorFinanciamento;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PROPOSTA)
    private Long codigoProposta;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PONTO_ATENDIMENTO)
    private Long codigoPontoAtendimento;

    @JsonProperty(value = ConstantesNegocioSiric.CLASSE_CONSULTA_SICLI)
    private Integer classeConsultaSicli;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_MODALIDADE)
    private Long codigoModalidade;

    @JsonProperty(value = ConstantesNegocioSiric.COMPROVACAO_ALUGUEL)
    private SiricComprovacaoAluguelEnum comprovacaoAluguel;

    @JsonProperty(value = ConstantesNegocioSiric.VALOR_MARGEM_CONSIGNAVEL)
    private Double valorMargemConsignavel;

    @JsonProperty(value = ConstantesNegocioSiric.CNPJ_CONVENENTE)
    private String cnpjConvenente;

    @JsonProperty(value = ConstantesNegocioSiric.SISTEMA_ARMOTIZACAO)
    private Integer sistemaArmotizacao;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PRODUTO)
    private Long codigoProduto;

    @JsonProperty(value = ConstantesNegocioSiric.INDICADOR_TOMADOR)
    private Integer indicadorTomador;

    @JsonProperty(value = ConstantesNegocioSiric.TIPO_GARANTIA) 
    private Long tipoGarantia;

    @JsonProperty(value = ConstantesNegocioSiric.ANO_VEICULO)
    private Integer anoVeiculo;

    @JsonProperty(value = ConstantesNegocioSiric.TAXA_JUROS)
    private Double taxaJuros;

    @JsonProperty(value = ConstantesNegocioSiric.PRAZO_MESES)
    private Integer prazoMeses;

    public List<RelacionamentoPropostaDTO> getRelacionamentos() {
        return relacionamentos;
    }

    public void setRelacionamentos(List<RelacionamentoPropostaDTO> relacionamentos) {
        this.relacionamentos = relacionamentos;
    }

    public List<QuadroDTO> getQuadros() {
        return quadros;
    }

    public void setQuadros(List<QuadroDTO> quadros) {
        this.quadros = quadros;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Double getPrestacaoNecessariaFinanciamento() {
        return prestacaoNecessariaFinanciamento;
    }

    public void setPrestacaoNecessariaFinanciamento(Double prestacaoNecessariaFinanciamento) {
        this.prestacaoNecessariaFinanciamento = prestacaoNecessariaFinanciamento;
    }

    public SiricIndicadorConvenioEnum getIndicadorConvenio() {
        return indicadorConvenio;
    }

    public void setIndicadorConvenio(SiricIndicadorConvenioEnum indicadorConvenio) {
        this.indicadorConvenio = indicadorConvenio;
    }

    public Long getCodigoCorrespondente() {
        return codigoCorrespondente;
    }

    public void setCodigoCorrespondente(Long codigoCorrespondente) {
        this.codigoCorrespondente = codigoCorrespondente;
    }

    public Integer getOrigemRecurso() {
        return origemRecurso;
    }

    public void setOrigemRecurso(Integer origemRecurso) {
        this.origemRecurso = origemRecurso;
    }

    public Long getCodigoConvenio() {
        return codigoConvenio;
    }

    public void setCodigoConvenio(Long codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }

    public Double getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(Double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public Double getValorFinanciamento() {
        return valorFinanciamento;
    }

    public void setValorFinanciamento(Double valorFinanciamento) {
        this.valorFinanciamento = valorFinanciamento;
    }

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

    public Integer getClasseConsultaSicli() {
        return classeConsultaSicli;
    }

    public void setClasseConsultaSicli(Integer classeConsultaSicli) {
        this.classeConsultaSicli = classeConsultaSicli;
    }

    public Long getCodigoModalidade() {
        return codigoModalidade;
    }

    public void setCodigoModalidade(Long codigoModalidade) {
        this.codigoModalidade = codigoModalidade;
    }

    public SiricComprovacaoAluguelEnum getComprovacaoAluguel() {
        return comprovacaoAluguel;
    }

    public void setComprovacaoAluguel(SiricComprovacaoAluguelEnum comprovacaoAluguel) {
        this.comprovacaoAluguel = comprovacaoAluguel;
    }

    public Double getValorMargemConsignavel() {
        return valorMargemConsignavel;
    }

    public void setValorMargemConsignavel(Double valorMargemConsignavel) {
        this.valorMargemConsignavel = valorMargemConsignavel;
    }

    public String getCnpjConvenente() {
        return cnpjConvenente;
    }

    public void setCnpjConvenente(String cnpjConvenente) {
        this.cnpjConvenente = cnpjConvenente;
    }

    public Integer getSistemaArmotizacao() {
        return sistemaArmotizacao;
    }

    public void setSistemaArmotizacao(Integer sistemaArmotizacao) {
        this.sistemaArmotizacao = sistemaArmotizacao;
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

    public Long getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(Long tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    public Integer getAnoVeiculo() {
        return anoVeiculo;
    }

    public void setAnoVeiculo(Integer anoVeiculo) {
        this.anoVeiculo = anoVeiculo;
    }

    public Double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(Double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }
}
