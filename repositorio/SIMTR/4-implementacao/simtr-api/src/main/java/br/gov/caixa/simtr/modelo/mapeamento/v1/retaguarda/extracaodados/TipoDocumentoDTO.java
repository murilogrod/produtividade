package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento sob a ótica do serviços de Extração de Dados e Avaliação de Autenticidade."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.ID)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.NOME)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    public TipoDocumentoDTO() {
        super();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        if (tipoDocumento != null) {
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
