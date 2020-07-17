package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_TIPO_RELACIONAMENTO,
        description = "Objeto utilizado para representar o Tipo de Relacionamento de um dossiê de cliente no contexto da validação de um dossiê de produto"
)
public class TipoRelacionamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ID, required = true, value = "Valor que identifica o Tipo de Relacionamento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Relacionamento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ASSOCIADO_PROCESSO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ASSOCIADO_PROCESSO, required = true, value = "Valor que indica se o tipo de relacionamento esta associado ao processo ou não.")
    private Boolean associadoProcesso;

    public TipoRelacionamentoDTO(Integer id) {
        super();
        this.id = id;
        this.associadoProcesso = Boolean.FALSE;
    }

    public TipoRelacionamentoDTO(TipoRelacionamento tipoRelacionamento, boolean associadoProcesso) {
        super();
        this.id = tipoRelacionamento.getId();
        this.nome = tipoRelacionamento.getNome();
        this.associadoProcesso = associadoProcesso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAssociadoProcesso() {
        return associadoProcesso;
    }

    public void setAssociadoProcesso(Boolean associadoProcesso) {
        this.associadoProcesso = associadoProcesso;
    }

}
