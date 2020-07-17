package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.controle.vo.extracaodados.AtributoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesRetaguardaAvaliacaoDocumento.XML_ROOT_ELEMENT_DADO_BASE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaAvaliacaoDocumento.API_MODEL_V1_DADO_BASE,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class DadoBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.CHAVE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.CHAVE, required = true, value = "Nome do atributo do documento", example = "data_base")
    private String chave;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.VALOR)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.VALOR, required = true, value = "Valor do atributo extraído do documento", example = "10/08/2019")
    private String valor;

    public DadoBaseDTO() {
        super();
    }

    public DadoBaseDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }

    public DadoBaseDTO(AtributoDocumento atributoDocumento) {
        this(atributoDocumento.getDescricao(), atributoDocumento.getConteudo());
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

    public AtributoDocumentoVO prototype() {
        return new AtributoDocumentoVO(this.chave, this.valor);
    }
}
