package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_SOLICITACAO_INCLUSAO_CARTAO_ASSINATURA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_SOLICITACAO_INCLUSAO_CARTAO_ASSINATURA,
          description = "Objeto utilizado para representar a solicitação de inclusão de documento do tipo cartão assinatura digitalizado para guarda junto a solução do SIMTR/SIECM.")
public class SolicitacaoInclusaoCartaoAssinaturaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.MIMETYPE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.MIMETYPE, required = true, value = "Formato da imagem.", example = "image/png",
                      allowableValues = "image/jpg, image/jpeg, application/pdf, image/png, image/tiff")
    private String mimetype;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.BINARIO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.BINARIO, required = true, value = "Binario que representa o cartão de assinaturas digitalizado em formato base64")
    private String binario;

    public SolicitacaoInclusaoCartaoAssinaturaDTO() {
        super();
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }
}
