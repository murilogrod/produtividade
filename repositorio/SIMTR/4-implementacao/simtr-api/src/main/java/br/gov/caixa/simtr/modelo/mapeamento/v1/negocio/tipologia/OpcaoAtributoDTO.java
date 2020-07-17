package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_OPCAO_ATRIBUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.API_MODEL_V1_OPCAO_ATRIBUTO,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class OpcaoAtributoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTipologia.CHAVE)
    @ApiModelProperty(name = ConstantesNegocioTipologia.CHAVE, required = true, value = "Identificador a ser utilizado na opção escolhida pelo usuário")
    private String chave;

    @XmlElement(name = ConstantesNegocioTipologia.VALOR)
    @ApiModelProperty(name = ConstantesNegocioTipologia.VALOR, required = true, value = "Descrição da opção a ser exibida na opção para o usuário")
    private String valor;

    public OpcaoAtributoDTO() {
        super();
    }

    public OpcaoAtributoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
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
