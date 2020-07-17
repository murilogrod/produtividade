package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_SOLICITACAO_EXTRACAO_DADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_SOLICITACAO_EXTRACAO_DADOS,
          description = "Objeto utilizado para representar a solicitação de extração de dados de um documento peranto o fluxo do Dossiê Digital.")
public class SolicitacaoExtracaoDadosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.MIMETYPE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.MIMETYPE, required = true, value = "Formato da imagem.", example = "image/png",
                      allowableValues = "image/jpg, image/jpeg, application/pdf, image/png, image/tiff")
    private String mimetype;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO, required = true, value = "Nome negocial do tipo de documento", example = "0001000100020007")
    private String tipoDocumento;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.BINARIO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.BINARIO, required = true, value = "Binario em formato base64 que representa o documento.")
    private String binario;

    public SolicitacaoExtracaoDadosDTO() {
        super();
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }
}
