package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.documento;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDocumento.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDocumento.API_MODEL_V1_TIPO_DOCUMENTO,
          description = "Objeto utilizado para representar o Tipo de Documento no retorno as consultas realizadas a partir do Dossiê do Cliente no contexto do dossiê digital")
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDocumento.ID)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.ID, required = true, value = "Valor que identifica o Tipo de Documento")
    private Integer id;

    @XmlElement(name = ConstantesDossieDigitalDocumento.NOME)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento")
    private String nome;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    public TipoDocumentoDTO() {
        super();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.ativo = tipoDocumento.getAtivo();
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
