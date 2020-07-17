package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOCUMENTO_INCLUSAO,
        description = "Objeto utilizado para representar o documento a ser incluido em um dossiê de cliente"
)
public class DocumentoInclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.TIPO_DOCUMENTO, required = true, value = "Tipo do documento categorizado")
    private Integer tipoDocumento;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.ORIGEM_DOCUMENTO, required = false, value = "Identificador do tipo de midia origem do documento digitalizado, caso não seja informado, será definido o valor como \"Copia Simples\"")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.MIME_TYPE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.MIME_TYPE, required = false, value = "Indica o mime type do conteudo do documento e torna-se obrigatório para documentos com conteudo binário", allowableValues = "image/bmp, image/jpg, image/jpeg, application/pdf, image/png,image/tiff")
    private String mimeType;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieClienteManutencao.ATRIBUTOS)
    @JsonProperty(value = ConstantesNegocioDossieClienteManutencao.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.ATRIBUTOS, required = false, value = "Lista dos atributos extraidos do documento")
    private List<AtributoDocumentoDTO> atributosDocumentoDTO;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.BINARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieClienteManutencao.BINARIO)
    @JsonProperty(value = ConstantesNegocioDossieClienteManutencao.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.BINARIO, required = false, value = "Representa o binario do documento em padrão BASE64")
    private String binario;

    public DocumentoInclusaoDTO() {
        super();
        this.atributosDocumentoDTO = new ArrayList<>();
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
        return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
        this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumentoDTO() {
        return atributosDocumentoDTO;
    }

    public void setAtributosDocumentoDTO(List<AtributoDocumentoDTO> atributosDocumentoDTO) {
        this.atributosDocumentoDTO = atributosDocumentoDTO;
    }

    public String getBinario() {
	return binario;
    }

    public void setBinario(String binario) {
	this.binario = binario;
    }
	
}
