package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_DOSSIE_CLIENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo_pessoa")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DossieClientePFDTO.class, name = "F"),
    @JsonSubTypes.Type(value = DossieClientePJDTO.class, name = "J")
})
@ApiModel(
          value = ConstantesNegocioDossieProduto.API_MODEL_V1_DOSSIE_CLIENTE,
          description = "Objeto utilizado para representar o Dossiê Cliente no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê de Produto.",
          subTypes =
          {
              DossieClientePFDTO.class,
              DossieClientePJDTO.class
          },
          discriminator = ConstantesNegocioDossieProduto.TIPO_PESSOA)
public abstract class DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador do dossiê do cliente.")
    protected Long id;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Nome de identificação do cliente.")
    protected String nome;

    @XmlElement(name = ConstantesNegocioDossieProduto.EMAIL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.EMAIL, required = true, value = "Telefone de contato do cliente.")
    protected String email;

    // *****************************************
    @XmlTransient
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.TIPO_PESSOA, required = true, value = "Indicador do tipo de pessoa vinculada ao Dossiê de Cliente.")
    protected TipoPessoaEnum tipoPessoa;

    // *****************************************
    public DossieClienteDTO() {
        super();
    }

    public DossieClienteDTO(DossieCliente dossieCliente) {
        this();
        if (dossieCliente != null) {
            this.id = dossieCliente.getId();
            this.nome = dossieCliente.getNome();
            this.email = dossieCliente.getEmail();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }
}
