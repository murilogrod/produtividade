package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.apimanager;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesApiManager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesApiManager.XML_ROOT_ELEMENT_CORPO_API_MANAGER)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesApiManager.XML_ROOT_ELEMENT_CORPO_API_MANAGER, description = "Objeto utilizado para representar o corpo utilizado para transportar as informações a serem consumidas no API Manager.")
public class ApiManagerDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesApiManager.SERVICO)
    @ApiModelProperty(name = ConstantesApiManager.SERVICO, required = true, value = "Indica qual Endpoint do Serviço a ser consumido junto ao API Manager.")
    private String servico;
    
    @XmlElement(name = ConstantesApiManager.VERBO)
    @ApiModelProperty(name = ConstantesApiManager.VERBO, required = true, value = "Indica qual Verbo deverá ser utilizado no consumo do serviço junto ao API Manager.")
    private String verbo;
    
    @XmlElement(name = ConstantesApiManager.CORPO)
    @ApiModelProperty(name = ConstantesApiManager.CORPO, required = false, value = "Corpo do Serviço que deve ser passado ao serviço a ser consumido junto ao API Manager.")
    private String corpo;
    
    @XmlElement(name = ConstantesApiManager.CABECALHOS)
    @ApiModelProperty(name = ConstantesApiManager.CABECALHOS, required = false, value = "Cabeçalhos que devem ser passados ao serviço a ser consumido junto ao API Manager")
    private Map<String, String> cabecalhos;
  

    public ApiManagerDTO() {
        super();
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getVerbo() {
        return verbo;
    }

    public void setVerbo(String verbo) {
        this.verbo = verbo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }
    
    public Map<String, String> getCabecalhos() {
        return cabecalhos;
    }

    public void setCabecalhos(Map<String, String> cabecalhos) {
        this.cabecalhos = cabecalhos;
    }

    
}
