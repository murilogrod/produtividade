package br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_RETORNO_EXTRACAO_DADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V2_RETORNO_EXTRACAO_DADOS,
          description = "Objeto utilizado para representar a resposta a uma solicitação de extração de dados de um documento peranto o fluxo do Dossiê Digital.")
public class RetornoExtracaoDadosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIMTR)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIMTR, required = true, value = "Código de identificação do documento perante a plataforma do SIMTR",
                      example = "185")
    private Long identificadorSimtr;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.MIMETYPE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.MIMETYPE, required = true, value = "Formato da imagem.", example = "image/png",
                      allowableValues = "image/jpg, image/jpeg, application/pdf, image/png, image/tiff")
    private String mimetype;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE, required = true, value = "Nome do Object Store em que o documento deverá ser localizado no SIECM", example = "OS_CAIXA")
    private String objectStore;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM, required = true, value = "Código de identificação do documento junto ao GED",
                      example = "F002D46C-0000-C94F-BD27-766730261721")
    private String identificadorSiecm;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.LINK)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.LINK, required = true, value = "Link em formato URL para visualização do documento", example = "http://siecm.des.caixa/......")
    private String link;


    public RetornoExtracaoDadosDTO() {
        super();
    }

    public Long getIdentificadorSimtr() {
        return identificadorSimtr;
    }

    public void setIdentificadorSimtr(Long identificadorSimtr) {
        this.identificadorSimtr = identificadorSimtr;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(String objectStore) {
        this.objectStore = objectStore;
    }

    public String getIdentificadorSiecm() {
        return identificadorSiecm;
    }

    public void setIdentificadorSiecm(String identificadorSiecm) {
        this.identificadorSiecm = identificadorSiecm;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
