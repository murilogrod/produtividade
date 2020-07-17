package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o tipo de documento relacionado ao vinculo de pessoas na definição do processo."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do tipo de documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Nome descritivo do tipo de documento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioProcesso.PERMITE_REUSO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PERMITE_REUSO, required = true, value = "Valor que indica se o tipo de documento possibilita o reuso.")
    private Boolean indicacaoReuso;
    
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
            this.indicacaoReuso = tipoDocumento.getReuso();
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

    public Boolean getIndicacaoReuso() {
        return indicacaoReuso;
    }

    public void setIndicacaoReuso(Boolean indicacaoReuso) {
        this.indicacaoReuso = indicacaoReuso;
    }
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
