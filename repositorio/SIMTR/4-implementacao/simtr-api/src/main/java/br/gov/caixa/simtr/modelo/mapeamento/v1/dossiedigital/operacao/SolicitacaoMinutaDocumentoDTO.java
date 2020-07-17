package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_SOLICITACAO_MINUTA_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_SOLICITACAO_MINUTA_DOCUMENTO,
        description = "Objeto utilizado para representar a solicitação de emissão de minuta de um documento."
)
public class SolicitacaoMinutaDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @XmlElement(name = ConstantesDossieDigitalOperacao.INTEGRACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.INTEGRACAO, required = true, value = "Código de integração para identificar o canal solicitante")
    private Long codigoIntegracao;

    
    @XmlElement(name = ConstantesDossieDigitalOperacao.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.TIPO_DOCUMENTO, required = true, value = "Identificação do tipo de documento encaminhado. Para conhecer a lista de tipologia acesse: GET /cadastro/v1/tipo-documento/dossie-digital")
    private String tipoDocumento;

    @XmlElement(name = ConstantesDossieDigitalOperacao.FORMATO_SAIDA, required = true)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.FORMATO_SAIDA, value = "Identificação do formato de saída da minuta de documento emitida (PDF, BASE64).", required = true, allowableValues = "PDF, BASE64")
    private FormatoConteudoEnum formatoConteudoEnum;

    @XmlElement(name = "atributo", required = true)
    @XmlElementWrapper(name = "atributos")
    @JsonProperty("atributos")
    @ApiModelProperty(value = "Lista dos atributos do documento com os valores declarados.")
    private List<AtributoDocumentoDTO> atributosDocumento;

    public SolicitacaoMinutaDocumentoDTO() {
        this.atributosDocumento = new ArrayList<>();
    }
    
    public Long getCodigoIntegracao() {
        return codigoIntegracao;
    }

    public void setCodigoIntegracao(Long codigoIntegracao) {
        this.codigoIntegracao = codigoIntegracao;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public FormatoConteudoEnum getFormatoConteudoEnum() {
        return formatoConteudoEnum;
    }

    public void setFormatoConteudoEnum(FormatoConteudoEnum formatoConteudoEnum) {
        this.formatoConteudoEnum = formatoConteudoEnum;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }
}
