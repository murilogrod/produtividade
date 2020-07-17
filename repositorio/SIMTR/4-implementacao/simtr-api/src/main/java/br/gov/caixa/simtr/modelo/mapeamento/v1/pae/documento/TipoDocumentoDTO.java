package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAETipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesPAETipoDocumento.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAETipoDocumento.API_MODEL_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento sob a otica do PAE."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TipoDocumentoDTO.class.getName());

    @XmlElement(name = ConstantesPAETipoDocumento.ID)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesPAETipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;       

    @XmlElement(name = ConstantesPAETipoDocumento.TAGS)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.TAGS, required = true, value = "Valor que reprensenta as tags definidas para o Tipo de Documento.")
    private String tags;

    // ***********************************
    @XmlElementWrapper(name = ConstantesPAETipoDocumento.ATRIBUTOS_EXTRACAO)
    @XmlElement(name = ConstantesPAETipoDocumento.ATRIBUTO_EXTRACAO)
    @JsonProperty( value = ConstantesPAETipoDocumento.ATRIBUTOS_EXTRACAO)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.ATRIBUTOS_EXTRACAO, required = true, value = "Lista de atributos esperados pelo tipo de documento.")
    private List<AtributoExtracaoDTO> atributosExtracaoDTO;

    public TipoDocumentoDTO() {
        super();
        this.atributosExtracaoDTO = new ArrayList<>();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        if (tipoDocumento != null) {
            this.id = tipoDocumento.getId();
            this.nome = tipoDocumento.getNome();
            this.ativo = tipoDocumento.getAtivo();
            this.tags = tipoDocumento.getTags();
            try {
                tipoDocumento.getAtributosExtracao()
                        .forEach(ae -> this.atributosExtracaoDTO.add(new AtributoExtracaoDTO(ae)));
            } catch (RuntimeException re) {
                //LazyException ou Atributo não carregado
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.atributosExtracaoDTO = null;
            }
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<AtributoExtracaoDTO> getAtributosExtracaoDTO() {
        return atributosExtracaoDTO;
    }

    public void setAtributosExtracaoDTO(List<AtributoExtracaoDTO> atributosExtracaoDTO) {
        this.atributosExtracaoDTO = atributosExtracaoDTO;
    }
}
