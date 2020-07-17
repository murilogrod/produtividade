package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_CANAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_CANAL , 
        description = "Objeto utilizado para representar o Canal de Captura do Documento no contexto do Dossiê de Produto."
)
public class CanalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.SIGLA_CANAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.SIGLA_CANAL, required = true, value = "Sigla de identificação do canal de captura do documento documento.")
    private String siglaCanal;

    @XmlElement(name = ConstantesNegocioDossieProduto.DESCRICAO_CANAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DESCRICAO_CANAL, required = true, value = "Descrição do canal de captura do documento.")
    private String descricaoCanal;

    @XmlElement(name = ConstantesNegocioDossieProduto.CANAL_CAIXA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CANAL_CAIXA, required = true, value = "Indicador do canal fisico da CAIXA vinculado.")
    private CanalCaixaEnum canalCaixaEnum;
    // *********************************************

    public CanalDTO() {
        super();
    }

    public CanalDTO(Canal canal) {
        this();
        if (canal != null) {
            this.siglaCanal = canal.getSigla();
            this.descricaoCanal = canal.getDescricao();
            this.canalCaixaEnum = canal.getCanalCaixa();
        }
    }

    public String getSiglaCanal() {
        return siglaCanal;
    }

    public void setSiglaCanal(String siglaCanal) {
        this.siglaCanal = siglaCanal;
    }

    public String getDescricaoCanal() {
        return descricaoCanal;
    }

    public void setDescricaoCanal(String descricaoCanal) {
        this.descricaoCanal = descricaoCanal;
    }

    public CanalCaixaEnum getCanalCaixaEnum() {
        return canalCaixaEnum;
    }

    public void setCanalCaixaEnum(CanalCaixaEnum canalCaixaEnum) {
        this.canalCaixaEnum = canalCaixaEnum;
    }
}
