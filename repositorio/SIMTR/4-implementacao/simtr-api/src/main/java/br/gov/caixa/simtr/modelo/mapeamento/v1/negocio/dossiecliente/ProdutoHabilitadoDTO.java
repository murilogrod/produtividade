package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_PRODUTO_HABILITADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_PRODUTO_HABILITADO,
        description = "Objeto utilizado para representar o Produto Habilitado pelo modelo do Dossiê Digital devido ao atendimento das composições documentais no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class ProdutoHabilitadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CODIGO_OPERACAO, value = "Codigo da operação corporativo do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    private Integer operacao;

    @XmlElement(name = ConstantesNegocioDossieCliente.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CODIGO_MODALIDADE, value = "Codigo da modalidade do do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    private Integer modalidade;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, value = "Nome do produto CAIXA")
    private String nome;

    public ProdutoHabilitadoDTO() {
        super();
    }

    public ProdutoHabilitadoDTO(Produto produto) {
        this();
        this.operacao = produto.getOperacao();
        this.modalidade = produto.getModalidade();
        this.nome = produto.getNome();
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
