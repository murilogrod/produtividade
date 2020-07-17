package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_APONTAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_APONTAMENTO,
        description = "Objeto utilizado para representar apontamento relacionado a um checklist"
)
public class ApontamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do apontamento")
    private Long id;

    @XmlElement(name = ConstantesNegocioProcesso.TITULO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TITULO, required = true, value = "Valor que representa o titulo do apontamento a ser apresentado ao operador")
    private String titulo;

    @XmlElement(name = ConstantesNegocioProcesso.DESCRICAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.DESCRICAO, required = false, value = "Valor que indica um detalhamento sobre o apontamento. Pode ser usado como uma instrução mais detalhada sobre o que realizar naquela verificação")
    private String descricao;

    public ApontamentoDTO() {
        super();
    }

    public ApontamentoDTO(Apontamento apontamento) {
        this();
        if (Objects.nonNull(apontamento)) {
            this.id = apontamento.getId();
            this.titulo = apontamento.getNome();
            this.descricao = apontamento.getDescricao();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
