package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.DocumentoConclusaoDTO;
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
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AbstractBaseDTO;
import java.util.ArrayList;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_SOLICITACAO_CONCLUSAO_OPERACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_SOLICITACAO_CONCLUSAO_OPERACAO,
        description = "Objeto utilizado para representar a solicitação de conclusão de uma operação com a guarda de documentos comprobatorios."
)
public class SolicitacaoConclusaoOperacaoDTO extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DOCUMENTO_UTILIZADO)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS, required = true, value = "Lista dos documentos encaminhados para guarda.")
    private List<DocumentoConclusaoDTO> documentos;

    public SolicitacaoConclusaoOperacaoDTO() {
        this.documentos = new ArrayList<>();
    }

    public List<DocumentoConclusaoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoConclusaoDTO> documentos) {
        this.documentos = documentos;
    }
}
