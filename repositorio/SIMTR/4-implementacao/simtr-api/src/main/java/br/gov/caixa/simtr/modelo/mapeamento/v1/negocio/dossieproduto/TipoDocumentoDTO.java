package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento no retorno as consultas realizadas ao Dossiê de Produtosob a ótica Apoio ao Negocio."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    public TipoDocumentoDTO() {
        super();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        if (Objects.nonNull(tipoDocumento)) {
            this.id = tipoDocumento.getId();
            this.nome = tipoDocumento.getNome();
            this.ativo = tipoDocumento.getAtivo();
        }
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
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
