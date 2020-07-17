package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a função documental no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, required = true, value = "Identificador da função documental")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, required = true, value = "Nome negocial da função documental")
    private String nome;

    public FuncaoDocumentalDTO() {
        super();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
