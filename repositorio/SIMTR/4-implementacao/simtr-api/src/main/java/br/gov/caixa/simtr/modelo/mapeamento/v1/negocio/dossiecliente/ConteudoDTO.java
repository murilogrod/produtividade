package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_CONTEUDO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_CONTEUDO,
        description = "Objeto utilizado para representar o conteudo do documento no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class ConteudoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.BINARIO, required = true, value = "Representa o binario do documento em padrão BASE64")
    private String base64;

    // *********************************************
    public ConteudoDTO() {
        super();
    }

    public ConteudoDTO(Conteudo conteudo) {
        this();
        this.base64 = conteudo.getBase64();
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
