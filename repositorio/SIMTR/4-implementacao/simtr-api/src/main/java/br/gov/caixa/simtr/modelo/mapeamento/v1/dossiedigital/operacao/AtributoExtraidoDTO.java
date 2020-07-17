package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_ATRIBUTO_EXTRAIDO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_ATRIBUTO_EXTRAIDO,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class AtributoExtraidoDTO extends AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalOperacao.PERCENTUAL_ALTERACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.PERCENTUAL_ALTERACAO, required = false, value = "Percentual de alteração permitido para o atributo")
    private BigDecimal percentualAlteracaoPermitido;

    public AtributoExtraidoDTO() {
        super();
    }

    public AtributoExtraidoDTO(String chave, String valor) {
        super(chave, valor);
    }

    public AtributoExtraidoDTO(String chave, String valor, BigDecimal percentualAlteracaoPermitido) {
        super(chave, valor);
        this.percentualAlteracaoPermitido = percentualAlteracaoPermitido;
    }

    public AtributoExtraidoDTO(AtributoDocumento atributoDocumento) {
        super(atributoDocumento);
    }

    public BigDecimal getPercentualAlteracaoPermitido() {
        return percentualAlteracaoPermitido;
    }

    public void setPercentualAlteracaoPermitido(BigDecimal percentualAlteracaoPermitido) {
        this.percentualAlteracaoPermitido = percentualAlteracaoPermitido;
    }
}
