package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDashboard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDashboard.XML_ROOT_ELEMENT_PRODUTO_CONTRATADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDashboard.API_MODEL_V1_PRODUTO_CONTRATADO,
        description = "Objeto utilizado para representar o produto contratado e vinculado a um dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio para um dashboard"
)
public class ProdutoContratadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.CODIGO_OPERACAO, required = true, value = "Código da operação corporativo do produto CAIXA")
    private Integer operacao;

    @XmlElement(name = ConstantesNegocioDashboard.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDashboard.CODIGO_MODALIDADE, required = true, value = "Codigo da modalidade do do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    private Integer modalidade;

    @XmlElement(name = ConstantesNegocioDashboard.NOME)
    @ApiModelProperty(name = ConstantesNegocioDashboard.NOME, required = true, value = "Nome do produto CAIXA")
    private String nome;

    public ProdutoContratadoDTO() {
        super();
    }

    public ProdutoContratadoDTO(ProdutoDossie produtoDossie) {
        this();
        if (produtoDossie != null) {
            if (produtoDossie.getProduto() != null) {
                this.operacao = produtoDossie.getProduto().getOperacao();
                this.modalidade = produtoDossie.getProduto().getModalidade();
                this.nome = produtoDossie.getProduto().getNome();
            }
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
}
