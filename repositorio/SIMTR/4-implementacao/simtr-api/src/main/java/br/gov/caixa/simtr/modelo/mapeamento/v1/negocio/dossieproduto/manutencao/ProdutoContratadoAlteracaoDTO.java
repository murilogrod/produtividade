package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_PRODUTO_CONTRATADO_ALTERACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_PRODUTO_CONTRATADO_ALTERACAO,
        description = "Objeto utilizado para realizar a inclusão/alteração dos produtos selecionados na contratação de um dossiê de produto inicial limitando as possibilidades de definição dos atributos"
)
public class ProdutoContratadoAlteracaoDTO extends ProdutoContratadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO, required = true, value = "indica se a ação a realizar com o produto é de exclusão. Em caso negativa inclui novo registro se não localizado para o mesmo produto ou altera os dados do existente no vinculo")
    private boolean exclusao;

    public ProdutoContratadoAlteracaoDTO() {
        super();
    }

    public boolean isExclusao() {
        return exclusao;
    }

    public void setExclusao(boolean exclusao) {
        this.exclusao = exclusao;
    }

}
