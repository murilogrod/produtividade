package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_SOLICITACAO_AUTORIZACAO_CONJUNTA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_SOLICITACAO_AUTORIZACAO_CONJUNTA)
public class SolicitacaoAutorizacaoConjuntaDTO {

    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.INTEGRACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.INTEGRACAO, required = true, value = "Código de integração para identificar o canal solicitante")
    private Long codigoIntegracao;

    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OPERACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OPERACAO, required = true, value = "Número da operação do produto desejado")
    private Integer operacao;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MODALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MODALIDADE, required = true, value = "Número da modalidade do produto desejado")
    private Integer modalidade;

    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.AUTORIZACOES)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.AUTORIZACOES)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.AUTORIZACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.AUTORIZACOES, required = true, value = "Lista de autorizações individuais para vincular como conjunta")
    private List<Long> autorizacoes;

    public SolicitacaoAutorizacaoConjuntaDTO() {
        super();
        this.autorizacoes = new ArrayList<>();
    }

    public Long getCodigoIntegracao() {
        return codigoIntegracao;
    }

    public void setCodigoIntegracao(Long codigoIntegracao) {
        this.codigoIntegracao = codigoIntegracao;
    }

    public List<Long> getAutorizacoes() {
        return autorizacoes;
    }

    public void setAutorizacoes(List<Long> autorizacoes) {
        this.autorizacoes = autorizacoes;
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
}
