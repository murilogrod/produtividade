package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_OPCAO_DOMINIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.API_MODEL_V1_OPCAO_DOMINIO,
        description = "Objeto utilizado para representar a opções do domínio sob a ótica Apoio ao Negocio."
)
public class OpcaoDominioDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = ConstantesNegocioTipologia.CHAVE)
    @ApiModelProperty(name = ConstantesNegocioTipologia.CHAVE, value = "Identificador a ser utilizado na opção escolhida pelo usuário")
    private String chave;

    @XmlElement(name = ConstantesNegocioTipologia.VALOR)
    @ApiModelProperty(name = ConstantesNegocioTipologia.VALOR, value = "Descrição da opção a ser exibida na opção para o usuário")
    private String valor;

    public OpcaoDominioDTO() {
    	super();
    }
    
    public OpcaoDominioDTO(String value, String label) {
    	 this();
         this.chave = value;
         this.valor = label;
    }
    
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
