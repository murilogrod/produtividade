package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_CHECKLIST)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_CHECKLIST,
        description = "Objeto utilizado para representar o checklist vinculado a uma verificação de um dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio."
)
public class ChecklistDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_CHECKLIST, required = true, value = "Identificador do checklist relacionado com a verificação.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Nome utilizado para fins de identificação do checklist a apresentação do mesmo como label.")
    private String nome;

    public ChecklistDTO() {
        super();
    }

    public ChecklistDTO(Checklist checklist) {
        this();
        this.id = checklist.getId();
        this.nome = checklist.getNome();
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
