package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO_SIRIC)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
          value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_ATRIBUTO_DOCUMENTO_SIRIC,
          description = "Objeto utilizado para representar um atributo com o valor extraido de um documento ou como dados de ajuste para atualização da informação perante o documento")
public class ProdutoAvalidadorTomadorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.RATING)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.RATING, required = true, value = "Nome do atributo do documento")
    private String rating;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CODIGO_MODALIDADE, required = true, value = "Valor do atributo do documento")
    private String codigoModalidade;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CODIGO_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CODIGO_PRODUTO, required = true, value = "Valor do atributo do documento")
    private String codigoProduto;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.RESULTADO_AVALIACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.RESULTADO_AVALIACAO, required = true, value = "Valor do atributo do documento")
    private String resultadoAvaliacao;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.VALOR_DISPONIVEL)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.VALOR_DISPONIVEL, required = true, value = "Valor do atributo do documento")
    private String valorDisponivel;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.VALOR_CALCULADO_TOTAL)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.VALOR_CALCULADO_TOTAL, required = true, value = "Valor do atributo do documento")
    private String valorCalculadoTotal;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.PRAZO_MESES)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.PRAZO_MESES, required = true, value = "Valor do atributo do documento")
    private String prazoMeses;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCodigoModalidade() {
        return codigoModalidade;
    }

    public void setCodigoModalidade(String codigoModalidade) {
        this.codigoModalidade = codigoModalidade;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getResultadoAvaliacao() {
        return resultadoAvaliacao;
    }

    public void setResultadoAvaliacao(String resultadoAvaliacao) {
        this.resultadoAvaliacao = resultadoAvaliacao;
    }

    public String getValorDisponivel() {
        return valorDisponivel;
    }

    public void setValorDisponivel(String valorDisponivel) {
        this.valorDisponivel = valorDisponivel;
    }

    public String getValorCalculadoTotal() {
        return valorCalculadoTotal;
    }

    public void setValorCalculadoTotal(String valorCalculadoTotal) {
        this.valorCalculadoTotal = valorCalculadoTotal;
    }

    public String getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(String prazoMeses) {
        this.prazoMeses = prazoMeses;
    }
}