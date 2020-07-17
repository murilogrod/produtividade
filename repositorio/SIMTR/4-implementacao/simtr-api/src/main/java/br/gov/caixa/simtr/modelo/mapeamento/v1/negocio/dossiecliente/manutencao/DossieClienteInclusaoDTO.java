package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DossieClienteInclusaoPFDTO.class, name = "F"),
    @JsonSubTypes.Type(value = DossieClienteInclusaoPJDTO.class, name = "J")
})
@ApiModel(
          value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO,
          description = "Objeto utilizado para representar o Dossiê Cliente a ser cadastrado.",
          subTypes =
          {
              DossieClienteInclusaoPFDTO.class,
              DossieClienteInclusaoPJDTO.class
          },
          discriminator = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA)
public abstract class DossieClienteInclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME, required = true, value = "Nome de identificação do cliente para PF ou nome fantasia para clientes PJ")
    protected String nome;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.EMAIL)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.EMAIL, required = false, value = "Email de contato do cliente utilizado nas rotinas de comunicação com o SSO")
    protected String email;

    // *****************************************
    @XmlTransient
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA, required = true, value = "Tipo de pessoa encaminhado", allowableValues = "F,J")
    protected TipoPessoaEnum tipoPessoa;

    public DossieClienteInclusaoDTO() {
        super();
    }

    public DossieClienteInclusaoDTO(DossieCliente dossieCliente) {
        this();
        this.nome = dossieCliente.getNome();

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract DossieCliente prototype();
}
