package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_OPCAO_CAMPO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_OPCAO_CAMPO,
        description = "Objeto utilizado para representar a opção de campo de formulario selecionada para os casos de elementos objetivos no retorno as consultas para Dossiê de Produto realizadas sob a ótica Apoio ao Negocio."
)
public class OpcaoCampoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.VALOR_OPCAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.VALOR_OPCAO, required = true, value = "Elemento que representa o valor da resposta. Atributo value do HTML.")
    private String value;

    @XmlElement(name = ConstantesNegocioDossieProduto.DESCRICAO_OPCAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DESCRICAO_OPCAO, required = true, value = "Elemento que representa a descricao da resposta. Descrição visualç apresentada no HTML.")
    private String nome;

    // *************************************
    public OpcaoCampoDTO() {
        super();
    }

    public OpcaoCampoDTO(OpcaoCampo opcaoCampo) {
        super();
        if (opcaoCampo != null) {
            this.value = opcaoCampo.getValue();
            this.nome = opcaoCampo.getNome();
        }
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
