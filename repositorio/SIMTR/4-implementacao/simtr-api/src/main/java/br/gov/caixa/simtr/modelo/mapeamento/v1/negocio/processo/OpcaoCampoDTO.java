package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_OPCAO_CAMPO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_OPCAO_CAMPO,
        description = "Objeto utilizado para representar uma opção de campo de formulario para os casos de elementos objetivos"
)
public class OpcaoCampoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioProcesso.VALOR_OPCAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.VALOR_OPCAO, required = true, value = "Elemento que representa o valor da resposta. Conteúdo a ser utilizado no atributo value do HTML.")
    private String value;

    @XmlElement(name = ConstantesNegocioProcesso.DESCRICAO_OPCAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.DESCRICAO_OPCAO, required = true, value = "Elemento que representa a descricao da resposta. Descrição visual apresentada no HTML.")
    private String nome;

    // *************************************
    public OpcaoCampoDTO() {
        super();
    }

    public OpcaoCampoDTO(OpcaoCampo opcaoCampo) {
        super();
        this.value = opcaoCampo.getValue();
        this.nome = opcaoCampo.getNome();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
