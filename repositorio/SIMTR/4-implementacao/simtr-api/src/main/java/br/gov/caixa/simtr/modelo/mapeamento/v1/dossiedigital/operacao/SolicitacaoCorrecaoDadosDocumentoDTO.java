package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AbstractBaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_CORRECAO_DADOS_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_CORRECAO_DADOS_DOCUMENTO,
        description = "Objeto utilizado para representar a indicação de correção de dados de um documento peranto o fluxo do Dossiê Digital."
)
public class SolicitacaoCorrecaoDadosDocumentoDTO extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalOperacao.IDENTIFICADOR)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.IDENTIFICADOR, required = true, value = "Código de identificação do documento a ser alterado.")
    private String codigoIdentificadorDocumento;

    @JsonProperty(ConstantesDossieDigitalOperacao.DADOS)
    @XmlElement(name = ConstantesDossieDigitalOperacao.DADO)
    @XmlElementWrapper(name = ConstantesDossieDigitalOperacao.DADOS)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.DADOS, required = true, value = "Lista dos atributos do documento com os valores ajustados.")
    private List<AtributoDocumentoDTO> dadosDocumento;

    public SolicitacaoCorrecaoDadosDocumentoDTO() {
        super();
        this.dadosDocumento = new ArrayList<>();
    }

    public String getCodigoIdentificadorDocumento() {
        return codigoIdentificadorDocumento;
    }

    public void setCodigoIdentificadorDocumento(String codigoIdentificadorDocumento) {
        this.codigoIdentificadorDocumento = codigoIdentificadorDocumento;
    }

    public List<AtributoDocumentoDTO> getDadosDocumento() {
        return dadosDocumento;
    }

    public void setDadosDocumento(List<AtributoDocumentoDTO> dadosDocumento) {
        this.dadosDocumento = dadosDocumento;
    }
}
