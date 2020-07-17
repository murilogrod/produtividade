package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_PROCESSO,
        description = "Objeto utilizado para representar o processo (Originador, fase ou patriarca) de vinculação ao dossiê de produto no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class ProcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, required = true, value = "Codigo de identificação do processo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, required = true, value = "Nome de identificação do processo.")
    private String nome;

    // *********************************************
    public ProcessoDTO() {
        super();
    }

    public ProcessoDTO(Processo processo) {
        this();
        this.id = processo.getId();
        this.nome = processo.getNome();
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
