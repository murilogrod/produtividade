package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.SiricIdentificadorTipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioSiric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
          value = ConstantesNegocioSiric.API_MODEL_QUADRO,
          description = "Objeto utilizado para representar quadro de avaliações.")
public class QuadroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesNegocioSiric.CODIGO_QUADRO_VALOR)
    @ApiModelProperty(name = ConstantesNegocioSiric.CODIGO_QUADRO_VALOR, required = true, value = "Atributo que representa o código quadro valor.")
    private Long codigoQuadroValor;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioSiric.IDENTIFICADOR_TIPO_PESSOA, required = true, value = "Enum utilizado para representar o Tipo de Pessoa.", notes = "CPF = 1 | CNPJ = 2")
    private SiricIdentificadorTipoPessoaEnum identificadorTipoPessoa;

    @JsonProperty(value = ConstantesNegocioSiric.IDENTIFICACAO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioSiric.IDENTIFICACAO_PESSOA, required = true, value = "Valor que representa o identificador Pessoa.")
    private String identificadorPessoa;

    @JsonProperty(value = ConstantesNegocioSiric.LINHAS_QUADRO_VALOR)
    @ApiModelProperty(name = ConstantesNegocioSiric.LINHAS_QUADRO_VALOR, required = true, value = "Linhas quadro valor")
    private List<Double> linhasQuadroValor;

    public Long getCodigoQuadroValor() {
        return codigoQuadroValor;
    }

    public void setCodigoQuadroValor(Long codigoQuadroValor) {
        this.codigoQuadroValor = codigoQuadroValor;
    }

    public SiricIdentificadorTipoPessoaEnum getIdentificadorTipoPessoa() {
        return identificadorTipoPessoa;
    }

    public void setIdentificadorTipoPessoa(SiricIdentificadorTipoPessoaEnum identificadorTipoPessoa) {
        this.identificadorTipoPessoa = identificadorTipoPessoa;
    }

    public String getIdentificadorPessoa() {
        return identificadorPessoa;
    }

    public void setIdentificadorPessoa(String identificadorPessoa) {
        this.identificadorPessoa = identificadorPessoa;
    }

    public List<Double> getLinhasQuadroValor() {
        return linhasQuadroValor;
    }

    public void setLinhasQuadroValor(List<Double> linhasQuadroValor) {
        this.linhasQuadroValor = linhasQuadroValor;
    }
}
