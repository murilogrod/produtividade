package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_PRODUTO_CONTRATADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_PRODUTO_CONTRATADO,
        description = "Objeto utilizado para representar o produto contratado e vinculado a um dossiê de produto no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class ProdutoContratadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CODIGO_OPERACAO, required = true, value = "Codigo da operação corporativo do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    private Integer operacao;

    @XmlElement(name = ConstantesNegocioDossieCliente.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CODIGO_MODALIDADE, required = true, value = "Codigo da modalidade do do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    private Integer modalidade;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, required = true, value = "Nome do produto CAIXA")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieCliente.VALOR_CONTRATO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.VALOR_CONTRATO, required = false, value = "Valor do contrato vinculado a contratação do produto")
    private BigDecimal valor;

    public ProdutoContratadoDTO() {
        super();
    }

    public ProdutoContratadoDTO(ProdutoDossie produtoDossie) {
        this();
        if (produtoDossie.getProduto() != null) {
            this.operacao = produtoDossie.getProduto().getOperacao();
            this.modalidade = produtoDossie.getProduto().getModalidade();
            this.nome = produtoDossie.getProduto().getNome();
        }
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
