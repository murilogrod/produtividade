package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AbstractBaseDTO;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_SOLICITACAO_AUTORIZACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_SOLICITACAO_AUTORIZACAO)
public class SolicitacaoAutorizacaoDTO extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OPERACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OPERACAO, required = true, value = "Número da operação do produto desejado")
    private Integer operacao;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MODALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MODALIDADE, required = true, value = "Número da modalidado do produto desejado")
    private Integer modalidade;

    public SolicitacaoAutorizacaoDTO() {
        super();
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
