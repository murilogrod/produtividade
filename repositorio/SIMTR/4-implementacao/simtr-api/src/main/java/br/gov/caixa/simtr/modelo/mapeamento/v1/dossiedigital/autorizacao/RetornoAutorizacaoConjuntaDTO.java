package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_RETORNO_AUTORIZACAO_CONJUNTA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_RETORNO_AUTORIZACAO_CONJUNTA)
public class RetornoAutorizacaoConjuntaDTO {

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.AUTORIZACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.AUTORIZACAO, required = true, value = "Identificador da autorização conjunta quando concedida")
    private Long autorizacao;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.AUTORIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.AUTORIZADO, required = true, value = "Indicativo se a operação foi autorizada ou não")
    private Boolean autorizado;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.PRODUTO_LOCALIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.PRODUTO_LOCALIZADO, required = false, value = "Indicativo se o produto foi localizado")
    private Boolean produtoLocalizado;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OPERACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OPERACAO, required = true, value = "Número da operação do produto solicitado.")
    private Integer operacao;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MODALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MODALIDADE, required = true, value = "Número da Modalidade do produto solicitado.")
    private Integer modalidade;

    @JsonInclude(Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.PRODUTO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.PRODUTO, required = false, value = "Nome do produto solicitado, quando localizado")
    private String nomeProduto;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OBSERVACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OBSERVACAO, required = false, value = "Descrição da mensagem de erro ou observação definida para ser encaminhada ao solicitante")
    private String observacao;


    public RetornoAutorizacaoConjuntaDTO() {
        super();
    }

    public Long getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Long autorizacao) {
        this.autorizacao = autorizacao;
    }

    public Boolean getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Boolean getProdutoLocalizado() {
        return produtoLocalizado;
    }

    public void setProdutoLocalizado(Boolean produtoLocalizado) {
        this.produtoLocalizado = produtoLocalizado;
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}
