package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.tipo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.VISAO_TIPO_API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a Função Documental no contexto do Tipo de Documento sob a visão da sustentação ao negócio."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTipologia.ID)
    @ApiModelProperty(value = "Identificador da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioTipologia.NOME)
    @ApiModelProperty(value = "Nome negocial da função documental.", required = true)
    private String nome;

    public FuncaoDocumentalDTO() {
        super();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
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

}
