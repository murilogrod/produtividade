package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AbstractBaseDTO;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_SOLICITACAO_INCLUSAO_CARTAO_ASSINATURA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_SOLICITACAO_INCLUSAO_CARTAO_ASSINATURA,
        description = "Objeto utilizado para representar a solicitação de inclusão de documento do tipo cartão assinatura digitalizado para guarda junto a solução do SIMTR/SIECM."
)
public class SolicitacaoInclusaoCartaoAssinaturaDTO extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalOperacao.FORMATO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.FORMATO, required = true, value = "Formato da imagem")
    private FormatoConteudoEnum formato;

    @XmlElement(name = ConstantesDossieDigitalOperacao.IMAGEM)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.IMAGEM, required = true, value = "Imagem do cartão assinatura em formato base64")
    private String imagem;

    public SolicitacaoInclusaoCartaoAssinaturaDTO() {
        super();
    }

    public FormatoConteudoEnum getFormato() {
        return formato;
    }

    public void setFormato(FormatoConteudoEnum formato) {
        this.formato = formato;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
