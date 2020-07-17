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

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_SOLICITACAO_INCLUSAO_DADOS_DECLARADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_SOLICITACAO_INCLUSAO_DADOS_DECLARADOS,
        description = "Objeto utilizado para representar um documento de dados declarados para atiualização inclusão do mesmo vinculado a um dossiê de cliente."
)
public class SolicitacaoInclusaoDadosDeclaradosDTO extends AbstractBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesDossieDigitalOperacao.DADOS)
    @XmlElement(name = ConstantesDossieDigitalOperacao.DADO)
    @XmlElementWrapper(name = ConstantesDossieDigitalOperacao.DADOS)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.DADOS, required = true, value = "Lista dos atributos do documento com os valores declarados.")
    private List<AtributoDocumentoDTO> dadosDocumento;

    public SolicitacaoInclusaoDadosDeclaradosDTO() {
        super();
        this.dadosDocumento = new ArrayList<>();
    }

    public List<AtributoDocumentoDTO> getDadosDocumento() {
        return dadosDocumento;
    }

    public void setDadosDocumento(List<AtributoDocumentoDTO> dadosDocumento) {
        this.dadosDocumento = dadosDocumento;
    }
}
