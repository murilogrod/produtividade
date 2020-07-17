package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_DOCUMENTO,
        description = "Objeto utilizado para representar um documento no momento de cadastro do dossiê de produto na ótica do Apoio ao Negócio."
)
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.TIPO_DOCUMENTO, required = true, value = "Identificação do tipo de documento encaminhado. Para conhecer a lista de tipologia acesse: GET /negocio/v1/documento/tipologia")
    private Integer codigoTipoDocumento;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ORIGEM_DOCUMENTO, required = true, value = "Identificação da mídia origem utilizada para capturar o documento")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.MIME_TYPE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.MIME_TYPE, required = true, value = "Indica o mime type do conteudo do documento", allowableValues = "image/bmp, image/jpg, image/jpeg, application/pdf, image/png")
    private String mimeType;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.BINARIO, required = false, value = "String em formato Base64 que representa o conteudo do documento")
    private String binario;
    
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.ATRIBUTOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ATRIBUTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ATRIBUTOS, required = false, value = "Lista dos atributos do documento com os valores respectivos conforme definição de tipologia. Para conhecer a lista de tipologia acesse: GET /negocio/v1/documento/tipologia")
    private List<AtributoDocumentoDTO> atributosDocumento;

    public DocumentoDTO() {
        super();
        this.atributosDocumento = new ArrayList<>();
    }

    public Integer getCodigoTipoDocumento() {
        return codigoTipoDocumento;
    }

    public void setCodigoTipoDocumento(Integer codigoTipoDocumento) {
        this.codigoTipoDocumento = codigoTipoDocumento;
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

    public String getBinario() {
	return binario;
    }

    public void setBinario(String binario) {
	this.binario = binario;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
	return atributosDocumento;
    }
    
    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }
}
