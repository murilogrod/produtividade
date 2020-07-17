package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.SiricIdentificadorTipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;

/**
 * 
 * @author f525904
 *
 */

@ApiModel(
        value = ConstantesNegocioSiric.API_MODEL_RELACIONAMENTO_PROPOSTA,
        description = "Objeto utilizado para representar relacionadmento da proposta por operação.")
public class RelacionamentoPropostaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PRODUTO)
    private Long codigoProduto;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICADOR_TIPO_PESSOA)
    private SiricIdentificadorTipoPessoaEnum identificadorTipoPessoa;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICACAO_PESSOA)
    private String identificacaoPessoa;

    @JsonProperty(value = ConstantesNegocioSiric.VALOR_RELACIONAMENTO)
    private Double valorRelacionamento;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_PERGUNTA)
    private Long codigoPergunta;

    @JsonProperty(value = ConstantesNegocioSiric.LINHAS_QUADRO_VALOR)
    private List<Double> linhasQuadroValor;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICADOR_TIPO_PESSOA_RECIPROCO)
    private String identificadorTipoPessoaReciproco;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICACAO_PESSOA_RECIPROCO)
    private String identificacaoPessoaReciproco;

    @JsonProperty(value = ConstantesNegocioSiric.DATA_INICIO)
    private String dataInicio;

    @JsonProperty(value = ConstantesNegocioSiric.DATA_FIM)
    private String dataFim;

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public SiricIdentificadorTipoPessoaEnum getIdentificadorTipoPessoa() {
        return identificadorTipoPessoa;
    }

    public void setIdentificadorTipoPessoa(SiricIdentificadorTipoPessoaEnum identificadorTipoPessoa) {
        this.identificadorTipoPessoa = identificadorTipoPessoa;
    }

    public String getIdentificacaoPessoa() {
        return identificacaoPessoa;
    }

    public void setIdentificacaoPessoa(String identificacaoPessoa) {
        this.identificacaoPessoa = identificacaoPessoa;
    }

    public Double getValorRelacionamento() {
        return valorRelacionamento;
    }

    public void setValorRelacionamento(Double valorRelacionamento) {
        this.valorRelacionamento = valorRelacionamento;
    }

    public Long getCodigoPergunta() {
        return codigoPergunta;
    }

    public void setCodigoPergunta(Long codigoPergunta) {
        this.codigoPergunta = codigoPergunta;
    }

    public List<Double> getLinhasQuadroValor() {
        return linhasQuadroValor;
    }

    public void setLinhasQuadroValor(List<Double> linhasQuadroValor) {
        this.linhasQuadroValor = linhasQuadroValor;
    }

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

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
