package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AbstractBaseDTO;
import java.util.ArrayList;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_SOLICITACAO_ATUALIZACAO_CADASTRO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_SOLICITACAO_ATUALIZACAO_CADASTRO,
        description = "Objeto utilizado para representar a solicitação de atualização dos dados cadastrais no SICLI e guarda definitiva dos documentos temporarios."
)
public class SolicitacaoAtualizacaoCadastroDTO extends AbstractBaseDTO implements Serializable {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesDossieDigitalOperacao.DOCUMENTOS)
    @XmlElement(name = ConstantesDossieDigitalOperacao.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesDossieDigitalOperacao.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.DOCUMENTOS, required = true, value = "Lista de identificadores dos documentos que serão atualizados")
    private List<String> documentos;

    public SolicitacaoAtualizacaoCadastroDTO() {
        this.documentos = new ArrayList<>();
    }
    
    public List<String> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<String> documentos) {
        this.documentos = documentos;
    }
}
