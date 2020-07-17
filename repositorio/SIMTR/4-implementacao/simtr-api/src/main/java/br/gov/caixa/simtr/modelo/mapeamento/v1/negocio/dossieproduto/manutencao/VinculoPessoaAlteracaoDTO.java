package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_VINCULO_PESSOA_ALTERACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_VINCULO_PESSOA_ALTERACAO,
        description = "Objeto utilizado para representar o vinculo em criação entre o Dossiê de Produto e o Dossiê Cliente sob a ótica do Dossiê de Produto"
)
public class VinculoPessoaAlteracaoDTO extends VinculoPessoaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO, required = true, value = "Indica se a ação a realizar com o produto é de exclusão. Em caso negativa inclui novo registro se não localizado para o mesmo produto ou altera os dados do existente no vinculo")
    private boolean exclusao;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE_RELACIONADO_ANTERIOR)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE_RELACIONADO_ANTERIOR, required = false, value = "Identificador do dossiê de cliente relacionado de que sofreu alteração. Este atributo só deve ser utilizado nos casos de registro de alteração de relacionamento entre dossiês no vinculo definido para alteração. Caso este atributo seja enviado nulo, um novo vinculo será criado com o dossiê de produto")
    private Long idDossieClienteRelacionadoAnterior;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.SEQUENCIA_TITULARIDADE_ANTERIOR)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.SEQUENCIA_TITULARIDADE_ANTERIOR, required = false, value = "Indica a alteração na sequencia de titularidade quando da contratação de produtos com multipla titularidade. Caso este atributo seja enviado nulo, um novo vinculo será criado com o dossiê de produto")
    private Integer sequenciaTitularidadeAnterior;

    public VinculoPessoaAlteracaoDTO() {
        super();
    }

    public boolean isExclusao() {
        return exclusao;
    }

    public void setExclusao(boolean exclusao) {
        this.exclusao = exclusao;
    }

    public Long getIdDossieClienteRelacionadoAnterior() {
        return idDossieClienteRelacionadoAnterior;
    }

    public void setIdDossieClienteRelacionadoAnterior(Long idDossieClienteRelacionadoAnterior) {
        this.idDossieClienteRelacionadoAnterior = idDossieClienteRelacionadoAnterior;
    }

    public Integer getSequenciaTitularidadeAnterior() {
        return sequenciaTitularidadeAnterior;
    }

    public void setSequenciaTitularidadeAnterior(Integer sequenciaTitularidadeAnterior) {
        this.sequenciaTitularidadeAnterior = sequenciaTitularidadeAnterior;
    }

}
