package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.controle.vo.extracaodados.AtributoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO_RESULTADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_ATRIBUTO_DOCUMENTO_RESULTADO,
        description = "Objeto utilizado para representar os atributos de um documento como retorno da atividade de extração para atualização."
)
public class AtributoDocumentoResultadoDTO extends AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.INDICE_ASSERTIVIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.INDICE_ASSERTIVIDADE, required = true, value = "Indice de assertividade retornado pelo serviço de extração de dados.")
    private BigDecimal indiceAssertividade;

    public AtributoDocumentoResultadoDTO() {
        super();
    }

    public AtributoDocumentoResultadoDTO(String chave, String valor, BigDecimal indiceAssertividade) {
        super(chave, valor);
        this.indiceAssertividade = indiceAssertividade;
    }

    public AtributoDocumentoResultadoDTO(String chave, String valor, List<String> opcoesSelecionadas, BigDecimal indiceAssertividade) {
        super(chave, valor);
        this.indiceAssertividade = indiceAssertividade;
        this.opcoesSelecionadas = opcoesSelecionadas;
    }

    public AtributoDocumentoResultadoDTO(AtributoDocumento atributoDocumento) {
        this(atributoDocumento.getDescricao(), atributoDocumento.getConteudo(), atributoDocumento.getIndiceAssertividade());
    }

    public BigDecimal getIndiceAssertividade() {
        return indiceAssertividade;
    }

    public void setIndiceAssertividade(BigDecimal indiceAssertividade) {
        this.indiceAssertividade = indiceAssertividade;
    }

    public AtributoDocumentoVO prototype() {
        return new AtributoDocumentoVO(this.chave, this.valor, this.indiceAssertividade);
    }

}
