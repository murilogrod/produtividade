package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.controle.vo.extracaodados.AtributoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesRetaguardaAvaliacaoDocumento.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaAvaliacaoDocumento.API_MODEL_V1_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.CHAVE)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.CHAVE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.CHAVE, required = true, value = "Nome do atributo do documento")
    private String chave;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.VALOR)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.VALOR)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.VALOR, required = true, value = "Valor do atributo extraído do documento")
    private String valor;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_ASSERTIVIDADE)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.INDICE_ASSERTIVIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_ASSERTIVIDADE, required = true, value = "Indice de assertividade retornado pelo serviço de extração de dados.")
    private BigDecimal indiceAssertividade;

    public AtributoDocumentoDTO() {
        super();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }

    public AtributoDocumentoDTO(String chave, String valor, BigDecimal indiceAssertividade) {
        this(chave, valor);
        this.indiceAssertividade = indiceAssertividade;
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this(atributoDocumento.getDescricao(), atributoDocumento.getConteudo(), atributoDocumento.getIndiceAssertividade());
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
