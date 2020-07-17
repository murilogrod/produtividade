package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_GARANTIA_INFORMADA_ALTERACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_GARANTIA_INFORMADA_ALTERACAO,
        description = "Objeto utilizado para representar a garantia ofertada na contratação de um dossiê de produto sob a ótica do apoio ao negócio."
)
public class GarantiaInformadaAlteracaoDTO extends GarantiaInformadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ID, required = true, value = "Identificador único do registro associativo da garantia informada pera o dossiê de produto")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.EXCLUSAO, required = true, value = "Indica se a ação a realizar com o produto é de exclusão. Em caso negativa inclui novo registro se não localizado para o mesmo produto ou altera os dados do existente no vinculo")
    private boolean exclusao;

    public GarantiaInformadaAlteracaoDTO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isExclusao() {
        return exclusao;
    }

    public void setExclusao(boolean exclusao) {
        this.exclusao = exclusao;
    }

}
