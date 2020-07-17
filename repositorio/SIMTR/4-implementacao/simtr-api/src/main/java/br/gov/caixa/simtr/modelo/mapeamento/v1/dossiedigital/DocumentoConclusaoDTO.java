package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_DOCUMENTO_CONCLUSAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_DOCUMENTO_CONCLUSAO,
        description = "Objeto utilizado para representar o documento basico, sem conteudos, submetido para atuação perante a solução do SIMTR/SIECM."
)
public class DocumentoConclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.TIPO_DOCUMENTO, required = true, value = "Identificação do tipo de documento encaminhado. Para conhecer a lista de tipologia acesse: GET /cadastro/v1/tipo-documento/dossie-digital")
    private String tipoDocumento;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DOCUMENTO_UTILIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DOCUMENTO_UTILIZADO, required = true, value = "Arquivo digital que representa o documento em formato Base64 para guarda")
    private String conteudo;

    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.ATRIBUTOS)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.ATRIBUTOS)
    @ApiModelProperty(value = "Lista dos atributos do documento com os valores utilizados na geração do mesmo.")
    private List<AtributoDocumentoDTO> atributosDocumento;

    public DocumentoConclusaoDTO() {
        super();
        this.atributosDocumento = new ArrayList<>();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }
}
