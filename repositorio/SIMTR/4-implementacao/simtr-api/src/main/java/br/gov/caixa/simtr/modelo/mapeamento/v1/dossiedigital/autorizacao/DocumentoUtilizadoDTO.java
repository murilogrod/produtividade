package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoDocumentoVO;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_DOCUMENTO_UTILIZADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_DOCUMENTO_UTILIZADO)
public class DocumentoUtilizadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.TIPO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.TIPO, required = true, value = "Nome do tipo de documento.")
    private String tipo;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.IDENTIFICADOR)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.IDENTIFICADOR, required = true, value = "Identificador do documento junto ao GED.")
    private String identificador;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.LINK)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.LINK, required = true, value = "Link de acesso ao binario do documento junto ao GED.")
    private String link;

    public DocumentoUtilizadoDTO() {
        super();
    }

    public DocumentoUtilizadoDTO(AutorizacaoDocumentoVO documentoUtilizado) {
        this();
        this.link = documentoUtilizado.getLink();
        this.tipo = documentoUtilizado.getDocumento().getTipoDocumento().getNome();
        this.identificador = documentoUtilizado.getDocumento().getCodigoGED();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
