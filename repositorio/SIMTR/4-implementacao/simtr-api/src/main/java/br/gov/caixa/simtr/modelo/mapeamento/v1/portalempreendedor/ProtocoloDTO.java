package br.gov.caixa.simtr.modelo.mapeamento.v1.portalempreendedor;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPortalEmpreendedor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesPortalEmpreendedor.XML_ROOT_ELEMENT_PROTOCOLO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPortalEmpreendedor.API_MODEL_V1_PORTALEMPREENDEDOR,
        description = "Objeto utilizado para representar um protocolo recebido do portal do empreendedor."
)
public class ProtocoloDTO implements Serializable{
	private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesPortalEmpreendedor.PROTOCOLO)
    @ApiModelProperty(name = ConstantesPortalEmpreendedor.PROTOCOLO, value = "Protocolo utilizado para consumir o serviço de integração do portal do empreendedor.")
    private String protocolo;
    
    public String getProtocolo() {
  		return protocolo;
  	}

  	public void setProtocolo(String protocolo) {
  		this.protocolo = protocolo;
  	}
}
